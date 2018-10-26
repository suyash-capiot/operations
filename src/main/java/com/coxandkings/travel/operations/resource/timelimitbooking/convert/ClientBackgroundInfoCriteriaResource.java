package com.coxandkings.travel.operations.resource.timelimitbooking.convert;

public class ClientBackgroundInfoCriteriaResource {

    private String bookID;
    private String fromDateRange;
    private String toDateRange;

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getFromDateRange() {
        return fromDateRange;
    }

    public void setFromDateRange(String fromDateRange) {
        this.fromDateRange = fromDateRange;
    }

    public String getToDateRange() {
        return toDateRange;
    }

    public void setToDateRange(String toDateRange) {
        this.toDateRange = toDateRange;
    }
}
