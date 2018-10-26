package com.coxandkings.travel.operations.resource.forex;

import com.coxandkings.travel.operations.enums.forex.IndentStatus;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.coxandkings.travel.operations.model.forex.ForexBuyingCurrency;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.ZonedDateTime;
import java.util.Set;

public class ForexIndentResource {

    private String id;
    private String createdByUserId;
    private String lastModifiedByUserId;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime createdAtTime;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime lastModifiedAtTime;

    private String indentFor;
    private String supplierName;
    private String supplierId;
    private String paymentType;
    private IndentStatus indentStatus;
    private Boolean isSentToSupplier;

    private String forexBooking;
    private Set<ForexBuyingCurrency> forexBuyingCcyDetails;

    private String approverRemark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public String getLastModifiedByUserId() {
        return lastModifiedByUserId;
    }

    public void setLastModifiedByUserId(String lastModifiedByUserId) {
        this.lastModifiedByUserId = lastModifiedByUserId;
    }

    public ZonedDateTime getCreatedAtTime() {
        return createdAtTime;
    }

    public void setCreatedAtTime(ZonedDateTime createdAtTime) {
        this.createdAtTime = createdAtTime;
    }

    public ZonedDateTime getLastModifiedAtTime() {
        return lastModifiedAtTime;
    }

    public void setLastModifiedAtTime(ZonedDateTime lastModifiedAtTime) {
        this.lastModifiedAtTime = lastModifiedAtTime;
    }

    public String getForexBooking() {
        return forexBooking;
    }

    public void setForexBooking(String forexBooking) {
        this.forexBooking = forexBooking;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Set<ForexBuyingCurrency> getForexBuyingCcyDetails() {
        return forexBuyingCcyDetails;
    }

    public void setForexBuyingCcyDetails(Set<ForexBuyingCurrency> forexBuyingCcyDetails) {
        this.forexBuyingCcyDetails = forexBuyingCcyDetails;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public IndentStatus getIndentStatus() {
        return indentStatus;
    }

    public void setIndentStatus(IndentStatus indentStatus) {
        this.indentStatus = indentStatus;
    }

    public String getApproverRemark() {
        return approverRemark;
    }

    public void setApproverRemark(String approverRemark) {
        this.approverRemark = approverRemark;
    }

    public String getIndentFor() {
        return indentFor;
    }

    public void setIndentFor(String indentFor) {
        this.indentFor = indentFor;
    }

    public Boolean getSentToSupplier() {
        return isSentToSupplier;
    }

    public void setSentToSupplier(Boolean sentToSupplier) {
        isSentToSupplier = sentToSupplier;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }
}
