package com.coxandkings.travel.operations.service.alternateoptions.impl;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.coxandkings.travel.operations.resource.alternateOption.AlternateOptionsCompanyDetailsResource;
import com.coxandkings.travel.operations.resource.alternateOption.AlternateOptionsProductDetailsResource;
import com.coxandkings.travel.operations.resource.alternateOption.AlternateOptionsResource;
import com.coxandkings.travel.operations.resource.user.OpsUser;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.coxandkings.travel.operations.criteria.alternateoptions.AlternateOptionsCriteria;
import com.coxandkings.travel.operations.criteria.alternateoptions.SearchCriteria;
import com.coxandkings.travel.operations.enums.alternateOptions.AlternateOptionsStatus;
import com.coxandkings.travel.operations.enums.forex.RequestStatus;
import com.coxandkings.travel.operations.enums.workflow.WorkflowOperation;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptionsCompanyDetails;
import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptionsProductDetails;
import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptionsV2;
import com.coxandkings.travel.operations.model.alternateoptions.RequestLockObject;
import com.coxandkings.travel.operations.model.forex.ForexBooking;
import com.coxandkings.travel.operations.model.alternateoptions.*;
import com.coxandkings.travel.operations.repository.alternateoptions.AlternateOptionsRepositoryV2;
import com.coxandkings.travel.operations.service.alternateoptions.AlternateOptionsServiceV2;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.service.workflow.WorkflowService;
import com.coxandkings.travel.operations.utils.Constants;

@Service
@Transactional(readOnly = false)
public class AlternateOptionsServiceImplV2 implements AlternateOptionsServiceV2 {

    private static String entityName = "Alternate Options";
    
    private static ObjectMapper objectMapper = new ObjectMapper();
    
    private Logger logger = Logger.getLogger(AlternateOptionsServiceV2.class);
  
    @Autowired
    private AlternateOptionsRepositoryV2 alternateOptionsRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private WorkflowService workflowService;

    @Override
    public Object saveRecord(AlternateOptionsResource alternateOptionsResource, Boolean draft, String id)throws OperationException {

      //AlternateOptionsV2 alternateOptions = new AlternateOptionsV2();
      
      String workflowResponse;
      String userId = userService.getLoggedInUserId();
	  
	  OpsUser loggedInUser =  userService.getLoggedInUser();
      
      alternateOptionsResource.setAction("Active");
      alternateOptionsResource.setStatus("Active");
      alternateOptionsResource.setLastModifiedByUserId(userId);
      alternateOptionsResource.setLastModifiedOn(ZonedDateTime.now(ZoneOffset.UTC));
      alternateOptionsResource.setCreatedOn(ZonedDateTime.now(ZoneOffset.UTC));
	  alternateOptionsResource.setCompanyId(loggedInUser.getCompanyId());
      
      //When draft = true and no id is passed that means we will be creating a workflow and only saving it
      //Add - Save
      if(draft!=null && draft == true && (id==null || StringUtils.isEmpty(id)))
      {
        workflowResponse = workflowService.saveWorkflow(alternateOptionsResource, WorkflowOperation.SAVE,entityName, userId);
          
        if(workflowResponse==null){
              throw new OperationException("Unable to save Workflow");
        }
          
        JSONObject workflowObject = new JSONObject(workflowResponse);
          
        try {
          AlternateOptionsV2 alternateOptions = objectMapper.readValue(
                  (workflowObject.getJSONObject("doc").getJSONObject("newDoc").toString()), AlternateOptionsV2.class);
          
          alternateOptions.set_id(workflowObject.getString("_id"));
          
          //return alternateOptions;
          
          return workflowObject;
          } catch (Exception e) {
              e.printStackTrace();
              return null;
          }

      }
      //When draft is null or empty or false and no id is passed that means we will be creating a workflow and
      //and it will be submitted for approval
      //Add - Submit
      else if((draft==null || StringUtils.isEmpty(draft) || draft==false) && (id==null || StringUtils.isEmpty(id)))
      {
        workflowResponse = workflowService.saveWorkflow(alternateOptionsResource, WorkflowOperation.SUBMIT,entityName, userId);
        
        if(workflowResponse==null){
          throw new OperationException("Unable to save Workflow");
        }
          
        JSONObject workflowObject = new JSONObject(workflowResponse);
          
        try {
          AlternateOptionsV2 alternateOptions = objectMapper.readValue(
                  (workflowObject.getJSONObject("doc").getJSONObject("newDoc").toString()), AlternateOptionsV2.class);
          
          alternateOptions.set_id(workflowObject.getString("_id"));
          
          //return alternateOptions;
          
          return workflowObject;
          } catch (Exception e) {
              e.printStackTrace();
              return null;
         }
        
      }
      //When draft is null or empty or false and id is also passed that means we will be updating an existing workflow for that id passed
      //and it will be submitted for approval
      //Edit - Save
      else if(draft!=null && draft == true && id!=null && !StringUtils.isEmpty(id))
      {
        workflowResponse = workflowService.updateWorkflowDoc(alternateOptionsResource, WorkflowOperation.SAVE, id);
        
        if(workflowResponse==null){
              throw new OperationException("Unable to update Workflow");
        }
          
        JSONObject workflowObject = new JSONObject(workflowResponse);
          
        try {
          AlternateOptionsV2 alternateOptions = objectMapper.readValue(
                  (workflowObject.getJSONObject("doc").getJSONObject("newDoc").toString()), AlternateOptionsV2.class);
          
          alternateOptions.set_id(workflowObject.getString("_id"));
          
          //return alternateOptions;
          
          return workflowObject;
          } catch (Exception e) {
              e.printStackTrace();
              return null;
          }
      }
      //When draft is true and id is also passed that means we will be updating an existing workflow for that id passed
      //and saving it only
      //Edit - Submit
      else if((draft==null || StringUtils.isEmpty(draft) || draft==false) && id!=null && !StringUtils.isEmpty(id))
      {
        workflowResponse = workflowService.updateWorkflowDoc(alternateOptionsResource, WorkflowOperation.SUBMIT, id);
        
        if(workflowResponse==null){
              throw new OperationException("Unable to update Workflow");
        }
          
        JSONObject workflowObject = new JSONObject(workflowResponse);
          
        try {
          AlternateOptionsV2 alternateOptions = objectMapper.readValue(
                  (workflowObject.getJSONObject("doc").getJSONObject("newDoc").toString()), AlternateOptionsV2.class);
          
          alternateOptions.set_id(workflowObject.getString("_id"));
          
          //return alternateOptions;
          
          return workflowObject;
          } catch (Exception e) {
              e.printStackTrace();
              return null;
          }
      }
      else {
        
        throw new OperationException("Invalid workflow Operation");
        
      }
      
    }
    
    @Override
    public AlternateOptionsV2 assembler(AlternateOptionsResource alternateOptionsResource, AlternateOptionsV2 alternateOptions) throws OperationException
    {
        try {
          //AlternateOptionsV2 alternateOptions = new AlternateOptionsV2();
          
          /*alternateOptions.setCompanyMarket(companyMarket);
          alternateOptions.setClientType(clientType);*/
          alternateOptions.setAlternateOfferProcess(alternateOptionsResource.getAlternateOfferProcess());
          alternateOptions.setHigherLimitThreshold(alternateOptionsResource.getHigherLimitThreshold());
          alternateOptions.setLowerLimitThreshold(alternateOptionsResource.getLowerLimitThreshold());
          /*if(alternateOptionsResource.getApproverRemark()!=null && !StringUtils.isEmpty(alternateOptionsResource.getApproverRemark()))
            alternateOptions.setStatus(AlternateOptionsStatus.REJECTED);
          else
            alternateOptions.setStatus(AlternateOptionsStatus.APPROVED);*/
          alternateOptions.setStatus(alternateOptionsResource.getStatus());
          alternateOptions.setAction(alternateOptionsResource.getAction());
          alternateOptions.setSameValue(alternateOptionsResource.isSameValue());
          //alternateOptions.setAction("Active");
          alternateOptions.setLastModifiedByUserId(alternateOptionsResource.getLastModifiedByUserId());
          alternateOptions.setApproverRemark(alternateOptionsResource.getApproverRemark());
          alternateOptions.setLastModifiedOn(alternateOptionsResource.getLastModifiedOn());
          alternateOptions.setCreatedOn(alternateOptionsResource.getCreatedOn());
          alternateOptions.setCompanyId(alternateOptionsResource.getCompanyId());
          
          //List<AlternateOptionsCompanyDetails> companyDetailsList = new ArrayList<AlternateOptionsCompanyDetails>();
          
          Set<AlternateOptionsCompanyDetails> companyDetailsList = new HashSet<AlternateOptionsCompanyDetails>();
          
          for (AlternateOptionsCompanyDetailsResource alternateOptionsCompanyDetailsResource : alternateOptionsResource.getCompanyDetails()) 
          {
            AlternateOptionsCompanyDetails alternateOptionsCompanyDetails = new AlternateOptionsCompanyDetails();
            
            alternateOptionsCompanyDetails.setCompanyMarket(alternateOptionsCompanyDetailsResource.getCompanyMarket());
            alternateOptionsCompanyDetails.setClientType(alternateOptionsCompanyDetailsResource.getClientType());
            alternateOptionsCompanyDetails.setAlternateOptions(alternateOptions);
            companyDetailsList.add(alternateOptionsCompanyDetails);
          }
          
          alternateOptions.setCompanyDetails(companyDetailsList);
          
          if (alternateOptionsResource.getProductDetails() != null
              && alternateOptionsResource.getProductDetails().isEmpty() == false) 
          {
            Set<AlternateOptionsProductDetails> ProductDetailsList = new HashSet<AlternateOptionsProductDetails>();
            //List<AlternateOptionsProductDetails> ProductDetailsList = new ArrayList<AlternateOptionsProductDetails>();
            
            for (AlternateOptionsProductDetailsResource alternateOptionsProductDetailsResource : alternateOptionsResource.getProductDetails()) 
            {
              AlternateOptionsProductDetails alternateOptionsProductDetails = new AlternateOptionsProductDetails();
              
              alternateOptionsProductDetails.setProductCategory(alternateOptionsProductDetailsResource.getProductCategory());
              alternateOptionsProductDetails.setProductCategorySubType(alternateOptionsProductDetailsResource.getProductCategorySubType());
              alternateOptionsProductDetails.setAlternateOptions(alternateOptions);
              ProductDetailsList.add(alternateOptionsProductDetails);
            }
            
            alternateOptions.setProductDetails(ProductDetailsList);
            
            /*//For Lock Object
            if (alternateOptionsResource.getLock() != null) 
            {
              RequestLockObject lockObject = new RequestLockObject();
              
              lockObject.setId(alternateOptionsResource.getLock().getId());
              lockObject.setEnabled(alternateOptionsResource.getLock().isEnabled());
              lockObject.setUserId(alternateOptionsResource.getLock().getUserId());
              lockObject.setWorkflowId(alternateOptionsResource.getLock().getWorkflowId());
              
              alternateOptions.setLock(lockObject);
            }*/
            
           }else
           {
            throw new OperationException(Constants.PROCESS_REQUEST_FAILURE);
           }
          
          return alternateOptions;
      } 
        catch (Exception e) {
        
        throw new OperationException(Constants.PROCESS_REQUEST_FAILURE);
      
      }
    }

    @Override
    public Object EditRecord(String id, Boolean workflow,Boolean lock) throws OperationException {
      
      String workflowResponse;
      String userId = userService.getLoggedInUserId();
      
      //If ID passed is Workflow ID
      if(workflow!=null && !StringUtils.isEmpty(workflow) && workflow==true)
      {
        workflowResponse = workflowService.getWorkFlowById(id);
        
        JSONObject workflowObject = new JSONObject(workflowResponse);
        
        try {
          AlternateOptionsV2 alternateOptions = objectMapper.readValue(
                  (workflowObject.getJSONArray("data").getJSONObject(0).getJSONObject("doc").getJSONObject("newDoc").toString()), AlternateOptionsV2.class);
          
          alternateOptions.set_id(workflowObject.getJSONArray("data").getJSONObject(0).getString("_id"));
          
          return workflowObject.getJSONArray("data").getJSONObject(0);
          } catch (Exception e) {
              e.printStackTrace();
              return null;
          }
      }
      //If ID passed is Master ID
      else if(workflow==null || StringUtils.isEmpty(workflow) || workflow==false)
      {
        AlternateOptionsV2 savedAlternateOptionsV2 = getMasterRecordByID(id);
        if (savedAlternateOptionsV2 == null) {
            logger.error("Alternate Options Configuration does not exists for id: " + id);
            throw new OperationException(Constants.ER01);
        }
        
        //Check if the Master Data already has a lock
        if (savedAlternateOptionsV2.getLock()==null || Boolean.FALSE.equals(savedAlternateOptionsV2.getLock().isEnabled())) 
        {
        
          if(lock!=null && !StringUtils.isEmpty(lock) && lock==true)
          {
            RequestLockObject lockobject = new RequestLockObject();
            lockobject.setEnabled(true);
            lockobject.setUserId(userId);
            lockobject.setUser(userId);
            
            lockobject.setAlternateOptions(savedAlternateOptionsV2);
              
            savedAlternateOptionsV2.setLock(lockobject);
            
            savedAlternateOptionsV2.set_id(id);
            String workflowId = workflowService.editMasterObject(savedAlternateOptionsV2,entityName, userId, id);
          
            workflowResponse = workflowService.getWorkFlowById(workflowId);
            
            if(workflowResponse==null){
              throw new OperationException("Unable to save Workflow");
            }
              
            JSONObject workflowObject = new JSONObject(workflowResponse);
              
            try {
              /*AlternateOptionsV2 alternateOptions = objectMapper.readValue(
                      (workflowObject.getJSONObject("doc").getJSONObject("newDoc").toString()), AlternateOptionsV2.class);
              
              alternateOptions.set_id(workflowObject.getString("_id"));*/
              
              //workflowObject.getJSONArray("data").getJSONObject(0).getJSONObject("doc").getJSONObject("newDoc").getJSONObject("lock").put("workflowId", workflowObject.getJSONArray("data").getJSONObject(0).getString("_id"));
              lockobject.setWorkflowId(workflowObject.getJSONArray("data").getJSONObject(0).getString("_id"));
              savedAlternateOptionsV2.setLock(lockobject);
              
              AlternateOptionsV2 updatedAlternateOptionsV2 = alternateOptionsRepository.update(savedAlternateOptionsV2);
              
              //workflowService.updateWorkflowDoc(updatedAlternateOptionsV2, WorkflowOperation.SAVE, workflowObject.getString("_id"));
              
              updatedAlternateOptionsV2.getLock().setUser(updatedAlternateOptionsV2.getLock().getUserId());
              
              return updatedAlternateOptionsV2;
              } catch (Exception e) {
                  e.printStackTrace();
                  return null;
              }
          }
          else
          {
            String workflowId = workflowService.editMasterObject(savedAlternateOptionsV2,entityName, userId, id);
          
            workflowResponse = workflowService.getWorkFlowById(workflowId);  
        
            JSONObject workflowObject = new JSONObject(workflowResponse);
        
            try {
              AlternateOptionsV2 alternateOptions = objectMapper.readValue(
                      (workflowObject.getJSONArray("data").getJSONObject(0).getJSONObject("doc").getJSONObject("newDoc").toString()), AlternateOptionsV2.class);
              
              alternateOptions.set_id(workflowObject.getJSONArray("data").getJSONObject(0).getString("_id"));
              
              alternateOptions.getLock().setUser(alternateOptions.getLock().getUserId());
              
              return alternateOptions;
              
              //return workflowObject.getJSONArray("data").getJSONObject(0);
              } catch (Exception e) {
                  e.printStackTrace();
                  return null;
              }  
          }
        }
        else
        {
          /*System.out.println(savedAlternateOptionsV2.getLock().getWorkflowId());
          
          workflowResponse = workflowService.getWorkFlowById(savedAlternateOptionsV2.getLock().getWorkflowId());
          
          JSONObject workflowObject = new JSONObject(workflowResponse);
          
          try {
            AlternateOptionsV2 alternateOptions = objectMapper.readValue(
                    (workflowObject.getJSONArray("data").getJSONObject(0).getJSONObject("doc").getJSONObject("newDoc").toString()), AlternateOptionsV2.class);
            
            alternateOptions.set_id(workflowObject.getJSONArray("data").getJSONObject(0).getString("_id"));
            
            //return workflowObject.getJSONArray("data").getJSONObject(0);
*/            
            return savedAlternateOptionsV2;
            
        }

      }
      else
      {
        throw new OperationException("Invalid workflow Operation");
      }
    
    }
    
    @Override
    public Object viewRecord(String id, Boolean workflow) throws OperationException {
      
      String workflowResponse;
      String userId = userService.getLoggedInUserId();
      
      //If ID passed is Workflow ID
      if(workflow!=null && !StringUtils.isEmpty(workflow) && workflow==true)
      {
        workflowResponse = workflowService.getWorkFlowById(id);
        
        JSONObject workflowObject = new JSONObject(workflowResponse);
        
        try {
          AlternateOptionsV2 alternateOptions = objectMapper.readValue(
                  (workflowObject.getJSONArray("data").getJSONObject(0).getJSONObject("doc").getJSONObject("newDoc").toString()), AlternateOptionsV2.class);
          
          alternateOptions.set_id(workflowObject.getJSONArray("data").getJSONObject(0).getString("_id"));
          
          return workflowObject.getJSONArray("data").getJSONObject(0);
          } catch (Exception e) {
              e.printStackTrace();
              return null;
          }
      }
      //If ID passed is Master ID
      else if(workflow==null || StringUtils.isEmpty(workflow) || workflow==false)
      {
        AlternateOptionsV2 savedAlternateOptionsV2 = getMasterRecordByID(id);
        if (savedAlternateOptionsV2 == null) {
            logger.error("Alternate Options Configuration does not exists for id: " + id);
            throw new OperationException(Constants.ER01);
        }
        
        if(savedAlternateOptionsV2.getLock()!=null)
          savedAlternateOptionsV2.getLock().setUser(savedAlternateOptionsV2.getLock().getUserId());
        
        return savedAlternateOptionsV2;

      }
      else
      {
        throw new OperationException("Invalid workflow Operation");
      }
    
    }
    
    @Override
    public Map<String, Object> getWorkflowList(SearchCriteria searchCriteria) throws OperationException{

        String userId = userService.getLoggedInUserId();
        
        AlternateOptionsCriteria alternateOptionsCriteria = searchCriteria.getFilter();

        JSONObject workflowFilterObject = new JSONObject();
        
        if (alternateOptionsCriteria.getProductCategory()!=null || alternateOptionsCriteria.getProductCategorySubType()!=null) {
  
          if (!StringUtils.isEmpty(alternateOptionsCriteria.getProductCategory())) {
            workflowFilterObject.putOpt("doc.newDoc.productDetails.productCategory", alternateOptionsCriteria.getProductCategory());
          }
          if (!StringUtils.isEmpty(alternateOptionsCriteria.getProductCategorySubType())) {
            workflowFilterObject.putOpt("doc.newDoc.productDetails.productCategorySubType", alternateOptionsCriteria.getProductCategorySubType());
          }
        }
        
        if (alternateOptionsCriteria.getCompanyMarket()!=null || alternateOptionsCriteria.getClientType()!=null) {

            if (!StringUtils.isEmpty(alternateOptionsCriteria.getCompanyMarket())) {
              workflowFilterObject.putOpt("doc.newDoc.companyDetails.companyMarket", alternateOptionsCriteria.getCompanyMarket());
            }
            if (!StringUtils.isEmpty(alternateOptionsCriteria.getClientType())) {
              workflowFilterObject.putOpt("doc.newDoc.companyDetails.clientType", alternateOptionsCriteria.getClientType());
            }
        }
        
        if (!StringUtils.isEmpty(alternateOptionsCriteria.getCompanyId())) {
          workflowFilterObject.putOpt("doc.newDoc.companyId", alternateOptionsCriteria.getCompanyId());
        }
        
        Map<String, Object> workflowList = workflowService.getWorkFlows(entityName, userId, workflowFilterObject, searchCriteria.getSort(), searchCriteria.getPage(), searchCriteria.getCount());
        
        return workflowList;
    }
    
    @Override
    public Map<String, Object> getByCriteriaFromMaster(SearchCriteria searchCriteria) throws OperationException {

        try {
          
            return alternateOptionsRepository.getAlternateOptionsByCriteria(searchCriteria);
            
        } catch (Exception e) {
            logger.error("Error occurred while loading Alternate Option Configuration Records");
            throw new OperationException(Constants.UNABLE_TO_FETCH_DETAILS);
        }
    }

    @Override
    public AlternateOptionsV2 saveMasterRecord(AlternateOptionsResource alternateOptionsResource)throws OperationException {

      try {
        
      OpsUser loggedInUser =  userService.getLoggedInUser();
        
      AlternateOptionsV2 alternateOptionsV2 = new AlternateOptionsV2();
        
      alternateOptionsResource.setAction("Active");
      
      /*if(!StringUtils.isEmpty(alternateOptionsResource.getApproverRemark()))
        alternateOptionsResource.setStatus(AlternateOptionsStatus.APPROVED);
      else
        alternateOptionsResource.setStatus(AlternateOptionsStatus.REJECTED);*/
      alternateOptionsResource.setStatus("InActive");
      alternateOptionsResource.setLastModifiedByUserId(userService.getLoggedInUserId());
      alternateOptionsResource.setLastModifiedOn(ZonedDateTime.now(ZoneOffset.UTC));
      alternateOptionsResource.setCreatedOn(ZonedDateTime.now(ZoneOffset.UTC));  
      alternateOptionsResource.setCompanyId(loggedInUser.getCompanyId());
      
      List<AlternateOptionsV2> alternateOptionsList = new ArrayList<AlternateOptionsV2>();
      
      AlternateOptionsV2 alternateOptionsObject = assembler(alternateOptionsResource,alternateOptionsV2);
        
      alternateOptionsList.add(alternateOptionsObject);

      List<AlternateOptionsV2> savedAlternateOptions = alternateOptionsRepository.saveAlternateOptions(alternateOptionsList);
      
      return savedAlternateOptions.get(0);

      } catch (Exception e) {
        logger.error("Exception occured while trying to save Alternate Options Configuration", e);
        throw new OperationException(Constants.UNABLE_TO_SAVE);
      }
    }

    @Override
    public AlternateOptionsV2 getMasterRecordByID(String id) throws OperationException {
      
      AlternateOptionsV2 alternateOptions;
      try {
        alternateOptions = alternateOptionsRepository.fetchAlternateOptionsForConfigurationId(id);
        
        alternateOptions.set_id(id);
     
      } catch (Exception e) {
          logger.error("Exception occurred while fetching Alternate Options Configuration for id " + id, e);
          throw new OperationException(Constants.UNABLE_TO_FETCH_DETAILS);
      }
      if (alternateOptions == null) {
          logger.error("No Alternate Options Configuration found for id " + id);
          throw new OperationException(Constants.NO_RESULT_FOUND);
      }
      return alternateOptions;
      
    }

    @Override
    public AlternateOptionsV2 UpdateMasterRecord(String id, AlternateOptionsResource alternateOptionsResource) throws OperationException {

        //String id = alternateOptionsResource.getConfigurationId();
       
        if (!StringUtils.isEmpty(id)) 
        {
          try {
              AlternateOptionsV2 savedAlternateOptionsV2 = getMasterRecordByID(id);
          
              if (savedAlternateOptionsV2 == null) {
                logger.error("Alternate Option Configuration does not exists for id: " + id);
                throw new OperationException(Constants.ER01);
              }
            
              //AlternateOptionsV2 alternateOptionsObject = assembler(alternateOptionsResource);
              
              //String response = workflowService.releaseWorkflow(savedAlternateOptionsV2.getLock().getWorkflowId());
              String response = "asd";
              
              /*if (response != null) 
              {
                RequestLockObject lockobject = savedAlternateOptionsV2.getLock();
                
                lockobject.setEnabled(false);
                lockobject.setUserId(null);
                lockobject.setUser(null);
                lockobject.setWorkflowId(null);
                
                alternateOptionsObject.setLock(lockobject);
              }
              else{
                  throw new OperationException(Constants.UNABLE_TO_RELEASE_LOCK, id);
              }*/
              
              if (response != null) 
              {
                int deleted = alternateOptionsRepository.deleteMasterRecord(savedAlternateOptionsV2.getLock().getId().toString());
                
                AlternateOptionsV2 updatedAlternateOptionsV2 = getMasterRecordByID(id);
                
                System.out.println("Deleted "+deleted+" records");
                
                //updatedAlternateOptionsV2 = assembler(alternateOptionsResource,updatedAlternateOptionsV2);
                if(alternateOptionsResource.getAlternateOfferProcess()!=null)
                  updatedAlternateOptionsV2.setAlternateOfferProcess(alternateOptionsResource.getAlternateOfferProcess());
                if(alternateOptionsResource.getHigherLimitThreshold()!=null)
                  updatedAlternateOptionsV2.setHigherLimitThreshold(alternateOptionsResource.getHigherLimitThreshold());
                if(alternateOptionsResource.getLowerLimitThreshold()!=null)
                  updatedAlternateOptionsV2.setLowerLimitThreshold(alternateOptionsResource.getLowerLimitThreshold());
                if(alternateOptionsResource.getApproverRemark()!=null && !StringUtils.isEmpty(alternateOptionsResource.getApproverRemark()))
                  updatedAlternateOptionsV2.setStatus("InActive");
                else if(alternateOptionsResource.getProductDetails() != null
                    && alternateOptionsResource.getProductDetails().isEmpty() == false && alternateOptionsResource.getCompanyDetails() != null
                    && alternateOptionsResource.getCompanyDetails().isEmpty() == false)
                  updatedAlternateOptionsV2.setStatus("Active");
                if(alternateOptionsResource.getAction()!=null)
                  updatedAlternateOptionsV2.setAction(alternateOptionsResource.getAction());
                if(alternateOptionsResource.isSameValue())
                  updatedAlternateOptionsV2.setSameValue(alternateOptionsResource.isSameValue());
                if(alternateOptionsResource.getApproverRemark()!=null)
                  updatedAlternateOptionsV2.setApproverRemark(alternateOptionsResource.getApproverRemark());
                
                if (alternateOptionsResource.getCompanyDetails() != null
                    && alternateOptionsResource.getCompanyDetails().isEmpty() == false) 
                {
                Set<AlternateOptionsCompanyDetails> companyDetailsList = updatedAlternateOptionsV2.getCompanyDetails();
                
                for (AlternateOptionsCompanyDetailsResource alternateOptionsCompanyDetailsResource : alternateOptionsResource.getCompanyDetails()) 
                {
                  for(AlternateOptionsCompanyDetails alternateOptionsCompanyDetails : companyDetailsList)
                  { 
                    if(alternateOptionsCompanyDetails.getId().equals(alternateOptionsCompanyDetailsResource.getId()))
                    {
                      alternateOptionsCompanyDetails.setCompanyMarket(alternateOptionsCompanyDetailsResource.getCompanyMarket());
                      alternateOptionsCompanyDetails.setClientType(alternateOptionsCompanyDetailsResource.getClientType());
                      alternateOptionsCompanyDetails.setAlternateOptions(updatedAlternateOptionsV2);
                      companyDetailsList.add(alternateOptionsCompanyDetails);
                    }
                  }
                }
                
                updatedAlternateOptionsV2.setCompanyDetails(companyDetailsList);
              }
                
               if (alternateOptionsResource.getProductDetails() != null
                    && alternateOptionsResource.getProductDetails().isEmpty() == false) 
                {
                  Set<AlternateOptionsProductDetails> ProductDetailsList = updatedAlternateOptionsV2.getProductDetails();
                  
                  for (AlternateOptionsProductDetailsResource alternateOptionsProductDetailsResource : alternateOptionsResource.getProductDetails()) 
                  {
                    for(AlternateOptionsProductDetails alternateOptionsProductDetails : ProductDetailsList)
                    { 
                      if(alternateOptionsProductDetails.getId().equals(alternateOptionsProductDetailsResource.getId()))
                      {
                        alternateOptionsProductDetails.setProductCategory(alternateOptionsProductDetailsResource.getProductCategory());
                        alternateOptionsProductDetails.setProductCategorySubType(alternateOptionsProductDetailsResource.getProductCategorySubType());
                        alternateOptionsProductDetails.setAlternateOptions(updatedAlternateOptionsV2);
                        ProductDetailsList.add(alternateOptionsProductDetails);
                      }
                    }
                  }
                  
                  updatedAlternateOptionsV2.setProductDetails(ProductDetailsList);
                }
                
                updatedAlternateOptionsV2.setLastModifiedOn(ZonedDateTime.now(ZoneOffset.UTC));
                updatedAlternateOptionsV2.setLastModifiedByUserId(userService.getLoggedInUserId());
  
                updatedAlternateOptionsV2 = alternateOptionsRepository.update(updatedAlternateOptionsV2);
                
                /*List<AlternateOptionsV2> alternateOptionsList = new ArrayList<AlternateOptionsV2>();
                
                alternateOptionsList.add(alternateOptionsObject);
 
                List<AlternateOptionsV2> savedAlternateOptions = alternateOptionsRepository.saveAlternateOptions(alternateOptionsList);
                */
                
                return updatedAlternateOptionsV2;
              }
              else{
                  throw new OperationException(Constants.UNABLE_TO_RELEASE_LOCK, id);
              }
              
            } catch (Exception e) {
                logger.error("Exception occured while trying to update Alternate Option Configuration for id:" + id, e);
                throw new OperationException(Constants.UNABLE_TO_SAVE);
            }
        } else {
            logger.error("Id is null or Empty");
            throw new OperationException(Constants.ID_NULL_EMPTY);
        }
        //return updatedAlternateOptionsV2;
    }
    
    
    
    @Override
    public AlternateOptionsV2 releaseLock(String id) throws OperationException{

        String userId = userService.getLoggedInUserId();
        
        AlternateOptionsV2 savedAlternateOptionsV2 = getMasterRecordByID(id);
        
        if (savedAlternateOptionsV2.getLock()!=null && Boolean.TRUE.equals(savedAlternateOptionsV2.getLock().isEnabled())) 
        {
            if(savedAlternateOptionsV2.getLock().getUserId().equals(userId)) 
            {
              String response = workflowService.deleteWorkflow(savedAlternateOptionsV2.getLock().getWorkflowId());
              
                if (response != null) 
                {
                    /*RequestLockObject lockobject = savedAlternateOptionsV2.getLock();
                    
                    lockobject.setEnabled(false);
                    lockobject.setUserId(null);
                    lockobject.setUser(null);
                    lockobject.setWorkflowId(null);
                    
                    savedAlternateOptionsV2.setLock(lockobject);
                    
                    savedAlternateOptionsV2.setLastModifiedByUserId(userId);
                    savedAlternateOptionsV2.setLastModifiedOn(ZonedDateTime.now(ZoneOffset.UTC));
                    
                    updatedAlternateOptionsV2 = alternateOptionsRepository.update(savedAlternateOptionsV2);*/
                  
                  //New Implementation
                  //1.save the changes.
                  //2.delete the lock object from the master
                  
                  int deleted = alternateOptionsRepository.deleteMasterRecord(savedAlternateOptionsV2.getLock().getId().toString());
                  
                  AlternateOptionsV2 updatedAlternateOptionsV2 = getMasterRecordByID(id);
                  
                  System.out.println("Deleted "+deleted+" records");
                  
                  updatedAlternateOptionsV2.setLastModifiedOn(ZonedDateTime.now(ZoneOffset.UTC));
                  updatedAlternateOptionsV2.setLastModifiedByUserId(userService.getLoggedInUserId());
    
                  updatedAlternateOptionsV2 = alternateOptionsRepository.update(updatedAlternateOptionsV2);
                  
                  return updatedAlternateOptionsV2;
                  
                }
                else{
                    throw new OperationException(Constants.UNABLE_TO_RELEASE_LOCK, id);
                }
            }
            else{
                logger.warn("Cannot Release: Locked by another user "+ savedAlternateOptionsV2.getLock().getUserId());
                throw new OperationException(Constants.LOCKED_BY_ANOTHER_USER, savedAlternateOptionsV2.getLock().getUserId());
            }
        } else {
            logger.warn("Alternate Option Configuration with id"+ id +" to be released is not locked");
            throw new OperationException(Constants.NOT_LOCKED, id);
        }
        //return updatedAlternateOptionsV2;
    }
}
