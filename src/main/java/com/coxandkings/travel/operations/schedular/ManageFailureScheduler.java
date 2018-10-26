package com.coxandkings.travel.operations.schedular;


import com.coxandkings.travel.operations.service.failure.FailureSchedulerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;


@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ManageFailureScheduler extends QuartzJobBean {

    private static final Logger logger = LogManager.getLogger(ManageFailureScheduler.class);
    
    @Autowired
    FailureSchedulerService service;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        try {
            service.processBooking();
            service.closeToDo();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Manage Failure batch job failed", e);
        }
    }

}
