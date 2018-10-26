package com.coxandkings.travel.operations.schedular.managearrivallist;


import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.managearrivallist.ConfigurationResource;
import com.coxandkings.travel.operations.service.managearrivallist.generalarrivallist.GeneralArrivalListBatchJobService;
import com.itextpdf.text.DocumentException;
import org.json.JSONException;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class GeneralArrivalListSchedular extends QuartzJobBean
{

	@Autowired
	GeneralArrivalListBatchJobService generalArrivalListBatchJobService;
	
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException
    {
        System.out.println("enter into General Arrival List Schedular");

        ConfigurationResource configurationResource = new ConfigurationResource();
        configurationResource.setCutOffGeneration(Duration.ofHours(24));
        configurationResource.setProductCategory("");
        configurationResource.setProductCategorySubType("");
        configurationResource.setSupplierName("");
        configurationResource.setClientType("");
        configurationResource.setClientGroup("");
        configurationResource.setClientName("");
        configurationResource.setSendToSupplier(true);
        configurationResource.setBookingDate(/*"2019-04-21 23:00:59"*/"");

        try {

            generalArrivalListBatchJobService.generateArrivalListBasedOnCutOffDate(configurationResource);
        } catch (OperationException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
