package com.coxandkings.travel.operations.service.booking;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.booking.BookingHistoryItem;

import java.util.List;

public interface BookingHistoryAirService {
    List<BookingHistoryItem> getAirHistory(String bookID) throws OperationException;
}
