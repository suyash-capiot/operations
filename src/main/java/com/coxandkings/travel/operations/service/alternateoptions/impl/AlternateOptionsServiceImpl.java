package com.coxandkings.travel.operations.service.alternateoptions.impl;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.coxandkings.travel.operations.resource.alternateOption.AlternateOptionResponse;
import com.coxandkings.travel.operations.resource.alternateOption.AlternateOptionsCompanyDetailsResource;
import com.coxandkings.travel.operations.resource.alternateOption.AlternateOptionsProductDetailsResource;
import com.coxandkings.travel.operations.resource.alternateOption.AlternateOptionsResource;
import com.coxandkings.travel.operations.resource.forex.ForexIndentResource;
import com.coxandkings.travel.operations.resource.forex.ForexPassengerResource;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.coxandkings.travel.operations.enums.alternateOptions.AlternateOptionsStatus;
import com.coxandkings.travel.operations.enums.forex.IndentStatus;
import com.coxandkings.travel.operations.enums.forex.IndentType;
import com.coxandkings.travel.operations.enums.forex.RequestStatus;
import com.coxandkings.travel.operations.enums.workflow.WorkflowOperation;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptions;
import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptionsProductDetails;
import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptionsResponseDetails;
import com.coxandkings.travel.operations.model.forex.ForexBooking;
import com.coxandkings.travel.operations.model.forex.ForexIndent;
import com.coxandkings.travel.operations.model.forex.RequestLockObject;
import com.coxandkings.travel.operations.repository.alternateoptions.AlternateOptionsRepository;
import com.coxandkings.travel.operations.service.alternateoptions.AlternateOptionsService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.service.workflow.WorkflowService;
import com.coxandkings.travel.operations.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional(readOnly = false)
public class AlternateOptionsServiceImpl implements AlternateOptionsService {

    private static String entityName = "AlternateOptions";
    
    private static ObjectMapper objectMapper = new ObjectMapper();
  
    @Autowired
    private AlternateOptionsRepository alternateOptionsRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private WorkflowService workflowService;

    @Override
    public String addAlternateOptions(JSONObject reqJson) {
        List<AlternateOptions> alternateOptionsList = new ArrayList<AlternateOptions>();

        JSONArray comapnyDetails = reqJson.getJSONArray("companyDetails");
        JSONArray productDetails = reqJson.getJSONArray("productDetails");

        List<AlternateOptions> alternateOptionsFromDB = alternateOptionsRepository.fetchAlternateoptions();

        for (int comapnyDetailsCount = 0; comapnyDetailsCount < comapnyDetails.length(); comapnyDetailsCount++) {
            JSONObject companyDetailsJSONObject = comapnyDetails.getJSONObject(comapnyDetailsCount);

            AlternateOptions alternateOptions = new AlternateOptions();
            String clienttype = companyDetailsJSONObject.optString("clientType");
            String companyMarket = companyDetailsJSONObject.optString("companyMarket");
            alternateOptions.setCompanyMarket(companyMarket);
            alternateOptions.setClientType(clienttype);

            boolean isOld = false;
            List<AlternateOptions> matchedCompanyMarketClientType = alternateOptionsFromDB.stream().filter(x -> x.getClientType().equals(clienttype) && x.getCompanyMarket().equals(companyMarket)).collect(Collectors.toList());
            if (matchedCompanyMarketClientType.size() > 0) {
                isOld = true;
                alternateOptions.setConfigurationId(matchedCompanyMarketClientType.get(0).getConfigurationId());
            }

            List<AlternateOptionsProductDetails> alternateOptionsProductDetails = new ArrayList<AlternateOptionsProductDetails>();
            for (int productDetailsCount = 0; productDetailsCount < productDetails.length(); productDetailsCount++) {
                JSONObject productDetailsJSONObject = productDetails.getJSONObject(productDetailsCount);
                AlternateOptionsProductDetails alternateOptionsProductDetail = new AlternateOptionsProductDetails();
                String productCategory = productDetailsJSONObject.optString("productCategory");
                String productSubCategory = productDetailsJSONObject.optString("productCategorySubType");
                alternateOptionsProductDetail.setProductCategory(productCategory);
                alternateOptionsProductDetail.setProductCategorySubType(productSubCategory);

                if (isOld) {
                    List<AlternateOptionsProductDetails> alternateOptionsProductDetailsSavedData = alternateOptionsRepository.fetchAlternateoptionProductDetails();

                    List<AlternateOptionsProductDetails> matchedAlternateOptionProductDetails = alternateOptionsProductDetailsSavedData.stream().filter(x -> x.getProductCategory().equals(productCategory) &&
                            x.getProductCategorySubType().equalsIgnoreCase(productSubCategory) && x.getAlternateOptions().getConfigurationId().equals(alternateOptions.getConfigurationId())).collect(Collectors.toList());

                    if (matchedAlternateOptionProductDetails.size() > 0) {
                        alternateOptionsProductDetail.setId(matchedAlternateOptionProductDetails.get(0).getId());
                    }
                }
                //alternateOptionsProductDetail.setAlternateOptions(alternateOptions);
                alternateOptionsProductDetails.add(alternateOptionsProductDetail);
            }

            alternateOptions.setAlternateOptionsProductDetails(alternateOptionsProductDetails);
            alternateOptions.setHigherLimitThreshold(reqJson.optString("higherThreshhold"));
            alternateOptions.setLowerLimitThreshold(reqJson.optString("lowerThreshold"));
            alternateOptions.setAlternateOfferProcess(reqJson.optString("alternateOfferProcess"));
            //alternateOptions.setStatus("Active");

            alternateOptionsList.add(alternateOptions);
        }

        List<AlternateOptions> savedAlternateOptions = alternateOptionsRepository.saveAlternateOptions(alternateOptionsList);

        JSONObject successObject = new JSONObject();
        successObject.put("status", "SUCCESS");
        return successObject.toString();
    }

    @Override
    public String searchAlternateOptions(JSONObject reqJson) {
        AlternateOptions alternateOptions = new AlternateOptions();

        String companyMarket = reqJson.optString("companyMarket");
        String clientType = reqJson.optString("clientType");

        String productCategory = reqJson.optString("productCategory");
        String productSubCategory = reqJson.optString("productCategorySubType");

        alternateOptions.setCompanyMarket(companyMarket);
        alternateOptions.setClientType(clientType);

        List<AlternateOptionsProductDetails> alternateOptionsProductDetails = new ArrayList<AlternateOptionsProductDetails>();
        AlternateOptionsProductDetails alternateOptionsProductDetail = new AlternateOptionsProductDetails();

        alternateOptionsProductDetail.setProductCategory(productCategory);
        alternateOptionsProductDetail.setProductCategorySubType(productSubCategory);
        alternateOptionsProductDetails.add(alternateOptionsProductDetail);
        alternateOptions.setAlternateOptionsProductDetails(alternateOptionsProductDetails);

        List<AlternateOptions> savedAlternateOptions = alternateOptionsRepository.searchAlternateOptions(alternateOptions);
        List<AlternateOptions> rearrangedAlternateOptions = savedAlternateOptions.stream().filter(distinctByKey(x -> x.getConfigurationId())).collect(Collectors.toList());

        JSONArray resultArray = new JSONArray();
        for (AlternateOptions alternateOptionsCurrent : rearrangedAlternateOptions) {
            JSONObject alternateJSONObject = new JSONObject();
            alternateJSONObject.put("configurationId", alternateOptionsCurrent.getConfigurationId());
            alternateJSONObject.put("companymarket", alternateOptionsCurrent.getCompanyMarket());
            alternateJSONObject.put("clientType", alternateOptionsCurrent.getClientType());
            alternateJSONObject.put("alternateOfferProcess", alternateOptionsCurrent.getAlternateOfferProcess());

            JSONArray productDetailsArray = new JSONArray();
            List<AlternateOptionsProductDetails> alternateOptionsProductDetails2 = alternateOptionsCurrent.getAlternateOptionsProductDetails();

            // TODO : Need to filter on the basis of productCategory and ProductSubCategory
//			List<AlternateOptionsProductDetails> filteredAlternateOptionsProductDetails = alternateOptionsProductDetails2.stream().filter(x-> (
//					/** Product Category not Empty, Product CategorySubType not empty*/
//					getProductCategorySubCategoryNonEmpty(productCategory, productSubCategory, x) ||
//					/** Product Category not Empty, Product CategorySubType empty*/
//					((((x.getProductcategorySubtype() == null || x.getProductcategorySubtype().isEmpty()) && (x.getProductCategory() != null && !x.getProductCategory().isEmpty()) && 
//							x.getProductCategory().equals(productCategory))) ||
//					/** Product Category Empty, Product SubCategory not empty */		
//					(((x.getProductCategory() == null || x.getProductCategory().isEmpty()) && (x.getProductcategorySubtype() != null && !x.getProductcategorySubtype().isEmpty()))  && 
//							x.getProductcategorySubtype().equals(productSubCategory)))  || 
//					/** Product Category Empty, Product CategorySubType empty*/
//					(((x.getProductcategorySubtype() == null || x.getProductcategorySubtype().isEmpty()) && (x.getProductCategory() == null || x.getProductCategory().isEmpty())) 
//							) )
//					).collect(Collectors.toList());

            for (AlternateOptionsProductDetails alternateOptionsProductDetailsCurrent : alternateOptionsProductDetails2) {
                JSONObject productDetailsJsonObject = new JSONObject();
                productDetailsJsonObject.put("productCategory", alternateOptionsProductDetailsCurrent.getProductCategory());
                productDetailsJsonObject.put("productCategorySubtype", alternateOptionsProductDetailsCurrent.getProductCategorySubType());
                productDetailsJsonObject.put("alternateOptionsProductDetailsID", alternateOptionsProductDetailsCurrent.getId());
                productDetailsArray.put(productDetailsJsonObject);
            }

            alternateJSONObject.put("higherLimit", alternateOptionsCurrent.getHigherLimitThreshold());
            alternateJSONObject.put("lowerLimit", alternateOptionsCurrent.getLowerLimitThreshold());

            alternateJSONObject.put("productDetails", productDetailsArray);
            alternateJSONObject.put("status", alternateOptionsCurrent.getStatus());
            resultArray.put(alternateJSONObject);
        }


        return resultArray.toString();
    }

    /**
     * This method return with consideration of pagination
     *
     * @param reqJson
     * @return
     */
    public AlternateOptionResponse searchByCriteria(JSONObject reqJson) {
        AlternateOptions alternateOptions = new AlternateOptions();

        String companyMarket = reqJson.optString("companyMarket");
        String clientType = reqJson.optString("clientType");

        String productCategory = reqJson.optString("productCategory");
        String productSubCategory = reqJson.optString("productCategorySubType");

        int pageSize = 0;
        int pageNumber = 0;
        try {
            pageSize = Integer.parseInt(reqJson.optString("pageSize"));
        } catch (Exception e) {
            pageSize = 5;
        }
        try {
            pageNumber = Integer.parseInt(reqJson.optString("pageNumber"));
        } catch (Exception e) {
            pageNumber = 1;
        }

        alternateOptions.setCompanyMarket(companyMarket);
        alternateOptions.setClientType(clientType);

        List<AlternateOptionsProductDetails> alternateOptionsProductDetails = new ArrayList<AlternateOptionsProductDetails>();
        AlternateOptionsProductDetails alternateOptionsProductDetail = new AlternateOptionsProductDetails();

        alternateOptionsProductDetail.setProductCategory(productCategory);
        alternateOptionsProductDetail.setProductCategorySubType(productSubCategory);
        alternateOptionsProductDetails.add(alternateOptionsProductDetail);
        alternateOptions.setAlternateOptionsProductDetails(alternateOptionsProductDetails);

        AlternateOptionResponse alternateOptionResponse = alternateOptionsRepository.searchAlternateOptions(alternateOptions, pageNumber, pageSize);
        List<AlternateOptions> rearrangedAlternateOptions = alternateOptionResponse.getAlternateOptions().stream().filter(distinctByKey(x -> x.getConfigurationId())).collect(Collectors.toList());
        alternateOptionResponse.setAlternateOptions(rearrangedAlternateOptions);
        return alternateOptionResponse;


    }

    /**
     * @param productCategory
     * @param productSubCategory
     * @param x
     * @return
     */
    private boolean getProductCategorySubCategoryNonEmpty(String productCategory, String productSubCategory,
                                                          AlternateOptionsProductDetails x) {
        return (x.getProductCategory() != null && !x.getProductCategory().isEmpty() && x.getProductCategorySubType() != null
                && !x.getProductCategorySubType().isEmpty()) && x.getProductCategory().equals(productCategory) && x.getProductCategorySubType().equals(productSubCategory);
    }

    private <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        /** keyExtractor.apply(t) implies t.getConfigurationID will be the return.
         * This will added to set. t-> seen.add(keyextractor.apply(t)) is for Predicate test. */
        return t -> seen.add(keyExtractor.apply(t));
    }

    @Override
    public String fetchAlternateOptions(String configurationId) {
        AlternateOptions alternateOptions = alternateOptionsRepository.fetchAlternateOptionsForConfigurationId(configurationId);

        JSONObject alternateOptionsJSONObject = new JSONObject();
        JSONArray companyDetailsJSONarray = new JSONArray();

        JSONObject companyDetailsJSONObject = new JSONObject();
        companyDetailsJSONObject.put("companyMarket", alternateOptions.getCompanyMarket());
        companyDetailsJSONObject.put("clientType", alternateOptions.getClientType());

        companyDetailsJSONarray.put(companyDetailsJSONObject);

        JSONArray productDetailsJSONarray = new JSONArray();

        List<AlternateOptionsProductDetails> alternateOptionsProductDetails = alternateOptions.getAlternateOptionsProductDetails();

        alternateOptionsProductDetails.stream().forEach(x -> {
            JSONObject productDetailsJSONObject = new JSONObject();
            productDetailsJSONObject.put("productCategory", x.getProductCategory());
            productDetailsJSONObject.put("productCategorySubType", x.getProductCategorySubType());
            productDetailsJSONarray.put(productDetailsJSONObject);
        });

        alternateOptionsJSONObject.put("companyDetails", companyDetailsJSONarray);
        alternateOptionsJSONObject.put("productDetails", productDetailsJSONarray);
        alternateOptionsJSONObject.put("higherThreshhold", alternateOptions.getHigherLimitThreshold());
        alternateOptionsJSONObject.put("lowerThreshold", alternateOptions.getLowerLimitThreshold());
        alternateOptionsJSONObject.put("alternateOfferProcess", alternateOptions.getAlternateOfferProcess());

        return alternateOptionsJSONObject.toString();
    }

    @Override
    public List<AlternateOptions> addAlternateOptionsResponseDetails(
            AlternateOptions alternateOptions) {
        List<AlternateOptions> alternateOptionsList = new ArrayList<>();
        alternateOptionsList.add(alternateOptions);
        List<AlternateOptions> alternateOptions1 = alternateOptionsRepository.saveAlternateOptions(alternateOptionsList);
//		AlternateOptionsResponseDetails alternateOptions1 = alternateOptionsRepository.addAlternateOptionsResponseDetails(alternateOptionsResponseDetails);
        return alternateOptionsList;
    }

    @Override
    public Map approve(String configurationId, String remarks) {
        return null;
    }

    @Override
    public Map reject(String configurationId, String remarks) {
        return null;
    }
    

}
