package com.coxandkings.travel.operations.resource.prepaymenttosupplier.paymentadvice;

import java.util.Comparator;

public class PaymentDueDate implements Comparator<DateWisePaymentPercentage> {

    @Override
    public int compare(DateWisePaymentPercentage o1, DateWisePaymentPercentage o2) {
        return o1.getPaymentDueDate().compareTo(o2.getPaymentDueDate());
    }
}

