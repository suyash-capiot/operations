package com.coxandkings.travel.operations.model.productbookedthrother;


import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "other_product_info_flight")
public class Flight extends CommanAttribute
{

    private String airlineName;
    private String flightNo;
    private String terminalNo;


    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getTerminalNo() {
        return terminalNo;
    }

    public void setTerminalNo(String terminalNo) {
        this.terminalNo = terminalNo;
    }

}