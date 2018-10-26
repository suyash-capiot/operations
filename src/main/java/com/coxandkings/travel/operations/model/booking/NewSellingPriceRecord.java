package com.coxandkings.travel.operations.model.booking;

import com.coxandkings.travel.operations.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class NewSellingPriceRecord extends BaseModel {

    @Column(name = "bookingReferenceNo")
    private String bookingRefNo;

    @Column(name = "productId")
    private String productId;

    @Column(name = "supplierId")
    private String supplierId;

    @Column(name = "newSellingPriceRecord")
    private String newSellingPriceRecordDetails;

    public String getBookingRefNo() {
        return bookingRefNo;
    }

    public void setBookingRefNo(String bookingRefNo) {
        this.bookingRefNo = bookingRefNo;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getNewSellingPriceRecordDetails() {
        return newSellingPriceRecordDetails;
    }

    public void setNewSellingPriceRecordDetails(String newSellingPriceRecordDetails) {
        this.newSellingPriceRecordDetails = newSellingPriceRecordDetails;
    }
}
