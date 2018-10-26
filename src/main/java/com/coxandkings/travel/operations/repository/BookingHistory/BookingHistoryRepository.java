package com.coxandkings.travel.operations.repository.BookingHistory;

import com.coxandkings.travel.operations.model.bookingHistory.BookingHistory;

import java.util.List;

public interface BookingHistoryRepository {

    BookingHistory saveOrUpdate(BookingHistory bookingHistory);

    List<BookingHistory> getBookingHistory(String bookId);
}
