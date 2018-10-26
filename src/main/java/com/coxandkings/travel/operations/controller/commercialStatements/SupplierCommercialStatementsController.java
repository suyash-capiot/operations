package com.coxandkings.travel.operations.controller.commercialStatements;

import com.coxandkings.travel.operations.enums.commercialStatements.CommercialStatementSortingCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.commercialstatements.SupplierCommercialStatement;
import com.coxandkings.travel.operations.resource.commercialStatement.CommercialStatementSearchCriteria;
import com.coxandkings.travel.operations.resource.commercialStatement.InvoiceCommercial;
import com.coxandkings.travel.operations.service.commercialstatements.SupplierCommercialStatementService;
import com.coxandkings.travel.operations.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/supplierCommercialStatement")
public class SupplierCommercialStatementsController {
    @Autowired
    private SupplierCommercialStatementService supplierCommercialStatementService;

    /*//to generate commercial statement
    @PostMapping(value = "/v1/create")
    public Map create(@RequestBody CommercialStatementGeneration commercialStatementGeneration) throws OperationException {
        try {
            return supplierCommercialStatementService.create(commercialStatementGeneration);
        } catch (OperationException e) {
            throw e;
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_11030);
        }
    }*/

    //to get commercial statement
    @GetMapping(value = "/v1/get")
    public SupplierCommercialStatement getCommercialStatementById(@RequestParam String statementId) throws OperationException {
        try {
            return supplierCommercialStatementService.get(statementId);
        } catch (OperationException e) {
            throw e;
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_11031);
        }
    }

    //to get commercial statement by name
    @GetMapping(value = "/v1/getByName")
    public SupplierCommercialStatement getCommercialStatementByName(@RequestParam String statementName) throws OperationException {
        try {
            return supplierCommercialStatementService.getByName(statementName);
        } catch (OperationException e) {
            throw e;
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_11032);
        }
    }

    //search by criteria
    @PostMapping(value = "/v1/searchByCriteria")
    public Map search(@RequestBody CommercialStatementSearchCriteria commercialStatementSearchCriteria) throws OperationException {
        try {
            return supplierCommercialStatementService.searchByCriteria(commercialStatementSearchCriteria);
        } catch (OperationException e) {
            throw e;
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_11033);
        }
    }

    @PostMapping(value = "/v1/updatePerformaInvoiceDetails")
    public Map updatePerformaInvoiceDetails(@RequestParam String statementid,@RequestParam String invoiceId) throws OperationException {
        try {
            return supplierCommercialStatementService.updatePerformaInvoiceDetails(statementid,invoiceId);
        } catch (OperationException e) {
            throw e;
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_11034);
        }
    }

    @PostMapping(value = "/v1/updateFinalInvoiceDetails")
    public Map updateFinalInvoiceDetails(@RequestParam String statementId, @RequestParam String invoiceId) throws OperationException {
        try {
            return supplierCommercialStatementService.updateFinalInvoiceDetails(statementId, invoiceId);
        } catch (OperationException e) {
            throw e;
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_11035);
        }
    }

    @PostMapping(value = "/v1/updateReceiptDetails")
    public Map updateReceiptDetails(@RequestBody Set<String> statementIds, @RequestParam String receiptNumber) throws OperationException {
        try {
            return supplierCommercialStatementService.updateReceiptDetails(statementIds, receiptNumber);
        } catch (OperationException e) {
            throw e;
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_11036);
        }
    }

    @PostMapping(value = "/v1/updateCreditNoteDetails")
    public Map updateCreditNoteeDetails(@RequestParam String statementId, @RequestParam String creditNoteNumber) throws OperationException {
        try {
            return supplierCommercialStatementService.updateCreditNoteDetails(statementId, creditNoteNumber);
        } catch (OperationException e) {
            throw e;
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_11037);
        }
    }

    @GetMapping(value = "/v1/getInvoiceDetails")
    public InvoiceCommercial getCommercialStatemementInvoiceDetails(@RequestParam String invoiceNumber) throws OperationException {
        try {
            return supplierCommercialStatementService.getInvoiceDetails(invoiceNumber);
        } catch (OperationException e) {
            throw e;
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_11038);
        }
    }

    @PostMapping(value = "/v1/update")
    public SupplierCommercialStatement update(@RequestBody SupplierCommercialStatement supplierCommercialStatement) throws OperationException {
        try {
            return supplierCommercialStatementService.update(supplierCommercialStatement);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_11039);
        }
    }

    @GetMapping(value = "/v1/getSupplierNames")
    public Set<String> getClientNames() throws OperationException {
        try {
            return supplierCommercialStatementService.getSupplierNames();
        } catch (Exception e) {
            throw new OperationException(Constants.ER1030);
        }
    }

    @GetMapping(value = "/v1/getCommercialHeads")
    public Set<String> getCommercialHeads() throws OperationException {
        try {
            return supplierCommercialStatementService.getCommercialHeads();
        } catch (Exception e) {
            throw new OperationException(Constants.ER1030);
        }
    }

    @GetMapping(value = "/v1/getCompanyMarkets")
    public Set<String> getCompanyMarkets() throws OperationException {
        try {
            return supplierCommercialStatementService.getCompanyMarkets();
        } catch (Exception e) {
            throw new OperationException(Constants.ER1030);
        }
    }

    @GetMapping(value = "/v1/getCurrency")
    public Set<String> getCurrency() throws OperationException {
        try {
            return supplierCommercialStatementService.getCurrency();
        } catch (Exception e) {
            throw new OperationException(Constants.ER1030);
        }
    }

    @GetMapping(value = "/v1/getProductCategories")
    public Set<String> getProductCategories() throws OperationException {
        try {
            return supplierCommercialStatementService.getProductCategories();
        } catch (Exception e) {
            throw new OperationException(Constants.ER1030);
        }
    }

    @GetMapping(value = "/v1/getProductCategorySubTypes")
    public Set<String> getProductCategorySubTypes() throws OperationException {
        try {
            return supplierCommercialStatementService.getProductCategorySubTypes();
        } catch (Exception e) {
            throw new OperationException(Constants.ER1030);
        }
    }

    @GetMapping(value = "/v1/getProductNames")
    public Set<String> getProductNames() throws OperationException {
        try {
            return supplierCommercialStatementService.getProductNames();
        } catch (Exception e) {
            throw new OperationException(Constants.ER1030);
        }
    }

    @GetMapping(value = "/v1/getSettlementStatus")
    public Set<String> getSettlementStatus() throws OperationException {
        try {
            return supplierCommercialStatementService.getSettlementStatus();
        } catch (Exception e) {
            throw new OperationException(Constants.ER1030);
        }
    }

    @GetMapping(value = "/v1/getSortingCriteria")
    public CommercialStatementSortingCriteria[] getSortingCriteria(){
        return supplierCommercialStatementService.getSortingCriteria();
    }
}
