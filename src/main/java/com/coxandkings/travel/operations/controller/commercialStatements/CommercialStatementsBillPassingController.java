package com.coxandkings.travel.operations.controller.commercialStatements;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.commercialstatements.ClientPaymentAdvice;
import com.coxandkings.travel.operations.model.commercialstatements.CommercialStatementsBillPassing;
import com.coxandkings.travel.operations.resource.commercialStatement.CommercialStatementSearchCriteria;
import com.coxandkings.travel.operations.resource.commercialStatement.CommercialStatementsBillPassingResource;
import com.coxandkings.travel.operations.resource.commercialStatement.CommercialStatementsPaymentAdviceResource;
import com.coxandkings.travel.operations.service.commercialstatements.CommercialStatementsBillPassingService;
import com.coxandkings.travel.operations.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/commercialStatementsBillPassing")
public class CommercialStatementsBillPassingController {

    @Autowired
    private CommercialStatementsBillPassingService commercialStatementsBillPassingService;

    @PostMapping(value = "/v1/approveOrReject")
    public Map updateStatus(@RequestParam String billPassingId, @RequestParam String status, @RequestParam(required = false) String remarks) throws OperationException {
        try {
            return commercialStatementsBillPassingService.updateStatus(billPassingId, status, remarks);
        } catch (OperationException e) {
            throw e;
        } catch (Exception e) {
            throw new OperationException(Constants.ER1030);
        }
    }

    @GetMapping(value = "/v1/get")
    public CommercialStatementsBillPassingResource get(@RequestParam  String billPassingId) throws OperationException {
        return commercialStatementsBillPassingService.get(billPassingId);
    }

    @PostMapping(value = "/v1/invoiceEntry")
    public Map commonBillPassing(@RequestBody CommercialStatementsBillPassingResource commercialStatementsBillPassingResource) throws OperationException {
        return commercialStatementsBillPassingService.invoiceEntry(commercialStatementsBillPassingResource);
    }

    @GetMapping(value = "/v1/getApprovalStatusList")
    private List<String> getApprovalStatusList() throws OperationException {
        return commercialStatementsBillPassingService.getApprovalStatusList();
    }

    @PostMapping("/v1/generate/paymentAdvice")
    public Map generatePaymentAdvice(@RequestBody CommercialStatementsPaymentAdviceResource commercialStatementsPaymentAdviceResource) throws OperationException {
        return commercialStatementsBillPassingService.generatePaymentAdvice(commercialStatementsPaymentAdviceResource);
    }

    @PostMapping("/v1/getBillPassingResource")
    public Map getBillPassingResource(@RequestBody CommercialStatementSearchCriteria commercialStatementSearchCriteria) throws OperationException {
        return commercialStatementsBillPassingService.getBillPassingResource(commercialStatementSearchCriteria);
    }

    @PostMapping("/v1/getPaymentAdviceDetails")
    public Map getPaymentAdviceDetails(@RequestBody CommercialStatementSearchCriteria commercialStatementSearchCriteria) throws OperationException {
        return commercialStatementsBillPassingService.getPaymentAdviceDetails(commercialStatementSearchCriteria);
    }

    @PostMapping("/v1/update")
    public CommercialStatementsBillPassing update(@RequestBody CommercialStatementsBillPassing commercialStatementsBillPassing) throws OperationException {
        return commercialStatementsBillPassingService.update(commercialStatementsBillPassing);
    }

    @GetMapping("/v1/paymentAdvice/get")
    public CommercialStatementsPaymentAdviceResource getPaymentAdvice(@RequestParam String paymentAdviceNumber,@RequestParam String commercialStatementFor) throws OperationException {
        try {
            return commercialStatementsBillPassingService.getPaymentAdviceByNumber(paymentAdviceNumber,commercialStatementFor);
        } catch (OperationException e) {
            throw e;
        } catch (Exception e) {
            throw new OperationException(Constants.ER1030);
        }
    }

    @GetMapping("/v1/paymentAdvice/getById/{id}")
    public ClientPaymentAdvice getPaymentAdviceById(@PathVariable String id) throws OperationException {
        try {
            return commercialStatementsBillPassingService.getPaymentAdviceById(id);
        } catch (OperationException e) {
            throw e;
        }
    }



}
