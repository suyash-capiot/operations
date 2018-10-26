package com.coxandkings.travel.operations.repository.timelimitbooking;

import com.coxandkings.travel.operations.model.timelimitbooking.TLCancellationBatchJobInfo;

import java.util.List;

public interface CancellationBatchJobInfoRepository {
    public TLCancellationBatchJobInfo saveCancellationBacthJobInfo(TLCancellationBatchJobInfo tlCancellationBatchJobInfo);

    public List<TLCancellationBatchJobInfo> getTLCancellationBatchJobInfoByCriteria();
}
