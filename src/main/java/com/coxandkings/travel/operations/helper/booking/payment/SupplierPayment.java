package com.coxandkings.travel.operations.helper.booking.payment;



public class SupplierPayment {

    private String productType;
    private String orderId;
    private String supplierId;
    //private String typeOfSettlement;
    private String supplierName;
    private String netPybleToSupplr;
    //private String paymentDueDate;
    //private String currencyCode;
    //private String amount;
    //private String paymentDate;
    //private String paymentReferenceNo;
    //private String paymentAdviceId;
    //private String paymentRefNo;
    //private String status;


    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getNetPybleToSupplr() {
        return netPybleToSupplr;
    }

    public void setNetPybleToSupplr(String netPybleToSupplr) {
        this.netPybleToSupplr = netPybleToSupplr;
    }
}
