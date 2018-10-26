package com.coxandkings.travel.operations.service.fullcancellation;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.resource.fullcancellation.OrderStatusResponse;
import com.coxandkings.travel.operations.resource.fullcancellation.OrderStatusUpdateResource;

import java.text.ParseException;

public interface FullCancellationService {
    void process(KafkaBookingMessage KafkaBookingMessage) throws OperationException;

    OrderStatusResponse updateOrderStatus(OrderStatusUpdateResource orderStatusUpdateResource) throws ParseException, OperationException;

    OrderStatusResponse updateOrderStatus(String supplierUniqueRefId) throws ParseException, OperationException;

    OrderStatusResponse updateInventoryStatus(String supplierUniqueRefId) throws ParseException, OperationException;
}
