package com.coxandkings.travel.operations.service.commercialstatements.impl;

import com.coxandkings.travel.operations.criteria.prepaymenttosupplier.PaymentCriteria;
import com.coxandkings.travel.operations.enums.commercialStatements.ApprovalStatus;
import com.coxandkings.travel.operations.enums.commercialStatements.CommercialStatementBillStatus;
import com.coxandkings.travel.operations.enums.commercialStatements.CommercialStatementFor;
import com.coxandkings.travel.operations.enums.commercialStatements.CommercialType;
import com.coxandkings.travel.operations.enums.prepaymenttosupplier.PaymentAdviceStatusValues;
import com.coxandkings.travel.operations.enums.supplierBillPassing.SupplierBillPassingStatus;
import com.coxandkings.travel.operations.enums.todo.*;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.commercialstatements.*;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice.PaymentAdvice;
import com.coxandkings.travel.operations.model.supplierbillpassing.SupplierInvoiceOCR;
import com.coxandkings.travel.operations.repository.commercialstatements.ClientCommercialStatementRepo;
import com.coxandkings.travel.operations.repository.commercialstatements.CommercialStatementsBillPassingRepo;
import com.coxandkings.travel.operations.repository.commercialstatements.SupplierCommercialStatementRepo;
import com.coxandkings.travel.operations.repository.supplierbillpassing.SupplierInvoiceOCRRepo;
import com.coxandkings.travel.operations.resource.commercialStatement.AttachedCommercialStatement;
import com.coxandkings.travel.operations.resource.commercialStatement.CommercialStatementSearchCriteria;
import com.coxandkings.travel.operations.resource.commercialStatement.CommercialStatementsBillPassingResource;
import com.coxandkings.travel.operations.resource.commercialStatement.CommercialStatementsPaymentAdviceResource;
import com.coxandkings.travel.operations.resource.notification.InlineMessageResource;
import com.coxandkings.travel.operations.resource.prepaymenttosupplier.paymentadvice.PaymentAdviceResource;
import com.coxandkings.travel.operations.resource.prepaymenttosupplier.paymentadvice.SupplierPaymentResource;
import com.coxandkings.travel.operations.resource.supplierbillpassing.SupplierInvoiceSearchCriteria;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResource;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.commercialstatements.ClientCommercialStatementService;
import com.coxandkings.travel.operations.service.commercialstatements.ClientPaymentAdviceService;
import com.coxandkings.travel.operations.service.commercialstatements.CommercialStatementsBillPassingService;
import com.coxandkings.travel.operations.service.commercialstatements.SupplierCommercialStatementService;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.paymentadvice.PaymentAdviceService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.systemlogin.MDMToken;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.CopyUtils;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.coxandkings.travel.operations.validations.HibernateValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CommercialStatementsBillPassingServiceImpl implements CommercialStatementsBillPassingService {

    @Autowired
    private CommercialStatementsBillPassingRepo commercialStatementsBillPassingRepo;

    @Autowired
    private ToDoTaskService toDoTaskService;

    @Autowired
    private AlertService alertService;

    @Autowired
    private SupplierInvoiceOCRRepo supplierInvoiceOCRRepo;

    @Autowired
    private ClientCommercialStatementRepo clientCommercialStatementRepo;

    @Autowired
    private ClientCommercialStatementService clientCommercialStatementService;

    @Autowired
    private SupplierCommercialStatementService supplierCommercialStatementService;

    @Autowired
    private SupplierCommercialStatementRepo supplierCommercialStatementRepo;

    private Logger logger = Logger.getLogger(CommercialStatementsBillPassingServiceImpl.class);


    @Value("${finance-commercial-statements-settelement.supplier_step_two}")
    private String financeSupplierCommercialStatementStepTwo;

    @Value("${finance-commercial-statements-settelement.supplier_step_three}")
    private String financeSupplierCommercialStatementStepThree;

    @Value("${finance-commercial-statements-settelement.client_step_two}")
    private String financeClientCommercialStatementStepTwo;

    @Value("${finance-commercial-statements-settelement.client_step_three}")
    private String financeClientCommercialStatementStepThree;

    @Value(value = "${commercial_statements.bill_passing_approval}")
    private String billPassingApproval;

    @Value(value = "${commercial_statements.client_statement_created}")
    private String clientSstatementCreated;

    @Value(value = "${commercial_statements.supplier_statement_created}")
    private String supplierStatementCreated;

    @Autowired
    private MDMToken mdmToken;

    @Autowired
    private PaymentAdviceService paymentAdviceService;

    @Autowired
    private ClientPaymentAdviceService clientPaymentAdviceService;

    @Autowired
    private RestUtils restUtils;

    @Override
    public Map updateStatus(String billPassingId, String status, String remarks) throws OperationException {
        if (StringUtils.isEmpty(status)) throw new OperationException(Constants.ER35);
        CommercialStatementsBillPassing commercialStatementsBillPassing = commercialStatementsBillPassingRepo.get(billPassingId);
        if (commercialStatementsBillPassing == null) throw new OperationException(Constants.ER01);
        if (!commercialStatementsBillPassing.getBillPassingStatus().equalsIgnoreCase(CommercialStatementBillStatus.PENDING_APPROVAL.getValue()))
            throw new OperationException(Constants.ER631);
        if (Arrays.stream(ApprovalStatus.values()).noneMatch(approvalStatus -> approvalStatus.getValue().equalsIgnoreCase(status)))
            throw new OperationException(Constants.ER35);
        if (!StringUtils.isEmpty(remarks))
            commercialStatementsBillPassing.setRemarks(remarks);
        if (status.equalsIgnoreCase(ApprovalStatus.APPROVED.getValue())) {
            commercialStatementsBillPassing.setBillPassingStatus(CommercialStatementBillStatus.APPROVED.getValue());
            if (commercialStatementsBillPassing.getCommercialStatementFor().equalsIgnoreCase(CommercialStatementFor.SUPPLIER.getName())) {
                Set<SupplierCommercialStatement> supplierCommercialStatements = commercialStatementsBillPassing.getSupplierCommercialStatements();
                SupplierCommercialStatement supplierCommercialStatement = supplierCommercialStatements.iterator().next();
                supplierCommercialStatement.setTotalPayable(commercialStatementsBillPassing.getNetPayableToSupplierOrClient());
                supplierCommercialStatement.setBalancePayable(supplierCommercialStatement.getTotalPayable().subtract(supplierCommercialStatement.getTotalPaid()));
            }
            else{
                Set<ClientCommercialStatement> clientCommercialStatements = commercialStatementsBillPassing.getClientCommercialStatements();
                ClientCommercialStatement clientCommercialStatement = clientCommercialStatements.iterator().next();
                clientCommercialStatement.setTotalPayable(commercialStatementsBillPassing.getNetPayableToSupplierOrClient());
                clientCommercialStatement.setBalancePayable(clientCommercialStatement.getTotalPayable().subtract(clientCommercialStatement.getTotalPaid()));
            }
            commercialStatementsBillPassingRepo.update(commercialStatementsBillPassing);
            callFinanceApiAfterBillPassingApproval(commercialStatementsBillPassing);
            createAlertandOperationsToDoTask(commercialStatementsBillPassing);
        } else if (status.equalsIgnoreCase(ApprovalStatus.REJECTED.getValue())) {
            commercialStatementsBillPassing.setBillPassingStatus(SupplierBillPassingStatus.REJECTED.getValue());
            commercialStatementsBillPassing.setNetPayableToSupplierOrClient(commercialStatementsBillPassing.getEquivalentCommercialStatementAmount());
            commercialStatementsBillPassingRepo.update(commercialStatementsBillPassing);
            callFinanceApiAfterBillPassingApproval(commercialStatementsBillPassing);
            createAlertandOperationsToDoTask(commercialStatementsBillPassing);
        }

        Map<String, String> entity = new HashMap<>();
        entity.put("message", "successfully updated");
        return entity;
    }

    private void callFinanceApiAfterBillPassingApproval(CommercialStatementsBillPassing commercialStatementsBillPassing) {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", mdmToken.getToken());
            Set<String> commercialStatementIds = new HashSet<>();
            if (commercialStatementsBillPassing.getCommercialStatementFor().equalsIgnoreCase(CommercialStatementFor.SUPPLIER.getName())) {
                commercialStatementIds.add(commercialStatementsBillPassing.getSupplierCommercialStatements().iterator().next().getStatementId());
                Map<String,Object> entity=new HashMap<>();
                entity.put("commercialStatementIds",commercialStatementIds);
                HttpEntity httpEntity = new HttpEntity(entity,httpHeaders);
                restUtils.postForEntity(financeSupplierCommercialStatementStepThree,httpEntity, String.class);
            } else {
                commercialStatementIds.add(commercialStatementsBillPassing.getClientCommercialStatements().iterator().next().getStatementId());
                commercialStatementIds.add(commercialStatementsBillPassing.getClientCommercialStatements().iterator().next().getStatementId());
                Map<String,Object> entity=new HashMap<>();
                entity.put("commercialStatementIds",commercialStatementIds);
                HttpEntity httpEntity = new HttpEntity(entity,httpHeaders);
                restUtils.postForEntity(financeClientCommercialStatementStepThree,httpEntity, String.class);
            }

        } catch (Exception e) {
            logger.error("Error in calling fiancne api after commercial statement bill passing approval");
        }
    }

    private void callFinanceApiAfterBillPassing(CommercialStatementsBillPassing commercialStatementsBillPassing) {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", mdmToken.getToken());
            Set<String> commercialStatementIds = new HashSet<>();
            if (commercialStatementsBillPassing.getCommercialStatementFor().equalsIgnoreCase(CommercialStatementFor.SUPPLIER.getName())) {
                commercialStatementsBillPassing.getSupplierCommercialStatements().stream().forEach(supplierCommercialStatement -> commercialStatementIds.add(supplierCommercialStatement.getStatementId()));
                Map<String,Object> entity=new HashMap<>();
                entity.put("commercialStatementIds",commercialStatementIds);
                HttpEntity httpEntity = new HttpEntity(entity,httpHeaders);
                ResponseEntity<String> result=restUtils.postForEntity(financeSupplierCommercialStatementStepTwo, httpEntity, String.class);
                System.out.println("result = " + result);
            } else {
                commercialStatementsBillPassing.getClientCommercialStatements().stream().forEach(clientCommercialStatement -> commercialStatementIds.add(clientCommercialStatement.getStatementId()));
                Map<String,Object> entity=new HashMap<>();
                entity.put("commercialStatementIds",commercialStatementIds);
                HttpEntity httpEntity = new HttpEntity(entity,httpHeaders);
                ResponseEntity<String> result=restUtils.postForEntity(financeClientCommercialStatementStepTwo, httpEntity, String.class);
            }

        } catch (Exception e) {
            logger.error("Error in calling fiancne api after commercial statement bill passing is done");
        }
    }

    public void createAlertandApprovalToDoTask(CommercialStatementsBillPassing commercialStatementsBillPassing) {
        InlineMessageResource inlineMessageResource = new InlineMessageResource();
        inlineMessageResource.setNotificationType("System");
        inlineMessageResource.setAlertName(billPassingApproval);
        ConcurrentHashMap<String, String> entity = new ConcurrentHashMap<>();
        entity.put("statementId", commercialStatementsBillPassing.getId());
        inlineMessageResource.setDynamicVariables(entity);

        ToDoTaskResource todo = new ToDoTaskResource();
        if (commercialStatementsBillPassing.getCommercialStatementFor().equalsIgnoreCase(CommercialStatementFor.CLIENT.getName()))
            todo.setTaskSubTypeId(ToDoTaskSubTypeValues.CLIENT_COMMERCIAL_STATEMENT_BILL_PASSING.toString());
        else todo.setTaskSubTypeId(ToDoTaskSubTypeValues.SUPPLIER_COMMERCIAL_STATEMENT_BILL_PASSING.toString());
        todo.setTaskPriorityId(ToDoTaskPriorityValues.MEDIUM.getValue());
        todo.setTaskNameId(ToDoTaskNameValues.APPROVE.getValue());
        todo.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
        todo.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());
        todo.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.toString());
        todo.setReferenceId(commercialStatementsBillPassing.getId());
        todo.setCreatedByUserId("luv@coxandkings.com");
        todo.setDueOnDate(ZonedDateTime.now().plusDays(5));

        try {
            toDoTaskService.save(todo);
        } catch (Exception e) {
            logger.error("Error in saving todo task");
        }
        try {
            alertService.sendInlineMessageAlert(inlineMessageResource);
        } catch (Exception e) {
            logger.error("Error in sending inline message");
        }
    }

    public void createAlertandOperationsToDoTask(CommercialStatementsBillPassing commercialStatementsBillPassing) {
        InlineMessageResource inlineMessageResource = new InlineMessageResource();
        if (commercialStatementsBillPassing.getCommercialStatementFor().equalsIgnoreCase(CommercialStatementFor.CLIENT.getName()))
            inlineMessageResource.setAlertName(clientSstatementCreated);
        else inlineMessageResource.setAlertName(supplierStatementCreated);
        inlineMessageResource.setNotificationType("System");
        ConcurrentHashMap<String, String> entity = new ConcurrentHashMap<>();
        entity.put("statementId", commercialStatementsBillPassing.getId());
        inlineMessageResource.setDynamicVariables(entity);

        ToDoTaskResource todo = new ToDoTaskResource();
        if (commercialStatementsBillPassing.getCommercialStatementFor().equalsIgnoreCase(CommercialStatementFor.CLIENT.getName()))
            todo.setTaskSubTypeId(ToDoTaskSubTypeValues.CLIENT_COMMERCIAL_STATEMENT_BILL_PASSING.toString());
        else todo.setTaskSubTypeId(ToDoTaskSubTypeValues.SUPPLIER_COMMERCIAL_STATEMENT_BILL_PASSING.toString());
        todo.setTaskPriorityId(ToDoTaskPriorityValues.MEDIUM.getValue());
        todo.setTaskNameId(ToDoTaskNameValues.SETTLEMENT.getValue());
        todo.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
        todo.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());
        todo.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.toString());
        todo.setReferenceId(commercialStatementsBillPassing.getId());
        todo.setCreatedByUserId("luv@coxandkings.com");
        todo.setDueOnDate(ZonedDateTime.now().plusDays(5));

        try {
            toDoTaskService.save(todo);
        } catch (Exception e) {
            logger.error("Error in saving todo task");
        }
        try {
            alertService.sendInlineMessageAlert(inlineMessageResource);
        } catch (OperationException e) {
            logger.error("Error in sending inline message");
        }
    }

    @Override
    public CommercialStatementsBillPassingResource get(String id) throws OperationException {

        CommercialStatementsBillPassing commercialStatementsBillPassing=commercialStatementsBillPassingRepo.get(id);
        if (commercialStatementsBillPassing==null) throw new OperationException(Constants.ER01);

        CommercialStatementsBillPassingResource commercialStatementsBillPassingResource=new CommercialStatementsBillPassingResource();
        CopyUtils.copy(commercialStatementsBillPassing,commercialStatementsBillPassingResource);
        Set<AttachedCommercialStatement> attachedCommercialStatementSet=new HashSet<>();
        if (commercialStatementsBillPassing.getCommercialStatementFor().equalsIgnoreCase(CommercialStatementFor.SUPPLIER.getName())) {
            commercialStatementsBillPassing.getSupplierCommercialStatements().stream().forEach(supplierCommercialStatement -> {
                AttachedCommercialStatement attachedCommercialStatement = new AttachedCommercialStatement();
                attachedCommercialStatement.setSupplierOrClientName(supplierCommercialStatement.getSupplierOrClientName());
                attachedCommercialStatement.setSupplierorClientId(supplierCommercialStatement.getSupplierOrClientId());
                attachedCommercialStatement.setProductName(supplierCommercialStatement.getProductName());
                attachedCommercialStatement.setCommercialHead(supplierCommercialStatement.getCommercialHead());
                attachedCommercialStatement.setStatementName(supplierCommercialStatement.getStatementName());
                attachedCommercialStatement.setStatementId(supplierCommercialStatement.getStatementId());
                attachedCommercialStatement.setCurrency(supplierCommercialStatement.getCurrency());
                attachedCommercialStatement.setTotalPayable(supplierCommercialStatement.getTotalPayable());
                if (supplierCommercialStatement.getTotalServiceTax()!=null)
                    attachedCommercialStatement.setClientServiceTax(supplierCommercialStatement.getTotalServiceTax());
                else attachedCommercialStatement.setClientServiceTax(BigDecimal.ZERO);
                attachedCommercialStatementSet.add(attachedCommercialStatement);
            });
        }
        else {
            commercialStatementsBillPassing.getClientCommercialStatements().stream().forEach(clientCommercialStatement -> {
                AttachedCommercialStatement attachedCommercialStatement = new AttachedCommercialStatement();
                attachedCommercialStatement.setSupplierOrClientName(clientCommercialStatement.getSupplierOrClientName());
                attachedCommercialStatement.setSupplierorClientId(clientCommercialStatement.getSupplierOrClientId());
                attachedCommercialStatement.setProductName(clientCommercialStatement.getProductName());
                attachedCommercialStatement.setCommercialHead(clientCommercialStatement.getCommercialHead());
                attachedCommercialStatement.setStatementName(clientCommercialStatement.getStatementName());
                attachedCommercialStatement.setStatementId(clientCommercialStatement.getStatementId());
                attachedCommercialStatement.setCurrency(clientCommercialStatement.getCurrency());
                attachedCommercialStatement.setTotalPayable(clientCommercialStatement.getTotalPayable());
                if (clientCommercialStatement.getTotalServiceTax()!=null)
                    attachedCommercialStatement.setClientServiceTax(clientCommercialStatement.getTotalServiceTax());
                else attachedCommercialStatement.setClientServiceTax(BigDecimal.ZERO);
                attachedCommercialStatementSet.add(attachedCommercialStatement);
            });
        }

        commercialStatementsBillPassingResource.setAttachedCommercialStatements(attachedCommercialStatementSet);
        return commercialStatementsBillPassingResource;
    }

    @Override
    public Map invoiceEntry(CommercialStatementsBillPassingResource commercialStatementsBillPassingResource) throws OperationException {
        CommercialStatementsBillPassing commercialStatementsBillPassing = new CommercialStatementsBillPassing();
        CopyUtils.copy(commercialStatementsBillPassingResource, commercialStatementsBillPassing);
        HibernateValidator.commercialSatementBillPassingValidator(commercialStatementsBillPassing);

        SupplierInvoiceOCR supplierInvoiceOCR=null;
        if (commercialStatementsBillPassing.getManualEntry()!=null && !commercialStatementsBillPassing.getManualEntry()) {
            SupplierInvoiceSearchCriteria supplierInvoiceSearchCriteria=new SupplierInvoiceSearchCriteria();
            supplierInvoiceSearchCriteria.setInvoiceNumber(commercialStatementsBillPassing.getInvoiceNumber());
            supplierInvoiceSearchCriteria.setSupplierId(commercialStatementsBillPassing.getSupplierOrClientId());
            List<SupplierInvoiceOCR> supplierInvoiceOCRList=supplierInvoiceOCRRepo.getAvailableInvoice(supplierInvoiceSearchCriteria);
            if (supplierInvoiceOCRList.isEmpty()) throw new OperationException(Constants.ER01);
            else supplierInvoiceOCR=supplierInvoiceOCRList.get(0);
        }

        if (commercialStatementsBillPassing.getCommercialStatementFor().equalsIgnoreCase(CommercialStatementFor.CLIENT.getName()))
            clientInvoiceEntry(commercialStatementsBillPassingResource,commercialStatementsBillPassing,supplierInvoiceOCR);
        else if (commercialStatementsBillPassing.getCommercialStatementFor().equalsIgnoreCase(CommercialStatementFor.SUPPLIER.getName()))
            supplierInvoiceEntry(commercialStatementsBillPassingResource,commercialStatementsBillPassing,supplierInvoiceOCR);

        Map<String, String> entity = new HashMap<>();
        entity.put("message", "Bill Passing completed successfully");
        return entity;
    }

    private void supplierInvoiceEntry(CommercialStatementsBillPassingResource commercialStatementsBillPassingResource, CommercialStatementsBillPassing commercialStatementsBillPassing, SupplierInvoiceOCR supplierInvoiceOCR) throws OperationException {
        BigDecimal totalCommercialStatementValue = BigDecimal.ZERO;

        if (commercialStatementsBillPassingResource.getAttachedCommercialStatements().size() == 0)
            throw new OperationException(Constants.ER1010);
        Set<SupplierCommercialStatement> supplierCommercialStatements = new HashSet<>();

        for (AttachedCommercialStatement attachedCommercialStatement : commercialStatementsBillPassingResource.getAttachedCommercialStatements()) {
            SupplierCommercialStatement supplierCommercialStatement = supplierCommercialStatementRepo.get(attachedCommercialStatement.getStatementId());
            if (supplierCommercialStatement == null) throw new OperationException(Constants.ER01);
            if (supplierCommercialStatement.getCommercialStatementsBillPassing() != null)
                throw new OperationException(Constants.ER1013);
            if (!supplierCommercialStatement.getCommercialType().equalsIgnoreCase(CommercialType.PAYABLE.getValue()))
                throw new OperationException(Constants.ER1002);
            if (!commercialStatementsBillPassing.getCommercialStatementFor().equalsIgnoreCase(supplierCommercialStatement.getCommercialStatementFor()))
                throw new OperationException(Constants.ER1004);
            if (!commercialStatementsBillPassing.getSupplierOrClientId().equalsIgnoreCase(supplierCommercialStatement.getSupplierOrClientId()))
                throw new OperationException(Constants.ER1015);
            totalCommercialStatementValue = totalCommercialStatementValue.add(supplierCommercialStatement.getTotalPayable());
            supplierCommercialStatements.add(supplierCommercialStatement);
        }

        if (totalCommercialStatementValue.compareTo(commercialStatementsBillPassing.getEquivalentCommercialStatementAmount()) != 0)
            throw new OperationException(Constants.ER1012);

        supplierCommercialStatements.stream().forEach(supplierCommercialStatement -> supplierCommercialStatement.setCommercialStatementsBillPassing(commercialStatementsBillPassing));

        if (commercialStatementsBillPassingResource.getAttachedCommercialStatements().size() > 1) {
            if (commercialStatementsBillPassing.getEquivalentCommercialStatementAmount().compareTo(commercialStatementsBillPassing.getNetPayableToSupplierOrClient()) != 0)
                throw new OperationException(Constants.ER1011);
            commercialStatementsBillPassing.setSupplierCommercialStatements(supplierCommercialStatements);
            commercialStatementsBillPassing.setBillPassingStatus(CommercialStatementBillStatus.DONE.getValue());
            updateFlow(commercialStatementsBillPassing, supplierInvoiceOCR);
        } else {
            if (commercialStatementsBillPassing.getNetPayableToSupplierOrClient().compareTo(commercialStatementsBillPassing.getEquivalentCommercialStatementAmount()) > 0) {
                commercialStatementsBillPassing.setSupplierCommercialStatements(supplierCommercialStatements);
                commercialStatementsBillPassing.setBillPassingStatus(CommercialStatementBillStatus.PENDING_APPROVAL.getValue());
                approvalFlow(commercialStatementsBillPassing, supplierInvoiceOCR);
            } else {
                commercialStatementsBillPassing.setSupplierCommercialStatements(supplierCommercialStatements);
                SupplierCommercialStatement supplierCommercialStatement = supplierCommercialStatements.iterator().next();
                supplierCommercialStatement.setTotalPayable(commercialStatementsBillPassing.getNetPayableToSupplierOrClient());
                supplierCommercialStatement.setBalancePayable(supplierCommercialStatement.getTotalPayable().subtract(supplierCommercialStatement.getTotalPaid()));
                commercialStatementsBillPassing.setBillPassingStatus(CommercialStatementBillStatus.DONE.getValue());
                updateFlow(commercialStatementsBillPassing, supplierInvoiceOCR);
            }
        }
    }

    private void clientInvoiceEntry(CommercialStatementsBillPassingResource commercialStatementsBillPassingResource, CommercialStatementsBillPassing commercialStatementsBillPassing, SupplierInvoiceOCR supplierInvoiceOCR) throws OperationException {
        BigDecimal totalCommercialStatementValue = BigDecimal.ZERO;

        if (commercialStatementsBillPassingResource.getAttachedCommercialStatements().size() == 0)
                throw new OperationException(Constants.ER1010);
            Set<ClientCommercialStatement> clientCommercialStatements = new HashSet<>();

            for (AttachedCommercialStatement attachedCommercialStatement : commercialStatementsBillPassingResource.getAttachedCommercialStatements()) {
                ClientCommercialStatement clientCommercialStatement = clientCommercialStatementRepo.get(attachedCommercialStatement.getStatementId());
                if (clientCommercialStatement == null) throw new OperationException(Constants.ER01);
                if (clientCommercialStatement.getCommercialStatementsBillPassing() != null)
                    throw new OperationException(Constants.ER1013);
                if (!clientCommercialStatement.getCommercialType().equalsIgnoreCase(CommercialType.PAYABLE.getValue()))
                    throw new OperationException(Constants.ER1002);
                totalCommercialStatementValue = totalCommercialStatementValue.add(clientCommercialStatement.getTotalPayable());
                if (!commercialStatementsBillPassing.getCommercialStatementFor().equalsIgnoreCase(clientCommercialStatement.getCommercialStatementFor()))
                    throw new OperationException(Constants.ER1003);
                if (!commercialStatementsBillPassing.getSupplierOrClientId().equalsIgnoreCase(clientCommercialStatement.getSupplierOrClientId()))
                    throw new OperationException(Constants.ER1015);
                clientCommercialStatements.add(clientCommercialStatement);
            }

            if (totalCommercialStatementValue.compareTo(commercialStatementsBillPassing.getEquivalentCommercialStatementAmount()) != 0)
                throw new OperationException(Constants.ER1012);

            clientCommercialStatements.stream().forEach(clientCommercialStatement -> clientCommercialStatement.setCommercialStatementsBillPassing(commercialStatementsBillPassing));

            if (commercialStatementsBillPassingResource.getAttachedCommercialStatements().size() > 1) {
                if (commercialStatementsBillPassing.getEquivalentCommercialStatementAmount().compareTo(commercialStatementsBillPassing.getNetPayableToSupplierOrClient()) != 0)
                    throw new OperationException(Constants.ER1011);
                commercialStatementsBillPassing.setClientCommercialStatements(clientCommercialStatements);
                commercialStatementsBillPassing.setBillPassingStatus(CommercialStatementBillStatus.DONE.getValue());
                updateFlow(commercialStatementsBillPassing,supplierInvoiceOCR);
            } else {
                if (commercialStatementsBillPassing.getNetPayableToSupplierOrClient().compareTo(commercialStatementsBillPassing.getEquivalentCommercialStatementAmount()) > 0) {
                    commercialStatementsBillPassing.setClientCommercialStatements(clientCommercialStatements);
                    commercialStatementsBillPassing.setBillPassingStatus(CommercialStatementBillStatus.PENDING_APPROVAL.getValue());
                    approvalFlow(commercialStatementsBillPassing,supplierInvoiceOCR);
                } else {
                    commercialStatementsBillPassing.setClientCommercialStatements(clientCommercialStatements);
                    commercialStatementsBillPassing.setBillPassingStatus(CommercialStatementBillStatus.DONE.getValue());
                    ClientCommercialStatement clientCommercialStatement = clientCommercialStatements.iterator().next();
                    clientCommercialStatement.setTotalPayable(commercialStatementsBillPassing.getNetPayableToSupplierOrClient());
                    clientCommercialStatement.setBalancePayable(clientCommercialStatement.getTotalPayable().subtract(clientCommercialStatement.getTotalPaid()));
                    updateFlow(commercialStatementsBillPassing,supplierInvoiceOCR);
                }
            }
        }

    private void updateFlow(CommercialStatementsBillPassing commercialStatementsBillPassing, SupplierInvoiceOCR supplierInvoiceOCR){
        commercialStatementsBillPassingRepo.add(commercialStatementsBillPassing);
        if (commercialStatementsBillPassing.getManualEntry()!=null && !commercialStatementsBillPassing.getManualEntry())
            updateSupplierInvoiceOCRDetails(supplierInvoiceOCR);
        callFinanceApiAfterBillPassing(commercialStatementsBillPassing);
        createAlertandOperationsToDoTask(commercialStatementsBillPassing);
    }

    private void approvalFlow(CommercialStatementsBillPassing commercialStatementsBillPassing, SupplierInvoiceOCR supplierInvoiceOCR){
        commercialStatementsBillPassingRepo.add(commercialStatementsBillPassing);
        if (commercialStatementsBillPassing.getManualEntry()!=null && !commercialStatementsBillPassing.getManualEntry())
            updateSupplierInvoiceOCRDetails(supplierInvoiceOCR);
        callFinanceApiAfterBillPassing(commercialStatementsBillPassing);
        createAlertandApprovalToDoTask(commercialStatementsBillPassing);
    }

    private void updateSupplierInvoiceOCRDetails(SupplierInvoiceOCR supplierInvoiceOCR){
        if (supplierInvoiceOCR!=null) {
            supplierInvoiceOCR.setUsed(true);
            supplierInvoiceOCRRepo.update(supplierInvoiceOCR);
        }
    }

    @Override
    public List<String> getApprovalStatusList() {
        List<String> statusList = new ArrayList<>();
        Arrays.asList(CommercialStatementBillStatus.values()).stream().forEach(commercialStatementBillStatus -> statusList.add(commercialStatementBillStatus.getValue()));
        return statusList;
    }

    @Override
    public Map generatePaymentAdvice(CommercialStatementsPaymentAdviceResource commercialStatementsPaymentAdviceResource) throws OperationException {

        BigDecimal netPayableToSupplierOrClient=BigDecimal.ZERO;
        BigDecimal amountPaidToSupplierOrClient=BigDecimal.ZERO;
        BigDecimal balanceAmtPayable=BigDecimal.ZERO;
        BigDecimal amountTobepaid;
        Set<PaymentAdviceStatementInfo> paymentAdviceStatementInfoSet=new HashSet<>();

        for (AttachedCommercialStatement attachedCommercialStatement:commercialStatementsPaymentAdviceResource.getAttachedCommercialStatements()){
            PaymentAdviceStatementInfo paymentAdviceStatementInfo=new PaymentAdviceStatementInfo();
            if (commercialStatementsPaymentAdviceResource.getCommercialStatementFor().equalsIgnoreCase(CommercialStatementFor.CLIENT.getName())) {
                ClientCommercialStatement clientCommercialStatement=clientCommercialStatementRepo.get(attachedCommercialStatement.getStatementId());
                if (clientCommercialStatement==null) throw new OperationException(Constants.ER01);
                /*if (attachedCommercialStatement.getAmountToBePaid().compareTo(clientCommercialStatement.getBalancePayable())>0)
                    throw new OperationException(Constants.ER1026);*/
                if (!clientCommercialStatement.getSupplierOrClientId().equalsIgnoreCase(commercialStatementsPaymentAdviceResource.getSupplierOrClientId()))
                    throw new OperationException(Constants.ER1025);
                if (!clientCommercialStatement.getCurrency().equalsIgnoreCase(commercialStatementsPaymentAdviceResource.getCurrency()))
                    throw new OperationException(Constants.ER1031);
                netPayableToSupplierOrClient=netPayableToSupplierOrClient.add(clientCommercialStatement.getTotalPayable());
                amountPaidToSupplierOrClient=amountPaidToSupplierOrClient.add(clientCommercialStatement.getTotalPaid());
                balanceAmtPayable=balanceAmtPayable.add(clientCommercialStatement.getBalancePayable());
                paymentAdviceStatementInfo.setStatementId(clientCommercialStatement.getStatementId());
                paymentAdviceStatementInfo.setStatementLevelBalanceAmtPayableToSupplierOrClient(clientCommercialStatement.getBalancePayable());
                paymentAdviceStatementInfo.setStatementLevelNetPayableToSupplierOrClient(clientCommercialStatement.getTotalPayable());
                paymentAdviceStatementInfo.setStatementName(clientCommercialStatement.getStatementName());
                paymentAdviceStatementInfo.setCommercialStatementFor(CommercialStatementFor.CLIENT.getName());
                paymentAdviceStatementInfoSet.add(paymentAdviceStatementInfo);
            }
            else if (commercialStatementsPaymentAdviceResource.getCommercialStatementFor().equalsIgnoreCase(CommercialStatementFor.SUPPLIER.getName())) {
                SupplierCommercialStatement supplierCommercialStatement = supplierCommercialStatementRepo.get(attachedCommercialStatement.getStatementId());
                if (supplierCommercialStatement==null) throw new OperationException(Constants.ER01);
                /*if (attachedCommercialStatement.getAmountToBePaid().compareTo(supplierCommercialStatement.getBalancePayable())>0)
                    throw new OperationException(Constants.ER1026);*/
                if (!supplierCommercialStatement.getSupplierOrClientId().equalsIgnoreCase(commercialStatementsPaymentAdviceResource.getSupplierOrClientId()))
                    throw new OperationException(Constants.ER1025);
                if (!supplierCommercialStatement.getCurrency().equalsIgnoreCase(commercialStatementsPaymentAdviceResource.getCurrency()))
                    throw new OperationException(Constants.ER1031);
                netPayableToSupplierOrClient=netPayableToSupplierOrClient.add(supplierCommercialStatement.getTotalPayable());
                amountPaidToSupplierOrClient=amountPaidToSupplierOrClient.add(supplierCommercialStatement.getTotalPaid());
                balanceAmtPayable=balanceAmtPayable.add(supplierCommercialStatement.getBalancePayable());
                paymentAdviceStatementInfo.setStatementId(supplierCommercialStatement.getStatementId());
                paymentAdviceStatementInfo.setStatementLevelBalanceAmtPayableToSupplierOrClient(supplierCommercialStatement.getBalancePayable());
                paymentAdviceStatementInfo.setStatementLevelNetPayableToSupplierOrClient(supplierCommercialStatement.getTotalPayable());
                paymentAdviceStatementInfo.setStatementName(supplierCommercialStatement.getStatementName());
                paymentAdviceStatementInfo.setCommercialStatementFor(CommercialStatementFor.SUPPLIER.getName());
                paymentAdviceStatementInfoSet.add(paymentAdviceStatementInfo);
            }else throw new OperationException(Constants.ER1016);
        }

        if (commercialStatementsPaymentAdviceResource.getBalanceAmtPayableToSupplierOrClient().compareTo(balanceAmtPayable)!=0 || commercialStatementsPaymentAdviceResource.getNetPayableToSupplierOrClient().compareTo(netPayableToSupplierOrClient)!=0 || amountPaidToSupplierOrClient.compareTo(commercialStatementsPaymentAdviceResource.getAmountPaidToSupplierOrClient())!=0 )
            throw new OperationException(Constants.ER1027);

        amountTobepaid=commercialStatementsPaymentAdviceResource.getAmountToBePaid();
        if (amountTobepaid.compareTo(balanceAmtPayable) >0)
            throw new OperationException(Constants.ER1032);

        String paymentAdviceNumber=null;
        if (commercialStatementsPaymentAdviceResource.getCommercialStatementFor().equalsIgnoreCase(CommercialStatementFor.SUPPLIER.getName()))
            paymentAdviceNumber=createSupplierPaymentAdvice(commercialStatementsPaymentAdviceResource,paymentAdviceStatementInfoSet);
        else paymentAdviceNumber=createClientPaymentAdvice(commercialStatementsPaymentAdviceResource,paymentAdviceStatementInfoSet);

        Map<String, String> entity = new HashMap<>();
        entity.put("message", "payment Advice created successfully");
        entity.put("paymentAdviceNumber",paymentAdviceNumber);
        return entity;
    }

    private String createClientPaymentAdvice(CommercialStatementsPaymentAdviceResource commercialStatementsPaymentAdviceResource,Set<PaymentAdviceStatementInfo> paymentAdviceStatementInfoSet) throws OperationException {
        ClientPaymentAdvice clientPaymentAdvice=new ClientPaymentAdvice();
        clientPaymentAdvice.setClientId(commercialStatementsPaymentAdviceResource.getSupplierOrClientId());
        clientPaymentAdvice.setAmountPayableForClient(commercialStatementsPaymentAdviceResource.getAmountPaidToSupplierOrClient());
        clientPaymentAdvice.setBalanceAmtPayableToClient(commercialStatementsPaymentAdviceResource.getBalanceAmtPayableToSupplierOrClient());
        clientPaymentAdvice.setAmountPayableForClient(commercialStatementsPaymentAdviceResource.getAmountToBePaid());
        clientPaymentAdvice.setClientCurrency(commercialStatementsPaymentAdviceResource.getCurrency());
        clientPaymentAdvice.setClientName(commercialStatementsPaymentAdviceResource.getSupplierOrClientName());
        clientPaymentAdvice.setPaymentDueDate(commercialStatementsPaymentAdviceResource.getPaymentDueDate());
        clientPaymentAdvice.setModeOfPayment(commercialStatementsPaymentAdviceResource.getModeOfPayment());
        clientPaymentAdvice.setDayOfPayment(commercialStatementsPaymentAdviceResource.getDayOfPayment());
        clientPaymentAdvice.setDayOfPaymentAdviceGeneration(commercialStatementsPaymentAdviceResource.getDayOfPaymentAdviceGeneration());
        clientPaymentAdvice.setNetPayableToClient(commercialStatementsPaymentAdviceResource.getNetPayableToSupplierOrClient());
        clientPaymentAdvice.setPrePaymentApplicable(false);
        clientPaymentAdvice.setApproverRemark(commercialStatementsPaymentAdviceResource.getApproverRemark());
        clientPaymentAdvice.setPaymentRemark(commercialStatementsPaymentAdviceResource.getPaymentRemark());
        clientPaymentAdvice.setPaymentAdviceStatus(PaymentAdviceStatusValues.APPROVAL_PENDING);
        clientPaymentAdvice.setPaymentAdviceStatementInfoSet(paymentAdviceStatementInfoSet);
        try {
             clientPaymentAdvice=clientPaymentAdviceService.savePaymentAdvice(clientPaymentAdvice);
        }catch (Exception e){
            throw new OperationException(Constants.ER1030);
        }
        for (AttachedCommercialStatement attachedCommercialStatement:commercialStatementsPaymentAdviceResource.getAttachedCommercialStatements()) {
            ClientCommercialStatement clientCommercialStatement = clientCommercialStatementRepo.get(attachedCommercialStatement.getStatementId());
            Set<String> paymentAdviceIds = new HashSet<>();
            if (clientCommercialStatement.getPaymentAdviceIds() != null)
                paymentAdviceIds = clientCommercialStatement.getPaymentAdviceIds();
            paymentAdviceIds.add(clientPaymentAdvice.getPaymentAdviceNumber());
            clientCommercialStatement.setPaymentAdviceIds(paymentAdviceIds);
            clientCommercialStatementRepo.update(clientCommercialStatement);
        }
        return clientPaymentAdvice.getPaymentAdviceNumber();

    }

    private String createSupplierPaymentAdvice(CommercialStatementsPaymentAdviceResource commercialStatementsPaymentAdviceResource,Set<PaymentAdviceStatementInfo> paymentAdviceStatementInfoSet) throws OperationException {
        SupplierPaymentResource supplierPaymentResource=new SupplierPaymentResource();
        supplierPaymentResource.setBalanceAmtPayableToSupplier(commercialStatementsPaymentAdviceResource.getBalanceAmtPayableToSupplierOrClient());
        supplierPaymentResource.setNetPayableToSupplier(commercialStatementsPaymentAdviceResource.getNetPayableToSupplierOrClient());
        supplierPaymentResource.setPrePaymentApplicable(true);

        supplierPaymentResource.setPaymentDueDate(commercialStatementsPaymentAdviceResource.getPaymentDueDate());
        supplierPaymentResource.setSupplierRefId(commercialStatementsPaymentAdviceResource.getSupplierOrClientId());
        supplierPaymentResource.setSupplierName(commercialStatementsPaymentAdviceResource.getSupplierOrClientName());
        supplierPaymentResource.setSupplierCurrency(commercialStatementsPaymentAdviceResource.getCurrency());

        PaymentAdviceResource paymentAdviceResource=new PaymentAdviceResource();
        paymentAdviceResource.setPaymentAdviceGenerationDueDate(commercialStatementsPaymentAdviceResource.getPaymentDueDate());
        paymentAdviceResource.setAmountPayableForSupplier(commercialStatementsPaymentAdviceResource.getAmountToBePaid());
        paymentAdviceResource.setModeOfPayment(commercialStatementsPaymentAdviceResource.getModeOfPayment());
        paymentAdviceResource.setSelectedSupplierCurrency(supplierPaymentResource.getSupplierCurrency());
        paymentAdviceResource.setPaymentAdviceStatusId(PaymentAdviceStatusValues.APPROVAL_PENDING);
        paymentAdviceResource.setPayToSupplier(true);
        supplierPaymentResource.setPaymentAdviceResource(paymentAdviceResource);
        supplierPaymentResource.setPaymentAdviceStatementInfoSet(paymentAdviceStatementInfoSet);

        PaymentAdvice paymentAdvice=null;
        try {
            paymentAdvice = paymentAdviceService.savePaymentAdvice(supplierPaymentResource);
            for (AttachedCommercialStatement attachedCommercialStatement:commercialStatementsPaymentAdviceResource.getAttachedCommercialStatements()) {
                SupplierCommercialStatement supplierCommercialStatement = supplierCommercialStatementRepo.get(attachedCommercialStatement.getStatementId());
                Set<String> paymentAdviceIds = new HashSet<>();
                if (supplierCommercialStatement.getPaymentAdviceIds() != null)
                    paymentAdviceIds = supplierCommercialStatement.getPaymentAdviceIds();
                paymentAdviceIds.add(paymentAdvice.getPaymentAdviceNumber());
                supplierCommercialStatement.setPaymentAdviceIds(paymentAdviceIds);
                supplierCommercialStatementRepo.update(supplierCommercialStatement);
            }
            return paymentAdvice.getPaymentAdviceNumber();
        }catch (Exception e){
            logger.error("error in saving payment advice for commercial statements");
            throw new OperationException(Constants.ER1030);
        }
    }

    @Override
    public Map getPaymentAdviceDetails(CommercialStatementSearchCriteria commercialStatementSearchCriteria) throws OperationException {

        if (Arrays.asList(CommercialStatementFor.values()).stream().noneMatch(commercialStatementFor -> commercialStatementFor.getName().equalsIgnoreCase(commercialStatementSearchCriteria.getCommercialStatementFor())))
            throw new OperationException(Constants.ER1016);

        commercialStatementSearchCriteria.setPaymentAdviceResource(true);
        commercialStatementSearchCriteria.setCommercialType(CommercialType.PAYABLE.getValue());
        if (commercialStatementSearchCriteria.getCommercialStatementFor().equalsIgnoreCase(CommercialStatementFor.CLIENT.getName()))
            return clientCommercialStatementService.getPaymentAdviceResource(commercialStatementSearchCriteria);
        else
            return supplierCommercialStatementService.getPaymentAdviceResource(commercialStatementSearchCriteria);
    }

    @Override
    public CommercialStatementsBillPassing update(CommercialStatementsBillPassing commercialStatementsBillPassing) {
        return commercialStatementsBillPassingRepo.update(commercialStatementsBillPassing);
    }


    @Override
    public Map getBillPassingResource(CommercialStatementSearchCriteria commercialStatementSearchCriteria) throws OperationException {

        if (Arrays.asList(CommercialStatementFor.values()).stream().noneMatch(commercialStatementFor -> commercialStatementFor.getName().equalsIgnoreCase(commercialStatementSearchCriteria.getCommercialStatementFor())))
            throw new OperationException(Constants.ER1016);

        commercialStatementSearchCriteria.setBillPassingResource(true);
        commercialStatementSearchCriteria.setCommercialType(CommercialType.PAYABLE.getValue());
        if (commercialStatementSearchCriteria.getCommercialStatementFor().equalsIgnoreCase(CommercialStatementFor.CLIENT.getName()))
            return clientCommercialStatementService.getBillPassingResource(commercialStatementSearchCriteria);
        else
            return supplierCommercialStatementService.getBillPassingResource(commercialStatementSearchCriteria);
    }

    @Override
    public CommercialStatementsPaymentAdviceResource getPaymentAdviceByNumber(String paymentAdviceNumber,String commercialStatementFor) throws OperationException {
        CommercialStatementsPaymentAdviceResource paymentAdviceGeneration = new CommercialStatementsPaymentAdviceResource();
        if (commercialStatementFor==null) throw new OperationException(Constants.ER1016);
        if(Arrays.stream(CommercialStatementFor.values()).noneMatch(commercialStatementFor1 -> commercialStatementFor1.getName().equalsIgnoreCase(commercialStatementFor)))
            throw new OperationException(Constants.ER1016);

        try {
            if (commercialStatementFor.equalsIgnoreCase(CommercialStatementFor.SUPPLIER.getName())) {
                PaymentAdvice paymentAdvice = null;
                PaymentCriteria paymentCriteria = new PaymentCriteria();
                paymentCriteria.setPaymentAdviceNumber(paymentAdviceNumber);
                paymentCriteria.setSearchByPaymentAdviceNumber(true);
                List<PaymentAdvice> paymentAdvices = paymentAdviceService.searchSupplierPayment(paymentCriteria);
                if (paymentAdvices == null || paymentAdvices.isEmpty()) {
                    throw new OperationException("Failed to get payment Advice with payment advice number " + paymentAdviceNumber);
                }
                paymentAdvice = paymentAdvices.get(0);
                paymentAdviceGeneration.setCurrency(paymentAdvice.getSupplierCurrency());
                paymentAdviceGeneration.setCommercialStatementFor(CommercialStatementFor.SUPPLIER.getName());
                paymentAdviceGeneration.setSupplierOrClientId(paymentAdvice.getSupplierRefId());
                paymentAdviceGeneration.setSupplierOrClientName(paymentAdvice.getSupplierName());
                paymentAdviceGeneration.setPaymentAdviceAmount(paymentAdvice.getAmountPayableForSupplier());
                paymentAdviceGeneration.setNetPayableToSupplierOrClient(paymentAdvice.getNetPayableToSupplier());
                paymentAdviceGeneration.setAmountToBePaid(paymentAdvice.getAmountPayableForSupplier());
                paymentAdviceGeneration.setBalanceAmtPayableToSupplierOrClient(paymentAdvice.getBalanceAmtPayableToSupplier());
                paymentAdviceGeneration.setAmountPaidToSupplierOrClient(paymentAdvice.getNetPayableToSupplier().subtract(paymentAdvice.getBalanceAmtPayableToSupplier()));
                paymentAdviceGeneration.setModeOfPayment(paymentAdvice.getModeOfPayment());
                paymentAdviceGeneration.setPaymentAdviceNumber(paymentAdvice.getPaymentAdviceNumber());
                paymentAdviceGeneration.setPaymentDueDate(paymentAdvice.getPaymentDueDate());
                paymentAdviceGeneration.setPaymentAdviceStatus(paymentAdvice.getPaymentAdviceStatus());

                Set<AttachedCommercialStatement> attachedCommercialStatementSet=new HashSet<>();
                for (PaymentAdviceStatementInfo paymentAdviceStatementInfo:paymentAdvice.getPaymentAdviceStatementInfoSet()){
                    AttachedCommercialStatement attachedCommercialStatement=new AttachedCommercialStatement();
                    SupplierCommercialStatement supplierCommercialStatement=supplierCommercialStatementService.get(paymentAdviceStatementInfo.getStatementId());
                    attachedCommercialStatement.setClientServiceTax(supplierCommercialStatement.getTotalServiceTax());
                    attachedCommercialStatement.setCurrency(supplierCommercialStatement.getCurrency());
                    attachedCommercialStatement.setStatementId(supplierCommercialStatement.getStatementId());
                    attachedCommercialStatement.setStatementName(supplierCommercialStatement.getStatementName());
                    attachedCommercialStatement.setCommercialHead(supplierCommercialStatement.getCommercialHead());
                    attachedCommercialStatement.setProductName(supplierCommercialStatement.getProductName());
                    attachedCommercialStatement.setSupplierOrClientName(supplierCommercialStatement.getSupplierOrClientName());
                    attachedCommercialStatement.setSupplierorClientId(supplierCommercialStatement.getSupplierOrClientId());
                    attachedCommercialStatement.setTotalPayable(supplierCommercialStatement.getTotalPayable());
                    attachedCommercialStatement.setBalancePayable(supplierCommercialStatement.getBalancePayable());
                    attachedCommercialStatement.setTotalPaid(supplierCommercialStatement.getTotalPaid());
                    attachedCommercialStatementSet.add(attachedCommercialStatement);
                }
                paymentAdviceGeneration.setAttachedCommercialStatements(attachedCommercialStatementSet);
            }
            else {
                //code for client
//                PaymentAdvice paymentAdvice = null;
//                PaymentCriteria paymentCriteria = new PaymentCriteria();
//                paymentCriteria.setPaymentAdviceNumber(paymentAdviceNumber);
                ClientPaymentAdvice clientPaymentAdvice = clientPaymentAdviceService.getByPaymentAdviceNumber(paymentAdviceNumber);
                if (clientPaymentAdvice == null) {
                    throw new OperationException("Failed to get payment Advice with payment advice number " + paymentAdviceNumber);
                }

                paymentAdviceGeneration.setCurrency(clientPaymentAdvice.getClientCurrency());
                paymentAdviceGeneration.setCommercialStatementFor(CommercialStatementFor.CLIENT.getName());
                paymentAdviceGeneration.setSupplierOrClientId(clientPaymentAdvice.getClientId());
                paymentAdviceGeneration.setSupplierOrClientName(clientPaymentAdvice.getClientName());
                paymentAdviceGeneration.setAmountToBePaid(clientPaymentAdvice.getAmountPayableForClient());
                paymentAdviceGeneration.setNetPayableToSupplierOrClient(clientPaymentAdvice.getNetPayableToClient());
                paymentAdviceGeneration.setBalanceAmtPayableToSupplierOrClient(clientPaymentAdvice.getBalanceAmtPayableToClient());
                paymentAdviceGeneration.setAmountPaidToSupplierOrClient(clientPaymentAdvice.getNetPayableToClient().subtract(clientPaymentAdvice.getBalanceAmtPayableToClient()));
                paymentAdviceGeneration.setModeOfPayment(clientPaymentAdvice.getModeOfPayment());
                paymentAdviceGeneration.setPaymentAdviceNumber(clientPaymentAdvice.getPaymentAdviceNumber());
                paymentAdviceGeneration.setPaymentDueDate(clientPaymentAdvice.getPaymentDueDate());
                paymentAdviceGeneration.setPaymentAdviceStatus(clientPaymentAdvice.getPaymentAdviceStatus());
                Set<AttachedCommercialStatement> attachedCommercialStatementSet=new HashSet<>();
                for (PaymentAdviceStatementInfo paymentAdviceStatementInfo:clientPaymentAdvice.getPaymentAdviceStatementInfoSet()){
                    AttachedCommercialStatement attachedCommercialStatement=new AttachedCommercialStatement();
                    ClientCommercialStatement clientCommercialStatement=clientCommercialStatementService.get(paymentAdviceStatementInfo.getStatementId());
                    attachedCommercialStatement.setClientServiceTax(clientCommercialStatement.getTotalServiceTax());
                    attachedCommercialStatement.setCurrency(clientCommercialStatement.getCurrency());
                    attachedCommercialStatement.setStatementId(clientCommercialStatement.getStatementId());
                    attachedCommercialStatement.setStatementName(clientCommercialStatement.getStatementName());
                    attachedCommercialStatement.setCommercialHead(clientCommercialStatement.getCommercialHead());
                    attachedCommercialStatement.setProductName(clientCommercialStatement.getProductName());
                    attachedCommercialStatement.setSupplierOrClientName(clientCommercialStatement.getSupplierOrClientName());
                    attachedCommercialStatement.setSupplierorClientId(clientCommercialStatement.getSupplierOrClientId());
                    attachedCommercialStatement.setTotalPayable(clientCommercialStatement.getTotalPayable());
                    attachedCommercialStatement.setBalancePayable(clientCommercialStatement.getBalancePayable());
                    attachedCommercialStatement.setTotalPaid(clientCommercialStatement.getTotalPaid());
                    attachedCommercialStatementSet.add(attachedCommercialStatement);
                }
                paymentAdviceGeneration.setAttachedCommercialStatements(attachedCommercialStatementSet);
            }
        }catch (OperationException e){
            throw e;
        } catch (Exception e) {
            throw new OperationException(Constants.ER1030);
        }
        return paymentAdviceGeneration;
    }

    @Override
    public ClientPaymentAdvice getPaymentAdviceById(String id) throws OperationException {
        ClientPaymentAdvice clientPaymentAdvice = clientPaymentAdviceService.getById(id);
        return clientPaymentAdvice;
    }
}