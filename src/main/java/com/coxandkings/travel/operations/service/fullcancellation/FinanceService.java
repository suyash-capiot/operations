package com.coxandkings.travel.operations.service.fullcancellation;


import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.resource.fullcancellation.OrderCancellation;

public interface FinanceService {

    com.coxandkings.travel.ext.model.finance.cancelinvoice.InvoiceDto getInvoicesByBookingDetails(KafkaBookingMessage kafkaBookingMessage) throws OperationException;
    void updateInvoice(OrderCancellation cancellations,KafkaBookingMessage kafkaBookingMessage) throws OperationException;
}
