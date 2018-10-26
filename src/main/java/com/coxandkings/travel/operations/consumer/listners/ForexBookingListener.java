package com.coxandkings.travel.operations.consumer.listners;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface ForexBookingListener {
    BookingListenerType getListenerType();
    void processBooking(OpsBooking opsBooking, KafkaBookingMessage kafkaBookingMessage) throws JsonProcessingException, OperationException;

}
