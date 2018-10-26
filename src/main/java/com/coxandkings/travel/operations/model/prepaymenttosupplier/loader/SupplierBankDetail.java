package com.coxandkings.travel.operations.model.prepaymenttosupplier.loader;


import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "intermediaryBankSWIFTorIFCCode",
        "supplierBankSWIFTorIFCCode",
        "supplierBankAddress",
        "supplierCurrency",
        "supplierBankAccNumber",
        "supplierBankName",
        "_id"
})
public class SupplierBankDetail {

    @JsonProperty("intermediaryBankSWIFTorIFCCode")
    private String intermediaryBankSWIFTorIFCCode;
    @JsonProperty("supplierBankSWIFTorIFCCode")
    private String supplierBankSWIFTorIFCCode;
    @JsonProperty("supplierBankAddress")
    private String supplierBankAddress;
    @JsonProperty("supplierCurrency")
    private String supplierCurrency;
    @JsonProperty("supplierBankAccNumber")
    private String supplierBankAccNumber;
    @JsonProperty("supplierBankName")
    private String supplierBankName;
    @JsonProperty("_id")
    private String id;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("intermediaryBankSWIFTorIFCCode")
    public String getIntermediaryBankSWIFTorIFCCode() {
        return intermediaryBankSWIFTorIFCCode;
    }

    @JsonProperty("intermediaryBankSWIFTorIFCCode")
    public void setIntermediaryBankSWIFTorIFCCode(String intermediaryBankSWIFTorIFCCode) {
        this.intermediaryBankSWIFTorIFCCode = intermediaryBankSWIFTorIFCCode;
    }

    @JsonProperty("supplierBankSWIFTorIFCCode")
    public String getSupplierBankSWIFTorIFCCode() {
        return supplierBankSWIFTorIFCCode;
    }

    @JsonProperty("supplierBankSWIFTorIFCCode")
    public void setSupplierBankSWIFTorIFCCode(String supplierBankSWIFTorIFCCode) {
        this.supplierBankSWIFTorIFCCode = supplierBankSWIFTorIFCCode;
    }

    @JsonProperty("supplierBankAddress")
    public String getSupplierBankAddress() {
        return supplierBankAddress;
    }

    @JsonProperty("supplierBankAddress")
    public void setSupplierBankAddress(String supplierBankAddress) {
        this.supplierBankAddress = supplierBankAddress;
    }

    @JsonProperty("supplierCurrency")
    public String getSupplierCurrency() {
        return supplierCurrency;
    }

    @JsonProperty("supplierCurrency")
    public void setSupplierCurrency(String supplierCurrency) {
        this.supplierCurrency = supplierCurrency;
    }

    @JsonProperty("supplierBankAccNumber")
    public String getSupplierBankAccNumber() {
        return supplierBankAccNumber;
    }

    @JsonProperty("supplierBankAccNumber")
    public void setSupplierBankAccNumber(String supplierBankAccNumber) {
        this.supplierBankAccNumber = supplierBankAccNumber;
    }

    @JsonProperty("supplierBankName")
    public String getSupplierBankName() {
        return supplierBankName;
    }

    @JsonProperty("supplierBankName")
    public void setSupplierBankName(String supplierBankName) {
        this.supplierBankName = supplierBankName;
    }

    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
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