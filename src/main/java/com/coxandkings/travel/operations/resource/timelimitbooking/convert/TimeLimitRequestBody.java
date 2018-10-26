package com.coxandkings.travel.operations.resource.timelimitbooking.convert;

import com.coxandkings.travel.operations.model.core.OpsBookingAttribute;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "product",
        "bookingAttribute",
        "orderID"
})
public class TimeLimitRequestBody {

    @JsonProperty("bookingAttribute")
    List<Map<OpsBookingAttribute, String>> bookingAttribute;
    @JsonProperty("product")
    private String product;
    @JsonProperty("orderID")
    private String orderID;

    @JsonProperty("product")
    public String getProduct() {
        return product;
    }

    @JsonProperty("product")
    public void setProduct(String product) {
        this.product = product;
    }

    @JsonProperty("bookingAttribute")
    public List<Map<OpsBookingAttribute, String>> getBookingAttribute() {
        return bookingAttribute;
    }

    @JsonProperty("bookingAttribute")
    public void setBookingAttribute(List<Map<OpsBookingAttribute, String>> bookingAttribute) {
        this.bookingAttribute = bookingAttribute;
    }

    @JsonProperty("orderID")
    public String getOrderID() {
        return orderID;
    }

    @JsonProperty("orderID")
    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }
}
