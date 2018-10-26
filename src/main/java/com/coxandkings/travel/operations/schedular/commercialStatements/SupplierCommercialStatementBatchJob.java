package com.coxandkings.travel.operations.schedular.commercialStatements;

import com.coxandkings.travel.operations.service.commercialstatements.SupplierCommercialStatementSchedulerService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SupplierCommercialStatementBatchJob  extends QuartzJobBean {
	
	@Autowired
	SupplierCommercialStatementSchedulerService supplierCommercialStatementSchedulerService;
	
    @Override
    protected void executeInternal(JobExecutionContext context)  {
        supplierCommercialStatementSchedulerService.scheduler();
    }
}
