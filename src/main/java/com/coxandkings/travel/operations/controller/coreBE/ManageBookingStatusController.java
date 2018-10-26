package com.coxandkings.travel.operations.controller.coreBE;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.MessageResource;
import com.coxandkings.travel.operations.resource.managebookingstatus.UpdateBookingStatusResource;
import com.coxandkings.travel.operations.resource.managebookingstatus.UpdateOrderStatusResource;
import com.coxandkings.travel.operations.resource.managebookingstatus.UpdateRoomStatusResource;
import com.coxandkings.travel.operations.service.booking.ManageBookingStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/bookingStatus")
public class ManageBookingStatusController {

    @Autowired
    ManageBookingStatusService manageBookingStatusService;

    @GetMapping("/v1/booking/{bookingRefId}")
    public ResponseEntity<StatusUI> getBookingStatus(@PathVariable String bookingRefId) throws OperationException {
        StatusUI bookingStatus = manageBookingStatusService.getBookingStatus(bookingRefId);
        return new ResponseEntity<StatusUI>(bookingStatus, HttpStatus.OK);

    }

    @GetMapping("/v1/booking")
    public ResponseEntity<List<StatusUI>> getAllBookingStatus() throws OperationException {
        List<StatusUI> bookingStatuses = manageBookingStatusService.getBookingStatus();
        return new ResponseEntity<List<StatusUI>>(bookingStatuses, HttpStatus.OK);

    }

    @PostMapping("/v1/booking")
    public ResponseEntity<MessageResource> updateBookingStatus(
            @RequestBody UpdateBookingStatusResource updateBookingStatusResource) throws OperationException {
        MessageResource message = new MessageResource();
        manageBookingStatusService.updateBookingStatus(updateBookingStatusResource);
        message.setMessage(String.format("Status for Booking <%s> updated Successfully",
                updateBookingStatusResource.getBookingRefId()));
        return new ResponseEntity<MessageResource>(message, HttpStatus.OK);
    }

    @GetMapping("/v1/booking/order")
    public ResponseEntity<StatusUI> getOrderStatus(@RequestParam(value = "bookingRefId") String bookingRefId,
                                                   @RequestParam(value = "orderId") String orderId) throws ParseException, OperationException {
        StatusUI orderStatus = manageBookingStatusService.getOrderStatus(bookingRefId, orderId);
        return new ResponseEntity<StatusUI>(orderStatus, HttpStatus.OK);

    }

    @PostMapping("/v1/booking/order")
    public ResponseEntity<MessageResource> updateOrderStatus(
            @RequestBody UpdateOrderStatusResource updateOrderStatusResource) throws OperationException {
        MessageResource message = new MessageResource();
        manageBookingStatusService.updateOrderStatus(updateOrderStatusResource);
        message.setMessage(
                String.format("Status for Order <%s> updated Successfully", updateOrderStatusResource.getOrderId()));
        return new ResponseEntity<MessageResource>(message, HttpStatus.OK);
    }

    @GetMapping("/v1/order")
    public ResponseEntity<List<StatusUI>> getAllOrderStatus() throws OperationException {
        List<StatusUI> orderStatuses = manageBookingStatusService.getOrderStatus();
        return new ResponseEntity<List<StatusUI>>(orderStatuses, HttpStatus.OK);

    }

    @PostMapping("/v1/room")
    public ResponseEntity<MessageResource> updateRoomStatus(
            @RequestBody UpdateRoomStatusResource updateRoomStatusResource) throws OperationException {
        MessageResource message = new MessageResource();
        manageBookingStatusService.updateRoomStatus(updateRoomStatusResource);
        message.setMessage(
                String.format("Status for Room <%s> updated Successfully", updateRoomStatusResource.getRoomID()));
        return new ResponseEntity<MessageResource>(message, HttpStatus.OK);
    }
}
