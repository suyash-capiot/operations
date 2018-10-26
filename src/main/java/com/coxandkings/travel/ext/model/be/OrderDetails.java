
package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "hotelDetails",
    "orderClientCommercials",
    "orderSupplierPriceInfo",
    "orderTotalPriceInfo",
    "orderSupplierCommercials",
    "airlinePNR",
    "ticketNumber",
    "tripIndicator",
    "tripType",
    "GDSPNR",
    "flightDetails",
    "paxInfo",
    "ticketingPCC",
    "activityDetails",
    "bookingAttribute",
    "holidaysDetails"
})
public class OrderDetails implements Serializable {

    @JsonProperty("hotelDetails")
    private HotelDetails hotelDetails;
    @JsonProperty("orderClientCommercials")
    private List<OrderClientCommercial> orderClientCommercials = new ArrayList<OrderClientCommercial>();
    @JsonProperty("orderSupplierPriceInfo")
    private OrderSupplierPriceInfo orderSupplierPriceInfo;
    @JsonProperty("orderTotalPriceInfo")
    private OrderTotalPriceInfo orderTotalPriceInfo;
    @JsonProperty("orderSupplierCommercials")
    private List<OrderSupplierCommercial> orderSupplierCommercials = new ArrayList<OrderSupplierCommercial>();
    @JsonProperty("airlinePNR")
    private String airlinePNR;
    @JsonProperty("tripIndicator")
    private String tripIndicator;
    @JsonProperty("tripType")
    private String tripType;
    @JsonProperty("GDSPNR")
    private String gDSPNR;
    @JsonProperty("fareInfo")
    private List<FareInfo> fareInfo;


    @JsonProperty("flightDetails")
    private FlightDetails flightDetails;
    @JsonProperty("paxInfo")
    private List<PaxInfo> paxInfo = new ArrayList<PaxInfo>();
    @JsonProperty("ticketingPCC")
    private String ticketingPCC;
    @JsonProperty("activitiesDetails")
    private ActivityDetails activityDetails;
    @JsonProperty("holidaysDetails")
    private HolidaysDetails holidaysDetails;
    @JsonProperty("ticketPNR")
    private String ticketPNR;
    @JsonProperty("ticketNumber")
    private String ticketNumber;
    @JsonProperty("bookingPCC")
    private String bookingPCC;

    private final static long serialVersionUID = 5346975950903763111L;

    public OrderDetails() {
    }

    
    @JsonProperty("activitiesDetails")
    public ActivityDetails getActivitiesDetails() {
		return activityDetails;
	}

    @JsonProperty("activityDetails")
	public void setActivitiesDetails(ActivityDetails activityDetails) {
		this.activityDetails = activityDetails;
	}
    @JsonProperty("hotelDetails")
    public HotelDetails getHotelDetails() {
        return hotelDetails;
    }

    @JsonProperty("hotelDetails")
    public void setHotelDetails(HotelDetails hotelDetails) {
        this.hotelDetails = hotelDetails;
    }

    @JsonProperty("orderClientCommercials")
    public List<OrderClientCommercial> getOrderClientCommercials() {
        return orderClientCommercials;
    }

    @JsonProperty("orderClientCommercials")
    public void setOrderClientCommercials(List<OrderClientCommercial> orderClientCommercials) {
        this.orderClientCommercials = orderClientCommercials;
    }

    @JsonProperty("orderSupplierPriceInfo")
    public OrderSupplierPriceInfo getOrderSupplierPriceInfo() {
        return orderSupplierPriceInfo;
    }

    @JsonProperty("orderSupplierPriceInfo")
    public void setOrderSupplierPriceInfo(OrderSupplierPriceInfo orderSupplierPriceInfo) {
        this.orderSupplierPriceInfo = orderSupplierPriceInfo;
    }

    @JsonProperty("orderTotalPriceInfo")
    public OrderTotalPriceInfo getOrderTotalPriceInfo() {
        return orderTotalPriceInfo;
    }

    @JsonProperty("orderTotalPriceInfo")
    public void setOrderTotalPriceInfo(OrderTotalPriceInfo orderTotalPriceInfo) {
        this.orderTotalPriceInfo = orderTotalPriceInfo;
    }

    @JsonProperty("orderSupplierCommercials")
    public List<OrderSupplierCommercial> getOrderSupplierCommercials() {
        return orderSupplierCommercials;
    }

    @JsonProperty("orderSupplierCommercials")
    public void setOrderSupplierCommercials(List<OrderSupplierCommercial> orderSupplierCommercials) {
        this.orderSupplierCommercials = orderSupplierCommercials;
    }

    @JsonProperty("airlinePNR")
    public String getAirlinePNR() {
        return airlinePNR;
    }

    @JsonProperty("airlinePNR")
    public void setAirlinePNR(String airlinePNR) {
        this.airlinePNR = airlinePNR;
    }

    @JsonProperty("ticketNumber")
    public String getTicketNumber() {
        return ticketNumber;
    }

    @JsonProperty("ticketNumber")
    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    @JsonProperty("tripIndicator")
    public String getTripIndicator() {
        return tripIndicator;
    }

    @JsonProperty("tripIndicator")
    public void setTripIndicator(String tripIndicator) {
        this.tripIndicator = tripIndicator;
    }

    @JsonProperty("tripType")
    public String getTripType() {
        return tripType;
    }

    @JsonProperty("tripType")
    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    @JsonProperty("GDSPNR")
    public String getGDSPNR() {
        return gDSPNR;
    }

    @JsonProperty("GDSPNR")
    public void setGDSPNR(String gDSPNR) {
        this.gDSPNR = gDSPNR;
    }

    @JsonProperty("flightDetails")
    public FlightDetails getFlightDetails() {
        return flightDetails;
    }

    @JsonProperty("flightDetails")
    public void setFlightDetails(FlightDetails flightDetails) {
        this.flightDetails = flightDetails;
    }

    @JsonProperty("paxInfo")
    public List<PaxInfo> getPaxInfo() {
        return paxInfo;
    }

    @JsonProperty("paxInfo")
    public void setPaxInfo(List<PaxInfo> paxInfo) {
        this.paxInfo = paxInfo;
    }

    @JsonProperty("ticketingPCC")
    public String getTicketingPCC() {
        return ticketingPCC;
    }

    @JsonProperty("ticketingPCC")
    public void setTicketingPCC(String ticketingPCC) {
        this.ticketingPCC = ticketingPCC;
    }

    @JsonProperty("holidaysDetails")
    public HolidaysDetails getHolidaysDetails() {
        return holidaysDetails;
    }

    @JsonProperty("holidaysDetails")
    public void setHolidaysDetails(HolidaysDetails holidaysDetails) {
        this.holidaysDetails = holidaysDetails;
    }

    @JsonProperty("gDSPNR")
    public String getgDSPNR() {
        return gDSPNR;
    }

    @JsonProperty("gDSPNR")
    public void setgDSPNR(String gDSPNR) {
        this.gDSPNR = gDSPNR;
    }

    @JsonProperty("ticketPNR")
    public String getTicketPNR() {
        return ticketPNR;
    }

    @JsonProperty("ticketPNR")
    public void setTicketPNR(String ticketPNR) {
        this.ticketPNR = ticketPNR;
    }

    @JsonProperty("bookingPCC")
    public String getBookingPCC() {
        return bookingPCC;
    }

    @JsonProperty("bookingPCC")
    public void setBookingPCC(String bookingPCC) {
        this.bookingPCC = bookingPCC;
    }

    public List<FareInfo> getFareInfo() {
        return fareInfo;
    }

    public void setFareInfo(List<FareInfo> fareInfo) {
        this.fareInfo = fareInfo;
    }
}
