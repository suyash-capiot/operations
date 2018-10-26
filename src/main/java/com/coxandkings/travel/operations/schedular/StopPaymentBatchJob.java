package com.coxandkings.travel.operations.schedular;

import com.coxandkings.travel.operations.service.supplierbillpassing.StopPaymentService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class StopPaymentBatchJob extends QuartzJobBean {
	
	@Autowired
	StopPaymentService stopPaymentService;
	
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        stopPaymentService.releasePaymentScheduler();
    }
}
