package com.coxandkings.travel.operations.schedular.managearrivallist;


import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.managearrivallist.ConfigurationResource;
import com.coxandkings.travel.operations.service.managearrivallist.flightarrivallist.FlightArrivalListBatchJobService;
import com.itextpdf.text.DocumentException;
import org.json.JSONException;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.IOException;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class FlightArrivalListSchedular extends QuartzJobBean
{

	@Autowired
	FlightArrivalListBatchJobService flightArrivalListBatchJobService;
	
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        System.out.println("enter into FlightArrivalList");
       
        ConfigurationResource configurationResource = new ConfigurationResource();
        configurationResource.setBookingDate("");
        configurationResource.setSupplierName("SUPP2");
        configurationResource.setProductCategorySubType("Flight");
        configurationResource.setAirlineName("");
        configurationResource.setFromCity("");
        configurationResource.setToCity("");
        configurationResource.setJourneyType("");
        configurationResource.setSendToSupplier(true);

        try {
            flightArrivalListBatchJobService.flightArrivalList(configurationResource);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (OperationException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
