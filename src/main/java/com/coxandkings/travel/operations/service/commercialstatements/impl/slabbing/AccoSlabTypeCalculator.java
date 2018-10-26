package com.coxandkings.travel.operations.service.commercialstatements.impl.slabbing;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
class AccoSlabTypeCalculator extends SlabTypeCalculator{
	
	@Override
	public BigDecimal getSlabValue(JSONObject orderJson, String slabType) {
		//change to enums
		switch(slabType){
		case "No of Bookings" : 
		      return getNoOfBookings(orderJson);	
		case "No of Passengers" : 
			  return getNoOfPassengers(orderJson);
		case "No of Nights" :
			  return getNoOfNights(orderJson);
		case "No of Rooms" :
			  return getNoOfRooms(orderJson);
		default:
			return new BigDecimal(0);
		}
	}	

	private BigDecimal getNoOfRooms(JSONObject orderJson) {
		return new BigDecimal(getRooms(orderJson).length());
	}
	
	private BigDecimal getNoOfPassengers(JSONObject orderJson) {
		int noOfPassengers=0;
		JSONArray rooms = getRooms(orderJson);
		for(int i=0;i<rooms.length();i++)
		{
			JSONArray paxArray = rooms.getJSONObject(i).getJSONArray("paxInfo");
			noOfPassengers+=paxArray.length();
		}
		return new BigDecimal(noOfPassengers);
	}
	
	private BigDecimal getNoOfNights(JSONObject orderJson) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		JSONArray rooms = getRooms(orderJson);
		int noOfNights=0;
		try {
		if(rooms.length()>0){
			String checkindate = rooms.getJSONObject(0).getString("checkIn");
			String checkoutdate = rooms.getJSONObject(0).getString("checkOut");
			Date firstDate =  sdf.parse(checkindate);
		    Date secondDate =  sdf.parse(checkoutdate);
		    long difference = secondDate.getTime() - firstDate.getTime();
            float nightsBetween = (difference / (1000*60*60*24));
            noOfNights = (int)nightsBetween;;
		}
		return new BigDecimal(noOfNights);
		}
		catch(Exception e) {}
		return new BigDecimal(0);
		/*int noOfPassengers=0;
		JSONArray rooms = getRooms(orderJson);
		for(int i=0;i<rooms.length();i++)
		{
			JSONArray paxArray = rooms.getJSONObject(i).getJSONArray("paxInfo");
			noOfPassengers+=paxArray.length();
		}
		return new BigDecimal(noOfPassengers);*/
	}
	
	private JSONArray getRooms(JSONObject orderJson) {
		return orderJson.getJSONObject("orderDetails").getJSONObject("hotelDetails").getJSONArray("rooms");
	}
	
}