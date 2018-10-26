package com.coxandkings.travel.operations.schedular;

import com.coxandkings.travel.operations.service.thirdPartyVoucher.ThirdPartyVouchersService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ThirdPartyVoucherBatchJob extends QuartzJobBean {
	@Autowired
	ThirdPartyVouchersService thirdPartyVouchersService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        thirdPartyVouchersService.getThirdPartyVoucher();

    }
}
