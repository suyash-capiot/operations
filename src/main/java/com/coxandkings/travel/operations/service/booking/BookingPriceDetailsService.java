package com.coxandkings.travel.operations.service.booking;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.pricedetails.PriceDetailsResource;

public interface BookingPriceDetailsService {

    public PriceDetailsResource getTotalSellingPriceDetails(String bookID, String orderID ) throws OperationException;

    public PriceDetailsResource getTotalSupplierPriceDetails(String bookID, String orderID ) throws OperationException;
}
