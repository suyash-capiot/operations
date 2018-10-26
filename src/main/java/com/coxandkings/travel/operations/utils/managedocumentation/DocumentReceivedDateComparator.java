package com.coxandkings.travel.operations.utils.managedocumentation;

import com.coxandkings.travel.operations.resource.managedocumentation.BookingDocumentDetailsResource;

import java.time.ZonedDateTime;
import java.util.Comparator;

public class DocumentReceivedDateComparator implements Comparator<BookingDocumentDetailsResource> {

    @Override
    public int compare(BookingDocumentDetailsResource o1, BookingDocumentDetailsResource o2) {
        return ZonedDateTime.parse(o2.getDocumentReceivedDate()).compareTo(ZonedDateTime.parse(o1.getDocumentReceivedDate()));
    }
}
