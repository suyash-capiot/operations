package com.coxandkings.travel.operations.utils.manageOfflineBooking;

import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.exceptions.OperationException;

import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@Scope("singleton")
public class MDMDataUtils {


    private static Logger logger = LogManager.getLogger(MDMDataUtils.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T00:00:00'");
    private List<ClientInfo> mClientHierarchyList = null;
    private List<String> mProdList = new ArrayList<String>();
    private static JSONObject mdmDataJson = new JSONObject();
    private static JSONObject orgHierarchyJson = new JSONObject();

    public static JSONObject getOrgHierarchyJson() {
        return orgHierarchyJson;
    }

    public static void setOrgHierarchyJson(JSONObject orgHierarchyJson) {
        MDMDataUtils.orgHierarchyJson = orgHierarchyJson;
    }

    @Value(value= "${offlineBooking.roe}")
    private String roeURL;

    @Value(value= "${offlineBooking.clientB2Bs}")
    private String clientB2BURL;

    @Value(value= "${offlineBooking.clientCommercialBudgetedMargins}")
    private String clientCommBudgetmarginURL;

    @Value(value= "${offlineBooking.clientType}")
    private String clientTypeURL;

    @Value(value= "${offlineBooking.clientConfigEnableDisableProductSuppliers}")
    private String productSuppliersURL;

    @Value(value= "${offlineBooking.associateClientToGroup}")
    private String associateClientToGroupURL;

    @Value(value= "${offlineBooking.suppliersCredentials}")
    private String suppliersCredentialsURL;

    @Value(value= "${offlineBooking.cityInfo}")
    private String cityInfoURL;

    @Value(value= "${offlineBooking.corporateTravellers}")
    private String corpTravellerURL;

    @Value(value= "${offlineBooking.countryInfo}")
    private String countryInfoURL;

    @Value(value= "${brms.username}")
    private String userName;

    @Value(value= "${brms.password}")
    private String password;

    JSONArray jsArrCityObject = new JSONArray();
//    @Autowired
//    public  MDMDataUtils(JSONObject reqJson){
//        loadMDMData(reqJson);
//    }
//
//    public static JSONObject getMDMJson(JSONObject reqJson){
//         new MDMDataUtils(reqJson);
//
//            return mdmDataJson;
//    }

    public BigDecimal getRateOfExchange(String fromCcy, String toCcy) throws JsonProcessingException, OperationException {

        return getRoe(fromCcy,toCcy,null);
    }

    public BigDecimal getRateOfExchange(String fromCcy, String toCcy, String market) throws JsonProcessingException, OperationException {
        return getRoe(fromCcy,toCcy,market);
    }

    public BigDecimal getRateOfExchange(String fromCcy, String toCcy, String market,Date date) throws JsonProcessingException, OperationException, ParseException {
        return getRoeV2(fromCcy,toCcy,market,date);
    }

    public JSONObject loadMDMData( JSONObject reqJson){
        try{
            String market ="",suppCcy="",clientCcy="",clientID="",clientType="";
            if(reqJson.optJSONObject(Constants.MDM_PROP_CLIENTDETAILS)!=null){
                if(reqJson.getJSONObject(Constants.MDM_PROP_CLIENTDETAILS).optString(Constants.JSON_PROP_COMPANY_MKT).isEmpty()){
                    throw new OperationException(Constants.OPS_ERR_101);
                }
                else{
                    market = reqJson.getJSONObject(Constants.MDM_PROP_CLIENTDETAILS).getString(Constants.JSON_PROP_COMPANY_MKT);
                }

                if(reqJson.getJSONObject(Constants.MDM_PROP_CLIENTDETAILS).optString(Constants.JSON_PROP_CLIENT_TYPE).isEmpty()){
                    throw new OperationException(Constants.OPS_ERR_103);
                }
                else{
                    clientType = reqJson.getJSONObject(Constants.MDM_PROP_CLIENTDETAILS).getString(Constants.JSON_PROP_CLIENT_TYPE);
                }
            }
            if(reqJson.optJSONObject(Constants.JSON_PROP_PRODUCTDTLS)!=null){

                String productCategory = reqJson.getJSONObject(Constants.JSON_PROP_PRODUCTDTLS).optString(Constants.JSON_PROP_PROD_CAT);
                String productSubCategory = reqJson.getJSONObject(Constants.JSON_PROP_PRODUCTDTLS).optString(Constants.JSON_PROP_PROD_SUB_CAT);
                OpsProductCategory aProductCategory = OpsProductCategory.getProductCategory(productCategory);
                OpsProductSubCategory aProductSubCategory = OpsProductSubCategory.getProductSubCategory(aProductCategory, productSubCategory);
                if( aProductSubCategory != null ){
                    switch (aProductCategory){
                        case PRODUCT_CATEGORY_ACCOMMODATION:{
                            switch (aProductSubCategory){
                                case PRODUCT_SUB_CATEGORY_HOTELS:{
                                    JSONArray roomBreakupArr = reqJson.getJSONObject(Constants.JSON_PROP_TRAVEL_PAX_DTLS).optJSONArray("roomBreakup");
                                    if(roomBreakupArr!=null){
                                        for(int i=0;i<roomBreakupArr.length();i++){
                                            JSONObject roomJson = roomBreakupArr.getJSONObject(i);
                                            Boolean isRoomSelected =  roomJson.optBoolean("isRoomSelected");
//                                            if(isRoomSelected)
                                            {
                                                boolean isDefinedRateManually = roomJson.optBoolean("isDefineRatesManually");

                                                if(!isDefinedRateManually && roomJson.optJSONObject("supplierRates")!=null){
                                                    if(roomJson.getJSONObject("supplierRates").getJSONObject("supplierRateDetails").optJSONObject("defineRates")!=null){
                                                        if(roomJson.getJSONObject("supplierRates").getJSONObject("supplierRateDetails").getJSONObject("defineRates").optString(Constants.JSON_PROP_CCY).isEmpty()){
                                                            throw new OperationException(Constants.OPS_ERR_102);
                                                        }else {
                                                            suppCcy = roomJson.getJSONObject("supplierRates").getJSONObject("supplierRateDetails").getJSONObject("defineRates").getString(Constants.JSON_PROP_CCY);
                                                        }
                                                    }
                                                }
                                                else
                                                {
                                                    if(roomJson.getJSONObject("defineRatesManually")!=null)
                                                    {
                                                        if(roomJson.getJSONObject("defineRatesManually").optString(Constants.JSON_PROP_CCY).isEmpty())
                                                        {
                                                            throw new OperationException(Constants.OPS_ERR_102);
                                                        }
                                                        else
                                                        {
                                                            suppCcy = roomJson.getJSONObject("defineRatesManually").getString(Constants.JSON_PROP_CCY);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                break;
                            }
                        }
                        break;
                    }
                }
            }
            mdmDataJson.put("supplierCurrency",suppCcy);
            JSONObject clientTypeJson = getPOSAndClientLang(market,clientType);
            String pointOfSale = "";
            String clientLang = "";
            if(clientTypeJson!=null && clientTypeJson.optJSONObject(Constants.MDM_PROP_CLIENTSTRUCT)!=null){
                clientLang = clientTypeJson.getJSONObject(Constants.MDM_PROP_CLIENTSTRUCT).optString(Constants.MDM_PROP_LANGUAGE);
                JSONArray posJsonArr = clientTypeJson.getJSONObject(Constants.MDM_PROP_CLIENTSTRUCT).optJSONArray(Constants.MDM_PROP_POS);
                if(posJsonArr!=null){
                    for(int i=0;i<posJsonArr.length();i++){
                        String pos = posJsonArr.getString(i);
                        if(pos.equalsIgnoreCase("Website")){
                            pointOfSale = pos;
                        }
                    }
                }
                if(pointOfSale.isEmpty()){
                    pointOfSale = "Website";//TODO: need to visit this
                }
                mdmDataJson.put("pointOfSale",pointOfSale);
                mdmDataJson.put("clientLanguage",clientLang);

                getOrgHierarchyJson(clientTypeJson);

                mdmDataJson.put("clientCategory",reqJson.getJSONObject(Constants.MDM_PROP_CLIENTDETAILS).getString("clientCategory"));
                mdmDataJson.put("clientSubCategory",reqJson.getJSONObject(Constants.MDM_PROP_CLIENTDETAILS).getString("clientSubCategory"));
                mdmDataJson.put("clientMarket",reqJson.getJSONObject(Constants.MDM_PROP_CLIENTDETAILS).getString("companyMarket"));
                mdmDataJson.put("clientName",reqJson.getJSONObject(Constants.MDM_PROP_CLIENTDETAILS).getString("clientName"));
                if(clientCcy.isEmpty()){
                    JSONArray transCurrJsonArr = clientTypeJson.getJSONObject(Constants.MDM_PROP_CLIENTSTRUCT).optJSONArray(Constants.MDM_PROP_TRANS_CCY);
                    if(transCurrJsonArr!=null){
                        for(int i=0;i<transCurrJsonArr.length();i++){
                            JSONObject currencyJson = transCurrJsonArr.getJSONObject(i);
                            if(market.equalsIgnoreCase(currencyJson.optString(Constants.JSON_PROP_MKT))){
                                if(currencyJson.optString(Constants.JSON_PROP_CCY).isEmpty()){
                                    clientCcy = suppCcy;
                                }
                                else {
                                    clientCcy = currencyJson.optString(Constants.JSON_PROP_CCY);
                                }

                            }

                        }
                    }
                }
            }
            mdmDataJson.put("clientCurrency",clientCcy);
            mClientHierarchyList = getClientCommercialEntityDetails(clientID,clientLang,clientType,market,pointOfSale,null);
            getEntityDetails( mClientHierarchyList);

            if (mClientHierarchyList != null && mClientHierarchyList.size() > 0) {
                ClientInfo clInfo = mClientHierarchyList.get(mClientHierarchyList.size() - 1);
                if (clInfo.getmCommEntityType() == ClientInfo.CommercialsEntityType.ClientGroup) {
                    mdmDataJson.put("clientGroup",clInfo.getmCommEntityId());
                }
            }

            getClientInfo(reqJson.getJSONObject(Constants.MDM_PROP_CLIENTDETAILS));
            getRoe(suppCcy, clientCcy,market);
            if(reqJson.optJSONObject(Constants.JSON_PROP_CLIENTDTLS)!=null)
                mdmDataJson.put("clientName",reqJson.getJSONObject(Constants.JSON_PROP_CLIENTDTLS).optString("clientName"));
            mdmDataJson.put("iatanumber",""); //required for activities supplierCommercialsRQ
            JSONObject productDetails = reqJson.optJSONObject(Constants.JSON_PROP_PRODUCTDTLS);

//            TODO: uncomment for activities
//            if(productDetails!=null){
//                if(isStringNotNullAndNotEmpty(productDetails.optString("productCategory")) || isStringNotNullAndNotEmpty(productDetails.optString("productSubCategory")))
//                    mdmDataJson.put(Constants.JSON_PROP_CREDSNAME,getCredentialName(productDetails,"",clientID,market,clientType,clientLang,pointOfSale));
//            }

            return mdmDataJson;
        }catch (Exception e){
            logger.info("Exception occured while loading MDMData");
        }
        return null;
    }

    public void getClientInfo(JSONObject clientDtls){
        try{
            JSONObject filters = new JSONObject();
            filters.put("deleted", false);
            filters.put("clientProfile.clientDetails.clientName", clientDtls.optString("clientName"));
            filters.put("clientProfile.clientDetails.clientCategory", clientDtls.optString("clientCategory"));
            filters.put("clientProfile.clientDetails.clientSubCategory", clientDtls.optString("clientSubCategory"));

            String URL =  clientB2BURL + objectMapper.writeValueAsString(filters);
            URI uri = UriComponentsBuilder.fromUriString(URL).build().encode().toUri();

            String result = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
            JSONObject latestJson = null;
            if(isStringNotNullAndNotEmpty(result)){
                latestJson = getLatestUpdatedJson(new JSONObject(result));
            }


            if(latestJson.optJSONArray("locationDetails")!=null){
                JSONObject locDtls = latestJson.getJSONArray("locationDetails").optJSONObject(0);
                if(locDtls!=null){
                    if(locDtls.optJSONObject("addressDetails")!=null){
                        JSONObject addrDtls = locDtls.getJSONObject("addressDetails");
                        mdmDataJson.put("clientCountry",addrDtls.optString("country"));
                        mdmDataJson.put("clientState",addrDtls.optString("state"));
                        mdmDataJson.put("clientCity",addrDtls.optString("city"));
                    }
                }
            }
        }catch(Exception e){
            logger.info("Exception occured while fetching clientInfo");
        }


    }

//    public String getClientCategoryForB2B(String clientId){
//        JSONObject clientB2B = getClientProfile(clientId);
//        String clientCatForB2B = "";
//        if(clientB2B!=null){
//            JSONObject clientProfile = clientB2B.optJSONObject("clientProfile");
//            if(clientProfile.optJSONObject("clientDetails")!=null){
//                JSONObject clientDetails = clientProfile.getJSONObject("clientDetails");
//                clientCatForB2B = clientDetails.optString("clientCategory");
//                mdmDataJson.put("clientCategoryForB2B",clientCatForB2B);
//            }
//
//        }
//        return clientCatForB2B;
//    }

    private void getClientCategoryForB2C(String clientType){
        try{
            JSONObject filters = new JSONObject();
            filters.put("deleted", false);
            filters.put(Constants.MDM_PROP_CLIENTDETAILS.concat(".").concat(Constants.MDM_PROP_CLIENTTYPE), clientType);

            String URL = corpTravellerURL + objectMapper.writeValueAsString(filters);
            URI uri = UriComponentsBuilder.fromUriString(URL).build().encode().toUri();
            String result = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
            JSONObject latestJson = null;
            if(isStringNotNullAndNotEmpty(result)){
                latestJson = getLatestUpdatedJson(new JSONObject(result));
            }
            if(latestJson!=null){
                if(latestJson.optJSONObject("clientDetails")!=null){
                    JSONObject clientDtls = latestJson.getJSONObject("clientDetails");
                    mdmDataJson.put("clientCategoryForB2C",clientDtls.optString("clientCategory"));
                    mdmDataJson.put("clientSubCategoryForB2C","");
                }
            }
        }catch (Exception e){
            logger.info("Exception while fetching B2C clientCategory");
        }

    }

    public JSONObject getOrgHierarchyJson(JSONObject clientTypeJson){
        JSONObject orgHierarchy = null;
        if(clientTypeJson.optJSONObject(Constants.MDM_PROP_ORGHIERARCHY)!=null){
            orgHierarchy = clientTypeJson.getJSONObject(Constants.MDM_PROP_ORGHIERARCHY);
            mdmDataJson.put(Constants.MDM_PROP_ORGHIERARCHY,orgHierarchy);
            orgHierarchyJson = orgHierarchy;
        }
        return orgHierarchy;
    }

    public JSONObject getPOSAndClientLang(String market,String clientType)throws OperationException{
        try{
            JSONObject filters = new JSONObject();
            filters.put("deleted", false);
            filters.put(Constants.MDM_PROP_CLIENTSTRUCT.concat(".").concat(Constants.MDM_PROP_CLIENTENTITYTYPE), clientType);
            filters.put(Constants.MDM_PROP_CLIENTSTRUCT.concat(".").concat(Constants.MDM_PROP_CLIENTMARKET), market);
            String URL = clientTypeURL + objectMapper.writeValueAsString(filters);
            URI uri = UriComponentsBuilder.fromUriString(URL).build().encode().toUri();
            String result = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
            JSONObject latestJson = null;
            if(isStringNotNullAndNotEmpty(result)){
                latestJson = getLatestUpdatedJson(new JSONObject(result));
            }
            return latestJson ;
        }catch(Exception e){
            logger.info("Exception occured while fetching POS and client language");
        }
        return null;
    }

    private String getCredentialName(JSONObject prodJson, String supplierID, String market, String clientID,String clientType,String clientLang,String pointOfSale)throws OperationException{

        JSONObject clientEntityProdSuppJson =  getProductSuppliersForClient(market,clientID,clientType,clientLang,pointOfSale);
        if (clientEntityProdSuppJson == null || clientEntityProdSuppJson.length()==0) {
            throw new OperationException("No product supplier mapping was found for client entity");
        }
        JSONArray prodCatDocsJsonArr = clientEntityProdSuppJson.optJSONArray(Constants.MDM_PROP_PRODCATEGS);
        if (prodCatDocsJsonArr == null) {
            throw new OperationException("No product categories defined in product supplier mapping for client entity");
        }
        JSONObject suppCredsJson = null;
        for(int i=0;i<prodCatDocsJsonArr.length();i++){
            JSONObject prodCatJson = prodCatDocsJsonArr.getJSONObject(i);
            String prodCategory = prodCatJson.getString(Constants.MDM_PROP_PRODCATEG);
            JSONArray prodSubCatJsonArr = prodCatJson.getJSONArray(Constants.MDM_PROP_PRODCATEGSUBTYPES);
            if (prodSubCatJsonArr == null || prodSubCatJsonArr.length() == 0) {
                continue;
            }
            for(int j=0;j<prodSubCatJsonArr.length();j++){
                JSONObject prodSubCatJson = prodSubCatJsonArr.getJSONObject(j);
                String prodSubCategory = prodSubCatJson.getString(Constants.MDM_PROP_SUBTYPE);
                String prod = prodCategory.concat("|").concat(prodSubCategory);
                mProdList.add(prod);
                JSONArray mappingsJsonArr = prodSubCatJson.optJSONArray(Constants.MDM_PROP_MAPPINGS);
                if (mappingsJsonArr == null || mappingsJsonArr.length() == 0) {
                    continue;
                }

                List<String> dupCheckList = new ArrayList<String>();
                for(int k=0;k<mappingsJsonArr.length();k++){
                    JSONObject mappingJson = mappingsJsonArr.getJSONObject(k);
                    JSONObject suppJson = mappingJson.getJSONObject(Constants.MDM_PROP_SUPP);
                    String suppID = suppJson.getString(Constants.MDM_PROP_SUPPID);
                    JSONArray creds = suppJson.getJSONArray(Constants.MDM_PROP_SUPPCREDS);
                    if (creds == null || creds.length() == 0) {
                        continue;
                    }
                    for(int l=0;l <creds.length();l++){
                        String cred = creds.getString(l);
                        String dupCheckKey = String.format("%s|%s", suppID, cred);
                        if (dupCheckList.contains(dupCheckKey)) {
                            continue;
                        }
                        dupCheckList.add(dupCheckKey);
                        suppCredsJson = getSupplierCredentialsConfig(suppID, cred);
                    }

                }
            }
        }
        if(suppCredsJson!=null){
            return suppCredsJson.optString(Constants.JSON_PROP_CREDSNAME);
        }
        return "";
    }

    private JSONObject getSupplierCredentialsConfig(String suppID, String cred){
        try{
            JSONObject filters = new JSONObject();
            filters.put("deleted", false);
            filters.put("_id", cred);
            filters.put(Constants.MDM_PROP_SUPP.concat(".").concat(Constants.MDM_PROP_SUPPID), suppID);

            String URL = suppliersCredentialsURL + objectMapper.writeValueAsString(filters);
            URI uri = UriComponentsBuilder.fromUriString(URL).build().encode().toUri();
            String result = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
            JSONObject latestJson = null;
            if(isStringNotNullAndNotEmpty(result)){
                latestJson = getLatestUpdatedJson(new JSONObject(result));
            }
            return latestJson;
        }catch(Exception e){
            logger.info("Exception occured while fetching supplier credentials");
        }
        return null;
    }
//
//    public ProductSupplier getSupplierForProduct(String prodCategory, String prodCategorySubType, String suppID){
//        List<ProductSupplier> prodSupps = mProdSuppMap.get(getProdSuppMapKey(prodCategory, prodCategorySubType));
//        for (ProductSupplier prodSupp : prodSupps) {
//            if (prodSupp.getSupplierID().equals(suppID)) {
//                return prodSupp;
//            }
//        }
//
//        return null;
//    }

    private JSONObject getProductSuppliersForClient(String clientMarket, String clientID,String clientType,String clientLang,String pointOfSale){
        JSONObject clientEntityProdSuppJson = null;
        if (isStringNotNullAndNotEmpty(clientID)){
            clientEntityProdSuppJson = getProductSuppliersConfig(clientID, clientMarket);
            if ( clientEntityProdSuppJson==null) {
                return clientEntityProdSuppJson;
            }

            clientEntityProdSuppJson = getClientGroupProductSuppliers(clientID, clientMarket);
            if (clientEntityProdSuppJson != null || clientEntityProdSuppJson.length()!=0) {
                return clientEntityProdSuppJson;
            }


        }
        return getClientTypeProductSuppliers(clientType, clientMarket,clientLang,pointOfSale);
    }

    private JSONObject getClientTypeProductSuppliers(String clientType, String clientMarket,String clientLang,String pointOfSale){
        String clientTypeEntity = getClientTypeEntity(clientType, clientMarket,clientLang,pointOfSale);
        if(isStringNotNullAndNotEmpty(clientTypeEntity)){
            getProductSuppliersConfig(clientTypeEntity, clientMarket);
        }else{
            return null;
        }
        return null;
    }

    private String getClientTypeEntity(String clientType, String clientMarket,String clientLang,String pointOfSale) {
        JSONObject clientEntityJson = getClientTypeEntityJson(clientType, clientMarket,clientLang,pointOfSale);
        return clientEntityJson.optString("_id");
    }

//    private static JSONObject getClientTypeEntityJson(String clientType, String clientMarket){
//        try{
//            JSONObject filters = new JSONObject();
//            filters.put("deleted", false);
//            filters.put(Constants.MDM_PROP_CLIENTSTRUCT.concat(".").concat(Constants.MDM_PROP_CLIENTENTITYTYPE), clientType);
//            filters.put(Constants.MDM_PROP_CLIENTSTRUCT.concat(".").concat(Constants.MDM_PROP_CLIENTMKT), clientMarket);
//
//
//
//        }catch(Exception e){
//            logger.info("Exception occured in getting client type entity");
//        }
//    }

    private JSONObject getClientGroupProductSuppliers(String clientID, String clientMarket){
        try{
            JSONObject filters = new JSONObject();
            filters.put("deleted", false);
            filters.put(Constants.MDM_PROP_CLIENTID, clientID);
//            filters.put(Constants.MDM_PROP_GROUPS.concat(".").concat(Constants.MDM_PROP_PRODSUPP), new Document(QueryOperators.EXISTS, true));

            String URL = associateClientToGroupURL + objectMapper.writeValueAsString(filters);
            URI uri = UriComponentsBuilder.fromUriString(URL).build().encode().toUri();
            String result = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
            JSONObject latestJson = null;
            if(isStringNotNullAndNotEmpty(result)){
                latestJson = getLatestUpdatedJson(new JSONObject(result));
                String clientGroupID = "";
                if(latestJson.optJSONObject(Constants.MDM_PROP_GROUPS)!=null){
                    clientGroupID = latestJson.getJSONObject(Constants.MDM_PROP_GROUPS).optString(Constants.MDM_PROP_PRODSUPP);
                }
                return getProductSuppliersConfig(clientGroupID, clientMarket);
            }
            else{
                return null;
            }

        }catch (Exception e){
            logger.info("Exception occured while fetching client product suppliers ");
        }
        return null;
    }

    private JSONObject getProductSuppliersConfig(String entityId, String market) {

        try{
            JSONObject filters = new JSONObject();
            filters.put("deleted", false);
            filters.put(Constants.MDM_PROP_PRODSUPPATTACHEDTO.concat(".").concat(Constants.MDM_PROP_ENTITYID), entityId);
            filters.put(Constants.MDM_PROP_PRODSUPPATTACHEDTO.concat(".").concat(Constants.MDM_PROP_COMPANYMKT), market);

            String URL =  productSuppliersURL + objectMapper.writeValueAsString(filters);
            URI uri = UriComponentsBuilder.fromUriString(URL).build().encode().toUri();
            String result = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
            JSONObject latestJson = null;
            if(isStringNotNullAndNotEmpty(result)){
                latestJson = getLatestUpdatedJson(new JSONObject(result));
            }

            return latestJson;
        }catch(Exception e){
            logger.info("Exception occured while getting supplier configuration");
        }
        return null;
    }

    private static String getProdSuppMapKey(String prodCategory, String prodCategorySubType) {
        return prodCategory.concat("|").concat(prodCategorySubType);
    }

    private BigDecimal getRoe(String fromCcy, String toCcy, String market)throws JsonProcessingException, OperationException {

        if( !isStringNotNullAndNotEmpty(fromCcy) || !isStringNotNullAndNotEmpty(toCcy)){
            throw new OperationException(Constants.OPS_ERR_100);
        }
        if(fromCcy.equalsIgnoreCase(toCcy)){
            JSONObject temp = new JSONObject();
            temp.put("fromCurrency",fromCcy);
            temp.put("toCurrency",fromCcy);
            temp.put("ROE",BigDecimal.ONE);
            if(market!=null){
                mdmDataJson.put("roeWithoutCompanyMarket",temp);
            }else{
                temp.put("companyMarket",market);
                mdmDataJson.put("roeWithoutCompanyMarket",temp);
            }
            return BigDecimal.ONE;
        }
        JSONObject props = new JSONObject();
        props.put("roeType", "Daily ROE");
        props.put("deleted", false);

        String URL = roeURL + objectMapper.writeValueAsString(props);

        URI uri = UriComponentsBuilder.fromUriString(URL).build().encode().toUri();

        String result = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);

        JSONObject resJson = new JSONObject(result!=null ? result : "{}");
        JSONArray dataJsoNarr = resJson.optJSONArray("data");
        Object effDate;
        Calendar cal;
        String currentFromCcy,currentToCcy;
        BigDecimal roeVal = new BigDecimal(0);
        for(int i=0;i<dataJsoNarr.length();i++){
            JSONObject dataJson = dataJsoNarr.getJSONObject(i);
            JSONArray roeDataArr = dataJson.optJSONArray("ROE")!=null ?  dataJson.getJSONArray("ROE") : new JSONArray();
            effDate = dataJson.opt("effectiveFrom");
            if(effDate instanceof String) {
                try {
                    effDate = DATE_FORMAT.format(DATE_FORMAT.parse((String) effDate));
                } catch (ParseException e) {
                    logger.warn(String.format("Effective date parse error for ROE document with id: %s", dataJson.getString("_id")));
                    continue;
                }
            }
            else if(effDate instanceof JSONObject) {
                cal = Calendar.getInstance();
                cal.set(((JSONObject) effDate).getInt("year"),((JSONObject) effDate).getInt("month")-1,((JSONObject) effDate).getInt("day"));
                effDate =  DATE_FORMAT.format(cal.getTime());
            }else {
                //As per BRD eff Date is mandatory but not in UI.This should not reach ideally
                logger.warn(String.format("Effective date not found for ROE document with id: %s", dataJson.getString("_id")));
                continue;
            }
            Boolean flag= false;
            for(int j=0;j<roeDataArr.length();j++){
                JSONObject roeData = roeDataArr.getJSONObject(j);
                currentFromCcy = roeData.optString("fromCurrency");
                currentToCcy = roeData.optString("toCurrency");
                if(!isStringNotNullAndNotEmpty(currentFromCcy) || !isStringNotNullAndNotEmpty(currentToCcy))
                {
                    logger.warn(String.format("One of the currency is evaluated to be null or empty for document  with id: %s, fromCurrency: %s, toCurrency: %s", dataJson.getString("_id"), fromCcy, toCcy));
                    continue;
                }
                if(fromCcy.equalsIgnoreCase(currentFromCcy) && toCcy.equalsIgnoreCase(currentToCcy)){
                    if(!roeData.has("sellingROE")) {
                        logger.warn(String.format("No selling ROE value found for document with id: %s, fromCurrency: %s, toCurrency: %s", dataJson.getString("_id"), fromCcy, toCcy));
                        //This should never happen as selling roe is mandatory when daily roe is defined.This check needs to be added in UI!!!
                        continue;
                    }
                    roeVal = roeData.optBigDecimal("sellingROE",BigDecimal.ZERO);
                    JSONObject temp = new JSONObject();
                    temp.put("fromCurrency",fromCcy);
                    temp.put("toCurrency",fromCcy);
                    temp.put("ROE",roeVal);
                    mdmDataJson.put("roeWithoutCompanyMarket",temp);
                    JSONArray mrktDataArr = dataJson.optJSONArray("companyMarkets")!=null ? dataJson.getJSONArray("companyMarkets") : new JSONArray();
                    String marketName = "";
                    for(int k=0;k<mrktDataArr.length();k++) {
                        JSONObject mrktJson = mrktDataArr.getJSONObject(k);
                        if(market!=null){
                            if(market.equalsIgnoreCase(mrktJson.optString("name")))
                                marketName = mrktJson.optString("name");
                        }

                    }
                    if (marketName.isEmpty() == false) {
                        temp.put("companyMarket",marketName);
                        mdmDataJson.put("roeWithoutCompanyMarket",temp);
                    }
                    flag = true;
                    break;
                }
            }
            if(flag==true)
                break;
        }
        return roeVal;
    }

    public static boolean isStringNotNullAndNotEmpty(String str) {
        return ! isStringNullOrEmpty(str);
    }

    public static boolean isStringNullOrEmpty(String str) {
        return (str == null || str.isEmpty());
    }


    public List<ClientInfo> getClientCommercialEntityDetails(String clientID, String clientLang, String clientType, String clientMkt, String pointOfSale, List<ClientInfo> clHierList) throws JsonProcessingException, OperationException {

        if (clHierList == null) {
            clHierList = new ArrayList<ClientInfo>();
        }
        JSONObject clientJson =  getClientProfile(clientID);
        JSONObject clientProfile = null;
        if(clientJson!=null){
            if(clientJson.optJSONObject(Constants.MDM_PROP_CLIENTPROFILE)!=null){
                clientProfile = clientJson.getJSONObject(Constants.MDM_PROP_CLIENTPROFILE);
            }
        }
        JSONObject clientDtls = null;
        if(clientProfile!=null){
            clientDtls = clientProfile.optJSONObject(Constants.MDM_PROP_CLIENTDETAILS)!=null ? clientProfile.getJSONObject(Constants.MDM_PROP_CLIENTDETAILS):null;
        }
        String clientMarket = "";
        String parentClientID = "";
        if(clientDtls!=null)
        {
            clientMarket = clientDtls.optString(Constants.MDM_PROP_CLIENTMKT);
            //String parentClientID = getParentClientID(clientID);
            parentClientID = clientDtls.optString(Constants.MDM_PROP_PARENTASSOC);
        }
        if (isStringNotNullAndNotEmpty(parentClientID)) {
            getClientCommercialEntityDetails(parentClientID, clientLang,clientType, clientMkt, pointOfSale, clHierList);
        }

        ClientInfo clInfo = new ClientInfo();
        clInfo.setmClientId(clientID);
        clInfo.setmClientMarket(clientMarket);
        clInfo.setmParentId(parentClientID);

        String clGrpID = null;
        JSONObject clTypeJson = null;

        if (hasClientCommercialsDefinition(clientID, "Client", clientMkt)){
            clInfo.setmCommEntityType(ClientInfo.CommercialsEntityType.ClientSpecific);
            clInfo.setmClientMarket(clientMarket);
            clInfo.setmCommEntityId(clientID);
        }
        else if (isStringNotNullAndNotEmpty(clGrpID = getClientGroupWithCommercialsDefinition(clientID, clientMkt))) {
            clInfo.setmCommEntityType(ClientInfo.CommercialsEntityType.ClientGroup);
            clInfo.setmCommEntityId(clGrpID);
            clInfo.setmClientMarket(clientMarket);
        }
        else if ((clTypeJson = getClientTypeEntityJson(clientType, clientMkt,clientLang,pointOfSale))!=null){
            clInfo.setmCommEntityType(ClientInfo.CommercialsEntityType.ClientType);
            //clInfo.setCommercialsEntityId(clTypeID);clTypeJson.get(DBCollection.ID_FIELD_NAME)
            clInfo.setmCommEntityId(clTypeJson.optString("_id"));
            String entityMarket = "";

            if(clTypeJson.optJSONObject(Constants.MDM_PROP_CLIENTSTRUCT)!=null){
                entityMarket = clTypeJson.getJSONObject(Constants.MDM_PROP_CLIENTSTRUCT).optString(Constants.MDM_PROP_CLIENTMKT);
            }
            clInfo.setmCommEntityMarket(entityMarket);
        }
        clHierList.add(clInfo);
        JSONArray clHierJsonArr = new JSONArray();
        clHierJsonArr.put(clInfo.toJSON());
        mdmDataJson.put(Constants.JSON_PROP_CLIENTCOMMENTITYDTLS,clHierJsonArr);
        return clHierList;

    }

    private JSONObject getClientTypeEntityJson(String clientType, String clientMarket,String clientLang,String pointOfSale){
        try{
            JSONObject filters = new JSONObject();
            filters.put("deleted", false);
            filters.put(Constants.MDM_PROP_CLIENTSTRUCT.concat(".").concat(Constants.MDM_PROP_CLIENTENTITYTYPE), clientType);
            filters.put(Constants.MDM_PROP_CLIENTSTRUCT.concat(".").concat(Constants.MDM_PROP_CLIENTMKT), clientMarket);
            filters.put(Constants.MDM_PROP_CLIENTSTRUCT.concat(".").concat(Constants.MDM_PROP_LANGUAGE), clientLang);
            filters.put(Constants.MDM_PROP_CLIENTSTRUCT.concat(".").concat(Constants.MDM_PROP_POS), pointOfSale);

            String URL =  clientTypeURL + objectMapper.writeValueAsString(filters);
            URI uri = UriComponentsBuilder.fromUriString(URL).build().encode().toUri();
            String result = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
            JSONObject latestJson = null;
            if(isStringNotNullAndNotEmpty(result)){
                latestJson = getLatestUpdatedJson(new JSONObject(result));
            }
            return latestJson;
        }catch (Exception e){

        }
        return null;
    }

    private String getClientGroupWithCommercialsDefinition(String clientId, String clientMarket){
        try{
            JSONObject filters = new JSONObject();
            filters.put("deleted", false);
            filters.put(Constants.MDM_PROP_CLIENTID, clientId);

            String URL =  clientCommBudgetmarginURL + objectMapper.writeValueAsString(filters);
            URI uri = UriComponentsBuilder.fromUriString(URL).build().encode().toUri();
            String result = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
            JSONObject latestJson = null;
            if(isStringNotNullAndNotEmpty(result)){
                latestJson = getLatestUpdatedJson(new JSONObject(result));
            }
            JSONArray dataJsonArr = null;
            if(latestJson!=null)
                dataJsonArr = latestJson.optJSONArray("data");
            JSONArray resJsonArr = new JSONArray();
            if(dataJsonArr!=null){
                for(int i=0;i<dataJsonArr.length();i++){
                    JSONObject groups = dataJsonArr.getJSONObject(i).optJSONObject(Constants.MDM_PROP_GROUPS);
                    if(groups!=null && isStringNotNullAndNotEmpty(groups.optString(Constants.MDM_PROP_COMMERCIALSID)))
                        resJsonArr.put(dataJsonArr.getJSONObject(i));
                }
            }
            JSONObject resJson = new JSONObject();
            resJson.put("data", resJsonArr);
            latestJson = getLatestUpdatedJson(resJson);
            String res = "";
            if(latestJson!=null && latestJson.optJSONObject(Constants.MDM_PROP_GROUPS)!=null){
                res = latestJson.getJSONObject(Constants.MDM_PROP_GROUPS).optString(Constants.MDM_PROP_COMMERCIALSID);
            }
            return res;
        }catch (Exception e){
            logger.info("exception occured while fetching clientgroup with commercial defination");
        }
        return null;
    }


    private boolean hasClientCommercialsDefinition(String entityId, String entityType, String entityMarket){
        try{
            JSONObject filters = new JSONObject();
            filters.put("deleted", false);
            filters.put(Constants.MDM_PROP_ENTITYID, entityId);
            filters.put(Constants.MDM_PROP_BUDGETMARGINATTACHEDTO.concat(".").concat(Constants.MDM_PROP_ENTITYTYPE), entityType);
            filters.put(Constants.MDM_PROP_BUDGETMARGINATTACHEDTO.concat(".").concat(Constants.MDM_PROP_COMPANYMKT), entityMarket);

            String URL =  clientCommBudgetmarginURL + objectMapper.writeValueAsString(filters);
            URI uri = UriComponentsBuilder.fromUriString(URL).build().encode().toUri();
            String result = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
            JSONObject latestJson = null;
            if(isStringNotNullAndNotEmpty(result)){
                latestJson = getLatestUpdatedJson(new JSONObject(result));
            }
            if(latestJson!=null ){
                return true;
            }
        }catch (Exception e){
            logger.info("Exception occured in hasClientCommercialDefination");

        }
        return false;
    }

    private JSONObject getClientProfile(String clientID) {
        try{
            JSONObject filters = new JSONObject();
            filters.put("deleted", false);
            filters.put("_id", clientID);

            String URL =  clientB2BURL + objectMapper.writeValueAsString(filters);
            URI uri = UriComponentsBuilder.fromUriString(URL).build().encode().toUri();

            String result = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
            JSONObject latestJson = null;
            if(isStringNotNullAndNotEmpty(result)){
                latestJson = getLatestUpdatedJson(new JSONObject(result));
            }
//            if(latestJson.optJSONArray("locationDetails")!=null ){
//                if(latestJson.getJSONArray("locationDetails").length()>0){
//                    JSONObject addressDtls = latestJson.getJSONArray("locationDetails").getJSONObject(0).optJSONObject("addressDetails");
//                    if(addressDtls!=null){
//                        mdmDataJson.put("clientCountry", addressDtls.optString("country"));
//                        mdmDataJson.put("clientstate", addressDtls.optString("state"));
//                        mdmDataJson.put("clientCity", addressDtls.optString("city"));
//                    }
//                }
//
//            }
            return latestJson;
        }
        catch(Exception e){
            logger.info(String.format("exception occured while fetching client details for client id %s",clientID));
        }
        return null;
    }

    public static JSONObject getLatestUpdatedJson(JSONObject resJson)
    {
        long latestUpdatedTime = Instant.EPOCH.toEpochMilli();
        JSONObject latestJson = null;
        JSONArray dataJsonArr = resJson.optJSONArray("data");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(int i=0;dataJsonArr!=null && i<dataJsonArr.length();i++)
        {
            JSONObject dataJson = dataJsonArr.getJSONObject(i);
            Date docUpdatedDate = null;
            try {
                docUpdatedDate = sdf.parse(dataJson.getString(Constants.MDM_PROP_LASTUPDATED));
            } catch (ParseException e) {
                // TODO : Log a warning here.
            }
            if (docUpdatedDate != null && docUpdatedDate.toInstant().toEpochMilli() > latestUpdatedTime) {
                latestJson = dataJson;
                latestUpdatedTime = docUpdatedDate.toInstant().toEpochMilli();
            }
        }

        return latestJson;

    }

    private JSONArray getEntityDetails(List<ClientInfo> mClientHierarchyList){
        JSONArray clCommHierarchyJsonArr = new JSONArray();
        for (int i = 0; i < mClientHierarchyList.size(); i++){
            ClientInfo clInfo = mClientHierarchyList.get(i);
            JSONObject clCommHierarchyJson = new JSONObject();
            if(clInfo.getmCommEntityType()!=null)
                clCommHierarchyJson.put(Constants.JSON_PROP_ENTITYTYPE, clInfo.getmCommEntityType().toString());
            if(clInfo.getmCommEntityId()!=null)
                clCommHierarchyJson.put(Constants.JSON_PROP_ENTITYNAME, clInfo.getmCommEntityId());
            if(clInfo.getmClientMarket()!=null)
                clCommHierarchyJson.put(Constants.JSON_PROP_ENTITYMARKET, clInfo.getmClientMarket());
            if (i > 0) {
                clCommHierarchyJson.put(Constants.JSON_PROP_PARENTENTITYNAME, mClientHierarchyList.get(i - 1).getmCommEntityId());
            }
            clCommHierarchyJsonArr.put(clCommHierarchyJson);
        }
        mdmDataJson.put(Constants.JSON_PROP_ENTITYDETAILS,clCommHierarchyJsonArr);
        return clCommHierarchyJsonArr;
    }

    /*Jagdish:Code commented and added below code*/
   /* public  Map<String, Object>  getCityInfo(String cityName)throws OperationException{
        try{
            Map<String, Object>  cityAttrs = new HashMap<>();
            if(isStringNotNullAndNotEmpty(cityName)){
                JSONObject filters = new JSONObject();
                filters.put("deleted", false);
                String URL =  cityInfoURL + objectMapper.writeValueAsString(filters);
                URI uri = UriComponentsBuilder.fromUriString(URL).build().encode().toUri();
                String result = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
                JSONArray cityInfoArr = null;
                if(MDMDataUtils.isStringNotNullAndNotEmpty(result)){
                    cityInfoArr = new JSONArray(result);
                }
                if(cityInfoArr!=null){
                    for(int i=0;i<cityInfoArr.length();i++){
                        JSONObject cityInfoJson = cityInfoArr.getJSONObject(i);

                        if(cityInfoJson.optJSONObject("data")!=null){
                            if(cityName.equalsIgnoreCase(cityInfoJson.getJSONObject("data").optString("value"))){
                                cityAttrs.put("continent",cityInfoJson.getJSONObject("data").optString("continent"));
                                cityAttrs.put("country",cityInfoJson.getJSONObject("data").optString("country"));
                                cityAttrs.put("region",cityInfoJson.getJSONObject("data").optString("region"));
                                cityAttrs.put("state",cityInfoJson.getJSONObject("data").optString("state"));
                                cityAttrs.put("longDescription",cityInfoJson.getJSONObject("data").optString("longDescription"));
                                cityAttrs.put("shortDescription",cityInfoJson.getJSONObject("data").optString("shortDescription"));
                                cityAttrs.put("cityCode",cityInfoJson.getJSONObject("data").optString("cityCode"));
                                cityAttrs.put("value",cityInfoJson.getJSONObject("data").optString("value"));
                            }

                        }

                    }
                }
            }else{
                throw new OperationException("city name is required");
            }
            return cityAttrs;
        }catch (Exception e){
            logger.info("Exception in fetching city data ");
        }
        return null;
    }*/

    @PostConstruct
    public void loadCityData() throws OperationException
    {
        try
        {
            JSONObject filters = new JSONObject();
            filters.put("deleted", false);
            String URL = cityInfoURL + objectMapper.writeValueAsString(filters);
            URI uri = UriComponentsBuilder.fromUriString(URL).build().encode().toUri();
            String result = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
            JSONArray cityInfoArr = null;
            if (MDMDataUtils.isStringNotNullAndNotEmpty(result))
            {
                cityInfoArr = new JSONArray(result);

                jsArrCityObject = cityInfoArr;
            }
        }
        catch (Exception e)
        {
            logger.info("Exception in fetching city data ");
        }
    }

   /* @PostConstruct
    public void loadCityData() throws OperationException
    {
        try
        {
            JSONObject obj2 = new JSONObject();
            obj2.put("continent","Asia");
            obj2.put("longDescription","Mumbai");
            obj2.put("country","India");
            obj2.put("cityCode","IND-MMBB000");
            obj2.put("state","Maharashtra");
            obj2.put("shortDescription","Mumbai");
            obj2.put("region","None");
            obj2.put("value","Mumbai");

            JSONObject obj1 = new JSONObject();
            obj1.put("ancillaryType","city");
            obj1.put("deleted",false);
            obj1.put("data",obj2);
            obj1.put("__v",0);
            obj1.put("_id","ANCLR862164");



            JSONObject obj3 = new JSONObject();
            obj3.put("continent","Asia");
            obj3.put("longDescription","Leh");
            obj3.put("country","India");
            obj3.put("cityCode","IND-LH000");
            obj3.put("state","Jammu And Kashmi");
            obj3.put("shortDescription","Leh");
            obj3.put("region","None");
            obj3.put("value","Leh");

            JSONObject obj4 = new JSONObject();
            obj4.put("ancillaryType","city");
            obj4.put("deleted",false);
            obj4.put("data",obj3);
            obj4.put("__v",0);
            obj4.put("_id","ANCLR861677");

            jsArrCityObject.put(obj1);
            jsArrCityObject.put(obj4);

        }
        catch (Exception e)
        {
            logger.info("Exception in fetching city data ");
        }
    }*/

    public Map<String, Object> getCityInfo(String cityName) throws OperationException {
        try {
            Map<String, Object> cityAttrs = new HashMap<>();
            if (isStringNotNullAndNotEmpty(cityName)) {
                JSONArray cityInfoArr = jsArrCityObject;

                if (cityInfoArr != null) {
                    long start = System.currentTimeMillis();
                    for (int i = 0; i < cityInfoArr.length(); i++) {
                        JSONObject cityInfoJson = cityInfoArr.getJSONObject(i);

                        if (cityInfoJson.optJSONObject("data") != null) {
                            if (cityName.equalsIgnoreCase(cityInfoJson.getJSONObject("data").optString("value"))) {
                                cityAttrs.put("continent", cityInfoJson.getJSONObject("data").optString("continent"));
                                cityAttrs.put("country", cityInfoJson.getJSONObject("data").optString("country"));
                                cityAttrs.put("region", cityInfoJson.getJSONObject("data").optString("region"));
                                cityAttrs.put("state", cityInfoJson.getJSONObject("data").optString("state"));
                                cityAttrs.put("longDescription", cityInfoJson.getJSONObject("data").optString("longDescription"));
                                cityAttrs.put("shortDescription", cityInfoJson.getJSONObject("data").optString("shortDescription"));
                                cityAttrs.put("cityCode", cityInfoJson.getJSONObject("data").optString("cityCode"));
                                cityAttrs.put("value", cityInfoJson.getJSONObject("data").optString("value"));
                            }
                        }
                    }
                    long end = System.currentTimeMillis();
                    System.out.println(end-start);
                }
            } else {
                throw new OperationException("city name is required");
            }
            return cityAttrs;
        } catch (Exception e) {
            logger.info("Exception in fetching city data ");
        }
        return null;
    }

    private BigDecimal getRoeV2(String fromCcy, String toCcy, String market,Date date) throws JsonProcessingException, OperationException, ParseException {

        if( !isStringNotNullAndNotEmpty(fromCcy) || !isStringNotNullAndNotEmpty(toCcy)){
            throw new OperationException(Constants.OPS_ERR_100);
        }
        if(fromCcy.equalsIgnoreCase(toCcy)){
            JSONObject temp = new JSONObject();
            temp.put("fromCurrency",fromCcy);
            temp.put("toCurrency",fromCcy);
            temp.put("ROE",BigDecimal.ONE);
            if(market!=null){
                mdmDataJson.put("roeWithoutCompanyMarket",temp);
            }else{
                temp.put("companyMarket",market);
                mdmDataJson.put("roeWithoutCompanyMarket",temp);
            }
            return BigDecimal.ONE;
        }
        JSONObject props = new JSONObject();
        props.put("roeType", "Daily ROE");
        props.put("deleted", false);

        String URL = roeURL + objectMapper.writeValueAsString(props);

        URI uri = UriComponentsBuilder.fromUriString(URL).build().encode().toUri();

        String result = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);

        JSONObject resJson = new JSONObject(result!=null ? result : "{}");
        JSONArray dataJsoNarr = resJson.optJSONArray("data");
        Object effDate;
        Calendar cal;
        String currentFromCcy,currentToCcy;
        BigDecimal roeVal = new BigDecimal(0);
        if(date ==null){
            date = new Date();
        }
        String dateStr =  DATE_FORMAT.format(date);
        dataJsoNarr = filterROEDataByCutOff(dataJsoNarr,dateStr);
        for(int i=0;i<dataJsoNarr.length();i++){
            JSONObject dataJson = dataJsoNarr.getJSONObject(i);
            JSONArray roeDataArr = dataJson.optJSONArray("ROE")!=null ?  dataJson.getJSONArray("ROE") : new JSONArray();
//            effDate = dataJson.opt("effectiveFrom");
//            if(effDate instanceof String) {
//                try {
//                    effDate = DATE_FORMAT.format(DATE_FORMAT.parse((String) effDate));
//                } catch (ParseException e) {
//                    logger.warn(String.format("Effective date parse error for ROE document with id: %s", dataJson.getString("_id")));
//                    continue;
//                }
//            }
//            else if(effDate instanceof JSONObject) {
//                cal = Calendar.getInstance();
//                cal.set(((JSONObject) effDate).getInt("year"),((JSONObject) effDate).getInt("month")-1,((JSONObject) effDate).getInt("day"));
//                effDate =  DATE_FORMAT.format(cal.getTime());
//            }else {
//                //As per BRD eff Date is mandatory but not in UI.This should not reach ideally
//                logger.warn(String.format("Effective date not found for ROE document with id: %s", dataJson.getString("_id")));
//                continue;
//            }
            Boolean flag= false;
            for(int j=0;j<roeDataArr.length();j++){
                JSONObject roeData = roeDataArr.getJSONObject(j);
                currentFromCcy = roeData.optString("fromCurrency");
                currentToCcy = roeData.optString("toCurrency");
                if(!isStringNotNullAndNotEmpty(currentFromCcy) || !isStringNotNullAndNotEmpty(currentToCcy))
                {
                    logger.warn(String.format("One of the currency is evaluated to be null or empty for document  with id: %s, fromCurrency: %s, toCurrency: %s", dataJson.getString("_id"), fromCcy, toCcy));
                    continue;
                }
                if(fromCcy.equalsIgnoreCase(currentFromCcy) && toCcy.equalsIgnoreCase(currentToCcy)){
                    if(!roeData.has("sellingROE")) {
                        logger.warn(String.format("No selling ROE value found for document with id: %s, fromCurrency: %s, toCurrency: %s", dataJson.getString("_id"), fromCcy, toCcy));
                        //This should never happen as selling roe is mandatory when daily roe is defined.This check needs to be added in UI!!!
                        continue;
                    }
                    roeVal = roeData.optBigDecimal("sellingROE",BigDecimal.ZERO);
                    JSONObject temp = new JSONObject();
                    temp.put("fromCurrency",fromCcy);
                    temp.put("toCurrency",fromCcy);
                    temp.put("ROE",roeVal);
                    mdmDataJson.put("roeWithoutCompanyMarket",temp);
                    JSONArray mrktDataArr = dataJson.optJSONArray("companyMarkets")!=null ? dataJson.getJSONArray("companyMarkets") : new JSONArray();
                    String marketName = "";
                    for(int k=0;k<mrktDataArr.length();k++) {
                        JSONObject mrktJson = mrktDataArr.getJSONObject(k);
                        if(market!=null){
                            if(market.equalsIgnoreCase(mrktJson.optString("name")))
                                marketName = mrktJson.optString("name");
                        }

                    }
                    if (marketName.isEmpty() == false) {
                        temp.put("companyMarket",marketName);
                        mdmDataJson.put("roeWithoutCompanyMarket",temp);
                    }
                    flag = true;
                    break;
                }
            }
            if(flag==true)
                break;
        }
        return roeVal;
    }

    public JSONArray filterROEDataByCutOff(JSONArray dataJsonArr, String date) throws ParseException {
        Object effDate;
        Calendar cal;
        Map<String,JSONObject> map = new HashMap<>();
        PriorityQueue<Date> dateQ = new PriorityQueue<Date>();
        for(int i=0;i<dataJsonArr.length();i++){
            JSONObject dataJson = dataJsonArr.getJSONObject(i);
            JSONArray roeDataArr = dataJson.optJSONArray("ROE")!=null ?  dataJson.getJSONArray("ROE") : new JSONArray();
            effDate = dataJson.opt("effectiveFrom");
            if(effDate instanceof String) {
                try {
                    effDate = DATE_FORMAT.format(DATE_FORMAT.parse((String) effDate));
                } catch (ParseException e) {
                    logger.warn(String.format("Effective date parse error for ROE document with id: %s", dataJson.getString("_id")));
                    continue;
                }
            }else if(effDate instanceof JSONObject) {
                cal = Calendar.getInstance();
                cal.set(((JSONObject) effDate).getInt("year"),((JSONObject) effDate).getInt("month")-1,((JSONObject) effDate).getInt("day"));
                effDate =  DATE_FORMAT.format(cal.getTime());

                if(DATE_FORMAT.parse(DATE_FORMAT.format(cal.getTime())).compareTo(DATE_FORMAT.parse(date))<=0){
                    dateQ.add(DATE_FORMAT.parse(DATE_FORMAT.format(cal.getTime())));
                    map.put(DATE_FORMAT.format(cal.getTime()),dataJson);
                }
            }else {
                //As per BRD eff Date is mandatory but not in UI.This should not reach ideally
                logger.warn(String.format("Effective date not found for ROE document with id: %s", dataJson.getString("_id")));
                continue;
            }
        }
        //Jagdish
        JSONObject resObj = dateQ.size() > 0 ? map.get(DATE_FORMAT.format(dateQ.peek())) : new JSONObject();

        return new JSONArray().put(resObj);

    }

    public JSONObject getCountryCode(String countryName){
        try{
            JSONObject filters = new JSONObject();
            filters.put("data.value", countryName);

            String URL = countryInfoURL + objectMapper.writeValueAsString(filters);
            URI uri = UriComponentsBuilder.fromUriString(URL).build().encode().toUri();
            String result = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
            JSONArray resultArr = null;
            JSONObject countryInfo = null;
            if(MDMDataUtils.isStringNotNullAndNotEmpty(result))
                resultArr = new JSONArray(result);
            if(resultArr!=null){
                JSONObject resJson = resultArr.getJSONObject(0);
                if(resJson.optJSONObject("data")!=null){
                    countryInfo = resJson.getJSONObject("data");
                }
            }
            return countryInfo;
        }catch (Exception e){
            logger.info("Exception occured while fetching country details ");
        }
        return null;
    }

    public static int calculateAge(String bDate) {

        if(bDate.isEmpty())
            return 1;
        try{
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
            Date birthDate = df.parse(bDate);
            //birthdate
            Calendar birthDay = Calendar.getInstance();
            birthDay.setTimeInMillis(birthDate.getTime());

            //create calendar object for current day
            long currentTime = System.currentTimeMillis();
            Calendar now = Calendar.getInstance();
            now.setTimeInMillis(currentTime);

            //Get difference between years
            int age = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
            return age;
        }catch (Exception e){
            logger.info("Exception occured while calculating age ");
            return 1;
        }

    }

}
