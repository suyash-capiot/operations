package com.coxandkings.travel.operations.consumer.listners.impl;

import com.coxandkings.travel.operations.consumer.listners.BookingListenerType;
import com.coxandkings.travel.operations.consumer.listners.IBookingListener;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.service.merge.MergeService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.text.ParseException;



@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MergeBookingListener implements IBookingListener {

    @Autowired
    private MergeService mergeService;

    @Override
    public BookingListenerType getListenerType() {
        return BookingListenerType.MERGE_BOOKING;
    }

    @Override
    public void processBooking(OpsBooking aBooking) throws ParseException, OperationException, SchedulerException {
        System.out.println("MergeBooking ref is : "+aBooking.getBookID());
        mergeService.saveMerge(aBooking);
    }
}
