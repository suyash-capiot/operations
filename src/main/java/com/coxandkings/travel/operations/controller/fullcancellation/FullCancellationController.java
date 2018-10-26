package com.coxandkings.travel.operations.controller.fullcancellation;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.fullcancellation.OrderStatusResponse;
import com.coxandkings.travel.operations.resource.fullcancellation.OrderStatusUpdateResource;
import com.coxandkings.travel.operations.service.fullcancellation.FullCancellationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@CrossOrigin("*")
@RestController
@RequestMapping("fullCancellations")
public class FullCancellationController {

    @Autowired
    private FullCancellationService fullCancellationService;

    @PutMapping("/v1/updateOrderStatus")
    public ResponseEntity updateOrderStatus(@RequestBody OrderStatusUpdateResource orderStatusUpdateResource) throws ParseException, OperationException {

        OrderStatusResponse orderStatusResponse = fullCancellationService.updateOrderStatus(orderStatusUpdateResource);
        return new ResponseEntity(orderStatusResponse, HttpStatus.OK);

    }

    /**
     * Actually this is put method but for non extranet supplier after click on link it will open browser.
     * So we need to use this method as get so we will get request
     *
     * @param supplierUniqueRefId
     * @return
     * @throws ParseException
     * @throws OperationException
     */
    @GetMapping("/v1/updateOrderStatusNotExternetSupplier/{supplierUniqueRefId}")
    public ResponseEntity updateOrderStatusNotExternetSupplier(@PathVariable("supplierUniqueRefId") String supplierUniqueRefId) throws ParseException, OperationException {


        OrderStatusResponse orderStatusResponse = fullCancellationService.updateOrderStatus(supplierUniqueRefId);
        return new ResponseEntity(orderStatusResponse, HttpStatus.OK);

    }

    @GetMapping("/v1/updateInventoryStatus/{supplierUniqueRefId}")
    public ResponseEntity updateInventoryStatus(@PathVariable("supplierUniqueRefId") String supplierUniqueRefId) throws ParseException, OperationException {

        return new ResponseEntity(fullCancellationService.updateInventoryStatus(supplierUniqueRefId), HttpStatus.OK);


    }


}
