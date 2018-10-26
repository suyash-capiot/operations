package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "amountAfterTax",
        "amountBeforeTax",
        "taxes",
        "currencyCode",
        "type"
})
public class HolidaysPaxTypeFares {

    @JsonProperty("amountAfterTax")
    private String amountAfterTax;

    @JsonProperty("amountBeforeTax")
    private String amountBeforeTax;

    @JsonProperty("taxes")
    private Taxes taxes;

    @JsonProperty("currencyCode")
    private String currencyCode;

    @JsonProperty("type")
    private String type;

    @JsonProperty("supplierCommercials")
    private List<SupplierCommercial> supplierCommercials;

    @JsonProperty("clientEntityCommercials")
    private List<ClientEntityCommercial> clientEntityCommercials;

    @JsonProperty("receivables")
    private Receivables receivables;

    public String getAmountAfterTax() {
        return amountAfterTax;
    }

    public void setAmountAfterTax(String amountAfterTax) {
        this.amountAfterTax = amountAfterTax;
    }

    public String getAmountBeforeTax() {
        return amountBeforeTax;
    }

    public void setAmountBeforeTax(String amountBeforeTax) {
        this.amountBeforeTax = amountBeforeTax;
    }

    public Taxes getTaxes() {
        return taxes;
    }

    public void setTaxes(Taxes taxes) {
        this.taxes = taxes;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<SupplierCommercial> getSupplierCommercials() {
        return supplierCommercials;
    }

    public void setSupplierCommercials(List<SupplierCommercial> supplierCommercials) {
        this.supplierCommercials = supplierCommercials;
    }

    public List<ClientEntityCommercial> getClientEntityCommercials() {
        return clientEntityCommercials;
    }

    public void setClientEntityCommercials(List<ClientEntityCommercial> clientEntityCommercials) {
        this.clientEntityCommercials = clientEntityCommercials;
    }

    public Receivables getReceivables() {
        return receivables;
    }

    public void setReceivables(Receivables receivables) {
        this.receivables = receivables;
    }

  
}
