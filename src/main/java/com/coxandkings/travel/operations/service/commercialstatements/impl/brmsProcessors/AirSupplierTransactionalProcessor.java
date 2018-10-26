package com.coxandkings.travel.operations.service.commercialstatements.impl.brmsProcessors;

import java.math.BigDecimal;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.coxandkings.travel.operations.model.commercialstatements.PassengerDetails;
import com.coxandkings.travel.operations.service.commercialstatements.impl.AirConstants;
import com.coxandkings.travel.operations.service.commercialstatements.impl.brmsProcessors.OrderWiseCommercialDetails.Commercials;
import com.coxandkings.travel.operations.service.commercialstatements.impl.slabbing.SlabProperties;
import com.coxandkings.travel.operations.service.commercialstatements.impl.slabbing.SlabbingTree.Slab;

@Component
@Primary
public class AirSupplierTransactionalProcessor extends BaseCommercialsProcessor implements AirConstants{

	private static final String COMMENG_URL_PATH = "${commercials_statements.brms.url.air-supplier-transactional}";
	private static final String COMMENG_SHELL_PATH = "${commercials_statements.brms.reqShell.air-supplier-nontransactional}";
	
	private static final Logger logger = LogManager.getLogger(AirSupplierTransactionalProcessor.class);
	
	AirSupplierTransactionalProcessor(@Value(value = COMMENG_URL_PATH) String url, @Value(value = COMMENG_SHELL_PATH) String shellStr){
		this.commEngURl = url;
		this.commEngRqShell = shellStr;
		this.briRqRootKey = "cnk.air_commercialscalculationengine.suppliertransactionalrules.Root";
		                     
	}
	
	private JSONArray getSupplierCommercialsBusinessRuleIntakeJSONArray(JSONObject supplierCommercialRS) {
		JSONArray briJsonArray = new JSONArray();

		try {
			briJsonArray =  supplierCommercialRS.getJSONObject("result").getJSONObject("execution-results").getJSONArray("results").getJSONObject(0).getJSONObject("value").getJSONObject("cnk.air_commercialscalculationengine.suppliertransactionalrules.Root").getJSONArray("businessRuleIntake");;
            
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("Supplier Commercials \"businessRuleIntake\" evaluated to be null");
		}
		if (briJsonArray == null) {
			logger.warn("Supplier Commercials \"businessRuleIntake\" evaluated to be null");
			briJsonArray = new JSONArray();
		}

		return briJsonArray;

	}
	
	@Override
	protected void populateOrderObject(OrderWiseCommercialDetails ordrDtls,JSONObject bookingJson,JSONObject productJson) {
		super.populateOrderObject(ordrDtls,bookingJson, productJson);
		ordrDtls.setProductName(productJson.getString(JSON_PROP_SUPPID));
		Set<PassengerDetails> paxDetails = new HashSet<>();
		paxDetails.addAll(getPaxDetails(productJson.getJSONObject("orderDetails").getJSONArray("paxInfo")));
		ordrDtls.setPaxDetails(paxDetails);
	}
	
	@Override
	protected String getBRIKey(JSONObject bookingJson, JSONObject productJson) {
		JSONObject resHdr = bookingJson.getJSONObject(JSON_PROP_RESHEADER);
		JSONObject resBody = bookingJson.getJSONObject(JSON_PROP_RESBODY);
		return String.format("%s%c%s%c%s%c%s%c%s%c%s%c%s", productJson.getString(JSON_PROP_SUPPID), KEYSEPARATOR,
				resHdr.getJSONObject(JSON_PROP_CLIENTCONTEXT).getString(JSON_PROP_CLIENTTYPE), KEYSEPARATOR,
				resHdr.getJSONObject(JSON_PROP_CLIENTCONTEXT).getString(JSON_PROP_CLIENTMARKET), KEYSEPARATOR,
				productJson.getJSONObject("orderDetails").getString("tripType"), KEYSEPARATOR,
				resHdr.getJSONObject(JSON_PROP_CLIENTCONTEXT).optString("clientIATANumber"), KEYSEPARATOR,
				productJson.getString("credentialsName"),KEYSEPARATOR,resHdr.getJSONObject(JSON_PROP_CLIENTCONTEXT).optString(JSON_PROP_COMPANYID));
	}

	@Override
	protected JSONObject createBRIAndPopulateData(JSONObject bookingJson, JSONObject productJson,
			CommercialProcessorUtils utils, OrderWiseCommercialDetails ordrDtls) {
		JSONObject briJson = new JSONObject();
		JSONObject resHdr = bookingJson.getJSONObject(JSON_PROP_RESHEADER);
		JSONObject commonElemsJson = new JSONObject();
		JSONObject advDefnJson = new JSONObject();
		String suppId = productJson.getString("supplierID");
		String suppMkt = resHdr.getJSONObject("clientContext").getString("clientMarket");//TODO:change this
		String productSubCat = productJson.getString("productSubCategory");
		JSONObject clientContext = resHdr.getJSONObject(JSON_PROP_CLIENTCONTEXT);
		commonElemsJson.put(JSON_PROP_SUPP, suppId);
		commonElemsJson.put(JSON_PROP_PRODNAME, PRODUCT_NAME_BRMS);
		commonElemsJson.put(JSON_PROP_SEGMENT, "Active");
		commonElemsJson.put(JSON_PROP_SUPPMARKET, suppMkt);
		commonElemsJson.put(JSON_PROP_CLIENTTYPE, clientContext.optString(JSON_PROP_CLIENTTYPE));
		commonElemsJson.put(JSON_PROP_IATANBR, clientContext.optString("clientIATANumber"));
		commonElemsJson.put(JSON_PROP_CLIENTNAME, "");
		commonElemsJson.put(JSON_PROP_CLIENTGROUP, "");
		commonElemsJson.put("contractValidity", DATE_FORMAT.format(new Date()));
		briJson.put(JSON_PROP_COMMONELEMS, commonElemsJson);
		
		advDefnJson.put(JSON_PROP_TRAVELTYPE, "SITI");
		advDefnJson.put(JSON_PROP_JOURNEYTYPE, productJson.getJSONObject("orderDetails").getString("tripType"));
		advDefnJson.put(JSON_PROP_CONNECTSUPPTYPE, "LCC");
		advDefnJson.put(JSON_PROP_CONNECTSUPP, suppId);
		advDefnJson.put(JSON_PROP_CREDSNAME, productJson.getString("credentialsName"));
		advDefnJson.put(JSON_PROP_BOOKINGTYPE, "Online");
		briJson.put(JSON_PROP_ADVANCEDDEF, advDefnJson);
		
		
		SlabProperties slabProps = new SlabProperties();
		slabProps.setProductCateg("Transportation");
		slabProps.setProductCategSubType(productSubCat);
		slabProps.setSupplierID(suppId);
		slabProps.setMarket(clientContext.optString(JSON_PROP_CLIENTMARKET));
		Collection<Slab> slabDetails = utils.getSlabData().get(slabProps);
		for(Slab slab:slabDetails) {
			JSONObject slabJson = new JSONObject();
			slabJson.put("slabType", slab.getSlabType());
			slabJson.put("slabTypeValue", slab.getSlabValue());
			briJson.append("slabDetails", slabJson);
		}

		
		JSONArray jrnyDtlsJsonArr = new JSONArray();
		briJson.put(JSON_PROP_JOURNEYDETAILS, jrnyDtlsJsonArr);
		updateBRIAndPopulateData(briJson, bookingJson, productJson, utils, ordrDtls);

		return briJson;
	}

	@Override
	protected void updateBRIAndPopulateData(JSONObject briJson, JSONObject bookingJson, JSONObject productJson,
			CommercialProcessorUtils utils, OrderWiseCommercialDetails ordrDtls) {
		JSONArray journeyDtlsArr= briJson.getJSONArray(JSON_PROP_JOURNEYDETAILS);
		JSONObject jrnyDtlsJson =  getBRMSFlightDetailsJSON(productJson);
	    journeyDtlsArr.put(jrnyDtlsJson);
	    populateOrderObject(ordrDtls, bookingJson, productJson);
	    ordrDtls.setProdIdx(briJson.getJSONArray(JSON_PROP_JOURNEYDETAILS).length()-1);
	}
	
	
	
	private JSONObject getBRMSFlightDetailsJSON(JSONObject productJson) {
		JSONObject jrnyDtlsJson = new JSONObject();
		JSONArray origDestJsonArr = productJson.getJSONObject(JSON_PROP_ORDERDETAILS).getJSONObject("flightDetails")
				.getJSONArray(JSON_PROP_ORIGDESTOPTS);
		JSONArray fltDtlsJsonArr = new JSONArray();
		JSONArray tvlDtlsJsonArr = new JSONArray();
		boolean isVia = false;
		for (int i = 0; i < origDestJsonArr.length(); i++) {
			JSONObject origDestJson = origDestJsonArr.getJSONObject(i);
			JSONArray fltSegsJsonArr = origDestJson.getJSONArray(JSON_PROP_FLIGHTSEG);
			JSONObject tvlDtlsJson = new JSONObject();

			isVia = (fltSegsJsonArr.length() > 1);
			for (int j = 0; j < fltSegsJsonArr.length(); j++) {
				JSONObject fltSegJson = fltSegsJsonArr.getJSONObject(j);

				JSONObject fltDtlJson = new JSONObject();
				JSONObject opAirlineJson = fltSegJson.getJSONObject(JSON_PROP_OPERAIRLINE);
				fltDtlJson.put(JSON_PROP_FLIGHTNBR, opAirlineJson.getString(JSON_PROP_FLIGHTNBR));
				fltDtlJson.put(JSON_PROP_FLIGHTTIMIMNG, fltSegJson.getString(JSON_PROP_DEPARTDATE));
				fltDtlJson.put(JSON_PROP_CABINCLASS, fltSegJson.getString(JSON_PROP_CABINTYPE));
				fltDtlJson.put(JSON_PROP_RBD, fltSegJson.getString(JSON_PROP_RESBOOKDESIG));
				fltDtlsJsonArr.put(fltDtlJson);

				
				// if (i == 0) { String origLoc = fltSegJson.getString(JSON_PROP_ORIGLOC);
				 tvlDtlsJson.put(JSON_PROP_FROMCITY,"");
				 // airportInfo.getOrDefault(RedisAirportData.AIRPORT_CITY, ""));
				 tvlDtlsJson.put(JSON_PROP_FROMCOUNTRY,"");
				 // airportInfo.getOrDefault(RedisAirportData.AIRPORT_COUNTRY, ""));
				  tvlDtlsJson.put(JSON_PROP_FROMCONTINENT,"");
				 /** airportInfo.getOrDefault(RedisAirportData.AIRPORT_CONTINENT, "")); }
				 * 
				 * if (i == (fltSegsJsonArr.length() - 1)) { String destLoc =
				 * fltSegJson.getString(JSON_PROP_DESTLOC); Map<String, Object> airportInfo =
				 * RedisAirportData.getAirportInfo(destLoc); tvlDtlsJson.put(JSON_PROP_TOCITY,
				 * airportInfo.getOrDefault(RedisAirportData.AIRPORT_CITY, ""));
*/				  tvlDtlsJson.put(JSON_PROP_TOCOUNTRY,"");
				 // airportInfo.getOrDefault(RedisAirportData.AIRPORT_COUNTRY, ""));
				  tvlDtlsJson.put(JSON_PROP_TOCONTINENT,"");
				 /** airportInfo.getOrDefault(RedisAirportData.AIRPORT_CONTINENT, "")); }
				 * 
				 * if (isVia && (i > 0 && i < (fltSegsJsonArr.length() - 1))) { String destLoc =
				 * fltSegJson.getString(JSON_PROP_DESTLOC); Map<String, Object> airportInfo =
				 * RedisAirportData.getAirportInfo(destLoc);
				 * 
				 * String city = airportInfo.getOrDefault(RedisAirportData.AIRPORT_CITY,
				 * "").toString(); String viaCity = tvlDtlsJson.optString(JSON_PROP_VIACITY,
				 * "");*/ tvlDtlsJson.put(JSON_PROP_VIACITY,"");
					/*	 , viaCity.concat((viaCity.length() > 0)
				 * ? "|" : "").concat(city)); String country =
				 * airportInfo.getOrDefault(RedisAirportData.AIRPORT_COUNTRY, "").toString();
				 * String viaCountry = tvlDtlsJson.optString(JSON_PROP_VIACOUNTRY, "");*/
				 tvlDtlsJson.put(JSON_PROP_VIACOUNTRY,""); 
				/*		 viaCountry.concat((viaCountry.length()
				 * > 0) ? "|" : "").concat(country)); String continent =
				 * airportInfo.getOrDefault(RedisAirportData.AIRPORT_CONTINENT, "").toString();
				 * String viaContinent = tvlDtlsJson.optString(JSON_PROP_VIACONTINENT, "");*/
				 tvlDtlsJson.put(JSON_PROP_VIACONTINENT,"");
				 /** viaContinent.concat((viaContinent.length() > 0) ? "|" :
				 * "").concat(continent)); }
				 * 
				 *//* }*/
			tvlDtlsJsonArr.put(tvlDtlsJson);
		}
		}
		jrnyDtlsJson.put(JSON_PROP_FLIGHTTYPE, ((isVia) ? "Via" : "Direct"));
		// TODO: Check if this hard-coding is alright...
		jrnyDtlsJson.put(JSON_PROP_FLIGHTLINETYPE, "Online");
		// TODO: Check if this hard-coding is alright...
		jrnyDtlsJson.put(JSON_PROP_CODESHAREFLIGHTINC, Boolean.TRUE.booleanValue());
		// TODO: Check if this hard-coding is alright...
		jrnyDtlsJson.put(JSON_PROP_TRAVELPRODNAME, "Flights");
		jrnyDtlsJson.put(JSON_PROP_FLIGHTDETAILS, fltDtlsJsonArr);
		jrnyDtlsJson.put(JSON_PROP_TRAVELDTLS, tvlDtlsJsonArr);

		JSONArray psgrDtlsJsonArr = new JSONArray();
		JSONArray paxPricingJsonArr = productJson.getJSONObject("orderDetails").getJSONObject("orderSupplierPriceInfo")
				.getJSONArray(JSON_PROP_PAXTYPEFARES);
		for (int i = 0; i < paxPricingJsonArr.length(); i++) {
			JSONObject psgrDtlsJson = new JSONObject();
			JSONObject paxPricingJson = paxPricingJsonArr.getJSONObject(i);
			psgrDtlsJson.put(JSON_PROP_PSGRTYPE, paxPricingJson.getString(JSON_PROP_PAXTYPE));
			// TODO: Map fareBasisValue
			// TODO: Map dealCode
			psgrDtlsJson.put(JSON_PROP_TOTALFARE,
					paxPricingJson.getJSONObject(JSON_PROP_TOTALFARE).getBigDecimal(JSON_PROP_AMOUNT));

			JSONObject fareBreakupJson = new JSONObject();
			fareBreakupJson.put(JSON_PROP_BASEFARE,
					paxPricingJson.getJSONObject(JSON_PROP_BASEFARE).getBigDecimal(JSON_PROP_AMOUNT));

			JSONArray taxDetailsJsonArr = new JSONArray();
			JSONArray taxesJsonArr = paxPricingJson.getJSONObject(JSON_PROP_TAXES).getJSONArray(JSON_PROP_TAX);
			for (int j = 0; j < taxesJsonArr.length(); j++) {
				JSONObject taxJson = taxesJsonArr.getJSONObject(j);
				JSONObject taxDetailJson = new JSONObject();
				taxDetailJson.put(JSON_PROP_TAXNAME, taxJson.getString(JSON_PROP_TAXCODE));
				taxDetailJson.put(JSON_PROP_TAXVALUE, taxJson.getBigDecimal(JSON_PROP_AMOUNT));
				taxDetailsJsonArr.put(taxDetailJson);
			}

			JSONArray feesJsonArr = paxPricingJson.getJSONObject(JSON_PROP_FEES).getJSONArray(JSON_PROP_FEE);
			for (int j = 0; j < feesJsonArr.length(); j++) {
				JSONObject feeJson = feesJsonArr.getJSONObject(j);
				JSONObject feeDetailJson = new JSONObject();
				feeDetailJson.put(JSON_PROP_TAXNAME, feeJson.getString(JSON_PROP_FEECODE));
				feeDetailJson.put(JSON_PROP_TAXVALUE, feeJson.getBigDecimal(JSON_PROP_AMOUNT));
				taxDetailsJsonArr.put(feeDetailJson);
			}

			fareBreakupJson.put(JSON_PROP_TAXDETAILS, taxDetailsJsonArr);
			psgrDtlsJson.put(JSON_PROP_FAREBREAKUP, fareBreakupJson);
			psgrDtlsJsonArr.put(psgrDtlsJson);
		}

		jrnyDtlsJson.put(JSON_PROP_PSGRDETAILS, psgrDtlsJsonArr);
		return jrnyDtlsJson;
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

	@Override
	protected void calculatePrices(JSONObject supplierCommercialRS, List<OrderWiseCommercialDetails> orderDtlsLst) {
		JSONArray scommBRIJsonArr = getSupplierCommercialsBusinessRuleIntakeJSONArray(supplierCommercialRS);
		JSONObject scommBRIJson,commHeadJson,journeyDetailsJson;
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
			
			 journeyDetailsJson = (JSONObject) scommBRIJson.getJSONArray("journeyDetails").get(orderDtls.getProdIdx());
			 Map<String,BigDecimal> commercialsAmountMap=new HashMap<String,BigDecimal>();
			 JSONArray paxDetails = journeyDetailsJson.getJSONArray("passengerDetails");
				for (int l = 0; l < paxDetails.length(); l++) {
					JSONObject paxDetailsObj = paxDetails.getJSONObject(l);
					JSONArray commercialDetailsArr = paxDetailsObj.getJSONArray("commercialDetails");
					for (int z = 0; z < commercialDetailsArr.length(); z++) {
						JSONObject currCommObj = commercialDetailsArr.getJSONObject(z);
						String commercialName = currCommObj.getString("commercialName");
						BigDecimal amount = getCommercialAmount(currCommObj, orderDtls.getCurrency(),
								scommBRIJson.getJSONObject("commonElements").getString("supplierMarket"));
						if (!commercialsAmountMap.containsKey(commercialName)) {
							commercialsAmountMap.put(commercialName, amount);
						} else {
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
