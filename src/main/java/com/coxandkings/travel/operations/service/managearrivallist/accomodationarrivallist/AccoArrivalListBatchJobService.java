package com.coxandkings.travel.operations.service.managearrivallist.accomodationarrivallist;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.managearrivallist.ArrivalListInfo;
import com.coxandkings.travel.operations.resource.managearrivallist.ConfigurationResource;
import com.itextpdf.text.DocumentException;
import org.json.JSONException;

import java.io.IOException;

public interface AccoArrivalListBatchJobService {

    public ArrivalListInfo accoArrivalList(ConfigurationResource configurationResource) throws IOException, DocumentException, JSONException, OperationException;
}
