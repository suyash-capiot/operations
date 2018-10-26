package com.coxandkings.travel.operations.service.commercialstatements.impl.settlementTerms;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.coxandkings.travel.operations.enums.commercialStatements.CommercialStatementFor;

@Component
public class ClientSettlementTerms extends SettlementTerms{

	private HashMap<String,List<ClientSettlementDetails>> periodWiseCommMap = new HashMap<String,List<ClientSettlementDetails>>();
	private HashMap<String,List<ClientSettlementDetails>> clientWiseCommMap = new HashMap<String,List<ClientSettlementDetails>>();

	
	{
		commStatementFor = CommercialStatementFor.CLIENT;
	}
	
	private void updateClientSettlementTerms() {
    	JSONArray dataArr = getUpdatedTerms();
    	for(int i=0;i<dataArr.length();i++) {
			JSONObject dataJson = dataArr.getJSONObject(i);
			String clientId= dataJson.getString("entityId");
			List<ClientSettlementDetails> commHeadLst = clientWiseCommMap.get(clientId);
			boolean deleted = dataJson.getBoolean("deleted");
			if(commHeadLst!=null) {
				for(ClientSettlementDetails commHead:commHeadLst) {
					periodWiseCommMap.get(commHead.periodicityMapKey).remove(commHead);
				}
				commHeadLst.removeAll(commHeadLst);
			}
			if(!deleted) {
				populateSettlementTerms(dataJson);
				return;
			}
		}
    }

	@Override
	protected void populateSettlementTerms(JSONObject dataJson) {
		JSONObject recPay=new JSONObject();
		JSONObject periodicity = new JSONObject();
		try {
			String clientId= dataJson.getString("entityId");
			String companyMarket = dataJson.getJSONObject("settlementAttachedTo").getString("companyMarket");
			String entityName = dataJson.getJSONObject("settlementAttachedTo").getString("entityName");
			JSONArray settlementTerms = dataJson.getJSONArray("settlementTerms");
			JSONObject settlementAttachedTo = dataJson.getJSONObject("settlementAttachedTo");
			for (int i = 0; i < settlementTerms.length(); i++) {
				JSONObject settlementTermObj = settlementTerms.getJSONObject(i);
	    		String commercialType = settlementTermObj.getString("commercialType");
	    		if(commercialType.equalsIgnoreCase("receivable"))
	    		{
	    			recPay = settlementTermObj.getJSONObject("receivable");
	    			periodicity = recPay.getJSONObject("periodicity");
	    			populateSettlementTerms(periodicity, clientId, recPay.getString("settlementSchedule"),settlementAttachedTo,settlementTermObj.getString("commercialHead"),commercialType);
	    		}
	    		else
	    		{
	    			recPay = settlementTermObj.getJSONObject("payable");
	    			if(settlementTermObj.getString("commercialHead").toLowerCase().contains("standard"))
	    			{
	    				JSONObject commissionable = new JSONObject();
	    				JSONObject commPayToClient = new JSONObject();
	    				JSONObject spPayToComp= new JSONObject();
	    				JSONObject standard = recPay.getJSONObject("standard");
	    				boolean isCommissionable = standard.getBoolean("isCommissionable");
	    				if(isCommissionable)
	    				{
	    					commissionable = standard.getJSONObject("commissionable");
	    					spPayToComp = commissionable.getJSONObject("sellingPricePayableToCompany");
	    					periodicity = spPayToComp.getJSONObject("periodicity");
	    					populateSettlementTerms(periodicity, clientId, spPayToComp.getString("settlementSchedule"),settlementAttachedTo,settlementTermObj.getString("commercialHead"),commercialType);
	    					commPayToClient = commissionable.getJSONObject("commissionPayableToClient");
	    					periodicity = commPayToClient.getJSONObject("periodicity");
	    					populateSettlementTerms(periodicity, clientId, commPayToClient.getString("settlementSchedule"),settlementAttachedTo,settlementTermObj.getString("commercialHead"),commercialType);
	    				}
	    				else
	    				{
	    					commissionable = standard.getJSONObject("nonCommissionable");
	    					spPayToComp = commissionable.getJSONObject("sellingPricePayableToCompany");
	    					periodicity = spPayToComp.getJSONObject("periodicity");
	    					populateSettlementTerms(periodicity, clientId, spPayToComp.getString("settlementSchedule"),settlementAttachedTo,settlementTermObj.getString("commercialHead"),commercialType);
	    				}
	    			}
	    			else
	    			{
	    			      JSONObject others = recPay.getJSONObject("other");
	    			      periodicity = others.getJSONObject("periodicity");
	    			      populateSettlementTerms(periodicity, clientId, others.getString("settlementSchedule"),settlementAttachedTo,settlementTermObj.getString("commercialHead"),commercialType);
	    			}		
	    		}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public class ClientSettlementDetails extends SettlementDetails{
		private String clientId;
		private String clientName;
		private String entityMarket;
		private String entityType;


		ClientSettlementDetails(String clientId,String commName,String commType,String settlementSchedule,PeriodicityData periodicityData,String clientName,String clientMarket,String clientType) {
			super(commName,commType,settlementSchedule,periodicityData);
			this.clientId=clientId;
			this.clientName=clientName;
			this.entityMarket = clientMarket;
			this.entityType = clientType;
		}

		public String getClientId() {
			return clientId;
		}

		public void setClientId(String clientId) {
			this.clientId = clientId;
		}

		public String getClientName() {
			return clientName;
		}

		public void setClientName(String clientName) {
			this.clientName = clientName;
		}

		public String getEntityMarket() {
			return entityMarket;
		}

		public void setEntityMarket(String entityMarket) {
			this.entityMarket = entityMarket;
		}

    	public String getEntityType() {
			return entityType;
		}

		public void setEntityType(String entityType) {
			this.entityType = entityType;
		}

		@Override
		public String toString() {
			return "ClientCommercialHead [clientId=" + clientId + ", clientName=" + clientName + ", entityMarket="
					+ entityMarket + ", entityType=" + entityType + "]";
		}

	}
	
	 private void populateSettlementTerms(JSONObject periodicity, String clientId,String settlementSchedule,JSONObject settlementAttachedTo,String commercialHead,String commercialType) {
	    	List<ClientSettlementDetails> commHeadLst;
	    		/*List<ProductInfo> prodInfoList = new ArrayList<ProductInfo>();
	    		JSONArray productInfo = commJson.getJSONArray("productInfo");
	    		for (int j = 0; j < productInfo.length(); j++) {
	    			JSONObject currentObj = productInfo.getJSONObject(j);
	    			ProductInfo prodInfo = new ProductInfo();
	    			prodInfo.setProductCategory(currentObj.getString("productCategory"));
	    			prodInfo.setProductSubCategory(currentObj.getString("productCategorySubType"));
	    			if (currentObj.has("productName"))
	    				prodInfo.setProductName(currentObj.getString("productName"));
	    			prodInfoList.add(prodInfo);
	    			//TODO: Add this list TO commHead Obj 
	    		}*/
	    		List<PeriodicityData> periodicityLst = getPeriodicityData(periodicity);
	    		//TODO:How to get periodicity Data
	    		for (PeriodicityData periodicityData : periodicityLst) {
	      
	    			ClientSettlementDetails commHead = new ClientSettlementDetails(clientId, commercialHead,
	    				commercialType, settlementSchedule, periodicityData,settlementAttachedTo.getString("entityName"),settlementAttachedTo.getString("companyMarket"),settlementAttachedTo.getString("entityType")
	    					);
	    			commHeadLst = clientWiseCommMap.get(clientId);
	    			if (commHeadLst == null || commHeadLst.size()==0) {
	    				commHeadLst = new ArrayList<ClientSettlementDetails>();
	    				clientWiseCommMap.put(clientId, commHeadLst);
	    			}
	    			commHeadLst.add(commHead);
	    			commHeadLst = periodWiseCommMap.get(commHead.periodicityMapKey);
	    			if (commHeadLst == null) {
	    				commHeadLst = new ArrayList<ClientSettlementDetails>();
	    				periodWiseCommMap.put(commHead.periodicityMapKey, commHeadLst);
	    			}
	    			commHeadLst.add(commHead);
	    		}
	    	
	    }
	 
	 
	 
	 public List<ClientSettlementDetails> getAllCommForStatementGeneration() {
			updateClientSettlementTerms();
			List<ClientSettlementDetails> resultLst = new ArrayList<ClientSettlementDetails>();
			LocalDate now = LocalDate.now();
			addCommHeadToLst(resultLst, now,MONTHDAY_PRFX);
			addCommHeadToLst(resultLst, now,WEEKDAY_PRFX);
			addCommHeadToLst(resultLst, now,MONTHEND_PRFX);
			return resultLst;
		}

		private void addCommHeadToLst(List<ClientSettlementDetails> resultLst, LocalDate date, char dayPfx) {
			PeriodicityData tempPeriodicity = new PeriodicityData();
			tempPeriodicity.toDate.dayPrefx=dayPfx;
			switch(dayPfx) {
			case MONTHDAY_PRFX:{
				tempPeriodicity.toDate.day = (short) date.getDayOfMonth();
				break;
			}
			case WEEKDAY_PRFX:{
				tempPeriodicity.toDate.day = getDayIdx(date.getDayOfWeek().toString());
				break;
			}
			case MONTHEND_PRFX:{
				if(date.getDayOfMonth()!=date.lengthOfMonth())
					return;
				break;
			}
			}
			addCommHeadToLst(resultLst, tempPeriodicity);
			tempPeriodicity.toDate.month = (short) date.getMonthValue();
			addCommHeadToLst(resultLst, tempPeriodicity);
			tempPeriodicity.toDate.year = (short) date.getYear();
			addCommHeadToLst(resultLst, tempPeriodicity);
			
		}

		private void addCommHeadToLst(List<ClientSettlementDetails> resultLst, PeriodicityData periodicityData) {
			List<ClientSettlementDetails> tempLst=periodWiseCommMap.get(getPeriodicityMapKey(periodicityData));
			if(tempLst!=null)
				resultLst.addAll(tempLst);
		}

}
