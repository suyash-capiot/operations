package com.coxandkings.travel.operations.model.fullCancellation;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FullCancellationIdentity implements Serializable {


    private static final long serialVersionUID = 4107399471749314632L;

    @NotNull
    private String bookId;

    @NotNull
    private String orderNo;

    public FullCancellationIdentity() {
        //no logic
    }

    public FullCancellationIdentity(String bookId, String orderId) {
        this.bookId = bookId;
        this.orderNo = orderId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FullCancellationIdentity)) return false;
        FullCancellationIdentity that = (FullCancellationIdentity) o;
        return Objects.equals(getBookId(), that.getBookId()) &&
                Objects.equals(getOrderNo(), that.getOrderNo());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getBookId(), getOrderNo());
    }
}
