package com.coxandkings.travel.operations.schedular.managearrivallist;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.managearrivallist.ConfigurationResource;
import com.coxandkings.travel.operations.service.managearrivallist.accomodationarrivallist.AccoArrivalListBatchJobService;
import com.itextpdf.text.DocumentException;
import org.json.JSONException;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.IOException;

public class AccoArrivalListSchedular extends QuartzJobBean
{
	@Autowired
	AccoArrivalListBatchJobService arrivalListService;
	
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException
    {
        System.out.println("enter into Acco Arrival List Batch Job Service");
        ConfigurationResource configurationResource = new ConfigurationResource();

        configurationResource.setProductCategorySubType("Hotel");
        configurationResource.setSupplierName(null);

        configurationResource.setContinent(null);
        configurationResource.setCity(null);
        configurationResource.setProductName(null);
        configurationResource.setChain(null);
        configurationResource.setIsMysteryProduct(null);
        configurationResource.setSendToSupplier(true);
        try {
            arrivalListService.accoArrivalList(configurationResource);
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
