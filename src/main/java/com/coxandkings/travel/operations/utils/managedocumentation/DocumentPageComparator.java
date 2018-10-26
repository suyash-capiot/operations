package com.coxandkings.travel.operations.utils.managedocumentation;

import com.coxandkings.travel.operations.resource.managedocumentation.BookingDocumentDetailsResource;

import java.util.Comparator;

public class DocumentPageComparator implements Comparator<BookingDocumentDetailsResource> {
    @Override
    public int compare(BookingDocumentDetailsResource o1, BookingDocumentDetailsResource o2) {
        return o2.getDocumentPageNumber().compareTo(o1.getDocumentPageNumber());
    }
}
