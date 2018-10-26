package com.coxandkings.travel.operations.schedular;


import com.coxandkings.travel.operations.service.email.EmailService;
import com.coxandkings.travel.operations.service.partPaymentMonitor.impl.PartPaymentMonitorServiceImpl;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Component
public class InboundEmailsScheduler extends QuartzJobBean  {

    private static final Logger logger = LogManager.getLogger( PartPaymentMonitorServiceImpl.class );
    
    @Autowired
    EmailService monitorService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
    	
       
       /* JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        EmailService monitorService = (EmailService) dataMap.get("InboundEMailsBatchJobService");*/
        try {
            monitorService.receiveClientInboundEmails();
            monitorService.receiveCustomerInboundEmails();
            monitorService.receiveSupplierInboundEmails();
            monitorService.receiveAutoInBoundEmail();
        }
        catch( Exception e )   {
            e.printStackTrace();
            logger.error( "Inbound Emails Batch Job failed", e );
        }
    }
}
