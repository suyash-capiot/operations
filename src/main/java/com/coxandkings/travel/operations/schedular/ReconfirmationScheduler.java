
package com.coxandkings.travel.operations.schedular;


import com.coxandkings.travel.operations.service.reconfirmation.batchjob.ReconfirmationService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ReconfirmationScheduler extends QuartzJobBean {
	
	@Autowired
	ReconfirmationService reconfirmationService;
	

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            reconfirmationService.schedulerJob();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

