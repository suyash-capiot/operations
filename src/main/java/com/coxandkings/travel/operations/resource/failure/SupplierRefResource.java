package com.coxandkings.travel.operations.resource.failure;

public class SupplierRefResource {
    private String orderID;
    private String userID;
    private String supplierReferenceId;
    private String supplierReservationId;
    private String clientReferenceId;
    private String supplierCancellationId;

    public SupplierRefResource() {
        this.supplierReferenceId = "";
        this.supplierReservationId = "";
        this.clientReferenceId = "";
        this.supplierCancellationId = "";
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getSupplierReferenceId() {
        return supplierReferenceId;
    }

    public void setSupplierReferenceId(String supplierReferenceId) {
        this.supplierReferenceId = supplierReferenceId;
    }

    public String getSupplierReservationId() {
        return supplierReservationId;
    }

    public void setSupplierReservationId(String supplierReservationId) {
        this.supplierReservationId = supplierReservationId;
    }

    public String getClientReferenceId() {
        return clientReferenceId;
    }

    public void setClientReferenceId(String clientReferenceId) {
        this.clientReferenceId = clientReferenceId;
    }

    public String getSupplierCancellationId() {
        return supplierCancellationId;
    }

    public void setSupplierCancellationId(String supplierCancellationId) {
        this.supplierCancellationId = supplierCancellationId;
    }
}
