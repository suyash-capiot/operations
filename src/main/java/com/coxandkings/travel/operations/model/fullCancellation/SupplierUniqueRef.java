package com.coxandkings.travel.operations.model.fullCancellation;

import com.coxandkings.travel.operations.model.BaseModel;

import javax.persistence.Entity;
import java.util.Objects;


@Entity
public class SupplierUniqueRef extends BaseModel {


    private String bookId;
    private String orderId;
    private String supplierId;


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

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SupplierUniqueRef that = (SupplierUniqueRef) o;
        return Objects.equals(bookId, that.bookId) &&
                Objects.equals(orderId, that.orderId) &&
                Objects.equals(supplierId, that.supplierId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(bookId, orderId, supplierId);
    }

    @Override
    public String toString() {
        return "SupplierUniqueRef{" +
                "bookId='" + bookId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", supplierId='" + supplierId + '\'' +
                '}';
    }
}
