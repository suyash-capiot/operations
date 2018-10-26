package com.coxandkings.travel.operations.model.amendmentandpartialcancellation;

import com.coxandkings.travel.operations.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "amend_partcanc_charges")
public class AmendAndPartCanc extends BaseModel implements Serializable {

    private static final long serialVersionUID = 9091700954899695964L;

    @Column(name = "user_id", nullable = false)
    private String userID;

    @Column(name = "client_id", nullable = false)
    private String clientID;

    @Column(name = "booking_ref_id", nullable = false)
    private String bookingRefNo;

    @Column(name = "order_id", nullable = false)
    private String orderID;

    @Column(name = "supplier_id", nullable = false)
    private String supplierID;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_category", nullable = false)
    private String productCategory;

    @Column(name = "product_sub_category", nullable = false)
    private String productSubCategory;

    @Column(name = "action_type", nullable = false)
    private String actionType;

    @Column(name = "unique_id", nullable = false)
    private String uniqueID;

    @Column(name = "old_value", nullable = false)
    private Double oldValue;

    @Column(name = "new_value", nullable = false)
    private Double newValue;

    @Column(name = "approval_user_status", nullable = false)
    private String approvalUserStatus;

    @Column(name = "remark")
    private String remark;

    @Column(name = "task_ref_id")
    private String taskRefID;

    @Column(name="amend_canc_id")
    private String amendCancID;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getBookingRefNo() {
        return bookingRefNo;
    }

    public void setBookingRefNo(String bookingRefNo) {
        this.bookingRefNo = bookingRefNo;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public Double getOldValue() {
        return oldValue;
    }

    public void setOldValue(Double oldValue) {
        this.oldValue = oldValue;
    }

    public Double getNewValue() {
        return newValue;
    }

    public void setNewValue(Double newValue) {
        this.newValue = newValue;
    }

    public String getApprovalUserStatus() {
        return approvalUserStatus;
    }

    public void setApprovalUserStatus(String approvalUserStatus) {
        this.approvalUserStatus = approvalUserStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTaskRefID() {
        return taskRefID;
    }

    public void setTaskRefID(String taskRefID) {
        this.taskRefID = taskRefID;
    }

    public String getAmendCancID() {
        return amendCancID;
    }

    public void setAmendCancID(String amendCancID) {
        this.amendCancID = amendCancID;
    }
}
