package com.coxandkings.travel.operations.resource.thirdpartyVoucher;

import com.coxandkings.travel.operations.enums.thirdPartyVoucher.VoucherCodeStatus;

import java.time.ZonedDateTime;

public class SearchVouchersToSend {

    private String supplierConfigId;
    private String bookId;
    private String orderId;
    private ZonedDateTime releaseDate;
    private VoucherCodeStatus voucherCodeStatus;

    public String getSupplierConfigId() {
        return supplierConfigId;
    }

    public void setSupplierConfigId(String supplierConfigId) {
        this.supplierConfigId = supplierConfigId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public ZonedDateTime getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(ZonedDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    public VoucherCodeStatus getVoucherCodeStatus() {
        return voucherCodeStatus;
    }

    public void setVoucherCodeStatus(VoucherCodeStatus voucherCodeStatus) {
        this.voucherCodeStatus = voucherCodeStatus;
    }
}
