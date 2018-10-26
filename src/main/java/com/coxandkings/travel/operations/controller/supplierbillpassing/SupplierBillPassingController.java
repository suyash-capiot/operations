package com.coxandkings.travel.operations.controller.supplierbillpassing;

import com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability.ServiceOrderSearchCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.supplierbillpassing.SupplierBillPassing;
import com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability.ServiceOrderResource;
import com.coxandkings.travel.operations.resource.supplierbillpassing.PaymentAdviceGeneration;
import com.coxandkings.travel.operations.resource.supplierbillpassing.SupplierBillPassingResource;
import com.coxandkings.travel.operations.service.supplierbillpassing.SupplierBillPassingService;
import com.coxandkings.travel.operations.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/supplierBillPassing/")
public class SupplierBillPassingController {

    @Autowired
    private SupplierBillPassingService supplierBillPassingService;

    @PostMapping(value = "/v1/singleEntry")
    public Map add(@RequestBody SupplierBillPassingResource supplierBillPassingResource) throws OperationException {
        try {
            return supplierBillPassingService.singleBillEntry(supplierBillPassingResource);
        }catch (OperationException e){
            throw e;
        }catch (Exception e){
            throw new OperationException(Constants.ER1030);
        }
    }

    @PostMapping(value="/v1/approveOrReject")
    public Map updateStatus(@RequestParam String billPassingId, @RequestParam String status, @RequestParam String remarks) throws OperationException {
        try {
            return supplierBillPassingService.updateStatus(billPassingId, status, remarks);
        }catch (OperationException e){
            throw e;
        }catch (Exception e){
            throw new OperationException(Constants.ER1030);
        }
    }

    @GetMapping(value = "/v1/get")
    public SupplierBillPassingResource get(String id) throws OperationException {
        try {
            return supplierBillPassingService.get(id);
        }catch (OperationException e){
            throw e;
        }catch (Exception e){
            throw new OperationException(Constants.ER1030);
        }
    }

    @GetMapping(value = "/v1/getAll")
    public List<SupplierBillPassing> getAll(){
        return supplierBillPassingService.getAll();
    }

    @PostMapping(value = "/v1/generate/creditDebitNote")
    public Map generateCreditDebit(@RequestParam String provisionalServiceOrderId) throws OperationException {
        try {
            return supplierBillPassingService.generateCreditDebit(provisionalServiceOrderId);
        }catch (OperationException e){
            throw e;
        }catch (Exception e){
            throw new OperationException(Constants.ER1030);
        }
    }

    //Whenever it is approved.
    @PostMapping(value = "/v1/commonEntry")
    public Map commonBillPassing(@RequestBody SupplierBillPassingResource supplierBillPassingResource) throws OperationException {
        try {
            return supplierBillPassingService.commonBillEntry(supplierBillPassingResource);
        }catch (OperationException e){
            throw e;
        }catch (Exception e){
            throw new OperationException(Constants.ER1030);
        }

    }

    @GetMapping(value = "/v1/getApprovalStatusList")
    private List<String> getApprovalStatusList(){
        return supplierBillPassingService.getApprovalStatusList();
    }

    @PostMapping("/v1/generate/paymentAdvice")
    public Map generatePaymentAdvice(@RequestBody PaymentAdviceGeneration paymentAdviceGeneration) throws OperationException{
        try {
            return supplierBillPassingService.generatePaymentAdvice(paymentAdviceGeneration);
        }catch (OperationException e){
            throw e;
        }catch (Exception e){
            throw new OperationException(Constants.ER1030);
        }
    }

    @GetMapping(value = "/v1/getStatusList")
    private List<String> getStatusList(){
        return supplierBillPassingService.getStatusList();
    }

    @GetMapping(value = "/v1/stopPaymentUntil/getAll")
    public Set<String > values(){ return supplierBillPassingService.getStopPaymentUntilValues(); }

    @GetMapping(value = "/v1/flightDiscrepancy/getFinalServiceOrders")
    public Set<ServiceOrderResource> getFinalServiceOrdersByDiscrepancyId(@RequestParam String id) throws OperationException {
        try {
            return supplierBillPassingService.getFinalServiceOrdersByDiscrepancyId(id);
        }catch (Exception e){
            throw new OperationException(Constants.ER1030);
        }
    }

    @GetMapping(value = "/v1/flightDiscrepancy/getPaymentAdvice")
    public Map<String, Object> getPaymentAdviceResourceByDiscrepancyId(@RequestParam String id) throws OperationException {
        try {
            return supplierBillPassingService.getPaymentAdviceResourceByDiscrepancyId(id);
        }catch (Exception e){
            throw new OperationException(Constants.ER1030);
        }
    }

    @GetMapping(value = "/v1/getUserRole")
    public  String getUserRole(String token) throws UnsupportedEncodingException, OperationException {
        return supplierBillPassingService.getUserRole(token);
    }

    @PostMapping(value = "/v1/getSupplierBillPassingResource")
    public Map getSupplierBillPassingResource(@RequestBody ServiceOrderSearchCriteria serviceOrderSearchCriteria) throws OperationException {
        try {
            return supplierBillPassingService.getCommonBillPassingResource(serviceOrderSearchCriteria);
        }catch (OperationException e){
            throw e;
        }catch (Exception e){
            throw new OperationException(Constants.ER1030);
        }
    }

    @PostMapping(value = "/v1/getPaymentAdviceResource")
    public Map getPaymentAdviceResource(@RequestBody ServiceOrderSearchCriteria serviceOrderSearchCriteria) throws OperationException {
        try {
            return supplierBillPassingService.getPaymentAdviceResource(serviceOrderSearchCriteria);
        }catch (OperationException e){
            throw e;
        }catch (Exception e){
            throw new OperationException(Constants.ER1030);
        }
    }

    @GetMapping(value = "/v1/getSingleEntryResource")
    public SupplierBillPassingResource getSingleEntryResource(@RequestParam String provisionalServiceOrderId) throws OperationException {
        try {
            return supplierBillPassingService.getSingleEntryResource(provisionalServiceOrderId);
        }catch (OperationException e){
            throw e;
        }catch (Exception e){
            throw new OperationException(Constants.ER1030);
        }
    }

    @GetMapping(value = "/v1/paymentAdvice/get")
    public PaymentAdviceGeneration getPaymentAdvice(@RequestParam("id") String paymentAdviceNumber) throws OperationException {
        try {
            return supplierBillPassingService.getPaymentAdviceById(paymentAdviceNumber);
        }catch (OperationException e){
            throw e;
        }catch (Exception e){
            throw new OperationException(Constants.ER1030);
        }
    }

    @PostMapping(value = "v1/attachedServiceOrders/pagination")
    public Map<String, Object> getAttachedServiceOrders(@RequestBody List<String> attachedServiceOrderIds,
                                        @RequestParam Integer page,
                                        @RequestParam Integer size,
                                        @RequestParam(value = "sortCriteria", required = false) String sortCriteria,
                                        @RequestParam(value = "descending", required = false) boolean descending) throws OperationException {
        return supplierBillPassingService.getAttachedServiceOrders(attachedServiceOrderIds, page, size, sortCriteria, descending);
    }

    @PostMapping(value = "v1/paymentAdvice/attachedServiceOrders/pagination")
    public Map<String, Object> getPaymentAdviceAttachedServiceOrders(@RequestBody List<String> attachedServiceOrderIds,
                                                        @RequestParam Integer page,
                                                        @RequestParam Integer size,
                                                        @RequestParam(value = "sortCriteria", required = false) String sortCriteria,
                                                        @RequestParam(value = "descending", required = false) boolean descending) throws OperationException {
        return supplierBillPassingService.getPaymentAdviceAttachedServiceOrders(attachedServiceOrderIds, page, size, sortCriteria, descending);
    }

}