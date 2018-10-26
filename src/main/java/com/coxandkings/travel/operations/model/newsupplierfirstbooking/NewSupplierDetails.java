package com.coxandkings.travel.operations.model.newsupplierfirstbooking;


import com.coxandkings.travel.operations.model.BaseModel;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "new_supplier_details")
public class NewSupplierDetails extends BaseModel implements Serializable {

    private String orderId;
    private String bookId;
    private boolean firstReservationCheckFlag;

    public NewSupplierDetails() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public boolean isFirstReservationCheckFlag() {
        return firstReservationCheckFlag;
    }

    public void setFirstReservationCheckFlag(boolean firstReservationCheckFlag) {
        this.firstReservationCheckFlag = firstReservationCheckFlag;
    }
}
