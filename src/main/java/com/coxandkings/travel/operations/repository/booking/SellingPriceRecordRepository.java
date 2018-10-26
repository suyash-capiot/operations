package com.coxandkings.travel.operations.repository.booking;

import com.coxandkings.travel.operations.model.booking.NewSellingPriceRecord;

public interface SellingPriceRecordRepository {
    public NewSellingPriceRecord saveOrUpdateNewSellingPriceDetails(NewSellingPriceRecord sellingPriceRecord);
    public NewSellingPriceRecord getNewSellingPriceDetails(String bookingRefNo, String productId);
    public String deleteNewSellingPriceDetails(String bookingRefNo, String productId);
}
