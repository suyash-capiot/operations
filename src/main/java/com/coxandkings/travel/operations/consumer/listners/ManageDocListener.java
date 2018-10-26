package com.coxandkings.travel.operations.consumer.listners;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.model.core.OpsBooking;

import java.io.IOException;

public interface ManageDocListener {

    public BookingListenerType getListenerType();
    public void processBooking(OpsBooking opsBooking, KafkaBookingMessage message) throws OperationException, IOException;

}
