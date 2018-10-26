
package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "taxValue",
    "taxName"
})
public class TaxDetail implements Serializable
{

    @JsonProperty("taxValue")
    private Integer taxValue;
    @JsonProperty("taxName")
    private String taxName;
    private final static long serialVersionUID = 601911517512270151L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public TaxDetail() {
    }

    /**
     * 
     * @param taxValue
     * @param taxName
     */
    public TaxDetail(Integer taxValue, String taxName) {
        super();
        this.taxValue = taxValue;
        this.taxName = taxName;
    }

    @JsonProperty("taxValue")
    public Integer getTaxValue() {
        return taxValue;
    }

    @JsonProperty("taxValue")
    public void setTaxValue(Integer taxValue) {
        this.taxValue = taxValue;
    }

    @JsonProperty("taxName")
    public String getTaxName() {
        return taxName;
    }

    @JsonProperty("taxName")
    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(TaxDetail.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("taxValue");
        sb.append('=');
        sb.append(((this.taxValue == null)?"<null>":this.taxValue));
        sb.append(',');
        sb.append("taxName");
        sb.append('=');
        sb.append(((this.taxName == null)?"<null>":this.taxName));
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
        result = ((result* 31)+((this.taxValue == null)? 0 :this.taxValue.hashCode()));
        result = ((result* 31)+((this.taxName == null)? 0 :this.taxName.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TaxDetail) == false) {
            return false;
        }
        TaxDetail rhs = ((TaxDetail) other);
        return (((this.taxValue == rhs.taxValue)||((this.taxValue!= null)&&this.taxValue.equals(rhs.taxValue)))&&((this.taxName == rhs.taxName)||((this.taxName!= null)&&this.taxName.equals(rhs.taxName))));
    }

}
