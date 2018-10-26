package com.coxandkings.travel.operations.resource.changesuppliername.request.accoV2;

import org.json.JSONObject;

public class CSCreateBookingResource {

    private String bookID;

    private String orderID;

    private String newSuppID;
    private JSONObject definedRates;

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getNewSuppID() {
        return newSuppID;
    }

    public void setNewSuppID(String newSuppID) {
        this.newSuppID = newSuppID;
    }

    public JSONObject getDefinedRates() {
        return definedRates;
    }

    public void setDefinedRates(JSONObject definedRates) {
        this.definedRates = definedRates;
    }
}
