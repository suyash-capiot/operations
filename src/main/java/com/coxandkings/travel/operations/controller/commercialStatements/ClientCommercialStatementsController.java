package com.coxandkings.travel.operations.controller.commercialStatements;

import com.coxandkings.travel.operations.enums.commercialStatements.CommercialStatementSortingCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.commercialstatements.ClientCommercialStatement;
import com.coxandkings.travel.operations.model.commercialstatements.ClientPaymentAdvice;
import com.coxandkings.travel.operations.resource.commercialStatement.CommercialStatementSearchCriteria;
import com.coxandkings.travel.operations.resource.commercialStatement.InvoiceCommercial;
import com.coxandkings.travel.operations.service.commercialstatements.ClientCommercialStatementService;
import com.coxandkings.travel.operations.service.commercialstatements.ClientPaymentAdviceService;
import com.coxandkings.travel.operations.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/clientCommercialStatement")
public class ClientCommercialStatementsController {

    @Autowired
    private ClientCommercialStatementService clientCommercialStatementService;

    @Autowired
    private ClientPaymentAdviceService clientPaymentAdviceService;

    //to generate commercial statement
    @PostMapping(value = "/v1/create")
    public ClientCommercialStatement create(@RequestBody ClientCommercialStatement clientCommercialStatement) throws OperationException {
        try {
            return clientCommercialStatementService.create(clientCommercialStatement);
        } catch (OperationException e) {
            throw e;
        } catch (Exception e) {
            throw new OperationException(Constants.ER1030);
        }
    }

    //to get commercial statement
    @GetMapping(value = "/v1/get")
    public ClientCommercialStatement getCommercialStatementById(@RequestParam String statementId) throws OperationException {
        try {
            return clientCommercialStatementService.get(statementId);
        } catch (OperationException e) {
            throw e;
        } catch (Exception e) {
            throw new OperationException(Constants.ER1030);
        }
    }

    //to get commercial statement
    @GetMapping(value = "/v1/getByName")
    public ClientCommercialStatement getCommercialStatementByName(@RequestParam String statementName) throws OperationException {
        try {
            return clientCommercialStatementService.getByName(statementName);
        } catch (OperationException e) {
            throw e;
        } catch (Exception e) {
            throw new OperationException(Constants.ER1030);
        }
    }



    //search by criteria
    @PostMapping(value = "/v1/searchByCriteria")
    public Map search(@RequestBody CommercialStatementSearchCriteria commercialStatementSearchCriteria) throws OperationException {
        try {
            return clientCommercialStatementService.searchByCriteria(commercialStatementSearchCriteria);
        } catch (OperationException e) {
            throw e;
        } catch (Exception e) {
            throw new OperationException(Constants.ER1030);
        }
    }

    @PostMapping(value = "/v1/updatePerformaInvoiceDetails")
    public Map updatePerformaInvoiceDetails(@RequestParam String statementId, @RequestParam String invoiceId) throws OperationException {
        try {
            return clientCommercialStatementService.updatePerformaInvoiceDetails(statementId, invoiceId);
        } catch (OperationException e) {
            throw e;
        } catch (Exception e) {
            throw new OperationException(Constants.ER1030);
        }
    }

    @PostMapping(value = "/v1/updateFinalInvoiceDetails")
    public Map updateFinalInvoiceDetails(@RequestParam String statementId, @RequestParam String invoiceId) throws OperationException {
        try {
            return clientCommercialStatementService.updateFinalInvoiceDetails(statementId, invoiceId);
        } catch (OperationException e) {
            throw e;
        } catch (Exception e) {
            throw new OperationException(Constants.ER1030);
        }
    }

    @PostMapping(value = "/v1/updateReceiptDetails")
    public Map updateReceiptDetails(@RequestBody Set<String> statementIds, @RequestParam String receiptNumber) throws OperationException {
        try {
            return clientCommercialStatementService.updateReceiptDetails(statementIds, receiptNumber);
        } catch (OperationException e) {
            throw e;
        } catch (Exception e) {
            throw new OperationException(Constants.ER1030);
        }
    }

    @PostMapping(value = "/v1/updateCreditNoteDetails")
    public Map updateCreditNoteeDetails(@RequestParam String statementId, @RequestParam String creditNoteNumber) throws OperationException {
        try {
            return clientCommercialStatementService.updateCreditNoteDetails(statementId, creditNoteNumber);
        } catch (OperationException e) {
            throw e;
        } catch (Exception e) {
            throw new OperationException(Constants.ER1030);
        }
    }

    @GetMapping(value = "/v1/getInvoiceDetails")
    public InvoiceCommercial getCommercialStatemementInvoiceDetails(@RequestParam String invoiceNumber) throws OperationException {
        try {
            return clientCommercialStatementService.getInvoiceDetails(invoiceNumber);
        } catch (Exception e) {
            throw new OperationException(Constants.ER1030);
        }
    }

    @PostMapping(value = "/v1/{paymentAdviceId}/approve")
    public ResponseEntity<ClientPaymentAdvice> approvePaymentAdvise(@PathVariable("paymentAdviceId") String paymentAdviceId, @RequestBody String remarks) throws OperationException {
        try {
            ClientPaymentAdvice paymentAdvice = clientPaymentAdviceService.approvePaymentAdvise(paymentAdviceId, remarks);
            return new ResponseEntity<>(paymentAdvice, HttpStatus.OK);
        }catch (OperationException e) {
            throw e;
        } catch (Exception e) {
            throw new OperationException(Constants.ER1030);
        }
    }

    @PostMapping(value = "/v1/{paymentAdviceId}/reject")
    public ResponseEntity<ClientPaymentAdvice> rejectPaymentAdvice(@PathVariable("paymentAdviceId") String paymentAdviceId, @RequestBody String remarks) throws OperationException {
        try {
            ClientPaymentAdvice paymentAdvice = clientPaymentAdviceService.rejectPaymentAdvice(paymentAdviceId, remarks);
            return new ResponseEntity<>(paymentAdvice, HttpStatus.OK);
        }catch (OperationException e) {
            throw e;
        } catch (Exception e) {
            throw new OperationException(Constants.ER1030);
        }
    }

    @PostMapping(value = "/v1/update")
    public ClientCommercialStatement update(@RequestBody ClientCommercialStatement clientCommercialStatement) throws OperationException {
        try {
            return clientCommercialStatementService.update(clientCommercialStatement);
        } catch (Exception e) {
            throw new OperationException(Constants.ER1030);
        }
    }

    @GetMapping(value = "/v1/getClientNames")
    public Set<String> getClientNames() throws OperationException {
        try {
            return clientCommercialStatementService.getClientNames();
        } catch (Exception e) {
            throw new OperationException(Constants.ER1030);
        }
    }

    @GetMapping(value = "/v1/getCommercialHeads")
    public Set<String> getCommercialHeads() throws OperationException {
        try {
            return clientCommercialStatementService.getCommercialHeads();
        } catch (Exception e) {
            throw new OperationException(Constants.ER1030);
        }
    }

    @GetMapping(value = "/v1/getCompanyMarkets")
    public Set<String> getCompanyMarkets() throws OperationException {
        try {
            return clientCommercialStatementService.getCompanyMarkets();
        } catch (Exception e) {
            throw new OperationException(Constants.ER1030);
        }
    }

    @GetMapping(value = "/v1/getCurrency")
    public Set<String> getCurrency() throws OperationException {
        try {
            return clientCommercialStatementService.getCurrency();
        } catch (Exception e) {
            throw new OperationException(Constants.ER1030);
        }
    }

    @GetMapping(value = "/v1/getProductCategories")
    public Set<String> getProductCategories() throws OperationException {
        try {
            return clientCommercialStatementService.getProductCategories();
        } catch (Exception e) {
            throw new OperationException(Constants.ER1030);
        }
    }

    @GetMapping(value = "/v1/getProductCategorySubTypes")
    public Set<String> getProductCategorySubTypes() throws OperationException {
        try {
            return clientCommercialStatementService.getProductCategorySubTypes();
        } catch (Exception e) {
            throw new OperationException(Constants.ER1030);
        }
    }

    @GetMapping(value = "/v1/getProductNames")
    public Set<String> getProductNames() throws OperationException {
        try {
            return clientCommercialStatementService.getProductNames();
        } catch (Exception e) {
            throw new OperationException(Constants.ER1030);
        }
    }

    @GetMapping(value = "/v1/getClientCategories")
    public Set<String> getClientCategories() throws OperationException {
        try {
            return clientCommercialStatementService.getClientCategories();
        } catch (Exception e) {
            throw new OperationException(Constants.ER1030);
        }
    }

    @GetMapping(value = "/v1/getClientSubCategories")
    public Set<String> getClientSubCategories() throws OperationException {
        try {
            return clientCommercialStatementService.getClientSubCategories();
        } catch (Exception e) {
            throw new OperationException(Constants.ER1030);
        }
    }

    @GetMapping(value = "/v1/getSettlementStatus")
    public Set<String> getSettlementStatus() throws OperationException {
        try {
            return clientCommercialStatementService.getSettlementStatus();
        } catch (Exception e) {
            throw new OperationException(Constants.ER1030);
        }
    }

    @GetMapping(value = "/v1/getSortingCriteria")
    public CommercialStatementSortingCriteria[] getSortingCriteria(){
        return clientCommercialStatementService.getSortingCriteria();
    }

}
