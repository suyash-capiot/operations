package com.coxandkings.travel.operations.controller.alternateoptions;

import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.coxandkings.travel.operations.resource.alternateOption.AlternateOptionResponse;
import com.coxandkings.travel.operations.resource.alternateOption.AlternateOptionsFollowUpResource;
import com.coxandkings.travel.operations.resource.alternateOption.AlternateOptionsModifySearchResource;
import com.coxandkings.travel.operations.resource.alternateOption.AlternateOptionsResource;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.resource.forex.ForexBookingResource;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.coxandkings.travel.ext.model.be.BookingActionConstants;
import com.coxandkings.travel.operations.consumer.listners.impl.AlternateOptionsBookingListenerImpl;
import com.coxandkings.travel.operations.consumer.listners.impl.ServiceOrderAndSupplierLiabilityListener;
import com.coxandkings.travel.operations.criteria.alternateoptions.AlternateOptionsCriteria;
import com.coxandkings.travel.operations.criteria.alternateoptions.AlternateOptionsResponseCriteria;
import com.coxandkings.travel.operations.criteria.alternateoptions.SearchCriteria;
import com.coxandkings.travel.operations.criteria.forex.ForexCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptions;
import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptionsResponseDetails;
import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptionsV2;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.forex.ForexBooking;
import com.coxandkings.travel.operations.service.alternateoptions.AlternateOptionsResponseService;
import com.coxandkings.travel.operations.service.alternateoptions.AlternateOptionsService;
import com.coxandkings.travel.operations.service.alternateoptions.AlternateOptionsServiceV2;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.forex.ForexBookingService;
import com.coxandkings.travel.operations.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/alternateOptions")
@CrossOrigin(origins="*")
public class AlternateOptionsController {

  private Logger logger = Logger.getLogger(AlternateOptionsService.class);
  
    @Autowired
    private AlternateOptionsService alternateOptionsService;
    
    @Autowired
    private AlternateOptionsServiceV2 alternateOptionsServiceV2;
    
    @Autowired
    private AlternateOptionsResponseService alternateOptionsResponseService;
    
    @Autowired
    private OpsBookingService opsBookingService;
    
    @Autowired
    private ApplicationContext context;

    /*@PostMapping(path = "/v1/addalternateoptions",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addAlternateOptions(InputStream req) throws OperationException, ParseException {
        JSONTokener jsonTok = new JSONTokener(req);
        JSONObject reqJson = new JSONObject(jsonTok);
        String  response = alternateOptionsService.addAlternateOptions(reqJson);
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }
*/
    /*@PostMapping(path = "/v1/searchalternateoptions",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AlternateOptionResponse> searchAlternateOptions(InputStream req) throws OperationException, ParseException {
        JSONTokener jsonTok = new JSONTokener(req);
        JSONObject reqJson = new JSONObject(jsonTok);
        AlternateOptionResponse alternateOptionResponse = alternateOptionsService.searchByCriteria(reqJson);
        return new ResponseEntity<AlternateOptionResponse>(alternateOptionResponse, HttpStatus.OK);
    }

    @GetMapping(path = "/v1/fetchAlternateOptions/{configurationId}", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<String> fetchAlternateOptions(@PathVariable("configurationId")String configurationId){
        String res = alternateOptionsService.fetchAlternateOptions(configurationId);
        return new ResponseEntity<String>(res, HttpStatus.OK);
    }*/

    @PostMapping(path = "/v1/approve")
    public Map approve(@RequestParam String configurationId, @RequestParam(required = false) String remarks){
        return alternateOptionsService.approve(configurationId,remarks);
    }

    @PostMapping(path = "/v1/reject")
    public Map reject(@RequestParam String configurationId,@RequestParam String remarks){
        return alternateOptionsService.reject(configurationId,remarks);
    }

    //----------------------------------WORKFLOW IMPLEMENTATIONS---------------------------------------
    
    @RequestMapping(path = "/v1/addalternateoptions",method = {RequestMethod.POST },produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> saveRecord(@RequestBody AlternateOptionsResource alternateOptionResource,
                                           @RequestParam(value = "draft", required = false) Boolean draft) throws OperationException{
      
      System.out.println("Entered The add method ");
        try {
            Object alternateOptions = alternateOptionsServiceV2.saveRecord(alternateOptionResource, draft, null);
            return new ResponseEntity<>(alternateOptions, HttpStatus.OK);

        }  catch (OperationException e) {
            logger.error("Exception occurred while saving record " +e);
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    //For Edit
    @RequestMapping(path = "/v1/addalternateoptions/{id}",method = { RequestMethod.PUT},produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> editRecord(@RequestBody AlternateOptionsResource alternateOptionResource,
                                           @RequestParam(value = "draft", required = false) Boolean draft,
                                           @PathVariable(value="id", required = false) String id) throws OperationException{
      
      System.out.println("Entered The edit method ");
        try {
            Object alternateOptions = alternateOptionsServiceV2.saveRecord(alternateOptionResource, draft, id);
            return new ResponseEntity<>(alternateOptions, HttpStatus.OK);

        }  catch (OperationException e) {
            logger.error("Exception occurred while saving record " +e);
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping(path = "/v1/add",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> saveMasterRecord(@RequestBody AlternateOptionsResource alternateOptionResource) throws OperationException{
        try {
            AlternateOptionsV2 alternateOptions = alternateOptionsServiceV2.saveMasterRecord(alternateOptionResource);
            return new ResponseEntity<>(alternateOptions, HttpStatus.OK);

        }  catch (Exception e) {
            logger.error("Exception occurred while saving record " +e);
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping(value = "/v1/searchById/{id}")
    public ResponseEntity<Object> getMasterRecordByID(@PathVariable("id") String id) throws OperationException {
        try {
          AlternateOptionsV2 alternateOptions = alternateOptionsServiceV2.getMasterRecordByID(id);
          return new ResponseEntity<>(alternateOptions, HttpStatus.OK);
  
        }  catch (OperationException e) {
          logger.error("Exception occurred while saving record " +e);
          return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping(path = "/v1/update/{id}",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateMasterRecord(@PathVariable("id") String id,
                                                     @RequestBody AlternateOptionsResource alternateOptionResource) throws OperationException{
        try {
            AlternateOptionsV2 alternateOptions = alternateOptionsServiceV2.UpdateMasterRecord(id,alternateOptionResource);
            return new ResponseEntity<>(alternateOptions, HttpStatus.OK);

        }  catch (OperationException e) {
            logger.error("Exception occurred while updating record " +e);
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/v1/searchalternateoptions")
    public HttpEntity<Object> getBookingByCriteria(@RequestBody SearchCriteria searchCriteria) throws OperationException {

        if (searchCriteria.getWorkflow()!=null && !StringUtils.isEmpty(searchCriteria.getWorkflow()) && searchCriteria.getWorkflow() == true) {

            Map<String, Object> workFlowRsResults;
            workFlowRsResults = alternateOptionsServiceV2.getWorkflowList(searchCriteria);
            return new ResponseEntity<>(workFlowRsResults, HttpStatus.OK);
        }else if(searchCriteria.getWorkflow()==null || StringUtils.isEmpty(searchCriteria.getWorkflow()) || searchCriteria.getWorkflow() == false)
        {
          Map<String, Object> masterResults = alternateOptionsServiceV2.getByCriteriaFromMaster(searchCriteria);
          return new ResponseEntity<>(masterResults, HttpStatus.OK);
        }
        else
        {
          logger.error("Exception occurred while retrieving the record ");
          return new ResponseEntity<>( new OperationException(Constants.FAILED_TO_ACCEPT_REQUEST), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    
    @GetMapping(value = "/v1/editalternateoptions/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> EditRecord(@PathVariable("id") String id,
                                           @RequestParam(value = "workflow", required = false) Boolean workflow,
                                           @RequestParam(value = "lock", required = false) Boolean lock) throws OperationException{
        if (id == null) {
            throw new OperationException(Constants.ID_NULL_EMPTY);
        }
        try {
          Object alternateOptions = null;
          
          if(lock==null)
          {
            alternateOptions = alternateOptionsServiceV2.viewRecord(id, workflow);
          }
          else {
            alternateOptions = alternateOptionsServiceV2.EditRecord(id, workflow, lock);
          }
          
            return new ResponseEntity<>(alternateOptions, HttpStatus.OK);

        }  catch (OperationException e) {
            logger.error("Exception occurred while updating record " +e);
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    //depricated - only one url for View and Edit
   /* @GetMapping(value = "/v1/viewalternateoptions/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> viewRecord(@PathVariable("id") String id,
                                           @RequestParam(value = "workflow", required = false) Boolean workflow) throws OperationException{
        if (id == null) {
            throw new OperationException(Constants.ID_NULL_EMPTY);
        }
        try {
            Object alternateOptions = alternateOptionsServiceV2.viewRecord(id, workflow);
          
            return new ResponseEntity<>(alternateOptions, HttpStatus.OK);

        }  catch (Exception e) {
            logger.error("Exception occurred while updating record " +e);
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/
    
    @RequestMapping(value = "/releaseLock/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> releaseLock(@PathVariable("id") String id) throws OperationException{

      if (id == null) {
        throw new OperationException(Constants.ID_NULL_EMPTY);
      }
      try {
          Object alternateOptions = alternateOptionsServiceV2.releaseLock(id);
        
          return new ResponseEntity<>(alternateOptions, HttpStatus.OK);
  
      }  catch (OperationException e) {
          logger.error("Exception occurred while updating record.Cannot Release Lock" + e);
          return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
    
  //----------------------------------FUNCTIONALITY IMPLEMENTATIONS---------------------------------------
    
    @PostMapping(value = "/generateAlternateOptions", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> generateAlternateOptions(InputStream kafka) throws OperationException, IOException {

      ObjectMapper mapper = new ObjectMapper();
      KafkaBookingMessage kafkaRequest = mapper.readValue(new JSONObject(new JSONTokener(kafka)).toString(), KafkaBookingMessage.class);

      if (kafkaRequest.getActionType() != null && kafkaRequest.getActionType().equalsIgnoreCase(BookingActionConstants.JSON_PROP_ERROR_BOOKING)) 
      {
        OpsBooking opsBooking = opsBookingService.getBooking(kafkaRequest.getBookId());
        try {
          AlternateOptionsBookingListenerImpl alternateOptionsBookingListener = context.getBean(AlternateOptionsBookingListenerImpl.class);
          
          alternateOptionsBookingListener.processBooking(opsBooking, kafkaRequest);
        } catch (Exception e) {
            logger.error("Error occured in processing message for AlternateOptionsBookingListener", e);
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Noper", HttpStatus.INTERNAL_SERVER_ERROR);
      }
      else
      {
        logger.error("Error occurred while processing message for Booking Notifications");
        return new ResponseEntity<>(new OperationException("Error occurred while processing message for Booking Notifications"), HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
    
    @GetMapping(value = "/v1/searchAlternateOptions/bookings/{bookId}/orders/{orderId}")
    public ResponseEntity<Object> searchAlternateOptions(@PathVariable("bookId") String bookId,
                                                         @PathVariable("orderId") String orderId) throws ParseException, OperationException, IOException 
    {
      
      if (bookId == null || orderId == null) {
        throw new OperationException(Constants.ID_NULL_EMPTY);
    }
    try {
      Object alternateOptions = null;
      
      alternateOptions = alternateOptionsResponseService.searchAlternateOptions(bookId, orderId, "Manual",null);
      
      return new ResponseEntity<>(alternateOptions, HttpStatus.OK);

      }  catch (OperationException e) {
          logger.error("Exception occurred while searching for alternate options for BookId " +bookId);
          return new ResponseEntity<>(new OperationException("Exception occurred while searching for alternate options for BookId " +bookId), HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
    
    /*@GetMapping(value = "/v1/sendEmailToClientOrCustomer/bookings/{bookingRefId}/orders/{orderId}/productSubType/{productCategorySubTypeValue}")
    public ResponseEntity<EmailResponse> sendAnEmailToClientOrCustomer(@PathVariable("bookingRefId") String bookingRefId,
                                                                       @PathVariable("orderId") String orderId,
                                                                       @PathVariable("productCategorySubTypeValue") String productCategorySubTypeValue) throws ParseException, OperationException, IOException {
        return new ResponseEntity<EmailResponse>(productBookedThrOtherService.sendAnEmailToClientOrCustomer(bookingRefId, orderId, productCategorySubTypeValue), HttpStatus.OK);
    }*/
    
    @PostMapping(value = "/v1/viewAlternateOptionsSent")
    public ResponseEntity<Object> viewAlternateOptionsSent(@RequestBody AlternateOptionsResponseCriteria alternateOptionsResponseCriteria) throws ParseException, OperationException, IOException 
    {
      try {
      Object alternateOptionsResponseSent = null;
      
      alternateOptionsResponseSent = alternateOptionsResponseService.viewAlternateOptionsSent(alternateOptionsResponseCriteria);
      
      return new ResponseEntity<>(alternateOptionsResponseSent, HttpStatus.OK);

      }  catch (Exception e) {
          logger.error("Exception occurred while searching for alternate options for BookId " +alternateOptionsResponseCriteria.getBookID());
          return new ResponseEntity<>(new OperationException("Exception occurred while searching for alternate options for BookId " +alternateOptionsResponseCriteria.getBookID()), HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
    
    @GetMapping(value = "/getAlternateOptionsSentById/{id}")
    public ResponseEntity<Object> getAlternateOptionsSentById(@PathVariable("id") String id) throws OperationException {
        try {
          JSONObject alternateOptionsResponseDetails = alternateOptionsResponseService.getAlternateOptionsSentById(id);
          return new ResponseEntity<>(alternateOptionsResponseDetails, HttpStatus.OK);
  
        }  catch (Exception e) {
          logger.error("Exception occurred while saving record " +e);
          return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping(value = "/v1/modifyAlternateOptions/bookings/{bookId}/orders/{orderId}")
    public ResponseEntity<Object> modifyAlternateOptions(@PathVariable("bookId") String bookId,
                                                         @PathVariable("orderId") String orderId,
                                                         @RequestBody AlternateOptionsModifySearchResource alternateOptionsModifySearchResource) throws ParseException, OperationException, IOException 
    {
      
      if (bookId == null || orderId == null) {
        throw new OperationException(Constants.ID_NULL_EMPTY);
    }
    try {
      Object alternateOptions = null;
      
      alternateOptions = alternateOptionsResponseService.searchAlternateOptions(bookId, orderId, "Modify",alternateOptionsModifySearchResource);
      
      return new ResponseEntity<>(alternateOptions, HttpStatus.OK);

      }  catch (OperationException e) {
          logger.error("Exception occurred while searching for alternate options for BookId " +bookId);
          return new ResponseEntity<>(new OperationException("Exception occurred while searching for alternate options for BookId " +bookId), HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
    
    @PostMapping(value = "/v1/modifySearchPDPPage/bookings/{bookId}/orders/{orderId}")
    public ResponseEntity<Object> modifySearchPDPPage(@PathVariable("bookId") String bookId,
                                                         @PathVariable("orderId") String orderId,
                                                         InputStream req) throws OperationException, ParseException {
        JSONTokener jsonTok = new JSONTokener(req);
        JSONObject reqJson = new JSONObject(jsonTok);
        
        if (bookId == null || orderId == null) {
          throw new OperationException(Constants.ID_NULL_EMPTY);
      }
      try {
        Object alternateOptions = null;
        
        alternateOptions = alternateOptionsResponseService.modifySearchPDPPage(bookId, orderId, reqJson);
        
        return new ResponseEntity<>(alternateOptions, HttpStatus.OK);

        }  catch (OperationException e) {
            logger.error("Exception occurred while searching for alternate options for BookId " +bookId);
            return new ResponseEntity<>(new OperationException("Exception occurred while searching for alternate options for BookId " +bookId), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping(value = "/v1/followUpAlternateOptionsSent")
    public ResponseEntity<Object> followUpAlternateOptionsSent(@RequestBody AlternateOptionsFollowUpResource alternateOptionsFollowUpResource) throws ParseException, OperationException, IOException 
    {
      System.out.println("Entered the controller");
      try {
      Object alternateOptionsResponseSent = null;
      
      alternateOptionsResponseSent = alternateOptionsResponseService.saveFollowUpSent(alternateOptionsFollowUpResource);
      
      return new ResponseEntity<>(alternateOptionsResponseSent, HttpStatus.OK);

      }  catch (Exception e) {
          logger.error("Exception occurred while searching for alternate options for BookId " +alternateOptionsFollowUpResource.getBookID());
          return new ResponseEntity<>(new OperationException("Exception occurred while searching for alternate options for BookId " +alternateOptionsFollowUpResource.getBookID()), HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
    
    @PostMapping(value = "/v1/followUpAutoSuggest")
    public ResponseEntity<Object> followUpAutoSuggest(@RequestBody AlternateOptionsFollowUpResource alternateOptionsFollowUpResource) throws ParseException, OperationException, IOException 
    {
      System.out.println("Entered the controller");
      try {
      Object alternateOptionsResponseSent = null;
      
      alternateOptionsResponseSent = alternateOptionsResponseService.followUpAutoSuggest(alternateOptionsFollowUpResource);
      
      return new ResponseEntity<>(alternateOptionsResponseSent, HttpStatus.OK);

      }  catch (OperationException e) {
          logger.error("Exception occurred while searching for Ops User Role " +alternateOptionsFollowUpResource.getBookID());
          return new ResponseEntity<>(new OperationException("Exception occurred while searching for Ops User Role " +alternateOptionsFollowUpResource.getBookID()), HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
    
}
