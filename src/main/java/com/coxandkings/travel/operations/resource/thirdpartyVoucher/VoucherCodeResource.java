package com.coxandkings.travel.operations.resource.thirdpartyVoucher;

import com.coxandkings.travel.operations.enums.thirdPartyVoucher.FileType;
import com.coxandkings.travel.operations.enums.thirdPartyVoucher.VoucherCodeUsageType;

import java.util.Set;

public class VoucherCodeResource {

    private String supplierConfigId;
    private VoucherCodeUsageType voucherCodeUsageType;
    private FileType fileType;
    private Set<String> inputVoucherCodes;
    private String fileName;
    private String fileId;

    public String getSupplierConfigId() {
        return supplierConfigId;
    }

    public void setSupplierConfigId(String supplierConfigId) {
        this.supplierConfigId = supplierConfigId;
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

    public Set<String> getInputVoucherCodes() {
        return inputVoucherCodes;
    }

    public void setInputVoucherCodes(Set<String> inputVoucherCodes) {
        this.inputVoucherCodes = inputVoucherCodes;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}
