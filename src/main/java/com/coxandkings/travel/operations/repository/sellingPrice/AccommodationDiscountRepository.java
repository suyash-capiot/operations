package com.coxandkings.travel.operations.repository.sellingPrice;

import com.coxandkings.travel.operations.model.sellingPrice.AccommodationDiscount;

import java.util.List;

public interface AccommodationDiscountRepository {
    AccommodationDiscount createDiscount(AccommodationDiscount accommodationDiscount);
    List<AccommodationDiscount> getAllDiscounts(String bookingRefId, String orderId);
}
