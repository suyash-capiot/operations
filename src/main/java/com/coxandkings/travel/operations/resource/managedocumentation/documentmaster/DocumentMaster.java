package com.coxandkings.travel.operations.resource.managedocumentation.documentmaster;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "_id",
    "companyName",
    "cdhmId",
    "cdhmCompanyId",
    "copiedFrom",
    "companyMarket",
    "cdhmEntityType",
    "cdhmEntityName",
    "clientId",
    "entityId",
    "tierId",
    "pos",
    "nationality",
    "productAccommodation",
    "productFlight",
    "productHolidays",
    "productCruise",
    "productTransportation",
    "productActivities",
    "productVisa",
    "productInsurance",
    "supplierDetails",
    "documentHandlingGrid",
    "status",
    "effectiveFrom",
    "reason",
    "deleted",
    "createdBy",
    "createdAt",
    "lastUpdatedBy",
    "lastUpdated",
    "lock"
})
public class DocumentMaster {

    @JsonProperty("_id")
    private String id;
    @JsonProperty("companyName")
    private String companyName;
    @JsonProperty("cdhmId")
    private String cdhmId;
    @JsonProperty("cdhmCompanyId")
    private String cdhmCompanyId;
    @JsonProperty("copiedFrom")
    private String copiedFrom;
    @JsonProperty("companyMarket")
    private String companyMarket;
    @JsonProperty("cdhmEntityType")
    private String cdhmEntityType;
    @JsonProperty("cdhmEntityName")
    private String cdhmEntityName;
    @JsonProperty("clientId")
    private String clientId;
    @JsonProperty("entityId")
    private String entityId;
    @JsonProperty("tierId")
    private String tierId;
    @JsonProperty("pos")
    private List<String> pos = null;
    @JsonProperty("nationality")
    private List<String> nationality = null;
    @JsonProperty("productAccommodation")
    private List<ProductAccommodation> productAccommodation = null;
    @JsonProperty("productFlight")
    private List<ProductFlight> productFlight = null;
    @JsonProperty("productHolidays")
    private List<ProductHoliday> productHolidays = null;
    @JsonProperty("productCruise")
    private List<ProductCruise> productCruise = null;
    @JsonProperty("productTransportation")
    private List<ProductTransportation> productTransportation = null;
    @JsonProperty("productActivities")
    private List<ProductActivity> productActivities = null;
    @JsonProperty("productVisa")
    private List<ProductVisa> productVisa = null;
    @JsonProperty("productInsurance")
    private List<ProductInsurance> productInsurance = null;
    @JsonProperty("supplierDetails")
    private List<SupplierDetail> supplierDetails = null;
    @JsonProperty("documentHandlingGrid")
    private List<DocumentHandlingGrid> documentHandlingGrid = null;
    @JsonProperty("status")
    private String status;
    @JsonProperty("effectiveFrom")
    private String effectiveFrom;
    @JsonProperty("reason")
    private String reason;
    @JsonProperty("deleted")
    private Boolean deleted;
    @JsonProperty("createdBy")
    private String createdBy;
    @JsonProperty("createdAt")
    private String createdAt;
    @JsonProperty("lastUpdatedBy")
    private String lastUpdatedBy;
    @JsonProperty("lastUpdated")
    private String lastUpdated;
    @JsonProperty("lock")
    private Lock lock;
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

    @JsonProperty("companyName")
    public String getCompanyName() {
        return companyName;
    }

    @JsonProperty("companyName")
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @JsonProperty("cdhmId")
    public String getCdhmId() {
        return cdhmId;
    }

    @JsonProperty("cdhmId")
    public void setCdhmId(String cdhmId) {
        this.cdhmId = cdhmId;
    }

    @JsonProperty("cdhmCompanyId")
    public String getCdhmCompanyId() {
        return cdhmCompanyId;
    }

    @JsonProperty("cdhmCompanyId")
    public void setCdhmCompanyId(String cdhmCompanyId) {
        this.cdhmCompanyId = cdhmCompanyId;
    }

    @JsonProperty("copiedFrom")
    public String getCopiedFrom() {
        return copiedFrom;
    }

    @JsonProperty("copiedFrom")
    public void setCopiedFrom(String copiedFrom) {
        this.copiedFrom = copiedFrom;
    }

    @JsonProperty("companyMarket")
    public String getCompanyMarket() {
        return companyMarket;
    }

    @JsonProperty("companyMarket")
    public void setCompanyMarket(String companyMarket) {
        this.companyMarket = companyMarket;
    }

    @JsonProperty("cdhmEntityType")
    public String getCdhmEntityType() {
        return cdhmEntityType;
    }

    @JsonProperty("cdhmEntityType")
    public void setCdhmEntityType(String cdhmEntityType) {
        this.cdhmEntityType = cdhmEntityType;
    }

    @JsonProperty("cdhmEntityName")
    public String getCdhmEntityName() {
        return cdhmEntityName;
    }

    @JsonProperty("cdhmEntityName")
    public void setCdhmEntityName(String cdhmEntityName) {
        this.cdhmEntityName = cdhmEntityName;
    }

    @JsonProperty("clientId")
    public String getClientId() {
        return clientId;
    }

    @JsonProperty("clientId")
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @JsonProperty("entityId")
    public String getEntityId() {
        return entityId;
    }

    @JsonProperty("entityId")
    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    @JsonProperty("tierId")
    public String getTierId() {
        return tierId;
    }

    @JsonProperty("tierId")
    public void setTierId(String tierId) {
        this.tierId = tierId;
    }

    @JsonProperty("pos")
    public List<String> getPos() {
        return pos;
    }

    @JsonProperty("pos")
    public void setPos(List<String> pos) {
        this.pos = pos;
    }

    @JsonProperty("nationality")
    public List<String> getNationality() {
        return nationality;
    }

    @JsonProperty("nationality")
    public void setNationality(List<String> nationality) {
        this.nationality = nationality;
    }

    @JsonProperty("productAccommodation")
    public List<ProductAccommodation> getProductAccommodation() {
        return productAccommodation;
    }

    @JsonProperty("productAccommodation")
    public void setProductAccommodation(List<ProductAccommodation> productAccommodation) {
        this.productAccommodation = productAccommodation;
    }

    @JsonProperty("productFlight")
    public List<ProductFlight> getProductFlight() {
        return productFlight;
    }

    @JsonProperty("productFlight")
    public void setProductFlight(List<ProductFlight> productFlight) {
        this.productFlight = productFlight;
    }

    @JsonProperty("productHolidays")
    public List<ProductHoliday> getProductHolidays() {
        return productHolidays;
    }

    @JsonProperty("productHolidays")
    public void setProductHolidays(List<ProductHoliday> productHolidays) {
        this.productHolidays = productHolidays;
    }

    @JsonProperty("productCruise")
    public List<ProductCruise> getProductCruise() {
        return productCruise;
    }

    @JsonProperty("productCruise")
    public void setProductCruise(List<ProductCruise> productCruise) {
        this.productCruise = productCruise;
    }

    @JsonProperty("productTransportation")
    public List<ProductTransportation> getProductTransportation() {
        return productTransportation;
    }

    @JsonProperty("productTransportation")
    public void setProductTransportation(List<ProductTransportation> productTransportation) {
        this.productTransportation = productTransportation;
    }

    @JsonProperty("productActivities")
    public List<ProductActivity> getProductActivities() {
        return productActivities;
    }

    @JsonProperty("productActivities")
    public void setProductActivities(List<ProductActivity> productActivities) {
        this.productActivities = productActivities;
    }

    @JsonProperty("productVisa")
    public List<ProductVisa> getProductVisa() {
        return productVisa;
    }

    @JsonProperty("productVisa")
    public void setProductVisa(List<ProductVisa> productVisa) {
        this.productVisa = productVisa;
    }

    @JsonProperty("productInsurance")
    public List<ProductInsurance> getProductInsurance() {
        return productInsurance;
    }

    @JsonProperty("productInsurance")
    public void setProductInsurance(List<ProductInsurance> productInsurance) {
        this.productInsurance = productInsurance;
    }

    @JsonProperty("supplierDetails")
    public List<SupplierDetail> getSupplierDetails() {
        return supplierDetails;
    }

    @JsonProperty("supplierDetails")
    public void setSupplierDetails(List<SupplierDetail> supplierDetails) {
        this.supplierDetails = supplierDetails;
    }

    @JsonProperty("documentHandlingGrid")
    public List<DocumentHandlingGrid> getDocumentHandlingGrid() {
        return documentHandlingGrid;
    }

    @JsonProperty("documentHandlingGrid")
    public void setDocumentHandlingGrid(List<DocumentHandlingGrid> documentHandlingGrid) {
        this.documentHandlingGrid = documentHandlingGrid;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("effectiveFrom")
    public String getEffectiveFrom() {
        return effectiveFrom;
    }

    @JsonProperty("effectiveFrom")
    public void setEffectiveFrom(String effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    @JsonProperty("reason")
    public String getReason() {
        return reason;
    }

    @JsonProperty("reason")
    public void setReason(String reason) {
        this.reason = reason;
    }

    @JsonProperty("deleted")
    public Boolean getDeleted() {
        return deleted;
    }

    @JsonProperty("deleted")
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @JsonProperty("createdBy")
    public String getCreatedBy() {
        return createdBy;
    }

    @JsonProperty("createdBy")
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @JsonProperty("createdAt")
    public String getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("createdAt")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("lastUpdatedBy")
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    @JsonProperty("lastUpdatedBy")
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    @JsonProperty("lastUpdated")
    public String getLastUpdated() {
        return lastUpdated;
    }

    @JsonProperty("lastUpdated")
    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @JsonProperty("lock")
    public Lock getLock() {
        return lock;
    }

    @JsonProperty("lock")
    public void setLock(Lock lock) {
        this.lock = lock;
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
