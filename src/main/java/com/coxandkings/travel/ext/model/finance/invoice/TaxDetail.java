
package com.coxandkings.travel.ext.model.finance.invoice;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "taxAmount",
    "taxCode",
    "currency",
    "taxRate"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaxDetail {

    @JsonProperty("id")
    private String id;
    @JsonProperty("taxAmount")
    private Double taxAmount;
    @JsonProperty("taxCode")
    private String taxCode;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("taxRate")
    private Double taxRate;
    @JsonProperty("hsnSacCode")
    private String hsnSacCode;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("taxAmount")
    public Double getTaxAmount() {
        return taxAmount;
    }

    @JsonProperty("taxAmount")
    public void setTaxAmount(Double taxAmount) {
        this.taxAmount = taxAmount;
    }

    @JsonProperty("taxCode")
    public String getTaxCode() {
        return taxCode;
    }

    @JsonProperty("taxCode")
    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    @JsonProperty("currency")
    public String getCurrency() {
        return currency;
    }

    @JsonProperty("currency")
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @JsonProperty("taxRate")
    public Double getTaxRate() {
        return taxRate;
    }

    @JsonProperty("taxRate")
    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public String getHsnSacCode() {
        return hsnSacCode;
    }

    public void setHsnSacCode(String hsnSacCode) {
        this.hsnSacCode = hsnSacCode;
    }

}
