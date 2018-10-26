package com.coxandkings.travel.operations.zmock.resource;

import java.util.List;

public class PotentialListResource {

    MatchedBookingResource requestForCancellation;
    List<MatchedBookingResource> onRequest;

    public MatchedBookingResource getRequestForCancellation() {
        return requestForCancellation;
    }

    public void setRequestForCancellation(MatchedBookingResource requestForCancellation) {
        this.requestForCancellation = requestForCancellation;
    }

    public List<MatchedBookingResource> getOnRequest() {
        return onRequest;
    }

    public void setOnRequest(List<MatchedBookingResource> onRequest) {
        this.onRequest = onRequest;
    }

    @Override
    public String toString() {
        return "PotentialListResource{" +
                "requestForCancellation=" + requestForCancellation +
                ", onRequest=" + onRequest +
                '}';
    }
}
