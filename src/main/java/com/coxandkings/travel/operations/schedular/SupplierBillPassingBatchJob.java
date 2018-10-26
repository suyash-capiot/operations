package com.coxandkings.travel.operations.schedular;

import com.coxandkings.travel.operations.service.supplierbillpassing.SupplierBillPassingService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SupplierBillPassingBatchJob extends QuartzJobBean {
	
	@Autowired
	SupplierBillPassingService supplierBillPassingService;
	
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        supplierBillPassingService.paymentAdviceAutoGeneration();
    }
}
