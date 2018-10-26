package com.coxandkings.travel.operations.schedular;


import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.service.managedocumentation.ManageDocumentationBatchJobService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;


@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ManageDocumentationScheduler extends QuartzJobBean {

    private static final Logger logger = LogManager.getLogger(ManageDocumentationScheduler.class);
    
    @Autowired
    ManageDocumentationBatchJobService manageDocumentationBatchJobService;
    
    @Override
    protected void executeInternal(JobExecutionContext context) {
        try {
            manageDocumentationBatchJobService.checkMDMUpdates();
        } catch (OperationException e) {
            logger.info("Problem in fetching documents details/order details");
        }
        try {
            manageDocumentationBatchJobService.generateHandoverDocsAndCustomerDocs();
        } catch (Exception ex) {
            logger.info("Problem in generating documents");
        }
        try {
            manageDocumentationBatchJobService.sendCutOffAlert();
        } catch (OperationException e) {
            logger.info("Problem in fetching the Order details");
        }
        try {
            manageDocumentationBatchJobService.checkReceivedDocsStatus();
        } catch (Exception e) {
            logger.info("Problem in checking the Document status");
        }
    }

}
