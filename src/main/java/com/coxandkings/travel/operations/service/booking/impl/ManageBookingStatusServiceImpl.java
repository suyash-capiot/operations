package com.coxandkings.travel.operations.service.booking.impl;

import com.coxandkings.travel.operations.controller.coreBE.StatusUI;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsBookingStatus;
import com.coxandkings.travel.operations.model.core.OpsOrderStatus;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.producer.service.WorkUnitDispatcher;
import com.coxandkings.travel.operations.resource.booking.UpdateFlightDetailResource;
import com.coxandkings.travel.operations.resource.managebookingstatus.*;
import com.coxandkings.travel.operations.service.booking.ManageBookingStatusService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.bookingHistory.BookingHistoryService;
import com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.ProvisionalServiceOrderService;
import com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.ServiceOrderAndSupplierLiabilityService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.coxandkings.travel.operations.utils.RESTErrorHandler;
import com.coxandkings.travel.operations.utils.RestUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ManageBookingStatusServiceImpl implements ManageBookingStatusService {

	@Autowired
	private OpsBookingService opsBookingService;

	@Autowired
	private UserService userService;
	
	@Autowired
	@Qualifier("bookingHistory")
	private BookingHistoryService bookingHistoryService;

	@Autowired
	private ServiceOrderAndSupplierLiabilityService serviceOrderAndSupplierLiabilityService;

	@Autowired
	private ProvisionalServiceOrderService provisionalServiceOrderService;

	@Value("${booking_engine.update.air.booking_status}")
	private String airUpdateOrderStatusUrl;

	@Value("${booking_engine.update.acco.booking_status}")
	private String accoUpdateOrderStatusUrl;

	@Value("${booking_engine.update.booking_status}")
	private String updateBookingStatusUrl;

	@Value("${booking_engine.update.acco.room_status}")
	private String accoUpdateRoomStatusUrl;

	@Value("${booking_engine.update.air.confirmation_details}")
	private String airUpdateConfirmationDetailsUrl;

	@Value("${kafka.producer.booking-notifications.topic}")
	private String bookingNotificationsTopic;

	@Autowired
	private WorkUnitDispatcher kafkaProducer;

	@Autowired
	private MDMRestUtils mdmRestUtils;

	@Override
	public StatusUI getBookingStatus(String bookingRefId) throws OperationException {
		OpsBooking booking=opsBookingService.getBooking(bookingRefId);
		if (booking == null) {
			throw new OperationException(Constants.DB_BOOKING_GET_ERROR);
		}
		OpsBookingStatus opsBookingStatus=booking.getStatus();
		StatusUI bookingStatus=new StatusUI();
		bookingStatus.setLabel(opsBookingStatus.getBookingStatus());
		bookingStatus.setValue(opsBookingStatus.name());
		return bookingStatus;
	}

	@Override
	public List<StatusUI> getBookingStatus() {
		List<StatusUI> bookingStatuses=new ArrayList<>();
		for(OpsBookingStatus opsBookingStatus:OpsBookingStatus.values()) {
			StatusUI bookingStatus=new StatusUI();
			bookingStatus.setLabel(opsBookingStatus.getBookingStatus());
			bookingStatus.setValue(opsBookingStatus.name());
			bookingStatuses.add(bookingStatus);
		}
		return bookingStatuses;
	}

	@Override
	public List<StatusUI> getOrderStatus() {
		List<StatusUI> orderStatuses= new ArrayList<StatusUI>();
		for(OpsOrderStatus opsOrderStatus:OpsOrderStatus.values()) {
			StatusUI orderStatus=new StatusUI();
			orderStatus.setLabel(opsOrderStatus.getProductStatus());
			orderStatus.setValue(opsOrderStatus.name());
			orderStatuses.add(orderStatus);
		}
		return orderStatuses;
	}

	@Override
	public StatusUI getOrderStatus(String bookingRefId, String orderId) throws OperationException {
		OpsBooking booking = opsBookingService.getBooking(bookingRefId);
		if (booking == null) {
			throw new OperationException(Constants.DB_BOOKING_GET_ERROR);
		}
		List<OpsProduct> opsProducts = booking.getProducts();
		for (OpsProduct opsProduct : opsProducts) {
			if (opsProduct.getOrderID().equals(orderId)) {
				StatusUI orderStatus=new StatusUI();
				OpsOrderStatus opsOrderStatus=opsProduct.getOrderDetails().getOpsOrderStatus();
				orderStatus.setLabel(opsOrderStatus.getProductStatus());
				orderStatus.setValue(opsOrderStatus.name());
				return orderStatus;
			}

		}
		throw new OperationException(Constants.ORDER_NOT_FOUND, orderId, bookingRefId);
	}



	@Override
	public void updateBookingStatus(String bookingRefId, OpsBookingStatus bookingStatus) throws OperationException {
		BookingStatusResource bookingStatusResource = new BookingStatusResource();
		String userID = userService.getLoggedInUserId();

		bookingStatusResource.setUserID(userID);
		bookingStatusResource.setBookID(bookingRefId);;
		bookingStatusResource.setStatus(bookingStatus.getBookingStatus());
		String url = updateBookingStatusUrl;
		try {
			RestUtils.put(url, bookingStatusResource, String.class);
		} catch (Exception e) {
			throw new OperationException(Constants.OPS_ERR_11253);
		}


	}


	@Override
	public void updateOrderStatus(String bookingRefId,String orderId, OpsProductSubCategory opsProductSubCategory,
								  OpsOrderStatus orderStatus) throws OperationException {
		OrderStatusResource orderStatusResource = new OrderStatusResource();
		String userID = userService.getLoggedInUserId();
		
		OpsBooking opsBooking=opsBookingService.getBooking(bookingRefId);
		if (userID == null) {
			userID = userService.getSystemUserIdFromMDMToken();
		}

		orderStatusResource.setUserID(userID);
		orderStatusResource.setOrderID(orderId);
		orderStatusResource.setStatus(orderStatus.getProductStatus());
		String url = null;
		switch (opsProductSubCategory) {
			case PRODUCT_SUB_CATEGORY_BUS:
				break;
			case PRODUCT_SUB_CATEGORY_FLIGHT:
				url = airUpdateOrderStatusUrl;
				break;
			case PRODUCT_SUB_CATEGORY_HOTELS:
				url = accoUpdateOrderStatusUrl;
				break;
			case PRODUCT_SUB_CATEGORY_INDIAN_RAIL:
				break;
			case PRODUCT_SUB_CATEGORY_RAIL:
				break;
			default:
				break;
		}
		try {

			RestUtils.put(url, orderStatusResource, String.class);

		} catch (Exception e) {
			throw new OperationException(Constants.OPS_ERR_11255);
		}
		
		KafkaBookingMessage kafkaBookMsg= new KafkaBookingMessage();
		kafkaBookMsg.setActionType("MODIFY");
		kafkaBookMsg.setBookId(bookingRefId);
		kafkaBookMsg.setOrderNo(orderId);
		kafkaBookMsg.setOperation("Change Booking Status");
		kafkaBookMsg.setStatus(orderStatus.getProductStatus());
		kafkaBookMsg.setTimestamp(ZonedDateTime.now( ZoneOffset.UTC ).toString());
		bookingHistoryService.captureDetailsForBookingHistory(kafkaBookMsg, opsBooking);

		switch (orderStatus) {
			case OK:
				// checking if all orders in booking are confirmed
				if (bookingConfirmed(bookingRefId)) {
					try {
						putKafkaNotification(bookingRefId);
					} catch (Exception e) {
						throw new OperationException(Constants.OPS_ERR_11258);
					}
				} else {
					generateNewServiceOrder(bookingRefId, orderId);
				}
				break;
			case RAMD:
				break;
			case REJ:
				break;
			case RQ:
				generateNewServiceOrder(bookingRefId, orderId);
				break;
			case RXL:
				break;
			case TKD:
				generateNewServiceOrder(bookingRefId, orderId);
				break;
			case VCH:
				generateNewServiceOrder(bookingRefId, orderId);
				break;
			case WL:
				break;
			case XL:
				break;
			default:
				break;

		}
		
		

	}

	private void putKafkaNotification(String bookingRefId) throws Exception {
		JSONObject msgJson=new JSONObject();
		msgJson.put("BookID", bookingRefId);
		msgJson.put("type", "NEW");
		msgJson.put("Operation", "book");

		kafkaProducer.dispatch(bookingNotificationsTopic, msgJson.toString());

	}

	private boolean bookingConfirmed(String bookingRefId) throws OperationException {
		boolean confirmedBooking=true;
		OpsBooking opsBooking=opsBookingService.getBooking(bookingRefId);
		for(OpsProduct opsProduct:opsBooking.getProducts()) {
			if(!opsProduct.getOrderDetails().getOpsOrderStatus().equals(OpsOrderStatus.OK)) {
				confirmedBooking=false;
			}
		}
		return confirmedBooking;
	}



	@Override
	public void updateBookingStatus(UpdateBookingStatusResource updateBookingStatusResource) throws OperationException {
		updateBookingStatus(updateBookingStatusResource.getBookingRefId(), updateBookingStatusResource.getBookingStatus());
	}

	@Override
	public void updateOrderStatus(UpdateOrderStatusResource updateOrderStatusResource) throws OperationException {
		updateOrderStatus(updateOrderStatusResource.getBookingId(),updateOrderStatusResource.getOrderId(),
				updateOrderStatusResource.getProductSubCategory(), updateOrderStatusResource.getOrderStatus());
	}

	@Override
	public void updateRoomStatus(UpdateRoomStatusResource updateRoomStatus) throws OperationException {
		updateRoomStatus(updateRoomStatus.getRoomID(), updateRoomStatus.getStatus());
	}

	@Override
	public void updateRoomStatus(String roomID, OpsOrderStatus orderStatus) throws OperationException {
		RoomStatusResource roomStatusResource = new RoomStatusResource();
		String userID = userService.getLoggedInUserId();
		roomStatusResource.setRoomID(roomID);
		roomStatusResource.setStatus(orderStatus.getProductStatus());
		roomStatusResource.setUserID(userID);
		try {
			RestUtils.put(accoUpdateRoomStatusUrl, roomStatusResource, String.class);
		} catch (Exception e) {
			throw new OperationException(Constants.OPS_ERR_11257);
		}
	}



	private void generateNewServiceOrder(String bookingRefId, String orderId) throws OperationException {
		OpsBooking opsBooking = opsBookingService.getBooking(bookingRefId);
		OpsProduct opsProduct = opsBookingService.getOpsProduct(opsBooking,orderId);
		try {
			serviceOrderAndSupplierLiabilityService.generateServiceOrder(opsProduct,opsBooking,false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updatePAXStatus(UpdatePAXStatusResource updatePAXStatus) throws OperationException {
//		updatePAXStatus(updatePAXStatus.getBookingId(),updatePAXStatus.getOrderId(),updatePAXStatus.getPassengerId(),updatePAXStatus.getStatus());

	}
/*
	@Override
	public void updatePAXStatus(String bookingId, String orderId, String passengerId, OpsOrderStatus orderStatus)
			throws OperationException {
		PAXStatusResource updatePaxStatusResource=new PAXStatusResource();
		String userID = userService.getLoggedInUserId();

		updatePaxStatusResource.setUserID(userID);
		updatePaxStatusResource.setPassengerID(passengerId);
		updatePaxStatusResource.setStatus(orderStatus.getProductStatus());

		try {
            RestUtils.put(airUpdateConfirmationDetailsUrl, updatePaxStatusResource, String.class);
        } catch (Exception e) {
        	//TODO
            throw new OperationException(Constants.OPS_ERR_11261);
        }

	}*/

	@Override
	public String updateConfirmationDetails(UpdateFlightDetailResource flightDetailResource) throws OperationException {
		URI uri = UriComponentsBuilder.fromUriString(airUpdateConfirmationDetailsUrl).build().encode().toUri();
		RestTemplate restTemplate = RestUtils.getTemplate();
		restTemplate.setErrorHandler(new RESTErrorHandler());
		HttpEntity httpEntity = new HttpEntity(flightDetailResource);
		ResponseEntity<String> responseEntity = restTemplate.exchange(uri.toString(),HttpMethod.PUT,httpEntity,String.class);
		String responseInString=responseEntity.getBody();
		return responseInString;
	}


}
