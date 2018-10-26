package com.coxandkings.travel.operations.model.merge;

import com.coxandkings.travel.operations.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BookProduct extends BaseModel {
    @Column
    private String bookingReferenceId;

    @Column
    private String productId;

    @Column
    private String oldSupplierRef;

    public BookProduct() {
    }

    public BookProduct(String bookingReferenceId, String productId, String oldSupplierRef) {
        this.bookingReferenceId = bookingReferenceId;
        this.productId = productId;
        this.oldSupplierRef = oldSupplierRef;
    }

    public String getBookingReferenceId() {
        return bookingReferenceId;
    }

    public void setBookingReferenceId(String bookingReferenceId) {
        this.bookingReferenceId = bookingReferenceId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getOldSupplierRef() {
        return oldSupplierRef;
    }

    public void setOldSupplierRef(String oldSupplierRef) {
        this.oldSupplierRef = oldSupplierRef;
    }
}
