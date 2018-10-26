package com.coxandkings.travel.operations.utils;

import com.coxandkings.travel.operations.resource.searchviewfilter.BookingSearchResponseItem;

import java.util.Comparator;

public class BookingSearchResponseSorter implements Comparator<BookingSearchResponseItem> {

    private String CLIENT_DETAILS = "clientDetails";
    private String COMPANY_DETAILS = "companyDetails";

    private String sortColumn = null;

    private boolean sortAsc = false;

    public BookingSearchResponseSorter(String columnToSort) {
        sortColumn = columnToSort;
    }

    public BookingSearchResponseSorter(String columnToSort, boolean newAsc) {
        sortColumn = columnToSort;
        sortAsc = newAsc;
    }

    @Override
    public int compare(BookingSearchResponseItem from, BookingSearchResponseItem to) {
        // First arg is less than second, return -ve
        // First arg is greater than second, return +ve
        // First arg is equal than second, return 0

        String fromCompanyName = null, fromClientName = null, toCompanyName = null, toClientName = null;
        if (from != null) {
            fromClientName = from.getClientName() != null ? from.getClientName().toLowerCase() : null;
            fromCompanyName = from.getCompanyName() != null ? from.getCompanyName().toLowerCase() : null;
        }

        if (to != null) {
            toCompanyName = to.getCompanyName() != null ? to.getCompanyName().toLowerCase() : null;
            toClientName = to.getClientName() != null ? to.getClientName().toLowerCase() : null;
        }


        int result = 0;
        if (sortColumn.equalsIgnoreCase(CLIENT_DETAILS)) {
            if (fromClientName == null) {
                return (toClientName == null) ? 0 : 1;
            }
            if (toClientName == null) {
                return -1;
            }
            result = fromClientName.compareTo(toClientName);
        } else if (sortColumn.equalsIgnoreCase(COMPANY_DETAILS)) {
            if (fromCompanyName == null) {
                return (toCompanyName == null) ? 0 : 1;
            }
            if (toCompanyName == null) {
                return -1;
            }

            result = fromCompanyName.compareTo(toCompanyName);
        }
        return sortAsc ? result : (result * -1);
    }
}
