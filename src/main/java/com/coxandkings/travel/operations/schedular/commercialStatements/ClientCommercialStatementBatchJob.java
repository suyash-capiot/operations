package com.coxandkings.travel.operations.schedular.commercialStatements;

import com.coxandkings.travel.operations.service.commercialstatements.ClientCommercialStatementSchedulerService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ClientCommercialStatementBatchJob extends QuartzJobBean {
	@Autowired
	ClientCommercialStatementSchedulerService clientCommercialStatementSchedulerService;
	
    @Override
    protected void executeInternal(JobExecutionContext context) {
        clientCommercialStatementSchedulerService.scheduler();
    }
}
