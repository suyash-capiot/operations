package com.coxandkings.travel.operations.resource.changesuppliername.request.air;

import com.coxandkings.travel.ext.model.be.PaxDetail;
import com.coxandkings.travel.ext.model.be.PaxInfo;
import com.coxandkings.travel.ext.model.be.PricedItinerary;

import java.util.List;

public class AirRePriceRequestBody {
    private String tripType;
    private List<PricedItinerary> pricedItinerary;
    private List<PaxInfo> paxInfo;
    private List<PaxDetail> paxDetails;

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public List<PricedItinerary> getPricedItinerary() {
        return pricedItinerary;
    }

    public void setPricedItinerary(List<PricedItinerary> pricedItinerary) {
        this.pricedItinerary = pricedItinerary;
    }

    public List<PaxInfo> getPaxInfo() {
        return paxInfo;
    }

    public void setPaxInfo(List<PaxInfo> paxInfo) {
        this.paxInfo = paxInfo;
    }

    public List<PaxDetail> getPaxDetails() {
        return paxDetails;
    }

    public void setPaxDetails(List<PaxDetail> paxDetails) {
        this.paxDetails = paxDetails;
    }
}
