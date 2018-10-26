package com.coxandkings.travel.operations.utils.adapter;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class OpsAdapterMdmRequirements {

    @Value(value = "${adapter.mdm.getAirlineName}")
    private String mdmAirlineNameUrl;

    @Value(value = "${adapter.mdm.airlineNamePathExpression}")
    private String airlineNamePathExpression;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    String getAirlineNameFromAirlineCode(String airlineCode) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data.code", airlineCode);

        URI uri = UriComponentsBuilder.fromUriString(mdmAirlineNameUrl + jsonObject.toString()).build().encode().toUri();
        String response = null;
        try {
            response = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
        } catch (OperationException e) {
            e.printStackTrace();
        }

        if (response != null) {
            String airlineName = (String) jsonObjectProvider.getChildObject(response, airlineNamePathExpression, String.class);
            return airlineName;
        } else {
            return null;
        }
    }
}
