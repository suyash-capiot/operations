package com.coxandkings.travel.operations.service.bookingHistory;

import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.resource.booking.BookingHistoryItem;

import java.util.List;

public interface BookingHistoryService {

    void captureDetailsForBookingHistory(KafkaBookingMessage kafkaBookingMessage, OpsBooking opsBooking);

    List<BookingHistoryItem> getBookingHistory(String bookID);
}
