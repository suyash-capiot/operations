package com.coxandkings.travel.operations.repository.timelimitbooking;

import com.coxandkings.travel.operations.model.timelimitbooking.TLBackgroundInfo;

public interface TimeLimitClientInfoRepository {
    public TLBackgroundInfo saveClientbackgroundInfo(TLBackgroundInfo tlBackgroundInfo);

    public TLBackgroundInfo getClientBackgroundInfoByBookId(String bookId);

    public TLBackgroundInfo getClientBackgroundInfoClientId(String clientId);

    public boolean isClientBackgroundInfoExists(String bookId);

}
