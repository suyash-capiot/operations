package com.coxandkings.travel.operations.schedular;

import com.coxandkings.travel.operations.model.timelimitbooking.TLCancellationBatchJobInfo;
import com.coxandkings.travel.operations.service.timelimitbooking.TimeLimitBatchJobInfoService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class TimeLimitCancellationSchedular extends QuartzJobBean {

	@Autowired
	TimeLimitBatchJobInfoService timeLimitBatchJobInfoService;
	
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        
        List<TLCancellationBatchJobInfo> aList = timeLimitBatchJobInfoService.getTLCancellationBatchInfoByCriteria();
        for (TLCancellationBatchJobInfo aTLCancellationInfo : aList) {
            timeLimitBatchJobInfoService.cancellationRequest(aTLCancellationInfo.getBookId(), aTLCancellationInfo.getOrderId(), aTLCancellationInfo.getCalculatedBeforeDueDate().toString());
        }
    }
}

