package com.coxandkings.travel.operations.model.prepaymenttosupplier.loader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "_id",
        "supplierName",
        "supplierId",
        "settlementTermId",
        "createdBy",
        "versionHistory",
        "versionNumber",
        "deleted",
        "lastUpdated",
        "createdAt",
        "supplierBankDetails",
        "nonCommissionableCommercials",
        "commissionableCommercials",
        "receivableCommercials",
        "payableCommercials",
        "__v"
})
public class MdmData {

    @JsonProperty("_id")
    private String id;
    @JsonProperty("supplierName")
    private String supplierName;
    @JsonProperty("supplierId")
    private String supplierId;
    @JsonProperty("settlementTermId")
    private String settlementTermId;
    @JsonProperty("createdBy")
    private String createdBy;
    @JsonProperty("versionHistory")
    private List<Object> versionHistory = null;
    @JsonProperty("versionNumber")
    private int versionNumber;
    @JsonProperty("deleted")
    private Boolean deleted;
    @JsonProperty("lastUpdated")
    private String lastUpdated;
    @JsonProperty("createdAt")
    private String createdAt;
    @JsonProperty("supplierBankDetails")
    private List<SupplierBankDetail> supplierBankDetails = null;
    @JsonProperty("nonCommissionableCommercials")
    private NonCommissionableCommercials nonCommissionableCommercials;
    @JsonProperty("commissionableCommercials")
    private CommissionableCommercials commissionableCommercials;
    @JsonProperty("receivableCommercials")
    private ReceivableCommercials receivableCommercials;
    @JsonProperty("payableCommercials")
    private PayableCommercials payableCommercials;
    @JsonProperty("__v")
    private int v;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("supplierName")
    public String getSupplierName() {
        return supplierName;
    }

    @JsonProperty("supplierName")
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    @JsonProperty("supplierId")
    public String getSupplierId() {
        return supplierId;
    }

    @JsonProperty("supplierId")
    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    @JsonProperty("settlementTermId")
    public String getSettlementTermId() {
        return settlementTermId;
    }

    @JsonProperty("settlementTermId")
    public void setSettlementTermId(String settlementTermId) {
        this.settlementTermId = settlementTermId;
    }

    @JsonProperty("createdBy")
    public String getCreatedBy() {
        return createdBy;
    }

    @JsonProperty("createdBy")
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @JsonProperty("versionHistory")
    public List<Object> getVersionHistory() {
        return versionHistory;
    }

    @JsonProperty("versionHistory")
    public void setVersionHistory(List<Object> versionHistory) {
        this.versionHistory = versionHistory;
    }

    @JsonProperty("versionNumber")
    public int getVersionNumber() {
        return versionNumber;
    }

    @JsonProperty("versionNumber")
    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    @JsonProperty("deleted")
    public Boolean getDeleted() {
        return deleted;
    }

    @JsonProperty("deleted")
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @JsonProperty("lastUpdated")
    public String getLastUpdated() {
        return lastUpdated;
    }

    @JsonProperty("lastUpdated")
    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @JsonProperty("createdAt")
    public String getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("createdAt")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("supplierBankDetails")
    public List<SupplierBankDetail> getSupplierBankDetails() {
        return supplierBankDetails;
    }

    @JsonProperty("supplierBankDetails")
    public void setSupplierBankDetails(List<SupplierBankDetail> supplierBankDetails) {
        this.supplierBankDetails = supplierBankDetails;
    }

    @JsonProperty("nonCommissionableCommercials")
    public NonCommissionableCommercials getNonCommissionableCommercials() {
        return nonCommissionableCommercials;
    }

    @JsonProperty("nonCommissionableCommercials")
    public void setNonCommissionableCommercials(NonCommissionableCommercials nonCommissionableCommercials) {
        this.nonCommissionableCommercials = nonCommissionableCommercials;
    }

    @JsonProperty("commissionableCommercials")
    public CommissionableCommercials getCommissionableCommercials() {
        return commissionableCommercials;
    }

    @JsonProperty("commissionableCommercials")
    public void setCommissionableCommercials(CommissionableCommercials commissionableCommercials) {
        this.commissionableCommercials = commissionableCommercials;
    }

    @JsonProperty("receivableCommercials")
    public ReceivableCommercials getReceivableCommercials() {
        return receivableCommercials;
    }

    @JsonProperty("receivableCommercials")
    public void setReceivableCommercials(ReceivableCommercials receivableCommercials) {
        this.receivableCommercials = receivableCommercials;
    }

    @JsonProperty("payableCommercials")
    public PayableCommercials getPayableCommercials() {
        return payableCommercials;
    }

    @JsonProperty("payableCommercials")
    public void setPayableCommercials(PayableCommercials payableCommercials) {
        this.payableCommercials = payableCommercials;
    }

    @JsonProperty("__v")
    public int getV() {
        return v;
    }

    @JsonProperty("__v")
    public void setV(int v) {
        this.v = v;
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