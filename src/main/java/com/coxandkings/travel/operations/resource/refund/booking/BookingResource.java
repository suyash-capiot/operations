package com.coxandkings.travel.operations.resource.refund.booking;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class BookingResource implements Serializable{
    private String refId;
    private Map<String, String> bookingAttributes;
    private List<ProductDetailsResource> productDetails;
    private List<PassengerDetails> passengerDetails;

}
