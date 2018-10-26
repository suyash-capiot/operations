package com.coxandkings.travel.operations.resource.managedocumentation.documentmaster;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "bookingType",
    "criteriaType",
    "payment",
    "numberOfNights",
    "others"
})
public class TriggerEvent {

    @JsonProperty("bookingType")
    private List<String> bookingType = null;
    @JsonProperty("criteriaType")
    private String criteriaType;
    @JsonProperty("payment")
    private List<Payment> payment = null;
    @JsonProperty("numberOfNights")
    private List<NumberOfNight> numberOfNights = null;
    @JsonProperty("others")
    private List<Other> others = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("bookingType")
    public List<String> getBookingType() {
        return bookingType;
    }

    @JsonProperty("bookingType")
    public void setBookingType(List<String> bookingType) {
        this.bookingType = bookingType;
    }

    @JsonProperty("criteriaType")
    public String getCriteriaType() {
        return criteriaType;
    }

    @JsonProperty("criteriaType")
    public void setCriteriaType(String criteriaType) {
        this.criteriaType = criteriaType;
    }

    @JsonProperty("payment")
    public List<Payment> getPayment() {
        return payment;
    }

    @JsonProperty("payment")
    public void setPayment(List<Payment> payment) {
        this.payment = payment;
    }

    @JsonProperty("numberOfNights")
    public List<NumberOfNight> getNumberOfNights() {
        return numberOfNights;
    }

    @JsonProperty("numberOfNights")
    public void setNumberOfNights(List<NumberOfNight> numberOfNights) {
        this.numberOfNights = numberOfNights;
    }

    @JsonProperty("others")
    public List<Other> getOthers() {
        return others;
    }

    @JsonProperty("others")
    public void setOthers(List<Other> others) {
        this.others = others;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
