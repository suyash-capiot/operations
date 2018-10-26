package com.coxandkings.travel.operations.consumer.listners.impl;

import com.coxandkings.travel.operations.consumer.listners.BookingListenerType;
import com.coxandkings.travel.operations.consumer.listners.RetrieveOrderStatusListener;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.service.retrieveorderstatusservice.RetrieveOrderStatusService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;

@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RetrieveOrderStatusListenerImpl implements RetrieveOrderStatusListener {

    @Autowired
    private RetrieveOrderStatusService retrieveOrderStatusService;

    @Override
    public BookingListenerType getListenerType() {
        return BookingListenerType.RETRIEVE_ORDER_STATUS;
    }

    @Override
    @Async
    public void processBooking(KafkaBookingMessage kafkaMessage) throws JsonProcessingException, OperationException {
        retrieveOrderStatusService.updateOrderStatusFromPnr(kafkaMessage);
    }
}
