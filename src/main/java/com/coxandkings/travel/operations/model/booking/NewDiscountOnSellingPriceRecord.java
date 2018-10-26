package com.coxandkings.travel.operations.model.booking;

import com.coxandkings.travel.operations.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Clob;

@Entity
@Table
public class NewDiscountOnSellingPriceRecord extends BaseModel {
    @Column(name = "bookingReferenceId")
    private String bookId;
    @Column(name = "detailsOfNewRecord")
    private Clob detailsOfNewRecord;
    @Column(name = "productReferenceId")
    private String bookProductId;

    public String getBookProductId() { return bookProductId; }

    public void setBookProductId(String bookProductId) { this.bookProductId = bookProductId; }

    public String getBookId() { return bookId; }

    public void setBookId(String bookId) { this.bookId = bookId; }

    public Clob getDetailsOfNewRecord() { return detailsOfNewRecord; }

    public void setDetailsOfNewRecord(Clob detailsOfNewRecord) { this.detailsOfNewRecord = detailsOfNewRecord; }
}
