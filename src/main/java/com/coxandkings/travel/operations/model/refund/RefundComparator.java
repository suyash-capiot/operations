package com.coxandkings.travel.operations.model.refund;

import java.util.Comparator;

public class RefundComparator implements Comparator<Refund> {
    @Override
    public int compare(Refund refund1, Refund refund2) {
        return refund1.getClaimNo().compareTo(refund2.getClaimNo());
    }
}
