package com.coxandkings.travel.operations.utils;

import com.coxandkings.travel.operations.enums.amendclientcommercials.BEInboundOperation;
import com.coxandkings.travel.operations.enums.amendclientcommercials.BEOperationId;
import com.coxandkings.travel.operations.enums.amendclientcommercials.BEServiceUri;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.utils.xml.XMLTransformer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

@Component
public class BookingEngineElasticData {

    @Value("${elastic-search.booking-engine}")
    private String bookingEngineIndexUrl;

    private JSONObject getElasticSearchFilter(String userId, String transactionId, String sessionId, BEInboundOperation inboundOperation, BEOperationId operationId, BEServiceUri service) throws JSONException {
        String inbound_operation = inboundOperation.getInboundOperation();
        String operationid = operationId.getOperationid();
        String serviceUri = service.getServiceUri();
        JSONObject searchFilter = new JSONObject();
        JSONObject queryJSON = new JSONObject();
        searchFilter.put("query", queryJSON);
        JSONObject booleanQueryJSON = new JSONObject();
        queryJSON.put("bool", booleanQueryJSON);
        JSONArray mustQueryArr = new JSONArray();
        booleanQueryJSON.put("must", mustQueryArr);
        mustQueryArr.put(new JSONObject().put("match_phrase", new JSONObject().put("inbound_operation", inbound_operation)));
        mustQueryArr.put(new JSONObject().put("match_phrase", new JSONObject().put("operationid", operationid)));
        mustQueryArr.put(new JSONObject().put("match_phrase", new JSONObject().put("sessionid", sessionId)));
        mustQueryArr.put(new JSONObject().put("match_phrase", new JSONObject().put("userid", userId)));
        mustQueryArr.put(new JSONObject().put("match_phrase", new JSONObject().put("transactionid", transactionId)));
        mustQueryArr.put(new JSONObject().put("match_phrase", new JSONObject().put("inbound_service_uri", serviceUri)));
        return searchFilter;

    }


    public JSONObject getJSONData(String userId, String transactionId, String sessionId, BEInboundOperation inboundOperation, BEOperationId operationId, BEServiceUri service) throws JSONException, OperationException {
        JSONObject messageJson = null;
        JSONObject searchFilter = getElasticSearchFilter(userId, transactionId, sessionId, inboundOperation, operationId, service);

        ResponseEntity<String> searchResult = RestUtils.postForEntity(bookingEngineIndexUrl, searchFilter.toString(), String.class);
        JSONObject searchResJSON = new JSONObject(searchResult.getBody());
        JSONArray searchResArr = searchResJSON.getJSONObject("hits").getJSONArray("hits");
        if (searchResArr.length() == 0) {
            throw new OperationException(String.format("No Matching Message Found for userId <%s>, transactionId <%s>, sessionId <%s>,  inboundOperation<%s>, operationId<%s>, service<%s>",
                    userId, transactionId, sessionId, inboundOperation.getInboundOperation(), operationId.getOperationid(), service.getServiceUri()));
        }
        String message = searchResArr.getJSONObject(0).getJSONObject("_source").getString("logmessage");
        if (!message.startsWith("{")) {
            int endWith = message.indexOf("{");
            String replaceString = message.substring(0, endWith);
            String requestBody = message.replace(replaceString, "");
            messageJson = new JSONObject(new JSONTokener(requestBody));
        } else
            messageJson = new JSONObject(new JSONTokener(message));
        return messageJson;
    }

    public Element getXMLData(String userId, String transactionId, String sessionId, BEInboundOperation inboundOperation, BEOperationId operationId, BEServiceUri service) throws JSONException, OperationException {
        JSONObject searchFilter = getElasticSearchFilter(userId, transactionId, sessionId, inboundOperation, operationId, service);
        ResponseEntity<String> searchResult = RestUtils.postForEntity(bookingEngineIndexUrl, searchFilter.toString(), String.class);
        JSONObject searchResJSON = new JSONObject(searchResult.getBody());
        JSONArray searchResArr = searchResJSON.getJSONObject("hits").getJSONArray("hits");
        if (searchResArr.length() == 0) {
            throw new OperationException(String.format("No Matching Message Found for userId <%s>, transactionId <%s>, sessionId <%s>,  inboundOperation<%s>, operationId<%s>, service<%s>",
                    userId, transactionId, sessionId, inboundOperation.getInboundOperation(), operationId.getOperationid(), service.getServiceUri()));
        }
        String message = searchResArr.getJSONObject(0).getJSONObject("_source").getString("logmessage");
        return XMLTransformer.fromEscapedString(message);
    }
	/*public Element getRsWrapper(String userId,String transactionId,String sessionId,BEInboundOperation inboundOperation,BEOperationId operationId,BEServiceUri service) throws JSONException, OperationException {
		JSONObject searchFilter=getElasticSearchFilter(userId, transactionId, sessionId, inboundOperation,operationId,service );
		ResponseEntity<String> searchResult=RestUtils.postForEntity(bookingEngineIndexUrl,searchFilter.toString(), String.class);
		JSONObject searchResJSON=new JSONObject(searchResult.getBody());
		JSONArray searchResArr=searchResJSON.getJSONObject("hits").getJSONArray("hits");
		if(searchResArr.length()==0) {
			throw new OperationException(String.format("No Matching Message Found for userId <%s>, transactionId <%s>, sessionId <%s>,  inboundOperation<%s>, operationId<%s>, service<%s>",
					userId, transactionId, sessionId, inboundOperation.getInboundOperation(),operationId.getOperationid(),service.getServiceUri()));
		}
		String message=searchResArr.getJSONObject(1).getJSONObject("_source").getString("message");
		return XMLTransformer.fromEscapedString(message);
	}*/


}
