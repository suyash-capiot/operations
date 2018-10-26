package com.coxandkings.travel.operations.service.mailroomanddispatch;

import com.coxandkings.travel.operations.criteria.mailroomanddispatch.*;
import com.coxandkings.travel.operations.criteria.mailroomanddispatch.workflowSearchCriteria.InboundSearchCriteria;
import com.coxandkings.travel.operations.criteria.mailroomanddispatch.workflowSearchCriteria.MasterSearchCriteria;
import com.coxandkings.travel.operations.criteria.mailroomanddispatch.workflowSearchCriteria.OutboundSearchCritieria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.mailroomanddispatch.*;
import com.coxandkings.travel.operations.resource.mailroomMaster.*;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Set;


public interface MailRoomService {
    public MailRoomMaster save(MailRoomMasterResource mailRoomMasterResource) throws OperationException;
    public MailRoomMaster update(MailRoomMasterResource mailRoomMasterResource) throws OperationException;
    public InboundEntry saveInboundEntry(InboundEntryResource inboundReource) throws OperationException;

    public InboundEntry updateInboundEntry(InboundEntryResource inboundReource) throws OperationException;

    public PlanDelivery setPlanDeliverytoInboundNoList(PlanDeliverytoInboundNoListResource planDeliverytoInboundNoListResource) throws OperationException;

    public PlanDelivery getPlanDeliveryFromInboundNo(String inboundNo) throws OperationException;

    public InboundEntry updatePlanDeliveryFromInboundNo(String inboundNo, PlanDeliveryResource planDeliveryResource) throws OperationException;

    public CommonDelivery getCommonDeliveryFromInbounfNo(String inboundNo) throws OperationException;

    public Set<InboundLogEntryStatus> updateCommonDeliveryFromInboundNo(CommonDeliveryResource commonDeliveryResource) throws OperationException;
    public Map<String, Object> getByCriteria(MailRoomSearchCriteria maiRoomCriteria)throws OperationException;
    public Map<String, Object> getByInboundCriteria(InboundEntryCriteria inboundEntryCriteria)throws OperationException;
    public MailRoomMaster getId(String mailRoomId) throws OperationException;

    public List<String> getMailRoomNames(String param);
    public List<String> getMailRooms();
    public Set<String> getEmployeeNames();
    public Set<String> getEmployeeIDs();
    public Set<String> getlinkToDispatchIDs();
    public InboundEntry getInboundId(String inboundId);
    public OutboundDispatch saveOutboundDispatch(OutboundDispatchResource otboundDispatchReource) throws OperationException;
    public OutboundDispatch updateOutboundDispatch(OutboundDispatchResource otboundDispatchReource) throws OperationException;
    public Map<String, Object> getOutBoundCriteria(OutBoundDispatchCriteria outBoundDispatchCriteria)throws OperationException;
    public OutboundDispatch getOutBoundId(String dispatchId);
    public List<OutboundDispatch> getOutboundsFromParcelId(String parcelID) throws OperationException;
    public Parcel saveParcelElementForDispatchId(ParcelCreationForOutboundIdResource parcelCreationForOutboundIdResource) throws OperationException;
    public PlanCollection savePlanCollectionForDispatchId(PlanCollectionForOutboundIdResource planCollectionForOutboundIdResource) throws OperationException;
    public List<Parcel> getParcels() throws OperationException;
    public Parcel saveParcelElement(CreateParcelResource createParcelReource) throws OperationException;
    public Parcel updateParcelElement(UpdateParcelResource updateParcelResource) throws OperationException;
    public ParcelResource saveOutboundDispatchToPreCreatedParcel(OutboundDispathToPreCreatedParcelResource outboundDispathToPreCreatedParcelResource) throws OperationException;
    public Parcel getParcelId(String parcelId);
    public List<String> getParcelIds();
    public List<OutboundDispatch> getOutboundsDetach(OutboundsDetachFromParcel outboundsDetachFromParcel) throws OperationException;

    public Parcel getParcelFromDispatchId(String dispatchID) throws OperationException;
    public Parcel getByCriteria(SearchParcelCriteria searchParcelCriteria);
    public List<MailRoomMaster> getAllMailRoomDetails();
    public List<MailRoomMaster> getAllMailRoomsSorted(String columnName,String order) throws OperationException;
    public List<String> suggest(String attribute, String value);
    public void deleteDispatch(String id);
    //public String getBarCode(String id,String companyName,String consigneeName,String consigneeLocation) throws FileNotFoundException, DocumentException;
    public BarCode saveBarCode(BarCodeResource barCodeResource) throws OperationException;
    public String generateBarCode(String id) throws FileNotFoundException;
    public BarCode getBarCodeId(String id);

    List<String> getInboundNo();

    List<String> getAwbNo();

    List<String> getSenderName();

    List<String> getRecipientName();

    public List<String> getDepartments();

    public MailRoomMaster addEmployeeDetails(EmployeeDetailsResource employeeDetailsResource) throws OperationException, NullPointerException;
    public Set<EmployeeDetails> getEmployeeDetails( String mailRoomId) throws OperationException;

    public Object saveWorkflowRecord(MailRoomMasterResource mailRoomMasterResource, Boolean draft, String id)throws OperationException;

    public Map<String,Object> getByCriteriaSorted(MailroomSearchCriteriaSorted mailroomSearchCriteriaSorted) throws OperationException;

    public List<String> getPassengarNames(String param);

    public List<String> getInboundNo(String param);
    public Set<String> getOutBoundSupplierIDs(String param);
    public Set<String> getPassportNo(String param);

    //Master Workflow
    public MailRoomMaster releaseMasterLock(String id) throws OperationException;
    public MailRoomMaster getMasterRecordByID(String id) throws OperationException;
    public Object EditWorkflowMasterRecord(String id, Boolean workflow,Boolean lock) throws OperationException;
    public Object viewWorkflowMasterRecord(String id, Boolean workflow) throws OperationException;
    public Map<String, Object> getMasterWorkflowList(MasterSearchCriteria masterSearchCriteria) throws OperationException;

    public List<EmployeeDetails> getSpecificEmployeeDetails(String param, String empID);
    public List<EmployeeDetails> getEmployeeNames(AutoSuggestResource autoResource);

    //Inbound Workflow
    public Object saveInboundRecord(InboundEntryResource inboundEntryResource, Boolean draft, String id)throws OperationException;
    public Map<String, Object> getWorkflowList(InboundSearchCriteria inboundSearchCriteria) throws OperationException;
    public Object viewInboundRecord(String id, Boolean workflow) throws OperationException;
    public Object EditInboundRecord(String id, Boolean workflow,Boolean lock) throws OperationException;
    public InboundEntry releaseLock(String id) throws OperationException;


    //Outbound Workflow
    public Object saveOutboundRecord(OutboundDispatchResource outboundDispatchResource, Boolean draft, String id)throws OperationException;
    public Map<String, Object> getWorkflowList(OutboundSearchCritieria outboundSearchCritieria) throws OperationException;
    public Object viewOutboundRecord(String id, Boolean workflow) throws OperationException;
    public Object EditOutboundRecord(String id, Boolean workflow,Boolean lock) throws OperationException;
    public OutboundDispatch releaseOutboundLock(String id) throws OperationException;
}
