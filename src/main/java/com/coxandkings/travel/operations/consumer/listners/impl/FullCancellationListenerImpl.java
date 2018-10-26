package com.coxandkings.travel.operations.consumer.listners.impl;

import com.coxandkings.travel.operations.consumer.listners.FullCancellationListener;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.service.fullcancellation.FullCancellationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FullCancellationListenerImpl implements FullCancellationListener {

    @Autowired
    private FullCancellationService fullCancellationService;

    @Async
    @Override
    public void processBooking(KafkaBookingMessage aBooking) throws JsonProcessingException, OperationException {
        fullCancellationService.process(aBooking);
    }
}
