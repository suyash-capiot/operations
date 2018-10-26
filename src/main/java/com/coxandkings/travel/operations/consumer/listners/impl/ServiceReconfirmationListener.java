package com.coxandkings.travel.operations.consumer.listners.impl;


import com.coxandkings.travel.operations.consumer.listners.BookingListenerType;
import com.coxandkings.travel.operations.consumer.listners.ReconfirmationListener;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.service.reconfirmation.batchjob.ReconfirmationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;


@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ServiceReconfirmationListener implements ReconfirmationListener {

    @Autowired
    private ReconfirmationService reconfirmationService;

    @Async
    @Override
    public void processBooking(OpsBooking aBooking, KafkaBookingMessage kafkaBookingMessage) {
        // Calling Reconfirmation main process
        reconfirmationService.processBooking(aBooking, kafkaBookingMessage);

    }

    @Override
    public BookingListenerType getListenerType() {
        return BookingListenerType.RECONFIRMATION_FIRST_BOOKING;
    }


}
