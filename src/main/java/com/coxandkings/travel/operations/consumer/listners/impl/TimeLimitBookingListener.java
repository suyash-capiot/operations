package com.coxandkings.travel.operations.consumer.listners.impl;

import com.coxandkings.travel.operations.consumer.listners.BookingListenerType;
import com.coxandkings.travel.operations.consumer.listners.IBookingListener;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.service.timelimitbooking.TimeLimitBatchJobInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;

@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TimeLimitBookingListener implements IBookingListener {
    @Autowired
    private TimeLimitBatchJobInfoService timeLimitBatchJobInfoService;

    @Async
    @Override
    public void processBooking(OpsBooking aOpsBooking) {
        if (aOpsBooking.getBookID().contains("-TL-")) {
            timeLimitBatchJobInfoService.tlAboutToExpire(aOpsBooking);
            timeLimitBatchJobInfoService.cancelTLIfNotConverted(aOpsBooking);
        }
    }

    @Override
    public BookingListenerType getListenerType() {
        return BookingListenerType.TIME_LIMIT_EXPIRY_DATE;
    }
}
