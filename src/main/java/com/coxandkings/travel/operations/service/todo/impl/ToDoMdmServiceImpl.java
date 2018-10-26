package com.coxandkings.travel.operations.service.todo.impl;

import com.coxandkings.travel.operations.enums.common.MDMClientType;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.repository.todo.ToDoTaskRepository;
import com.coxandkings.travel.operations.resource.todo.ToDoClientResource;
import com.coxandkings.travel.operations.resource.todo.ToDoCompanyMarketResource;
import com.coxandkings.travel.operations.resource.todo.ToDoCompanyResource;
import com.coxandkings.travel.operations.resource.todo.ToDoUserResource;
import com.coxandkings.travel.operations.service.mdmservice.ClientMasterDataService;
import com.coxandkings.travel.operations.service.mdmservice.CompanyMasterDataService;
import com.coxandkings.travel.operations.service.refund.RefundMDMService;
import com.coxandkings.travel.operations.service.todo.ToDoMdmService;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ToDoMdmServiceImpl implements ToDoMdmService {

    @Autowired
    MDMRestUtils mdmRestUtils;

    @Autowired
    RefundMDMService refundMDMService;

    @Value(value = "${todo.mdm.get.user}")
    private String userUrl;

    @Value(value = "${todo.mdm.company}")
    private String companyUrl;

    @Value("${mdm.common.client.b2b_client}")
    private String clientB2BURL;

    @Value(value = "${todo.mdm.company_market}")
    private String companyMarketUrl;

    @Value("${mdm.product}")
    private String getProductNameById;

    @Autowired
    private ClientMasterDataService clientService;

    @Autowired
    private CompanyMasterDataService companyService;

    @Autowired
    ToDoTaskRepository toDoTaskRepository;
    @Autowired
    private RestUtils restUtils;
//    @Override
//    public List<ToDoCompanyResource> getCompanies(String groupCompanyId) throws JSONException, IOException, OperationException {
//        JSONObject groupCompanyConstraint = new JSONObject();
//        groupCompanyConstraint.put("groupOfCompaniesId", groupCompanyId);
//        String filterString = groupCompanyConstraint.toString();
//        String fireUrl = companyUrl + filterString;
//        System.out.println(fireUrl);
//        StringBuffer stringBuffer = new StringBuffer();
//
//        for(int i = 0;i < fireUrl.toCharArray().length;i++) {
//            if(fireUrl.charAt(i) == '"') {
//                stringBuffer = stringBuffer.append('\\');
//                stringBuffer = stringBuffer.append('"');
//            } else if(fireUrl.charAt(i) == '{') {
//                stringBuffer = stringBuffer.append('{');
//                stringBuffer = stringBuffer.append('{');
//            } else if(fireUrl.charAt(i) == '}') {
//                stringBuffer = stringBuffer.append('}');
//                stringBuffer = stringBuffer.append('}');
//            }
//            else {
//                stringBuffer = stringBuffer.append(fireUrl.charAt(i));
//            }
//        }
//
//        fireUrl = stringBuffer.toString();
//        String response = mdmRestUtils.exchange(fireUrl, HttpMethod.GET, String.class);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        List<ToDoCompanyResource> companyResources = new LinkedList<>();
//
//        objectMapper.readTree(response).at("/data").elements().forEachRemaining(tempNode -> {
//            String companyName = tempNode.at("/name").asText();
//            String id = tempNode.findValue("_id").asText();
//
//            companyResources.add(new ToDoCompanyResource(companyName, id));
//        });
//
//        return companyResources;
//    }


    public ToDoCompanyResource getCompanyById(String companyId) throws JSONException, OperationException, IOException {

        ToDoCompanyResource aCompanyResource = null;
        if (companyId == null || companyId.trim().length() == 0) {
            return aCompanyResource;
        }

        ArrayList<String> companiesIDList = new ArrayList<>();
        companiesIDList.add(companyId);
        Map<String, String> companyNamesMap = companyService.getCompanyNames(companiesIDList);
        if (companyNamesMap != null && companyNamesMap.size() > 0 && companyNamesMap.containsKey(companyId)) {
            aCompanyResource = new ToDoCompanyResource(companyNamesMap.get(companyId), companyId);
        }

        return aCompanyResource;
    }

    @Override
    public List<ToDoCompanyResource> getCompanies(List<ToDoCompanyResource> toDoCompanyResources) throws JSONException, OperationException, IOException {
        List<String> companyIds = new ArrayList<>();
        for (ToDoCompanyResource toDoTaskResource : toDoCompanyResources) {
            if (!StringUtils.isEmpty(toDoTaskResource.getId())) {
                companyIds.add(toDoTaskResource.getId());
            }
        }

        if (companyIds == null || companyIds.size() == 0) {
            return null;
        }

        JSONObject includeConstraint = new JSONObject();
        includeConstraint.put("$in", companyIds);
        JSONObject constraint = new JSONObject();
        constraint.put("_id", includeConstraint);

        String filterString = constraint.toString();
        String fireUrl = companyUrl + filterString;
        URI uri = UriComponentsBuilder.fromUriString(fireUrl).build().encode().toUri();
        ResponseEntity<String> responseEntity = mdmRestUtils.exchange(uri, HttpMethod.GET, null, String.class);
        String response = responseEntity.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        List<ToDoCompanyResource> companyResources = new LinkedList<>();

        objectMapper.readTree(response).at("/data").elements().forEachRemaining(tempNode -> {
            String companyName = tempNode.at("/name").asText();
            String id = tempNode.findValue("_id").asText();

            companyResources.add(new ToDoCompanyResource(companyName, id));
        });

        return companyResources;
    }

    @Override
    public List<ToDoClientResource> getClients(List<ToDoClientResource> toDoClientResources) throws JSONException, OperationException, IOException {
        if (toDoClientResources == null || toDoClientResources.isEmpty()) {
            return new LinkedList<>();
        }

        List<String> clientIds = toDoClientResources.stream().map(ToDoClientResource::getId).collect(Collectors.toList());

        JSONObject includeConstraint = new JSONObject();
        includeConstraint.put("$in", clientIds);
        JSONObject constraint = new JSONObject();
        constraint.put("_id", includeConstraint);

        String filterString = constraint.toString();
        String fireUrl = clientB2BURL + "?filter=" + filterString;
        URI uri = UriComponentsBuilder.fromUriString(fireUrl).build().encode().toUri();
        String response = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        List<ToDoClientResource> clientResources = new LinkedList<>();

        objectMapper.readTree(response).at("/data").elements().forEachRemaining(tempNode -> {
            String clientName = tempNode.at("/clientProfile/clientDetails/clientName").asText();
            String id = tempNode.findValue("_id").asText();

            clientResources.add(new ToDoClientResource(id, clientName));
        });

        return clientResources;
    }

    @Override
    public List<ToDoCompanyMarketResource> getCompanyMarkets(String companyId) throws JSONException, OperationException, IOException {
        JSONObject companyIdConstraint = new JSONObject();
        companyIdConstraint.put("companyId", companyId);

        String filterString = companyIdConstraint.toString();
        String fireUrl = companyMarketUrl + filterString;
        URI uri = UriComponentsBuilder.fromUriString(fireUrl).build().encode().toUri();
        String response = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        List<ToDoCompanyMarketResource> companyMarketResources = new LinkedList<>();

        Iterator<JsonNode> jsonNodeIterator = objectMapper.readTree(response).at("/data").elements();
        if (jsonNodeIterator.hasNext()) {
            jsonNodeIterator.forEachRemaining(tempNode -> {
                String companyName = tempNode.at("/name").asText();
                String id = tempNode.findValue("_id").asText();

                companyMarketResources.add(new ToDoCompanyMarketResource(companyName, id));
            });
        }
//        else {
//            throw new OperationException("No user id matches any user in the database");
//        }

        if (!companyMarketResources.isEmpty())
            return companyMarketResources;
        else
            return null;

    }

    @Override
    public Map<String, ToDoUserResource> getUsersMap(List<ToDoUserResource> toDoUserResources) throws JSONException, OperationException, IOException {
        Map<String, ToDoUserResource> userResources = new HashMap<>();
        if (toDoUserResources.isEmpty()) {
            return userResources;
        }
        List<String> userIds = toDoUserResources.stream().map(ToDoUserResource::getId).collect(Collectors.toList());

        JSONObject includeConstraint = new JSONObject();
        includeConstraint.put("$in", userIds);
        JSONObject constraint = new JSONObject();
        constraint.put("_id", includeConstraint);

        String filterString = constraint.toString();
        String fireUrl = userUrl + filterString;
        URI uri = UriComponentsBuilder.fromUriString(fireUrl).build().encode().toUri();
        String response = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);

        ObjectMapper objectMapper = new ObjectMapper();

        Iterator<JsonNode> jsonNodeIterator = objectMapper.readTree(response).at("/data").elements();
        if (jsonNodeIterator.hasNext()) {
            jsonNodeIterator.forEachRemaining(tempNode -> {
                String firstName = tempNode.at("/userDetails/firstName").asText();
                String lastName = tempNode.at("/userDetails/lastName").asText();
                String email = tempNode.at("/userIdentification/userId").asText();
                String id = tempNode.findValue("_id").asText();

                userResources.put(id, new ToDoUserResource(id, firstName + " " + lastName, email));
            });
        }
        return userResources;
    }

    @Override
    public List<ToDoUserResource> getUsers(List<ToDoUserResource> toDoUserResources) throws JSONException, OperationException, IOException {
        List<ToDoUserResource> userResources = new LinkedList<>();
        if (toDoUserResources.isEmpty()) {
            return userResources;
        }

        List<String> userIds = new ArrayList<>();
        for (ToDoUserResource toDoUserResource : toDoUserResources) {
            if (!StringUtils.isEmpty(toDoUserResource.getId())) {
                userIds.add(toDoUserResource.getId());
            }
        }

        if (userIds == null || userIds.size() == 0) {
            return userResources;
        }

        JSONObject includeConstraint = new JSONObject();
        includeConstraint.put("$in", userIds);
        JSONObject constraint = new JSONObject();
        constraint.put("_id", includeConstraint);

        String filterString = constraint.toString();
        String fireUrl = userUrl + filterString;
        URI uri = UriComponentsBuilder.fromUriString(fireUrl).build().encode().toUri();
        String response = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);

        ObjectMapper objectMapper = new ObjectMapper();

        Iterator<JsonNode> jsonNodeIterator = objectMapper.readTree(response).at("/data").elements();
        if (jsonNodeIterator.hasNext()) {
            jsonNodeIterator.forEachRemaining(tempNode -> {
                String firstName = tempNode.at("/userDetails/firstName").asText();
                String lastName = tempNode.at("/userDetails/lastName").asText();
                String email = tempNode.at("/userIdentification/userId").asText();
                String id = tempNode.findValue("_id").asText();

                userResources.add(new ToDoUserResource(id, firstName + " " + lastName, email));
            });
        }
//        else {
//            throw new OperationException("No user id matches any user in the database");
//        }

        return userResources;
    }

    @Override
    public ToDoClientResource getClientById(String clientType, String clientId, String clientCategory, String clientSubCategory) throws OperationException {

        ToDoClientResource aClientResource = new ToDoClientResource();

        if (!StringUtils.isEmpty(clientType)) {
            aClientResource.setClientType(clientType);
        }

        if (!StringUtils.isEmpty(clientCategory)) {
            aClientResource.setClientCategory(clientCategory);
        }

        if (!StringUtils.isEmpty(clientSubCategory)) {
            aClientResource.setClientSubCategory(clientSubCategory);
        }

        ToDoClientResource aClientResourceWithName = getClientById(clientType, clientId);
        if (aClientResourceWithName != null) {
            aClientResource.setClientName(aClientResourceWithName.getClientName());
        }


        return aClientResource;
    }

    @Override
    public ToDoClientResource getClientById(String clientType, String clientId) throws OperationException {

        if (StringUtils.isEmpty(clientId) || StringUtils.isEmpty(clientType)) {
            return null;
        }

        MDMClientType aClientType = MDMClientType.fromString(clientType);
        // List<ToDoClientResource> clientResources = new LinkedList<>();

        ToDoClientResource aClientResource = null;
        if (aClientType != null) {
            switch (aClientType) {
                case B2B: {
                    ArrayList<String> clientIDList = new ArrayList<>();
                    clientIDList.add(clientId);
                    HashMap<String, String> clientNamesMap = (HashMap<String, String>) clientService.getB2BClientNames(clientIDList);
                    if (clientNamesMap.size() > 0) {
                        aClientResource = new ToDoClientResource(clientId, clientNamesMap.get(clientId));
                    }
                }
                break;

                case B2C: {
                    ArrayList<String> clientIDList = new ArrayList<>();
                    clientIDList.add(clientId);
                    HashMap<String, String> clientNamesMap = (HashMap<String, String>) clientService.getB2CClientNames(clientIDList);
                    if (clientNamesMap.size() > 0) {
                        aClientResource = new ToDoClientResource(clientId, clientNamesMap.get(clientId));
                    }
                }
                break;
            }
/*//            clientService
            String response = refundMDMService.getB2BClient(clientId);
            ObjectMapper objectMapper = new ObjectMapper();


            JsonNode jsonNode = objectMapper.readTree(response);//.(tempNode -> {
            String clientName = jsonNode.at("/clientProfile/clientDetails/clientName").asText();
            String id = jsonNode.findValue("_id").asText();

            clientResources.add(new ToDoClientResource(id, clientName));*/
        }

        return aClientResource;
    }

    @Override
    public ToDoCompanyMarketResource getCompanyMarketById(String companyMarketId) {

        ToDoCompanyMarketResource toDoCompanyMarketResource = null;
        if(StringUtils.isEmpty(companyMarketId)){
            return toDoCompanyMarketResource;
        }

        String companyMarketName = companyService.getCompanyMarketNameByID(companyMarketId);

        return new ToDoCompanyMarketResource(companyMarketName, companyMarketId);
    }


    @Override
    public ToDoUserResource getUserById(String userId) throws JSONException, OperationException, IOException {
        ToDoUserResource anUserInfo = null;

        if (userId != null && userId.trim().length() > 0) {
            JSONObject userIdConstraint = new JSONObject();
            userIdConstraint.put("_id", userId);
            String constraint = userIdConstraint.toString();
            String fireUrl = userUrl + constraint;
            URI uri = UriComponentsBuilder.fromUriString(fireUrl).build().encode().toUri();
            String response= RestUtils.exchange(uri,HttpMethod.GET, restUtils.getHttpEntity(), String.class).getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            List<ToDoUserResource> userResources = new LinkedList<>();

            Iterator<JsonNode> jsonNodeIterator = objectMapper.readTree(response).at("/data").elements();
            if (jsonNodeIterator.hasNext()) {
                jsonNodeIterator.forEachRemaining(tempNode -> {
                    String firstName = tempNode.at("/userDetails/firstName").asText();
                    String lastname = tempNode.at("/userDetails/lastName").asText();
                    String email = tempNode.at("/userIdentification/userId").asText();
                    String id = tempNode.findValue("_id").asText();

                    userResources.add(new ToDoUserResource(id, firstName + " " + lastname, email));
                });
            }

            if (!userResources.isEmpty()) {
                anUserInfo = userResources.get(0);
            } else {
                return null;
            }
        }

        return anUserInfo;
    }

    @Override
    public List<ToDoUserResource> assign() throws IOException, OperationException {
        String users = mdmRestUtils.exchange(userUrl, HttpMethod.GET, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        List<ToDoUserResource> userResources = new LinkedList<>();

        Iterator<JsonNode> jsonNodeIterator = objectMapper.readTree(users).at("/data").elements();
        if (jsonNodeIterator.hasNext()) {
            jsonNodeIterator.forEachRemaining(tempNode -> {
                String email = tempNode.at("/userIdentification/userId").asText();
                String firstName = tempNode.at("/userDetails/firstName").asText();
                String lastName = tempNode.at("/userDetails/lastName").asText();
                String id = tempNode.findValue("_id").asText();
                userResources.add(new ToDoUserResource(id, firstName + " " + lastName, email));
            });
        }
//        else {
//            throw new OperationException("No user id matches any user in the database");
//        }

        userResources.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        return userResources;
    }

    @Override
    public List<ToDoUserResource> assign(String functionalArea) {

        return null;
    }

    @Override
    public List<ToDoCompanyResource> getCompanies(String userId) throws JSONException, OperationException, IOException {
        JSONObject userIdConstraint = new JSONObject();
        userIdConstraint.put("" + "_id", userId);
        String constraint = userIdConstraint.toString();
        String fireUrl = userUrl + constraint;
        URI uri = UriComponentsBuilder.fromUriString(fireUrl).build().encode().toUri();
        String response = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        List<ToDoCompanyResource> companyResources = new LinkedList<>();

        Iterator<JsonNode> dataIterator = objectMapper.readTree(response).at("/data").elements();
        if (dataIterator.hasNext()) {
            JsonNode dataNode = dataIterator.next();
            Iterator<JsonNode> companyIterator = dataNode.at("/userDetails/companies").elements();
            companyIterator.forEachRemaining(tempNode -> {
                String companyId = tempNode.at("/companyId").asText();
                String companyName = tempNode.at("/companyName").asText();

                companyResources.add(new ToDoCompanyResource(companyName, companyId));
            });
        } else {
            throw new OperationException("The user with given id does not exists");
        }

        if (!companyResources.isEmpty()) {
            return companyResources;
        } else {
            return null;
        }
    }

    @Override
    public List<ToDoUserResource> getSubordinates(String userId) throws JSONException, OperationException, IOException {
        JSONObject includeConstraint = new JSONObject();
        includeConstraint.put("userDetails.reportingManager", userId);

        String filterString = includeConstraint.toString();
        String fireUrl = userUrl + filterString;
        URI uri = UriComponentsBuilder.fromUriString(fireUrl).build().encode().toUri();
        String response = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        List<ToDoUserResource> userResources = new LinkedList<>();

        Iterator<JsonNode> jsonNodeIterator = objectMapper.readTree(response).at("/data").elements();
        if (jsonNodeIterator.hasNext()) {
            jsonNodeIterator.forEachRemaining(tempNode -> {
                String firstName = tempNode.at("/userDetails/firstName").asText();
                String lastName = tempNode.at("/userDetails/lastName").asText();
                String email = tempNode.at("/userIdentification/userId").asText();
                String id = tempNode.findValue("_id").asText();

                userResources.add(new ToDoUserResource(id, firstName + " " + lastName, email));
            });
        }

        return userResources;
    }

    @Override
    public List<ToDoUserResource> getFileHandlersForSearch() {
        List<String> fileHandlerIds = toDoTaskRepository.getAllFileHandlers();
        List<ToDoUserResource> toDoUserResources = fileHandlerIds.stream().map(a -> new ToDoUserResource(a, null, null)).collect(Collectors.toList());
        try {
            toDoUserResources = getUsers(toDoUserResources);
        } catch (JSONException | OperationException | IOException e) {
            e.printStackTrace();
        }

        return toDoUserResources;
    }

    @Override
    public String getProductById(String id) {
        JSONObject jsonObject = new JSONObject();
        String productName = new String();
        try {
            jsonObject.put("_id", id);
            String filter = jsonObject.toString();
            System.out.println("getProductById+filter = " + getProductNameById + filter);
            URI uri = UriComponentsBuilder.fromUriString(getProductNameById + filter).build().encode().toUri();
            String response = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            if (!StringUtils.isEmpty(response) && objectMapper.readTree(response).size() > 0) {
                productName = objectMapper.readTree(response).elements().next().path("data").path("value").textValue();
            }
        } catch (JSONException | OperationException | IOException e) {
            e.printStackTrace();
        }
        return productName;
    }
}