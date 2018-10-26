package com.coxandkings.travel.operations.repository.sellingPrice;

import com.coxandkings.travel.operations.model.sellingPrice.Discount;

import java.util.List;

public interface DiscountRepository {
    Discount createDiscount(Discount discount);
    void deleteDiscount(String id);
    Discount getDiscount(String id);
    List<Discount> getAllDiscounts(String bookingRefId, String productId);
}
