package com.coxandkings.travel.operations.controller.productbookedthrother;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.productbookedthrother.Attribute;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.resource.productbookedthrother.ProductBookedThrOtherResource;
import com.coxandkings.travel.operations.service.productbookedthrother.ProductBookedThrOtherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping("/products/bookedThroughOtherSevices")
@CrossOrigin(origins = "*")
public class ProductBookedThrOtherController {

    @Autowired
    ProductBookedThrOtherService productBookedThrOtherService;


    @GetMapping(value = "/v1/getProducts/bookings/{bookingRefId}/orders/{orderId}/productSubType/{productCategorySubTypeValue}")
    public ResponseEntity<ProductBookedThrOtherResource> getProduct(@PathVariable("bookingRefId") String bookingRefId,
                                                                    @PathVariable("orderId") String orderId,
                                                                    @PathVariable("productCategorySubTypeValue") String productCategorySubTypeValue) throws Exception {
            return new ResponseEntity<ProductBookedThrOtherResource>(productBookedThrOtherService.getProduct(bookingRefId, orderId, productCategorySubTypeValue), HttpStatus.OK);

    }



    @PostMapping(value = "/v1/save")
    public ResponseEntity<?> saveProduct(@RequestBody ProductBookedThrOtherResource productBookedThrOtherResource) throws Exception {
            return new ResponseEntity<Attribute>(productBookedThrOtherService.saveProduct(productBookedThrOtherResource), HttpStatus.CREATED);
    }

    @PutMapping(value = "/v1/update")
    public ResponseEntity<?> updateProduct(@RequestBody ProductBookedThrOtherResource productBookedThrOtherResource) throws OperationException {
            return new ResponseEntity<Attribute>(productBookedThrOtherService.updateProduct(productBookedThrOtherResource), HttpStatus.CREATED);
    }

    @PostMapping(value = "/v1/sendAnEmailToSupplier")
    public ResponseEntity<EmailResponse> sendAnEmailToSupplier(@RequestBody ProductBookedThrOtherResource productBookedThrOtherResource) throws Exception {
            return new ResponseEntity<EmailResponse>(productBookedThrOtherService.sendAnEmailToSupplier(productBookedThrOtherResource), HttpStatus.OK);
    }

    @GetMapping(value = "/v1/sendAnEmailToClientOrCustomer/bookings/{bookingRefId}/orders/{orderId}/productSubType/{productCategorySubTypeValue}")
    public ResponseEntity<EmailResponse> sendAnEmailToClientOrCustomer(@PathVariable("bookingRefId") String bookingRefId,
                                                                       @PathVariable("orderId") String orderId,
                                                                       @PathVariable("productCategorySubTypeValue") String productCategorySubTypeValue) throws ParseException, OperationException, IOException {
        return new ResponseEntity<EmailResponse>(productBookedThrOtherService.sendAnEmailToClientOrCustomer(bookingRefId, orderId, productCategorySubTypeValue), HttpStatus.OK);
    }

    @GetMapping(value = "/v1/productSubType/{productCategorySubTypeValue}/id/{id}")
    public ResponseEntity<Boolean> isDetailSent(@PathVariable("productCategorySubTypeValue") String productCategorySubTypeValue, @PathVariable("id") String id) {
        boolean flag = productBookedThrOtherService.isDetailSent(productCategorySubTypeValue, id);
        return new ResponseEntity<>(flag, HttpStatus.OK);

    }

    @PutMapping(value = "/v1/updatedByClient")
    public ResponseEntity<?> updateProductByClient(@RequestBody ProductBookedThrOtherResource productBookedThrOtherResource) throws OperationException {
        return new ResponseEntity<Attribute>(productBookedThrOtherService.updateProductByClient(productBookedThrOtherResource), HttpStatus.CREATED);
    }
}
