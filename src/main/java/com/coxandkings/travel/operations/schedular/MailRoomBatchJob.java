package com.coxandkings.travel.operations.schedular;


import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.service.mailroomanddispatch.MailRoomBatchService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class MailRoomBatchJob extends QuartzJobBean {

	@Autowired
	MailRoomBatchService mailRoomBatchService;
	
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        
        try {
            mailRoomBatchService.updateMailroomStatusActive();
            mailRoomBatchService.updateMailroomStatusInactive();
            mailRoomBatchService.updateMailroomEmployeeStatusActive();
        } catch (OperationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
