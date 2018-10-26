package com.coxandkings.travel.operations.service.booking;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.booking.BookingHistoryItem;

import java.util.List;

public interface BookingHistoryAccoService {
    List<BookingHistoryItem> getAccoHistory(String bookID, List<String> paxIDs) throws OperationException;
}
