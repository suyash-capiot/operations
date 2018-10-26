package com.coxandkings.travel.operations.resource.failure;

import org.springframework.web.bind.annotation.RequestParam;

public class DuplicateBookingsResource {

    private String bookId;
    private String sortCriteria; // Default : Ascending
    private String sortOrder;


    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getSortCriteria() {
        return sortCriteria;
    }

    public void setSortCriteria(String sortCriteria) {
        this.sortCriteria = sortCriteria;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
