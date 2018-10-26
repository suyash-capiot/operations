package com.coxandkings.travel.operations.service.forex.impl;

import com.coxandkings.travel.operations.criteria.forex.ForexCriteria;
import com.coxandkings.travel.operations.enums.forex.IndentStatus;
import com.coxandkings.travel.operations.enums.forex.IndentType;
import com.coxandkings.travel.operations.enums.forex.RequestStatus;
import com.coxandkings.travel.operations.enums.todo.*;
import com.coxandkings.travel.operations.enums.workflow.WorkflowOperation;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.model.forex.*;
import com.coxandkings.travel.operations.model.productbookedthrother.mdm.B2BClient;
import com.coxandkings.travel.operations.model.productbookedthrother.mdm.B2CClient;
import com.coxandkings.travel.operations.model.todo.ToDoTask;
import com.coxandkings.travel.operations.repository.forex.ForexBookingRepository;
import com.coxandkings.travel.operations.repository.forex.ForexDisbursementRepository;
import com.coxandkings.travel.operations.repository.forex.ForexIndentRepository;
import com.coxandkings.travel.operations.resource.forex.*;
import com.coxandkings.travel.operations.resource.managedocumentation.*;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResource;
import com.coxandkings.travel.operations.resource.user.MdmUserInfo;
import com.coxandkings.travel.operations.resource.user.OpsUser;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.forex.ForexBookingService;
import com.coxandkings.travel.operations.service.forex.ForexIndentService;
import com.coxandkings.travel.operations.service.mdmservice.ClientMasterDataService;
import com.coxandkings.travel.operations.service.productbookedthrother.mdm.MdmClientService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.service.workflow.WorkflowService;
import com.coxandkings.travel.operations.systemlogin.MDMDataSource;
import com.coxandkings.travel.operations.systemlogin.MDMToken;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.CopyUtils;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class ForexBookingServiceImpl implements ForexBookingService {

    private static ObjectMapper objectMapper = new ObjectMapper();
    static String CCYCODE_IND = "INR";
    private Logger logger = Logger.getLogger(ForexBookingService.class);
    @Value(value = "${manage_documentation.be.get_document_details}")
    private String getBookingDocsUrl;
    @Value(value = "${daily_roe}")
    private String roeUrl;
    @Autowired
    private ClientMasterDataService clientMasterDataService;
    @Autowired
    private ForexBookingRepository forexBookingRepository;
    @Autowired
    private ForexIndentRepository forexIndentRepository;
    @Autowired
    private ForexDisbursementRepository forexDisbursementRepository;
    private ToDoTask toDoTask;
    @Autowired
    private OpsBookingService opsBookingService;
    @Autowired
    private ToDoTaskService toDoTaskService;
    @Autowired
    private MdmClientService mdmClientService;
    @Autowired
    private ForexIndentService forexIndentService;
    @Autowired
    @Qualifier(value = "mDMDataSource")
    private MDMDataSource mdmDataSource;
    @Autowired
    private UserService userService;
    @Autowired
    private WorkflowService workflowService;

    private static String entityName = "Forex-Request";

    @Autowired
    @Qualifier(value = "mDMToken")
    private MDMToken mdmToken;

    @Override
    public Map<String, Object> getByCriteria(ForexCriteria forexCriteria) throws OperationException {

        try {
            return forexBookingRepository.getForexBookByCriteria(forexCriteria);
        } catch (Exception e) {
            logger.error("Error occurred while loading Forex Booking details");
            throw new OperationException(Constants.UNABLE_TO_FETCH_DETAILS);
        }
    }

    @Override
    public ForexBooking saveOrUpdate(ForexBookingResource resource) throws OperationException {

        String id = resource.getId();
        ForexBooking forexBooking;
        if (!StringUtils.isEmpty(id)) {

            ForexBooking existingForexBooking = getForexByID(id);
            if (existingForexBooking == null) {
                logger.error("Booking not exists for id: " + id);
                throw new OperationException(Constants.ER01);
            }
            try {
                this.updateExistingBooking(existingForexBooking, resource);
                //TODO : To decide whether Personal Indent should be created each time a new request is approved or
                //TODO: make changes to existing indent.
                this.createPersonalExpenseIndent(existingForexBooking);
                forexBooking = existingForexBooking;
                forexBooking = forexBookingRepository.saveOrUpdate(forexBooking);
            } catch (Exception e) {
                logger.error("Exception occured while trying to save ForexBooking for id:" + id, e);
                throw new OperationException(Constants.UNABLE_TO_SAVE);
            }
        } else {
            logger.error("Id is null or Empty");
            throw new OperationException(Constants.ID_NULL_EMPTY);
        }
        return forexBooking;
    }

    private void createPersonalExpenseIndent(ForexBooking existingForexBooking) {

        Set<ForexIndent> indentSet = existingForexBooking.getIndentSet();

        ForexIndent personalIndent = null;
        for (ForexIndent indent : indentSet) {
            if (indent.getIndentFor().equals(IndentType.PERSONALEXPENSE))
                personalIndent = indent;
        }

        if (personalIndent!=null) {
            personalIndent.setLastModifiedAtTime(ZonedDateTime.now(ZoneOffset.UTC));
            personalIndent.setLastModifiedByUserId(userService.getLoggedInUserId());
        }else {
            personalIndent = new ForexIndent();
            personalIndent.setIndentFor(IndentType.PERSONALEXPENSE);
            personalIndent.setIndentStatus(IndentStatus.PENDING);
            personalIndent.setCreatedAtTime(ZonedDateTime.now(ZoneOffset.UTC));
            personalIndent.setLastModifiedAtTime(ZonedDateTime.now(ZoneOffset.UTC));
            personalIndent.setForexBooking(existingForexBooking);

            personalIndent.setSentToSupplier(Boolean.FALSE);
            personalIndent.setCreatedByUserId(userService.getLoggedInUserId());
            personalIndent.setLastModifiedByUserId(userService.getLoggedInUserId());
            //Reference to ForexRequest
            indentSet.add(personalIndent);
        }
        existingForexBooking.setIndentSet(indentSet);
    }

    private void createTourCostIndent(ForexBooking existingForexBooking) {

        Set<ForexIndent> indentSet = existingForexBooking.getIndentSet();
        Boolean isAlreadyRaised = false;

        if (indentSet != null) {
            for (ForexIndent indent : indentSet) {
                if (indent.getIndentFor().equals(IndentType.TOURCOST))
                    isAlreadyRaised = true;
            }
        }

        if (!isAlreadyRaised) {
            ForexIndent indent = new ForexIndent();
            indent.setIndentFor(IndentType.TOURCOST);
            indent.setIndentStatus(IndentStatus.PENDING);
            indent.setCreatedAtTime(ZonedDateTime.now(ZoneOffset.UTC));
            indent.setLastModifiedAtTime(ZonedDateTime.now(ZoneOffset.UTC));
            indent.setForexBooking(existingForexBooking);

            indent.setSentToSupplier(Boolean.FALSE);
            indent.setCreatedByUserId(userService.getLoggedInUserId());
            indent.setLastModifiedByUserId(userService.getLoggedInUserId());
            //Reference to ForexRequest
            indentSet.add(indent);
            existingForexBooking.setIndentSet(indentSet);
        }
    }

    private void updateExistingBooking(ForexBooking forexBooking, ForexBookingResource resource) {

        forexBooking.setLastModifiedAtTime(ZonedDateTime.now(ZoneOffset.UTC));
        forexBooking.setLastModifiedByUserId(userService.getLoggedInUserId());
        forexBooking.setDocumentIds(resource.getDocumentIds());
        forexBooking.setRequestStatus(RequestStatus.CONFIRMED);

        RequestLockObject lockObject = forexBooking.getLock();
        //Release Lock
        if(lockObject!=null) {
            lockObject.setEnabled(false);
            forexBooking.setLock(lockObject);
        }

        Set<ForexPassenger> forexPassengerSet = forexBooking.getForexPassengerSet();
        Map<String, ForexPassengerResource> paxMap = new HashMap<>();

        for (ForexPassengerResource forexPassengerResource : resource.getForexPassengerSet()) {
            paxMap.put(forexPassengerResource.getId(), forexPassengerResource);
        }

        for (ForexPassenger existingForexPassenger : forexPassengerSet) {

            ForexPassengerResource passengerResource = paxMap.get(existingForexPassenger.getId());
            existingForexPassenger.setLastModifiedByUserId(resource.getLastModifiedByUserId());

            if (passengerResource.getPassportDetails() != null) {
                PassportDetails passportDetails = existingForexPassenger.getPassportDetails();
                passportDetails = passportDetails != null ? passportDetails : new PassportDetails();
                CopyUtils.copy(passengerResource.getPassportDetails(), passportDetails);
                existingForexPassenger.setPassportDetails(passportDetails);
            }

            if (passengerResource.getPersonalExpenseDetails() != null) {
                PersonalExpenseDetails personalExpenseDetails = existingForexPassenger.getPersonalExpenseDetails();
                personalExpenseDetails = personalExpenseDetails != null ? personalExpenseDetails : new PersonalExpenseDetails();
                CopyUtils.copy(passengerResource.getPersonalExpenseDetails(), personalExpenseDetails);
                existingForexPassenger.setPersonalExpenseDetails(personalExpenseDetails);
            }
        }
        forexBooking.setForexPassengerSet(forexPassengerSet);
    }

    private void createIndentsAndAssociate(ForexBooking forexBooking, OpsUser aOpsUser) {

        Boolean isPersonalIndentCreated = false, isTourIndentCreated = false;
        Set<ForexIndent> indentSet = forexBooking.getIndentSet();
        //If indent is not already created then create and associate, else do nothing
        indentSet = indentSet == null ? new HashSet<>() : indentSet;

        for (ForexIndent indent : indentSet) {
            if (indent.getIndentFor().equals(IndentType.PERSONALEXPENSE))
                isPersonalIndentCreated = true;
            if (indent.getIndentFor().equals(IndentType.TOURCOST))
                isTourIndentCreated = true;
        }

        if (!isTourIndentCreated) {
            ForexIndent indent = new ForexIndent();
            indent.setIndentFor(IndentType.TOURCOST);
            indent.setIndentStatus(IndentStatus.PENDING);
            indent.setCreatedAtTime(ZonedDateTime.now(ZoneOffset.UTC));
            indent.setLastModifiedAtTime(ZonedDateTime.now(ZoneOffset.UTC));
            indent.setCreatedByUserId(aOpsUser.getUserID());
            indent.setLastModifiedByUserId(aOpsUser.getUserID());
            //Reference to ForexRequest
            indent.setForexBooking(forexBooking);
            indentSet.add(indent);
        }

//        Boolean isPersonalExpenseRequired = false;
        /* TODO : To Check how we will get Personal Expense Details from BookingEngine
           TODO : and if even a single passenger opts for personalExpense then raise PersonalExpense Indent for the booking.
           TODO : and set the boolean isPersonalExpenseRequired.
        */
        if (!isPersonalIndentCreated) {
            ForexIndent indent = new ForexIndent();
            indent.setIndentFor(IndentType.PERSONALEXPENSE);
            indent.setIndentStatus(IndentStatus.PENDING);
            indent.setCreatedAtTime(ZonedDateTime.now(ZoneOffset.UTC));
            indent.setLastModifiedAtTime(ZonedDateTime.now(ZoneOffset.UTC));
            indent.setCreatedByUserId(aOpsUser.getUserID());
            indent.setLastModifiedByUserId(aOpsUser.getUserID());
            //Reference to ForexRequest
            indent.setForexBooking(forexBooking);
            indentSet.add(indent);
        }
        forexBooking.setIndentSet(indentSet);
    }

    //Create a New Forex Request
    @Transactional
    public void process(OpsBooking opsBooking, OpsProduct opsProduct, List<OpsHolidaysPaxInfo> forexPassengerList) throws OperationException {

        ForexBooking record = forexBookingRepository.getRecordByBookId(opsBooking.getBookID());
        if (record != null) {
           logger.error("Forex record for BookId " + opsBooking.getBookID() + " already exists");
           return;
        }
        try {
            ForexBooking forexBooking = populateForexBooking(opsProduct, opsBooking, forexPassengerList);
            forexBookingRepository.saveOrUpdate(forexBooking);
        } catch (Exception e) {
            logger.error(String.format("Exception occured while processing forex request for bookID:%s", opsBooking.getBookID()), e);
            throw new OperationException(Constants.PROCESS_REQUEST_FAILURE);
        }
    }

    private ForexBooking populateForexBooking(OpsProduct opsProduct, OpsBooking opsBooking, List<OpsHolidaysPaxInfo> forexPassengerList)
            throws OperationException {

        ForexBooking forexBooking = new ForexBooking();
        forexBooking.setRequestStatus(RequestStatus.PENDING);
        String clientName = "";
        MdmUserInfo mdmUser;
        if (opsBooking.getClientType().equalsIgnoreCase("B2B")) {
            clientName = clientMasterDataService.getB2BClientNames(Arrays.asList(opsBooking.getClientID())).get(0);
        } else if (opsBooking.getClientType().equalsIgnoreCase("B2C")) {
            clientName = clientMasterDataService.getB2CClientNames(Arrays.asList(opsBooking.getClientID())).get(0);
        }

        OpsOrderDetails opsOrderDetails = opsProduct.getOrderDetails();
        OpsHolidaysDetails opsHolidaysDetails = opsOrderDetails.getPackageDetails();

        forexBooking.setBookRefNo(opsBooking.getBookID());
        forexBooking.setClientName(clientName);

        forexBooking.setBookingDate(opsBooking.getBookingDateZDT());

        forexBooking.setCreatedAtTime(ZonedDateTime.now(ZoneOffset.UTC));
        forexBooking.setLastModifiedAtTime(ZonedDateTime.now(ZoneOffset.UTC));

        //TODO: To check if this initialization of travel country correct?
        forexBooking.setTravelCountry(opsHolidaysDetails.getTourDetails().getDestination());
        forexBooking.setTravelStartDate(opsHolidaysDetails.getTravelStartDate());
        forexBooking.setTravelEndDate(opsHolidaysDetails.getTravelEndDate());
        //Auto generation-logic for RequestId and EnquiryId
        forexBooking.setRequestId("RQ-" + gen());
        forexBooking.setEnquiryId(forexBooking.getRequestId());

        //TODO: Set the document Id (BTQ Form) which is already present in the Manage Documentation,
        //TODO: Uploaded through WEM ,as discussed with Ashish.

        mdmUser = userService.createUserDetailsFromToken(mdmToken.getToken());

        OpsUser aOpsUser = userService.getOpsUser(mdmUser);
        forexBooking.setCreatedByUserId(aOpsUser.getUserID());
        forexBooking.setLastModifiedByUserId(aOpsUser.getUserID());

        Set<ForexPassenger> forexPassengerDtlsSet = populatePassengerDetailsSet(forexBooking, opsProduct, opsBooking, forexPassengerList);
        forexBooking.setForexPassengerSet(forexPassengerDtlsSet);

        return forexBooking;
    }

    private Set<ForexPassenger> populatePassengerDetailsSet(ForexBooking forexBooking, OpsProduct opsProduct,
                                                            OpsBooking opsBooking, List<OpsHolidaysPaxInfo> forexPassengerList) throws OperationException {

        MdmUserInfo mdmUser;
        OpsHolidaysDetails opsHolidaysDetails = opsProduct.getOrderDetails().getPackageDetails();
        String result = RestUtils.getForObject(getBookingDocsUrl + opsBooking.getBookID(), String.class);
        DocumentDetailsResponse documentDetailsResponse = null;
        try {
            documentDetailsResponse = objectMapper.readValue(result, DocumentDetailsResponse.class);
        } catch (Exception ex) {
            logger.info("No document details found for booking");
        }
        List<PaxDocuments> paxDocuments = documentDetailsResponse.getPaxDocument();

        Set<ForexPassenger> forexPassengerDtlsSet = new HashSet<>();
        for (OpsHolidaysPaxInfo opsHolidaysPaxInfo : forexPassengerList) {
            ForexPassenger forexPassenger = new ForexPassenger();
            forexPassenger.setDob(opsHolidaysPaxInfo.getBirthDate());
            forexPassenger.setLeadPassenger(opsHolidaysPaxInfo.getIsLeadPax());
            forexPassenger.setSalutation(opsHolidaysPaxInfo.getTitle());
            forexPassenger.setPassengerType(opsHolidaysPaxInfo.getPaxType());
            forexPassenger.setForexRequired(opsHolidaysPaxInfo.getForexRequired());

            mdmUser = userService.createUserDetailsFromToken(mdmToken.getToken());

            OpsUser aOpsUser = userService.getOpsUser(mdmUser);
            forexPassenger.setCreatedByUserId(aOpsUser.getUserID());
            forexPassenger.setLastModifiedByUserId(aOpsUser.getUserID());

            forexPassenger.setForexStatus("Pending");
            forexPassenger.setForex(forexBooking);

            forexPassenger.setFirstName(opsHolidaysPaxInfo.getFirstName());
            forexPassenger.setLastName(opsHolidaysPaxInfo.getLastName());
            forexPassenger.setMiddleName(opsHolidaysPaxInfo.getMiddleName());
            forexPassenger.setAddressDetails(opsHolidaysPaxInfo.getAddressDetails());

            Set<TourCostDetails> tourCostDetails = populateTourCostDetails(forexPassenger, opsHolidaysPaxInfo, opsBooking);
            forexPassenger.setTourCostDetails(tourCostDetails);

            /*
            TODO: To set departure Details from opsBooking.
            TODO: To Check how we will get airline Number, ticket number etc from Holidays booking.
            Currently, BE is not providing flight details in Holidays getBooking.
            */
            PaxDocInfo forexPaxDoc = null;
            for (PaxDocuments paxDocument : paxDocuments) {
                //Fetching the documents of the current passenger.
                if (opsHolidaysPaxInfo.getPaxID().equals(paxDocument.getPaxID())) {
                    forexPaxDoc = paxDocument.getDocumentInfo();
                }
            }

            if (forexPaxDoc != null) {
                PassportDetails passportDetails = populatePassportDetails(forexPaxDoc.getDocumentInfo());
                forexPassenger.setPassportDetails(passportDetails);
            }
            forexPassengerDtlsSet.add(forexPassenger);
        }
        return forexPassengerDtlsSet;
    }

    public ForexBooking updateRecord(String uid, ForexBookingResource resource,
                         String workflow) throws OperationException{

        //In case of save and submit UID will be Workflow ID, else forex dB Id
        ForexBooking forexBooking;
        String workflowResponse;
        String userId = userService.getLoggedInUserId();
        if(workflow == null) {
            ForexBooking existingForexBooking;
            //To Identify which stage is approved Request or indent as Only one workflow is getting created.
            if (resource.getStage().equalsIgnoreCase("Request")) {
                //MDM will hit this service whenever the request is approved and lock object is cleared.
                //If request is approved then create Tour Cost Indent and Personal Indent.
                //Personal Indent should be created each time a new request is approved.
                //TourCost Indent once created will work, as no change happens in TourCost.
                forexBooking = saveOrUpdate(resource);
                return forexBooking;
            } else {
                //MDM will hit this service whenever the indents are approved and lock object is cleared.
                existingForexBooking = getForexByID(resource.getId());
                existingForexBooking.setLastModifiedAtTime(ZonedDateTime.now(ZoneOffset.UTC));
                existingForexBooking.setLastModifiedByUserId(userService.getLoggedInUserId());
                existingForexBooking.setDocumentIds(resource.getDocumentIds());
                Set<ForexIndentResource> indentSet = resource.getIndentSet();
                Set<ForexIndent> indentSets = existingForexBooking.getIndentSet();
                for (ForexIndentResource indentResource : indentSet) {
                    ForexIndent indent = indentSets.stream().filter(s -> s.getId().equals(indentResource.getId())).findFirst().get();
                    forexIndentService.updateExistingForexIndent(indent, indentResource);
                }
                existingForexBooking.setIndentSet(indentSets);
                RequestLockObject lockObject = existingForexBooking.getLock();
                if(lockObject!=null) {
                    lockObject.setEnabled(resource.getLock().isEnabled());
                    lockObject.setUserId(resource.getLock().getUserId());
                    existingForexBooking.setLock(lockObject);
                }
                forexBooking = forexBookingRepository.saveOrUpdate(existingForexBooking);
                return forexBooking;
            }
        }
        switch (workflow){

            case "SAVE" :
                if(resource.getStage().equalsIgnoreCase("Request"))
                    resource.setRequestStatus(RequestStatus.PENDING.toString());
                else if(resource.getStage().equalsIgnoreCase("Indent"))
                    resource.getIndentSet().stream().forEach(indent -> indent.setIndentStatus(IndentStatus.PENDING));
                else
                    throw new OperationException("Stage not Specified - Either request or Indent");

                workflowResponse = workflowService.updateWorkflowDoc(resource, WorkflowOperation.SAVE, uid);
                if(workflowResponse==null){
                    throw new OperationException("Unable to save Workflow");
                }
                JSONObject workflowObject = new JSONObject(workflowResponse);
                try {
                    forexBooking = objectMapper.readValue(
                            (workflowObject.getJSONObject("doc").getJSONObject("newDoc").toString()),
                            ForexBooking.class);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                return forexBooking;
            case "SUBMIT":
                ForexBooking existingForexBooking = getForexByID(resource.getId());
                if (existingForexBooking == null) {
                    logger.error("Booking not exists for id: " + uid);
                    throw new OperationException(Constants.ER01);
                }
                if(resource.getStage().equalsIgnoreCase("Request")){

                    this.createTourCostIndent(existingForexBooking);
                    forexBooking = forexBookingRepository.saveOrUpdate(existingForexBooking);
                    //Check if Personal Expense is present. If it does not then confirm it directly without supervisor approval.
                    Boolean isPersonalRequired = false;
                    for (ForexPassengerResource forexPassengerResource : resource.getForexPassengerSet()){
                        if(forexPassengerResource.getPersonalExpenseDetails()!=null) {
                            isPersonalRequired = true;
                        }
                    }

                    if(isPersonalRequired) {
                        resource.setRequestStatus(RequestStatus.PENDING.toString());
                        workflowResponse = workflowService.updateWorkflowDoc(resource, WorkflowOperation.SUBMIT, uid);
                        workflowObject = new JSONObject(workflowResponse);
                        try {
                            forexBooking = objectMapper.readValue(
                                    (workflowObject.getJSONObject("doc").getJSONObject("newDoc").toString()), ForexBooking.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else{
                        //Confirm Request and release lock
                        existingForexBooking.setRequestStatus(RequestStatus.CONFIRMED);
                        for(ForexIndent indent : existingForexBooking.getIndentSet()){
                            if(indent.getIndentFor().equals(IndentType.PERSONALEXPENSE)) {
                                 //TODO: Remove this indent as personal Expense no longer required.
                            }
                        }
                        RequestLockObject lockobject = existingForexBooking.getLock();
                        if(lockobject!=null) {
                            lockobject.setEnabled(false);
                            lockobject.setUserId(userId);
                            lockobject.setWorkflowId(null);
                            existingForexBooking.setLock(lockobject);
                        }
                        if(workflowService.deleteWorkflow(uid)!=null) {
                            forexBooking = forexBookingRepository.saveOrUpdate(existingForexBooking);
                        }else{
                            throw new OperationException("Unable to release Lock WorkFlow : " + uid + "for Request Id" + existingForexBooking.getRequestId());
                        }
                    }
                    forexBooking.setStage(resource.getStage());
                    return forexBooking;
                }else if(resource.getStage().equalsIgnoreCase("Indent")){
                    try {
                        for(ForexIndentResource indent : resource.getIndentSet()){
                            indent.setIndentStatus(IndentStatus.PENDING);
                        }
                        workflowResponse = workflowService.updateWorkflowDoc(resource, WorkflowOperation.SUBMIT, uid);
                        workflowObject = new JSONObject(workflowResponse);
                        forexBooking = objectMapper.readValue(
                                (workflowObject.getJSONObject("doc").getJSONObject("newDoc").toString()), ForexBooking.class);
                        forexBooking.setStage(resource.getStage());
                        return forexBooking;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }else{
                    throw new OperationException("Stage not Specified - Either request or Indent");
                }
            case "MASTERUPDATE" :
                ForexBooking lockedObject = getEditLock(uid, userId);
                return lockedObject;

            default:
                throw new OperationException("Invalid workflow Operation");
            }

    }

    @Override
    public Map<String, Object> getWorkflowList(ForexCriteria forexCriteria) throws OperationException{

        String userId = userService.getLoggedInUserId();

        JSONArray andObject = new JSONArray();
        String sortCriteria = forexCriteria.getSortCriteria();
        if (!StringUtils.isEmpty(sortCriteria)) {
            if(sortCriteria.equalsIgnoreCase("bookingDate")){
                sortCriteria = forexCriteria.getDescending() ? sortCriteria : "-".concat(sortCriteria);
            }
        }

        JSONObject workflowFilterObject = new JSONObject();
        if (!StringUtils.isEmpty(forexCriteria.getReqOrIndentId())) {
            JSONArray idsFilter = new JSONArray();
            idsFilter.put(new JSONObject().put("doc.newDoc.requestId", forexCriteria.getReqOrIndentId()));
            idsFilter.put(new JSONObject().put("doc.newDoc.indentSet.id", forexCriteria.getReqOrIndentId()));
            andObject.put(new JSONObject().putOpt("$or", idsFilter));
        }
        if (!StringUtils.isEmpty(forexCriteria.getClientName())) {
            workflowFilterObject.putOpt("doc.newDoc.clientName", forexCriteria.getClientName());
        }
        if (!StringUtils.isEmpty(forexCriteria.getBookRefNo())) {
            workflowFilterObject.putOpt("doc.newDoc.bookRefNo", forexCriteria.getBookRefNo());
        }
        if (!StringUtils.isEmpty(forexCriteria.getFromDate())) {
            JSONObject workflowfilter = new JSONObject();
            workflowfilter.putOpt("$gte", forexCriteria.getFromDate());
            workflowFilterObject.putOpt("doc.newDoc.createdAtTime", workflowfilter);
        }
        if (!StringUtils.isEmpty(forexCriteria.getToDate())) {
            JSONObject workflowfilter = new JSONObject();
            workflowfilter.putOpt("$lte", forexCriteria.getToDate());
            workflowFilterObject.putOpt("doc.newDoc.createdAtTime", workflowfilter);
        }
        if (!StringUtils.isEmpty(forexCriteria.getTravelCountry())) {
            workflowFilterObject.putOpt("doc.newDoc.travelCountry", forexCriteria.getTravelCountry());
        }
        if (!StringUtils.isEmpty(forexCriteria.getSupplierName())) {
            workflowFilterObject.putOpt("doc.newDoc.indentSet.supplierName", forexCriteria.getSupplierName());
        }
        if (!StringUtils.isEmpty(forexCriteria.getCurrency())) {
            JSONArray currenciesFilter = new JSONArray();
            currenciesFilter.put(new JSONObject().put("doc.newDoc.forexPassengerSet.tourCostDetails.currency", forexCriteria.getCurrency()));
            currenciesFilter.put(new JSONObject().put("doc.newDoc.forexPassengerSet.personalExpenseDetails.currency", forexCriteria.getCurrency()));
            andObject.put(new JSONObject().putOpt("$or", currenciesFilter));
        }
        PassengerNameResource passengerNameResource = forexCriteria.getPassengerName();
        if(passengerNameResource!=null){
            JSONArray namesFilter = new JSONArray();
            if (!StringUtils.isEmpty(passengerNameResource.getFirstName())) {
                namesFilter.put(new JSONObject().put("doc.newDoc.forexPassengerSet.firstName", passengerNameResource.getFirstName()));
            }
            if (!StringUtils.isEmpty(passengerNameResource.getMiddleName())) {
                namesFilter.put(new JSONObject().put("doc.newDoc.forexPassengerSet.middleName", passengerNameResource.getMiddleName()));
            }
            if (!StringUtils.isEmpty(passengerNameResource.getLastName())) {
                namesFilter.put(new JSONObject().put("doc.newDoc.forexPassengerSet.lastName", passengerNameResource.getLastName()));
            }
            if(namesFilter.length()!=0)
                andObject.put(new JSONObject().putOpt("$and", namesFilter));
        }
        if(andObject!=null && andObject.length()!=0)
             workflowFilterObject.putOpt("$and", andObject);

        Map<String, Object> workflowList = workflowService.getWorkFlows(entityName, userId, workflowFilterObject, sortCriteria, forexCriteria.getPageNumber(), forexCriteria.getPageSize());
        return workflowList;
    }

    @Override
    public ForexBooking releaseEditLock(String requestId) throws OperationException{

        String userId = userService.getLoggedInUserId();
        ForexBooking forexBooking = getForexByRequestID(requestId);
        if (forexBooking.getLock()!=null && Boolean.TRUE.equals(forexBooking.getLock().isEnabled())) {
            if(forexBooking.getLock().getUserId().equals(userId)) {
                String res = workflowService.deleteWorkflow(forexBooking.getLock().getWorkflowId());
                if (res != null && res.equals("success")) {
                    RequestLockObject lockobject = forexBooking.getLock();
                    lockobject.setEnabled(false);
                    lockobject.setUserId(userId);
                    lockobject.setWorkflowId(null);
                    forexBooking.setLock(lockobject);
                    forexBooking.setLastModifiedByUserId(userId);
                    forexBooking.setLastModifiedAtTime(ZonedDateTime.now(ZoneOffset.UTC));
                    forexBookingRepository.saveOrUpdate(forexBooking);
                }
                else{
                    throw new OperationException(Constants.UNABLE_TO_RELEASE_LOCK, requestId);
                }
            }
            else{
                logger.warn("Cannot Release: Locked by another user "+ forexBooking.getLock().getUserId());
                throw new OperationException(Constants.LOCKED_BY_ANOTHER_USER, forexBooking.getLock().getUserId());
            }
        } else {
            logger.warn("Forex Booking with requestId"+ requestId +" to be released is not locked");
            throw new OperationException(Constants.NOT_LOCKED, requestId);
        }
        return forexBooking;
    }

    public ForexBooking getEditLock(String requestId, String userId) throws OperationException{

        String workflowId;
        ForexBooking forexBooking = getForexByRequestID(requestId);
        forexBooking.set_id(forexBooking.getId());

        if (forexBooking.getLock()==null || Boolean.FALSE.equals(forexBooking.getLock().isEnabled())) {

            forexBooking.setStage("request");
            workflowId = workflowService.editMasterObject(forexBooking, entityName, userId,null);
            if(workflowId == null)
                throw new OperationException(Constants.UNABLE_TO_CREATE_WORKFLOW);

            RequestLockObject lockobject = new RequestLockObject();
            lockobject.setEnabled(true);
            lockobject.setUserId(userId);
            lockobject.setWorkflowId(workflowId);
            lockobject.setForexBooking(forexBooking);
            forexBooking.setLock(lockobject);
            forexBooking = forexBookingRepository.saveOrUpdate(forexBooking);

        } else {
            //TODO: Check if the logged in user is same as the user with lock, then return the workflow if already exists.
            //TODO: else return master Data which will be shown as readonly "Locked by another user".
            logger.info("Locked by user: " + forexBooking.getLock().getUserId());
            //Instead of throwing exception returning the booking as it has to be displayed in read-only mode.
            //UI will check the userId of the returned booking and if it is not same as the logged in user then display in
            //read-only mode and prompt the message "Locked by  "
        }
        return forexBooking;
    }

    private Set<TourCostDetails> populateTourCostDetails(ForexPassenger forexPassenger, OpsHolidaysPaxInfo opsHolidaysPaxInfo, OpsBooking opsBooking) {

        Set<TourCostDetails> tourCostDetails = new HashSet<>();
        Map<String, BigDecimal> currencyToAmount = new HashMap<>();

        BigDecimal total;
        List<OpsHolidaysProductPrice> opsHolidaysProductPrices = opsHolidaysPaxInfo.getPriceDetails().getTotalNetPrice().getProductPrice();
        for (OpsHolidaysProductPrice opsHolidaysProductPrice : opsHolidaysProductPrices) {
            for (OpsHolidaysProductDetails opsHolidaysProductDetails : opsHolidaysProductPrice.getProductDetails()) {
                if((total = currencyToAmount.get(opsHolidaysProductDetails.getCurrencyCode()))!=null) {
                    total = total.add(new BigDecimal(StringUtils.isEmpty(opsHolidaysProductDetails.getPrice()) ? "0" : opsHolidaysProductDetails.getPrice()));
                    currencyToAmount.put(opsHolidaysProductDetails.getCurrencyCode(), total);
                }
                else{
                    currencyToAmount.put(opsHolidaysProductDetails.getCurrencyCode(), new BigDecimal(StringUtils.isEmpty(opsHolidaysProductDetails.getPrice()) ? "0" : opsHolidaysProductDetails.getPrice()));
                }
            }
        }
        for (Map.Entry<String, BigDecimal> entry : currencyToAmount.entrySet()) {
            //No Forex Request or Indent will be created for INR.
            if(!entry.getKey().equals(CCYCODE_IND)) {
                TourCostDetails tourCostDetail = new TourCostDetails();
                String cCy = entry.getKey();
                //TODO : Check if this is correct?
                String url = String.format(roeUrl, cCy, CCYCODE_IND, opsBooking.getClientMarket());
                Float roe = RestUtils.getForObject(url, Float.class);

                tourCostDetail.setBuyingAmount(entry.getValue());
                tourCostDetail.setCurrency(cCy);
                tourCostDetail.setRateOfExchange(roe); // Daily ROE - MDM
                tourCostDetail.setInrEquivalent(new BigDecimal(roe * (tourCostDetail.getBuyingAmount().floatValue())));
                //TODO: To Check if this hard-coding is ok?
                tourCostDetail.setBillTo("Company");
                tourCostDetail.setForexPassenger(forexPassenger);
                tourCostDetails.add(tourCostDetail);
            }
        }
        return tourCostDetails;
    }

    private PassportDetails populatePassportDetails(List<BookingDocumentDetailsResource> forexPaxDocs) {

        PassportDetails passportDetails = new PassportDetails();
        for (BookingDocumentDetailsResource forexPaxDoc : forexPaxDocs) {
            PassportResource passportResource = forexPaxDoc.getPassport();

            if (forexPaxDoc.getDocumentName().equalsIgnoreCase("Passport") && passportResource != null) {
                passportDetails.setDateOfIssue(passportResource.getDateOfIssue());
                passportDetails.setExpiryDate(passportResource.getPassportExpiryDate());
                passportDetails.setNationality(passportResource.getNationality());
                passportDetails.setPassportNumber(passportResource.getPassportNumber());
                passportDetails.setPlaceOfIssue(passportResource.getPlaceOfIssue());
            }
        }
        return passportDetails;
    }


    public ForexBooking getForexByID(String id) throws OperationException {

        ForexBooking forexBooking;
        try {
            forexBooking = forexBookingRepository.getById(id);
            forexBooking.set_id(forexBooking.getId());
        } catch (Exception e) {
            logger.error("Exception occurred while fetching Forex Booking Details for id " + id, e);
            throw new OperationException(Constants.UNABLE_TO_FETCH_DETAILS);
        }
        if (forexBooking == null) {
            logger.error("No ForexBooking found for id " + id);
            throw new OperationException(Constants.NO_RESULT_FOUND);
        }
        return forexBooking;
    }

    @Override
    public ForexBooking getForexByRequestID(String requestId) throws OperationException {

        ForexBooking forexBooking;
        try {
            forexBooking = forexBookingRepository.getByRequestId(requestId);
        } catch (Exception e) {
            logger.error("Exception occurred while fetching Forex Details for RequestId " + requestId, e);
            throw new OperationException(Constants.UNABLE_TO_FETCH_DETAILS);
        }
        if(forexBooking==null){
            logger.error("No result found for RequestId " + requestId);
            throw new OperationException(Constants.NO_RESULT_FOUND);
        }
        return forexBooking;
    }

    @Override
    public List<String> getClientListForGivenName(String name) throws OperationException {

        try {
            return forexBookingRepository.getClientListForGivenName(name);
        } catch (Exception e) {
            logger.error("Exception occured while fetching ClientName From Forex record", e);
            throw new OperationException(Constants.UNABLE_TO_FETCH_DETAILS);
        }
    }

    @Override
    public List<String> getClientList() throws OperationException {

        try {
            return forexBookingRepository.getClientList();
        } catch (Exception e) {
            logger.error("Exception occured while fetching ClientNames From Forex record", e);
            throw new OperationException(Constants.UNABLE_TO_FETCH_DETAILS);
        }
    }

    @Override
    public List<String> getBookRefList() throws OperationException {
        try {
            return forexBookingRepository.getBookRefList();
        } catch (Exception e) {
            logger.error("Exception occured while fetching BookReference Number from Forex record", e);
            throw new OperationException(Constants.UNABLE_TO_FETCH_DETAILS);
        }
    }

    @Override
    public List<String> getBookRefListForGivenString(String bookRef) throws OperationException {
        try {
            return forexBookingRepository.getBookRefListForGivenString(bookRef);
        } catch (Exception e) {
            logger.error("Exception occured while fetching BookReference Number from Forex record", e);
            throw new OperationException(Constants.UNABLE_TO_FETCH_DETAILS);
        }
    }

    public List<String> getReqOrIndentIds(String id) throws OperationException {

        try {
            return forexBookingRepository.getReqOrIndentIdsForGivenValue(id);
        } catch (Exception e) {
            logger.error("Exception occured while fetching Request/Indent-Id from Forex record", e);
            throw new OperationException(Constants.UNABLE_TO_FETCH_DETAILS);
        }
    }

    @Override
    public ForexBooking submit(ForexBookingResource resource) throws OperationException {

        ForexBooking forexBooking = getForexByID(resource.getId());
        //Create a TourCost Indent if not already created as user approval not required for TourCost.
        this.createTourCostIndent(forexBooking);
        //Remove Existing PersonalIndent, a new indent will be created after this request is approved.
        this.removeExistingPersonalIndent(forexBooking);
        forexBookingRepository.saveOrUpdate(forexBooking);

        //TODO: Save only when todo is successfully created.
        forexBooking = saveOrUpdate(resource);

        Boolean isPersonalRequired = false;
        for (ForexPassengerResource forexPassengerResource : resource.getForexPassengerSet()){
            if(forexPassengerResource.getPersonalExpenseDetails()!=null)
                isPersonalRequired = true;
        }

        if(isPersonalRequired) {
            try {
                //Create TO-DO task for Approval
                //If TO-DO task is already created, close it and create a new TO-DO Task
                //TODO: If the request is rejected and a new submit comes , then we can edit the Db ,
                //TODO: if it was already approved then, a new submit request should not be changed in db and only after it is approved it must be changed.

                if (forexBooking.getApproverToDoTaskId() != null && forexBooking.getRequestStatus().equals(RequestStatus.PENDING)) {
                    toDoTask = toDoTaskService.getById(forexBooking.getApproverToDoTaskId());
                    toDoTaskService.updateToDoTaskStatus(toDoTask.getReferenceId(), ToDoTaskSubTypeValues.FOREX_REQUEST, ToDoTaskStatusValues.CLOSED);
                }

                toDoTask = toDoTaskService.save(getTodoForOpsApproval(forexBooking));
                //If a edited request is submitted then the status should again be changed to PENDING
                forexBooking.setRequestStatus(RequestStatus.PENDING);
                forexBooking.setApproverToDoTaskId(toDoTask.getId());
                logger.info("To-do task id " + toDoTask.getId());

            } catch (Exception e) {
                logger.error("Unable to create Ops user To-Do Task for Updating of Forex Indent", e);
                throw new OperationException("Unable to create Ops user To-Do Task for Updating of Forex Indent");
            }
        }else{
            //If Personal Expense not required then no need for approval, Set the status as Confirmed.
            forexBooking.setRequestStatus(RequestStatus.CONFIRMED);
        }
        forexBookingRepository.saveOrUpdate(forexBooking);
        return forexBooking;
    }

    private void removeExistingPersonalIndent(ForexBooking forexBooking) {

        Set<ForexIndent> forexIndents = forexBooking.getIndentSet();
        if(forexIndents!=null && forexIndents.size()!=0) {

            ForexIndent personalIndent = null;
            for (ForexIndent indent : forexIndents) {
                if (indent.getIndentFor().equals(IndentType.PERSONALEXPENSE))
                    personalIndent = indent;
            }
            if (personalIndent != null) {
                 forexIndents.remove(personalIndent);
                 forexBooking.setIndentSet(forexIndents);
            }
        }
    }

    public ToDoTaskResource getTodoForOpsApproval(ForexBooking forexBooking) throws OperationException {

        ToDoTaskResource toDoTaskResource = new ToDoTaskResource();
        B2BClient b2BClient;
        B2CClient b2CClient;
        toDoTaskResource.setCreatedByUserId(userService.getLoggedInUserId());
        toDoTaskResource.setReferenceId(forexBooking.getRequestId());
        OpsBooking opsBooking = opsBookingService.getBooking(forexBooking.getBookRefNo());
        toDoTaskResource.setClientTypeId(opsBooking.getClientType());
        toDoTaskResource.setCompanyId(opsBooking.getCompanyId());
        toDoTaskResource.setClientId(opsBooking.getClientID());
        if (opsBooking.getClientType().equalsIgnoreCase("B2B")) {
            b2BClient = mdmClientService.getB2bClient(opsBooking.getClientID());
            toDoTaskResource.setClientCategoryId(b2BClient.getClientCategory());
            toDoTaskResource.setCompanyMarketId(b2BClient.getCompanyId());//TODo: need to add in B2Bclient class
            toDoTaskResource.setClientSubCategoryId(b2BClient.getClientSubCategory());
        }
        if (opsBooking.getClientType().equalsIgnoreCase("B2C")) {
            b2CClient = mdmClientService.getB2cClient(opsBooking.getClientID());
            toDoTaskResource.setClientCategoryId(b2CClient.getClientCategory());
            toDoTaskResource.setCompanyMarketId(b2CClient.getCompanyId());//TODo: need to add in B2Bclient class
            toDoTaskResource.setClientSubCategoryId(b2CClient.getClientSubCategory());
        }
//        TODO : Remove sample time and replace with original time
        toDoTaskResource.setDueOnDate(ZonedDateTime.now());
        toDoTaskResource.setTaskNameId(ToDoTaskNameValues.APPROVE.getValue());
        toDoTaskResource.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());

        toDoTaskResource.setTaskSubTypeId(ToDoTaskSubTypeValues.FOREX_REQUEST.toString());
        toDoTaskResource.setBookingRefId(forexBooking.getBookRefNo());

        toDoTaskResource.setFileHandlerId(userService.getLoggedInUserId());
        toDoTaskResource.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());//ToDoFunctionalAreaValues.OPERATIONS.getValue()); //Ops Approver

        return toDoTaskResource;
    }

    @Override
    public ForexBooking approveForexRequest(String requestId, String remarks) throws OperationException {

        String toDoId;
        ToDoTask toDoTask;
        ForexBooking forexBooking = getForexByRequestID(requestId);
        if (RequestStatus.PENDING.equals(forexBooking.getRequestStatus())) {
            JSONObject jsonObject = new JSONObject(remarks);
            String remarkValue = (String) jsonObject.get("remarks");
            forexBooking.setApproverRemark(remarkValue);
            forexBooking.setRequestStatus(RequestStatus.CONFIRMED);
            this.createPersonalExpenseIndent(forexBooking);

            forexBookingRepository.saveOrUpdate(forexBooking);
            toDoId = forexBooking.getApproverToDoTaskId();
            try {
                toDoTask = toDoTaskService.getById(toDoId);
            } catch (Exception e) {
                logger.error("IOException in To-Do task");
                //TODO : Log an appropriate Exception
                throw new OperationException("Unable to get TO-DO Task for Id" + toDoId);
            }
            toDoTaskService.updateToDoTaskStatus(toDoTask.getReferenceId(), ToDoTaskSubTypeValues.FOREX_REQUEST, ToDoTaskStatusValues.CLOSED);


        } else {
            throw new OperationException(Constants.ACTION_ALREADY_TAKEN, "Request", forexBooking.getRequestStatus().name());
        }
        return forexBooking;
    }

    @Override
    public ForexBooking rejectForexRequest(String requestId, String remarks) throws OperationException {
        String toDoId;
        ToDoTask toDoTask;
        ForexBooking forexBooking = getForexByRequestID(requestId);

        if (RequestStatus.PENDING.equals(forexBooking.getRequestStatus())) {
            JSONObject jsonObject = new JSONObject(remarks);
            String remarkValue = (String) jsonObject.get("remarks");
            forexBooking.setApproverRemark(remarkValue);
            forexBooking.setRequestStatus(RequestStatus.REJECTED);
            forexBookingRepository.saveOrUpdate(forexBooking);
            toDoId = forexBooking.getApproverToDoTaskId();
            try {
                toDoTask = toDoTaskService.getById(toDoId);
            } catch (Exception e) {
                logger.error("IOException in To-Do task");
                //TODO : Log an appropriate Exception
                throw new OperationException("Unable to get TO-DO Task for Id" + toDoId);
            }
            toDoTaskService.updateToDoTaskStatus(toDoTask.getReferenceId(), ToDoTaskSubTypeValues.FOREX_REQUEST, ToDoTaskStatusValues.CLOSED);

        } else {
            throw new OperationException(Constants.ACTION_ALREADY_TAKEN, "Request", forexBooking.getRequestStatus().name());
        }
        return forexBooking;
    }

    @Override
    public ForexCountResource getCountByStatus() throws OperationException {

        ForexCountResource forexCountResource = new ForexCountResource();
        try {
            forexCountResource.setRequestPending(forexBookingRepository.getRequestStatusCount(RequestStatus.PENDING));
            forexCountResource.setRequestCompleted(forexBookingRepository.getRequestStatusCount(RequestStatus.CONFIRMED));
            forexCountResource.setIndentPending(forexIndentRepository.getIndentStatusCount(IndentStatus.PENDING));
            forexCountResource.setIndentCompleted(forexIndentRepository.getIndentStatusCount(IndentStatus.CONFIRMED));
            forexCountResource.setDisbursementPending(forexDisbursementRepository.getDisbursementStatusCount("PENDING"));
            forexCountResource.setDisbursementCompleted(forexDisbursementRepository.getDisbursementStatusCount("CONFIRMED"));

        } catch (Exception e) {
            logger.error("Unable to get the Status wise counts of Forex record");
            throw new OperationException(Constants.UNABLE_TO_GET_COUNTS);
        }
        return forexCountResource;
    }

    @Override
    public List<PassengerNameResource> getPaxListForGivenName(String name) throws OperationException {

        try {
            return forexBookingRepository.getPaxListForGivenName(name);
        } catch (Exception e) {
            logger.error("Exception occured while fetching Passenger names from Forex record", e);
            throw new OperationException(Constants.UNABLE_TO_FETCH_DETAILS);
        }
    }

    //Logic for Request-Id Generation
    public Long gen() {
        Long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
        return number;
    }

}
