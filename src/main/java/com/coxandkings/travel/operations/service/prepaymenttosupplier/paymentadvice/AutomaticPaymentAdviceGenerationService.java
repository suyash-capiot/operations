package com.coxandkings.travel.operations.service.prepaymenttosupplier.paymentadvice;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice.PaymentAdvice;
import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;

public interface AutomaticPaymentAdviceGenerationService {

//    public PaymentAdvice generatePaymentAdviceAutomatically(OpsBooking opsBooking) throws IOException, OperationException, JSONException;


    public PaymentAdvice generatePaymentAdvice() throws OperationException, ParseException, JSONException, IOException, InvocationTargetException, IllegalAccessException;

    public boolean isPaymentDueDateReached(String bookingId,String orderId,String supplierId) throws OperationException;

    public BigDecimal getAmountPayableForSupplier(BigDecimal netPayable, BigDecimal balanceAmt, BigDecimal percentage);

}
