package com.coxandkings.travel.operations.service.commercialstatements.impl.brmsProcessors;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.coxandkings.travel.operations.model.commercialstatements.PassengerDetails;
import com.coxandkings.travel.operations.service.commercialstatements.impl.AccoConstants;
import com.coxandkings.travel.operations.service.commercialstatements.impl.RestUtilities;
import com.coxandkings.travel.operations.service.commercialstatements.impl.brmsProcessors.OrderWiseCommercialDetails.Commercials;
import com.coxandkings.travel.operations.service.commercialstatements.impl.settlementTerms.SettlementTerms.SettlementDetails;
import com.coxandkings.travel.operations.service.commercialstatements.impl.slabbing.SlabProperties;
import com.coxandkings.travel.operations.service.commercialstatements.impl.slabbing.SlabbingTree;
import com.coxandkings.travel.operations.service.commercialstatements.impl.slabbing.SlabbingTree.Slab;
import com.coxandkings.travel.operations.service.qcmanagement.QcUtilService;

@Component
class AccoSupplierTransactionalProcessor extends BaseCommercialsProcessor implements AccoConstants{

	private static final String COMMENG_URL_PATH = "${commercials_statements.brms.url.acco-supplier-transactional}";
	private static final String COMMENG_SHELL_PATH = "${commercials_statements.brms.reqShell.acco-supplier-nontransactional}";

	private static final Logger logger = LogManager.getLogger(AccoSupplierTransactionalProcessor.class);

	AccoSupplierTransactionalProcessor(@Value(value = COMMENG_URL_PATH) String url, @Value(value = COMMENG_SHELL_PATH) String shellStr){
		this.commEngURl = url;
		this.commEngRqShell = shellStr;
		this.briRqRootKey = "cnk.acco_commercialscalculationengine.suppliertransactionalrules.Root";
	}

	private JSONArray getSupplierCommercialsBusinessRuleIntakeJSONArray(JSONObject supplierCommercialRS) {
		JSONArray briJsonArray = new JSONArray();

		try{
			briJsonArray = supplierCommercialRS.getJSONObject(JSON_PROP_RESULT).getJSONObject(JSON_PROP_EXECUTIONRES).getJSONArray(JSON_PROP_RESULTS).getJSONObject(0).getJSONObject(JSON_PROP_VALUE).getJSONObject(JSON_PROP_SUPPTRANRULES).getJSONArray(JSON_PROP_BUSSRULEINTAKE);
		}
		catch(Exception e){
			logger.warn("Supplier Commercials \"businessRuleIntake\" evaluated to be null");
		}
		if(briJsonArray==null) {
			logger.warn("Supplier Commercials \"businessRuleIntake\" evaluated to be null");
			briJsonArray = new JSONArray();
		}

		return briJsonArray;
	}

	@Override
	protected void populateOrderObject(OrderWiseCommercialDetails ordrDtls,JSONObject bookingJson,JSONObject productJson) {
		super.populateOrderObject(ordrDtls,bookingJson, productJson);
		JSONObject hotlDtls = productJson.getJSONObject("orderDetails").getJSONObject("hotelDetails");
		ordrDtls.setProductName(hotlDtls.getString(JSON_PROP_HOTELNAME));
		Set<PassengerDetails> paxDetails =new HashSet<PassengerDetails>();
		for(Object roomStay: hotlDtls.getJSONArray("rooms")) {
			paxDetails.addAll(getPaxDetails(((JSONObject) roomStay).getJSONArray("paxInfo")));
		}
		ordrDtls.setPaxDetails(paxDetails);
	}
	
	@Override
	protected String getBRIKey(JSONObject bookingJson, JSONObject productJson) {
		JSONObject resHdr = bookingJson.getJSONObject(JSON_PROP_RESHEADER);
		JSONObject resBody = bookingJson.getJSONObject(JSON_PROP_RESBODY);
		JSONObject roomStayJson = (JSONObject) productJson.getJSONObject("orderDetails").getJSONObject("hotelDetails").getJSONArray("rooms").get(0);
		// TODO:add salesDate,Country,continent,state,market,currency in key
		return String.format("%s%c%s%c%s%c%s%c%s%c%s%c%s", productJson.getString("productSubCategory"), KEYSEPARATOR, productJson.getString("supplierID"), KEYSEPARATOR,
				resHdr.getJSONObject(JSON_PROP_CLIENTCONTEXT).getString(JSON_PROP_CLIENTTYPE), KEYSEPARATOR, roomStayJson.getString(JSON_PROP_CHKIN),
				KEYSEPARATOR, roomStayJson.getString(JSON_PROP_CHKOUT),KEYSEPARATOR,resHdr.optString(JSON_PROP_COMPANYID),KEYSEPARATOR,resBody.getString("bookID"));
	}

	@Override
	protected JSONObject createBRIAndPopulateData(JSONObject bookingJson, JSONObject productJson,
			CommercialProcessorUtils utils, OrderWiseCommercialDetails ordrDtls) {
		JSONObject briJson = new JSONObject();
		JSONObject resHdr = bookingJson.getJSONObject(JSON_PROP_RESHEADER);
		JSONObject commonElemsJson = new JSONObject();
		JSONObject advDefnJson = new JSONObject();
		String suppId = productJson.getString("supplierID");
		JSONObject roomStayJson = (JSONObject) productJson.getJSONObject("orderDetails").getJSONObject("hotelDetails").getJSONArray("rooms").get(0);
		String suppMkt = resHdr.getJSONObject("clientContext").getString("clientMarket");//TODO:change this
		String productSubCat = productJson.getString("productSubCategory");
		Map<String, Object> cityAttrs=new HashMap<>();//TODO:change this
		String cityName = "";//TODO:change this

		commonElemsJson.put(JSON_PROP_SUPP, suppId);
		commonElemsJson.put(JSON_PROP_SUPPMARKET, suppMkt);
		commonElemsJson.put("contractValidity", DATE_FORMAT.format(new Date()));
		commonElemsJson.put(JSON_PROP_PRODCATEGSUBTYPE, productSubCat);
		commonElemsJson.put(JSON_PROP_CLIENTTYPE, resHdr.getJSONObject(JSON_PROP_CLIENTCONTEXT).getString(JSON_PROP_CLIENTTYPE));
		commonElemsJson.put("companyId", resHdr.getJSONObject(JSON_PROP_CLIENTCONTEXT).getString(JSON_PROP_COMPANY));

		advDefnJson.put("travelCheckInDate", roomStayJson.getString(JSON_PROP_CHKIN).concat("T00:00:00"));
		advDefnJson.put("travelCheckOutDate",roomStayJson.getString(JSON_PROP_CHKOUT).concat("T00:00:00"));
		advDefnJson.put("salesDate",bookingJson.getJSONObject(JSON_PROP_RESBODY).getString("bookingDate"));
		advDefnJson.put("bookingType", "Online");
		advDefnJson.put(JSON_PROP_CREDSNAME,productJson.optString("credentialsName"));
		advDefnJson.put(JSON_PROP_NATIONALITY, "Indian");
		advDefnJson.put(JSON_PROP_CONTINENT, cityAttrs.getOrDefault(JSON_PROP_CONTINENT,""));
		advDefnJson.put(JSON_PROP_COUNTRY, cityAttrs.getOrDefault(JSON_PROP_COUNTRY,""));
		advDefnJson.put(JSON_PROP_CITY, cityName);
		advDefnJson.put(JSON_PROP_STATE, cityAttrs.getOrDefault(JSON_PROP_STATE,""));

		SlabProperties slabProps = new SlabProperties();
		slabProps.setProductCateg("Accommodation");
		slabProps.setProductCategSubType(productSubCat);
		slabProps.setSupplierID(suppId);
		slabProps.setMarket(suppMkt);
		Collection<Slab> slabDetails = utils.getSlabData().get(slabProps);
		for(Slab slab:slabDetails) {
			JSONObject slabJson = new JSONObject();
			slabJson.put("slabType", slab.getSlabType());
			slabJson.put("slabTypeValue", slab.getSlabValue());
			briJson.append("slabDetails", slabJson);
		}

		briJson.put(JSON_PROP_COMMONELEMS, commonElemsJson);
		briJson.put(JSON_PROP_ADVANCEDDEF, advDefnJson);
		updateBRIAndPopulateData(briJson, bookingJson, productJson, utils, ordrDtls);

		return briJson;
	}

	@Override
	protected void updateBRIAndPopulateData(JSONObject briJson, JSONObject bookingJson, JSONObject productJson,
			CommercialProcessorUtils utils, OrderWiseCommercialDetails ordrDtls) {
		JSONObject hotelDetailsJson = new JSONObject();
		Map<String, Object> hotelAttrs=new HashMap<>();//TODO:change this

		//hotelAttrs = RedisHotelData.getHotelInfo(hotelCode);
		hotelDetailsJson.put("productName", hotelAttrs.getOrDefault("name", ""));
		hotelDetailsJson.put("productBrand", hotelAttrs.getOrDefault("brand", ""));
		hotelDetailsJson.put("productChain", hotelAttrs.getOrDefault("chain", ""));

		briJson.append(JSON_PROP_HOTELDETAILS, hotelDetailsJson);
		populateOrderObject(ordrDtls, bookingJson, productJson);
		ordrDtls.setProdIdx(briJson.getJSONArray(JSON_PROP_HOTELDETAILS).length()-1);

		for(Object roomStayJson: productJson.getJSONObject("orderDetails").getJSONObject("hotelDetails").getJSONArray("rooms")) {
			hotelDetailsJson.append(JSON_PROP_ROOMDETAILS, getBRMSRoomDetailsJSON((JSONObject) roomStayJson));
		}

	}

	private Set<PassengerDetails> getPaxDetails(JSONArray paxInfo) {
		Set<PassengerDetails> paxArray= new HashSet<PassengerDetails>();
		for(int i=0;i<paxInfo.length();i++)
		{
			JSONObject paxInfoObj = paxInfo.getJSONObject(i);
			PassengerDetails pax = new PassengerDetails();
			pax.setPassengerName(paxInfoObj.optString("title").concat(" ").concat(paxInfoObj.optString("firstName")).concat(" ").concat(paxInfoObj.optString("middleName")).concat(" ").concat(paxInfoObj.optString("lastName")));
			pax.setPassengerType(paxInfoObj.getString("paxType"));
			paxArray.add(pax);
		}
		return paxArray;
	}

	private JSONObject getBRMSRoomDetailsJSON(JSONObject roomStayJson) {

		JSONObject roomDetailsJson = new JSONObject();

		JSONObject roomTypeJson = roomStayJson.getJSONObject(JSON_PROP_ROOMTYPEINFO);
		roomDetailsJson.put("roomType",roomTypeJson.get(JSON_PROP_ROOMTYPENAME));
		roomDetailsJson.put("roomCategory",roomTypeJson.get(JSON_PROP_ROOMCATEGNAME));

		JSONObject ratePlanJson = roomStayJson.getJSONObject(JSON_PROP_RATEPLANINFO);
		roomDetailsJson.put("rateType", ratePlanJson.get(JSON_PROP_RATEPLANNAME));
		roomDetailsJson.put("rateCode", ratePlanJson.get(JSON_PROP_RATEPLANCODE));

		JSONObject totalPriceJson =  roomStayJson.getJSONObject("supplierPriceInfo");
		BigDecimal totalPrice = totalPriceJson.getBigDecimal("supplierPrice");
		roomDetailsJson.put("totalFare", String.valueOf(totalPrice));

		JSONObject taxesJson = totalPriceJson.optJSONObject(JSON_PROP_TOTALTAX);
		if(taxesJson!=null && taxesJson.has(JSON_PROP_AMOUNT)) {
			JSONObject fareBreakupJson = new JSONObject();
			fareBreakupJson.put("baseFare", totalPrice.subtract(taxesJson.getBigDecimal(JSON_PROP_AMOUNT)));
			JSONArray taxArr = taxesJson.optJSONArray(JSON_PROP_TAXBRKPARR);
			if(taxArr!=null) {
				JSONArray taxDetailsArr = new JSONArray();
				JSONObject taxDetailJson;
				for(Object tax:taxArr) {
					taxDetailJson = new JSONObject();
					taxDetailJson.put("taxName", ((JSONObject) tax).getString(JSON_PROP_TAXCODE));
					taxDetailJson.put("taxValue", ((JSONObject) tax).getBigDecimal(JSON_PROP_AMOUNT));
					taxDetailsArr.put(taxDetailJson);
				}
				fareBreakupJson.put("taxDetails",taxDetailsArr);
			}
			roomDetailsJson.put("fareBreakUp", fareBreakupJson);
		}

		return roomDetailsJson;

	}
	
	protected void calculatePrices(JSONObject supplierCommercialRS,List<OrderWiseCommercialDetails> orderDtlsLst) {
		JSONArray scommBRIJsonArr = getSupplierCommercialsBusinessRuleIntakeJSONArray(supplierCommercialRS);
		JSONObject scommBRIJson,commHeadJson,hotelDtlsJson;
		JSONArray commHeadJsonArr = null;
		Map<String, String> suppCommToTypeMap = new HashMap<String, String>();
		for(OrderWiseCommercialDetails orderDtls:orderDtlsLst) {
			scommBRIJson = scommBRIJsonArr.getJSONObject(orderDtls.getBriIdx());
			commHeadJsonArr = scommBRIJson.optJSONArray(JSON_PROP_COMMHEAD);
			if (commHeadJsonArr == null) {
				logger.warn("No commercial heads found in supplier commercials");
				continue;
			}

			for (int j=0; j < commHeadJsonArr.length(); j++) {
				commHeadJson = commHeadJsonArr.getJSONObject(j);
				suppCommToTypeMap.put(commHeadJson.getString(JSON_PROP_COMMHEADNAME), commHeadJson.getString(JSON_PROP_COMMTYPE));
			}

			hotelDtlsJson = (JSONObject) scommBRIJson.getJSONArray("hotelDetails").get(orderDtls.getProdIdx());
			JSONArray  roomDetails = hotelDtlsJson.getJSONArray("roomDetails");
			Map<String,BigDecimal> commercialsAmountMap=new HashMap<String,BigDecimal>();
			for(int l = 0;l<roomDetails.length();l++)
			{
				JSONObject roomDetailsObj = roomDetails.getJSONObject(l);
				JSONArray commercialDetailsArr = roomDetailsObj.getJSONArray("commercialDetails");
				for(int z=0;z<commercialDetailsArr.length();z++)
				{
					JSONObject currCommObj  = commercialDetailsArr.getJSONObject(z);
					String commercialName = currCommObj.getString("commercialName");
					BigDecimal amount = getCommercialAmount(currCommObj,orderDtls.getCurrency(),scommBRIJson.getJSONObject("commonElements").getString("supplierMarket"));
					if(!commercialsAmountMap.containsKey(commercialName))
					{
						commercialsAmountMap.put(commercialName, amount);
					}
					else
					{
						commercialsAmountMap.put(commercialName, commercialsAmountMap.get(commercialName).add(amount));
					}
				}
			}
			Iterator<Map.Entry<String, BigDecimal>> commercialsMapIter = commercialsAmountMap.entrySet().iterator();
			List<Commercials> commsList = new ArrayList<Commercials>();
			while (commercialsMapIter.hasNext()) {
				Map.Entry<String, BigDecimal> commEntry = commercialsMapIter.next();
				String commercialName = commEntry.getKey();
				BigDecimal commercialAmount = commEntry.getValue();
				String commercialType = suppCommToTypeMap.get(commercialName);
				Commercials commObj = new Commercials();
				commObj.setCommercialName(commercialName);
				commObj.setCommercialAmount(commercialAmount);
				commObj.setCommercialType(commercialType);
				commsList.add(commObj);
			}
			orderDtls.setCommercials(commsList);
		}
	}

	private BigDecimal getCommercialAmount(JSONObject currCommObj, String toCurr, String market) {
		BigDecimal commercialAmount = new BigDecimal(0);
		if(currCommObj.getInt("commercialCalculationPercentage")==0)
		{
			BigDecimal roe = restUtils.getInRequestedCurrency(currCommObj.getString("commercialCurrency"),toCurr,market);
			commercialAmount = currCommObj.getBigDecimal("commercialInitialAmount").add(roe.multiply(currCommObj.getBigDecimal("commercialAmount")));

		}
		else
		{
			commercialAmount = currCommObj.getBigDecimal("commercialTotalAmount");
		}
		return commercialAmount;
	}

}
