package com.coxandkings.travel.operations.schedular;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.paymentadvice.AutomaticPaymentAdviceGenerationService;
import org.json.JSONException;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

public class PaymentAdviceSchedular extends QuartzJobBean {

	@Autowired
	AutomaticPaymentAdviceGenerationService arrivalListService;
	
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            arrivalListService.generatePaymentAdvice();
        } catch (OperationException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
