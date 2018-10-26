package com.coxandkings.travel.operations.resource.managebookingstatus;

import com.coxandkings.travel.operations.model.core.OpsOrderStatus;

public class UpdatePAXStatusResource {

	private String passengerId;
    private OpsOrderStatus status;
    private String orderId;
    private String bookingId;
    
	
	public OpsOrderStatus getStatus() {
		return status;
	}
	public void setStatus(OpsOrderStatus status) {
		this.status = status;
	}
	public String getPassengerId() {
		return passengerId;
	}
	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getBookingId() {
		return bookingId;
	}
	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}
    
}
