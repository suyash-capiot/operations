package com.coxandkings.travel.operations.criteria.thirdPartyVoucher;

public class ReportGenerationCriteria {

    private String supplierName;
    private String productCategoryName;
    private String productSubCategoryName;
    private String productName;

    private String voucherCodeStatus;
    private String voucherCodeUsageType;
    private String paymentStatusToReleaseVoucher;
    private String paymentStatusOfBooking;
    private String companyId;
    private Integer pageNo;
    private Integer pageSize;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getProductCategoryName() {
        return productCategoryName;
    }

    public void setProductCategoryName(String productCategoryName) {
        this.productCategoryName = productCategoryName;
    }

    public String getProductSubCategoryName() {
        return productSubCategoryName;
    }

    public void setProductSubCategoryName(String productSubCategoryName) {
        this.productSubCategoryName = productSubCategoryName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getVoucherCodeStatus() {
        return voucherCodeStatus;
    }

    public void setVoucherCodeStatus(String voucherCodeStatus) {
        this.voucherCodeStatus = voucherCodeStatus;
    }

    public String getVoucherCodeUsageType() {
        return voucherCodeUsageType;
    }

    public void setVoucherCodeUsageType(String voucherCodeUsageType) {
        this.voucherCodeUsageType = voucherCodeUsageType;
    }

    public String getPaymentStatusToReleaseVoucher() {
        return paymentStatusToReleaseVoucher;
    }

    public void setPaymentStatusToReleaseVoucher(String paymentStatusToReleaseVoucher) {
        this.paymentStatusToReleaseVoucher = paymentStatusToReleaseVoucher;
    }

    public String getPaymentStatusOfBooking() {
        return paymentStatusOfBooking;
    }

    public void setPaymentStatusOfBooking(String paymentStatusOfBooking) {
        this.paymentStatusOfBooking = paymentStatusOfBooking;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
