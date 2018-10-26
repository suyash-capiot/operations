package com.coxandkings.travel.operations.service.mailroomanddispatch.Impl;


import com.coxandkings.travel.operations.config.Messages;
import com.coxandkings.travel.operations.criteria.mailroomanddispatch.*;
import com.coxandkings.travel.operations.criteria.mailroomanddispatch.workflowSearchCriteria.InboundSearchCriteria;
import com.coxandkings.travel.operations.criteria.mailroomanddispatch.workflowSearchCriteria.MasterSearchCriteria;
import com.coxandkings.travel.operations.criteria.mailroomanddispatch.workflowSearchCriteria.OutboundSearchCritieria;
import com.coxandkings.travel.operations.enums.mailroomanddispatch.DispatchStatus;
import com.coxandkings.travel.operations.enums.mailroomanddispatch.WorkflowEnums;
import com.coxandkings.travel.operations.enums.workflow.WorkflowOperation;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.mailroomanddispatch.*;
import com.coxandkings.travel.operations.repository.mailroomanddispatch.*;
import com.coxandkings.travel.operations.resource.mailroomMaster.*;
import com.coxandkings.travel.operations.service.mailroomanddispatch.MailRoomService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.service.workflow.ReleaseLockService;
import com.coxandkings.travel.operations.service.workflow.WorkflowService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.CopyUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.FileNotFoundException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.coxandkings.travel.operations.utils.Constants.*;

@Service
public class MailRoomServiceImpl implements MailRoomService,ReleaseLockService {
    private Logger log = LogManager.getLogger(MailRoomService.class);
    @Autowired
    private MailRoomRepository mailRoomRepository;

    @Autowired
    private InboundEntryRepository inboundEntryRepository;

    @Autowired
    private OutBoundDispatchRepository outBoundDispatchRepository;

    @Autowired
    private ParcelRepository parcelRepository;

    @Autowired
    private DispatchStatusRepository dispatchStatusRepository;

    @Autowired
    private BarCodeRepository barCodeRepository;

    @Autowired
    private MessageSource messageSource;
    @Autowired
    Messages messages;

    @Autowired
    private InboundEntryStatusRepository inboundEntryStatusRepository;

    @Autowired
    private PlanDeliveryRepository planDeliveryRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private WorkflowService workflowService;

    private static String MasterEntityName = "Mailroom And Dispatch Master";
    private static String InboundEntityName = "Mailroom And Dispatch Inbound";
    private static String OutboundEntityName = "Mailroom And Dispatch Outbound";

    private static ObjectMapper objectMapper = new ObjectMapper();

    public MailRoomMaster save(MailRoomMasterResource mailRoomMasterResource)  throws OperationException{



        MailRoomMaster roomMaster=new MailRoomMaster();
        MailRoomSearchCriteria maiRoomCriteria = new MailRoomSearchCriteria();
        maiRoomCriteria.setMailRoomName(mailRoomMasterResource.getMailRoomName());
//        maiRoomCriteria.setCity(mailRoomMasterResource.getCity());
        Map<String, Object> mailRoomMasterMap = mailRoomRepository.getByCriteria(maiRoomCriteria);
        List<MailRoomMaster> list = (List<MailRoomMaster>) mailRoomMasterMap.get("data");
        if (list.size() > 0) {
            throw new OperationException(Constants.OPS_ERR_Mail_Room_02);
        } else {
            CopyUtils.copy(mailRoomMasterResource, roomMaster);
            /*roomMaster.setRoomStatus(statusRepository.getByCode(MailRoomStatus.ACTIVE));*/
            log.info("roomMaster" + roomMaster);
        }


        return mailRoomRepository.saveOrUpdate(roomMaster);
    }

    @Override
    public MailRoomMaster update(MailRoomMasterResource mailRoomMasterResource) throws OperationException {

        MailRoomMaster roomMaster=new MailRoomMaster();
        MailRoomMaster existingMailRoom=new MailRoomMaster();
        if(!StringUtils.isEmpty(mailRoomMasterResource.getId())) {
            // checking task exists or not
            existingMailRoom= mailRoomRepository.getId(mailRoomMasterResource.getId());
            if (existingMailRoom == null) {
                log.error("mail room not exists for id:"+mailRoomMasterResource.getId());
                throw new OperationException(ER01);
            }
            CopyUtils.copy(mailRoomMasterResource, roomMaster);
            roomMaster.setEmployeeDetails(existingMailRoom.getEmployeeDetails());

            successMessage();
        }
        return mailRoomRepository.saveOrUpdate(roomMaster);
    }

    private String successMessage() {
        return messages.get("BM02");
    }


    public List<MailRoomMaster> getAllMailRoomDetails(){

        return mailRoomRepository.getAllMailRoomDetails();
    }

    @Override
    public List<MailRoomMaster> getAllMailRoomsSorted(String columnName,String order) throws OperationException {
        List<MailRoomMaster> mailroomsSorted = mailRoomRepository.getAllMailRoomsSorted(columnName,order);
        return mailroomsSorted;
    }

    public Map<String, Object> getByCriteria(MailRoomSearchCriteria maiRoomCriteria)throws OperationException{

        Map<String, Object> mailRoomMasterMap= mailRoomRepository.getByCriteria(maiRoomCriteria);
        if (mailRoomMasterMap == null || mailRoomMasterMap.size() == 0) {
            throw new OperationException(Constants.ER01);
        }
        return mailRoomMasterMap;
    }

    @Override
    public Map<String, Object> getByCriteriaSorted(MailroomSearchCriteriaSorted mailroomSearchCriteriaSorted) throws OperationException {
        Map<String, Object> mailRoomMasterMap= mailRoomRepository.getByCriteriaSorted(mailroomSearchCriteriaSorted);
        if (mailRoomMasterMap == null || mailRoomMasterMap.size() == 0) {
            throw new OperationException(Constants.ER01);
        }
        return mailRoomMasterMap;
    }

    public MailRoomMaster getId(String mailRoomId) throws OperationException {
        MailRoomMaster mailRoomMaster=mailRoomRepository.getId(mailRoomId);
        if (mailRoomMaster==null){
            throw new OperationException(Constants.ER01);
        }
        return mailRoomMaster;

    }

    @Override
    public List<String> getMailRoomNames(String param) {
        return mailRoomRepository.getMailRoomNames(param);
    }

    @Override
    public List<String> getPassengarNames(String param) {//Auto Suggest
        return outBoundDispatchRepository.getPassengarNames(param);
    }

    @Override
    public List<String> getMailRooms() {
        return mailRoomRepository.getMailRooms();
    }

    @Override
    public Set<String> getEmployeeNames() {
        List<MailRoomMaster> mailRoomMasterList = mailRoomRepository.getAllMailRoomDetails();
        Set<EmployeeDetails> employeeDetailsSet = mailRoomMasterList.parallelStream().flatMap(x -> x.getEmployeeDetails().stream()).collect(Collectors.toSet());
        Set<String> employeeNames = employeeDetailsSet.parallelStream().map(y -> y.getEmployeeName()).collect(Collectors.toSet());
        return employeeNames;
    }

    @Override
    public Set<String> getEmployeeIDs() {
        List<MailRoomMaster> mailRoomMasterList = mailRoomRepository.getAllMailRoomDetails();
        Set<EmployeeDetails> employeeDetailsSet = mailRoomMasterList.parallelStream().flatMap(x -> x.getEmployeeDetails().stream()).collect(Collectors.toSet());
        Set<String> employeeIDs = employeeDetailsSet.parallelStream().map(y -> y.getEmployeeId()).collect(Collectors.toSet());
        return employeeIDs;
    }

    @Override
    public List<EmployeeDetails> getSpecificEmployeeDetails(String param , String empID) {
        List<MailRoomMaster> mailRoomMasterList = mailRoomRepository.getAllMailRoomDetails();
        Set<EmployeeDetails> employeeDetailsSet = mailRoomMasterList.parallelStream().flatMap(x -> x.getEmployeeDetails().stream()).collect(Collectors.toSet());

        return employeeDetailsSet.stream().filter(p -> p.getEmployeeName().equalsIgnoreCase(param) && p.getEmployeeId().equalsIgnoreCase(empID)).collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDetails> getEmployeeNames(AutoSuggestResource autoResource) {
        List<MailRoomMaster> mailRoomMasterList = mailRoomRepository.getAllMailRoomDetails();
        Set<EmployeeDetails> employeeDetailsSet = mailRoomMasterList.parallelStream().flatMap(x -> x.getEmployeeDetails().stream()).collect(Collectors.toSet());
        employeeDetailsSet = mailRoomMasterList.parallelStream().flatMap(x -> x.getEmployeeDetails().stream()).collect(Collectors.toSet());

        if(autoResource.getEmployeeName()!=null && !autoResource.getEmployeeName().isEmpty()) {
            String regex = new StringBuilder().append(".*").append("(?i)").append(autoResource.getEmployeeName()).append(".*").toString().trim();
            return employeeDetailsSet.stream().filter(x -> x.getEmployeeName().matches(regex)).collect(Collectors.toList());
        }
        else {
            String regex = new StringBuilder().append(".*").append("(?i)").append(autoResource.getEmployeeID()).append(".*").toString().trim();
            return employeeDetailsSet.stream().filter(x -> x.getEmployeeId().matches(regex)).collect(Collectors.toList());
        }
    }

    @Override
    public Set<String> getlinkToDispatchIDs() {
        List<OutboundDispatch> outboundDispatchList = outBoundDispatchRepository.getAllOutboundDetails();
        Set<String> linkToDispatch = outboundDispatchList.parallelStream().map(x -> x.getDispatchId()).collect(Collectors.toSet());
        return linkToDispatch;
    }

    @Override
    public Set<String> getOutBoundSupplierIDs(String param){

        List<OutboundDispatch> outboundDispatches = outBoundDispatchRepository.getAllOutboundDetails();
        String regex = new StringBuilder().append(".*").append("(?i)").append(param).append(".*").toString().trim();
        java.util.function.Predicate<String> predicateRegex = Pattern.compile(regex).asPredicate();

        Set<String> dispatchCostDetailsSet = outboundDispatches.parallelStream().map(x -> x.getDispatchCostDetails().getSupplierName()).collect(Collectors.toSet());

        return dispatchCostDetailsSet.parallelStream().filter(predicateRegex).collect(Collectors.toSet());
    }

    @Override
    public Set<String> getPassportNo(String param){

        List<OutboundDispatch> outboundDispatches = outBoundDispatchRepository.getAllOutboundDetails();
        String regex = new StringBuilder().append(".*").append("(?i)").append(param).append(".*").toString().trim();
        java.util.function.Predicate<String> predicateRegex = Pattern.compile(regex).asPredicate();

        Set<String> passportNo = outboundDispatches.parallelStream().map(x -> x.getPassportNo()).collect(Collectors.toSet());

        return passportNo.parallelStream().filter(predicateRegex).collect(Collectors.toSet());
    }

    public InboundEntry saveInboundEntry(InboundEntryResource inboundReource) throws OperationException{
        String id = inboundReource.getId();
        InboundEntry inbound = new InboundEntry();
        InboundEntryCriteria inboundEntryCriteria = new InboundEntryCriteria();
        inboundEntryCriteria.setAwbNo(inboundReource.getAwbNo());
        Map<String, Object> inboundEntryMap = inboundEntryRepository.getByInboundCriteria(inboundEntryCriteria);
        List<InboundEntry> list = (List<InboundEntry>) inboundEntryMap.get("result");

        inboundReource.getInboundLogEntryStatus().stream().map(inboundLogEntryStatus -> inboundEntryStatusRepository.saveOrUpdate(inboundLogEntryStatus)).collect(Collectors.toList());
        if (list.size() > 0) {
            throw new OperationException(Constants.ER02);
        } else {
            CopyUtils.copy(inboundReource, inbound);
            successSaveMessage();
            if (inbound.getPackageList() != null) {
                for (PackageDetails packageDetails : inbound.getPackageList()) {
                    packageDetails.setInboundEntry(inbound);
                }
            }

            return inboundEntryRepository.saveOrUpdate(inbound);

        }
    }


    public InboundEntry updateInboundEntry(InboundEntryResource inboundReource) throws OperationException{
        String id = inboundReource.getId();
        InboundEntry existingInboundEntry = new InboundEntry();
        if(!StringUtils.isEmpty(id)) {
            // checking task exists or not
            existingInboundEntry = inboundEntryRepository.getInboundId(id);
            if(existingInboundEntry == null) {
                log.error("inbound entry not exists for id:"+id);
                throw new OperationException(ER01);
            }
            inboundReource.getInboundLogEntryStatus().stream().map(inboundLogEntryStatus -> inboundEntryStatusRepository.saveOrUpdate(inboundLogEntryStatus)).collect(Collectors.toSet());
            CopyUtils.copy(inboundReource, existingInboundEntry);
            successMessage();
        }else {

            throw new OperationException(Constants.ER711);

        }

        return inboundEntryRepository.saveOrUpdate(existingInboundEntry);

    }

    @Override
    public PlanDelivery setPlanDeliverytoInboundNoList(PlanDeliverytoInboundNoListResource planDeliverytoInboundNoListResource) throws OperationException {
        PlanDelivery existingPlanDelivery = new PlanDelivery();
        PlanDelivery planDelivery = new PlanDelivery();
        existingPlanDelivery.setPlanDeliveryDate(planDeliverytoInboundNoListResource.getPlanDeliveryDate());
        existingPlanDelivery.setDeliveryEmployee(planDeliverytoInboundNoListResource.getDeliveryEmployee());
        existingPlanDelivery.setPlanDeliverySlot(planDeliverytoInboundNoListResource.getPlanDeliverySlot());
        planDelivery = planDeliveryRepository.saveOrUpdate(existingPlanDelivery);
        List<String> inboundNoList = planDeliverytoInboundNoListResource.getInboundNo();
        for(String inboundNo : inboundNoList){
            InboundEntry existingInboundEntry = inboundEntryRepository.getInboundId(inboundNo);
            if (StringUtils.isEmpty(inboundNo)) {
                log.error("Inbound number required.");
                throw new OperationException(Constants.ER17);
            } else if (existingInboundEntry == null) {
                log.error("inbound entry not exists for id:" + inboundNo);
                throw new OperationException(ER01);
            } else {

                existingInboundEntry.setPlanDelivery(planDelivery);
                inboundEntryRepository.saveOrUpdate(existingInboundEntry);
            }
        }
        return planDelivery ;
    }

    @Override
    public PlanDelivery getPlanDeliveryFromInboundNo(String inboundNo) throws OperationException {
        InboundEntry existingInboundEntry = inboundEntryRepository.getInboundId(inboundNo);
        if (StringUtils.isEmpty(inboundNo)) {
            log.error("Inbound number required.");
            throw new OperationException(Constants.ER17);
        } else if (existingInboundEntry == null) {
            log.error("inbound entry not exists for id:" + inboundNo);
            throw new OperationException(ER01);
        } else {
            return existingInboundEntry.getPlanDelivery();
        }
    }

    @Override
    public InboundEntry updatePlanDeliveryFromInboundNo(String inboundNo, PlanDeliveryResource planDeliveryResource) throws OperationException {
        InboundEntry existingInboundEntry = inboundEntryRepository.getInboundId(inboundNo);

        if (StringUtils.isEmpty(inboundNo) || planDeliveryResource == null) {
            log.error("Inbound number or plan delivery details required.");
            throw new OperationException(Constants.ER17);
        } else if (existingInboundEntry == null) {
            log.error("inbound entry not exists for id:" + inboundNo);
            throw new OperationException(ER01);
        } else {
            PlanDelivery planDelivery = existingInboundEntry.getPlanDelivery();
            CopyUtils.copy(planDeliveryResource, planDelivery);
            existingInboundEntry.setPlanDelivery(planDelivery);
            return inboundEntryRepository.saveOrUpdate(existingInboundEntry);
        }

    }

    @Override
    public CommonDelivery getCommonDeliveryFromInbounfNo(String inboundNo) throws OperationException {
        InboundEntry existingInboundEntry = inboundEntryRepository.getInboundId(inboundNo);
        if (StringUtils.isEmpty(inboundNo)) {
            log.error("Inbound number required.");
            throw new OperationException(Constants.ER17);
        } else if (existingInboundEntry == null) {
            log.error("inbound entry not exists for id:" + inboundNo);
            throw new OperationException(ER01);
        } else {
            //return existingInboundEntry.getCommonDelivery();
            return null;
        }
    }
    @Override
    public Set<InboundLogEntryStatus> updateCommonDeliveryFromInboundNo( CommonDeliveryResource commonDeliveryResource) throws OperationException {
        List<String> uniqueInboundIDs = new ArrayList<>();
        InboundEntry existingInboundEntry = new InboundEntry();
        Set<InboundLogEntryStatus> existingInboundLogEntryStatus = new HashSet<>();
        uniqueInboundIDs = commonDeliveryResource.getInboundNo();

        InboundLogEntryStatus inboundLogEntryStatus = inboundEntryStatusRepository.saveOrUpdate(commonDeliveryResource.getInboundLogEntryStatuses().iterator().next());
        existingInboundLogEntryStatus.add(inboundLogEntryStatus);

        for(String uniqueInboundId : uniqueInboundIDs){
        existingInboundEntry = inboundEntryRepository.getInboundId(uniqueInboundId);
        if (StringUtils.isEmpty(uniqueInboundId) || commonDeliveryResource == null) {
            log.error("Inbound number or Common Delivery details required.");
            throw new OperationException(Constants.ER17);
        } else if (existingInboundEntry == null) {
            log.error("inbound entry not exists for id:" + commonDeliveryResource.getInboundNo());
            throw new OperationException(ER01);
        } else {
            /*existingInboundLogEntryStatus = existingInboundEntry.getInboundLogEntryStatus();*/
            existingInboundEntry.setInboundLogEntryStatus(existingInboundLogEntryStatus);
            inboundEntryRepository.saveOrUpdate(existingInboundEntry);
        }
    }

    return existingInboundLogEntryStatus;
    }


    private String successSaveMessage() {
        return messages.get("BM01");
    }

    public Map<String, Object> getByInboundCriteria(InboundEntryCriteria inboundEntryCriteria)throws OperationException{

        Map<String, Object> inboundEntryMap= inboundEntryRepository.getByInboundCriteria(inboundEntryCriteria);
        if (inboundEntryMap == null || inboundEntryMap.size() == 0) {
            throw new OperationException(Constants.ER01);
        }
        return inboundEntryMap;
    }

    public InboundEntry getInboundId(String inboundId){

        return inboundEntryRepository.getInboundId(inboundId);
    }

    @Override
    public List<String> getInboundNo(String param) {
        return inboundEntryRepository.inboundNoList(param);
    }

    @Override
    public List<String> getAwbNo() {
        return inboundEntryRepository.awbNo();
    }

    @Override
    public List<String> getSenderName() {
        return inboundEntryRepository.senderNames();
    }

    @Override
    public List<String> getRecipientName() {
        return inboundEntryRepository.recipientNames();
    }

    @Override
    public List<String> getDepartments() {
        return inboundEntryRepository.departments();
    }

    @Override
    public MailRoomMaster addEmployeeDetails(EmployeeDetailsResource employeeDetailsResource) throws OperationException {

        MailRoomMaster mailRoomMaster  = mailRoomRepository.getId(employeeDetailsResource.getMailRoomId());
        if (mailRoomMaster == null) {
            throw new OperationException(Constants.ER01);
        }
        EmployeeDetails employeeDetails = new EmployeeDetails();
        Set<EmployeeDetails> employeeDetailsPrevList = new HashSet<>();
        employeeDetailsPrevList = mailRoomMaster.getEmployeeDetails();
        if (employeeDetailsResource.getEmployeeActivityExpiryDate() != null && employeeDetailsResource.getEmployeeActivityStartDate() != null &&
                employeeDetailsResource.getEmployeeEmailId() != null && employeeDetailsResource.getEmployeeId() != null) {

            CopyUtils.copy(employeeDetailsResource, employeeDetails);
            employeeDetails.setEmpStatus(employeeDetailsResource.getEmployeeStatus());
            employeeDetailsPrevList.add(employeeDetails);
            mailRoomMaster.setEmployeeDetails(employeeDetailsPrevList);
            return mailRoomRepository.saveOrUpdate(mailRoomMaster);
        } else {
            throw new OperationException("Provide atleast one Employee's detail");

        }

    }

    @Override
    public Set<EmployeeDetails> getEmployeeDetails(String mailRoomId) throws OperationException {
        Set<EmployeeDetails> employeeDetailsList = new HashSet<>();
        MailRoomMaster mailRoomMaster = mailRoomRepository.getId(mailRoomId);
        if(mailRoomMaster != null ) {
            employeeDetailsList = mailRoomMaster.getEmployeeDetails();
        }
            return employeeDetailsList;
    }

    public OutboundDispatch saveOutboundDispatch(OutboundDispatchResource otboundDispatchReource) throws OperationException{
       OutboundDispatchStatus outboundDispatchStatus = new OutboundDispatchStatus();
        OutboundDispatch outBoundDispatch = new OutboundDispatch();
        ExistingOutboundDispatchCriteria existingOutboundDispatchCriteria = new ExistingOutboundDispatchCriteria();
//        existingOutboundDispatchCriteria.setDispatchId(otboundDispatchReource.getDispatchId());
        existingOutboundDispatchCriteria.setReferenceNumber(otboundDispatchReource.getReferenceNumber());
        existingOutboundDispatchCriteria.setLinkInBoudNumber(otboundDispatchReource.getLinkInBoudNumber());
        List<OutboundDispatch> outboundEntryList = outBoundDispatchRepository.getByExistingOutboundCriteria(existingOutboundDispatchCriteria);
        if (outboundEntryList != null && outboundEntryList.size() > 0) {
            throw new OperationException(Constants.ER02);
        } else {


            CopyUtils.copy(otboundDispatchReource, outBoundDispatch);
            outboundDispatchStatus.setStatus(otboundDispatchReource.getDispatchStatus().getStatus());
            outboundDispatchStatus.setDateOfDelivery(otboundDispatchReource.getDispatchStatus().getDateOfDelivery());
            outboundDispatchStatus.setRemarks(otboundDispatchReource.getDispatchStatus().getRemarks());
            outBoundDispatch.setStatus(outboundDispatchStatus);
            outBoundDispatch.setDispatchId(UUID.randomUUID().toString()); //only doing this during saving. We should not allow it to change in edit or update in the UI
            return outBoundDispatchRepository.saveOrUpdate(outBoundDispatch);
        }
       /* OutboundStatus dispatchStatus= dispatchStatusRepository.getById(otboundDispatchReource.getStatusId());
       outBoundDispatch.setOutboundStatus(dispatchStatus);*/
       /* Set<Tracker> trackSet=new HashSet<>();
        Set<TrackerResource> track=otboundDispatchReource.getTracker();
        Tracker tempTracker=new Tracker();
        for(TrackerResource t:track){
            String statusId=t.getStatusId();

           // tempTracker.setOutboundStatus(dispatchStatusRepository.getById(statusId));
            tempTracker.setDispatchDate(t.getDispatchDate());
            tempTracker.setDispatchStatus(t.getDispatchStatus());
            trackSet.add(tempTracker);
        }
        outBoundDispatch.setTracker(trackSet);*/


    }

    public OutboundDispatch updateOutboundDispatch(OutboundDispatchResource otboundDispatchReource) throws OperationException{
        OutboundDispatch existingOutBoundDispatch = new OutboundDispatch();
        String id = otboundDispatchReource.getId();
        if(!StringUtils.isEmpty(id)){
            existingOutBoundDispatch = outBoundDispatchRepository.getOutBoundId(id);
            System.out.println("existingOutBoundDispatch"+existingOutBoundDispatch);
            if(existingOutBoundDispatch == null) {
                log.error("outbound dispatch not exists for id:"+id);
                throw new OperationException(ER01);
            }
            if(existingOutBoundDispatch.getStatus().getStatus()==DispatchStatus.PARCEL_NOT_CREATED || existingOutBoundDispatch.getStatus().getStatus()==DispatchStatus.PARCEL_CREATED_BUT_NOT_DISPATCHED || existingOutBoundDispatch.getStatus().getStatus()==DispatchStatus.PARCEL_DISPATCHED_AWB_NOT_UPDATED || existingOutBoundDispatch.getStatus().getStatus()==DispatchStatus.AWB_UPDATED_BILL_AMOUNT_NOT_UPDATED || existingOutBoundDispatch.getStatus().getStatus()==DispatchStatus.IN_TRANSIT)
            {
                CopyUtils.copy(otboundDispatchReource, existingOutBoundDispatch);
            }
            return outBoundDispatchRepository.saveOrUpdate(existingOutBoundDispatch);
        }
        else{
            log.error("Outbound Dispatch ID required");
            throw new OperationException(Constants.ER17);
        }
        /*OutboundStatus dispatchStatus= dispatchStatusRepository.getById(otboundDispatchReource.getStatusId());
        outBoundDispatch.setOutboundStatus(dispatchStatus);*/
       /* Set<Tracker> trackSet=new HashSet<>();
        Set<TrackerResource> track=otboundDispatchReource.getTracker();
        for(TrackerResource t:track){
            String statusId=t.getStatusId();
            Tracker tempTracker=new Tracker();
            //tempTracker.setOutboundStatus(dispatchStatusRepository.getById(statusId));
            tempTracker.setDispatchDate(t.getDispatchDate());
            trackSet.add(tempTracker);
        }
        outBoundDispatch.setTracker(trackSet);*/


    }

    public Map<String, Object> getOutBoundCriteria(OutBoundDispatchCriteria outBoundDispatchCriteria)throws OperationException{
        Map<String, Object> outBoundDispatchMap= outBoundDispatchRepository.getByOutBoundCriteria(outBoundDispatchCriteria);
        if(outBoundDispatchMap.size() == 0 ) {
            throw new OperationException(Constants.ER01);
        }
        return outBoundDispatchMap;
    }

    public OutboundDispatch getOutBoundId(String dispatchId) {

        return outBoundDispatchRepository.getOutBoundId(dispatchId);
    }


    @Override
    public List<OutboundDispatch> getOutboundsFromParcelId(String parcelID) throws OperationException {
        List<OutboundDispatch> outboundDispatchList = new ArrayList<>();
        Parcel parcel = parcelRepository.getParcelId(parcelID);
        outboundDispatchList = parcel.getOutboundDispatches();
        if (outboundDispatchList != null) {
            return outboundDispatchList;
        } else {
            throw new OperationException(Constants.ER01);
        }

    }

    public Parcel saveParcelElementForDispatchId(ParcelCreationForOutboundIdResource parcelCreationForOutboundIdResource)throws OperationException {

        OutboundDispatchStatus outboundDispatchStatus = new OutboundDispatchStatus();
        OutboundDispatchResource otboundDispatchReource= new OutboundDispatchResource();
        ExistingOutboundDispatchCriteria existingOutboundDispatchCriteria = new ExistingOutboundDispatchCriteria();
        SearchParcelCriteria searchParcelCriteria = new SearchParcelCriteria();
        List<OutboundDispatch> existingOutboundList = new ArrayList<>();
        OutboundDispatch existingOutbound = new OutboundDispatch();
        List<String> ids = parcelCreationForOutboundIdResource.getDispatchUniqueId();
        Parcel parcel=new Parcel();
        searchParcelCriteria.setParcelId(parcelCreationForOutboundIdResource.getParcelId());
        Parcel existingParcel = parcelRepository.searchByCriteria(searchParcelCriteria);
        if(existingParcel != null)
        {
            log.error("Parcel already exists with this Parcel ID.");
            throw new OperationException(ER02);
        }
        else{
        CopyUtils.copy(parcelCreationForOutboundIdResource, parcel);}
        parcelRepository.saveOrUpdate(parcel);
        Parcel savedParcel = parcelRepository.searchByCriteria(searchParcelCriteria);
       // String savedParcelId = savedParcel.getId();
        for(String id : ids) {
            // CreateParcelResource createParcelResource = null;
            if (!StringUtils.isEmpty(id)) {
                existingOutbound = outBoundDispatchRepository.getOutBoundId(id);

                if (existingOutbound == null) {
                    log.error("outbound dispatch not exists for id:" + id);
                    throw new OperationException(ER01);
                }
                CopyUtils.copy(existingOutbound,otboundDispatchReource);
                outboundDispatchStatus.setStatus(DispatchStatus.PARCEL_CREATED_BUT_NOT_DISPATCHED);
                outboundDispatchStatus.setDateOfDelivery(otboundDispatchReource.getDispatchStatus().getDateOfDelivery());
                outboundDispatchStatus.setRemarks(otboundDispatchReource.getDispatchStatus().getRemarks());
                existingOutbound.setStatus(outboundDispatchStatus);

                existingOutboundList.add(existingOutbound);
                existingOutbound.setParcel(savedParcel);

                successMessage();
                outBoundDispatchRepository.saveOrUpdate(existingOutbound);

            } else {
                log.error("outbound dispatch id not mentioned" + id);
                throw new OperationException(ER08);
            }
        }
        savedParcel.setOutboundDispatches(existingOutboundList);
        return parcelRepository.saveOrUpdate(savedParcel);
    }


    public PlanCollection savePlanCollectionForDispatchId(PlanCollectionForOutboundIdResource planCollectionForOutboundIdResource) throws OperationException {
        List<String> ids = new ArrayList<>();
        ids = planCollectionForOutboundIdResource.getDispatchId();
        OutboundDispatch outboundDispatch = new OutboundDispatch();
        PlanCollection planCollection = new PlanCollection();
            for (String id : ids) {
                outboundDispatch = outBoundDispatchRepository.getOutBoundId(id);
                if (StringUtils.isEmpty(id) || planCollectionForOutboundIdResource == null) {
                    log.error("Outbound Id or Plan Delivery details required.");
                    throw new OperationException(Constants.ER17);
                } else if (outboundDispatch == null) {
                    log.error("Outbound entry not exists for id:" + id);
                    throw new OperationException(ER01);
                } else {
                    CopyUtils.copy(planCollectionForOutboundIdResource, planCollection);
                    outboundDispatch.setPlanCollection(planCollection);
                    outBoundDispatchRepository.saveOrUpdate(outboundDispatch);
                }
            }
        return planCollection;
    }

    public void deleteDispatch(String id){
        outBoundDispatchRepository.deleteDispatch(id);
    }



    public Parcel saveParcelElement(CreateParcelResource createParcelReource) throws OperationException{
        SearchParcelCriteria searchParcelCriteria = new SearchParcelCriteria();
        searchParcelCriteria.setParcelId(createParcelReource.getParcelId());
        Parcel existingParcel = parcelRepository.searchByCriteria(searchParcelCriteria);
        Parcel parcel= new Parcel();
        if(existingParcel != null) {
            throw new OperationException(Constants.ER02);
            } else {
                CopyUtils.copy(createParcelReource, parcel);

        return parcelRepository.saveOrUpdate(parcel);
        }
    }

    @Override
    public Parcel updateParcelElement(UpdateParcelResource updateParcelResource) throws OperationException {
       Parcel existingParcel = parcelRepository.getParcelId(updateParcelResource.getId());

       if(existingParcel == null){
           throw new OperationException(Constants.ER01);
       } else{
           CopyUtils.copy(updateParcelResource,existingParcel);
          // existingParcel.setId(updateParcelResource.getUniqueID());

       }
        return parcelRepository.saveOrUpdate(existingParcel);
    }

    @Override
    public List<Parcel> getParcels() throws OperationException {

        List<Parcel> parcels = parcelRepository.getParcels();
        return parcels;
    }

    public ParcelResource saveOutboundDispatchToPreCreatedParcel(OutboundDispathToPreCreatedParcelResource outboundDispathToPreCreatedParcelResource) throws OperationException {
        OutboundDispatchResource otboundDispatchReource = new OutboundDispatchResource();
        OutboundDispatchStatus outboundDispatchStatus = new OutboundDispatchStatus();
        OutboundDispatch existingOutbound = new OutboundDispatch();
        ParcelResource parcelResource = new ParcelResource();
        List<OutboundDispatch> existingOutboundList = new ArrayList<>();
        List<OutboundDispatch> existingOutboundIDinParcel = new ArrayList<>();
        SearchParcelCriteria searchParcelCriteria = new SearchParcelCriteria();
        List<String> dispatchIds = outboundDispathToPreCreatedParcelResource.getSavedDispatchId();
        searchParcelCriteria.setParcelId(outboundDispathToPreCreatedParcelResource.getParcelId());
        Parcel parcel = parcelRepository.searchByCriteria(searchParcelCriteria);
        if(parcel == null){
            log.error("Parcel does not exists for id:");
            throw new OperationException(Constants.ER01);
        }
        else{
            for(String id : dispatchIds) {
                if (!StringUtils.isEmpty(id)) {
                    existingOutbound = outBoundDispatchRepository.getOutBoundId(id);

                    if (existingOutbound == null) {
                        log.error("outbound dispatch not exists for id:" + id);
                        throw new OperationException(ER01);
                    }
                    if(existingOutbound.getStatus().getStatus()==DispatchStatus.PARCEL_NOT_CREATED || existingOutbound.getStatus().getStatus()==DispatchStatus.PARCEL_CREATED_BUT_NOT_DISPATCHED ){

                    CopyUtils.copy(existingOutbound,otboundDispatchReource);
                    outboundDispatchStatus.setStatus(DispatchStatus.PARCEL_CREATED_BUT_NOT_DISPATCHED);
                    outboundDispatchStatus.setDateOfDelivery(otboundDispatchReource.getDispatchStatus().getDateOfDelivery());
                    outboundDispatchStatus.setRemarks(otboundDispatchReource.getDispatchStatus().getRemarks());
                    existingOutbound.setStatus(outboundDispatchStatus);
                    existingOutboundList.add(existingOutbound);

                    existingOutbound.setParcel(parcel);
                    successMessage();
                    outBoundDispatchRepository.saveOrUpdate(existingOutbound);
                    }
                    else{log.error("Previous status of the outbound is not PARCEL_NOT_CREATED or PARCEL_CREATED_BUT_NOT_DISPATCHED");
                    throw new OperationException(ER314);
                    }

                } else {
                    log.error("outbound dispatch id not mentioned" + id);
                    throw new OperationException(ER08);
                }
            }

            existingOutboundIDinParcel = parcel.getOutboundDispatches();
            existingOutboundList.addAll(existingOutboundIDinParcel);
            Set<OutboundDispatch> existingOutboundSet = new HashSet<OutboundDispatch>(existingOutboundList);
            existingOutboundList = existingOutboundSet.stream().collect(Collectors.toList());
            parcel.setOutboundDispatches(existingOutboundList);
            parcelRepository.saveOrUpdate(parcel);
            CopyUtils.copy(parcel,parcelResource);
            return parcelResource;

        }
    }

    public Parcel getParcelId(String parcelId){

        return parcelRepository.getParcelId(parcelId);
    }

    public List<String> getParcelIds(){
        List<String> parcelIdsList = new ArrayList<>();
        List<Parcel> parcelList= parcelRepository.getParcels();
        List<String> parcelIds = parcelList.stream().map(x -> x.getParcelId()).collect(Collectors.toList());
        Set<String> parcelIdsSet = new HashSet<>(parcelIds);
        parcelIdsList = parcelIdsSet.stream().collect(Collectors.toList());
        return parcelIdsList;
    }

    @Override
    public Parcel getParcelFromDispatchId(String dispatchID) throws OperationException {
        Parcel parcel = new Parcel();
        OutboundDispatch outboundDispatch = outBoundDispatchRepository.getOutBoundId(dispatchID);
        if (outboundDispatch != null) {
            parcel = outboundDispatch.getParcel();
            return parcel;
        } else {
            throw new OperationException(ER01);
        }

    }

    public Parcel getByCriteria(SearchParcelCriteria searchParcelCriteria){
        return parcelRepository.searchByCriteria(searchParcelCriteria);
    }

    @Override
    public List<OutboundDispatch> getOutboundsDetach(OutboundsDetachFromParcel outboundsDetachFromParcel) throws OperationException {
        List<OutboundDispatch> outboundDispatchList = new ArrayList<>();
        OutboundDispatch outboundDispatch = new OutboundDispatch();
        for (String outboundID:
             outboundsDetachFromParcel.getOutboundIds()) {

        outboundDispatch = outBoundDispatchRepository.getOutBoundId(outboundID);
        outboundDispatch.setParcel(null);
        outBoundDispatchRepository.saveOrUpdate(outboundDispatch);
        }
        Parcel parcel = parcelRepository.getParcelId(outboundsDetachFromParcel.getParcelId());
        outboundDispatchList = parcel.getOutboundDispatches();
        if(outboundDispatchList != null){
            return outboundDispatchList;
        }
        else{
            throw new OperationException("No dispatches attached to this parcel");
        }
    }

    public List<String> suggest(String attribute, String value){
        List<String> result =  inboundEntryRepository.suggest(attribute, value).stream()
                .map(inboundRecipientDetails -> inboundRecipientDetails.getEmpName())
                .collect(Collectors.toList());
        Collections.sort(result, (a, b) -> a.toUpperCase().indexOf(value.toUpperCase()) - b.toUpperCase().indexOf(value.toUpperCase()));
        return result;

    }
    private void validateTask(MailRoomMasterResource mailRoomMasterResource) throws OperationException {
        checkDuplicate(mailRoomMasterResource);

    }




    private void checkDuplicate(MailRoomMasterResource mailRoomMasterResource) throws OperationException {
        MailRoomSearchCriteria mailRoomCriteria=new MailRoomSearchCriteria();
        mailRoomCriteria.setMailRoomName(mailRoomMasterResource.getMailRoomName());
        Map<String, Object> roomMaster = mailRoomRepository.getByCriteria(mailRoomCriteria);
        if(!CollectionUtils.isEmpty(roomMaster)) {
            throw new OperationException(ER02);
        }

    }

    /* public String getBarCode(String id,String companyName,String consigneeName,String consigneeLocation) throws FileNotFoundException, DocumentException {
             List<String> barCodeList=new ArrayList<String>();
             barCodeList.add(id);
             barCodeList.add(companyName);
             barCodeList.add(consigneeName);
             barCodeList.add(consigneeLocation);
            String barCode= BarCodeUtil.generateBarCodes(barCodeList);

             return barCode;

         }
     */
    public BarCode saveBarCode(BarCodeResource barCodeResource) throws OperationException{
        BarCode barCode=new BarCode();
        String id=barCodeResource.getId();
        if(!StringUtils.isEmpty(id)) {
            // checking task exists or not
            barCode= barCodeRepository.getBarCodeId(id);
            if(barCode == null) {
                log.error("mail room not exists for id:"+id);
                throw new OperationException(ER01);
            }
            CopyUtils.copy(barCodeResource, barCode);
           /* RoomStatus roomStatus= statusRepository.getById(mailRoomMasterResource.getRoomStatus());
            roomMaster.setRoomStatus(roomStatus);
            successMessage();*/
        }
        else{
            CopyUtils.copy(barCodeResource, barCode);
        }
        return barCodeRepository.saveOrUpdate(barCode);
    }

    public String generateBarCode(String id) throws FileNotFoundException {
        List<String> barcodes=new ArrayList<String>();
        //BarCode code=barCodeRepository.getBarCodeId(id);
        barcodes.add(id);
        // barcodes.add(String.valueOf(barcodes));
    //    BarCodeUtil.generateBarCodes(barcodes);
      //  String barCode= BarCodeUtil.generateBarCodes(barcodes);;
        String barCode=null;
        return barCode;
    }

    public BarCode getBarCodeId(String parcelId){

        return barCodeRepository.getBarCodeId(parcelId);
    }

    @Override
    public List<String> getInboundNo() {
        return null;
    }


    public ZonedDateTime getCurrentTime() {
        return ZonedDateTime.parse(ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME),DateTimeFormatter.ISO_DATE_TIME);
    }


    //------------------WORKFLOW IMPLEMENTATION MASTER----------------------------------------------------------

    public Object saveWorkflowRecord(MailRoomMasterResource mailRoomMasterResource, Boolean draft, String id)throws OperationException {

        MailRoomSearchCriteria maiRoomCriteria = new MailRoomSearchCriteria();
        maiRoomCriteria.setMailRoomName(mailRoomMasterResource.getMailRoomName());
//        maiRoomCriteria.setCity(mailRoomMasterResource.getCity());
        Map<String, Object> mailRoomMasterMap = mailRoomRepository.getByCriteria(maiRoomCriteria);
        List<MailRoomMaster> list = (List<MailRoomMaster>) mailRoomMasterMap.get("data");
        if (list.size() > 0)
            throw new OperationException(Constants.OPS_ERR_Mail_Room_02);

        String workflowResponse;
        String userId = userService.getLoggedInUserId();

        mailRoomMasterResource.setAction("Active");
        mailRoomMasterResource.setStatus(WorkflowEnums.APPROVED.PENDING_APPROVAL);
        mailRoomMasterResource.setLastModifiedByUserId(userId);
        mailRoomMasterResource.setLastModifiedOn(getCurrentTime());
        mailRoomMasterResource.setCreatedOn(getCurrentTime());

        //When draft = true and no id is passed that means we will be creating a workflow and only saving it
        //Add - Save
        if(draft!=null && draft == true && (id==null || StringUtils.isEmpty(id)))
        {
            workflowResponse = workflowService.saveWorkflow(mailRoomMasterResource, WorkflowOperation.SAVE,MasterEntityName, userId);

            if(workflowResponse==null){
                throw new OperationException("Unable to save Workflow");
            }

            try {

                JSONObject workflowObject = new JSONObject(workflowResponse);
                return workflowObject;
            } catch (Exception e) {
                throw new OperationException("Add - Save method failed");
            }
        }
        //When draft is null or empty or false and no id is passed that means we will be creating a workflow and
        //and it will be submitted for approval
        //Add - Submit
        else if((draft==null || StringUtils.isEmpty(draft) || draft==false) && (id==null || StringUtils.isEmpty(id)))
        {
            workflowResponse = workflowService.saveWorkflow(mailRoomMasterResource, WorkflowOperation.SUBMIT,MasterEntityName, userId);

            if(workflowResponse==null){
                throw new OperationException("Unable to save Workflow");
            }

            try {

                JSONObject workflowObject = new JSONObject(workflowResponse);

                return workflowObject;
            } catch (Exception e) {
                throw new OperationException("Add - Submit method failed");
            }
        }
        //When draft is null or empty or false and id is also passed that means we will be updating an existing workflow for that id passed
        //and it will be submitted for approval
        //Edit - Save
        else if(draft!=null && draft == true && id!=null && !StringUtils.isEmpty(id))
        {
            workflowResponse = workflowService.updateWorkflowDoc(mailRoomMasterResource, WorkflowOperation.SAVE, id);

            if(workflowResponse==null){
                throw new OperationException("Unable to update Workflow");
            }

            try {
                JSONObject workflowObject = new JSONObject(workflowResponse);

                return workflowObject;
            } catch (Exception e) {
                throw new OperationException("Edit - Save method failed");
            }
        }
        //When draft is true and id is also passed that means we will be updating an existing workflow for that id passed
        //and saving it only
        //Edit - Submit
        else if((draft==null || StringUtils.isEmpty(draft) || draft==false) && id!=null && !StringUtils.isEmpty(id))
        {
            workflowResponse = workflowService.updateWorkflowDoc(mailRoomMasterResource, WorkflowOperation.SUBMIT, id);

            if(workflowResponse==null){
                throw new OperationException("Unable to update Workflow");
            }
            try {
                JSONObject workflowObject = new JSONObject(workflowResponse);

                return workflowObject;
            } catch (Exception e) {
                throw new OperationException("Edit - Submit method failed");
            }
        }
        else {
            throw new OperationException("Invalid workflow Operation");
        }
    }

    @Override
    public MailRoomMaster releaseMasterLock(String id) throws OperationException{

        MailRoomMaster mailRoomMaster = null;
        String userId = userService.getLoggedInUserId();

        MailRoomMaster savedMailRoomMasterRcrds = getMasterRecordByID(id);

        if (savedMailRoomMasterRcrds.getLock()!=null && Boolean.TRUE.equals(savedMailRoomMasterRcrds.getLock().isEnabled()))
        {
            if(savedMailRoomMasterRcrds.getLock().getUserId().equals(userId))
            {
                String response = workflowService.releaseWorkflow(savedMailRoomMasterRcrds.getLock().getWorkflowId());

                if (response != null)
                {
                    RequestLockObject lockobject = savedMailRoomMasterRcrds.getLock();

                    lockobject.setEnabled(false);
                    lockobject.setUserId(userId);
                    lockobject.setWorkflowId(null);

                    savedMailRoomMasterRcrds.setLock(lockobject);

                    savedMailRoomMasterRcrds.setLastModifiedByUserId(userId);
                    savedMailRoomMasterRcrds.setLastModifiedOn(ZonedDateTime.now(ZoneOffset.UTC));

                    mailRoomMaster = mailRoomRepository.update(savedMailRoomMasterRcrds);
                }
                else{
                    throw new OperationException(Constants.UNABLE_TO_RELEASE_LOCK, id);
                }
            }
            else{
                log.warn("Cannot Release: Locked by another user "+ savedMailRoomMasterRcrds.getLock().getUserId());
                throw new OperationException(Constants.LOCKED_BY_ANOTHER_USER, savedMailRoomMasterRcrds.getLock().getUserId());
            }
        } else {
            log.warn("Alternate Option Configuration with id"+ id +" to be released is not locked");
            throw new OperationException(Constants.NOT_LOCKED, id);
        }
        return mailRoomMaster;
    }

    @Override
    public MailRoomMaster getMasterRecordByID(String id) throws OperationException {

        MailRoomMaster mailRoomMaster;
        try {
            mailRoomMaster = mailRoomRepository.fetchMailroomMasterForConfigId(id);

        } catch (Exception e) {
            log.error("Exception occurred while fetching Alternate Options Configuration for id " + id, e);
            throw new OperationException(Constants.UNABLE_TO_FETCH_DETAILS);
        }
        if (mailRoomMaster == null) {
            log.error("No Alternate Options Configuration found for id " + id);
            throw new OperationException(Constants.NO_RESULT_FOUND);
        }
        return mailRoomMaster;
    }

    @Override
    public Object EditWorkflowMasterRecord(String id, Boolean workflow,Boolean lock) throws OperationException {

        String workflowResponse;
        String userId = userService.getLoggedInUserId();

        //If ID passed is Workflow ID
        if(workflow!=null && !StringUtils.isEmpty(workflow) && workflow==true)
        {
            workflowResponse = workflowService.getWorkFlowById(id);

            JSONObject workflowObject = new JSONObject(workflowResponse);

            try {
                /*MailRoomMasterResource mailRoomMaster = objectMapper.readValue(
                        (workflowObject.getJSONArray("data").getJSONObject(0).getJSONObject("doc").getJSONObject("newDoc").toString()), MailRoomMasterResource.class);

                mailRoomMaster.set_id(workflowObject.getJSONArray("data").getJSONObject(0).getString("_id"));*/

                return workflowObject.getJSONArray("data").getJSONObject(0);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        //If ID passed is Master ID
        else if(workflow==null || StringUtils.isEmpty(workflow) || workflow==false)
        {
            MailRoomMaster savedMailroomMasterRecords = getMasterRecordByID(id);
            if (savedMailroomMasterRecords == null) {
                log.error("Alternate Options Configuration does not exists for id: " + id);
                throw new OperationException(Constants.ER01);
            }

            //Check if the Master Data already has a lock
            if (savedMailroomMasterRecords.getLock()==null || Boolean.FALSE.equals(savedMailroomMasterRecords.getLock().isEnabled()))
            {

                if(lock!=null && !StringUtils.isEmpty(lock) && lock==true)
                {
                    RequestLockObject lockobject = new RequestLockObject();
                    lockobject.setEnabled(true);
                    lockobject.setUserId(userId);

                    lockobject.setMailRoomMaster(savedMailroomMasterRecords);

                    savedMailroomMasterRecords.setLock(lockobject);
                    savedMailroomMasterRecords.set_id(id);

                    String workflowId = workflowService.editMasterObject(savedMailroomMasterRecords,MasterEntityName, userId, id);
                    workflowResponse = workflowService.getWorkFlowById(workflowId);
                    if(workflowResponse==null){
                        throw new OperationException("Unable to save Workflow");
                    }

                    JSONObject workflowObject = new JSONObject(workflowResponse);

                    try {

                        lockobject.setWorkflowId(workflowObject.getJSONArray("data").getJSONObject(0).getString("_id"));
                        savedMailroomMasterRecords.setLock(lockobject);
                        MailRoomMaster mailMaster = mailRoomRepository.update(savedMailroomMasterRecords);

                        mailMaster.getLock().setUser(mailMaster.getLock().getUserId());

                        return mailMaster;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
                else
                {
                    String workflowId = workflowService.editMasterObject(savedMailroomMasterRecords, MasterEntityName, userId, id);

                    workflowResponse = workflowService.getWorkFlowById(workflowId);

                    if(workflowResponse==null){
                        throw new OperationException("Unable to save Workflow");
                    }

                    JSONObject workflowObject = new JSONObject(workflowResponse);

                    try {
                        MailRoomMaster mailRoomMaster = objectMapper.readValue(
                                (workflowObject.getJSONArray("data").getJSONObject(0).getJSONObject("doc").getJSONObject("newDoc").toString()), MailRoomMaster.class);

                        mailRoomMaster.set_id(workflowObject.getString("_id"));
                        mailRoomMaster.getLock().setUser(mailRoomMaster.getLock().getUserId());

                        return mailRoomMaster;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
            else
            {
                //Instead of throwing exception, return the booking as it has to be displayed in read-only mode.
                //UI will check the userId of the returned booking and if it is not same as the logged in user
                //then they will display it in read-only mode and prompt the message "Locked by "
               /* log.info("Locked by user: " + savedMailroomMasterRecords.getLock().getUserId());

                workflowResponse = workflowService.getWorkFlowById(savedMailroomMasterRecords.getLock().getWorkflowId());

                JSONObject workflowObject = new JSONObject(workflowResponse);

                try {
                    MailRoomMaster mailRoomMaster = objectMapper.readValue(
                            (workflowObject.getJSONArray("data").getJSONObject(0).getJSONObject("doc").getJSONObject("newDoc").toString()), MailRoomMaster.class);

                    mailRoomMaster.set_id(workflowObject.getJSONArray("data").getJSONObject(0).getString("_id"));

                    //return workflowObject.getJSONArray("data").getJSONObject(0);

                    return mailRoomMaster;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }*/
                return savedMailroomMasterRecords;
            }

        }
        else
        {
            throw new OperationException("Invalid workflow Operation");
        }

    }

    @Override
    public Object viewWorkflowMasterRecord(String id, Boolean workflow) throws OperationException {

        String workflowResponse;
        String userId = userService.getLoggedInUserId();

        //If ID passed is Workflow ID
        if(workflow!=null && !StringUtils.isEmpty(workflow) && workflow==true)
        {
            workflowResponse = workflowService.getWorkFlowById(id);

            JSONObject workflowObject = new JSONObject(workflowResponse);

            try {
                MailRoomMasterResource mailRoomMaster = objectMapper.readValue(
                        (workflowObject.getJSONArray("data").getJSONObject(0).getJSONObject("doc").getJSONObject("newDoc").toString()), MailRoomMasterResource.class);

                mailRoomMaster.set_id(workflowObject.getJSONArray("data").getJSONObject(0).getString("_id"));

                return workflowObject.getJSONArray("data").getJSONObject(0);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        //If ID passed is Master ID
        else if(workflow==null || StringUtils.isEmpty(workflow) || workflow==false)
        {
            MailRoomMaster savedMailroomMaster = getMasterRecordByID(id);
            if (savedMailroomMaster == null) {
                log.error("Alternate Options Configuration does not exists for id: " + id);
                throw new OperationException(Constants.ER01);
            }
            return savedMailroomMaster;
        }
        else
        {
            throw new OperationException("Invalid workflow Operation");
        }
    }

    @Override
    public Map<String, Object> getMasterWorkflowList(MasterSearchCriteria masterSearchCriteria) throws OperationException{

        String userId = userService.getLoggedInUserId();

        MailroomSearchCriteriaSorted masterSearchCriteriaFilter = masterSearchCriteria.getFilter();

        JSONObject workflowFilterObject = new JSONObject();

        if(masterSearchCriteriaFilter.getId()!=null && !masterSearchCriteriaFilter.getId().isEmpty())
            workflowFilterObject.putOpt("doc.newDoc.id", masterSearchCriteriaFilter.getId());

        if(masterSearchCriteriaFilter.getMailRoomName()!=null && !masterSearchCriteriaFilter.getMailRoomName().isEmpty())
            workflowFilterObject.putOpt("doc.newDoc.mailRoomName", masterSearchCriteriaFilter.getMailRoomName());

        if(masterSearchCriteriaFilter.getCountry()!=null && !masterSearchCriteriaFilter.getCountry().isEmpty())
            workflowFilterObject.putOpt("doc.newDoc.country", masterSearchCriteriaFilter.getCountry());

        if(masterSearchCriteriaFilter.getState()!=null && !masterSearchCriteriaFilter.getState().isEmpty())
            workflowFilterObject.putOpt("doc.newDoc.state", masterSearchCriteriaFilter.getState());

        if(masterSearchCriteriaFilter.getCity()!=null && !masterSearchCriteriaFilter.getCity().isEmpty())
            workflowFilterObject.putOpt("doc.newDoc.city", masterSearchCriteriaFilter.getCity());

        if(masterSearchCriteriaFilter.getRoomStatus()!=null && !masterSearchCriteriaFilter.getRoomStatus().getValue().isEmpty())
            workflowFilterObject.putOpt("doc.newDoc.roomStatus", masterSearchCriteriaFilter.getRoomStatus());

        Map<String, Object> workflowList = workflowService.getWorkFlows(MasterEntityName, userId, workflowFilterObject, masterSearchCriteria.getSort(), masterSearchCriteria.getPage(), masterSearchCriteria.getCount());
        return workflowList;
    }

//--------------------------------------------------------WORKFLOW IMPLEMENTATION INBOUND----------------------------------------------------------
    @Override
    public Object saveInboundRecord(InboundEntryResource inboundEntryResource, Boolean draft, String id)throws OperationException {

    try {
        InboundEntryCriteria inboundEntryCriteria = new InboundEntryCriteria();
        inboundEntryCriteria.setAwbNo(inboundEntryResource.getAwbNo());
        Map<String, Object> inboundEntryMap = inboundEntryRepository.getByInboundCriteria(inboundEntryCriteria);
        List<InboundEntry> list = (List<InboundEntry>) inboundEntryMap.get("data");
        if (list.size() > 0) {
            throw new OperationException(Constants.ER02);
        }

        String workflowResponse;
        String userId = userService.getLoggedInUserId();

        inboundEntryResource.setAction("Active");
        inboundEntryResource.setStatus(WorkflowEnums.PENDING_APPROVAL);
        inboundEntryResource.setLastModifiedByUserId(userId);
        inboundEntryResource.setLastModifiedOn(getCurrentTime());
        inboundEntryResource.setCreatedOn(getCurrentTime());

        //When draft = true and no id is passed that means we will be creating a workflow and only saving it
        //Add - Save
        if (draft != null && draft == true && (id == null || StringUtils.isEmpty(id))) {
            workflowResponse = workflowService.saveWorkflow(inboundEntryResource, WorkflowOperation.SAVE, InboundEntityName, userId);

            if (workflowResponse == null) {
                throw new OperationException("Unable to save Workflow");
            }
            try {
                JSONObject workflowObject = new JSONObject(workflowResponse);
                return workflowObject;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }
        //When draft is null or empty or false and no id is passed that means we will be creating a workflow and
        //and it will be submitted for approval
        //Add - Submit
        else if ((draft == null || StringUtils.isEmpty(draft) || draft == false) && (id == null || StringUtils.isEmpty(id))) {
            workflowResponse = workflowService.saveWorkflow(inboundEntryResource, WorkflowOperation.SUBMIT, InboundEntityName, userId);

            if (workflowResponse == null) {
                throw new OperationException("Unable to save Workflow");
            }
            try {
                JSONObject workflowObject = new JSONObject(workflowResponse);
                return workflowObject;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }
        //When draft is null or empty or false and id is also passed that means we will be updating an existing workflow for that id passed
        //and it will be submitted for approval
        //Edit - Save
        else if (draft != null && draft == true && id != null && !StringUtils.isEmpty(id)) {
            workflowResponse = workflowService.updateWorkflowDoc(inboundEntryResource, WorkflowOperation.SAVE, id);

            if (workflowResponse == null) {
                throw new OperationException("Unable to update Workflow");
            }
            try {
                JSONObject workflowObject = new JSONObject(workflowResponse);
                return workflowObject;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        //When draft is true and id is also passed that means we will be updating an existing workflow for that id passed
        //and saving it only
        //Edit - Submit
        else if ((draft == null || StringUtils.isEmpty(draft) || draft == false) && id != null && !StringUtils.isEmpty(id)) {
            workflowResponse = workflowService.updateWorkflowDoc(inboundEntryResource, WorkflowOperation.SUBMIT, id);

            if (workflowResponse == null) {
                throw new OperationException("Unable to update Workflow");
            }
            try {
                JSONObject workflowObject = new JSONObject(workflowResponse);
                return workflowObject;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {

            throw new OperationException("Invalid workflow Operation");

        }
    }catch(Exception e)
    {
        throw e;
    }
    }

    @Override
    public Map<String, Object> getWorkflowList(InboundSearchCriteria inboundSearchCriteria) throws OperationException{

        String userId = userService.getLoggedInUserId();

        InboundEntryCriteria inboundEntryCriteria = inboundSearchCriteria.getFilter();

        JSONObject workflowFilterObject = new JSONObject();

        if(inboundEntryCriteria.getId()!=null && !inboundEntryCriteria.getId().isEmpty())
            workflowFilterObject.putOpt("doc.newDoc.id", inboundEntryCriteria.getId());

        if(inboundEntryCriteria.getInboundNo()!=null && !inboundEntryCriteria.getInboundNo().isEmpty())
            workflowFilterObject.putOpt("doc.newDoc.inboundNo", inboundEntryCriteria.getInboundNo());

        if(inboundEntryCriteria.getId()!=null && !inboundEntryCriteria.getId().isEmpty())
            workflowFilterObject.putOpt("doc.newDoc.awbNo", inboundEntryCriteria.getAwbNo());

        if(inboundEntryCriteria.getId()!=null && !inboundEntryCriteria.getId().isEmpty())
            workflowFilterObject.putOpt("doc.newDoc.inboundSenderDetails.senderName", inboundEntryCriteria.getSenderName());

        if(inboundEntryCriteria.getId()!=null && !inboundEntryCriteria.getId().isEmpty())
            workflowFilterObject.putOpt("doc.newDoc.inboundRecipientDetails.empName", inboundEntryCriteria.getRecipientName());

        if(inboundEntryCriteria.getId()!=null && !inboundEntryCriteria.getId().isEmpty())
            workflowFilterObject.putOpt("doc.newDoc.inboundLogEntryStatus.status", inboundEntryCriteria.getStatusId());

        if (inboundEntryCriteria.getReceiptFromDate()!=null) {
            JSONObject workflowfilter = new JSONObject();
            workflowfilter.putOpt("$gte", inboundEntryCriteria.getReceiptFromDate());
            workflowFilterObject.putOpt("doc.newDoc.receivedDate", workflowfilter);
        }
        if (inboundEntryCriteria.getReceiptToDate()!=null) {
            JSONObject workflowfilter = new JSONObject();
            workflowfilter.putOpt("$lte", inboundEntryCriteria.getReceiptToDate());
            workflowFilterObject.putOpt("doc.newDoc.receivedDate", workflowfilter);
        }

        Map<String, Object> workflowList = workflowService.getWorkFlows(InboundEntityName, userId, workflowFilterObject, inboundSearchCriteria.getSort(), inboundSearchCriteria.getPage(), inboundSearchCriteria.getCount());
        return workflowList;
    }

    @Override
    public Object viewInboundRecord(String id, Boolean workflow) throws OperationException {

        String workflowResponse;
        String userId = userService.getLoggedInUserId();

        //If ID passed is Workflow ID
        if(workflow!=null && !StringUtils.isEmpty(workflow) && workflow==true)
        {
            workflowResponse = workflowService.getWorkFlowById(id);
            try {
                 JSONObject workflowObject = new JSONObject(workflowResponse);
                 return workflowObject.getJSONArray("data").getJSONObject(0);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        //If ID passed is Master ID
        else if(workflow==null || StringUtils.isEmpty(workflow) || workflow==false)
        {
            InboundEntry savedAlternateOptionsV2 = getInboundId(id);
            if (savedAlternateOptionsV2 == null) {
                log.error("Inbound Entry Configuration does not exists for id: " + id);
                throw new OperationException(Constants.ER01);
            }

            return savedAlternateOptionsV2;

        }
        else
        {
            throw new OperationException("Invalid workflow Operation");
        }

    }

    @Override
    public Object EditInboundRecord(String id, Boolean workflow,Boolean lock) throws OperationException {

        String workflowResponse;
        String userId = userService.getLoggedInUserId();

        //If ID passed is Workflow ID
        if(workflow!=null && !StringUtils.isEmpty(workflow) && workflow==true)
        {
            try {
                workflowResponse = workflowService.getWorkFlowById(id);

                JSONObject workflowObject = new JSONObject(workflowResponse);

                return workflowObject.getJSONArray("data").getJSONObject(0);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        //If ID passed is Master ID
        else if(workflow==null || StringUtils.isEmpty(workflow) || workflow==false) {
            InboundEntry savedInboundEntry = getInboundId(id);
            if (savedInboundEntry == null) {
                log.error("Inbound Entry Configuration does not exists for id: " + id);
                throw new OperationException(Constants.ER01);
            }

            //Check if the Master Data already has a lock
            if (savedInboundEntry.getLock() == null || Boolean.FALSE.equals(savedInboundEntry.getLock().isEnabled())) {

                if (lock != null && !StringUtils.isEmpty(lock) && lock == true) {
                    RequestLockObject lockobject = new RequestLockObject();
                    lockobject.setEnabled(true);
                    lockobject.setUserId(userId);
                    lockobject.setUser(userId);

                    lockobject.setInboundEntry(savedInboundEntry);

                    savedInboundEntry.setLock(lockobject);

                    workflowResponse = workflowService.saveWorkflow(savedInboundEntry, WorkflowOperation.SAVE, InboundEntityName, userId);

                    if (workflowResponse == null) {
                        throw new OperationException("Unable to save Workflow");
                    }

                    JSONObject workflowObject = new JSONObject(workflowResponse);

                    try {
              /*AlternateOptionsV2 alternateOptions = objectMapper.readValue(
                      (workflowObject.getJSONObject("doc").getJSONObject("newDoc").toString()), AlternateOptionsV2.class);

              alternateOptions.set_id(workflowObject.getString("_id"));*/

                        workflowObject.getJSONObject("doc").getJSONObject("newDoc").getJSONObject("lock").put("workflowId", workflowObject.getString("_id"));
                        lockobject.setWorkflowId(workflowObject.getString("_id"));
                        InboundEntry updatedAlternateOptionsV2 = inboundEntryRepository.saveOrUpdate(savedInboundEntry);

                        workflowService.updateWorkflowDoc(updatedAlternateOptionsV2, WorkflowOperation.SAVE, workflowObject.getString("_id"));

                        updatedAlternateOptionsV2.getLock().setUser(updatedAlternateOptionsV2.getLock().getUserId());

                        return updatedAlternateOptionsV2;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                } else {
                    workflowResponse = workflowService.saveWorkflow(savedInboundEntry, WorkflowOperation.SAVE, InboundEntityName, userId);

                    if (workflowResponse == null) {
                        throw new OperationException("Unable to save Workflow");
                    }

                    JSONObject workflowObject = new JSONObject(workflowResponse);

                    try {
                        InboundEntry inboundEntry = objectMapper.readValue(
                                (workflowObject.getJSONObject("doc").getJSONObject("newDoc").toString()), InboundEntry.class);

                        inboundEntry.getLock().setUser(inboundEntry.getLock().getUserId());
                        return inboundEntry;

                        //return workflowObject;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            } else {
                //Instead of throwing exception, return the booking as it has to be displayed in read-only mode.
                //UI will check the userId of the returned booking and if it is not same as the logged in user
                //then they will display it in read-only mode and prompt the message "Locked by "
                log.info("Locked by user: " + savedInboundEntry.getLock().getUserId());

                return savedInboundEntry;
            }
        }
        else
        {
            throw new OperationException("Invalid workflow Operation");
        }

    }

    @Override
    public InboundEntry releaseLock(String id) throws OperationException{

        String userId = userService.getLoggedInUserId();

        InboundEntry savedInboundEntry = getInboundId(id);

        if (savedInboundEntry.getLock()!=null && Boolean.TRUE.equals(savedInboundEntry.getLock().isEnabled()))
        {
            if(savedInboundEntry.getLock().getUserId().equals(userId))
            {
                String response = workflowService.releaseWorkflow(savedInboundEntry.getLock().getWorkflowId());

                if (response != null)
                {

                    //New Implementation
                    //1.save the changes.
                    //2.delete the lock object from the master

                    int deleted = mailRoomRepository.deleteMasterRecord(savedInboundEntry.getLock().getId().toString());

                    InboundEntry updatedAlternateOptionsV2 = getInboundId(id);

                    System.out.println("Deleted "+deleted+" records");

                    updatedAlternateOptionsV2.setLastModifiedOn(ZonedDateTime.now(ZoneOffset.UTC));
                    updatedAlternateOptionsV2.setLastModifiedByUserId(userService.getLoggedInUserId());

                    updatedAlternateOptionsV2 = inboundEntryRepository.saveOrUpdate(updatedAlternateOptionsV2);

                    return updatedAlternateOptionsV2;

                }
                else{
                    throw new OperationException(Constants.UNABLE_TO_RELEASE_LOCK, id);
                }
            }
            else{
                log.warn("Cannot Release: Locked by another user "+ savedInboundEntry.getLock().getUserId());
                throw new OperationException(Constants.LOCKED_BY_ANOTHER_USER, savedInboundEntry.getLock().getUserId());
            }
        } else {
            log.warn("Alternate Option Configuration with id"+ id +" to be released is not locked");
            throw new OperationException(Constants.NOT_LOCKED, id);
        }
        //return updatedAlternateOptionsV2;
    }

    //--------------------------------------------------------WORKFLOW IMPLEMENTATION OUTBOUND----------------------------------------------------------
    public Object saveOutboundRecord(OutboundDispatchResource outboundDispatchResource, Boolean draft, String id)throws OperationException {

        ExistingOutboundDispatchCriteria existingOutboundDispatchCriteria = new ExistingOutboundDispatchCriteria();
        existingOutboundDispatchCriteria.setReferenceNumber(outboundDispatchResource.getReferenceNumber());
        existingOutboundDispatchCriteria.setLinkInBoudNumber(outboundDispatchResource.getLinkInBoudNumber());
        List<OutboundDispatch> outboundEntryList = outBoundDispatchRepository.getByExistingOutboundCriteria(existingOutboundDispatchCriteria);
        if (outboundEntryList != null && outboundEntryList.size() > 0)
            throw new OperationException(Constants.ER02);

        String workflowResponse;
        String userId = userService.getLoggedInUserId();

        outboundDispatchResource.setAction("Active");
        outboundDispatchResource.setLastModifiedByUserId(userId);
        outboundDispatchResource.setLastModifiedOn(getCurrentTime());
        outboundDispatchResource.setCreatedOn(getCurrentTime());

        //When draft = true and no id is passed that means we will be creating a workflow and only saving it
        //Add - Save
        if(draft!=null && draft == true && (id==null || StringUtils.isEmpty(id)))
        {
            workflowResponse = workflowService.saveWorkflow(outboundDispatchResource, WorkflowOperation.SAVE,OutboundEntityName, userId);

            if(workflowResponse==null){
                throw new OperationException("Unable to save Workflow");
            }
            try {
                JSONObject workflowObject = new JSONObject(workflowResponse);
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
            workflowResponse = workflowService.saveWorkflow(outboundDispatchResource, WorkflowOperation.SUBMIT,OutboundEntityName, userId);

            if(workflowResponse==null){
                throw new OperationException("Unable to save Workflow");
            }
            try {
                JSONObject workflowObject = new JSONObject(workflowResponse);
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
            workflowResponse = workflowService.updateWorkflowDoc(outboundDispatchResource, WorkflowOperation.SAVE, id);

            if(workflowResponse==null){
                throw new OperationException("Unable to update Workflow");
            }
            try {
                JSONObject workflowObject = new JSONObject(workflowResponse);
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
            workflowResponse = workflowService.updateWorkflowDoc(outboundDispatchResource, WorkflowOperation.SUBMIT, id);

            if(workflowResponse==null){
                throw new OperationException("Unable to update Workflow");
            }
            try {
                JSONObject workflowObject = new JSONObject(workflowResponse);
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
    public Map<String, Object> getWorkflowList(OutboundSearchCritieria outboundSearchCritieria) throws OperationException{

        String userId = userService.getLoggedInUserId();

        OutBoundDispatchCriteria outboundDispatchCriteria = outboundSearchCritieria.getFilter();

        JSONObject workflowFilterObject = new JSONObject();

        if(outboundDispatchCriteria.getId()!=null && !outboundDispatchCriteria.getId().isEmpty())
            workflowFilterObject.putOpt("doc.newDoc.id", outboundDispatchCriteria.getId());

//        if(outboundDispatchCriteria.getParcelId()!=null && !outboundDispatchCriteria.getParcelId().isEmpty()) // Work this out later
//            workflowFilterObject.putOpt("doc.newDoc.parcelId", outboundDispatchCriteria.getParcelId());

        if(outboundDispatchCriteria.getPassportNumber()!=null && !outboundDispatchCriteria.getPassportNumber().isEmpty())
            workflowFilterObject.putOpt("doc.newDoc.passportNumber", outboundDispatchCriteria.getPassportNumber());

        if(outboundDispatchCriteria.getEmployeeName()!=null && !outboundDispatchCriteria.getEmployeeName().isEmpty())
            workflowFilterObject.putOpt("doc.newDoc.dispatchSenderDetails.employeeName", outboundDispatchCriteria.getEmployeeName());

        if(outboundDispatchCriteria.getEmployeeId()!=null && !outboundDispatchCriteria.getEmployeeId().isEmpty())
            workflowFilterObject.putOpt("doc.newDoc.dispatchSenderDetails.employeeId", outboundDispatchCriteria.getEmployeeId());

        if(outboundDispatchCriteria.getPassengerName()!=null && !outboundDispatchCriteria.getPassengerName().isEmpty())
            workflowFilterObject.putOpt("doc.newDoc.leadPassengerName", outboundDispatchCriteria.getPassengerName());

        if(outboundDispatchCriteria.getSupplierName()!=null && !outboundDispatchCriteria.getSupplierName().isEmpty())
            workflowFilterObject.putOpt("doc.newDoc.dispatchCostDetails.supplierName", outboundDispatchCriteria.getSupplierName());

        if(outboundDispatchCriteria.getDispatchStatus()!=null && !outboundDispatchCriteria.getDispatchStatus().name().isEmpty())
            workflowFilterObject.putOpt("doc.newDoc.dispatchStatus.status", outboundDispatchCriteria.getDispatchStatus());

        Map<String, Object> workflowList = workflowService.getWorkFlows(OutboundEntityName, userId, workflowFilterObject, outboundSearchCritieria.getSort(), outboundSearchCritieria.getPage(), outboundSearchCritieria.getCount());
        return workflowList;
    }

    @Override
    public Object viewOutboundRecord(String id, Boolean workflow) throws OperationException {

        String workflowResponse;
        String userId = userService.getLoggedInUserId();

        //If ID passed is Workflow ID
        if(workflow!=null && !StringUtils.isEmpty(workflow) && workflow==true)
        {
            workflowResponse = workflowService.getWorkFlowById(id);
            try {
                JSONObject workflowObject = new JSONObject(workflowResponse);
                return workflowObject.getJSONArray("data").getJSONObject(0);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        //If ID passed is Master ID
        else if(workflow==null || StringUtils.isEmpty(workflow) || workflow==false)
        {
            OutboundDispatch outboundDispatch = getOutBoundId(id);
            if (outboundDispatch == null) {
                log.error("Inbound Entry Configuration does not exists for id: " + id);
                throw new OperationException(Constants.ER01);
            }
            return outboundDispatch;
        }
        else
        {
            throw new OperationException("Invalid workflow Operation");
        }
    }

    @Override
    public Object EditOutboundRecord(String id, Boolean workflow,Boolean lock) throws OperationException {

        String workflowResponse;
        String userId = userService.getLoggedInUserId();

        //If ID passed is Workflow ID
        if(workflow!=null && !StringUtils.isEmpty(workflow) && workflow==true)
        {
            try {
                workflowResponse = workflowService.getWorkFlowById(id);

                JSONObject workflowObject = new JSONObject(workflowResponse);

                return workflowObject.getJSONArray("data").getJSONObject(0);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        //If ID passed is Master ID
        else if(workflow==null || StringUtils.isEmpty(workflow) || workflow==false) {
            OutboundDispatch savedOutboundEntry = getOutBoundId(id);
            if (savedOutboundEntry == null) {
                log.error("Inbound Entry Configuration does not exists for id: " + id);
                throw new OperationException(Constants.ER01);
            }

            //Check if the Master Data already has a lock
            if (savedOutboundEntry.getLock() == null || Boolean.FALSE.equals(savedOutboundEntry.getLock().isEnabled())) {

                if (lock != null && !StringUtils.isEmpty(lock) && lock == true) {
                    RequestLockObject lockobject = new RequestLockObject();
                    lockobject.setEnabled(true);
                    lockobject.setUserId(userId);
                    lockobject.setUser(userId);

                    lockobject.setOutboundDispatch(savedOutboundEntry);

                    savedOutboundEntry.setLock(lockobject);

                    workflowResponse = workflowService.saveWorkflow(savedOutboundEntry, WorkflowOperation.SAVE, OutboundEntityName, userId);

                    if (workflowResponse == null) {
                        throw new OperationException("Unable to save Workflow");
                    }

                    JSONObject workflowObject = new JSONObject(workflowResponse);

                    try {
              /*AlternateOptionsV2 alternateOptions = objectMapper.readValue(
                      (workflowObject.getJSONObject("doc").getJSONObject("newDoc").toString()), AlternateOptionsV2.class);

              alternateOptions.set_id(workflowObject.getString("_id"));*/

                        workflowObject.getJSONObject("doc").getJSONObject("newDoc").getJSONObject("lock").put("workflowId", workflowObject.getString("_id"));
                        lockobject.setWorkflowId(workflowObject.getString("_id"));
                        OutboundDispatch updatedAlternateOptionsV2 = outBoundDispatchRepository.saveOrUpdate(savedOutboundEntry);

                        workflowService.updateWorkflowDoc(updatedAlternateOptionsV2, WorkflowOperation.SAVE, workflowObject.getString("_id"));

                        updatedAlternateOptionsV2.getLock().setUser(updatedAlternateOptionsV2.getLock().getUserId());

                        return updatedAlternateOptionsV2;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                } else {
                    workflowResponse = workflowService.saveWorkflow(savedOutboundEntry, WorkflowOperation.SAVE, OutboundEntityName, userId);

                    if (workflowResponse == null) {
                        throw new OperationException("Unable to save Workflow");
                    }

                    JSONObject workflowObject = new JSONObject(workflowResponse);

                    try {
                        OutboundDispatch outboundDispatch = objectMapper.readValue(
                                (workflowObject.getJSONObject("doc").getJSONObject("newDoc").toString()), OutboundDispatch.class);

                        outboundDispatch.getLock().setUser(outboundDispatch.getLock().getUserId());
                        return outboundDispatch;

                        //return workflowObject;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            } else {
                //Instead of throwing exception, return the booking as it has to be displayed in read-only mode.
                //UI will check the userId of the returned booking and if it is not same as the logged in user
                //then they will display it in read-only mode and prompt the message "Locked by "
                log.info("Locked by user: " + savedOutboundEntry.getLock().getUserId());

                return savedOutboundEntry;
            }
        }
        else
        {
            throw new OperationException("Invalid workflow Operation");
        }
    }

    @Override
    public OutboundDispatch releaseOutboundLock(String id) throws OperationException{

        String userId = userService.getLoggedInUserId();

        OutboundDispatch savedOutboundDispatch = getOutBoundId(id);

        if (savedOutboundDispatch.getLock()!=null && Boolean.TRUE.equals(savedOutboundDispatch.getLock().isEnabled()))
        {
            if(savedOutboundDispatch.getLock().getUserId().equals(userId))
            {
                String response = workflowService.releaseWorkflow(savedOutboundDispatch.getLock().getWorkflowId());

                if (response != null)
                {

                    //New Implementation
                    //1.save the changes.
                    //2.delete the lock object from the master

                    int deleted = mailRoomRepository.deleteMasterRecord(savedOutboundDispatch.getLock().getId().toString());

                    OutboundDispatch updatedOutboundDispatch = getOutBoundId(id);

                    System.out.println("Deleted "+deleted+" records");

                    updatedOutboundDispatch.setLastModifiedOn(ZonedDateTime.now(ZoneOffset.UTC));
                    updatedOutboundDispatch.setLastModifiedByUserId(userService.getLoggedInUserId());

                    updatedOutboundDispatch = outBoundDispatchRepository.saveOrUpdate(updatedOutboundDispatch);

                    return updatedOutboundDispatch;

                }
                else{
                    throw new OperationException(Constants.UNABLE_TO_RELEASE_LOCK, id);
                }
            }
            else{
                log.warn("Cannot Release: Locked by another user "+ savedOutboundDispatch.getLock().getUserId());
                throw new OperationException(Constants.LOCKED_BY_ANOTHER_USER, savedOutboundDispatch.getLock().getUserId());
            }
        } else {
            log.warn("Alternate Option Configuration with id"+ id +" to be released is not locked");
            throw new OperationException(Constants.NOT_LOCKED, id);
        }
    }


    @Override
    public boolean isResponsibleFor(String type) throws OperationException {
        return type.equalsIgnoreCase(MasterEntityName) ||
                type.equalsIgnoreCase(InboundEntityName) ||
                type.equalsIgnoreCase(OutboundEntityName);
    }
}
