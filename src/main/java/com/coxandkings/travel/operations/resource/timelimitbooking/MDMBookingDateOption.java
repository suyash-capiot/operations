
package com.coxandkings.travel.operations.resource.timelimitbooking;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "reconfirmationSetFor",
    "tlSetFor",
    "bookingDate"
})
public class MDMBookingDateOption {

    @JsonProperty("reconfirmationSetFor")
    private ReconfirmationSetFor reconfirmationSetFor;
    @JsonProperty("tlSetFor")
    private TlSetFor tlSetFor;
    @JsonProperty("bookingDate")
    private BookingDate bookingDate;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("reconfirmationSetFor")
    public ReconfirmationSetFor getReconfirmationSetFor() {
        return reconfirmationSetFor;
    }

    @JsonProperty("reconfirmationSetFor")
    public void setReconfirmationSetFor(ReconfirmationSetFor reconfirmationSetFor) {
        this.reconfirmationSetFor = reconfirmationSetFor;
    }

    @JsonProperty("tlSetFor")
    public TlSetFor getTlSetFor() {
        return tlSetFor;
    }

    @JsonProperty("tlSetFor")
    public void setTlSetFor(TlSetFor tlSetFor) {
        this.tlSetFor = tlSetFor;
    }

    @JsonProperty("bookingDate")
    public BookingDate getBookingDate() {
        return bookingDate;
    }

    @JsonProperty("bookingDate")
    public void setBookingDate(BookingDate bookingDate) {
        this.bookingDate = bookingDate;
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
