package com.coxandkings.travel.operations.service.retrieveorderstatusservice;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;

public interface RetrieveOrderStatusService {
    void updateOrderStatusFromPnr(KafkaBookingMessage kafkaBookingMessage) throws OperationException;
}
