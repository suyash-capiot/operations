
package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "originDestinationOptions"
})
public class  FlightDetails implements Serializable
{

    @JsonProperty("originDestinationOptions")
    private List<OriginDestinationOption> originDestinationOptions = new ArrayList<OriginDestinationOption>();
    private final static long serialVersionUID = 4863361733107817778L;


    /**
     * No args constructor for use in serialization
     * 
     */
    public FlightDetails() {
    }

    /**
     * 
     * @param originDestinationOptions
     */
    public FlightDetails(List<OriginDestinationOption> originDestinationOptions) {
        super();
        this.originDestinationOptions = originDestinationOptions;
    }

    @JsonProperty("originDestinationOptions")
    public List<OriginDestinationOption> getOriginDestinationOptions() {
        return originDestinationOptions;
    }

    @JsonProperty("originDestinationOptions")
    public void setOriginDestinationOptions(List<OriginDestinationOption> originDestinationOptions) {
        this.originDestinationOptions = originDestinationOptions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(FlightDetails.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("originDestinationOptions");
        sb.append('=');
        sb.append(((this.originDestinationOptions == null)?"<null>":this.originDestinationOptions));
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
        result = ((result* 31)+((this.originDestinationOptions == null)? 0 :this.originDestinationOptions.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof FlightDetails) == false) {
            return false;
        }
        FlightDetails rhs = ((FlightDetails) other);
        return ((this.originDestinationOptions == rhs.originDestinationOptions)||((this.originDestinationOptions!= null)&&this.originDestinationOptions.equals(rhs.originDestinationOptions)));
    }

}
