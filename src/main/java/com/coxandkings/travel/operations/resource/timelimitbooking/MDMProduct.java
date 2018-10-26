
package com.coxandkings.travel.operations.resource.timelimitbooking;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "mode",
    "productCat",
    "productCatSubtype",
    "supplierId",
    "supplierName",
    "effectiveFrom",
    "_id",
    "productFlav",
    "airType",
    "status",
    "reason"
})
public class MDMProduct {

    @JsonProperty("mode")
    private String mode;
    @JsonProperty("productCat")
    private String productCat;
    @JsonProperty("productCatSubtype")
    private String productCatSubtype;
    @JsonProperty("supplierId")
    private String supplierId;
    @JsonProperty("supplierName")
    private String supplierName;
    @JsonProperty("effectiveFrom")
    private String effectiveFrom;
    @JsonProperty("_id")
    private String id;
    @JsonProperty("productFlav")
    private List<ProductFlav> productFlav = null;
    @JsonProperty("airType")
    private String airType;
    @JsonProperty("status")
    private String status;
    @JsonProperty("reason")
    private String reason;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("mode")
    public String getMode() {
        return mode;
    }

    @JsonProperty("mode")
    public void setMode(String mode) {
        this.mode = mode;
    }

    @JsonProperty("productCat")
    public String getProductCat() {
        return productCat;
    }

    @JsonProperty("productCat")
    public void setProductCat(String productCat) {
        this.productCat = productCat;
    }

    @JsonProperty("productCatSubtype")
    public String getProductCatSubtype() {
        return productCatSubtype;
    }

    @JsonProperty("productCatSubtype")
    public void setProductCatSubtype(String productCatSubtype) {
        this.productCatSubtype = productCatSubtype;
    }

    @JsonProperty("supplierId")
    public String getSupplierId() {
        return supplierId;
    }

    @JsonProperty("supplierId")
    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    @JsonProperty("supplierName")
    public String getSupplierName() {
        return supplierName;
    }

    @JsonProperty("supplierName")
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    @JsonProperty("effectiveFrom")
    public String getEffectiveFrom() {
        return effectiveFrom;
    }

    @JsonProperty("effectiveFrom")
    public void setEffectiveFrom(String effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("productFlav")
    public List<ProductFlav> getProductFlav() {
        return productFlav;
    }

    @JsonProperty("productFlav")
    public void setProductFlav(List<ProductFlav> productFlav) {
        this.productFlav = productFlav;
    }

    @JsonProperty("airType")
    public String getAirType() {
        return airType;
    }

    @JsonProperty("airType")
    public void setAirType(String airType) {
        this.airType = airType;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("reason")
    public String getReason() {
        return reason;
    }

    @JsonProperty("reason")
    public void setReason(String reason) {
        this.reason = reason;
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
