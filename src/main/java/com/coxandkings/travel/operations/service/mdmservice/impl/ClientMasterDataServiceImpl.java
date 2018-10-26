package com.coxandkings.travel.operations.service.mdmservice.impl;

import com.coxandkings.travel.operations.enums.common.MDMClientType;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.service.mdmservice.ClientMasterDataService;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClientMasterDataServiceImpl implements ClientMasterDataService {

    private static Logger logger = LogManager.getLogger(ClientMasterDataServiceImpl.class);

    @Value(value = "${mdm.client}")
    private String getClientsURL;
    @Value(value = "{mdm.common.client.client_type}")
    private String getClientType;
    @Value(value = "${offlineBooking.corporateTravellers}")
    private String getCorpTravellerURL;
    @Value("${mdm.user-management}")
    private String userManagementUrl;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Autowired
    private JsonObjectProvider jsonFilter;

    @Override
    public Map<String, String> getB2BClientNames(List<String> clientIDs) throws OperationException {

        HashMap<String, String> clientNamesMap = new HashMap<>();
        try {
            JSONObject filterParams = new JSONObject();
            JSONArray clientIDsList = new JSONArray();
            for (String aClientID : clientIDs) {
                if (aClientID != null && aClientID.trim().length() > 0) {
                    clientIDsList.put(aClientID);
                }
            }

            filterParams.put("_id", clientIDsList);

            String URL = getClientsURL + "clientB2B?filter=" + filterParams.toString() + "&select=clientProfile";
            logger.info("URL to get B2B Client Names are: " + URL);
            URI uri = UriComponentsBuilder.fromUriString(URL).build().encode().toUri();
            ResponseEntity<String> responseEntity = mdmRestUtils.exchange(uri, HttpMethod.GET, null, String.class);
            String responseInString = responseEntity.getBody();
            List<String> clientDetailsList = jsonFilter.getChildrenCollection(responseInString, "$.data", String.class);
            if (clientDetailsList != null && clientDetailsList.size() > 0) {
                for (String aCustomerProfile : clientDetailsList) {
                    String clientName = jsonFilter.getAttributeValue(aCustomerProfile, "$.clientProfile.clientDetails.clientName", String.class);
                    String clientID = jsonFilter.getAttributeValue(aCustomerProfile, "$._id", String.class);
                    clientNamesMap.put(clientID, clientName);
                }
            }
        } catch (Exception e) {
            OperationException anErr = new OperationException("Unable to get B2B Client Names", e.getMessage());
            e.printStackTrace();
        }
                return clientNamesMap;
    }

    @Override
    public JSONArray getClientNames(JSONObject object) {

        JSONArray in = new JSONArray();
        JSONObject filterObject = new JSONObject();
        String clientName = object.optString("clientName");
        String clientType = object.optString("clientType");
        String clientId = object.optString("clientId");

        if(clientType.equals("B2B")) {

            if(!object.optString("companyId").isEmpty())
                filterObject.put("clientProfile.orgHierarchy.companyId", object.getString("companyId"));
            if(!clientId.isEmpty())
                filterObject.put("_id", new JSONObject().put("$regex", "(?i).*"+clientId+".*"));
            if(!object.optString("clientCategory").isEmpty())
                filterObject.put("clientProfile.clientDetails.clientCategory", object.getString("clientCategory"));
            if(!object.optString("clientSubCategory").isEmpty())
                filterObject.put("clientProfile.clientDetails.clientSubCategory", object.getString("clientSubCategory"));
            if(!clientName.isEmpty()) {
                String value = "(?i).*"+clientName+".*";
                filterObject.putOpt("clientProfile.clientDetails.clientName", new JSONObject().put("$regex", value));
            }
            JSONArray b2bNames = getB2BClientNames(filterObject);
            return b2bNames;
        }
       else if(clientType.equals("B2C") || clientType.equalsIgnoreCase("corporate")) {

            if(!clientId.isEmpty())
                filterObject.put("_id", new JSONObject().put("$regex", "(?i).*"+clientId+".*"));
            if(!object.optString("companyId").isEmpty())
                filterObject.put("clientProfile.orgHierarchy.companyId", object.getString("companyId"));
            if(!object.optString("clientCategory").isEmpty())
                filterObject.put("clientProfile.clientStructure.clientCategory", object.getString("clientCategory"));
            if(!object.optString("clientSubCategory").isEmpty())
                filterObject.put("clientProfile.clientStructure.clientSubCategory", object.getString("clientSubCategory"));
            if(!clientName.isEmpty()) {
                String value = "(?i).*"+clientName+".*";
                filterObject.putOpt("clientProfile.clientStructure.clientName", new JSONObject().put("$regex", value));
            }
            JSONArray b2cNames = getB2CClientNames(filterObject);
            return b2cNames;
        }
        else{

            if(!object.optString("companyId").isEmpty())
                filterObject.put("clientProfile.orgHierarchy.companyId", object.getString("companyId"));
            if(!clientId.isEmpty())
                filterObject.put("_id", new JSONObject().put("$regex", "(?i).*"+clientId+".*"));
            if(!object.optString("clientCategory").isEmpty())
                filterObject.put("clientProfile.clientDetails.clientCategory", object.getString("clientCategory"));
            if(!object.optString("clientSubCategory").isEmpty())
                filterObject.put("clientProfile.clientDetails.clientSubCategory", object.getString("clientSubCategory"));
            if(!clientName.isEmpty()) {
                String value = "(?i).*"+clientName+".*";
                filterObject.putOpt("clientProfile.clientDetails.clientName", new JSONObject().put("$regex", value));
            }
            JSONArray b2bNames = getB2BClientNames(filterObject);
            filterObject = new JSONObject();
            in = new JSONArray();

            if(!clientId.isEmpty())
                filterObject.put("_id", new JSONObject().put("$regex", "(?i).*"+clientId+".*"));
            if(!object.optString("companyId").isEmpty())
                filterObject.put("clientProfile.orgHierarchy.companyId", object.getString("companyId"));
            if(!object.optString("clientCategory").isEmpty())
                filterObject.put("clientProfile.clientStructure.clientCategory", object.getString("clientCategory"));
            if(!object.optString("clientSubCategory").isEmpty())
                filterObject.put("clientProfile.clientStructure.clientSubCategory", object.getString("clientSubCategory"));
            if(!clientName.isEmpty()) {
                String value = "(?i).*"+clientName+".*";
                filterObject.putOpt("clientProfile.clientStructure.clientName", new JSONObject().put("$regex", value));
            }
            JSONArray names = getB2CClientNames(filterObject);
            for(int j=0;j<b2bNames.length();j++){
                names.put(b2bNames.getJSONObject(j));
            }
            return names;
        }
    }

    @Override
    public JSONArray getCorporateTravellersClient(JSONObject object) {
        JSONObject filterObject = new JSONObject();
        JSONArray orArray = new JSONArray();
        String clientId = object.optString("clientId");
        String travellerName = object.optString("travellerName");

        if (!clientId.isEmpty())
            filterObject.put("_id", new JSONObject().put("$regex", "(?i).*" + clientId + ".*"));
        if (!travellerName.isEmpty()) {
            String value = "(?i).*" + travellerName + ".*";
            orArray.put(new JSONObject().putOpt("travellerDetails.employee.firstName", new JSONObject().put("$regex", value)));
            orArray.put(new JSONObject().putOpt("travellerDetails.employee.lastName", new JSONObject().put("$regex", value)));
            filterObject.put("$or", orArray);
        }
        JSONArray names = getTravellerNames(filterObject);
        return names;
    }

    @Override
    public JSONArray getUsersFromUserManagement(JSONObject object) {

        JSONArray res = new JSONArray();
        String companyId = object.optString("companyId");
        String userName = object.optString("name");
        String userId = object.optString("id");
        JSONObject filterObj = new JSONObject();
        if(!companyId.isEmpty())
            filterObj.put("userDetails.companies.companyId", companyId);
        if(!userId.isEmpty())
            filterObj.put("_id", userId);
        JSONArray orArray = new JSONArray();
        if(!userName.isEmpty()) {
            String value = "(?i).*" + userName + ".*";
            orArray.put(new JSONObject().putOpt("userDetails.firstName", new JSONObject().put("$regex", value)));
            orArray.put(new JSONObject().putOpt("userDetails.lastName", new JSONObject().put("$regex", value)));
            filterObj.put("$or", orArray);
        }
        String url = userManagementUrl + "/user";
        url = url + "?filter=" + filterObj.toString();

        URI uri = UriComponentsBuilder.fromUriString(url).build().encode().toUri();
        String resStr = null;
        try {
            resStr = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
        } catch (Exception e) {
            logger.error("Error in fetching details of user" + e);
        }
        if(resStr==null || resStr.length()==0)
            return res;

        JSONArray response = new JSONObject(new JSONTokener(resStr)).optJSONArray("data");
        for(int i=0;i<response.length();i++){
            JSONObject user = new JSONObject();
            user.put("id", response.getJSONObject(i).getString("_id"));
            user.put("firstName", response.getJSONObject(i).getJSONObject("userDetails").getString("firstName"));
            user.put("lastName", response.getJSONObject(i).getJSONObject("userDetails").getString("lastName"));
            user.put("name", user.getString("firstName") + " " + user.getString("lastName"));
            res.put(user);
        }
        return res;
    }

    private JSONArray getTravellerNames(JSONObject filterObject) {

        JSONArray names = new JSONArray();
        String URL = getCorpTravellerURL + filterObject.toString();
        logger.info("URL to get Corporate Travellers Names is: " + URL);
        URI uri = UriComponentsBuilder.fromUriString(URL).build().encode().toUri();
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = mdmRestUtils.exchange(uri, HttpMethod.GET, null, String.class);
        } catch (OperationException e) {
            e.printStackTrace();
            return names;
        }
        String res = responseEntity.getBody();
        if(res==null)
            return names;

        JSONObject result = new JSONObject(res);
        JSONArray data = result.getJSONArray("data");
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<data.length();i++){
            try{
                JSONObject dtls = new JSONObject();
                sb.append(data.getJSONObject(i).getJSONObject("travellerDetails").getJSONObject("employee").getString("firstName"));
                sb.append(" ");
                sb.append(data.getJSONObject(i).getJSONObject("travellerDetails").getJSONObject("employee").getString("lastName"));
                dtls.put("travellerName" , sb.toString());
                dtls.put("clientId" , data.getJSONObject(i).getString("_id"));
                names.put(dtls);
                sb.setLength(0);
            }catch (Exception e){
                continue;
            }
        }
        return names;
    }

    private JSONArray getB2BClientNames(JSONObject filterObject) {

        JSONArray names = new JSONArray();
        String URL = getClientsURL + "clientB2B?filter=" + filterObject.toString();

        logger.info("URL to get B2B Client Names are: " + URL);
        URI uri = UriComponentsBuilder.fromUriString(URL).build().encode().toUri();
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = mdmRestUtils.exchange(uri, HttpMethod.GET, null, String.class);
        } catch (OperationException e) {
            e.printStackTrace();
            return names;
        }
        String b2bNames = responseEntity.getBody();
        if(b2bNames==null)
            return names;

        JSONObject result = new JSONObject(b2bNames);
        JSONArray data = result.getJSONArray("data");
        for(int i=0;i<data.length();i++){
            try{
                JSONObject dtls = new JSONObject();
                dtls.put("clientName" , data.getJSONObject(i).getJSONObject("clientProfile").getJSONObject("clientDetails").getString("clientName"));
                dtls.put("clientId" , data.getJSONObject(i).getString("_id"));
                names.put(dtls);

            }catch (Exception e){
                continue;
            }
        }
        return names;
    }

    private JSONArray getB2CClientNames(JSONObject filterObject) {

        JSONArray names = new JSONArray();
        String URL = getClientsURL + "corpClient?filter=" + filterObject.toString();

        logger.info("URL to get B2B Client Names are: " + URL);
        URI uri = UriComponentsBuilder.fromUriString(URL).build().encode().toUri();
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = mdmRestUtils.exchange(uri, HttpMethod.GET, null, String.class);
        } catch (OperationException e) {
            e.printStackTrace();
            return names;
        }
        String b2bNames = responseEntity.getBody();
        if(b2bNames==null)
            return names;

        JSONObject result = new JSONObject(b2bNames);
        JSONArray data = result.getJSONArray("data");
        for(int i=0;i<data.length();i++){
            try{
                JSONObject dtls = new JSONObject();
                dtls.put("clientName" , data.getJSONObject(i).getJSONObject("clientProfile").getJSONObject("clientStructure").getString("clientName"));
                dtls.put("clientId" , data.getJSONObject(i).getString("_id"));
                names.put(dtls);
            }catch (Exception e){
                continue;
            }
        }
        return names;
    }

    @Override
    public Map<String, String> getB2CClientNames(List<String> clientIDs) throws OperationException {

        HashMap<String, String> clientNamesMap = new HashMap<>();
        try {
            JSONObject filterParams = new JSONObject();
            JSONArray clientIDsList = new JSONArray();
            for (String aClientID : clientIDs) {
                if (aClientID != null && aClientID.trim().length() > 0) {
                    clientIDsList.put(aClientID);
                }
            }

            filterParams.put("_id", clientIDsList);

            String URL = getClientsURL + "corpTraveller?filter=" + filterParams.toString() + "&select=travellerDetails";
            logger.info("URL to get B2C Client Names are: " + URL);
            URI uri = UriComponentsBuilder.fromUriString(URL).build().encode().toUri();
            ResponseEntity<String> responseEntity = mdmRestUtils.exchange(uri, HttpMethod.GET, null, String.class);
            String responseInString = responseEntity.getBody();
            List<String> clientDetailsList = jsonFilter.getChildrenCollection(responseInString, "$.data", String.class);
            if (clientDetailsList != null && clientDetailsList.size() > 0) {
                for (String aCustomerProfile : clientDetailsList) {
                    String firstName = jsonFilter.getAttributeValue(aCustomerProfile, "$.travellerDetails.employee.firstName", String.class);
                    String lastName = jsonFilter.getAttributeValue(aCustomerProfile, "$.travellerDetails.employee.lastName", String.class);
                    String clientID = jsonFilter.getAttributeValue(aCustomerProfile, "$._id", String.class);
                    clientNamesMap.put(clientID, firstName + " " + lastName);
                }
            }
        } catch (Exception e) {
            OperationException anErr = new OperationException("Unable to get B2C Client Names", e.getMessage());
            //e.printStackTrace();
        }
        return clientNamesMap;
    }

    @Override
    public String getB2BClientDetails(String clientID) throws OperationException {
        String clientDetails = null;
        try {
            JSONObject filterParams = new JSONObject();
            JSONArray clientIDsList = new JSONArray();

            if (clientID != null && clientID.trim().length() > 0) {
                clientIDsList.put(clientID);
            }

            filterParams.put("_id", clientIDsList);

            String URL = getClientsURL + "clientB2B?filter=" + filterParams.toString();
            logger.info("URL to get B2B Client Details is: " + URL);
            URI uri = UriComponentsBuilder.fromUriString(URL).build().encode().toUri();
            ResponseEntity<String> responseEntity = mdmRestUtils.exchange(uri, HttpMethod.GET, null, String.class);
            clientDetails = responseEntity.getBody();
            clientDetails = jsonFilter.getChildJSON(clientDetails, "$.data[0]");
//            logger.debug("B2B client details response: " + clientDetails);
        } catch (Exception e) {
            OperationException anErr = new OperationException("Unable to get B2B client details:", e.getMessage());
            logger.error("Unable to get B2B client details", e);
            //e.printStackTrace();
        }
        return clientDetails;
    }

    @Override
    public String getB2CClientDetails(String clientID) throws OperationException {
        String clientDetails = null;
        try {
            JSONObject filterParams = new JSONObject();
            JSONArray clientIDsList = new JSONArray();
            if (clientID != null && clientID.trim().length() > 0) {
                clientIDsList.put(clientID);
            }

            filterParams.put("_id", clientIDsList);

            String URL = getClientsURL + "corpTraveller?filter=" + filterParams.toString();
            logger.info("URL to get B2C Client Details is: " + URL);
            URI uri = UriComponentsBuilder.fromUriString(URL).build().encode().toUri();
            ResponseEntity<String> responseEntity = mdmRestUtils.exchange(uri, HttpMethod.GET, null, String.class);
            clientDetails = responseEntity.getBody();
            clientDetails = jsonFilter.getChildJSON(clientDetails, "$.data[0]");
//            logger.info("B2C client details response: " + clientDetails);
        } catch (Exception e) {
            OperationException anErr = new OperationException("", e.getMessage());
            logger.error(e.getMessage(), e);

        }
        return clientDetails;
    }

    @Override
    public ZonedDateTime getClientKPI(String clientId, String clientMarket, MDMClientType clientType) throws OperationException {
        if (MDMClientType.B2B == clientType) {

        } else if (MDMClientType.B2C == clientType) {
            URI uri = UriComponentsBuilder.fromUriString(getClientType).build().encode().toUri();
            String clientTypeResponse = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
            String clientTypeId = jsonFilter.getAttributeValue(clientTypeResponse, "$.data[:1]._id", String.class);

        } else {
            logger.info("Unable to get Client KPI for client details");
            throw new OperationException("Other client KPI support is not provided Yet");
        }
        return null;
    }

    public String getClientEmailId(String clientId, MDMClientType clientType) throws OperationException {
        try {
            if (MDMClientType.B2B == clientType) {
                return jsonFilter.getAttributeValue(getB2BClientDetails(clientId), "$.adminUserDetails.users[0].email", String.class);
            } else if (MDMClientType.B2C == clientType) {
                return jsonFilter.getAttributeValue(getB2CClientDetails(clientId), "  $.travellerDetails.employmentDetails.officeEmailId", String.class);
            }
        } catch (Exception e) {
            logger.error("Error occurred in loading client email - " + clientId + " client type: " + clientType.getClientType(), e);
        }

        return null;
    }
}
