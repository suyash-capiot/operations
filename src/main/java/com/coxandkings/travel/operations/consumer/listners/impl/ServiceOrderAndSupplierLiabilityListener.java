package com.coxandkings.travel.operations.consumer.listners.impl;


import com.coxandkings.travel.operations.consumer.listners.BookingListenerType;
import com.coxandkings.travel.operations.consumer.listners.ServiceOrderListener;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.ServiceOrderAndSupplierLiabilityService;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;


@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ServiceOrderAndSupplierLiabilityListener implements ServiceOrderListener {

    @Autowired
    private ServiceOrderAndSupplierLiabilityService serviceOrderAndSupplierLiabilityService;

    private static final Logger logger = LogManager.getLogger(ServiceOrderAndSupplierLiabilityListener.class);

    @Async
    @Override
    public void processBooking(OpsBooking opsBooking, KafkaBookingMessage message) throws IllegalAccessException, ParseException, IOException, OperationException, InvocationTargetException, JSONException {

        logger.info("*****Start Processing Kafka message for ServiceOrderAndLiability for a Booking *****");
        if (opsBooking != null && opsBooking.getProducts() != null && opsBooking.getProducts().size() >= 1) {
            serviceOrderAndSupplierLiabilityService.processBooking(opsBooking, message);
            logger.info("**** Processed Kafka message for generation of Service Order and Supplier Liabilities ****");
        } else
            logger.info("Cannot process generation of Service Order and Supplier Liabilities as there are no booking details");

    }

    @Override
    public BookingListenerType getListenerType() {
        return BookingListenerType.SERVICE_ORDER_AND_SUPPLIER_LIABILITY;
    }

    @Lookup
    public ServiceOrderAndSupplierLiabilityListener getServiceOrderAndLiabilityListener() {
        return null;
    }


}
