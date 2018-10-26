package com.coxandkings.travel.operations.repository.changeSupplierPrice;

import com.coxandkings.travel.operations.model.booking.NewSellingPriceRecord;

public interface ChangeSupplierPriceRepo {

    public NewSellingPriceRecord add(NewSellingPriceRecord newSellingPriceRecord);
    public NewSellingPriceRecord update(NewSellingPriceRecord newSellingPriceRecord);
    public void delete(String bookingRefno);

}
