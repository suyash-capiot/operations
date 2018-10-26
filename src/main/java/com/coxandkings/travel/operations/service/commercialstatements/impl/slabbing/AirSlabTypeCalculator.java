package com.coxandkings.travel.operations.service.commercialstatements.impl.slabbing;

import java.math.BigDecimal;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
class AirSlabTypeCalculator extends SlabTypeCalculator{

	@Override
	public BigDecimal getSlabValue(JSONObject orderJson, String slabType) {
		switch(slabType){
		case "No of Bookings" : 
		      return getNoOfBookings(orderJson);
		case "No of Passengers" : 
			  return getNoOfPassengers(orderJson);
		case "Total No. Ticketed Passive Segments" :
			  return getNoOfPassiveSegments(orderJson);
		case "Total No. of Active Segments" :
			  return getNoOfActiveSegments(orderJson);
		case "Revenue" :
			  return getRevenue(orderJson);
		default:
			return BigDecimal.ZERO;
		}
	}

	private BigDecimal getRevenue(JSONObject orderJson) {
		//TODO : ADD LOGIC 
		//slabTree.get(slabProp).forEach(slab->System.out.println(String.format(" {type:%s value:%s} ", slab.getSlabType(),slab.getSlabValue())));
		return BigDecimal.ZERO;
	}

	private BigDecimal getNoOfActiveSegments(JSONObject orderJson) {
		//TODO : ADD LOGIC 
		return BigDecimal.ZERO;
	}

	private BigDecimal getNoOfPassiveSegments(JSONObject orderJson) {
		//TODO : ADD LOGIC 
		return BigDecimal.ZERO;
	}

	private BigDecimal getNoOfPassengers(JSONObject orderJson) {
		JSONArray paxInfoJson = orderJson.getJSONObject("orderDetails").getJSONArray("paxInfo");
		return new BigDecimal(paxInfoJson.length());
	}

}
