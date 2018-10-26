package com.coxandkings.travel.operations.consumer.listners;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import org.json.JSONException;
import org.quartz.SchedulerException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

public interface ServiceOrderListener {

    public BookingListenerType getListenerType();
    public void processBooking(OpsBooking opsBooking, KafkaBookingMessage message) throws IOException, OperationException, ParseException, SchedulerException, JSONException, IllegalAccessException, InvocationTargetException;

}
