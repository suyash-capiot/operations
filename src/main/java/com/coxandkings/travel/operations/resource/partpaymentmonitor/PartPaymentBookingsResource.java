package com.coxandkings.travel.operations.resource.partpaymentmonitor;

import java.util.List;

public class PartPaymentBookingsResource {

    private List<String> partPaymentBookings;

    public PartPaymentBookingsResource()    {
    }

    public List<String> getPartPaymentBookings() {
        return partPaymentBookings;
    }

    public void setPartPaymentBookings(List<String> partPaymentBookings) {
        this.partPaymentBookings = partPaymentBookings;
    }
}
