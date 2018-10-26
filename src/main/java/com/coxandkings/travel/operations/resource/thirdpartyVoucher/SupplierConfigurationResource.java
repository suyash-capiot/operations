package com.coxandkings.travel.operations.resource.thirdpartyVoucher;

import com.coxandkings.travel.operations.enums.thirdPartyVoucher.PaymentStatusToReleaseVoucher;
import com.coxandkings.travel.operations.enums.thirdPartyVoucher.UnitOfMeasurement;
import com.coxandkings.travel.operations.enums.thirdPartyVoucher.VoucherCodeUsageType;
import com.coxandkings.travel.operations.enums.thirdPartyVoucher.VoucherToBeAppliedOn;
import com.coxandkings.travel.operations.model.thirdPartyVouchers.CommunicationTemplateOfCustomer;
import com.coxandkings.travel.operations.model.thirdPartyVouchers.CommunicationTemplateOfSupplier;
import com.coxandkings.travel.operations.resource.BaseResource;

import java.util.Set;

public class SupplierConfigurationResource extends BaseResource {

    private String id;
    private String supplierId;
    private String supplierName;
    private String productName;
    private String productCategoryName;
    private String productSubCategoryName;

    private VoucherToBeAppliedOn voucherToBeAppliedOn;

    private VoucherCodeUsageType voucherCodeUsageType;

    private PaymentStatusToReleaseVoucher paymentStatusToReleaseVoucher;

    private UnitOfMeasurement unitOfMeasurement;
    private Integer noOfDaysToReleaseVoucher;
    private Integer noOfDaysToSendAlarm;
    private Integer multiplier;
    private String customerTemplateId;
    private String supplierTemplateId;
    private Set<VoucherCodeResource> voucherCodes;
    private String companyId;

    private String remarks;

    public String getRemarks() {
        return remarks;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public VoucherToBeAppliedOn getVoucherToBeAppliedOn() {
        return voucherToBeAppliedOn;
    }

    public void setVoucherToBeAppliedOn(VoucherToBeAppliedOn voucherToBeAppliedOn) {
        this.voucherToBeAppliedOn = voucherToBeAppliedOn;
    }

    public VoucherCodeUsageType getVoucherCodeUsageType() {
        return voucherCodeUsageType;
    }

    public void setVoucherCodeUsageType(VoucherCodeUsageType voucherCodeUsageType) {
        this.voucherCodeUsageType = voucherCodeUsageType;
    }

    public PaymentStatusToReleaseVoucher getPaymentStatusToReleaseVoucher() {
        return paymentStatusToReleaseVoucher;
    }

    public void setPaymentStatusToReleaseVoucher(PaymentStatusToReleaseVoucher paymentStatusToReleaseVoucher) {
        this.paymentStatusToReleaseVoucher = paymentStatusToReleaseVoucher;
    }

    public UnitOfMeasurement getUnitOfMeasurement() {
        return unitOfMeasurement;
    }

    public void setUnitOfMeasurement(UnitOfMeasurement unitOfMeasurement) {
        this.unitOfMeasurement = unitOfMeasurement;
    }

    public Integer getNoOfDaysToReleaseVoucher() {
        return noOfDaysToReleaseVoucher;
    }

    public void setNoOfDaysToReleaseVoucher(Integer noOfDaysToReleaseVoucher) {
        this.noOfDaysToReleaseVoucher = noOfDaysToReleaseVoucher;
    }

    public Integer getNoOfDaysToSendAlarm() {
        return noOfDaysToSendAlarm;
    }

    public void setNoOfDaysToSendAlarm(Integer noOfDaysToSendAlarm) {
        this.noOfDaysToSendAlarm = noOfDaysToSendAlarm;
    }

    public Integer getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(Integer multiplier) {
        this.multiplier = multiplier;
    }

    public String getCustomerTemplateId() {
        return customerTemplateId;
    }

    public void setCustomerTemplateId(String customerTemplateId) {
        this.customerTemplateId = customerTemplateId;
    }

    public String getSupplierTemplateId() {
        return supplierTemplateId;
    }

    public void setSupplierTemplateId(String supplierTemplateId) {
        this.supplierTemplateId = supplierTemplateId;
    }

    public Set<VoucherCodeResource> getVoucherCodes() {
        return voucherCodes;
    }

    public void setVoucherCodes(Set<VoucherCodeResource> voucherCodes) {
        this.voucherCodes = voucherCodes;
    }
}
