
package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "flightSegment"
})
public class OriginDestinationOption implements Serializable
{

    @JsonProperty("flightSegment")
    private List<FlightSegment> flightSegment = new ArrayList<FlightSegment>();
    private final static long serialVersionUID = -7692507603335734839L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public OriginDestinationOption() {
    }

    /**
     * 
     * @param flightSegment
     */
    public OriginDestinationOption(List<FlightSegment> flightSegment) {
        super();
        this.flightSegment = flightSegment;
    }

    @JsonProperty("flightSegment")
    public List<FlightSegment> getFlightSegment() {
        return flightSegment;
    }

    @JsonProperty("flightSegment")
    public void setFlightSegment(List<FlightSegment> flightSegment) {
        this.flightSegment = flightSegment;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(OriginDestinationOption.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("flightSegment");
        sb.append('=');
        sb.append(((this.flightSegment == null)?"<null>":this.flightSegment));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.flightSegment == null)? 0 :this.flightSegment.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof OriginDestinationOption) == false) {
            return false;
        }
        OriginDestinationOption rhs = ((OriginDestinationOption) other);
        return ((this.flightSegment == rhs.flightSegment)||((this.flightSegment!= null)&&this.flightSegment.equals(rhs.flightSegment)));
    }

}
