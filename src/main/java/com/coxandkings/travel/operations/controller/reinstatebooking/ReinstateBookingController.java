package com.coxandkings.travel.operations.controller.reinstatebooking;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.ErrorResponseResource;
import com.coxandkings.travel.operations.service.reinstatebooking.ReinstateBookingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("reinstate")
@CrossOrigin(origins = "*")
public class ReinstateBookingController {
    private static Logger logger = LogManager.getLogger(ReinstateBookingController.class);

    @Autowired
    private ReinstateBookingService reinstateBookingService;

    @PutMapping(value = "/v1/booking/{bookingRefNo}/{orderID}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ErrorResponseResource reinstateBooking(@PathVariable("bookingRefNo") String bookingRefNo, @PathVariable("orderID") String orderID) throws OperationException {
        logger.info("-- Entering ReinstateBookingController.reinstateBooking");
        return reinstateBookingService.reinstateBooking(bookingRefNo, orderID);
    }
}
