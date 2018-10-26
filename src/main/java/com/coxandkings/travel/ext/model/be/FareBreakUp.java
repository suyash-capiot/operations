
package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "baseFare",
    "taxDetails"
})
public class FareBreakUp implements Serializable
{

    @JsonProperty("baseFare")
    private Double baseFare;
    @JsonProperty("taxDetails")
    private List<TaxDetail> taxDetails = new ArrayList<TaxDetail>();
    private final static long serialVersionUID = 5725753931411553355L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public FareBreakUp() {
    }

    /**
     * 
     * @param baseFare
     * @param taxDetails
     */
    public FareBreakUp(Double baseFare, List<TaxDetail> taxDetails) {
        super();
        this.baseFare = baseFare;
        this.taxDetails = taxDetails;
    }

    @JsonProperty("baseFare")
    public Double getBaseFare() {
        return baseFare;
    }

    @JsonProperty("baseFare")
    public void setBaseFare(Double baseFare) {
        this.baseFare = baseFare;
    }

    @JsonProperty("taxDetails")
    public List<TaxDetail> getTaxDetails() {
        return taxDetails;
    }

    @JsonProperty("taxDetails")
    public void setTaxDetails(List<TaxDetail> taxDetails) {
        this.taxDetails = taxDetails;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(FareBreakUp.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("baseFare");
        sb.append('=');
        sb.append(((this.baseFare == null)?"<null>":this.baseFare));
        sb.append(',');
        sb.append("taxDetails");
        sb.append('=');
        sb.append(((this.taxDetails == null)?"<null>":this.taxDetails));
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
        result = ((result* 31)+((this.taxDetails == null)? 0 :this.taxDetails.hashCode()));
        result = ((result* 31)+((this.baseFare == null)? 0 :this.baseFare.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof FareBreakUp) == false) {
            return false;
        }
        FareBreakUp rhs = ((FareBreakUp) other);
        return (((this.taxDetails == rhs.taxDetails)||((this.taxDetails!= null)&&this.taxDetails.equals(rhs.taxDetails)))&&((this.baseFare == rhs.baseFare)||((this.baseFare!= null)&&this.baseFare.equals(rhs.baseFare))));
    }

}
