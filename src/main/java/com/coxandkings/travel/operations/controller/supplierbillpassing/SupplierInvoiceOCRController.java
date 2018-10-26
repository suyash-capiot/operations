package com.coxandkings.travel.operations.controller.supplierbillpassing;

import com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability.ServiceOrderSearchCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.supplierbillpassing.SupplierInvoiceOCR;
import com.coxandkings.travel.operations.resource.commercialStatement.CommercialStatementSearchCriteria;
import com.coxandkings.travel.operations.resource.supplierbillpassing.SupplierInvoiceId;
import com.coxandkings.travel.operations.resource.supplierbillpassing.SupplierInvoiceSearchCriteria;
import com.coxandkings.travel.operations.service.supplierbillpassing.SupplierInvoiceOCRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/supplierInvoice")
public class SupplierInvoiceOCRController {

    @Autowired
    private SupplierInvoiceOCRService supplierInvoiceOCRService;

    @PostMapping("/add")
    public Map add(@RequestBody SupplierInvoiceOCR supplierInvoiceOCR) throws OperationException {
        supplierInvoiceOCRService.save(supplierInvoiceOCR);
        Map<String, String> entity = new HashMap<>();
        entity.put("message", "successfully updated");
        return entity;
    }

    @PostMapping("/v1/add")
    public Map save(@RequestBody SupplierInvoiceOCR supplierInvoiceOCR) throws OperationException {
        supplierInvoiceOCRService.save(supplierInvoiceOCR);
        Map<String, String> entity = new HashMap<>();
        entity.put("message", "successfully updated");
        return entity;
    }

    @PostMapping(value = "v1/getAvailableInvoice")
    public Set<SupplierInvoiceId> getAvailableInvoice(@RequestBody SupplierInvoiceSearchCriteria supplierInvoiceSearchCriteria) throws OperationException {
        return supplierInvoiceOCRService.getAvailableInvoice(supplierInvoiceSearchCriteria);
    }

  /*  @GetMapping(value = "v1/get")
    public SupplierBillPassingResource get(@RequestParam String id) throws OperationException {
        return supplierInvoiceOCRService.get(id);
    }*/

    @PostMapping(value = "/v1/getCommercialStatementInvoice")
    public Map getCommercialStatementBillPassingResource(@RequestParam String id, @RequestBody CommercialStatementSearchCriteria commercialStatementSearchCriteria) throws OperationException {
        return supplierInvoiceOCRService.getCommercialStatementInvoice(id,commercialStatementSearchCriteria);
    }

    @PostMapping(value = "v1/getSupplierBillPassingResource")
    public Map get(@RequestParam String id, @RequestBody ServiceOrderSearchCriteria serviceOrderSearchCriteria) throws OperationException {
        return supplierInvoiceOCRService.getSupplierBillPassingResource(id,serviceOrderSearchCriteria);
    }
}
