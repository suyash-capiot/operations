package com.coxandkings.travel.operations.service.changesuppliername.impl;

import com.coxandkings.travel.ext.model.finance.creditdebitnote.CostHeaderCharge;
import com.coxandkings.travel.ext.model.finance.creditdebitnote.CreditDebitNote;
import com.coxandkings.travel.ext.model.finance.invoice.Invoice;
import com.coxandkings.travel.ext.model.finance.invoice.InvoiceParticularsDto;
import com.coxandkings.travel.operations.criteria.changesuppliername.SupplementOnSupplierPriceCriteria;
import com.coxandkings.travel.operations.enums.amendsuppliercommercial.SupplierCommercialApproval;
import com.coxandkings.travel.operations.enums.debitNote.FundType;
import com.coxandkings.travel.operations.enums.debitNote.NotePhase;
import com.coxandkings.travel.operations.enums.debitNote.NoteType;
import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.enums.refund.ReasonForRequest;
import com.coxandkings.travel.operations.enums.refund.RefundStatus;
import com.coxandkings.travel.operations.enums.refund.RefundTypes;
import com.coxandkings.travel.operations.enums.todo.ToDoTaskStatusValues;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.changesuppliername.DiscountOnSupplierPrice;
import com.coxandkings.travel.operations.model.changesuppliername.SupplementOnSupplierPrice;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsPaymentInfo;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.refund.finaceRefund.ProductDetail;
import com.coxandkings.travel.operations.model.todo.ToDoTask;
import com.coxandkings.travel.operations.repository.changesuppliername.DiscountOnSupplierPriceRepository;
import com.coxandkings.travel.operations.repository.changesuppliername.SupplementOnSupplierPriceRepository;
import com.coxandkings.travel.operations.repository.todo.ToDoTaskRepository;
import com.coxandkings.travel.operations.resource.changesuppliername.ChangeSupplierPriceApprovalResource;
import com.coxandkings.travel.operations.resource.refund.RefundResource;
import com.coxandkings.travel.operations.service.bedbservice.acco.AccoBeDBUpdateService;
import com.coxandkings.travel.operations.service.bedbservice.air.AirBeDBUpdateService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.changesuppliername.ChangeSupplierPriceService;
import com.coxandkings.travel.operations.service.refund.RefundService;
import com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.ServiceOrderAndSupplierLiabilityService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.RestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

@Service("changeSupplierPriceServiceImpl")
public class ChangeSupplierPriceServiceImpl implements ChangeSupplierPriceService {
    private static Logger logger = LogManager.getLogger(ChangeSupplierPriceServiceImpl.class);
    @Autowired
    private ToDoTaskService toDoTaskService;
    @Autowired
    private DiscountOnSupplierPriceRepository discountOnSupplierPriceRepository;
    @Autowired
    @Qualifier("SupplementOnSupplierPriceRepositoryImpl")
    private SupplementOnSupplierPriceRepository supplementOnSupplierPriceRepository;
    @Autowired
    private ToDoTaskRepository toDoTaskRepository;
    @Autowired
    @Qualifier("accoBeDBUpdateServiceImpl")
    private AccoBeDBUpdateService accoBeDBUpdateService;
    @Autowired
    @Qualifier("airBeDBUpdateServiceImpl")
    private AirBeDBUpdateService airBeDBUpdateService;
   
    @Autowired
    private OpsBookingService opsBookingService;
    @Autowired
    private RefundService refundService;
    
    /*@Autowired
    private MDMRestUtils mdmRestUtils;*/
    
    @Autowired
    RestUtils restUtils;
    
    
    @Value("${amend-commercials.create.debit_note}")
    private String createDebitNoteUrl;
    
    @Value("${amend-commercials.get.invoice-by-booking-and-order}")
    private String getInvoiceUrl;

    @Value("${amend-commercials.update.invoice}")
    private String updateInvoiceUrl;
    
    @Autowired
	private ServiceOrderAndSupplierLiabilityService serviceOrderAndSupplierLiabilityService;
   

    @Override
    public JSONObject approveOrReject(ChangeSupplierPriceApprovalResource approvalResource) throws OperationException {
        JSONObject jsonObject = new JSONObject();
        try {
        	
            ToDoTask toDoTask=null;
			try {
				toDoTask = toDoTaskService.getById(approvalResource.getTodoId());
			} catch (IOException e) {
				logger.error("ToDoTask not found with id " + approvalResource.getTodoId());
                throw new OperationException("ToDoTask not found with id " + approvalResource.getTodoId());
			}
            
            if (toDoTask == null) {
                logger.error("ToDoTask not found with id " + approvalResource.getTodoId());
                throw new OperationException("ToDoTask not found with id " + approvalResource.getTodoId());
            }
            
            String referenceId = toDoTask.getReferenceId();

            String status = null;
            
           
            ToDoTaskStatusValues toDoStatus;
            if (approvalResource.getApprovalStatus().getStatus().equals(SupplierCommercialApproval.APPROVED.getStatus())) {
                
                toDoStatus = ToDoTaskStatusValues.CLOSED;
                toDoTask.setTaskStatus(toDoStatus);
                toDoTask.setRemark(approvalResource.getRemark());
                
                if (approvalResource.getOperationName().equalsIgnoreCase("Discount")) {
                    DiscountOnSupplierPrice discountOnSupplierPrice = null;
                    discountOnSupplierPrice = discountOnSupplierPriceRepository.getDiscountOnSupplierPriceById(referenceId);
                    if (discountOnSupplierPrice == null) {
                        logger.error("Discount on Supplier Price not found with id " + referenceId);
                        throw new OperationException("Discount on Supplier Price not found with id " + referenceId);
                    }

                    // initiating the refund process
                    String creditNoteId = null;
                    if (discountOnSupplierPrice.getPassToClientAmount() != null && discountOnSupplierPrice.getPassToClientAmount()>0) {
                        creditNoteId = createCreditNote(discountOnSupplierPrice.getBookingId(), discountOnSupplierPrice.getOrderId(), discountOnSupplierPrice.getPassToClientAmount());
                    }
                    updateOrderLevelPriceDueToDiscountOnSupplierPrice(discountOnSupplierPrice, creditNoteId); // where to update the credit note in booking

                }

                if (approvalResource.getOperationName().equalsIgnoreCase("Supplement")) {
                	
                    SupplementOnSupplierPrice supplementOnSupplierPrice = null;
                    supplementOnSupplierPrice = supplementOnSupplierPriceRepository.getSupplementOnSupplierPriceById(referenceId);
                    if (supplementOnSupplierPrice == null) {

                        logger.error("Supplement on Supplier Price not found with id " + referenceId);
                        throw new OperationException("Supplement on Supplier Price not found with id " + referenceId);
                    }
                    supplementOnSupplierPrice.setOpsUserApprovalStatus(true);
                    supplementOnSupplierPrice = supplementOnSupplierPriceRepository.saveSupplementOnSupplierPrice(supplementOnSupplierPrice);
                    processApproveOrRejectRequest(supplementOnSupplierPrice);
                }


                status = "approval";

            } else if (approvalResource.getApprovalStatus().getStatus().equals(SupplierCommercialApproval.REJECTED.getStatus())) {
                status = "rejection";
                toDoStatus = ToDoTaskStatusValues.REJECTED;
                toDoTask.setTaskStatus(toDoStatus);
                toDoTask.setRemark(approvalResource.getRemark());
                // ToDo create Alert for ips user
            }
            
            toDoTask = toDoTaskRepository.saveOrUpdate(toDoTask);

            jsonObject.put("message", "Your Request for " + status + " completed successfully ");

            return jsonObject;

        } catch (JSONException e) {
            e.printStackTrace();
        } 

        return null;
    }

    @Override
    public JSONObject approveClientRequest(String identifier) throws OperationException {
        SupplementOnSupplierPriceCriteria supplementOnSupplierPriceCriteria = new SupplementOnSupplierPriceCriteria();
        supplementOnSupplierPriceCriteria.setIdentifier(identifier);
        JSONObject jsonObject = new JSONObject();

        SupplementOnSupplierPrice supplementOnSupplierPrice = supplementOnSupplierPriceRepository.getByCriteria(supplementOnSupplierPriceCriteria);
        if (supplementOnSupplierPrice == null) {
            throw new OperationException(Constants.LINKED_FAILED);
        }
        supplementOnSupplierPrice.setClientApprovalStatus(true);
        supplementOnSupplierPrice.setIdentifier("");
        supplementOnSupplierPriceRepository.saveSupplementOnSupplierPrice(supplementOnSupplierPrice);
        processApproveOrRejectRequest(supplementOnSupplierPrice);

        return jsonObject.put("message", "You accepted the request");
    }

    @Override
    @Transactional(rollbackFor = OperationException.class)
    public JSONObject rejectClientRequest(String identifier) throws OperationException {
        SupplementOnSupplierPriceCriteria supplementOnSupplierPriceCriteria = new SupplementOnSupplierPriceCriteria();
        supplementOnSupplierPriceCriteria.setIdentifier(identifier);
        JSONObject jsonObject = new JSONObject();
        SupplementOnSupplierPrice supplementOnSupplierPrice = supplementOnSupplierPriceRepository.getByCriteria(supplementOnSupplierPriceCriteria);
        if (supplementOnSupplierPrice == null) {
            throw new OperationException(Constants.LINKED_FAILED);
        }
        supplementOnSupplierPrice.setClientApprovalStatus(false);
        supplementOnSupplierPrice.setIdentifier("");
        supplementOnSupplierPriceRepository.saveSupplementOnSupplierPrice(supplementOnSupplierPrice);
       /* if (supplementOnSupplierPrice.isClientApprovalStatus() && supplementOnSupplierPrice.isOpsUserApprovalStatus()) {
            updateOrderLevelPriceDueToSupplementOnSupplierPrice(supplementOnSupplierPrice);


            return jsonObject.put("message", "your request has been accepted for rejection");
        }*/

        return jsonObject.put("message", "your request has been accepted for rejection");

    }

    @Override
    public SupplementOnSupplierPrice getSupplement(String identifier) throws OperationException {
        SupplementOnSupplierPrice supplementOnSupplierPriceById = supplementOnSupplierPriceRepository.getSupplementOnSupplierPriceById(identifier);
        if (supplementOnSupplierPriceById == null) {
            throw new OperationException(Constants.RECORD_NOT_FOUND, identifier);
        }
        return supplementOnSupplierPriceRepository.getSupplementOnSupplierPriceById(identifier);
    }

    @Override
    public DiscountOnSupplierPrice getDiscount(String identifier) throws OperationException {
        DiscountOnSupplierPrice discountOnSupplierprice = discountOnSupplierPriceRepository.getDiscountOnSupplierPriceById(identifier);
        if (discountOnSupplierprice == null) {
            throw new OperationException(Constants.RECORD_NOT_FOUND, identifier);
        }
        return discountOnSupplierprice;
    }

    private String createCreditNote(String bookingId, String orderId, Double passToClientAmount) throws OperationException {
        OpsBooking opsBooking = null;
        OpsProduct opsProduct = null;
        
            opsBooking = opsBookingService.getBooking(bookingId);
            opsProduct = opsBookingService.getOpsProduct(opsBooking, orderId);
            RefundResource refundResource = new RefundResource();
           /* Optional<String> anyCurrencyOpt = opsProduct.getOrderDetails().getClientCommercials().stream().map(opsOrderClientCommercial -> opsOrderClientCommercial.getCommercialCurrency()).findAny();
            refundResource.setBookingReferenceNo(bookingId);//m
            if (anyCurrencyOpt.isPresent()) {
                refundResource.setClaimCurrency(anyCurrencyOpt.get());// todo
            }*/


            refundResource.setBookingReferenceNo(bookingId);
            refundResource.setBookingRefId(bookingId);
            refundResource.setClaimCurrency(opsBooking.getClientCurrency());
            refundResource.setClientId(opsBooking.getClientID());
            refundResource.setClientName(opsBooking.getClientID());
            refundResource.setClientType(opsBooking.getClientType());
            ProductDetail productDetail = new ProductDetail();
            productDetail.setProductSubName(opsProduct.getProductSubCategory());
            productDetail.setProductCategorySubType(opsProduct.getProductSubCategory());
            productDetail.setProductCategory(opsProduct.getProductCategory());
            productDetail.setProductName(opsProduct.getProductName());
            productDetail.setOrderId(orderId);
            productDetail.setProductName(opsProduct.getProductName());
            refundResource.setProductDetail(productDetail);
            OpsPaymentInfo opsPaymentInfo = opsBooking.getPaymentInfo().get(0);
            refundResource.setDefaultModeOfPayment(opsPaymentInfo.getPaymentMethod());// TODO
            refundResource.setRequestedModeOfPayment(opsPaymentInfo.getPaymentMethod());
            //refundResource.setDueOn(ZonedDateTime.now().plusDays(10));//ToDosuggested by manoj
            // refundResource.setNetAmountPayable(amendSupplierCommercial.getRevisedSupplierCommercialResource().getPassOnClientAmount());//ToDo suggested by manoj
            // refundResource.setRefundCutOff(ZonedDateTime.now().plusDays(12));//ToDo suggested by manoj
            refundResource.setReasonForRequest(ReasonForRequest.DISCOUNT_SUPPLIER_PRICE);
            refundResource.setRefundAmount(new BigDecimal(passToClientAmount));//ToDo
            // refundResource.setRefundProcessedDate(ZonedDateTime.now().plusDays(5));//ToDo
            refundResource.setRefundStatus(RefundStatus.PENDING_WITH_OPS.getStatus());
            //ROE is set as ONE because margin is calculated in Client currency, therefore difference in margin will be in client currency
            refundResource.setRoeAsInClaim(BigDecimal.ONE);
            refundResource.setRoeRequested(BigDecimal.ONE);
            // refundResource.setRoeRequested(new BigDecimal(200));//ToDo
            refundResource.setRefundType(RefundTypes.REFUND_REDEEMABLE);
            return refundService.add(refundResource).getCreditNoteNo();
           
    }


    private String updateOrderLevelPriceDueToSupplementOnSupplierPrice(SupplementOnSupplierPrice supplementOnSupplierPrice) throws OperationException {
        String result = null;
        OpsProduct opsProduct = supplementOnSupplierPrice.getNewOpsProduct();
        OpsProductCategory opsProductCategory = OpsProductCategory.getProductCategory(opsProduct.getProductCategory());
        OpsProductSubCategory opsProductSubCategory = OpsProductSubCategory.getProductSubCategory(opsProductCategory, opsProduct.getProductSubCategory());
        switch (opsProductSubCategory) {
            case PRODUCT_SUB_CATEGORY_HOTELS:
            	result = accoBeDBUpdateService.updateOrderPrice(opsProduct);
                break;
            case PRODUCT_SUB_CATEGORY_FLIGHT:
                result = airBeDBUpdateService.updateOrderPrice(opsProduct);
            	break;
		default:
			break;
        }

        return result;
    }

    private String updateOrderLevelPriceDueToDiscountOnSupplierPrice(DiscountOnSupplierPrice discountOnSupplierPrice, String creditNoteId) throws OperationException {
        String result = null;
        OpsProduct opsProduct = discountOnSupplierPrice.getNewOpsProduct();
        OpsProductCategory opsProductCategory = OpsProductCategory.getProductCategory(opsProduct.getProductCategory());
        OpsProductSubCategory opsProductSubCategory = OpsProductSubCategory.getProductSubCategory(opsProductCategory, opsProduct.getProductSubCategory());
        switch (opsProductSubCategory) {
            case PRODUCT_SUB_CATEGORY_HOTELS:
                result = accoBeDBUpdateService.updateOrderPrice(opsProduct);
                break;
            case PRODUCT_SUB_CATEGORY_FLIGHT:
                result = airBeDBUpdateService.updateOrderPrice(opsProduct);
            	break;
		default:
			break;
        }

        return result;
    }
    
    
    private void processApproveOrRejectRequest(SupplementOnSupplierPrice supplementOnSupplierPrice) throws OperationException {
		boolean bookingUpdated=false;
    	
    	// case when both client and company both bear supplement money
		if (supplementOnSupplierPrice.getPassToClientAmount() > 0
				&& supplementOnSupplierPrice.getRetainByCompanyAmount() > 0) {
			if (supplementOnSupplierPrice.isClientApprovalStatus()
					&& supplementOnSupplierPrice.isOpsUserApprovalStatus()) {
				updateOrderLevelPriceDueToSupplementOnSupplierPrice(supplementOnSupplierPrice);
				Invoice invoice=getAndUpdateInvoice(supplementOnSupplierPrice);
				createDebitNote(invoice, supplementOnSupplierPrice);
				bookingUpdated=true;
			}
		} else if (supplementOnSupplierPrice.getPassToClientAmount() > 0) { // case when only client bear supplement
																			// money
			if (supplementOnSupplierPrice.isClientApprovalStatus()) {
				updateOrderLevelPriceDueToSupplementOnSupplierPrice(supplementOnSupplierPrice);
				Invoice invoice=getAndUpdateInvoice(supplementOnSupplierPrice);
				createDebitNote(invoice, supplementOnSupplierPrice);
				bookingUpdated=true;
			}
		} else if (supplementOnSupplierPrice.getRetainByCompanyAmount() > 0) { // case when only company bear supplement
																				// money
			if (supplementOnSupplierPrice.isOpsUserApprovalStatus()) {
				updateOrderLevelPriceDueToSupplementOnSupplierPrice(supplementOnSupplierPrice);
				bookingUpdated=true;
			}
		}
		
		if(bookingUpdated) {
			   generateNewServiceOrder(supplementOnSupplierPrice.getBookingId(),supplementOnSupplierPrice.getOrderId());
		}
	}
    
    private void createDebitNote(Invoice invoice,SupplementOnSupplierPrice supplementOnSupplierPrice) throws OperationException {
    	
   	 try {

            InvoiceParticularsDto invoiceDto = invoice.getInvoiceParticularsDto().stream()
                    .filter(dto -> dto.getOrderID().equalsIgnoreCase(supplementOnSupplierPrice.getOrderId())).findFirst().get();
            CreditDebitNote creditDebitNote = new CreditDebitNote();
            creditDebitNote.setBookingRefNumber(Collections.singleton(supplementOnSupplierPrice.getBookingId()));
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


            costHeaderCharge.setCostHeader(ReasonForRequest.SUPPLEMENT_SUPPLIER_PRICE.getReason());

            costHeaderCharge.setAmount(supplementOnSupplierPrice.getPassToClientAmount().doubleValue());
            //costHeaderCharge.setPassengerQuantity(1);
            costHeaderCharge.setTotalAmount(supplementOnSupplierPrice.getPassToClientAmount().doubleValue());

            List<CostHeaderCharge> costHeaderChargesList = new ArrayList<CostHeaderCharge>();
            costHeaderChargesList.add(costHeaderCharge);

            creditDebitNote.setCostHeaderCharges(costHeaderChargesList);

            creditDebitNote.setProductCategory(invoiceDto.getProductCategory());
            creditDebitNote.setProductCategorySubType(invoiceDto.getProductCategorySubType());
            creditDebitNote.setProductName(invoiceDto.getProductName());
            creditDebitNote.setProductSubName(invoiceDto.getProductNameSubType());
            creditDebitNote.setTotalAmount(supplementOnSupplierPrice.getPassToClientAmount().doubleValue());
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
			serviceOrderAndSupplierLiabilityService.generateServiceOrder(opsProduct, opsBooking,false);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(String.format("Service Order generation failed for Booking ID %s and Order id %s",bookingRefId,orderId));
		}
	}

    private Invoice getAndUpdateInvoice(SupplementOnSupplierPrice supplementOnSupplierPrice) throws OperationException {
    	
		String bookingId=supplementOnSupplierPrice.getBookingId();
		String orderId=supplementOnSupplierPrice.getOrderId();
		
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

        //TODO is Invoice update required
       /* Invoice updatedInvoice = null;
        try {
            UriComponentsBuilder amendInvoicebuilder = UriComponentsBuilder.fromUriString(updateInvoiceUrl).queryParam("invoiceNumber", invoice.getInvoiceNumber());
            ResponseEntity<Invoice> amendedInvoice = mdmRestUtils.exchange(amendInvoicebuilder.build().encode().toUri(),
                    HttpMethod.PUT, objectMapper.writeValueAsString(invoice), Invoice.class);
            updatedInvoice = amendedInvoice.getBody();
            logger.info("Updated Invoice:" + objectMapper.writeValueAsString(updatedInvoice));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new OperationException(Constants.FINANCE_INVOICE_UPDATE_FAILED);
        }
        return updatedInvoice;*/
        return invoice;
    } 

}
