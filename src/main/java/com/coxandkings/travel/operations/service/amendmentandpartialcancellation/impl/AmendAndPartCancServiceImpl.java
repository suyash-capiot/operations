package com.coxandkings.travel.operations.service.amendmentandpartialcancellation.impl;

import com.coxandkings.travel.ext.model.be.AmendmentChargesBE;
import com.coxandkings.travel.ext.model.finance.Enums.FundType;
import com.coxandkings.travel.ext.model.finance.Enums.NoteType;
import com.coxandkings.travel.ext.model.finance.creditdebitnote.CostHeaderCharge;
import com.coxandkings.travel.ext.model.finance.creditdebitnote.CreditDebitNote;
import com.coxandkings.travel.ext.model.finance.invoice.Invoice;
import com.coxandkings.travel.ext.model.finance.invoice.InvoiceParticularsDto;
import com.coxandkings.travel.ext.model.finance.invoice.TaxDetail;
import com.coxandkings.travel.operations.enums.amendmentandpartialcancellation.ActionType;
import com.coxandkings.travel.operations.enums.amendmentandpartialcancellation.OpsApprovalStatus;
import com.coxandkings.travel.operations.enums.debitNote.NotePhase;
import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.enums.refund.ReasonForRequest;
import com.coxandkings.travel.operations.enums.refund.RefundStatus;
import com.coxandkings.travel.operations.enums.todo.*;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.amendmentandpartialcancellation.AmendAndPartCanc;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.model.refund.finaceRefund.ProductDetail;
import com.coxandkings.travel.operations.repository.amendmentandpartialcancellation.CompChgAmendAndPartCancRepository;
import com.coxandkings.travel.operations.resource.ErrorResponseResource;
import com.coxandkings.travel.operations.resource.amendmentandpartialcancellation.AmendAndpartCancResource;
import com.coxandkings.travel.operations.resource.amendmentandpartialcancellation.AmendmentDetailsResource;
import com.coxandkings.travel.operations.resource.refund.RefundResource;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResource;
import com.coxandkings.travel.operations.service.amendmentandpartialcancellation.AmendAndPartCancService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.refund.RefundService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.*;
import com.coxandkings.travel.operations.utils.supplier.SupplierDetailsService;
import com.coxandkings.travel.operations.utils.taxengine.TaxEngineUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AmendAndPartCancServiceImpl implements AmendAndPartCancService {

    private final Logger logger = LogManager.getLogger(AmendAndPartCancServiceImpl.class);

    @Autowired
    private CompChgAmendAndPartCancRepository compChgAmendAndPartCancRepository;

    @Autowired
    private ToDoTaskService toDoTaskService;

    @Autowired
    private OpsBookingService opsBookingService;

    @Value("${amendmentAndPartialCancellation.invoiceCheck}")
    private String invoiceCheckAPIUrl;

    @Value("${amendmentAndPartialCancellation.invoiceUpdate}")
    private String invoiceUpdateAPIUrl;

    @Autowired
    private RefundService refundService;

    @Autowired
    private MessageSource messageSource;

    @Value("${amendmentAndPartialCancellation.debitNote}")
    private String creditDebitMemoAPIUrl;

    @Autowired
    private UserService userService;

    @Value("${amendmentAndPartialCancellation.approved}")
    private String suppApprovalUrl;

    @Value("${amendmentAndPartialCancellation.rejected}")
    private String suppRejectionUrl;

    @Value("${amendmentAndPartialCancellation.bookingEngine.compSuppChrgAmendUpdation}")
    private String compSuppChrgUpdateUrl;

    @Autowired
    private EmailUtils emailUtils;

    @Value("${amendmentAndPartialCancellation.template_config.function}")
    private String function;

    @Value("${amendmentAndPartialCancellation.template_config.scenario}")
    private String scenario;

    @Value("${amendmentAndPartialCancellation.template_config.subject}")
    private String subject;

    @Autowired
    private TaxEngineUtils taxEngineUtils;

    @Autowired
    private SupplierDetailsService supplierDetailsService;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Value("${amendmentAndPartialCancellation.bookingEngine.addNewRecordInAMCLTable}")
    private String addNewRecInAMCLUrl;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean saveAmendAndPartCanc(AmendAndpartCancResource amendAndpartCancResource) throws OperationException {
        logger.info("-- Entering AmendAndPartCancServiceImpl.saveAmendAndPartCanc ");

        List<AmendAndPartCanc> amendAndPartCancList = new ArrayList<AmendAndPartCanc>();

        try {
            logger.info("-- Setting values to model class list : amendAndPartCancList ");
            setResourceToModel(amendAndpartCancResource, amendAndPartCancList);
        } catch (Exception e) {
            logger.error("Exception :", e);
            throw new OperationException(Constants.TECHNICAL_ISSUE_SET_MODEL);
        }

        if (!compChgAmendAndPartCancRepository.saveCompanyChargesAmendment(amendAndPartCancList)) {
            return false;// If there is any problem occurred while database storage
        } else {
            List<AmendAndPartCanc> compChrgList = amendAndPartCancList.stream().filter(obj -> obj.getActionType().equalsIgnoreCase(ActionType.COMPANY_CHARGES_AMENDMENT.toString())
                    || obj.getActionType().equalsIgnoreCase(ActionType.COMPANY_CHARGES_CANCELLATION.toString())).collect(Collectors.toList());

            List<AmendAndPartCanc> suppChrgList = amendAndPartCancList.stream().filter(obj -> obj.getActionType().equalsIgnoreCase(ActionType.SUPPLIER_CHARGES_AMENDMENT.toString())
                    || obj.getActionType().equalsIgnoreCase(ActionType.SUPPLIER_CHARGES_CANCELLATION.toString())).collect(Collectors.toList());

            if (null != compChrgList && compChrgList.size() != 0) {
                logger.info("-- Creating TODO task for company charges amendment/cancellation ");
                createToDoTaskForApprovalUser(compChrgList.stream().filter(obj -> obj.getNewValue() < obj.getOldValue()).collect(Collectors.toList()));
                logger.info("-- Creating credit/debit note for company charges amendment/cancellation ");
                createDebitNote(compChrgList.stream().filter(obj -> obj.getOldValue() < obj.getNewValue()).collect(Collectors.toList()), true, false);
            }

            if (null != suppChrgList && suppChrgList.size() != 0) {
                logger.info("-- Creating TODO task for supplier charges amendment/cancellation ");
                createToDoTaskForApprovalUser(suppChrgList);
            }
        }
        logger.info("-- Exiting AmendAndPartCancServiceImpl.saveAmendAndPartCanc ");
        return true;
    }

    private void setResourceToModel(AmendAndpartCancResource amendAndpartCancResource, List<AmendAndPartCanc> amendAndPartCancList) {
        AmendAndPartCanc amendAndPartCanc = null;
        String taskRefID = UUID.randomUUID().toString();

        for (AmendmentDetailsResource amendmentDetailsResource : amendAndpartCancResource.getAmendmentDetails()) {
            amendAndPartCanc = new AmendAndPartCanc();
            CopyUtils.copy(amendmentDetailsResource, amendAndPartCanc);

            if (amendAndPartCanc.getActionType().equalsIgnoreCase(ActionType.COMPANY_CHARGES_AMENDMENT.toString()) ||
                    amendAndPartCanc.getActionType().equalsIgnoreCase(ActionType.COMPANY_CHARGES_CANCELLATION.toString())) {
                if (amendAndPartCanc.getNewValue() < amendAndPartCanc.getOldValue()) {
                    amendAndPartCanc.setApprovalUserStatus(OpsApprovalStatus.APPROVAL_PENDING.toString());
                    amendAndPartCanc.setTaskRefID(taskRefID);
                } else {
                    // INFO: If updated company amendment charges are greater than existing then approval is not required.
                    amendAndPartCanc.setApprovalUserStatus(OpsApprovalStatus.AUTO_APPROVED.toString());
                }
            } else {
                // INFO: If there is supplier charges amendment, Supplier approval is required.
                amendAndPartCanc.setApprovalUserStatus(OpsApprovalStatus.APPROVAL_PENDING.toString());
                amendAndPartCanc.setTaskRefID(taskRefID);
            }

            amendAndPartCanc.setCreatedTime(Calendar.getInstance().getTimeInMillis());
            amendAndPartCanc.setRemark(amendAndpartCancResource.getRemark());

            amendAndPartCancList.add(amendAndPartCanc);
        }
    }

    private void createToDoTaskForApprovalUser(List<AmendAndPartCanc> amendedChrgList) throws OperationException {
        logger.info("-- Entering AmendAndPartCancServiceImpl.createToDoTaskForApprovalUser ");

        for (AmendAndPartCanc amendAndPartCanc : amendedChrgList) {
            logger.info("-- Setting values of TODO task for amend/canc : " + amendAndPartCanc.getId());
            ToDoTaskResource toDoTaskResource = new ToDoTaskResource();
            OpsBooking booking = opsBookingService.getBooking(amendAndPartCanc.getBookingRefNo());

            toDoTaskResource.setCompanyId(booking.getCompanyId());
            toDoTaskResource.setReferenceId(amendAndPartCanc.getTaskRefID());
            toDoTaskResource.setBookingRefId(amendAndPartCanc.getBookingRefNo());
            toDoTaskResource.setClientId(amendAndPartCanc.getClientID());
            toDoTaskResource.setTaskNameId(ToDoTaskNameValues.APPROVE.getValue());
            toDoTaskResource.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());

            if (amendAndPartCanc.getActionType().equalsIgnoreCase(ActionType.COMPANY_CHARGES_AMENDMENT.toString())) {
                toDoTaskResource.setTaskSubTypeId(ActionType.COMPANY_CHARGES_AMENDMENT.toString());
            } else if (amendAndPartCanc.getActionType().equalsIgnoreCase(ActionType.COMPANY_CHARGES_CANCELLATION.toString())) {
                toDoTaskResource.setTaskSubTypeId(ActionType.COMPANY_CHARGES_CANCELLATION.toString());
            } else if (amendAndPartCanc.getActionType().equalsIgnoreCase(ActionType.SUPPLIER_CHARGES_AMENDMENT.toString())) {
                toDoTaskResource.setTaskSubTypeId(ActionType.SUPPLIER_CHARGES_AMENDMENT.toString());
            } else if (amendAndPartCanc.getActionType().equalsIgnoreCase(ActionType.SUPPLIER_CHARGES_CANCELLATION.toString())) {
                toDoTaskResource.setTaskSubTypeId(ActionType.SUPPLIER_CHARGES_CANCELLATION.toString());
            }

            toDoTaskResource.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());
            toDoTaskResource.setTaskStatusId(ToDoTaskStatusValues.NEW.getValue());
            toDoTaskResource.setTaskPriorityId(ToDoTaskPriorityValues.MEDIUM.getValue());
            toDoTaskResource.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.toString());
            toDoTaskResource.setCreatedByUserId(userService.getLoggedInUserId());

            toDoTaskResource.setTaskOrientedTypeId(ToDoTaskOrientedValues.APPROVAL_ORIENTED.getValue());

            try {
                logger.info("-- Calling toDoTaskService.save method ");
                toDoTaskService.save(toDoTaskResource);
            } catch (Exception e) {
                logger.error("Exception :", e);
                throw new OperationException(Constants.TODO_TASK_CREATION_FAIL);
            }
            break;
        }
        logger.info("-- Exiting AmendAndPartCancServiceImpl.createToDoTaskForApprovalUser ");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ErrorResponseResource approveRejectCompanyChargesAmendment(String requestJSON) throws OperationException {
        logger.info("-- Entering AmendAndPartCancServiceImpl.approveRejectCompanyChargesAmendment ");
        int noOfRowsAffected = 0;
        String taskRefId = null, approvalStatus = null, remark = null;
        List<AmendAndPartCanc> amendAndPartCancList = null, companyChgList = null, supplierChgList = null;

        try {
            JSONObject requestObj = new JSONObject(requestJSON);

            taskRefId = requestObj.getString(Constants.JSON_STR_TASK_ID);
            approvalStatus = requestObj.getString(Constants.JSON_STR_BOOK_APPROVAL_STATUS);
            remark = requestObj.getString(Constants.JSON_STR_REMARK);

            noOfRowsAffected = compChgAmendAndPartCancRepository.approveRejectCompanyChargesAmendment(taskRefId, approvalStatus, remark);
            logger.info("-- No. of amendment/cancellations records updated by approver user: " + noOfRowsAffected);

        } catch (Exception e) {
            logger.error("Exception :", e);
            throw new OperationException(Constants.TECHNICAL_ISSUE_APPROVE_REJECT_AMENDMENT);
        }

        if (approvalStatus.equalsIgnoreCase(Constants.APPROVER_APPROVED_STATUS)) {
            amendAndPartCancList = compChgAmendAndPartCancRepository.getAmendmentDetailsByTaskRefId(taskRefId);

            companyChgList = amendAndPartCancList.stream().filter(obj -> obj.getActionType().equalsIgnoreCase(ActionType.COMPANY_CHARGES_AMENDMENT.toString())
                    || obj.getActionType().equalsIgnoreCase(ActionType.COMPANY_CHARGES_CANCELLATION.toString())).collect(Collectors.toList());

            supplierChgList = amendAndPartCancList.stream().filter(obj -> obj.getActionType().equalsIgnoreCase(ActionType.SUPPLIER_CHARGES_AMENDMENT.toString())
                    || obj.getActionType().equalsIgnoreCase(ActionType.SUPPLIER_CHARGES_CANCELLATION.toString())).collect(Collectors.toList());

            if (null != supplierChgList && supplierChgList.size() != 0) {
                logger.info("-- Sending email to supplier after getting approval from Approver user ");
                updateBooking(supplierChgList);
                sendEmailsToSupplier(supplierChgList);
            }

            if (null != companyChgList && companyChgList.size() > 0) {
                /**
                 * INFO:  Below if condition is to check whether tour is started or not. If not, then just need to create new performa/final invoice, credit note and refund claim generation
                 * are handled in invoice generation/updation by finance team. So no need to do separate call to create credit note.
                 * If tour is started then only credit note is need to generate and it's handled in refund service. This method also handled credit note and refund claim generation.
                 */
                logger.info("-- Checking whether tour is started or not ");
                if (checkWhetherTourStartedOrNot(companyChgList.get(0).getBookingRefNo())) {
                    try {
                        logger.info("-- Updating invoice for company charges ");
                        updateInvoice(companyChgList, true);
                    } catch (HttpClientErrorException e) {
                        logger.error("Exception :", e);
                        throw new OperationException(Constants.INVOICE_NOT_FOUND);
                    }
                } else {
                    try {
                        /**
                         * INFO: If tour is started then need to generate debit note only.
                         */
                        logger.info("-- Creating debit note as tour has been already started");
                        createCreditNote(companyChgList, true);
                    } catch (OperationException e) {
                        logger.error("Exception :", e);
                        throw new OperationException(Constants.TECHNICAL_ISSUE_CREDIT_NOTE_CREATE);
                    }
                }
                return updateBooking(companyChgList);
            }
        }

        logger.info("-- Exiting AmendAndPartCancServiceImpl.approveRejectCompanyChargesAmendment ");

        return noOfRowsAffected > 0 ? approvalStatus.equalsIgnoreCase(Constants.APPROVER_APPROVED_STATUS) ?
                getMessageToUser(messageSource.getMessage(Constants.AMENDMENT_APPROVED, new Object[0], Locale.US), HttpStatus.OK)
                : getMessageToUser(messageSource.getMessage(Constants.AMENDMENT_REJECTED, new Object[0], Locale.US), HttpStatus.OK)
                : getMessageToUser(messageSource.getMessage(Constants.AMENDMENT_NOT_FOUND, new Object[0], Locale.US), HttpStatus.NOT_FOUND);
    }

    private boolean checkWhetherTourStartedOrNot(String bookingRefNo) throws OperationException {
        logger.info("-- Entering AmendAndPartCancServiceImpl.checkWhetherTourStartedOrNot ");
        ZonedDateTime tourStartDate = null;

        try {
            if (null != bookingRefNo) {
                logger.info("-- Getting booking details of booking # " + bookingRefNo);
                OpsBooking opsBooking = opsBookingService.getBooking(bookingRefNo);
                List<OpsProduct> productList = opsBooking.getProducts();

                for (OpsProduct opsProduct : productList) {
                    switch (opsProduct.getProductSubCategory()) {
                        case Constants.PROD_CAT_SUB_FLIGHT:
                            List<OpsOriginDestinationOption> odoList = opsProduct.getOrderDetails().getFlightDetails().getOriginDestinationOptions();
                            for (OpsOriginDestinationOption odo : odoList) {
                                List<OpsFlightSegment> flightSegmentList = odo.getFlightSegment();
                                for (OpsFlightSegment flightSegment : flightSegmentList) {
                                    if (null == tourStartDate) {
                                        tourStartDate = flightSegment.getDepartureDateZDT();
                                    } else {
                                        if (tourStartDate.isAfter(flightSegment.getDepartureDateZDT())) {
                                            tourStartDate = flightSegment.getDepartureDateZDT();
                                        }
                                    }
                                }
                            }
                            break;
                        case Constants.PROD_CAT_SUB_HOTEL:
                            List<OpsRoom> roomsList = opsProduct.getOrderDetails().getHotelDetails().getRooms();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.ROOM_DATE_FORMAT);
                            LocalDate localDate = null;
                            ZonedDateTime dbZonedDateTime = null;

                            for (OpsRoom room : roomsList) {
                                localDate = LocalDate.parse(room.getCheckIn(), formatter);
                                dbZonedDateTime = ZonedDateTime.of(localDate, LocalTime.MIDNIGHT, ZoneId.of(Constants.ROOM_ZONE_ID));

                                if (null == tourStartDate) {
                                    tourStartDate = dbZonedDateTime;
                                } else {
                                    if (tourStartDate.isAfter(dbZonedDateTime)) {
                                        tourStartDate = dbZonedDateTime;
                                    }
                                }
                            }
                            break;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception :", e);
            throw new OperationException(Constants.TECHNICAL_ISSUE_TOUR_STARTED);
        }
        logger.info("-- Exiting AmendAndPartCancServiceImpl.checkWhetherTourStartedOrNot ");
        return tourStartDate.isAfter(ZonedDateTime.now());
    }

    private void createCreditNote(List<AmendAndPartCanc> amendAndPartCancList, boolean isCompanyChargesAmendment) throws OperationException {
        logger.info("-- Entering AmendAndPartCancServiceImpl.createCreditNote ");

        RefundResource refundResource = null;
        ProductDetail productDetail = null;
        if (null != amendAndPartCancList && amendAndPartCancList.size() != 0) {
            if (isCompanyChargesAmendment) {
                for (AmendAndPartCanc amendAndPartCanc : amendAndPartCancList) {
                    refundResource = new RefundResource();
                    refundResource.setClientId(amendAndPartCanc.getClientID());
                    refundResource.setRefundAmount(new BigDecimal(amendAndPartCanc.getOldValue() - amendAndPartCanc.getNewValue()));
                    refundResource.setRefundStatus(RefundStatus.PENDING_WITH_OPS.getStatus());
                    refundResource.setReasonForRequest(ReasonForRequest.AMENDMENTS);

                    productDetail = new ProductDetail();
                    productDetail.setOrderId(amendAndPartCanc.getOrderID());
                    productDetail.setProductCategory(amendAndPartCanc.getProductCategory());
                    productDetail.setProductCategorySubType(amendAndPartCanc.getProductSubCategory());
                    productDetail.setProductName(amendAndPartCanc.getProductName());

                    refundResource.setProductDetail(productDetail);
                    logger.info("-- Calling refundService.add to generate credit note ");
                    refundService.add(refundResource);
                }
            } else {
                logger.info("-- Creating credit note for supplier charges amendment/cancellation ");
                createDebitNote(amendAndPartCancList, false, true);
            }
        }
        logger.info("-- Exiting AmendAndPartCancServiceImpl.createCreditNote ");
    }

    private void createDebitNote(List<AmendAndPartCanc> compChrgList, boolean isCreatingDebitNoteAtStart, boolean isCreatingCreditNoteForSupplierCharges) throws OperationException {
        logger.info("-- Entering AmendAndPartCancServiceImpl.createDebitNote ");
        CreditDebitNote creditDebitNote = null;
        Invoice invoiceToCheck = null;
        InvoiceParticularsDto dtoForValue = null;
        List<CostHeaderCharge> costHeaderChargesList = null;
        CostHeaderCharge costHeaderCharge = null;

        try {
            if (null != compChrgList && compChrgList.size() != 0) {
                invoiceToCheck = getInvoicesByAmendAndPartCanc(compChrgList.get(0));
                logger.info("-- Getting invoice details to verify : " + invoiceToCheck.getInvoiceNumber());
                for (AmendAndPartCanc amendAndPartCanc : compChrgList) {
                    dtoForValue = invoiceToCheck.getInvoiceParticularsDto().stream()
                            .filter(dto -> dto.getOrderID().equalsIgnoreCase(amendAndPartCanc.getOrderID())).findFirst().orElse(null);

                    if (null != dtoForValue) {
                        creditDebitNote = new CreditDebitNote();
                        creditDebitNote.setBookingRefNumber(Collections.singleton(amendAndPartCanc.getBookingRefNo()));
                        creditDebitNote.setInvoiceNumber(invoiceToCheck.getInvoiceNumber());
                        creditDebitNote.setTypeOfInvoice(invoiceToCheck.getInvoiceType());
                        creditDebitNote.setNoteType(isCreatingCreditNoteForSupplierCharges ? NoteType.CREDIT.toString() : NoteType.DEBIT.toString());
                        creditDebitNote.setNotePhase(NotePhase.Final.toString());
                        creditDebitNote.setClient_supplierID(amendAndPartCanc.getSupplierID());
                        creditDebitNote.setClientType(invoiceToCheck.getClient().getClientType());
                        creditDebitNote.setIssuedDate(Calendar.getInstance().getTimeInMillis());
                        creditDebitNote.setIssuedTo("CLIENT");
                        creditDebitNote.setClient_supplierName(invoiceToCheck.getClient().getClientName());
                        creditDebitNote.setStatus("APPROVED");
                        creditDebitNote.setTransactionDate(Calendar.getInstance().getTimeInMillis());
                        creditDebitNote.setFundType(FundType.REDEEMABLE.toString());
                        creditDebitNote.setTaxApplicable(false);
                        creditDebitNote.setTaxType("NA");
                        creditDebitNote.setConsumed(false);

                        costHeaderCharge = new CostHeaderCharge();
                        costHeaderCharge.setCostHeader(ReasonForRequest.AMENDMENTS.getReason());
                        costHeaderCharge.setAmount(isCreatingCreditNoteForSupplierCharges ? amendAndPartCanc.getOldValue() - amendAndPartCanc.getNewValue() :
                                amendAndPartCanc.getNewValue() - amendAndPartCanc.getOldValue());
                        costHeaderCharge.setPassengerQuantity(1);
                        costHeaderCharge.setTotalAmount(isCreatingCreditNoteForSupplierCharges ? amendAndPartCanc.getOldValue() - amendAndPartCanc.getNewValue() :
                                amendAndPartCanc.getNewValue() - amendAndPartCanc.getOldValue());

                        /*Added for resolving issues*/
                        creditDebitNote.setCurrency(invoiceToCheck.getInvoiceCurrency());
                        creditDebitNote.setRoe(invoiceToCheck.getRoe());

                        String paxType = "";

                        if(dtoForValue.getProductCategory().equalsIgnoreCase("AIR")) {
                            paxType = (String) dtoForValue.getPassengerDetails().stream().filter(pax -> pax.getPassengerID().equals(amendAndPartCanc.getUniqueID())).findAny().get().getAdditionalProperties().get("passengerType");
                        }else  if(dtoForValue.getProductCategory().equalsIgnoreCase("Accommodation")) {
                            paxType = (String) dtoForValue.getPassengerDetails().stream().filter(pax -> new Boolean(pax.getAdditionalProperties().get("isLeadPassenger").toString())).findAny().get().getAdditionalProperties().get("passengerType");
                        }

                        costHeaderCharge.setPassengerType(getPaxType(paxType));

                        costHeaderChargesList = new ArrayList<CostHeaderCharge>();
                        costHeaderChargesList.add(costHeaderCharge);

                        creditDebitNote.setCostHeaderCharges(costHeaderChargesList);

                        creditDebitNote.setProductCategory(dtoForValue.getProductCategory());
                        creditDebitNote.setProductCategorySubType(dtoForValue.getProductCategorySubType());
                        creditDebitNote.setProductName(dtoForValue.getProductName());
                        creditDebitNote.setProductSubName(dtoForValue.getProductNameSubType());
                        creditDebitNote.setTotalAmount(isCreatingCreditNoteForSupplierCharges ? amendAndPartCanc.getOldValue() - amendAndPartCanc.getNewValue() :
                                amendAndPartCanc.getNewValue() - amendAndPartCanc.getOldValue());
                        creditDebitNote.setLinkedNoteNumber(invoiceToCheck.getInvoiceNumber());
                        creditDebitNote.setLinkedNoteType("INVOICE");
                        /**
                         * TODO: Remaining fields are depends upon tax details from UI team, after that calculation can be done and save.
                         */
                        logger.info("-- Calling " + creditDebitMemoAPIUrl + " to generate credit/debit note");
                        /*Start:Code Commented and Added*/
                        //RestUtils.postForEntity(creditDebitMemoAPIUrl, creditDebitNote, Object.class);
                        HttpEntity httpEntity = new HttpEntity(creditDebitNote, getHttpHeader());
                        RestUtils.exchange(creditDebitMemoAPIUrl, HttpMethod.POST, httpEntity, Object.class);
                        /*End:*/
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception :", e);
            throw new OperationException(Constants.DEBIT_NOTE_CREATION_FAIL);
        }

        // INFO: Below condition is required when there is increase in company charges. In this case, there is no approval needed and booking should be updated.
        if (isCreatingDebitNoteAtStart && compChrgList.size() != 0) {
            logger.info("-- Calling update booking for company charges where approval not required.");
            updateBooking(compChrgList);
        }
        logger.info("-- Exiting AmendAndPartCancServiceImpl.createDebitNote ");
    }

    private String getPaxType(String paxType) {

        switch (paxType) {
            case "ADT":
                return "ADULT";
            case "CHD":
                return "CHILD";
            default:
                return "NA";
        }
    }

    private HttpHeaders getHttpHeader() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String token = userService.getLoggedInUserToken();
        headers.add("Authorization", token);
        return headers;
    }

    private Invoice getInvoicesByAmendAndPartCanc(AmendAndPartCanc amendAndPartCanc) throws OperationException {
        logger.info("-- Entering AmendAndPartCancServiceImpl.getInvoicesByAmendAndPartCanc ");
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(invoiceCheckAPIUrl).queryParam("bookingRef", amendAndPartCanc.getBookingRefNo());
        /*Start:Code Commented and Added*/
//        Invoice[] invoiceArr = RestUtils.getTemplate().getForObject(builder.toUriString(), Invoice[].class);
        ResponseEntity<Invoice[]> dataObject = RestUtils.exchange(builder.toUriString(), HttpMethod.GET, new HttpEntity(getHttpHeader()), Invoice[].class);
        Invoice[] invoiceArr = dataObject.getBody();
        /*End:*/
        Invoice invoiceToCheck = null;

        invoiceToUpdateForLoop:
        for (Invoice invoice : invoiceArr) {
            for (InvoiceParticularsDto dto : invoice.getInvoiceParticularsDto()) {
                if (null != dto.getOrderID() && dto.getOrderID().equalsIgnoreCase(amendAndPartCanc.getOrderID())) {
                    invoiceToCheck = invoice;
                    break invoiceToUpdateForLoop;
                }
            }
        }
        logger.info("-- Exiting AmendAndPartCancServiceImpl.getInvoicesByAmendAndPartCanc ");
        return invoiceToCheck;
    }

    public ErrorResponseResource getMessageToUser(String errorStr, HttpStatus httpStatus) {
        logger.info("-- Entering AmendAndPartCancServiceImpl.getMessageToUser ");
        ErrorResponseResource resource = new ErrorResponseResource();
        resource.setMessage(errorStr);
        resource.setStatus(httpStatus);
        logger.info("-- Exiting AmendAndPartCancServiceImpl.getMessageToUser ");
        return resource;
    }

    private void sendEmailsToSupplier(List<AmendAndPartCanc> suppChrgList) throws OperationException {
        logger.info("-- Entering AmendAndPartCancServiceImpl.sendEmailsToSupplier ");
        try {
            OpsBooking opsBooking = opsBookingService.getBooking(suppChrgList.get(0).getBookingRefNo());
            logger.info("-- Getting booking details of booking : " + opsBooking.getBookID());
            String supplierRefNumber = null;
            Map<String, String> dynamicValuesMap = null;
            String supplierEmailId = null;

            for (AmendAndPartCanc amendAndPartCanc : suppChrgList) {
                supplierRefNumber = opsBooking.getProducts().stream().filter(opsProduct -> opsProduct.getOrderID().equalsIgnoreCase(amendAndPartCanc.getOrderID()))
                        .findFirst().get().getSupplierRefNumber();
                logger.info("-- Sending email to supplier: " + supplierRefNumber);
                /**
                 * INFO: For testing below line is commented and sending email to QA. Uncomment below line afterwords.
                 */
                //supplierEmailId= jsonObjectProvider.getAttributeValue(supplierDetailsService.getSupplierDetails(amendAndPartCanc.getSupplierID()),"$.contactInfo.contactDetails.email",String.class);

                dynamicValuesMap = new HashMap<String, String>();
                dynamicValuesMap.put("supplierID", amendAndPartCanc.getSupplierID());
                dynamicValuesMap.put("supplierRefNo", supplierRefNumber);
                dynamicValuesMap.put("Approve", suppApprovalUrl + amendAndPartCanc.getTaskRefID());
                dynamicValuesMap.put("Reject", suppRejectionUrl + amendAndPartCanc.getTaskRefID());

                try {
                    emailUtils.buildClientMail(function, scenario, /*supplierEmailId*/"aasif.solkar@coxandkings.com", subject + supplierRefNumber, dynamicValuesMap, null, null);
                } catch (Exception e) {
                    throw new OperationException(Constants.EMAIL_SENT_SUPPLIER_FAILURE, supplierRefNumber);
                }
            }
        } catch (Exception e) {
            logger.error("Exception :", e);
            throw new OperationException(Constants.TECHNICAL_ISSUE_BOOKING_FIND, suppChrgList.get(0).getBookingRefNo());
        }
        logger.info("-- Exiting AmendAndPartCancServiceImpl.sendEmailsToSupplier ");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ErrorResponseResource handleSupplierResponseToChargesAmendment(String suppResponse, String taskRefID) throws OperationException {
        logger.info("-- Entering AmendAndPartCancServiceImpl.handleSupplierResponseToChargesAmendment ");
        List<AmendAndPartCanc> suppAmendAndPartCancList = null;

        try {
            if (suppResponse.equalsIgnoreCase(OpsApprovalStatus.APPROVED.toString()) || suppResponse.equalsIgnoreCase(OpsApprovalStatus.REJECTED.toString())) {
                if (compChgAmendAndPartCancRepository.updateSupplierResponseToAmendment(suppResponse, taskRefID) > 0) {
                    suppAmendAndPartCancList = compChgAmendAndPartCancRepository.getSupplierChargesAmendments(taskRefID);
                    updateInvoice(suppAmendAndPartCancList, false);

                    createCreditNote(suppAmendAndPartCancList.stream().filter(obj -> obj.getOldValue() > obj.getNewValue()).collect(Collectors.toList()), false);
                    createDebitNote(suppAmendAndPartCancList.stream().filter(obj -> obj.getOldValue() < obj.getNewValue()).collect(Collectors.toList()), false, false);

                    updateBooking(suppAmendAndPartCancList);
                } else {
                    return getMessageToUser(messageSource.getMessage(Constants.RECORD_NOT_FOUND_FOR_ID, new Object[]{suppResponse}, Locale.US), HttpStatus.NOT_FOUND);
                }
            } else {
                return getMessageToUser(messageSource.getMessage(Constants.SUPPLIER_REPLIED_STATUS_INCORRECT, new Object[]{suppResponse}, Locale.US), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            logger.error("Exception :", e);
            throw new OperationException(Constants.TECHNICAL_ISSUE_APPROVE_REJECT_RECORD);
        }
        logger.info("-- Exiting AmendAndPartCancServiceImpl.handleSupplierResponseToChargesAmendment ");
        return getMessageToUser(messageSource.getMessage(Constants.RECORD_APPROVED_REJECTED_SUCCESS, new Object[]{suppResponse}, Locale.US), HttpStatus.OK);
    }

    private void updateInvoice(List<AmendAndPartCanc> amendAndPartCancList, boolean isForCompanyChargesAmendment) throws OperationException {
        logger.info("-- Entering AmendAndPartCancServiceImpl.updateInvoice ");
        Invoice invoiceToCheck = getInvoicesByAmendAndPartCanc(amendAndPartCancList.get(0));
        InvoiceParticularsDto dtoToCheck = null;
        JSONObject taxEngineRSJSON = null, appliedTaxEle = null, fareDetailsEle = null;
        JSONArray appliedTaxDetArr = null;
        List<TaxDetail> taxDetailList = new ArrayList<TaxDetail>();
        TaxDetail taxDetail = null;

        for (AmendAndPartCanc amendAndPartCanc : amendAndPartCancList) {
            dtoToCheck = invoiceToCheck.getInvoiceParticularsDto().stream().filter(obj -> obj.getOrderID().equalsIgnoreCase(amendAndPartCanc.getOrderID())).findFirst().get();

            if (isForCompanyChargesAmendment) {
                dtoToCheck.setAmendmentPrice(amendAndPartCanc.getNewValue() - amendAndPartCanc.getOldValue());
            } else {
                dtoToCheck.setSupplierCost(amendAndPartCanc.getNewValue() - amendAndPartCanc.getOldValue());
            }

            logger.info("-- Getting GST details from Tax Engine");
            taxEngineRSJSON = taxEngineUtils.getGSTDetails(amendAndPartCanc.getBookingRefNo(), amendAndPartCanc.getOrderID(), amendAndPartCanc.getNewValue(), true);

            fareDetailsEle = taxEngineRSJSON.getJSONObject("result").getJSONObject("execution-results").getJSONArray("results").getJSONObject(0).getJSONObject("value")
                    .getJSONObject("cnk.taxcalculation.Root").getJSONObject("gstCalculationIntake").getJSONArray("supplierPricingDetails").getJSONObject(0).getJSONArray("travelDetails")
                    .getJSONObject(0).getJSONArray("fareDetails").getJSONObject(0);

            Object jsonObj = fareDetailsEle.get("appliedTaxDetails");

            if (jsonObj != null && jsonObj instanceof JSONArray) {
                appliedTaxDetArr = fareDetailsEle.getJSONArray("appliedTaxDetails");

                if (null != appliedTaxDetArr && appliedTaxDetArr.length() != 0) {
                    for (int i = 0; i < appliedTaxDetArr.length(); i++) {
                        taxDetail = new TaxDetail();
                        appliedTaxEle = appliedTaxDetArr.getJSONObject(i);
                        taxDetail.setTaxAmount(appliedTaxEle.getDouble("taxValue"));
                        taxDetail.setTaxCode(appliedTaxEle.optString("sacCode")); //changed to optString as it was null
                        taxDetail.setTaxRate(appliedTaxEle.getDouble("taxPercentage"));
                        taxDetailList.add(taxDetail);
                    }
                }
            }

            dtoToCheck.setTaxDetails(taxDetailList);
        }

        logger.info("-- Updating GST details of invoice : " + invoiceToCheck.getInvoiceNumber());
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(invoiceUpdateAPIUrl).queryParam("invoiceNumber", invoiceToCheck.getInvoiceNumber());

        /*Start : Code Commented added*/
        //RestUtils.getTemplate().put(builder.build().encode().toUri(), invoiceToCheck);
        HttpEntity httpEntity = new HttpEntity(invoiceToCheck, getHttpHeader());
        RestUtils.exchange(builder.toUriString(), HttpMethod.PUT, httpEntity, Object.class);
        /*End:*/

        logger.info("-- Exiting AmendAndPartCancServiceImpl.updateInvoice ");
    }

    private ErrorResponseResource updateBooking(List<AmendAndPartCanc> amendAndPartCancList) throws OperationException {
        logger.info("-- Entering AmendAndPartCancServiceImpl.updateBooking ");
        AmendmentChargesBE amendmentChargesBE = null;
        HttpEntity<AmendmentChargesBE> httpEntity = null;
        try {

            for (AmendAndPartCanc amendAndPartCanc : amendAndPartCancList) {
                amendmentChargesBE = new AmendmentChargesBE();

                for (OpsProductSubCategory opsProductSubCategory : OpsProductSubCategory.values()) {
                    if (opsProductSubCategory.toString().equalsIgnoreCase(amendAndPartCanc.getProductSubCategory())) {
                        amendmentChargesBE.setProduct(opsProductSubCategory.getSubCategory());
                        break;
                    }
                }

                amendmentChargesBE.setId(amendAndPartCanc.getAmendCancID());
                if (amendAndPartCanc.getActionType().equals(ActionType.COMPANY_CHARGES_AMENDMENT.toString()) ||
                        amendAndPartCanc.getActionType().equals(ActionType.COMPANY_CHARGES_CANCELLATION.toString())) {
                    amendmentChargesBE.setCompanyCharges(amendAndPartCanc.getNewValue().toString());
                    amendmentChargesBE.setSupplierCharges("0");
                } else {
                    amendmentChargesBE.setCompanyCharges("0");
                    amendmentChargesBE.setSupplierCharges(amendAndPartCanc.getNewValue().toString());
                }
                amendmentChargesBE.setUserID(amendAndPartCanc.getUserID());

                httpEntity = new HttpEntity<AmendmentChargesBE>(amendmentChargesBE);

                if (amendAndPartCanc.getAmendCancID() != null && !amendAndPartCanc.getAmendCancID().equals("")) {
                    RestUtils.getTemplate().put(compSuppChrgUpdateUrl, httpEntity);
                } else {
                    createNewRecordInBEamcl(amendAndPartCanc);
                }
            }
        } catch (Exception e) {
            logger.error("Exception :", e);
            throw new OperationException(Constants.PRODUCTS_AMENDMENT_CHARGES_UPDATED_FAILURE, amendAndPartCancList.get(0).getBookingRefNo());
        }
        logger.info("-- Exiting AmendAndPartCancServiceImpl.updateBooking ");
        return getMessageToUser(messageSource.getMessage(Constants.PRODUCTS_AMENDMENT_CHARGES_UPDATED_SUCCESS,
                new Object[]{amendAndPartCancList.get(0).getBookingRefNo()}, Locale.US), HttpStatus.OK);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public JSONObject getAmendAndCancDetails(String taskRefId) throws OperationException {
        logger.info("-- Entering AmendAndPartCancServiceImpl.getAmendAndCancDetails ");

        List<AmendAndPartCanc> amendAndCancList = null, flightAmendList = null, hotelAmendList = null;
        AmendAndPartCanc amendAndPartCanc = null;
        OpsBooking opsBooking = null;
        JSONObject approverUserJSON = new JSONObject();
        try {
            amendAndCancList = compChgAmendAndPartCancRepository.getAmendAndCancDetailsList(taskRefId);
            hotelAmendList = amendAndCancList.stream().filter(obj -> obj.getProductSubCategory().equalsIgnoreCase(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS.toString())).collect(Collectors.toList());
            flightAmendList = amendAndCancList.stream().filter(obj -> obj.getProductSubCategory().equalsIgnoreCase(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.toString())).collect(Collectors.toList());

            if (null != amendAndCancList && amendAndCancList.size() != 0) {
                amendAndPartCanc = amendAndCancList.get(0);

                approverUserJSON.put("userID", amendAndPartCanc.getUserID());
                approverUserJSON.put("clientID", amendAndPartCanc.getClientID());
                approverUserJSON.put("bookingRefNo", amendAndPartCanc.getBookingRefNo());
                approverUserJSON.put("orderID", amendAndPartCanc.getOrderID());
                approverUserJSON.put("supplierID", amendAndPartCanc.getSupplierID());
                approverUserJSON.put("productCategory", amendAndPartCanc.getProductCategory());
                approverUserJSON.put("productSubCategory", amendAndPartCanc.getProductSubCategory());
                approverUserJSON.put("productName", amendAndPartCanc.getProductName());
                approverUserJSON.put("actionType", amendAndPartCanc.getActionType());

                opsBooking = opsBookingService.getBooking(amendAndPartCanc.getBookingRefNo());

                approverUserJSON.put("roomInfo", getRoomInfoArray(hotelAmendList, opsBooking));
                approverUserJSON.put("paxDetails", getPaxDetailsArray(flightAmendList, opsBooking));
            }
        } catch (Exception e) {
            logger.error("Exception :", e);
            throw new OperationException(Constants.TECHNICAL_DIFFICULTY_AMEND_CANC_DETAILS, taskRefId);
        }
        logger.info("-- Exiting AmendAndPartCancServiceImpl.getAmendAndCancDetails ");
        return approverUserJSON;
    }

    private JSONArray getRoomInfoArray(List<AmendAndPartCanc> hotelAmendList, OpsBooking opsBooking) throws OperationException {
        logger.info("-- Entering AmendAndPartCancServiceImpl.getRoomInfoArray ");

        JSONArray roomsArr = new JSONArray();
        JSONObject roomJSON = null;
        OpsRoom opsRoom = null;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            for (AmendAndPartCanc apc : hotelAmendList) {
                opsRoom = opsBooking.getProducts().stream().filter(opsProduct -> opsProduct.getOrderID().equalsIgnoreCase(apc.getOrderID()))
                        .findFirst().get().getOrderDetails().getHotelDetails().getRooms().stream()
                        .filter(room -> room.getRoomID().equalsIgnoreCase(apc.getUniqueID())).findFirst().get();

                roomJSON = new JSONObject();
                roomJSON.put("mealInfo", new JSONObject(objectMapper.writeValueAsString(opsRoom.getMealInfo())));
                roomJSON.put("roomTypeInfo", new JSONObject(objectMapper.writeValueAsString(opsRoom.getRoomTypeInfo())));
                roomJSON.put("paxInfo", opsRoom.getPaxInfo());
                roomJSON.put("roomID", opsRoom.getRoomID());
                roomJSON.put("oldValue", apc.getOldValue());
                roomJSON.put("newValue", apc.getNewValue());

                roomsArr.put(roomJSON);
            }
        } catch (Exception e) {
            logger.error("Exception :", e);
            throw new OperationException();
        }
        logger.info("-- Exiting AmendAndPartCancServiceImpl.getRoomInfoArray ");
        return roomsArr;
    }

    private JSONArray getPaxDetailsArray(List<AmendAndPartCanc> flightAmendList, OpsBooking opsBooking) throws OperationException {
        logger.info("-- Entering AmendAndPartCancServiceImpl.getPaxDetailsArray ");

        JSONArray paxDetArr = new JSONArray();
        JSONObject paxDetJSON = null;
        OpsFlightPaxInfo opsFlightPaxInfo = null;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            for (AmendAndPartCanc apc : flightAmendList) {
                opsFlightPaxInfo = opsBooking.getProducts().stream().filter(opsProduct -> opsProduct.getOrderID().equalsIgnoreCase(apc.getOrderID())).findFirst().get()
                        .getOrderDetails().getFlightDetails().getPaxInfo().stream()
                        .filter(paxInfo -> paxInfo.getPassengerID().equalsIgnoreCase(apc.getUniqueID())).findFirst().get();

                paxDetJSON = new JSONObject(objectMapper.writeValueAsString(opsFlightPaxInfo));
                paxDetJSON.put("oldValue", apc.getOldValue());
                paxDetJSON.put("newValue", apc.getNewValue());

                paxDetArr.put(paxDetJSON);
            }
        } catch (Exception e) {
            logger.error("Exception :", e);
            throw new OperationException();
        }
        logger.info("-- Exiting AmendAndPartCancServiceImpl.getPaxDetailsArray ");
        return paxDetArr;
    }

    private void createNewRecordInBEamcl(AmendAndPartCanc amendAndPartCanc) throws OperationException {
        JSONObject requestJSON = new JSONObject(), responseJSON = new JSONObject();
        OpsBooking opsBooking = opsBookingService.getBooking(amendAndPartCanc.getBookingRefNo());

        requestJSON.put("requestHeader", createRequestHeader(amendAndPartCanc, opsBooking));
        responseJSON.put("responseHeader", requestJSON.getJSONObject("requestHeader"));

        if (amendAndPartCanc.getActionType().equals(ActionType.COMPANY_CHARGES_AMENDMENT.toString())
                || amendAndPartCanc.getActionType().equals(ActionType.SUPPLIER_CHARGES_AMENDMENT.toString())) {
            // INFO: Creating new record for Acco
            if (amendAndPartCanc.getProductCategory().equals(OpsProductCategory.PRODUCT_CATEGORY_ACCOMMODATION.toString())) {
                requestJSON.put("requestBody", roomAmendRequestBody(amendAndPartCanc, opsBooking));
                responseJSON.put("responseBody", roomResponseBody(amendAndPartCanc, "UPDATEROOM", "amend"));
                // INFO: Creating new record for Air
            } else if (amendAndPartCanc.getProductCategory().equals(OpsProductCategory.PRODUCT_CATEGORY_TRANSPORTATION.toString())) {
                requestJSON.put("requestBody", airAmendRequestBody(amendAndPartCanc, opsBooking));
                responseJSON.put("responseBody", airResponseBody(amendAndPartCanc, "UPDATEPASSENGER", "amend"));
            }
        } else if (amendAndPartCanc.getActionType().equals(ActionType.COMPANY_CHARGES_CANCELLATION.toString())
                || amendAndPartCanc.getActionType().equals(ActionType.SUPPLIER_CHARGES_CANCELLATION.toString())) {
            // INFO: Creating new record for Acco
            if (amendAndPartCanc.getProductCategory().equals(OpsProductCategory.PRODUCT_CATEGORY_ACCOMMODATION.toString())) {
                requestJSON.put("requestBody", roomCancelRequestBody(amendAndPartCanc));
                responseJSON.put("responseBody", roomResponseBody(amendAndPartCanc, "CANCELROOM", "cancel"));
                // INFO: Creating new record for Air
            } else if (amendAndPartCanc.getProductCategory().equals(OpsProductCategory.PRODUCT_CATEGORY_TRANSPORTATION.toString())) {
                requestJSON.put("requestBody", airCancelRequestBody(amendAndPartCanc, opsBooking));
                responseJSON.put("responseBody", airResponseBody(amendAndPartCanc, "CANCELPASSENGER", "cancel"));
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //INFO: Saving request to amcl
        HttpEntity<String> httpEntity = new HttpEntity<String>(requestJSON.toString(), headers);
        RestUtils.postForEntity(addNewRecInAMCLUrl, httpEntity, String.class);

        //INFO: Saving response to amcl
        httpEntity = new HttpEntity<String>(responseJSON.toString(), headers);
        RestUtils.postForEntity(addNewRecInAMCLUrl, httpEntity, String.class);
    }

    private JSONObject createRequestHeader(AmendAndPartCanc amendAndPartCanc, OpsBooking opsBooking) {
        JSONObject reqheader = null, clientContextJSON = null;

        if (opsBooking != null && opsBooking.getProducts() != null && opsBooking.getProducts().size() != 0) {
            OpsProduct opsProduct = opsBooking.getProducts().stream().filter(obj -> obj.getOrderID().equals(amendAndPartCanc.getOrderID())).findFirst().orElse(null);

            if (opsProduct != null) {
                clientContextJSON = new JSONObject();
                clientContextJSON.put("pointOfSale", opsBooking.getPointOfSale());
                clientContextJSON.put("clientID", opsBooking.getClientID());
                clientContextJSON.put("clientLanguage", opsBooking.getClientLanguage());
                clientContextJSON.put("clientType", opsBooking.getClientType());
                clientContextJSON.put("clientMarket", opsBooking.getClientMarket());
                clientContextJSON.put("clientCurrency", opsBooking.getClientCurrency());
                clientContextJSON.put("clientNationality", "");

                reqheader = new JSONObject();
                reqheader.put("clientContext", clientContextJSON);
                reqheader.put("sessionID", opsBooking.getSessionID());
                reqheader.put("userID", opsBooking.getUserID());
                reqheader.put("transactionID", opsBooking.getTransactionID());
            }
        }
        return reqheader;
    }

    private JSONObject roomAmendRequestBody(AmendAndPartCanc amendAndPartCanc, OpsBooking opsBooking) {
        JSONObject requestBody = new JSONObject();

        OpsProduct opsProduct = opsBooking.getProducts().stream().filter(obj -> obj.getOrderID().equals(amendAndPartCanc.getOrderID())).findFirst().orElse(null);

        if (opsProduct != null && opsProduct.getOrderID().equals(amendAndPartCanc.getOrderID())) {
            requestBody.put("product", "ACCO");
            requestBody.put("type", "UPDATEROOM");

            JSONArray entityIDArr = new JSONArray();
            JSONObject entityID = new JSONObject();
            entityID.put("entityID", amendAndPartCanc.getUniqueID());
            entityIDArr.put(entityID);

            requestBody.put("entityIDs", entityIDArr);
            requestBody.put("orderID", amendAndPartCanc.getOrderID());
            requestBody.put("requestType", "amend");
            requestBody.put("entityName", "room");

            OpsRoom opsRoom = opsProduct.getOrderDetails().getHotelDetails().getRooms().stream().filter(obj -> obj.getRoomID().equals(amendAndPartCanc.getUniqueID())).findFirst().get();
            requestBody.put("roomTypeCode", opsRoom.getRoomTypeInfo().getRoomTypeCode());
            requestBody.put("ratePlanCode", opsRoom.getRatePlanInfo().getRatePlanCode());
        }
        return requestBody;
    }

    private JSONObject roomResponseBody(AmendAndPartCanc amendAndPartCanc, String type, String requestType) {
        JSONObject respBodyJSON = new JSONObject();
        respBodyJSON.put("product", "ACCO");
        if (amendAndPartCanc.getActionType().equals(ActionType.COMPANY_CHARGES_AMENDMENT.toString()) || amendAndPartCanc.getActionType().equals(ActionType.COMPANY_CHARGES_CANCELLATION.toString())) {
            respBodyJSON.put("companyCharges", amendAndPartCanc.getNewValue().toString());
            respBodyJSON.put("supplierCharges", "0");
        } else if (amendAndPartCanc.getActionType().equals(ActionType.SUPPLIER_CHARGES_AMENDMENT.toString()) || amendAndPartCanc.getActionType().equals(ActionType.SUPPLIER_CHARGES_CANCELLATION.toString())) {
            respBodyJSON.put("companyCharges", "0");
            respBodyJSON.put("supplierCharges", amendAndPartCanc.getNewValue().toString());
        }

        respBodyJSON.put("companyChargesCurrencyCode", ""); // INFO: not available & non-mandatory
        respBodyJSON.put("supplierChargesCurrencyCode", ""); // INFO: not available & non-mandatory
        respBodyJSON.put("requestType", requestType);
        respBodyJSON.put("type", type);
        respBodyJSON.put("entityName", "room");
        respBodyJSON.put("orderID", amendAndPartCanc.getOrderID());

        JSONArray entityIDsArr = new JSONArray();
        JSONObject entityIDJSON = new JSONObject();
        entityIDJSON.put("entityID", amendAndPartCanc.getUniqueID());
        entityIDsArr.put(entityIDJSON);

        respBodyJSON.put("entityIDs", entityIDsArr);
        return respBodyJSON;
    }

    private JSONObject airAmendRequestBody(AmendAndPartCanc amendAndPartCanc, OpsBooking opsBooking) {
        JSONObject requestBodyJSON = new JSONObject();
        requestBodyJSON.put("product", "AIR");
        requestBodyJSON.put("type", "UPDATEPASSENGER");
        requestBodyJSON.put("requestType", "amend");
        requestBodyJSON.put("entityName", "pax");

        OpsProduct opsProduct = opsBooking.getProducts().stream().filter(obj -> obj.getOrderID().equals(amendAndPartCanc.getOrderID())).findFirst().orElse(null);
        OpsFlightPaxInfo opsFlightPaxInfo = opsProduct.getOrderDetails().getFlightDetails().getPaxInfo().stream().filter(obj -> obj.getPassengerID().equals(amendAndPartCanc.getUniqueID())).findFirst().orElse(null);

        JSONArray entityIDsArr = new JSONArray();
        JSONObject entityIDsEle = new JSONObject();
        entityIDsEle.put("entityID", opsFlightPaxInfo.getPassengerID());//Instead of amendment id replaced it with passenger id
        entityIDsArr.put(entityIDsEle);

        JSONObject jsObjAddressDetails = new JSONObject();
        jsObjAddressDetails.put("zip", opsFlightPaxInfo.getAddressDetails().getZipCode());
        jsObjAddressDetails.put("country", opsFlightPaxInfo.getAddressDetails().getCountryName());
        jsObjAddressDetails.put("city", opsFlightPaxInfo.getAddressDetails().getCityName());
        jsObjAddressDetails.put("addrLine1", opsFlightPaxInfo.getAddressDetails().getAddressLines().get(0));
        jsObjAddressDetails.put("addrLine2", opsFlightPaxInfo.getAddressDetails().getAddressLines().get(1));
        jsObjAddressDetails.put("state", opsFlightPaxInfo.getAddressDetails().getState());

        JSONArray paxDetailsArr = new JSONArray();
        JSONObject paxDetailsEle = new JSONObject();
        paxDetailsEle.put("paxID", opsFlightPaxInfo.getPassengerID());
        paxDetailsEle.put("documentDetails", new JSONObject());//INFO: It's optional & currently not available in OpsFlightPaxInfo
        paxDetailsEle.put("title", opsFlightPaxInfo.getTitle());
        paxDetailsEle.put("firstName", opsFlightPaxInfo.getFirstName());
        paxDetailsEle.put("isLeadPax", opsFlightPaxInfo.getLeadPax());
        paxDetailsEle.put("paxType", opsFlightPaxInfo.getPaxType());
        paxDetailsEle.put("gender", "");//INFO: currently not available in OpsFlightPaxInfo
        paxDetailsEle.put("surname", opsFlightPaxInfo.getLastName());
        paxDetailsEle.put("dob", opsFlightPaxInfo.getBirthDate());
        paxDetailsEle.put("middleName", opsFlightPaxInfo.getMiddleName());
        paxDetailsEle.put("addressDetails", jsObjAddressDetails);
        paxDetailsEle.put("ancillaryServices", opsFlightPaxInfo.getAncillaryServices().getAncillaryInfo());
        paxDetailsEle.put("contactDetails", opsFlightPaxInfo.getContactDetails());
        paxDetailsArr.put(paxDetailsEle);

        JSONArray supplierBookReferencesArr = new JSONArray();
        JSONObject supplierBookReferencesEle = new JSONObject();

        supplierBookReferencesEle.put("orderID", amendAndPartCanc.getOrderID());
        supplierBookReferencesEle.put("entityIDs", entityIDsArr);
        supplierBookReferencesEle.put("paxDetails", paxDetailsArr);
        supplierBookReferencesArr.put(supplierBookReferencesEle);

        requestBodyJSON.put("supplierBookReferences", supplierBookReferencesArr);
        return requestBodyJSON;
    }

    private JSONObject airResponseBody(AmendAndPartCanc amendAndPartCanc, String type, String requestType) {
        JSONObject responseBodyJSON = new JSONObject();
        responseBodyJSON.put("product", "AIR");
        responseBodyJSON.put("entityName", "pax");
        responseBodyJSON.put("type", type);
        responseBodyJSON.put("bookID", amendAndPartCanc.getBookingRefNo());
        responseBodyJSON.put("requestType", requestType);

        responseBodyJSON.put("orderID", amendAndPartCanc.getOrderID()); //TODO this temporary need to talk with B.E

        JSONArray supplierBookReferencesArr = new JSONArray();
        responseBodyJSON.put("supplierBookReferences", supplierBookReferencesArr);

        JSONObject supplierBookReferencesEle = new JSONObject();
        supplierBookReferencesArr.put(supplierBookReferencesEle);

        supplierBookReferencesEle.put("entityName", "pax");
        supplierBookReferencesEle.put("orderID", amendAndPartCanc.getOrderID());

        JSONArray entityIDsArr = new JSONArray();
        supplierBookReferencesEle.put("entityIDs", entityIDsArr);

        JSONObject entityIDsEle = new JSONObject();
        entityIDsArr.put(entityIDsEle);
        entityIDsEle.put("entityID", amendAndPartCanc.getUniqueID()); //Made entityID from entityIDs

//        supplierBookReferencesEle.put("requestType", "amend");

        if (amendAndPartCanc.getActionType().equals(ActionType.COMPANY_CHARGES_AMENDMENT.toString()) || amendAndPartCanc.getActionType().equals(ActionType.COMPANY_CHARGES_CANCELLATION.toString())) {
            supplierBookReferencesEle.put("companyCharges", amendAndPartCanc.getNewValue());
            supplierBookReferencesEle.put("supplierCharges", "0");
        } else if (amendAndPartCanc.getActionType().equals(ActionType.SUPPLIER_CHARGES_AMENDMENT.toString()) || amendAndPartCanc.getActionType().equals(ActionType.SUPPLIER_CHARGES_CANCELLATION.toString())) {
            supplierBookReferencesEle.put("companyCharges", "0");
            supplierBookReferencesEle.put("supplierCharges", amendAndPartCanc.getNewValue());
        }
        supplierBookReferencesEle.put("supplierChargesCurrencyCode", "");
        supplierBookReferencesEle.put("companyChargesCurrencyCode", "");

        return responseBodyJSON;
    }


    private JSONObject roomCancelRequestBody(AmendAndPartCanc amendAndPartCanc) {
        JSONObject requestJSON = new JSONObject();
        requestJSON.put("product", "ACCO");
        requestJSON.put("type", "CANCELROOM");

        JSONArray entityIDsArr = new JSONArray();
        requestJSON.put("entityIDs", entityIDsArr);

        JSONObject entityIDsEle = new JSONObject();
        entityIDsEle.put("entityID", amendAndPartCanc.getUniqueID());
        entityIDsArr.put(entityIDsEle);

        requestJSON.put("orderID", amendAndPartCanc.getOrderID());
        requestJSON.put("requestType", "cancel");
        requestJSON.put("entityName", "room");

        return requestJSON;
    }

    private JSONObject airCancelRequestBody(AmendAndPartCanc amendAndPartCanc, OpsBooking opsBooking) {
        JSONObject requestJSON = new JSONObject();
        requestJSON.put("product", "AIR");
        requestJSON.put("requestType", "cancel");
        requestJSON.put("entityName", "pax");
        requestJSON.put("type", "CANCELPASSENGER");

        JSONArray supplierBookReferencesArr = new JSONArray();
        requestJSON.put("supplierBookReferences", supplierBookReferencesArr);

        JSONObject supplierBookReferencesEle = new JSONObject();
        supplierBookReferencesArr.put(supplierBookReferencesEle);

        OpsProduct opsProduct = opsBooking.getProducts().stream().filter(obj -> obj.getOrderID().equals(amendAndPartCanc.getOrderID())).findFirst().orElse(null);
        supplierBookReferencesEle.put("supplierRef", opsProduct.getSupplierRefNumber());
        supplierBookReferencesEle.put("orderID", amendAndPartCanc.getOrderID());

        JSONArray paxDetailsArr = new JSONArray();
        supplierBookReferencesEle.put("paxDetails", paxDetailsArr);

        JSONObject paxDetailsEle = new JSONObject();
        paxDetailsArr.put(paxDetailsEle);

        OpsFlightPaxInfo opsFlightPaxInfo = opsProduct.getOrderDetails().getFlightDetails().getPaxInfo().stream().filter(obj -> obj.getPassengerID().equals(amendAndPartCanc.getUniqueID())).findFirst().orElse(null);
        paxDetailsEle.put("firstName", opsFlightPaxInfo.getFirstName());
        paxDetailsEle.put("paxType", opsFlightPaxInfo.getPaxType());
        paxDetailsEle.put("namePrefix", opsFlightPaxInfo.getTitle());
        paxDetailsEle.put("surname", opsFlightPaxInfo.getLastName());
        paxDetailsEle.put("paxID", opsFlightPaxInfo.getPassengerID());
        paxDetailsEle.put("middleName", opsFlightPaxInfo.getMiddleName());

        supplierBookReferencesEle.put("bookRefID", amendAndPartCanc.getBookingRefNo());

        JSONArray entityIDsArr = new JSONArray();
        supplierBookReferencesEle.put("entityIDs", entityIDsArr);

        JSONObject entityIDsEle = new JSONObject();
        entityIDsArr.put(entityIDsEle);
        entityIDsEle.put("entityID", amendAndPartCanc.getUniqueID());

        return requestJSON;
    }
}
