package com.coxandkings.travel.operations.controller.sellingPrice;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.sellingPrice.Discount;
import com.coxandkings.travel.operations.model.sellingPrice.SellingPriceApprovalComponent;
import com.coxandkings.travel.operations.resource.sellingPrice.DiscountResource;
import com.coxandkings.travel.operations.resource.sellingPrice.ProductInformation;
import com.coxandkings.travel.operations.resource.sellingPrice.SellingPrice;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.sellingPrice.DiscountService;
import com.coxandkings.travel.operations.utils.Constants;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/sellingPriceDiscounts")
public class DiscountOnSellingPriceController {

    private static Logger logger = LogManager.getLogger(DiscountOnSellingPriceController.class);

    @Autowired
    DiscountService discountService;

    @Autowired
    private OpsBookingService opsBookingService;

    @PostMapping("/v1/estimateSellingPrice")
    public ResponseEntity<SellingPrice> estimateAccommodationHotelDiscount(@RequestBody DiscountResource accommodationDiscountResource) throws OperationException, IOException {
        try {
            SellingPrice sellingPrice = discountService.estimateRevisedSellingPrice(accommodationDiscountResource);
            return new ResponseEntity<>(sellingPrice, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30501);
        }
    }

    @PostMapping("/v1/originalSellingPrice")
    public ResponseEntity<SellingPrice> getAbstractSellingPrice(@RequestBody ProductInformation productInformation) throws OperationException, IOException {
        try {
            SellingPrice sellingPrice = null;
            sellingPrice = discountService.getOriginalSellingPrice(productInformation);
            return new ResponseEntity<>(sellingPrice, HttpStatus.OK);
        } catch (OperationException e) {
            throw new OperationException(Constants.OPS_ERR_30502);
        }
    }

    @PostMapping("/v1/")
    public ResponseEntity<Discount> saveAccommodationHotelDiscount(@RequestBody DiscountResource discountResource) throws OperationException, ParseException, InvocationTargetException, IllegalAccessException, IOException, JSONException {
        try {
            return new ResponseEntity<>(discountService.createDiscount(discountResource), HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30503);
        }
    }

    @PostMapping("/v1/approvalScreen")
    public ResponseEntity<SellingPriceApprovalComponent> getApprovalScreen(@RequestBody DiscountResource discountResource) throws IOException, OperationException {
        try {
            return new ResponseEntity<>(discountService.getApprovalScreenDetails(discountResource), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OperationException(Constants.OPS_ERR_30504);
        }
    }

    @GetMapping("/v1/")
    public ResponseEntity<List<? extends Discount>> getSellingPrices(@RequestParam(name = "bookingRefId") String bookingRefId,
                                                                     @RequestParam(name = "orderId") String productId,
                                                                     @RequestParam(name = "productCategory") String productCategory,
                                                                     @RequestParam(name = "productSubCategory") String productSubCategory) throws OperationException {
        try {
            List<? extends Discount> discounts = discountService.getAllRecords(bookingRefId, productId, productCategory, productSubCategory);
            return new ResponseEntity<>(discounts, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30505);
        }
    }

    @GetMapping("/v1/{id}")
    public ResponseEntity<Discount> getDiscount(@PathVariable(name = "id") String discountId) throws OperationException {
        try {
            Discount discount = discountService.getDiscountById(discountId);
            return new ResponseEntity<>(discount, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30506);
        }
    }

    @PostMapping("/v1/approve")
    public ResponseEntity<Discount> approveDiscount(@RequestBody DiscountResource discountResource) throws OperationException, ParseException, IllegalAccessException, InvocationTargetException, IOException, JSONException {

        Discount discount = discountService.approveDiscount(discountResource);
        return new ResponseEntity<>(discount, HttpStatus.OK);

    }

    @PostMapping("/v1/reject")
    public ResponseEntity<Discount> rejectDiscount(@RequestBody DiscountResource discountResource) throws OperationException, ParseException, IllegalAccessException, InvocationTargetException, IOException, JSONException {
        try {
            Discount discount = discountService.rejectDiscount(discountResource);
            return new ResponseEntity<>(discount, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30508);
        }
    }

    @PostMapping("/v1/sellingPriceComponents")
    public ResponseEntity<List<String>> sellingPriceComponents(@RequestBody ProductInformation productInformation) throws OperationException, IOException {
        try {
            List<String> sellingPriceComponents = discountService.sellingPriceComponents(productInformation);
            return new ResponseEntity<>(sellingPriceComponents, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30509);
        }
    }

}
