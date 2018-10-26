package com.coxandkings.travel.operations.service.sellingPrice;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.sellingPrice.Discount;
import com.coxandkings.travel.operations.model.sellingPrice.SellingPriceApprovalComponent;
import com.coxandkings.travel.operations.resource.sellingPrice.DiscountResource;
import com.coxandkings.travel.operations.resource.sellingPrice.ProductInformation;
import com.coxandkings.travel.operations.resource.sellingPrice.SellingPrice;
import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;

public interface DiscountService {
    OpsBooking setApplicability(OpsBooking opsBooking);

    Discount createDiscount(DiscountResource discountResource) throws OperationException, ParseException, InvocationTargetException, IllegalAccessException, IOException, JSONException;

    Discount successApproval(Discount discount) throws ParseException, OperationException, IOException;

    List<? extends Discount> getAllRecords(String bookingRefId,
                                           String orderId,
                                           String productCategory,
                                           String productSubCategory) throws OperationException;

    SellingPrice getOriginalSellingPrice(ProductInformation productInformation) throws OperationException, IOException;

    SellingPrice estimateRevisedSellingPrice(DiscountResource discountResource) throws OperationException, IOException;

    Discount approveDiscount(DiscountResource discountResource) throws OperationException, ParseException, IllegalAccessException, InvocationTargetException, IOException, JSONException;

    Discount rejectDiscount(DiscountResource discountResource) throws OperationException, ParseException, IllegalAccessException, InvocationTargetException, IOException, JSONException;

    Discount getDiscountById(String discountId);

    SellingPriceApprovalComponent getApprovalScreenDetails(DiscountResource discountResource) throws OperationException, IOException;

    List<String> sellingPriceComponents(ProductInformation productInformation) throws OperationException, IOException;
}
