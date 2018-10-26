package com.coxandkings.travel.operations.enums.history;

import org.springframework.util.StringUtils;

public enum BookingHistoryResponse {
    BOOKID("bookID"),
    ORDERID("orderID"),
    PRODUCT_CATEGORY("productCategory"),
    PRODUCT_SUB_CATEGORY("productSubCategory"),
    ACTION("action"),
    DESCRIPTION("description"),
    STATUS("status"),
    TIMESTAMP("timestamp");

    private String response;

    BookingHistoryResponse(String newResponse) {
        response = newResponse;
    }

    public static BookingHistoryResponse getBookingResponse(String newResponse) {
        BookingHistoryResponse bookingHistoryResponse = null;
        if (StringUtils.isEmpty(newResponse)) {
            return null;
        }
        for (BookingHistoryResponse tempResponse : BookingHistoryResponse.values()) {
            if (tempResponse.getResponse().equalsIgnoreCase(newResponse)) {
                bookingHistoryResponse = tempResponse;
                break;
            }
        }
        return bookingHistoryResponse;
    }

    public String getResponse() {
        return response;
    }
}
