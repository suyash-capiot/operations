package com.coxandkings.travel.operations.service.commercialstatements.impl;

import java.lang.reflect.Field;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.coxandkings.travel.ext.model.be.Booking;
import com.coxandkings.travel.operations.config.ZonedDateTimeConverter;
import com.coxandkings.travel.operations.criteria.booking.becriteria.BookingDetailsFilter;
import com.coxandkings.travel.operations.criteria.booking.becriteria.BookingSearchCriteria;
import com.coxandkings.travel.operations.criteria.booking.becriteria.ProductDetailsFilter;
import com.coxandkings.travel.operations.enums.commercialStatements.CommercialStatementFor;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.resource.searchviewfilter.BookingSearchResponseItem;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.coxandkings.travel.operations.utils.RESTErrorHandler;
import com.coxandkings.travel.operations.utils.RestUtils;

@Component
@Service
public class Utils {

	@Autowired
	private OpsBookingService opsBookingService;

	@Value(value = "${mdm.getSlabTypes}")
	private  String getSlabTypes;

	@Autowired
	private  MDMRestUtils mdmRestUtils;

	//            @Value(value = "${booking_engine_db_services}")

	//@Value(value = "${booking_engine.getBookingsSlabwise}")
	//private  String searchbkngs;

	//@Value(value = "${booking_engine.searchBookings}")
	//private  static String searchBookings;

	public static void main(String args[]) throws Exception {
		System.out.println(ZonedDateTime.parse(getZonedDateTimeString("2018-07-22"),DateTimeFormatter.ISO_OFFSET_DATE_TIME));
		//System.out.println(searchBookings);
		String a= new String("abc");
		String b= "abc";
		getbyCriteria("TOURICO",("2018-06-06"),("2018-06-08"));
		Field field = String.class.getDeclaredField("value");
		field.setAccessible(true);
		char[] value = (char[])field.get(a);
		value[0]='b';
		String c= "abc";
		temp();
	}

	static void temp() {
		String d= "abc";
	}

	static String getZonedDateTimeString(String date) {
		String defTime ="T00:00:00";
		String defOffset =ZonedDateTime.now().getOffset().toString();
		String defZone =ZonedDateTime.now().getZone().toString();
		if(date.length()==10)
			return String.format("%s%s%s", date,defTime,defOffset);
		else if(date.length()==19)
			return String.format("%s%s", date,defOffset);
		else if(date.length()==25)
			return date;
		else
			return "";
	}
	public static int ordinalIndexOf(String str, char substr, int n) {
		int pos = str.indexOf(substr);
		while (--n > 0 && pos != -1)
			pos = str.indexOf(substr, pos + 1);
		return pos;
	}

	public List<BookingSearchResponseItem> getSupplierWiseRangeBookings(String suppId, String fromDate, String toDate) {
		BookingSearchCriteria bkngSrchCriteria = new BookingSearchCriteria();
		BookingDetailsFilter bkngFilter = new BookingDetailsFilter();
		ProductDetailsFilter prdctFilter = new ProductDetailsFilter();
		//TODO:set nulls and empty check
		//TODO:take date here
		bkngFilter.setBookingFromDate(fromDate);
		bkngFilter.setBookingToDate(toDate);
		prdctFilter.setSupplierName(suppId);
		bkngSrchCriteria.setBookingBasedFilter(bkngFilter);
		bkngSrchCriteria.setProductBasedFilter(prdctFilter);
		//TODO:remove hardcoding
		bkngSrchCriteria.setPage(1);
		bkngSrchCriteria.setSize(10000000);
		return opsBookingService.searchBookings(bkngSrchCriteria);
	}

	public static String getbyCriteria(String suppId, String fromDate, String toDate) {
		BookingSearchCriteria bkngSrchCriteria = new BookingSearchCriteria();
		BookingDetailsFilter bkngFilter = new BookingDetailsFilter();
		ProductDetailsFilter prdctFilter = new ProductDetailsFilter();
		//TODO:set nulls and empty check
		//TODO:take date here
		bkngFilter.setBookingFromDate(getZonedDateTimeString(fromDate));
		System.out.println(bkngFilter.getBookingFromDate());
		bkngFilter.setBookingToDate(getZonedDateTimeString(toDate));
		prdctFilter.setSupplierName(suppId);
		bkngSrchCriteria.setBookingBasedFilter(bkngFilter);
		bkngSrchCriteria.setProductBasedFilter(prdctFilter);

		//TODO:remove hardcoding
		bkngSrchCriteria.setPage(1);
		bkngSrchCriteria.setSize(10000000);
		return getBookingByCriteriaSlabWise(bkngSrchCriteria).toString();
	}

	
	public static JSONArray getBookingFromFile() {
		try {
			//return new JSONArray(new String(Files.readAllBytes(Paths.get("D:/Temp/SampleAccoBookingJson"))));
			return new JSONArray(new String(Files.readAllBytes(Paths.get("D:/Temp/SampleAirBookingJson"))));
			
		} catch (Exception e) {
			return new JSONArray();
		} 
	}
	
	public static JSONArray getAccoRateFromFile() {
		try {
			return new JSONArray(new String(Files.readAllBytes(Paths.get("D:/Temp/SampleAccoRateJson"))));
			
		} catch (Exception e) {
			return new JSONArray();
		} 
	}
	
	public static JSONArray getSettlementTermsFromFile(CommercialStatementFor commFor) {
		try {
			String path = commFor == CommercialStatementFor.SUPPLIER?"D:/Temp/SampleSuppSettlementJson":"D:/Temp/SampleClientSettlementJson";
			return new JSONArray(new String(Files.readAllBytes(Paths.get(path))));
			
		} catch (Exception e) {
			return new JSONArray();
		} 
	}
	
	public static JSONArray getBookingByCriteriaSlabWise(BookingSearchCriteria bookingSearchCriteria) {

		String searchResult = null;
		List<Booking> bookings = null;

		final String uri = "http://10.29.17.84:2019/BookingService/getBookingsBySearchSlab";
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(uri);


		RestTemplate restTemplate = RestUtils.getTemplate();
		restTemplate.setErrorHandler(new RESTErrorHandler());
		HttpEntity httpEntity = new HttpEntity(bookingSearchCriteria);

		//searchResult  = restTemplate.getForObject(uri, String.class,httpEntity);

		ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);


		// bookings = searchResult.getBody();
		searchResult = result.getBody();


		//System.out.println("SearchResult"+searchResult);
		return searchResult!=null && !searchResult.isEmpty()?new JSONArray(searchResult):new JSONArray();
	}



	public ResponseEntity<String> getMDMSlabTypes(String prdctCateg,String prdctCategSubType) throws Exception{
		JSONObject queryJson = new JSONObject();
		queryJson.put("data.productCategory", prdctCateg);
		queryJson.put("data.productCategorySubType", prdctCategSubType);

		URI uri=UriComponentsBuilder.fromUriString(String.format("%s%s",getSlabTypes,queryJson.toString())).build().encode().toUri();
		return mdmRestUtils.exchange(uri, HttpMethod.GET, null, String.class);
	}
}
