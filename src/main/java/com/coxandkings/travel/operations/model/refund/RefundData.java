package com.coxandkings.travel.operations.model.refund;


import com.coxandkings.travel.operations.enums.refund.ReasonForRequest;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/*

    This class is used to store the refund details which not send to finance.
    Which is maintained by ops
 */
@Entity
@Table(name = "refund")
public class RefundData implements Serializable {
    @Id
    private String claimNo;

    /*this attribute is used when we get the request back from finance then we will take this reason*/
    @Enumerated(EnumType.STRING)
    private ReasonForRequest refundReason;

    public String getClaimNo() {
        return claimNo;
    }

    public void setClaimNo(String claimNo) {
        this.claimNo = claimNo;
    }

    public ReasonForRequest getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(ReasonForRequest refundReason) {
        this.refundReason = refundReason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefundData that = (RefundData) o;
        return Objects.equals(claimNo, that.claimNo) &&
                refundReason == that.refundReason;
    }

    @Override
    public int hashCode() {

        return Objects.hash(claimNo, refundReason);
    }

    @Override
    public String toString() {
        return "RefundData{" +
                "claimNo='" + claimNo + '\'' +
                ", refundReason=" + refundReason +
                '}';
    }
}
