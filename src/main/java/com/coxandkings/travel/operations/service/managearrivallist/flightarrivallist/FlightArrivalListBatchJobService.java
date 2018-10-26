package com.coxandkings.travel.operations.service.managearrivallist.flightarrivallist;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.managearrivallist.ArrivalListInfo;
import com.coxandkings.travel.operations.resource.managearrivallist.ConfigurationResource;
import com.itextpdf.text.DocumentException;
import org.json.JSONException;

import java.io.IOException;

public interface FlightArrivalListBatchJobService
{
    public ArrivalListInfo flightArrivalList(ConfigurationResource configurationResource) throws IOException, DocumentException, JSONException, OperationException;
}
