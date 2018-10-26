package com.coxandkings.travel.operations.service.prepaymenttosupplier.loader;

import com.coxandkings.travel.operations.exceptions.OperationException;

import java.io.IOException;
import java.math.BigDecimal;

public interface PaymentAdviceFinanceLoaderService  {

    public BigDecimal getBalanceAmtPayableToSupplier(String bookingRefId, String orderId, String supplierId, BigDecimal netPayableToSupplier) throws IOException, OperationException;
}
