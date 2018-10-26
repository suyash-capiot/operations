package com.coxandkings.travel.operations.service.failure;

import org.json.JSONArray;
import org.json.JSONObject;

public interface FailureThresholdConfigurationService {

    String addThresholdConfiguration(JSONObject req);

    JSONArray searchThresholdConfiguration(JSONObject reqJson);

    String fetchThresholdConfiguration(String configurationId);

}
