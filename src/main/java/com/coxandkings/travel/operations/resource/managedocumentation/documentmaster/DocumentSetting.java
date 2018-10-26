package com.coxandkings.travel.operations.resource.managedocumentation.documentmaster;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "salesStage",
    "documentType",
    "documentName",
    "documentMandatory",
    "handoverDocumentMandatory",
    "documentBy",
    "documentsAsPer",
    "documentFormat",
    "modeOfComm",
    "cutOf",
    "restrictionForAutogen",
    "uploadDetails"
})
public class DocumentSetting {

    @JsonProperty("_id")
    private String id;
    @JsonProperty("salesStage")
    private String salesStage;
    @JsonProperty("documentType")
    private String documentType;
    @JsonProperty("documentName")
    private String documentName;
    @JsonProperty("documentMandatory")
    private Boolean documentMandatory;
    @JsonProperty("handoverDocumentMandatory")
    private Boolean handoverDocumentMandatory;
    @JsonProperty("documentBy")
    private String documentBy;
    @JsonProperty("documentsAsPer")
    private String documentsAsPer;
    @JsonProperty("documentFormat")
    private String documentFormat;
    @JsonProperty("modeOfComm")
    private List<String> modeOfComm = null;
    @JsonProperty("cutOf")
    private CutOf cutOf;
    @JsonProperty("restrictionForAutogen")
    private RestrictionForAutogen restrictionForAutogen;
    @JsonProperty("uploadDetails")
    private List<UploadDetail> uploadDetails = null;
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

    @JsonProperty("salesStage")
    public String getSalesStage() {
        return salesStage;
    }

    @JsonProperty("salesStage")
    public void setSalesStage(String salesStage) {
        this.salesStage = salesStage;
    }

    @JsonProperty("documentType")
    public String getDocumentType() {
        return documentType;
    }

    @JsonProperty("documentType")
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    @JsonProperty("documentName")
    public String getDocumentName() {
        return documentName;
    }

    @JsonProperty("documentName")
    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    @JsonProperty("documentMandatory")
    public Boolean getDocumentMandatory() {
        return documentMandatory;
    }

    @JsonProperty("documentMandatory")
    public void setDocumentMandatory(Boolean documentMandatory) {
        this.documentMandatory = documentMandatory;
    }

    @JsonProperty("handoverDocumentMandatory")
    public Boolean getHandoverDocumentMandatory() {
        return handoverDocumentMandatory;
    }

    @JsonProperty("handoverDocumentMandatory")
    public void setHandoverDocumentMandatory(Boolean handoverDocumentMandatory) {
        this.handoverDocumentMandatory = handoverDocumentMandatory;
    }

    @JsonProperty("documentBy")
    public String getDocumentBy() {
        return documentBy;
    }

    @JsonProperty("documentBy")
    public void setDocumentBy(String documentBy) {
        this.documentBy = documentBy;
    }

    @JsonProperty("documentsAsPer")
    public String getDocumentsAsPer() {
        return documentsAsPer;
    }

    @JsonProperty("documentsAsPer")
    public void setDocumentsAsPer(String documentsAsPer) {
        this.documentsAsPer = documentsAsPer;
    }

    @JsonProperty("documentFormat")
    public String getDocumentFormat() {
        return documentFormat;
    }

    @JsonProperty("documentFormat")
    public void setDocumentFormat(String documentFormat) {
        this.documentFormat = documentFormat;
    }

    @JsonProperty("modeOfComm")
    public List<String> getModeOfComm() {
        return modeOfComm;
    }

    @JsonProperty("modeOfComm")
    public void setModeOfComm(List<String> modeOfComm) {
        this.modeOfComm = modeOfComm;
    }

    @JsonProperty("cutOf")
    public CutOf getCutOf() {
        return cutOf;
    }

    @JsonProperty("cutOf")
    public void setCutOf(CutOf cutOf) {
        this.cutOf = cutOf;
    }

    @JsonProperty("restrictionForAutogen")
    public RestrictionForAutogen getRestrictionForAutogen() {
        return restrictionForAutogen;
    }

    @JsonProperty("restrictionForAutogen")
    public void setRestrictionForAutogen(RestrictionForAutogen restrictionForAutogen) {
        this.restrictionForAutogen = restrictionForAutogen;
    }

    @JsonProperty("uploadDetails")
    public List<UploadDetail> getUploadDetails() {
        return uploadDetails;
    }

    @JsonProperty("uploadDetails")
    public void setUploadDetails(List<UploadDetail> uploadDetails) {
        this.uploadDetails = uploadDetails;
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
