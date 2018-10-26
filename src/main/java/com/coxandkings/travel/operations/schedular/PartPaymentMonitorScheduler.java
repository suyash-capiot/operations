package com.coxandkings.travel.operations.schedular;


import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.service.partPaymentMonitor.PartPaymentMonitorService;
import com.coxandkings.travel.operations.service.partPaymentMonitor.impl.PartPaymentMonitorServiceImpl;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class PartPaymentMonitorScheduler extends QuartzJobBean  {

    private static final Logger logger = LogManager.getLogger( PartPaymentMonitorServiceImpl.class );
    
    @Autowired
    PartPaymentMonitorService monitorService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            monitorService.findPartPaymentBookings();
        }
        catch( OperationException e )   {
            e.printStackTrace();
            logger.error( "Part Payment Monitor Batch Job failed", e );
        }
    }
}
