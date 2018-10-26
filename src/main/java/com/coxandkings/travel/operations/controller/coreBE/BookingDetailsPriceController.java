package com.coxandkings.travel.operations.controller.coreBE;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.pricedetails.PriceDetailsResource;
import com.coxandkings.travel.operations.service.booking.BookingPriceDetailsService;
import com.coxandkings.travel.operations.utils.Constants;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/bookingPrices")
public class BookingDetailsPriceController {

    private static Logger logger = LogManager.getLogger(BookingDetailsPriceController.class);

    @Autowired
    private BookingPriceDetailsService priceDetailsService;

    @GetMapping("/v1/{bookingRefId}/sellingPrice/{orderID}")
    public HttpEntity<PriceDetailsResource>  getSellingPriceDetails(@PathVariable String bookingRefId, @PathVariable String orderID ) throws OperationException {
        try {
            PriceDetailsResource sellingPriceDetailsMap = priceDetailsService.getTotalSellingPriceDetails(bookingRefId, orderID);
            return new ResponseEntity<PriceDetailsResource>(sellingPriceDetailsMap, HttpStatus.OK);
        } catch (OperationException e) {
            throw new OperationException(Constants.OPS_ERR_11221);
        }
    }

    @GetMapping("/v1/{bookingRefId}/supplierPrice/{orderID}")
    public HttpEntity<PriceDetailsResource> getSupplierPriceDetails(@PathVariable String bookingRefId, @PathVariable String orderID ) throws OperationException {
        try {
            PriceDetailsResource supplierPriceDetailsMap = priceDetailsService.getTotalSupplierPriceDetails(bookingRefId, orderID);
            return new ResponseEntity<PriceDetailsResource>(supplierPriceDetailsMap, HttpStatus.OK);
        } catch (OperationException e) {
            throw new OperationException(Constants.OPS_ERR_11222);
        }
    }
}
