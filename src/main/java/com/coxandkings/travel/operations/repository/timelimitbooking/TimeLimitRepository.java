package com.coxandkings.travel.operations.repository.timelimitbooking;

import com.coxandkings.travel.operations.model.timelimitbooking.TimeLimitExpiryInfo;

public interface TimeLimitRepository {

    public TimeLimitExpiryInfo saveExpiryInfo(TimeLimitExpiryInfo timeLimitExpiryInfo);

    public TimeLimitExpiryInfo getExpiryInfo(String orderId);
}
