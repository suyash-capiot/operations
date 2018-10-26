package com.coxandkings.travel.operations.service.alternateoptions;

import java.io.IOException;
import java.text.ParseException;
import org.json.JSONArray;
import org.json.JSONObject;
import com.coxandkings.travel.operations.criteria.alternateoptions.AlternateOptionsResponseCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptionsFollowUpDetails;
import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptionsResponseDetails;
import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptionsV2;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.resource.alternateOption.AlternateOptionsFollowUpResource;
import com.coxandkings.travel.operations.resource.alternateOption.AlternateOptionsModifySearchResource;
import com.coxandkings.travel.operations.resource.alternateOption.AlternateOptionsResource;
import com.coxandkings.travel.operations.resource.email.EmailResponse;

public interface AlternateOptionsResponseService {

  JSONObject searchAlternateOptions(String bookID, String orderID,String alternateOfferProcess, AlternateOptionsModifySearchResource alternateOptionsModifySearchResource) throws OperationException;
  
  EmailResponse sendAnEmailToClientOrCustomer(OpsBooking opsBooking, JSONObject finalBESearchResponse, String destination ,String productName,String emailId, String name) throws IOException, ParseException, OperationException;
  
  Object viewAlternateOptionsSent(AlternateOptionsResponseCriteria alternateOptionsResponseCriteria) throws OperationException;
  
  JSONObject getAlternateOptionsSentById(String id) throws OperationException;
  
  //Modify Search Urls
  String modifySearch(AlternateOptionsModifySearchResource alternateOptionsModifySearchResource, OpsBooking opsFailedBooking , OpsProduct product) throws OperationException;
  
  JSONObject modifySearchPDPPage(String bookID, String orderID, JSONObject pdpPageRequest) throws OperationException;
  
  //Follow Up URLs
  JSONObject saveFollowUpSent(AlternateOptionsFollowUpResource alternateOptionsFollowUpResource) throws OperationException;
  
  JSONArray followUpAutoSuggest(AlternateOptionsFollowUpResource alternateOptionsFollowUpResource) throws OperationException;
}
