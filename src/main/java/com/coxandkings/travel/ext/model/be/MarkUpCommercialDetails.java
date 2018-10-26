
package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "totalFare",
    "commercialCalculationAmount",
    "commercialFareComponent",
    "commercialCalculationPercentage",
    "commercialCurrency",
    "commercialAmount",
    "fareBreakUp",
    "commercialName"
})
public class MarkUpCommercialDetails implements Serializable
{

    @JsonProperty("totalFare")
    private Double totalFare;
    @JsonProperty("commercialCalculationAmount")
    private Integer commercialCalculationAmount;
    @JsonProperty("commercialFareComponent")
    private String commercialFareComponent;
    @JsonProperty("commercialCalculationPercentage")
    private Integer commercialCalculationPercentage;
    @JsonProperty("commercialCurrency")
    private Object commercialCurrency;
    @JsonProperty("commercialAmount")
    private Double commercialAmount;
    @JsonProperty("fareBreakUp")
    private FareBreakUp fareBreakUp;
    @JsonProperty("commercialName")
    private String commercialName;
    private final static long serialVersionUID = 8180651582152072845L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public MarkUpCommercialDetails() {
    }

    /**
     * 
     * @param totalFare
     * @param commercialCalculationAmount
     * @param commercialFareComponent
     * @param commercialCalculationPercentage
     * @param commercialCurrency
     * @param commercialAmount
     * @param fareBreakUp
     * @param commercialName
     */
    public MarkUpCommercialDetails(Double totalFare, Integer commercialCalculationAmount, String commercialFareComponent, Integer commercialCalculationPercentage, Object commercialCurrency, Double commercialAmount, FareBreakUp fareBreakUp, String commercialName) {
        super();
        this.totalFare = totalFare;
        this.commercialCalculationAmount = commercialCalculationAmount;
        this.commercialFareComponent = commercialFareComponent;
        this.commercialCalculationPercentage = commercialCalculationPercentage;
        this.commercialCurrency = commercialCurrency;
        this.commercialAmount = commercialAmount;
        this.fareBreakUp = fareBreakUp;
        this.commercialName = commercialName;
    }

    @JsonProperty("totalFare")
    public Double getTotalFare() {
        return totalFare;
    }

    @JsonProperty("totalFare")
    public void setTotalFare(Double totalFare) {
        this.totalFare = totalFare;
    }

    @JsonProperty("commercialCalculationAmount")
    public Integer getCommercialCalculationAmount() {
        return commercialCalculationAmount;
    }

    @JsonProperty("commercialCalculationAmount")
    public void setCommercialCalculationAmount(Integer commercialCalculationAmount) {
        this.commercialCalculationAmount = commercialCalculationAmount;
    }

    @JsonProperty("commercialFareComponent")
    public String getCommercialFareComponent() {
        return commercialFareComponent;
    }

    @JsonProperty("commercialFareComponent")
    public void setCommercialFareComponent(String commercialFareComponent) {
        this.commercialFareComponent = commercialFareComponent;
    }

    @JsonProperty("commercialCalculationPercentage")
    public Integer getCommercialCalculationPercentage() {
        return commercialCalculationPercentage;
    }

    @JsonProperty("commercialCalculationPercentage")
    public void setCommercialCalculationPercentage(Integer commercialCalculationPercentage) {
        this.commercialCalculationPercentage = commercialCalculationPercentage;
    }

    @JsonProperty("commercialCurrency")
    public Object getCommercialCurrency() {
        return commercialCurrency;
    }

    @JsonProperty("commercialCurrency")
    public void setCommercialCurrency(Object commercialCurrency) {
        this.commercialCurrency = commercialCurrency;
    }

    @JsonProperty("commercialAmount")
    public Double getCommercialAmount() {
        return commercialAmount;
    }

    @JsonProperty("commercialAmount")
    public void setCommercialAmount(Double commercialAmount) {
        this.commercialAmount = commercialAmount;
    }

    @JsonProperty("fareBreakUp")
    public FareBreakUp getFareBreakUp() {
        return fareBreakUp;
    }

    @JsonProperty("fareBreakUp")
    public void setFareBreakUp(FareBreakUp fareBreakUp) {
        this.fareBreakUp = fareBreakUp;
    }

    @JsonProperty("commercialName")
    public String getCommercialName() {
        return commercialName;
    }

    @JsonProperty("commercialName")
    public void setCommercialName(String commercialName) {
        this.commercialName = commercialName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(MarkUpCommercialDetails.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("totalFare");
        sb.append('=');
        sb.append(((this.totalFare == null)?"<null>":this.totalFare));
        sb.append(',');
        sb.append("commercialCalculationAmount");
        sb.append('=');
        sb.append(((this.commercialCalculationAmount == null)?"<null>":this.commercialCalculationAmount));
        sb.append(',');
        sb.append("commercialFareComponent");
        sb.append('=');
        sb.append(((this.commercialFareComponent == null)?"<null>":this.commercialFareComponent));
        sb.append(',');
        sb.append("commercialCalculationPercentage");
        sb.append('=');
        sb.append(((this.commercialCalculationPercentage == null)?"<null>":this.commercialCalculationPercentage));
        sb.append(',');
        sb.append("commercialCurrency");
        sb.append('=');
        sb.append(((this.commercialCurrency == null)?"<null>":this.commercialCurrency));
        sb.append(',');
        sb.append("commercialAmount");
        sb.append('=');
        sb.append(((this.commercialAmount == null)?"<null>":this.commercialAmount));
        sb.append(',');
        sb.append("fareBreakUp");
        sb.append('=');
        sb.append(((this.fareBreakUp == null)?"<null>":this.fareBreakUp));
        sb.append(',');
        sb.append("commercialName");
        sb.append('=');
        sb.append(((this.commercialName == null)?"<null>":this.commercialName));
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
        result = ((result* 31)+((this.totalFare == null)? 0 :this.totalFare.hashCode()));
        result = ((result* 31)+((this.commercialCalculationAmount == null)? 0 :this.commercialCalculationAmount.hashCode()));
        result = ((result* 31)+((this.commercialFareComponent == null)? 0 :this.commercialFareComponent.hashCode()));
        result = ((result* 31)+((this.commercialCalculationPercentage == null)? 0 :this.commercialCalculationPercentage.hashCode()));
        result = ((result* 31)+((this.commercialCurrency == null)? 0 :this.commercialCurrency.hashCode()));
        result = ((result* 31)+((this.commercialAmount == null)? 0 :this.commercialAmount.hashCode()));
        result = ((result* 31)+((this.fareBreakUp == null)? 0 :this.fareBreakUp.hashCode()));
        result = ((result* 31)+((this.commercialName == null)? 0 :this.commercialName.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof MarkUpCommercialDetails) == false) {
            return false;
        }
        MarkUpCommercialDetails rhs = ((MarkUpCommercialDetails) other);
        return (((((((((this.totalFare == rhs.totalFare)||((this.totalFare!= null)&&this.totalFare.equals(rhs.totalFare)))&&((this.commercialCalculationAmount == rhs.commercialCalculationAmount)||((this.commercialCalculationAmount!= null)&&this.commercialCalculationAmount.equals(rhs.commercialCalculationAmount))))&&((this.commercialFareComponent == rhs.commercialFareComponent)||((this.commercialFareComponent!= null)&&this.commercialFareComponent.equals(rhs.commercialFareComponent))))&&((this.commercialCalculationPercentage == rhs.commercialCalculationPercentage)||((this.commercialCalculationPercentage!= null)&&this.commercialCalculationPercentage.equals(rhs.commercialCalculationPercentage))))&&((this.commercialCurrency == rhs.commercialCurrency)||((this.commercialCurrency!= null)&&this.commercialCurrency.equals(rhs.commercialCurrency))))&&((this.commercialAmount == rhs.commercialAmount)||((this.commercialAmount!= null)&&this.commercialAmount.equals(rhs.commercialAmount))))&&((this.fareBreakUp == rhs.fareBreakUp)||((this.fareBreakUp!= null)&&this.fareBreakUp.equals(rhs.fareBreakUp))))&&((this.commercialName == rhs.commercialName)||((this.commercialName!= null)&&this.commercialName.equals(rhs.commercialName))));
    }

}
