package com.coxandkings.travel.operations.controller.mailroomanddispatch;

import com.coxandkings.travel.operations.criteria.mailroomanddispatch.*;
import com.coxandkings.travel.operations.criteria.mailroomanddispatch.workflowSearchCriteria.InboundSearchCriteria;
import com.coxandkings.travel.operations.criteria.mailroomanddispatch.workflowSearchCriteria.MasterSearchCriteria;
import com.coxandkings.travel.operations.criteria.mailroomanddispatch.workflowSearchCriteria.OutboundSearchCritieria;
import com.coxandkings.travel.operations.enums.mailroomanddispatch.*;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.mailroomanddispatch.*;
import com.coxandkings.travel.operations.resource.mailroomMaster.*;
import com.coxandkings.travel.operations.service.mailroomanddispatch.MailRoomService;
import com.coxandkings.travel.operations.service.mailroomanddispatch.SupplierNamesService;
import com.coxandkings.travel.operations.service.remarks.MDMUserService;
import com.coxandkings.travel.operations.utils.Constants;
import org.dom4j.DocumentException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/mailrooms")
public class MailRoomController {

    @Autowired
    private MailRoomService mailRoomService;

    @Autowired
    MDMUserService mdmUserService;

    @Autowired
    SupplierNamesService supplierNamesService;

    @PostMapping(value="/v1/add") //WORKFLOW ADD
    public MailRoomMaster addMasterRoomElement(@RequestBody MailRoomMasterResource mailRoomMasterResource) throws OperationException {
        try {
            MailRoomMaster mailRoom = mailRoomService.save(mailRoomMasterResource);
            return mailRoom;
        } catch (Exception e) {
            //throw new OperationException(Constants.OPS_ERR_70500);
            throw e;
        }

    }

    @PostMapping(value="/v1/update") //WORKFLOW UPDATE
    public MailRoomMaster updateMasterRoomElement(@RequestBody MailRoomMasterResource mailRoomMasterResource) throws OperationException {
        try {
            MailRoomMaster mailRoom = mailRoomService.update(mailRoomMasterResource);
            return mailRoom;
        } catch (Exception e) {
           // throw new OperationException(Constants.OPS_ERR_70501);
            throw e;
        }

    }

    @PostMapping("/v1/list/searchByCriteria")
    public ResponseEntity<Map<String, Object>> getMailRoomElement(@RequestBody MailRoomSearchCriteria maiRoomCriteria) throws OperationException {
        try {
            Map<String, Object> mailRoomMaster = mailRoomService.getByCriteria(maiRoomCriteria);
            return new ResponseEntity<>(mailRoomMaster, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_70502);
        }
    }

    @PostMapping("/v1/list/sort/searchByCriteria")
    public ResponseEntity<Map<String, Object>> getMailRoomElementSorted(@RequestBody MailroomSearchCriteriaSorted mailroomSearchCriteriaSorted) throws OperationException {
        try {
            Map<String, Object> mailRoomMaster = mailRoomService.getByCriteriaSorted(mailroomSearchCriteriaSorted);
            return new ResponseEntity<>(mailRoomMaster, HttpStatus.OK);
        } catch (Exception e) {
            throw e;
            //throw new OperationException(Constants.OPS_ERR_70502);
        }
    }

    @GetMapping("/v1/detail/{mailRoomId}")
    public ResponseEntity<MailRoomMaster> getMailRoomId(@PathVariable("mailRoomId") String mailRoomId) throws OperationException {
        try {
            MailRoomMaster mailRoomMaster = mailRoomService.getId(mailRoomId);
            return new ResponseEntity<MailRoomMaster>(mailRoomMaster, HttpStatus.OK);
        } catch (Exception e) {
            //throw new OperationException(Constants.OPS_ERR_70503);
            throw e;
        }

    }

    @PostMapping("/v1/employeeDetails/add")
    public ResponseEntity<MailRoomMaster> addEmployeeDetails(@RequestBody EmployeeDetailsResource employeeDetailsResource) throws OperationException {
        MailRoomMaster mailRoomMaster = mailRoomService.addEmployeeDetails(employeeDetailsResource);
        return  new ResponseEntity<MailRoomMaster>(mailRoomMaster,HttpStatus.OK);
    }

    @GetMapping("/v1/employeeDetails/get")
    public ResponseEntity<Set<EmployeeDetails>> getEmployeeDetails(@RequestParam("mailRoomId") String mailRoomId) throws OperationException {
        Set<EmployeeDetails> employeeDetailsList = mailRoomService.getEmployeeDetails( mailRoomId);
        return  new ResponseEntity<>(employeeDetailsList,HttpStatus.OK);
    }

    @PostMapping("/v1/inbound/list/employeeNameDetails") //Employee details
    public ResponseEntity<List<EmployeeDetails>> getSpecificEmployeeDetails(@RequestBody  AutoSuggestResource autoSuggestResource) throws OperationException {
        List<EmployeeDetails> empNames = null;
        try {
            empNames = mailRoomService.getSpecificEmployeeDetails(autoSuggestResource.getEmployeeName(), autoSuggestResource.getEmployeeID());

        } catch (Exception e) {
            throw e;
        }
        return new ResponseEntity<List<EmployeeDetails>>(empNames, HttpStatus.OK);
    }

    @PostMapping("/v1/inbound/list/employeeNames") //Auto Suggest that returns name and id of the employee
    public ResponseEntity<JSONArray> getEmployeeNames(@RequestBody  AutoSuggestResource autoSuggestResource) throws OperationException {
        JSONArray empNameIdArr = new JSONArray();
        try {
            List<EmployeeDetails> empNames =  mailRoomService.getEmployeeNames(autoSuggestResource);

            Iterator<EmployeeDetails> employeeDtlsItr = empNames.iterator();
            while(employeeDtlsItr.hasNext()){
                EmployeeDetails employeeDetails = employeeDtlsItr.next();

                empNameIdArr.put(employeeDetails.toEmpNameIdJson());
            }

        } catch (Exception e) {
            throw e;
        }
        return new ResponseEntity<JSONArray>(empNameIdArr, HttpStatus.OK);
    }

    @GetMapping("/v1/details")
    public ResponseEntity<List<MailRoomMaster>> getAllMailRoomDetails() throws OperationException {
        try {
            List<MailRoomMaster> mailRoomMasterList = mailRoomService.getAllMailRoomDetails();
            return new ResponseEntity<List<MailRoomMaster>>(mailRoomMasterList, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_70504);
        }
    }

    @PostMapping("/v1/details/sorted")
    public ResponseEntity<List<MailRoomMaster>> getAllMailRoomsSorted(@RequestParam("columnName") String columnName, @RequestParam("order") String order) throws OperationException {
        try {
            List<MailRoomMaster> mailRoomMasterList = mailRoomService.getAllMailRoomsSorted(columnName,order);
            return new ResponseEntity<List<MailRoomMaster>>(mailRoomMasterList, HttpStatus.OK);
        } catch (Exception e) {
           // throw new OperationException(Constants.OPS_ERR_70504);
            throw e;
        }
    }

    @PostMapping("/v1/list/mailRoomNames") //Auto suggest
    public ResponseEntity<String> getMailRoomNames(@RequestBody  AutoSuggestResource autoSuggestResource) throws OperationException {
        List<String> mailRoomMasterNamesList = mailRoomService.getMailRoomNames(autoSuggestResource.getMailName());

        JSONArray jsonArray = new JSONArray();
        Iterator<String> namesIterator = mailRoomMasterNamesList.iterator();
        while(namesIterator.hasNext())
        {
            JSONObject mailObj = new JSONObject();
            String mailName = namesIterator.next();
            mailObj.put("mailName", mailName);
            jsonArray.put(mailObj);
        }

        return new ResponseEntity<String>(jsonArray.toString(), HttpStatus.OK);
    }

    @GetMapping("/v1/list/mailRoomNamesDrop") //Auto-suggest drop down
    public ResponseEntity<List<String>> getMailRoomNamesDrop() throws OperationException {
        List<String> mailRoomMasterNamesList = mailRoomService.getMailRooms();
        return new ResponseEntity<List<String>>(mailRoomMasterNamesList, HttpStatus.OK);
    }


    @GetMapping("/v1/inbound/list/contents") //Drop down
    public ResponseEntity<List<Enum>> getContents() throws OperationException {
        try {
            List<Enum> enumValues = new ArrayList<Enum>(EnumSet.allOf(Contents.class));
            return new ResponseEntity<List<Enum>>(enumValues, HttpStatus.OK);
        } catch (Exception e) {
            throw e;
        }
    }

    @GetMapping("/v1/inbound/list/mode") //Drop down
    public ResponseEntity<List<Enum>> getMode() throws OperationException {
        try {
            List<Enum> enumValues = new ArrayList<Enum>(EnumSet.allOf(Mode.class));
            return new ResponseEntity<List<Enum>>(enumValues, HttpStatus.OK);
        } catch (Exception e) {
            throw e;
        }
    }

    @GetMapping("/v1/inbound/list/inboundEntryStatus") //Drop down
    public ResponseEntity<List<Enum>> getInboundEntryStatus() throws OperationException {
        try {
            List<Enum> enumValues = new ArrayList<Enum>(EnumSet.allOf(InboundEntryStatus.class));
            return new ResponseEntity<List<Enum>>(enumValues, HttpStatus.OK);
        } catch (Exception e) {
            throw e;
        }
    }

    @GetMapping("/v1/inbound/list/employeeNames") //Drop down
    public ResponseEntity<Set<String>> getEmployeeNames() throws OperationException {
        try {
            Set<String> empNames = mailRoomService.getEmployeeNames();
            return new ResponseEntity<>(empNames, HttpStatus.OK);
        } catch (Exception e) {
            throw e;
        }
    }

    @GetMapping("/v1/inbound/list/employeeIDs") //Drop down
    public ResponseEntity<Set<String>> getEmployeeIDs() throws OperationException {
        try {
            Set<String> employeeIDs = mailRoomService.getEmployeeIDs();
            return new ResponseEntity<>(employeeIDs, HttpStatus.OK);
        } catch (Exception e) {
            throw e;
        }
    }

    @GetMapping("/v1/inbound/list/linkToDispatchIDs")
    public ResponseEntity<Set<String>> getlinkToDispatchIDs() throws OperationException {
        try {
            Set<String> employeeIDs = mailRoomService.getlinkToDispatchIDs();
            return new ResponseEntity<>(employeeIDs, HttpStatus.OK);
        } catch (Exception e) {
            throw e;

        }
    }

    @GetMapping("/v1/inbound/list/outboundStatus")
    public ResponseEntity<List<Enum>> getOutboundStatus() throws OperationException {
        try {
            List<Enum> enumValues = new ArrayList<Enum>(EnumSet.allOf(DispatchStatus.class));
            return new ResponseEntity<List<Enum>>(enumValues, HttpStatus.OK);
        } catch (Exception e) {
            //throw new OperationException(Constants.OPS_ERR_70505);
            throw e;
        }
    }

    @GetMapping("/v1/inbound/list/priority")
    public ResponseEntity<List<Enum>> getListOfPriority() throws OperationException {
        try {
            List<Enum> enumValues = new ArrayList<Enum>(EnumSet.allOf(InboundPriority.class));
            return new ResponseEntity<>(enumValues, HttpStatus.OK);
        } catch (Exception e) {
            //throw new OperationException(Constants.OPS_ERR_70505);
            throw e;
        }
    }


    @PostMapping("/v1/inbound/add") //WORKFLOW Add
    public ResponseEntity<InboundEntry> addInboundEntry(@RequestBody InboundEntryResource inboundReource) throws OperationException {
        try {
            InboundEntry inboundEntry = mailRoomService.saveInboundEntry(inboundReource);
            return new ResponseEntity<>(inboundEntry, HttpStatus.OK);
        } catch (Exception e) {
            //throw new OperationException(Constants.OPS_ERR_70506);
            throw e;
        }

    }

    @PostMapping("/v1/inbound/update") //WORKFLOW Update
    public ResponseEntity<InboundEntry> updateInboundEntry(@RequestBody InboundEntryResource inboundReource) throws OperationException {
        try {
            InboundEntry inboundEntry = mailRoomService.updateInboundEntry(inboundReource);
            return new ResponseEntity<>(inboundEntry, HttpStatus.OK);
        } catch (Exception e) {
            throw e;
            //throw new OperationException(Constants.OPS_ERR_70507);
        }

    }

    @PostMapping("/v1/inbound/list/searchByCriteria")
    public ResponseEntity<Map<String, Object>> getInboundEntry(@RequestBody InboundEntryCriteria inboundEntryCriteria) throws OperationException {
        try {
            Map<String, Object> inboundEntry = mailRoomService.getByInboundCriteria(inboundEntryCriteria);
            return new ResponseEntity<>(inboundEntry, HttpStatus.OK);
        } catch (Exception e) {
            throw e;
            //throw new OperationException(Constants.OPS_ERR_70508);
        }
    }

    @GetMapping("/v1/inbound/{inboundId}") //WORKFLOW getbyid Inbound
    public ResponseEntity<InboundEntry> getInboundId(@PathVariable("inboundId") String inboundId) throws OperationException {
        try {
            InboundEntry inboundEntry = mailRoomService.getInboundId(inboundId);
            return new ResponseEntity<InboundEntry>(inboundEntry, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_70509);
        }

    }

    @GetMapping("/v1/inbound/planDelivery")
    public ResponseEntity<PlanDelivery> getPlanDeliveryFromInboundNo(@RequestParam("inboundNo") String inboundNo) throws OperationException{
        try{
            PlanDelivery planDelivery = mailRoomService.getPlanDeliveryFromInboundNo(inboundNo);
            return new ResponseEntity<>(planDelivery,HttpStatus.OK);
        }catch (Exception e){
            throw new OperationException(Constants.OPS_ERR_70530);
        }
    }

    @PostMapping("/v1/inbound/planDelivery/update")
    public ResponseEntity<InboundEntry> updatePlanDeliveryFromInboundNo(@RequestParam("inboundNo") String inboundNo, @RequestBody PlanDeliveryResource planDeliveryResource ) throws OperationException{
        try{
            InboundEntry inboundEntry = mailRoomService.updatePlanDeliveryFromInboundNo(inboundNo,planDeliveryResource);
            return new ResponseEntity<>(inboundEntry,HttpStatus.OK);
        }catch (Exception e){
            throw new OperationException(Constants.OPS_ERR_70531);
        }

    }

    @PostMapping("/v1/inbound/planDelivery/addToList")
    public ResponseEntity<PlanDelivery> setPlanDeliveryToInboundNoList(@RequestBody PlanDeliverytoInboundNoListResource planDeliverytoInboundNoListResource ) throws OperationException{
        try{
            PlanDelivery planDelivery = mailRoomService.setPlanDeliverytoInboundNoList(planDeliverytoInboundNoListResource);
            return new ResponseEntity<>(planDelivery,HttpStatus.OK);
        }catch (Exception e){
            //throw new OperationException(Constants.OPS_ERR_70531);
            throw e;
        }

    }


    /*@GetMapping("/v1/inbound/commonDelivery")
    public ResponseEntity<CommonDelivery> getCommonDeliveryFromInboundNo(@RequestParam("inboundNo") String inboundNo)throws OperationException{
        try{
            CommonDelivery commonDelivery = mailRoomService.getCommonDeliveryFromInbounfNo(inboundNo);
            return new ResponseEntity<>(commonDelivery,HttpStatus.OK);
        }catch (Exception e){
           // throw new OperationException(Constants.OPS_ERR_70532);
            throw e;
        }
    }*/

    @PostMapping("/v1/inbound/commonDelivery/updateToList")
    public ResponseEntity<Set<InboundLogEntryStatus>> updateCommonDeliveryFromInboundNo(@RequestBody CommonDeliveryResource commonDeliveryResource)throws OperationException{
        try{
            Set<InboundLogEntryStatus> commonDelivery = mailRoomService.updateCommonDeliveryFromInboundNo(commonDeliveryResource);
            return new ResponseEntity<>(commonDelivery,HttpStatus.OK);
        }catch (Exception e){
            //throw new OperationException(Constants.OPS_ERR_70533);
            throw e;
        }
    }


    @PostMapping("/v1/list/inBoundNo") //Auto-suggest
    public ResponseEntity<JSONArray> getInboundNo(@RequestBody  AutoSuggestResource autoSuggestResource) {
        List<String> inboundNoList = mailRoomService.getInboundNo(autoSuggestResource.getInboundNo());

        JSONArray jsonArray = new JSONArray();
        Iterator<String> namesIterator = inboundNoList.iterator();
        while(namesIterator.hasNext())
        {
            JSONObject mailObj = new JSONObject();
            String passName = namesIterator.next();
            mailObj.put("inboundNo", passName);
            jsonArray.put(mailObj);
        }

        return new ResponseEntity<JSONArray>(jsonArray, HttpStatus.OK);

    }

    @GetMapping("/v1/list/awbNo") //Auto-suggest
    public ResponseEntity<List<String>> getAwbNo() {
        List<String> awbNoList = mailRoomService.getAwbNo();
        return new ResponseEntity<List<String>>(awbNoList, HttpStatus.OK);
    }

    @GetMapping("/v1/list/senderName") //Auto-suggest
    public ResponseEntity<List<String>> getSenderName() {
        List<String> SenderNameList = mailRoomService.getSenderName();
        return new ResponseEntity<List<String>>(SenderNameList, HttpStatus.OK);
    }

    @GetMapping("/v1/list/recipientName") //Auto-suggest
    public ResponseEntity<List<String>> getRecipientName() {
        List<String> RecipientNameList = mailRoomService.getRecipientName();
        return new ResponseEntity<List<String>>(RecipientNameList, HttpStatus.OK);
    }

    @GetMapping("/v1/list/departments") //Auto-suggest
    public ResponseEntity<List<String>> getDepartments() {
        List<String> DepartmentsList = mailRoomService.getDepartments();
        return new ResponseEntity<List<String>>(DepartmentsList, HttpStatus.OK);
    }

    @GetMapping("/v1/inbound/mdmSupplierNames") //Auto-suggest
    public ResponseEntity<String> getMdmSupplierNames() throws OperationException {
        try {
            String mdmNames = supplierNamesService.getSupplierNames();
            return  new ResponseEntity<String>(mdmNames,HttpStatus.OK);
        } catch (Exception e) {
            throw e;
            //throw new OperationException(Constants.OPS_ERR_70510);
        }
    }



    @PostMapping("/v1/outbound/add") // WORKFLOW ADD
    public OutboundDispatch addOutboundDispatch(@RequestBody OutboundDispatchResource outboundDispatchResource) throws OperationException {
        try {
            OutboundDispatch outboundDispatch = mailRoomService.saveOutboundDispatch(outboundDispatchResource);
            return outboundDispatch;
        } catch (Exception e) {
            throw e;
        }

    }

    @PostMapping("/v1/outbound/update") // WORKFLOW UPDATE
    public OutboundDispatch updateOutboundDispatch(@RequestBody OutboundDispatchResource otboundDispatchReource) throws OperationException {
        try {
            OutboundDispatch outboundDispatch = mailRoomService.updateOutboundDispatch(otboundDispatchReource);
            return outboundDispatch;
        } catch (Exception e) {
            //throw new OperationException(Constants.OPS_ERR_70512);
            throw e;
        }

    }


    @PostMapping("/v1/outbound/list/searchByCriteria")
    public ResponseEntity<Map<String, Object>> getOutBoundDispatch(@RequestBody OutBoundDispatchCriteria outBoundDispatchCriteria) throws OperationException {
        try {
            Map<String, Object> outboundDispatches = mailRoomService.getOutBoundCriteria(outBoundDispatchCriteria);
            return new ResponseEntity<>(outboundDispatches, HttpStatus.OK);
        } catch (Exception e) {
            //throw new OperationException(Constants.OPS_ERR_70513);
            throw e;
        }
    }

    @GetMapping("/v1/outbound/{dispatchId}") // WORKFLOW SEARCH BY ID
    public ResponseEntity<OutboundDispatch> getOutboundId(@PathVariable("dispatchId") String dispatchId) throws OperationException {
        try {
            OutboundDispatch outboundDispatch = mailRoomService.getOutBoundId(dispatchId);
            return new ResponseEntity<OutboundDispatch>(outboundDispatch, HttpStatus.OK);
        } catch (Exception e) {
           // throw new OperationException(Constants.OPS_ERR_70514);
            throw e;
        }

    }

    @GetMapping("/v1/outbound/list/parcel/{parcelID}")
    public ResponseEntity<List<OutboundDispatch>> getOutboundsFromParcelId(@PathVariable("parcelID") String parcelID) throws OperationException {
        try {
            List<OutboundDispatch> outboundDispatch = mailRoomService.getOutboundsFromParcelId(parcelID);
            return new ResponseEntity<>(outboundDispatch, HttpStatus.OK);
        } catch (Exception e) {
            // throw new OperationException(Constants.OPS_ERR_70514);
            throw e;
        }

    }


    @PostMapping("/v1/outbound/addParcelDispatchId")
    public ResponseEntity<Parcel> addParcelDispatchId(@RequestBody ParcelCreationForOutboundIdResource parcelCreationForOutboundIdResource) throws OperationException {
        try {
            Parcel parcel = mailRoomService.saveParcelElementForDispatchId(parcelCreationForOutboundIdResource);
            return new ResponseEntity<Parcel>(parcel, HttpStatus.OK);
        } catch (Exception e) {
            throw e;
        }
    }

    @PostMapping("/v1/outbound/addPlanCollectionDispatchId")
    public ResponseEntity<PlanCollection> addPlanCollectionDispatchId(@RequestBody PlanCollectionForOutboundIdResource planCollectionForOutboundIdResource) throws OperationException {
        try {
            PlanCollection planCollection = mailRoomService.savePlanCollectionForDispatchId(planCollectionForOutboundIdResource);
            return new ResponseEntity<>(planCollection, HttpStatus.OK);
        } catch (Exception e) {
           // throw new OperationException(Constants.OPS_ERR_70516);
            throw e;
        }
    }

    @PostMapping("/v1/list/passengarnames") //Auto suggest
    public ResponseEntity<String> getPassengarNames(@RequestBody  AutoSuggestResource autoSuggestResource) throws OperationException {
        List<String> passNamesList = mailRoomService.getPassengarNames(autoSuggestResource.getPassName());

        JSONArray jsonArray = new JSONArray();
        Iterator<String> namesIterator = passNamesList.iterator();
        while(namesIterator.hasNext())
        {
            JSONObject mailObj = new JSONObject();
            String passName = namesIterator.next();
            mailObj.put("passName", passName);
            jsonArray.put(mailObj);
        }

        return new ResponseEntity<String>(jsonArray.toString(), HttpStatus.OK);
    }

    @PostMapping("/v1/list/outbound/supplierNames") //Auto suggest
    public ResponseEntity<String> getSupplierNames(@RequestBody  AutoSuggestResource autoSuggestResource) throws OperationException {
        Set<String> supplierNames = mailRoomService.getOutBoundSupplierIDs(autoSuggestResource.getSuppID());

        JSONArray jsonArray = new JSONArray();
        Iterator<String> suppNamesItr = supplierNames.iterator();
        while(suppNamesItr.hasNext())
        {
            JSONObject outboundObj = new JSONObject();
            String suppName = suppNamesItr.next();
            outboundObj.put("suppID", suppName);
            jsonArray.put(outboundObj);
        }

        return new ResponseEntity<String>(jsonArray.toString(), HttpStatus.OK);
    }

    @PostMapping("/v1/list/outbound/passportNo") //Auto suggest
    public ResponseEntity<String> getPassportNo(@RequestBody  AutoSuggestResource autoSuggestResource) throws OperationException {
        Set<String> passportNoSet = mailRoomService.getPassportNo(autoSuggestResource.getPassportNo());

        JSONArray jsonArray = new JSONArray();
        Iterator<String> passportItr = passportNoSet.iterator();
        while(passportItr.hasNext())
        {
            JSONObject outboundObj = new JSONObject();
            String passportNo = passportItr.next();
            outboundObj.put("passportNo", passportNo);
            jsonArray.put(outboundObj);
        }

        return new ResponseEntity<String>(jsonArray.toString(), HttpStatus.OK);
    }

    @DeleteMapping("/v1/dispatch/delete/{dispatchId}")
    public void deleteDispatch(@PathVariable("dispatchId") String id) throws OperationException {
        try {
            mailRoomService.deleteDispatch(id);
        } catch (Exception e) {
            throw e;
            // throw new OperationException(Constants.OPS_ERR_70517);
        }

    }

    @PostMapping("/v1/parcel/add")
    public ResponseEntity<Parcel> addParcelElement(@RequestBody CreateParcelResource createParcelReource) throws OperationException {
        try {
            Parcel createParcel = mailRoomService.saveParcelElement(createParcelReource);
            return new ResponseEntity<Parcel>(createParcel, HttpStatus.OK);
        } catch (Exception e) {
            throw e;
            //throw new OperationException(Constants.OPS_ERR_70518);
        }

    }

    @GetMapping("/v1/parcel/list/details")
    public ResponseEntity<List<Parcel>> getParcels() throws OperationException {
        try {
            List<Parcel> createParcel = mailRoomService.getParcels();
            return new ResponseEntity<>(createParcel, HttpStatus.OK);
        } catch (Exception e) {
            throw e;
            //throw new OperationException(Constants.OPS_ERR_70518);
        }

    }

    @PostMapping("/v1/parcel/addDispatchToParcel") //Attach to parcel
    public ResponseEntity<ParcelResource> addDispatchToParcel(@RequestBody OutboundDispathToPreCreatedParcelResource outboundDispathToPreCreatedParcelResource) throws OperationException{
        try {
            ParcelResource parcelWithDispatch = mailRoomService.saveOutboundDispatchToPreCreatedParcel(outboundDispathToPreCreatedParcelResource);
            return new ResponseEntity<ParcelResource>(parcelWithDispatch, HttpStatus.OK);
        } catch (Exception e) {
            throw e;
            //throw new OperationException(Constants.OPS_ERR_70515);
        }
    }

    @PostMapping("/v1/parcel/update")
    public ResponseEntity<Parcel> updateParcelElement(@RequestBody UpdateParcelResource updateParcelResource) throws OperationException {
        try {
            Parcel updatedParcel = mailRoomService.updateParcelElement(updateParcelResource);
            return new ResponseEntity<>(updatedParcel, HttpStatus.OK);
        } catch (Exception e) {
           // throw new OperationException(Constants.OPS_ERR_70519);
            throw e;
        }

    }

    @GetMapping("/v1/parcel/dispatch/{dispatchID}")
    public ResponseEntity<Parcel> getParcelFromDispatchId(@PathVariable("dispatchID") String dispatchID) throws OperationException {
        try {
            Parcel createParcel = mailRoomService.getParcelFromDispatchId(dispatchID);
            return new ResponseEntity<Parcel>(createParcel, HttpStatus.OK);
        } catch (Exception e) {
            //throw new OperationException(Constants.OPS_ERR_70520);
            throw e;
        }

    }


    @PostMapping("/v1/parcel/searchByCriteria")
    public ResponseEntity<Parcel> getParcelId(@RequestBody SearchParcelCriteria searchParcelCriteria) throws OperationException {
        try {
            Parcel createParcel = mailRoomService.getByCriteria(searchParcelCriteria);
            return new ResponseEntity<Parcel>(createParcel, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_70520);
        }

    }

    @GetMapping("/v1/parcel/{parcelId}")
    public ResponseEntity<Parcel> getParcelId(@PathVariable("parcelId") String parcelId) throws OperationException {
        try {
            Parcel createParcel = mailRoomService.getParcelId(parcelId);
            return new ResponseEntity<Parcel>(createParcel, HttpStatus.OK);
        } catch (Exception e) {
            //throw new OperationException(Constants.OPS_ERR_70520);
            throw e;
        }

    }


    @GetMapping("/v1/parcel/list/parcelsIds")
    public ResponseEntity<List<String>> getParcelIds() throws OperationException {
        try {
            List<String> createParcel = mailRoomService.getParcelIds();
            return new ResponseEntity<>(createParcel, HttpStatus.OK);
        } catch (Exception e) {
            throw e;
            //throw new OperationException(Constants.OPS_ERR_70520);
        }

    }

    @PostMapping("/v1/parcel/outbounds/detach")
    public ResponseEntity<List<OutboundDispatch>> getOutboundsDetach(@RequestBody OutboundsDetachFromParcel outboundsDetachFromParcel) throws OperationException {
        try {
            List<OutboundDispatch> outboundDispatchList = mailRoomService.getOutboundsDetach(outboundsDetachFromParcel);
            return new ResponseEntity<>(outboundDispatchList, HttpStatus.OK);
        } catch (Exception e) {
            throw e;
            //throw new OperationException(Constants.OPS_ERR_70520);
        }

    }


    @PostMapping("/v1/suggest/searchByCriteria")
    public List<String> suggestinBoundRecipient(@RequestBody InboundRecipientCriteria criteria) throws OperationException {
        try {
            return mailRoomService.suggest("empName", criteria.getEmpName());
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_70521);
        }
    }

    @PostMapping("/v1/barcode/add")
    public BarCode addBarCode(@RequestBody BarCodeResource barCodeResource) throws OperationException {
        try {
            BarCode barCode = mailRoomService.saveBarCode(barCodeResource);
            return barCode;
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_70522);
        }

    }

    @PostMapping("/v1/barcode/update")
    public BarCode updateBarCode(@RequestBody BarCodeResource barCodeResource) throws OperationException {
        try {
            BarCode barCode = mailRoomService.saveBarCode(barCodeResource);
            return barCode;
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_70523);
        }

    }


    @GetMapping("/v1/barcode/generate/{id}")
    public ResponseEntity<String> generateBarCodeforId(@PathVariable("id") String id) throws FileNotFoundException, DocumentException, OperationException {
        try {
            String barCode = mailRoomService.generateBarCode(id);
            return new ResponseEntity<String>(barCode, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_70524);
        }

    }

    @GetMapping("/v1/barcode/{id}")
    public ResponseEntity<BarCode> getBarCodeDetails(@PathVariable("id") String id) throws OperationException {
        try {
            BarCode barCode = mailRoomService.getBarCodeId(id);
            return new ResponseEntity<BarCode>(barCode, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_70525);
        }

    }

    //----x-----------x-------------x-------------x-----------x----------Work Flow Implementations MASTER----------x--------------x-------------x-------------x---------------x--------------x-------------x

    //Save-Submit
    @RequestMapping(path = "/v1/addMailroom",method = {RequestMethod.POST },produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> saveRecord(@RequestBody MailRoomMasterResource mailRoomMasterResource,
                                             @RequestParam(value = "draft", required = false) Boolean draft) throws OperationException{

        System.out.println("Entered The add method ");
        try {

            Object mailRoom = mailRoomService.saveWorkflowRecord(mailRoomMasterResource,draft,null);
            return new ResponseEntity<>(mailRoom, HttpStatus.OK);

        }  catch (Exception e) {
//            logger.error("Exception occurred while saving record " +e);
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //For Edit
    @RequestMapping(path = "/v1/addMailroom/{id}",method = { RequestMethod.PUT},produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> editRecord(@RequestBody MailRoomMasterResource mailRoomMasterResource,
                                             @RequestParam(value = "draft", required = false) Boolean draft,
                                             @PathVariable(value="id", required = false) String id) throws OperationException{

        System.out.println("Entered The edit method ");
        try {
            Object alternateOptions = mailRoomService.saveWorkflowRecord(mailRoomMasterResource, draft, id);
            return new ResponseEntity<>(alternateOptions, HttpStatus.OK);

        }  catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/releaseLock/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> releaseMasterLock(@PathVariable("id") String id) throws OperationException{

        if (id == null) {
            throw new OperationException(Constants.ID_NULL_EMPTY);
        }
        try {
            Object alternateOptions = mailRoomService.releaseMasterLock(id);

            return new ResponseEntity<>(alternateOptions, HttpStatus.OK);

        }  catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/v1/searchById/{id}")
    public ResponseEntity<Object> getMasterRecordByID(@PathVariable("id") String id) throws OperationException {
        try {
            MailRoomMaster roomMaster = mailRoomService.getMasterRecordByID(id);

            roomMaster.set_id(id);

            return new ResponseEntity<>(roomMaster, HttpStatus.OK);

        }  catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/v1/editmailroommaster/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> EditRecord(@PathVariable("id") String id,
                                             @RequestParam(value = "workflow", required = false) Boolean workflow,
                                             @RequestParam(value = "lock", required = false) Boolean lock) throws OperationException{
        if (id == null) {
            throw new OperationException(Constants.ID_NULL_EMPTY);
        }
        try {
            Object mailRoomOptions = null;
            if(lock==null)
            {
                mailRoomOptions = mailRoomService.viewWorkflowMasterRecord(id, workflow);
            }
            else {
                mailRoomOptions = mailRoomService.EditWorkflowMasterRecord(id, workflow, lock);
            }

            return new ResponseEntity<>(mailRoomOptions, HttpStatus.OK);

        }  catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @GetMapping(value = "/v1/viewmailroommaster/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Object> viewRecord(@PathVariable("id") String id,
//                                             @RequestParam(value = "workflow", required = false) Boolean workflow) throws OperationException{
//        if (id == null) {
//            throw new OperationException(Constants.ID_NULL_EMPTY);
//        }
//        try {
//            Object alternateOptions = mailRoomService.viewWorkflowMasterRecord(id, workflow);
//
//            return new ResponseEntity<>(alternateOptions, HttpStatus.OK);
//
//        }  catch (Exception e) {
//            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @PostMapping("/v1/searchMaster")
    public HttpEntity<Object> getMasterBookingByCriteria(@RequestBody MasterSearchCriteria masterSearchCriteria) throws OperationException {

        if (masterSearchCriteria.getWorkflow()!=null && !StringUtils.isEmpty(masterSearchCriteria.getWorkflow()) && masterSearchCriteria.getWorkflow() == true) {

            Map<String, Object> workFlowRsResults;
            workFlowRsResults = mailRoomService.getMasterWorkflowList(masterSearchCriteria);
            return new ResponseEntity<>(workFlowRsResults, HttpStatus.OK);
        }else if(masterSearchCriteria.getWorkflow()==null || StringUtils.isEmpty(masterSearchCriteria.getWorkflow()) || masterSearchCriteria.getWorkflow() == false)
        {
            masterSearchCriteria.getFilter().setPageSize(masterSearchCriteria.getCount());
            masterSearchCriteria.getFilter().setPageNumber(masterSearchCriteria.getPage());
            masterSearchCriteria.getFilter().setSortCriteria(masterSearchCriteria.getSort());

            Map<String, Object> masterResults = mailRoomService.getByCriteriaSorted(masterSearchCriteria.getFilter());
            return new ResponseEntity<>(masterResults, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>( new OperationException(Constants.FAILED_TO_ACCEPT_REQUEST), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //----x-----------x-------------x-------------x-----------x----------Work Flow Implementations INBOUND----------x--------------x-------------x-------------x---------------x--------------x-------------x

    @RequestMapping(path = "/v1/addInboundEntry",method = {RequestMethod.POST },produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> saveInboundRecord(@RequestBody InboundEntryResource inboundEntryResource,
                                             @RequestParam(value = "draft", required = false) Boolean draft) throws OperationException{
        try {
            Object alternateOptions = mailRoomService.saveInboundRecord(inboundEntryResource, draft, null);
            return new ResponseEntity<>(alternateOptions, HttpStatus.OK);

        }  catch (Exception e) {
            throw e;
        }
    }

    //For Edit
    @RequestMapping(path = "/v1/addInboundEntry/{id}",method = { RequestMethod.PUT},produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> editInboundRecord(@RequestBody InboundEntryResource inboundEntryResource,
                                             @RequestParam(value = "draft", required = false) Boolean draft,
                                             @PathVariable(value="id", required = false) String id) throws OperationException{
        try {
            Object alternateOptions = mailRoomService.saveInboundRecord(inboundEntryResource, draft, id);
            return new ResponseEntity<>(alternateOptions, HttpStatus.OK);

        }  catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/v1/searchinboundentry")
    public HttpEntity<Object> getInboundBookingByCriteria(@RequestBody InboundSearchCriteria inboundSearchCriteria) throws OperationException {

        if (inboundSearchCriteria.getWorkflow()!=null && !StringUtils.isEmpty(inboundSearchCriteria.getWorkflow()) && inboundSearchCriteria.getWorkflow() == true) {

            Map<String, Object> workFlowRsResults;
            workFlowRsResults = mailRoomService.getWorkflowList(inboundSearchCriteria);
            return new ResponseEntity<>(workFlowRsResults, HttpStatus.OK);
        }else if(inboundSearchCriteria.getWorkflow()==null || StringUtils.isEmpty(inboundSearchCriteria.getWorkflow()) || inboundSearchCriteria.getWorkflow() == false)
        {
            inboundSearchCriteria.getFilter().setPageSize(inboundSearchCriteria.getCount());
            inboundSearchCriteria.getFilter().setPageNumber(inboundSearchCriteria.getPage());
            inboundSearchCriteria.getFilter().setSortCriteria(inboundSearchCriteria.getSort());

            Map<String, Object> masterResults = mailRoomService.getByInboundCriteria(inboundSearchCriteria.getFilter());
            return new ResponseEntity<>(masterResults, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>( new OperationException(Constants.FAILED_TO_ACCEPT_REQUEST), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/v1/editinboundentry/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> editInboundRecord(@PathVariable("id") String id,
                                             @RequestParam(value = "workflow", required = false) Boolean workflow,
                                             @RequestParam(value = "lock", required = false) Boolean lock) throws OperationException{
        if (id == null) {
            throw new OperationException(Constants.ID_NULL_EMPTY);
        }
        try {
            Object inboundEntry = null;

            if(lock==null)
            {
                inboundEntry = mailRoomService.viewInboundRecord(id, workflow);
            }
            else {
                inboundEntry = mailRoomService.EditInboundRecord(id, workflow, lock);
            }

            return new ResponseEntity<>(inboundEntry, HttpStatus.OK);

        }  catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/releaseInboundLock/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> releaseInboundLock(@PathVariable("id") String id) throws OperationException{

        if (id == null) {
            throw new OperationException(Constants.ID_NULL_EMPTY);
        }
        try {
            Object alternateOptions = mailRoomService.releaseLock(id);

            return new ResponseEntity<>(alternateOptions, HttpStatus.OK);

        }  catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //----x-----------x-------------x-------------x-----------x----------Work Flow Implementations OUTBOUND----------x--------------x-------------x-------------x---------------x--------------x-------------x

    @RequestMapping(path = "/v1/addOutboundEntry",method = {RequestMethod.POST },produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> saveOutboundRecord(@RequestBody OutboundDispatchResource outboundDispatchResource,
                                                    @RequestParam(value = "draft", required = false) Boolean draft) throws OperationException{
        try {
            Object alternateOptions = mailRoomService.saveOutboundRecord(outboundDispatchResource, draft, null);
            return new ResponseEntity<>(alternateOptions, HttpStatus.OK);

        }  catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //For Edit
    @RequestMapping(path = "/v1/addOutboundEntry/{id}",method = { RequestMethod.PUT},produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> editOutboundRecord(@RequestBody OutboundDispatchResource outboundDispatchResource,
                                                    @RequestParam(value = "draft", required = false) Boolean draft,
                                                    @PathVariable(value="id", required = false) String id) throws OperationException{
        try {
            Object alternateOptions = mailRoomService.saveOutboundRecord(outboundDispatchResource, draft, id);
            return new ResponseEntity<>(alternateOptions, HttpStatus.OK);

        }  catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/v1/searchOutboundEntry")
    public HttpEntity<Object> getOutboundRecordByCriteria(@RequestBody OutboundSearchCritieria outboundSearchCritieria) throws OperationException {

        if (outboundSearchCritieria.getWorkflow()!=null && !StringUtils.isEmpty(outboundSearchCritieria.getWorkflow()) && outboundSearchCritieria.getWorkflow() == true) {

            Map<String, Object> workFlowRsResults;
            workFlowRsResults = mailRoomService.getWorkflowList(outboundSearchCritieria);
            return new ResponseEntity<>(workFlowRsResults, HttpStatus.OK);
        }else if(outboundSearchCritieria.getWorkflow()==null || StringUtils.isEmpty(outboundSearchCritieria.getWorkflow()) || outboundSearchCritieria.getWorkflow() == false)
        {
            outboundSearchCritieria.getFilter().setPageSize(outboundSearchCritieria.getCount());
            outboundSearchCritieria.getFilter().setPageNumber(outboundSearchCritieria.getPage());
            outboundSearchCritieria.getFilter().setSortCriteria(outboundSearchCritieria.getSort());

            Map<String, Object> masterResults = mailRoomService.getOutBoundCriteria(outboundSearchCritieria.getFilter());
            return new ResponseEntity<>(masterResults, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>( new OperationException(Constants.FAILED_TO_ACCEPT_REQUEST), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/v1/editOutboundRecord/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> editOutboundRecord(@PathVariable("id") String id,
                                                    @RequestParam(value = "workflow", required = false) Boolean workflow,
                                                    @RequestParam(value = "lock", required = false) Boolean lock) throws OperationException{
        if (id == null) {
            throw new OperationException(Constants.ID_NULL_EMPTY);
        }
        try {
            Object inboundEntry = null;

            if(lock==null)
            {
                inboundEntry = mailRoomService.viewOutboundRecord(id, workflow);
            }
            else {
                inboundEntry = mailRoomService.EditOutboundRecord(id, workflow, lock);
            }

            return new ResponseEntity<>(inboundEntry, HttpStatus.OK);

        }
        catch(OperationException e)
        {
            throw e;
        }
        catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/releaseOutboundLock/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> realeaseOutboundLock(@PathVariable("id") String id) throws OperationException{

        if (id == null) {
            throw new OperationException(Constants.ID_NULL_EMPTY);
        }
        try {
            Object alternateOptions = mailRoomService.releaseOutboundLock(id);

            return new ResponseEntity<>(alternateOptions, HttpStatus.OK);

        }  catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/v1/test")
    public ResponseEntity<Object> test(HttpServletRequest request) throws OperationException, IOException {

        JSONTokener jsonTok = new JSONTokener(request.getInputStream());
        JSONObject reqJson = new JSONObject(jsonTok);

        System.out.println(reqJson);

        return new ResponseEntity<>(reqJson, HttpStatus.OK);
    }

}
