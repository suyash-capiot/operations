package com.coxandkings.travel.operations.service.alternateoptions.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.coxandkings.travel.operations.criteria.alternateoptions.AlternateOptionsCriteria;
import com.coxandkings.travel.operations.criteria.alternateoptions.AlternateOptionsResponseCriteria;
import com.coxandkings.travel.operations.criteria.alternateoptions.SearchCriteria;
import com.coxandkings.travel.operations.enums.common.MDMClientType;
import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptionsFollowUpDetails;
import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptionsReOccurrence;
import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptionsResponseDetails;
import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptionsV2;
import com.coxandkings.travel.operations.model.core.OpsAccommodationPaxInfo;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsFlightSegment;
import com.coxandkings.travel.operations.model.core.OpsOriginDestinationOption;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.core.OpsRoom;
import com.coxandkings.travel.operations.model.productbookedthrother.Accomodation;
import com.coxandkings.travel.operations.model.productbookedthrother.Bus;
import com.coxandkings.travel.operations.model.productbookedthrother.Flight;
import com.coxandkings.travel.operations.model.productbookedthrother.Train;
import com.coxandkings.travel.operations.repository.alternateoptions.AlternateOptionsFollowUpRepository;
import com.coxandkings.travel.operations.repository.alternateoptions.AlternateOptionsRepositoryV2;
import com.coxandkings.travel.operations.repository.alternateoptions.AlternateOptionsResponseRepository;
import com.coxandkings.travel.operations.resource.alternateOption.AlternateOptionsFollowUpResource;
import com.coxandkings.travel.operations.resource.alternateOption.AlternateOptionsModifySearchResource;
import com.coxandkings.travel.operations.resource.alternateOption.AlternateOptionsProductDetailsResource;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.service.alternateoptions.AlternateOptionsResponseService;
import com.coxandkings.travel.operations.service.alternateoptions.AlternateOptionsServiceV2;
import com.coxandkings.travel.operations.service.beconsumer.acco.AccoBookingEngineConsumptionService;
import com.coxandkings.travel.operations.service.beconsumer.activities.ActivitiesBookingEngineConsumptionService;
import com.coxandkings.travel.operations.service.beconsumer.air.AirBookingEngineConsumptionService;
import com.coxandkings.travel.operations.service.beconsumer.holidays.HolidaysBookingEngineConsumptionService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.mdmservice.ClientMasterDataService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.EmailUtils;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AlternateOptionsResponseServiceImpl implements AlternateOptionsResponseService {

  @Autowired
  private OpsBookingService opsBookingService;
  
  @Autowired
  private AlternateOptionsServiceV2 alternateOptionsServiceV2;
  
  @Autowired
  private AlternateOptionsRepositoryV2 alternateOptionsRepository;
  
  @Autowired
  private AlternateOptionsResponseRepository alternateOptionsResponseRepository;
  
  @Autowired
  private AlternateOptionsFollowUpRepository alternateOptionsFollowUpRepository;
  
  @Autowired
  private AirBookingEngineConsumptionService airBookingEngineConsumptionService;
  
  @Autowired
  private AccoBookingEngineConsumptionService accoBookingEngineConsumptionService;
  
  @Autowired
  private ActivitiesBookingEngineConsumptionService activitiesBookingEngineConsumptionService;
  
  @Autowired
  private HolidaysBookingEngineConsumptionService holidaysBookingEngineConsumptionService;
  
  @Value("${booking-engine-core-services.air.search}")
  private String airSearchURL;
  
  @Value("${booking-engine-core-services.air.price}")
  private String airPriceURL;
  
  @Value("${booking-engine-core-services.acco.search}")
  private String accoSearchURL;
  
  @Value("${booking-engine-core-services.acco.price}")
  private String accoPriceURL;
  
  @Autowired
  private EmailUtils emailUtils;
  
  @Autowired
  private ClientMasterDataService clientMasterDataService;
  
   @Value("${alternate-options.template_config.function}")
   private String function;

   @Value("${alternate-options.template_config.scenario}")
   private String scenario;

   @Value("${alternate-options.template_config.subject}")
   private String subject;
   
   @Value("${alternate-options.dynamic_variables.product_name}")
   private String clientProductName;

   @Value("${alternate-options.dynamic_variables.first_name}")
   private String clientFirstName;

   @Value("${alternate-options.dynamic_variables.destination_name}")
   private String clientDestinationName;
   
   @Value("${alternate-options.dynamic_variables.review_url}")
   private String wemURL;
   
   @Value(value = "${mdm.common.get.filter-based-roles}")
   private String mdmUserRolesUrl;
   
   @Autowired
   private MDMRestUtils mdmRestUtils;
   
   private Logger logger = Logger.getLogger(AlternateOptionsResponseService.class);
   
   private static ObjectMapper objectMapper = new ObjectMapper();
   
   @Autowired
   private UserService userService;
   
   int check=0;
  
  @Override
  public JSONObject searchAlternateOptions(String bookID, String orderID, String alternateOfferProcess, AlternateOptionsModifySearchResource alternateOptionsModifySearchResource)throws OperationException 
  {
    
    try {
      
      JSONObject finalBESearchResponse = new JSONObject();
      OpsBooking opsFailedBooking = opsBookingService.getBooking(bookID);
      
      /** Create request for search Alternate Options Configuration in DB */
      SearchCriteria alternateConfigRequest = new SearchCriteria();
      
      AlternateOptionsCriteria alternateConfigResource = new AlternateOptionsCriteria();
      
      alternateConfigResource.setCompanyMarket(opsFailedBooking.getClientMarket());
      alternateConfigResource.setClientType(opsFailedBooking.getClientType());

      System.out.println("CompanyMarket : "+opsFailedBooking.getClientMarket() );
      System.out.println("ClientType : "+  opsFailedBooking.getClientType());
      
      OpsProduct product = null;
      
      Set<AlternateOptionsProductDetailsResource> productDetails = new HashSet<>();
      
      List<OpsProduct> opsFailedBookingProducts = opsFailedBooking.getProducts();
      
      for(int i=0;i<opsFailedBookingProducts.size();i++) 
      {
          if(orderID.equals(opsFailedBooking.getProducts().get(i).getOrderID())) 
          {
              product = opsFailedBooking.getProducts().get(i);
              
              alternateConfigResource.setProductCategory(product.getProductCategory() );
              alternateConfigResource.setProductCategorySubType(product.getProductSubCategory());
              
              System.out.println("productCategory : "+product.getProductCategory() );
              System.out.println("productCategorySubType : "+  product.getProductSubCategory());
              
              break;
          }
      }
      
      alternateConfigRequest.setFilter(alternateConfigResource);
      alternateConfigRequest.setWorkflow(false);
      
      /** Search alternateOption Configuration for Booking configuration*/
      Map<String, Object> alternateOptionConfigResponse = alternateOptionsServiceV2.getByCriteriaFromMaster(alternateConfigRequest);

      String beSearchResponse = null;
      JSONObject alternateOptionResponse = new JSONObject();
      
      if(alternateOptionConfigResponse != null && !alternateOptionConfigResponse.isEmpty()) {
          
          System.out.println("before test");
          String productCategory = alternateConfigResource.getProductCategory();
          
          List<AlternateOptionsV2> alternateOptionsRecords = (List<AlternateOptionsV2>) alternateOptionConfigResponse.get("data");
          
          //Selecting Alternate Option Based on Process
          AlternateOptionsV2 alternateOptionRecord = new AlternateOptionsV2();
          for(int i=0;i<alternateOptionsRecords.size();i++)
          {
            if(alternateOptionsRecords.get(i).getAlternateOfferProcess().equalsIgnoreCase(alternateOfferProcess))
            {
              alternateOptionRecord = alternateOptionsRecords.get(i);
              break;
            }
            else if(alternateOfferProcess.equalsIgnoreCase("Modify") && alternateOptionsRecords.get(i).getAlternateOfferProcess().equalsIgnoreCase("Manual"))
            {
              alternateOptionRecord = alternateOptionsRecords.get(i);
              
              if(alternateOptionsModifySearchResource.getHigherLimitThreshold()!=null && !alternateOptionsModifySearchResource.getHigherLimitThreshold().isEmpty())
                alternateOptionRecord.setHigherLimitThreshold(alternateOptionsModifySearchResource.getHigherLimitThreshold());
              
              if(alternateOptionsModifySearchResource.getLowerLimitThreshold()!=null && !alternateOptionsModifySearchResource.getLowerLimitThreshold().isEmpty())
                alternateOptionRecord.setLowerLimitThreshold(alternateOptionsModifySearchResource.getLowerLimitThreshold());
              break;
            }
          }
          
          if(alternateOptionRecord.getConfigurationId()==null)
            throw new OperationException("No Alternate Option Configuration found for failed booking parameters");
          
          String higherLimitThreshold = alternateOptionRecord.getHigherLimitThreshold();
          String lowerLimitThreshold = alternateOptionRecord.getLowerLimitThreshold();
          
          System.out.println(higherLimitThreshold);
          System.out.println(lowerLimitThreshold);
          
          if (product.getProductCategory().equals(OpsProductCategory.PRODUCT_CATEGORY_TRANSPORTATION.getCategory())
                  && product.getProductSubCategory().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory())) 
          {
              /** Called Air Search */
              if(alternateOfferProcess.equalsIgnoreCase("Modify"))
                beSearchResponse = modifySearch(alternateOptionsModifySearchResource, opsFailedBooking, product);
              else
                beSearchResponse = airBookingEngineConsumptionService.search(opsFailedBooking, product);
              
              if(beSearchResponse==null || beSearchResponse.length()==0)
              {
                logger.warn("No Results found for given parameters");
                throw new OperationException(Constants.NO_RESULT_FOUND);
              }
              
              System.out.println(beSearchResponse);

              /** Data filtered on the basis of Higher and Lower limit */
              finalBESearchResponse = getFilteredAirSearchResponseAndSave(bookID, orderID,product,alternateOptionRecord ,beSearchResponse);

              /** Notifying Customer with Email about Alternate option */
              int odoSize = product.getOrderDetails().getFlightDetails().getOriginDestinationOptions().size()-1;
              int flightSegmentSize = product.getOrderDetails().getFlightDetails().getOriginDestinationOptions().get(odoSize).getFlightSegment().size()-1;
              String destination = product.getOrderDetails().getFlightDetails().getOriginDestinationOptions().get(odoSize).getFlightSegment().get(flightSegmentSize).getDestinationLocation();
              
              String firstName = product.getOrderDetails().getFlightDetails().getPaxInfo().stream().filter(x-> x.getLeadPax()).findFirst().get().getFirstName();
              String lastName = product.getOrderDetails().getFlightDetails().getPaxInfo().stream().filter(x-> x.getLeadPax()).findFirst().get().getLastName();
              String title = product.getOrderDetails().getFlightDetails().getPaxInfo().stream().filter(x-> x.getLeadPax()).findFirst().get().getTitle();
              
              String leadPaxName = title.concat(" ").concat(firstName).concat(" ").concat(lastName);
              
              String email = product.getOrderDetails().getFlightDetails().getPaxInfo().stream().filter(x -> x.getLeadPax()).findFirst().get().getContactDetails().get(0).getContactInfo().getEmail();
              String productName = "";
              
              for( OpsOriginDestinationOption odo : product.getOrderDetails().getFlightDetails().getOriginDestinationOptions())
              {
                for( OpsFlightSegment fs : odo.getFlightSegment())
                {
                  if(fs.getOperatingAirline().getAirlineName()!=null && !fs.getOperatingAirline().getAirlineName().isEmpty())
                    if(productName.isEmpty())
                      productName = productName.concat(fs.getOperatingAirline().getAirlineName());
                    else
                      productName = productName.concat(", ").concat(fs.getOperatingAirline().getAirlineName());
                }
              }
              
              sendAnEmailToClientOrCustomer(opsFailedBooking, finalBESearchResponse, destination, productName, email, leadPaxName);
              
              
           } else if (product.getProductCategory().equals(OpsProductCategory.PRODUCT_CATEGORY_ACCOMMODATION.getCategory())) 
           {
             /** Called Acco Search */
             if(alternateOfferProcess.equalsIgnoreCase("Modify"))
               beSearchResponse = modifySearch(alternateOptionsModifySearchResource, opsFailedBooking, product);
             else
               beSearchResponse = accoBookingEngineConsumptionService.search(opsFailedBooking, product);
             
             if(beSearchResponse==null || beSearchResponse.length()==0)
             {
               logger.warn("No Results found for given parameters");
               throw new OperationException(Constants.NO_RESULT_FOUND);
             }
             
             System.out.println(beSearchResponse);

             /** Data filtered on the basis of Higher and Lower limit */
             finalBESearchResponse = getFilteredAccoSearchResponseAndSave(bookID, orderID,product,alternateOptionRecord ,beSearchResponse);

             /** Notifying Customer with Email about Alternate option */
             String destination = product.getOrderDetails().getHotelDetails().getCityName();
             
             String firstName = product.getOrderDetails().getHotelDetails().getRooms().get(0).getPaxInfo().stream().filter(x-> x.getLeadPax()).findFirst().get().getFirstName();
             String lastName = product.getOrderDetails().getHotelDetails().getRooms().get(0).getPaxInfo().stream().filter(x-> x.getLeadPax()).findFirst().get().getLastName();
             String title = product.getOrderDetails().getHotelDetails().getRooms().get(0).getPaxInfo().stream().filter(x-> x.getLeadPax()).findFirst().get().getTitle();
             
             String leadPaxName = title.concat(" ").concat(firstName).concat(" ").concat(lastName);
             
             String email = product.getOrderDetails().getHotelDetails().getRooms().get(0).getPaxInfo().stream().filter(x-> x.getLeadPax()).findFirst().get().getContactDetails().get(0).getEmail();
             String productName = product.getOrderDetails().getHotelDetails().getHotelName();
             
             sendAnEmailToClientOrCustomer(opsFailedBooking, finalBESearchResponse, destination, productName, email, leadPaxName);
             
           } 
           else if(product.getProductCategory().equals(OpsProductCategory.PRODUCT_CATEGORY_ACTIVITIES.getCategory())) {}
           else if(product.getProductCategory().equals(OpsProductCategory.PRODUCT_CATEGORY_HOLIDAYS.getCategory())) { }
          
          System.out.println(finalBESearchResponse);
          
          if(finalBESearchResponse==null || finalBESearchResponse.length()==0)
          {
            logger.warn("No Results found for given parameters");
            throw new OperationException(Constants.NO_RESULT_FOUND);
          }
          
          return finalBESearchResponse;

      } else {
        logger.warn("No Alternate Option Configuration found for failed booking parameters");
        throw new OperationException("No Alternate Option Configuration found for failed booking parameters");
      }
  
  } catch (Exception e) {
    e.printStackTrace();
    throw new OperationException(Constants.TECHNICAL_ISSUE_BOOKING_FIND);
  }

  }

   
  private JSONObject getFilteredAirSearchResponseAndSave(String bookID, String orderID, OpsProduct product, AlternateOptionsV2 alternateOptionConfiguration, String beSearchResponse) {
    
    JSONObject filteredBESearchResponse = new JSONObject();
    
    JSONObject airSearchresponseObj = new JSONObject(beSearchResponse);
    JSONObject resonseBodyObj = airSearchresponseObj.getJSONObject("responseBody");
    JSONArray pricedItenaryArray = resonseBodyObj.getJSONArray("pricedItinerary");
    
    String sellingPrice =  product.getOrderDetails().getFlightDetails().getTotalPriceInfo().getTotalPrice();
    BigDecimal sellingPriceInt = new BigDecimal(0);
    
    if(sellingPrice!= null && !sellingPrice.isEmpty()) {
        sellingPriceInt = new BigDecimal(sellingPrice);
        
        BigDecimal higherLimitSearch = new BigDecimal(0);
        BigDecimal lowerLimitSearch = new BigDecimal(0);
        BigDecimal sameValueSearch = null;
        
        higherLimitSearch = getHigherLimitSearch(alternateOptionConfiguration.getHigherLimitThreshold(), sellingPriceInt, higherLimitSearch);
        
        lowerLimitSearch = getLowerLimitSearch(alternateOptionConfiguration.getLowerLimitThreshold(), sellingPriceInt, lowerLimitSearch);
        
        if(higherLimitSearch.floatValue() > 0 && lowerLimitSearch.floatValue() == 0)
            lowerLimitSearch = sellingPriceInt;
        else if(lowerLimitSearch.floatValue()>0 && higherLimitSearch.floatValue() == 0)
            higherLimitSearch = sellingPriceInt;
        
        JSONArray filteredArray = new JSONArray();
        
        for(int count=0;count<pricedItenaryArray.length();count++)
        {
            check=0;
            JSONObject pricedItenaryObject = pricedItenaryArray.getJSONObject(count);
            BigDecimal totalPrice = pricedItenaryObject.getJSONObject("airItineraryPricingInfo").getJSONObject("itinTotalFare").getBigDecimal("amount");
            
            if(!StringUtils.isEmpty(alternateOptionConfiguration.isSameValue()) && alternateOptionConfiguration.isSameValue()==true)
            {
              sameValueSearch = totalPrice;
            }
            
            getModifiedFilteredArray(higherLimitSearch, lowerLimitSearch,sameValueSearch, filteredArray, pricedItenaryObject,
                    totalPrice);
            
            if(check==1)
            {
              
              String firstName = product.getOrderDetails().getFlightDetails().getPaxInfo().stream().filter(x-> x.getLeadPax()).findFirst().get().getFirstName();
              String lastName = product.getOrderDetails().getFlightDetails().getPaxInfo().stream().filter(x-> x.getLeadPax()).findFirst().get().getLastName();
              String title = product.getOrderDetails().getFlightDetails().getPaxInfo().stream().filter(x-> x.getLeadPax()).findFirst().get().getTitle();
              
              String leadPaxName = title.concat(" ").concat(firstName).concat(" ").concat(lastName);
              
              String tourEndDate = product.getOrderDetails().getFlightDetails().getOriginDestinationOptions().get(0).getFlightSegment().get(0).getArrivalDateZDT().toLocalDate().toString();
              
              int odoSize = product.getOrderDetails().getFlightDetails().getOriginDestinationOptions().size()-1;
              int flightSegmentSize = product.getOrderDetails().getFlightDetails().getOriginDestinationOptions().get(odoSize).getFlightSegment().size()-1;
              String tourStartDate = product.getOrderDetails().getFlightDetails().getOriginDestinationOptions().get(odoSize).getFlightSegment().get(flightSegmentSize).getDepartureDateZDT().toLocalDate().toString();
              
              AlternateOptionsResponseDetails savedResponse = addAlternateOptionsResponseDetails(bookID,orderID,leadPaxName,tourEndDate, tourStartDate, alternateOptionConfiguration, OpsProductCategory.PRODUCT_CATEGORY_TRANSPORTATION.getCategory(),sellingPrice,totalPrice.toString(),pricedItenaryObject);
            
              filteredArray.getJSONObject(filteredArray.length()-1).put("alternateOptionResponseId", savedResponse.getId());
            }
        }
        
        
        
        JSONObject finalResponseBody = new JSONObject();
        
        finalResponseBody.put("pricedItinerary", filteredArray);
        finalResponseBody.put("configurationId", alternateOptionConfiguration.getConfigurationId());
        finalResponseBody.put("tripIndicator", resonseBodyObj.getString("tripIndicator"));
        
        filteredBESearchResponse.put("responseBody", resonseBodyObj);
        filteredBESearchResponse.put("responseHeader", airSearchresponseObj.getJSONObject("responseHeader"));
    }
    return filteredBESearchResponse;
  }
  
  private JSONObject getFilteredAccoSearchResponseAndSave(String bookID, String orderID, OpsProduct product, AlternateOptionsV2 alternateOptionConfiguration, String beSearchResponse) {
    
    JSONObject filteredBESearchResponse = new JSONObject();
    
    JSONObject roomStayResponseObj = new JSONObject(beSearchResponse);
    JSONObject resonseBodyObj = roomStayResponseObj.getJSONObject("responseBody");
    JSONObject accomodationObj = resonseBodyObj.getJSONArray("accommodationInfo").getJSONObject(0);
    JSONArray roomStay = accomodationObj.getJSONArray("roomStay");
    
    String sellingPrice =  product.getOrderDetails().getHotelDetails().getRooms().get(0).getRoomTotalPriceInfo().getRoomTotalPrice();
    BigDecimal sellingPriceInt = new BigDecimal(0);
    if(sellingPrice!= null && !sellingPrice.isEmpty()) 
    {
        sellingPriceInt = new BigDecimal(sellingPrice);
        
        BigDecimal higherLimitSearch = new BigDecimal(0);
        BigDecimal lowerLimitSearch = new BigDecimal(0);
        BigDecimal sameValueSearch = null;
        
        higherLimitSearch = getHigherLimitSearch(alternateOptionConfiguration.getHigherLimitThreshold(), sellingPriceInt, higherLimitSearch);
        
        lowerLimitSearch = getLowerLimitSearch(alternateOptionConfiguration.getLowerLimitThreshold(), sellingPriceInt, lowerLimitSearch);
        
        if(higherLimitSearch.floatValue() > 0 && lowerLimitSearch.floatValue() == 0)
            lowerLimitSearch = sellingPriceInt;
        else if(lowerLimitSearch.floatValue()>0 && higherLimitSearch.floatValue() == 0)
            higherLimitSearch = sellingPriceInt;
        
        JSONArray filteredArray = new JSONArray();
        
        for(int count=0;count<roomStay.length();count++)
        {
            check=0;
            JSONObject roomStayObject = roomStay.getJSONObject(count);
            BigDecimal totalPrice = roomStayObject.getJSONObject("totalPriceInfo").getBigDecimal("amount");
            
            if(!StringUtils.isEmpty(alternateOptionConfiguration.isSameValue()) && alternateOptionConfiguration.isSameValue()==true)
            {
              sameValueSearch = totalPrice;
            }
            
            getModifiedFilteredArray(higherLimitSearch, lowerLimitSearch,sameValueSearch, filteredArray, roomStayObject,
                totalPrice);
        
            if(check==1)
            {
              String firstName = product.getOrderDetails().getHotelDetails().getRooms().get(0).getPaxInfo().stream().filter(x-> x.getLeadPax()).findFirst().get().getFirstName();
              String lastName = product.getOrderDetails().getHotelDetails().getRooms().get(0).getPaxInfo().stream().filter(x-> x.getLeadPax()).findFirst().get().getLastName();
              String title = product.getOrderDetails().getHotelDetails().getRooms().get(0).getPaxInfo().stream().filter(x-> x.getLeadPax()).findFirst().get().getTitle();
              
              String leadPaxName = title.concat(" ").concat(firstName).concat(" ").concat(lastName);
              
              String tourEndDate = product.getOrderDetails().getHotelDetails().getRooms().get(0).getCheckOut();
              
              String tourStartDate = product.getOrderDetails().getHotelDetails().getRooms().get(0).getCheckIn();
              
              AlternateOptionsResponseDetails savedResponse = addAlternateOptionsResponseDetails(bookID,orderID,leadPaxName,tourEndDate, tourStartDate, alternateOptionConfiguration, OpsProductCategory.PRODUCT_CATEGORY_ACCOMMODATION.getCategory(),sellingPrice,totalPrice.toString(),roomStayObject);
            
              filteredArray.getJSONObject(filteredArray.length()-1).put("alternateOptionResponseId", savedResponse.getId());
            }
        }
        
        JSONObject finalResponseBody = new JSONObject();
        
        finalResponseBody.put("roomStay", filteredArray);
        //Search Response will be changed by BE Team not yet implemented in BE
        //finalResponseBody.put("configurationId", alternateOptionConfiguration.getConfigurationId());
        //finalResponseBody.put("tripIndicator", resonseBodyObj.getString("tripIndicator"));
        
        filteredBESearchResponse.put("responseBody", resonseBodyObj);
        filteredBESearchResponse.put("responseHeader", roomStayResponseObj.getJSONObject("responseHeader"));
    }
    return filteredBESearchResponse;

  }
  
  private void getModifiedFilteredArray(BigDecimal higherLimitSearch, BigDecimal lowerLimitSearch, BigDecimal sameValueSearch,
      JSONArray filteredArray, JSONObject jsonObj, BigDecimal totalPrice) 
      {
      if(sameValueSearch!= null && !StringUtils.isEmpty(sameValueSearch) && sameValueSearch.floatValue()!=0)
      {
        if((totalPrice.floatValue()<=higherLimitSearch.floatValue() && totalPrice.floatValue()>=lowerLimitSearch.floatValue()) || totalPrice.floatValue()==sameValueSearch.floatValue() ) 
        {
          filteredArray.put(jsonObj);
          check=1;
        }
      }
      else {
        if(totalPrice.floatValue()<=higherLimitSearch.floatValue() && totalPrice.floatValue()>=lowerLimitSearch.floatValue()) 
        {
          filteredArray.put(jsonObj);
          check=1;
        }
      }
   }
  
  private BigDecimal getHigherLimitSearch(String higherLimitThreshold, BigDecimal sellingPriceInt,
      BigDecimal higherLimitSearch) {
  BigDecimal higherLimitThresholdint = new BigDecimal(0);
    if(higherLimitThreshold != null && !higherLimitThreshold.isEmpty()) {
        higherLimitThresholdint = new BigDecimal(higherLimitThreshold);
        higherLimitSearch = sellingPriceInt.add(higherLimitThresholdint);
    }
    return higherLimitSearch;
  }
  
  private BigDecimal getLowerLimitSearch(String lowerLimitThreshold, BigDecimal sellingPriceInt,BigDecimal lowerLimitSearch) 
  {
      BigDecimal lowerLimitThresholdint = new BigDecimal(0);
      
      if(lowerLimitThreshold != null && !lowerLimitThreshold.isEmpty()) 
      {
          lowerLimitThresholdint = new BigDecimal(lowerLimitThreshold);
          
          if(lowerLimitThresholdint.floatValue()<sellingPriceInt.floatValue())
          lowerLimitSearch = sellingPriceInt.subtract(lowerLimitThresholdint);
      }
      return lowerLimitSearch;
  }
  
  private AlternateOptionsResponseDetails addAlternateOptionsResponseDetails(String BookID, String orderID,String leadPaxName,String tourEndDate,String tourStartDate, AlternateOptionsV2 configResponse,
      String productCategory,String failedBookingPrice,String alternateOptionPrice,JSONObject alternateOptionJson) {

      AlternateOptionsV2 savedAlternateOptionsV2 = alternateOptionsRepository.fetchAlternateOptionsForConfigurationId(configResponse.getConfigurationId());
      
      AlternateOptionsResponseDetails alternateOptionsResponseDetails = new AlternateOptionsResponseDetails();
      
      alternateOptionsResponseDetails.setBookID(BookID);
      alternateOptionsResponseDetails.setOrderID(orderID);
      alternateOptionsResponseDetails.setLeadPaxName(leadPaxName);
      alternateOptionsResponseDetails.setProductCategory(productCategory);
      /** Add Data in Database "Response Pending" (initial status) */
      alternateOptionsResponseDetails.setStatus("Response Pending");
      alternateOptionsResponseDetails.setTourEndDate(tourEndDate);
      alternateOptionsResponseDetails.setTourStartDate(tourStartDate);
      alternateOptionsResponseDetails.setAlternateOptionSentDate(ZonedDateTime.now());
      alternateOptionsResponseDetails.setFailedBookingPrice(failedBookingPrice);
      alternateOptionsResponseDetails.setAlternateOptionPrice(alternateOptionPrice);
      BigDecimal decimalfailedBookingPrice = new BigDecimal(failedBookingPrice);
      BigDecimal decimalAlternateOptionPrice = new BigDecimal(alternateOptionPrice);
      if( decimalAlternateOptionPrice.floatValue() > decimalfailedBookingPrice.floatValue())
        alternateOptionsResponseDetails.setPayable(true);
      else
        alternateOptionsResponseDetails.setPayable(false);
      alternateOptionsResponseDetails.setAlternateOptionDetails(alternateOptionJson.toString());
      
      alternateOptionsResponseDetails.setAlternateOptions(savedAlternateOptionsV2);
      
      Set<AlternateOptionsResponseDetails> alternateOptionsResponseDetailsList = null;
      if(savedAlternateOptionsV2.getAlternateOptionsResponseDetails()==null || savedAlternateOptionsV2.getAlternateOptionsResponseDetails().isEmpty() || savedAlternateOptionsV2.getAlternateOptionsResponseDetails().size()==0)
      {
        alternateOptionsResponseDetailsList = new HashSet<AlternateOptionsResponseDetails>();
        alternateOptionsResponseDetailsList.add(alternateOptionsResponseDetails);
      }
      else
      {
         alternateOptionsResponseDetailsList = savedAlternateOptionsV2.getAlternateOptionsResponseDetails();
         alternateOptionsResponseDetailsList.add(alternateOptionsResponseDetails);
      }
      
      savedAlternateOptionsV2.setAlternateOptionsResponseDetails(alternateOptionsResponseDetailsList);
      
      //AlternateOptionsV2 updatedAlternateOptionsV2 = alternateOptionsRepository.update(savedAlternateOptionsV2);
      
      AlternateOptionsResponseDetails savedResponse = alternateOptionsResponseRepository.saveOrUpdate(alternateOptionsResponseDetails);
      
      return savedResponse;
    }
  
  //SEND EMAIL TO CLIENT/CUSTOMER
  @Override
  public EmailResponse sendAnEmailToClientOrCustomer(OpsBooking opsBooking, JSONObject finalBESearchResponse , String destination, String productName ,String emailId, String name) throws IOException, ParseException, OperationException {
      EmailResponse emailResponse = null;
      Map<String, String> dynamicVariable = new HashMap<>();

      String clientType = opsBooking.getClientType();
      String clientId = opsBooking.getClientID();
      
      String clientEmailId = null;
      String clientName = null;
      
      if (!StringUtils.isEmpty(clientType) && !StringUtils.isEmpty(clientId)) 
      {
         clientEmailId = clientMasterDataService.getClientEmailId(clientId, MDMClientType.fromString(clientType));
         
         if ("B2B".equalsIgnoreCase(clientType)) 
         {
           Map<String, String> b2BClientNames = clientMasterDataService.getB2BClientNames(Collections.singletonList(clientId));
           
           if (b2BClientNames != null && b2BClientNames.size() > 0) 
           {
             for (Map.Entry<String, String> entry : b2BClientNames.entrySet()) 
             {
               clientName = entry.getValue();
             }
           }
         } 
         else if ("B2C".equalsIgnoreCase(clientType)) 
         {
           Map<String, String> b2CClientNames = clientMasterDataService.getB2CClientNames(Collections.singletonList(clientId));
                  
           if (b2CClientNames != null && b2CClientNames.size() > 0) 
           {
             for (Map.Entry<String, String> entry : b2CClientNames.entrySet()) 
             {
                clientName = entry.getValue();
             }
           }
         } 
         else 
         {
           logger.info("Client type not specified");    
         }
       }

      
       if(clientEmailId==null || clientName==null)
       {
         clientEmailId = emailId;
         clientName = name;
       }
      
       //For Testing Delete Later
       clientEmailId = "priyana.godiwalla@coxandkings.com";
         
       dynamicVariable.put(clientFirstName, clientName);
       
       dynamicVariable.put(clientDestinationName, destination);

       dynamicVariable.put(clientProductName, productName);
       
       dynamicVariable.put(wemURL, "test");
       //dynamicVariable.put(wemURL, "<a href='\" + clientAcceptanceUrl + supplementOnSupplierPrice.getIdentifier() + \"'>Click Here</a>");
                      
       emailResponse = emailUtils.buildClientMail(function, scenario, clientEmailId, subject, dynamicVariable, null, null);

       if ("Success".equalsIgnoreCase(emailResponse.getStatus())) 
       {
           logger.error("Email Sent to Client/Customer");
           System.out.println("Email Sent to Client/Customer");
           return emailResponse;
       } 
       else 
       {
         logger.error("Error while sending an email");
         throw new OperationException("Error while sending an email");
       }
  }
  
  //VIEW ALTERNATE OPTIONS SENT
  @Override
  public Map<String, Object> viewAlternateOptionsSent(AlternateOptionsResponseCriteria alternateOptionsResponseCriteria) throws OperationException {

      try {
        
          return alternateOptionsResponseRepository.searchAlternateOptionResponse(alternateOptionsResponseCriteria);
          
      } catch (Exception e) {
          logger.error("Error occurred while loading Alternate Option Response Records");
          throw new OperationException(Constants.UNABLE_TO_FETCH_DETAILS);
      }
  }
  
  //GET ALTERNATE OPTIONS RESPONSE BY ID
  @Override
  public JSONObject getAlternateOptionsSentById(String id) throws OperationException {
    
    AlternateOptionsResponseDetails alternateOptionsResponseDetails;
    
    JSONObject responseDetails = new JSONObject();
    try {
      alternateOptionsResponseDetails = alternateOptionsResponseRepository.getAlternateOptionsSentById(id);
   
      responseDetails.put("id", alternateOptionsResponseDetails.getId());
      responseDetails.put("leadPaxName", alternateOptionsResponseDetails.getLeadPaxName());
      responseDetails.put("productCategory", alternateOptionsResponseDetails.getProductCategory());
      responseDetails.put("tourStartDate", alternateOptionsResponseDetails.getTourStartDate());
      responseDetails.put("tourEndDate", alternateOptionsResponseDetails.getTourEndDate());
      responseDetails.put("alternateOptionSentDate", alternateOptionsResponseDetails.getAlternateOptionSentDate());
      responseDetails.put("failedBookingPrice", alternateOptionsResponseDetails.getFailedBookingPrice());
      responseDetails.put("alternateOptionPrice", alternateOptionsResponseDetails.getAlternateOptionPrice());
      responseDetails.put("bookID", alternateOptionsResponseDetails.getBookID());
      responseDetails.put("orderID", alternateOptionsResponseDetails.getOrderID());
      responseDetails.put("alternateOptionDetails", new JSONObject(alternateOptionsResponseDetails.getAlternateOptionDetails()));
      responseDetails.put("status", alternateOptionsResponseDetails.getStatus());
      responseDetails.put("payable", alternateOptionsResponseDetails.isPayable());
      
    } catch (Exception e) {
        logger.error("Exception occurred while fetching Alternate Options Configuration for id " + id, e);
        throw new OperationException(Constants.UNABLE_TO_FETCH_DETAILS);
    }
    if (alternateOptionsResponseDetails == null) {
        logger.error("No Alternate Options Response found for id " + id);
        throw new OperationException(Constants.NO_RESULT_FOUND);
    }
    return responseDetails;
    
  }
  
  //MODIFY SEARCH IMPLEMENTATION
  @Override
  public String modifySearch(AlternateOptionsModifySearchResource alternateOptionsModifySearchResource, OpsBooking opsFailedBooking , OpsProduct product) throws OperationException {
    
    JSONObject searchRequest = new JSONObject();
    JSONObject requestBody = new JSONObject();
    JSONObject requestHeader = new JSONObject();
    
    ResponseEntity<String> beSearchResponse = null;
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    
    requestHeader = getRequestHeader(opsFailedBooking);
    
    if (product.getProductCategory().equals(OpsProductCategory.PRODUCT_CATEGORY_TRANSPORTATION.getCategory())
        && product.getProductSubCategory().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory())) 
    {
      requestBody.put("tripType", alternateOptionsModifySearchResource.getFlightSearch().getTripType());
      requestBody.put("tripIndicator", product.getOrderDetails().getFlightDetails().getTripIndicator());
      requestBody.put("originDestinationInfo", alternateOptionsModifySearchResource.getFlightSearch().getOriginDestinationInfo());
      requestBody.put("paxInfo", alternateOptionsModifySearchResource.getFlightSearch().getPaxInfo());
      requestBody.put("cabinType", alternateOptionsModifySearchResource.getFlightSearch().getCabinType());
      searchRequest.put("requestHeader", requestHeader);
      searchRequest.put("requestBody", requestBody);
      
      HttpEntity<String> httpEntity = new HttpEntity<>(searchRequest.toString(), headers);
      try {
        beSearchResponse = RestUtils.exchange(this.airSearchURL, HttpMethod.POST, httpEntity, String.class);
      } catch (Exception e) {
          e.printStackTrace();
      }
      
      if(beSearchResponse==null)
        throw new OperationException(Constants.NO_RESULT_FOUND);
      
    }
    else if (product.getProductCategory().equals(OpsProductCategory.PRODUCT_CATEGORY_ACCOMMODATION.getCategory())) 
    {
      requestBody.put("countryCode", alternateOptionsModifySearchResource.getAccoSearch().getCountryCode());
      requestBody.put("cityCode", alternateOptionsModifySearchResource.getAccoSearch().getCityCode());
      requestBody.put("hotelCode", alternateOptionsModifySearchResource.getAccoSearch().getHotelCode());
      requestBody.put("accommodationSubTypes", alternateOptionsModifySearchResource.getAccoSearch().getAccommodationSubTypes());
      requestBody.put("checkIn", alternateOptionsModifySearchResource.getAccoSearch().getCheckIn());
      requestBody.put("checkOut", alternateOptionsModifySearchResource.getAccoSearch().getCheckOut());
      requestBody.put("paxNationality", "IN");// todo
      requestBody.put("roomConfig", alternateOptionsModifySearchResource.getAccoSearch().getRoomConfig());
      searchRequest.put("requestHeader", requestHeader);
      searchRequest.put("requestBody", requestBody);
      
      HttpEntity<String> httpEntity = new HttpEntity<>(searchRequest.toString(), headers);
      try {
        beSearchResponse = RestUtils.exchange(this.accoSearchURL, HttpMethod.POST, httpEntity, String.class);
      } catch (Exception e) {
          e.printStackTrace();
      }
      
      if(beSearchResponse==null)
        throw new OperationException(Constants.NO_RESULT_FOUND);
    }
    
    String response = null;
    if(beSearchResponse != null)
        response = beSearchResponse.getBody();

    return response;  
  }
  
  @Override
  public JSONObject modifySearchPDPPage(String bookID, String orderID, JSONObject pdpPageRequest) throws OperationException {
    
    OpsBooking opsFailedBooking = opsBookingService.getBooking(bookID);
    
    OpsProduct product = null;
    
    List<OpsProduct> opsFailedBookingProducts = opsFailedBooking.getProducts();
    
    for(int i=0;i<opsFailedBookingProducts.size();i++) 
    {
        if(orderID.equals(opsFailedBooking.getProducts().get(i).getOrderID())) 
        {
            product = opsFailedBooking.getProducts().get(i);
            
            System.out.println("productCategory : "+product.getProductCategory() );
            System.out.println("productCategorySubType : "+  product.getProductSubCategory());
            
            break;
        }
    }
    
    //Making Request Header
    JSONObject searchRequest = new JSONObject();
    JSONObject requestBody = new JSONObject();
    JSONObject requestHeader = new JSONObject();
    
    ResponseEntity<String> beSearchResponse = null;
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    
    requestHeader = getRequestHeader(opsFailedBooking);
    
    //Making Request Body
    if (product.getProductCategory().equals(OpsProductCategory.PRODUCT_CATEGORY_TRANSPORTATION.getCategory())
        && product.getProductSubCategory().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory())) 
    {
      searchRequest.put("requestHeader", requestHeader);
      searchRequest.put("requestBody", requestBody);
      
      HttpEntity<String> httpEntity = new HttpEntity<>(searchRequest.toString(), headers);
      try {
        beSearchResponse = RestUtils.exchange(this.airPriceURL, HttpMethod.POST, httpEntity, String.class);
      } catch (Exception e) {
          e.printStackTrace();
      }
      
      if(beSearchResponse==null)
        throw new OperationException(Constants.NO_RESULT_FOUND);
      
    }
    else if (product.getProductCategory().equals(OpsProductCategory.PRODUCT_CATEGORY_ACCOMMODATION.getCategory())) 
    {
      searchRequest.put("requestHeader", requestHeader);
      searchRequest.put("requestBody", requestBody);
      
      HttpEntity<String> httpEntity = new HttpEntity<>(searchRequest.toString(), headers);
      try {
        beSearchResponse = RestUtils.exchange(this.accoPriceURL, HttpMethod.POST, httpEntity, String.class);
      } catch (Exception e) {
          e.printStackTrace();
      }
      
      if(beSearchResponse==null)
        throw new OperationException(Constants.NO_RESULT_FOUND);
    }
    
    JSONObject response = null;
    if(beSearchResponse != null)
        response = new JSONObject(beSearchResponse.getBody());

    return response; 
  }
  
  private JSONObject getRequestHeader(OpsBooking opsBooking) throws JSONException {
    JSONObject requestHeader = new JSONObject();

    requestHeader.put("sessionID", opsBooking.getSessionID());
    requestHeader.put("transactionID", opsBooking.getTransactionID());
    requestHeader.put("userID", opsBooking.getUserID());
    
    JSONObject clientContext = new JSONObject();
    clientContext.put("clientID", opsBooking.getClientID());
    clientContext.put("clientMarket", opsBooking.getClientMarket());
    clientContext.put("clientType", opsBooking.getClientType());
    clientContext.put("clientLanguage", opsBooking.getClientLanguage());
    clientContext.put("clientIATANumber", opsBooking.getClientIATANumber());
    clientContext.put("pointOfSale", opsBooking.getPointOfSale());
    clientContext.put("clientNationality", ""); // todo be not sending currently
    clientContext.put("clientCallbackAddress", "");// todo be not sending currently
    clientContext.put("clientCurrency", opsBooking.getClientCurrency());
    
    requestHeader.put("clientContext", clientContext);
    
    return requestHeader;
  }


  //FOLLOW UP IMPLEMENTATION
  @Override
  public JSONObject saveFollowUpSent(AlternateOptionsFollowUpResource alternateOptionsFollowUpResource) throws OperationException {
    try
    {
    AlternateOptionsResponseCriteria alternateOptionsResponseCriteria = new AlternateOptionsResponseCriteria();
    alternateOptionsResponseCriteria.setLeadPaxName(alternateOptionsFollowUpResource.getAssignedUserName());
    alternateOptionsResponseCriteria.setBookID(alternateOptionsFollowUpResource.getBookID());
    alternateOptionsResponseCriteria.setOrderID(alternateOptionsFollowUpResource.getOrderID());
    
    Map<String, Object> alternateOptionsResponseDetails = alternateOptionsResponseRepository.searchAlternateOptionResponse(alternateOptionsResponseCriteria);
    
    List<JSONObject> alternateOptionsResponseList = (List<JSONObject>) alternateOptionsResponseDetails.get("data");
    
    AlternateOptionsFollowUpDetails alternateOptionsFollowUpDetails = new AlternateOptionsFollowUpDetails();
    
    alternateOptionsFollowUpDetails = followUpAssembler(alternateOptionsFollowUpResource, alternateOptionsFollowUpDetails);
    
    JSONArray responseIDs = new JSONArray();
    for (JSONObject responseDetail : alternateOptionsResponseList) 
    {
        JSONObject responseIDJson = new JSONObject();
        responseIDJson.put("responseId", responseDetail.getString("id"));
        responseIDs.put(responseIDJson);
    }
    alternateOptionsFollowUpDetails.setAlternateOptionsResponseDetails(responseIDs.toString()); 
    
    alternateOptionsFollowUpRepository.saveOrUpdate(alternateOptionsFollowUpDetails);
    
    JSONObject followUpDetails = new JSONObject();
      
    followUpDetails.put("id", alternateOptionsFollowUpDetails.getId());
    followUpDetails.put("subject", alternateOptionsFollowUpDetails.getSubject());
    followUpDetails.put("followUpActivityType", alternateOptionsFollowUpDetails.getFollowUpActivityType());
    followUpDetails.put("creationDate", alternateOptionsFollowUpDetails.getCreationDate());
    followUpDetails.put("dueDate", alternateOptionsFollowUpDetails.getDueDate());
    followUpDetails.put("status", alternateOptionsFollowUpDetails.getStatus());
    followUpDetails.put("assignedUserRole", alternateOptionsFollowUpDetails.getAssignedUserRole());
    followUpDetails.put("assignedUserName", alternateOptionsFollowUpDetails.getAssignedUserName());
    followUpDetails.put("startDate", alternateOptionsFollowUpDetails.getStartDate());
    followUpDetails.put("startTime", alternateOptionsFollowUpDetails.getStartTime());
    followUpDetails.put("endTime", alternateOptionsFollowUpDetails.getEndTime());
    followUpDetails.put("scheduleType", alternateOptionsFollowUpDetails.getScheduleType());
    followUpDetails.put("reminderType", alternateOptionsFollowUpDetails.getReminderType());
    followUpDetails.put("reOccurringSchedule", alternateOptionsFollowUpDetails.getReOccurringSchedule());
    followUpDetails.put("alternateOptionsResponseDetails", new JSONArray(alternateOptionsFollowUpDetails.getAlternateOptionsResponseDetails()));
      
    return followUpDetails;
    }catch(Exception e)
    {
      e.printStackTrace();
    }
    return null;
  }
  
  private AlternateOptionsFollowUpDetails followUpAssembler(AlternateOptionsFollowUpResource alternateOptionsFollowUpResource, AlternateOptionsFollowUpDetails alternateOptionsFollowUpDetails)
  {
    if(alternateOptionsFollowUpResource.getId()!=null && !StringUtils.isEmpty(alternateOptionsFollowUpResource.getId()))
      alternateOptionsFollowUpDetails.setId(alternateOptionsFollowUpResource.getId());
    if(alternateOptionsFollowUpResource.getSubject()!=null && !StringUtils.isEmpty(alternateOptionsFollowUpResource.getSubject()))
      alternateOptionsFollowUpDetails.setSubject(alternateOptionsFollowUpResource.getSubject());
    if(alternateOptionsFollowUpResource.getFollowUpActivityType()!=null && !StringUtils.isEmpty(alternateOptionsFollowUpResource.getFollowUpActivityType()))
      alternateOptionsFollowUpDetails.setFollowUpActivityType(alternateOptionsFollowUpResource.getFollowUpActivityType());
    if(alternateOptionsFollowUpDetails.getCreationDate()!=null && !StringUtils.isEmpty(alternateOptionsFollowUpDetails.getCreationDate()))
      alternateOptionsFollowUpDetails.setCreationDate(ZonedDateTime.now(ZoneOffset.UTC));
    if(alternateOptionsFollowUpResource.getDueDate()!=null && !StringUtils.isEmpty(alternateOptionsFollowUpResource.getDueDate()))
      alternateOptionsFollowUpDetails.setDueDate(alternateOptionsFollowUpResource.getDueDate());
    if(alternateOptionsFollowUpResource.getStatus()!=null && !StringUtils.isEmpty(alternateOptionsFollowUpResource.getStatus()))
      alternateOptionsFollowUpDetails.setStatus(alternateOptionsFollowUpResource.getStatus());
    if(alternateOptionsFollowUpResource.getAssignedUserName()!=null && !StringUtils.isEmpty(alternateOptionsFollowUpResource.getAssignedUserName()))
      alternateOptionsFollowUpDetails.setAssignedUserName(alternateOptionsFollowUpResource.getAssignedUserName());
    if(alternateOptionsFollowUpResource.getAssignedUserRole()!=null && !StringUtils.isEmpty(alternateOptionsFollowUpResource.getAssignedUserRole()))
      alternateOptionsFollowUpDetails.setAssignedUserRole(alternateOptionsFollowUpResource.getAssignedUserRole());
    if(alternateOptionsFollowUpResource.getStartDate()!=null && !StringUtils.isEmpty(alternateOptionsFollowUpResource.getStartDate()))
      alternateOptionsFollowUpDetails.setStartDate(alternateOptionsFollowUpResource.getStartDate());
    if(alternateOptionsFollowUpResource.getStartTime()!=null && !StringUtils.isEmpty(alternateOptionsFollowUpResource.getStartTime()))
      alternateOptionsFollowUpDetails.setStartTime(alternateOptionsFollowUpResource.getStartTime());
    if(alternateOptionsFollowUpResource.getEndTime()!=null && !StringUtils.isEmpty(alternateOptionsFollowUpResource.getEndTime()))
      alternateOptionsFollowUpDetails.setEndTime(alternateOptionsFollowUpResource.getEndTime());
    if(alternateOptionsFollowUpResource.getScheduleType()!=null && !StringUtils.isEmpty(alternateOptionsFollowUpResource.getScheduleType()))
      alternateOptionsFollowUpDetails.setScheduleType(alternateOptionsFollowUpResource.getScheduleType());
    if(alternateOptionsFollowUpResource.getReminderType()!=null && !StringUtils.isEmpty(alternateOptionsFollowUpResource.getReminderType()))
      alternateOptionsFollowUpDetails.setReminderType(alternateOptionsFollowUpResource.getReminderType());
    
    if(alternateOptionsFollowUpResource.getReOccurringSchedule()!=null && !StringUtils.isEmpty(alternateOptionsFollowUpResource.getReOccurringSchedule()) )
    {
      AlternateOptionsReOccurrence reOccurringSchedule ;
      if(alternateOptionsFollowUpResource.getReOccurringSchedule()!=null && !StringUtils.isEmpty(alternateOptionsFollowUpResource.getReOccurringSchedule()))
      {
        reOccurringSchedule = new AlternateOptionsReOccurrence();
      }
      else
      {
        reOccurringSchedule = alternateOptionsFollowUpDetails.getReOccurringSchedule();
      }
      
      if(alternateOptionsFollowUpResource.getReOccurringSchedule().getId()!=null && !StringUtils.isEmpty(alternateOptionsFollowUpResource.getReOccurringSchedule().getId()))
        reOccurringSchedule.setId(alternateOptionsFollowUpResource.getReOccurringSchedule().getId());
      if(alternateOptionsFollowUpResource.getReOccurringSchedule().getReOccurrenceType()!=null && !StringUtils.isEmpty(alternateOptionsFollowUpResource.getReOccurringSchedule().getReOccurrenceType()))
        reOccurringSchedule.setReOccurrenceType(alternateOptionsFollowUpResource.getReOccurringSchedule().getReOccurrenceType());
      if(alternateOptionsFollowUpResource.getReOccurringSchedule().getIsWeekday()!=null && !StringUtils.isEmpty(alternateOptionsFollowUpResource.getReOccurringSchedule().getIsWeekday()))
        reOccurringSchedule.setIsWeekday(alternateOptionsFollowUpResource.getReOccurringSchedule().getIsWeekday());
      if(alternateOptionsFollowUpResource.getReOccurringSchedule().getAfterHowManyDays()!=null && !StringUtils.isEmpty(alternateOptionsFollowUpResource.getReOccurringSchedule().getAfterHowManyDays()))
        reOccurringSchedule.setAfterHowManyDays(alternateOptionsFollowUpResource.getReOccurringSchedule().getAfterHowManyDays());
      if(alternateOptionsFollowUpResource.getReOccurringSchedule().getDayOfTheWeek()!=null && !StringUtils.isEmpty(alternateOptionsFollowUpResource.getReOccurringSchedule().getDayOfTheWeek()))
        reOccurringSchedule.setDayOfTheWeek(alternateOptionsFollowUpResource.getReOccurringSchedule().getDayOfTheWeek());
      if(alternateOptionsFollowUpResource.getReOccurringSchedule().getNumberOfWeeks()!=null && !StringUtils.isEmpty(alternateOptionsFollowUpResource.getReOccurringSchedule().getNumberOfWeeks()))
        reOccurringSchedule.setNumberOfWeeks(alternateOptionsFollowUpResource.getReOccurringSchedule().getNumberOfWeeks());
      if(alternateOptionsFollowUpResource.getReOccurringSchedule().getIsWeekdayMonthlyOccurrence()!=null && !StringUtils.isEmpty(alternateOptionsFollowUpResource.getReOccurringSchedule().getIsWeekdayMonthlyOccurrence()))
        reOccurringSchedule.setIsWeekdayMonthlyOccurrence(alternateOptionsFollowUpResource.getReOccurringSchedule().getIsWeekdayMonthlyOccurrence());
      if(alternateOptionsFollowUpResource.getReOccurringSchedule().getDay()!=null && !StringUtils.isEmpty(alternateOptionsFollowUpResource.getReOccurringSchedule().getDay()))
        reOccurringSchedule.setDay(alternateOptionsFollowUpResource.getReOccurringSchedule().getDay());
      if(alternateOptionsFollowUpResource.getReOccurringSchedule().getMonth()!=null && !StringUtils.isEmpty(alternateOptionsFollowUpResource.getReOccurringSchedule().getMonth()))
        reOccurringSchedule.setMonth(alternateOptionsFollowUpResource.getReOccurringSchedule().getMonth());
      if(alternateOptionsFollowUpResource.getReOccurringSchedule().getWeek()!=null && !StringUtils.isEmpty(alternateOptionsFollowUpResource.getReOccurringSchedule().getWeek()))
        reOccurringSchedule.setWeek(alternateOptionsFollowUpResource.getReOccurringSchedule().getWeek());
      if(alternateOptionsFollowUpResource.getReOccurringSchedule().getEndDate()!=null && !StringUtils.isEmpty(alternateOptionsFollowUpResource.getReOccurringSchedule().getEndDate()))
        reOccurringSchedule.setEndDate(alternateOptionsFollowUpResource.getReOccurringSchedule().getEndDate());
      if(alternateOptionsFollowUpResource.getReOccurringSchedule().getNumberOfOccurrences()!=null && !StringUtils.isEmpty(alternateOptionsFollowUpResource.getReOccurringSchedule().getNumberOfOccurrences()))
        reOccurringSchedule.setNumberOfOccurrences(alternateOptionsFollowUpResource.getReOccurringSchedule().getNumberOfOccurrences());
      
      reOccurringSchedule.setAlternateOptionsFollowUpDetails(alternateOptionsFollowUpDetails);
      alternateOptionsFollowUpDetails.setReOccurringSchedule(reOccurringSchedule);
    }
    
    return alternateOptionsFollowUpDetails;
  }


  @Override
  public JSONArray followUpAutoSuggest(AlternateOptionsFollowUpResource alternateOptionsFollowUpResource) throws OperationException {

    ResponseEntity<String> mdmResponse = null;
    JSONArray autoSuggestAssignedToUser = new JSONArray();
    
    String URL = String.format("%s{\"roleName\":\"/%s/\"}",mdmUserRolesUrl, alternateOptionsFollowUpResource.getAssignedUserRole());
    
    if(alternateOptionsFollowUpResource.getAssignedUserRole() != null && !alternateOptionsFollowUpResource.getAssignedUserRole().isEmpty())
    {
      mdmResponse = RestUtils.exchange(URL, HttpMethod.GET, getHttpEntity(), String.class);
    }
    
    JSONObject autoSuggest = new JSONObject(mdmResponse.getBody());
    
    JSONArray autoSuggestArray = autoSuggest.getJSONArray("data");
    
    for(int i=0;i<autoSuggest.getJSONArray("data").length();i++)
    {
      JSONObject autoSuggestObj = new JSONObject();
      
      autoSuggestObj.put("roleName", autoSuggest.getJSONArray("data").getJSONObject(i).getString("roleName"));
      
      autoSuggestAssignedToUser.put(autoSuggestObj);
    }
    
    return autoSuggestAssignedToUser;
  }
  
  private HttpEntity getHttpEntity(){

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    String token = userService.getLoggedInUserToken();
    headers.add("Authorization", token);
    return new HttpEntity(headers);
  }
  
  
  
}
