package com.coxandkings.travel.operations.service.fullcancellation;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.model.core.OpsBooking;

public interface Communication {


    boolean sendMailSupplier(String supplierEmail, KafkaBookingMessage kafkaBookingMessage, String supplierRefNumber) throws OperationException;

    boolean sendMailNotExternetSupplier(String supplierEmail, String emailLink, KafkaBookingMessage kafkaBookingMessage) throws OperationException;

    boolean sendMailClient(String clientEmail, OpsBooking booking,KafkaBookingMessage kafkaBookingMessage) throws OperationException;

    boolean sendMailInventorySupplier(String supplierEmail, String emailLink, KafkaBookingMessage kafkaBookingMessage) throws OperationException;

    void sendAlert(KafkaBookingMessage kafkaBookingMessage, String alert) throws OperationException;
}
