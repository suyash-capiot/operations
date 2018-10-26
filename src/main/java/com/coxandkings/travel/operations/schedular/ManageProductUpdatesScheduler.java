package com.coxandkings.travel.operations.schedular;


import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.service.manageproductupdates.ManageProductUpdatesService;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ManageProductUpdatesScheduler extends QuartzJobBean  {

    private static final Logger logger = LogManager.getLogger( ManageProductUpdatesScheduler.class );
    
    @Autowired
    ManageProductUpdatesService monitorService;
    
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            monitorService.handleProductUpdates();
        }
        catch( OperationException e )   {
            e.printStackTrace();
            logger.error( "Manage ProductResource Updates Batch Job failed", e );
        }
    }
}
