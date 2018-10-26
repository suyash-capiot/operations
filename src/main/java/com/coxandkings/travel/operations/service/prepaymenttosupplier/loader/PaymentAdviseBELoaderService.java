package com.coxandkings.travel.operations.service.prepaymenttosupplier.loader;


import com.coxandkings.travel.operations.exceptions.OperationException;

import java.math.BigDecimal;

public interface PaymentAdviseBELoaderService {

    public BigDecimal getNetPayableToSupplier(String bookingRefId, String orderId) throws OperationException;

    public String getSupplierCurrency(String bookingId, String orderId) throws OperationException;






}
