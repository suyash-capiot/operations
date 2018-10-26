package com.coxandkings.travel.operations.service.remarks.impl;

import com.coxandkings.travel.operations.enums.remarks.RemarksTo;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.remarks.BookingRemarks;
import com.coxandkings.travel.operations.repository.remarks.BookingRemarksRepository;
import com.coxandkings.travel.operations.resource.notification.NotificationResource;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.remarks.BookingRemarksService;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class BookingRemarksServiceImpl implements BookingRemarksService {
    private static final Logger logger = LogManager.getLogger(BookingRemarksServiceImpl.class);
    @Autowired
    AlertService alertService;
    @Value(value = "${new-supplier-first-booking.alertConfig.businessProcess}")
    private String businessProcess;
    @Value(value = "${new-supplier-first-booking.alertConfig.function}")
    private String function;
    @Value(value = "${new-supplier-first-booking.alertConfig.alertName}")
    private String alertName;
    @Autowired
    BookingRemarksRepository bookingRemarksRepository;
    @Autowired
    private OpsBookingService opsBookingService;

    @Override
    public BookingRemarks saveBookingRemarks(BookingRemarks bookingRemarks) {

        BookingRemarks remarks = null;
//        BookingRemarks bookingRemarksDetails = bookingRemarksRepository.findBookingRemarkByBookId(bookingRemarks);
        if (StringUtils.isEmpty(bookingRemarks.getRemarkId()))
        {
            remarks = new BookingRemarks();
            remarks = bookingRemarksRepository.saveOrUpdateBookingRemarks(bookingRemarks);
        }
       /* else {
            bookingRemarks = updateBookingRemarks(bookingRemarksDetails, bookingRemarks);
            remarks = bookingRemarksRepository.saveOrUpdateBookingRemarks(bookingRemarks);
        }*/
        return getRemark(remarks);
    }

    private BookingRemarks getRemark(BookingRemarks remarks) {
        if (remarks != null && remarks.getRemarksCategory().getCategoryType().equalsIgnoreCase("first_reservation_check")) {
            OpsBooking opsBooking = null;
            try {
                opsBooking = opsBookingService.getBooking(remarks.getBookId());
            } catch (OperationException e) {
                e.printStackTrace();
            }
            String message = "Note Assigned : " + remarks.getDetails();
            NotificationResource notificationResource = null;
            try {
                notificationResource = alertService.createAlert(businessProcess, function, opsBooking.getCompanyId(), alertName, remarks.getUserId(), message);
            } catch (OperationException e) {
                logger.error("Error while sending alerts to whome you are assigned" + e);
            }

        }
        //checking bookingRemarks for customer or supplier
        else if (remarks.getRemarksTo().getToType().equalsIgnoreCase(String.valueOf(RemarksTo.SUPPLIER))) {
            // Todo for supplier send an email
        }
        return remarks;

    }

    @Override
    public BookingRemarks updateBookingRemarks(BookingRemarks bookingRemarks) throws OperationException {
        if (bookingRemarks.getRemarkId() == null) {
            throw new OperationException("Remark id cannot be null or empty");
        }
        BookingRemarks remarks = bookingRemarksRepository.saveOrUpdateBookingRemarks(bookingRemarks);
        return getRemark(remarks);
    }

    @Override
    public List<BookingRemarks> getRemarksByBookId(String bookId) throws OperationException {
        List<BookingRemarks> resList = bookingRemarksRepository.getRemarksByBookId(bookId);
        if(resList==null)
            throw new OperationException("no results found");

        return resList;
    }

    @Override
    public List<BookingRemarks> getAllBookingRemarks() {
        return bookingRemarksRepository.getAllBookingRemarks();
    }

    @Override
    public BookingRemarks getBookingRemarksById(String remarkId) {
        return bookingRemarksRepository.getBookingRemarksById(remarkId);
    }

    @Transactional
    @Override
    public void deleteByRemarkId(String remarkId) {
        bookingRemarksRepository.deleteByRemarkId(remarkId);
    }


    @Override
    public BookingRemarks updateBookingRemarks(BookingRemarks bookingRemarks, BookingRemarks bookingRemark) {

        bookingRemarks.setBookId(bookingRemark.getBookId());

        if (bookingRemark.getCurrentUser() != null) {
            bookingRemarks.setCurrentUser(bookingRemark.getCurrentUser());
        }
        if (bookingRemark.getDetails() != null) {
            bookingRemarks.setDetails(bookingRemark.getDetails());
        }
        if (bookingRemark.getRemarksCategory() != null) {
            bookingRemarks.setRemarksCategory(bookingRemark.getRemarksCategory());
        }
        if (bookingRemark.getUserId() != null) {
            bookingRemarks.setUserId(bookingRemark.getUserId());
        }
        if (bookingRemark.getUserId() != null) {
            bookingRemarks.setUserId(bookingRemark.getUserId());
        }
        if (bookingRemark.getRemarksTo() != null) {
            bookingRemarks.setRemarksTo(bookingRemark.getRemarksTo());
        }
        if (bookingRemark.getUserRole() != null) {
            bookingRemarks.setUserRole(bookingRemark.getUserRole());
        }

        return bookingRemarks;

    }
}
