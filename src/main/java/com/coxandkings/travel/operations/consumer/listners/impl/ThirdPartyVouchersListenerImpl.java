package com.coxandkings.travel.operations.consumer.listners.impl;

import com.coxandkings.travel.operations.consumer.listners.BookingListenerType;
import com.coxandkings.travel.operations.consumer.listners.ThirdPartyVoucherListener;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.utils.thirdpartyvouchers.AssignVoucherCode;
import org.json.JSONException;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ThirdPartyVouchersListenerImpl implements ThirdPartyVoucherListener {

    @Autowired
    private AssignVoucherCode assignVoucherCode;

    @Override
    public BookingListenerType getListenerType() {
        return BookingListenerType.THIRD_PARTY_VOUCHERS;
    }

    @Async
    @Override
    public void processBooking(OpsBooking opsBooking, KafkaBookingMessage message) throws IOException, OperationException, ParseException, SchedulerException, JSONException, IllegalAccessException, InvocationTargetException {
        System.out.println("*****Start Flow for ThirdPartyVouchers*****");
        assignVoucherCode.processBooking(opsBooking);
    }

    @Lookup
    public ThirdPartyVouchersListenerImpl getThirdPartyVouchersListener() {
        return null;
    }
}
