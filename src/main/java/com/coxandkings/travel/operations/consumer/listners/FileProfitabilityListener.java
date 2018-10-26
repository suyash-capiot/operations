package com.coxandkings.travel.operations.consumer.listners;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface FileProfitabilityListener {
    public BookingListenerType getListenerType();

    public void processBooking(KafkaBookingMessage kafkaBookingMessage) throws JsonProcessingException, OperationException;
}
