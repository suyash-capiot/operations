package com.coxandkings.travel.operations.repository.remarks;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.remarks.BookingRemarks;

import java.util.List;

public interface BookingRemarksRepository {

    public BookingRemarks saveOrUpdateBookingRemarks(BookingRemarks bookingRemarks);

    public List<BookingRemarks> getAllBookingRemarks();

    public BookingRemarks getBookingRemarksById(String remarkId);

    public void deleteByRemarkId(String remarkId);

    BookingRemarks findBookingRemarkByBookId(BookingRemarks bookingRemarks);

    List<BookingRemarks> getRemarksByBookId(String bookId)throws OperationException;
}
