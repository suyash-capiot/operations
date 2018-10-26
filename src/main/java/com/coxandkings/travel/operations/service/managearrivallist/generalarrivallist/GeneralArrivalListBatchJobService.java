package com.coxandkings.travel.operations.service.managearrivallist.generalarrivallist;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.managearrivallist.ArrivalListInfo;
import com.coxandkings.travel.operations.resource.managearrivallist.ConfigurationResource;
import com.itextpdf.text.DocumentException;
import org.json.JSONException;

import java.io.IOException;

public interface GeneralArrivalListBatchJobService {


    public ArrivalListInfo generateArrivalListBasedOnCutOffDate(ConfigurationResource configurationResource) throws OperationException, IOException, DocumentException, JSONException;

    public String getSupplierId(String supplierName) throws JSONException, OperationException;

    public String getSupName(String supplierId) throws JSONException, OperationException;

    public String getClientId(String clientName, String clientType) throws JSONException, OperationException;

    public String getClientNameUsingClientIDAndType(String clientId, String clientType) throws JSONException, OperationException;

    public String getClientGroupId(String clientGroupName) throws JSONException, OperationException;

    public String getClientGroupName(String clientGroupId) throws JSONException, OperationException;
}
