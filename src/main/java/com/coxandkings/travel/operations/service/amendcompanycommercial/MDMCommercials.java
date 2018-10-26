package com.coxandkings.travel.operations.service.amendcompanycommercial;

import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.commercials.SellingPriceComponent;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.resource.amendentitycommercial.AmendCommercialResource;
import com.coxandkings.travel.operations.resource.amendentitycommercial.ClientCommercialsMDMFilter;
import com.coxandkings.travel.operations.resource.amendsuppliercommercial.AmendSupplierCommercialResource;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URI;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Component
public class MDMCommercials {

	@Autowired
    private MDMRestUtils mdmRestUtils;

    private static final Logger logger = LogManager.getLogger(MDMCommercials.class);
    @Autowired
    private OpsBookingService opsBookingService;

    @Value("${mdm.commercials-kafka}")
    private String commercialUrl;
    @Autowired
    private ObjectMapper objectMapper;

    @Value(value = "${ROE.booking-date}")
    private String getROEUrl;
    
    private JSONObject getMDMClientCommercial(String version, String refId) throws OperationException {

        //String filter = "?filter={\"refId\":\"" + refId +"\", \"__v\":\"" + refId + "\"}";
		 
	       JSONObject mdmRuleJSON=null;
	    try {

            String mdmFilter = objectMapper.writeValueAsString(new ClientCommercialsMDMFilter(refId, version));
            URI builder = UriComponentsBuilder.fromUriString(commercialUrl + "?filter=" + mdmFilter).build().encode().toUri();
            ResponseEntity<String> mdmRuleData = mdmRestUtils.exchange(builder, HttpMethod.GET, null, String.class);
            JSONObject mdmRuleDataJSON = new JSONObject(mdmRuleData.getBody());
            mdmRuleJSON = mdmRuleDataJSON.getJSONArray("data").getJSONObject(0).getJSONObject("doc");
            //mdmRuleJSON.put("method", "POST");
            mdmRuleJSON.getJSONObject("data").put("advancedDefinitionData", new JSONArray());
            mdmRuleJSON.getJSONObject("data").put("clientCommercialOtherHead", new JSONArray());
        } catch (JSONException | JsonProcessingException e) {
            logger.error(e.getMessage(), e);
        }

        return mdmRuleJSON;
    }

    private JSONObject getMDMSupplierCommercial(String version, String refId) throws OperationException {

        //String filter = "?filter={\"refId\":\"" + refId + "\", \"doc.data.SupplierCommercialData.versionNumber\":" + version + "}";

        JSONObject mdmRuleJSON = null;
        try {

            String mdmFilter=objectMapper.writeValueAsString(new ClientCommercialsMDMFilter(refId,version));
            URI builder = UriComponentsBuilder.fromUriString(commercialUrl + "?filter=" + mdmFilter).build().encode().toUri();
			ResponseEntity<String> mdmRuleData=mdmRestUtils.exchange(builder,HttpMethod.GET,null, String.class);
			JSONObject mdmRuleDataJSON=new JSONObject(mdmRuleData.getBody());
			mdmRuleJSON=mdmRuleDataJSON.getJSONArray("data").getJSONObject(0).getJSONObject("doc");
			mdmRuleJSON.put("method", "POST");
			mdmRuleJSON.getJSONObject("data").put("advancedDefinitionData",new JSONArray());
			mdmRuleJSON.getJSONObject("data").put("clientCommercialOtherHead",new JSONArray());
        } catch (JSONException| JsonProcessingException e) {
            logger.error(e.getMessage(), e);
	    }
	    
	    return mdmRuleJSON;
}

    public JSONObject updateMDMEntityCommercial(AmendCommercialResource companyCommercialResource, List<String> approvedCommercialHeads, List<String> amendedCommercialHeads) throws OperationException {
        String mdmCompositeKey = companyCommercialResource.getMdmRuleId();
        List<String> mdmKeys = Arrays.asList(mdmCompositeKey.split("\\|"));
        String version = "";
        String mdmClientCommId = "";
       

        try {
            version = mdmKeys.get(0);
            mdmClientCommId = mdmKeys.get(1);
        } catch (ArrayIndexOutOfBoundsException e) {
            logger.error(e.getMessage(), e);
            throw new OperationException(Constants.INVALID_MDM_RULEID);
        }


        JSONObject mdmRuleJSON = this.getMDMClientCommercial(version, mdmClientCommId);

        if (mdmRuleJSON == null) {
            throw new OperationException(Constants.MDM_COMMERCIAL_DEFINITION_NOT_FOUND);
        }

        /*if (amendedCommercialHeads.size() == 0) {
            //check whether booking is amended for the first time
            mdmRuleJSON.put("method", "POST");
        } else {
            mdmRuleJSON.put("method", "PUT");
        }*/
        
        mdmRuleJSON.put("method", "PUT");

        //list of commercials whose values should not be updated
        approvedCommercialHeads.removeAll(Collections.singleton(companyCommercialResource.getCommercialHead()));

        JSONObject opsAmendmentJson=getClientCommOpsAmendmentsJsonArr(companyCommercialResource, approvedCommercialHeads);
        JSONArray opsAmendmentsJsonArr=opsAmendmentJson.getJSONArray("opsAmendmentsJsonArr");
        JSONArray revisedCommercialJson=opsAmendmentJson.getJSONArray("revisedCommercialJsonArr"); 
        
        JSONObject budgetedMarginDetailsJSON = mdmRuleJSON.getJSONObject("data").getJSONObject("budgetedMarginDetails");
        String budgetMarginId = updateBudgetedMarginJSON(budgetedMarginDetailsJSON, companyCommercialResource, opsAmendmentsJsonArr);
        
        JSONArray clientCommDataJSONArr = mdmRuleJSON.getJSONObject("data").getJSONArray("clientCommercialData");

        boolean matchFound = false;
        for (int idx = 0; idx < clientCommDataJSONArr.length(); idx++) {
            JSONObject clientCommDataJSON = clientCommDataJSONArr.getJSONObject(idx);
            if (clientCommDataJSON.getString("budgetedMarginDetailId").equals(budgetMarginId)) {
                updateClientCommercialJSONFromPath(clientCommDataJSON,revisedCommercialJson);
                matchFound = true;
            }
        }
        if (!matchFound) {
            throw new OperationException(Constants.MDM_COMMERCIAL_DEFINITION_NOT_FOUND);
        }


        return mdmRuleJSON;
    }

    public JSONObject updateMDMSupplierCommercial(AmendSupplierCommercialResource supplierCommercialResource) throws OperationException {

        OpsBooking opsBooking = null;
        String bookingId = supplierCommercialResource.getBookingId();
        String orderId = supplierCommercialResource.getOrderId();


        opsBooking = opsBookingService.getBooking(bookingId);

        if (opsBooking == null) {
            throw new OperationException(Constants.BOOKING_NOT_FOUND, bookingId);
        }


        Optional<OpsProduct> optOrder = opsBooking.getProducts().parallelStream().filter(orders -> orders.getOrderID().equals(orderId)).findAny();
        if (!optOrder.isPresent()) {
            throw new OperationException(Constants.ORDER_NOT_FOUND, orderId, bookingId);
        }

        OpsProduct order = optOrder.get();
        OpsProductSubCategory opsProductSubCategory = order.getOpsProductSubCategory();

        String mdmCompositeKey = supplierCommercialResource.getMdmRuleId();
        List<String> mdmKeys = Arrays.asList(mdmCompositeKey.split("\\|"));
        String version = mdmKeys.get(0);
        String mdmSupplierCommId = mdmKeys.get(1);
        
        try {
        	  version = mdmKeys.get(0);
              mdmSupplierCommId = mdmKeys.get(1);
        } catch (ArrayIndexOutOfBoundsException e) {
            logger.error(e.getMessage(), e);
            throw new OperationException(Constants.INVALID_MDM_RULEID);
        }


        JSONObject mdmRuleJSON = this.getMDMSupplierCommercial(version, mdmSupplierCommId);
        if (mdmRuleJSON == null) {
            throw new OperationException(Constants.MDM_COMMERCIAL_DEFINITION_NOT_FOUND);
        }

        JSONArray opsAmendmentsJsonArr = getSupplierCommOpsAmendmentsJsonArr(supplierCommercialResource, order);

        JSONObject cceAmendmentRQJson = new JSONObject();
        cceAmendmentRQJson.put("method", "POST");
        JSONObject rootDataJson = new JSONObject();
        cceAmendmentRQJson.put("data", rootDataJson);
        rootDataJson.put("bookingId", supplierCommercialResource.getBookingId());
        rootDataJson.put("commercialDefinition", new JSONObject().put("productCategorySubType", opsProductSubCategory.getSubCategory()).put("productCategory", opsProductSubCategory.getCategory().getCategory()));
        rootDataJson.put("opsAmendments", opsAmendmentsJsonArr);
        rootDataJson.put("data", updateSupplierCommercialJSON(supplierCommercialResource, mdmRuleJSON, opsAmendmentsJsonArr));
        return cceAmendmentRQJson;
    }

    private JSONArray updateSupplierCommercialJSON(AmendSupplierCommercialResource supplierCommercialResource, JSONObject mdmRuleJSON, JSONArray opsAmendmentsJsonArr) throws OperationException {
        JSONArray mdmCommercialData = new JSONArray();
        for (int idx = 0; idx < opsAmendmentsJsonArr.length(); idx++) {
            JSONObject opsAmendmentsJson = opsAmendmentsJsonArr.getJSONObject(idx);
            
            JSONObject mdmCommercialJson = getMDMCommercial(supplierCommercialResource,opsAmendmentsJson, mdmRuleJSON);
           
            mdmCommercialData.put(mdmCommercialJson);
        }
        return mdmCommercialData;
    }

    private void updateMDMCommercial(JSONObject opsAmendmentsJson,AmendSupplierCommercialResource supplierCommercialResource,
			JSONObject mdmCommercialJson, String fixedSlabIndicator) throws OperationException {

		JSONObject commercialCalculationJson = mdmCommercialJson.getJSONObject(fixedSlabIndicator);
		boolean amendmentCommercial = opsAmendmentsJson.getString("commercialName")
				.equals(supplierCommercialResource.getSupplierCommercialHead());

		BigDecimal amount = opsAmendmentsJson.getBigDecimal("commercialCalculationAmount");

		String currency = opsAmendmentsJson.optString("commercialCurrency", null);

		boolean amountApplicable = supplierCommercialResource.getAmountApplicable();

		if (amountApplicable && amendmentCommercial) {

			currency = supplierCommercialResource.getCurrency();

			if (supplierCommercialResource.getAddToOriginal()) {

				amount = amount.add(supplierCommercialResource.getAmount());
			}

			else {
				if (amount.compareTo(supplierCommercialResource.getAmount()) < 0) {
					throw new OperationException("Reduced Amount cannot be Negative");
				}
				amount = amount.subtract(supplierCommercialResource.getAmount());
			}
		}

		if (amount.equals(BigDecimal.ZERO)|| amount.equals(new BigDecimal(0.0))) {
			commercialCalculationJson.put("isAmount", false);
		} else {
			commercialCalculationJson.put("valueAmount", amount);
			commercialCalculationJson.put("currency", currency);
			commercialCalculationJson.put("isAmount", true);
		}

		String fareComponents = opsAmendmentsJson.optString("commercialFareComponent",null);
		BigDecimal percentage = opsAmendmentsJson.getBigDecimal("commercialCalculationPercentage");

		Map<String, BigDecimal> fareComponentMap = getFareComponents(fareComponents, percentage);

		if (!amountApplicable && amendmentCommercial) {
			BigDecimal amendmentPercentage = supplierCommercialResource.getPercentage();
			Iterator<SellingPriceComponent> priceComponentItr = supplierCommercialResource.getSupplierPriceComponents()
					.iterator();

			while (priceComponentItr.hasNext()) {
				SellingPriceComponent priceComponent = priceComponentItr.next();

				if (fareComponentMap.containsKey(priceComponent.getSellingPriceComponent())) {
					if (supplierCommercialResource.getAddToOriginal()) {
						fareComponentMap.put(priceComponent.getSellingPriceComponent(),fareComponentMap.get(priceComponent.getSellingPriceComponent()).add(amendmentPercentage));
					}

					else {
						if (fareComponentMap.get(priceComponent.getSellingPriceComponent())
								.compareTo(amendmentPercentage) < 0) {
							throw new OperationException("Reduced Percentage Cannot be Negative");
						}
						fareComponentMap.put(priceComponent.getSellingPriceComponent(), fareComponentMap.get(priceComponent.getSellingPriceComponent()).subtract(amendmentPercentage));
					}
				}

				else {
					if (supplierCommercialResource.getAddToOriginal()) {
						fareComponentMap.put(priceComponent.getSellingPriceComponent(), amendmentPercentage);
					} else {
						throw new OperationException(String.format(
								"Percentage Cannot be reduced, Fare Component<%s> not applied in Commercial",
								priceComponent.getSellingPriceComponent()));
					}

				}

			}
		}

		if (fareComponentMap.size() == 0) {
			commercialCalculationJson.put("isPercentage", false);
		}

		else {
			commercialCalculationJson.put("isPercentage", true);
			JSONArray percentageDetailsJsonArr = new JSONArray();
			commercialCalculationJson.put("percentageDetails", percentageDetailsJsonArr);
			for (Map.Entry<String, BigDecimal> entry : fareComponentMap.entrySet()) {
				JSONObject percentageDetailsJson = new JSONObject();
				percentageDetailsJson.put("farePriceComponents", entry.getKey());
				percentageDetailsJson.put("valuePercentage", entry.getValue());
				percentageDetailsJsonArr.put(percentageDetailsJson);
			}
		}

		/*
		 * if (commercialCalculationJson.getBoolean("isPercentage")) {
		 * 
		 * Map<String, JSONObject> farePriceComponentsMap = new HashMap<String,
		 * JSONObject>(); JSONArray percentageDetailsJsonArr =
		 * commercialCalculationJson.getJSONArray("percentageDetails"); for (int idx =
		 * 0; idx < percentageDetailsJsonArr.length(); idx++) { JSONObject
		 * percentageDetailsJson = percentageDetailsJsonArr.getJSONObject(idx);
		 * farePriceComponentsMap.put(percentageDetailsJson.getString(
		 * "farePriceComponents"), percentageDetailsJson); }
		 * 
		 * 
		 * 
		 * Iterator<SellingPriceComponent> priceComponentItr =
		 * supplierCommercialResource.getSupplierPriceComponents().iterator(); if
		 * (supplierCommercialResource.getAddToOriginal()) {
		 * 
		 * 
		 * while (priceComponentItr.hasNext()) { SellingPriceComponent priceComponent =
		 * priceComponentItr.next(); if
		 * (farePriceComponentsMap.containsKey(priceComponent.getSellingPriceComponent()
		 * )) { JSONObject percentageDetailsJson =
		 * farePriceComponentsMap.get(priceComponent.getSellingPriceComponent());
		 * percentageDetailsJson.put("valuePercentage",
		 * percentageDetailsJson.getBigDecimal("valuePercentage").add(
		 * supplierCommercialResource.getPercentage())); } else
		 * percentageDetailsJsonArr.put(new JSONObject().put("farePriceComponents",
		 * priceComponent.getSellingPriceComponent()).put("valuePercentage",
		 * supplierCommercialResource.getPercentage())); }
		 * 
		 * } else {
		 * 
		 * while (priceComponentItr.hasNext()) { // SupplierPriceComponent
		 * priceComponent = priceComponentItr.next(); SellingPriceComponent
		 * priceComponent = priceComponentItr.next(); if
		 * (farePriceComponentsMap.containsKey(priceComponent.getSellingPriceComponent()
		 * )) { farePriceComponentsMap.get(priceComponent.getSellingPriceComponent()).
		 * getBigDecimal("valuePercentage").subtract(supplierCommercialResource.
		 * getPercentage()); } else throw new OperationException(String.
		 * format("Percentage Cannot be reduced, Fare Component<%s> not applied in Commercial"
		 * , priceComponent.getSellingPriceComponent()));
		 * 
		 * 
		 * }
		 * 
		 * }
		 * 
		 * }
		 * 
		 * 
		 * else {
		 * 
		 * if (!supplierCommercialResource.getAddToOriginal()) { throw new
		 * OperationException("Percentage Cannot be reduced, No Percentage applied in Commercial"
		 * ); } JSONArray percentageDetailsJsonArr = new JSONArray();
		 * commercialCalculationJson.put("percentageDetails", percentageDetailsJsonArr);
		 * commercialCalculationJson.put("isPercentage", true);
		 * Iterator<SellingPriceComponent> priceComponentItr =
		 * supplierCommercialResource.getSupplierPriceComponents().iterator(); while
		 * (priceComponentItr.hasNext()) { SellingPriceComponent priceComponent =
		 * priceComponentItr.next(); JSONObject percentageDetailsJson = new
		 * JSONObject(); percentageDetailsJson.put("farePriceComponents",
		 * priceComponent.getSellingPriceComponent());
		 * percentageDetailsJson.put("valuePercentage",
		 * supplierCommercialResource.getPercentage());
		 * percentageDetailsJsonArr.put(percentageDetailsJson);
		 * 
		 * } }
		 */

	}

    private Map<String, BigDecimal> getFareComponents(String fareComponents, BigDecimal percentage) {
    	Map<String, BigDecimal> fareCompMap=new HashMap<String, BigDecimal>();
    	
    	if(fareComponents==null || fareComponents.length()<=0) {
    		return fareCompMap;
    	}
    	
    	List<String> fareComps=Arrays.asList(fareComponents.split(","));
    	
    	for(String fareComp:fareComps) {
    	if(fareComp.equals("Basic")||fareComp.equals("Total")) {
    		fareCompMap.put(fareComp, percentage);
    		continue;
    	}
    	
    	String[] taxComps=fareComp.split(";");
    	String taxComp=taxComps[0];
    	BigDecimal taxPercentage=new BigDecimal(taxComps[1]);
    	fareCompMap.put(taxComp, taxPercentage);
    		
    	}
		return fareCompMap;
	}

	private JSONObject getMDMCommercial(AmendSupplierCommercialResource supplierCommercialResource,JSONObject opsAmendmentsJson, JSONObject mdmRuleJSON) throws OperationException {
    	
    	String mdmRuleId = opsAmendmentsJson.getString("mdmRuleID");
        String commercialName = opsAmendmentsJson.getString("commercialName");
        
        
        JSONObject mdmCommercialJson = new JSONObject();
        
        mdmCommercialJson.put("commercialName", commercialName);
        List<String> mdmKeys = Arrays.asList(mdmRuleId.split("\\|"));
        String stdAdvCommIndicator = mdmKeys.get(2);
        String advCommercialIds = mdmKeys.get(3);
        String fixedSlabIndicator = mdmKeys.get(4);
        String slabIndex = mdmKeys.get(5);


        JSONObject supplierCommercialRootJson = mdmRuleJSON.getJSONObject("data");
        JSONObject supplierCommercialJson = null;
        JSONObject commercialCalculationJson = null;
        if (stdAdvCommIndicator.equals("standardCommercial")) {
            supplierCommercialJson = supplierCommercialRootJson.getJSONObject("SupplierCommercialData").getJSONObject("standardCommercial");
        } else {
            JSONArray advanceCommercialJsonArr = supplierCommercialRootJson.getJSONArray("advanceCommercialData");
            String[] advCommercialId = advCommercialIds.split("_");
            String id = advCommercialId[0];
            String name = advCommercialId[1];
            boolean matchfound = false;
            for (int idx = 0; idx < advanceCommercialJsonArr.length(); idx++) {
                JSONObject advanceCommercialJson = advanceCommercialJsonArr.getJSONObject(idx);
                if (advanceCommercialJson.getString("_id").equals(id)) {
                    matchfound = true;
                    supplierCommercialJson = advanceCommercialJson.getJSONObject("advanceCommercial").getJSONObject(name);
                    break;
                }
            }

            if (!matchfound) {
                throw new OperationException(Constants.ADVANCED_COMMERCIAL_NOT_FOUND);
            }
        }

        if (fixedSlabIndicator.equals("fixed")) {
            commercialCalculationJson = supplierCommercialJson.getJSONObject("fixed");
            mdmCommercialJson.put("fixed", commercialCalculationJson);
        } else {
            JSONArray slabJsonArr = supplierCommercialJson.getJSONArray("slab");

            try {
                JSONObject slabJson = slabJsonArr.getJSONObject(Integer.parseInt(slabIndex));
                commercialCalculationJson = slabJson;
                mdmCommercialJson.put("slab", commercialCalculationJson);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new OperationException(Constants.SLAB_NOT_FOUND);
            }
        }

        
            updateMDMCommercial(opsAmendmentsJson,supplierCommercialResource, mdmCommercialJson, fixedSlabIndicator);
        
        
        return mdmCommercialJson;
    }

    private void updateClientCommercialJSONFromPath(JSONObject clientCommDataJSON, JSONArray revisedCommercialJsonArr) throws OperationException {
    	
    	
    	for(int i=0;i<revisedCommercialJsonArr.length();i++) {
    	
    	JSONObject revisedCommercialJson=revisedCommercialJsonArr.getJSONObject(i);
    	String commercialName = revisedCommercialJson.getString("commercialName");
    	String mdmCompositeKey = revisedCommercialJson.getString("mdmRuleID");
    	
        List<String> mdmKeys = Arrays.asList(mdmCompositeKey.split("\\|"));
         
        String path = "";
        String commercialIndex = "";
        String commercialType = "";


         try {
             path = mdmKeys.get(2);
             commercialIndex = mdmKeys.get(3);
             commercialType = mdmKeys.get(4);
         } catch (ArrayIndexOutOfBoundsException e) {
             logger.error(e.getMessage(), e);
             throw new OperationException(Constants.INVALID_MDM_RULEID);
         }
    	
    	 
        List<String> paths = Arrays.asList(path.split("_"));
        JSONObject flightsAndNonAirJSON = clientCommDataJSON.getJSONObject("commercialDetails").getJSONObject("flightsAndNonAir");
        JSONObject commercialJSON = null;
        JSONArray commercialJSONArr = null;
        if (commercialName.equals("MarkUp")) {
            commercialJSONArr = flightsAndNonAirJSON.getJSONArray(paths.get(0));
        } else {
            commercialJSONArr = flightsAndNonAirJSON.getJSONObject(paths.get(0)).getJSONArray(paths.get(1));
        }

        try {
            commercialJSON = commercialJSONArr.getJSONObject(Integer.parseInt(commercialIndex));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new OperationException(Constants.COMMERCIAL_NOT_FOUND);
        }

        JSONObject commmercialCalculationJSON = commercialJSON.getJSONObject(commercialType);
        String percentageKey =  "percentage";
        String amountKey =  "amount";
        
        if(revisedCommercialJson.getBoolean("isRetention")) {
        	//Retention Commercial
        	commmercialCalculationJSON.put(amountKey, revisedCommercialJson.getBigDecimal("retentionAmountPercentage"));
        	commmercialCalculationJSON.put(percentageKey, revisedCommercialJson.getBigDecimal("retentionPercentage"));
        }
        
      
        else {
        	//additional,fixed,markup commercial
        	commmercialCalculationJSON.put(amountKey, revisedCommercialJson.getBigDecimal("commercialCalculationAmount"));
        	commmercialCalculationJSON.put(percentageKey, revisedCommercialJson.getBigDecimal("commercialCalculationPercentage"));
        	if (commercialName.equals("MarkUp")) {
        		String minSellingPriceKey=commercialType.equals("client")?"markupToMinSellingPriceClient":"markupToMinSellingPriceCompany";
        		commercialJSON.getJSONObject(minSellingPriceKey).put(amountKey,revisedCommercialJson.getBigDecimal("commercialCalculationAmount"));
            }
        	
        	commmercialCalculationJSON.put("fareComponents",revisedCommercialJson.getJSONArray("commercialFareComponent"));
        }
        
        
       /* if (companyCommercialResource.getAmountApplicable()) {

            String amountKey = commercialType.contains("Retention") ? "retentionAmount" : "amount";
            //TODO check if currency conversion required
            if (!commmercialCalculationJSON.getString("currency").equals(companyCommercialResource.getCurrency())) {
                throw new OperationException(Constants.CURRENCY_NOT_MATCHING, commmercialCalculationJSON.getString("currency"), companyCommercialResource.getCurrency());
            }
            if (companyCommercialResource.getAddToOriginal()) {
                if (commmercialCalculationJSON.has(amountKey)) {
                    BigDecimal amount = new BigDecimal(commmercialCalculationJSON.getDouble(amountKey));
                    commmercialCalculationJSON.put(amountKey, amount.add(companyCommercialResource.getAmount()));
                } else
                    commmercialCalculationJSON.put(amountKey, companyCommercialResource.getAmount());

            } else {
                if (commmercialCalculationJSON.has(amountKey)) {
                    BigDecimal amount = new BigDecimal(commmercialCalculationJSON.getDouble(amountKey));
                    if (amount.compareTo(companyCommercialResource.getAmount()) < 0) {
                        throw new OperationException(Constants.NEGETIVE_AMOUNT);
                    }
                    commmercialCalculationJSON.put(amountKey, amount.subtract(companyCommercialResource.getAmount()));
                } else {
                    throw new OperationException(Constants.CANNOT_REDUCE_COMMERCIAL_AMOUNT);
                }
            }
        } else {


            if (companyCommercialResource.getAddToOriginal()) {
                if (commmercialCalculationJSON.has(percentageKey)) {
                    BigDecimal percentage = new BigDecimal(commmercialCalculationJSON.getDouble(percentageKey));
                    commmercialCalculationJSON.put(percentageKey, percentage.add(companyCommercialResource.getPercentage()));
                } else {
                    commmercialCalculationJSON.put(percentageKey, companyCommercialResource.getPercentage());
                }
            } else {
                if (commmercialCalculationJSON.has(percentageKey)) {
                    BigDecimal percentage = new BigDecimal(commmercialCalculationJSON.getDouble(percentageKey));
                    if (percentage.compareTo(companyCommercialResource.getPercentage()) < 0) {
                        throw new OperationException(Constants.NEGETIVE_PERCENTAGE);
                    }
                    commmercialCalculationJSON.put(percentageKey, percentage.subtract(companyCommercialResource.getPercentage()));
                } else {
                    commmercialCalculationJSON.put(percentageKey, companyCommercialResource.getPercentage());
                }
            }


            //TODO can't change only 1 fare component
	    		Iterator<SellingPriceComponent> priceComponentItr=companyCommercialResource.getSellingPriceComponent().iterator();
	    		JSONArray fareComponentJSONArr=new JSONArray();
	        	while(priceComponentItr.hasNext()) {
	        		SellingPriceComponent priceComponent=priceComponentItr.next();
	        		fareComponentJSONArr.put(priceComponent.getSellingPriceComponent());
	        	}
	        	commmercialCalculationJSON.put("fareComponents", fareComponentJSONArr);

        }*/
    	}
    }

    private String updateBudgetedMarginJSON(JSONObject budgetedDetailsMarginJSON, AmendCommercialResource companyCommercialResource, JSONArray opsAmendmentsJsonArr) throws OperationException {
        JSONArray budgetMarginsJSONArr = budgetedDetailsMarginJSON.getJSONArray("budgetMargins");
        OpsProductSubCategory opsProductSubCategory = companyCommercialResource.getProductSubCategory();
        boolean matchFound = false;
        JSONObject budgetMarginJSON = null;
        String budgetMarginId = null;
        for (int idx = 0; idx < budgetMarginsJSONArr.length(); idx++) {
            budgetMarginJSON = budgetMarginsJSONArr.getJSONObject(idx);
            if (budgetMarginJSON.getString("subCategory").equals(opsProductSubCategory.getSubCategory())) {
                matchFound = true;
                budgetMarginId = budgetMarginJSON.getString("_id");
                break;

            }
        }

        if (!matchFound) {
            throw new OperationException(Constants.BUDGET_MARGIN_NOT_FOUND);
        }


        JSONArray revBudgetMarginJSONArr = new JSONArray();
        revBudgetMarginJSONArr.put(budgetMarginJSON);
        budgetedDetailsMarginJSON.put("budgetMargins", revBudgetMarginJSONArr);

        JSONObject entityDetailsJSON = budgetedDetailsMarginJSON.getJSONObject("budgetMarginAttachedTo");
        entityDetailsJSON.put("bookingId", companyCommercialResource.getBookingId());
        entityDetailsJSON.put("opsAmendments", opsAmendmentsJsonArr);
        return budgetMarginId;
    }

    private JSONObject getClientCommOpsAmendmentsJsonArr(AmendCommercialResource companyCommercialResource, List<String> approvedCommercialHeads) throws OperationException {
        JSONObject rootJson=new JSONObject();
    	JSONArray opsAmendmentsJsonArr = new JSONArray();
    	JSONArray revisedCommercialJsonArr = new JSONArray();
        OpsBooking opsBooking = null;
        String bookingId = companyCommercialResource.getBookingId();
        String orderId = companyCommercialResource.getOrderId();
        OpsClientEntityCommercial entityCommercials=null;

        opsBooking = opsBookingService.getBooking(bookingId);

        if (opsBooking == null) {
            throw new OperationException(Constants.BOOKING_NOT_FOUND, bookingId);
        }


        Optional<OpsProduct> optOrder = opsBooking.getProducts().parallelStream().filter(orders -> orders.getOrderID().equals(orderId)).findAny();
        if (!optOrder.isPresent()) {
            throw new OperationException(Constants.ORDER_NOT_FOUND, orderId, bookingId);
        }

        OpsProduct order = optOrder.get();

        OpsProductSubCategory opsProductSubCategory = companyCommercialResource.getProductSubCategory();
        switch (opsProductSubCategory) {
            case PRODUCT_SUB_CATEGORY_BUS:
                break;
            case PRODUCT_SUB_CATEGORY_CAR:
                break;
            case PRODUCT_SUB_CATEGORY_FLIGHT:
                Optional<OpsPaxTypeFareFlightClient> optPaxTypeFare = order.getOrderDetails().getFlightDetails().getTotalPriceInfo().getPaxTypeFares().parallelStream()
                        .filter(pax -> pax.getPaxType().equals(companyCommercialResource.getPaxType())).findAny();
                if (!optPaxTypeFare.isPresent()) {
                    throw new OperationException(String.format(Constants.PAX_NOT_FOUND, companyCommercialResource.getPaxType()));
                }
                OpsPaxTypeFareFlightClient paxTypeFare = optPaxTypeFare.get();
                Optional<OpsClientEntityCommercial> optEntityCommercial = paxTypeFare.getOpsClientEntityCommercial().parallelStream()
                        .filter(entity -> entity.getCommercialEntityID().equals(companyCommercialResource.getCommercialEntityID())).findFirst();
                if (!optEntityCommercial.isPresent()) {
                    throw new OperationException(Constants.ENTITY_ID_NOT_FOUND, companyCommercialResource.getCommercialEntityID());
                }
                entityCommercials= optEntityCommercial.get();
                break;
            case PRODUCT_SUB_CATEGORY_HOTELS:
                Optional<OpsRoom> optOpsRoom = order.getOrderDetails().getHotelDetails().getRooms().parallelStream()
                        .filter(room -> room.getRoomID().equals(companyCommercialResource.getRoomId())).findAny();
                if (!optOpsRoom.isPresent()) {
                    throw new OperationException(Constants.ROOM_NOT_FOUND, companyCommercialResource.getRoomId());
                }
                Optional<OpsClientEntityCommercial> optRoomEntityCommercial = optOpsRoom.get().getOpsClientEntityCommercial().parallelStream()
                        .filter(entity -> entity.getCommercialEntityID().equals(companyCommercialResource.getCommercialEntityID())).findFirst();
                if (!optRoomEntityCommercial.isPresent()) {
                    throw new OperationException(Constants.ENTITY_ID_NOT_FOUND, companyCommercialResource.getCommercialEntityID());
                }
                entityCommercials= optRoomEntityCommercial.get();
                break;
            case PRODUCT_SUB_CATEGORY_INDIAN_RAIL:
                break;
            case PRODUCT_SUB_CATEGORY_RAIL:
                break;
            default:
                break;

        }

        
        for (OpsPaxRoomClientCommercial clientCommercial : entityCommercials.getOpsPaxRoomClientCommercial()) {
            
                JSONObject opsAmendmentsJson = new JSONObject();
                opsAmendmentsJson.put("commercialName", clientCommercial.getCommercialName());
                opsAmendmentsJson.put("mdmRuleID", clientCommercial.getMdmRuleID());
                opsAmendmentsJsonArr.put(opsAmendmentsJson);
                revisedCommercialJsonArr.put(getRevisedCommercialJson(clientCommercial,entityCommercials,opsBooking,companyCommercialResource));
            
        }
        
        
        
        
        rootJson.put("opsAmendmentsJsonArr", opsAmendmentsJsonArr);
        rootJson.put("revisedCommercialJsonArr", revisedCommercialJsonArr);
        return rootJson;
    }

    private JSONObject getRevisedCommercialJson(OpsPaxRoomClientCommercial originalCommercial,OpsClientEntityCommercial entityCommercials,OpsBooking opsBooking,AmendCommercialResource companyCommercialResource) throws OperationException {
    	OpsProduct order=opsBookingService.getOpsProduct(opsBooking, companyCommercialResource.getOrderId());
    	JSONObject revisedCommercialJson=new JSONObject();
    	String mdmRuleID=originalCommercial.getMdmRuleID();
    	String commercialType=mdmRuleID.split("\\|")[2];
    	boolean isRetention=commercialType.contains("retention")?true:false;
    	revisedCommercialJson.put("isRetention",isRetention);
    	revisedCommercialJson.put("mdmRuleID",originalCommercial.getMdmRuleID());
    	revisedCommercialJson.put("commercialName", originalCommercial.getCommercialName());
    	if(isRetention) {
    		
    		BigDecimal remainingAmount =null;
    		BigDecimal remainingPercentageAmount = null;
    		BigDecimal retentionPercentage= null;
            BigDecimal retentionAmountPercentage = null;
            String retentionCommercialCurrency=null;
            String sellingPriceCurrency=null;
    		
            
            retentionPercentage=getBigDecimal(originalCommercial.getRetentionPercentage());
            retentionAmountPercentage=getBigDecimal(originalCommercial.getRetentionAmountPercentage());
        
    		
            if(originalCommercial.getCommercialName().equals(companyCommercialResource.getCommercialHead())) {
            OpsProductSubCategory opsProductSubCategory = companyCommercialResource.getProductSubCategory();
             switch (opsProductSubCategory) {
                case PRODUCT_SUB_CATEGORY_BUS:
                    break;
                case PRODUCT_SUB_CATEGORY_CAR:
                    break;
                case PRODUCT_SUB_CATEGORY_FLIGHT:
                	sellingPriceCurrency=order.getOrderDetails().getFlightDetails().getTotalPriceInfo().getCurrencyCode();
                	if(entityCommercials.getParentClientID()==null|| entityCommercials.getParentClientID().length()<=0) {
                		OpsPaxTypeFareFlightSupplier paxTypeFare=	order.getOrderDetails().getFlightDetails().getOpsFlightSupplierPriceInfo().getPaxTypeFares().parallelStream()
                        .filter(pax -> pax.getPaxType().equals(companyCommercialResource.getPaxType())).findAny().get();
                		OpsFlightPaxSupplierCommercial supplierCommercial = paxTypeFare.getSupplierCommercials().parallelStream().
                				filter(commercial->commercial.getCommercialName().equals(companyCommercialResource.getCommercialHead())).findAny().get();
                		remainingAmount=getBigDecimal(supplierCommercial.getCommercialCalculationAmount());
            			remainingPercentageAmount=new BigDecimal(supplierCommercial.getCommercialAmount()).subtract(remainingAmount);
            			retentionCommercialCurrency=supplierCommercial.getCommercialCurrency();
            		}

                	else {
                		
                    Optional<OpsPaxTypeFareFlightClient> optPaxTypeFare = order.getOrderDetails().getFlightDetails().getTotalPriceInfo().getPaxTypeFares().parallelStream()
                            .filter(pax -> pax.getPaxType().equals(companyCommercialResource.getPaxType())).findAny();
                    if (!optPaxTypeFare.isPresent()) {
                        throw new OperationException(String.format(Constants.PAX_NOT_FOUND, companyCommercialResource.getPaxType()));
                    }
                    OpsPaxTypeFareFlightClient paxTypeFare = optPaxTypeFare.get();
                    Optional<OpsClientEntityCommercial> optEntityCommercial = paxTypeFare.getOpsClientEntityCommercial().parallelStream()
                            .filter(entity -> entity.getCommercialEntityID().equals(entityCommercials.getParentClientID())).findFirst();
                    if (!optEntityCommercial.isPresent()) {
                        throw new OperationException(Constants.ENTITY_ID_NOT_FOUND, entityCommercials.getParentClientID());
                    }
                    OpsPaxRoomClientCommercial parentCommercial = optEntityCommercial.get().getOpsPaxRoomClientCommercial().parallelStream().
                    		filter(commercial->commercial.getCommercialName().equals(companyCommercialResource.getCommercialHead())).findAny().get();
                	remainingAmount=getBigDecimal(parentCommercial.getRemainingAmount());
                	remainingPercentageAmount=getBigDecimal(parentCommercial.getRemainingPercentageAmount());
                	retentionCommercialCurrency=parentCommercial.getCommercialCurrency();
                	}
                	
                    break;
                case PRODUCT_SUB_CATEGORY_HOTELS:
                	sellingPriceCurrency=order.getOrderDetails().getHotelDetails().getOpsAccommodationTotalPriceInfo().getCurrencyCode();
                	Optional<OpsRoom> optOpsRoom = order.getOrderDetails().getHotelDetails().getRooms().parallelStream()
                    .filter(room -> room.getRoomID().equals(companyCommercialResource.getRoomId())).findAny();
            if (!optOpsRoom.isPresent()) {
                throw new OperationException(Constants.ROOM_NOT_FOUND, companyCommercialResource.getRoomId());
            }
                	
                	if(entityCommercials.getParentClientID()==null|| entityCommercials.getParentClientID().length()<=0) {
                		OpsRoomSuppCommercial roomSupplierCommercial=optOpsRoom.get().getRoomSuppCommercials().parallelStream().
                				filter(commercial->commercial.getCommercialName().equals(companyCommercialResource.getCommercialHead())).findAny().get();
            			remainingAmount=getBigDecimal(roomSupplierCommercial.getCommercialCalculationAmount());
            			remainingPercentageAmount=new BigDecimal(roomSupplierCommercial.getCommercialAmount()).subtract(remainingAmount);
            			retentionCommercialCurrency=roomSupplierCommercial.getCommercialCurrency();
            		}

                	else {
                    
                	
                    Optional<OpsClientEntityCommercial> optRoomEntityCommercial = optOpsRoom.get().getOpsClientEntityCommercial().parallelStream()
                            .filter(entity -> entity.getCommercialEntityID().equals(companyCommercialResource.getCommercialEntityID())).findFirst();
                    if (!optRoomEntityCommercial.isPresent()) {
                        throw new OperationException(Constants.ENTITY_ID_NOT_FOUND, companyCommercialResource.getCommercialEntityID());
                    }
                    OpsPaxRoomClientCommercial parentCommercial = optRoomEntityCommercial.get().getOpsPaxRoomClientCommercial().parallelStream().
                    		filter(commercial->commercial.getCommercialName().equals(companyCommercialResource.getCommercialHead())).findAny().get();
                    remainingAmount=getBigDecimal(parentCommercial.getRemainingAmount());
                	remainingPercentageAmount=getBigDecimal(parentCommercial.getRemainingPercentageAmount());
                	retentionCommercialCurrency=parentCommercial.getCommercialCurrency();
                	}
                    break;
                case PRODUCT_SUB_CATEGORY_INDIAN_RAIL:
                    break;
                case PRODUCT_SUB_CATEGORY_RAIL:
                    break;
                default:
                    break;

            }
            
    		
            // calculating increase in retention amount/retention percentage 
            
            if(companyCommercialResource.getAmountApplicable()) {
            	
            	if(remainingAmount.compareTo(BigDecimal.ZERO)>0) {
            		
            		
            		BigDecimal changeRetentionAmountPercentage=(companyCommercialResource.getAmount().divide(remainingAmount,new MathContext(4,RoundingMode.HALF_UP))).multiply(new BigDecimal(100));
            		
            		if(companyCommercialResource.getAddToOriginal()) {
            			retentionAmountPercentage=retentionAmountPercentage.add(changeRetentionAmountPercentage);
            		}
            		
            		else {
            			retentionAmountPercentage=retentionAmountPercentage.subtract(changeRetentionAmountPercentage);
            			if(retentionAmountPercentage.compareTo(BigDecimal.ZERO)<0) {
            				throw new OperationException(Constants.NEGETIVE_AMOUNT);
            			}
            		}
            	}
            	
            	else	{
            		
            			throw new OperationException("Amount Cannot be Increased/Decreased for Retention Commercial as Parent has not Retained Amount");
            	}
            	
            }
            else {
            	
            	if(remainingPercentageAmount.compareTo(BigDecimal.ZERO)>0) {
            		
            		BigDecimal amount=new BigDecimal(0.0);
            		 HashMap<String,BigDecimal> priceComponentMap=getPriceComponentMap(order,companyCommercialResource);
            		 Iterator<SellingPriceComponent> priceComponentItr = companyCommercialResource.getSellingPriceComponent().iterator();
            		 while(priceComponentItr.hasNext()) {
            			 SellingPriceComponent priceComponent= priceComponentItr.next();
            			 amount.add(priceComponentMap.get(priceComponent.getSellingPriceComponent()));
            		 }
            		 
            		 if(!retentionCommercialCurrency.equals(companyCommercialResource.getCurrency())) {
            			 String bookingDate=DateTimeFormatter.ofPattern("yyyy-MM-dd").format(opsBooking.getBookingDateZDT());
            			 BigDecimal roe=null;
            					try {      
            		            	UriComponents getROE = UriComponentsBuilder.fromUriString(getROEUrl).pathSegment(retentionCommercialCurrency).
            		            			pathSegment(sellingPriceCurrency).pathSegment(opsBooking.getClientMarket()).pathSegment(bookingDate).build();
            		            	roe=RestUtils.getForObject(getROE.toUriString(), BigDecimal.class);
            		            	
            		            	}
            		            	catch(Exception e) {
            		            		throw new OperationException("Cannot Get ROE for Commercial");
            		            	}
            					amount=amount.divide(roe,new MathContext(4,RoundingMode.HALF_UP));
            				}
            			
            		 
            		 BigDecimal changeRetentionPercentage=(amount.divide(remainingPercentageAmount,new MathContext(4,RoundingMode.HALF_UP))).multiply(new BigDecimal(100));
            		 
            		 if(companyCommercialResource.getAddToOriginal()) {
            			 retentionPercentage=retentionPercentage.add(changeRetentionPercentage);
            		 }
            		 
            		 else {
            			 retentionPercentage=retentionPercentage.subtract(changeRetentionPercentage);
            			 if(retentionPercentage.compareTo(BigDecimal.ZERO)<0) {
            				 throw new OperationException(Constants.NEGETIVE_PERCENTAGE); 
            			 }
            		 }
            	}
            	
            	else {
            		
            		throw new OperationException("Percentage Cannot be Increased/Decreased for Retention Commercial as Parent has not Retained Percentage");
            	}
            	
            
            }	
            }  		
            		
            revisedCommercialJson.put("retentionPercentage" ,retentionPercentage);
    		revisedCommercialJson.put("retentionAmountPercentage" ,retentionAmountPercentage);
    	}
    	
    	
    	else {
    		
    		BigDecimal amount=getBigDecimal(originalCommercial.getCommercialCalculationAmount());
    		BigDecimal percent=getBigDecimal(originalCommercial.getCommercialCalculationPercentage());
    		JSONArray fareCompJson=new JSONArray();
    		String fareComponent=originalCommercial.getCommercialFareComponent();
    		if(fareComponent!=null && fareComponent.length()>0) {
    		
    			List<String> fareComps=Arrays.asList(fareComponent.split("\\s*,\\s*"));
    			for(String fareComp:fareComps) {
    				if(fareComp.equals("Basic")) {
    					fareComp="baseFare";
    				}
    				fareCompJson.put(fareComp);
    			}
    		}
    		
    		if(originalCommercial.getCommercialName().equals(companyCommercialResource.getCommercialHead())) {
    		if(companyCommercialResource.getAmountApplicable()) {
    			
    			
    			if(companyCommercialResource.getAddToOriginal()) {
    				amount=amount.add(companyCommercialResource.getAmount());
    			}
    			
    			else {
    				
    				amount=amount.subtract(companyCommercialResource.getAmount());
    				if(amount.compareTo(BigDecimal.ZERO)<0) {
    					throw new OperationException(Constants.NEGETIVE_AMOUNT);
    				}
    			}
    				
    		}
    		
    		else {
    			
    			if(companyCommercialResource.getAddToOriginal()) {
    				percent=percent.add(companyCommercialResource.getPercentage());
    				
    				//if no percentage was applied in original booking
    				if(fareCompJson.length()==0) {
    					 Iterator<SellingPriceComponent> fareCompItr=companyCommercialResource.getSellingPriceComponent().iterator();
    	        		 while(fareCompItr.hasNext()) {
    	        			 fareCompJson.put(fareCompItr.next().getSellingPriceComponent());
    	        		 }
    				}
    			}
    			
    			else {
    				percent=percent.subtract(companyCommercialResource.getPercentage());
    				if(percent.compareTo(BigDecimal.ZERO)<0) {
    					throw new OperationException(Constants.NEGETIVE_PERCENTAGE); 
    				}
    			}
    			
    			//TODO Only 1 Percentage for all Fare Component Supported by CCE, whereas Amendment can change Percentage of Individual Fare Components
    			
        		
    		}
    		
    		}
    		
    		revisedCommercialJson.put("commercialCalculationAmount" , amount);
    		revisedCommercialJson.put("commercialCalculationPercentage" ,percent);
    		revisedCommercialJson.put("commercialFareComponent" ,fareCompJson);
    		
    	}
        
    	
		return revisedCommercialJson;
	}

	private BigDecimal getBigDecimal(Object input) {
		BigDecimal output=null;
		try {
			
			if( input != null ) {
	            if( input instanceof BigDecimal ) {
	            	output = (BigDecimal) input;
	            } else if( input instanceof String ) {
	            	output = new BigDecimal( (String) input );
	            } else if( input instanceof BigInteger ) {
	            	output = new BigDecimal( (BigInteger) input );
	            } else if( input instanceof Number ) {
	            	output = new BigDecimal( ((Number)input).doubleValue() );
	            } 
		}
		}
		
		catch(Exception e) {
			output=new BigDecimal(0);
		}
		return output;
	}

    
    private HashMap<String, BigDecimal> getPriceComponentMap(OpsProduct order,
			AmendCommercialResource companyCommercialResource) {

		HashMap<String, BigDecimal> priceComponentMap=new HashMap<>();
		OpsProductSubCategory opsProductSubCategory = companyCommercialResource.getProductSubCategory();
		
		switch(opsProductSubCategory) {
		case PRODUCT_SUB_CATEGORY_BUS:
			break;
		case PRODUCT_SUB_CATEGORY_CAR:
			break;
		case PRODUCT_SUB_CATEGORY_EVENTS:
			break;
		case PRODUCT_SUB_CATEGORY_FLIGHT:
			OpsPaxTypeFare totalPriceInfo=order.getOrderDetails().getFlightDetails().getTotalPriceInfo().getPaxTypeFares().parallelStream()
			.filter(paxType->paxType.getPaxType().equals(companyCommercialResource.getPaxType())).findAny().get();
			
			priceComponentMap.put("totalPrice",new BigDecimal(totalPriceInfo.getTotalFare().getAmount()));
			if (totalPriceInfo.getBaseFare() != null) {
				priceComponentMap.put("baseFare",new BigDecimal(totalPriceInfo.getBaseFare().getAmount()));
			}
			for(OpsFee fee:totalPriceInfo.getFees().getFee()) {
				priceComponentMap.put(fee.getFeeCode(), new BigDecimal(fee.getAmount()));
			}
			
			for(OpsTax tax:totalPriceInfo.getTaxes().getTax()) {
				priceComponentMap.put(tax.getTaxCode(), new BigDecimal(tax.getAmount()));
			}
				
			
			break;
		case PRODUCT_SUB_CATEGORY_HOLIDAYS:
			break;
		case PRODUCT_SUB_CATEGORY_HOTELS:
			OpsRoomTotalPriceInfo roomTotalPriceInfo = order.getOrderDetails().getHotelDetails().getRooms().parallelStream()
			.filter(room->room.getRoomID().equals(companyCommercialResource.getRoomId())).findAny().get().getRoomTotalPriceInfo();
			
			
			priceComponentMap.put("totalPrice",new BigDecimal(roomTotalPriceInfo.getRoomTotalPrice()));
			
			for(OpsTax tax: roomTotalPriceInfo.getOpsTaxes().getTax()) {
				priceComponentMap.put(tax.getTaxCode(),new BigDecimal(tax.getAmount()));

			}
			
			
			break;
		case PRODUCT_SUB_CATEGORY_INDIAN_RAIL:
			break;
		case PRODUCT_SUB_CATEGORY_RAIL:
			break;
		default:
			break;
		
		}
		return priceComponentMap;
	}

	private JSONArray getSupplierCommOpsAmendmentsJsonArr(AmendSupplierCommercialResource supplierCommercialResource, OpsProduct order) throws OperationException {
        JSONArray opsAmendmentsJsonArr = new JSONArray();


        OpsProductSubCategory opsProductSubCategory = order.getOpsProductSubCategory();
        switch (opsProductSubCategory) {
            case PRODUCT_SUB_CATEGORY_BUS:
                break;
            case PRODUCT_SUB_CATEGORY_CAR:
                break;
            case PRODUCT_SUB_CATEGORY_FLIGHT:

                Optional<OpsPaxTypeFareFlightSupplier> optPaxTypeFare = order.getOrderDetails().getFlightDetails().getOpsFlightSupplierPriceInfo().getPaxTypeFares().parallelStream()
                        .filter(pax -> pax.getPaxType().equals(supplierCommercialResource.getPaxType())).findAny();
                if (!optPaxTypeFare.isPresent()) {
                    throw new OperationException(String.format(Constants.PAX_NOT_FOUND, supplierCommercialResource.getPaxType()));
                }

                List<OpsFlightPaxSupplierCommercial> airSupplierCommercials = optPaxTypeFare.get().getSupplierCommercials();

                for (OpsFlightPaxSupplierCommercial airSupplierCommercial : airSupplierCommercials) {
                    JSONObject opsAmendmentsJson = new JSONObject();
                    opsAmendmentsJson.put("commercialName", airSupplierCommercial.getCommercialName());
                    opsAmendmentsJson.put("mdmRuleID", airSupplierCommercial.getMdmRuleID());
                    opsAmendmentsJson.put("commercialCalculationAmount", airSupplierCommercial.getCommercialCalculationAmount()==null?BigDecimal.ZERO:airSupplierCommercial.getCommercialCalculationAmount());
                    opsAmendmentsJson.put("commercialCalculationPercentage", airSupplierCommercial.getCommercialCalculationPercentage()==null?BigDecimal.ZERO:airSupplierCommercial.getCommercialCalculationPercentage());
                    opsAmendmentsJson.put("commercialFareComponent", airSupplierCommercial.getCommercialFareComponent());
                    opsAmendmentsJson.put("commercialCurrency",airSupplierCommercial.getCommercialCurrency());
                    opsAmendmentsJsonArr.put(opsAmendmentsJson);
                }

                break;

            case PRODUCT_SUB_CATEGORY_HOTELS:

                Optional<OpsRoom> optOpsRoom = order.getOrderDetails().getHotelDetails().getRooms().parallelStream()
                        .filter(room -> room.getRoomID().equals(supplierCommercialResource.getRoomId())).findAny();
                if (!optOpsRoom.isPresent()) {
                    throw new OperationException(String.format(Constants.ROOM_NOT_FOUND, supplierCommercialResource.getRoomId()));
                }

                List<OpsRoomSuppCommercial> accoSupplierCommercials = optOpsRoom.get().getRoomSuppCommercials();

                for (OpsRoomSuppCommercial accoSupplierCommercial : accoSupplierCommercials) {
                    JSONObject opsAmendmentsJson = new JSONObject();
                    opsAmendmentsJson.put("commercialName", accoSupplierCommercial.getCommercialName());
                    opsAmendmentsJson.put("mdmRuleID", accoSupplierCommercial.getMdmRuleID());
                    opsAmendmentsJson.put("commercialCalculationAmount", accoSupplierCommercial.getCommercialCalculationAmount()==null?BigDecimal.ZERO:accoSupplierCommercial.getCommercialCalculationAmount());
                    opsAmendmentsJson.put("commercialCalculationPercentage", accoSupplierCommercial.getCommercialCalculationPercentage()==null?BigDecimal.ZERO:accoSupplierCommercial.getCommercialCalculationPercentage());
                    opsAmendmentsJson.put("commercialFareComponent", accoSupplierCommercial.getCommercialFareComponent());
                    opsAmendmentsJson.put("commercialCurrency",accoSupplierCommercial.getCommercialCurrency());
                    opsAmendmentsJsonArr.put(opsAmendmentsJson);
                }

                break;
            case PRODUCT_SUB_CATEGORY_INDIAN_RAIL:
                break;
            case PRODUCT_SUB_CATEGORY_RAIL:
                break;
            default:
                break;

        }


        return opsAmendmentsJsonArr;
    }
		
}