package com.coxandkings.travel.operations.service.failure.impl;

import com.coxandkings.travel.operations.model.failure.ThresholdConfiguration;
import com.coxandkings.travel.operations.model.failure.ThresholdConfigurationProductDetails;
import com.coxandkings.travel.operations.repository.failure.FailureThresholdConfigurationRepository;
import com.coxandkings.travel.operations.service.failure.FailureThresholdConfigurationService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class FailuresThresholdConfigurationServiceImpl implements FailureThresholdConfigurationService {

    @Autowired
    private FailureThresholdConfigurationRepository failureThresholdConfigurationRepository;

    @Override
    @Transactional
    public String addThresholdConfiguration(JSONObject reqJson) {
        List<ThresholdConfiguration> thresholdConfigurationList = new ArrayList<ThresholdConfiguration>();

        JSONArray companyDetails = reqJson.getJSONArray("companyDetails");
        JSONArray productDetails = reqJson.getJSONArray("productDetails");

        List<ThresholdConfiguration> thresholdConfigurations = failureThresholdConfigurationRepository.fetchThresholdConfiguration();

        for (int companyDetailsCount = 0; companyDetailsCount < companyDetails.length(); companyDetailsCount++) {
            JSONObject companyDetailsJSONObject = companyDetails.getJSONObject(companyDetailsCount);

            ThresholdConfiguration thresholdConfiguration = new ThresholdConfiguration();
            String clientType = companyDetailsJSONObject.optString("clientType");
            String companyMarket = companyDetailsJSONObject.optString("companyMarket");
            thresholdConfiguration.setCompanyMarket(companyMarket);
            thresholdConfiguration.setClientType(clientType);

            Boolean isOld = false;
            List<ThresholdConfiguration> matchedCompanyMarketClientType = thresholdConfigurations.stream().
                    filter(x -> x.getClientType().equals(clientType) && x.getCompanyMarket().equals(companyMarket)).collect(Collectors.toList());
            if (matchedCompanyMarketClientType.size() > 0) {
                isOld = true;
                thresholdConfiguration.setConfigurationId(matchedCompanyMarketClientType.get(0).getConfigurationId());
            }

            List<ThresholdConfigurationProductDetails> thresholdConfigurationProductDetails = new ArrayList<ThresholdConfigurationProductDetails>();
            for (int productDetailsCount = 0; productDetailsCount < productDetails.length(); productDetailsCount++) {
                JSONObject productDetailsJSONObject = productDetails.getJSONObject(productDetailsCount);
                ThresholdConfigurationProductDetails thresholdConfigurationProductDetails1 = new ThresholdConfigurationProductDetails();
                String productCategory = productDetailsJSONObject.optString("productCategory");
                String productSubCategory = productDetailsJSONObject.optString("productCategorySubType");
                thresholdConfigurationProductDetails1.setProductCategory(productCategory);
                thresholdConfigurationProductDetails1.setProductCategorySubtype(productSubCategory);

                if (isOld) {
                    List<ThresholdConfigurationProductDetails> alternateOptionsProductDetailsSavedData = failureThresholdConfigurationRepository.fetchThresholdConfigurationProductDetails();

                    List<ThresholdConfigurationProductDetails> matchedAlternateOptionProductDetails = alternateOptionsProductDetailsSavedData.stream().
                            filter(x -> x.getProductCategory().equals(productCategory) &&
                                    x.getProductCategorySubtype().equalsIgnoreCase(productSubCategory) && x.getThresholdConfiguration().getConfigurationId().
                                    equals(thresholdConfiguration.getConfigurationId())).collect(Collectors.toList());

                    if (matchedAlternateOptionProductDetails.size() > 0) {
                        thresholdConfigurationProductDetails1.setId(matchedAlternateOptionProductDetails.get(0).getId());
                    }
                }
                thresholdConfigurationProductDetails1.setThresholdConfiguration(thresholdConfiguration);
                thresholdConfigurationProductDetails.add(thresholdConfigurationProductDetails1);
            }

            thresholdConfiguration.setThresholdConfigurationProductDetails(thresholdConfigurationProductDetails);
            thresholdConfiguration.setMaxThresholdValue(reqJson.optString("maxThresholdValue"));
            thresholdConfiguration.setClientCurrency(reqJson.optString("clientCurrency"));
            thresholdConfiguration.setThresholdOfferProcess(reqJson.optString("thresholdOfferProcess"));
            thresholdConfiguration.setStatus("Active");

            thresholdConfigurationList.add(thresholdConfiguration);
        }

        List<ThresholdConfiguration> saveThresholdConfiguration = failureThresholdConfigurationRepository.saveThresholdConfiguration(thresholdConfigurationList);
        JSONObject successObject = new JSONObject();
        successObject.put("status", "SUCCESS");
        return successObject.toString();
    }

    @Override
    public JSONArray searchThresholdConfiguration(JSONObject reqJson) {
        ThresholdConfiguration thresholdConfiguration = new ThresholdConfiguration();

        String companyMarket = reqJson.optString("companyMarket");
        String clientType = reqJson.optString("clientType");

        String productCategory = reqJson.optString("productCategory");
        String productSubCategory = reqJson.optString("productCategorySubType");

        thresholdConfiguration.setCompanyMarket(companyMarket);
        thresholdConfiguration.setClientType(clientType);

        List<ThresholdConfigurationProductDetails> thresholdConfigurationProductDetailsList = new ArrayList<ThresholdConfigurationProductDetails>();
        ThresholdConfigurationProductDetails thresholdConfigurationProductDetails = new ThresholdConfigurationProductDetails();

        thresholdConfigurationProductDetails.setProductCategory(productCategory);
        thresholdConfigurationProductDetails.setProductCategorySubtype(productSubCategory);
        thresholdConfigurationProductDetailsList.add(thresholdConfigurationProductDetails);
        thresholdConfiguration.setThresholdConfigurationProductDetails(thresholdConfigurationProductDetailsList);

        List<ThresholdConfiguration> savedAlternateOptions = failureThresholdConfigurationRepository.searchThresholdConfiguration(thresholdConfiguration);
        List<ThresholdConfiguration> rearrangedAlternateOptions = savedAlternateOptions.stream().filter(distinctByKey(ThresholdConfiguration::getConfigurationId)).collect(Collectors.toList());

        JSONArray resultArray = new JSONArray();
        for (ThresholdConfiguration alternateOptionsCurrent : rearrangedAlternateOptions) {
            JSONObject alternateJSONObject = new JSONObject();
            alternateJSONObject.put("configurationId", alternateOptionsCurrent.getConfigurationId());
            alternateJSONObject.put("companyMarket", alternateOptionsCurrent.getCompanyMarket());
            alternateJSONObject.put("clientType", alternateOptionsCurrent.getClientType());
            alternateJSONObject.put("thresholdOfferProcess", alternateOptionsCurrent.getThresholdOfferProcess());
            alternateJSONObject.put("maxThresholdValue", alternateOptionsCurrent.getMaxThresholdValue());

            JSONArray productDetailsArray = new JSONArray();
            List<ThresholdConfigurationProductDetails> alternateOptionsProductDetails2 = alternateOptionsCurrent.getThresholdConfigurationProductDetails();
            List<ThresholdConfigurationProductDetails> filteredAlternateOptionsProductDetails = alternateOptionsProductDetails2.stream().filter(x -> (
                    /** Product Category not Empty, Product CategorySubType not empty*/
                    getProductCategorySubCategoryNonEmpty(productCategory, productSubCategory, x) ||
                            /** Product Category not Empty, Product CategorySubType empty*/
                            ((((x.getProductCategorySubtype() == null || x.getProductCategorySubtype().isEmpty()) && (x.getProductCategory() != null && !x.getProductCategory().isEmpty()) &&
                                    x.getProductCategory().equals(productCategory))) ||
                                    /** Product Category Empty, Product SubCategory not empty */
                                    (((x.getProductCategory() == null || x.getProductCategory().isEmpty()) && (x.getProductCategorySubtype() != null && !x.getProductCategorySubtype().isEmpty())) &&
                                            x.getProductCategorySubtype().equals(productSubCategory))) ||
                            /** Product Category Empty, Product CategorySubType empty*/
                            (((x.getProductCategorySubtype() == null || x.getProductCategorySubtype().isEmpty()) && (x.getProductCategory() == null || x.getProductCategory().isEmpty()))
                            ))
            ).collect(Collectors.toList());

            for (ThresholdConfigurationProductDetails alternateOptionsProductDetailsCurrent : filteredAlternateOptionsProductDetails) {
                JSONObject productDetailsJsonObject = new JSONObject();
                productDetailsJsonObject.put("productCategory", alternateOptionsProductDetailsCurrent.getProductCategory());
                productDetailsJsonObject.put("productCategorySubtype", alternateOptionsProductDetailsCurrent.getProductCategorySubtype());
                productDetailsArray.put(productDetailsJsonObject);
            }
            alternateJSONObject.put("productDetails", productDetailsArray);
            alternateJSONObject.put("status", alternateOptionsCurrent.getStatus());
            resultArray.put(alternateJSONObject);
        }
        return resultArray;
    }

    /**
     * @param productCategory
     * @param productSubCategory
     * @param x
     * @return
     */
    private boolean getProductCategorySubCategoryNonEmpty(String productCategory, String productSubCategory,
                                                          ThresholdConfigurationProductDetails x) {
        return (x.getProductCategory() != null && !x.getProductCategory().isEmpty() && x.getProductCategorySubtype() != null
                && !x.getProductCategorySubtype().isEmpty()) && x.getProductCategory().equals(productCategory) && x.getProductCategorySubtype().equals(productSubCategory);
    }

    private <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        /** keyExtractor.apply(t) implies t.getConfigurationID will be the return.
         * This will added to set. t-> seen.add(keyextractor.apply(t)) is for Predicate test. */
        return t -> seen.add(keyExtractor.apply(t));
    }


    @Override
    public String fetchThresholdConfiguration(String configurationId) {
        ThresholdConfiguration thresholdConfiguration = failureThresholdConfigurationRepository.fetchThresholdForConfigurationId(configurationId);

        JSONObject alternateOptionsJSONObject = new JSONObject();
        JSONArray companyDetailsJSONArray = new JSONArray();

        JSONObject companyDetailsJSONObject = new JSONObject();
        companyDetailsJSONObject.put("companyMarket", thresholdConfiguration.getCompanyMarket());
        companyDetailsJSONObject.put("clientType", thresholdConfiguration.getClientType());

        companyDetailsJSONArray.put(companyDetailsJSONObject);

        JSONArray productDetailsJSONArray = new JSONArray();

        List<ThresholdConfigurationProductDetails> thresholdConfigurationProductDetailsList = thresholdConfiguration.getThresholdConfigurationProductDetails();

        thresholdConfigurationProductDetailsList.forEach(x -> {
            JSONObject productDetailsJSONObject = new JSONObject();
            productDetailsJSONObject.put("productCategory", x.getProductCategory());
            productDetailsJSONObject.put("productCategorySubType", x.getProductCategorySubtype());
            productDetailsJSONArray.put(productDetailsJSONObject);
        });

        alternateOptionsJSONObject.put("companyDetails", companyDetailsJSONArray);
        alternateOptionsJSONObject.put("productDetails", productDetailsJSONArray);
        alternateOptionsJSONObject.put("maxThresholdValue", thresholdConfiguration.getMaxThresholdValue());
        alternateOptionsJSONObject.put("clientCurrency", thresholdConfiguration.getClientCurrency());
        alternateOptionsJSONObject.put("thresholdOfferProcess", thresholdConfiguration.getThresholdOfferProcess());

        return alternateOptionsJSONObject.toString();
    }

}
