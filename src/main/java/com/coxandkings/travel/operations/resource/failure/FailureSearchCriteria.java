package com.coxandkings.travel.operations.resource.failure;

import com.coxandkings.travel.operations.criteria.booking.becriteria.BookingSearchCriteria;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FailureSearchCriteria extends BookingSearchCriteria {

    private String failureFlag;
    private String paymentFailureFlag;

    public String getFailureFlag() {
        return failureFlag;
    }

    public void setFailureFlag(String failureFlag) {
        this.failureFlag = failureFlag;
    }

    public String getPaymentFailureFlag() {
        return paymentFailureFlag;
    }

    public void setPaymentFailureFlag(String paymentFailureFlag) {
        this.paymentFailureFlag = paymentFailureFlag;
    }
}
