package com.coxandkings.travel.operations.schedular;

import com.coxandkings.travel.operations.service.booking.impl.OpsBookingServiceImpl;
import com.coxandkings.travel.operations.service.prodreview.ProductReviewSchedulerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ProductReviewScheduler extends QuartzJobBean {

    private static Logger logger = LogManager.getLogger(OpsBookingServiceImpl.class);
    
    @Autowired
    ProductReviewSchedulerService schedulerService;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        
        try {
            schedulerService.sendReviewToClient();
            //schedulerService.updateAging();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Product Review Service failed to start", e);
        }
    }
}
