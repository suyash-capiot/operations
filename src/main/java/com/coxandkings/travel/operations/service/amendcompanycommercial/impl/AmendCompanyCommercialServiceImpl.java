package com.coxandkings.travel.operations.service.amendcompanycommercial.impl;

import com.coxandkings.travel.ext.model.finance.creditdebitnote.CostHeaderCharge;
import com.coxandkings.travel.ext.model.finance.creditdebitnote.CreditDebitNote;
import com.coxandkings.travel.ext.model.finance.invoice.CommercialEntity;
import com.coxandkings.travel.ext.model.finance.invoice.Invoice;
import com.coxandkings.travel.ext.model.finance.invoice.InvoiceParticularsDto;
import com.coxandkings.travel.operations.enums.amendclientcommercials.ApprovalStatus;
import com.coxandkings.travel.operations.enums.amendclientcommercials.BEInboundOperation;
import com.coxandkings.travel.operations.enums.amendclientcommercials.BEOperationId;
import com.coxandkings.travel.operations.enums.amendclientcommercials.BEServiceUri;
import com.coxandkings.travel.operations.enums.debitNote.FundType;
import com.coxandkings.travel.operations.enums.debitNote.NotePhase;
import com.coxandkings.travel.operations.enums.debitNote.NoteType;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.enums.refund.ReasonForRequest;
import com.coxandkings.travel.operations.enums.refund.RefundStatus;
import com.coxandkings.travel.operations.enums.refund.RefundTypes;
import com.coxandkings.travel.operations.enums.todo.*;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.commercials.ApplyCommercialOn;
import com.coxandkings.travel.operations.model.commercials.BookingIneligibleFor;
import com.coxandkings.travel.operations.model.commercials.SellingPriceComponent;
import com.coxandkings.travel.operations.model.companycommercial.AmendCompanyCommercial;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.model.productbookedthrother.mdm.B2BClient;
import com.coxandkings.travel.operations.model.productbookedthrother.mdm.B2CClient;
import com.coxandkings.travel.operations.model.refund.finaceRefund.ProductDetail;
import com.coxandkings.travel.operations.model.todo.ToDoTask;
import com.coxandkings.travel.operations.producer.service.WorkUnitDispatcher;
import com.coxandkings.travel.operations.repository.amendclientcommercial.AmendClientCommercialRepository;
import com.coxandkings.travel.operations.repository.amendcompanycommercial.AmendCompanyCommercialRepository;
import com.coxandkings.travel.operations.repository.commercials.ApplyCommercialOnRepository;
import com.coxandkings.travel.operations.repository.commercials.BookingNotEligibleForRepository;
import com.coxandkings.travel.operations.repository.commercials.SellingPriceComponentRepository;
import com.coxandkings.travel.operations.resource.amendentitycommercial.*;
import com.coxandkings.travel.operations.resource.refund.RefundResource;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResource;
import com.coxandkings.travel.operations.service.amendcompanycommercial.AmendCompanyCommercialService;
import com.coxandkings.travel.operations.service.amendcompanycommercial.MDMCommercials;
import com.coxandkings.travel.operations.service.beconsumer.BeToOpsBookingConverter;
import com.coxandkings.travel.operations.service.bedbservice.BeDBUpdateService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.productbookedthrother.mdm.MdmClientService;
import com.coxandkings.travel.operations.service.refund.RefundService;
import com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.ServiceOrderAndSupplierLiabilityService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.*;
import com.coxandkings.travel.operations.utils.xml.XMLTransformer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Element;

import javax.transaction.Transactional;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class AmendCompanyCommercialServiceImpl implements AmendCompanyCommercialService {


    private static final Logger logger = LogManager.getLogger(AmendCompanyCommercialServiceImpl.class);
    @Autowired
    private AmendCompanyCommercialRepository amendCompanyCommercialrepository;
	
	@Autowired
    private ApplyCommercialOnRepository applyCommercialOnRepository;

    @Autowired
    private BookingNotEligibleForRepository bookingNotEligibleForRepository;

    @Autowired
    private SellingPriceComponentRepository sellingPriceComponentRepository;

    @Autowired
    private ToDoTaskService toDoTaskService;
    
    @Autowired
    private RefundService refundService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
	private OpsBookingService opsBookingService;
    @Autowired
    private AmendClientCommercialRepository amendClientCommercialRepository;
    
    @Value(value = "${booking_engine.get.booking}")
    private String getBookingUrl;

    @Value("${amend-commercials.get.invoice-by-booking-and-order}")
    private String getInvoiceUrl;

    @Value("${amend-commercials.update.invoice}")
    private String updateInvoiceUrl;
    @Autowired
    private BeDBUpdateService bookingEngineDbUpdate;
    
    @Autowired
    private MarginCalculatorUtil marginCalculator;
    
    @Autowired
	private ServiceOrderAndSupplierLiabilityService serviceOrderAndSupplierLiabilityService;
    
    @Autowired
    private MdmClientService mdmClientService;

    @Value("${booking-engine-core-services.acco.opsreprice}")
    private String accoBookingEngineOpsRepriceUrl;

    @Value("${booking-engine-core-services.air.opsreprice}")
    private String airBookingEngineOpsRepriceUrl;
    @Value("${booking-engine-core-services.acco.hoteldata}")
    private String redisHotelDataUrl;
    
    @Value("${amend-commercials.create.debit_note}")
    private String createDebitNoteUrl;
    
    @Value("${kafka.producer.client-commercials.topic}")
    private String commercialsTopic;
        
    @Autowired
    private BookingEngineElasticData bookingEngineElasticData;

    @Autowired
    private MDMCommercials mdmCommercials;

    @Autowired
    private BeToOpsBookingConverter beToOpsBookingConverter;
    
    @Autowired
    private WorkUnitDispatcher clientCommercialProducer;
    @Autowired
    private ObjectMapper objectMapper;
    
    /*@Autowired
    private MDMRestUtils mdmRestUtils;
    */
    @Autowired
    RestUtils restUtils;
    
    @Value(value = "${ROE.booking-date}")
    private String getROEUrl;
    
    @Override
    public void update(AmendCommercialResource amendCompanyCommercialResource)
            throws OperationException {
    	
    	OpsBooking booking = null;
        
        String bookingId = amendCompanyCommercialResource.getBookingId();
        String orderId = amendCompanyCommercialResource.getOrderId();

        booking = opsBookingService.getBooking(bookingId);

        if (booking == null) {
            throw new OperationException(Constants.BOOKING_NOT_FOUND, bookingId);
        }

        Optional<OpsProduct> order = booking.getProducts().parallelStream().filter(orders -> orders.getOrderID().equals(orderId)).findAny();
        if (!order.isPresent()) {
            throw new OperationException(Constants.ORDER_NOT_FOUND, orderId, bookingId);
        }
        
        if(amendCompanyCommercialResource.getAmountApplicable()) {
        updateCommercialFromROE(amendCompanyCommercialResource,booking);
        }

        
        List<String> amendedCommercials = amendCompanyCommercialrepository.getAmendedCommercialHeads(amendCompanyCommercialResource.getBookingId());
        List<String> approvedCommercials = amendCompanyCommercialrepository.getApprovedCommercialHeads(amendCompanyCommercialResource.getBookingId());
        approvedCommercials.addAll(amendClientCommercialRepository.getApprovedCommercialHeads(amendCompanyCommercialResource.getBookingId()));
        amendedCommercials.addAll(amendClientCommercialRepository.getAmendedCommercialHeads(amendCompanyCommercialResource.getBookingId()));
        JSONObject mdmCommercial=null;
        try {
        	mdmCommercial = mdmCommercials.updateMDMEntityCommercial(amendCompanyCommercialResource, approvedCommercials, amendedCommercials);
        }
        catch(Exception e) {
        	logger.error("MDM Rule Update Failed",e);
        	throw new OperationException(Constants.MDM_COMMERCIAL_UPDATE_FAILED);
        }
        if (mdmCommercial == null) {
            throw new OperationException(Constants.MDM_COMMERCIAL_UPDATE_FAILED);
        }
    	clientCommercialProducer.dispatch(commercialsTopic,mdmCommercial.toString());
        logger.info("Amend Company Commercial Kafka Msg :" + mdmCommercial.toString());
    }
    
    
    private void updateCommercialFromROE(AmendCommercialResource amendCompanyCommercialResource,
    		OpsBooking booking) throws OperationException {
    	String amendmentCurrency=amendCompanyCommercialResource.getCurrency();
    	String bookingDate=DateTimeFormatter.ofPattern("yyyy-MM-dd").format(booking.getBookingDateZDT());
		String commercialCurrency=amendCompanyCommercialResource.getOriginalCommercials().getOrderClientCommercials().stream().
				filter(commercial->commercial.getCommercialName().equals(amendCompanyCommercialResource.getCommercialHead())).findAny().get().getCommercialCurrency();
		if(!amendmentCurrency.equals(commercialCurrency)) {
			BigDecimal commercialToClientROE=null;
			try {      
            	UriComponents getROE = UriComponentsBuilder.fromUriString(getROEUrl).pathSegment(commercialCurrency).
            			pathSegment(amendmentCurrency).pathSegment(booking.getClientMarket()).pathSegment(bookingDate).build();
            	commercialToClientROE=RestUtils.getForObject(getROE.toUriString(), BigDecimal.class);
            	
            	}
            	catch(Exception e) {
            		throw new OperationException("Cannot Get ROE for Commercial");
            	}
			amendCompanyCommercialResource.setAmount(amendCompanyCommercialResource.getAmount().divide(commercialToClientROE,new MathContext(12,RoundingMode.HALF_UP)));
			amendCompanyCommercialResource.setCurrency(commercialCurrency);
		}
		
	}


	@Override
    public AmendCommercialResource apply(AmendCommercialResource companyCommercialResource) throws OperationException {
        OpsBooking booking = null;
        String userId = null;
        String transactionId = null;
        String sessionId = null;
        String bookingId = companyCommercialResource.getBookingId();
        String orderId = companyCommercialResource.getOrderId();

        booking = opsBookingService.getBooking(bookingId);

        if (booking == null) {
            throw new OperationException(Constants.BOOKING_NOT_FOUND, bookingId);
        }

        userId = booking.getUserID();
        transactionId = booking.getTransactionID();
        sessionId = booking.getSessionID();

        Optional<OpsProduct> order = booking.getProducts().parallelStream().filter(orders -> orders.getOrderID().equals(orderId)).findAny();
        if (!order.isPresent()) {
            throw new OperationException(Constants.ORDER_NOT_FOUND, orderId, bookingId);
        }

        OpsProductSubCategory opsProdSubCateogry = order.get().getOpsProductSubCategory();
        CommercialResource commercialResource = null;
        JSONObject beRepriceRq = null;
        Element siRepriceResponse = null;
        JSONObject opsAmendmentsJson = null;
        JSONObject beResponseJson = null;
        OpsBooking updatedOpsBooking = null;
        OpsProduct updatedOpsOrder = null;
        JSONObject suppCommRs=null;

        switch (opsProdSubCateogry) {
            case PRODUCT_SUB_CATEGORY_BUS:
                break;
            case PRODUCT_SUB_CATEGORY_CAR:
                break;
            case PRODUCT_SUB_CATEGORY_FLIGHT:
                beRepriceRq = bookingEngineElasticData.getJSONData(userId, transactionId, sessionId, BEInboundOperation.REPRICE, BEOperationId.BOOKING_ENGINE_RQ, BEServiceUri.FLIGHT);
                beRepriceRq.getJSONObject("requestHeader").put("userID", userService.getLoggedInUserId());
                JSONObject beRepriceRs = bookingEngineElasticData.getJSONData(userId, transactionId, sessionId, BEInboundOperation.REPRICE, BEOperationId.BOOKING_ENGINE_RS, BEServiceUri.FLIGHT);
                logger.info("Booking Engine Air Reprice Request : " + beRepriceRq.toString());
                siRepriceResponse = bookingEngineElasticData.getXMLData(userId, transactionId, sessionId, BEInboundOperation.REPRICE, BEOperationId.SUPPLIER_INTEGRATION_RS, BEServiceUri.FLIGHT);
                logger.info("SI Air Reprice Response : " + XMLTransformer.toString(siRepriceResponse));
                suppCommRs=bookingEngineElasticData.getJSONData(userId, transactionId, sessionId, BEInboundOperation.REPRICE, BEOperationId.SUPPLIER_COMMERCIAL_RS, BEServiceUri.FLIGHT);
                logger.info("Supplier Commercials Air Reprice Response : " + suppCommRs.toString());
                int ccommJrnyDtlsJsonIdx = getJourneyDetailsIndex(order.get(), beRepriceRs);
                opsAmendmentsJson = getAirOpsAmendmentsJson(booking.getBookID(), order.get(), companyCommercialResource, siRepriceResponse,suppCommRs, ccommJrnyDtlsJsonIdx);
                opsAmendmentsJson.put("bookingDate", DateTimeFormatter.ofPattern("yyyy-MM-dd'T00:00:00'").format(booking.getBookingDateZDT()));
                beRepriceRq.getJSONObject("requestBody").put("opsAmendments", opsAmendmentsJson);
                beResponseJson = consumeBEOpsReprice(beRepriceRq, opsProdSubCateogry);
                updatedOpsBooking = beToOpsBookingConverter.updateAirOrder(beRepriceRq, beResponseJson, bookingId, orderId, companyCommercialResource.getPaxType());
                updatedOpsOrder = updatedOpsBooking.getProducts().parallelStream().filter(orders -> orders.getOrderID().equals(companyCommercialResource.getOrderId())).findAny().get();

                try {
                    logger.info("Updated Air Order : " + objectMapper.writeValueAsString(updatedOpsOrder));
                } catch (JsonProcessingException e2) {
                    e2.printStackTrace();
                }

                commercialResource = getCommercialResourceForOrder(updatedOpsOrder.getOrderDetails(), opsProdSubCateogry, companyCommercialResource.getPaxType());
                break;
            case PRODUCT_SUB_CATEGORY_HOTELS:

                beRepriceRq = bookingEngineElasticData.getJSONData(userId, transactionId, sessionId, BEInboundOperation.REPRICE, BEOperationId.BOOKING_ENGINE_RQ, BEServiceUri.HOTEL);
                beRepriceRq.getJSONObject("requestHeader").put("userID", userService.getLoggedInUserId());
                logger.info("Booking Engine Acco Reprice Request : " + beRepriceRq.toString());
                siRepriceResponse = bookingEngineElasticData.getXMLData(userId, transactionId, sessionId, BEInboundOperation.REPRICE, BEOperationId.SUPPLIER_INTEGRATION_RS, BEServiceUri.HOTEL);
                logger.info("SI Acco Reprice Response : " + XMLTransformer.toString(siRepriceResponse));
                suppCommRs=bookingEngineElasticData.getJSONData(userId, transactionId, sessionId, BEInboundOperation.REPRICE, BEOperationId.SUPPLIER_COMMERCIAL_RS, BEServiceUri.HOTEL);
                logger.info("Supplier Commercials Acco Reprice Response : " + suppCommRs.toString());
                opsAmendmentsJson = getAccoOpsAmendmentsJson(booking.getBookID(), order.get(), companyCommercialResource, siRepriceResponse,suppCommRs);
                opsAmendmentsJson.put("bookingDate", DateTimeFormatter.ofPattern("yyyy-MM-dd'T00:00:00'").format(booking.getBookingDateZDT()));
                beRepriceRq.getJSONObject("requestBody").put("opsAmendments", opsAmendmentsJson);
                beResponseJson = consumeBEOpsReprice(beRepriceRq, opsProdSubCateogry);
                updatedOpsBooking = beToOpsBookingConverter.updateAccoOrder(beRepriceRq, beResponseJson, bookingId, orderId, companyCommercialResource.getRoomId());
                updatedOpsOrder = updatedOpsBooking.getProducts().parallelStream().filter(orders -> orders.getOrderID().equals(companyCommercialResource.getOrderId())).findAny().get();
                try {
                    logger.info("Updated Acco Order: " + objectMapper.writeValueAsString(updatedOpsOrder));
                } catch (JsonProcessingException e1) {
                    e1.printStackTrace();
                }

                OpsRoom opsRoom = updatedOpsOrder.getOrderDetails().getHotelDetails().getRooms().parallelStream().filter(room -> room.getRoomID().equals(companyCommercialResource.getRoomId())).findAny().get();
                //removing all other rooms for db udapte
                updatedOpsOrder.getOrderDetails().getHotelDetails().getRooms().clear();
                updatedOpsOrder.getOrderDetails().getHotelDetails().getRooms().add(opsRoom);
                commercialResource = getCommercialResourceForOrder(updatedOpsOrder.getOrderDetails(), opsProdSubCateogry, companyCommercialResource.getRoomId());
                break;
            case PRODUCT_SUB_CATEGORY_INDIAN_RAIL:
                break;
            case PRODUCT_SUB_CATEGORY_RAIL:
                break;
            default:
                break;

        }

		commercialResource.setMarginDetails(marginCalculator.calculateMargin(updatedOpsBooking,updatedOpsOrder));
        companyCommercialResource.setRecalculatedCommercials(commercialResource);
        companyCommercialResource.setUpdatedOpsOrder(updatedOpsOrder);
        if(companyCommercialResource.getOriginalCommercials().getMarginDetails().getNetMargin().compareTo(companyCommercialResource.getRecalculatedCommercials().getMarginDetails().getNetMargin())<0) {
        	companyCommercialResource.setMarginIncreased(true);
        }
		return companyCommercialResource;
    }
    
	
	@Override
    @Transactional(rollbackOn = OperationException.class)
    public void save(AmendCommercialResource companyCommercialResource) throws OperationException {
		AmendCompanyCommercial amendCompanyCommercial = new AmendCompanyCommercial();
        CopyUtils.copy(companyCommercialResource, amendCompanyCommercial);
        amendCompanyCommercial.setCreatedByUserId(userService.getLoggedInUserId());
        amendCompanyCommercial.setCreatedTime(System.currentTimeMillis());
        validateCommercial(amendCompanyCommercial);
        amendCompanyCommercial.setApprovalStatus(ApprovalStatus.PENDING);
        amendCompanyCommercial = amendCompanyCommercialrepository.saveCommercial(amendCompanyCommercial);
        createToDoTask(amendCompanyCommercial);
		
	}
	
	
	@Override
	public AmendCompanyCommercial getCommercial(String id) {
		AmendCompanyCommercial companyCommercial= amendCompanyCommercialrepository.getCommercial(id);
		return companyCommercial;
	}


    /* (non-Javadoc)
     * @see com.coxandkings.travel.operations.service.amendcompanycommercial.AmendCompanyCommercialService#changeApprovalStatus(com.coxandkings.travel.operations.resource.amendentitycommercial.ChangeApprovalStatusResource)
     */
    /* (non-Javadoc)
     * @see com.coxandkings.travel.operations.service.amendcompanycommercial.AmendCompanyCommercialService#changeApprovalStatus(com.coxandkings.travel.operations.resource.amendentitycommercial.ChangeApprovalStatusResource)
     */
    @Override
    @Transactional(rollbackOn = OperationException.class)
    public String changeApprovalStatus(ChangeApprovalStatusResource changeApprovalStatusResource) throws OperationException {
        if (StringUtils.isEmpty(changeApprovalStatusResource.getId())) {
            throw new OperationException(Constants.INVALID_RQ_MISSING_ATTRIBUTE, "id");
        }
        String id = changeApprovalStatusResource.getId();

        AmendCompanyCommercial companyCommercial = getCommercial(id);
        if (companyCommercial == null) {
            throw new OperationException(Constants.INVALID_ID);
        }

        String bookingId = companyCommercial.getBookingId();
        String orderId = companyCommercial.getOrderId();
        OpsBooking booking = null;

        booking = opsBookingService.getBooking(bookingId);

        if (booking == null) {
            throw new OperationException(Constants.BOOKING_NOT_FOUND, bookingId);
        }

        Optional<OpsProduct> order = booking.getProducts().parallelStream().filter(orders -> orders.getOrderID().equals(orderId)).findAny();
        if (!order.isPresent()) {
            throw new OperationException(Constants.ORDER_NOT_FOUND, orderId, bookingId);
        }

        String message = "";
        ApprovalStatus approvalStatus = changeApprovalStatusResource.getApprovalStatus();
        message = approvalStatus.getCategory();
        companyCommercial.setApprovalStatus(approvalStatus);
        companyCommercial.setApproverRemarks(changeApprovalStatusResource.getApproverRemarks());
        if (approvalStatus.equals(ApprovalStatus.APPROVED)) {

            OpsProduct opsProduct = companyCommercial.getUpdatedOpsOrder();
            CommercialResource originalCommercials = companyCommercial.getOriginalCommercials();
            CommercialResource recalculatedCommercials = companyCommercial.getRecalculatedCommercials();

            try {
                bookingEngineDbUpdate.updateOrderPrice(opsProduct);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new OperationException(Constants.BOOKING_DB_UPDATE_FAILED);
            }
            
            //Generate Service Order
        	generateNewServiceOrder(bookingId, orderId);
            
        	//Update Invoice
        	Invoice invoice = getAndUpdateInvoice(companyCommercial.getBookingId(), companyCommercial.getOrderId(), companyCommercial.getProductSubCategory(), originalCommercials, recalculatedCommercials);


            if (originalCommercials.getMarginDetails().getClientCommercialPayables().compareTo(recalculatedCommercials.getMarginDetails().getClientCommercialPayables()) < 0) {

                //Initiate Refund
                RefundResource refundResource = new RefundResource();
                refundResource.setClientId(companyCommercial.getClientId());
                refundResource.setClientType(companyCommercial.getClientType());
                refundResource.setClaimCurrency(booking.getClientCurrency());
                
                refundResource.setBookingReferenceNo(bookingId);
                refundResource.setClientName(invoice.getClient().getClientName());
                refundResource.setDefaultModeOfPayment(booking.getPaymentInfo().get(0).getPaymentMethod());
                refundResource.setRequestedModeOfPayment(booking.getPaymentInfo().get(0).getPaymentMethod());
                
                ProductDetail productDetail = new ProductDetail();
                productDetail.setOrderId(companyCommercial.getOrderId());
                productDetail.setProductCategory(companyCommercial.getProductSubCategory().getCategory().getCategory());
                productDetail.setProductCategorySubType(companyCommercial.getProductSubCategory().getSubCategory());
                //TODO product name for each product category
                productDetail.setProductName(order.get().getProductName());
                refundResource.setProductDetail(productDetail);

                //List<OpsPaymentInfo> paymentInfo = opsBooking.getPaymentInfo(); //todo: need to think
                //refundResource.setDefaultModeOfPayment(paymentInfo.get(0).getPaymentMethod());


                refundResource.setRefundAmount(recalculatedCommercials.getMarginDetails().getClientCommercialPayables().subtract(originalCommercials.getMarginDetails().getClientCommercialPayables()));
                refundResource.setRefundStatus(RefundStatus.PENDING_WITH_OPS.getStatus());
                refundResource.setReasonForRequest(ReasonForRequest.AMEND_COMPANY_COMMERCIALS);
                refundResource.setRefundType(RefundTypes.REFUND_AMOUNT);
                refundResource.setCreatedByUserId(userService.getLoggedInUserId());
                //TODO get ROE for commercial cuurency and client currency
                //Refund is always happening in client currency, setting ROE as 1
                refundResource.setRoeAsInClaim(BigDecimal.ONE);
                refundResource.setRoeRequested(BigDecimal.ONE);
                refundService.add(refundResource);


            } else if (originalCommercials.getMarginDetails().getClientCommercialPayables().compareTo(recalculatedCommercials.getMarginDetails().getClientCommercialPayables()) > 0) {

                //Create Debit Note

                try {

                    InvoiceParticularsDto invoiceDto = invoice.getInvoiceParticularsDto().stream()
                            .filter(dto -> dto.getOrderID().equalsIgnoreCase(companyCommercial.getOrderId())).findFirst().get();
                    CreditDebitNote creditDebitNote = new CreditDebitNote();
                    creditDebitNote.setBookingRefNumber(Collections.singleton(companyCommercial.getBookingId()));
                    creditDebitNote.setInvoiceNumber(invoice.getInvoiceNumber());
                    creditDebitNote.setTypeOfInvoice(invoice.getInvoiceType());
                    creditDebitNote.setNoteType(NoteType.DEBIT.toString());
                    creditDebitNote.setNotePhase(NotePhase.Final.toString());
                    creditDebitNote.setClient_supplierID(invoice.getClient().getClientId());
                    creditDebitNote.setClientType(invoice.getClient().getClientType());
                    creditDebitNote.setIssuedDate(Calendar.getInstance().getTimeInMillis());
                    creditDebitNote.setIssuedTo("CLIENT");
                    creditDebitNote.setClient_supplierName(invoice.getClient().getClientName());
                    
                    creditDebitNote.setCurrency(invoice.getInvoiceCurrency());
                    creditDebitNote.setRoe(invoice.getRoe());
                    creditDebitNote.setStatus("APPROVED");
                    creditDebitNote.setTransactionDate(Calendar.getInstance().getTimeInMillis());
                    creditDebitNote.setFundType(FundType.REDEEMABLE.toString());
                    creditDebitNote.setTaxApplicable(false);
                    
                    creditDebitNote.setCompanyId(invoice.getSalesOffice().getCompanyID());
                    creditDebitNote.setTaxType("NA");
                    creditDebitNote.setConsumed(false);

                    CostHeaderCharge costHeaderCharge = new CostHeaderCharge();


                    costHeaderCharge.setCostHeader(ReasonForRequest.AMEND_COMPANY_COMMERCIALS.getReason());

                    costHeaderCharge.setAmount(originalCommercials.getMarginDetails().getClientCommercialPayables().subtract(recalculatedCommercials.getMarginDetails().getClientCommercialPayables()).doubleValue());
                    //costHeaderCharge.setPassengerQuantity(1);
                    costHeaderCharge.setTotalAmount(originalCommercials.getMarginDetails().getClientCommercialPayables().subtract(recalculatedCommercials.getMarginDetails().getClientCommercialPayables()).doubleValue());

                    List<CostHeaderCharge> costHeaderChargesList = new ArrayList<CostHeaderCharge>();
                    costHeaderChargesList.add(costHeaderCharge);

                    creditDebitNote.setCostHeaderCharges(costHeaderChargesList);

                    creditDebitNote.setProductCategory(invoiceDto.getProductCategory());
                    creditDebitNote.setProductCategorySubType(invoiceDto.getProductCategorySubType());
                    creditDebitNote.setProductName(invoiceDto.getProductName());
                    creditDebitNote.setProductSubName(invoiceDto.getProductNameSubType());
                    creditDebitNote.setTotalAmount(originalCommercials.getMarginDetails().getClientCommercialPayables().subtract(recalculatedCommercials.getMarginDetails().getClientCommercialPayables()).doubleValue());
                    creditDebitNote.setLinkedNoteNumber(invoice.getInvoiceNumber());
                    creditDebitNote.setLinkedNoteType("INVOICE");
                    /**
                     * TODO: Remaining fields are depends upon tax details from UI team, after that calculation can be done and save.
                     */
                    //mdmRestUtils.postForEntity(createDebitNoteUrl, creditDebitNote, Object.class);
                    RestUtils.exchange( createDebitNoteUrl, HttpMethod.POST, restUtils.getHttpEntity(creditDebitNote), String.class);

                } catch (Exception e) {
                    logger.error("Exception :", e);
                    throw new OperationException(Constants.DEBIT_NOTE_CREATION_FAIL);
                }
            }
            
            toDoTaskService.updateToDoTaskStatus(id, ToDoTaskSubTypeValues.AMEND_COMPANY_COMMERCIAL, ToDoTaskStatusValues.CLOSED);

        }
        
        else if(approvalStatus.equals(ApprovalStatus.REJECTED)) {
        	toDoTaskService.updateToDoTaskStatus(id, ToDoTaskSubTypeValues.AMEND_COMPANY_COMMERCIAL, ToDoTaskStatusValues.REJECTED);
        }
        amendCompanyCommercialrepository.saveCommercial(companyCommercial);
        return message;

    }


    @SuppressWarnings("unchecked")
	private JSONObject getAccoOpsAmendmentsJson(String bookingId, OpsProduct opsOrder, AmendCommercialResource companyCommercialResource, Element siRepriceRS,JSONObject suppCommRs) throws OperationException {
        JSONObject opsAmendmentsJson = new JSONObject();
        Optional<OpsRoom> opsRoomOpt = opsOrder.getOrderDetails().getHotelDetails().getRooms().parallelStream().filter(room -> room.getRoomID().equals(companyCommercialResource.getRoomId())).findAny();
        if (!opsRoomOpt.isPresent()) {
            throw new OperationException(Constants.ROOM_NOT_FOUND, companyCommercialResource.getRoomId());
        }
        
        String hotelCode=opsOrder.getOrderDetails().getHotelDetails().getHotelCode();
        OpsRoom opsRoom = opsRoomOpt.get();
        Map<String, Object> hotelAttrs=null;
        try {
        	hotelAttrs = RestUtils.getForObject(redisHotelDataUrl+hotelCode,Map.class);
        }
        catch(Exception e) {
        	logger.error(e.getMessage(),e);
        	throw new OperationException(Constants.CANNOT_GET_HOTEL_DATA);
        }
        
        opsAmendmentsJson.put("siRepriceResponse", XMLTransformer.toString(siRepriceRS));
        opsAmendmentsJson.put("suppCommRs", suppCommRs.toString());
        opsAmendmentsJson.put("actionItem", "amendEntityCommercials");
        opsAmendmentsJson.put("supplierId", opsOrder.getSupplierID());
        opsAmendmentsJson.put("hotelName", hotelAttrs.getOrDefault("name", ""));
        opsAmendmentsJson.put("roomTypeName", opsRoom.getRoomTypeInfo().getRoomTypeName());
        opsAmendmentsJson.put("roomCategoryName", opsRoom.getRoomTypeInfo().getRoomCategoryName());
        opsAmendmentsJson.put("ratePlanName", opsRoom.getRatePlanInfo().getRatePlanname());
        opsAmendmentsJson.put("ratePlanCode", opsRoom.getRatePlanInfo().getRatePlanCode());
        opsAmendmentsJson.put("bookingId", bookingId);
        JSONArray ineligibleCommercialsJsonArr = new JSONArray();
        opsAmendmentsJson.put("ineligibleCommercials", ineligibleCommercialsJsonArr);
        Set<BookingIneligibleFor> ineligibleCommercials = companyCommercialResource.getBookingNotEligibleFor();
        Iterator<BookingIneligibleFor> ineligibleCommercialsItr = ineligibleCommercials.iterator();
        while (ineligibleCommercialsItr.hasNext()) {
            ineligibleCommercialsJsonArr.put("Client_"+ineligibleCommercialsItr.next().getBookingNotEligibleFor());
        }
        return opsAmendmentsJson;
    }

    private JSONObject getAirOpsAmendmentsJson(String bookingId, OpsProduct opsOrder, AmendCommercialResource companyCommercialResource, Element siRepriceRS,JSONObject suppCommRs, int ccommJrnyDtlsJsonIdx) throws OperationException {
        JSONObject opsAmendmentsJson = new JSONObject();

        opsAmendmentsJson.put("siRepriceResponse", XMLTransformer.toString(siRepriceRS));
        opsAmendmentsJson.put("suppCommRs", suppCommRs.toString());
        opsAmendmentsJson.put("actionItem", "amendEntityCommercials");
        opsAmendmentsJson.put("supplierId", opsOrder.getSupplierID());
        opsAmendmentsJson.put("paxType", companyCommercialResource.getPaxType());
        opsAmendmentsJson.put("bookingId", bookingId);
        opsAmendmentsJson.put("journeyDetailsIdx", ccommJrnyDtlsJsonIdx);
        JSONArray ineligibleCommercialsJsonArr = new JSONArray();
        opsAmendmentsJson.put("ineligibleCommercials", ineligibleCommercialsJsonArr);
        Set<BookingIneligibleFor> ineligibleCommercials = companyCommercialResource.getBookingNotEligibleFor();
        Iterator<BookingIneligibleFor> ineligibleCommercialsItr = ineligibleCommercials.iterator();
        while (ineligibleCommercialsItr.hasNext()) {
            ineligibleCommercialsJsonArr.put("Client_"+ineligibleCommercialsItr.next().getBookingNotEligibleFor());
        }
        return opsAmendmentsJson;
    }

    private JSONObject consumeBEOpsReprice(JSONObject siRepriceRQ, OpsProductSubCategory opsProdSubCateogry) throws OperationException {
        JSONObject beRepriceRespJson = null;
        RestTemplate restTemplate = RestUtils.getTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(siRepriceRQ.toString(), headers);
        ResponseEntity<String> searchResponseEntity = null;
        String url = null;
        switch (opsProdSubCateogry) {
            case PRODUCT_SUB_CATEGORY_BUS:
                break;
            case PRODUCT_SUB_CATEGORY_CAR:
                break;
            case PRODUCT_SUB_CATEGORY_FLIGHT:
                url = this.airBookingEngineOpsRepriceUrl;
                break;
            case PRODUCT_SUB_CATEGORY_HOTELS:
                url = this.accoBookingEngineOpsRepriceUrl;
                break;
            case PRODUCT_SUB_CATEGORY_INDIAN_RAIL:
                break;
            case PRODUCT_SUB_CATEGORY_RAIL:
                break;
            default:
                break;

        }
        try {
            searchResponseEntity =
                    restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
            beRepriceRespJson = new JSONObject(searchResponseEntity.getBody());
            logger.info("Booking Engine Reprice Response : " + beRepriceRespJson.toString());
        } catch (RestClientException e) {
            logger.error(e.getMessage(), e);
            throw new OperationException(Constants.BE_OPS_SERVICE_ERROR, url);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
            throw new OperationException(Constants.BE_OPS_SERVICE_ERROR, url);
        }
        return beRepriceRespJson;
    }

    private int getJourneyDetailsIndex(OpsProduct opsProduct, JSONObject beRepriceRs) {
        JSONArray pricedItinJsonArr = beRepriceRs.getJSONObject("responseBody").getJSONArray("pricedItinerary");
        Map<String, Integer> suppIndexMap = new HashMap<String, Integer>();
        String opsKey = beToOpsBookingConverter.getOpsKeyForPricedItinerary(opsProduct);
        for (int i = 0; i < pricedItinJsonArr.length(); i++) {
            JSONObject pricedItinJson = pricedItinJsonArr.getJSONObject(i);
            String suppID = pricedItinJson.getString("supplierRef");
            int idx = (suppIndexMap.containsKey(suppID)) ? (suppIndexMap.get(suppID) + 1) : 0;
            suppIndexMap.put(suppID, idx);
            String beKey = beToOpsBookingConverter.getBEKeyForPricedItinerary(pricedItinJson);
            if (opsKey.equals(beKey)) {
                return suppIndexMap.get(suppID);
            }
        }
        return -1;
    }


	private void validateCommercial(AmendCompanyCommercial amendCompanyCommercial) {
		
		
        List<ApplyCommercialOn> applyCommercialOnDB = applyCommercialOnRepository.getAllStatus();
        List<BookingIneligibleFor> bookingNotEligibleForDB = bookingNotEligibleForRepository.getAllStatus();
        List<SellingPriceComponent> sellingPriceComponentsDB = sellingPriceComponentRepository.getAllStatus();
      
        Set<ApplyCommercialOn> applyCommercialOn=null;
        Set<BookingIneligibleFor> bookingNotEligibleFor=null;
        Set<SellingPriceComponent> sellingPriceComponents=null;
        
        if(amendCompanyCommercial.getApplyOnProducts()!=null) {
        	applyCommercialOn=new HashSet<>();
        	Iterator<ApplyCommercialOn> applyOnItr=amendCompanyCommercial.getApplyOnProducts().iterator();
        	Boolean matchFound = false;
        	while(applyOnItr.hasNext()) {
        		ApplyCommercialOn applyOnRQ=applyOnItr.next();
        		for(ApplyCommercialOn applyOnDB:applyCommercialOnDB) {
        			if(applyOnRQ.getApplyCommercialOn().equals(applyOnDB.getApplyCommercialOn())) {
        				applyCommercialOn.add(applyOnDB);
        				matchFound = true;
        				break;
        			}
        		}
        		if (!(matchFound)) {
        			applyCommercialOn.add(applyOnRQ);
                }
        	}
        	amendCompanyCommercial.setApplyOnProducts(applyCommercialOn);
        }
        
        if(amendCompanyCommercial.getBookingNotEligibleFor()!=null) {
        	bookingNotEligibleFor= new HashSet<>();
        	Iterator<BookingIneligibleFor> bookingNotEligibleItr=amendCompanyCommercial.getBookingNotEligibleFor().iterator();
        	while(bookingNotEligibleItr.hasNext()) {
        		BookingIneligibleFor bookingNotEligibleRQ=bookingNotEligibleItr.next();
        		Boolean matchFound = false;
        		for(BookingIneligibleFor bookingNotEligibleDB:bookingNotEligibleForDB) {
        			if(bookingNotEligibleRQ.getBookingNotEligibleFor().equals(bookingNotEligibleDB.getBookingNotEligibleFor())) {
        				bookingNotEligibleFor.add(bookingNotEligibleDB);
        				matchFound = true;
        				break;
        			}
        		}
        		if (!(matchFound)) {
        			bookingNotEligibleFor.add(bookingNotEligibleRQ);
                }       		
        	}
        	amendCompanyCommercial.setBookingNotEligibleFor(bookingNotEligibleFor);
        }

        if(amendCompanyCommercial.getSellingPriceComponent()!=null) {
        	sellingPriceComponents=new HashSet<>();
        	Iterator<SellingPriceComponent> priceComponentItr=amendCompanyCommercial.getSellingPriceComponent().iterator();
        	while(priceComponentItr.hasNext()) {
        		SellingPriceComponent priceComponentRQ=priceComponentItr.next();
        		Boolean matchFound = false;
        		for(SellingPriceComponent priceComponentDB:sellingPriceComponentsDB) {
        			if(priceComponentRQ.getSellingPriceComponent().equals(priceComponentDB.getSellingPriceComponent())) {
        				sellingPriceComponents.add(priceComponentDB);
        				matchFound =true;
        				break;
        			}
        		}
        		if (!(matchFound)) {
        			sellingPriceComponents.add(priceComponentRQ);
                }
        	}
        	amendCompanyCommercial.setSellingPriceComponent(sellingPriceComponents);
        }
        
		
	}

    private ToDoTask createToDoTask(AmendCompanyCommercial amendCompanyCommercial) throws OperationException {
    	
    	String clientType=amendCompanyCommercial.getClientType();
    	String clientId=amendCompanyCommercial.getClientId();
    	
    	String clientCategory=null;
    	String clientSubCategory=null;
    	String companyMarket=null;
    	
    	if(clientType.equalsIgnoreCase("B2B")) {
    		B2BClient b2bClient=mdmClientService.getB2bClient(clientId);
    		clientCategory=b2bClient.getClientCategory();
    		clientSubCategory=b2bClient.getClientSubCategory(); 
    		companyMarket= b2bClient.getCompanyId();//TODo: need to add in B2Bclient class
    	}
    	else if(clientType.equalsIgnoreCase("B2C")) {
    		B2CClient b2cClient=mdmClientService.getB2cClient(clientId);
    		clientCategory=b2cClient.getClientCategory();
    		clientSubCategory=b2cClient.getClientSubCategory();
    		companyMarket=b2cClient.getCompanyId();//TODo: need to add in B2Bclient class
    	}

		ToDoTaskResource toDoTaskResource = new ToDoTaskResource();
		toDoTaskResource.setReferenceId(amendCompanyCommercial.getId());
		toDoTaskResource.setBookingRefId(amendCompanyCommercial.getBookingId());
		toDoTaskResource.setCreatedByUserId(userService.getLoggedInUserId());
		toDoTaskResource.setAssignedBy(userService.getLoggedInUserId());
		
		toDoTaskResource.setProductId(amendCompanyCommercial.getProductSubCategory().getCategory().getCategory());
        toDoTaskResource.setClientId(clientId);
        toDoTaskResource.setClientTypeId(clientType);
        toDoTaskResource.setCompanyId(amendCompanyCommercial.getCompanyId());
        toDoTaskResource.setClientCategoryId(clientCategory);
		toDoTaskResource.setClientSubCategoryId(clientSubCategory);
        toDoTaskResource.setCompanyMarketId(companyMarket);
        toDoTaskResource.setTaskOrientedTypeId(ToDoTaskOrientedValues.APPROVAL_ORIENTED.getValue());
		toDoTaskResource.setTaskNameId(ToDoTaskNameValues.APPROVE.getValue());
		toDoTaskResource.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
		toDoTaskResource.setTaskSubTypeId(ToDoTaskSubTypeValues.AMEND_COMPANY_COMMERCIAL.toString()); // AMEND_CLIENT_COMMERCIAL
		toDoTaskResource.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue()); // OPERATIONS or FINANCE
		toDoTaskResource.setTaskStatusId(ToDoTaskStatusValues.ASSIGNED.getValue()); // ASSIGN as per my assumption
		toDoTaskResource.setTaskPriorityId(ToDoTaskPriorityValues.HIGH.getValue()); // Dummy value HIGH
		toDoTaskResource.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.toString());
        //toDoTaskResource.setFileHandlerId("COMMERCIAL_APPROVER");// person whom the request is send for approval
        //toDoTaskResource.setSecondaryFileHandlerId("COMMERCIAL_SECONDARY_APPROVER"); // secondary approver
		toDoTaskResource.setSuggestedActions("");// sent the rest end point for now
		// toDoTaskResource.setDueOn(System.currentTimeMillis() + 55L);
		ToDoTask toDoTask = null;

        try {
            toDoTask = toDoTaskService.save(toDoTaskResource);
        } catch (InvocationTargetException | IllegalAccessException | ParseException | JSONException | IOException e) {
            logger.error(e.getMessage(), e);
            throw new OperationException(Constants.TODO_TASK_CREATION_FAIL);
        }
		

		return toDoTask;
	}
	
	
	/*@Override
	public MarginDetails calculateMargin(CommercialResource commercialResource,String companyMarket) {
		MarginDetails marginDetails=new MarginDetails();
		BigDecimal netMargin=null;
		BigDecimal netSupplierCost=null;
		BigDecimal netSellingPrice=null;
		BigDecimal supplierCost=null;
		BigDecimal supplierCommercialPayables=new BigDecimal(0);
		BigDecimal supplierCommercialsReveivables=new BigDecimal(0);
		BigDecimal clientCommercialPayables=new BigDecimal(0);
		BigDecimal clientCommercialsReveivables=new BigDecimal(0);
		String supplierCurrency="";
		String clientCurrency="";
		
		if(commercialResource.getFlightPrice()!=null) {
			FlightPrice flightPrice=commercialResource.getFlightPrice();
			supplierCost=new BigDecimal(flightPrice.getOrderSupplierPriceInfo().getSupplierPrice());
			supplierCurrency=flightPrice.getOrderSupplierPriceInfo().getCurrencyCode();
			clientCurrency=flightPrice.getOrderTotalPriceInfo().getCurrencyCode();
			
		}
		
		
		else if(commercialResource.getHotelPrice()!=null) {
			HotelPrice hotelPrice=commercialResource.getHotelPrice();
			supplierCost=new BigDecimal(hotelPrice.getOrderSupplierPriceInfo().getSupplierPrice());
			supplierCurrency=hotelPrice.getOrderSupplierPriceInfo().getCurrencyCode();
			clientCurrency=hotelPrice.getOrderTotalPriceInfo().getCurrencyCode();
				
		}
		
		for(OpsOrderSupplierCommercial supplierCommercial:commercialResource.getOrderSupplierCommercials()) {
			
			if(supplierCommercial.getCommercialType().equals("Receivable"))
				supplierCommercialsReveivables=supplierCommercialsReveivables.add(new BigDecimal(supplierCommercial.getCommercialAmount()));
			else if(supplierCommercial.getCommercialType().equals("Payable"))
				supplierCommercialPayables=supplierCommercialPayables.add(new BigDecimal(supplierCommercial.getCommercialAmount()));
			
		}
		
		for(OpsOrderClientCommercial clientCommercial:commercialResource.getOrderClientCommercials().stream().filter(commercial->commercial.getCompanyFlag()==true).collect(Collectors.toList())) {
			if(clientCommercial.getCommercialType().equals("Receivable"))
				clientCommercialsReveivables=clientCommercialsReveivables.add(new BigDecimal(clientCommercial.getCommercialAmount()));
			else if(clientCommercial.getCommercialType().equals("Payable"))
				clientCommercialPayables=clientCommercialPayables.add(new BigDecimal(clientCommercial.getCommercialAmount()));
			
		}
		
		
		netSupplierCost=supplierCost.add(supplierCommercialPayables).subtract(supplierCommercialsReveivables);
		netSellingPrice=supplierCost.add(clientCommercialsReveivables).subtract(clientCommercialPayables);
		netMargin=netSellingPrice.subtract(netSupplierCost);
		
		//BigDecimal roe=RestUtils.getForObject("http://10.24.2.248:8080/booking_engine/getRoe/{supplierCurrency}/{clientCurrency}", BigDecimal.class, supplierCurrency,clientCurrency);
		//System.out.println("ROE Fetched "+roe);
		//TODO check if ROE is required to change everything to client currency
		marginDetails.setCurrencyCode(supplierCurrency);
		marginDetails.setNetMargin(netMargin);
		marginDetails.setNetSellingPrice(netSellingPrice);
		marginDetails.setNetSupplierCost(netSupplierCost);
		marginDetails.setClientCommercialPayables(clientCommercialPayables);
		marginDetails.setClientCommercialsReveivables(clientCommercialsReveivables);
		marginDetails.setSupplierCommercialPayables(supplierCommercialPayables);
		marginDetails.setSupplierCommercialsReveivables(supplierCommercialsReveivables);
		return marginDetails;
		
	}
*/


	@Override
	public CommercialResource getCommercialResourceForOrder(OpsOrderDetails opsOrder, OpsProductSubCategory opsProductSubCategory ,String uniqueId) {
		
		CommercialResource commercialResource=new CommercialResource();
		commercialResource.setOrderClientCommercials(opsOrder.getClientCommercials());
		commercialResource.setOrderSupplierCommercials(opsOrder.getSupplierCommercials());
		switch(opsProductSubCategory) {
		case PRODUCT_SUB_CATEGORY_FLIGHT:
		{
			FlightPrice flightPrice=new FlightPrice();
			flightPrice.setOrderSupplierPriceInfo(opsOrder.getFlightDetails().getOpsFlightSupplierPriceInfo());
			flightPrice.setOrderTotalPriceInfo(opsOrder.getFlightDetails().getTotalPriceInfo());
			commercialResource.setFlightPrice(flightPrice);
			break;
		}
		case PRODUCT_SUB_CATEGORY_HOTELS:
		{
			HotelPrice hotelPrice=new HotelPrice();
			hotelPrice.setOrderSupplierPriceInfo(opsOrder.getHotelDetails().getOpsAccoOrderSupplierPriceInfo());
			hotelPrice.setOrderTotalPriceInfo(opsOrder.getHotelDetails().getOpsAccommodationTotalPriceInfo());
			if(uniqueId!=null) {
				OpsRoom opsRoom=opsOrder.getHotelDetails().getRooms().stream().filter(room->room.getRoomID().equals(uniqueId)).findAny().get();
				hotelPrice.setRoomClientCommercials(opsRoom.getOpsClientEntityCommercial());
				hotelPrice.setRoomSupplierCommercials(opsRoom.getRoomSuppCommercials());
				hotelPrice.setRoomSupplierPriceInfo(opsRoom.getRoomSuppPriceInfo());
				hotelPrice.setRoomTotalPriceInfo(opsRoom.getRoomTotalPriceInfo());
			}
			commercialResource.setHotelPrice(hotelPrice);
			break;
		}
		
		default:
			break;
		
	}
		return commercialResource;
	}

    private Invoice getAndUpdateInvoice(String bookingId, String orderId, OpsProductSubCategory productSubCategory, CommercialResource originalResource, CommercialResource recalculatedCommercials) throws OperationException {
		UriComponentsBuilder getInvoicebuilder = UriComponentsBuilder.fromUriString(getInvoiceUrl)
                .queryParam("bookingRef", bookingId)
                .queryParam("orderID", orderId);
        ResponseEntity<List<Invoice>> response = null;
        
        try {
            //response=RestUtils.exchange(getInvoicebuilder.toUriString(),HttpMethod.GET,null, new ParameterizedTypeReference<List<Invoice>>() {});
            response = RestUtils.exchange(getInvoicebuilder.build().encode().toUri().toString(), HttpMethod.GET, restUtils.getHttpEntity(), new ParameterizedTypeReference<List<Invoice>>() {
            });
        } catch (RestClientException e) {
            logger.error(e.getMessage(), e);
            throw new OperationException(Constants.FINANCE_SERVICE_ERROR,getInvoicebuilder.build().encode().toUri().toString());
        }

        List<Invoice> invoices = response.getBody();
        if (invoices == null) {
            throw new OperationException(Constants.FINANCE_SERVICE_ERROR,getInvoicebuilder.build().encode().toUri().toString());
        }

        Invoice invoice = invoices.get(0); //TODO check with finance about how to identify multiple invoices in case of holidays
		
		InvoiceParticularsDto invoiceDto=invoice.getInvoiceParticularsDto().parallelStream().filter(dto -> dto.getOrderID().equalsIgnoreCase(orderId)).findFirst().orElse(null);

        if (invoiceDto == null) {
            throw new OperationException(Constants.FINANCE_SERVICE_INVOICE_NOT_FOUND, bookingId, orderId);
        }

        List<CommercialEntity> invoicedCommercials = new ArrayList<CommercialEntity>();
        for (OpsOrderClientCommercial clientCommercial : recalculatedCommercials.getOrderClientCommercials()) {
        	if(clientCommercial.isEligible()) {
            CommercialEntity invoiceCommercial = new CommercialEntity();
            invoiceCommercial.setCommercialAmount(Double.parseDouble(clientCommercial.getCommercialAmount()));
            invoiceCommercial.setCommercialName(clientCommercial.getCommercialName());
            invoiceCommercial.setCommercialType(clientCommercial.getCommercialType());
            invoicedCommercials.add(invoiceCommercial);
        	}
        }
        invoiceDto.setOrderCommercials(invoicedCommercials);
		
		
		
		/*switch(productSubCategory) {
		case PRODUCT_SUB_CATEGORY_FLIGHT:
			invoiceDto.setSellingPrice(Double.parseDouble(recalculatedCommercials.getFlightPrice().getOrderTotalPriceInfo().getTotalPrice()));
			invoiceDto.setTotalTax(recalculatedCommercials.getFlightPrice().getOrderTotalPriceInfo().getTaxes().getAmount());
			for(TaxDetail tax:invoiceDto.getTaxDetails()) {
				Optional<OpsTax> optOpsTax =recalculatedCommercials.getFlightPrice().getOrderTotalPriceInfo().getTaxes().getTax().stream().filter(opsTax->opsTax.getTaxCode().equals(tax.getTaxCode())).findFirst();
				if(optOpsTax.isPresent()) {
					tax.setTaxAmount(optOpsTax.get().getAmount());
					tax.setCurrency(optOpsTax.get().getCurrencyCode());
				}
				else {
					tax.setTaxAmount(0.0);
				}
				
			}
			break;
		case PRODUCT_SUB_CATEGORY_HOTELS:
			invoiceDto.setSellingPrice(Double.parseDouble(recalculatedCommercials.getHotelPrice().getOrderTotalPriceInfo().getTotalPrice()));
			invoiceDto.setTotalTax(recalculatedCommercials.getHotelPrice().getOrderTotalPriceInfo().getOpsTaxes().getAmount());
			for(TaxDetail tax:invoiceDto.getTaxDetails()) {
				Optional<OpsTax> optOpsTax =recalculatedCommercials.getHotelPrice().getOrderTotalPriceInfo().getOpsTaxes().getTax().stream().filter(opsTax->opsTax.getTaxCode().equals(tax.getTaxCode())).findFirst();
				if(optOpsTax.isPresent()) {
					tax.setTaxAmount(optOpsTax.get().getAmount());
					tax.setCurrency(optOpsTax.get().getCurrencyCode());
				}
				else {
					tax.setTaxAmount(0.0);
				}
				
			}
			break;
		default:
			break;
		
		}*/
        Invoice updatedInvoice = null;
        try {

            UriComponentsBuilder amendInvoicebuilder = UriComponentsBuilder.fromUriString(updateInvoiceUrl)
                    .queryParam("invoiceNumber", invoice.getInvoiceNumber());
            ResponseEntity<Invoice> amendedInvoice = RestUtils.exchange(amendInvoicebuilder.build().encode().toUri(),
                    HttpMethod.PUT, restUtils.getHttpEntity(objectMapper.writeValueAsString(invoice)), Invoice.class);
            updatedInvoice = amendedInvoice.getBody();
            logger.info("Updated Invoice:" + objectMapper.writeValueAsString(updatedInvoice));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new OperationException(Constants.FINANCE_INVOICE_UPDATE_FAILED);
        }

        return updatedInvoice;
    }
    
    private void generateNewServiceOrder(String bookingRefId, String orderId) throws OperationException {
		OpsBooking opsBooking = opsBookingService.getBooking(bookingRefId);
		OpsProduct opsProduct = opsBookingService.getOpsProduct(opsBooking,orderId);
		try {
			serviceOrderAndSupplierLiabilityService.generateServiceOrder(opsProduct,opsBooking,false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
