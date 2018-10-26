package com.coxandkings.travel.operations.helper.booking.product.forex;

import com.coxandkings.travel.operations.helper.enums.PassengerTypeValues;

import java.math.BigDecimal;

public class ForexPassenger {
    private Integer id;
    private Boolean leadPassenger;
    private String salutaion;
    private String passengerName;
    private PassengerTypeValues passengerType;
    private Long dob;

    private BigDecimal tourCost;
    private BigDecimal personal;

    //travel details
    private String airLine; //currecny usd 23
    private Long departureDate;
    private String ticketNumber;

    private String forexStatus;
    private BigDecimal actualMarginAmount;
    private Forex forex;
    private PassportDetails passportDetails;
    private Buying tourCostBuying;
    private Buying personalExpensesBuying;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getLeadPassenger() {
        return leadPassenger;
    }

    public void setLeadPassenger(Boolean leadPassenger) {
        this.leadPassenger = leadPassenger;
    }

    public String getSalutaion() {
        return salutaion;
    }

    public void setSalutaion(String salutaion) {
        this.salutaion = salutaion;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public PassengerTypeValues getPassengerType() {
        return passengerType;
    }

    public void setPassengerType(PassengerTypeValues passengerType) {
        this.passengerType = passengerType;
    }

    public Long getDob() {
        return dob;
    }

    public void setDob(Long dob) {
        this.dob = dob;
    }

    public BigDecimal getTourCost() {
        return tourCost;
    }

    public void setTourCost(BigDecimal tourCost) {
        this.tourCost = tourCost;
    }

    public BigDecimal getPersonal() {
        return personal;
    }

    public void setPersonal(BigDecimal personal) {
        this.personal = personal;
    }

    public String getAirLine() {
        return airLine;
    }

    public void setAirLine(String airLine) {
        this.airLine = airLine;
    }

    public Long getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Long departureDate) {
        this.departureDate = departureDate;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getForexStatus() {
        return forexStatus;
    }

    public void setForexStatus(String forexStatus) {
        this.forexStatus = forexStatus;
    }

    public BigDecimal getActualMarginAmount() {
        return actualMarginAmount;
    }

    public void setActualMarginAmount(BigDecimal actualMarginAmount) {
        this.actualMarginAmount = actualMarginAmount;
    }

    public Forex getForex() {
        return forex;
    }

    public void setForex(Forex forex) {
        this.forex = forex;
    }

    public PassportDetails getPassportDetails() {
        return passportDetails;
    }

    public void setPassportDetails(PassportDetails passportDetails) {
        this.passportDetails = passportDetails;
    }

    public Buying getTourCostBuying() {
        return tourCostBuying;
    }

    public void setTourCostBuying(Buying tourCostBuying) {
        this.tourCostBuying = tourCostBuying;
    }

    public Buying getPersonalExpensesBuying() {
        return personalExpensesBuying;
    }

    public void setPersonalExpensesBuying(Buying personalExpensesBuying) {
        this.personalExpensesBuying = personalExpensesBuying;
    }
}
