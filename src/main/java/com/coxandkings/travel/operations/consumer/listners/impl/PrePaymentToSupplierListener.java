package com.coxandkings.travel.operations.consumer.listners.impl;

import com.coxandkings.travel.operations.consumer.listners.BookingListenerType;
import com.coxandkings.travel.operations.consumer.listners.IBookingListener;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.paymentadvice.AutomaticPaymentAdviceGenerationService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;

@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PrePaymentToSupplierListener implements IBookingListener{

    @Autowired
    AutomaticPaymentAdviceGenerationService automaticPaymentAdviceGenerationService;

    @Override
    public BookingListenerType getListenerType() {
        return BookingListenerType.PRE_PAYMENT_TO_SUPPLIER;
    }

    @Async
    @Override
    public void processBooking(OpsBooking aBooking) throws IOException, OperationException, JSONException {
//        PaymentAdvice paymentAdvice = automaticPaymentAdviceGenerationService.generatePaymentAdviceAutomatically(aBooking);
    }
}
