package com.coxandkings.travel.operations.resource.managebookingstatus;

import com.coxandkings.travel.operations.model.core.OpsBookingStatus;

public class UpdateBookingStatusResource {
	
	private String bookingRefId;
	private OpsBookingStatus bookingStatus;
	
	public String getBookingRefId() {
		return bookingRefId;
	}
	public void setBookingRefId(String bookingRefId) {
		this.bookingRefId = bookingRefId;
	}
	public OpsBookingStatus getBookingStatus() {
		return bookingStatus;
	}
	public void setBookingStatus(OpsBookingStatus bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

}
