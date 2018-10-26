package com.coxandkings.travel.operations.service.prepaymenttosupplier.paymentadvice.impl;

import com.coxandkings.travel.operations.criteria.prepaymenttosupplier.PaymentCriteria;
import com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability.ServiceOrderSearchCriteria;
import com.coxandkings.travel.operations.enums.commercialStatements.CommercialType;
import com.coxandkings.travel.operations.enums.commercialStatements.SettlementStatus;
import com.coxandkings.travel.operations.enums.prepaymenttosupplier.PaymentAdviceStatusValues;
import com.coxandkings.travel.operations.enums.serviceOrderAndSupplierLiability.ServiceOrderAndSupplierLiabilityType;
import com.coxandkings.travel.operations.enums.todo.*;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.commercialstatements.SupplierCommercialStatement;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsBookingStatus;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice.PaymentAdvice;
import com.coxandkings.travel.operations.model.productbookedthrother.mdm.B2BClient;
import com.coxandkings.travel.operations.model.productbookedthrother.mdm.B2CClient;
import com.coxandkings.travel.operations.model.todo.ToDoTask;
import com.coxandkings.travel.operations.repository.prepaymenttosupplier.paymentadvice.PaymentAdviceRepository;
import com.coxandkings.travel.operations.resource.commercialStatement.CommercialStatementSearchCriteria;
import com.coxandkings.travel.operations.resource.notification.InlineMessageResource;
import com.coxandkings.travel.operations.resource.notification.NotificationResource;
import com.coxandkings.travel.operations.resource.prepaymenttosupplier.SupplierPayableReceivableAmt;
import com.coxandkings.travel.operations.resource.prepaymenttosupplier.paymentadvice.PaymentAdviceResource;
import com.coxandkings.travel.operations.resource.prepaymenttosupplier.paymentadvice.SupplierPaymentResource;
import com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability.ServiceOrderResource;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResource;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.commercialstatements.SupplierCommercialStatementService;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.loader.PaymentAdviceFinanceLoaderService;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.loader.PaymentAdviseMDMLoaderService;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.paymentadvice.PaymentAdviceService;
import com.coxandkings.travel.operations.service.productbookedthrother.mdm.MdmClientService;
import com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.ServiceOrderAndSupplierLiabilityService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.systemlogin.MDMToken;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.CopyUtils;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.coxandkings.travel.operations.utils.supplier.SupplierDetailsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service(value = "PrePaymentService")
public class PaymentAdviceServiceImpl implements PaymentAdviceService {

    private static final Logger logger = LogManager.getLogger(PaymentAdviceServiceImpl.class);

    @Autowired
    PaymentAdviceRepository paymentAdviceRepository;

    @Autowired
    ToDoTaskService toDoTaskService;

    @Autowired
    AlertService alertService;

    @Value(value = "${pre-payment-to-supplier.mdm.supplier-settlement}")
    private String prepaymentUrl;

    @Value(value = "${booking_engine.base_url}")
    private String bookingUrl;

    @Autowired
    private PaymentAdviceFinanceLoaderService paymentAdviceFinanceLoaderService;

    @Autowired
    private PaymentAdviseMDMLoaderService paymentAdviseMDMLoaderService;

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private MdmClientService mdmClientService;

    private ToDoTask toDoTask;
    @Autowired
    private UserService userService;
    @Autowired
    private MDMToken mdmToken;

    @Value("${payment_advice.notify_for_accounting_entry}")
    private String notifyFinanceForAccountingEntryUrl;

    @Value("${pre_payment_to_supplier.alert.approval.alertName}")
    private String approvalAlertName;

    @Value("${pre_payment_to_supplier.alert.reject.alertName}")
    private String rejectAlertName;

    @Value(value = "${pre_payment_to_supplier.alert.notificationType}")
    private String notificationType;

    @Autowired
    private ServiceOrderAndSupplierLiabilityService serviceOrderAndSupplierLiabilityService;

    @Autowired
    private SupplierDetailsService supplierDetailsService;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Autowired
    private SupplierCommercialStatementService supplierCommercialStatementService;


    @Override
    public PaymentAdvice savePaymentAdvice(SupplierPaymentResource resource) throws OperationException {
        PaymentAdvice paymentAdvice = null;
        Boolean isAmountPayable = false;
        BigDecimal balanceAmt = new BigDecimal("0");
        PaymentAdviceStatusValues status = null;
        PaymentAdviceResource paymentAdviceResource = resource.getPaymentAdviceResource();

        if (resource.getId() == null || resource.getId().isEmpty()) // to create payment advise
        {
            BigDecimal balanceAmtPayableToSupplier = resource.getBalanceAmtPayableToSupplier();
            int compareBalWithPayable = balanceAmtPayableToSupplier.compareTo(resource.getNetPayableToSupplier());
            if (compareBalWithPayable == 0 || compareBalWithPayable == -1) {
                int i = balanceAmtPayableToSupplier.compareTo(balanceAmt);
                if (i == 0) {
                    throw new OperationException(Constants.OPS_ERR_21405);
                } else {
                    isAmountPayable = compareAmount(resource.getBalanceAmtPayableToSupplier(), resource.getPaymentAdviceResource().getAmountPayableForSupplier());
                    if (isAmountPayable) {
                        paymentAdvice = new PaymentAdvice();
                        if (paymentAdviceResource.getPaymentAdviceStatusId() != null) {
                            paymentAdvice.setPaymentAdviceStatus(paymentAdviceResource.getPaymentAdviceStatusId());
                        }
                    } else {
                        throw new OperationException(Constants.OPS_ERR_21402);
                    }
                    if (resource.getSupplierCurrency().equalsIgnoreCase(resource.getPaymentAdviceResource().getSelectedSupplierCurrency())) {
                        CopyUtils.copy(resource, paymentAdvice);
                        CopyUtils.copy(paymentAdviceResource, paymentAdvice);
                        paymentAdvice.setDocumentReferenceId(resource.getDocumentReferenceId());
                        paymentAdvice.setPaymentAdviceNumber(getNewPaymentAdviceID());
                /*PaymentDetails paymentDetails=new PaymentDetails();
                paymentAdvice.setPaymentDetails(paymentDetails);*/
                        paymentAdvice.setPaymentAdviceGenerationDate(ZonedDateTime.now());
                        paymentAdvice = paymentAdviceRepository.savePaymentAdvice(paymentAdvice);
                    } else {
                        throw new OperationException(Constants.OPS_ERR_21406);
                    }

                }
            } else {
                throw new OperationException(Constants.OPS_ERR_21407);
            }
        }

        //Create To-DO Task
        if (paymentAdvice.getPaymentAdviceStatus().equals(PaymentAdviceStatusValues.APPROVAL_PENDING)) {
            try {
                toDoTask = toDoTaskService.save(getTodoForOpsApproval(paymentAdvice));
                paymentAdvice.setApproverToDoTaskId(toDoTask.getId());
                paymentAdvice = paymentAdviceRepository.updatePaymentAdvice(paymentAdvice);
                logger.info("todo task id " + toDoTask.getId());
            } catch (Exception e) {
                logger.debug("Error occured in saving todo task");
//                throw new OperationException(Constants.OPS_ERR_21403);
            }
        }
        return paymentAdvice;
    }

    @Override
    public PaymentAdvice updatePaymentAdvice(SupplierPaymentResource resource) throws OperationException {
        PaymentAdvice paymentAdvice = null;
        Boolean isNetPayableGreaterThanAmountPayable = false;
        BigDecimal balanceAmt = new BigDecimal("0");
        PaymentAdviceResource paymentAdviceResource = resource.getPaymentAdviceResource();

        if (resource.getId() != null || !resource.getId().isEmpty()) {
            PaymentAdvice existinDetails = paymentAdviceRepository.getById(resource.getId());
            if (existinDetails != null) {
                BigDecimal balanceAmtPayableToSupplier = resource.getBalanceAmtPayableToSupplier();
                int i = balanceAmtPayableToSupplier.compareTo(balanceAmt);
                if (i == 0) {
                    isNetPayableGreaterThanAmountPayable = compareAmount(resource.getNetPayableToSupplier(), resource.getPaymentAdviceResource().getAmountPayableForSupplier());

                    if (isNetPayableGreaterThanAmountPayable) {
                        paymentAdvice = new PaymentAdvice();
                        if (paymentAdviceResource.getPaymentAdviceStatusId() != null) {

                            paymentAdvice.setPaymentAdviceStatus(paymentAdviceResource.getPaymentAdviceStatusId());
                        }
                    } else {
                        throw new OperationException(Constants.OPS_ERR_21401);
                    }
                    CopyUtils.copy(resource, paymentAdvice);
                    CopyUtils.copy(paymentAdviceResource, paymentAdvice);
//                   paymentAdvice.setPaymentAdviceNumber(paymentAdviceResource.getPaymentAdviceNumber());
                    paymentAdvice.setPaymentAdviceGenerationDate(ZonedDateTime.now());
                    paymentAdvice = paymentAdviceRepository.updatePaymentAdvice(paymentAdvice);
                } else {
                    isNetPayableGreaterThanAmountPayable = compareAmount(resource.getBalanceAmtPayableToSupplier(), resource.getPaymentAdviceResource().getAmountPayableForSupplier());
                    if (isNetPayableGreaterThanAmountPayable) {
                        paymentAdvice = new PaymentAdvice();
                        if (paymentAdviceResource.getPaymentAdviceStatusId() != null) {
                            paymentAdvice.setPaymentAdviceStatus(paymentAdviceResource.getPaymentAdviceStatusId());
                        }
                    } else {
                        throw new OperationException(Constants.OPS_ERR_21402);
                    }
                    CopyUtils.copy(resource, paymentAdvice);
                    CopyUtils.copy(paymentAdviceResource, paymentAdvice);
                    paymentAdvice.setPaymentAdviceGenerationDate(ZonedDateTime.now());
                    paymentAdvice = paymentAdviceRepository.updatePaymentAdvice(paymentAdvice);

                }
            } else {
                throw new OperationException(Constants.ER01);
            }
        }

        //Create To-DO Task
        if (paymentAdvice.getPaymentAdviceStatus().equals(PaymentAdviceStatusValues.APPROVAL_PENDING)) {
            try {
                try {
                    toDoTask = toDoTaskService.save(getTodoForOpsApproval(paymentAdvice));
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            } catch (OperationException e) {
                logger.error("Unable to create Approval toDo Task", e);
                throw new OperationException(Constants.OPS_ERR_21403);
            } catch (ParseException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return paymentAdvice;
    }

    @Override
    public List<PaymentAdvice> searchSupplierPayment(PaymentCriteria paymentCriteria) throws OperationException {
        List<PaymentAdvice> supplierPaymentDetailNews = paymentAdviceRepository.searchPaymentAdvise(paymentCriteria);


        if (supplierPaymentDetailNews == null) {
            throw new OperationException(Constants.ER01);
        }
        return supplierPaymentDetailNews;
    }


    @Override
    public PaymentAdvice getById(String id) throws OperationException {
        PaymentAdvice paymentAdvice = paymentAdviceRepository.getById(id);
        if (paymentAdvice == null)
            throw new OperationException(Constants.ER01);
        return paymentAdvice;
    }

    @Override
    public PaymentAdvice approvePaymentAdvise(String paymentAdviceNumber, String remarks) throws OperationException, JSONException, ParseException {
        PaymentCriteria paymentCriteria = new PaymentCriteria();
        PaymentAdvice paymentAdvice = null;
        String toDoId = null;
        ToDoTask toDoTask = null;
        paymentCriteria.setPaymentAdviceNumber(paymentAdviceNumber);
        List<PaymentAdvice> paymentAdviceList = paymentAdviceRepository.searchPaymentAdvise(paymentCriteria);

        if (paymentAdviceList != null && paymentAdviceList.size() > 0) {
            for (PaymentAdvice paymentDetailsNew : paymentAdviceList)
            {
                if (PaymentAdviceStatusValues.APPROVAL_PENDING.equals(paymentDetailsNew.getPaymentAdviceStatus()))
                {
                CopyUtils.copy(paymentAdviceList, paymentDetailsNew);
                JSONObject jsonObject = new JSONObject(remarks);
                String remarkValue = (String) jsonObject.get("remarks");
                paymentDetailsNew.setApproverRemark(remarkValue);
                paymentDetailsNew.setPaymentAdviceStatus(PaymentAdviceStatusValues.APPROVED);
                paymentAdvice = paymentAdviceRepository.updatePaymentAdvice(paymentDetailsNew);
                toDoId = paymentAdvice.getApproverToDoTaskId();
                try {
                    toDoTask = toDoTaskService.getById(toDoId);
                } catch (Exception e) {
                    logger.error("IOException in TODo task");
                }
                    if (paymentAdvice.getPaymentAdviceStatementInfoSet().isEmpty()) {
                        if (StringUtils.isEmpty(paymentAdvice.getPaymentAdviceOrderInfoSet().iterator().next().getServiceOrderId())) {
                            toDoTaskService.updateToDoTaskStatus(toDoTask.getReferenceId(), ToDoTaskSubTypeValues.PAYMENT_ADVICE, ToDoTaskStatusValues.CLOSED);
                        } else {
                            toDoTaskService.updateToDoTaskStatus(toDoTask.getReferenceId(), ToDoTaskSubTypeValues.PAYMENT_ADVICE_SUPPLIER_BILL_PASSING, ToDoTaskStatusValues.CLOSED);
                        }
                    }
                    else toDoTaskService.updateToDoTaskStatus(toDoTask.getReferenceId(),ToDoTaskSubTypeValues.SUPPLIER_COMMERCIAL_STATEMENT_PAYMENT_ADVICE,ToDoTaskStatusValues.CLOSED);
                    notifyFinanceAfterPaymentAdviceApproval(paymentAdvice.getId());
                }
                else {
                    throw new OperationException(Constants.OPS_ERR_21400, String.valueOf(paymentDetailsNew.getPaymentAdviceStatus()));
                }
            }
        } else {
            throw new OperationException(Constants.RECORD_NOT_FOUND, paymentAdviceNumber);
        }

        if (paymentAdvice.getPaymentAdviceStatus().equals(PaymentAdviceStatusValues.APPROVED)) {
            InlineMessageResource inlineMessageResource = new InlineMessageResource();
            inlineMessageResource.setAlertName(approvalAlertName);
            inlineMessageResource.setNotificationType(notificationType);
            ConcurrentHashMap<String, String> dynamicVariables = new ConcurrentHashMap<>();
            if (paymentAdvice.getPaymentAdviceStatementInfoSet().isEmpty()) {
                dynamicVariables.put("bookingRefId", paymentAdvice.getPaymentAdviceOrderInfoSet().iterator().next().getBookingRefId());
                dynamicVariables.put("orderId", paymentAdvice.getPaymentAdviceOrderInfoSet().iterator().next().getOrderId());
            }
            dynamicVariables.put("paymentAdviceNumber", paymentAdvice.getPaymentAdviceNumber());
            inlineMessageResource.setDynamicVariables(dynamicVariables);
            try {
                NotificationResource notificationResource = alertService.sendInlineMessageAlert(inlineMessageResource);
                if (!StringUtils.isEmpty(notificationResource.get_id())) {
                    logger.info("Alert has been sent");
                }
            }
            catch (Exception e)
            {
                logger.debug("alert not sent");
                throw new OperationException(Constants.OPS_ERR_21408, paymentAdvice.getPaymentAdviceNumber());
            }

            finally
            {
                try {
                    toDoTask = toDoTaskService.save(getTodoForFinance(paymentAdvice));
                    paymentAdvice.setFinanceTodoTaskId(toDoTask.getId());
                    paymentAdvice = paymentAdviceRepository.updatePaymentAdvice(paymentAdvice);

                } catch (Exception e) {
                    logger.error("Unable to create Finance To-Do Task", e);
                    throw new OperationException(Constants.OPS_ERR_21404);
                }
            }


        }
        return paymentAdvice;
    }

    private void notifyFinanceAfterPaymentAdviceApproval(String paymentAdviceId) {
        logger.info(" Notifying Finance after PaymentAdvice approved");
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", mdmToken.getToken());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("paymentAdviceId", paymentAdviceId);
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity httpEntity = new HttpEntity(jsonObject.toString(), httpHeaders);
            RestTemplate restTemplate = RestUtils.getTemplate();
            logger.info("calling " + notifyFinanceForAccountingEntryUrl);

            restTemplate.exchange(notifyFinanceForAccountingEntryUrl, HttpMethod.POST, httpEntity, String.class);
        } catch (Exception e) {
            logger.error("Error while Notifying Finance after PaymentAdvice approved ");

        }
    }

    @Override
    public PaymentAdvice rejected(String paymentAdviceNumber, String remarks) throws JSONException, OperationException, ParseException, InvocationTargetException, IllegalAccessException {
        PaymentCriteria paymentCriteria = new PaymentCriteria();
        PaymentAdvice paymentAdvice = new PaymentAdvice();
        paymentCriteria.setPaymentAdviceNumber(paymentAdviceNumber);

        List<PaymentAdvice> paymentAdviceList = paymentAdviceRepository.searchPaymentAdvise(paymentCriteria);
        if (paymentAdviceList != null && paymentAdviceList.size() > 0)
        {
                for (PaymentAdvice paymentDetailsNew : paymentAdviceList)
                {
                    if (PaymentAdviceStatusValues.APPROVAL_PENDING.equals(paymentDetailsNew.getPaymentAdviceStatus()))
                    {
                        CopyUtils.copy(paymentAdviceList, paymentDetailsNew);
                        JSONObject jsonObject = new JSONObject(remarks);
                        String remarkValue = (String) jsonObject.get("remarks");
                        paymentDetailsNew.setApproverRemark(remarkValue);
                        paymentDetailsNew.setPaymentAdviceStatus(PaymentAdviceStatusValues.REJECTED);
                        paymentAdvice = paymentAdviceRepository.updatePaymentAdvice(paymentDetailsNew);
                        try {
                            toDoTask = toDoTaskService.getById(paymentDetailsNew.getApproverToDoTaskId());
                        } catch (IOException e) {
                            logger.error("Todo task not found for TODO Task Id: " + paymentDetailsNew.getApproverToDoTaskId());
                        }
                        if (paymentAdvice.getPaymentAdviceStatementInfoSet().isEmpty()) {
                            if (StringUtils.isEmpty(paymentAdvice.getPaymentAdviceOrderInfoSet().iterator().next().getServiceOrderId())) {
                                toDoTaskService.updateToDoTaskStatus(toDoTask.getReferenceId(), ToDoTaskSubTypeValues.PAYMENT_ADVICE, ToDoTaskStatusValues.CLOSED);
                            } else {
                                toDoTaskService.updateToDoTaskStatus(toDoTask.getReferenceId(), ToDoTaskSubTypeValues.PAYMENT_ADVICE_SUPPLIER_BILL_PASSING, ToDoTaskStatusValues.CLOSED);
                            }
                        }
                        else toDoTaskService.updateToDoTaskStatus(toDoTask.getReferenceId(),ToDoTaskSubTypeValues.SUPPLIER_COMMERCIAL_STATEMENT_PAYMENT_ADVICE,ToDoTaskStatusValues.CLOSED);
                    }
                    else
                    {
                        throw new OperationException(Constants.OPS_ERR_21400, String.valueOf(paymentDetailsNew.getPaymentAdviceStatus()));
                    }
                }
        }
        else {
            throw new OperationException(Constants.OPS_ERR_21411, paymentAdviceList.get(0).getPaymentAdviceNumber());
        }
        if (paymentAdvice.getPaymentAdviceStatus().equals(PaymentAdviceStatusValues.REJECTED)) {
            InlineMessageResource inlineMessageResource = new InlineMessageResource();
            inlineMessageResource.setAlertName(rejectAlertName);
            inlineMessageResource.setNotificationType(notificationType);
            ConcurrentHashMap<String, String> dynamicVariables = new ConcurrentHashMap<>();
            if (paymentAdvice.getPaymentAdviceStatementInfoSet().isEmpty()) {
                dynamicVariables.put("bookingRefId", paymentAdvice.getPaymentAdviceOrderInfoSet().iterator().next().getBookingRefId());
                dynamicVariables.put("orderId", paymentAdvice.getPaymentAdviceOrderInfoSet().iterator().next().getOrderId());
            }
            dynamicVariables.put("paymentAdviceNumber", paymentAdvice.getPaymentAdviceNumber());
            inlineMessageResource.setDynamicVariables(dynamicVariables);
            try {
                NotificationResource notificationResource = alertService.sendInlineMessageAlert(inlineMessageResource);
                if (!StringUtils.isEmpty(notificationResource.get_id())) {
                    logger.info("Alert has been sent");
                }
            }
            catch (Exception e)
            {
                logger.debug("alert not sent");
                throw new OperationException(Constants.OPS_ERR_21409, paymentAdvice.getPaymentAdviceNumber());
            }
            finally {
                try {
                    toDoTask = toDoTaskService.save(getTodoForOps(paymentAdvice));
                    paymentAdvice.setApproverToDoTaskId(toDoTask.getId());
                    paymentAdvice = paymentAdviceRepository.updatePaymentAdvice(paymentAdvice);
                } catch (Exception e) {
                    logger.error("Unable to create Ops user To-Do Task for Updation of Payment Advice", e);
//                throw new OperationException(Constants.OPS_ERR_21404);
                }
            }
        }
        return paymentAdvice;
    }


    @Override
    public void updateProductForAction(OpsBooking opsBooking) {
        if (opsBooking.getStatus().equals(OpsBookingStatus.CNF.toString())) {

        } else {

        }
//        product.getProductLevelActions( ).put( ProductActions.PRE_PAYMENT_TO_SUPPLIER.getActionType( ) , false );
    }


    //To Compare 2 values
    public Boolean compareAmount(BigDecimal balanceAmtPayableToSupplier, BigDecimal amountPayableForSupplier) {
        int compare = balanceAmtPayableToSupplier.compareTo(amountPayableForSupplier);
        if (compare == 1 || compare == 0)
            return true;
        else
            return false;
    }


    public ToDoTaskResource getTodoForOpsApproval(PaymentAdvice paymentAdvice) throws OperationException, ParseException {
        ToDoTaskResource toDoTaskResource = new ToDoTaskResource();
        B2BClient b2BClient = new B2BClient();
        B2CClient b2CClient = new B2CClient();

        toDoTaskResource.setCreatedByUserId(userService.getLoggedInUserId());
        toDoTaskResource.setReferenceId(paymentAdvice.getPaymentAdviceNumber());
        if (StringUtils.isEmpty(paymentAdvice.getPaymentAdviceStatementInfoSet())){
            OpsBooking opsBooking = opsBookingService.getBooking(paymentAdvice.getPaymentAdviceOrderInfoSet().iterator().next().getBookingRefId());
            toDoTaskResource.setClientTypeId(opsBooking.getClientType());
            toDoTaskResource.setCompanyId(opsBooking.getCompanyId());
            toDoTaskResource.setClientId(opsBooking.getClientID());
            if (opsBooking.getClientType().equalsIgnoreCase("B2B")) {
                b2BClient = mdmClientService.getB2bClient(opsBooking.getClientID());
                toDoTaskResource.setClientCategoryId(b2BClient.getClientCategory());
                toDoTaskResource.setCompanyMarketId(b2BClient.getCompanyId());//TODo: need to add in B2Bclient class
                toDoTaskResource.setClientSubCategoryId(b2BClient.getClientSubCategory());
            }
            if (opsBooking.getClientType().equalsIgnoreCase("B2C")) {
                b2CClient = mdmClientService.getB2cClient(opsBooking.getClientID());
                toDoTaskResource.setClientCategoryId(b2CClient.getClientCategory());
                toDoTaskResource.setCompanyMarketId(b2CClient.getCompanyId());//TODo: need to add in B2Bclient class
                toDoTaskResource.setClientSubCategoryId(b2CClient.getClientSubCategory());
            }
        }
//        TODO : Remove sample time and replace with original time
        toDoTaskResource.setDueOnDate(ZonedDateTime.now());
        toDoTaskResource.setTaskNameId(ToDoTaskNameValues.APPROVE.getValue());
        toDoTaskResource.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
//        toDoTaskResource.setTaskStatusId(ToDoTaskStatusValues.NEW.getValue());
        if (StringUtils.isEmpty(paymentAdvice.getPaymentAdviceStatementInfoSet())){
            if (StringUtils.isEmpty(paymentAdvice.getPaymentAdviceOrderInfoSet().iterator().next().getServiceOrderId()))
                toDoTaskResource.setTaskSubTypeId(ToDoTaskSubTypeValues.PAYMENT_ADVICE.toString());
            else
                toDoTaskResource.setTaskSubTypeId(ToDoTaskSubTypeValues.PAYMENT_ADVICE_SUPPLIER_BILL_PASSING.toString());
            toDoTaskResource.setBookingRefId(paymentAdvice.getPaymentAdviceOrderInfoSet().iterator().next().getBookingRefId());
        } else
            toDoTaskResource.setTaskSubTypeId(ToDoTaskSubTypeValues.SUPPLIER_COMMERCIAL_STATEMENT_PAYMENT_ADVICE.toString());
        //ToDoTaskSubTypeValues.PAYMENT_ADVICE.toString());
        toDoTaskResource.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());//ToDoFunctionalAreaValues.OPERATIONS.getValue()); //Ops Approver
        toDoTaskResource.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.name());
        toDoTaskResource.setTaskOrientedTypeId(ToDoTaskOrientedValues.APPROVAL_ORIENTED.getValue());

        return toDoTaskResource;
    }

    public ToDoTaskResource getTodoForFinance(PaymentAdvice paymentAdvice) throws OperationException, ParseException {
        ToDoTaskResource toDoTaskResource = new ToDoTaskResource();
        B2BClient b2BClient = new B2BClient();
        B2CClient b2CClient = new B2CClient();

        toDoTaskResource.setCreatedByUserId(userService.getLoggedInUserId());
        toDoTaskResource.setReferenceId(paymentAdvice.getPaymentAdviceNumber());

        if (paymentAdvice.getPaymentAdviceStatementInfoSet().isEmpty()) {
            OpsBooking opsBooking = opsBookingService.getBooking(paymentAdvice.getPaymentAdviceOrderInfoSet().iterator().next().getBookingRefId());
            toDoTaskResource.setProductId(getProductName(paymentAdvice.getPaymentAdviceOrderInfoSet().iterator().next().getBookingRefId(),paymentAdvice.getPaymentAdviceOrderInfoSet().iterator().next().getOrderId()));
            toDoTaskResource.setClientTypeId(opsBooking.getClientType());
            toDoTaskResource.setCompanyId(opsBooking.getCompanyId());
            toDoTaskResource.setClientId(opsBooking.getClientID());
            if (opsBooking.getClientType().equalsIgnoreCase("B2B")) {
                b2BClient = mdmClientService.getB2bClient(opsBooking.getClientID());
                toDoTaskResource.setClientCategoryId(b2BClient.getClientCategory());
                toDoTaskResource.setCompanyMarketId(b2BClient.getCompanyId());//TODo: need to add in B2Bclient class
                toDoTaskResource.setClientSubCategoryId(b2BClient.getClientSubCategory());
            }

            if (opsBooking.getClientType().equalsIgnoreCase("B2C")) {
                b2CClient = mdmClientService.getB2cClient(opsBooking.getClientID());
                toDoTaskResource.setClientCategoryId(b2CClient.getClientCategory());
                toDoTaskResource.setCompanyMarketId(b2CClient.getCompanyId());//TODo: need to add in B2Bclient class
                toDoTaskResource.setClientSubCategoryId(b2CClient.getClientSubCategory());
            }
            toDoTaskResource.setTaskSubTypeId(ToDoTaskSubTypeValues.PAYMENT_ADVICE.toString());
            toDoTaskResource.setBookingRefId(paymentAdvice.getPaymentAdviceOrderInfoSet().iterator().next().getBookingRefId());
            //ToDoTaskSubTypeValues.PAYMENT_ADVICE.toString());
        }
        else
            toDoTaskResource.setTaskSubTypeId(ToDoTaskSubTypeValues.SUPPLIER_COMMERCIAL_STATEMENT_PAYMENT_ADVICE.toString());//ToDoTaskSubTypeValues.PAYMENT_ADVICE.toString());

//        TODO : Remove sample time and replace with original time
        toDoTaskResource.setDueOnDate(paymentAdvice.getPaymentDueDate());

        toDoTaskResource.setTaskNameId(ToDoTaskNameValues.UPDATE.getValue());
        toDoTaskResource.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
//        toDoTaskResource.setTaskStatusId(ToDoTaskStatusValues.NEW.getValue());
        toDoTaskResource.setTaskSubTypeId(ToDoTaskSubTypeValues.PAYMENT_ADVICE.toString());//ToDoTaskSubTypeValues.PAYMENT_ADVICE.toString());
        toDoTaskResource.setBookingRefId(paymentAdvice.getPaymentAdviceOrderInfoSet().iterator().next().getBookingRefId());
        toDoTaskResource.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.FINANCE.getValue());
        toDoTaskResource.setFileHandlerId(userService.getLoggedInUserId());
        toDoTaskResource.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.name());
        toDoTaskResource.setTaskOrientedTypeId(ToDoTaskOrientedValues.ACTION_ORIENTED.getValue());
        return toDoTaskResource;
    }

    public ToDoTaskResource getTodoForOps(PaymentAdvice paymentAdvice) throws OperationException, ParseException {
        ToDoTaskResource toDoTaskResource = new ToDoTaskResource();
        B2BClient b2BClient = new B2BClient();
        B2CClient b2CClient = new B2CClient();
        toDoTaskResource.setCreatedByUserId(userService.getLoggedInUserId());
        toDoTaskResource.setReferenceId(paymentAdvice.getPaymentAdviceNumber());
        OpsBooking opsBooking = opsBookingService.getBooking(paymentAdvice.getPaymentAdviceOrderInfoSet().iterator().next().getBookingRefId());
//        toDoTaskResource.setProductId(getProductName(paymentAdvice.getPaymentAdviceOrderInfoSet().iterator().next().getBookingRefId(),paymentAdvice.getPaymentAdviceOrderInfoSet().iterator().next().getOrderId()));
        toDoTaskResource.setClientTypeId(opsBooking.getClientType());
        toDoTaskResource.setCompanyId(opsBooking.getCompanyId());
        toDoTaskResource.setClientId(opsBooking.getClientID());
        if (opsBooking.getClientType().equalsIgnoreCase("B2B")) {
            b2BClient = mdmClientService.getB2bClient(opsBooking.getClientID());
            toDoTaskResource.setClientCategoryId(b2BClient.getClientCategory());
            toDoTaskResource.setCompanyMarketId(b2BClient.getCompanyId());//TODo: need to add in B2Bclient class
            toDoTaskResource.setClientSubCategoryId(b2BClient.getClientSubCategory());
        }
        if (opsBooking.getClientType().equalsIgnoreCase("B2C")) {
            b2CClient = mdmClientService.getB2cClient(opsBooking.getClientID());
            toDoTaskResource.setClientCategoryId(b2CClient.getClientCategory());
            toDoTaskResource.setCompanyMarketId(b2CClient.getCompanyId());//TODo: need to add in B2Bclient class
            toDoTaskResource.setClientSubCategoryId(b2CClient.getClientSubCategory());
        }
//        TODO : Remove sample time and replace with original time
        toDoTaskResource.setDueOnDate(ZonedDateTime.now());
        toDoTaskResource.setTaskNameId(ToDoTaskNameValues.UPDATE.getValue());
        toDoTaskResource.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
        if (StringUtils.isEmpty(paymentAdvice.getPaymentAdviceOrderInfoSet().iterator().next().getServiceOrderId())) {
            toDoTaskResource.setTaskSubTypeId(ToDoTaskSubTypeValues.PAYMENT_ADVICE.toString());
            toDoTaskResource.setBookingRefId(paymentAdvice.getPaymentAdviceOrderInfoSet().iterator().next().getBookingRefId());
        }
        else {
            toDoTaskResource.setTaskSubTypeId(ToDoTaskSubTypeValues.PAYMENT_ADVICE_SUPPLIER_BILL_PASSING.toString());
        }
        toDoTaskResource.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());//ToDoFunctionalAreaValues.OPERATIONS.getValue()); //Ops Approver

        return toDoTaskResource;
    }


    private String getProductName(String bookingId, String orderId) throws OperationException {

        OpsProduct opsProduct = opsBookingService.getProduct(bookingId, orderId);
        if (opsProduct!=null)
        {
            String productCategory = opsProduct.getProductCategory();
            if (!StringUtils.isEmpty(productCategory))
            {
                return productCategory;
            }
        }
        else {
            logger.error("Product not found");
        }
        return null;
    }


    public String getNewPaymentAdviceID() {
        SecureRandom random = new SecureRandom();
        int num = random.nextInt(100000);
        String formatted = String.format("%05d", num);
        return formatted;
    }

    @Override
    public SupplierPayableReceivableAmt getSupplierPayableAndReceivableAmt(String supplierId) throws OperationException {
        BigDecimal payableAmt = BigDecimal.ZERO;
        BigDecimal receivableAmt = BigDecimal.ZERO;
        String supplierName = getSupplierName(supplierId);
        SupplierPayableReceivableAmt supplierPayableReceivableAmt = new SupplierPayableReceivableAmt();

        supplierPayableReceivableAmt.setPayableAmt(payableAmt);
        if (!StringUtils.isEmpty(supplierName)) {
            CommercialStatementSearchCriteria criteria = new CommercialStatementSearchCriteria();
            criteria.setSupplierName(supplierName);
            criteria.setCommercialStatementFor("Supplier");

            Map<String, Object> map = supplierCommercialStatementService.searchByCriteria(criteria);
            if (map != null && map.size() > 0) {
                List<SupplierCommercialStatement> commercialStatementList = (List<SupplierCommercialStatement>) map.entrySet().stream().filter(e -> e.getKey().equalsIgnoreCase("result")).map(Map.Entry::getValue).findFirst().orElse(null);
                for (SupplierCommercialStatement supplierCommercialStatement : commercialStatementList) {
                    if (CommercialType.RECEIVABLE.getValue().equalsIgnoreCase(supplierCommercialStatement.getCommercialType())) {
                        if (SettlementStatus.UNSETTLED.getValue().equalsIgnoreCase(supplierCommercialStatement.getSettlementStatus()) ||
                                SettlementStatus.PARTIALLY_SETTLED.getValue().equalsIgnoreCase(supplierCommercialStatement.getSettlementStatus())) {
                            receivableAmt = receivableAmt.add(supplierCommercialStatement.getBalanceReceivable());

                        }
                    }
                }
            }
            supplierPayableReceivableAmt.setReceivableAmt(receivableAmt);
        }


        ServiceOrderSearchCriteria serviceOrderSearchCriteria = new ServiceOrderSearchCriteria();
        serviceOrderSearchCriteria.setSupplierId(supplierId);
        serviceOrderSearchCriteria.setServiceOrderType(ServiceOrderAndSupplierLiabilityType.FSO);

        Map<String, Object> serviceOrdersAndSupplierLiabilities = null;
        try {
            serviceOrdersAndSupplierLiabilities = serviceOrderAndSupplierLiabilityService.getServiceOrdersAndSupplierLiabilities(serviceOrderSearchCriteria);
        } catch (OperationException | IOException e) {
            supplierPayableReceivableAmt.setPayableAmt(payableAmt);
        }
        if (serviceOrdersAndSupplierLiabilities != null && serviceOrdersAndSupplierLiabilities.size() > 0) {
            List<ServiceOrderResource> resources = (List<ServiceOrderResource>) serviceOrdersAndSupplierLiabilities.entrySet().stream().filter(e -> e.getKey().equalsIgnoreCase("result")).map(Map.Entry::getValue).findFirst().orElse(null);
            for (ServiceOrderResource serviceOrderResource : resources) {
                payableAmt = payableAmt.add(serviceOrderResource.getSupplierPricingResource().getTotalBalanceAmountPayable());
            }
            supplierPayableReceivableAmt.setPayableAmt(payableAmt);
        }


        return supplierPayableReceivableAmt;
    }

//    @Override
//    public BigDecimal getSupplierPayableAmt(String supplierId) throws OperationException {
//        BigDecimal payableAmt = BigDecimal.ZERO;
//
//        ServiceOrderSearchCriteria serviceOrderSearchCriteria = new ServiceOrderSearchCriteria();
//        serviceOrderSearchCriteria.setSupplierId(supplierId);
//        serviceOrderSearchCriteria.setServiceOrderType(ServiceOrderAndSupplierLiabilityType.FSO);
//
//        try {
//            Map<String, Object> serviceOrdersAndSupplierLiabilities = serviceOrderAndSupplierLiabilityService.getServiceOrdersAndSupplierLiabilities(serviceOrderSearchCriteria);
//            if (serviceOrdersAndSupplierLiabilities!=null && serviceOrdersAndSupplierLiabilities.size()>0)
//            {
//                List<ServiceOrderResource> resources = (List<ServiceOrderResource>)serviceOrdersAndSupplierLiabilities.entrySet().stream().filter(e -> e.getKey().equalsIgnoreCase("result")).map(Map.Entry::getValue).findFirst().orElse(null);
//                for (ServiceOrderResource serviceOrderResource: resources)
//                {
//                    payableAmt = payableAmt.add(serviceOrderResource.getSupplierPricingResource().getTotalBalanceAmountPayable());
//                }
//            }
//        } catch (OperationException | IOException e)
//        {
//            logger.error("No record found for Supplier Id:"+supplierId);
//        }
//        return payableAmt;
//    }

//    @Override
//    public BigDecimal getSupplierReceivableAmt(String supplierId) throws OperationException {
//        BigDecimal receivableAmt = BigDecimal.ZERO;
//        String supplierName = getSupplierName(supplierId);
//        if (!StringUtils.isEmpty(supplierName))
//        {
//            CommercialStatementSearchCriteria criteria = new CommercialStatementSearchCriteria();
//            criteria.setSupplierName(supplierName);
//            criteria.setCommercialStatementFor("Supplier");
//            try {
//                Map<String, Object> map = supplierCommercialStatementService.searchByCriteria(criteria);
//
//
//                if (map != null && map.size() > 0) {
//                    List<SupplierCommercialStatement> commercialStatementList = (List<SupplierCommercialStatement>) map.entrySet().stream().filter(e -> e.getKey().equalsIgnoreCase("result")).map(Map.Entry::getValue).findFirst().orElse(null);
//                    for (SupplierCommercialStatement supplierCommercialStatement : commercialStatementList) {
//                        if (CommercialType.RECEIVABLE.getValue().equalsIgnoreCase(supplierCommercialStatement.getCommercialType())) {
//                            if (SettlementStatus.UNSETTLED.getValue().equalsIgnoreCase(supplierCommercialStatement.getSettlementStatus()) ||
//                                    SettlementStatus.PARTIALLY_SETTLED.getValue().equalsIgnoreCase(supplierCommercialStatement.getSettlementStatus())) {
//                                receivableAmt = receivableAmt.add(supplierCommercialStatement.getBalanceReceivable());
//
//                            }
//                        }
//                    }
//                }
//            }
//            catch (OperationException e)
//            {
//
//            }
//        }
//        return receivableAmt;
//    }

    private String getSupplierName(String supplierId) throws OperationException {
        String supplierName = null;
        String jsonSupplier = supplierDetailsService.getSupplierDetails(supplierId);
        if (jsonSupplier != null) {
            supplierName = jsonObjectProvider.getAttributeValue(jsonSupplier, "$.supplier.name", String.class);
        } else {
            supplierName = new String();
        }
        return supplierName;
    }


}