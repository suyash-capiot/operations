package com.coxandkings.travel.operations.service.bookingHistory.impl;

import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.model.bookingHistory.BookingHistory;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.repository.BookingHistory.BookingHistoryRepository;
import com.coxandkings.travel.operations.resource.booking.BookingHistoryItem;
import com.coxandkings.travel.operations.service.bookingHistory.BookingHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service("bookingHistory")
public class BookingHistoryServiceImpl implements BookingHistoryService {

    DecimalFormat df = new DecimalFormat(".#");

    @Autowired
    private BookingHistoryRepository bookingHistoryRepository;

    public void captureDetailsForBookingHistory(KafkaBookingMessage kafkaBookingMessage, OpsBooking opsBooking) {

        BookingHistory bookingHistory = new BookingHistory();

        //TODO: To set Short Description for the changes in Booking.
        bookingHistory.setBookId(kafkaBookingMessage.getBookId());
        bookingHistory.setOperation(kafkaBookingMessage.getOperation());
        bookingHistory.setErrorCode(kafkaBookingMessage.getErrorCode());
        bookingHistory.setErrorMessage(kafkaBookingMessage.getErrorMessage());

        if(kafkaBookingMessage.getStatus()!=null && !kafkaBookingMessage.getStatus().isEmpty())
            bookingHistory.setStatus(kafkaBookingMessage.getStatus());
        else if(opsBooking!=null)
            bookingHistory.setStatus(opsBooking.getStatus().getBookingStatus());

        if(kafkaBookingMessage.getOrderNo()!=null && !kafkaBookingMessage.getOrderNo().isEmpty()) {
            OpsProduct opsProduct = opsBooking.getProducts().stream().filter(s -> s.getOrderID().equalsIgnoreCase(kafkaBookingMessage.getOrderNo())).findFirst().get();
            bookingHistory.setProductCategory(opsProduct.getProductCategory());
            bookingHistory.setProductSubCategory(opsProduct.getProductSubCategory());
            bookingHistory.setOrderId(kafkaBookingMessage.getOrderNo());
        }else{
            if(opsBooking.getProducts().size() == 1){
                OpsProduct opsProduct = opsBooking.getProducts().get(0);
                bookingHistory.setProductCategory(opsProduct.getProductCategory());
                bookingHistory.setProductSubCategory(opsProduct.getProductSubCategory());
                bookingHistory.setOrderId(opsProduct.getOrderID());
            }
        }

        bookingHistory.setActionType(kafkaBookingMessage.getActionType());

        String pattern = "yyyy-MM-dd'T'HH:mm:ssXXX";
        //BE gives timestamp as String cannot rely on that as of now.
        ZonedDateTime timeStamp;
        try {
            DateTimeFormatter Parser = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault());
            timeStamp = ZonedDateTime.parse(kafkaBookingMessage.getTimestamp(), Parser);
        }catch (Exception e){
            timeStamp = ZonedDateTime.now();
        }
        bookingHistory.setTimeStamp(timeStamp);
        Float versionNo = 1.0f;
        List<BookingHistoryItem> currentBookingHistory = getBookingHistory(opsBooking.getBookID());
        if(currentBookingHistory!=null)
            for(BookingHistoryItem bookingHistoryItem : currentBookingHistory){
               if(versionNo < bookingHistoryItem.getVersionNo())
                   versionNo = bookingHistoryItem.getVersionNo();
            }
        //Initial Version : 1.1
        versionNo = versionNo + 0.1f;
        bookingHistory.setVersionNo(Float.valueOf(df.format(versionNo)));
        bookingHistoryRepository.saveOrUpdate(bookingHistory);
    }

    @Override
    public List<BookingHistoryItem> getBookingHistory(String bookID) {

        List<BookingHistory> bookingHistoryList = bookingHistoryRepository.getBookingHistory(bookID);
        List<BookingHistoryItem> bookingHistoryItems = new ArrayList<>();
        for(BookingHistory bookingHistory : bookingHistoryList){
            bookingHistoryItems.add(new BookingHistoryItem(bookingHistory));
        }
       return bookingHistoryItems;

    }


}
