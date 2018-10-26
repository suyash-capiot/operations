package com.coxandkings.travel.operations.consumer.listners.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.coxandkings.travel.operations.consumer.listners.AlternateOptionsBookingListener;
import com.coxandkings.travel.operations.consumer.listners.BookingListenerType;
import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptions;
import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptionsResponseDetails;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.service.alternateoptions.AlternateOptionsService;
import com.coxandkings.travel.operations.service.beconsumer.acco.AccoBookingEngineConsumptionService;
import com.coxandkings.travel.operations.service.beconsumer.activities.ActivitiesBookingEngineConsumptionService;
import com.coxandkings.travel.operations.service.beconsumer.air.AirBookingEngineConsumptionService;
import com.coxandkings.travel.operations.service.beconsumer.holidays.HolidaysBookingEngineConsumptionService;
import com.coxandkings.travel.operations.utils.EmailUtils;

@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AlternateOptionsBookingListenerImpl implements AlternateOptionsBookingListener{
	
	@Autowired
	private AlternateOptionsService alternateOptionsService;
	
	@Autowired
	private AirBookingEngineConsumptionService airBookingEngineConsumptionService;
	
	@Autowired
	private AccoBookingEngineConsumptionService accoBookingEngineConsumptionService;
	
	@Autowired
	private ActivitiesBookingEngineConsumptionService activitiesBookingEngineConsumptionService;
	
	@Autowired
	private HolidaysBookingEngineConsumptionService holidaysBookingEngineConsumptionService;
	
	@Autowired
	private EmailUtils emailUtils;
	
	 @Value("${alternate-options.template_config.function}")
	 private String function;

	 @Value("${alternate-options.template_config.scenario}")
	 private String scenario;

	 @Value("${alternate-options.template_config.subject}")
	 private String subject;

    // TODO : AlternateOffer Process Auto/Manual needs to be put in IF condition. For Manual Some steps will happen 
    // after user instantiating it
	public void processBooking(OpsBooking opsBooking,KafkaBookingMessage kafkaBookingMessage ) throws OperationException {
		JSONObject reqJson = new JSONObject();
		reqJson.put("companyMarket", opsBooking.getClientMarket());
		reqJson.put("clientType", opsBooking.getClientType());
		OpsProduct product = null;
		
		System.out.println("CompanyMarket : "+opsBooking.getClientMarket() );
		System.out.println("ClientType : "+  opsBooking.getClientType());
		
		/** Create request for search Alternate Options Configuration in DB */
		String orderID = kafkaBookingMessage.getOrderNo();
		List<OpsProduct> products = opsBooking.getProducts();
		for(int i=0;i<products.size();i++) {
			if(orderID.equals(opsBooking.getProducts().get(i).getOrderID())) {
				product = opsBooking.getProducts().get(i);
				reqJson.put("productCategory", product.getProductCategory());
			    reqJson.put("productCategorySubType", product.getProductSubCategory());	
			    
			    System.out.println("productCategory : "+product.getProductCategory() );
				System.out.println("productCategorySubType : "+  product.getProductSubCategory());
			    
			    break;
		}
		}
		
		
		/** Search alternateOption Configuration for Booking configuration*/
		String  response =	alternateOptionsService.searchAlternateOptions(reqJson);
		
		JSONArray responseJSON = new JSONArray(response);
	
				
		String beSearchResponse = null;
		if(responseJSON != null && responseJSON.length()> 0) {
			
			String productCategory = reqJson.optString("productCategory");
			
			String higherLimitThreshold = responseJSON.getJSONObject(0).getString("higherLimit");
			String lowerLimitThreshold = responseJSON.getJSONObject(0).getString("lowerLimit");
			
			// TODO : Change the hard coded values of category and subCategory, Get the values from Enum
			if (product.getProductCategory().equals(OpsProductCategory.PRODUCT_CATEGORY_TRANSPORTATION.getCategory())
					&& product.getProductSubCategory().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory())) {

				/** Called Air Search */
				beSearchResponse = airBookingEngineConsumptionService.search(opsBooking, product);

				/** Data filtered on the basis of Higher and Lower limit */
				beSearchResponse = getFilteredAirSearchResponseForHigherLowerLimits(product, higherLimitThreshold, lowerLimitThreshold,
						beSearchResponse);

				/** Notifying Customer with Email about Alternate option */
				sendAirClientEmail(product, beSearchResponse);
				
				
                String leadPaxName = product.getOrderDetails().getFlightDetails().getPaxInfo().stream().filter(x-> x.getLeadPax()).findFirst().get().getFirstName();
				
				String tourEndDate = product.getOrderDetails().getFlightDetails().getOriginDestinationOptions().get(0).getFlightSegment().get(0).getArrivalDateZDT().toLocalDate().toString();
				
				String tourStartDate = product.getOrderDetails().getFlightDetails().getOriginDestinationOptions().get(0).getFlightSegment().get(0).getDepartureDateZDT().toLocalDate().toString();

				/** Add Data in Database "Response Pending" (initial status) */
				addAlternateOptionsResponseDetails(leadPaxName,tourEndDate, tourStartDate, responseJSON, productCategory);

			} else if (product.getProductCategory().equals(OpsProductCategory.PRODUCT_CATEGORY_ACCOMMODATION.getCategory())) {
			
				/** Called Acco Search */
				beSearchResponse = accoBookingEngineConsumptionService.search(opsBooking, product);

				/** Data filtered on the basis of Higher and Lower limit */
				beSearchResponse = getFilteredAccoSearchResponseForHigherLowerLimits(product, higherLimitThreshold, lowerLimitThreshold,
						beSearchResponse);
				
				/** Notifying Customer with Email about Alternate option */
				sendAccoClientEmail(product, beSearchResponse);
				
				String leadPaxName = product.getOrderDetails().getHotelDetails().getRooms().get(0).getPaxInfo().stream().filter(x-> x.getLeadPax()).findFirst().get().getFirstName();
				
				String tourEndDate = product.getOrderDetails().getHotelDetails().getRooms().get(0).getCheckOut();
				
				String tourStartDate = product.getOrderDetails().getHotelDetails().getRooms().get(0).getCheckIn();
				
				
				/**Add Data in Database "Response Pending" (initial status) */
				addAlternateOptionsResponseDetails(leadPaxName,tourEndDate, tourStartDate,responseJSON, productCategory);
				
			} else if(product.getProductCategory().equals(OpsProductCategory.PRODUCT_CATEGORY_ACTIVITIES.getCategory())) {  
				
				/** Called Activities Search */
				beSearchResponse = activitiesBookingEngineConsumptionService.search(opsBooking, product);

				/** Data filtered on the basis of Higher and Lower limit */
				beSearchResponse = getFilteredActivitiesSearchResponseForHigherLowerLimits(product, higherLimitThreshold, lowerLimitThreshold,
						beSearchResponse);
				
				/** Notifying Customer with Email about Alternate option */ 
				sendActivitiesClientEmail(product, beSearchResponse);
				
				 String leadPaxName = product.getOrderDetails().getHotelDetails().getRooms().get(0).getPaxInfo().stream().filter(x-> x.getLeadPax()).findFirst().get().getFirstName();
					
				 String tourEndDate = product.getOrderDetails().getHotelDetails().getRooms().get(0).getCheckOut();
					
				 String tourStartDate = product.getOrderDetails().getHotelDetails().getRooms().get(0).getCheckIn();
				
				/**Add Data in Database "Response Pending" (initial status) */
				addAlternateOptionsResponseDetails(leadPaxName,tourEndDate, tourStartDate,responseJSON, productCategory);
				
			}else if(product.getProductCategory().equals(OpsProductCategory.PRODUCT_CATEGORY_HOLIDAYS.getCategory())) {
			
				/** Called Holidays Search */
				beSearchResponse = holidaysBookingEngineConsumptionService.search(opsBooking, product);

				/** Data filtered on the basis of Higher and Lower limit */
				beSearchResponse = getFilteredHolidaysSearchResponseForHigherLowerLimits(product, higherLimitThreshold, lowerLimitThreshold,
						beSearchResponse);
				
				/** Notifying Customer with Email about Alternate option */ 
				sendHolidaysClientEmail(product, beSearchResponse);
				
	            String leadPaxName = product.getOrderDetails().getPackageDetails().getPaxInfo().stream().filter(x-> x.getIsLeadPax()).findFirst().get().getFirstName();
				
				String tourEndDate = product.getOrderDetails().getPackageDetails().getTourEndDate();
				
				String tourStartDate = product.getOrderDetails().getPackageDetails().getTourStartDate();
				
				/**Add Data in Database "Response Pending" (initial status) */
				addAlternateOptionsResponseDetails(leadPaxName,tourEndDate, tourStartDate,responseJSON, productCategory);
			}

		} else {
			// TODO : alert code for Ops user
			
		}
	}

	 
// TODO : remaining Holidays Code 
	private void sendHolidaysClientEmail(OpsProduct product, String beSearchResponse) throws OperationException {


//		String email = product.getOrderDetails().getPackageDetails().getPaxInfo().stream().filter(x->x.getIsLeadPax()).findFirst().get().getContactDetails().get(0).getEmail();
		String email = "anuradha.kumari@coxandkings.com";
		JSONArray holidayArray = new JSONObject(beSearchResponse).getJSONObject("responseBody").getJSONArray("accommodationInfo").getJSONObject(0).getJSONArray("roomStay");
		StringBuilder alternateOptionsRoomStaydetails = new StringBuilder();
		for(int count=0;count<holidayArray.length();count++){
			JSONObject roomStayObject = holidayArray.getJSONObject(count);
			
			alternateOptionsRoomStaydetails =  alternateOptionsRoomStaydetails.append(roomStayObject.getString("supplierRef")+" : "+roomStayObject.getString("accommodationSubType")+"   Price : "+
			roomStayObject.getJSONObject("totalPriceInfo").getBigDecimal("amount")+roomStayObject.getJSONObject("totalPriceInfo").getString("currencyCode")+"   nightlyPrice : "+roomStayObject.getJSONArray("nightlyPriceInfo").getJSONObject(0).getBigDecimal("amount")+roomStayObject.getJSONArray("nightlyPriceInfo").getJSONObject(0).getString("currencyCode"));
			alternateOptionsRoomStaydetails.append(String.format("%n", ""));
		}
		
		 Map<String, String> dynamicValuesMap =  new HashMap<String, String>();
		 dynamicValuesMap.put("name", product.getOrderDetails().getHotelDetails().getRooms().get(0).getPaxInfo().stream().filter(x-> x.getLeadPax()).findFirst().get().getFirstName());
	     dynamicValuesMap.put("alternateOptions", alternateOptionsRoomStaydetails.toString());
	     
	     // TODO : Accept/Reject API. Pending. Modify URI
	     dynamicValuesMap.put("accept", "<a href='\" + clientAcceptanceUrl + supplementOnSupplierPrice.getIdentifier() + \"'>Accept</a>");
	     dynamicValuesMap.put("reject", "<a href='\" + clientAcceptanceUrl + supplementOnSupplierPrice.getIdentifier() + \"'>Reject</a>");
		 emailUtils.buildClientMail(function, scenario, email, subject + product.getSupplierRefNumber(), dynamicValuesMap, null, null);



		
	}


	private String getFilteredHolidaysSearchResponseForHigherLowerLimits(OpsProduct product,
			String higherLimitThreshold, String lowerLimitThreshold, String beSearchResponse) {
		JSONObject holidayResponseObj = new JSONObject(beSearchResponse);
		JSONObject resonseBodyObj = holidayResponseObj.getJSONObject("responseBody");
		JSONArray holidayObj = resonseBodyObj.getJSONArray("dynamicPackage");
		
		// TODO : Confirmation required for extracting end price for customer from below statement 
		String sellingPrice =  product.getOrderDetails().getPackageDetails().getOrderClientTotalPriceInfo().getAmountAfterTax();
		BigDecimal sellingPriceInt = new BigDecimal(0);
		if(sellingPrice!= null && !sellingPrice.isEmpty()) {
			sellingPriceInt = new BigDecimal(sellingPrice);
			
			BigDecimal higherLimitSearch = new BigDecimal(0);
			BigDecimal lowerLimitSearch = new BigDecimal(0);
			
			higherLimitSearch = getHigherLimitSearch(higherLimitThreshold, sellingPriceInt, higherLimitSearch);
			
			lowerLimitSearch = getLowerLimitSearch(lowerLimitThreshold, sellingPriceInt, lowerLimitSearch);
			
			if(higherLimitSearch.floatValue() > 0 && lowerLimitSearch.floatValue() == 0)
				lowerLimitSearch = sellingPriceInt;
			else if(lowerLimitSearch.floatValue()>0 && higherLimitSearch.floatValue() == 0)
				higherLimitSearch = sellingPriceInt;
			else if(higherLimitSearch.floatValue() == 0 && lowerLimitSearch.floatValue() == 0) {}
			
			JSONArray filteredArray = new JSONArray();
			for(int count=0;count<holidayObj.length();count++){
				JSONObject holidayJSONObject = holidayObj.getJSONObject(count);
				BigDecimal totalPrice = holidayJSONObject.getJSONObject("globalInfo").getJSONObject("total").getBigDecimal("amountAfterTax");
				
				getModifiedFilteredArray(higherLimitSearch, lowerLimitSearch, filteredArray, holidayJSONObject,
						totalPrice);
			
			}
			
			resonseBodyObj.remove("dynamicPackage");
			resonseBodyObj.put("dynamicPackage", filteredArray);
			
			
			
			
		}
		
		return holidayResponseObj.toString();
	
	
	}


	private void sendActivitiesClientEmail(OpsProduct product, String beSearchResponse) {
		// TODO Auto-generated method stub
		
	}


	private String getFilteredActivitiesSearchResponseForHigherLowerLimits(OpsProduct product,
			String higherLimitThreshold, String lowerLimitThreshold, String beSearchResponse) {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * @param product
	 * @param responseJSON
	 * @param productCategory
	 */
	private void addAlternateOptionsResponseDetails(String leadPaxName,String tourEndDate,String tourStartDate, JSONArray responseJSON,
			String productCategory) {
		
		AlternateOptions alternateOptions = new AlternateOptions();
		alternateOptions.setConfigurationId(responseJSON.getJSONObject(0).getString("configurationId"));

		AlternateOptionsResponseDetails alternateOptionsResponseDetails = new AlternateOptionsResponseDetails();
		alternateOptionsResponseDetails.setLeadPaxName(leadPaxName);
		alternateOptionsResponseDetails.setProductCategory(productCategory);
		alternateOptionsResponseDetails.setStatus("Response Pending");
		alternateOptionsResponseDetails.setTourEndDate(tourEndDate);
		alternateOptionsResponseDetails.setTourStartDate(tourStartDate);
		//alternateOptionsResponseDetails.setAlternateOptions(alternateOptions);
		
		List<AlternateOptionsResponseDetails> alternateOptionsResponseDetails2 = new ArrayList<AlternateOptionsResponseDetails>();
		alternateOptionsResponseDetails2.add(alternateOptionsResponseDetails);
		
		alternateOptions.setAlternateOptionsResponseDetails(alternateOptionsResponseDetails2);
		
		alternateOptions.setAlternateOfferProcess(responseJSON.getJSONObject(0).getString("alternateOfferProcess"));
		
		alternateOptions.setClientType(responseJSON.getJSONObject(0).getString("clientType"));
		
		alternateOptions.setCompanyMarket(responseJSON.getJSONObject(0).getString("companymarket"));
		alternateOptions.setHigherLimitThreshold(responseJSON.getJSONObject(0).getString("higherLimit"));
		alternateOptions.setLowerLimitThreshold(responseJSON.getJSONObject(0).getString("lowerLimit"));
		
		//alternateOptions.setStatus(responseJSON.getJSONObject(0).getString("status"));
		alternateOptionsService.addAlternateOptionsResponseDetails(alternateOptions);
	}

	/**
	 * @param product
	 * @param beSearchResponse
	 * @throws OperationException 
	 */
	private void sendAccoClientEmail(OpsProduct product, String beSearchResponse) throws OperationException {

//					String email = product.getOrderDetails().getHotelDetails().getRooms().get(0).getPaxInfo().stream().filter(x-> x.getLeadPax()).findFirst().get().getContactDetails().get(0).getEmail();
					String email = "anuradha.kumari@coxandkings.com";
					JSONArray roomStayArray = new JSONObject(beSearchResponse).getJSONObject("responseBody").getJSONArray("accommodationInfo").getJSONObject(0).getJSONArray("roomStay");
					StringBuilder alternateOptionsRoomStaydetails = new StringBuilder();
					for(int count=0;count<roomStayArray.length();count++){
						JSONObject roomStayObject = roomStayArray.getJSONObject(count);
						
						alternateOptionsRoomStaydetails =  alternateOptionsRoomStaydetails.append(roomStayObject.getString("supplierRef")+" : "+roomStayObject.getString("accommodationSubType")+"   Price : "+
						roomStayObject.getJSONObject("totalPriceInfo").getBigDecimal("amount")+roomStayObject.getJSONObject("totalPriceInfo").getString("currencyCode")+"   nightlyPrice : "+roomStayObject.getJSONArray("nightlyPriceInfo").getJSONObject(0).getBigDecimal("amount")+roomStayObject.getJSONArray("nightlyPriceInfo").getJSONObject(0).getString("currencyCode"));
						alternateOptionsRoomStaydetails.append(String.format("%n", ""));
					}
					
					 Map<String, String> dynamicValuesMap =  new HashMap<String, String>();
					 dynamicValuesMap.put("name", product.getOrderDetails().getHotelDetails().getRooms().get(0).getPaxInfo().stream().filter(x-> x.getLeadPax()).findFirst().get().getFirstName());
				     dynamicValuesMap.put("alternateOptions", alternateOptionsRoomStaydetails.toString());
				     dynamicValuesMap.put("accept", "<a href='\" + clientAcceptanceUrl + supplementOnSupplierPrice.getIdentifier() + \"'>Accept</a>");
				     dynamicValuesMap.put("reject", "<a href='\" + clientAcceptanceUrl + supplementOnSupplierPrice.getIdentifier() + \"'>Reject</a>");
					 emailUtils.buildClientMail(function, scenario, email, subject + product.getSupplierRefNumber(), dynamicValuesMap, null, null);
	
		
	}

	/**
	 * @param product
	 * @param higherLimitThreshold
	 * @param lowerLimitThreshold
	 * @param beSearchResponse
	 */
	private String getFilteredAccoSearchResponseForHigherLowerLimits(OpsProduct product, String higherLimitThreshold,
			String lowerLimitThreshold, String beSearchResponse) {

		JSONObject roomStayResponseObj = new JSONObject(beSearchResponse);
		JSONObject resonseBodyObj = roomStayResponseObj.getJSONObject("responseBody");
		JSONObject accomodationObj = resonseBodyObj.getJSONArray("accommodationInfo").getJSONObject(0);
		JSONArray roomStay = accomodationObj.getJSONArray("roomStay");
		
		String sellingPrice =  product.getOrderDetails().getHotelDetails().getRooms().get(0).getRoomTotalPriceInfo().getRoomTotalPrice();
		BigDecimal sellingPriceInt = new BigDecimal(0);
		if(sellingPrice!= null && !sellingPrice.isEmpty()) {
			sellingPriceInt = new BigDecimal(sellingPrice);
			
			BigDecimal higherLimitSearch = new BigDecimal(0);
			BigDecimal lowerLimitSearch = new BigDecimal(0);
			
			higherLimitSearch = getHigherLimitSearch(higherLimitThreshold, sellingPriceInt, higherLimitSearch);
			
			lowerLimitSearch = getLowerLimitSearch(lowerLimitThreshold, sellingPriceInt, lowerLimitSearch);
			
			if(higherLimitSearch.floatValue() > 0 && lowerLimitSearch.floatValue() == 0)
				lowerLimitSearch = sellingPriceInt;
			else if(lowerLimitSearch.floatValue()>0 && higherLimitSearch.floatValue() == 0)
				higherLimitSearch = sellingPriceInt;
			else if(higherLimitSearch.floatValue() == 0 && lowerLimitSearch.floatValue() == 0) {}
			
			JSONArray filteredArray = new JSONArray();
			for(int count=0;count<roomStay.length();count++){
				JSONObject roomStayObject = roomStay.getJSONObject(count);
				BigDecimal totalPrice = roomStayObject.getJSONObject("totalPriceInfo").getBigDecimal("amount");
				
				getModifiedFilteredArray(higherLimitSearch, lowerLimitSearch, filteredArray, roomStayObject,
						totalPrice);
			
			}
			
			accomodationObj.remove("roomStay");
			accomodationObj.put("roomStay", filteredArray);
			
			
			
			
		}
		
		return roomStayResponseObj.toString();
	
	}

	/**
	 * @param lowerLimitThreshold
	 * @param sellingPriceInt
	 * @param lowerLimitSearch
	 * @return
	 */
	private BigDecimal getLowerLimitSearch(String lowerLimitThreshold, BigDecimal sellingPriceInt,
			BigDecimal lowerLimitSearch) {
		BigDecimal lowerLimitThresholdint = new BigDecimal(0);
		if(lowerLimitThreshold != null && !lowerLimitThreshold.isEmpty()) {
			lowerLimitThresholdint = new BigDecimal(lowerLimitThreshold);
			if(lowerLimitThresholdint.floatValue()<sellingPriceInt.floatValue())
			lowerLimitSearch = sellingPriceInt.subtract(lowerLimitThresholdint);
		}
		return lowerLimitSearch;
	}

	/**
	 * @param higherLimitThreshold
	 * @param sellingPriceInt
	 * @param higherLimitSearch
	 * @return
	 */
	private BigDecimal getHigherLimitSearch(String higherLimitThreshold, BigDecimal sellingPriceInt,
			BigDecimal higherLimitSearch) {
		BigDecimal higherLimitThresholdint = new BigDecimal(0);
		if(higherLimitThreshold != null && !higherLimitThreshold.isEmpty()) {
			higherLimitThresholdint = new BigDecimal(higherLimitThreshold);
			higherLimitSearch = sellingPriceInt.add(higherLimitThresholdint);
		}
		return higherLimitSearch;
	}

	/**
	 * @param higherLimitSearch
	 * @param lowerLimitSearch
	 * @param filteredArray
	 * @param jsonObj
	 * @param totalPrice
	 */
	private void getModifiedFilteredArray(BigDecimal higherLimitSearch, BigDecimal lowerLimitSearch,
			JSONArray filteredArray, JSONObject jsonObj, BigDecimal totalPrice) {
		if(totalPrice.floatValue()<=higherLimitSearch.floatValue() && totalPrice.floatValue()>=lowerLimitSearch.floatValue()) {
			filteredArray.put(jsonObj);
		}
	}

	/**
	 * @param product
	 * @param beSearchResponse
	 * @throws OperationException
	 */
	private void sendAirClientEmail(OpsProduct product, String beSearchResponse) throws OperationException {
		//			String email = product.getOrderDetails().getFlightDetails().getPaxInfo().stream().filter(x -> x.getLeadPax()).findFirst().get().getContactDetails().get(0).getContactInfo().getEmail();
					String email = "anuradha.kumari@coxandkings.com";
					JSONArray pricedItenary = new JSONObject(beSearchResponse).getJSONObject("responseBody").getJSONArray("pricedItinerary");
					StringBuilder alternateOptionsFlightdetails = new StringBuilder();
					for(int count=0;count<pricedItenary.length();count++){
						JSONObject pricedItenaryObject = pricedItenary.getJSONObject(count);
						JSONObject flightSegment = pricedItenaryObject.getJSONObject("airItinerary").getJSONArray("originDestinationOptions").getJSONObject(0).getJSONArray("flightSegment").getJSONObject(0);
						alternateOptionsFlightdetails =  alternateOptionsFlightdetails.append(flightSegment.getJSONObject("operatingAirline").getString("airlineCode")+"-"+flightSegment.getJSONObject("operatingAirline").getString("flightNumber")+"  "+
						flightSegment.getString("originLocation")+"-"+flightSegment.getString("destinationLocation")+"  "+"departureDate : "+flightSegment.getString("departureDate")+"  "+"arrivalDate : "+
						flightSegment.getString("arrivalDate")+"  "+"cabinType : "+flightSegment.getString("cabinType"));
						alternateOptionsFlightdetails.append(String.format("%n", ""));
					}
					
					 Map<String, String> dynamicValuesMap =  new HashMap<String, String>();
					 dynamicValuesMap.put("name", product.getOrderDetails().getFlightDetails().getPaxInfo().get(0).getFirstName());
				     dynamicValuesMap.put("alternateOptions", alternateOptionsFlightdetails.toString());
				     dynamicValuesMap.put("accept", "<a href='\" + clientAcceptanceUrl + supplementOnSupplierPrice.getIdentifier() + \"'>Accept</a>");
				     dynamicValuesMap.put("reject", "<a href='\" + clientAcceptanceUrl + supplementOnSupplierPrice.getIdentifier() + \"'>Reject</a>");
					 emailUtils.buildClientMail(function, scenario, email, subject + product.getSupplierRefNumber(), dynamicValuesMap, null, null);
	}

	/**
	 * @param product
	 * @param higherLimitThreshold
	 * @param lowerLimitThreshold
	 * @param beSearchResponse
	 */
	private String getFilteredAirSearchResponseForHigherLowerLimits(OpsProduct product, String higherLimitThreshold,
			String lowerLimitThreshold, String beSearchResponse) {
		JSONObject airSearchresponseObj = new JSONObject(beSearchResponse);
		JSONObject resonseBodyObj = airSearchresponseObj.getJSONObject("responseBody");
		JSONArray pricedItenaryArray = resonseBodyObj.getJSONArray("pricedItinerary");
		
		String sellingPrice =  product.getOrderDetails().getFlightDetails().getTotalPriceInfo().getTotalPrice();
		BigDecimal sellingPriceInt = new BigDecimal(0);
		if(sellingPrice!= null && !sellingPrice.isEmpty()) {
			sellingPriceInt = new BigDecimal(sellingPrice);
			
			BigDecimal higherLimitSearch = new BigDecimal(0);
			BigDecimal lowerLimitSearch = new BigDecimal(0);
			
			higherLimitSearch = getHigherLimitSearch(higherLimitThreshold, sellingPriceInt, higherLimitSearch);
			
			lowerLimitSearch = getLowerLimitSearch(lowerLimitThreshold, sellingPriceInt, lowerLimitSearch);
			
			if(higherLimitSearch.floatValue() > 0 && lowerLimitSearch.floatValue() == 0)
				lowerLimitSearch = sellingPriceInt;
			else if(lowerLimitSearch.floatValue()>0 && higherLimitSearch.floatValue() == 0)
				higherLimitSearch = sellingPriceInt;
			else if(higherLimitSearch.floatValue() == 0 && lowerLimitSearch.floatValue() == 0) {}
			
			JSONArray filteredArray = new JSONArray();
			for(int count=0;count<pricedItenaryArray.length();count++){
				JSONObject pricedItenaryObject = pricedItenaryArray.getJSONObject(count);
				BigDecimal totalPrice = pricedItenaryObject.getJSONObject("airItineraryPricingInfo").getJSONObject("itinTotalFare").getBigDecimal("amount");
				
				getModifiedFilteredArray(higherLimitSearch, lowerLimitSearch, filteredArray, pricedItenaryObject,
						totalPrice);
			
			}
			
			resonseBodyObj.remove("pricedItinerary");
			resonseBodyObj.put("pricedItinerary", filteredArray);
			
		}
		return airSearchresponseObj.toString();
	}

	

	@Override
	public BookingListenerType getListenerType() {
		return BookingListenerType.ALTERNATE_OPTIONS;
	}

    @Lookup
    public AlternateOptionsBookingListenerImpl getAlternateOptionsBookingListener() {
        return null;
    }

}
