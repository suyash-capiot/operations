package com.coxandkings.travel.operations.service.forex.impl;

import com.coxandkings.travel.operations.enums.forex.IndentStatus;
import com.coxandkings.travel.operations.enums.forex.IndentType;
import com.coxandkings.travel.operations.enums.serviceOrderAndSupplierLiability.ServiceOrderAndSupplierLiabilityType;
import com.coxandkings.travel.operations.enums.todo.ToDoFunctionalAreaValues;
import com.coxandkings.travel.operations.enums.todo.ToDoTaskNameValues;
import com.coxandkings.travel.operations.enums.todo.ToDoTaskSubTypeValues;
import com.coxandkings.travel.operations.enums.todo.ToDoTaskTypeValues;
import com.coxandkings.travel.operations.enums.workflow.WorkflowOperation;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.forex.*;
import com.coxandkings.travel.operations.model.productbookedthrother.mdm.B2BClient;
import com.coxandkings.travel.operations.model.productbookedthrother.mdm.B2CClient;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.PassengersDetails;
import com.coxandkings.travel.operations.model.todo.ToDoTask;
import com.coxandkings.travel.operations.repository.forex.ForexBookingRepository;
import com.coxandkings.travel.operations.repository.forex.ForexIndentRepository;
import com.coxandkings.travel.operations.resource.forex.ForexBookingResource;
import com.coxandkings.travel.operations.resource.forex.ForexIndentResource;
import com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability.ServiceOrderResource;
import com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability.SupplierPricingResource;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResource;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.forex.ForexBookingService;
import com.coxandkings.travel.operations.service.forex.ForexIndentService;
import com.coxandkings.travel.operations.service.mdmservice.ClientMasterDataService;
import com.coxandkings.travel.operations.service.productbookedthrother.mdm.MdmClientService;
import com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.ProvisionalServiceOrderService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.service.workflow.WorkflowService;
import com.coxandkings.travel.operations.systemlogin.MDMDataSource;
import com.coxandkings.travel.operations.systemlogin.MDMToken;
import com.coxandkings.travel.operations.utils.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class ForexIndentServiceImpl implements ForexIndentService {

    private Logger logger = Logger.getLogger(ForexIndentService.class);

    @Autowired
    private MDMRestUtils mdmRestUtil;

    @Autowired
    private ClientMasterDataService clientMasterDataService;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private MdmClientService mdmClientService;

    private ToDoTask toDoTask;

    @Autowired
    private ToDoTaskService toDoTaskService;

    @Autowired
    private ForexBookingRepository forexBookingRepository;
    @Autowired
    private ForexIndentRepository forexIndentRepository;
    @Autowired
    private ForexBookingService forexBookingService;
    @Autowired
    @Qualifier(value = "mDMDataSource")
    private MDMDataSource mdmDataSource;
    @Autowired
    private UserService userService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ProvisionalServiceOrderService provisionalServiceOrder;

    @Value(value = "${manage_documentation.be.get_document_details}")
    private String getBookingDocsUrl;

    @Value(value = "${daily_roe}")
    private String roeUrl;

    @Value("${forex_indent_to_supplier.template_config.function}")
    private String function;

    @Value("${forex_indent_to_supplier.template_config.scenario}")
    private String scenario;

    @Value("${forex_indent_to_supplier.template_config.subject}")
    private String subject;

    @Value("${forex_indent_to_supplier.dynamic_variables.supplier_name}")
    private String supplierName;

    @Value("${forex_indent_to_supplier.dynamic_variables.tabular_info}")
    private String tabularInfo;

    @Autowired
    private EmailUtils emailUtils;

    @Value("${manage-forex.mdm.extranet-access}")
    private String extranetAccess;

    @Autowired
    @Qualifier(value = "mDMToken")
    private MDMToken mdmToken;

    private static String entityName = "Forex-Request";
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Deprecated
    @Override
    public ForexIndent saveOrUpdate(ForexIndentResource resource) throws OperationException {

        String id = resource.getId();
        ForexIndent forexIndent = null;
        if (!StringUtils.isEmpty(id)) {

            ForexIndent existingForexIndent = forexIndentRepository.getById(id);
            if (forexIndent.getIndentStatus().equals(IndentStatus.PENDING)) {

                if (existingForexIndent == null) {
                    logger.error("Forex Indent not exists for id: " + id);
                    throw new OperationException(Constants.ER01);
                }
                this.updateExistingForexIndent(existingForexIndent, resource);
                forexIndent = existingForexIndent;

                try {
                    forexIndentRepository.saveOrUpdate(forexIndent);
                } catch (Exception e) {
                    logger.error("Exception occured while trying to update/save ForexIndent for id:" + id, e);
                    //TODO: Log an exception.
                    throw new OperationException(Constants.UNABLE_TO_SAVE);
                }
            }
        } else {
            logger.error("Id is null or Empty");
            throw new OperationException(Constants.ID_NULL_EMPTY);
        }
        return forexIndent;
    }

     @Override
     public void updateExistingForexIndent(ForexIndent existingForexIndent, ForexIndentResource resource) {

        existingForexIndent.setSupplierName(resource.getSupplierName());
        existingForexIndent.setSupplierId(resource.getSupplierId());
        existingForexIndent.setPaymentType(resource.getPaymentType());
        existingForexIndent.setLastModifiedAtTime(ZonedDateTime.now(ZoneOffset.UTC));
        existingForexIndent.setLastModifiedByUserId(userService.getLoggedInUserId());
        existingForexIndent.setIndentStatus(IndentStatus.CONFIRMED);

        Set<ForexBuyingCurrency> forexBuyingCurrencies = resource.getForexBuyingCcyDetails();
        if(forexBuyingCurrencies!=null) {
            forexBuyingCurrencies.stream().forEach(forexBuyingCurrency -> {
                forexBuyingCurrency.setIndentId(existingForexIndent.getId());
                forexBuyingCurrency.setSupplierName(resource.getSupplierName());
            });
            existingForexIndent.setForexBuyingCcyDetails(forexBuyingCurrencies);
        }
    }

    @Override
    public ForexIndent getIndentById(String id) throws OperationException {

        ForexIndent forexIndent;
        try {
            forexIndent = forexIndentRepository.getById(id);
        } catch (Exception e) {
            logger.error("Exception occured while fetching Forex Indent Details for id " + id, e);
            throw new OperationException(Constants.UNABLE_TO_FETCH_DETAILS);
        }
        if (forexIndent == null) {
            logger.error("No indent found for id " + id);
            throw new OperationException(Constants.NO_RESULT_FOUND);
        }
        return forexIndent;
    }

    @Override
    public List<ForexIndent> getIndentsByForexId(String id) throws OperationException {

        try {
            List<ForexIndent> forexIndents = forexIndentRepository.getIndentsByForexId(id);
            return forexIndents;
        } catch (Exception e) {
            logger.error("Exception occured while fetching Forex Indent Details for Forex id " + id, e);
            throw new OperationException(Constants.UNABLE_TO_FETCH_DETAILS);
        }
    }

    @Override
    public List<ForexIndent> getIndentsByRequestId(String id) throws OperationException {

        try {
            List<ForexIndent> forexIndents = forexIndentRepository.getIndentsByRequestId(id);
            return forexIndents;
        } catch (Exception e) {
            logger.error("Exception occured while fetching Forex Indent Details for Request id " + id, e);
            throw new OperationException(Constants.UNABLE_TO_FETCH_DETAILS);
        }
    }

    @Override
    public void sendIndentToSupplier(String id) throws OperationException {

        ForexIndent forexIndent = getIndentById(id);
        Map<String, String> dynamicVariables = new HashMap<>();
        try {
            dynamicVariables.put(supplierName, forexIndent.getSupplierName());
            /*if (forexIndent.getIndentFor().equals(IndentType.TOURCOST))
                dynamicVariables.put(tabularInfo, getTourCostTableInfo(forexIndent));
            else
                dynamicVariables.put(tabularInfo, getPersonalExpTableInfo(forexIndent));*/
        } catch (Exception e) {
            logger.error("Exception occured while sending Indent mail to supplier-" + forexIndent.getSupplierName() + " For Indent Id-" + id, e);
            throw new OperationException(Constants.UNABLE_TO_SEND_MAIL);
        }
        //TODO : Change the email to Suppliers mail, Uncomment this later.
//      emailUtils.buildClientMail(function, scenario, getSupplierEmailId(forexIndent.getSupplierName()), subject, dynamicVariables, null, null);
//        emailUtils.buildClientMail(function, scenario, "sharma.shivam@coxandkings.com", subject, dynamicVariables, null, null);
//        forexIndent.setSentToSupplier(true);
        forexIndentRepository.saveOrUpdate(forexIndent);

        //Service order is generated only in case of TOURCOST Indent.
        if(forexIndent.getIndentFor().equals(IndentType.TOURCOST))
            this.generatePSO(forexIndent);

    }

    private void generatePSO(ForexIndent indent) throws OperationException{

        ServiceOrderResource serviceOrder = new ServiceOrderResource();
        try {
            ForexBooking booking = getForexByIndentId(indent.getId());
            serviceOrder.setBookingRefNo(booking.getBookRefNo());
            serviceOrder.setCreatedByUserId(userService.getLoggedInUserId());
            serviceOrder.setCreatedTime(ZonedDateTime.now());
            serviceOrder.setDateOfGeneration(ZonedDateTime.now());
            //TODO : How to find Company Market Id.
            serviceOrder.setCompanyMarketId("");
            //TODO:use Enum
            serviceOrder.setProductCategory("Other Products");
            serviceOrder.setProductCategorySubType("Forex");
            serviceOrder.setProductCategoryId("Other Products");
            serviceOrder.setProductCategorySubTypeId("Forex");
            serviceOrder.setOrderId(indent.getId());
            serviceOrder.setSupplierName(indent.getSupplierName());
            serviceOrder.setSupplierId(indent.getSupplierId());
            serviceOrder.setSupplierCurrency(ForexBookingServiceImpl.CCYCODE_IND);

            serviceOrder.setType(ServiceOrderAndSupplierLiabilityType.PSO);
            SupplierPricingResource supplierPricingResource = new SupplierPricingResource();
            BigDecimal totalSupplierCost = BigDecimal.ZERO;
            for(ForexBuyingCurrency forexBuyingCurrency : indent.getForexBuyingCcyDetails()){
                totalSupplierCost = totalSupplierCost.add(forexBuyingCurrency.getInrEquivalent());
            }
            supplierPricingResource.setSupplierCost(totalSupplierCost);
            supplierPricingResource.setCancellationCharges(BigDecimal.ZERO);
            supplierPricingResource.setAmendmentCharges(BigDecimal.ZERO);
            supplierPricingResource.setSupplierCommercials(BigDecimal.ZERO);
            supplierPricingResource.setSupplierGst(BigDecimal.ZERO);
            supplierPricingResource.setAmountPaidToSupplier(BigDecimal.ZERO);
            supplierPricingResource.setSurcharges(BigDecimal.ZERO);
            supplierPricingResource.setNetPayableToSupplier(totalSupplierCost);
            supplierPricingResource.setTotalBalanceAmountPayable(totalSupplierCost);

            Set<PassengersDetails> passengersDetails = new HashSet<>();

            BigDecimal totalCost = BigDecimal.ZERO;
            for(ForexPassenger pax : booking.getForexPassengerSet()){
                BigDecimal totalPaxCost = BigDecimal.ZERO;
                PassengersDetails paxDetails = new PassengersDetails();
                Set<TourCostDetails> tourCostDetails = pax.getTourCostDetails();
                for(TourCostDetails tourCostDetail : tourCostDetails) {
                    ForexBuyingCurrency forexBuyingCurrency = indent.getForexBuyingCcyDetails().stream().filter(buyingAmount ->
                            buyingAmount.getBuyingCurrency().equals(tourCostDetail.getCurrency())).findFirst().get();
                    totalPaxCost = totalPaxCost.add(tourCostDetail.getBuyingAmount().multiply(forexBuyingCurrency.getDiscountedRoe()));
                }
                paxDetails.setSupplierCostPrice(totalPaxCost);
                paxDetails.setPassengerType(pax.getPassengerType());
                paxDetails.setRatePerPassenger(totalPaxCost);
                paxDetails.setNoOfPassengers(booking.getForexPassengerSet().size());
                passengersDetails.add(paxDetails);
                totalCost = totalCost.add(totalPaxCost);
            }
            supplierPricingResource.setSupplierCost(totalCost);
            supplierPricingResource.setPassengerDetails(passengersDetails);
            //TODO : To decide how to set the supplier pricing Info.
            serviceOrder.setSupplierPricingResource(supplierPricingResource);
            serviceOrder.setNetPayableToSupplier(supplierPricingResource.getNetPayableToSupplier());
            provisionalServiceOrder.generatePSO(serviceOrder);

        }catch(IOException e){
            throw new OperationException("Unable to create Provisional Service Order for Forex Indent"+ indent.getId());
        }
    }

    private ForexBooking getForexByIndentId(String indentId) throws OperationException{

        try {
            ForexBooking booking = forexIndentRepository.getForexByIndentId(indentId);
            return booking;
        }catch(Exception e){
            logger.warn("No Forex Booking found for Indent Id" + indentId, e);
            throw new OperationException(Constants.NO_REQUEST_FOUND_FOR_INDENT, indentId);
        }
    }

    @Override
    public ForexIndent getIndentByType(String requestId, String type) throws OperationException {

        ForexIndent forexIndent;
        IndentType indentType = IndentType.fromString(type);
        if (indentType == null) {
            logger.error("Indent Type " + type + " not valid");
            throw new OperationException(Constants.INVALID_INDENT_TYPE, type);
        }
        try {
            forexIndent = forexIndentRepository.getIndentsByType(requestId, indentType);
        } catch (Exception e) {
            logger.error("Exception occurred while fetching Forex Indent Details for Request id " + requestId, e);
            throw new OperationException(Constants.UNABLE_TO_FETCH_DETAILS);
        }
        if (forexIndent == null) {
            logger.error(String.format("No %d Indent found for Request id: %d", type, requestId));
            throw new OperationException(Constants.NO_RESULT_FOUND);
        }
        return forexIndent;
    }

    /* @Override
     public ForexIndent submit(ForexIndentResource resource) throws OperationException {

         ForexIndent forexIndent = saveOrUpdate(resource);

         if (forexIndent.getIndentStatus().equals(IndentStatus.PENDING)) {
             try {
                 toDoTask = toDoTaskService.save(getTodoForOpsApproval(forexIndent));
                 forexIndent.setApproverToDoTaskId(toDoTask.getId());
                 forexIndent = forexIndentRepository.saveOrUpdate(forexIndent);
                 logger.info("To-do task id " + toDoTask.getId());
             } catch (Exception e) {
                 logger.error("Unable to create Ops user To-Do Task for Updating of Forex Indent", e);
             }
         }
         else
             logger.error("TO-DO Task already created and action taken by approver for Indent Id "+ forexIndent.getId());

         return forexIndent;
     }
 */
   /* @Override
    public ForexIndent approveForexIndent(String indentId, String remarks) throws OperationException {

        String toDoId;
        ToDoTask toDoTask;
        ForexIndent forexIndent = getIndentById(indentId);

        if (IndentStatus.PENDING.equals(forexIndent.getIndentStatus())) {
            JSONObject jsonObject = new JSONObject(remarks);
            String remarkValue = (String) jsonObject.get("remarks");
            forexIndent.setApproverRemark(remarkValue);
            forexIndent.setIndentStatus(IndentStatus.CONFIRMED);
            toDoId = forexIndent.getApproverToDoTaskId();
            try {
                toDoTask = toDoTaskService.getById(toDoId);
            } catch (Exception e) {
                logger.error("IOException in To-Do task");
                //TODO : Log an appropriate Exception
                throw new OperationException("Unable to get TO-DO Task for Id" + toDoId);
            }
            toDoTaskService.updateToDoTaskStatus(toDoTask.getReferenceId(), ToDoTaskSubTypeValues.FOREX_INDENT, ToDoTaskStatusValues.CLOSED);

            //TODO: Need to generate payment Advice
            //Create Disbursement Details with Status PENDING

            DisbursementDetails disbursementDetails = new DisbursementDetails();
            disbursementDetails.setStatus("Pending");
            disbursementDetails.setIndent(forexIndent.getId());
            forexIndent.setDisbursementDetails(disbursementDetails);

            forexIndent = forexIndentRepository.saveOrUpdate(forexIndent);

        } else {
            throw new OperationException(Constants.ACTION_ALREADY_TAKEN, "Indent", forexIndent.getIndentStatus().name());
        }
        return forexIndent;
    }*/
/*

    @Override
    public ForexIndent rejectForexIndent(String indentId, String remarks) throws OperationException {
        String toDoId;
        ToDoTask toDoTask;
        ForexIndent forexIndent = getIndentById(indentId);

        if (IndentStatus.PENDING.equals(forexIndent.getIndentStatus())) {
            JSONObject jsonObject = new JSONObject(remarks);
            String remarkValue = (String) jsonObject.get("remarks");
            forexIndent.setApproverRemark(remarkValue);
            forexIndent.setIndentStatus(IndentStatus.REJECTED);
            forexIndent = forexIndentRepository.saveOrUpdate(forexIndent);
            toDoId = forexIndent.getApproverToDoTaskId();
            try {
                toDoTask = toDoTaskService.getById(toDoId);
            } catch (Exception e) {
                logger.error("IOException in To-Do task");
                //TODO : Log an appropriate Exception
                throw new OperationException("Unable to get TO-DO Task for Id" + toDoId);
            }
            toDoTaskService.updateToDoTaskStatus(toDoTask.getReferenceId(), ToDoTaskSubTypeValues.FOREX_INDENT, ToDoTaskStatusValues.CLOSED);

        } else {
            throw new OperationException(Constants.ACTION_ALREADY_TAKEN, "Indent", forexIndent.getIndentStatus().name());
        }
        return forexIndent;
    }
*/

    @Override
    public List<String> getSupplierListForGivenName(String name) throws OperationException {

        try {
            List<String> supplierList = forexIndentRepository.getSupplierListForGivenName(name);
            supplierList.addAll(getSupplierNameFromWorkflow(name));
            Set<String> suppliersWithoutDuplicates = new HashSet<>(supplierList);
            supplierList.clear();
            supplierList.addAll(suppliersWithoutDuplicates);

            return supplierList;
        } catch (Exception e) {
            logger.error("Exception occurred while fetching SupplierName from Forex record", e);
            throw new OperationException(Constants.UNABLE_TO_FETCH_DETAILS);
        }
    }

    private List<String> getSupplierNameFromWorkflow(String name){

        ResponseEntity<String> entity;
        List<String> supplierList = new ArrayList<>();
        JSONObject filterObject = new JSONObject();
        JSONObject statusFilter = new JSONObject();
        statusFilter.putOpt("$ne", "Approved");
        filterObject.putOpt("type", entityName);
        filterObject.putOpt("createdBy", userService.getLoggedInUserId());
        filterObject.putOpt("status", statusFilter);

        String value = "(?i).*"+name+".*";
        filterObject.putOpt("clientProfile.clientStructure.clientName", new JSONObject().put("$regex", value));
//        in.put("/"+name+"/"); // Similar to LIKE in SQL
//        filterObject.putOpt("doc.newDoc.indentSet.supplierName" , new JSONObject().put("$in", in));
        filterObject.putOpt("doc.newDoc.indentSet.supplierName" , new JSONObject().put("$regex", value));
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String token = userService.getLoggedInUserToken();
            headers.add("Authorization", token);
            String url = workflowService.getWorkflowGetUrl() + "?filter={filter}"+"&select=doc.newDoc.indentSet.supplierName";
            entity = RestUtils.exchange(url, HttpMethod.GET, new HttpEntity(headers), String.class, filterObject.toString());

            String res = entity.getBody();
            JSONObject resJson = new JSONObject(new JSONTokener(res));
            JSONArray results = resJson.getJSONArray("data");

            for(Object doc : results){
                JSONObject docObject = (JSONObject) doc;
                JSONArray indentSet = docObject.getJSONObject("doc").getJSONObject("newDoc").getJSONArray("indentSet");
                for(Object indent : indentSet){
                    supplierList.add(((JSONObject)indent).getString("supplierName"));
                }
            }
        } catch (Exception e) {
            logger.warn("Unable to get supplierList from WorkFlow");
        }
        return supplierList;
    }

    @Override
    public ForexBooking updateIndent(String uid, ForexBookingResource resource,
                                     String workflow) throws OperationException{

        //In case of save and submit UID will be Workflow ID, else forex dB Id
        ForexBooking forexBooking;
        String workflowResponse;
        String userId = userService.getLoggedInUserId();
        switch (workflow){

            case "SAVE" :
                workflowResponse = workflowService.updateWorkflowDoc(resource, WorkflowOperation.SAVE, uid);
                JSONObject workflowObject = new JSONObject(workflowResponse);
                try {
                    forexBooking = objectMapper.readValue(
                            (workflowObject.getJSONObject("doc").getJSONObject("newDoc").toString()),
                            ForexBooking.class);
                    return forexBooking;
                } catch (Exception e) {
                    throw new OperationException("");
                }
            case "SUBMIT":

                ForexBooking existingForexBooking = forexBookingService.getForexByID(resource.getId());
                if (existingForexBooking == null) {
                    logger.error("Booking not exists for id: " + uid);
                    throw new OperationException(Constants.ER01);
                }
                workflowResponse = workflowService.updateWorkflowDoc(resource, WorkflowOperation.SUBMIT, uid);
                workflowObject = new JSONObject(workflowResponse);
                try {
                    forexBooking = objectMapper.readValue(
                            (workflowObject.getJSONObject("doc").getJSONObject("newDoc").toString()), ForexBooking.class);
                    return forexBooking;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            case "MASTERUPDATE" :

                ForexBooking lockedObject = getEditLock(uid, userId);
                return lockedObject;

            default:
                //MDM will hit this service whenever the indents are approved and lock object is cleared.
                existingForexBooking = forexBookingService.getForexByID(resource.getId());
                existingForexBooking.setLastModifiedAtTime(ZonedDateTime.now(ZoneOffset.UTC));
                existingForexBooking.setLastModifiedByUserId(userService.getLoggedInUserId());
                existingForexBooking.setDocumentIds(resource.getDocumentIds());
                Set<ForexIndentResource> indentSet = resource.getIndentSet();
                Set<ForexIndent> indentSets = existingForexBooking.getIndentSet();
                for(ForexIndentResource indentResource : indentSet){
                    ForexIndent indent = indentSets.stream().filter(s -> s.getId().equals(indentResource.getId())).findFirst().get();
                    this.updateExistingForexIndent(indent, indentResource);
                }
                existingForexBooking.setIndentSet(indentSets);
                forexBooking = forexBookingRepository.saveOrUpdate(existingForexBooking);
                return forexBooking;
        }
    }

    public ForexBooking getEditLock(String forexId, String userId) throws OperationException{

        String workflowId;
        ForexBooking forexBooking = forexBookingService.getForexByID(forexId);
        forexBooking.set_id(forexBooking.getId());

        if (forexBooking.getLock()==null || Boolean.FALSE.equals(forexBooking.getLock().isEnabled())) {

            workflowId = workflowService.editMasterObject(forexBooking, entityName, userId,null);
            if(workflowId== null)
                throw new OperationException(Constants.UNABLE_TO_CREATE_WORKFLOW);

            RequestLockObject lockobject = new RequestLockObject();
            lockobject.setEnabled(true);
            lockobject.setUserId(userId);
            lockobject.setWorkflowId(workflowId);
            forexBooking.setLock(lockobject);
            forexBooking = forexBookingRepository.saveOrUpdate(forexBooking);

        } else {
            logger.info("Locked by user: " + forexBooking.getLock().getUserId());
            //Instead of throwing exception returning the booking as it has to be displayed in read-only mode.
            //UI will check the userId of the returned booking and if it is not same as the logged in user then display in
            //read-only mode and prompt the message "Locked by user"
        }
        return forexBooking;
    }

    @Override
    public List<String> getSupplierList() throws OperationException {

        try {
            return forexIndentRepository.getSupplierList();
        } catch (Exception e) {
            logger.error("Exception occured while fetching SupplierNames from Forex record", e);
            throw new OperationException(Constants.UNABLE_TO_FETCH_DETAILS);
        }
    }

    public ToDoTaskResource getTodoForOpsApproval(ForexIndent forexIndent) throws OperationException {

        ToDoTaskResource toDoTaskResource = new ToDoTaskResource();
        B2BClient b2BClient;
        B2CClient b2CClient;
        toDoTaskResource.setCreatedByUserId(userService.getLoggedInUserId());
        toDoTaskResource.setReferenceId(forexIndent.getId());
        OpsBooking opsBooking = opsBookingService.getBooking(forexIndent.getForexBooking().getBookRefNo());
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

        toDoTaskResource.setTaskSubTypeId(ToDoTaskSubTypeValues.FOREX_INDENT.toString());
        toDoTaskResource.setBookingRefId(forexIndent.getForexBooking().getBookRefNo());

        toDoTaskResource.setFileHandlerId(userService.getLoggedInUserId());
        toDoTaskResource.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());//ToDoFunctionalAreaValues.OPERATIONS.getValue()); //Ops Approver

        return toDoTaskResource;
    }

/*
    private String getTourCostTableInfo(ForexIndent forexIndent) {

        Map<String, BigDecimal> discountedRoeMap = getDiscountedROEMap(forexIndent);

        ForexBooking forexBooking = forexIndent.getForexBooking();
        Set<ForexPassenger> forexPassengers = forexBooking.getForexPassengerSet();
        String tabularInfo = getTableHead(forexIndent.getIndentFor());

        BigDecimal tourCostTotal = BigDecimal.ZERO, mInrTourCostTotal = BigDecimal.ZERO;

        for (ForexPassenger forexPassenger : forexPassengers) {

            PassportDetails passportDetails = forexPassenger.getPassportDetails();
            TourCostDetails tourCostDetails = forexPassenger.getTourCostDetails();

            tourCostTotal = tourCostTotal.add(tourCostDetails.getBuyingAmount());
            mInrTourCostTotal = mInrTourCostTotal.add(tourCostDetails.getBuyingAmount().multiply(discountedRoeMap.get(tourCostDetails.getCurrency())));

            String paxName = forexPassenger.getSalutation() + "." + forexPassenger.getFirstName() + " " + forexPassenger.getMiddleName() + " " + forexPassenger.getLastName();
            tabularInfo += "\t\t<tr>\n" +
                    "\t\t\t<td>" + paxName + "</td>\n" +
                    "\t\t\t<td>" + forexBooking.getTravelCountry() + "</td>\n" +
                    "\t\t\t<td>" + forexPassenger.getAirLine() + "</td>\n" +
                    "\t\t\t<td>" + forexPassenger.getDepartureDate() + "</td>\n" +
                    "\t\t\t<td>" + forexPassenger.getTicketNumber() + "</td>\n" +
                    "\t\t\t<td>" + passportDetails.getPassportNumber() + "</td>\n" +
                    "\t\t\t<td>" + passportDetails.getPlaceOfIssue() + "</td>\n" +
                    "\t\t\t<td>" + passportDetails.getDateOfIssue() + "</td>\n" +
                    "\t\t\t<td>" + passportDetails.getExpiryDate() + " </td>\n" +
                    "\t\t\t<td>" + tourCostDetails.getBuyingAmount() + "</td>\n" +
                    "\t\t\t<td>" + tourCostDetails.getCurrency() + "</td>\n" +
                    "\t\t\t<td>" + discountedRoeMap.get(tourCostDetails.getCurrency()) + "</td>\n" +
                    "\t\t\t<td>" + tourCostDetails.getBuyingAmount().multiply(discountedRoeMap.get(tourCostDetails.getCurrency())) + "</td>\n" +
                    "\t\t\t<td>" + tourCostDetails.getBillTo() + "</td>\n" +
                    "\t\t</tr>\n";

        }
        tabularInfo += "\t\t<tr>\n" +
                "\t\t\t<td colspan=\"9\">Total</td>\n" +
                "\t\t\t<td>" + tourCostTotal + "</td>\n" +
                "\t\t\t<td>&nbsp;</td>\n" +
                "\t\t\t<td>&nbsp;</td>\n" +
                "\t\t\t<td>" + mInrTourCostTotal + "</td>\n" +
                "\t\t</tr>\n" +
                "\t</tbody>\n" +
                "</table>\n";

        return tabularInfo;
    }

    private String getPersonalExpTableInfo(ForexIndent personalExpIndent) throws OperationException {

        Map<String, BigDecimal> personalDiscountedRoeMap = getDiscountedROEMap(personalExpIndent);
        ForexBooking forexBooking = personalExpIndent.getForexBooking();
        ForexIndent tourCostIndent = getIndentByType(forexBooking.getRequestId(), "TOURCOST");

        Map<String, BigDecimal> tourCostDiscountedRoeMap = getDiscountedROEMap(tourCostIndent);

        Set<ForexPassenger> forexPassengers = forexBooking.getForexPassengerSet();
        String tabularInfo = getTableHead(personalExpIndent.getIndentFor());

        BigDecimal tourCostTotal = BigDecimal.ZERO, mInrTourCostTotal = BigDecimal.ZERO, mInrPersonalTotal = BigDecimal.ZERO;
        BigDecimal mCashAmount = BigDecimal.ZERO, mCardAmount = BigDecimal.ZERO, mTcAmount = BigDecimal.ZERO;
        for (ForexPassenger forexPassenger : forexPassengers) {

            BigDecimal amount, mInrPersonalAmount;
            PassportDetails passportDetails = forexPassenger.getPassportDetails();
            TourCostDetails tourCostDetails = forexPassenger.getTourCostDetails();
            PersonalExpenseDetails personalExpenseDetails = forexPassenger.getPersonalExpenseDetails();

            tourCostTotal = tourCostTotal.add(tourCostDetails.getBuyingAmount());
            mInrTourCostTotal = mInrTourCostTotal.add(tourCostDiscountedRoeMap.get(tourCostDetails.getCurrency()).multiply(tourCostDetails.getBuyingAmount()));

            mCardAmount = mCardAmount.add((amount = personalExpenseDetails.getCardAmount()) != null ? amount : BigDecimal.ZERO);
            mCashAmount = mCashAmount.add((amount = personalExpenseDetails.getCashAmount()) != null ? amount : BigDecimal.ZERO);
            mTcAmount = mTcAmount.add((amount = personalExpenseDetails.getTravellersChequeAmount()) != null ? amount : BigDecimal.ZERO);

            mInrPersonalAmount = mCardAmount.add(mCashAmount).add(mTcAmount)
                    .multiply(personalDiscountedRoeMap.get(tourCostDetails.getCurrency()));

            mInrPersonalTotal = mInrPersonalTotal.add(mInrPersonalAmount);

            String paxName = forexPassenger.getSalutation() + "." + forexPassenger.getFirstName() + " " + forexPassenger.getMiddleName() + " " + forexPassenger.getLastName();
            tabularInfo += "\t\t<tr>\n" +
                    "\t\t\t<td>" + paxName + "</td>\n" +
                    "\t\t\t<td>" + forexBooking.getTravelCountry() + "</td>\n" +
                    "\t\t\t<td>" + forexPassenger.getAirLine() + "</td>\n" +
                    "\t\t\t<td>" + forexPassenger.getDepartureDate() + "</td>\n" +
                    "\t\t\t<td>" + forexPassenger.getTicketNumber() + "</td>\n" +
                    "\t\t\t<td>" + passportDetails.getPassportNumber() + "</td>\n" +
                    "\t\t\t<td>" + passportDetails.getPlaceOfIssue() + "</td>\n" +
                    "\t\t\t<td>" + passportDetails.getDateOfIssue() + "</td>\n" +
                    "\t\t\t<td>" + passportDetails.getExpiryDate() + " </td>\n" +
                    "\t\t\t<td>" + tourCostDetails.getBuyingAmount() + "</td>\n" +
                    "\t\t\t<td>" + tourCostDetails.getCurrency() + "</td>\n" +
                    "\t\t\t<td>" + tourCostDiscountedRoeMap.get(tourCostDetails.getCurrency()) + "</td>\n" +
                    "\t\t\t<td>" + tourCostDiscountedRoeMap.get(tourCostDetails.getCurrency()).multiply(tourCostDetails.getBuyingAmount()) + "</td>\n" +
                    "\t\t\t<td>" + tourCostDetails.getBillTo() + "</td>\n" +
                    "\t\t\t<td>" + ((amount = personalExpenseDetails.getTravellersChequeAmount()) != null ? amount : "-") + "</td>\n" +
                    "\t\t\t<td>" + ((amount = personalExpenseDetails.getCashAmount()) != null ? amount : "-") + "</td>\n" +
                    "\t\t\t<td>" + ((amount = personalExpenseDetails.getCardAmount()) != null ? amount : "-") + "</td>\n" +
                    "\t\t\t<td>" + personalExpenseDetails.getCurrency() + "</td>\n" +
                    "\t\t\t<td>" + personalDiscountedRoeMap.get(personalExpenseDetails.getCurrency()) + "</td>\n" +
                    "\t\t\t<td>" + mInrPersonalAmount + "</td>\n" +
                    "\t\t\t<td>" + personalExpenseDetails.getDelivery() + "</td>\n" +
                    "\t\t\t<td>" + personalExpenseDetails.getBillTo() + "</td>\n" +
                    "\t\t</tr>\n";

        }
        tabularInfo += "\t\t<tr>\n" +
                "\t\t\t<td colspan=\"9\">Total</td>\n" +
                "\t\t\t<td>" + tourCostTotal + "</td>\n" +
                "\t\t\t<td>&nbsp;</td>\n" +
                "\t\t\t<td>&nbsp;</td>\n" +
                "\t\t\t<td>" + mInrTourCostTotal + "</td>\n" +
                "\t\t\t<td>&nbsp;</td>\n" +
                "\t\t\t<td>" + mTcAmount + "</td>\n" +
                "\t\t\t<td>" + mCashAmount + "</td>\n" +
                "\t\t\t<td>" + mCardAmount + "</td>\n" +
                "\t\t\t<td colspan=\"2\">&nbsp;</td>\n" +
                "\t\t\t<td>" + mInrPersonalTotal + "</td>\n" +
                "\t\t\t<td colspan=\"2\">&nbsp;</td>\n" +
                "\t\t</tr>\n" +
                "\t</tbody>\n" +
                "</table>\n";

        return tabularInfo;
    }

    private String getTableHead(IndentType indentType) {

        switch (indentType) {

            case PERSONALEXPENSE:
                return "\n" + "<table border=\"1\"><thead>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<th colspan=\"5\" rowspan=\"2\">Passenger Details</th>\n" +
                        "\t\t\t<th colspan=\"4\" rowspan=\"2\">Passport Details</th>\n" +
                        "\t\t\t<th colspan=\"5\" rowspan=\"2\">Tour Cost Details</th>\n" +
                        "\t\t\t<th colspan=\"8\">Personal Expense Details</th>\n" +
                        "\t\t</tr>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<th colspan=\"3\">Personal Exp. FCY Details</th>\n" +
                        "\t\t\t<th rowspan=\"2\">Currency</th>\n" +
                        "\t\t\t<th rowspan=\"2\">ROE</th>\n" +
                        "\t\t\t<th rowspan=\"2\">Personal Exp. INR Equivalent</th>\n" +
                        "\t\t\t<th rowspan=\"2\">Delivery</th>\n" +
                        "\t\t\t<th rowspan=\"2\">Bill To</th>\n" +
                        "\t\t</tr>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<th>Passenger Name</th>\n" +
                        "\t\t\t<th>Travelling Country</th>\n" +
                        "\t\t\t<th>Airline</th>\n" +
                        "\t\t\t<th>Departure Date</th>\n" +
                        "\t\t\t<th>Ticket No.</th>\n" +
                        "\t\t\t<th>Passport No.</th>\n" +
                        "\t\t\t<th>Place Of Issue</th>\n" +
                        "\t\t\t<th>Issuance Date</th>\n" +
                        "\t\t\t<th>Expiry Date</th>\n" +
                        "\t\t\t<th>Tour Cost FCY Component</th>\n" +
                        "\t\t\t<th>FCY Currency</th>\n" +
                        "\t\t\t<th>ROE</th>\n" +
                        "\t\t\t<th>Tour Cost INR Equivalent</th>\n" +
                        "\t\t\t<th>Bill To</th>\n" +
                        "\t\t\t<th>TC</th>\n" +
                        "\t\t\t<th>Cash</th>\n" +
                        "\t\t\t<th>Card</th>\n" +
                        "\t\t</tr>\n" +
                        "\t</thead>\n" +
                        "\t<tbody>\n";

            case TOURCOST:
                return "\n" +
                        "<table border=\"1\"><thead>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<th colspan=\"5\" >Passenger Details</th>\n" +
                        "\t\t\t<th colspan=\"4\" >Passport Details</th>\n" +
                        "\t\t\t<th colspan=\"5\" >Tour Cost Details</th>\n" +
                        "\t\t</tr>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<th>Passenger Name</th>\n" +
                        "\t\t\t<th>Travelling Country</th>\n" +
                        "\t\t\t<th>Airline</th>\n" +
                        "\t\t\t<th>Departure Date</th>\n" +
                        "\t\t\t<th>Ticket No.</th>\n" +
                        "\t\t\t<th>Passport No.</th>\n" +
                        "\t\t\t<th>Place Of Issue</th>\n" +
                        "\t\t\t<th>Issuance Date</th>\n" +
                        "\t\t\t<th>Expiry Date</th>\n" +
                        "\t\t\t<th>Tour Cost FCY Component</th>\n" +
                        "\t\t\t<th>FCY Currency</th>\n" +
                        "\t\t\t<th>ROE</th>\n" +
                        "\t\t\t<th>Tour Cost INR Equivalent</th>\n" +
                        "\t\t\t<th>Bill To</th>\n" +
                        "\t\t</tr>\n" +
                        "\t</thead>\n" +
                        "\t<tbody>\n";

            default:
                return null;
        }
    }
*/
    private Map<String, BigDecimal> getDiscountedROEMap(ForexIndent indent) {
        Map<String, BigDecimal> discountedRoeMap = new HashMap<>();
        Set<ForexBuyingCurrency> forexBuyingCcyDetails = indent.getForexBuyingCcyDetails();
        for (ForexBuyingCurrency forexBuyingCurrency : forexBuyingCcyDetails) {
            discountedRoeMap.put(forexBuyingCurrency.getBuyingCurrency(), forexBuyingCurrency.getDiscountedRoe());
        }
        return discountedRoeMap;
    }

    private String getSupplierEmailId(String supplierName) throws OperationException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("supplier.name", supplierName);
        String url = extranetAccess + jsonObject.toString() + "&select=contactInfo.contactDetails.email";
        URI toUri = UriComponentsBuilder.fromUriString(url).build().toUri();
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = mdmRestUtil.exchange(toUri, HttpMethod.GET, null, String.class);
        } catch (OperationException e) {
            throw new OperationException("Email id is not configured for Supplier " + supplierName);
        }
        String responseInString = responseEntity.getBody();
        String emailId = jsonObjectProvider.getAttributeValue(responseInString, "$.data[0].contactInfo.contactDetails.email", String.class);
        return emailId;
    }

}
