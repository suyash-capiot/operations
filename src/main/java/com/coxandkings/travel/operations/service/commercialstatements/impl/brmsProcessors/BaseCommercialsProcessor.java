package com.coxandkings.travel.operations.service.commercialstatements.impl.brmsProcessors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.coxandkings.travel.operations.service.commercialstatements.impl.Constants;
import com.coxandkings.travel.operations.service.commercialstatements.impl.RestUtilities;
import com.coxandkings.travel.operations.service.qcmanagement.QcUtilService;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.coxandkings.travel.operations.utils.RestUtils;

@Component
public abstract class BaseCommercialsProcessor implements Constants{

	@Autowired
	private CommercialsProcessorFactory prcsrFactry;

	@Autowired
	protected  RestUtilities restUtils;
	
	@Autowired
	protected  MDMRestUtils mdmRestUtils;

	private static final Logger logger = LogManager.getLogger(BaseCommercialsProcessor.class);

	protected String commEngURl;
	protected String commEngRqShell;
	protected String briRqRootKey;

	@Autowired
	protected QcUtilService qcUtilService;

	public List<OrderWiseCommercialDetails> process(JSONArray bookingJsonArr,CommercialProcessorUtils utils){
		List<OrderWiseCommercialDetails> resultLst = new ArrayList<OrderWiseCommercialDetails>();
		Map<BaseCommercialsProcessor,BRIDetails> prodBriMap = new HashMap<BaseCommercialsProcessor,BRIDetails>();
		Map<BaseCommercialsProcessor,List<OrderWiseCommercialDetails>> prodOrdrMap = new HashMap<BaseCommercialsProcessor,List<OrderWiseCommercialDetails>>();
		for(int i=0;i<bookingJsonArr.length();i++) {
			JSONObject bookingJson = (JSONObject) bookingJsonArr.get(i);
			JSONArray orderJsonArr = (JSONArray) bookingJson.getJSONObject(JSON_PROP_RESBODY).remove("products");
			BRIDetails briDtls;
			List<OrderWiseCommercialDetails> ordrLst;
			for(int j=0;j<orderJsonArr.length();j++) {
				JSONObject orderJson = (JSONObject) orderJsonArr.get(j);
				String prodCateg = orderJson.optString("productCategory");
				String prodCategSubType = orderJson.optString("productSubCategory");
				BaseCommercialsProcessor prcsr = prcsrFactry.getProcessor(prodCateg, prodCategSubType,utils.getSettlementDetails().getCommercialFor());
				if(prcsr==null)
					continue;
				briDtls = prodBriMap.get(prcsr);
				if(briDtls==null) {
					briDtls = new BRIDetails();
					prodBriMap.put(prcsr, briDtls);
				}
				ordrLst = prodOrdrMap.get(prcsr);
				if(ordrLst==null) {
					ordrLst = new ArrayList<OrderWiseCommercialDetails>();
					prodOrdrMap.put(prcsr, ordrLst);
				}
				OrderWiseCommercialDetails ordrDtls = new OrderWiseCommercialDetails();
				ordrLst.add(ordrDtls);
				String briKey=prcsr.getBRIKey(bookingJson, orderJson);
				BRINode briNode= briDtls.get(briKey);
				if(briNode==null) {
					briDtls.add(briKey, prcsr.createBRIAndPopulateData(bookingJson, orderJson,utils,ordrDtls));
					briNode = briDtls.get(briKey);
				}
				else {
					prcsr.updateBRIAndPopulateData(briNode.briJson, bookingJson, orderJson,utils,ordrDtls);
				}
				ordrDtls.setBriIdx(briNode.index);
				//prcsrSet.add(prcsr);
				//OrderWiseCommercialDetails ordrDtls = new OrderWiseCommercialDetails();
				//ordrLst.add(ordrDtls);
				//bookingJson.getJSONObject(JSON_PROP_RESBODY).append("products", orderJson);
				//prcsr.populateData(briMap,ordrLst,bookingJson,slabData, settlementDef);
				//bookingJson.getJSONObject(JSON_PROP_RESBODY).remove("products");
			}              
		}
		Iterator<BaseCommercialsProcessor> itr = prodBriMap.keySet().iterator();
		while (itr.hasNext()) 
		{
			BaseCommercialsProcessor prcsr = itr.next();
			BRIDetails briDtls = prodBriMap.get(prcsr);
			List<OrderWiseCommercialDetails> ordrDtlsLst = prodOrdrMap.get(prcsr);
			resultLst.addAll(execute(prcsr,briDtls,ordrDtlsLst));//change to linked list
		}
		return resultLst;
	}

	private List<OrderWiseCommercialDetails> execute(BaseCommercialsProcessor prcsr, BRIDetails briDtls,
			List<OrderWiseCommercialDetails> ordrDtlsLst) {
		JSONObject commResJson = prcsr.getCommercialsResponse(briDtls.briArray);
		if(commResJson==null)
			return new ArrayList<OrderWiseCommercialDetails>();
		prcsr.calculatePrices(commResJson, ordrDtlsLst);
		return ordrDtlsLst;
	}

	protected JSONObject getCommercialsResponse(JSONArray briArr) {
		JSONObject breSuppReqJson = new JSONObject(commEngRqShell);
		JSONObject breSuppResJson =new JSONObject();
		JSONObject breHdrJson = createHeader();
		JSONObject rootJson = breSuppReqJson.getJSONArray("commands").getJSONObject(0).getJSONObject("insert").getJSONObject("object").getJSONObject(briRqRootKey);
		rootJson.put(JSON_PROP_HEADER, breHdrJson);
		rootJson.put(JSON_PROP_BUSSRULEINTAKE,briArr);
		String brmsReqStr = breSuppReqJson.toString(0);
		logger.info(String.format("%s_RQ: %s", this.getClass().getName(),brmsReqStr));
		if (breSuppReqJson != null) {
			HttpEntity<String> requestEntity = qcUtilService.createHeader(brmsReqStr);
			String brmsResStr = "";
			try {
				brmsResStr = RestUtils.exchange(commEngURl, HttpMethod.POST, requestEntity, String.class).getBody();
				breSuppResJson = new JSONObject(brmsResStr);
			} catch (Exception e) {
				logger.error("Error While Retreiving Supplier Commercial From BRMS " + e);
			}
			logger.info(String.format("%s_RS: %s", this.getClass().getName(),brmsResStr));

		}
		return breSuppResJson;
	}

	protected JSONObject createHeader() {
		JSONObject breHdrJson = new JSONObject();

		breHdrJson.put(JSON_PROP_SESSIONID, this.getClass().getName());
		breHdrJson.put(JSON_PROP_TRANSACTID, System.currentTimeMillis());
		breHdrJson.put(JSON_PROP_USERID, "Operations");
		breHdrJson.put(JSON_PROP_OPERATIONNAME, "Booking");//as this would be called only after book

		return breHdrJson;
	}
	
	private class BRIDetails{
		private Map<String,BRINode> briMap=new HashMap<String,BRINode>();
		private JSONArray briArray= new JSONArray();
		
		public void add(String briKey,JSONObject briJson) {
			BRINode node = briMap.get(briKey);
			if(node==null) {
				briMap.put(briKey, new BRINode(briJson,briArray.length()));
				briArray.put(briJson);
			}
			/*else {
				node.briJson = briJson;
			}
			return node;*/
		}
		
		public BRINode get(String briKey) {
			return briMap.get(briKey);
		}
	}
	
   private class BRINode{
		private JSONObject briJson;
		private int index;
		
		BRINode(JSONObject briJson,int index) {
			this.briJson = briJson;
			this.index = index;
		}
		
		public JSONObject getBRIJson() {
			return briJson;
		}
		
		public int getBRIPosition() {
			return index;
		}
	}
   
   protected void populateOrderObject(OrderWiseCommercialDetails ordrDtls,JSONObject bookingJson,JSONObject productJson) {
		JSONObject resBody = bookingJson.getJSONObject(JSON_PROP_RESBODY);
		JSONObject resHdr = bookingJson.getJSONObject(JSON_PROP_RESHEADER);
		ordrDtls.setBookid(resBody.getString("bookID"));
		ordrDtls.setBookingDate(resBody.getString("bookingDate"));
		ordrDtls.setCurrency(resHdr.getJSONObject("clientContext").getString("clientCurrency"));
		ordrDtls.setMarket(resHdr.getJSONObject("clientContext").getString("clientMarket"));//TODO:change this
		ordrDtls.setProdCateg(productJson.optString("productCategory"));
		ordrDtls.setProdSubCateg(productJson.optString("productSubCategory"));
	}
   
   protected abstract String getBRIKey(JSONObject bookingJson, JSONObject productJson);
   protected abstract JSONObject createBRIAndPopulateData(JSONObject bookingJson, JSONObject productJson,
			CommercialProcessorUtils utils, OrderWiseCommercialDetails ordrDtls);
   protected abstract void updateBRIAndPopulateData(JSONObject briJson, JSONObject bookingJson, JSONObject productJson,
			CommercialProcessorUtils utils, OrderWiseCommercialDetails ordrDtls);
   protected abstract void calculatePrices(JSONObject supplierCommercialRS,List<OrderWiseCommercialDetails> orderDtlsLst);
}
