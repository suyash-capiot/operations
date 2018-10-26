package com.coxandkings.travel.operations.schedular;


import com.coxandkings.travel.operations.model.timelimitbooking.TimeLimitBatchJobInfo;
import com.coxandkings.travel.operations.service.timelimitbooking.TimeLimitBatchJobInfoService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class TimeLimitSchedular extends QuartzJobBean {

	@Autowired
	TimeLimitBatchJobInfoService timeLimitBatchJobInfoService;
	
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        List<TimeLimitBatchJobInfo> aList=timeLimitBatchJobInfoService.getTLBatchJobInfoByCriteria();
        for (TimeLimitBatchJobInfo jobInfo : aList) {
            timeLimitBatchJobInfoService.sendEmailAndAlertWhenCloseToExpiry(jobInfo.getBookId(), jobInfo.getOrderId(), jobInfo.getCalculatedExpiryDueDate().toString());
        }
    }
}

