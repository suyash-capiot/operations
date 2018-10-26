package com.coxandkings.travel.operations.service.changesuppliername.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.RestUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ChangeSupplierNameOrPriceServiceImpl {
    @Value(value = "${booking_engine.get.booking}")
    private String getBookingUrl;

    @Value(value = "${booking_engine.base_url}")
    private String beBaseUrl;
    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    public JSONObject getRepriceForAir(String bookingId, String orderId, String productCategory, String productSubCategory) {
        JSONObject jsonObjectResponse = null;
        String bookingDetails= getBooking(bookingId);
        JSONObject requestHeader= (JSONObject) jsonObjectProvider.getChildObject(bookingDetails,"$.responseHeader",JSONObject.class);

        return jsonObjectResponse;
    }

    public String getBooking(String bookingId) {
        String parameterizedGetBooking = getBookingUrl + bookingId;
        String bookingDetails = null;
        try {
            bookingDetails = RestUtils.getForObject(parameterizedGetBooking, String.class);

            if (bookingDetails.contains("BE_ERR_001")) {
                throw new OperationException("Failed to get booking for booking id : " + bookingId);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookingDetails;
    }
}
