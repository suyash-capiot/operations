package com.coxandkings.travel.operations.service.commercialstatements.impl.slabbing;

import java.math.BigDecimal;

import org.json.JSONObject;

import com.coxandkings.travel.operations.service.commercialstatements.impl.Constants;

abstract class SlabTypeCalculator  implements Constants {
	
	public abstract BigDecimal getSlabValue(JSONObject orderJson,String slabType);
	
	protected BigDecimal getNoOfBookings(JSONObject orderJson) {
		return new BigDecimal(1);
	}

}
