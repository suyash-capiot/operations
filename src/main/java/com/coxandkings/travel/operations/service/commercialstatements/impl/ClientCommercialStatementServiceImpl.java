package com.coxandkings.travel.operations.service.commercialstatements.impl;

import com.coxandkings.travel.operations.enums.commercialStatements.*;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.commercialstatements.*;
import com.coxandkings.travel.operations.repository.commercialstatements.ClientCommercialStatementRepo;
import com.coxandkings.travel.operations.resource.commercialStatement.*;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.commercialstatements.ClientCommercialStatementService;
import com.coxandkings.travel.operations.systemlogin.MDMToken;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.coxandkings.travel.operations.utils.supplierBillPassing.DateConverter;
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

@Service
public class ClientCommercialStatementServiceImpl implements ClientCommercialStatementService {

    @Autowired
    private ClientCommercialStatementRepo clientCommercialStatementRepo;

    @Autowired
    private AlertService alertService;

    @Autowired
    private OpsBookingService opsBookingService;

    @Value(value = "${finance-commercial-statements-settelement.client_step_one}")
    private String financeClientCommercialStatementStepOne;

    @Value(value = "${commercial-statements.client_id}")
    private String getClientSettlement_id;

    @Value(value = "${commercial-statements.getClientSettlementTerm}")
    private String getClientSettlementDetails;

    @Value(value = "${finance-commercial-statements-settelement.get_invoice}")
    private String getInvoiceDetails;

    @Value(value = "${commercial_statements.client_alert}")
    private String clientAlert;

    @Autowired
    private MDMToken mdmToken;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    private Logger logger=Logger.getLogger(ClientCommercialStatementServiceImpl.class);


    @Override
    public ClientCommercialStatement get(String id) throws OperationException {
        ClientCommercialStatement clientCommercialStatement= clientCommercialStatementRepo.get(id);
        if (clientCommercialStatement==null) throw new OperationException(Constants.ER01);
        return clientCommercialStatement;
    }

    @Override
    public Map updatePerformaInvoiceDetails(String statementId,String invoiceId) throws OperationException {
        ClientCommercialStatement clientCommercialStatement=clientCommercialStatementRepo.get(statementId);
        if (clientCommercialStatement==null) throw new OperationException(Constants.ER01);
        if (!clientCommercialStatement.getCommercialType().equalsIgnoreCase(CommercialType.RECEIVABLE.getValue()))
            throw new OperationException(Constants.ER1001);
        clientCommercialStatement.setPerformaInvoiceNumber(invoiceId);
        clientCommercialStatementRepo.update(clientCommercialStatement);
        Map<String,String> reponse=new HashMap<>();
        reponse.put("message","successfully updated");
        return reponse;
    }

    @Override
    public Map updateCreditNoteDetails(String statementId, String creditNoteNumber) throws OperationException {
        ClientCommercialStatement clientCommercialStatement=clientCommercialStatementRepo.get(statementId);
        if (clientCommercialStatement==null) throw new OperationException(Constants.ER01);
        if (!clientCommercialStatement.getCommercialType().equalsIgnoreCase(CommercialType.PAYABLE.getValue()))
            throw new OperationException(Constants.ER1002);
        clientCommercialStatement.setCreditOrDebitNoteNumber(creditNoteNumber);
        clientCommercialStatementRepo.update(clientCommercialStatement);
        Map<String,String> reponse=new HashMap<>();
        reponse.put("message","successfully updated");
        return reponse;
    }

    @Override
    public Map updateFinalInvoiceDetails(String statementId,String invoiceId) throws OperationException {
        ClientCommercialStatement clientCommercialStatement=clientCommercialStatementRepo.get(statementId);
        if (clientCommercialStatement==null) throw new OperationException(Constants.ER01);
        if (!clientCommercialStatement.getCommercialType().equalsIgnoreCase(CommercialType.RECEIVABLE.getValue()))
            throw new OperationException(Constants.ER1001);
        clientCommercialStatement.setFinalInvoiceNumber(invoiceId);
        clientCommercialStatementRepo.update(clientCommercialStatement);
        Map<String,String> reponse=new HashMap<>();
        reponse.put("message","successfully updated");
        return reponse;
    }

    @Override
    public Map updateReceiptDetails(Set<String> statementIds, String receiptNumber) throws OperationException {

        //TODO need api for getting receipt details
        //TODO validate total amount with receipt details
        BigDecimal totalAmount=BigDecimal.ZERO;
        for (String statementId:statementIds) {
            ClientCommercialStatement clientCommercialStatement = clientCommercialStatementRepo.get(statementId);
            if (clientCommercialStatement == null) throw new OperationException(Constants.ER01);
            if (!clientCommercialStatement.getCommercialType().equalsIgnoreCase(CommercialType.RECEIVABLE.getValue()))
                throw new OperationException(Constants.ER1001);


            if (totalAmount.compareTo(BigDecimal.ZERO) > 0) {
                if (clientCommercialStatement.getBalanceReceivable().compareTo(totalAmount) < 0) {
                    totalAmount = totalAmount.subtract(clientCommercialStatement.getBalancePayable());
                    clientCommercialStatement.setTotalReceived(clientCommercialStatement.getTotalReceivable());
                    clientCommercialStatement.setBalanceReceivable(BigDecimal.ZERO);
                    clientCommercialStatement.setSettlementStatus(SettlementStatus.SETTLED.getValue());
                }else {
                    clientCommercialStatement.setBalanceReceivable(clientCommercialStatement.getBalancePayable().subtract(totalAmount));
                    clientCommercialStatement.setTotalReceived(clientCommercialStatement.getTotalReceived().add(totalAmount));
                    totalAmount=BigDecimal.ZERO;
                    clientCommercialStatement.setSettlementStatus(SettlementStatus.PARTIALLY_SETTLED.getValue());
                }
                clientCommercialStatement.setReceiptNumber(receiptNumber);
                clientCommercialStatementRepo.update(clientCommercialStatement);
            }
        }

        Map<String,String> response=new HashMap<>();
        response.put("message","successfully updated");
        return response;
    }

    @Override
    public ClientCommercialStatement create(ClientCommercialStatement clientCommercialStatement) throws OperationException {
        return clientCommercialStatementRepo.add(clientCommercialStatement);
    }

    @Override
    public Map searchByCriteria(CommercialStatementSearchCriteria commercialStatementSearchCriteria) throws OperationException {

       if (StringUtils.isEmpty(commercialStatementSearchCriteria.getCommercialStatementFor()) || !commercialStatementSearchCriteria.getCommercialStatementFor().equalsIgnoreCase(CommercialStatementFor.CLIENT.getName()))
           throw new OperationException(Constants.ER1016);
        if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getSettlementDueDateTo())) {
            if (DateConverter.stringToZonedDateTime(commercialStatementSearchCriteria.getSettlementDueDateTo()).compareTo(ZonedDateTime.now())>0)
                throw new OperationException(Constants.ER1017);
            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getSettlementDueDateFrom()))
                if (DateConverter.stringToZonedDateTime(commercialStatementSearchCriteria.getSettlementDueDateFrom()).compareTo(ZonedDateTime.now()) >0 || DateConverter.stringToZonedDateTime(commercialStatementSearchCriteria.getSettlementDueDateFrom()).compareTo(DateConverter.stringToZonedDateTime(commercialStatementSearchCriteria.getSettlementDueDateTo()))>0)
                    throw new OperationException(Constants.ER1018);
        }

        if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getBookingDateTo())) {
            if (DateConverter.stringToZonedDateTime(commercialStatementSearchCriteria.getBookingDateTo()).compareTo(ZonedDateTime.now()) >0 )
                throw new OperationException(Constants.ER1019);
            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getBookingDateFrom()))
                if (DateConverter.stringToZonedDateTime(commercialStatementSearchCriteria.getBookingDateFrom()).compareTo(ZonedDateTime.now()) >0 || DateConverter.stringToZonedDateTime(commercialStatementSearchCriteria.getBookingDateFrom()).compareTo(DateConverter.stringToZonedDateTime(commercialStatementSearchCriteria.getBookingDateTo()))>0)
                    throw new OperationException(Constants.ER1020);
        }

        if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getTravelDateTo())) {
            if (StringUtils.isEmpty(commercialStatementSearchCriteria.getProductCategorySubType()) || !commercialStatementSearchCriteria.getProductCategorySubType().equalsIgnoreCase("Flight"))
                if (DateConverter.stringToZonedDateTime(commercialStatementSearchCriteria.getTravelDateTo()).compareTo(ZonedDateTime.now()) > 0)
                    throw new OperationException(Constants.ER1021);
            if (!StringUtils.isEmpty(commercialStatementSearchCriteria.getTravelDateFrom()))
                if (DateConverter.stringToZonedDateTime(commercialStatementSearchCriteria.getTravelDateFrom()).compareTo(ZonedDateTime.now()) >0 || DateConverter.stringToZonedDateTime(commercialStatementSearchCriteria.getTravelDateFrom()).compareTo(DateConverter.stringToZonedDateTime(commercialStatementSearchCriteria.getTravelDateTo())) > 0)
                    throw new OperationException(Constants.ER1022);
        }


        return clientCommercialStatementRepo.searchByCriteria(commercialStatementSearchCriteria);
    }

    @Override
    public ClientCommercialStatement getByName(String statementName) throws OperationException {
        ClientCommercialStatement clientCommercialStatement= clientCommercialStatementRepo.getByName(statementName);
        if (clientCommercialStatement==null)
            throw new OperationException(Constants.ER01);
        return clientCommercialStatement;
    }

    @Override
    public InvoiceCommercial getInvoiceDetails(String invoiceNumber) {
        ResponseEntity<InvoiceCommercial> responseEntity=null;
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", mdmToken.getToken());
            HttpEntity httpEntity = new HttpEntity(httpHeaders);
            responseEntity= RestUtils.getTemplate().exchange(getInvoiceDetails+invoiceNumber, HttpMethod.POST, httpEntity, InvoiceCommercial.class);
        } catch (Exception e) {
            logger.debug("Error in calling finance supplier commercial statements api after generation");
        }

        return responseEntity.getBody();
    }

    @Override
    public ClientCommercialStatement update(ClientCommercialStatement clientCommercialStatement) {

        clientCommercialStatement.getCommercialStatementDetails().stream().forEach(commercialStatementDetails -> {
            commercialStatementDetails.getOrderCommercialDetails().stream().forEach(orderCommercialDetails -> {
                orderCommercialDetails.getPassengerDetailsSet().stream().forEach(passengerDetails -> {
                    passengerDetails.setOrderCommercialDetails(orderCommercialDetails);
                });
                orderCommercialDetails.setCommercialStatementDetails(commercialStatementDetails);
            });
            commercialStatementDetails.setCommercialStatement(clientCommercialStatement);
        });

        if (clientCommercialStatement.getCommercialStatementsBillPassing()!=null)
            clientCommercialStatement.setCommercialStatementsBillPassing(clientCommercialStatement.getCommercialStatementsBillPassing());
        return clientCommercialStatementRepo.update(clientCommercialStatement);
    }

    @Override
    public Map getBillPassingResource(CommercialStatementSearchCriteria commercialStatementSearchCriteria) throws OperationException {
        Map result = new HashMap();
        commercialStatementSearchCriteria.setBillPassingResource(true);
        try {
            result = clientCommercialStatementRepo.searchByCriteria(commercialStatementSearchCriteria);
            Set<AttachedCommercialStatement> attachedCommercialStatementSet = new HashSet<>();
            CommercialStatementsBillPassingResource commercialStatementsBillPassingResource = new CommercialStatementsBillPassingResource();
            BigDecimal commercialValueAsPerStatement = BigDecimal.ZERO, clientServiceTaxAmount = BigDecimal.ZERO;
            ZonedDateTime paymentDueDate = null;
            Set<String> productname = new HashSet<>();

            for (String id : commercialStatementSearchCriteria.getAttachedStatementIds()) {
                BigDecimal clientServiceTax= BigDecimal.ZERO;
                AttachedCommercialStatement attachedCommercialStatement = new AttachedCommercialStatement();
                ClientCommercialStatement clientCommercialStatement = clientCommercialStatementRepo.get(id);
                if (clientCommercialStatement == null) throw new OperationException(Constants.ER01);
                if (!clientCommercialStatement.getCommercialType().equalsIgnoreCase(CommercialType.PAYABLE.getValue()))
                    throw new OperationException(Constants.ER1002);
                if (clientCommercialStatement.getCommercialStatementsBillPassing() != null)
                    throw new OperationException(Constants.ER1029);
                attachedCommercialStatement.setSupplierorClientId(clientCommercialStatement.getSupplierOrClientId());
                attachedCommercialStatement.setSupplierOrClientName(clientCommercialStatement.getSupplierOrClientName());
                attachedCommercialStatement.setProductName(clientCommercialStatement.getProductName());
                attachedCommercialStatement.setTotalPayable(clientCommercialStatement.getTotalPayable());
                attachedCommercialStatement.setCommercialHead(clientCommercialStatement.getCommercialHead());
                attachedCommercialStatement.setStatementName(clientCommercialStatement.getStatementName());
                attachedCommercialStatement.setStatementId(clientCommercialStatement.getStatementId());
                attachedCommercialStatement.setCurrency(clientCommercialStatement.getCurrency());
                clientServiceTax=clientServiceTax.add(clientCommercialStatement.getTotalServiceTax());
                attachedCommercialStatement.setClientServiceTax(clientServiceTax);
                attachedCommercialStatementSet.add(attachedCommercialStatement);

                if (productname.isEmpty())
                    productname.add(clientCommercialStatement.getProductName());
                else if (!productname.contains(clientCommercialStatement.getProductName()))
                    productname.add(clientCommercialStatement.getProductName());

                if (clientCommercialStatement.getTotalServiceTax() != null)
                    clientServiceTaxAmount = clientServiceTaxAmount.add(clientCommercialStatement.getTotalServiceTax());

                if (paymentDueDate == null)
                    paymentDueDate = clientCommercialStatement.getSettlementDueDate();
                else if (paymentDueDate.compareTo(clientCommercialStatement.getSettlementDueDate()) < 0)
                    paymentDueDate = clientCommercialStatement.getSettlementDueDate();

                commercialValueAsPerStatement = commercialValueAsPerStatement.add(clientCommercialStatement.getTotalPayable());

            }
            commercialStatementsBillPassingResource.setAttachedCommercialStatements(attachedCommercialStatementSet);
            commercialStatementsBillPassingResource.setPaymentDueDate(paymentDueDate);
            commercialStatementsBillPassingResource.setManualEntry(true);
            commercialStatementsBillPassingResource.setInvoiceCurrency(attachedCommercialStatementSet.stream().iterator().next().getCurrency());
            commercialStatementsBillPassingResource.setSupplierOrClientId(attachedCommercialStatementSet.stream().iterator().next().getSupplierorClientId());
            commercialStatementsBillPassingResource.setSupplierOrClientName(attachedCommercialStatementSet.stream().iterator().next().getSupplierOrClientName());
            commercialStatementsBillPassingResource.setEquivalentCommercialStatementAmount(commercialValueAsPerStatement);
            commercialStatementsBillPassingResource.setNetPayableToSupplierOrClient(commercialValueAsPerStatement);
            commercialStatementsBillPassingResource.setClientServiceTaxAmount(clientServiceTaxAmount);
            commercialStatementsBillPassingResource.setProductName(productname);
            commercialStatementsBillPassingResource.setCommercialStatementFor(commercialStatementSearchCriteria.getCommercialStatementFor());
            result.put("billPassingResource", commercialStatementsBillPassingResource);
            return result;
        } catch (OperationException e){
            throw e;
        }catch (Exception e) {
            logger.error("Error occured in creating commercial statements bill passing resource method");
            throw new OperationException(Constants.ER1030);
        }
    }

    @Override
    public Map getPaymentAdviceResource(CommercialStatementSearchCriteria commercialStatementSearchCriteria) throws OperationException {
        Map result=new HashMap();
        commercialStatementSearchCriteria.setPaymentAdviceResource(true);
        try {
            result = clientCommercialStatementRepo.searchByCriteria(commercialStatementSearchCriteria);
            Set<AttachedCommercialStatement> attachedCommercialStatements = new HashSet<>();
            BigDecimal netPayableToSupplierOrClient = BigDecimal.ZERO;
            BigDecimal amountPaidToSupplierOrClient = BigDecimal.ZERO;
            BigDecimal balanceAmtPayable = BigDecimal.ZERO;
            BigDecimal serviceTax = BigDecimal.ZERO;

            for (String id : commercialStatementSearchCriteria.getAttachedStatementIds()) {
                ClientCommercialStatement clientCommercialStatement = clientCommercialStatementRepo.get(id);
                if (clientCommercialStatement == null) throw new OperationException(Constants.ER01);
                if (!clientCommercialStatement.getCommercialType().equalsIgnoreCase(CommercialType.PAYABLE.getValue()))
                    throw new OperationException(Constants.ER1002);
                if (clientCommercialStatement.getCommercialStatementsBillPassing() == null || clientCommercialStatement.getCommercialStatementsBillPassing().getBillPassingStatus().equalsIgnoreCase(CommercialStatementBillStatus.PENDING_APPROVAL.getValue()))
                    throw new OperationException(Constants.ER1024);
                AttachedCommercialStatement attachedCommercialStatement = new AttachedCommercialStatement();
                attachedCommercialStatement.setCommercialHead(clientCommercialStatement.getCommercialHead());
                attachedCommercialStatement.setProductName(clientCommercialStatement.getProductName());
                attachedCommercialStatement.setStatementId(id);
                attachedCommercialStatement.setCurrency(clientCommercialStatement.getCurrency());
                attachedCommercialStatement.setStatementName(clientCommercialStatement.getStatementName());
                attachedCommercialStatement.setSupplierOrClientName(clientCommercialStatement.getSupplierOrClientName());
                attachedCommercialStatement.setTotalPayable(clientCommercialStatement.getTotalPayable());
                attachedCommercialStatement.setSupplierorClientId(clientCommercialStatement.getSupplierOrClientId());
                if (clientCommercialStatement.getTotalServiceTax() != null)
                    attachedCommercialStatement.setClientServiceTax(clientCommercialStatement.getTotalServiceTax());
                else attachedCommercialStatement.setClientServiceTax(BigDecimal.ZERO);
                attachedCommercialStatements.add(attachedCommercialStatement);
                netPayableToSupplierOrClient = netPayableToSupplierOrClient.add(attachedCommercialStatement.getTotalPayable());
                amountPaidToSupplierOrClient = amountPaidToSupplierOrClient.add(clientCommercialStatement.getTotalPaid());
                balanceAmtPayable = balanceAmtPayable.add(clientCommercialStatement.getBalancePayable());
            }
            CommercialStatementsPaymentAdviceResource commercialStatementsPaymentAdviceResource = new CommercialStatementsPaymentAdviceResource();
            commercialStatementsPaymentAdviceResource.setAmountPaidToSupplierOrClient(amountPaidToSupplierOrClient);
            commercialStatementsPaymentAdviceResource.setNetPayableToSupplierOrClient(netPayableToSupplierOrClient);
            commercialStatementsPaymentAdviceResource.setBalanceAmtPayableToSupplierOrClient(balanceAmtPayable);
            commercialStatementsPaymentAdviceResource.setSupplierOrClientId(attachedCommercialStatements.stream().iterator().next().getSupplierorClientId());
            commercialStatementsPaymentAdviceResource.setSupplierOrClientName(attachedCommercialStatements.stream().iterator().next().getSupplierOrClientName());
            commercialStatementsPaymentAdviceResource.setCurrency(attachedCommercialStatements.stream().iterator().next().getCurrency());
            commercialStatementsPaymentAdviceResource.setCommercialStatementFor(commercialStatementSearchCriteria.getCommercialStatementFor());
            commercialStatementsPaymentAdviceResource.setServiceTax(serviceTax);
            commercialStatementsPaymentAdviceResource.setAttachedCommercialStatements(attachedCommercialStatements);
            result.put("paymentAdviceResource",commercialStatementsPaymentAdviceResource);
            return result;
        }catch (OperationException e){
            throw e;
        }catch (Exception e){
            logger.error("Error occured in creating client commercial statements payment advice resource method");
            throw new OperationException(Constants.ER1030);
        }
    }

    @Override
    public Set<String> getCompanyMarkets() {
        return clientCommercialStatementRepo.getCompanyMarkets();
    }

    @Override
    public Set<String> getClientNames() {
        return clientCommercialStatementRepo.getClientNames();
    }

    @Override
    public Set<String> getCommercialHeads() {
        return clientCommercialStatementRepo.getCommercialHeads();
    }

    @Override
    public Set<String> getCurrency() {
        return clientCommercialStatementRepo.getCurrency();
    }

    @Override
    public Set<String> getProductCategories() {
        return clientCommercialStatementRepo.getProductCategories();
    }

    @Override
    public Set<String> getProductCategorySubTypes() {
        return clientCommercialStatementRepo.getProductCategorySubTypes();
    }

    @Override
    public Set<String> getProductNames() {
        return clientCommercialStatementRepo.getProductNames();
    }

    @Override
    public Set<String> getClientCategories() {
        return clientCommercialStatementRepo.getClientCategories();
    }

    @Override
    public Set<String> getClientSubCategories() {
        return clientCommercialStatementRepo.getClientSubCategories();
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
    public void updatePaymentDetails(ClientPaymentAdvice clientPaymentAdvice) {
        BigDecimal amountPaid=clientPaymentAdvice.getAmountPayableForClient();
        for (PaymentAdviceStatementInfo clientCommercialStatementPaymentDetail:clientPaymentAdvice.getPaymentAdviceStatementInfoSet()){
            ClientCommercialStatement clientCommercialStatement=clientCommercialStatementRepo.get(clientCommercialStatementPaymentDetail.getStatementId());
            if (amountPaid.compareTo(BigDecimal.ZERO) > 0) {
                if (clientCommercialStatement.getBalancePayable().compareTo(amountPaid) < 0) {
                    amountPaid = amountPaid.subtract(clientCommercialStatement.getBalancePayable());
                    clientCommercialStatement.setTotalPaid(clientCommercialStatement.getTotalPayable());
                    clientCommercialStatement.setBalancePayable(BigDecimal.ZERO);
                    clientCommercialStatement.setSettlementStatus(SettlementStatus.SETTLED.getValue());
                }else {
                    clientCommercialStatement.setBalancePayable(clientCommercialStatement.getBalancePayable().subtract(amountPaid));
                    clientCommercialStatement.setTotalPaid(clientCommercialStatement.getTotalPaid().add(amountPaid));
                    amountPaid=BigDecimal.ZERO;
                    clientCommercialStatement.setSettlementStatus(SettlementStatus.PARTIALLY_SETTLED.getValue());
                }
                clientCommercialStatementRepo.update(clientCommercialStatement);
            } else return;
        }
    }

}

