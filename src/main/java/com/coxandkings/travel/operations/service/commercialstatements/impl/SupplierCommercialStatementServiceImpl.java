package com.coxandkings.travel.operations.service.commercialstatements.impl;

import com.coxandkings.travel.operations.enums.commercialStatements.*;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.accountsummary.PaymentAdvisePaymentDetails;
import com.coxandkings.travel.operations.model.commercialstatements.PaymentAdviceStatementInfo;
import com.coxandkings.travel.operations.model.commercialstatements.SupplierCommercialStatement;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice.PaymentAdvice;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentdetails.PaymentDetails;
import com.coxandkings.travel.operations.repository.commercialstatements.SupplierCommercialStatementRepo;
import com.coxandkings.travel.operations.resource.commercialStatement.*;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.commercialstatements.SupplierCommercialStatementService;
import com.coxandkings.travel.operations.systemlogin.MDMToken;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.coxandkings.travel.operations.utils.supplierBillPassing.DateConverter;
import org.apache.log4j.Logger;
import org.json.JSONObject;
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

@Service
public class SupplierCommercialStatementServiceImpl implements SupplierCommercialStatementService {

    @Autowired
    private SupplierCommercialStatementRepo supplierCommercialStatementRepo;

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private AlertService alertService;

    @Value("${finance-commercial-statements-settelement.supplier_step_one}")
    private String financeSupplierCommercialStatementStepOne;

    @Value(value = "${commercial-statements.supplier_id}")
    private String getSupplierSettlement_id;

    @Value(value = "${commercial-statements.getSupplierSettlementTerm}")
    private String getSupplierSettlementDetails;

    @Value(value = "${commercial-statements.getSupplierCommercials}")
    private String getSupplierCommercial;

    @Value(value = "${finance-commercial-statements-settelement.get_invoice}")
    private String getInvoiceDetails;

    @Value(value = "${commercial_statements.supplier_alert}")
    private String supplierAlert;

    @Autowired
    private MDMToken mdmToken;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Autowired
    private RestUtils restUtils;

    private Logger logger = Logger.getLogger(SupplierCommercialStatementServiceImpl.class);

    @Override
    public SupplierCommercialStatement get(String id) throws OperationException {
        SupplierCommercialStatement supplierCommercialStatement = supplierCommercialStatementRepo.get(id);
        if (supplierCommercialStatement == null) throw new OperationException(Constants.ER01);
        return supplierCommercialStatement;
    }

    @Override
    public Map updatePerformaInvoiceDetails(String statementId, String invoiceId) throws OperationException {
        SupplierCommercialStatement supplierCommercialStatement = supplierCommercialStatementRepo.get(statementId);
        if (supplierCommercialStatement == null) throw new OperationException(Constants.ER01);
        if (!supplierCommercialStatement.getCommercialType().equalsIgnoreCase(CommercialType.RECEIVABLE.getValue()))
            throw new OperationException(Constants.ER1001);
        supplierCommercialStatement.setPerformaInvoiceNumber(invoiceId);
        supplierCommercialStatementRepo.update(supplierCommercialStatement);
        Map<String, String> reponse = new HashMap<>();
        reponse.put("message", "successfully updated");
        return reponse;
    }

    @Override
    public Map updateFinalInvoiceDetails(String statementId, String invoiceId) throws OperationException {
        SupplierCommercialStatement supplierCommercialStatement = supplierCommercialStatementRepo.get(statementId);
        if (supplierCommercialStatement == null) throw new OperationException(Constants.ER01);
        if (!supplierCommercialStatement.getCommercialType().equalsIgnoreCase(CommercialType.RECEIVABLE.getValue()))
            throw new OperationException(Constants.ER1001);

        supplierCommercialStatement.setFinalInvoiceNumber(invoiceId);
        supplierCommercialStatementRepo.update(supplierCommercialStatement);
        Map<String, String> response = new HashMap<>();
        response.put("message", "successfully updates");
        return response;
    }

    @Override
    public Map searchByCriteria(CommercialStatementSearchCriteria commercialStatementSearchCriteria) throws OperationException {

        if (StringUtils.isEmpty(commercialStatementSearchCriteria.getCommercialStatementFor()) || !commercialStatementSearchCriteria.getCommercialStatementFor().equalsIgnoreCase(CommercialStatementFor.SUPPLIER.getName()))
            throw new OperationException(Constants.ER1016);
        if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getSettlementDueDateTo())) {
            if (DateConverter.stringToZonedDateTime(commercialStatementSearchCriteria.getSettlementDueDateTo()).compareTo(ZonedDateTime.now()) > 0)
                throw new OperationException(Constants.ER1017);
            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getSettlementDueDateFrom()))
                if (DateConverter.stringToZonedDateTime(commercialStatementSearchCriteria.getSettlementDueDateFrom()).compareTo(ZonedDateTime.now()) > 0 || DateConverter.stringToZonedDateTime(commercialStatementSearchCriteria.getSettlementDueDateFrom()).compareTo(DateConverter.stringToZonedDateTime(commercialStatementSearchCriteria.getSettlementDueDateTo())) > 0)
                    throw new OperationException(Constants.ER1018);
        }

        if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getBookingDateTo())) {
            if (DateConverter.stringToZonedDateTime(commercialStatementSearchCriteria.getBookingDateTo()).compareTo(ZonedDateTime.now()) > 0)
                throw new OperationException(Constants.ER1019);
            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getBookingDateFrom()))
                if (DateConverter.stringToZonedDateTime(commercialStatementSearchCriteria.getBookingDateFrom()).compareTo(ZonedDateTime.now()) > 0 || DateConverter.stringToZonedDateTime(commercialStatementSearchCriteria.getBookingDateFrom()).compareTo(DateConverter.stringToZonedDateTime(commercialStatementSearchCriteria.getBookingDateTo())) > 0)
                    throw new OperationException(Constants.ER1020);
        }

        if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getTravelDateTo())) {
            if (StringUtils.isEmpty(commercialStatementSearchCriteria.getProductCategorySubType()) || !commercialStatementSearchCriteria.getProductCategorySubType().equalsIgnoreCase("Flight"))
                if (DateConverter.stringToZonedDateTime(commercialStatementSearchCriteria.getTravelDateTo()).compareTo(ZonedDateTime.now()) > 0)
                    throw new OperationException(Constants.ER1021);
            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getTravelDateFrom()))
                if (DateConverter.stringToZonedDateTime(commercialStatementSearchCriteria.getTravelDateFrom()).compareTo(ZonedDateTime.now()) > 0 || DateConverter.stringToZonedDateTime(commercialStatementSearchCriteria.getTravelDateFrom()).compareTo(DateConverter.stringToZonedDateTime(commercialStatementSearchCriteria.getTravelDateTo())) > 0)
                    throw new OperationException(Constants.ER1022);
        }

        return supplierCommercialStatementRepo.searchByCriteria(commercialStatementSearchCriteria);
    }


    @Override
    public Map updateReceiptDetails(Set<String> statementIds, String receiptNumber) throws OperationException {
        //TODO need api for getting receipt details
        //TODO validate total amount with receipt details
        ResponseEntity<String> responseEntity=restUtils.exchange("http://10.25.6.237:8080/ams/api/v1/receipt/receiptNumber/"+receiptNumber,HttpMethod.GET, (HttpEntity<?>) null,String.class);
        BigDecimal totalAmount = new JSONObject(responseEntity.getBody()).getJSONObject("paymentEntity").getBigDecimal("functionalAmount");
        for (String statementId : statementIds) {
            SupplierCommercialStatement supplierCommercialStatement = supplierCommercialStatementRepo.get(statementId);
            if (supplierCommercialStatement == null) throw new OperationException(Constants.ER01);
            if (!supplierCommercialStatement.getCommercialType().equalsIgnoreCase(CommercialType.RECEIVABLE.getValue()))
                throw new OperationException(Constants.ER1001);


            if (totalAmount.compareTo(BigDecimal.ZERO) > 0) {
                if (supplierCommercialStatement.getBalanceReceivable().compareTo(totalAmount) < 0) {
                    totalAmount = totalAmount.subtract(supplierCommercialStatement.getBalancePayable());
                    supplierCommercialStatement.setTotalReceived(supplierCommercialStatement.getTotalReceivable());
                    supplierCommercialStatement.setBalanceReceivable(BigDecimal.ZERO);
                    supplierCommercialStatement.setSettlementStatus(SettlementStatus.SETTLED.getValue());
                } else {
                    supplierCommercialStatement.setBalanceReceivable(supplierCommercialStatement.getBalancePayable().subtract(totalAmount));
                    supplierCommercialStatement.setTotalReceived(supplierCommercialStatement.getTotalReceived().add(totalAmount));
                    totalAmount = BigDecimal.ZERO;
                    supplierCommercialStatement.setSettlementStatus(SettlementStatus.PARTIALLY_SETTLED.getValue());
                }
                supplierCommercialStatement.setReceiptNumber(receiptNumber);
                supplierCommercialStatementRepo.update(supplierCommercialStatement);
            }
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "successfully updated");
        return response;
    }

    @Override
    public Map updateCreditNoteDetails(String statementId, String creditNoteNumber) throws OperationException {
        SupplierCommercialStatement supplierCommercialStatement = supplierCommercialStatementRepo.get(statementId);
        if (supplierCommercialStatement == null) throw new OperationException(Constants.ER01);
        if (!supplierCommercialStatement.getCommercialType().equalsIgnoreCase(CommercialType.PAYABLE.getValue()))
            throw new OperationException(Constants.ER1002);
        supplierCommercialStatement.setCreditOrDebitNoteNumber(creditNoteNumber);
        supplierCommercialStatementRepo.update(supplierCommercialStatement);
        Map<String, String> reponse = new HashMap<>();
        reponse.put("message", "successfully updated");
        return reponse;
    }

    @Override
    public SupplierCommercialStatement getByName(String statementName) throws OperationException {
        SupplierCommercialStatement supplierCommercialStatement = supplierCommercialStatementRepo.getByName(statementName);
        if (supplierCommercialStatement == null) throw new OperationException(Constants.ER01);
        return supplierCommercialStatement;
    }

    @Override
    public InvoiceCommercial getInvoiceDetails(String invoiceNumber) throws OperationException {

        ResponseEntity<InvoiceCommercial> responseEntity = null;
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", mdmToken.getToken());
            HttpEntity httpEntity = new HttpEntity(httpHeaders);
            responseEntity = RestUtils.getTemplate().exchange(getInvoiceDetails + invoiceNumber, HttpMethod.GET, httpEntity, InvoiceCommercial.class);
        } catch (Exception e) {
            logger.debug("Error in getting commercials invoice details");
            throw new OperationException(Constants.ER1030);
        }

        return responseEntity.getBody();
    }

    @Override
    public SupplierCommercialStatement update(SupplierCommercialStatement supplierCommercialStatement) {

        supplierCommercialStatement.getCommercialStatementDetails().stream().forEach(commercialStatementDetails -> {
            commercialStatementDetails.getOrderCommercialDetails().stream().forEach(orderCommercialDetails -> {
                orderCommercialDetails.getPassengerDetailsSet().stream().forEach(passengerDetails -> {
                    passengerDetails.setOrderCommercialDetails(orderCommercialDetails);
                });
                orderCommercialDetails.setCommercialStatementDetails(commercialStatementDetails);
            });
            commercialStatementDetails.setCommercialStatement(supplierCommercialStatement);
        });

        if (supplierCommercialStatement.getCommercialStatementsBillPassing() != null)
            supplierCommercialStatement.setCommercialStatementsBillPassing(supplierCommercialStatement.getCommercialStatementsBillPassing());
        return supplierCommercialStatementRepo.update(supplierCommercialStatement);
    }

    @Override
    public Map getBillPassingResource(CommercialStatementSearchCriteria commercialStatementSearchCriteria) throws OperationException {

        Map result = new HashMap();
        try {
            result = supplierCommercialStatementRepo.searchByCriteria(commercialStatementSearchCriteria);
            Set<AttachedCommercialStatement> attachedCommercialStatementSet = new HashSet<>();
            CommercialStatementsBillPassingResource commercialStatementsBillPassingResource = new CommercialStatementsBillPassingResource();
            BigDecimal commercialValueAsPerStatement = BigDecimal.ZERO, clientServiceTaxAmount = BigDecimal.ZERO;
            ZonedDateTime paymentDueDate = null;
            Set<String> productname = new HashSet<>();

            for (String id : commercialStatementSearchCriteria.getAttachedStatementIds()) {
                AttachedCommercialStatement attachedCommercialStatement = new AttachedCommercialStatement();
                SupplierCommercialStatement supplierCommercialStatement = supplierCommercialStatementRepo.get(id);
                if (supplierCommercialStatement == null) throw new OperationException(Constants.ER01);
                if (!supplierCommercialStatement.getCommercialType().equalsIgnoreCase(CommercialType.PAYABLE.getValue()))
                    throw new OperationException(Constants.ER1002);
                if (supplierCommercialStatement.getCommercialStatementsBillPassing() != null)
                    throw new OperationException(Constants.ER1029);
                attachedCommercialStatement.setSupplierorClientId(supplierCommercialStatement.getSupplierOrClientId());
                attachedCommercialStatement.setSupplierOrClientName(supplierCommercialStatement.getSupplierOrClientName());
                attachedCommercialStatement.setProductName(supplierCommercialStatement.getProductName());
                attachedCommercialStatement.setTotalPayable(supplierCommercialStatement.getTotalPayable());
                attachedCommercialStatement.setCommercialHead(supplierCommercialStatement.getCommercialHead());
                attachedCommercialStatement.setStatementName(supplierCommercialStatement.getStatementName());
                attachedCommercialStatement.setStatementId(supplierCommercialStatement.getStatementId());
                attachedCommercialStatement.setCurrency(supplierCommercialStatement.getCurrency());
                attachedCommercialStatement.setClientServiceTax(BigDecimal.ZERO);
                attachedCommercialStatementSet.add(attachedCommercialStatement);

                if (productname.isEmpty())
                    productname.add(supplierCommercialStatement.getProductName());
                else if (!productname.contains(supplierCommercialStatement.getProductName()))
                    productname.add(supplierCommercialStatement.getProductName());

                if (supplierCommercialStatement.getTotalServiceTax() != null)
                    clientServiceTaxAmount = clientServiceTaxAmount.add(supplierCommercialStatement.getTotalServiceTax());

                if (paymentDueDate == null) paymentDueDate = supplierCommercialStatement.getSettlementDueDate();
                else if (paymentDueDate.compareTo(supplierCommercialStatement.getSettlementDueDate()) < 0)
                    paymentDueDate = supplierCommercialStatement.getSettlementDueDate();

                commercialValueAsPerStatement = commercialValueAsPerStatement.add(supplierCommercialStatement.getTotalPayable());
            }
            commercialStatementsBillPassingResource.setAttachedCommercialStatements(attachedCommercialStatementSet);
            commercialStatementsBillPassingResource.setPaymentDueDate(paymentDueDate);
            commercialStatementsBillPassingResource.setManualEntry(true);
            commercialStatementsBillPassingResource.setSupplierOrClientId(attachedCommercialStatementSet.stream().iterator().next().getSupplierorClientId());
            commercialStatementsBillPassingResource.setSupplierOrClientName(attachedCommercialStatementSet.stream().iterator().next().getSupplierOrClientName());
            commercialStatementsBillPassingResource.setEquivalentCommercialStatementAmount(commercialValueAsPerStatement);
            commercialStatementsBillPassingResource.setClientServiceTaxAmount(clientServiceTaxAmount);
            commercialStatementsBillPassingResource.setProductName(productname);
            commercialStatementsBillPassingResource.setNetPayableToSupplierOrClient(commercialValueAsPerStatement);
            commercialStatementsBillPassingResource.setInvoiceCurrency(attachedCommercialStatementSet.stream().iterator().next().getCurrency());
            commercialStatementsBillPassingResource.setCommercialStatementFor(commercialStatementSearchCriteria.getCommercialStatementFor());
            result.put("billPassingResource", commercialStatementsBillPassingResource);
            return result;
        }catch(OperationException e) {
            throw e;
        }catch (Exception e) {
            logger.error("Error occured in creating supplier commercial statements bill passing resource method");
            throw new OperationException(Constants.ER1030);
        }
    }


    @Override
    public Map getPaymentAdviceResource(CommercialStatementSearchCriteria commercialStatementSearchCriteria) throws OperationException {
        Map result=new HashMap();
        try {
            result = supplierCommercialStatementRepo.searchByCriteria(commercialStatementSearchCriteria);
            Set<AttachedCommercialStatement> attachedCommercialStatements = new HashSet<>();
            BigDecimal netPayableToSupplierOrClient = BigDecimal.ZERO;
            BigDecimal amountPaidToSupplierOrClient = BigDecimal.ZERO;
            BigDecimal balanceAmtPayable = BigDecimal.ZERO;
            ZonedDateTime paymentDueDate=null;

            for (String id : commercialStatementSearchCriteria.getAttachedStatementIds()) {
                SupplierCommercialStatement supplierCommercialStatement = supplierCommercialStatementRepo.get(id);
                if (supplierCommercialStatement == null) throw new OperationException(Constants.ER01);
                if (!supplierCommercialStatement.getCommercialType().equalsIgnoreCase(CommercialType.PAYABLE.getValue()))
                    throw new OperationException(Constants.ER1002);
                if (supplierCommercialStatement.getCommercialStatementsBillPassing() == null || supplierCommercialStatement.getCommercialStatementsBillPassing().getBillPassingStatus().equalsIgnoreCase(CommercialStatementBillStatus.PENDING_APPROVAL.getValue()))
                    throw new OperationException(Constants.ER1024);
                AttachedCommercialStatement attachedCommercialStatement = new AttachedCommercialStatement();
                attachedCommercialStatement.setCommercialHead(supplierCommercialStatement.getCommercialHead());
                attachedCommercialStatement.setProductName(supplierCommercialStatement.getProductName());
                attachedCommercialStatement.setStatementId(id);
                attachedCommercialStatement.setCurrency(supplierCommercialStatement.getCurrency());
                attachedCommercialStatement.setStatementName(supplierCommercialStatement.getStatementName());
                attachedCommercialStatement.setSupplierOrClientName(supplierCommercialStatement.getSupplierOrClientName());
                attachedCommercialStatement.setTotalPayable(supplierCommercialStatement.getTotalPayable());
                attachedCommercialStatement.setSupplierorClientId(supplierCommercialStatement.getSupplierOrClientId());
                if (supplierCommercialStatement.getSettlementDueDate()!=null){
                    if (paymentDueDate==null) paymentDueDate=supplierCommercialStatement.getSettlementDueDate();
                    else if (paymentDueDate.compareTo(supplierCommercialStatement.getSettlementDueDate())<0)
                        paymentDueDate=supplierCommercialStatement.getSettlementDueDate();
                }
                if (supplierCommercialStatement.getTotalServiceTax() != null)
                    attachedCommercialStatement.setClientServiceTax(supplierCommercialStatement.getTotalServiceTax());
                attachedCommercialStatements.add(attachedCommercialStatement);
                netPayableToSupplierOrClient = netPayableToSupplierOrClient.add(attachedCommercialStatement.getTotalPayable());
                amountPaidToSupplierOrClient = amountPaidToSupplierOrClient.add(supplierCommercialStatement.getTotalPaid());
                balanceAmtPayable = balanceAmtPayable.add(supplierCommercialStatement.getBalancePayable());
            }
            CommercialStatementsPaymentAdviceResource commercialStatementsPaymentAdviceResource = new CommercialStatementsPaymentAdviceResource();
            commercialStatementsPaymentAdviceResource.setAmountPaidToSupplierOrClient(amountPaidToSupplierOrClient);
            commercialStatementsPaymentAdviceResource.setNetPayableToSupplierOrClient(netPayableToSupplierOrClient);
            commercialStatementsPaymentAdviceResource.setBalanceAmtPayableToSupplierOrClient(balanceAmtPayable);
            commercialStatementsPaymentAdviceResource.setSupplierOrClientId(attachedCommercialStatements.stream().iterator().next().getSupplierorClientId());
            commercialStatementsPaymentAdviceResource.setSupplierOrClientName(attachedCommercialStatements.stream().iterator().next().getSupplierOrClientName());
            commercialStatementsPaymentAdviceResource.setCurrency(attachedCommercialStatements.stream().iterator().next().getCurrency());
            commercialStatementsPaymentAdviceResource.setCommercialStatementFor(commercialStatementSearchCriteria.getCommercialStatementFor());
            commercialStatementsPaymentAdviceResource.setAmountToBePaid(balanceAmtPayable);
            commercialStatementsPaymentAdviceResource.setPaymentDueDate(paymentDueDate);
            commercialStatementsPaymentAdviceResource.setAttachedCommercialStatements(attachedCommercialStatements);
            result.put("paymentAdviceResource",commercialStatementsPaymentAdviceResource);
            return result;
        }catch (OperationException e){
            throw e;
        }catch (Exception e){
            logger.error("Error occured in creating supplier commercial statements payment advice resource method");
            throw new OperationException(Constants.ER1030);
        }
    }

    @Override
    public Set<String> getSupplierNames() {
        return supplierCommercialStatementRepo.getSupplierNames();
    }

    @Override
    public Set<String> getCommercialHeads() {
        return supplierCommercialStatementRepo.getCommercialHeads();
    }

    @Override
    public Set<String> getCompanyMarkets() {
        return supplierCommercialStatementRepo.getCompanyMarkets();
    }

    @Override
    public Set<String> getCurrency() {
        return supplierCommercialStatementRepo.getCurrency();
    }

    @Override
    public Set<String> getProductCategories() {
        return supplierCommercialStatementRepo.getProductCategories();
    }

    @Override
    public Set<String> getProductCategorySubTypes() {
        return supplierCommercialStatementRepo.getProductCategorySubTypes();
    }

    @Override
    public Set<String> getProductNames() {
        return supplierCommercialStatementRepo.getProductNames();
    }

    @Override
    public Set<String> getSettlementStatus() {
        Set<String> settlementStatuses=new HashSet<>();
        Arrays.asList(SettlementStatus.values()).forEach(settlementStatus -> settlementStatuses.add(settlementStatus.getValue()));
        return settlementStatuses;
    }

    @Override
    public CommercialStatementSortingCriteria[] getSortingCriteria() {
        return CommercialStatementSortingCriteria.values();
    }

    @Override
    public void updatePaymentDetails(PaymentAdvice paymentAdvice) {
        BigDecimal amountPaid=paymentAdvice.getAmountPayableForSupplier();
        for (PaymentAdviceStatementInfo paymentAdviceStatementInfo:paymentAdvice.getPaymentAdviceStatementInfoSet()){
            SupplierCommercialStatement supplierCommercialStatement=supplierCommercialStatementRepo.get(paymentAdviceStatementInfo.getStatementId());
            if (amountPaid.compareTo(BigDecimal.ZERO) > 0) {
                if (supplierCommercialStatement.getBalancePayable().compareTo(amountPaid) <= 0) {
                    amountPaid = amountPaid.subtract(supplierCommercialStatement.getBalancePayable());
                    supplierCommercialStatement.setTotalPaid(supplierCommercialStatement.getTotalPayable());
                    supplierCommercialStatement.setBalancePayable(BigDecimal.ZERO);
                    supplierCommercialStatement.setSettlementStatus(SettlementStatus.SETTLED.getValue());
                }else {
                    supplierCommercialStatement.setBalancePayable(supplierCommercialStatement.getBalancePayable().subtract(amountPaid));
                    supplierCommercialStatement.setTotalPaid(supplierCommercialStatement.getTotalPaid().add(amountPaid));
                    amountPaid=BigDecimal.ZERO;
                    supplierCommercialStatement.setSettlementStatus(SettlementStatus.PARTIALLY_SETTLED.getValue());
                }
                supplierCommercialStatementRepo.update(supplierCommercialStatement);
            } else return;
        }
    }

}
