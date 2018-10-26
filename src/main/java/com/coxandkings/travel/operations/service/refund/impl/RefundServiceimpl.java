package com.coxandkings.travel.operations.service.refund.impl;


import com.coxandkings.travel.ext.model.finance.creditdebitnote.CreditDebitNote;
import com.coxandkings.travel.operations.enums.refund.*;
import com.coxandkings.travel.operations.enums.todo.*;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.refund.*;
import com.coxandkings.travel.operations.model.refund.finaceRefund.*;
import com.coxandkings.travel.operations.model.todo.ToDoTask;
import com.coxandkings.travel.operations.repository.refund.ChangeTypeRefundRepository;
import com.coxandkings.travel.operations.repository.refund.RefundDataRepository;
import com.coxandkings.travel.operations.repository.todo.ToDoTaskRepository;
import com.coxandkings.travel.operations.resource.MessageResource;
import com.coxandkings.travel.operations.resource.fullcancellation.clientKPI.KpiDefinition;
import com.coxandkings.travel.operations.resource.refund.ApprovalResource;
import com.coxandkings.travel.operations.resource.refund.ChangeRefundTypeResponse;
import com.coxandkings.travel.operations.resource.refund.RefundResource;
import com.coxandkings.travel.operations.resource.refund.SendMailResource;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResource;
import com.coxandkings.travel.operations.resource.user.OpsUser;
import com.coxandkings.travel.operations.service.fullcancellation.MDMService;
import com.coxandkings.travel.operations.service.mdmservice.ClientMasterDataService;
import com.coxandkings.travel.operations.service.refund.*;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.DateTimeUtil;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.*;

import static com.coxandkings.travel.operations.utils.Constants.ER02;

@Service
@Transactional
public class RefundServiceimpl implements RefundService {


    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Autowired

    private ChangeTypeRefundRepository changeTypeRefundRepository;

    @Autowired
    private ToDoTaskService toDoTaskService;

    @Autowired
    private RefundDataRepository refundDataRepository;

    @Autowired
    private RefundFinanceService refundFinanceService;

    @Autowired
    private MDMService mdmService;

    @Autowired
    private RefundMDMService refundMDMService;

    @Autowired
    private UserService userService;

    @Autowired
    private RefundCommunicationService refundCommunicationService;

    @Autowired
    private ClientMasterDataService clientMasterDataService;

    @Autowired
    private ToDoTaskRepository toDoTaskRepository;

    @Autowired
    private RefundConfigurationService refundConfigurationService;

    private static final Logger logger = LogManager.getLogger(RefundServiceimpl.class);


    @Override
    @Transactional
    public Refund add(RefundResource refundResource) throws OperationException {


        RefundProcess refundProcess = refundResourceToRefundProcess(refundResource);

        refundFinanceService.getInvoicesByBookingDetails(refundProcess.getBookingReferenceNumber(), refundProcess.getProductDetail().getOrderId());

        RefundClaim refundClaimResponse = null;

        String clientEmail = null;
        try{
            if (ClientType.B2B == (refundProcess.getClientDetail().getClientType())) {
                clientEmail = refundMDMService.getB2BClientEmail(refundProcess.getClientDetail().getClientId());
            } else {
                clientEmail = refundMDMService.getB2CClientEmail(refundProcess.getClientDetail().getClientId());
            }
        }catch (Exception e){
            logger.error("Exception occured while sending an email to client ");
        }

        if (refundResource.getRefundType() == RefundTypes.REFUND_AMOUNT) {
            refundClaimResponse = refundFinanceService.saveRefundClaim(refundProcess);

            if (refundClaimResponse != null) {


                //creating to do task for finance user
                ToDoTaskResource toDoTaskResource = new ToDoTaskResource();
                toDoTaskResource.setReferenceId(refundClaimResponse.getClaimNumber()); //OPS DB ClientCommercialClaimNO
                //  created by used id

                String userID = userService.getLoggedInUserId();
                toDoTaskResource.setBookingRefId(refundClaimResponse.getBookingReferenceNumber());
                toDoTaskResource.setCreatedByUserId(userID);
                toDoTaskResource.setProductId(refundResource.getProductDetail().getProductName());

                //set client id
                if (ClientType.B2B == refundResource.getClientType()) {
                    String clientResponse = refundMDMService.getB2BClient(refundResource.getClientId());
                    CompanyDetail companyDetails = null;
                    ClientDetail clientDetail = null;
                    if (null != clientResponse) {

                        //create Company Details
                        companyDetails = getCompanyDetail(clientResponse);


                        //create client Details
                        clientDetail = getB2BClientDetail(refundResource, clientResponse);
                        toDoTaskResource.setClientCategoryId(clientDetail.getClientCategory());
                        toDoTaskResource.setClientSubCategoryId(clientDetail.getClientSubCategory());
                        toDoTaskResource.setClientTypeId(clientDetail.getClientType().name());
                        toDoTaskResource.setCompanyId(companyDetails.getCompanyId());
                        toDoTaskResource.setCompanyMarketId(companyDetails.getMarket());

                    }
                } else if (ClientType.B2C == refundResource.getClientType()) {
                    String clientResponse = refundMDMService.getB2CClient(refundResource.getClientId());


                    ClientDetail clientDetail = null;

                    if (null != clientResponse) {
                        //create client Details
                        clientDetail = getB2BClientDetail(refundResource, clientResponse);
                        toDoTaskResource.setClientCategoryId(clientDetail.getClientCategory());
                        toDoTaskResource.setClientSubCategoryId(clientDetail.getClientSubCategory());
                        toDoTaskResource.setClientTypeId(clientDetail.getClientType().name());
                    }
                }

                toDoTaskResource.setClientId(refundResource.getClientId());
                toDoTaskResource.setTaskNameId(ToDoTaskNameValues.APPROVE.getValue());
                toDoTaskResource.setTaskTypeId("Main task");
                toDoTaskResource.setTaskSubTypeId("REFUND"); //AMEND_CLIENT_COMMERCIAL
                toDoTaskResource.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.FINANCE.getValue()); // OPERATIONS or FINANCE
                toDoTaskResource.setTaskStatusId(ToDoTaskStatusValues.ASSIGNED.getValue()); //ASSIGN as per my assumption
                toDoTaskResource.setTaskPriorityId(ToDoTaskPriorityValues.HIGH.getValue());                // Dummy value HIGH
                toDoTaskResource.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.toString());

                toDoTaskResource.setSuggestedActions("");//sent the rest end point for now

                //toDoTaskResource.setDueOnDate(getKpi(refundResource.getClientId()).toString());


                try {
                    toDoTaskService.save(toDoTaskResource);
                } catch (Exception e) {
                    logger.error("Unable to create finance toDo Task", e);

                }


            } else {
                logger.error("Record can't be saved");
                throw new OperationException(Constants.ER05, "The Record can't be saved");
            }
        } else if (refundResource.getRefundType() == RefundTypes.REFUND_REDEEMABLE) {
            //generate credit note send mail to customer
            CreditDebitNote creditDebitNote = refundFinanceService.createCreditDebitNote(refundProcess);
            refundProcess.setRedeemableCreditNote(creditDebitNote.getNoteNumber());
            refundProcess.getRefundDetail().setRefundProcessedDate(ZonedDateTime.now());
            refundProcess.getRefundDetail().setStatus(ClaimStatus.Paid);

            String clientName = refundProcess.getClientDetail().getClientName();
            refundClaimResponse = refundFinanceService.saveRefundClaim(refundProcess);
            refundCommunicationService.sendMailToClient(clientEmail, clientName, refundProcess.getBookingReferenceNumber(), refundProcess.getProductDetail().getOrderId());

        } else if (refundResource.getRefundType() == RefundTypes.ACCOUNT_CREDIT) {
            //store the refund amount account credit
            //no credit note will be generated
            refundProcess.getRefundDetail().setRefundProcessedDate(ZonedDateTime.now());
            refundProcess.getRefundDetail().setStatus(ClaimStatus.Paid);
            refundClaimResponse = refundFinanceService.saveRefundClaim(refundProcess);

        }

        RefundData refundData = new RefundData();
        if (null != refundClaimResponse) {
            refundData.setClaimNo(refundClaimResponse.getClaimNumber());
        }
        refundData.setRefundReason(refundResource.getReasonForRequest());
        refundDataRepository.saveOrUpdate(refundData);

        if (refundResource.getRefundType() == RefundTypes.ACCOUNT_CREDIT && null != refundClaimResponse) {
            SendMailResource sendMailResource = new SendMailResource();
            sendMailResource.setBookingNo(refundClaimResponse.getBookingReferenceNumber());
            sendMailResource.setClaimNo(refundClaimResponse.getClaimNumber());
            sendMailResource.setClientId(refundClaimResponse.getClientDetail().getClientId());
            sendMailResource.setClientType(refundClaimResponse.getClientDetail().getClientType().getClientType());
            sendMailResource.setOrderNo(refundClaimResponse.getProductDetail().getOrderId());

            sendMailToClient(sendMailResource);
        }
        logger.info("***Refund: " + refundClaimResponse);
        return copyRefundClaimInRefund(refundClaimResponse);


    }


    private RefundProcess refundResourceToRefundProcess(RefundResource refundResource) throws OperationException {
        RefundProcess refundProcess = new RefundProcess();
        refundProcess.setBookingReferenceNumber(refundResource.getBookingReferenceNo());

        RefundDetail refundDetail = new RefundDetail();
        RefundReason mappingReason = getMapping(refundResource.getReasonForRequest());
        refundDetail.setRefundReason(mappingReason);
        refundDetail.setDefaultModeOfPayment(refundResource.getDefaultModeOfPayment());
        refundDetail.setRequestedModeOfPayment(refundResource.getRequestedModeOfPayment());

        refundDetail.setRequestedROE(refundResource.getRoeRequested());
        refundDetail.setClaimCurrency(refundResource.getClaimCurrency());
        refundDetail.setClaimROE(refundResource.getRoeAsInClaim());
        refundDetail.setCurrentModeOfPayment(refundResource.getDefaultModeOfPayment());
        refundDetail.setDueOn(getKpi(refundResource.getClientId()));

        MathContext mc = new MathContext(4);

        refundDetail.setNetAmountPayable(refundResource.getRefundAmount().multiply(refundResource.getRoeAsInClaim(), mc));
        refundDetail.setRefundAmount(refundResource.getRefundAmount());
        refundProcess.setBspRaNumber(refundResource.getBspRaNumber());


        refundProcess.setProductDetail(refundResource.getProductDetail());

        String clientResponse = null;
        if (ClientType.B2B == refundResource.getClientType()) {
            clientResponse = refundMDMService.getB2BClient(refundResource.getClientId());
            CompanyDetail companyDetails = null;
            ClientDetail clientDetail = null;
            if (null != clientResponse) {

                //create Company Details
                companyDetails = getCompanyDetail(clientResponse);

                //create client Details
                clientDetail = getB2BClientDetail(refundResource, clientResponse);
                refundProcess.setClientDetail(clientDetail);
                refundProcess.setCompanyDetail(companyDetails);

            }
        } else if (ClientType.B2C == refundResource.getClientType()) {
            clientResponse = refundMDMService.getB2CClient(refundResource.getClientId());
            ClientDetail clientDetail = null;

            if (null != clientResponse) {
                //create client Details
                clientDetail = getB2CClientDetail(refundResource, clientResponse);
                refundProcess.setClientDetail(clientDetail);

            }
        }

        RefundConfiguration refundConfiguration = new RefundConfiguration();
        refundConfiguration.setClientCategory(refundProcess.getClientDetail().getClientCategory());
        refundConfiguration.setClientType(refundProcess.getClientDetail().getClientType().getClientType());
        refundConfiguration.setCompanyMarket(refundMDMService.getMarketName(refundProcess.getCompanyDetail().getMarket()).replace("[","").replace("]","").replace("\"",""));
        refundConfiguration.setClientGroup(refundProcess.getClientDetail().getClientGroupId());
        refundConfiguration.setClientName(refundProcess.getClientDetail().getClientName());
        refundConfiguration.setClientSubCategory(refundProcess.getClientDetail().getClientSubCategory());
        /**
         * getting refund configuration stored in data base
         */
        RefundTypes refundTypeConfiguration = refundConfigurationService.getRefundConfiguration(refundConfiguration);
        if (null != refundTypeConfiguration) {
            refundDetail.setRefundType(refundTypeConfiguration);
        } else {
            refundDetail.setRefundType(refundResource.getRefundType());
        }
        refundProcess.setRefundDetail(refundDetail);

        return refundProcess;
    }


    @Override
    public Refund update(RefundResource refundResource) throws OperationException {


        if (refundResource.getClaimNo() == null) {
            logger.error(refundResource.getClaimNo() + " claim number is not valid");
            throw new OperationException(ER02, "please enter a valid primary key");
        }
        RefundClaim responseRefundClaim;

        RefundClaim refundClaim = refundFinanceService.getRefundClaim(refundResource.getClaimNo());
        if ((RefundTypes.REFUND_AMOUNT == refundClaim.getRefundDetail().getRefundType()) && (refundClaim.getRefundUserStatus() == RefundStatus.PROCESSED)) {
            logger.error("Refund claim " + refundClaim.getClaimNumber() + " is processed not allowed to update");
            throw new OperationException(Constants.OPS_ERR_30214);
        }

        //chane roe/ change pay mode
        refundClaim.getRefundDetail().setClaimROE(refundResource.getRoeAsInClaim());
        refundClaim.getRefundDetail().setClaimCurrency(refundResource.getClaimCurrency());
        if (null != refundResource.getRoeAsInClaim()) {

            refundClaim.getRefundDetail().setNetAmountPayable(refundResource.getRefundAmount().multiply(refundResource.getRoeAsInClaim()));
        }

        refundClaim.getRefundDetail().setRefundAmount(refundResource.getRefundAmount());


        if (refundResource.getRefundType() != refundClaim.getRefundDetail().getRefundType()) {
            throw new OperationException("You can not change refund Type in update refund Claim");
        }

        String clientResponse = null;
        if (ClientType.B2B == refundResource.getClientType()) {
            clientResponse = refundMDMService.getB2BClient(refundResource.getClientId());
            CompanyDetail companyDetails = null;
            ClientDetail clientDetail = null;
            if (null != clientResponse) {

                //create Company Details
                companyDetails = getCompanyDetail(clientResponse);
                companyDetails.setId(refundClaim.getCompanyDetail().getId());
                //create client Details
                clientDetail = getB2BClientDetail(refundResource, clientResponse);
                clientDetail.setId(refundClaim.getClientDetail().getId());
                refundClaim.setClientDetail(clientDetail);
                refundClaim.setCompanyDetail(companyDetails);

            }
        } else if (ClientType.B2C == refundResource.getClientType()) {
            clientResponse = refundMDMService.getB2CClient(refundResource.getClientId());
            ClientDetail clientDetail = null;

            if (null != clientResponse) {
                //create client Details
                clientDetail = getB2CClientDetail(refundResource, clientResponse);
                clientDetail.setId(refundClaim.getClientDetail().getId());
                refundClaim.setClientDetail(clientDetail);

            }
        }
        refundClaim.setBspRaNumber(refundResource.getBspRaNumber());

        //set credit note no
        if (refundClaim.getRefundDetail().getRefundType() == RefundTypes.REFUND_AMOUNT) {
            refundClaim.setGeneratedCreditNote(refundResource.getCreditNoteNo());
            refundClaim.setRedeemableCreditNote("");
        } else if (refundClaim.getRefundDetail().getRefundType() == RefundTypes.REFUND_REDEEMABLE) {
            refundClaim.setGeneratedCreditNote("");
            refundClaim.setRedeemableCreditNote(refundResource.getRedeemableCreditNote());
        } else {
            refundClaim.setGeneratedCreditNote("");
            refundClaim.setRedeemableCreditNote("");
        }

        //code for getting user id
        UsernamePasswordAuthenticationToken userPwdAuth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        OpsUser loggedInUser = (OpsUser) userPwdAuth.getPrincipal();
        String userID = loggedInUser.getUserID();
        refundClaim.setLastUpdatedBy(userID);

        RefundReason mappingReason = getMapping(refundResource.getReasonForRequest());
        RefundDetail refundDetail = refundClaim.getRefundDetail();
        refundDetail.setRefundReason(mappingReason);
        refundClaim.setRefundDetail(refundDetail);

        ProductDetail productDetail = refundResource.getProductDetail();
        productDetail.setId(refundClaim.getProductDetail().getId());
        refundClaim.setProductDetail(productDetail);
        responseRefundClaim = refundFinanceService.updateRefundClaim(refundClaim);

        if (responseRefundClaim != null) {
            RefundData refundData = new RefundData();
            refundData.setClaimNo(refundResource.getClaimNo());
            refundData.setRefundReason(refundResource.getReasonForRequest());
            refundDataRepository.saveOrUpdate(refundData);

        }
        return copyRefundClaimInRefund(responseRefundClaim);

    }


    /*
        This method will map the reason from refund to finance refund in finance there is only 4 type of reasons but
        in refund there is 13 types of reasons

     */
    private RefundReason getMapping(ReasonForRequest reasonForRequest) throws OperationException {
        RefundReason mappedReason = null;
        switch (reasonForRequest) {
            case NO_SHOW:
                mappedReason = RefundReason.Non_Utilization;
                break;
            case CANCELLATION:
            case BOOKING_FAILURE:
            case TRANSACTION_FAILURE:
                mappedReason = RefundReason.Cancellation;
                break;
            case COMPENSATION:
                mappedReason = RefundReason.Compensation;
                break;

            case ALTERNATE_OPTIONS:
            case AMEND_CLIENT_COMMERCIALS:
            case AMEND_SUPPLIER_COMMERCIALS:
            case AMEND_COMPANY_COMMERCIALS:
            case AMEND_SELLING_PRICE:
            case AMENDMENTS:
            case REINSTATE_ORIGINAL_BOOKINGS:
            case REPRICING_DURING_TICKETING:
            case SHARING_CONCEPT:
                mappedReason = RefundReason.Reduction;
                break;
            case DISCOUNT_SUPPLIER_PRICE:
            	mappedReason=RefundReason.Discount;
            	break;
            case CLAIM_DISCOUNT:
                mappedReason = RefundReason.Discount;
                break;
            default:
                throw new OperationException("Reason is not matching");

        }
        return mappedReason;
    }



    /*This method extract the client details from MDM response*/

    private ClientDetail getB2BClientDetail(RefundResource refundResource, String clientResponse) {
        ClientDetail clientDetail = new ClientDetail();
        if (clientResponse != null) {
            clientDetail.setClientId(refundResource.getClientId());
            clientDetail.setClientName(jsonObjectProvider.getAttributeValue(clientResponse, "$.clientProfile.clientDetails.clientName", String.class));
            clientDetail.setClientCategory(jsonObjectProvider.getAttributeValue(clientResponse, "$.clientProfile.clientDetails.clientCategory", String.class));
            clientDetail.setClientSubCategory(jsonObjectProvider.getAttributeValue(clientResponse, "$.clientProfile.clientDetails.clientSubCategory", String.class));

        }
        clientDetail.setClientType(refundResource.getClientType());
        return clientDetail;
    }

    private ClientDetail getB2CClientDetail(RefundResource refundResource, String clientResponse) {


        ClientDetail clientDetail = new ClientDetail();
        if (clientResponse != null) {

            clientDetail.setClientId(refundResource.getClientId());
            String firstName = jsonObjectProvider.getAttributeValue(clientResponse, "$.travellerDetails.employee.firstName", String.class);
            String lastName = jsonObjectProvider.getAttributeValue(clientResponse, "$.travellerDetails.employee.lastName", String.class);
            clientDetail.setClientName(firstName.concat(" ").concat(lastName));
        }
        clientDetail.setClientType(refundResource.getClientType());
        return clientDetail;
    }

    /*This method extract the Company details from MDM response*/
    private CompanyDetail getCompanyDetail(String clientResponse) {
        CompanyDetail companyDetails = new CompanyDetail();
        if (clientResponse != null) {
            companyDetails.setCompanyId(jsonObjectProvider.getAttributeValue(clientResponse, "$.clientProfile.orgHierarchy.companyId", String.class));
            companyDetails.setDepartmentBU(jsonObjectProvider.getAttributeValue(clientResponse, "$.clientProfile.orgHierarchy.BU", String.class));
            companyDetails.setMarket(jsonObjectProvider.getAttributeValue(clientResponse, "$.clientProfile.orgHierarchy.companyMarkets.[0].marketId", String.class));
            companyDetails.setDivisionSBU(jsonObjectProvider.getAttributeValue(clientResponse, "$.clientProfile.orgHierarchy.SBU", String.class));
        }
        return companyDetails;
    }


    @Override
    public Refund get(String claimNo) throws OperationException {
        Refund refund = null;
        //calling finance service
        RefundClaim refundClaim = refundFinanceService.getRefundClaim(claimNo);
        if (refundClaim != null) {
            refund = copyRefundClaimInRefund(refundClaim);
            RefundData refundData = refundDataRepository.getRefund(refund.getClaimNo());
            if (refundData != null) {
                refund.setReasonForRequest(refundData.getRefundReason());
            } else {
                logger.error("Not able to get data for mapping for claim no: " + refund.getClaimNo());
                throw new OperationException("Not able to get data for mapping for claim No: " + refund.getClaimNo());
            }

        } else {
            logger.error("refund Claim not found for claim No: " + claimNo);
            throw new OperationException(Constants.ER02, "refund Claim not found for claim No: " + claimNo);

        }

        return refund;
    }


    //this method will copy RefundClaim into refund
    private Refund copyRefundClaimInRefund(RefundClaim refundClaim) throws OperationException {
        Refund refund;
        refund = new Refund();

        refund.setClaimNo(refundClaim.getClaimNumber());
        refund.setClientId(refundClaim.getClientDetail().getClientId());
        refund.setBookingReferenceNo(refundClaim.getBookingReferenceNumber());
        //ToDO:refund type is not proper
        refund.setRefundType(refundClaim.getRefundDetail().getRefundType());
        refund.setClientId(refundClaim.getClientDetail().getClientId());
        refund.setClientType(refundClaim.getClientDetail().getClientType());
        refund.setClientName(refundClaim.getClientDetail().getClientName());
        refund.setRefundAmount(refundClaim.getRefundDetail().getRefundAmount());
        refund.setRefundStatus(refundClaim.getRefundUserStatus());
        refund.setDueOn(refundClaim.getRefundDetail().getDueOn());
        refund.setClaimCurrency(refundClaim.getRefundDetail().getClaimCurrency());
        refund.setRefundProcessedDate(refundClaim.getRefundDetail().getRefundProcessedDate());
        refund.setBspRaNumber(refundClaim.getBspRaNumber());

        refund.setRefundProcessedDate(refundClaim.getRefundDetail().getRefundProcessedDate());
        if (refundClaim.getRefundDetail().getRefundType() == RefundTypes.REFUND_REDEEMABLE) {
            refund.setCreditNoteNo(refundClaim.getRedeemableCreditNote());
        } else if (RefundTypes.REFUND_AMOUNT == refundClaim.getRefundDetail().getRefundType()) {
            refund.setCreditNoteNo(refundClaim.getGeneratedCreditNote());
        }
        refund.setDefaultModeOfPayment(refundClaim.getRefundDetail().getDefaultModeOfPayment());


        refund.setRequestedModeOfPayment(refundClaim.getRefundDetail().getRequestedModeOfPayment());


        refund.setRoeAsInClaim(refundClaim.getRefundDetail().getClaimROE());
        refund.setRoeRequested(refundClaim.getRefundDetail().getRequestedROE());
        refund.setNetAmountPayable(refundClaim.getRefundDetail().getNetAmountPayable());

        if (refundClaim.getClaimNumber() != null) {
            RefundData refund1 = null;
            try {
                refund1 = refundDataRepository.getRefund(refundClaim.getClaimNumber());
            } catch (Exception e) {
                logger.error("Not able to get refund Data");
                throw new OperationException("Not able to get refund data");
            }
            if (refund1 != null) {
                refund.setReasonForRequest(refund1.getRefundReason());
            }

        }


        Set<InvoiceDetail> invoiceDetail = refundClaim.getInvoiceDetail();
        refund.setInvoiceDetail(invoiceDetail);

        refund.setNetAmountRequest(refundClaim.getRefundDetail().getNetAmountPayable());

        refund.setOpsRemarks(refundClaim.getRefundDetail().getOpsRemarks());
        refund.setApprovalRemarks(refundClaim.getRefundDetail().getApprovalRemarks());

        refund.setProductDetail(refundClaim.getProductDetail());
        return refund;
    }


    @Override
    public Map changeRefundType(ChangeRefundTypeResponse changeRefundTypeResponse) throws OperationException {
        Map<String, Object> entity = new HashMap<>();
        String refundClaimNo = changeRefundTypeResponse.getRefundClaimNo();
        //BR 566
        if (changeRefundTypeResponse.getRefundTypes() != RefundTypes.REFUND_AMOUNT) {
            logger.error("Your are allowed to change refund Type, only Refund type Refund Amount is allowed");
            throw new OperationException("Your are allowed to change refund Type, only Refund type Refund Amount is allowed");
        }
        if (refundClaimNo != null) {
            Refund refund = get(refundClaimNo);
            if (refund != null) {
                RefundClaim refundClaim = refundFinanceService.getRefundClaim(refundClaimNo);

                if (refund.getRefundType().equals(RefundTypes.ACCOUNT_CREDIT)) {


                    BigDecimal myAccountBalance = refundFinanceService.getMyAccountBalance(refundClaim);
                    if (refundClaim.getRefundDetail().getRefundAmount().compareTo(myAccountBalance) >= 0) {

                        logger.error(Constants.REFUND_USED_BY_CUSTOMER);
                        throw new OperationException(Constants.REFUND_USED_BY_CUSTOMER);
                    }
                } else if (refund.getRefundType().equals(RefundTypes.REFUND_REDEEMABLE)) {
                    String creditNoteNo = refund.getCreditNoteNo();
                    Boolean consumed = refundFinanceService.isPromoUsed(creditNoteNo);

                    if (consumed) {
                        logger.error(Constants.REFUND_USED_BY_CUSTOMER);
                        throw new OperationException(Constants.REFUND_USED_BY_CUSTOMER);

                    }
                }


                ChangeType changeType = new ChangeType();
                changeType.setClaimNo(refund.getClaimNo());
                changeType.setOldRefundType(refund.getRefundType());
                changeType.setNewRefundType(changeRefundTypeResponse.getRefundTypes());
                changeType.setOpsRemark(changeRefundTypeResponse.getOpsRemark());

                //create alert to finance user
                refundCommunicationService.sendAlert(refund.getClaimNo());
                //creating to_do task to finance user
                ToDoTaskResource toDoTaskResource = new ToDoTaskResource();
                toDoTaskResource.setReferenceId(refund.getClaimNo()); //OPS DB ClientCommercialClaimNO
                toDoTaskResource.setBookingRefId(refund.getBookingReferenceNo());
                String userID = userService.getLoggedInUserId();
                toDoTaskResource.setCreatedByUserId(userID);

                toDoTaskResource.setProductId(refund.getProductDetail().getProductName());
                toDoTaskResource.setClientId(refund.getClientId());
                toDoTaskResource.setClientCategoryId(refundClaim.getClientDetail().getClientCategory());
                toDoTaskResource.setClientSubCategoryId(refundClaim.getClientDetail().getClientSubCategory());
                toDoTaskResource.setClientTypeId(refundClaim.getClientDetail().getClientType().name());
                toDoTaskResource.setCompanyId(refundClaim.getCompanyDetail().getCompanyId());
                toDoTaskResource.setCompanyMarketId(refundClaim.getCompanyDetail().getMarket());
                toDoTaskResource.setTaskNameId(ToDoTaskNameValues.APPROVE.getValue());
                toDoTaskResource.setTaskTypeId("Main task");
                toDoTaskResource.setTaskSubTypeId("REFUND"); //AMEND_CLIENT_COMMERCIAL
                toDoTaskResource.setTaskPriorityId(ToDoTaskPriorityValues.HIGH.getValue());
                // Dummy value HIGH
                toDoTaskResource.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue()); // OPERATIONS or FINANCE
                toDoTaskResource.setTaskStatusId(ToDoTaskStatusValues.ASSIGNED.getValue()); //ASSIGN as per my assumption
                toDoTaskResource.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.toString());

                toDoTaskResource.setSuggestedActions("");//sent the rest end point for now

                //toDoTaskResource.setDueOnDate(getKpi(refund.getClientId()).toString());
                ToDoTask toDoTask = null;
                try {
                    toDoTask = toDoTaskService.save(toDoTaskResource);

                } catch (ParseException e) {
                    throw new OperationException("Unable to create to do task");
                } catch (IllegalAccessException | InvocationTargetException | JSONException | IOException e) {
                    logger.error("Unable to create to do task", e);
                    throw new OperationException("Unable to create to do task");
                }

                changeType.setToDoTaskNo(toDoTask.getId());
                logger.info("Change Request " + changeType);
                try {
                    changeTypeRefundRepository.saveAndUpdateChangeType(changeType);
                } catch (Exception e) {
                    logger.error("Not able to create change request", e);
                    throw new OperationException("Not able to create change request");
                }


                entity.put("success", "Send to Approval");

            } else {
                logger.error(refundClaimNo + " not found");
                throw new OperationException(Constants.ER01, refundClaimNo + " no record found");
            }
        } else {
            logger.error(refundClaimNo + " no found");
            throw new OperationException(Constants.ER01, "");
        }
        return entity;
    }

    /* approval flow for change refund type*/
    @Override
    public Map<String, String> approval(ApprovalResource approvalResource) throws OperationException {
        ChangeType changeTypeApproval = changeTypeRefundRepository.getChangeTypeByClaimNo(approvalResource.getClaimNo());
        if (changeTypeApproval == null) {
            throw new OperationException("No approval request found for " + approvalResource.getClaimNo());
        }
        changeTypeApproval.setApprovalRemark(approvalResource.getApprovalRemark());
        changeTypeApproval.setApprovalStatus(approvalResource.getApprovalStatus());
        ChangeType changeType = null;
        Map<String, String> entity = new HashMap<>();
        try {
            changeType = changeTypeRefundRepository.saveAndUpdateChangeType(changeTypeApproval);
        } catch (Exception e) {
            logger.error("Not able to save approval for refund using " + approvalResource);
            throw new OperationException("Not able to save approval for refund using " + approvalResource);
        }
        if (changeType != null) {

            if (changeType.getApprovalStatus() == ApprovalStatus.Accepted) {

                RefundClaim refundClaim = refundFinanceService.getRefundClaim(changeType.getClaimNo());

                if (!refundClaim.isLocked() && (refundClaim.getRefundDetail().getStatus() != ClaimStatus.Paid || refundClaim.getRefundDetail().getRefundType() != RefundTypes.REFUND_AMOUNT)) {


                    //updated new approval refund type

                    if (refundClaim.getRefundDetail().getRefundType() == RefundTypes.REFUND_REDEEMABLE) {
                        //deactivate the promo code
                        //get credit note and update noteStatus
                        CreditDebitNote creditDebitNote = null;
                        try {
                            ObjectMapper objMapper = new ObjectMapper();
                            objMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                            creditDebitNote = objMapper.readValue(getCreditNote(refundClaim.getRedeemableCreditNote()), CreditDebitNote.class);
                            if (creditDebitNote.getStatus().equalsIgnoreCase("REVERSED")) {
                                logger.info("Credit Note " + creditDebitNote.getId() + " is already reversed");
                            } else {

                                creditDebitNote.setStatus("REVERSED");
                                creditDebitNote.setIsFromUI(false);
                                refundFinanceService.updateCreditNote(creditDebitNote);
                            }
                        } catch (IOException e) {
                            throw new OperationException("Unable to parse json object");
                        }


                    } else if (refundClaim.getRefundDetail().getRefundType() == RefundTypes.ACCOUNT_CREDIT) {
                        logger.info("In account credit for approval");

                        refundClaim.setReduceAccountCredit(true);


                    }


                    try {
                        //call to finance and update
                        refundClaim.setRequestAfterTypeChange(true);
                        refundClaim.getRefundDetail().setRefundType(changeType.getNewRefundType());
                        refundClaim.setRefundUserStatus(RefundStatus.PENDING_WITH_FINANCE);
                        refundClaim.getRefundDetail().setStatus(ClaimStatus.Pending_Payment);
                        refundFinanceService.updateRefundClaim(refundClaim);

                        logger.info("Refund updated after approval " + refundClaim);
                        logger.info("Record is updated " + approvalResource.getClaimNo());
                        entity.put("message", "Refund is Approved " + approvalResource.getClaimNo());
                        refundCommunicationService.sendApproverAlert(refundClaim.getClaimNumber());

                    } catch (Exception e) {
                        logger.error("Not able to update refund", e);
                        throw new OperationException("Not able to update refund");
                    }

                } else {
                    entity.put("message", "Refund is Rejected because it is processed in finance " + approvalResource.getClaimNo());
                }

            } else {
                entity.put("message", "Refund is Rejected by approver" + approvalResource.getClaimNo());
            }

        } else {
            logger.error("Not able to update approval record " + approvalResource);
            entity.put("error", "Not able to update approval record " + approvalResource.getClaimNo());
        }
        try {
            if (null != changeType) {
                ToDoTask toDoTask = toDoTaskService.getById(changeType.getToDoTaskNo());
                toDoTask.setTaskStatus(ToDoTaskStatusValues.CLOSED);
                toDoTaskRepository.saveOrUpdate(toDoTask);
            } else {
                logger.error("Unable to close todo task because changeType is null");
            }


        } catch (IOException e) {
            logger.error(e);
        }

        return entity;
    }

    @Override
    public String getCreditNote(String crediNoteNo) throws OperationException {


        return refundFinanceService.getGetCreditNote(crediNoteNo);
    }


    @Override
    public List<Refund> refundClaimsByBookingReferenceNo(String bookingReferenceNo) throws OperationException {
        List<Refund> refunds = null;
        RestTemplate restTemplate = RestUtils.getTemplate();

        List<RefundClaim> refundClaims = refundFinanceService.getRefundByBookingReferenceNo(bookingReferenceNo, restTemplate);

        if (refundClaims != null) {
            refunds = new ArrayList<>();

            for (RefundClaim refundClaim : refundClaims) {
                Refund refund = null;
                refund = copyRefundClaimInRefund(refundClaim);
                refunds.add(refund);
            }
        } else {
            logger.error(bookingReferenceNo + " No refund found ");
            //NO RECORD found throw exception
            throw new OperationException(Constants.ER01);

        }
        Collections.sort(refunds, new RefundComparator());
        return refunds;

    }

    @Override
    public ClientDetail getClientById(String clientId, String clientType) throws OperationException {
        ClientDetail clientDetail = new ClientDetail();
        if ("B2C".equalsIgnoreCase(clientType)) {
            String clientResponse = refundMDMService.getB2CClient(clientId);
            if (null != clientResponse) {
                //create client Details
                String firstName = jsonObjectProvider.getAttributeValue(clientResponse, "$.travellerDetails.employee.firstName", String.class);
                String lastName = jsonObjectProvider.getAttributeValue(clientResponse, "$.travellerDetails.employee.lastName", String.class);
                clientDetail.setClientName(firstName.concat(" ").concat(lastName));

            }

        } else if ("B2B".equalsIgnoreCase(clientType)) {
            String clientResponse;
            try {
                clientResponse = refundMDMService.getB2BClient(clientId);
            } catch (Exception e) {
                logger.error(e);
                throw new OperationException("Not able to get client details " + e.getMessage());
            }


            clientDetail.setClientName(jsonObjectProvider.getAttributeValue(clientResponse, "$.clientProfile.clientDetails.clientName", String.class));
            clientDetail.setClientCategory(jsonObjectProvider.getAttributeValue(clientResponse, "$.clientProfile.clientDetails.clientCategory", String.class));
            clientDetail.setClientSubCategory(jsonObjectProvider.getAttributeValue(clientResponse, "$.clientProfile.clientDetails.clientSubCategory", String.class));
        }
        if (clientDetail.getClientName() == null || clientDetail.getClientName().length() == 0) {
            throw new OperationException("Not able to get client details client Id " + clientId);
        }
        clientDetail.setClientId(clientId);
        clientDetail.setClientType(ClientType.B2B);


        return clientDetail;
    }

    @Override
    public MessageResource sendMailToClient(SendMailResource sendMailResource) throws OperationException {
        String clientEmail = null;
        String clientName = null;
        if ("B2B".equalsIgnoreCase(sendMailResource.getClientType())) {
            clientEmail = refundMDMService.getB2BClientEmail(sendMailResource.getClientId());
            clientName = clientMasterDataService.getB2BClientNames(Collections.singletonList(sendMailResource.getClientId())).get(sendMailResource.getClientId());
        } else {
            clientEmail = refundMDMService.getB2CClientEmail(sendMailResource.getClientId());
            clientName = clientMasterDataService.getB2CClientNames(Collections.singletonList(sendMailResource.getClientId())).get(sendMailResource.getClientId());
        }
        MessageResource messageResource = new MessageResource();
        Refund refund = get(sendMailResource.getClaimNo());
        boolean isEmailSent = false;
        if (refund.getRefundType() == RefundTypes.REFUND_REDEEMABLE) {
            String creditNoteNo = refund.getCreditNoteNo();
            isEmailSent = refundCommunicationService.sendMailToClient(clientEmail, clientName, sendMailResource.getBookingNo(), sendMailResource.getOrderNo(), creditNoteNo);
        } else {
            isEmailSent = refundCommunicationService.sendMailToClient(clientEmail, clientName, sendMailResource.getBookingNo(), sendMailResource.getOrderNo());

        }
        if (isEmailSent) {
            messageResource.setCode("SUCCESS");
            messageResource.setMessage("Email sent to client");
        } else {
            messageResource.setCode("Error");
            messageResource.setMessage("Not able to send mail client " + clientEmail);

        }
        return messageResource;
    }

    //client KPI date
    private ZonedDateTime getKpi(String clientId) throws OperationException {

        KpiDefinition kpiDefinition = mdmService.getKPIDate(clientId);

        if (null == kpiDefinition) {
            throw new OperationException("KPI is not configured for client " + clientId);
        }

        int days = Integer.parseInt(kpiDefinition.getKpiMeasure().getAchievingPeriod().getValue());
        String[] time = kpiDefinition.getKpiMeasure().getAchievingPeriod().getTime().split(":");
        long hour = Long.parseLong(time[0]);
        long minutes = Long.parseLong(time[1]);
        ZonedDateTime currentTime = ZonedDateTime.now();
        ZonedDateTime kpiDateTime = currentTime.plusDays(days).plusMinutes(minutes).plusHours(hour);


        return DateTimeUtil.formatBEDateTimeZone(kpiDateTime.toString());


    }

    /**
     * This method will provide default refund configuration
     *
     * @param clientId
     * @param clientType
     * @return
     * @throws OperationException
     */
    @Override
    public RefundTypes getDefaultRefundType(String clientId, ClientType clientType) throws OperationException {

        RefundConfiguration refundConfiguration = new RefundConfiguration();

        ClientDetail clientDetail = getClientById(clientId, clientType.getClientType());

        refundConfiguration.setClientName(clientDetail.getClientName());
        refundConfiguration.setClientType(clientType.getClientType());
        refundConfiguration.setClientCategory(clientDetail.getClientCategory());
        refundConfiguration.setClientSubCategory(clientDetail.getClientSubCategory());
        String clientGroupId = null;
        if (clientType == ClientType.B2B) {
            String b2BClientDetails = clientMasterDataService.getB2BClientDetails(clientId);
            clientGroupId = jsonObjectProvider.getAttributeValue(b2BClientDetails, "$.generalInfo.clientGroupId", String.class);
            String[] clientGroup = refundMDMService.getClientGroup(clientGroupId);

            refundConfiguration.setCompanyMarket(clientGroup[1]);
            refundConfiguration.setClientGroup(clientGroup[0]);
        } else {

            refundConfiguration.setCompanyMarket(null);
            refundConfiguration.setClientGroup(null);

        }


        return refundMDMService.getRefundConfiguration(refundConfiguration);
    }

    @Override
    public ChangeType getDefaultRefundType(String refundClaim) {
        return changeTypeRefundRepository.getChangeTypeByClaimNo(refundClaim);
    }


}
