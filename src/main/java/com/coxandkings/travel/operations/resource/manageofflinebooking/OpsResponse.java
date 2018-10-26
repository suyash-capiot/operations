package com.coxandkings.travel.operations.resource.manageofflinebooking;

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
    "opsProductCategory",
    "opsProductSubCategory",
    "productLevelActions",
    "amendmentChargesDetails",
    "cancellationChargesDetails",
    "averagePriced",
    "supplierID",
    "orderID",
    "inventory",
    "productCategory",
    "orderDetails",
    "productSubCategory",
    "fileHandlerUserID",
    "fileHandlerUserName",
    "roe",
    "isAveragePriced",
    "actualMarginAmount",
    "checked",
    "bookingId"
})
public class OpsResponse {

    @JsonProperty("opsProductCategory")
    private String opsProductCategory;
    @JsonProperty("opsProductSubCategory")
    private String opsProductSubCategory;
    @JsonProperty("productLevelActions")
    private ProductLevelActions productLevelActions;
    @JsonProperty("amendmentChargesDetails")
    private List<Object> amendmentChargesDetails = null;
    @JsonProperty("cancellationChargesDetails")
    private List<Object> cancellationChargesDetails = null;
    @JsonProperty("averagePriced")
    private Boolean averagePriced;
    @JsonProperty("supplierID")
    private String supplierID;
    @JsonProperty("orderID")
    private String orderID;
    @JsonProperty("inventory")
    private String inventory;
    @JsonProperty("productCategory")
    private String productCategory;
    @JsonProperty("orderDetails")
    private OrderDetails orderDetails;
    @JsonProperty("productSubCategory")
    private String productSubCategory;
    @JsonProperty("fileHandlerUserID")
    private String fileHandlerUserID;
    @JsonProperty("fileHandlerUserName")
    private String fileHandlerUserName;
    @JsonProperty("roe")
    private Integer roe;
    @JsonProperty("isAveragePriced")
    private Boolean isAveragePriced;
    @JsonProperty("actualMarginAmount")
    private Integer actualMarginAmount;
    @JsonProperty("checked")
    private Boolean checked;
    @JsonProperty("bookingId")
    private String bookingId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("opsProductCategory")
    public String getOpsProductCategory() {
        return opsProductCategory;
    }

    @JsonProperty("opsProductCategory")
    public void setOpsProductCategory(String opsProductCategory) {
        this.opsProductCategory = opsProductCategory;
    }

    @JsonProperty("opsProductSubCategory")
    public String getOpsProductSubCategory() {
        return opsProductSubCategory;
    }

    @JsonProperty("opsProductSubCategory")
    public void setOpsProductSubCategory(String opsProductSubCategory) {
        this.opsProductSubCategory = opsProductSubCategory;
    }

    @JsonProperty("productLevelActions")
    public ProductLevelActions getProductLevelActions() {
        return productLevelActions;
    }

    @JsonProperty("productLevelActions")
    public void setProductLevelActions(ProductLevelActions productLevelActions) {
        this.productLevelActions = productLevelActions;
    }

    @JsonProperty("amendmentChargesDetails")
    public List<Object> getAmendmentChargesDetails() {
        return amendmentChargesDetails;
    }

    @JsonProperty("amendmentChargesDetails")
    public void setAmendmentChargesDetails(List<Object> amendmentChargesDetails) {
        this.amendmentChargesDetails = amendmentChargesDetails;
    }

    @JsonProperty("cancellationChargesDetails")
    public List<Object> getCancellationChargesDetails() {
        return cancellationChargesDetails;
    }

    @JsonProperty("cancellationChargesDetails")
    public void setCancellationChargesDetails(List<Object> cancellationChargesDetails) {
        this.cancellationChargesDetails = cancellationChargesDetails;
    }

    @JsonProperty("averagePriced")
    public Boolean getAveragePriced() {
        return averagePriced;
    }

    @JsonProperty("averagePriced")
    public void setAveragePriced(Boolean averagePriced) {
        this.averagePriced = averagePriced;
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

    @JsonProperty("inventory")
    public String getInventory() {
        return inventory;
    }

    @JsonProperty("inventory")
    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    @JsonProperty("productCategory")
    public String getProductCategory() {
        return productCategory;
    }

    @JsonProperty("productCategory")
    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
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

    @JsonProperty("fileHandlerUserID")
    public String getFileHandlerUserID() {
        return fileHandlerUserID;
    }

    @JsonProperty("fileHandlerUserID")
    public void setFileHandlerUserID(String fileHandlerUserID) {
        this.fileHandlerUserID = fileHandlerUserID;
    }

    @JsonProperty("fileHandlerUserName")
    public String getFileHandlerUserName() {
        return fileHandlerUserName;
    }

    @JsonProperty("fileHandlerUserName")
    public void setFileHandlerUserName(String fileHandlerUserName) {
        this.fileHandlerUserName = fileHandlerUserName;
    }

    @JsonProperty("roe")
    public Integer getRoe() {
        return roe;
    }

    @JsonProperty("roe")
    public void setRoe(Integer roe) {
        this.roe = roe;
    }

    @JsonProperty("isAveragePriced")
    public Boolean getIsAveragePriced() {
        return isAveragePriced;
    }

    @JsonProperty("isAveragePriced")
    public void setIsAveragePriced(Boolean isAveragePriced) {
        this.isAveragePriced = isAveragePriced;
    }

    @JsonProperty("actualMarginAmount")
    public Integer getActualMarginAmount() {
        return actualMarginAmount;
    }

    @JsonProperty("actualMarginAmount")
    public void setActualMarginAmount(Integer actualMarginAmount) {
        this.actualMarginAmount = actualMarginAmount;
    }

    @JsonProperty("checked")
    public Boolean getChecked() {
        return checked;
    }

    @JsonProperty("checked")
    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    @JsonProperty("bookingId")
    public String getBookingId() {
        return bookingId;
    }

    @JsonProperty("bookingId")
    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
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