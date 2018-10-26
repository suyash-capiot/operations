package com.coxandkings.travel.operations.response.thirdpartyvouchers;

import com.coxandkings.travel.operations.enums.thirdPartyVoucher.PaymentStatusToReleaseVoucher;
import com.coxandkings.travel.operations.enums.thirdPartyVoucher.VoucherCodeStatus;
import com.coxandkings.travel.operations.enums.thirdPartyVoucher.VoucherCodeUsageType;

public class ThirdPartyVouchersReportResponse {

    private String supplier;
    private String ProductName;
    private String productCategory;
    private String productCategorySubtype;
    private VoucherCodeUsageType voucherCodeUsageType;
    private VoucherCodeStatus status;
    private String paymentStatusOfBooking;
    private PaymentStatusToReleaseVoucher voucherToBeAppliedOn;
    private String companyId;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductCategorySubtype() {
        return productCategorySubtype;
    }

    public void setProductCategorySubtype(String productCategorySubtype) {
        this.productCategorySubtype = productCategorySubtype;
    }

    public VoucherCodeUsageType getVoucherCodeUsageType() {
        return voucherCodeUsageType;
    }

    public void setVoucherCodeUsageType(VoucherCodeUsageType voucherCodeUsageType) {
        this.voucherCodeUsageType = voucherCodeUsageType;
    }

    public VoucherCodeStatus getStatus() {
        return status;
    }

    public void setStatus(VoucherCodeStatus status) {
        this.status = status;
    }

    public String getPaymentStatusOfBooking() {
        return paymentStatusOfBooking;
    }

    public void setPaymentStatusOfBooking(String paymentStatusOfBooking) {
        this.paymentStatusOfBooking = paymentStatusOfBooking;
    }

    public PaymentStatusToReleaseVoucher getVoucherToBeAppliedOn() {
        return voucherToBeAppliedOn;
    }

    public void setVoucherToBeAppliedOn(PaymentStatusToReleaseVoucher voucherToBeAppliedOn) {
        this.voucherToBeAppliedOn = voucherToBeAppliedOn;
    }
}
