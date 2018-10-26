package com.coxandkings.travel.operations.response.thirdpartyvouchers;

import com.coxandkings.travel.operations.enums.thirdPartyVoucher.FileType;
import com.coxandkings.travel.operations.enums.thirdPartyVoucher.VoucherCodeStatus;
import com.coxandkings.travel.operations.enums.thirdPartyVoucher.VoucherCodeUsageType;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.coxandkings.travel.operations.utils.supplierBillPassing.ZonedDateSerializer;
import com.coxandkings.travel.operations.utils.supplierBillPassing.ZonedDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.ZonedDateTime;

public class VoucherCodeResponse {

    private String voucherCode;
    private VoucherCodeStatus voucherCodeStatus;
    private ZonedDateTime dateOfUpload;
    private VoucherCodeUsageType voucherCodeUsageType;
    private FileType fileType;
    private String flieId;
    private String supplierConfigId;

    public String getSupplierConfigId() {
        return supplierConfigId;
    }

    public void setSupplierConfigId(String supplierConfigId) {
        this.supplierConfigId = supplierConfigId;
    }

    public String getFlieId() {
        return flieId;
    }

    public void setFlieId(String flieId) {
        this.flieId = flieId;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public VoucherCodeStatus getVoucherCodeStatus() {
        return voucherCodeStatus;
    }

    public void setVoucherCodeStatus(VoucherCodeStatus voucherCodeStatus) {
        this.voucherCodeStatus = voucherCodeStatus;
    }

    public ZonedDateTime getDateOfUpload() {
        return dateOfUpload;
    }

    public void setDateOfUpload(ZonedDateTime dateOfUpload) {
        this.dateOfUpload = dateOfUpload;
    }

    public VoucherCodeUsageType getVoucherCodeUsageType() {
        return voucherCodeUsageType;
    }

    public void setVoucherCodeUsageType(VoucherCodeUsageType voucherCodeUsageType) {
        this.voucherCodeUsageType = voucherCodeUsageType;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }
}
