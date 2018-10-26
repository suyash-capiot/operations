package com.coxandkings.travel.operations.consumer.listners.impl;


import com.coxandkings.travel.operations.consumer.listners.BookingListenerType;
import com.coxandkings.travel.operations.consumer.listners.IBookingListener;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.service.newsupplierfirstbooking.NewSupplierFirstBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;


@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NewSupplierFirstBookingListener implements IBookingListener {

    @Autowired
    NewSupplierFirstBookingService newSupplierFirstBookingService;

    @Async
    @Override
    public void processBooking(OpsBooking aBooking) {
            newSupplierFirstBookingService.firstReservationChecks(aBooking);
    }

    @Override
    public BookingListenerType getListenerType() {
        return BookingListenerType.NEW_SUPPLIER_FIRST_BOOKING;
    }


}
