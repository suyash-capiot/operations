package com.coxandkings.travel.operations.helper.booking.product.transport.rail;

import com.coxandkings.travel.operations.helper.booking.product.transport.Transfer;

public class Rail extends Transfer {
    private String trainName;
    private String journeyType;

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getJourneyType() {
        return journeyType;
    }

    public void setJourneyType(String journeyType) {
        this.journeyType = journeyType;
    }
}
