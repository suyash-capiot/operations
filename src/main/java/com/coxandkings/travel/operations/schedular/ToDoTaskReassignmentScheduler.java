package com.coxandkings.travel.operations.schedular;


import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.service.todo.ToDoBatchJobService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ToDoTaskReassignmentScheduler extends QuartzJobBean {

    private static final Logger logger  = LogManager.getLogger(ToDoTaskReassignmentScheduler.class);
    
    @Autowired
    ToDoBatchJobService toDoBatchJobService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("Begin todo task reassignment batch job");
        
        try {
            toDoBatchJobService.reassignUnreadTask();
            logger.info("End todo task reassignment batch job");
        } catch (OperationException e) {
            e.printStackTrace();
        }
    }
}

