package com.coxandkings.travel.operations.controller.coreBE;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.booking.BookingHistoryItem;
import com.coxandkings.travel.operations.service.bookingHistory.BookingHistoryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/bookingHistoryService")
public class BookingHistoryController {

    private static Logger logger = LogManager.getLogger(BookingHistoryController.class);

//    @Autowired
//    private BookingHistoryService bookingHistoryService;

    @Autowired
    private BookingHistoryService bookingHistoryService;


    @GetMapping("/v1/getBookingHistory")
    public ResponseEntity<List<BookingHistoryItem>> getOrderActionItems(@RequestParam String bookID) throws OperationException {

        List<BookingHistoryItem> bookingHistoryList = new ArrayList<>();

        try {
            bookingHistoryList =  bookingHistoryService.getBookingHistory(bookID);
        } catch (Exception e) {

        }

        return new ResponseEntity<>(bookingHistoryList, HttpStatus.OK);


       /* List<BookingHistoryItem> bookingHistoryList = new ArrayList<>();
        try {
            logger.info("-- BookingHistoryController : GET : /v1/getBookingHistory --");
            bookingHistoryList = bookingHistoryService.getBookingHistory(bookID);
            return new ResponseEntity<>(bookingHistoryList, HttpStatus.OK);
        } catch (OperationException e) {
            logger.info("Error occurred in geting Booking History", e);
            throw new OperationException(Constants.OPS_ERR_11208);
        }*/
    }
}
