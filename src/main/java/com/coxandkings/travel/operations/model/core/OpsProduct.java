package com.coxandkings.travel.operations.model.core;

import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsProduct implements Serializable {

    @JsonProperty("cancelDate")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime cancelDateZDT;

    @JsonProperty("supplierID")
    private String supplierID;

    @JsonProperty("orderID")
    private String orderID;

    @JsonProperty("amendDate")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime amendDateZDT;

    @JsonProperty("lastModifiedBy")
    private String lastModifiedBy;

    @JsonProperty("inventory")
    private String inventory;

    @JsonProperty("enamblerSupplierName")
    private String enamblerSupplierName;

    @JsonProperty("settlementSupplierName")
    private String settlementSupplierName;

    @JsonProperty("settlementSupplierID")
    private String settlementSupplierID;

    @JsonProperty("productCategory")
    private String productCategory;

    private OpsProductCategory opsProductCategory;

    @JsonProperty("orderDetails")
    private OpsOrderDetails orderDetails;

    @JsonProperty("productSubCategory")
    private String productSubCategory;

    private OpsProductSubCategory opsProductSubCategory;

    @JsonProperty("sourceSupplierName")
    private String sourceSupplierName;

    @JsonProperty("sourceSupplierID")
    private String sourceSupplierID;
    
    @JsonProperty("enamblerSupplierID")
    private String enamblerSupplierID;
    
    @JsonProperty("credentialsName")
    private String credentialsName;

    @JsonProperty("createdAt")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime createdAt;

    @JsonProperty("productName")
    private String productName;

    private final static long serialVersionUID = -3407271985194420166L;

    @JsonProperty("fileHandlerUserID")
    private String fileHandlerUserID;

    @JsonProperty("fileHandlerUserName")
    private String fileHandlerUserName;

    @JsonProperty("supplierRefNumber")
    private String supplierRefNumber;

    @JsonProperty("supplierRateType")
    private String supplierRateType;

    @JsonProperty("status")
    private String status;

    @JsonProperty("ticketingPCC")
    private String ticketingPCC;

    @JsonProperty("bookingPCC")
    private String bookingPCC;

    @JsonProperty("pseudoCityCode")
    private String pseudoCityCode;

    private Map<String, Boolean> productLevelActions = new HashMap<>();

    @JsonProperty("roe")
    private BigDecimal roe;

    @JsonProperty("supplierReservationId")
    private String supplierReservationId;

    //TODO: Adding for merge booking
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime cancellationPeriodFrom;
    //TODO : Booking Engine Not yet giving this value.
    @JsonProperty("isAveragePriced")
    private Boolean isAveragePriced;
    @JsonProperty("supplierReferenceId")
    private String supplierReferenceId;
    @JsonProperty("supplierCancellationId")
    private String supplierCancellationId;
    @JsonProperty("clientReferenceId")
    private String clientReferenceId;

    @JsonProperty("actualMarginAmount")
    private BigDecimal actualMarginAmount;

    @JsonProperty("vouchers")
    private List<String> vouchers;

    private String financeControlId;

    private List<OpsAmendDetails> amendmentChargesDetails;

    private List<OpsCancDetails> cancellationChargesDetails;

    @JsonProperty("supplierType")
    private String supplierType;

    public OpsProduct() {
    }

    public String getSupplierType() {
        return supplierType;
    }

    public void setSupplierType(String supplierType) {
        this.supplierType = supplierType;
    }

    public Boolean getAveragePriced() {
        return isAveragePriced;
    }

    public void setAveragePriced(Boolean averagePriced) {
        isAveragePriced = averagePriced;
    }

    public ZonedDateTime getCancellationPeriodFrom() {
        return cancellationPeriodFrom;
    }

    public void setCancellationPeriodFrom(ZonedDateTime cancellationPeriodFrom) {
        this.cancellationPeriodFrom = cancellationPeriodFrom;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public String getEnamblerSupplierName() {
        return enamblerSupplierName;
    }

    public void setEnamblerSupplierName(String enamblerSupplierName) {
        this.enamblerSupplierName = enamblerSupplierName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductSubCategory() {
        return productSubCategory;
    }

    public void setProductSubCategory(String productSubCategory) {
        this.productSubCategory = productSubCategory;
    }

    public String getSourceSupplierName() {
        return sourceSupplierName;
    }

    public void setSourceSupplierName(String sourceSupplierName) {
        this.sourceSupplierName = sourceSupplierName;
    }

    public String getCredentialsName() {
        return credentialsName;
    }

    public void setCredentialsName(String credentialsName) {
        this.credentialsName = credentialsName;
    }

    public OpsOrderDetails getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(OpsOrderDetails orderDetails) {
        this.orderDetails = orderDetails;
    }

    public ZonedDateTime getCancelDateZDT() {
        return cancelDateZDT;
    }

    public void setCancelDateZDT(ZonedDateTime cancelDateZDT) {
        this.cancelDateZDT = cancelDateZDT;
    }

    public ZonedDateTime getAmendDateZDT() {
        return amendDateZDT;
    }

    public void setAmendDateZDT(ZonedDateTime amendDateZDT) {
        this.amendDateZDT = amendDateZDT;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }


    public Map<String, Boolean> getProductLevelActions() {
        return productLevelActions;
    }

    public void setProductLevelActions(Map<String, Boolean> productLevelActions) {
        this.productLevelActions = productLevelActions;
    }

    public String getFileHandlerUserID() {
        return fileHandlerUserID;
    }

    public void setFileHandlerUserID(String fileHandlerUserID) {
        this.fileHandlerUserID = fileHandlerUserID;
    }

    public String getFileHandlerUserName() {
        return fileHandlerUserName;
    }

    public void setFileHandlerUserName(String fileHandlerUserName) {
        this.fileHandlerUserName = fileHandlerUserName;
    }

    public String getSupplierRefNumber() {
        return supplierRefNumber;
    }

    public void setSupplierRefNumber(String supplierRefNumber) {
        this.supplierRefNumber = supplierRefNumber;
    }

    public String getSupplierRateType() {
        return supplierRateType;
    }

    public void setSupplierRateType(String supplierRateType) {
        this.supplierRateType = supplierRateType;
    }

    public BigDecimal getRoe() {
        return roe;
    }

    public void setRoe(BigDecimal roe) {
        this.roe = roe;
    }

    public OpsProductCategory getOpsProductCategory() {
        return opsProductCategory;
    }

    public void setOpsProductCategory(OpsProductCategory opsProductCategory) {
        this.opsProductCategory = opsProductCategory;
    }

    public OpsProductSubCategory getOpsProductSubCategory() {
        return opsProductSubCategory;
    }

    public void setOpsProductSubCategory(OpsProductSubCategory opsProductSubCategory) {
        this.opsProductSubCategory = opsProductSubCategory;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSupplierReservationId() {
        return supplierReservationId;
    }

    public void setSupplierReservationId(String supplierReservationId) {
        this.supplierReservationId = supplierReservationId;
    }

    public String getSupplierReferenceId() {
        return supplierReferenceId;
    }

    public void setSupplierReferenceId(String supplierReferenceId) {
        this.supplierReferenceId = supplierReferenceId;
    }

    public String getSupplierCancellationId() {
        return supplierCancellationId;
    }

    public void setSupplierCancellationId(String supplierCancellationId) {
        this.supplierCancellationId = supplierCancellationId;
    }

    public String getClientReferenceId() {
        return clientReferenceId;
    }

    public void setClientReferenceId(String clientReferenceId) {
        this.clientReferenceId = clientReferenceId;
    }

    public BigDecimal getActualMarginAmount() {
        return actualMarginAmount;
    }

    public void setActualMarginAmount(BigDecimal actualMarginAmount) {
        this.actualMarginAmount = actualMarginAmount;
    }

    public List<OpsAmendDetails> getAmendmentChargesDetails() {
        return amendmentChargesDetails;
    }

    public void setAmendmentChargesDetails(List<OpsAmendDetails> amendmentChargesDetails) {
        this.amendmentChargesDetails = amendmentChargesDetails;
    }

    public List<OpsCancDetails> getCancellationChargesDetails() {
        return cancellationChargesDetails;
    }

    public void setCancellationChargesDetails(List<OpsCancDetails> cancellationChargesDetails) {
        this.cancellationChargesDetails = cancellationChargesDetails;
    }

    public String getTicketingPCC() {
        return ticketingPCC;
    }

    public void setTicketingPCC(String ticketingPCC) {
        this.ticketingPCC = ticketingPCC;
    }

    public String getBookingPCC() {
        return bookingPCC;
    }

    public void setBookingPCC(String bookingPCC) {
        this.bookingPCC = bookingPCC;
    }

    public String getPseudoCityCode() {
        return pseudoCityCode;
    }

    public void setPseudoCityCode(String pseudoCityCode) {
        this.pseudoCityCode = pseudoCityCode;
    }

    public List<String> getVouchers() {
        return vouchers;
    }

    public void setVouchers(List<String> vouchers) {
        this.vouchers = vouchers;
    }

    public String getFinanceControlId() {
        return financeControlId;
    }

    public void setFinanceControlId(String financeControlId) {
        this.financeControlId = financeControlId;
    }

	public String getSourceSupplierID() {
		return sourceSupplierID;
	}

	public void setSourceSupplierID(String sourceSupplierID) {
		this.sourceSupplierID = sourceSupplierID;
	}

	public String getEnamblerSupplierID() {
		return enamblerSupplierID;
	}

	public void setEnamblerSupplierID(String enamblerSupplierID) {
		this.enamblerSupplierID = enamblerSupplierID;
	}

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getSettlementSupplierName() {
        return settlementSupplierName;
    }

    public void setSettlementSupplierName(String settlementSupplierName) {
        this.settlementSupplierName = settlementSupplierName;
    }

    public String getSettlementSupplierID() {
        return settlementSupplierID;
    }

    public void setSettlementSupplierID(String settlementSupplierID) {
        this.settlementSupplierID = settlementSupplierID;
    }
}
