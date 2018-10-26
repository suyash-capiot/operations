package com.coxandkings.travel.operations.model.thirdPartyVouchers;

import com.coxandkings.travel.operations.enums.thirdPartyVoucher.FileType;
import com.coxandkings.travel.operations.enums.thirdPartyVoucher.VoucherCodeStatus;
import com.coxandkings.travel.operations.enums.thirdPartyVoucher.VoucherCodeUsageType;
import com.coxandkings.travel.operations.utils.supplierBillPassing.ZonedDateSerializer;
import com.coxandkings.travel.operations.utils.supplierBillPassing.ZonedDateTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "voucherCode")
@IdClass(VoucherCodePK.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@AttributeOverrides({

        @AttributeOverride(name = "id", column = @Column(name = "id")),

        @AttributeOverride(name = "voucherCode", column = @Column(name = "voucherCode"))

})
public class VoucherCode {

    @Id
    private String voucherCode;

    @Id
    private String id;

    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @JsonSerialize(using = ZonedDateSerializer.class)
    @Column(name = "date_of_upload")
    private ZonedDateTime dateOfUpload;

    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @JsonSerialize(using = ZonedDateSerializer.class)
    @Column(name = "last_modified_date")
    private ZonedDateTime lastModifiedDate;

    @Enumerated(EnumType.STRING)
    private VoucherCodeUsageType voucherCodeUsageType;

    @Enumerated(EnumType.STRING)
    private VoucherCodeStatus voucherCodeStatus;

    @Enumerated
    private FileType fileType;

    private String voucherDetails;
    private String fileId;



    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @JsonSerialize(using = ZonedDateSerializer.class)
    private ZonedDateTime releaseDate;

    private String bookId;
    private String orderId;
    private String paymentStatus;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id", insertable = false, updatable = false)
    @JsonIgnore
    private SupplierVoucherCodes supplierVoucherCodes;

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

    public VoucherCodeStatus getVoucherCodeStatus() {
        return voucherCodeStatus;
    }

    public void setVoucherCodeStatus(VoucherCodeStatus voucherCodeStatus) {
        this.voucherCodeStatus = voucherCodeStatus;
    }

    public SupplierVoucherCodes getSupplierVoucherCodes() {
        return supplierVoucherCodes;
    }

    public void setSupplierVoucherCodes(SupplierVoucherCodes supplierVoucherCodes) {
        this.supplierVoucherCodes = supplierVoucherCodes;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public ZonedDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getVoucherDetails() {
        return voucherDetails;
    }

    public void setVoucherDetails(String voucherDetails) {
        this.voucherDetails = voucherDetails;
    }

    public ZonedDateTime getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(ZonedDateTime releaseDate) {
        this.releaseDate = releaseDate;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }


}
