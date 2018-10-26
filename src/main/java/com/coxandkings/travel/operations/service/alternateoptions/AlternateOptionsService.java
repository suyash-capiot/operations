package com.coxandkings.travel.operations.service.alternateoptions;

import java.util.List;
import java.util.Map;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.alternateOption.AlternateOptionResponse;
import com.coxandkings.travel.operations.resource.alternateOption.AlternateOptionsResource;
import org.json.JSONObject;

import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptions;
import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptionsResponseDetails;
import org.springframework.http.HttpStatus;

public interface AlternateOptionsService { 

	String addAlternateOptions(JSONObject reqJson) throws OperationException;
 
	String searchAlternateOptions(JSONObject reqJson);
	AlternateOptionResponse searchByCriteria(JSONObject reqJson);

	String fetchAlternateOptions(String configurationId);
	
	List<AlternateOptions> addAlternateOptionsResponseDetails(
			AlternateOptions alternateOptionsResponseDetails);

	Map approve(String configurationId, String remarks);

	Map reject(String configurationId, String remarks);
	
}
