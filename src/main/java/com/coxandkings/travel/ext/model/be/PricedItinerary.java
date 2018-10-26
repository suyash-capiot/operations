
package com.coxandkings.travel.ext.model.be;


public class PricedItinerary {

    private Boolean isReturnJourneyCombined;
    private AirItinerary airItinerary;
    private String supplierRef;

    public Boolean getIsReturnJourneyCombined() {
        return isReturnJourneyCombined;
    }

    public void setIsReturnJourneyCombined(Boolean isReturnJourneyCombined) {
        this.isReturnJourneyCombined = isReturnJourneyCombined;
    }

    public AirItinerary getAirItinerary() {
        return airItinerary;
    }

    public void setAirItinerary(AirItinerary airItinerary) {
        this.airItinerary = airItinerary;
    }

    public String getSupplierRef() {
        return supplierRef;
    }

    public void setSupplierRef(String supplierRef) {
        this.supplierRef = supplierRef;
    }

}
