package com.coxandkings.travel.operations.service.commercialstatements.impl.slabbing;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coxandkings.travel.operations.service.commercialstatements.impl.Constants;
import com.coxandkings.travel.operations.service.commercialstatements.impl.RestUtilities;

@Component
public class SlabbingUtils implements Constants {
	
	@Autowired
	private RestUtilities mdmUtils;
	
	@Autowired
	private SlabTypeCalculatorFactory slabCalcFactry;

	private Map<String,JSONArray> prodSlabTypeMap=new HashMap<>();
	
	public JSONArray getSlabTypes(String prodCateg, String prodCategSubType){
		String key = String.format("%s|%s",prodCateg,prodCategSubType);
		JSONArray slabJsonArr=prodSlabTypeMap.get(key);
		if(slabJsonArr==null) {
			slabJsonArr = mdmUtils.getMDMSlabTypes(prodCateg, prodCategSubType);
			prodSlabTypeMap.put(key,slabJsonArr);
		}
		return slabJsonArr==null?new JSONArray():slabJsonArr;
	}
	
	 public SlabbingTree constructSlabData(JSONArray bookingJsonArray) {
	    	SlabbingTree slabData = new SlabbingTree();
	    	if(bookingJsonArray==null)
	    		return slabData;
	    	for(int i=0;i<bookingJsonArray.length();i++) {
	    		addSlabData((JSONObject) bookingJsonArray.get(i),slabData);
			}
	    	return slabData;
	 }
	 
	 public void addSlabData(JSONObject bookingJson,SlabbingTree slabData) {
		 JSONArray orderJsonArr = (JSONArray) bookingJson.getJSONObject(JSON_PROP_RESBODY).get("products");
 		//In BE suppliers are derived from client market
 		//If this booking is done,that means supplier supports that client market
 		//TODO:This is current understanding. Subject to change
 		String suppMkt = bookingJson.getJSONObject(JSON_PROP_RESHEADER).getJSONObject("clientContext").getString("clientMarket");
 		SlabProperties slabProps = new SlabProperties();
 		slabProps.setMarket(suppMkt);
			for(int j=0;j<orderJsonArr.length();j++) {
				JSONObject orderJson = (JSONObject) orderJsonArr.get(j);
				String prodCateg = orderJson.optString("productCategory");
				String prodCategSubType = orderJson.optString("productSubCategory");
				slabProps.setProductCateg(prodCateg);
				slabProps.setProductCategSubType(prodCategSubType);
				slabProps.setSupplierID(orderJson.getString("supplierID"));
				JSONArray slabTypes=getSlabTypes(prodCateg, prodCategSubType);
				SlabTypeCalculator slabCalctr = slabCalcFactry.getInstance(prodCateg,prodCategSubType);
				if(slabCalctr==null)
					continue;
				for(int k=0;k<slabTypes.length();k++) {
					JSONObject slabTypeJson= (JSONObject) slabTypes.get(k);
					String slabType=slabTypeJson.getJSONObject("data").getString("value");
					slabData.put(slabProps, slabType, slabCalctr.getSlabValue(orderJson, slabType));
				}
			}
	 }

}
