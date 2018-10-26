package com.coxandkings.travel.operations.service.amendsuppliercommercial.impl;

import com.coxandkings.travel.ext.model.finance.creditdebitnote.CostHeaderCharge;
import com.coxandkings.travel.ext.model.finance.creditdebitnote.CreditDebitNote;
import com.coxandkings.travel.ext.model.finance.invoice.CommercialEntity;
import com.coxandkings.travel.ext.model.finance.invoice.Invoice;
import com.coxandkings.travel.ext.model.finance.invoice.InvoiceParticularsDto;
import com.coxandkings.travel.operations.enums.amendclientcommercials.BEInboundOperation;
import com.coxandkings.travel.operations.enums.amendclientcommercials.BEOperationId;
import com.coxandkings.travel.operations.enums.amendclientcommercials.BEServiceUri;
import com.coxandkings.travel.operations.enums.amendsuppliercommercial.SupplierCommercialApproval;
import com.coxandkings.travel.operations.enums.debitNote.FundType;
import com.coxandkings.travel.operations.enums.debitNote.NotePhase;
import com.coxandkings.travel.operations.enums.debitNote.NoteType;
import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.enums.refund.ReasonForRequest;
import com.coxandkings.travel.operations.enums.refund.RefundStatus;
import com.coxandkings.travel.operations.enums.refund.RefundTypes;
import com.coxandkings.travel.operations.enums.todo.*;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.amendsuppliercommercial.AccoSupplierCommercial;
import com.coxandkings.travel.operations.model.amendsuppliercommercial.AirSupplierCommercial;
import com.coxandkings.travel.operations.model.amendsuppliercommercial.AmendSupplierCommercial;
import com.coxandkings.travel.operations.model.amendsuppliercommercial.SupplierCommercialResource;
import com.coxandkings.travel.operations.model.commercials.ApplyCommercialOn;
import com.coxandkings.travel.operations.model.commercials.BookingIneligibleFor;
import com.coxandkings.travel.operations.model.commercials.SellingPriceComponent;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.model.productbookedthrother.mdm.B2BClient;
import com.coxandkings.travel.operations.model.productbookedthrother.mdm.B2CClient;
import com.coxandkings.travel.operations.model.refund.finaceRefund.ProductDetail;
import com.coxandkings.travel.operations.model.todo.ToDoTask;
import com.coxandkings.travel.operations.producer.service.WorkUnitDispatcher;
import com.coxandkings.travel.operations.repository.amendsuppliercommercial.AmendSupplierCommercialRepository;
import com.coxandkings.travel.operations.repository.commercials.ApplyCommercialOnRepository;
import com.coxandkings.travel.operations.repository.commercials.BookingNotEligibleForRepository;
import com.coxandkings.travel.operations.repository.commercials.SellingPriceComponentRepository;
import com.coxandkings.travel.operations.repository.todo.ToDoTaskRepository;
import com.coxandkings.travel.operations.resource.amendsuppliercommercial.AmendSupplierCommercialApprovalResource;
import com.coxandkings.travel.operations.resource.amendsuppliercommercial.AmendSupplierCommercialResource;
import com.coxandkings.travel.operations.resource.amendsuppliercommercial.SupplierCommercialPricingDetailResource;
import com.coxandkings.travel.operations.resource.refund.RefundResource;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResource;
import com.coxandkings.travel.operations.service.amendcompanycommercial.MDMCommercials;
import com.coxandkings.travel.operations.service.amendsuppliercommercial.AmendSupplierCommercialService;
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
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.*;

//import javax.transaction.Transactional;

@Service
public class AmendSupplierCommercialServiceImpl implements AmendSupplierCommercialService {
    private static Logger logger = LogManager.getLogger(AmendSupplierCommercialServiceImpl.class);
    @Autowired
    private AmendSupplierCommercialRepository amendSupplierCommercialRepository;
    @Autowired
    private ToDoTaskService toDoTaskService;
    @Autowired
    private ToDoTaskRepository toDoTaskRepository;

    @Autowired
    private OpsBookingService opsBookingService;
    @Autowired
    private RefundService refundService;
    @Autowired
    private UserService userService;
   
    
    @Value("${kafka.producer.supplier-commercials.topic}")
    private String commercialsTopic;

    @Autowired
    private MDMCommercials mdmCommercials;
    @Autowired
    private WorkUnitDispatcher supplierCommercialProducer;

    @Autowired
    private BookingEngineElasticData bookingEngineElasticData;
    @Autowired
    private BeToOpsBookingConverter beToOpsBookingConverter;
    @Value("${booking-engine-core-services.air.opsreprice}")
    private String airBookingEngineOpsRepriceUrl;
    @Value("${booking-engine-core-services.acco.opsreprice}")
    private String accoBookingEngineOpsRepriceUrl;
    @Value("${booking-engine-core-services.acco.hoteldata}")
    private String redisHotelDataUrl;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ApplyCommercialOnRepository applyCommercialOnRepository;

    @Autowired
    private BookingNotEligibleForRepository bookingNotEligibleForRepository;

    @Autowired
    private SellingPriceComponentRepository sellingPriceComponentRepository;

    @Autowired
    private BeDBUpdateService beDBUpdateService;
    
    @Value(value = "${ROE.booking-date}")
    private String getROEUrl;

    @Autowired
    private MdmClientService mdmClientService;
    
   /* @Autowired
    private MDMRestUtils mdmRestUtils;*/
    
    
    @Value("${amend-commercials.create.debit_note}")
    private String createDebitNoteUrl;
    
    @Value("${amend-commercials.get.invoice-by-booking-and-order}")
    private String getInvoiceUrl;

    @Value("${amend-commercials.update.invoice}")
    private String updateInvoiceUrl;
    
    @Autowired
	private ServiceOrderAndSupplierLiabilityService serviceOrderAndSupplierLiabilityService;
    
    @Autowired
    RestUtils restUtils;
    
    
    public AmendSupplierCommercialServiceImpl() {
        super();
    }

    @Override
    @Transactional(rollbackOn = OperationException.class)
    public void save(AmendSupplierCommercialResource amendSupplierCommercialResource) throws OperationException {
  /*      Set<ApplySupplierCommercialOn> applyProducts = amendSupplierCommercial.getApplyProducts();
        if (applyProducts != null) {
            for (ApplySupplierCommercialOn aAmendSupplierCommercial : applyProducts) {
                aAmendSupplierCommercial.setAmendSupplierCommercial(amendSupplierCommercial);
            }
        }
        Set<BookingNotEligibleFor> bookingNotEligibleFor = amendSupplierCommercial.getBookingNotEligibleFor();
        if (bookingNotEligibleFor != null) {
            for (BookingNotEligibleFor booking : bookingNotEligibleFor) {
                booking.setAmendSupplierCommercial(amendSupplierCommercial);
            }
        }
        Set<SupplierPriceComponent> supplierPriceComponents = amendSupplierCommercial.getSupplierPriceComponents();
        if (supplierPriceComponents != null) {
            for (SupplierPriceComponent supplierPriceComponent : supplierPriceComponents) {
                supplierPriceComponent.setAmendSupplierCommercial(amendSupplierCommercial);
            }
        }

*/
        AmendSupplierCommercial amendSupplierCommercial = new AmendSupplierCommercial();
        CopyUtils.copy(amendSupplierCommercialResource, amendSupplierCommercial);
        amendSupplierCommercial.setCreatedByUserId(userService.getLoggedInUserId());
        amendSupplierCommercial.setCreatedTime(System.currentTimeMillis());
        validateCommercial(amendSupplierCommercial);
        amendSupplierCommercial.setApprovalStatus(SupplierCommercialApproval.PENDING);
        amendSupplierCommercial = amendSupplierCommercialRepository.saveAmendSupplierCommercial(amendSupplierCommercial);// save and get the id
        ToDoTask toDoTask = createToDoTask(amendSupplierCommercial);// id is required to save in todo
        amendSupplierCommercial.setTodoTaskId(toDoTask.getId());
        amendSupplierCommercial = amendSupplierCommercialRepository.saveAmendSupplierCommercial(amendSupplierCommercial);
        
    }


    @Override
    public void update(AmendSupplierCommercialResource supplierCommercial) throws OperationException {
    	
    	OpsBooking booking = null;
        
        String bookingId = supplierCommercial.getBookingId();
        String orderId = supplierCommercial.getOrderId();

        booking = opsBookingService.getBooking(bookingId);

        if (booking == null) {
            throw new OperationException(Constants.BOOKING_NOT_FOUND, bookingId);
        }

        Optional<OpsProduct> order = booking.getProducts().parallelStream().filter(orders -> orders.getOrderID().equals(orderId)).findAny();
        if (!order.isPresent()) {
            throw new OperationException(Constants.ORDER_NOT_FOUND, orderId, bookingId);
        }
        if(supplierCommercial.getAmountApplicable()) {
        updateCommercialFromROE(supplierCommercial,booking);
        }
        
        JSONObject mdmCommercial = mdmCommercials.updateMDMSupplierCommercial(supplierCommercial);
        if (mdmCommercial == null) {
            throw new OperationException("Could not update MDM Commercial");
        }
        supplierCommercialProducer.dispatch(commercialsTopic,mdmCommercial.toString());
        logger.info("Amend Supplier Commercial Kafka Msg :" + mdmCommercial.toString());
        System.out.println(mdmCommercial);
    }

    private void updateCommercialFromROE(AmendSupplierCommercialResource supplierCommercial, OpsBooking booking) throws OperationException {
		String amendmentCurrency=supplierCommercial.getCurrency();
		String bookingDate=DateTimeFormatter.ofPattern("yyyy-MM-dd").format(booking.getBookingDateZDT());
		String commercialCurrency=supplierCommercial.getOldSupplierCommercialResource().getOrderSupplierCommercials().stream().
				filter(commercial->commercial.getCommercialName().equals(supplierCommercial.getSupplierCommercialHead())).findAny().get().getCommercialCurrency();
		if(commercialCurrency==null || commercialCurrency.length()<=0) {
			return;
		}
		if(!amendmentCurrency.equals(commercialCurrency)) {
			BigDecimal commercialToClientROE=null;
			try {      
            	UriComponents getROE = UriComponentsBuilder.fromUriString(getROEUrl).pathSegment(amendmentCurrency).
            			pathSegment(commercialCurrency).pathSegment(booking.getClientMarket()).pathSegment(bookingDate).build();
            	commercialToClientROE=RestUtils.getForObject(getROE.toUriString(), BigDecimal.class);
            	
            	}
            	catch(Exception e) {
            		throw new OperationException("Cannot Get ROE for Commercial");
            	}
			supplierCommercial.setAmount(supplierCommercial.getAmount().multiply(commercialToClientROE));
			supplierCommercial.setCurrency(commercialCurrency);
		}
		
	}
    
    @Override
    public AmendSupplierCommercial getAmendSupplierCommercial(String id) {
        return amendSupplierCommercialRepository.getAmendSupplierCommercial(id);
    }

   /* @Override
    @Transactional
    public String changeApprovalStatus(AmendSupplierCommercialApprovalResource resource) throws OperationException {
        if (StringUtils.isEmpty(resource.getAmendSupplierCommercialId())) {
            throw new OperationException("Invalid request - ID or DiscrepancyStatus Missing");
        }
        String id = resource.getAmendSupplierCommercialId();
        SupplierCommercialApproval approvalStatus = resource.getApprovalStatus();
        AmendSupplierCommercial supplierCommercial = getAmendSupplierCommercial(id);
        supplierCommercial.setApprovalStatus(approvalStatus);
        ToDoTask todoTask = null;
        try {
            todoTask = toDoTaskService.getById(resource.getTodoTaskId());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ToDoTaskStatusValues toDoStatus = todoTask.getTaskStatus();
        if (approvalStatus == SupplierCommercialApproval.APPROVED) {
            toDoStatus = ToDoTaskStatusValues.CLOSED;
            todoTask.setTaskStatus(toDoStatus);
        }
        //TODO : Change repository with service layer functions
        toDoTaskRepository.saveOrUpdate(todoTask);
        return "";
    }
*/
    @Override
    public ToDoTask createToDoTask(AmendSupplierCommercial amendSupplierCommercial) throws OperationException {
    	
    	String clientType=amendSupplierCommercial.getClientTypeId();
    	String clientId=amendSupplierCommercial.getClientId();
    	
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
        toDoTaskResource.setReferenceId(amendSupplierCommercial.getId()); //OPS DB ClientCommercialAmendmentID
        toDoTaskResource.setBookingRefId(amendSupplierCommercial.getBookingId());
        toDoTaskResource.setCreatedByUserId(userService.getLoggedInUserId());
        toDoTaskResource.setAssignedBy(userService.getLoggedInUserId());
        
        toDoTaskResource.setProductId(amendSupplierCommercial.getUpdatedOpsOrder().getOpsProductCategory().getCategory());
        toDoTaskResource.setClientId(clientId);
        toDoTaskResource.setClientTypeId(clientType);
        toDoTaskResource.setCompanyId(amendSupplierCommercial.getCompanyId());
        toDoTaskResource.setClientCategoryId(clientCategory);
		toDoTaskResource.setClientSubCategoryId(clientSubCategory);
        toDoTaskResource.setCompanyMarketId(companyMarket);
        
        toDoTaskResource.setTaskOrientedTypeId(ToDoTaskOrientedValues.APPROVAL_ORIENTED.getValue());
        toDoTaskResource.setTaskNameId(ToDoTaskNameValues.APPROVE.getValue());
        toDoTaskResource.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
        toDoTaskResource.setTaskSubTypeId(ToDoTaskSubTypeValues.AMEND_SUPPLIER_COMMERCIAL.toString()); //AMEND_CLIENT_COMMERCIAL
        toDoTaskResource.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue()); // OPERATIONS or FINANCE
        toDoTaskResource.setTaskStatusId(ToDoTaskStatusValues.ASSIGNED.getValue()); //ASSIGN as per my assumption
        toDoTaskResource.setTaskPriorityId(ToDoTaskPriorityValues.HIGH.getValue()); // Dummy value HIGH
        toDoTaskResource.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.toString());
        //toDoTaskResource.setFileHandlerId("CLIENT_COMMERCIAL_APPROVER");// person whom the request is send for approval
        //toDoTaskResource.setSecondaryFileHandlerId("CLIENT_COMMERCIAL_SECONDARY_APPROVER"); // secondary approver
        toDoTaskResource.setSuggestedActions("");//sent the rest end point for now
        //toDoTaskResource.setDueOn(System.currentTimeMillis() + 55L);
		ToDoTask toDoTask = null;
		try {
			toDoTask = toDoTaskService.save(toDoTaskResource);
        } catch (InvocationTargetException | IllegalAccessException | ParseException | JSONException | IOException e) {
            logger.error(e.getMessage(), e);
            throw new OperationException(Constants.TODO_TASK_CREATION_FAIL);
		}
		
        return toDoTask;
    }


    @Override
    @Transactional(rollbackOn = OperationException.class)
    public AmendSupplierCommercial calculateSupplierCommercialForAcco(AmendSupplierCommercialResource supplierCommercial) throws OperationException {
        AmendSupplierCommercial amendSupplierCommercial = new AmendSupplierCommercial();

      //  String url = this.revisedAccoURL;
        String bookingId = supplierCommercial.getBookingId();
        String orderId = supplierCommercial.getOrderId();
        OpsBooking opsBooking = opsBookingService.getBooking(bookingId);
        OpsProduct opsProduct = opsBookingService.getOpsProduct(opsBooking, orderId);

        OpsProductCategory productCategory = OpsProductCategory.getProductCategory(opsProduct.getProductCategory());
        OpsProductSubCategory productSubCategory = OpsProductSubCategory.getProductSubCategory(productCategory, opsProduct.getProductSubCategory());
        String userId = opsBooking.getUserID();
        String transactionId = opsBooking.getTransactionID();
        String sessionId = opsBooking.getSessionID();
        JSONObject beRepriceRq = null;
        Element siRepriceResponse = null;

        JSONObject opsAmendmentsJson = null;
        JSONObject beResponseJson = null;
        OpsBooking updatedOpsBooking = null;
        OpsProduct updatedOpsOrder = null;
        SupplierCommercialResource revisedSupplierCommercial = null;

        beRepriceRq = bookingEngineElasticData.getJSONData(userId, transactionId, sessionId, BEInboundOperation.REPRICE, BEOperationId.BOOKING_ENGINE_RQ, BEServiceUri.HOTEL);
        beRepriceRq.getJSONObject("requestHeader").put("userID", userService.getLoggedInUserId());
        logger.trace("Booking Engine Acco Reprice Request : " + beRepriceRq.toString());
        siRepriceResponse = bookingEngineElasticData.getXMLData(userId, transactionId, sessionId, BEInboundOperation.REPRICE, BEOperationId.SUPPLIER_INTEGRATION_RS, BEServiceUri.HOTEL);
        logger.trace("SI Acco Reprice Response : " + XMLTransformer.toString(siRepriceResponse));
        opsAmendmentsJson = getAccoOpsAmendmentsJson(bookingId, opsProduct, supplierCommercial, siRepriceResponse);
        opsAmendmentsJson.put("bookingDate", DateTimeFormatter.ofPattern("yyyy-MM-dd'T00:00:00'").format(opsBooking.getBookingDateZDT()));
        beRepriceRq.getJSONObject("requestBody").put("opsAmendments", opsAmendmentsJson);
        beResponseJson = consumeBEOpsReprice(beRepriceRq, productSubCategory);
        updatedOpsBooking = beToOpsBookingConverter.updateAccoOrder(beRepriceRq, beResponseJson, bookingId, orderId, supplierCommercial.getRoomId());
        updatedOpsOrder = updatedOpsBooking.getProducts().parallelStream().filter(orders -> orders.getOrderID().equals(supplierCommercial.getOrderId())).findAny().get();
        OpsRoom opsRoom = updatedOpsOrder.getOrderDetails().getHotelDetails().getRooms().parallelStream().filter(room -> room.getRoomID().equals(supplierCommercial.getRoomId())).findAny().get();
        updatedOpsOrder.getOrderDetails().getHotelDetails().getRooms().clear();
        updatedOpsOrder.getOrderDetails().getHotelDetails().getRooms().add(opsRoom);
        revisedSupplierCommercial = getCommercialResourceForOrder(updatedOpsBooking, updatedOpsOrder, supplierCommercial);
        CopyUtils.copy(supplierCommercial, amendSupplierCommercial);
        amendSupplierCommercial.setRevisedSupplierCommercialResource(revisedSupplierCommercial);
        amendSupplierCommercial.setUpdatedOpsOrder(updatedOpsOrder);
        return amendSupplierCommercial;
    }

    @Override
    @Transactional(rollbackOn = OperationException.class)
    public AmendSupplierCommercial calculateSupplierCommercialForAir(AmendSupplierCommercialResource supplierCommercial) throws OperationException {
        String bookingId = supplierCommercial.getBookingId();
        String orderId = supplierCommercial.getOrderId();
        OpsBooking opsBooking = opsBookingService.getBooking(bookingId);
        OpsProduct opsProduct = opsBookingService.getOpsProduct(opsBooking, orderId);
        OpsProductCategory productCategory = OpsProductCategory.getProductCategory(opsProduct.getProductCategory());
        OpsProductSubCategory productSubCategory = OpsProductSubCategory.getProductSubCategory(productCategory, opsProduct.getProductSubCategory());
        String userId = opsBooking.getUserID();
        String transactionId = opsBooking.getTransactionID();
        String sessionId = opsBooking.getSessionID();
        JSONObject beRepriceRq = null;
        Element siRepriceResponse = null;
        JSONObject opsAmendmentsJson = null;
        JSONObject beResponseJson = null;
        OpsBooking updatedOpsBooking = null;
        OpsProduct updatedOpsOrder = null;
        AmendSupplierCommercial amendSupplierCommercial = new AmendSupplierCommercial();
       // String url = this.revisedAirURL;
        CopyUtils.copy(supplierCommercial, amendSupplierCommercial);
        beRepriceRq = bookingEngineElasticData.getJSONData(userId, transactionId, sessionId, BEInboundOperation.REPRICE, BEOperationId.BOOKING_ENGINE_RQ, BEServiceUri.FLIGHT);
        beRepriceRq.getJSONObject("requestHeader").put("userID", userService.getLoggedInUserId());
        JSONObject beRepriceRs = bookingEngineElasticData.getJSONData(userId, transactionId, sessionId, BEInboundOperation.REPRICE, BEOperationId.BOOKING_ENGINE_RS, BEServiceUri.FLIGHT);
        logger.trace("Booking Engine Air Reprice Request : " + beRepriceRq.toString());
        siRepriceResponse = bookingEngineElasticData.getXMLData(userId, transactionId, sessionId, BEInboundOperation.REPRICE, BEOperationId.SUPPLIER_INTEGRATION_RS, BEServiceUri.FLIGHT);
        logger.trace("SI Air Reprice Response : " + XMLTransformer.toString(siRepriceResponse));
        int ccommJrnyDtlsJsonIdx = getJourneyDetailsIndex(opsProduct, beRepriceRs);
        opsAmendmentsJson = getAirOpsAmendmentsJson(bookingId, opsProduct, supplierCommercial, siRepriceResponse, ccommJrnyDtlsJsonIdx);
        opsAmendmentsJson.put("bookingDate", DateTimeFormatter.ofPattern("yyyy-MM-dd'T00:00:00'").format(opsBooking.getBookingDateZDT()));
        beRepriceRq.getJSONObject("requestBody").put("opsAmendments", opsAmendmentsJson);
        beResponseJson = consumeBEOpsReprice(beRepriceRq, productSubCategory);
        updatedOpsBooking = beToOpsBookingConverter.updateAirOrder(beRepriceRq, beResponseJson, bookingId, orderId, supplierCommercial.getPaxType());
        updatedOpsOrder = updatedOpsBooking.getProducts().parallelStream().filter(orders -> orders.getOrderID().equals(supplierCommercial.getOrderId())).findAny().get();

        try {
            logger.trace("Updated Air Order : " + objectMapper.writeValueAsString(updatedOpsOrder));
        } catch (JsonProcessingException e2) {
            e2.printStackTrace();
        }

        SupplierCommercialResource revisedSupplierCommercial = null;
        revisedSupplierCommercial = getCommercialResourceForOrder(updatedOpsBooking,updatedOpsOrder, supplierCommercial);
        amendSupplierCommercial.setRevisedSupplierCommercialResource(revisedSupplierCommercial);
        amendSupplierCommercial.setUpdatedOpsOrder(updatedOpsOrder);
        return amendSupplierCommercial;
    }

    private void validateCommercial(AmendSupplierCommercial amendSupplierCommercial) {


        List<ApplyCommercialOn> applyCommercialOnDB = applyCommercialOnRepository.getAllStatus();
        List<BookingIneligibleFor> bookingNotEligibleForDB = bookingNotEligibleForRepository.getAllStatus();
        List<SellingPriceComponent> sellingPriceComponentsDB = sellingPriceComponentRepository.getAllStatus();

        Set<ApplyCommercialOn> applyCommercialOn = null;
        Set<BookingIneligibleFor> bookingNotEligibleFor = null;
        Set<SellingPriceComponent> sellingPriceComponents = null;

        if (amendSupplierCommercial.getApplyProducts() != null) {
            applyCommercialOn = new HashSet<>();
            Iterator<ApplyCommercialOn> applyOnItr = amendSupplierCommercial.getApplyProducts().iterator();
            Boolean matchFound = false;
            while (applyOnItr.hasNext()) {
                ApplyCommercialOn applyOnRQ = applyOnItr.next();
                for (ApplyCommercialOn applyOnDB : applyCommercialOnDB) {
                    if (applyOnRQ.getApplyCommercialOn().equals(applyOnDB.getApplyCommercialOn())) {
                        applyCommercialOn.add(applyOnDB);
                        matchFound = true;
                        break;
                    }
                }
                if (!(matchFound)) {
                    applyCommercialOn.add(applyOnRQ);
                }
            }
            amendSupplierCommercial.setApplyProducts(applyCommercialOn);
        }

        if (amendSupplierCommercial.getBookingNotEligibleFor() != null) {
            bookingNotEligibleFor = new HashSet<>();
            Iterator<BookingIneligibleFor> bookingNotEligibleItr = amendSupplierCommercial.getBookingNotEligibleFor().iterator();

            while (bookingNotEligibleItr.hasNext()) {
                BookingIneligibleFor bookingNotEligibleRQ = bookingNotEligibleItr.next();
                Boolean matchFound = false;
                for (BookingIneligibleFor bookingNotEligibleDB : bookingNotEligibleForDB) {
                    if (bookingNotEligibleRQ.getBookingNotEligibleFor()!=null && bookingNotEligibleRQ.getBookingNotEligibleFor().equals(bookingNotEligibleDB.getBookingNotEligibleFor())) {
                        bookingNotEligibleFor.add(bookingNotEligibleDB);
                        matchFound = true;
                        break;
                    }
                }
                if (!(matchFound)) {
                    bookingNotEligibleFor.add(bookingNotEligibleRQ);
                }
            }
            amendSupplierCommercial.setBookingNotEligibleFor(bookingNotEligibleFor);
        }

        if (amendSupplierCommercial.getSupplierPriceComponents() != null) {
            sellingPriceComponents = new HashSet<>();
            Iterator<SellingPriceComponent> priceComponentItr = amendSupplierCommercial.getSupplierPriceComponents().iterator();
            while (priceComponentItr.hasNext()) {
                SellingPriceComponent priceComponentRQ = priceComponentItr.next();
                Boolean matchFound = false;
                for (SellingPriceComponent priceComponentDB : sellingPriceComponentsDB) {
                    if (priceComponentRQ.getSellingPriceComponent().equals(priceComponentDB.getSellingPriceComponent())) {
                        sellingPriceComponents.add(priceComponentDB);
                        matchFound = true;
                        break;
                    }
                }
                if (!(matchFound)) {
                    sellingPriceComponents.add(priceComponentRQ);
                }
            }
            amendSupplierCommercial.setSupplierPriceComponents(sellingPriceComponents);
        }


    }

    public SupplierCommercialResource getCommercialResourceForOrder(OpsBooking opsBooking,OpsProduct opsProduct , AmendSupplierCommercialResource supplierCommercial) throws OperationException {
    	BigDecimal differenceInAmount=new BigDecimal(0);
    	
        SupplierCommercialResource supplierCommercialResource = new SupplierCommercialResource();
        supplierCommercialResource.setOrderSupplierCommercials(opsProduct.getOrderDetails().getSupplierCommercials());
        supplierCommercialResource.setOrderClientCommercials(opsProduct.getOrderDetails().getClientCommercials());
        OpsOrderDetails opsOrder=opsProduct.getOrderDetails();
        OpsProductSubCategory opsProductSubCategory=opsProduct.getOpsProductSubCategory();
        switch (opsProductSubCategory) {
            case PRODUCT_SUB_CATEGORY_FLIGHT: {
                AirSupplierCommercial airSupplierCommercial = new AirSupplierCommercial();
                airSupplierCommercial.setOpsFlightTotalPriceInfo(opsOrder.getFlightDetails().getTotalPriceInfo());
                airSupplierCommercial.setOpsFlightSupplierCommercials(opsOrder.getSupplierCommercials());
                airSupplierCommercial.setOpsFlightSupplierPriceInfo(opsOrder.getFlightDetails().getOpsFlightSupplierPriceInfo());
                SupplierCommercialPricingDetailResource supplierCommercialPricingDetailResource = calculateMargin(opsBooking,opsProduct);
                airSupplierCommercial.setMargin(supplierCommercialPricingDetailResource.getMargin());
                airSupplierCommercial.setNetPayableToSupplier(supplierCommercialPricingDetailResource.getNetPayableToSupplier());
                airSupplierCommercial.setTotalSellingPrice(supplierCommercialPricingDetailResource.getTotalSellingPrice());
                airSupplierCommercial.setCurrencyCode(supplierCommercialPricingDetailResource.getCurrencyCode());
                differenceInAmount=supplierCommercial.getOldSupplierCommercialResource().getAirSupplierCommercial().getMargin().subtract(supplierCommercialPricingDetailResource.getMargin());
                airSupplierCommercial.setMarginIncreased(differenceInAmount.signum()==-1?true:false);
                
                supplierCommercialResource.setAirSupplierCommercial(airSupplierCommercial);
                break;
            }
            case PRODUCT_SUB_CATEGORY_HOTELS: {

                AccoSupplierCommercial accoSupplierCommercial = new AccoSupplierCommercial();

                accoSupplierCommercial.setOpsAccoOrderSupplierPriceInfo(opsOrder.getHotelDetails().getOpsAccoOrderSupplierPriceInfo());
                accoSupplierCommercial.setOpsOrderSupplierCommercials(opsOrder.getSupplierCommercials());
                accoSupplierCommercial.setOpsAccommodationTotalPriceInfo(opsOrder.getHotelDetails().getOpsAccommodationTotalPriceInfo());
                SupplierCommercialPricingDetailResource supplierCommercialPricingDetailResource = calculateMargin(opsBooking,opsProduct);
                accoSupplierCommercial.setMargin(supplierCommercialPricingDetailResource.getMargin());
                accoSupplierCommercial.setNetPayableToSupplier(supplierCommercialPricingDetailResource.getNetPayableToSupplier());
                accoSupplierCommercial.setTotalSellingPrice(supplierCommercialPricingDetailResource.getTotalSellingPrice());
                accoSupplierCommercial.setCurrencyCode(supplierCommercialPricingDetailResource.getCurrencyCode());
                if (supplierCommercial.getRoomId() != null) {
                    OpsRoom opsRoom = opsOrder.getHotelDetails().getRooms().stream().filter(room -> room.getRoomID().equals(supplierCommercial.getRoomId())).findAny().get();
                    accoSupplierCommercial.setRoom(opsRoom);
                }
                
                differenceInAmount=supplierCommercial.getOldSupplierCommercialResource().getAccoSupplierCommercial().getMargin().subtract(supplierCommercialPricingDetailResource.getMargin());
                accoSupplierCommercial.setMarginIncreased(differenceInAmount.signum()==-1?true:false);
                
                supplierCommercialResource.setAccoSupplierCommercial(accoSupplierCommercial);
                break;
            }

            default:
                break;

        }
        boolean isPassOnOrRetain=differenceInAmount.signum()==-1?true:false;
        supplierCommercialResource.setIsPassOnOrRetain(isPassOnOrRetain);
        
        supplierCommercialResource.setDifferenceInAmount(differenceInAmount.abs());
        
      
        return supplierCommercialResource;
    }

    private JSONObject getAccoOpsAmendmentsJson(String bookingId, OpsProduct opsOrder, AmendSupplierCommercialResource amendSupplierCommercialResource, Element siRepriceRS) throws OperationException {
        JSONObject opsAmendmentsJson = new JSONObject();
        Optional<OpsRoom> opsRoomOpt = opsOrder.getOrderDetails().getHotelDetails().getRooms().parallelStream().filter(room -> room.getRoomID().equals(amendSupplierCommercialResource.getRoomId())).findAny();
        if (!opsRoomOpt.isPresent()) {
            throw new OperationException(Constants.ROOM_NOT_FOUND, amendSupplierCommercialResource.getRoomId());
        }


        String hotelCode = opsOrder.getOrderDetails().getHotelDetails().getHotelCode();
        OpsRoom opsRoom = opsRoomOpt.get();
        @SuppressWarnings("unchecked")
        Map<String, Object> hotelAttrs = RestUtils.getForObject(redisHotelDataUrl + hotelCode, Map.class);
        opsAmendmentsJson.put("siRepriceResponse", XMLTransformer.toString(siRepriceRS));
        opsAmendmentsJson.put("actionItem", "amendSupplierCommercials");
        opsAmendmentsJson.put("supplierId", opsOrder.getSupplierID());
        opsAmendmentsJson.put("hotelName", hotelAttrs.getOrDefault("name", ""));
        opsAmendmentsJson.put("roomTypeName", opsRoom.getRoomTypeInfo().getRoomTypeName());
        opsAmendmentsJson.put("roomCategoryName", opsRoom.getRoomTypeInfo().getRoomCategoryName());
        opsAmendmentsJson.put("ratePlanName", opsRoom.getRatePlanInfo().getRatePlanname());
        opsAmendmentsJson.put("ratePlanCode", opsRoom.getRatePlanInfo().getRatePlanCode());
        opsAmendmentsJson.put("bookingId", bookingId);
        JSONArray ineligibleCommercialsJsonArr = new JSONArray();
        opsAmendmentsJson.put("ineligibleCommercials", ineligibleCommercialsJsonArr);
        Set<BookingIneligibleFor> ineligibleCommercials = amendSupplierCommercialResource.getBookingNotEligibleFor();
        Iterator<BookingIneligibleFor> ineligibleCommercialsItr = ineligibleCommercials.iterator();
        while (ineligibleCommercialsItr.hasNext()) {
            ineligibleCommercialsJsonArr.put("Supplier_"+AmendSupplierCommercialMasterDataLoaderServiceImpl.NonEligibleCommercialHeads.getCommercialHead(ineligibleCommercialsItr.next().getBookingNotEligibleFor()).name());
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
            logger.trace("Booking Engine Reprice Response : " + beRepriceRespJson.toString());
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new OperationException(Constants.BE_OPS_SERVICE_ERROR, url);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new OperationException(Constants.BE_OPS_SERVICE_ERROR, url);
        }
        return beRepriceRespJson;
    }


    private JSONObject getAirOpsAmendmentsJson(String bookingId, OpsProduct opsOrder, AmendSupplierCommercialResource amendSupplierCommercialResource, Element siRepriceRS, int ccommJrnyDtlsJsonIdx) throws OperationException {
        JSONObject opsAmendmentsJson = new JSONObject();

        opsAmendmentsJson.put("siRepriceResponse", XMLTransformer.toString(siRepriceRS));
        opsAmendmentsJson.put("actionItem", "amendSupplierCommercials");
        opsAmendmentsJson.put("supplierId", opsOrder.getSupplierID());
        opsAmendmentsJson.put("paxType", amendSupplierCommercialResource.getPaxType());
        opsAmendmentsJson.put("bookingId", bookingId);
        opsAmendmentsJson.put("journeyDetailsIdx", ccommJrnyDtlsJsonIdx);
        JSONArray ineligibleCommercialsJsonArr = new JSONArray();
        opsAmendmentsJson.put("ineligibleCommercials", ineligibleCommercialsJsonArr);
        Set<BookingIneligibleFor> bookingNotEligibleFor = amendSupplierCommercialResource.getBookingNotEligibleFor();
        Iterator<BookingIneligibleFor> ineligibleCommercialsItr = bookingNotEligibleFor.iterator();
        while (ineligibleCommercialsItr.hasNext()) {
            ineligibleCommercialsJsonArr.put("Supplier_"+AmendSupplierCommercialMasterDataLoaderServiceImpl.NonEligibleCommercialHeads.getCommercialHead(ineligibleCommercialsItr.next().getBookingNotEligibleFor()).name());
        }
        return opsAmendmentsJson;
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

   /* public SupplierCommercialPricingDetailResource calculateMarginForAcco(AccoSupplierCommercial revisedAccoSupplierCommercial) throws OperationException {
        BigDecimal totalSellingPrice = null;
        BigDecimal totalSupplierPrice = null;
        BigDecimal margin = new BigDecimal(0);
        BigDecimal netPayableToSupplier = new BigDecimal(0);
        BigDecimal receivableAmount = new BigDecimal(0);
        BigDecimal payableAmount = new BigDecimal(0);
        String totalPrice = revisedAccoSupplierCommercial.getOpsAccommodationTotalPriceInfo().getTotalPrice();
        String supplierPrice = revisedAccoSupplierCommercial.getOpsAccoOrderSupplierPriceInfo().getSupplierPrice();
        List<OpsOrderSupplierCommercial> opsOrderSupplierCommercials = revisedAccoSupplierCommercial.getOpsOrderSupplierCommercials();
        calculateAllReceivableAndPayable(opsOrderSupplierCommercials, receivableAmount, payableAmount);
        try {
            totalSellingPrice = new BigDecimal(totalPrice);
            totalSupplierPrice = new BigDecimal(supplierPrice);
        } catch (NumberFormatException ex) {
            throw new OperationException("Conversion of string to BigDecimal failed ");
        }
        margin = new BigDecimal(0);
        netPayableToSupplier = new BigDecimal(0);
        netPayableToSupplier = totalSupplierPrice.subtract(receivableAmount).add(payableAmount);
        margin = totalSellingPrice.subtract(netPayableToSupplier);
        revisedAccoSupplierCommercial.setMargin(margin);
        revisedAccoSupplierCommercial.setNetPayableToSupplier(netPayableToSupplier);
        revisedAccoSupplierCommercial.setTotalSellingPrice(totalSellingPrice);
        SupplierCommercialPricingDetailResource supplierCommercialPricingDetailResource = new SupplierCommercialPricingDetailResource();
        supplierCommercialPricingDetailResource.setMargin(margin);
        supplierCommercialPricingDetailResource.setNetPayableToSupplier(netPayableToSupplier);
        supplierCommercialPricingDetailResource.setTotalSellingPrice(totalSellingPrice);
        return supplierCommercialPricingDetailResource;
    }*/

   /* private void calculateAllReceivableAndPayable(List<OpsOrderSupplierCommercial> opsOrderSupplierCommercials, BigDecimal receivableAmount, BigDecimal payableAmount) {
        for (OpsOrderSupplierCommercial opsOrderSupplierCommercial : opsOrderSupplierCommercials) {
            if (opsOrderSupplierCommercial.getCommercialType().equalsIgnoreCase("Receivable")) {
                String commercialAmount = opsOrderSupplierCommercial.getCommercialAmount();
                receivableAmount.add(new BigDecimal(commercialAmount));
            }
            if (opsOrderSupplierCommercial.getCommercialType().equalsIgnoreCase("Payable")) {
                payableAmount.add(new BigDecimal(opsOrderSupplierCommercial.getCommercialAmount()));
            }
        }
    }
*/
   /* public BigDecimal calculateMarginAir(AirSupplierCommercial revisedAirSupplierCommercial) throws OperationException {
        BigDecimal totalSellingPrice = null;
        BigDecimal totalSupplierPrice = null;
        BigDecimal margin = new BigDecimal(0);
        BigDecimal netPayableToSupplier = new BigDecimal(0);
        BigDecimal receivableAmount = new BigDecimal(0);
        BigDecimal payableAmount = new BigDecimal(0);
        String supplierPrice = revisedAirSupplierCommercial.getOpsFlightSupplierPriceInfo().getSupplierPrice();
        String totalPrice = revisedAirSupplierCommercial.getOpsFlightTotalPriceInfo().getTotalPrice();
        List<OpsOrderSupplierCommercial> opsFlightSupplierCommercials = revisedAirSupplierCommercial.getOpsFlightSupplierCommercials();

        calculateAllReceivableAndPayable(opsFlightSupplierCommercials, receivableAmount, payableAmount);
        try {
            totalSellingPrice = new BigDecimal(totalPrice);
            totalSupplierPrice = new BigDecimal(supplierPrice);
        } catch (NumberFormatException ex) {
            throw new OperationException("Conversion of string to BigDecimal failed ");
        }
        margin = new BigDecimal(0);
        netPayableToSupplier = new BigDecimal(0);
        netPayableToSupplier = totalSupplierPrice.subtract(receivableAmount).add(payableAmount);
        margin = totalSellingPrice.subtract(netPayableToSupplier);
        revisedAirSupplierCommercial.setMargin(margin);
        revisedAirSupplierCommercial.setNetPayableToSupplier(netPayableToSupplier);
        revisedAirSupplierCommercial.setTotalSellingPrice(totalSellingPrice);
        return margin;
    }*/

    @Override
    @Transactional(rollbackOn = OperationException.class)
    public String approveOrReject(AmendSupplierCommercialApprovalResource resource) throws OperationException {
    	
    	if (StringUtils.isEmpty(resource.getTodoTaskId())) {
            throw new OperationException(Constants.INVALID_RQ_MISSING_ATTRIBUTE, "ToDo Task ID");
        }
    	
    	ToDoTask toDoTask = toDoTaskRepository.getById(resource.getTodoTaskId());
        
    	if (toDoTask == null) {
            logger.error("ToDoTask not found with id " + resource.getTodoTaskId());
            throw new OperationException("ToDoTask not found with id " + resource.getTodoTaskId());
        }

        
        AmendSupplierCommercial amendSupplierCommercial = null;
        
        amendSupplierCommercial = amendSupplierCommercialRepository.getAmendSupplierCommercial(resource.getAmendSupplierCommercialId());
        
        if (amendSupplierCommercial == null) {
            logger.error("Supplier Commercial Amendment not found with id " + resource.getAmendSupplierCommercialId());
            throw new OperationException("Supplier Commercial Amendment not found with id " + resource.getAmendSupplierCommercialId());
        }
        
        
        String status = null;
        
        SupplierCommercialApproval approvalStatus = resource.getApprovalStatus();
        status = approvalStatus.getStatus();
        
        amendSupplierCommercial.setApprovalStatus(approvalStatus);
        amendSupplierCommercial.setRemark(resource.getRemark());
        
        
        if (approvalStatus.equals(SupplierCommercialApproval.APPROVED)) {
            
            toDoTask.setTaskStatus(ToDoTaskStatusValues.CLOSED);
            toDoTask.setRemark(resource.getRemark());
            
            Invoice invoice = getAndUpdateInvoice(amendSupplierCommercial);//update invoice
            
            //update booking db
            updateRevisedSupplierCommercial(amendSupplierCommercial);
            
            
            //Generate Service Order
        	generateNewServiceOrder(amendSupplierCommercial.getBookingId(),amendSupplierCommercial.getOrderId());
                      
           
            
            
            if(amendSupplierCommercial.getRevisedSupplierCommercialResource().getIsPassOnOrRetain()) {
            	BigDecimal passOnToClientAmount=amendSupplierCommercial.getRevisedSupplierCommercialResource().getPassOnOrChargeClientAmount();
            	BigDecimal retainByCompanyAmount=amendSupplierCommercial.getRevisedSupplierCommercialResource().getRetainOrAbsorbedByCompanyAmount();
            	//TODO  what to do with retain by company amount
            	if(passOnToClientAmount!=null && passOnToClientAmount.signum()==1) {
            		refundToClientOrCustomer(amendSupplierCommercial);
            	}
            	
            	
            }
            
            else {
            	
            	BigDecimal chargeToClientAmount=amendSupplierCommercial.getRevisedSupplierCommercialResource().getPassOnOrChargeClientAmount();
            	BigDecimal absorbedByCompanyAmount=amendSupplierCommercial.getRevisedSupplierCommercialResource().getRetainOrAbsorbedByCompanyAmount();
            	
            	//TODO charged the client, what to do with absorbed by company amount
            	if(chargeToClientAmount!=null && chargeToClientAmount.signum()==1) {
            		createDebitNote(invoice,amendSupplierCommercial);
            	}
            	
            }
            
            //update invoice
        } 
        
        
        else if (approvalStatus.equals(SupplierCommercialApproval.REJECTED)) {
        	
        	toDoTask.setTaskStatus(ToDoTaskStatusValues.REJECTED);
            toDoTask.setRemark(resource.getRemark());

        }
        
        
        toDoTask = toDoTaskRepository.saveOrUpdate(toDoTask);
        amendSupplierCommercial = amendSupplierCommercialRepository.saveAmendSupplierCommercial(amendSupplierCommercial);

        return status + " successfully ";
    }


    private void refundToClientOrCustomer(AmendSupplierCommercial amendSupplierCommercial) throws OperationException {
       	
        RefundResource refundResource = new RefundResource();
        OpsBooking opsBooking = opsBookingService.getBooking(amendSupplierCommercial.getBookingId());
        OpsProduct opsProduct = opsBookingService.getOpsProduct(opsBooking, amendSupplierCommercial.getOrderId());
        
        refundResource.setClientId(amendSupplierCommercial.getClientId());
        refundResource.setClientType(amendSupplierCommercial.getClientTypeId());
        refundResource.setClaimCurrency(opsBooking.getClientCurrency());
        
        refundResource.setBookingReferenceNo(amendSupplierCommercial.getBookingId());
        refundResource.setClientName(opsBooking.getClientID());
        refundResource.setBookingRefId(amendSupplierCommercial.getBookingId());
       
        
        ProductDetail productDetail = new ProductDetail();
        productDetail.setProductSubName(opsProduct.getProductSubCategory());
        productDetail.setProductCategory(opsProduct.getProductCategory());
        productDetail.setProductCategorySubType(opsProduct.getProductSubCategory());
        productDetail.setProductName(opsProduct.getProductName());
        productDetail.setOrderId(amendSupplierCommercial.getOrderId());
        productDetail.setProductName(opsProduct.getProductName());
        refundResource.setProductDetail(productDetail);
        
        OpsPaymentInfo opsPaymentInfo = opsBooking.getPaymentInfo().get(0);
        refundResource.setDefaultModeOfPayment(opsPaymentInfo.getPaymentMethod());// TODO
        refundResource.setRequestedModeOfPayment(opsPaymentInfo.getPaymentMethod());
        //refundResource.setDueOn(ZonedDateTime.now().plusDays(10));//ToDosuggested by manoj
        // refundResource.setNetAmountPayable(amendSupplierCommercial.getRevisedSupplierCommercialResource().getPassOnClientAmount());//ToDo suggested by manoj
        // refundResource.setRefundCutOff(ZonedDateTime.now().plusDays(12));//ToDo suggested by manoj
        
        refundResource.setCreatedByUserId(userService.getLoggedInUserId());
        
        
        refundResource.setReasonForRequest(ReasonForRequest.AMEND_SUPPLIER_COMMERCIALS);
        refundResource.setRefundAmount(amendSupplierCommercial.getRevisedSupplierCommercialResource().getPassOnOrChargeClientAmount());//ToDo
        // refundResource.setRefundProcessedDate(ZonedDateTime.now().plusDays(5));//ToDo
        refundResource.setRefundStatus(RefundStatus.PENDING_WITH_OPS.getStatus());
        
        //ROE is set as ONE because margin is calculated in Client currency, therefore difference in margin will be in client currency
        refundResource.setRoeAsInClaim(BigDecimal.ONE);
        refundResource.setRoeRequested(BigDecimal.ONE);
        refundResource.setRefundType(RefundTypes.REFUND_REDEEMABLE);
        refundService.add(refundResource);

    }


    @Override
    public SupplierCommercialPricingDetailResource calculateMargin(OpsBooking opsBooking,OpsProduct opsProduct) throws OperationException {
        BigDecimal totalSellingPrice;
        BigDecimal totalSupplierPrice;
        BigDecimal margin = new BigDecimal(0);
        BigDecimal netPayableToSupplier = new BigDecimal(0);
        BigDecimal receivableAmounts = new BigDecimal(0);
        BigDecimal payableAmounts = new BigDecimal(0);
        String sourceSupplierId=opsProduct.getSourceSupplierID();
        String totalPrice = null;
        String supplierPrice = null;
        String supplierCurrency = null;
        String clientCurrency = null;
        OpsOrderDetails orderDetails=opsProduct.getOrderDetails();
        OpsProductSubCategory opsProductSubCategory=opsProduct.getOpsProductSubCategory();
        SupplierCommercialPricingDetailResource supplierCommercialPricingDetailResource = new SupplierCommercialPricingDetailResource();
        switch (opsProductSubCategory) {
            case PRODUCT_SUB_CATEGORY_FLIGHT:
                totalPrice = orderDetails.getFlightDetails().getTotalPriceInfo().getTotalPrice();
                supplierPrice = orderDetails.getFlightDetails().getOpsFlightSupplierPriceInfo().getSupplierPrice();
                supplierCurrency = orderDetails.getFlightDetails().getOpsFlightSupplierPriceInfo().getCurrencyCode();
                clientCurrency = orderDetails.getFlightDetails().getTotalPriceInfo().getCurrencyCode();
                break;
            case PRODUCT_SUB_CATEGORY_HOTELS:
                totalPrice = orderDetails.getHotelDetails().getOpsAccommodationTotalPriceInfo().getTotalPrice();
                supplierPrice = orderDetails.getHotelDetails().getOpsAccoOrderSupplierPriceInfo().getSupplierPrice();
                supplierCurrency = orderDetails.getHotelDetails().getOpsAccoOrderSupplierPriceInfo().getCurrencyCode();
                clientCurrency = orderDetails.getHotelDetails().getOpsAccommodationTotalPriceInfo().getCurrencyCode();
                break;
            default:
                break;

        }


        List<OpsOrderSupplierCommercial> supplierCommercials = orderDetails.getSupplierCommercials();
        receivableAmounts = new BigDecimal(0);
        payableAmounts = new BigDecimal(0);
        for (OpsOrderSupplierCommercial opsOrderSupplierCommercial : supplierCommercials) {
        	if(opsOrderSupplierCommercial.getSupplierID()==null) {
        		throw new OperationException("Supplier Id not found for Supplier Commercial- "+opsOrderSupplierCommercial.getCommercialName());
        	}
        	if(opsOrderSupplierCommercial.isEligible()&& opsOrderSupplierCommercial.getSupplierID().equals(sourceSupplierId)) {
        		BigDecimal commercialAmount = new BigDecimal(opsOrderSupplierCommercial.getCommercialAmount());
        		
        	 if (!opsOrderSupplierCommercial.getCommercialCurrency().equalsIgnoreCase(supplierCurrency)) {
        		 String bookingDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(opsBooking.getBookingDateZDT());
                 //TODO get ROE of Commercial Currency and Client Currency from BE API
                 BigDecimal commercialToSupplierROE = null;
                 try {
                     UriComponents getROE = UriComponentsBuilder.fromUriString(getROEUrl).pathSegment(opsOrderSupplierCommercial.getCommercialCurrency()).
                             pathSegment(supplierCurrency).pathSegment(opsBooking.getClientMarket()).pathSegment(bookingDate).build();
                     commercialToSupplierROE = RestUtils.getForObject(getROE.toUriString(), BigDecimal.class);
                 } catch (Exception e) {
                     throw new OperationException("Cannot Get ROE for Commercial");
                 }
                 commercialAmount=commercialAmount.multiply(commercialToSupplierROE);
                 
        	 }
            if (opsOrderSupplierCommercial.getCommercialType().equalsIgnoreCase("Receivable")) {
                
                receivableAmounts = receivableAmounts.add(commercialAmount);
            }
            else if (opsOrderSupplierCommercial.getCommercialType().equalsIgnoreCase("Payable")) {
                payableAmounts = payableAmounts.add(commercialAmount);
            }
        }
        }
        try {
            totalSellingPrice = new BigDecimal(totalPrice);
            totalSupplierPrice = new BigDecimal(supplierPrice);
          /*  if (!supplierCurrency.equals(clientCurrency)) {
                totalSupplierPrice = totalSupplierPrice.multiply(opsProduct.getRoe());
            }*/
        } catch (NumberFormatException ex) {
            throw new OperationException("Conversion of string to BigDecimal failed ");
        }

        margin = new BigDecimal(0);
        netPayableToSupplier = new BigDecimal(0);
        netPayableToSupplier = totalSupplierPrice.subtract(receivableAmounts).add(payableAmounts);
        margin = totalSupplierPrice.subtract(netPayableToSupplier);
        supplierCommercialPricingDetailResource.setTotalSellingPrice(totalSupplierPrice);
        supplierCommercialPricingDetailResource.setNetPayableToSupplier(netPayableToSupplier);
        supplierCommercialPricingDetailResource.setMargin(margin);
        supplierCommercialPricingDetailResource.setCurrencyCode(supplierCurrency);
        return supplierCommercialPricingDetailResource;
    }

    private String updateRevisedSupplierCommercial(AmendSupplierCommercial amendSupplierCommercial) throws OperationException {
		try {
			OpsProduct updatedOpsProduct = amendSupplierCommercial.getUpdatedOpsOrder();
			beDBUpdateService.updateOrderPrice(updatedOpsProduct);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new OperationException(Constants.BOOKING_DB_UPDATE_FAILED);
		}

		return "Price Updated Successfully";
	}

    
    private void createDebitNote(Invoice invoice,AmendSupplierCommercial amendSupplierCommercial) throws OperationException {
    	
    	 try {

             InvoiceParticularsDto invoiceDto = invoice.getInvoiceParticularsDto().stream()
                     .filter(dto -> dto.getOrderID().equalsIgnoreCase(amendSupplierCommercial.getOrderId())).findFirst().get();
             CreditDebitNote creditDebitNote = new CreditDebitNote();
             creditDebitNote.setBookingRefNumber(Collections.singleton(amendSupplierCommercial.getBookingId()));
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


             costHeaderCharge.setCostHeader(ReasonForRequest.AMEND_SUPPLIER_COMMERCIALS.getReason());

             costHeaderCharge.setAmount(amendSupplierCommercial.getRevisedSupplierCommercialResource().getPassOnOrChargeClientAmount().doubleValue());
             //costHeaderCharge.setPassengerQuantity(1);
             costHeaderCharge.setTotalAmount(amendSupplierCommercial.getRevisedSupplierCommercialResource().getPassOnOrChargeClientAmount().doubleValue());

             List<CostHeaderCharge> costHeaderChargesList = new ArrayList<CostHeaderCharge>();
             costHeaderChargesList.add(costHeaderCharge);

             creditDebitNote.setCostHeaderCharges(costHeaderChargesList);

             creditDebitNote.setProductCategory(invoiceDto.getProductCategory());
             creditDebitNote.setProductCategorySubType(invoiceDto.getProductCategorySubType());
             creditDebitNote.setProductName(invoiceDto.getProductName());
             creditDebitNote.setProductSubName(invoiceDto.getProductNameSubType());
             creditDebitNote.setTotalAmount(amendSupplierCommercial.getRevisedSupplierCommercialResource().getPassOnOrChargeClientAmount().doubleValue());
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

    
    private void generateNewServiceOrder(String bookingRefId, String orderId) throws OperationException {
		OpsBooking opsBooking = opsBookingService.getBooking(bookingRefId);
		OpsProduct opsProduct = opsBookingService.getOpsProduct(opsBooking,orderId);
		try {
			serviceOrderAndSupplierLiabilityService.generateServiceOrder(opsProduct,opsBooking,false);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(String.format("Service Order generation failed for Booking ID %s and Order id %s",bookingRefId,orderId));
		}
	}

    private Invoice getAndUpdateInvoice(AmendSupplierCommercial amendSupplierCommercial) throws OperationException {
    	
		String bookingId=amendSupplierCommercial.getBookingId();
		String orderId=amendSupplierCommercial.getOrderId();
		
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
        for (OpsOrderClientCommercial clientCommercial : amendSupplierCommercial.getRevisedSupplierCommercialResource().getOrderClientCommercials()) {
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
            UriComponentsBuilder amendInvoicebuilder = UriComponentsBuilder.fromUriString(updateInvoiceUrl).queryParam("invoiceNumber", invoice.getInvoiceNumber());
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

}
