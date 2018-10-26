
package com.coxandkings.travel.ext.model.be;

import com.coxandkings.travel.operations.model.core.OpsBookingAttribute;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "lastUpdatedBy",
        "cancelDate",
        "supplierID",
        "orderID",
        "supplierRateType",
        "amendDate",
        "inventory",
        "enamblerSupplierName",
        "productCategory",
        "createdAt",
        "orderDetails",
        "productSubCategory",
        "sourceSupplierName",
        "credentialsName",
        "status",
        "lastModifiedBy"
})
public class Product implements Serializable {

    @JsonProperty("lastUpdatedBy")
    private String lastUpdatedBy;
    @JsonProperty("cancelDate")
    private String cancelDate;
    @JsonProperty("supplierID")
    private String supplierID;
    @JsonProperty("orderID")
    private String orderID;
    @JsonProperty("supplierRateType")
    private String supplierRateType;
    @JsonProperty("amendDate")
    private String amendDate;
    @JsonProperty("inventory")
    private String inventory;
    @JsonProperty("enamblerSupplierName")
    private String enamblerSupplierName;
    @JsonProperty("productCategory")
    private String productCategory;
    @JsonProperty("createdAt")
    private String createdAt;
    @JsonProperty("orderDetails")
    private OrderDetails orderDetails;
    @JsonProperty("productSubCategory")
    private String productSubCategory;
    @JsonProperty("sourceSupplierName")
    private String sourceSupplierName;
    @JsonProperty("credentialsName")
    private String credentialsName;
    @JsonProperty("status")
    private String status;
    @JsonProperty("lastModifiedBy")
    private String lastModifiedBy;
    @JsonProperty("roe")
    private BigDecimal roe;

    @JsonProperty("supplierReservationId")
    private String supplierReservationId;

    @JsonProperty("supplierType")
    private String supplierType;
    @JsonProperty("supplierReferenceId")
    private String supplierReferenceId;
    @JsonProperty("supplierCancellationId")
    private String supplierCancellationId;
    @JsonProperty("clientReferenceId")
    private String clientReferenceId;

    @JsonProperty("ticketNumber")
    private String ticketNumber;

    @JsonProperty("ticketIssueDate")
    private String ticketIssueDate;

    @JsonProperty("clientReconfirmDate")
    private String clientReconfirmDate;

    @JsonProperty("clientreconfirmationStatus")
    private String clientreconfirmationStatus;

    @JsonProperty("suppReconfirmationStatus")
    private String suppReconfirmationStatus;

    @JsonProperty("suppReconfirmDate")
    private String supplierReconfirmDate;

    @JsonProperty("bookingAttribute")
    private List<Map<OpsBookingAttribute, String>> bookingAttribute = new ArrayList<>();


    @JsonProperty("expiryTimeLimit")
    private String expiryTimeLimit;

    @JsonProperty("ticketingPCC")
    private String ticketingPCC;

    @JsonProperty("bookingPCC")
    private String bookingPCC;

    @JsonProperty("accoRefNumber")
    private String accoRefNumber;

    @JsonProperty("pseudoCityCode")
    private String pseudoCityCode;

    @JsonProperty("vouchers")
    private List<String> vouchers;

    @JsonProperty("financeControlId")
    private String financeControlId;


    private final static long serialVersionUID = -1483311632024147663L;

    /**
     * No args constructor for use in serialization
     */
    public Product() {
    }

    public String getAccoRefNumber() {
        return accoRefNumber;
    }

    public void setAccoRefNumber(String accoRefNumber) {
        this.accoRefNumber = accoRefNumber;
    }

    public String getTicketingPCC() {
        return ticketingPCC;
    }

    public String getBookingPCC() {
        return bookingPCC;
    }

    public void setBookingPCC(String bookingPCC) {
        this.bookingPCC = bookingPCC;
    }

    public void setTicketingPCC(String ticketingPCC) {
        this.ticketingPCC = ticketingPCC;
    }

    public String getSupplierType() {
        return supplierType;
    }

    public void setSupplierType(String supplierType) {
        this.supplierType = supplierType;
    }

    @JsonProperty("lastUpdatedBy")
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    @JsonProperty("lastUpdatedBy")
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    @JsonProperty("cancelDate")
    public String getCancelDate() {
        return cancelDate;
    }

    @JsonProperty("cancelDate")
    public void setCancelDate(String cancelDate) {
        this.cancelDate = cancelDate;
    }

    @JsonProperty("supplierID")
    public String getSupplierID() {
        return supplierID;
    }

    @JsonProperty("supplierID")
    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    @JsonProperty("orderID")
    public String getOrderID() {
        return orderID;
    }

    @JsonProperty("orderID")
    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    @JsonProperty("supplierRateType")
    public String getSupplierRateType() {
        return supplierRateType;
    }

    @JsonProperty("supplierRateType")
    public void setSupplierRateType(String supplierRateType) {
        this.supplierRateType = supplierRateType;
    }

    @JsonProperty("amendDate")
    public String getAmendDate() {
        return amendDate;
    }

    @JsonProperty("amendDate")
    public void setAmendDate(String amendDate) {
        this.amendDate = amendDate;
    }

    @JsonProperty("inventory")
    public String getInventory() {
        return inventory;
    }

    @JsonProperty("inventory")
    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    @JsonProperty("enamblerSupplierName")
    public String getEnamblerSupplierName() {
        return enamblerSupplierName;
    }

    @JsonProperty("enamblerSupplierName")
    public void setEnamblerSupplierName(String enamblerSupplierName) {
        this.enamblerSupplierName = enamblerSupplierName;
    }

    @JsonProperty("productCategory")
    public String getProductCategory() {
        return productCategory;
    }

    @JsonProperty("productCategory")
    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    @JsonProperty("createdAt")
    public String getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("createdAt")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("orderDetails")
    public OrderDetails getOrderDetails() {
        return orderDetails;
    }

    @JsonProperty("orderDetails")
    public void setOrderDetails(OrderDetails orderDetails) {
        this.orderDetails = orderDetails;
    }

    @JsonProperty("productSubCategory")
    public String getProductSubCategory() {
        return productSubCategory;
    }

    @JsonProperty("productSubCategory")
    public void setProductSubCategory(String productSubCategory) {
        this.productSubCategory = productSubCategory;
    }

    @JsonProperty("sourceSupplierName")
    public String getSourceSupplierName() {
        return sourceSupplierName;
    }

    @JsonProperty("sourceSupplierName")
    public void setSourceSupplierName(String sourceSupplierName) {
        this.sourceSupplierName = sourceSupplierName;
    }

    @JsonProperty("credentialsName")
    public String getCredentialsName() {
        return credentialsName;
    }

    @JsonProperty("credentialsName")
    public void setCredentialsName(String credentialsName) {
        this.credentialsName = credentialsName;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("lastModifiedBy")
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    @JsonProperty("lastModifiedBy")
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public BigDecimal getRoe() {
        return roe;
    }

    public void setRoe(BigDecimal roe) {
        this.roe = roe;
    }

    public String getSupplierReservationId() {
        return supplierReservationId;
    }

    public void setSupplierReservationId(String supplierReservationId) {
        this.supplierReservationId = supplierReservationId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getTicketIssueDate() {
        return ticketIssueDate;
    }

    public void setTicketIssueDate(String ticketIssueDate) {
        this.ticketIssueDate = ticketIssueDate;
    }

    public String getClientReconfirmDate() {
        return clientReconfirmDate;
    }

    public void setClientReconfirmDate(String clientReconfirmDate) {
        this.clientReconfirmDate = clientReconfirmDate;
    }

    public String getSupplierReconfirmDate() {
        return supplierReconfirmDate;
    }

    public void setSupplierReconfirmDate(String supplierReconfirmDate) {
        this.supplierReconfirmDate = supplierReconfirmDate;
    }

    public List<Map<OpsBookingAttribute, String>> getBookingAttribute() {
        return bookingAttribute;
    }

    public void setBookingAttribute(List<Map<OpsBookingAttribute, String>> bookingAttribute) {
        this.bookingAttribute = bookingAttribute;
    }

    public String getPseudoCityCode() {
        return pseudoCityCode;
    }

    public void setPseudoCityCode(String pseudoCityCode) {
        this.pseudoCityCode = pseudoCityCode;
    }

    public String getExpiryTimeLimit() {
        return expiryTimeLimit;
    }

    public void setExpiryTimeLimit(String expiryTimeLimit) {
        this.expiryTimeLimit = expiryTimeLimit;
    }


    public String getClientreconfirmationStatus() {
        return clientreconfirmationStatus;
    }

    public void setClientreconfirmationStatus(String clientreconfirmationStatus) {
        this.clientreconfirmationStatus = clientreconfirmationStatus;
    }

    public String getSuppReconfirmationStatus() {
        return suppReconfirmationStatus;
    }

    public void setSuppReconfirmationStatus(String suppReconfirmationStatus) {
        this.suppReconfirmationStatus = suppReconfirmationStatus;
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
}
