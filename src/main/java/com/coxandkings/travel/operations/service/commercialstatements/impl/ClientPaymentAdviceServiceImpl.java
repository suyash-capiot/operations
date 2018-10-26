package com.coxandkings.travel.operations.service.commercialstatements.impl;

import com.coxandkings.travel.operations.criteria.prepaymenttosupplier.PaymentCriteria;
import com.coxandkings.travel.operations.enums.prepaymenttosupplier.PaymentAdviceStatusValues;
import com.coxandkings.travel.operations.enums.todo.ToDoFunctionalAreaValues;
import com.coxandkings.travel.operations.enums.todo.ToDoTaskNameValues;
import com.coxandkings.travel.operations.enums.todo.ToDoTaskSubTypeValues;
import com.coxandkings.travel.operations.enums.todo.ToDoTaskTypeValues;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.commercialstatements.ClientPaymentAdvice;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice.PaymentAdvice;
import com.coxandkings.travel.operations.model.productbookedthrother.mdm.B2BClient;
import com.coxandkings.travel.operations.model.productbookedthrother.mdm.B2CClient;
import com.coxandkings.travel.operations.repository.commercialstatements.ClientPaymentAdviceRepo;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResource;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.commercialstatements.ClientPaymentAdviceService;
import com.coxandkings.travel.operations.service.productbookedthrother.mdm.MdmClientService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.Constants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;

@Service(value = "ClientPaymentAdviceServiceImpl")
public class ClientPaymentAdviceServiceImpl implements ClientPaymentAdviceService {

    private static final Logger logger = Logger.getLogger(ClientPaymentAdviceServiceImpl.class);

    @Autowired
    ClientPaymentAdviceRepo clientPaymentAdviceRepo;

    @Autowired
    ToDoTaskService toDoTaskService;

    @Autowired
    AlertService alertService;

    @Autowired
    private UserService userService;

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private MdmClientService mdmClientService;


    @Override
    public ClientPaymentAdvice savePaymentAdvice(ClientPaymentAdvice clientPaymentAdvice) throws OperationException {

        clientPaymentAdvice.setPaymentAdviceNumber(getNewPaymentAdviceID());

        ClientPaymentAdvice paymentAdvice = clientPaymentAdviceRepo.add(clientPaymentAdvice);
        if (paymentAdvice.getPaymentAdviceStatus().equals(PaymentAdviceStatusValues.APPROVAL_PENDING)) {
            try {
                toDoTaskService.save(getTodoForOpsApproval(paymentAdvice));
            } catch (OperationException e) {
                throw e;
            } catch (Exception e) {
                throw new OperationException(Constants.ER1030);
            }
        }
        else {
            try{
                toDoTaskService.save(getTodoForFinance(paymentAdvice));
            }catch (OperationException e){
                throw e;
            }catch (Exception e){
                throw new OperationException(Constants.ER1030);
            }
        }
        return paymentAdvice;
    }

    @Override
    public ClientPaymentAdvice updatePaymentAdvice(ClientPaymentAdvice clientPaymentAdvice) {
        return clientPaymentAdviceRepo.update(clientPaymentAdvice);
    }


    @Override
    public ClientPaymentAdvice getById(String id) throws OperationException {
        ClientPaymentAdvice paymentAdvice = clientPaymentAdviceRepo.getById(id);
        if (paymentAdvice == null)
            throw new OperationException(Constants.ER01);
        return paymentAdvice;
    }

    @Override
    public ClientPaymentAdvice getByPaymentAdviceNumber(String paymentAdviceNumber) {
        return clientPaymentAdviceRepo.getByPaymentAdviceNumber(paymentAdviceNumber);
    }

    @Override
    public ClientPaymentAdvice approvePaymentAdvise(String paymentAdviceNumber, String remarks) throws OperationException {
        ClientPaymentAdvice clientPaymentAdvice = clientPaymentAdviceRepo.getByPaymentAdviceNumber(paymentAdviceNumber);
        if (clientPaymentAdvice == null) throw new OperationException(Constants.ER01);
        if (clientPaymentAdvice.getPaymentAdviceStatus()!=(PaymentAdviceStatusValues.APPROVAL_PENDING))
            throw new OperationException(Constants.ER631);
        clientPaymentAdvice.setPaymentAdviceStatus(PaymentAdviceStatusValues.APPROVED);
        clientPaymentAdvice.setApproverRemark(remarks);
        clientPaymentAdviceRepo.update(clientPaymentAdvice);

        try {
            toDoTaskService.save(getTodoForFinance(clientPaymentAdvice));
        } catch (Exception e) {
            logger.error("Unable to create Finance To-Do Task", e);
        }
        return clientPaymentAdvice;
    }

    @Override
    public ClientPaymentAdvice rejectPaymentAdvice(String paymentAdviceNumber, String remarks) throws OperationException {
        ClientPaymentAdvice clientPaymentAdvice = clientPaymentAdviceRepo.getByPaymentAdviceNumber(paymentAdviceNumber);
        if (clientPaymentAdvice == null) throw new OperationException(Constants.ER01);
        if (!clientPaymentAdvice.getPaymentAdviceStatus().equals(PaymentAdviceStatusValues.APPROVAL_PENDING.getPaymentAdviseStatus()))
            throw new OperationException(Constants.ER631);
        clientPaymentAdvice.setPaymentAdviceStatus(PaymentAdviceStatusValues.REJECTED);
        clientPaymentAdvice.setApproverRemark(remarks);
        clientPaymentAdviceRepo.update(clientPaymentAdvice);

        try {
            toDoTaskService.save(getTodoForFinance(clientPaymentAdvice));
        } catch (Exception e) {
            logger.error("Unable to create Finance To-Do Task", e);
        }
        return clientPaymentAdvice;
    }

    @Override
    public ClientPaymentAdvice searchClientPayment(PaymentCriteria paymentCriteria) throws OperationException {
        ClientPaymentAdvice clientPaymentDetails = clientPaymentAdviceRepo.searchPaymentAdvise(paymentCriteria);


        if (clientPaymentDetails == null) {
            throw new OperationException(Constants.ER01);
        }
        return clientPaymentDetails;
    }


    public ToDoTaskResource getTodoForOpsApproval(ClientPaymentAdvice paymentAdvice) throws OperationException {
        ToDoTaskResource toDoTaskResource=new ToDoTaskResource();
        toDoTaskResource.setCreatedByUserId(userService.getLoggedInUserId());
        toDoTaskResource.setReferenceId(paymentAdvice.getPaymentAdviceNumber());
        toDoTaskResource.setDueOnDate(ZonedDateTime.now().plusDays(2));
        toDoTaskResource.setTaskNameId(ToDoTaskNameValues.APPROVE.getValue());
        toDoTaskResource.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
        toDoTaskResource.setTaskSubTypeId(ToDoTaskSubTypeValues.CLIENT_COMMERCIAL_STATEMENT_PAYMENT_ADVICE.toString());
        toDoTaskResource.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());
        return toDoTaskResource;

//        ToDoTaskResource toDoTaskResource = new ToDoTaskResource();
//        B2BClient b2BClient = new B2BClient();
//        B2CClient b2CClient = new B2CClient();
//
//        toDoTaskResource.setCreatedByUserId(userService.getLoggedInUserId());
//        toDoTaskResource.setReferenceId(paymentAdvice.getPaymentAdviceNumber());
//        OpsBooking opsBooking = opsBookingService.getBooking(paymentAdvice.getClientCommercialStatementSet().iterator().next().getCommercialStatementDetails().iterator().next().getBookingRefNum());
//        toDoTaskResource.setClientTypeId(opsBooking.getClientType());
//        toDoTaskResource.setCompanyId(opsBooking.getCompanyId());
//        toDoTaskResource.setClientId(opsBooking.getClientID());
//
//        if (opsBooking.getClientType().equalsIgnoreCase("B2B")) {
//            b2BClient = mdmClientService.getB2bClient(opsBooking.getClientID());
//            toDoTaskResource.setClientCategoryId(b2BClient.getClientCategory());
//            toDoTaskResource.setCompanyMarketId(b2BClient.getCompanyId());//TODo: need to add in B2Bclient class
//            toDoTaskResource.setClientSubCategoryId(b2BClient.getClientSubCategory());
//        }
//
//        if (opsBooking.getClientType().equalsIgnoreCase("B2C")) {
//            b2CClient = mdmClientService.getB2cClient(opsBooking.getClientID());
//            toDoTaskResource.setClientCategoryId(b2CClient.getClientCategory());
//            toDoTaskResource.setCompanyMarketId(b2CClient.getCompanyId());//TODo: need to add in B2Bclient class
//            toDoTaskResource.setClientSubCategoryId(b2CClient.getClientSubCategory());
//        }
//
//        toDoTaskResource.setDueOnDate(ZonedDateTime.now());
//        toDoTaskResource.setTaskNameId(ToDoTaskNameValues.APPROVE.getValue());
//        toDoTaskResource.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
//
//        if (StringUtils.isEmpty(paymentAdvice.getClientCommercialStatementSet().iterator().next().getStatementId())) {
//
//            toDoTaskResource.setTaskSubTypeId(ToDoTaskSubTypeValues.PAYMENT_ADVICE.toString());
//            toDoTaskResource.setBookingRefId(paymentAdvice.getClientCommercialStatementSet().iterator().next().getStatementId());
//
//        } else {
//
//            toDoTaskResource.setTaskSubTypeId(ToDoTaskSubTypeValues.PAYMENT_ADVICE_CLIENT_BILL_PASSING.toString());
//        }
//        toDoTaskResource.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());
//        return  toDoTaskResource;
    }

    public ToDoTaskResource getTodoForFinance(ClientPaymentAdvice paymentAdvice)  throws OperationException {
        ToDoTaskResource toDoTaskResource = new ToDoTaskResource();
        toDoTaskResource.setCreatedByUserId(userService.getLoggedInUserId());
        toDoTaskResource.setReferenceId(paymentAdvice.getPaymentAdviceNumber());
        toDoTaskResource.setDueOnDate(ZonedDateTime.now().plusDays(2));
        toDoTaskResource.setTaskNameId(ToDoTaskNameValues.SETTLEMENT.getValue());
        toDoTaskResource.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
        toDoTaskResource.setTaskSubTypeId(ToDoTaskSubTypeValues.CLIENT_COMMERCIAL_STATEMENT_PAYMENT_ADVICE.toString());
        toDoTaskResource.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.FINANCE.getValue());
        return toDoTaskResource;

//        ToDoTaskResource toDoTaskResource = new ToDoTaskResource();
//        B2BClient b2BClient = new B2BClient();
//        B2CClient b2CClient = new B2CClient();
//
//        toDoTaskResource.setCreatedByUserId(userService.getLoggedInUserId());
//        toDoTaskResource.setReferenceId(paymentAdvice.getPaymentAdviceNumber());
//        OpsBooking opsBooking = opsBookingService.getBooking(paymentAdvice.getClientCommercialStatementSet().iterator().next().getCommercialStatementDetails().iterator().next().getBookingRefNum());
//        toDoTaskResource.setClientTypeId(opsBooking.getClientType());
//        toDoTaskResource.setCompanyId(opsBooking.getCompanyId());
//        toDoTaskResource.setClientId(opsBooking.getClientID());
//
//        if (opsBooking.getClientType().equalsIgnoreCase("B2B")) {
//            b2BClient = mdmClientService.getB2bClient(opsBooking.getClientID());
//            toDoTaskResource.setClientCategoryId(b2BClient.getClientCategory());
//            toDoTaskResource.setCompanyMarketId(b2BClient.getCompanyId());//TODo: need to add in B2Bclient class
//            toDoTaskResource.setClientSubCategoryId(b2BClient.getClientSubCategory());
//        }
//
//        if (opsBooking.getClientType().equalsIgnoreCase("B2C")) {
//            b2CClient = mdmClientService.getB2cClient(opsBooking.getClientID());
//            toDoTaskResource.setClientCategoryId(b2CClient.getClientCategory());
//            toDoTaskResource.setCompanyMarketId(b2CClient.getCompanyId());//TODo: need to add in B2Bclient class
//            toDoTaskResource.setClientSubCategoryId(b2CClient.getClientSubCategory());
//        }
//
//        toDoTaskResource.setDueOnDate(ZonedDateTime.now());
//
//        toDoTaskResource.setTaskNameId(ToDoTaskNameValues.UPDATE.getValue());
//        toDoTaskResource.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
//
//        toDoTaskResource.setTaskSubTypeId(ToDoTaskSubTypeValues.PAYMENT_ADVICE.toString());//ToDoTaskSubTypeValues.PAYMENT_ADVICE.toString());
//        toDoTaskResource.setBookingRefId(paymentAdvice.getClientCommercialStatementSet().iterator().next().getCommercialStatementDetails().iterator().next().getBookingRefNum());
//        toDoTaskResource.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.FINANCE.getValue());
//        return toDoTaskResource;

    }

    public String getNewPaymentAdviceID() {
        return String.valueOf(System.currentTimeMillis());
    }


//    public AlertResource sendAnAlertToOps(PaymentAdvice paymentAdvice)
//    {
//        AlertResource alertResource=new AlertResource();
//        Map<String,String> map=new HashMap<>();
//        if (paymentAdvice.getPaymentAdviceStatus().equals(PaymentAdviceStatusValues.APPROVED))
//        {
//            map.put("TemplateId","TemplateId123");
//            map.put("message","Payment Advice");
//            map.put("receiverEmail","pooja.gawde@coxandkings.com");
//            map.put("subject","Approver approved Payment Advice");
//            map.put("userId","1234");
//            alertResource.setAlertType(AlertType.EMAIL);
//            alertResource.setParams(map);
//            // alertResource.setToDoTaskResource(getTodo());
//            alertResource.setEntityRefId("");
//            alertResource.setUserTaskName("Ops USer");
//            alertResource.setProductId("CNK-0008");
//        }
//        else if (paymentAdvice.getPaymentAdviceStatus().equals(PaymentAdviceStatusValues.REJECTED))
//        {
//            map.put("TemplateId","TemplateId123");
//            map.put("message","Payment Advice");
//            map.put("receiverEmail","pooja.gawde@coxandkings.com");
//            map.put("subject","Approver rejected Payment Advice");
//            map.put("userId","1234");
//            alertResource.setAlertType(AlertType.EMAIL);
//            alertResource.setParams(map);
//            // alertResource.setToDoTaskResource(getTodo());
//            alertResource.setEntityRefId("");
//            alertResource.setUserTaskName("Ops USer");
//            alertResource.setProductId("CNK-0008");
//        }
//        else {}
//
//        return alertResource;
//    }

}