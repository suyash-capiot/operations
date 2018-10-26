package com.coxandkings.travel.operations.service.alternateoptions;

import java.util.Map;
import com.coxandkings.travel.operations.criteria.alternateoptions.SearchCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.alternateOption.AlternateOptionsResource;
import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptionsV2;
import com.coxandkings.travel.operations.model.forex.ForexBooking;

public interface AlternateOptionsServiceV2 { 

    Object saveRecord(AlternateOptionsResource alternateOptionsResource, Boolean draft, String id) throws OperationException;
	
	Object EditRecord(String id, Boolean workflow,Boolean lock) throws OperationException;
	
	Object viewRecord(String id, Boolean workflow) throws OperationException;
	
	AlternateOptionsV2 saveMasterRecord(AlternateOptionsResource alternateOptionsResource) throws OperationException;

    AlternateOptionsV2 assembler(AlternateOptionsResource alternateOptionsResource,AlternateOptionsV2 alternateOptionsV2) throws OperationException;
    
    AlternateOptionsV2 UpdateMasterRecord(String id, AlternateOptionsResource alternateOptionsResource) throws OperationException ;
    
    Map<String, Object> getWorkflowList(SearchCriteria alternateOptionsCriteria) throws OperationException;

    Map<String, Object> getByCriteriaFromMaster(SearchCriteria alternateOptionsCriteria) throws OperationException;
    
    AlternateOptionsV2 getMasterRecordByID(String id) throws OperationException ;
    
    AlternateOptionsV2 releaseLock(String id) throws OperationException;
}
