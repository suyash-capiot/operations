
package com.coxandkings.travel.operations.resource.qcmanagement.supplier;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "isAdvanceDefinition",
    "ticketingDate",
    "travelType",
    "journeyType",
    "connectivitySupplier",
    "connectivitySupplierType",
    "credentialsName",
    "bookingType"
})
public class BrmsSupplierAdvancedDefinition {

    @JsonProperty("isAdvanceDefinition")
    private Boolean isAdvanceDefinition;
    @JsonProperty("ticketingDate")
    private String ticketingDate;
    @JsonProperty("travelType")
    private String travelType;
    @JsonProperty("journeyType")
    private String journeyType;
    @JsonProperty("connectivitySupplier")
    private String connectivitySupplier;
    @JsonProperty("connectivitySupplierType")
    private String connectivitySupplierType;
    @JsonProperty("credentialsName")
    private String credentialsName;
    @JsonProperty("bookingType")
    private String bookingType;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("isAdvanceDefinition")
    public Boolean getIsAdvanceDefinition() {
        return isAdvanceDefinition;
    }

    @JsonProperty("isAdvanceDefinition")
    public void setIsAdvanceDefinition(Boolean isAdvanceDefinition) {
        this.isAdvanceDefinition = isAdvanceDefinition;
    }

    @JsonProperty("ticketingDate")
    public String getTicketingDate() {
        return ticketingDate;
    }

    @JsonProperty("ticketingDate")
    public void setTicketingDate(String ticketingDate) {
        this.ticketingDate = ticketingDate;
    }

    @JsonProperty("travelType")
    public String getTravelType() {
        return travelType;
    }

    @JsonProperty("travelType")
    public void setTravelType(String travelType) {
        this.travelType = travelType;
    }

    @JsonProperty("journeyType")
    public String getJourneyType() {
        return journeyType;
    }

    @JsonProperty("journeyType")
    public void setJourneyType(String journeyType) {
        this.journeyType = journeyType;
    }

    @JsonProperty("connectivitySupplier")
    public String getConnectivitySupplier() {
        return connectivitySupplier;
    }

    @JsonProperty("connectivitySupplier")
    public void setConnectivitySupplier(String connectivitySupplier) {
        this.connectivitySupplier = connectivitySupplier;
    }

    @JsonProperty("connectivitySupplierType")
    public String getConnectivitySupplierType() {
        return connectivitySupplierType;
    }

    @JsonProperty("connectivitySupplierType")
    public void setConnectivitySupplierType(String connectivitySupplierType) {
        this.connectivitySupplierType = connectivitySupplierType;
    }

    @JsonProperty("credentialsName")
    public String getCredentialsName() {
        return credentialsName;
    }

    @JsonProperty("credentialsName")
    public void setCredentialsName(String credentialsName) {
        this.credentialsName = credentialsName;
    }

    @JsonProperty("bookingType")
    public String getBookingType() {
        return bookingType;
    }

    @JsonProperty("bookingType")
    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
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
