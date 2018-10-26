package com.coxandkings.travel.operations.service.workflow;

import com.coxandkings.travel.operations.criteria.workflow.WorkFlowFilter;
import com.coxandkings.travel.operations.enums.workflow.WorkflowOperation;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.workflow.WorkFlow;
import org.json.JSONObject;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface WorkflowService {

	public String saveWorkflow(Object object,WorkflowOperation workflowOperation,String entityName,String UserId);

	public String updateWorkflowDoc(Object object, WorkflowOperation workflowOperation, String id) throws OperationException;

	public Page<WorkFlow> getWorkFlow(String type, String userId ,JSONObject filterObject,JSONObject sortObject,Integer page,Integer count) throws OperationException;

	public String getWorkFlowById(String workFlowId) throws OperationException;

	public String editMasterObject(Object newObject,String entityName,String UserId,String masterId) throws OperationException;

	public String releaseWorkflow(String workFlowId);

	String saveWorkflowFilter(Object object, WorkflowOperation workflowOperation, String entityName,
							  String UserId, Integer i);

	Integer getIndex(String entityName);

	String updateWorkflow(String workFlowid, JSONObject updateObject);

	String deleteWorkflow(String uid);

    Map<String,Object> getWorkFlows(String entityName, String userId, JSONObject workflowFilterObject, String sort, Integer pageNumber, Integer pageSize) throws OperationException;

    public String getWorkflowGetUrl();

	public String getWfDocUpdateUrl();

	public String getWorkflowAddUrl();

	Map<String,Object> getWorkFlows(String entityName, WorkFlowFilter workflowFilter, JSONObject filterObject, String sortCriteria, Integer pageNo, Integer size) throws OperationException;

	JSONObject getDocs(String id) throws OperationException;

	Object releaseLock(String workFlowId) throws OperationException;
}
