package com.coxandkings.travel.operations.service.booking;

import com.coxandkings.travel.operations.controller.coreBE.StatusUI;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBookingStatus;
import com.coxandkings.travel.operations.model.core.OpsOrderStatus;
import com.coxandkings.travel.operations.resource.booking.UpdateFlightDetailResource;
import com.coxandkings.travel.operations.resource.managebookingstatus.UpdateBookingStatusResource;
import com.coxandkings.travel.operations.resource.managebookingstatus.UpdateOrderStatusResource;
import com.coxandkings.travel.operations.resource.managebookingstatus.UpdatePAXStatusResource;
import com.coxandkings.travel.operations.resource.managebookingstatus.UpdateRoomStatusResource;

import java.util.List;

public interface ManageBookingStatusService {

	StatusUI getBookingStatus(String bookingRefId) throws OperationException;
	List<StatusUI> getBookingStatus();
	List<StatusUI> getOrderStatus();

	StatusUI getOrderStatus(String bookingRefId, String orderId) throws OperationException;
	void updateBookingStatus(String bookingRefId, OpsBookingStatus bookingStatus) throws OperationException;
	void updateBookingStatus(UpdateBookingStatusResource updateBookingStatus)throws OperationException;
	void updateOrderStatus(UpdateOrderStatusResource updateOrderStatus)throws OperationException;
	void updateOrderStatus(String bookingRefId,String orderId,OpsProductSubCategory opsProductSubCategory, OpsOrderStatus orderStatus)throws OperationException;
	void updateRoomStatus(UpdateRoomStatusResource updateRoomStatus) throws OperationException;
	void updateRoomStatus(String roomID, OpsOrderStatus orderStatus) throws OperationException;
	void updatePAXStatus(UpdatePAXStatusResource updatePAXStatus) throws OperationException;
//	void updatePAXStatus(String bookingId, String orderId, String passengerId, OpsOrderStatus orderStatus) throws OperationException;

	String updateConfirmationDetails(UpdateFlightDetailResource flightDetailResource) throws OperationException;

}
