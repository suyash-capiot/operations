package com.coxandkings.travel.operations.service.remarks;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.remarks.BookingRemarks;

import java.util.List;

public interface BookingRemarksService {

    public BookingRemarks saveBookingRemarks(BookingRemarks bookingRemarks) throws OperationException;
    public List<BookingRemarks> getAllBookingRemarks();
    public BookingRemarks getBookingRemarksById(String remarkId);
    public void deleteByRemarkId(String remarkId);
     BookingRemarks updateBookingRemarks(BookingRemarks bookingRemarks, BookingRemarks bookingRemark);
     BookingRemarks updateBookingRemarks(BookingRemarks bookingRemarks)  throws OperationException;
     List<BookingRemarks> getRemarksByBookId(String bookId)throws OperationException;
}
