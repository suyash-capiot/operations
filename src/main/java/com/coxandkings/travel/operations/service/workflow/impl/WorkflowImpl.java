package com.coxandkings.travel.operations.service.workflow.impl;

import com.coxandkings.travel.operations.criteria.workflow.WorkFlowFilter;
import com.coxandkings.travel.operations.enums.workflow.WorkflowOperation;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.workflow.Doc;
import com.coxandkings.travel.operations.model.workflow.WorkFlow;
import com.coxandkings.travel.operations.service.mailroomanddispatch.MailRoomService;
import com.coxandkings.travel.operations.service.thirdPartyVoucher.SupplierConfigurationService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.service.workflow.ReleaseLockService;
import com.coxandkings.travel.operations.service.workflow.WorkflowService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.modeshape.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

@Service
public class WorkflowImpl implements WorkflowService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Value(value = "${mdm.workflow-dictionary}")
    private String workflowDictionaryUrl;

    @Value(value = "${workflow.mdm.doc-update}")
    private String wfDocUpdateUrl;

    @Value(value = "${workflow.mdm.add}")
    private String workflowAddUrl;

    @Value(value = "${workflow.mdm.get}")
    private String workflowGetUrl;

    @Value(value = "${workflow.mdm.wf-update}")
    private String wfUpdateUrl;

    @Autowired
    private List<ReleaseLockService> services;

    @Autowired
    MailRoomService mailRoomService;

    @Autowired
    SupplierConfigurationService supplierConfigurationService;

    @Override
    public String saveWorkflow(Object object, WorkflowOperation workflowOperation,
                       String entityName, String userId) {

        ResponseEntity<String> workflowId = null;
        try {
            String dataDictionary = workflowDictionaryUrl +"entity/"+ entityName;
            String workflowAdd;
            HttpEntity<Object> requestEntity = getHttpEntity();
            ResponseEntity<String> dataObject = RestUtils.exchange(dataDictionary, HttpMethod.GET, requestEntity, String.class);

            JSONObject jsonObject = new JSONObject(dataObject.getBody());
            WorkFlow workFlow = new WorkFlow();
            workFlow.setCreateURL(jsonObject.getJSONObject("urlSet").getString("createURL"));
            workFlow.setDeleteURL(jsonObject.getJSONObject("urlSet").getString("deleteURL"));
            workFlow.setGetURL(jsonObject.getJSONObject("urlSet").getString("getURL"));
            workFlow.setUpdateURL(jsonObject.getJSONObject("urlSet").getString("updateURL"));
            workFlow.setServiceName(jsonObject.getJSONObject("urlSet").getString("serviceName"));
            workFlow.setWorkFlowOperationType("Create");
            workFlow.setCreatedBy(userId);
            workFlow.setType(entityName);
            Doc doc = new Doc();
            doc.setNewDoc(object);
            workFlow.setDoc(doc);

            if (workflowOperation.equals(workflowOperation.SUBMIT)) {
                workflowAdd = workflowAddUrl;
            } else {
                workflowAdd = workflowAddUrl+ "?draft=true";
            }
            requestEntity = getHttpEntity(workFlow);
            workflowId = RestUtils.exchange(workflowAdd, HttpMethod.POST, requestEntity, String.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (workflowId != null) {
            return workflowId.getBody();
        }
        return null;
    }

    @Override
    public String editMasterObject(Object newObject, String entityName, String userId, String masterId) throws OperationException{

        ResponseEntity<String> WorkflowId;
        String workflowId = null;
        try {
            String dataDictionary = workflowDictionaryUrl +"entity/"+ entityName;
            String workflowAdd;

            HttpEntity<Object> requestEntity = getHttpEntity();
            ResponseEntity<String> dataObject = RestUtils.exchange(dataDictionary, HttpMethod.GET, requestEntity ,String.class);
            JSONObject jsonObject = new JSONObject(dataObject.getBody());
            WorkFlow workFlow = new WorkFlow();
            workFlow.setCreateURL(jsonObject.getJSONObject("urlSet").getString("createURL"));
            workFlow.setDeleteURL(jsonObject.getJSONObject("urlSet").getString("deleteURL"));
            workFlow.setGetURL(jsonObject.getJSONObject("urlSet").getString("getURL"));
            workFlow.setUpdateURL(jsonObject.getJSONObject("urlSet").getString("updateURL"));
            workFlow.setServiceName(jsonObject.getJSONObject("urlSet").getString("serviceName"));
            workFlow.setWorkFlowOperationType("Update");
            workFlow.setCreatedBy(userId);
            workFlow.setType(entityName);
            Doc doc = new Doc();
            doc.setNewDoc(newObject);
            doc.setOldDoc(newObject);
            workFlow.setDoc(doc);
            workflowAdd = workflowAddUrl + "?draft=true";

            // creating workflow and returning its Id to make changes to it.
            WorkflowId = RestUtils.exchange(workflowAdd, HttpMethod.POST, getHttpEntity(workFlow), String.class);
            JSONObject workflowObject = new JSONObject(WorkflowId.getBody());
            workflowId = (workflowObject.getString("_id"));

        } catch (Exception e) {
            //TODO : Log an Exception
        }
        return workflowId;
    }

    @Override
    public String updateWorkflowDoc(Object object, WorkflowOperation workflowOperation, String id) throws OperationException{
        ResponseEntity<String> WorkflowId = null;
        try {
            String updateWorkflow ;
            if (workflowOperation.equals(workflowOperation.SUBMIT))
                updateWorkflow = wfDocUpdateUrl + id;
             else
                updateWorkflow = wfDocUpdateUrl + id + "?draft=true";

            WorkflowId = RestUtils.exchange(updateWorkflow, HttpMethod.PUT, getHttpEntity(object), String.class);

        } catch (Exception e) {
            throw new OperationException(Constants.UNABLE_TO_UPDATE_WORKFLOW, id);
        }
        return WorkflowId!=null ? WorkflowId.getBody() : null;
    }

    @Override
    public Page<WorkFlow> getWorkFlow(String type, String userId, JSONObject filterObject, JSONObject sortObject,
                              Integer page, Integer count) throws OperationException{

        ResponseEntity<Object> workFlows;
        filterObject.putOpt("type", type);
        filterObject.putOpt("status", new JSONObject().putOpt("$ne", "Approved"));
        JSONArray userFilter = new JSONArray();
        userFilter.put(new JSONObject().putOpt("dictionary.authorizationDetails.users.userId" ,userId));
        userFilter.put(new JSONObject().putOpt("createdBy", userId));
        filterObject.putOpt("$or", userFilter);

        try {
            String url = workflowGetUrl + "?filter={filter}";
            url = url + "&sort={sort}";
            url = url + "&count=" + count + "&page=" + page;
            workFlows = RestUtils.exchange(url, HttpMethod.GET, getHttpEntity(), Object.class, filterObject.toString(), sortObject.toString());

        } catch (Exception e) {
           throw new OperationException("Unable to get WorkFlow :", e.getMessage());
        }
        if (workFlows != null || workFlows != null) {
            List<WorkFlow> workflowDtls;
            LinkedHashMap map = (LinkedHashMap) workFlows.getBody();
            workflowDtls = objectMapper.convertValue(map.get("data"),
                    new TypeReference<List<WorkFlow>>() {
                    });
            Integer totalCount = (Integer) (map.get("totalCount"));
            Pageable pageable = new PageRequest(page, count);

            Page<WorkFlow> responseSet = new PageImpl<>(
                    new ArrayList<>(workflowDtls), pageable, totalCount);

            return responseSet;
        }
        return null;
    }

    @Override
    public Map<String, Object> getWorkFlows(String entityName, WorkFlowFilter workflowFilter, JSONObject filterObject,
                                            String sortCriteria, Integer pageNo, Integer size) throws OperationException {

        String userId = userService.getLoggedInUserId();
        ResponseEntity<Object> workFlows;
        filterObject.putOpt("type", entityName);

        if(workflowFilter!=null) {
            String workFlowStatus = workflowFilter.getStatus();
            if(workFlowStatus!=null && !workFlowStatus.isEmpty()) {
                filterObject.putOpt("status", workflowFilter.getStatus());
            }
        }

        JSONArray userFilter = new JSONArray();
        //TODO : There is no seperate approver screen in Ops to approve Workflows,
        //TODO: May get changed later.
        //TODO: Both the approver and the user who created the workflow should be able to access the workflow, but approver only when it is submitted
        //TODO: Handled temporarily for now, When sepereate Approver screen will be developed remove this.
        JSONArray approverFilter = new JSONArray();
        approverFilter.put(new JSONObject().put("dictionary.authorizationDetails.users.userId", userId));
        approverFilter.put(new JSONObject().put("status", new JSONObject().put("$nin", new JSONArray("[\"Draft\",\"Rejected\"]"))));

        userFilter.put(new JSONObject().putOpt("$and", approverFilter));
        userFilter.put(new JSONObject().putOpt("createdBy", userId));
        filterObject.putOpt("$or", userFilter);
        try {
            String url = workflowGetUrl + "?filter={filter}";
            url = url + "&sort={sort}";
            url = url + "&count=" + size + "&page=" + pageNo;
            workFlows = RestUtils.exchange(url, HttpMethod.GET, getHttpEntity(), Object.class, filterObject.toString(), sortCriteria);

        } catch (Exception e) {
            throw new OperationException(Constants.UNABLE_TO_GET_WORKFLOWS);
        }
        if (workFlows != null || workFlows != null) {
            List<WorkFlow> workflowDtls;
            LinkedHashMap map = (LinkedHashMap) workFlows.getBody();
            workflowDtls = objectMapper.convertValue(map.get("data"),
                    new TypeReference<List<WorkFlow>>() {
                    });

            return applyPagination(workflowDtls, size, pageNo, (Integer) map.get("totalCount"));
        }
        return null;
    }

    @Override
    public Object releaseLock(String workFlowId) throws OperationException {

        String workFlowStr = getWorkFlowById(workFlowId);
        JSONObject workFlowJson =null;
        if(!workFlowStr.isEmpty())
            workFlowJson = new JSONObject(workFlowStr);
        if(workFlowJson==null)
            return null;
        JSONArray dataJsonArr = workFlowJson.optJSONArray("data");
        if(dataJsonArr==null){
            throw new OperationException("no such workflow id found ");
        }

        if(dataJsonArr.length()== 0)
            throw new OperationException("no such workflow id found ");

        if(dataJsonArr.length()>1){
            // this will never happen..
            // TODO: in case code comes into this block the what to do...... ???
        }

        JSONObject dataJson = dataJsonArr.getJSONObject(0);

        String type = dataJson.optString("type");
        if(type.isEmpty())
            throw new OperationException("type is undefined in workflow response");

        ReleaseLockService service = serviceForWorkFlow(type);
        Object res = null;
        try{
            switch (type){
                case "Mailroom And Dispatch Master": mailRoomService.releaseMasterLock(workFlowId);
                        break;
                case "Mailroom And Dispatch Inbound": mailRoomService.releaseLock(workFlowId);
                    break;
                case "Mailroom And Dispatch Outbound": mailRoomService.releaseOutboundLock(workFlowId);
                    break;
                case "Third Party Vouchers": supplierConfigurationService.releaseLock(workFlowId);
                    break;

            }

            //Add all service calls of the all modules having workflow implementation

        }catch (OperationException oe){
            throw oe;
        }catch (Exception e){
            throw new OperationException(Constants.OPS_ERR_11200);
        }

        return res;
    }

    ReleaseLockService serviceForWorkFlow(String type)throws OperationException{
        for(ReleaseLockService service: services){
            if (service.isResponsibleFor(type)) {
                return service;
            }
        }
        throw new UnsupportedOperationException("Unsupported workflowType");
    }

    @Override
    public Map<String, Object> getWorkFlows(String type, String userId, JSONObject filterObject, String sort,
                               Integer page, Integer count) throws OperationException{

        ResponseEntity<Object> workFlows;
        filterObject.putOpt("type", type);
        filterObject.putOpt("status", new JSONObject().putOpt("$ne", "Approved"));
        JSONArray userFilter = new JSONArray();
        //Both the approver and the user who created the workflow should be able to access the workflow, but approver only when it is "Awaiting for Approval"
        JSONArray approverFilter = new JSONArray();
        approverFilter.put(new JSONObject().put("dictionary.authorizationDetails.users.userId", userId));
        approverFilter.put(new JSONObject().put("status", new JSONObject().put("$nin", new JSONArray("[\"Draft\",\"Rejected\"]"))));

        userFilter.put(new JSONObject().putOpt("$and", approverFilter));
        userFilter.put(new JSONObject().putOpt("createdBy", userId));
        filterObject.putOpt("$or", userFilter);
        try {
//            String url = workflowGetUrl + "?filter={filter}";
//            url = url + "&sort={sort}";
//            url = url + "&count=" + count + "&page=" + page;
//            workFlows = RestUtils.exchange(url, HttpMethod.GET, getHttpEntity(), Object.class, filterObject.toString(), sort);

            String url = workflowGetUrl + "?filter="+filterObject.toString();
            if (!StringUtils.isEmpty(sort))
            {
                url = url + "&sort="+sort;
            }
            url = url + "&count=" + count + "&page=" + page;
            URI uri = UriComponentsBuilder.fromUriString(url).build().encode().toUri();
            workFlows = mdmRestUtils.exchange(uri,HttpMethod.GET,null,Object.class);
//            workFlows = RestUtils.exchange(url, HttpMethod.GET, getHttpEntity(), Object.class, filterObject.toString(), sort);

        } catch (Exception e) {
            throw new OperationException(Constants.UNABLE_TO_GET_WORKFLOWS);
        }
        if (workFlows != null || workFlows != null) {
            List<WorkFlow> workflowDtls;
            LinkedHashMap map = (LinkedHashMap) workFlows.getBody();
            workflowDtls = objectMapper.convertValue(map.get("data"),
                    new TypeReference<List<WorkFlow>>() {
                    });

            return applyPagination(workflowDtls, count, page, (Integer) map.get("totalCount"));
        }
        return null;
    }

    @Override
    public String getWorkFlowById(String workFlowId) throws OperationException{
        ResponseEntity<String> WorkflowId = null;
        Map<String, String> newMap = new HashMap<>();
        newMap.put("_id", workFlowId);
        JSONObject jsonObject = new JSONObject(newMap);

        try {
            String url = workflowGetUrl + "?filter={filter}";
            WorkflowId = RestUtils.exchange(url, HttpMethod.GET, getHttpEntity(), String.class, jsonObject);
        } catch (Exception e) {
            throw new OperationException(Constants.UNABLE_TO_GET_WORKFLOW, workFlowId);
        }
        return WorkflowId.getBody();
    }

    @Override
    public String releaseWorkflow(String workFlowId) {
        ResponseEntity<String> WorkflowId;
        try {
            // http://10.24.2.5:10011/workFlow/v1/WF100005434/lock/release
            String url = workflowGetUrl + workFlowId + "/lock/release";
            WorkflowId = RestUtils.exchange(url, HttpMethod.POST, getHttpEntity(), String.class);
            JSONObject workflow = new JSONObject(WorkflowId.getBody());
            if (workflow != null) {
                return workflow.getString("message");
            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }

    @Override
    public String saveWorkflowFilter(Object object, WorkflowOperation workflowOperation,
                                     String entityName, String UserId, Integer i) {

        ResponseEntity<String> WorkflowId = null;
        try {
            String dataDictionary = workflowDictionaryUrl +"entity/"+ entityName;
            String workflowAddUrl;
            ResponseEntity<String> dataObject = RestUtils.exchange(dataDictionary, HttpMethod.GET, getHttpEntity(), String.class);
            JSONObject jsonObject = new JSONObject(dataObject.getBody());
            WorkFlow workFlow = new WorkFlow();
            workFlow.setCreateURL(jsonObject.getJSONObject("urlSet").getString("createURL"));
            workFlow.setDeleteURL(jsonObject.getJSONObject("urlSet").getString("deleteURL"));
            workFlow.setGetURL(jsonObject.getJSONObject("urlSet").getString("getURL"));
            workFlow.setUpdateURL(jsonObject.getJSONObject("urlSet").getString("updateURL"));
            workFlow.setServiceName(jsonObject.getJSONObject("urlSet").getString("serviceName"));
            workFlow.setWorkFlowOperationType("Create");
            workFlow.setCreatedBy(UserId);
            workFlow.setType(entityName);
            Doc doc = new Doc();
            doc.setNewDoc(object);
            workFlow.setDoc(doc);

            if (workflowOperation.equals(WorkflowOperation.SUBMIT)) {
                workflowAddUrl = this.workflowAddUrl + "?nextIndex=" + i;
            } else {
                workflowAddUrl = this.workflowAddUrl + "?draft=true&nextIndex=" + i;
            }
            WorkflowId = RestUtils.exchange(workflowAddUrl, HttpMethod.POST, getHttpEntity(workFlow), String.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (WorkflowId != null) {
            return WorkflowId.getBody();
        }
        return null;

    }

    @Override
    public Integer getIndex(String entityName) {

        String dataDictionary = workflowDictionaryUrl +"entity/"+ entityName;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> dataObject;
        try {
            dataObject = RestUtils.exchange(dataDictionary, HttpMethod.GET, getHttpEntity(), String.class);
            JSONObject jsonObject = new JSONObject(dataObject.getBody());
            JSONArray toReturn = jsonObject.getJSONArray("dictionary");
            for (int i = 0; i < toReturn.length(); i++) {
                JSONObject objects = toReturn.getJSONObject(i);
                if ("Awaiting Supervisor Approval".equals(objects.getString("stepName"))) {
                    return i;
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String updateWorkflow(String workFlowid, JSONObject updateObject) {

        JSONObject filterObject = new JSONObject();
        filterObject.put("_id", workFlowid);
        String WorkflowId;
        try {
            String url = wfUpdateUrl + "?filter={filter}" + "&update={update}";
            WorkflowId = mdmRestUtils.exchange(url, HttpMethod.GET, String.class, filterObject.toString(), updateObject.toString());
            JSONObject workflow = new JSONObject(WorkflowId);
            if (workflow != null) {
                return workflow.getString("message");
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    public String deleteWorkflow(String workFlowid) {
        JSONObject filterObject = new JSONObject();
        filterObject.put("_id", workFlowid);

        JSONObject updateObject = new JSONObject();
        updateObject.put("deleted", true);
        String workFlow;
        try {
            String url = wfUpdateUrl + "?filter={filter}" + "&update={update}";
            workFlow = mdmRestUtils.exchange(url, HttpMethod.GET, String.class, filterObject.toString(), updateObject.toString());
           if(workFlow==null)
               return null;
            JSONObject workflow = new JSONObject(workFlow);
            if (workflow.optInt("nModified")!=0) {
                return "success";
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }


    private HttpEntity getHttpEntity(){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String token = userService.getLoggedInUserToken();
        headers.add("Authorization", token);
        return new HttpEntity(headers);
    }

    private HttpEntity getHttpEntity(Object object) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String token = userService.getLoggedInUserToken();
        headers.add("Authorization", token);
        return new HttpEntity(object, headers);
    }

    private Map<String, Object> applyPagination(List<WorkFlow> objects, Integer size, Integer page, Integer actualSize) {
        Map<String, Object> entity = new HashMap<>();
        entity.put("data", objects);
		entity.put("totalCount", actualSize);

        if (objects.isEmpty())
            return entity;

        entity.put("size", size);
        entity.put("page", page);

        Integer noOfPages = 0;
        actualSize = (null == actualSize) ? 0 : actualSize;
        size = (null == size) ? actualSize : size;
        if (actualSize % size == 0)
            noOfPages = actualSize / size;
        else noOfPages = actualSize / size + 1;

        entity.put("size", size);
        entity.put("page", page);
        entity.put("noOfPages", noOfPages);
        return entity;
    }


    public String getWorkflowGetUrl() {
        return workflowGetUrl;
    }

    public String getWfDocUpdateUrl() {
        return wfDocUpdateUrl;
    }

    public String getWorkflowAddUrl() {
        return workflowAddUrl;
    }

    @Override
    public JSONObject getDocs(String id) throws OperationException {
        try {
            String url = workflowGetUrl + "/fetch/"+id;
            String workFlowResp = mdmRestUtils.exchange(url, HttpMethod.GET, String.class);
            JSONObject workFlowJson = new JSONObject(workFlowResp);
            JSONObject docJsonObject = workFlowJson.optJSONObject("doc");
            if (docJsonObject!=null && docJsonObject.length()>0)
            {
                JSONObject newDocJson = docJsonObject.optJSONObject("newDoc");
                if (newDocJson!=null && newDocJson.length()>0)
                {
                    return newDocJson;
                }
            }
        } catch (Exception e) {
            throw new OperationException(Constants.UNABLE_TO_GET_WORKFLOW, id);
        }
        return null;
    }

}
