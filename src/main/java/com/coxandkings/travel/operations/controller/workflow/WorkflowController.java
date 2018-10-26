package com.coxandkings.travel.operations.controller.workflow;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.service.workflow.WorkflowService;
import com.coxandkings.travel.operations.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/workflow")

@CrossOrigin(origins = "*", maxAge = 100000)
public class WorkflowController {

	@Autowired
	private WorkflowService workflowService;

	@RequestMapping(value = "/getWorkflowById/{workFlowId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Object> getWorkflowById(@PathVariable("workFlowId") String workFlowId) throws OperationException{
		Object val = workflowService.getWorkFlowById(workFlowId);
		return new ResponseEntity<>(val, HttpStatus.OK);
	}

	@GetMapping(value = "/releasLock/{workFlowId}")
	public ResponseEntity<Object> releaseLock(@PathVariable String workFlowId) throws OperationException{
		try{
				Object res = workflowService.releaseLock(workFlowId);
				return new ResponseEntity<>(res,HttpStatus.OK);
		}catch (OperationException oe){
			throw oe;
		}catch (Exception e){
			throw new OperationException(Constants.OPS_ERR_11200);
		}
	}

}
