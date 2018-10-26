package com.coxandkings.travel.operations.repository.timelimitbooking;

import com.coxandkings.travel.operations.model.timelimitbooking.TimeLimitBatchJobInfo;

import java.util.List;

public interface TimeLimitBatchJobRepository {
    public TimeLimitBatchJobInfo saveTimeLimitBacthJobInfo(TimeLimitBatchJobInfo timeLimitBatchJobInfo);

    public List<TimeLimitBatchJobInfo> getTLBatchJobInfoByCriteria();

}
