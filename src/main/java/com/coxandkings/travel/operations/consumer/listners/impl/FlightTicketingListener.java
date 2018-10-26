package com.coxandkings.travel.operations.consumer.listners.impl;

import com.coxandkings.travel.operations.consumer.listners.BookingListenerType;
import com.coxandkings.travel.operations.consumer.listners.IBookingListener;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;

import java.text.ParseException;

@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FlightTicketingListener implements IBookingListener {

	@Override
	public BookingListenerType getListenerType() {
		return BookingListenerType.FLIGHT_TICKETING;
	}
	
	@Async
	@Override
	public void processBooking(OpsBooking aBooking)
			throws JsonProcessingException, OperationException, ParseException, SchedulerException {
		// TODO Auto-generated method stub

	}

}
