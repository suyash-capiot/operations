package com.coxandkings.travel.operations.resource.amendentitycommercial;

import com.coxandkings.travel.operations.model.core.OpsOrderClientCommercial;
import com.coxandkings.travel.operations.model.core.OpsOrderSupplierCommercial;

import java.util.List;

public class CommercialResource {

    private MarginDetails marginDetails;
    private List<OpsOrderClientCommercial> orderClientCommercials;
    private List<OpsOrderSupplierCommercial> orderSupplierCommercials;
    private HotelPrice hotelPrice;
    private FlightPrice flightPrice;

    public HotelPrice getHotelPrice() {
        return hotelPrice;
    }

    public void setHotelPrice(HotelPrice hotelPrice) {
        this.hotelPrice = hotelPrice;
    }

    public FlightPrice getFlightPrice() {
        return flightPrice;
    }

    public void setFlightPrice(FlightPrice flightPrice) {
        this.flightPrice = flightPrice;
    }

    public List<OpsOrderClientCommercial> getOrderClientCommercials() {
        return orderClientCommercials;
    }

    public void setOrderClientCommercials(List<OpsOrderClientCommercial> orderClientCommercials) {
        this.orderClientCommercials = orderClientCommercials;
    }

    public List<OpsOrderSupplierCommercial> getOrderSupplierCommercials() {
        return orderSupplierCommercials;
    }

    public void setOrderSupplierCommercials(List<OpsOrderSupplierCommercial> orderSupplierCommercials) {
        this.orderSupplierCommercials = orderSupplierCommercials;
    }

    public MarginDetails getMarginDetails() {
        return marginDetails;
    }

    public void setMarginDetails(MarginDetails marginDetails) {
        this.marginDetails = marginDetails;
    }

}
	