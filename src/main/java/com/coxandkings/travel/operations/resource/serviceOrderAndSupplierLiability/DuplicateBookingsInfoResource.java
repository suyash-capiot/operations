package com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability;

import java.util.List;

public class DuplicateBookingsInfoResource {

    private String id;
    private String bookId;
    private List<DuplicateOrdersResource> duplicateOrders;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public List<DuplicateOrdersResource> getDuplicateOrders() {
        return duplicateOrders;
    }

    public void setDuplicateOrders(List<DuplicateOrdersResource> duplicateOrders) {
        this.duplicateOrders = duplicateOrders;
    }
}
