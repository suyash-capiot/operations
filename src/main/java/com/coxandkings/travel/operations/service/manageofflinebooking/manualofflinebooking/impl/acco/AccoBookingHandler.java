package com.coxandkings.travel.operations.service.manageofflinebooking.manualofflinebooking.impl.acco;

import com.coxandkings.travel.operations.enums.manageofflinebooking.CommercialsOperation;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.service.manageofflinebooking.manualofflinebooking.ManualOfflineBookingService;
import com.coxandkings.travel.operations.service.manageofflinebooking.manualofflinebooking.ProductBookingHandler;
import com.coxandkings.travel.operations.service.manageofflinebooking.manualofflinebooking.enums.ClientType;
import com.coxandkings.travel.operations.service.manageofflinebooking.manualofflinebooking.enums.OffersType;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.coxandkings.travel.operations.utils.manageOfflineBooking.ClientInfo;
import com.coxandkings.travel.operations.utils.manageOfflineBooking.Constants;
import com.coxandkings.travel.operations.utils.manageOfflineBooking.MDMDataUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONPointer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@Service
@Qualifier("ACCO")
@Transactional(readOnly = false)
public class AccoBookingHandler implements ProductBookingHandler, AccoConstants {

    private static Logger logger = Logger.getLogger(AccoBookingHandler.class);

    @Value(value = "${offlineBooking.suppRateAcco}")
    private String suppRateAccoURL;

    @Value(value = "${offlineBooking.suppRateAccoTaxes}")
    private String suppRateAccoTaxesURL;

    @Value(value = "${offlineBooking.advanceDefAcco}")
    private String advanceDefAccoURL;

    @Value(value = "${offlineBooking.surcahrgeAndSuppDef}")
    private String surChargeAndSuppDefAccoURL;

    @Value(value = "${offlineBooking.amendmentPolicy}")
    private String amendAndCancelPolicyURL;

    @Value("${offline-booking.BE-apply-commercials}")
    private String applyCommercialsUrl;

    @Value(value = "${offlineBooking.clientType}")
    private String clientTypeURL;

    @Value(value = "${offlineBooking.offers}")
    private String offersURL;

    @Value(value = "${offlineBooking.paymentDetails}")
    private String paymentDetailsURL;

    @Autowired
    private UserService userService;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Value("${mdm.common.supplier.supplier-by-id}")
    private String suppByIdUrl;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Autowired
    private MDMDataUtils dataUtils;

    @Autowired
    private SupplierCommercials supplierCommercials;

    @Autowired
    private ClientCommercials clientCommercials;

    @Autowired
    private TaxEngine taxEngine;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    ManualOfflineBookingService manualOfflineBookingService;

    @Autowired
    CompanyOffers companyOffers;

    @Override
    public boolean isResponsibleFor(String product) {
        return "Accommodation".equalsIgnoreCase(product);
    }

    public JSONObject calculateCommercials(JSONObject pUIJSObject, JSONObject mdmDataJSon) throws OperationException {
        logger.debug("Inside calculateCommercials method:Start");
        JSONObject jsObjRequest = null, jsObjResponse = null, jsObjSupplierCommercialsResponse = null, jsObjClientCommercialsResponse = null;
        Map<Integer, String> mapSI2BRMSRoomMap = new HashMap<Integer, String>();
        try {
            jsObjRequest = getRequestJSON(pUIJSObject, mdmDataJSon);
            jsObjResponse = getResponseJSON(pUIJSObject, mdmDataJSon);
            jsObjSupplierCommercialsResponse = supplierCommercials.getSupplierCommercials(CommercialsOperation.Book, jsObjRequest, jsObjResponse, mapSI2BRMSRoomMap, mdmDataJSon);
            jsObjClientCommercialsResponse = clientCommercials.getClientCommercials(jsObjRequest, jsObjSupplierCommercialsResponse, mdmDataJSon);
            calculatePrices(jsObjRequest, jsObjResponse, jsObjClientCommercialsResponse, jsObjSupplierCommercialsResponse, mapSI2BRMSRoomMap, true, null, mdmDataJSon);
            taxEngine.getCompanyTaxes(jsObjRequest, jsObjResponse, mdmDataJSon, pUIJSObject);
            companyOffers.getCompanyOffers(CommercialsOperation.Book, jsObjRequest, jsObjResponse, OffersType.COMPANY_SEARCH_TIME, mdmDataJSon);
        } catch (OperationException oe) {
            logger.error("An exceptions occurred calculateCommercials : " + oe.getErrors());
            throw oe;
        } catch (Exception e) {
            logger.error("Exception occurred in calculateCommercials method : " + e.getMessage());
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred in calculateCommercials method : " + e.getMessage()));
            throw new OperationException(entity);
        }
        logger.debug("Inside calculateCommercials method:End");
        return jsObjResponse;
    }

    public JSONObject getRequestJSON(JSONObject pUIJSObject, JSONObject mdmDataJSon) throws OperationException {
        logger.debug("Inside getRequestJSON method:Start");
        JSONObject jsObjRequest = new JSONObject();
        try {
            JSONObject productDetailsJSON = pUIJSObject.getJSONObject(Constants.JSON_PROP_PRODUCT_DETAILS);
            JSONObject travelPaxDetailsJSON = pUIJSObject.getJSONObject(Constants.JSON_PROP_TRAVEL_PAX_DETAILS);

            JSONObject jsObjReqHeader = getRequestHeaderFromUIReq(pUIJSObject, mdmDataJSon);
            JSONObject jsObjReqBody = new JSONObject("{}");

            jsObjReqBody.put("checkIn", travelPaxDetailsJSON.getString("checkInDate").substring(0, 10));
            jsObjReqBody.put("checkOut", travelPaxDetailsJSON.getString("checkOutDate").substring(0, 10));
            jsObjReqBody.put("city", productDetailsJSON.getString("city"));
            jsObjReqBody.put("hotelInfo", getHotelInformation(travelPaxDetailsJSON));
            jsObjReqBody.put("roomConfig", getRoomConfigForRQ(travelPaxDetailsJSON));
            jsObjRequest.put("requestHeader", jsObjReqHeader);
            jsObjRequest.put("requestBody", jsObjReqBody);
        } catch (OperationException oe) {
            logger.error("Exception occurred in getRequestJSON method : " + oe.getErrors());
            throw oe;
        } catch (Exception e) {
            logger.error("Exception occurred in getRequestJSON method : " + e.getMessage());
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred in getRequestJSON method : " + e.getMessage()));
            throw new OperationException(entity);
        }
        logger.debug("Inside getRequestJSON method:End");
        return jsObjRequest;
    }

    private JSONArray getRoomConfigForRQ(JSONObject travelPaxDetailsJSON) {
        JSONArray roomConfigArr = new JSONArray();
        JSONObject roomConfig = null;
        JSONArray roomBreakupArr = travelPaxDetailsJSON.optJSONArray("roomBreakup");
        JSONArray chldAgeArr = null;
        if (roomBreakupArr != null) {
            for (int i = 0; i < roomBreakupArr.length(); i++) {
                JSONObject roomJson = roomBreakupArr.getJSONObject(i);
//                if (roomJson.optBoolean("isRoomSelected"))
                {
                    int adtCount = 0;
                    chldAgeArr = new JSONArray();
                    JSONArray paxDetails = roomJson.optJSONArray("passengerDetails");
                    for (int j = 0; j < paxDetails.length(); j++) {
                        JSONObject paxJson = paxDetails.getJSONObject(j);
                        if (paxJson.getString("passengerType").equalsIgnoreCase("Child")) {
                            chldAgeArr.put(MDMDataUtils.calculateAge(paxJson.getString("dateOfBirth")));
                        } else if (paxJson.getString("passengerType").equalsIgnoreCase("Adult")) {
                            adtCount++;
                        }
                    }
                    roomConfig = new JSONObject();
                    roomConfig.put("adultCount", adtCount);
                    roomConfig.put("childAges", chldAgeArr);
                    roomConfigArr.put(roomConfig);
                }
            }
        }
        return roomConfigArr;
    }
    //TODO Should be kept in Abstract class
    private JSONObject getRequestHeaderFromUIReq(JSONObject req, JSONObject mdmDataJSon) throws OperationException {
        logger.debug("Inside getRequestHeaderFromUIReq method:Start");
        JSONObject reqHeader = new JSONObject();
        try {
            reqHeader.put("sessionID", "");
            reqHeader.put("userID", "");
            reqHeader.put("transactionID", "");

            JSONObject clientContext = new JSONObject();

            if (req.optJSONObject("clientDetails") != null) {
                JSONObject clientDetails = req.getJSONObject("clientDetails");
                clientContext.put("clientCurrency", mdmDataJSon.optString("clientCurrency"));
                clientContext.put("clientType", clientDetails.optString("clientType"));
                clientContext.put("clientID", "");
                clientContext.put("clientMarket", clientDetails.optString("companyMarket"));
                clientContext.put("clientLanguage", mdmDataJSon.optString("clientLanguage"));
                clientContext.put("pointOfSale", mdmDataJSon.optString("pointOfSale"));
                reqHeader.put("clientContext", clientContext);
            }
        } catch (Exception e) {
            logger.error("Exception occurred in getRequestHeaderFromUIReq method : " + e.getMessage());
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred in getRequestHeaderFromUIReq method : " + e.getMessage()));
            throw new OperationException(entity);
        }
        logger.debug("Inside getRequestHeaderFromUIReq method:End");
        return reqHeader;
    }

    private Map<String, Object> getHotelInformation(JSONObject travelPaxDetailsJSON) throws OperationException {
        logger.debug("Inside getHotelInformation method:Start");
        Map<String, Object> hotelAttrs = new HashMap<>();
        try {
            boolean isDefineRatesManually = travelPaxDetailsJSON.getJSONArray("roomBreakup").getJSONObject(0).optBoolean("isDefineRatesManually");
            if (!isDefineRatesManually) {
                JSONObject jSObjCommonProductInfo = travelPaxDetailsJSON.getJSONArray("roomBreakup").getJSONObject(0).getJSONObject("supplierRates").getJSONObject("supplierRateDetails").getJSONObject("accoData").getJSONObject("accomodationInfo");
                hotelAttrs.put("productName", jSObjCommonProductInfo.optString("name"));
                hotelAttrs.put("productBrand", jSObjCommonProductInfo.optString("brand"));
                hotelAttrs.put("productChain", jSObjCommonProductInfo.optString("chain"));
            } else {
                //TODO how to get following if rates are defined manually
                hotelAttrs.put("productName", "Hotel Leela");
                hotelAttrs.put("productBrand", "");
                hotelAttrs.put("productChain", "Westin");
            }
        } catch (Exception e) {
            logger.error("Exception occurred in getHotelInformation method : " + e);
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred in getHotelInformation method : " + e.getMessage()));
            throw new OperationException(entity);
        }
        logger.debug("Inside getHotelInformation method:End");
        return hotelAttrs;
    }

    public JSONObject getResponseJSON(JSONObject pUIJSObject, JSONObject mdmDataJSon) throws OperationException {
        logger.debug("Inside getResponseJSON method:Start");
        JSONObject jsObjResponse = new JSONObject();
        try {
            JSONObject jsObjResponseBody = new JSONObject("{\"accommodationInfo\":[{}]}");
            jsObjResponse.put("responseBody", jsObjResponseBody);
            jsObjResponse.put("responseHeader", getRequestHeaderFromUIReq(pUIJSObject, mdmDataJSon));
            JSONArray arrAccommodationInfo = jsObjResponseBody.getJSONArray("accommodationInfo");

            for (Object objAccommodationInfo : arrAccommodationInfo) {
                JSONObject arrRoomStay = (JSONObject) objAccommodationInfo;
                arrRoomStay.put("roomStay", getRoomStayArr(pUIJSObject));
            }
        } catch (OperationException oe) {
            logger.error("Exception occurred in getResponseJSON method : " + oe.getErrors());
            throw oe;
        } catch (Exception e) {
            logger.error("Exception occurred in getResponseJSON method : " + e.getMessage());
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred in getRequestJSON method : " + e.getMessage()));
            throw new OperationException(entity);
        }
        logger.debug("Inside getResponseJSON method:End");
        return jsObjResponse;
    }

    private JSONArray getRoomStayArr(JSONObject pUIJSObject) throws OperationException {
        logger.debug("Inside getRoomStayArr method:Start");
        JSONArray jsArrRoomStay = new JSONArray();
        try {
            JSONObject productDetailsJSON = pUIJSObject.getJSONObject(Constants.JSON_PROP_PRODUCT_DETAILS);
            JSONObject travelPaxDetailsJSON = pUIJSObject.getJSONObject(Constants.JSON_PROP_TRAVEL_PAX_DETAILS);

            long lngNumberofNight = travelPaxDetailsJSON.getLong("numberOfNights");
            JSONArray jsArrRoomBreakup = travelPaxDetailsJSON.getJSONArray("roomBreakup");

            for (int i = 0; i < jsArrRoomBreakup.length(); i++) {
                JSONObject jsObjRoomBreakup = jsArrRoomBreakup.getJSONObject(i);
                JSONObject jsObjRoomStay = new JSONObject();

                boolean isRoomSelected = jsObjRoomBreakup.getBoolean("isRoomSelected");

//                if (isRoomSelected)
                {
                    boolean isDefineRateManually = jsObjRoomBreakup.getBoolean("isDefineRatesManually");

                    String strRoomCategory = jsObjRoomBreakup.getString("roomCategory");
                    String strRoomType = jsObjRoomBreakup.getString("roomType");
                    String strCheckInDate = travelPaxDetailsJSON.getString("checkInDate").substring(0, 10);
                    JSONArray jsArrPolicies = getPolicies(jsObjRoomBreakup, isDefineRateManually);

                    JSONArray jsArrNightlyPriceInfo = isDefineRateManually ? getNightlyPriceInfoObjForManualSuppliers(jsObjRoomBreakup, lngNumberofNight, strRoomCategory, strRoomType, strCheckInDate) :
                            getNightlyPriceInfoObj(jsObjRoomBreakup, lngNumberofNight);

                    jsObjRoomStay.put("supplierRef", jsObjRoomBreakup.getJSONObject("supplierRates").getString("supplierId")); //TODO supplierRef not coming in case of manually define rates
                    jsObjRoomStay.put("accommodationSubType", productDetailsJSON.getString("productSubCategory"));
                    jsObjRoomStay.put("paxInfo", getPassengerDetails(jsObjRoomBreakup.getJSONArray("passengerDetails")));
                    jsObjRoomStay.put("policies", jsArrPolicies);
                    jsObjRoomStay.put("totalPriceInfo", getTotalPriceInfoObj(jsArrNightlyPriceInfo));
                    jsObjRoomStay.put("nightlyPriceInfo", jsArrNightlyPriceInfo);
                    jsObjRoomStay.put("occupancyInfo", getOccupancyInfo(jsObjRoomBreakup.getJSONArray("passengerDetails")));
                    jsObjRoomStay.put("roomInfo", getRoomInfo(jsObjRoomBreakup, productDetailsJSON));
                    jsArrRoomStay.put(jsObjRoomStay);
                }
            }
        } catch (OperationException oe) {
            logger.error("Exception occurred in getRoomStayArr method : " + oe.getErrors());
            throw oe;
        } catch (Exception e) {
            logger.error("Exception occurred in getRoomStayArr method : " + e);
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred in getRoomStayArr method : " + e.getMessage()));
            throw new OperationException(entity);
        }
        logger.debug("Inside getRoomStayArr method:End");
        return jsArrRoomStay;
    }

    private JSONArray getNightlyPriceInfoObj(JSONObject jsObjRoomBreakup, long pLngNumberOfNights) throws OperationException {
        logger.debug("Inside getNightlyPriceInfoObj method:Start");
        JSONArray jsArrNightlyPriceInfo = new JSONArray();

        try {
            String strRoomType = jsObjRoomBreakup.getString("roomType");
            JSONObject jsSupplierRateDetails = jsObjRoomBreakup.getJSONObject("supplierRates").getJSONObject("supplierRateDetails").getJSONObject("defineRates");
            String strCurrency = jsSupplierRateDetails.getString("currency");
            String strEffectiveDate = jsSupplierRateDetails.getString("effectiveFrom").substring(0, 10);

            JSONArray jsArrAddDefineRate = jsSupplierRateDetails.getJSONArray("addDefineRates");

            for (int i = 0; i < jsArrAddDefineRate.length(); i++) {
                JSONObject jsObjAddDefineRate = jsArrAddDefineRate.getJSONObject(i);
                if (jsObjAddDefineRate.getBoolean("isSelected")) {
                    JSONArray jsArrChildRecordScreen1 = jsObjAddDefineRate.getJSONArray("childRecordScreen1");
//            JSONArray jsArrChildRecordScreen2 = new JSONArray();

                    for (int j = 0; j < jsArrChildRecordScreen1.length(); j++) {
                        JSONObject jsObjChildRecordScreen1 = jsArrChildRecordScreen1.getJSONObject(j);
                        if (jsObjChildRecordScreen1.getBoolean("isSelected")) {
                            JSONArray jsArrRoomTypeDetails = jsObjChildRecordScreen1.getJSONArray("roomTypeDetails");
                            BigDecimal bigDecAmount = new BigDecimal(0);

                            for (int k = 0; k < jsArrRoomTypeDetails.length(); k++) {
                                if (strRoomType.equals(jsArrRoomTypeDetails.getJSONObject(k).getString("roomType"))) {
                                    JSONObject jsObjRoomTypeDetails = jsArrRoomTypeDetails.getJSONObject(k);
                                    bigDecAmount = jsObjRoomTypeDetails.getBigDecimal("value");
                                    break;
                                }
                            }
                            //TODO adding pax with extra bed and without extra bed amount in "bigDecAmount"
                            for (int l = 0; l < pLngNumberOfNights; l++) {
                                JSONObject jsObjNightlyPriceInfo = new JSONObject();
                                jsObjNightlyPriceInfo.put("amount", bigDecAmount);
                                jsObjNightlyPriceInfo.put("taxes", getNightlyPriceInfoTax(jsSupplierRateDetails, strCurrency, false));
                                jsObjNightlyPriceInfo.put("currencyCode", strCurrency);
                                jsObjNightlyPriceInfo.put("effectiveDate", strEffectiveDate);
                                jsArrNightlyPriceInfo.put(jsObjNightlyPriceInfo);
                            }
                        }
                    }
                }

           /* for (int k = 0; k <= jsArrChildRecordScreen2.length(); k++) {
                JSONObject jsObjNightlyPriceInfo = new JSONObject();
                jsObjNightlyPriceInfo.put("amount", "");
                jsObjNightlyPriceInfo.put("taxes", new JSONObject());
                jsObjNightlyPriceInfo.put("currencyCode", strCurrency);
                jsObjNightlyPriceInfo.put("effectiveDate", strEffectiveDate);

                jsArrNightlyPriceInfo.put(jsObjNightlyPriceInfo);
            }*/
            }
        } catch (OperationException oe) {
            logger.error("Exception occurred in getNightlyPriceInfoObj method : " + oe.getErrors());
            throw oe;
        } catch (Exception e) {
            logger.error("Exception occurred in getNightlyPriceInfoObj method : " + e.getMessage());
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred in getNightlyPriceInfoObj method : " + e.getMessage()));
            throw new OperationException(entity);
        }
        logger.debug("Inside getNightlyPriceInfoObj method:End");
        return jsArrNightlyPriceInfo;
    }

    private JSONObject getNightlyPriceInfoTax(JSONObject pJSObjDefineRates, String pStrCurrency, boolean isDefinedRateManually) throws OperationException {
        logger.debug("Inside getNightlyPriceInfoTax method:Start");
        JSONObject taxes = new JSONObject();
        try {
            if (pJSObjDefineRates.getString("taxesApplicable").equalsIgnoreCase("Yes")) {

                JSONArray jsArrTaxDetails = isDefinedRateManually ? pJSObjDefineRates.getJSONArray("taxes") : pJSObjDefineRates.getJSONObject("taxDetails").getJSONArray("taxes");

                JSONArray jsArrTax = new JSONArray();
                BigDecimal totalAmount = new BigDecimal(0);
                for (int i = 0; i < jsArrTaxDetails.length(); i++) {
                    JSONObject jsObjTaxDetails = jsArrTaxDetails.getJSONObject(i);
                    JSONObject jsObjTax = new JSONObject();
                    totalAmount = totalAmount.add(jsObjTaxDetails.getBigDecimal("taxValue"));
                    jsObjTax.put("amount", jsObjTaxDetails.getBigDecimal("taxValue"));
                    jsObjTax.put("taxCode", jsObjTaxDetails.getString("taxType"));
                    jsObjTax.put("currencyCode", pStrCurrency);
                    jsArrTax.put(jsObjTax);
                }
                taxes.put("amount", totalAmount);
                taxes.put("tax", jsArrTax);
                taxes.put("currencyCode", pStrCurrency);
            } else {
                new JSONObject();
            }
        } catch (Exception e) {
            logger.error("Exception occurred in getNightlyPriceInfoTax method : " + e);
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred in getNightlyPriceInfoTax method : " + e.getMessage()));
            throw new OperationException(entity);
        }
        logger.debug("Inside getNightlyPriceInfoTax method:End");
        return taxes;
    }

    private JSONArray getNightlyPriceInfoObjForManualSuppliers(JSONObject jsObjRoomBreakup, long pLngNumberOfNights, String jsObjRoomCategory, String jsObjRoomType, String strCheckInDate) throws OperationException {
        JSONArray jsArrNightlyPriceInfo = new JSONArray();
        logger.debug("Inside getNightlyPriceInfoObjForManualSuppliers method:Start");
        try {
            JSONObject jsObjDefineRateManually = jsObjRoomBreakup.getJSONObject("defineRatesManually");
            String strCurrency = jsObjDefineRateManually.getString("currency");
            JSONArray jsArrRoomRates = jsObjDefineRateManually.getJSONArray("roomRates");

            for (int i = 0; i < jsArrRoomRates.length(); i++) {
                JSONObject jsObjRoomRates = jsArrRoomRates.getJSONObject(i);

                if (jsObjRoomCategory.equalsIgnoreCase(jsObjRoomRates.getString("roomCategory")) && jsObjRoomType.equalsIgnoreCase(jsObjRoomRates.getString("roomType"))) {
                    for (int l = 0; l < pLngNumberOfNights; l++) {
                        JSONObject jsObjNightlyPriceInfo = new JSONObject();
                        jsObjNightlyPriceInfo.put("amount", jsObjRoomRates.getBigDecimal("roomRate"));
                        jsObjNightlyPriceInfo.put("taxes", getNightlyPriceInfoTax(jsObjDefineRateManually, strCurrency, true));
                        jsObjNightlyPriceInfo.put("currencyCode", strCurrency);
                        jsObjNightlyPriceInfo.put("effectiveDate", strCheckInDate);
                        jsArrNightlyPriceInfo.put(jsObjNightlyPriceInfo);
                    }
                }
            }
        } catch (OperationException oe) {
            logger.error("Exception occurred in getNightlyPriceInfoObjForManualSuppliers method : " + oe.getErrors());
            throw oe;
        } catch (Exception e) {
            logger.error("Exception occurred in getNightlyPriceInfoObjForManualSuppliers method : " + e);
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred in getNightlyPriceInfoObjForManualSuppliers method : " + e.getMessage()));
            throw new OperationException(entity);
        }
        logger.debug("Inside getNightlyPriceInfoObjForManualSuppliers method:End");
        return jsArrNightlyPriceInfo;
    }

    private JSONArray getPassengerDetails(JSONArray pJSArrPassengerDetails) throws OperationException {
        logger.debug("Inside getPassengerDetails method:Start");
        JSONArray jsArrPaxInfo = new JSONArray();
        try {
            boolean isLeadPax = false;

            for (int i = 0; i < pJSArrPassengerDetails.length(); i++) {
                JSONObject jsObjPaxInfo = new JSONObject();
                JSONObject jsObjPassengerDetails = pJSArrPassengerDetails.getJSONObject(i);
                String strPaxtype = jsObjPassengerDetails.getString("passengerType");
                if (!isLeadPax) {
                    isLeadPax = ("Adult".equalsIgnoreCase(strPaxtype) && isLeadPax == false) ? true : false;
                }
                jsObjPaxInfo.put("documentDetails", getDocumentInfo());// TODO Document details are not provided from UI
                jsObjPaxInfo.put("isLeadPax", isLeadPax);//TODO Lead pax details not provide from UI ,due to which first adult passenger is considered as Lead passenger
                jsObjPaxInfo.put("firstName", jsObjPassengerDetails.getString("passengerName"));
                jsObjPaxInfo.put("paxType", strPaxtype);
                jsObjPaxInfo.put("surname", "");// TODO Surname not provided from UI
                jsObjPaxInfo.put("dob", jsObjPassengerDetails.getString("dateOfBirth"));
                jsObjPaxInfo.put("middleName", "");// TODO Middle Name not provided from UI
                jsObjPaxInfo.put("addressDetails", getAddressDetails());// TODO Address details of client not provided from UI
                jsObjPaxInfo.put("title", "");// TODO Title not provided from UI
                jsObjPaxInfo.put("contactDetails", getContactDetails());// TODO Contact details of client not provided from UI

                jsArrPaxInfo.put(jsObjPaxInfo);

            }
        } catch (OperationException oe) {
            logger.error("Exception occurred in getPassengerDetails method : " + oe.getErrors());
            throw oe;
        } catch (Exception e) {
            logger.error("Exception occurred in getPassengerDetails method : " + e);
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred in getPassengerDetails method : " + e.getMessage()));
            throw new OperationException(entity);
        }
        logger.debug("Inside getPassengerDetails method:End");
        return jsArrPaxInfo;
    }

    private JSONArray getPolicies(JSONObject jsObjRoomBreakup, boolean isSupplierRateManually) throws OperationException {

        JSONArray jsArrPolicies = new JSONArray();
        try {
            String strSupplierCurrency = jsObjRoomBreakup.optJSONObject("supplierRates").optJSONObject("supplierRateDetails").optJSONObject("defineRates").optString("currency");
            if (isSupplierRateManually) {

                //Applying Ammendment policies
                if(jsObjRoomBreakup.optJSONObject("defineRatesManually") != null) {
                    JSONArray jsArrAmmendment = jsObjRoomBreakup.optJSONObject("defineRatesManually").optJSONArray("amendmentPoliciesData");
                    getPolicies(jsArrPolicies, jsArrAmmendment, strSupplierCurrency);
                }

                //Applying Cancellation policies
                if(jsObjRoomBreakup.optJSONObject("defineRatesManually") != null) {
                    JSONArray jsArrCancellation = jsObjRoomBreakup.optJSONObject("defineRatesManually").optJSONArray("cancellationPoliciesData");
                    getPolicies(jsArrPolicies, jsArrCancellation, strSupplierCurrency);
                }

            } else {
                JSONObject jsSupplierRateDetails = jsObjRoomBreakup.optJSONObject("supplierRates").optJSONObject("supplierRateDetails").optJSONObject("defineRates");
                JSONArray jsArrAddDefineRate = jsSupplierRateDetails.getJSONArray("addDefineRates");
                for (int i = 0; i < jsArrAddDefineRate.length(); i++) {
                    JSONObject jsObjAddDefineRate = jsArrAddDefineRate.getJSONObject(i);

                    if (jsObjAddDefineRate.getBoolean("isSelected")) {
                        //Applying Ammendment policies
                        if(jsObjAddDefineRate.optJSONObject("amendmentPolicyDetails") != null) {
                            JSONArray jsArrAmmendment = jsObjAddDefineRate.optJSONObject("amendmentPolicyDetails").optJSONArray("data");
                            getPolicies(jsArrPolicies, jsArrAmmendment, strSupplierCurrency);
                        }

                        //Applying Cancellation policies
                        if(jsObjAddDefineRate.optJSONObject("cancellationPolicyDetails") != null) {
                            JSONArray jsArrCancellation = jsObjAddDefineRate.optJSONObject("cancellationPolicyDetails").optJSONArray("data");
                            getPolicies(jsArrPolicies, jsArrCancellation, strSupplierCurrency);
                        }
                    }
                }
            }
        } catch (OperationException oe) {
            logger.error("Exception occurred in getPolicies method : " + oe.getErrors());
            throw oe;
        } catch (Exception e) {
            logger.error("Exception occurred in getPolicies method : " + e.getMessage());
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred in getPolicies method : " + e.getMessage()));
            throw new OperationException(entity);
        }
        return jsArrPolicies;
    }

    private JSONArray getPolicies(JSONArray jsArrPolicies, JSONArray pJSArrPolicies, String pSupplierCurrency) throws OperationException {
        logger.debug("Inside getPolicies method:Start");
        try {
            for (int i = 0; i < pJSArrPolicies.length(); i++) {
                JSONObject jsObjPolicyCharges = new JSONObject();
                JSONObject jsObjPolicyApplicability = new JSONObject();
                JSONObject jsObjPolicy = new JSONObject();

                JSONObject jsObjPolicies = pJSArrPolicies.getJSONObject(i);
                JSONObject jsObjPolicyInfo = jsObjPolicies.optJSONObject("policyInformation");
                JSONArray jsArrPolicy = jsObjPolicyInfo.optJSONObject("cancelAmend").optJSONObject("accommodation").optJSONArray("policies");

                for (int j = 0; j < jsArrPolicy.length(); j++) {
                    JSONObject jsObjPolicyData = jsArrPolicy.getJSONObject(j);

                    jsObjPolicyCharges.put("chargeType", jsObjPolicyData.optString("chargeDefinedBy"));
                    jsObjPolicyCharges.put("chargeValue", jsObjPolicyData.optBigDecimal("chargePercent", new BigDecimal(0)));
                    jsObjPolicyCharges.put("currencyCode", pSupplierCurrency);

                    jsObjPolicyApplicability.put("from", jsObjPolicyData.optBigDecimal("from", new BigDecimal(0)));
                    jsObjPolicyApplicability.put("to", jsObjPolicyData.optBigDecimal("to", new BigDecimal(0)));
                    jsObjPolicyApplicability.put("timeUnit", jsObjPolicyData.optString("definedBy"));

                    jsObjPolicy.put("policyCharges", jsObjPolicyCharges);
                    jsObjPolicy.put("policyApplicability", jsObjPolicyApplicability);
                    jsObjPolicy.put("policyType", jsObjPolicyInfo.optString("policyType"));
                    jsArrPolicies.put(jsObjPolicy);
                }
            }
            logger.debug("Inside getPolicies method:End");
        } catch (Exception e) {
            logger.error("Exception occurred in getPolicies method : " + e.getMessage());
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred in getPolicies method : " + e.getMessage()));
            throw new OperationException(entity);
        }
        logger.debug("Inside getPolicies method:End");
        return jsArrPolicies;
    }

    private JSONObject getTotalPriceInfoObj(JSONArray jsArryNightlyPriceInfo) throws OperationException {
        logger.debug("Inside getTotalPriceInfoObj method:Start");
        JSONObject jsObjTotalPriceInfo = new JSONObject();
        try {
            String currencyCode = "";
            BigDecimal bigDecTotal = new BigDecimal(0);
            JSONArray jsArrNightlyPriceInfo = new JSONArray(jsArryNightlyPriceInfo.toString());

            for (int i = 0; i < jsArrNightlyPriceInfo.length(); i++) {
                bigDecTotal = bigDecTotal.add(jsArrNightlyPriceInfo.getJSONObject(i).getBigDecimal("amount"));
                if (i == 0) {
                    currencyCode = jsArrNightlyPriceInfo.getJSONObject(i).getString("currencyCode");
                }
            }
            jsObjTotalPriceInfo.put("amount", bigDecTotal);
            jsObjTotalPriceInfo.put("taxes", getTotalPriceInfoTax(jsArryNightlyPriceInfo, currencyCode));
            jsObjTotalPriceInfo.put("currencyCode", currencyCode);
        } catch (OperationException oe) {
            logger.error("Exception occurred in getTotalPriceInfoObj method : " + oe.getErrors());
            throw oe;
        } catch (Exception e) {
            logger.error("Exception occurred in getTotalPriceInfoObj method : " + e.getMessage());
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred in getTotalPriceInfoObj method : " + e.getMessage()));
            throw new OperationException(entity);
        }
        logger.debug("Inside getTotalPriceInfoObj method:End");
        return jsObjTotalPriceInfo;
    }

    private JSONObject getTotalPriceInfoTax(JSONArray pJSArrNigthlyPriceInfo, String currencyCode) throws OperationException {
        {
            logger.debug("Inside getTotalPriceInfoObj method:Start");
            JSONObject jsObjTotalPriceInfo = new JSONObject();
            try {
                BigDecimal bigDecTotal = new BigDecimal(0);
                HashMap<String, BigDecimal> hsPaxTaxes = new HashMap<>();
                BigDecimal totalTaxes = new BigDecimal(0);
                for (int i = 0; i < pJSArrNigthlyPriceInfo.length(); i++) {
                    JSONObject jsObjNightlyPriceInfo = pJSArrNigthlyPriceInfo.getJSONObject(i);
                    JSONObject jsObjTaxes = jsObjNightlyPriceInfo.getJSONObject("taxes");
                    if (jsObjTaxes.length() > 0) {
                        bigDecTotal = bigDecTotal.add(jsObjTaxes.getBigDecimal("amount"));
                        totalTaxes = new BigDecimal(0);
                        JSONArray jsArrTax = jsObjTaxes.getJSONArray("tax");
                        for (int j = 0; j < jsArrTax.length(); j++) {
                            JSONObject objTax = jsArrTax.getJSONObject(j);
                            String strTaxCode = (String) objTax.get("taxCode");
                            BigDecimal amt = new BigDecimal(objTax.get("amount").toString());
                            totalTaxes = totalTaxes.add(amt);
                            if (hsPaxTaxes.containsKey(strTaxCode)) {
                                BigDecimal oldAmt = hsPaxTaxes.get(strTaxCode);
                                BigDecimal newAmt = oldAmt.add(amt);
                                hsPaxTaxes.put(strTaxCode, newAmt);
                            } else {
                                hsPaxTaxes.put(strTaxCode, amt);
                            }
                        }
                    }
                }

                JSONArray arrTax = new JSONArray();

                Set<Map.Entry<String, BigDecimal>> entry = hsPaxTaxes.entrySet();

                for (Map.Entry<String, BigDecimal> e : entry) {
                    JSONObject objTaxes = new JSONObject();
                    objTaxes.put("taxCode", e.getKey());
                    objTaxes.put("amount", e.getValue());
                    objTaxes.put("currencyCode", currencyCode);
                    arrTax.put(objTaxes);
                }

                jsObjTotalPriceInfo.put("amount", bigDecTotal);
                jsObjTotalPriceInfo.put("tax", arrTax);
                jsObjTotalPriceInfo.put("currencyCode", currencyCode);
            } catch (Exception e) {
                logger.error("Exception occurred in getTotalPriceInfoObj method : " + e.getMessage());
                Map<String, String> entity = new HashMap<>();
                entity.put("message", String.format("Exception occurred in getTotalPriceInfoObj method : " + e.getMessage()));
                throw new OperationException(entity);
            }
            logger.debug("Inside getTotalPriceInfoObj method:End");
            return jsObjTotalPriceInfo;
        }
    }

    private JSONArray getOccupancyInfo(JSONArray pJSArrPassengerDetails) throws OperationException {
        logger.debug("Inside getOccupancyInfo method:Start");
        JSONArray occupancyInfoArr = new JSONArray();
        try {
            JSONObject occupancyInfoEle = null;
            for (int i = 0; i < pJSArrPassengerDetails.length(); i++) {
                occupancyInfoEle = new JSONObject();
                occupancyInfoEle.put("minOccupancy", "");
                occupancyInfoEle.put("paxType", pJSArrPassengerDetails.getJSONObject(i).getString("passengerType"));
                occupancyInfoEle.put("maxAge", "");
                occupancyInfoEle.put("minAge", "");
                occupancyInfoEle.put("maxOccupancy", "");
                occupancyInfoArr.put(occupancyInfoEle);
            }
        } catch (Exception e) {
            logger.error("Exception occurred in getOccupancyInfo method : " + e.getMessage());
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred in getOccupancyInfo method : " + e.getMessage()));
            throw new OperationException(entity);
        }
        logger.debug("Inside getOccupancyInfo method:End");
        return occupancyInfoArr;
    }

    private JSONObject getRoomInfo(JSONObject jsObjRoomBreakup, JSONObject productDetailsJSON) throws OperationException {
        logger.debug("Inside getRoomInfo method:Start");
        JSONObject jsObjRoomInfo = new JSONObject();
        try {
            jsObjRoomInfo.put("mealInfo", getMealInfo(jsObjRoomBreakup));
            jsObjRoomInfo.put("hotelInfo", getHotelInfo(productDetailsJSON));
            jsObjRoomInfo.put("roomTypeInfo", getRoomTypeInfo(jsObjRoomBreakup));
            jsObjRoomInfo.put("ratePlanInfo", getRatePlanInfo(jsObjRoomBreakup));
        } catch (OperationException oe) {
            logger.error("Exception occurred in getRoomInfo method : " + oe.getErrors());
            throw oe;
        } catch (Exception e) {
            logger.error("Exception occurred in getRoomInfo method : " + e.getMessage());
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred in getRoomInfo method : " + e.getMessage()));
            throw new OperationException(entity);
        }
        logger.debug("Inside getRoomInfo method:End");
        return jsObjRoomInfo;
    }

    private JSONObject getMealInfo(JSONObject jsObjRoomBreakup) throws OperationException {
        logger.debug("Inside getMealInfo method:Start");
        JSONObject jsObjMealInfo = new JSONObject();
        try {
            boolean isDefineRateManually = jsObjRoomBreakup.getBoolean("isDefineRatesManually");

            if (isDefineRateManually) {
                jsObjMealInfo.put("mealName", "");
                jsObjMealInfo.put("mealCode", "");
            } else {
                JSONArray jsArrAddDefineRates = jsObjRoomBreakup.getJSONObject("supplierRates").getJSONObject("supplierRateDetails").getJSONObject("defineRates").getJSONArray("addDefineRates");
                for (int i = 0; i < jsArrAddDefineRates.length(); i++) {
                    JSONObject jsObjAddDefineRates = jsArrAddDefineRates.getJSONObject(i);
                    if (jsObjAddDefineRates.optBoolean("isSelected")) {
                        JSONArray jsArrChildRecordScreen = jsArrAddDefineRates.getJSONObject(i).getJSONArray("childRecordScreen1");
                        for (int j = 0; j < jsArrChildRecordScreen.length(); j++) {
                            JSONObject jsObjChildRecordScreen = jsArrChildRecordScreen.getJSONObject(j);
                            if (jsObjChildRecordScreen.getBoolean("isSelected")) {
                                jsObjMealInfo.put("mealName", "");
                                jsObjMealInfo.put("mealCode", jsObjChildRecordScreen.getString("mealPlan"));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception occurred in getMealInfo method : " + e.getMessage());
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred in getMealInfo method : " + e.getMessage()));
            throw new OperationException(entity);
        }
        logger.debug("Inside getMealInfo method:End");
        return jsObjMealInfo;
    }

    private JSONObject getHotelInfo(JSONObject productDetailsJSON) throws OperationException {
        logger.debug("Inside getHotelInfo method:Start");
        JSONObject jsObjHotelInfo = new JSONObject();
        try {
            jsObjHotelInfo.put("hotelCode", "");
            jsObjHotelInfo.put("hotelName", productDetailsJSON.getJSONObject("productName").getString("name"));
            jsObjHotelInfo.put("hotelRef", "");
        } catch (Exception e) {
            logger.error("Exception occurred in getHotelInfo method : " + e.getMessage());
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred in getHotelInfo method : " + e.getMessage()));
            throw new OperationException(entity);
        }
        logger.debug("Inside getHotelInfo method:End");

        return jsObjHotelInfo;
    }

    private JSONObject getRoomTypeInfo(JSONObject jsObjRoomBreakup) throws OperationException {
        logger.debug("Inside getRoomTypeInfo method:Start");
        JSONObject jsObjRoomTypeInfo = new JSONObject();
        try {
            jsObjRoomTypeInfo.put("roomTypeCode", "");
            jsObjRoomTypeInfo.put("roomCategoryCode", "");
            jsObjRoomTypeInfo.put("roomRef", "");
            jsObjRoomTypeInfo.put("roomCategoryName", jsObjRoomBreakup.getString("roomCategory"));
            jsObjRoomTypeInfo.put("roomTypeName", jsObjRoomBreakup.getString("roomType"));
        } catch (Exception e) {
            logger.error("Exception occurred in getRoomTypeInfo method : " + e.getMessage());
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred in getRoomTypeInfo method : " + e.getMessage()));
            throw new OperationException(entity);
        }
        logger.debug("Inside getRoomTypeInfo method:End");
        return jsObjRoomTypeInfo;
    }

    private JSONObject getRatePlanInfo(JSONObject jsObjRoomBreakup) throws OperationException {
        logger.debug("Inside getRatePlanInfo method:Start");
        JSONObject jsObjRatePlanInfo = new JSONObject();
        try {
            jsObjRatePlanInfo.put("ratePlanName", "");
            jsObjRatePlanInfo.put("ratePlanRef", "");
            jsObjRatePlanInfo.put("ratePlanCode", "");
            jsObjRatePlanInfo.put("bookingRef", "");
        } catch (Exception e) {
            logger.error("Exception occurred in getRatePlanInfo method : " + e.getMessage());
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred in getRatePlanInfo method : " + e.getMessage()));
            throw new OperationException(entity);
        }
        logger.debug("Inside getRatePlanInfo method:End");
        return jsObjRatePlanInfo;
    }

    /*Below code is written for creating structure for which we don't get values from UI*/
    /*Start*/

    private JSONObject getDocumentInfo() throws OperationException {
        logger.debug("Inside getDocumentInfo method:Start");

        JSONObject jsObjDocumentDetails = new JSONObject();
        try {
            JSONArray jsArrDocumentInfo = new JSONArray();
            JSONObject jsObjDocumentInfo = new JSONObject();

            jsObjDocumentInfo.put("expiryDate", "");
            jsObjDocumentInfo.put("nationality", "");
            jsObjDocumentInfo.put("docNumber", "");
            jsObjDocumentInfo.put("docType", "");
            jsObjDocumentInfo.put("issueCountry", "");
            jsObjDocumentInfo.put("issueAuthority", "");
            jsObjDocumentInfo.put("effectiveDate", "");
            jsObjDocumentInfo.put("issueLocation", "");
            jsArrDocumentInfo.put(jsObjDocumentInfo);
            jsObjDocumentDetails.put("documentInfo", jsObjDocumentInfo);
        } catch (Exception e) {
            logger.error("Exception occurred in getDocumentInfo method : " + e.getMessage());
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred in getDocumentInfo method : " + e.getMessage()));
            throw new OperationException(entity);
        }
        logger.debug("Inside getDocumentInfo method:End");
        return jsObjDocumentDetails;
    }

    private JSONObject getAddressDetails() throws OperationException {
        logger.debug("Inside getAddressDetails method:Start");
        JSONObject jsObjAddressDetails = new JSONObject();
        try {
            jsObjAddressDetails.put("zip", "");
            jsObjAddressDetails.put("country", "");
            jsObjAddressDetails.put("city", "");
            jsObjAddressDetails.put("addrLine2", "");
            jsObjAddressDetails.put("addrLine1", "");
            jsObjAddressDetails.put("state", "");
        } catch (Exception e) {
            logger.error("Exception occurred in getAddressDetails method : " + e.getMessage());
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred in getAddressDetails method : " + e.getMessage()));
            throw new OperationException(entity);
        }
        logger.debug("Inside getAddressDetails method:End");
        return jsObjAddressDetails;
    }

    private JSONArray getContactDetails() throws OperationException {
        logger.debug("Inside getContactDetails method:Start");
        JSONArray jsArrContactDetails = new JSONArray();
        try {
            JSONObject jsObjContactDetails = new JSONObject();
            JSONObject jsObjContactInfo = new JSONObject();

            jsObjContactInfo.put("countryCode", "");
            jsObjContactInfo.put("contactType", "");
            jsObjContactInfo.put("mobileNo", "");
            jsObjContactInfo.put("email", "");

            jsObjContactDetails.put("contactInfo", jsObjContactInfo);
            jsArrContactDetails.put(jsObjContactDetails);
        } catch (Exception e) {
            logger.error("Exception occurred in getContactDetails method : " + e.getMessage());
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred in getContactDetails method : " + e.getMessage()));
            throw new OperationException(entity);
        }
        logger.debug("Inside getContactDetails method:End");
        return jsArrContactDetails;
    }
    /*End*/

    /*Below code is written for calculating price*/
    /*Start*/
    private void calculatePrices(JSONObject reqJson, JSONObject resJson, JSONObject clientCommResJson, JSONObject suppCommResJson, Map<Integer, String> SI2BRMSRoomMap,
                                 boolean retainSuppFaresAndComm, Date cutOffDate, JSONObject mdmDataJSon) throws OperationException {
        logger.debug("Inside calculatePrices method:Start");
        try {
            JSONArray resRoomJsonArr, entityCommJsonArr, additionalCommsJsonArr;
            JSONArray newTaxJsonArr, newNghtlyPriceJsonArr;
            JSONObject resRoomJson, ccommRoomJson, entityCommJson, markupCalcJson, fareBreakupJson;
            JSONObject newTotalPriceJson, newTotalTaxJson;

            JSONArray briArr = getClientCommercialsBusinessRuleIntakeJSONArray(clientCommResJson);
            ClientType clientType = ClientType.valueOf(reqJson.getJSONObject(JSON_PROP_REQHEADER).getJSONObject(JSON_PROP_CLIENTCONTEXT).getString(JSON_PROP_CLIENTTYPE));
            Map<String, String> ccommToTypeMap = getClientCommercialsAndTheirType(clientCommResJson);
            Map<String, String> scommToTypeMap = getSupplierCommercialsAndTheirType(suppCommResJson);
            JSONArray resAccoInfoJsonArr = resJson.getJSONObject(JSON_PROP_RESBODY).getJSONArray(JSON_PROP_ACCOMODATIONARR);

            int roomIdx = 0;
            JSONObject reqHdr = reqJson.getJSONObject(JSON_PROP_REQHEADER);
            String clientMarket = reqHdr.getJSONObject(JSON_PROP_CLIENTCONTEXT).getString(JSON_PROP_CLIENTMARKET);
            String clientCcyCode = reqHdr.getJSONObject(JSON_PROP_CLIENTCONTEXT).getString(JSON_PROP_CLIENTCCY);
            String suppId = DEF_SUPPID, supplierCurrency;
            boolean isMrkpAdded, isRecvblsAdded;
            BigDecimal totalPrice, newTotalPrice, totalTaxPrice, totalRecvbls, totalIncentives;

            for (int i = 0; i < resAccoInfoJsonArr.length(); i++) {
                resRoomJsonArr = resAccoInfoJsonArr.getJSONObject(i).getJSONArray(JSON_PROP_ROOMSTAYARR);
                for (int j = 0; j < resRoomJsonArr.length(); j++) {
                    /**
                     * Get supplier roomStay json and apply ROE to all the prices
                     * TODO:This ROE can also be applied while creating the response.
                     * Supplier prices are retained in case of reprice operation where flag retainFaresAndComm comes as true
                     * Commercials and supplier prices breakup might be needed after calculating prices. So pushing to redis is not done here
                     * due to which new price json's have been made
                     */
                    //---------------Create new price structs--------------------
                    resRoomJson = resRoomJsonArr.getJSONObject(j);
                    supplierCurrency = resRoomJson.getJSONObject(JSON_PROP_ROOMPRICE).getString(JSON_PROP_CCYCODE);
                    newTotalPriceJson = new JSONObject(resRoomJson.getJSONObject(JSON_PROP_ROOMPRICE).toString());
                    newTotalTaxJson = newTotalPriceJson.getJSONObject(JSON_PROP_TOTALTAX);
                    newTaxJsonArr = new JSONArray();
                    newNghtlyPriceJsonArr = new JSONArray(resRoomJson.getJSONArray(JSON_PROP_NIGHTLYPRICEARR).toString());
                    //-------------Apply ROE-------------
                    applyRoe(newTotalPriceJson, clientCcyCode, clientMarket, cutOffDate);
                    applyRoe(newTotalTaxJson, clientCcyCode, clientMarket, cutOffDate);
                    if (newTotalTaxJson.has(JSON_PROP_TAXBRKPARR)) {
                        newTaxJsonArr = newTotalTaxJson.getJSONArray(JSON_PROP_TAXBRKPARR);
                        for (Object taxJson : newTaxJsonArr) {
                            applyRoe((JSONObject) taxJson, clientCcyCode, clientMarket, cutOffDate);
                        }
                    }
                    for (Object nghtPriceJson : newNghtlyPriceJsonArr) {
                        applyRoe((JSONObject) nghtPriceJson, clientCcyCode, clientMarket, cutOffDate);
                        applyRoe(((JSONObject) nghtPriceJson).getJSONObject(JSON_PROP_TOTALTAX), clientCcyCode, clientMarket, cutOffDate);
                    }
                    //----------------Retain fares-------------
                    if (retainSuppFaresAndComm) {
                        resRoomJson.put(JSON_PROP_SUPPROOMPRICE, resRoomJson.getJSONObject(JSON_PROP_ROOMPRICE));
                        resRoomJson.put(JSON_PROP_SUPPNIGHTLYPRICEARR, resRoomJson.getJSONArray(JSON_PROP_NIGHTLYPRICEARR));
                    }
                    //--------------Initialize prices-----------
                    totalPrice = newTotalPrice = newTotalPriceJson.getBigDecimal(JSON_PROP_AMOUNT);
                    totalTaxPrice = newTotalTaxJson.optBigDecimal(JSON_PROP_AMOUNT, new BigDecimal(0));
                    totalRecvbls = new BigDecimal(0);
                    totalIncentives = new BigDecimal(0);
                    //---------------Inititalize receivables-----------
                    JSONObject recevblsJson = new JSONObject();
                    recevblsJson.put(JSON_PROP_CCYCODE, clientCcyCode);
                    recevblsJson.put(JSON_PROP_AMOUNT, totalRecvbls);
                    recevblsJson.put(JSON_PROP_RECEIVABLE, new JSONArray());
                    //---------------Inititalize incentives-----------
                    JSONObject incentivesJson = new JSONObject();
                    incentivesJson.put(JSON_PROP_CCYCODE, clientCcyCode);
                    incentivesJson.put(JSON_PROP_AMOUNT, totalIncentives);
                    incentivesJson.put(JSON_PROP_INCENTIVE, new JSONArray());
                    //-------------replace current prices---------
                    newTotalPriceJson.put(JSON_PROP_RECEIVABLES, recevblsJson);
                    resRoomJson.put(JSON_PROP_ROOMPRICE, newTotalPriceJson);
                    resRoomJson.put(JSON_PROP_NIGHTLYPRICEARR, newNghtlyPriceJsonArr);
                    //---------------------------------------------------------------------------

                    /**
                     * SI2BRMS map has been populated during supplier commercials.
                     * rooms in SI response are indexed according to the natural ordering of their occurence
                     * rooms in Comm response are indexed depending on the position of BRi,hotel and the room itself and forming a composite briKey
                     */
                    ccommRoomJson = getClientCommercialsRoomDetailJson(SI2BRMSRoomMap.get(roomIdx++), briArr);//decompose key to find indexes and get matching room
                    if (ccommRoomJson == null) {
                        logger.warn(String.format("Client commercials room detail not found at briKey %s for room number %s", SI2BRMSRoomMap.get(roomIdx - 1), roomIdx - 1));
                        continue;
                    }
                    entityCommJsonArr = ccommRoomJson.optJSONArray(JSON_PROP_ENTITYCOMMS);//retieve client commercials
                    JSONArray clntEntityCommJsonArray = getClntEntityCommJsonArray(entityCommJsonArr, ccommToTypeMap, reqHdr, supplierCurrency, mdmDataJSon);

                    if (retainSuppFaresAndComm) {
                        //JSONArray clntEntityCommJsonArray = getClntEntityCommJsonArray(entityCommJsonArr, ccommToTypeMap,reqHdr,supplierCurrency);
                        JSONArray suppCommJsonArray = getSuppCommJsonArray(ccommRoomJson.optJSONArray(JSON_PROP_COMMDETAILS), scommToTypeMap, supplierCurrency);

                        if (suppCommJsonArray.length() == 0)
                            logger.warn(String.format("No supplier commercials found for supplier %s at briKey %s for room number %s", suppId, SI2BRMSRoomMap.get(roomIdx - 1), roomIdx - 1));
                        //if (clntEntityCommJsonArray.length()==0) :- done below
                        //logger.warn(String.format("No client commercials found for supplier %s at briKey %s for room number %s",suppId,SI2BRMSRoomMap.get(roomIdx-1),roomIdx-1));

                        resRoomJson.put(JSON_PROP_CLIENTENTITYCOMMS, clntEntityCommJsonArray);
                        resRoomJson.put(JSON_PROP_SUPPCOMM, suppCommJsonArray);
                    }

                    suppId = resRoomJson.getString(JSON_PROP_SUPPREF);
                    isMrkpAdded = isRecvblsAdded = false;
                    if (entityCommJsonArr == null || entityCommJsonArr.length() == 0) {
                        logger.info(String.format("No client commercials found for supplier %s at briKey %s for room number %s", suppId, SI2BRMSRoomMap.get(roomIdx - 1), roomIdx - 1));
                        continue;
                    }
                    //backtrack entity commercials and apply the latest one
                    for (int l = (entityCommJsonArr.length() - 1); l >= 0 && !(isMrkpAdded && isRecvblsAdded); l--) {
                        entityCommJson = entityCommJsonArr.getJSONObject(l);

                        markupCalcJson = entityCommJson.optJSONObject(JSON_PROP_MARKUPCOMDTLS);
                        if (markupCalcJson != null && !isMrkpAdded) {
                            BigDecimal markupCalcRoe = dataUtils.getRateOfExchange(markupCalcJson.optString(JSON_PROP_COMMCCY, supplierCurrency), clientCcyCode, clientMarket, cutOffDate);
                            newTotalPrice = newTotalPrice.add(markupCalcJson.optBigDecimal(JSON_PROP_COMMAMOUNT, new BigDecimal(0)).multiply(markupCalcRoe));

                            fareBreakupJson = markupCalcJson.optJSONObject(JSON_PROP_FAREBRKUP);
                            if (fareBreakupJson != null) {
                                //BigDecimal mrkdUpBasePrice = fareBreakupJson.optBigDecimal(JSON_PROP_BASEFARE,new BigDecimal(0)).multiply(markupCalcRoe);
                                //newTotalTaxJson.put(JSON_PROP_AMOUNT, totalPrice.subtract(mrkdUpBasePrice));
                                BigDecimal totalTaxDiff = new BigDecimal(0);
                                for (Object taxComp : newTaxJsonArr) {
                                    //TODO:chek for optional taxDetails
                                    String taxCode = ((JSONObject) taxComp).getString(JSON_PROP_TAXCODE);
                                    JSONObject ccommTaxDetailJson = getTaxDetailForTaxCode(fareBreakupJson.optJSONArray(JSON_PROP_TAXDETAILS), taxCode);
                                    if (ccommTaxDetailJson != null && ccommTaxDetailJson.has(JSON_PROP_TAXVALUE)) {
                                        BigDecimal mrkdUpTaxPrice = ccommTaxDetailJson.getBigDecimal(JSON_PROP_TAXVALUE);
                                        if (mrkdUpTaxPrice != null) {
                                            mrkdUpTaxPrice = mrkdUpTaxPrice.multiply(markupCalcRoe);
                                            ((JSONObject) taxComp).put(JSON_PROP_AMOUNT, mrkdUpTaxPrice);
                                            totalTaxDiff = totalTaxDiff.add(mrkdUpTaxPrice.subtract(((JSONObject) taxComp).getBigDecimal(JSON_PROP_AMOUNT)));
                                        }
                                    }
                                }
                                if (newTotalTaxJson.has(JSON_PROP_AMOUNT)) {
                                    newTotalTaxJson.put(JSON_PROP_AMOUNT, newTotalTaxJson.getBigDecimal(JSON_PROP_AMOUNT).add(totalTaxDiff));
                                }
                            }
                            isMrkpAdded = true;
                        }

                        /** TODO: In case of B2B, do we need to add additional receivable commercials for all client hierarchy levels?
                         * As of now the latest one is considered
                         */
                        additionalCommsJsonArr = entityCommJson.optJSONArray(JSON_PROP_ADDCOMMDETAILS);//take the array additionalcommercialDetails
                        if (additionalCommsJsonArr != null && !isRecvblsAdded) {
                            for (int x = 0; x < additionalCommsJsonArr.length(); x++) {
                                JSONObject additionalCommsJson = additionalCommsJsonArr.getJSONObject(x);//take object of additionalcommercialDetails array one by one
                                if (COMM_TYPE_RECEIVABLE.equals(ccommToTypeMap.get(additionalCommsJson.optString(JSON_PROP_COMMNAME)))) {//is the additionalCommName receivable?
                                    String additionalCommCcy = additionalCommsJson.optString(JSON_PROP_COMMCCY, supplierCurrency);
                                    BigDecimal additionalCommAmt = additionalCommsJson.getBigDecimal(JSON_PROP_COMMAMOUNT);
                                    if (additionalCommAmt != null) {
                                        additionalCommAmt = additionalCommAmt.multiply(dataUtils.getRateOfExchange(additionalCommCcy, clientCcyCode, clientMarket, cutOffDate));
                                        newTotalPrice = newTotalPrice.add(additionalCommAmt);
                                        //add receivable
                                        JSONObject recevblJson = new JSONObject();
                                        recevblJson.put(JSON_PROP_AMOUNT, additionalCommAmt);
                                        recevblJson.put(JSON_PROP_CCYCODE, clientCcyCode);
                                        recevblJson.put(JSON_PROP_CODE, additionalCommsJson.optString(JSON_PROP_COMMNAME));

                                        recevblsJson.append(JSON_PROP_RECEIVABLE, recevblJson);
                                        totalRecvbls = totalRecvbls.add(additionalCommAmt);
                                        isRecvblsAdded = true;
                                    }
                                }
                            }
                        }
                        // For B2B clients, the incentives of the last client hierarchy level should be accumulated and returned in the response.
                        if (clientType == ClientType.B2B) {
                            if (l == (clntEntityCommJsonArray.length() - 1)) {
                                JSONObject clientEntityCommercialsJson = clntEntityCommJsonArray.getJSONObject(l);
                                JSONArray clntCommArr = clientEntityCommercialsJson.getJSONArray("clientCommercials");
                                for (int x = 0; x < clntCommArr.length(); x++) {
                                    JSONObject clntCommObj = clntCommArr.getJSONObject(x);
                                    if (COMM_TYPE_PAYABLE.equals(clntCommObj.getString(JSON_PROP_COMMTYPE))) {
                                        String commCcy = clntCommObj.getString(JSON_PROP_COMMCCY);
                                        String commName = clntCommObj.getString(JSON_PROP_COMMNAME);
                                        BigDecimal commAmt = clntCommObj.getBigDecimal(JSON_PROP_COMMAMOUNT).multiply(dataUtils.getRateOfExchange(commCcy, clientCcyCode, clientMarket, cutOffDate));

                                        //add incentives
                                        if (commAmt != null) {
                                            JSONObject incentiveJson = new JSONObject();
                                            incentiveJson.put(JSON_PROP_AMOUNT, commAmt);
                                            incentiveJson.put(JSON_PROP_CCYCODE, clientCcyCode);
                                            incentiveJson.put(JSON_PROP_CODE, commName);
                                            incentivesJson.append(JSON_PROP_INCENTIVE, incentiveJson);
                                            totalIncentives = totalIncentives.add(commAmt);
                                        }
                                    }
                                }
                            }
                            newTotalPriceJson.put(JSON_PROP_INCENTIVES, incentivesJson);
                        }
                    }

                    newTotalPriceJson.put(JSON_PROP_AMOUNT, newTotalPrice);
                    recevblsJson.put(JSON_PROP_AMOUNT, totalRecvbls);
                    incentivesJson.put(JSON_PROP_AMOUNT, totalIncentives);
                    BigDecimal increasedTotalPrice = newTotalPrice.subtract(totalPrice);
                    BigDecimal increasedTotalTaxPrice = newTotalTaxJson.optBigDecimal(JSON_PROP_AMOUNT, new BigDecimal(0)).subtract(totalTaxPrice);

                    //calculate night price by weighted avg
                    for (Object nghtPriceJson : newNghtlyPriceJsonArr) {
                        BigDecimal nightlyPrice = ((JSONObject) nghtPriceJson).getBigDecimal(JSON_PROP_AMOUNT);
                        ((JSONObject) nghtPriceJson).put(JSON_PROP_AMOUNT, nightlyPrice.add(nightlyPrice.divide(totalPrice, new MathContext(6, RoundingMode.HALF_UP)).multiply(increasedTotalPrice)));

                        JSONObject nghtTotalTaxJson = ((JSONObject) nghtPriceJson).getJSONObject(JSON_PROP_TOTALTAX);
                        if (nghtTotalTaxJson.has(JSON_PROP_AMOUNT)) {
                            //if nght tax is present total tax should be present.Else raise it to si
                            nightlyPrice = ((JSONObject) nghtTotalTaxJson).getBigDecimal(JSON_PROP_AMOUNT);
                            nghtTotalTaxJson.put(JSON_PROP_AMOUNT, nightlyPrice.add(nightlyPrice.divide(totalTaxPrice, new MathContext(6, RoundingMode.HALF_UP)).multiply(increasedTotalTaxPrice)));
                        }
                    }

                }
            }
        } catch (OperationException oe) {
            logger.error("An exceptions occurred calculateCommercials : " + oe.getErrors());
            throw oe;
        } catch (Exception e) {
            logger.error("Exception occurred in calculatePrices method : " + e.getMessage());
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("An exceptions occurred calculateCommercials : " + e.getMessage()));
            throw new OperationException(entity);
        }
        logger.debug("Inside calculatePrices method:Start");
    }

    private JSONArray getClientCommercialsBusinessRuleIntakeJSONArray(JSONObject clientCommResJson) {

        JSONArray briJsonArray = new JSONArray();

        try {
            briJsonArray = clientCommResJson.getJSONObject(JSON_PROP_RESULT).getJSONObject(JSON_PROP_EXECUTIONRES).getJSONArray(JSON_PROP_RESULTS).getJSONObject(0).getJSONObject(JSON_PROP_VALUE).getJSONObject(JSON_PROP_CLIENTTRANRULES).getJSONArray(JSON_PROP_BUSSRULEINTAKE);
        } catch (Exception e) {
            logger.warn("Client Commercials \"businessRuleIntake\" evaluated to be null");
        }

        if (briJsonArray == null) {
            logger.warn("Client Commercials \"businessRuleIntake\" evaluated to be null");
            briJsonArray = new JSONArray();
        }

        return briJsonArray;
    }

    private JSONObject getClientCommercialsRoomDetailJson(String briKey, JSONArray briArr) {
        JSONObject roomJson = null;

        if (briKey == null)
            return roomJson;

        String[] briIdxs = briKey.split(Pattern.quote(String.valueOf(KEYSEPARATOR)));//to escape this pipe
        //Key has been made using bri index,hotel index and room index in supplier commercials. Therefore this below restriction.
        if (briIdxs.length != 3)
            return roomJson;
        try {
            roomJson = (JSONObject) new JSONPointer(String.format("/%s/%s/%s/%s/%s", briIdxs[0], JSON_PROP_HOTELDETAILS, briIdxs[1], JSON_PROP_ROOMDETAILS, briIdxs[2])).queryFrom(briArr);
        } catch (Exception e) {
            logger.error("Exception occured in getClientCommercialsRoomDetailJson method");
        }

        return roomJson;
    }

    private JSONArray getSuppCommJsonArray(JSONArray suppCommDtlsJsonArr, Map<String, String> scommTypeMap, String supplierCurrency) {

        JSONArray suppCommJsonArr = new JSONArray();

        if (suppCommDtlsJsonArr == null)
            return suppCommJsonArr;

        suppCommDtlsJsonArr.forEach(suppCommDtlsJson -> addCommercialJson(suppCommJsonArr, (JSONObject) suppCommDtlsJson, scommTypeMap, supplierCurrency));

        return suppCommJsonArr;
    }

    private JSONArray getClntEntityCommJsonArray(JSONArray entityCommJsonArr, Map<String, String> ccommTypeMap, JSONObject reqHdr, String supplierCurrency, JSONObject mdmDataJSon) throws JsonProcessingException, OperationException {

        JSONArray clntEntityCommJsonArr = new JSONArray();

        if (entityCommJsonArr == null)
            return clntEntityCommJsonArr;

        entityCommJsonArr.forEach(clntentityJson -> {
            try {
                addClientEntityJson(clntEntityCommJsonArr, (JSONObject) clntentityJson, ccommTypeMap, reqHdr, supplierCurrency, mdmDataJSon);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (OperationException e) {
                e.printStackTrace();
            }
        });

        return clntEntityCommJsonArr;
    }

    private void addClientEntityJson(JSONArray clntEntityJsonArr, JSONObject clntentityJson,
                                     Map<String, String> ccommTypeMap, JSONObject reqHdr, String supplierCurrency, JSONObject mdmDataJSon) throws JsonProcessingException, OperationException {

        JSONObject entityJson = new JSONObject();
        JSONArray ccommJsonArr = new JSONArray();
        //  UserContext usrCtx = UserContext.getUserContextForSession(reqHdr); //Operations Specific

        // TODO:get data from usr ctx
        //JSONObject userCtxJson = usrCtx.toJSON(); //Operations Specific
        List<ClientInfo> clHierList = null;//Operations Specific
//        JSONArray clientEntityDetailsArr = dataUtils.getClientCommercialEntityDetails( "", "", "", "", "",   clHierList);
        JSONArray clientEntityDetailsArr = mdmDataJSon.optJSONArray(Constants.JSON_PROP_CLIENTCOMMENTITYDTLS);
        for (int y = 0; y < clientEntityDetailsArr.length(); y++) {
			/*if(clientEntityDetailsArr.getJSONObject(y).opt(JSON_PROP_COMMENTITYID)!=null)
			{
			if(clientEntityDetailsArr.getJSONObject(y).opt(JSON_PROP_COMMENTITYID).toString().equalsIgnoreCase(clientEntityCommJson.get("entityName").toString()))
			{
				clientEntityDetailsJson=clientEntityDetailsArr.getJSONObject(y);
			}
			}*/
            //TODO:Add a check later
            entityJson = clientEntityDetailsArr.getJSONObject(y);
            entityJson.put(JSON_PROP_ENTITYNAME, clntentityJson.getString(JSON_PROP_ENTITYNAME));
            entityJson.put(JSON_PROP_CLIENTCOMM, ccommJsonArr);
            // insert markup
            addCommercialJson(ccommJsonArr, clntentityJson.optJSONObject(JSON_PROP_MARKUPCOMDTLS), ccommTypeMap, supplierCurrency);
            // insert additional comm details
            JSONArray addCommDtlsJsonArr = clntentityJson.optJSONArray(JSON_PROP_ADDCOMMDETAILS);
            if (addCommDtlsJsonArr != null) {
                addCommDtlsJsonArr.forEach(addCommDtlsJson -> addCommercialJson(ccommJsonArr, (JSONObject) addCommDtlsJson, ccommTypeMap, supplierCurrency));
            }
            // insert fixed comm details
            JSONArray fixedCommDtlsJsonArr = clntentityJson.optJSONArray("fixedCommercialDetails");
            if (fixedCommDtlsJsonArr != null) {
                fixedCommDtlsJsonArr.forEach(fixedCommDtlsJson -> addCommercialJson(ccommJsonArr, (JSONObject) fixedCommDtlsJson, ccommTypeMap, supplierCurrency));
            }
            // insert retention comm details
            JSONArray retentionCommDtlsJsonArr = clntentityJson.optJSONArray("retentionCommercialDetails");
            if (retentionCommDtlsJsonArr != null) {
                retentionCommDtlsJsonArr.forEach(retentionCommDtlsJson -> addCommercialJson(ccommJsonArr, (JSONObject) retentionCommDtlsJson, ccommTypeMap, supplierCurrency));
            }
            clntEntityJsonArr.put(entityJson);
        }
    }

    private void addCommercialJson(JSONArray commJsonArr, JSONObject commJson, Map<String, String> commTypeMap, String supplierCurrency) {

        if (commJson == null)
            return;
        JSONObject newCommJson = new JSONObject();

        newCommJson.put("commercialCalculationPercentage", commJson.optString("commercialCalculationPercentage", null));
        newCommJson.put("commercialCalculationAmount", commJson.optString("commercialCalculationAmount", null));
        newCommJson.put("commercialFareComponent", commJson.optString("commercialFareComponent", null));
        newCommJson.put("retentionPercentage", commJson.optString("retentionPercentage", null));
        newCommJson.put("retentionAmountPercentage", commJson.optString("retentionAmountPercentage", null));
        newCommJson.put("remainingPercentageAmount", commJson.optString("remainingPercentageAmount", null));
        newCommJson.put("remainingAmount", commJson.optString("remainingAmount", null));
        newCommJson.put(JSON_PROP_COMMCCY, commJson.optString(JSON_PROP_COMMCCY, supplierCurrency));
        newCommJson.put(JSON_PROP_COMMAMOUNT, commJson.getBigDecimal(JSON_PROP_COMMAMOUNT));
        newCommJson.put(JSON_PROP_COMMNAME, commJson.optString(JSON_PROP_COMMNAME, ""));
        newCommJson.put(JSON_PROP_COMMTYPE, commTypeMap.get(commJson.get(JSON_PROP_COMMNAME)));
        newCommJson.put(JSON_PROP_MDMRULEID, commJson.optString("mdmruleId"));

        commJsonArr.put(newCommJson);
    }

    public Map<String, String> getClientCommercialsAndTheirType(JSONObject clientCommResJson) {
        JSONArray commHeadJsonArr = null;
        JSONArray entDetaiJsonArray = null;
        JSONObject commHeadJson = null;
        Map<String, String> commToTypeMap = new HashMap<String, String>();
        JSONArray ccommBRIJsonArr = getClientCommercialsBusinessRuleIntakeJSONArray(clientCommResJson);

        for (int i = 0; i < ccommBRIJsonArr.length(); i++) {
            if ((entDetaiJsonArray = ccommBRIJsonArr.getJSONObject(i).optJSONArray(JSON_PROP_ENTITYDETAILS)) == null) {
                continue;
            }
            for (int j = 0; j < entDetaiJsonArray.length(); j++) {
                if ((commHeadJsonArr = entDetaiJsonArray.getJSONObject(j).optJSONArray(JSON_PROP_COMMHEAD)) == null) {
                    logger.warn("No commercial heads found in client commercials");
                    continue;
                }

                for (int k = 0; k < commHeadJsonArr.length(); k++) {
                    commHeadJson = commHeadJsonArr.getJSONObject(k);
                    commToTypeMap.put(commHeadJson.getString(JSON_PROP_COMMHEADNAME), commHeadJson.getString(JSON_PROP_COMMTYPE));
                }
            }
        }
        return commToTypeMap;
    }

    public Map<String, String> getSupplierCommercialsAndTheirType(JSONObject suppCommResJson) {
        JSONObject scommBRIJson, commHeadJson;
        JSONArray commHeadJsonArr = null;
        Map<String, String> suppCommToTypeMap = new HashMap<String, String>();
        JSONArray scommBRIJsonArr = getSupplierCommercialsBusinessRuleIntakeJSONArray(suppCommResJson);
        for (int i = 0; i < scommBRIJsonArr.length(); i++) {
            scommBRIJson = scommBRIJsonArr.getJSONObject(i);
            commHeadJsonArr = scommBRIJson.optJSONArray(JSON_PROP_COMMHEAD);
            if (commHeadJsonArr == null) {
                logger.warn("No commercial heads found in supplier commercials");
                continue;
            }

            for (int j = 0; j < commHeadJsonArr.length(); j++) {
                commHeadJson = commHeadJsonArr.getJSONObject(j);
                suppCommToTypeMap.put(commHeadJson.getString(JSON_PROP_COMMHEADNAME), commHeadJson.getString(JSON_PROP_COMMTYPE));
            }
        }
        return suppCommToTypeMap;
    }

    private JSONArray getSupplierCommercialsBusinessRuleIntakeJSONArray(JSONObject suppCommResJson) {
        JSONArray briJsonArray = new JSONArray();

        try {
            briJsonArray = suppCommResJson.getJSONObject(JSON_PROP_RESULT).getJSONObject(JSON_PROP_EXECUTIONRES).getJSONArray(JSON_PROP_RESULTS).getJSONObject(0).getJSONObject(JSON_PROP_VALUE).getJSONObject(JSON_PROP_SUPPTRANRULES).getJSONArray(JSON_PROP_BUSSRULEINTAKE);
        } catch (Exception e) {
            logger.warn("Supplier Commercials \"businessRuleIntake\" evaluated to be null");
        }
        if (briJsonArray == null) {
            logger.warn("Supplier Commercials \"businessRuleIntake\" evaluated to be null");
            briJsonArray = new JSONArray();
        }
        return briJsonArray;
    }

    private void applyRoe(JSONObject priceJson, String toCcy, String market, Date cutOffDate) throws OperationException, ParseException, JsonProcessingException {
        if (priceJson == null)
            return;
        if (priceJson.has(JSON_PROP_AMOUNT)) {
            BigDecimal currAmt = priceJson.getBigDecimal(JSON_PROP_AMOUNT);
            priceJson.put(JSON_PROP_AMOUNT, currAmt.multiply(dataUtils.getRateOfExchange(priceJson.optString(JSON_PROP_CCYCODE, ""), toCcy, market, cutOffDate)));
            priceJson.put(JSON_PROP_CCYCODE, toCcy);
        }
    }

    private JSONObject getTaxDetailForTaxCode(JSONArray ccommTaxDetailsJsonArr, String taxCode) {
        if (taxCode == null || taxCode.isEmpty()) {
            return null;
        }

        for (int i = 0; i < ccommTaxDetailsJsonArr.length(); i++) {
            JSONObject ccommTaxDetailJson = ccommTaxDetailsJsonArr.getJSONObject(i);
            if (taxCode.equals(ccommTaxDetailJson.getString(JSON_PROP_TAXNAME))) {
                return ccommTaxDetailJson;
            }
        }

        return null;
    }

    /*End*/

    //----------------------------------------------------------------KAFKA RQ starts here-------------------------------------------------------------------------------------
    @Override
    public JSONObject saveBooking(JSONObject applyCommRespJSON, JSONObject createBookingJSON, String bookID) throws OperationException {
        logger.debug("Inside saveBooking method:Start");
        JSONObject bookingJSON = new JSONObject();
        try {
            bookingJSON.put("requestHeader", getBookRequestHeader(applyCommRespJSON.getJSONObject(Constants.JSON_PROP_RESP_HEADER)));
            bookingJSON.put("requestBody", getBookRequestBody(applyCommRespJSON, createBookingJSON, bookID));
        } catch (OperationException oe) {
            logger.error("An exceptions occurred saveBooking" + oe.getErrors());
            throw oe;
        } catch (Exception e) {
            logger.error("Exception occurred in saveBooking method : " + e.getMessage());
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred in saveBooking method : " + e.getMessage()));
            throw new OperationException(entity);
        }
        logger.debug("Inside saveBooking method:End");
        return bookingJSON;
    }

    private JSONObject getBookRequestHeader(JSONObject respHdrJSON) throws OperationException {
        logger.debug("Inside getBookRequestHeader method:Start");
        JSONObject requestHeader = new JSONObject();
        try {
            requestHeader.put("sessionID", respHdrJSON.getString(Constants.JSON_PROP_SESSION_ID));
            requestHeader.put("userID", respHdrJSON.getString(Constants.JSON_PROP_USER_ID));
            requestHeader.put("transactionID", respHdrJSON.getString(Constants.JSON_PROP_TRAN_ID));
            requestHeader.put("clientContext", respHdrJSON.getJSONObject(Constants.JSON_PROP_CLI_CONT));
        } catch (Exception e) {
            logger.error("Exception occurred in getBookRequestHeader method : " + e.getMessage());
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred in getBookRequestHeader method : " + e.getMessage()));
            throw new OperationException(entity);
        }
        logger.debug("Inside getBookRequestHeader method:End");
        return requestHeader;
    }

    private JSONObject getBookRequestBody(JSONObject applyCommRespJSON, JSONObject createBookingJSON, String bookID) throws OperationException {
        logger.debug("Inside getBookRequestBody method:Start");
        JSONObject responseBodyJSON = new JSONObject();
        JSONArray accommodationInfoArr = new JSONArray();
        JSONObject accommodationInfoObj = new JSONObject();

        JSONObject travelPaxDetailsJSON = null, productDetailsJSON = null;

        productDetailsJSON = createBookingJSON.getJSONObject(Constants.JSON_PROP_PRODUCT_DETAILS);
        travelPaxDetailsJSON = createBookingJSON.getJSONObject(Constants.JSON_PROP_TRAVEL_PAX_DETAILS);
        JSONArray jsArrRoomStay = applyCommRespJSON.getJSONObject("responseBody").getJSONArray("accommodationInfo").getJSONObject(0).getJSONArray("roomStay");
        try {
            for (int i = 0; i < jsArrRoomStay.length(); i++) {
                JSONObject jsObjRoomStay = jsArrRoomStay.getJSONObject(i);
                JSONObject objTotalRoomInfo = getTotalRoomInfo(jsObjRoomStay);

                String supplierID = jsObjRoomStay.getString("supplierRef");
                accommodationInfoObj.put("cityCode", getCityCode(productDetailsJSON.getString("city")));
                accommodationInfoObj.put("supplierCommercialsTotals", objTotalRoomInfo.getJSONArray("supplierCommercialsTotals"));
                accommodationInfoObj.put("roe", getROE(jsObjRoomStay));
                accommodationInfoObj.put("supplierBookingPriceInfo", objTotalRoomInfo.getJSONObject("supplierBookingPriceInfo"));
                accommodationInfoObj.put("bookingPriceInfo", objTotalRoomInfo.getJSONObject("bookingPriceInfo"));
                accommodationInfoObj.put("checkIn", travelPaxDetailsJSON.getString("checkInDate").substring(0, 10));
                accommodationInfoObj.put("roomConfig", getRoomConfig(jsObjRoomStay));
                accommodationInfoObj.put("supplierRef", supplierID); // this change because for PSO generation
                JSONObject countryInfo = dataUtils.getCountryCode(productDetailsJSON.optString("country"));
                if (countryInfo != null)
                    accommodationInfoObj.put("countryCode", countryInfo.optString("code"));
                accommodationInfoObj.put("accommodationSubType", productDetailsJSON.getString("productSubCategory"));
                accommodationInfoObj.put("checkOut", travelPaxDetailsJSON.getString("checkOutDate").substring(0, 10));
                //TODO Need to find from where to get
                accommodationInfoObj.put("credentialName", "BookEngB2CAccommodationHotelTouricoIndia");
                accommodationInfoObj.put("clientEntityTotalCommercials", getClientEntityTotalCommercials(jsObjRoomStay.getJSONArray("clientEntityCommercials")));
                if(jsObjRoomStay.optJSONObject("offerDetailsSet")!=null)
                    accommodationInfoObj.put("offerDetailsSet",jsObjRoomStay.getJSONObject("offerDetailsSet"));
                accommodationInfoArr.put(accommodationInfoObj);
            }
            responseBodyJSON.put("accommodationInfo", accommodationInfoArr);
            responseBodyJSON.put("product", "ACCO");
            responseBodyJSON.put("type", "request");
            responseBodyJSON.put("paymentInfo", getPaymentInfo(createBookingJSON));
            responseBodyJSON.put("bookID", bookID);
//            responseBodyJSON.put("bookingType","offline");

        } catch (OperationException oe) {
            logger.error("Exception occurred in getBookRequestBody method : " + oe.getErrors());
            throw oe;
        } catch (Exception e) {
            logger.error("Exception occurred in getBookRequestBody method : " + e.getMessage());
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred in getBookRequestBody method : " + e.getMessage()));
            throw new OperationException(entity);
        }
        logger.debug("Inside getBookRequestBody method:End");
        return responseBodyJSON;
    }

    private JSONArray getPaymentInfo(JSONObject createBookingJSON) throws OperationException {
        JSONArray paymentArr = new JSONArray();
        JSONObject paymentJson = new JSONObject();
        if (createBookingJSON.optJSONObject("paymentDetails") != null) {
            JSONObject paymentDtls = createBookingJSON.getJSONObject("paymentDetails");
            if (paymentDtls.optBoolean("isPaymentPending") == false) {
                String paymentType = paymentDtls.optString("paymentType");
                if (!MDMDataUtils.isStringNotNullAndNotEmpty(paymentType))
                    throw new OperationException("please provide payment type.");
                paymentJson.put("paymentMethod", paymentType);
                switch (paymentType) {
                    case "Credit/DebitCard": {
                        JSONObject cardDetails = paymentDtls.optJSONObject("creditCardPaymentDetails");
                        if (cardDetails != null) {
                            paymentJson.put("cardType", cardDetails.optString("cardType"));
                        }
                        break;
                    }
                    case "WireTransfer":
                        break;
                    case "WireTransfer/NEFT":
                        break;
                    case "Cash":
                        break;

                }
                paymentJson.put("paymentType", "Full");
                paymentJson.put("paymentAmount", paymentDtls.optBigDecimal("amount", BigDecimal.ZERO));
                paymentJson.put("amountCurrency", paymentDtls.optString("currency"));

                paymentArr.put(paymentJson);
            } else {
                paymentArr.put(paymentJson);
            }

        } else {
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Please provide payment details"));
            throw new OperationException(entity);
        }

        return paymentArr;
    }

    private String getCityCode(String pCityName) throws OperationException {
        return dataUtils.getCityInfo(pCityName).get("cityCode").toString();
    }

    private BigDecimal getROE(JSONObject jsObjRoomStay) throws JsonProcessingException, OperationException {
        String supplierCurrency = jsObjRoomStay.getJSONObject("supplierTotalPriceInfo").getString("currencyCode");
        String clientCurrency = jsObjRoomStay.getJSONObject("totalPriceInfo").getString("currencyCode");
        return dataUtils.getRateOfExchange(supplierCurrency, clientCurrency);
    }

    private JSONArray getRoomConfig(JSONObject pJsObjRoomStay) throws OperationException {
        JSONArray arrRoomConfig = new JSONArray();
        logger.debug("Inside getRoomConfig method:Start");
        try {
            JSONObject objRoomConfig = new JSONObject();
            objRoomConfig.put("clientCommercialsEntityDetails", pJsObjRoomStay.getJSONArray("clientEntityCommercials"));
            objRoomConfig.put("supplierTotalPriceInfo", pJsObjRoomStay.getJSONObject("supplierTotalPriceInfo"));
            objRoomConfig.put("paxInfo", pJsObjRoomStay.getJSONArray("paxInfo"));
            objRoomConfig.put("policies", pJsObjRoomStay.getJSONArray("policies"));
            objRoomConfig.put("totalPriceInfo", pJsObjRoomStay.getJSONObject("totalPriceInfo"));
            objRoomConfig.put("supplierCommercials", pJsObjRoomStay.getJSONArray("supplierCommercials"));
            objRoomConfig.put("occupancyInfo", pJsObjRoomStay.getJSONArray("occupancyInfo"));
            objRoomConfig.put("roomInfo", pJsObjRoomStay.getJSONObject("roomInfo"));
            arrRoomConfig.put(objRoomConfig);

        } catch (Exception e) {
            logger.error("Exception occurred in getRoomConfig method : " + e.getMessage());
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred in getRoomConfig method : " + e.getMessage()));
            throw new OperationException(entity);
        }
        logger.debug("Inside getRoomConfig method:End");
        return arrRoomConfig;
    }

    private JSONObject getTotalRoomInfo(JSONObject pJsObjRoomStay) throws OperationException {

        JSONObject objTotalRoomInfo = new JSONObject();
        logger.debug("Inside getRoomConfig method:Start");
        try {
            /*SuppplierBookingPriceInfo*/
            BigDecimal supplierTotalPriceInfoAmt = new BigDecimal(0);
            BigDecimal supplierTotalPriceInfoTaxesAmt = new BigDecimal(0);
            String strSupplierTotalPriceInfoCurrency = "";
            String strSupplierTotalPriceInfoTaxesCurrency = "";

            /*BookingPriceInfo*/
            BigDecimal bookingPriceInfoAmt = new BigDecimal(0);
            BigDecimal bookingPriceInfoTaxesAmt = new BigDecimal(0);
            String strBookingPriceInfoCurrency = "";
            String strBookingPriceInfoTaxesCurrency = "";

            /*SuppplierBookingPriceInfo*/
            JSONObject objSupplierTotalPricInfo = pJsObjRoomStay.optJSONObject("supplierTotalPriceInfo");
            supplierTotalPriceInfoAmt = supplierTotalPriceInfoAmt.add(objSupplierTotalPricInfo.getBigDecimal("amount"));
            supplierTotalPriceInfoTaxesAmt = supplierTotalPriceInfoTaxesAmt.add(objSupplierTotalPricInfo.optJSONObject("taxes").getBigDecimal("amount"));
            strSupplierTotalPriceInfoCurrency = objSupplierTotalPricInfo.getString("currencyCode");
            strSupplierTotalPriceInfoTaxesCurrency = objSupplierTotalPricInfo.optJSONObject("taxes").getString("currencyCode");

            JSONObject objSupplierCommercialsTotals = new JSONObject();
            JSONObject objTaxes = new JSONObject();
            objTaxes.put("amount", supplierTotalPriceInfoTaxesAmt);
            objTaxes.put("currencyCode", strSupplierTotalPriceInfoTaxesCurrency);
            objSupplierCommercialsTotals.put("amount", supplierTotalPriceInfoAmt);
            objSupplierCommercialsTotals.put("taxes", objTaxes);
            objSupplierCommercialsTotals.put("currencyCode", strSupplierTotalPriceInfoCurrency);

            /*BookingPriceInfo*/
            JSONObject objbookingPriceInfo = pJsObjRoomStay.optJSONObject("totalPriceInfo");
            bookingPriceInfoAmt = bookingPriceInfoAmt.add(objbookingPriceInfo.getBigDecimal("amount"));
            strBookingPriceInfoCurrency = objbookingPriceInfo.getString("currencyCode");
            bookingPriceInfoTaxesAmt = bookingPriceInfoTaxesAmt.add(objbookingPriceInfo.optJSONObject("taxes").getBigDecimal("amount"));
            strBookingPriceInfoTaxesCurrency = objbookingPriceInfo.optJSONObject("taxes").getString("currencyCode");

            JSONObject objTotalPriceInfo = new JSONObject();
            JSONObject objTotalPriceInfoTaxes = new JSONObject();
            objTotalPriceInfoTaxes.put("amount", bookingPriceInfoTaxesAmt);
            objTotalPriceInfoTaxes.put("currencyCode", strBookingPriceInfoTaxesCurrency);
            objTotalPriceInfo.put("amount", bookingPriceInfoAmt);
            objTotalPriceInfo.put("taxes", objTotalPriceInfoTaxes);
            objTotalPriceInfo.put("companyTaxes", objbookingPriceInfo.optJSONObject("companyTaxes"));
            objTotalPriceInfo.put("currencyCode", strBookingPriceInfoCurrency);
            objTotalRoomInfo.put("supplierBookingPriceInfo", objSupplierCommercialsTotals);
            objTotalRoomInfo.put("bookingPriceInfo", objTotalPriceInfo);
            objTotalRoomInfo.put("supplierCommercialsTotals", getCommercialsTotals(pJsObjRoomStay.optJSONArray("supplierCommercials")));
        } catch (OperationException oe) {
            logger.error("Exception occurred in getTotalRoomInfo method : " + oe.getErrors());
            throw oe;
        } catch (Exception e) {
            logger.error("Exception occurred in getTotalRoomInfo method : " + e.getMessage());
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred in getTotalRoomInfo method : " + e.getMessage()));
            throw new OperationException(entity);
        }
        logger.debug("Inside getTotalRoomInfo method:End");
        return objTotalRoomInfo;
    }

    /*This method is called for adding both Supplier and Client commercials as there element names are same*/
    private JSONArray getCommercialsTotals(JSONArray pJsArrCommercials) throws OperationException {
        JSONArray jsArrCommercialsTotals = new JSONArray();
        try {
            HashMap<String, BigDecimal> hsCommercials = new HashMap<>();

            for (int i = 0; i < pJsArrCommercials.length(); i++) {
                JSONObject pJsObjCommercials = pJsArrCommercials.getJSONObject(i);

                String strCommercialName = pJsObjCommercials.get("commercialName").toString();
                String strCommercialType = pJsObjCommercials.get("commercialType").toString();
                String strCommercialCurrency = pJsObjCommercials.get("commercialCurrency").toString();
                String strKey = String.format("%s#%s#%s", strCommercialName, strCommercialType, strCommercialCurrency);

                BigDecimal amt = pJsObjCommercials.getBigDecimal("commercialAmount");

                if (hsCommercials.containsKey(strKey)) {
                    BigDecimal oldAmt = hsCommercials.get(strCommercialName);
                    BigDecimal newAmt = oldAmt.add(amt);
                    hsCommercials.put(strCommercialName, newAmt);
                } else {
                    hsCommercials.put(strKey, amt);
                }
            }

            Set<Map.Entry<String, BigDecimal>> setCommercials = hsCommercials.entrySet();

            for (Map.Entry<String, BigDecimal> s : setCommercials) {
                String strKey = s.getKey();
                String commercialInfo[] = strKey.split("#");
                JSONObject jsObjCommercialsTotals = new JSONObject();
                jsObjCommercialsTotals.put("commercialCurrency", commercialInfo[2]);
                jsObjCommercialsTotals.put("commercialType", commercialInfo[1]);
                jsObjCommercialsTotals.put("commercialAmount", s.getValue());
                jsObjCommercialsTotals.put("commercialName", commercialInfo[0]);
                jsArrCommercialsTotals.put(jsObjCommercialsTotals);
            }
        } catch (Exception e) {
            logger.error("Exception occurred in getCommercialsTotals method : " + e.getMessage());
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred in getCommercialsTotals method : " + e.getMessage()));
            throw new OperationException(entity);
        }
        return jsArrCommercialsTotals;
    }

    private JSONArray getClientEntityTotalCommercials(JSONArray pJsArrClientEntityCommercials) throws OperationException {
        JSONArray jsArrSupplierCommercialsTotals = new JSONArray();
        try {
            HashMap<String, JSONArray> hsClientEntityCommercials = new HashMap<>();
            for (int i = 0; i < pJsArrClientEntityCommercials.length(); i++) {
                JSONObject pJsObjClientEntityCommercials = pJsArrClientEntityCommercials.getJSONObject(i);
                String strClientID = pJsObjClientEntityCommercials.optString("clientID");
                String strParentClientID = pJsObjClientEntityCommercials.optString("parentClientID");
                String strCommercialEntityType = pJsObjClientEntityCommercials.optString("commercialEntityType");
                String strEntityName = pJsObjClientEntityCommercials.optString("entityName");
                String strCommercialEntityID = pJsObjClientEntityCommercials.optString("commercialEntityID");
                String strKey = String.format("%s # %s # %s # %s # %s", strClientID, strParentClientID, strCommercialEntityType, strEntityName, strCommercialEntityID);

                hsClientEntityCommercials.put(strKey, pJsObjClientEntityCommercials.getJSONArray("clientCommercials"));
            }

            Set<Map.Entry<String, JSONArray>> setClientCommercails = hsClientEntityCommercials.entrySet();

            for (Map.Entry<String, JSONArray> entryClientCommercails : setClientCommercails) {

                String strKey = entryClientCommercails.getKey();
                String commercialInfo[] = strKey.split("#");
                JSONObject jsObjSupplierCommercialsTotals = new JSONObject();
                jsObjSupplierCommercialsTotals.put("clientID", commercialInfo[0]);
                jsObjSupplierCommercialsTotals.put("parentClientID", commercialInfo[1]);
                jsObjSupplierCommercialsTotals.put("commercialEntityType", commercialInfo[2]);
                jsObjSupplierCommercialsTotals.put("entityName", commercialInfo[3]);
                jsObjSupplierCommercialsTotals.put("clientCommercialsTotal", getCommercialsTotals(entryClientCommercails.getValue()));
                jsObjSupplierCommercialsTotals.put("commercialEntityID", commercialInfo[4]);
                jsArrSupplierCommercialsTotals.put(jsObjSupplierCommercialsTotals);
            }
        } catch (OperationException oe) {
            logger.error("Exception occurred in getCommercialsTotals method : " + oe.getErrors());
            throw oe;
        } catch (Exception e) {
            logger.error("Exception occurred in getCommercialsTotals method : " + e.getMessage());
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred in getCommercialsTotals method : " + e.getMessage()));
            throw new OperationException(entity);
        }
        return jsArrSupplierCommercialsTotals;
    }

    //----------------------------------------------------------------KAFKA RS starts here-------------------------------------------------------------------------------------
    @Override
    public JSONObject generateBookingInvoice(JSONObject applyCommRespJSON, JSONObject createBookingJSON, String bookID) throws OperationException {
        JSONObject kafkaRSJSON = new JSONObject();
        try {
            String productCategory = createBookingJSON.getJSONObject(Constants.JSON_PROP_PRODUCT_DETAILS).getString(Constants.JSON_PROP_PROD_CAT);
            kafkaRSJSON.put("responseHeader", applyCommRespJSON.getJSONObject(Constants.JSON_PROP_RESP_HEADER));

            kafkaRSJSON.put("responseBody", getInvoiceRequestBody(applyCommRespJSON, bookID));

        } catch (OperationException oe) {
            logger.error("Exception occurred in generateBookingInvoice method : " + oe.getErrors());
            throw oe;
        } catch (Exception e) {
            logger.error("Exception occurred in generateBookingInvoice method : " + e.getMessage());
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred in generateBookingInvoice method : " + e.getMessage()));
            throw new OperationException(entity);
        }
        return kafkaRSJSON;
    }

    @Override
    public String getSupplierName(JSONObject travelDetails) throws OperationException {
        String supplierName = "";
        try {
            JSONArray roomBreakUpArr = travelDetails.optJSONArray("roomBreakup");
            if (roomBreakUpArr != null) {
                JSONObject roomJson = roomBreakUpArr.optJSONObject(0);
                if (roomJson != null) {
                    if (roomJson.optJSONObject("supplierRates") != null) {
                        supplierName = roomJson.optJSONObject("supplierRates").optString("supplierName");
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception occurred in getSupplierName method : " + e.getMessage());
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred in getSupplierName method : " + e.getMessage()));
            throw new OperationException(entity);
        }
        return supplierName;
    }

    //TODO First room,First Adult as Lead pax as discussion
    public String getLeadPaxName(JSONObject travelDetails) throws OperationException {
        String leadPaxName = "";
        try {
            JSONArray roomBreakUpArr = travelDetails.optJSONArray("roomBreakup");
            if (roomBreakUpArr != null) {
                JSONObject roomJson = roomBreakUpArr.optJSONObject(0);
                if (roomJson != null) {
                    if (roomJson.optJSONArray("passengerDetails") != null) {
                        JSONArray jsArrPassenger =  getPassengerDetails(roomJson.optJSONArray("passengerDetails"));
                        List<HashMap> arrPass = (ArrayList) jsArrPassenger.toList();
                        leadPaxName = arrPass.stream().filter(isLead -> isLead.getOrDefault("isLeadPax","").toString().equals("true")).findFirst().get().getOrDefault("firstName","").toString();
                    }
                }
            }
        }catch (NoSuchElementException noElem){
            //code comes into this block when passenger details such as paxName, paxType is empty
            //so we allow
            return leadPaxName;
        }catch (Exception e) {
            logger.error("Exception occurred in getLeadPaxName method : " + e.getMessage());
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred in getLeadPaxName method : " + e.getMessage()));
            throw new OperationException(entity);
        }
        return leadPaxName;
    }

    private JSONObject getInvoiceRequestBody(JSONObject applyCommRespJSON, String bookID) throws OperationException {

        JSONObject jsObjResponse = new JSONObject();
        jsObjResponse.put("accommodationInfo", getBookingInvoiceAccoInfo(applyCommRespJSON));
        jsObjResponse.put("product", "ACCO");
        jsObjResponse.put("bookID", bookID);
        return jsObjResponse;
    }

    private JSONArray getBookingInvoiceAccoInfo(JSONObject applyCommRespJSON) throws OperationException {
        logger.debug("Inside getBookingInvoiceAccoInfo method:Start");
        JSONArray jsArrAccoInfo = new JSONArray();
        JSONArray jsArrRoomStay = applyCommRespJSON.getJSONObject("responseBody").getJSONArray("accommodationInfo").getJSONObject(0).getJSONArray("roomStay");
        try {
            for (int i = 0; i < jsArrRoomStay.length(); i++) {
                JSONObject jsObjRoomStay = jsArrRoomStay.getJSONObject(i);
                String supplierID = jsObjRoomStay.getString("supplierRef");

                JSONArray jsonArrSupplierRoomReferences = new JSONArray();
                JSONObject jsObjSupplierRoomReferences = new JSONObject();
                jsObjSupplierRoomReferences.put("roomStatus", "Confirmed"); //status changed from CONF to Confirmed - changed by nachiket while solving booking status issue
                jsObjSupplierRoomReferences.put("supplierRoomIndex", UUID.randomUUID().toString());//TODO need this details are provided by suppliers in Booking Engine
                jsObjSupplierRoomReferences.put("requestedRoomIndex", i + 1);
                jsonArrSupplierRoomReferences.put(jsObjSupplierRoomReferences);

                JSONObject jsObjAccoInfo = new JSONObject();
                jsObjAccoInfo.put("suppierReservationId", UUID.randomUUID().toString());//TODO need this details are provided by suppliers in Booking Engine
                jsObjAccoInfo.put("supplierRoomReferences", jsonArrSupplierRoomReferences);
                jsObjAccoInfo.put("supplierRef", supplierID);
                jsObjAccoInfo.put("supplierReferenceId", "");
                jsObjAccoInfo.put("clientReferenceId", "");
                jsObjAccoInfo.put("supplierCancellationId", "");
                jsObjAccoInfo.put("priority", new JSONObject());
                jsObjAccoInfo.put("status", "Confirmed");//status changed from CONF to Confirmed - changed by nachiket while solving booking status issue
                jsArrAccoInfo.put(jsObjAccoInfo);
            }
        } catch (Exception e) {
            logger.error("Exception occurred in getBookingInvoiceAccoInfo method : " + e.getMessage());
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred in getBookingInvoiceAccoInfo method : " + e.getMessage()));
            throw new OperationException(entity);
        }
        logger.debug("Inside getBookingInvoiceAccoInfo method:End");
        return jsArrAccoInfo;
    }

    //----------------------------------------------------------------Fetch Data for Supplier starts here-------------------------------------------------------------------------------------

    private boolean isValidRequest(JSONObject reqJson) {
        if (reqJson.optJSONObject("travelAndPassengerDetails") == null || reqJson.getJSONObject("travelAndPassengerDetails").optJSONArray("roomBreakup") == null
                || reqJson.optJSONObject("productDetails") == null || reqJson.getJSONObject("productDetails").optJSONObject("productName") == null) {
            return false;
        }
        return true;
    }

    private JSONObject getMDMResponseforSupplierData(JSONObject filters, JSONObject supplierRateJson) throws OperationException {
        JSONObject mdmSuppRes = null;
        try {
            String URL = suppRateAccoURL + objectMapper.writeValueAsString(filters);
            URI uri = UriComponentsBuilder.fromUriString(URL).build().encode().toUri();
            String result = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
            if (MDMDataUtils.isStringNotNullAndNotEmpty(result)) {
                mdmSuppRes = new JSONObject(result);
            }
        } catch (OperationException oe) {
            logger.error("Exception occurred while fetching  supplier rates from MDM" + oe.getErrors());
            throw oe;
        } catch (Exception e) {
            logger.error("Exception occurred while fetching  supplier rates from MDM");
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred while fetching  supplier rates from MDM"));
            throw new OperationException(entity);
        }

        if (mdmSuppRes == null || mdmSuppRes.optJSONArray("data").length() == 0) {
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Supplier rates not found for Supplier Id : %s in MDM", supplierRateJson.getString("supplierId")));
            throw new OperationException(entity);
        }
        return mdmSuppRes;
    }

    private void getTaxDetails(JSONObject defineRateJson, String taxId) throws OperationException {
        try {
            JSONObject tempJson = null;
            String accoSuppTaxesURL = suppRateAccoTaxesURL.concat(taxId);
            URI uri = UriComponentsBuilder.fromUriString(accoSuppTaxesURL).build().encode().toUri();
            String result = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
            if (MDMDataUtils.isStringNotNullAndNotEmpty(result)) {
                tempJson = new JSONObject(result);
                defineRateJson.put("taxDetails", tempJson);
            }
        } catch (OperationException oe) {
            logger.error("An exceptions occurred calculateCommercials" + oe.getErrors());
            throw oe;
        } catch (Exception e) {
            logger.error("Exception occurred while fetching  getTaxDetails from MDM" + e.getMessage());
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred while fetching  getTaxDetails from MDM"));
            throw new OperationException(entity);
        }
    }

    private void getSurchargeAndSupplementDetails(JSONObject addDefineJson, String surchargeAndSuppId) throws OperationException {
        try {
            if (MDMDataUtils.isStringNotNullAndNotEmpty(surchargeAndSuppId)) {
                JSONObject tempJson = null;
                String surchargeDefAccoURl = surChargeAndSuppDefAccoURL.concat(surchargeAndSuppId);
                URI uri = UriComponentsBuilder.fromUriString(surchargeDefAccoURl).build().encode().toUri();
                String result = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
                if (MDMDataUtils.isStringNotNullAndNotEmpty(result)) {
                    tempJson = new JSONObject(result);
                    addDefineJson.put("surchargeAndSupplementDetails", tempJson);
                    addDefineJson.remove("surchargeAndSupplementId");
                }
            }
        } catch (OperationException oe) {
            logger.error("Exception occurred while fetching  Surcharge And SupplementDetails from MDM" + oe.getErrors());
            throw oe;
        } catch (Exception e) {
            logger.error("Exception occurred while fetching  Surcharge And SupplementDetails from MDM");
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred while fetching  Surcharge And SupplementDetails from MDM"));
            throw new OperationException(entity);
        }
    }

    private void getAmendmentDetails(JSONObject addDefineJson, String amendPolicyId) throws OperationException {
        try {
            if (MDMDataUtils.isStringNotNullAndNotEmpty(amendPolicyId)) {
                JSONObject resJson = null;
                JSONObject props = new JSONObject();
                props.put("policyInformation.policyId", amendPolicyId);
                String URL = amendAndCancelPolicyURL + objectMapper.writeValueAsString(props);
                URI uri = UriComponentsBuilder.fromUriString(URL).build().encode().toUri();
                String result = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
                if (MDMDataUtils.isStringNotNullAndNotEmpty(result)) {
                    resJson = new JSONObject(result);
                    addDefineJson.put("amendmentPolicyDetails", resJson);
                }
            }
        } catch (OperationException oe) {
            logger.error("Exception occurred while fetching  Amendment policy from MDM" + oe.getErrors());
            throw oe;
        } catch (Exception e) {
            logger.error("Exception occurred while fetching  Amendment policy from MDM");
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred while fetching  Amendment policy from MDM"));
            throw new OperationException(entity);
        }
    }

    private void getCancellationDetails(JSONObject addDefineJson, String cancellationPolicyId) throws OperationException {
        try {
            if (MDMDataUtils.isStringNotNullAndNotEmpty(cancellationPolicyId)) {
                JSONObject resJson = null;

                JSONObject props = new JSONObject();

                props.put("policyInformation.policyId", cancellationPolicyId);
                String URL = amendAndCancelPolicyURL + objectMapper.writeValueAsString(props);
                URI uri = UriComponentsBuilder.fromUriString(URL).build().encode().toUri();
                String result = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
                if (MDMDataUtils.isStringNotNullAndNotEmpty(result)) {
                    resJson = new JSONObject(result);
                    addDefineJson.put("cancellationPolicyDetails", resJson);
                }
            }
        } catch (OperationException oe) {
            logger.error("Exception while fetching cancellation policy details from MDM" + oe.getErrors());
            throw oe;
        } catch (Exception e) {
            logger.error("Exception while fetching cancellation policy details from MDM");
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception while fetching cancellation policy details from MDM"));
            throw new OperationException(entity);
        }
    }

    private void getChildRecordScreen1(StringBuilder matchKeyBldr, JSONArray chldScreenJsonArr, JSONObject travelAndPaxDetails, JSONObject roomBreakupJson, JSONArray tempArr) throws OperationException {
        try {
            Boolean flag = false;
            String matchChldKey = "";

            for (int l = 0; l < chldScreenJsonArr.length(); l++) {
                StringBuilder chldkeyBldr = new StringBuilder();
                JSONObject chldJson = chldScreenJsonArr.getJSONObject(l);
                String chldKey = createChldScreenKeyFromRQ(travelAndPaxDetails, roomBreakupJson);
//                if (chldJson.optJSONArray("daysOfTheWeek") != null) {
//                    if (MDMDataUtils.isStringNotNullAndNotEmpty(travelAndPaxDetails.optString("checkInDate"))) {
//                        for (int m = 0; m < chldJson.getJSONArray("daysOfTheWeek").length(); m++) {
//                            String day = chldJson.getJSONArray("daysOfTheWeek").getString(m);
//                            if (day.equalsIgnoreCase(getDay(travelAndPaxDetails.optString("checkInDate"))))
//                                chldkeyBldr.append(day);
//                        }
//                    }
//                    if (MDMDataUtils.isStringNotNullAndNotEmpty(travelAndPaxDetails.optString("checkOutDate"))) {
//                        for (int m = 0; m < chldJson.getJSONArray("daysOfTheWeek").length(); m++) {
//                            String day = chldJson.getJSONArray("daysOfTheWeek").getString(m);
//                            if (day.equalsIgnoreCase(getDay(travelAndPaxDetails.optString("checkOutDate"))))
//                                chldkeyBldr.append(day);
//                        }
//                    }
//                }
                List<String> requestedDays = getDaysInRange(travelAndPaxDetails.optString("checkInDate"), travelAndPaxDetails.optString("checkOutDate"));
                JSONArray daysOfWeekJsonArr = chldJson.optJSONArray("daysOfTheWeek");
                if (daysOfWeekJsonArr != null && daysOfWeekJsonArr.length() > 0) {
                    if (isDayValidForReq(requestedDays, daysOfWeekJsonArr) == false)
                        continue;
                }

                if (MDMDataUtils.isStringNotNullAndNotEmpty(travelAndPaxDetails.optString("checkInDate")) && MDMDataUtils.isStringNotNullAndNotEmpty(travelAndPaxDetails.optString("checkOutDate"))) {
                    if (travelAndPaxDetails.optInt("numberOfNights") >= 0) {
                        int numOfNights = Integer.parseInt(travelAndPaxDetails.optString("numberOfNights"));
                        if (numOfNights >= chldJson.optInt("minimumNumberOfNights")) {
                            chldkeyBldr.append(String.valueOf(travelAndPaxDetails.optInt("numberOfNights")));
                        } else {
                            throw new OperationException(String.format(" please select the number of nights more than  %s", chldJson.getInt("minimumNumberOfNights")));
                        }
                    }
                }

                if (MDMDataUtils.isStringNotNullAndNotEmpty(travelAndPaxDetails.optString("mealPlan"))) {
                    if (travelAndPaxDetails.optString("mealPlan").equalsIgnoreCase(chldJson.optString("mealPlan"))) {
                        chldkeyBldr.append(chldJson.getString("mealPlan").trim());
                    }
                }

                if (MDMDataUtils.isStringNotNullAndNotEmpty(roomBreakupJson.optString("roomCategory"))) {
                    chldkeyBldr.append(chldJson.optString("roomCategory").trim());
                }

                if (MDMDataUtils.isStringNotNullAndNotEmpty(roomBreakupJson.optString("roomType"))) {
                    JSONArray roomTypeDetailsArr = chldJson.optJSONArray("roomTypeDetails");
                    for (int m = 0; roomTypeDetailsArr != null && m < roomTypeDetailsArr.length(); m++) {
                        JSONObject roomJson = roomTypeDetailsArr.getJSONObject(m);
                        if (roomBreakupJson.optString("roomType").equalsIgnoreCase(roomJson.optString("roomType"))) {
                            chldkeyBldr.append(roomBreakupJson.optString("roomType").trim());
                        }
                    }
                }

                //added criteria - paxWithExtraBed and childWithNoBed
                JSONArray paxDetails = roomBreakupJson.optJSONArray("passengerDetails");
                List<Integer> ageListForChildNoBedDetails = new ArrayList<>();
                List<Integer> ageListForPaxWithExtraBedDetails = new ArrayList<>();
                List<String> paxTypeListForPaxWithExtraBedDetails = new ArrayList<>();
                if (paxDetails != null) {
                    for (int k = 0; k < paxDetails.length(); k++) {
                        JSONObject paxJson = paxDetails.getJSONObject(k);
                        if (paxJson.optBoolean("childWithoutExtraBed")) {
                            String dob = paxJson.optString("dateOfBirth");
                            ageListForChildNoBedDetails.add(MDMDataUtils.calculateAge(dob));
                        }
                        if (paxJson.optBoolean("paxWithExtraBed")) {
                            String dob = paxJson.optString("dateOfBirth");
                            ageListForPaxWithExtraBedDetails.add(MDMDataUtils.calculateAge(dob));
                            paxTypeListForPaxWithExtraBedDetails.add(paxJson.getString("passengerType"));
                        }
                    }
                }
                JSONArray childNoBedDetailsDetailsArr = chldJson.optJSONArray("childNoBedDetails");
                if (childNoBedDetailsDetailsArr != null && ageListForChildNoBedDetails.size() > 0) {
                    if (checkAvailabilirtyWithoutExtraBed(ageListForChildNoBedDetails, childNoBedDetailsDetailsArr) == false) {
                        continue;
                    }
                }

                JSONArray paxWithExtraBedDetailsArr = chldJson.optJSONArray("paxWithExtraBedDetails");
                if (paxWithExtraBedDetailsArr != null && ageListForPaxWithExtraBedDetails.size() > 0) {
                    if (checkAvailabilirtyPaxWithExtraBed(paxTypeListForPaxWithExtraBedDetails, ageListForPaxWithExtraBedDetails, paxWithExtraBedDetailsArr) == false) {
                        continue;
                    }
                }

                if (chldKey.equalsIgnoreCase(chldkeyBldr.toString())) {
                    if (chldJson.optJSONObject("advanceDefinition") != null) {
                        JSONObject advanceDefJson = chldJson.getJSONObject("advanceDefinition");
                        if (advanceDefJson.optString("advanceDefinitionId").isEmpty()) {
                            throw new OperationException("advanceDefinitionId is required");
                        } else {
                            JSONObject tempJson = null;
                            String advanceDefinitionId = advanceDefJson.getString("advanceDefinitionId");
                            String accoSuppRateURL = advanceDefAccoURL.concat(advanceDefinitionId);
                            URI uri = UriComponentsBuilder.fromUriString(accoSuppRateURL).build().encode().toUri();
                            String result = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
                            if (MDMDataUtils.isStringNotNullAndNotEmpty(result)) {
                                tempJson = new JSONObject(result);
                                advanceDefJson.put("advanceDefinationDetails", tempJson);
                            }
                        }
                    }

                    if (chldJson.optJSONObject("attachedOffers") != null) {
                        JSONObject offers = chldJson.getJSONObject("attachedOffers");
                        JSONArray offerIds = offers.optJSONArray("offerId");
                        if (offerIds != null && offerIds.length() > 0) {
                            JSONObject latestJson = null;
                            JSONObject filters = new JSONObject();
                            JSONObject _id = new JSONObject();
                            _id.put("$in", offerIds);
                            filters.put("_id", _id);
                            String URL = offersURL + objectMapper.writeValueAsString(filters);
                            URI uri = UriComponentsBuilder.fromUriString(URL).build().encode().toUri();
                            String result = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
                            if (MDMDataUtils.isStringNotNullAndNotEmpty(result)) {
                                JSONObject mdmRs = new JSONObject(result);
                                if (mdmRs.optJSONArray("data") != null)
                                    offers.put("offerDetails", mdmRs.getJSONArray("data"));
                            }
                        }
                    }
                    tempArr.put(chldJson);
                    matchChldKey = chldkeyBldr.toString();
                }
                if (flag == false) {
                    matchKeyBldr.append(matchChldKey);
                    flag = true;
                }
            }
        } catch (OperationException oe) {
            logger.error("Exception while fetching  ChildRecordScreen1" + oe.getErrors());
            throw oe;
        } catch (Exception e) {
            logger.error("Exception while fetching  ChildRecordScreen1");
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception while fetching  ChildRecordScreen1"));
            throw new OperationException(entity);
        }
    }

    private boolean isDayValidForReq(List<String> requestedDays, JSONArray daysOfWeekJsonArr) {
        int matchCount = 0;
        for (int i = 0; i < daysOfWeekJsonArr.length(); i++) {
            String dayMustInclude = daysOfWeekJsonArr.getString(i);
            for (String day : requestedDays) {
                if (day.equalsIgnoreCase(dayMustInclude)) {
                    matchCount++;
                }
            }
        }
        if (matchCount == daysOfWeekJsonArr.length())
            return true;
        else
            return false;


    }

    private List<String> getDaysInRange(String start, String end) {
        List<String> daysInRange = new ArrayList<>();
        try {


            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
            Date startDate = df.parse(start);
            Date endDate = df.parse(end);

            Calendar calendar = new GregorianCalendar();
            calendar.setTime(startDate);

            Calendar endCalendar = new GregorianCalendar();
            endCalendar.setTime(endDate);
            endCalendar.add(Calendar.DAY_OF_MONTH, 1);// this one is added for inclusion of both (start and end) days

            while (calendar.before(endCalendar)) {
                Date result = calendar.getTime();
                daysInRange.add(getDay(df.format(result)));
                calendar.add(Calendar.DATE, 1);
            }
            return daysInRange;
        } catch (Exception e) {
            logger.info("Exception while fetching days betweenn dates");
            return daysInRange;
        }

    }

    private boolean checkAvailabilirtyWithoutExtraBed(List<Integer> ageList, JSONArray paxJsonArr) {
        int matchCount = 0;
        for (int i = 0; i < paxJsonArr.length(); i++) {
            JSONObject paxJson = paxJsonArr.getJSONObject(i);
            for (int age : ageList) {
                if (age >= paxJson.getInt("ageFrom") && age <= paxJson.getInt("ageTo")) {
                    matchCount++;
                }
            }
        }
        if (matchCount == ageList.size()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkAvailabilirtyPaxWithExtraBed(List<String> paxTypeListForPaxWithExtraBedDetails, List<Integer> ageList, JSONArray paxJsonArr) {
        boolean adultFlag = false;
        boolean childFlag = false;
        boolean adultExist = false;
        boolean childExist = false;
        for (String paxType : paxTypeListForPaxWithExtraBedDetails) {
            if (paxType.equalsIgnoreCase("Adult")) {
                adultExist = true;
            } else {
                childExist = true;
            }
        }
        JSONArray childWithExtraBedArr = new JSONArray();
        for (int i = 0; i < paxJsonArr.length(); i++) {
            JSONObject paxJson = paxJsonArr.getJSONObject(i);
            if (paxJson.optString("paxType").equalsIgnoreCase("Adult")) {
                continue;
            } else {
                childWithExtraBedArr.put(paxJson);
            }
        }
        for (String paxType : paxTypeListForPaxWithExtraBedDetails) {
            if (paxType.equalsIgnoreCase("Adult")) {
                for (int i = 0; i < paxJsonArr.length(); i++) {
                    JSONObject paxJson = paxJsonArr.getJSONObject(i);
                    if (paxType.equalsIgnoreCase(paxJson.optString("paxType"))) {
                        adultFlag = true;
                    }
                }
            } else {
                childFlag = checkAvailabilirtyWithoutExtraBed(ageList, childWithExtraBedArr);
            }
        }
        if (adultExist && childExist) {
            if (adultFlag && childFlag) {
                return true;
            } else {
                return false;
            }
        } else if (childExist && !adultExist) {
            if (childFlag)
                return true;
            else
                return false;
        } else if (adultExist && !childExist) {
            if (adultFlag)
                return true;
            else
                return false;
        } else
            return false;

    }

    private void getPaxOccupancyDetails(JSONObject roomBreakupJson, StringBuilder matchKeyBldr, JSONArray tempArr, JSONObject paxOccupancyJson) throws OperationException {
        try {
            String matchPaxKey = " ";
            String paxKey = createPaxOccupancyKeyFromRQ(roomBreakupJson);
            for (int k = 0; paxOccupancyJson.optJSONArray("type1") != null && k < paxOccupancyJson.optJSONArray("type1").length(); k++) {
                StringBuilder paxBldr = new StringBuilder();
                JSONObject paxJson = paxOccupancyJson.getJSONArray("type1").optJSONObject(k);

                if (MDMDataUtils.isStringNotNullAndNotEmpty(roomBreakupJson.optString("roomCategory"))) {
                    paxBldr.append(roomBreakupJson.optString("roomCategory").trim());
                }
                if (MDMDataUtils.isStringNotNullAndNotEmpty(roomBreakupJson.optString("roomType"))) {
                    paxBldr.append(roomBreakupJson.optString("roomType").trim());
                }
                if (roomBreakupJson.optInt("maximumNumberOfPax") > 0) {
                    if (roomBreakupJson.optInt("maximumNumberOfPax") <= paxJson.getInt("maxPax")) {
                        paxBldr.append(roomBreakupJson.optInt("maximumNumberOfPax"));
                    }
                }
                if (paxKey.equalsIgnoreCase(paxBldr.toString())) {
                    tempArr.put(paxJson);
                    matchPaxKey = paxBldr.toString();
                }
            }
            matchKeyBldr.append(matchPaxKey);
        } catch (Exception e) {
            logger.error("Exception while populating PaxOccupancyDetails ");
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception while populating PaxOccupancyDetails "));
            throw new OperationException(entity);
        }
    }

    public JSONObject fetchDataFromMDM(JSONObject reqJson) throws OperationException {
        if (isValidRequest(reqJson) == false) {
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Incorrect Request ,Check [JSONObjects] travelAndPassengerDetails , productDetails or roomBreakup"));
            throw new OperationException(entity);
        }
        JSONObject clientDtls = reqJson.getJSONObject("clientDetails");
        String market = clientDtls.getString("companyMarket");

        JSONObject clientTypeJson = getPOSAndClientLang(market, clientDtls.getString("clientType"));
        if (clientTypeJson != null && clientTypeJson.optJSONObject(Constants.MDM_PROP_CLIENTSTRUCT) != null) {
            JSONArray transCurrJsonArr = clientTypeJson.getJSONObject(Constants.MDM_PROP_CLIENTSTRUCT).optJSONArray(Constants.MDM_PROP_TRANS_CCY);
            if (transCurrJsonArr != null) {
                for (int i = 0; i < transCurrJsonArr.length(); i++) {
                    JSONObject currencyJson = transCurrJsonArr.getJSONObject(i);
                    if (market.equalsIgnoreCase(currencyJson.optString(Constants.JSON_PROP_MKT))) {
                        if (currencyJson.optString(Constants.JSON_PROP_CCY).isEmpty()) {
                            clientDtls.put("clientCurrency", "");
                        } else {
                            clientDtls.put("clientCurrency", currencyJson.optString(Constants.JSON_PROP_CCY));
                        }
                    }
                }
            }
        }
        JSONObject travelAndPaxDetails = reqJson.getJSONObject("travelAndPassengerDetails");
        JSONObject productDetails = reqJson.getJSONObject("productDetails");
        JSONArray rommBreakUpJsonArr = travelAndPaxDetails.optJSONArray("roomBreakup");

        for (int i = 0; i < rommBreakUpJsonArr.length(); i++) {
            JSONObject roomBreakupJson = rommBreakUpJsonArr.getJSONObject(i);

            if (roomBreakupJson.optBoolean("isRoomSelected") == true) {
                String reqKey = createKeyFromReq(productDetails, travelAndPaxDetails, roomBreakupJson);

                if (roomBreakupJson.optJSONObject("supplierRates") != null) {
                    JSONObject resultJson = null;
                    JSONObject supplierRateJson = roomBreakupJson.getJSONObject("supplierRates");

                    if (MDMDataUtils.isStringNotNullAndNotEmpty(supplierRateJson.optString("supplierId"))) {
                        JSONObject filters = new JSONObject();
                        filters.put("supplierId", supplierRateJson.getString("supplierId"));

                        //Load supplier data based on supplier id
                        JSONObject mdmSuppRes = getMDMResponseforSupplierData(filters, supplierRateJson);

                        JSONArray supplierRateJsonArr = mdmSuppRes.optJSONArray("data");

                        if (supplierRateJsonArr != null) {
                            for (int j = 0; j < supplierRateJsonArr.length(); j++) {

                                StringBuilder matchKeyBldr = new StringBuilder();
                                JSONObject supplierRate = supplierRateJsonArr.getJSONObject(j);

                                if (MDMDataUtils.isStringNotNullAndNotEmpty(productDetails.optString("productSubCategory"))) {
                                    matchKeyBldr.append(supplierRate.optString("productCategorySubType"));
                                }

                                if (MDMDataUtils.isStringNotNullAndNotEmpty(productDetails.getJSONObject("productName").optString("name"))) {
                                    matchKeyBldr.append(supplierRate.optString("productName").trim());
                                } else {
                                    Map<String, String> entity = new HashMap<>();
                                    entity.put("message", String.format("Product name not provided"));
                                    throw new OperationException(entity);
                                }

                                if (supplierRate.optJSONObject("defineRates") != null) {
                                    JSONObject defineRateJson = supplierRate.getJSONObject("defineRates");
                                    if (MDMDataUtils.isStringNotNullAndNotEmpty(defineRateJson.optString("taxId"))) {
                                        String taxId = defineRateJson.getString("taxId");
                                        //Add TaxDetails
                                        getTaxDetails(defineRateJson, taxId);
                                    }

                                    JSONArray addDefineRatesArr = supplierRate.getJSONObject("defineRates").optJSONArray("addDefineRates");
                                    if (addDefineRatesArr != null) {
                                        for (int k = 0; k < addDefineRatesArr.length(); k++) {
                                            JSONObject addDefineJson = addDefineRatesArr.getJSONObject(k);

                                            if (addDefineJson.optJSONArray("childRecordScreen1") != null) {
                                                JSONArray chldScreenJsonArr = addDefineJson.getJSONArray("childRecordScreen1");
                                                JSONArray tempArr = new JSONArray();

                                                //tempArr will get initialise here
                                                getChildRecordScreen1(matchKeyBldr, chldScreenJsonArr, travelAndPaxDetails, roomBreakupJson, tempArr);

                                                if (tempArr != null)
                                                    addDefineJson.put("childRecordScreen1", tempArr);
                                                else
                                                    throw new OperationException("Supplier rates not found.");
                                            }

                                            String surchargeAndSuppId = addDefineJson.optString("surchargeAndSupplementId");
                                            //Add surcharge and supplementid
                                            getSurchargeAndSupplementDetails(addDefineJson, surchargeAndSuppId);

                                            String amendPolicyId = addDefineJson.optString("amendmentPolicyId");
                                            //Add amendment details
                                            getAmendmentDetails(addDefineJson, amendPolicyId);

                                            String cancellationPolicyId = addDefineJson.optString("cancellationPolicyId");
                                            //Add cancellation details
                                            getCancellationDetails(addDefineJson, cancellationPolicyId);

                                            String paymentDetailsId = addDefineJson.optString("paymentDetailsId");
                                            getPaymentDetails(addDefineJson, paymentDetailsId);
                                        }
                                    }
                                }

                                if (supplierRate.optJSONObject("accoData") != null) {
                                    if (supplierRate.optJSONObject("accoData").optJSONObject("accomodationInfo") != null) {
                                        StringBuilder accoDataBldr = new StringBuilder();
                                        String accoKey = createAccoInfoKey(productDetails, travelAndPaxDetails, supplierRate.optJSONObject("accoData").optJSONObject("accomodationInfo"));
                                        JSONObject acooInfo = supplierRate.getJSONObject("accoData").getJSONObject("accomodationInfo");
                                        JSONObject address = acooInfo.optJSONObject("address");
                                        if (address != null) {
                                            if (MDMDataUtils.isStringNotNullAndNotEmpty(productDetails.optString("country")))
                                                accoDataBldr.append(address.optString("country").trim());
                                            if (MDMDataUtils.isStringNotNullAndNotEmpty(productDetails.optString("city")))
                                                accoDataBldr.append(address.optString("city").trim());
                                        }
                                        if(acooInfo.optInt("noOfRooms")>0 && travelAndPaxDetails.optInt("totalNumberOfRooms")>0){
                                            if (travelAndPaxDetails.optInt("totalNumberOfRooms") >= acooInfo.optInt("noOfRooms")) {
                                                continue;
                                            }
                                        }


                                        if (!accoKey.equalsIgnoreCase(accoDataBldr.toString()))
                                            continue;
                                        else
                                            matchKeyBldr.append(accoDataBldr.toString());

                                        //Add pax occupancy details
                                        JSONArray tempArr = new JSONArray();
                                        JSONObject paxOccupancyJson = supplierRate.optJSONObject("accoData").optJSONObject("passengerOccupancy");
                                        getPaxOccupancyDetails(roomBreakupJson, matchKeyBldr, tempArr, paxOccupancyJson);

                                        if (tempArr != null) {
                                            paxOccupancyJson.put("type1", tempArr);
                                        } else {
                                            continue;
                                        }
                                    }
                                }

                                if (reqKey.equalsIgnoreCase(matchKeyBldr.toString().trim())) {
                                    resultJson = new JSONObject(supplierRate.toString());
                                    supplierRateJson.put("supplierRateDetails", resultJson);
                                }
                            }
                        }
                    } else {
                        JSONObject todoResJson = manualOfflineBookingService.createTODO(reqJson);
                        return todoResJson;
                    }
                } else {
                    Map<String, String> entity = new HashMap<>();
                    entity.put("message", String.format("Incorrect Request [JSONObject] Supplier Rates not found"));
                    throw new OperationException(entity);
                }
            }
        }
        return reqJson;
    }

    private void getPaymentDetails(JSONObject addDefineJson, String paymentDetailsId) throws OperationException {
        try {
            if (MDMDataUtils.isStringNotNullAndNotEmpty(paymentDetailsId)) {
                String URL = paymentDetailsURL.concat(paymentDetailsId);
                URI uri = UriComponentsBuilder.fromUriString(URL).build().encode().toUri();
                String result = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
                JSONObject resJson = null;
                if (MDMDataUtils.isStringNotNullAndNotEmpty(result)) {
                    resJson = new JSONObject(result);
                    addDefineJson.put("paymentDetails", resJson);
                }
            }
        } catch (OperationException oe) {
            logger.error("Exception while fetching payment details from MDM" + oe.getErrors());
            throw oe;
        } catch (Exception e) {
            logger.error("Exception while fetching payment details from MDM");
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception while fetching payment details from MDM"));
            throw new OperationException(entity);
        }
    }

    public JSONObject getPOSAndClientLang(String market, String clientType) throws OperationException {
        try {
            JSONObject filters = new JSONObject();
            filters.put("deleted", false);
            filters.put(Constants.MDM_PROP_CLIENTSTRUCT.concat(".").concat(Constants.MDM_PROP_CLIENTENTITYTYPE), clientType);
            filters.put(Constants.MDM_PROP_CLIENTSTRUCT.concat(".").concat(Constants.MDM_PROP_CLIENTMARKET), market);
            String URL = clientTypeURL + objectMapper.writeValueAsString(filters);
            URI uri = UriComponentsBuilder.fromUriString(URL).build().encode().toUri();
            String result = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
            JSONObject latestJson = null;
            if (MDMDataUtils.isStringNotNullAndNotEmpty(result)) {
                latestJson = MDMDataUtils.getLatestUpdatedJson(new JSONObject(result));
            }
            return latestJson;
        } catch (Exception e) {
            logger.info("Exception occured while fetching POS and client language");
        }
        return null;
    }

    private String createKeyFromReq(JSONObject productDetails, JSONObject travelAndPaxDetails, JSONObject roomBreakupJson) {
        StringBuilder sb = new StringBuilder();
        if (MDMDataUtils.isStringNotNullAndNotEmpty(productDetails.optString("productSubCategory"))) {
            sb.append(productDetails.optString("productSubCategory"));
        }

        if (productDetails.optJSONObject("productName") != null) {
            if (MDMDataUtils.isStringNotNullAndNotEmpty(productDetails.getJSONObject("productName").optString("name"))) {
                sb.append(productDetails.getJSONObject("productName").optString("name").trim());
            }
        }

//        if (MDMDataUtils.isStringNotNullAndNotEmpty(travelAndPaxDetails.optString("checkInDate"))) {
//            sb.append(getDay(travelAndPaxDetails.optString("checkInDate")));
//        }
//
//        if (MDMDataUtils.isStringNotNullAndNotEmpty(travelAndPaxDetails.optString("checkOutDate"))) {
//            sb.append(getDay(travelAndPaxDetails.optString("checkOutDate")));
//        }

        if (MDMDataUtils.isStringNotNullAndNotEmpty(travelAndPaxDetails.optString("checkInDate")) &&
                MDMDataUtils.isStringNotNullAndNotEmpty(travelAndPaxDetails.optString("checkOutDate"))) {
            if (travelAndPaxDetails.optInt("numberOfNights") >= 0)
                sb.append(String.valueOf(travelAndPaxDetails.optInt("numberOfNights")));
        }

        if (MDMDataUtils.isStringNotNullAndNotEmpty(travelAndPaxDetails.optString("mealPlan"))) {
            sb.append(travelAndPaxDetails.optString("mealPlan").trim());
        }

        if (MDMDataUtils.isStringNotNullAndNotEmpty(roomBreakupJson.optString("roomCategory"))) {
            sb.append(roomBreakupJson.optString("roomCategory").trim());
        }

        if (MDMDataUtils.isStringNotNullAndNotEmpty(roomBreakupJson.optString("roomType"))) {
            sb.append(roomBreakupJson.optString("roomType").trim());
        }

        if (MDMDataUtils.isStringNotNullAndNotEmpty(productDetails.optString("country"))) {
            sb.append(productDetails.optString("country").trim());
        }

        if (MDMDataUtils.isStringNotNullAndNotEmpty(productDetails.optString("city"))) {
            sb.append(productDetails.optString("city").trim());
        }

//        if (travelAndPaxDetails.optInt("totalNumberOfRooms") >= 0) {
//            sb.append(travelAndPaxDetails.optString("totalNumberOfRooms"));
//        }

        if (MDMDataUtils.isStringNotNullAndNotEmpty(roomBreakupJson.optString("roomCategory"))) {
            sb.append(roomBreakupJson.optString("roomCategory").trim());
        }

        if (MDMDataUtils.isStringNotNullAndNotEmpty(roomBreakupJson.optString("roomType"))) {
            sb.append(roomBreakupJson.optString("roomType").trim());
        }

        if (roomBreakupJson.optInt("maximumNumberOfPax") > 0) {
            sb.append(roomBreakupJson.optInt("maximumNumberOfPax"));
        }

        return sb.toString().trim();
    }

    private String getDay(String stringDate) {
        try {
            if (MDMDataUtils.isStringNotNullAndNotEmpty(stringDate)) {

//            Date date = Date.from(DateTimeUtil.formatBEDateTimeZone(stringDate).toInstant());
//            Calendar c = Calendar.getInstance();
//            SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE");
//            return simpleDateformat.format(date);

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date d = df.parse(stringDate);
                DateFormat df2 = new SimpleDateFormat("EEEE");
                return df2.format(d);
            }
        } catch (Exception e) {
            logger.info("Exception in get Day from date ");
        }

        return "";
    }

    private String createChldScreenKeyFromRQ(JSONObject travelAndPaxDetails, JSONObject roomBreakupJson) {
        StringBuilder chld = new StringBuilder();

//        if (MDMDataUtils.isStringNotNullAndNotEmpty(travelAndPaxDetails.optString("checkInDate")))
//            chld.append(getDay(travelAndPaxDetails.getString("checkInDate")));
//
//        if (MDMDataUtils.isStringNotNullAndNotEmpty(travelAndPaxDetails.optString("checkOutDate")))
//            chld.append(getDay(travelAndPaxDetails.getString("checkOutDate")));

        if (MDMDataUtils.isStringNotNullAndNotEmpty(travelAndPaxDetails.optString("checkInDate")) && MDMDataUtils.isStringNotNullAndNotEmpty(travelAndPaxDetails.optString("checkOutDate"))) {
            if (travelAndPaxDetails.optInt("numberOfNights") >= 0)
                chld.append(String.valueOf(travelAndPaxDetails.optInt("numberOfNights")));
        }

        if (MDMDataUtils.isStringNotNullAndNotEmpty(travelAndPaxDetails.optString("mealPlan")))
            chld.append(travelAndPaxDetails.optString("mealPlan"));
        if (MDMDataUtils.isStringNotNullAndNotEmpty(roomBreakupJson.optString("roomCategory")))
            chld.append(roomBreakupJson.optString("roomCategory"));
        if (MDMDataUtils.isStringNotNullAndNotEmpty(roomBreakupJson.optString("roomType")))
            chld.append(roomBreakupJson.optString("roomType"));
        return chld.toString();
    }

    private String createPaxOccupancyKeyFromRQ(JSONObject roomBreakupJson) {
        StringBuilder paxBldr = new StringBuilder();
        if (MDMDataUtils.isStringNotNullAndNotEmpty(roomBreakupJson.optString("roomCategory")))
            paxBldr.append(roomBreakupJson.optString("roomCategory"));
        if (MDMDataUtils.isStringNotNullAndNotEmpty(roomBreakupJson.optString("roomType")))
            paxBldr.append(roomBreakupJson.optString("roomType"));
        if (roomBreakupJson.optInt("maximumNumberOfPax") > 0)
            paxBldr.append(roomBreakupJson.optInt("maximumNumberOfPax"));
        return paxBldr.toString();
    }

    private String createAccoInfoKey(JSONObject productDetails, JSONObject travelAndPaxDetails, JSONObject acooInfo) {
        StringBuilder accoBldr = new StringBuilder();
        JSONObject address = acooInfo.optJSONObject("address");
        if (address != null) {
            if (MDMDataUtils.isStringNotNullAndNotEmpty(productDetails.optString("country")))
                accoBldr.append(address.optString("country"));
            if (MDMDataUtils.isStringNotNullAndNotEmpty(productDetails.optString("country")))
                accoBldr.append(address.optString("city"));
        }

//        if (travelAndPaxDetails.optInt("totalNumberOfRooms") <= acooInfo.optInt("noOfRooms")) {
//            accoBldr.append(String.valueOf(travelAndPaxDetails.optString("totalNumberOfRooms")));
//        }
        return accoBldr.toString();
    }

    //----------------------------------------------------------------Fetch Data for Supplier Ends here-------------------------------------------------------------------------------------
}
