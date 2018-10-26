package com.coxandkings.travel.operations.service.mdmservice;

import com.coxandkings.travel.operations.enums.common.MDMClientType;
import com.coxandkings.travel.operations.exceptions.OperationException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public interface ClientMasterDataService {

    public Map<String, String> getB2BClientNames(List<String> clientIDs) throws OperationException;

    public Map<String, String> getB2CClientNames(List<String> clientIDs) throws OperationException;

    public String getB2BClientDetails(String clientID) throws OperationException;

    public String getB2CClientDetails(String clientID) throws OperationException;

    ZonedDateTime getClientKPI(String clientId, String clientMarket, MDMClientType clientType) throws OperationException;

    String getClientEmailId(String clientId, MDMClientType clientType) throws OperationException;

    JSONArray getClientNames(JSONObject object);

    JSONArray getCorporateTravellersClient(JSONObject object);

    JSONArray getUsersFromUserManagement(JSONObject object);
}
