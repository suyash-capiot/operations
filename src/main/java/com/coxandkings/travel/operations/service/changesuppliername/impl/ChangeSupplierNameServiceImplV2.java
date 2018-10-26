package com.coxandkings.travel.operations.service.changesuppliername.impl;

import com.coxandkings.travel.ext.model.be.PaxInfo;
import com.coxandkings.travel.operations.enums.amendclientcommercials.BEInboundOperation;
import com.coxandkings.travel.operations.enums.amendclientcommercials.BEOperationId;
import com.coxandkings.travel.operations.enums.amendclientcommercials.BEServiceUri;
import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.enums.todo.ToDoFunctionalAreaValues;
import com.coxandkings.travel.operations.enums.todo.ToDoTaskNameValues;
import com.coxandkings.travel.operations.enums.todo.ToDoTaskSubTypeValues;
import com.coxandkings.travel.operations.enums.todo.ToDoTaskTypeValues;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.helper.beconsumption.SupplierDetailAccessor;
import com.coxandkings.travel.operations.model.changesuppliername.DiscountOnSupplierPrice;
import com.coxandkings.travel.operations.model.changesuppliername.SupplementOnSupplierPrice;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.model.refund.finaceRefund.ClientType;
import com.coxandkings.travel.operations.model.todo.ToDoTask;
import com.coxandkings.travel.operations.repository.changesuppliername.DiscountOnSupplierPriceRepository;
import com.coxandkings.travel.operations.repository.changesuppliername.SupplementOnSupplierPriceRepository;
import com.coxandkings.travel.operations.resource.changesuppliername.*;
import com.coxandkings.travel.operations.resource.changesuppliername.cms.CmsSupplierResource;
import com.coxandkings.travel.operations.resource.changesuppliername.request.ClientContext;
import com.coxandkings.travel.operations.resource.changesuppliername.request.accoV2.CSCreateBookingResource;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResource;
import com.coxandkings.travel.operations.service.beconsumer.BeToOpsBookingConverter;
import com.coxandkings.travel.operations.service.beconsumer.acco.AccoBookingEngineConsumptionService;
import com.coxandkings.travel.operations.service.beconsumer.acco.AccoCancellationService;
import com.coxandkings.travel.operations.service.beconsumer.air.AirBookingEngineConsumptionService;
import com.coxandkings.travel.operations.service.bedbservice.acco.AccoBeDBUpdateService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.changesuppliername.ChangeSupplierNameService;
import com.coxandkings.travel.operations.service.changesuppliername.ChangeSupplierNameServiceV2;
import com.coxandkings.travel.operations.service.refund.RefundMDMService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.*;
import com.coxandkings.travel.operations.utils.changeSupplier.CachingConfig;
import com.coxandkings.travel.operations.utils.changeSupplier.ChangeSupplierHTTPServiceConsumer;
import com.coxandkings.travel.operations.utils.changeSupplier.CmsXmlStructure;
import com.coxandkings.travel.operations.utils.changeSupplier.UserContext;
import com.coxandkings.travel.operations.utils.xml.XMLTransformer;
import com.coxandkings.travel.operations.utils.xml.XMLUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import io.swagger.models.Operation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.bcel.Const;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.management.OperationsException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = "cmssuppliercache")
public class ChangeSupplierNameServiceImplV2 implements ChangeSupplierNameServiceV2,CmsXmlStructure {
    private static Logger logger = LogManager.getLogger(ChangeSupplierNameServiceImpl.class);
    private RestTemplate restTemplate;
    @Autowired
    private OpsBookingService opsBookingService;
    @Autowired
    private ObjectMapper objectMapper;
    @Value("${change_supplier_name.suppliers}")
    private String supplierNamesUrl;
    @Value("${mdm.supplier-rates}")
    private String supplierRateUrl;
    @Autowired
    private MDMRestUtils mdmRestUtils;
    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Autowired
    @Qualifier("accoCancellationService")
    private AccoCancellationService accoCancellationService;
    @Autowired
    private BookingEngineElasticData bookingEngineElasticData;

    private String accoSupplierRedisKey;
    private String airSupplierRedisKey;
    @Value("${booking-engine-core-services.acco.reprice}")
    private String accoRePriceURL;
    @Autowired
    private AirBookingEngineConsumptionService airBookingEngineConsumptionService;
    @Autowired
    private AccoBookingEngineConsumptionService accoBookingEngineConsumptionService;
    @Autowired
    @Qualifier("discountOnSupplierPriceRepositoryImpl")
    private DiscountOnSupplierPriceRepository discountOnSupplierPriceRepository;
    @Autowired
    @Qualifier("SupplementOnSupplierPriceRepositoryImpl")
    private SupplementOnSupplierPriceRepository supplementOnSupplierPriceRepository;
    @Autowired
    private ToDoTaskService toDoTaskService;
    @Autowired
    private UserService userService;
    @Value("${booking-engine-core-services.acco.opsreprice}")
    private String accoBookingEngineOpsRepriceUrl;

    @Value("${booking-engine-core-services.air.opsreprice}")
    private String airBookingEngineOpsRepriceUrl;
    @Autowired()
    @Qualifier("beToOpsBookingConverter")
    private BeToOpsBookingConverter beToOpsBookingConverter;

    @Autowired
    @Qualifier("accoBeDBUpdateServiceImpl")
    private AccoBeDBUpdateService accoBeDBUpdateService;
    @Autowired
    private RefundMDMService refundMDMService;
    @Autowired
    private EmailUtils emailUtils;
    @Value("${supplement_on_supplier_price.template_config.function}")
    private String functionNameForSupplementOnSupplierPrice;
    @Value("${supplement_on_supplier_price.template_config.scenario}")
    private String scenarioForSupplementOnSupplierPrice;
    @Value("${supplement_on_supplier_price.template_config.subject}")
    private String subjectForSupplementOnSupplierPrice;

    @Value("${change_supplier_price.client.communication.accept.url}")
    private String clientAcceptanceUrl;
    @Value("${change_supplier_price.client.communication.reject.url}")
    private String clientRejectionUrl;
    @Value("${change_supplier_price.ops.fromEmailAddress}")
    private String fromEmailAddress;
    @Value("${change-supplier.acco.applycommercials}")
    private String applyCommercialsurl;
    @Value("${change-supplier.acco.kafka-rq-url}")
    private String kafkaRQurl;
    @Value("${change-supplier.acco.supplier-url}")
    private String supplierURL;
    @Value("${change-supplier.acco.booking-url}")
    private String cmsAccoBookURL;
    @Value("${product_sharing.be.acco_modify}")
    private String cancelURL;
    @Value("${change-supplier.acco.defined-rates}")
    private String definedRates;

    //Change it to handle Channel Manager System! Wrote this to retrieve from MDM and since CMS service isn't ready i cannot change it
    @Override
    public JSONObject getDefinedRates(String bookID, String orderID, String suppID) throws OperationException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        OpsBooking opsBooking = opsBookingService.getBooking(bookID);
        List<OpsProduct> opsProducts =  opsBooking.getProducts();

        OpsOrderDetails opsOrderDetails = null;

        for(OpsProduct opsProduct : opsProducts){
            if(opsProduct.getOrderID().equalsIgnoreCase(orderID)) {
                opsOrderDetails = opsProduct.getOrderDetails();
                break;
            }
        }

        if(opsOrderDetails==null)
            return null;//Insert error message here

        OpsHotelDetails hotelDetails = opsOrderDetails.getHotelDetails();

        String hotelCode = hotelDetails.getHotelCode();
        List<OpsRoom> opsRooms = hotelDetails.getRooms();

        Map<String, List<OpsRoom>> roomTypeToNameMap = new HashMap<String, List<OpsRoom>>();
        Map<String, Set<String>> roomTypeToWeekMap = new HashMap<String, Set<String>>();


        for(OpsRoom opsRoom : opsRooms){ //this map will have all the roomCat to roomType names so we can make a filter to query these rooms for defined rates
            OpsRoomTypeInfo opsRoomTypeInfo = opsRoom.getRoomTypeInfo();
            Set<String> weekDays = new HashSet<String>();

            String roomCatName = opsRoomTypeInfo.getRoomCategoryName();

            String checkInDate=null,checkOutDate=null;

            try {
                checkInDate = opsRoom.getCheckIn();
                checkOutDate = opsRoom.getCheckOut();

                if(checkInDate == null || checkOutDate == null)
                    return null;//Insert error message here

                weekDays.addAll(getDateRange(checkInDate, checkInDate, true, false,true));
            }catch(Exception e) {
                e.printStackTrace();
            }

            if(roomTypeToNameMap.containsKey(roomCatName)) {

                Set<String> weekList = roomTypeToWeekMap.get(roomCatName);
                weekList.addAll(weekDays);
                roomTypeToWeekMap.put(roomCatName, weekList);

                List<OpsRoom> loadList = roomTypeToNameMap.get(roomCatName);
                loadList.add(opsRoom);
                roomTypeToNameMap.put(roomCatName, loadList);
                continue;
            }
            //First Time a room Cat name comes it will come here
            Set<String> weekList = new HashSet<String>();
            weekList.addAll(weekDays);
            roomTypeToWeekMap.put(roomCatName, weekList);

            List<OpsRoom> loadList = new ArrayList<OpsRoom>();
            loadList.add(opsRoom);
            roomTypeToNameMap.put(roomCatName, loadList);
        }

        JSONObject filter = new JSONObject();
        JSONArray andArr = new JSONArray();

        //Creation of filter for Client details
        andArr.put(makeJsonObj("clientType", opsBooking.getClientType()));

        //Creation of filter for room types
        for(Map.Entry<String, List<OpsRoom>> entry : roomTypeToNameMap.entrySet())
        {
            JSONArray chdScrArr = new JSONArray();

            String roomCatName = entry.getKey();
            List<OpsRoom> rooms = entry.getValue();

            chdScrArr.put(makeJsonObj("roomCategory", roomCatName));

            ListIterator<OpsRoom> opsRoomListIterator = rooms.listIterator();

            while(opsRoomListIterator.hasNext()){

                OpsRoom opsRoom = opsRoomListIterator.next();
                chdScrArr.put(makeJsonObj("roomTypeDetails.roomType", opsRoom.getRoomTypeInfo().getRoomTypeName()));
            }

            //Creation of weekdays filter
            Set<String> weekSet = roomTypeToWeekMap.get(entry.getKey());
            Iterator<String> weekItr = weekSet.iterator();

            while(weekItr.hasNext()){
                chdScrArr.put(makeJsonObj("daysOfTheWeek", weekItr.next()));
            }

            andArr.put(makeJsonObj("childRecordScreen1", makeElemMatchObj(chdScrArr)));
        }

        filter.put("defineRates.addDefineRates", makeElemMatchObj(andArr));
        filter.put("defineRates.addDefineRates", "Active");

        MDMRestUtils mdmRestUtils = new MDMRestUtils();

        String url = "http://10.24.2.5:10075/supplier-rate/v1/accommodation/main?filter=%s";
        String test =  mdmRestUtils.getResponseJSON(String.format(url, filter));

        JSONObject jsonObject = new JSONObject(test);

        System.out.println(jsonObject);

        return null;
    }

    private static JSONObject makeElemMatchObj(JSONArray andArr)
    {
        JSONObject andJson = new JSONObject();
        andJson.put("$and", new JSONArray(andArr.toString()));

        JSONObject elemMatchJson = new JSONObject();
        elemMatchJson.put("$elemMatch", andJson);

        return elemMatchJson;
    }

    private static JSONObject makeJsonObj(String key, Object value)
    {
        JSONObject json = new JSONObject();
        json.put(key, value);

        return json;
    }


    private static ArrayList<String> getDateRange(String startDate,String endDate, boolean strtInclusive,boolean endInclusive,boolean isWeekResponse) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<String> dates = new ArrayList<>();

        if(startDate==null || startDate.isEmpty()){
            //System.out.println("No start date is provided");
            return dates;
        }
        if(endDate==null || endDate.isEmpty()){
            //System.out.println("No end date is provided");
            if(strtInclusive) {
                Date start = dateFormat.parse(startDate);
                if(isWeekResponse)
                    dates.add(new SimpleDateFormat("EEEE", Locale.ENGLISH).format(start));
                else
                    dates.add(startDate);
            }
            return dates;
        }
        try
        {
            Calendar start = Calendar.getInstance();
            start.setTime(dateFormat.parse(startDate));

            Calendar end = Calendar.getInstance();
            end.setTime(dateFormat.parse(endDate));

            while(!start.after(end))
            {
                Date targetDay = start.getTime();
                if(isWeekResponse)
                    dates.add(new SimpleDateFormat("EEEE", Locale.ENGLISH).format(targetDay));
                else
                    dates.add(dateFormat.format(targetDay));
                start.add(Calendar.DATE, 1);
            }
            if(!dates.isEmpty() && !strtInclusive)
                dates.remove(0);
            if(!dates.isEmpty() && !endInclusive)
                dates.remove(dates.size()-1);
            return dates;

        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ArrayList<String>();
        }
    }

    @Override
    public JSONObject createBooking(CSCreateBookingResource csCreateBookingResource) throws OperationException {
        try {
            OpsBooking opsBooking = null;

            opsBooking = opsBookingService.getBooking(csCreateBookingResource.getBookID());

            JSONObject definedRate = csCreateBookingResource.getDefinedRates();

            String newSuppID = csCreateBookingResource.getNewSuppID();

            //requestHeader
            JSONObject requestHeader = applyCommReqHdr(opsBooking);

            List<OpsProduct> opsProducts = opsBooking.getProducts();
            OpsOrderDetails opsOrderDetails = null;

            for (OpsProduct opsProduct : opsProducts) {
                if (opsProduct.getOrderID().equalsIgnoreCase(csCreateBookingResource.getOrderID())) {
                    opsOrderDetails = opsProduct.getOrderDetails();
                    break;
                }
            }

            if (opsOrderDetails == null)
                throw new OperationException(Constants.OPS_CS_00003);

            OpsHotelDetails hotelDetails = null;
            if (opsOrderDetails.getHotelDetails() != null)
                hotelDetails = opsOrderDetails.getHotelDetails();

            JSONObject cmsResponse = processCMSBooking(requestHeader, hotelDetails, newSuppID, definedRate);

            if(cmsResponse==null)
                throw new OperationException(Constants.OPS_CS_00004);

            cancelBooking(csCreateBookingResource.getBookID(),csCreateBookingResource.getOrderID());

            //requestBody
            JSONObject requestBody = applyCommReqBody(hotelDetails);

            OpsHotelDetails opsHotelDetailsUpdated = new OpsHotelDetails();
            opsHotelDetailsUpdated = getUpdatedHotelDetails(hotelDetails, definedRate);

            //responseBody
            JSONObject responseBody = applyCommResBody(opsHotelDetailsUpdated, newSuppID);

            JSONObject payLoad = new JSONObject();
            payLoad.put("commercialsOperation", "Reprice");
            payLoad.put("requestHeader", requestHeader);
            payLoad.put("requestBody", requestBody);
            payLoad.put("responseBody", responseBody);

            ResponseEntity<String> applyCommercialsRs = null;
            ResponseEntity<String> rqResponse = null;

            //Applied commercials
            try {
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> httpEntity = new HttpEntity<String>(payLoad.toString(), httpHeaders);

                applyCommercialsRs = RestUtils.postForEntity(applyCommercialsurl, httpEntity, String.class);
            }
            catch (Exception e) {
                logger.warn("Something went wrong in Booking Engine while applying commercials");
                throw new OperationException(Constants.OPS_CS_00007);
            }

            //Populated the request in DB
            JSONObject applyCommJsonRS = new JSONObject(applyCommercialsRs.getBody());

            JSONObject tunedCommJson = new JSONObject();

            tunedCommJson.put("requestHeader", applyCommJsonRS.getJSONObject("responseHeader"));

            tunedCommJson.put("requestBody", applyCommJsonRS.getJSONObject("responseBody"));
            tunedCommJson.getJSONObject("requestBody").put("bookID", UUID.randomUUID().toString());

            tunedCommJson = getTunedKafkaRQ(applyCommJsonRS);
            JSONObject rqResponseJsn = new JSONObject();
            try {

                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> httpEntity = new HttpEntity<String>(tunedCommJson.toString(), httpHeaders);

                rqResponse = RestUtils.postForEntity(kafkaRQurl, httpEntity, String.class);

                rqResponseJsn = new JSONObject(rqResponse.getBody());

                if (!rqResponseJsn.getString("id").equalsIgnoreCase("true"))
                    throw new OperationException(Constants.OPS_CS_00007);

            }
            catch(OperationException e){
                logger.warn("Something went wrong while creating a record in DB Services");
                throw e;
            }
            catch (Exception e) {
                logger.warn("Something went wrong while creating a record in DB Services");
                throw new OperationException(Constants.OPS_CS_00007);
            }
            return payLoad;
        }
        catch(OperationException x)
        {
            logger.error("Exception occured while changing supplier // Class : ChangeSupplierNameServiceImplV2 // Method : createBooking");
            throw x;
        }
        catch(Exception e)
        {
            logger.error("Exception occured while changing supplier // Class : ChangeSupplierNameServiceImplV2 // Method : createBooking");
            throw new OperationException(Constants.OPS_CS_00007);
        }
    }

    @Override
    public JSONObject processCMSBooking(JSONObject requestHeader, OpsHotelDetails opsHotelDetails, String newSuppId, JSONObject definedRate) throws OperationException {
    try {

        Element reqElem = (Element) XMLTransformer.fromEscapedString(cmsBookXml).cloneNode(true);
        Document reqDoc = reqElem.getOwnerDocument();

        //Creation of request header

        Element userIdElem = XMLUtils.getFirstElementAtXPath(reqElem, "./RequestHeader/UserID");
        userIdElem.setTextContent(requestHeader.getString("userID"));

        Element sessionIdElem = XMLUtils.getFirstElementAtXPath(reqElem, "./RequestHeader/SessionID");
        sessionIdElem.setTextContent(UUID.randomUUID().toString());

        Element transactionIdElem = XMLUtils.getFirstElementAtXPath(reqElem, "./RequestHeader/TransactionID");
        transactionIdElem.setTextContent(UUID.randomUUID().toString());

        Element hotelReservationsElem = XMLUtils.getFirstElementAtXPath(reqElem, "/AccoInterfaceRQ/RequestBody/OTA_HotelResRQWrapper/OTA_HotelResRQ/HotelReservations");

        List<OpsRoom> opsRooms = opsHotelDetails.getRooms();
        Iterator<OpsRoom> opsRoomIterator = opsRooms.iterator();

        List<OpsAccommodationPaxInfo> opsAccommodationPaxInfos = new ArrayList<>();

        Element basicPropertyInfoGlobal = reqDoc.createElement("BasicPropertyInfo");

        Element roomStays = reqDoc.createElement("RoomStays");

        while (opsRoomIterator.hasNext()) {
            Element roomStay = reqDoc.createElement("RoomStay");
            OpsRoom opsRoom = opsRoomIterator.next();

            //----------------Room Types--------------------------------

            OpsRoomTypeInfo opsRoomTypeInfo = opsRoom.getRoomTypeInfo();

            Element textElem = reqDoc.createElement("Text");
            textElem.setTextContent("NA");

            Element detailDesc = reqDoc.createElement("DetailDescription");
            detailDesc.setAttribute("Name", "NA");
            detailDesc.appendChild(textElem);

            Element additionalElement = reqDoc.createElement("AdditionalDetail");
            additionalElement.setAttribute("Code", "NA");
            additionalElement.setAttribute("Type", "NA");
            additionalElement.appendChild(detailDesc);

            Element additonalDtlsElem = reqDoc.createElement("AdditionalDetails");
            additonalDtlsElem.appendChild(additionalElement);

            Element tpaExtensionsRoomType = reqDoc.createElement("TPA_Extensions");

            Element roomCategoryId = reqDoc.createElement("RoomCategoryID");
            roomCategoryId.setTextContent(opsRoomTypeInfo.getRoomCategoryID());

            Element roomCategoryName = reqDoc.createElement("RoomCategoryName");
            roomCategoryName.setTextContent(opsRoomTypeInfo.getRoomCategoryName());

            Element sharingBedding = reqDoc.createElement("SharingBedding");

            tpaExtensionsRoomType.appendChild(sharingBedding);
            tpaExtensionsRoomType.insertBefore(roomCategoryName, sharingBedding);
            tpaExtensionsRoomType.insertBefore(roomCategoryId, roomCategoryName);

            Element roomTypeElem = reqDoc.createElement("RoomType");
            roomTypeElem.setAttribute("RoomID", opsRoomTypeInfo.getRoomRef());
            roomTypeElem.setAttribute("RoomType", opsRoomTypeInfo.getRoomTypeName());
            roomTypeElem.setAttribute("RoomTypeCode", opsRoomTypeInfo.getRoomTypeCode());
            roomTypeElem.setAttribute("BedTypeCode", "NA");

            roomTypeElem.appendChild(tpaExtensionsRoomType);
            roomTypeElem.insertBefore(additonalDtlsElem, tpaExtensionsRoomType);

            Element roomTypesElem = reqDoc.createElement("RoomTypes");
            roomTypesElem.appendChild(roomTypeElem);

            //---------------RATE PLAN-------------------------------------------------------

            OpsMealInfo opsMealInfo = opsRoom.getMealInfo();

            Element mealElem = reqDoc.createElement("Meal");
            mealElem.setAttribute("MealId", opsMealInfo.getMealID());
            mealElem.setAttribute("Price", "NA");

            Element mealsElem = reqDoc.createElement("Meals");
            mealsElem.appendChild(mealElem);

            Element tpaExtionsionsMealInfo = reqDoc.createElement("TPA_Extensions");
            tpaExtionsionsMealInfo.appendChild(mealsElem);

            OpsRatePlanInfo opsRatePlanInfo = opsRoom.getRatePlanInfo();

            Element ratePlanElem = reqDoc.createElement("RatePlan");
            ratePlanElem.setAttribute("BookingCode", opsRatePlanInfo.getBookingRef());
            ratePlanElem.setAttribute("RatePlanCode", opsRatePlanInfo.getRatePlanCode());
            ratePlanElem.setAttribute("RatePlanID", "NA");
            ratePlanElem.setAttribute("RatePlanName", opsRatePlanInfo.getRatePlanname());
            ratePlanElem.setAttribute("RatePlanType", opsRatePlanInfo.getRatePlanRef());

            ratePlanElem.appendChild(tpaExtionsionsMealInfo);

            Element ratePlansElem = reqDoc.createElement("RatePlans");
            ratePlansElem.appendChild(ratePlanElem);

            //---------------------------ROOM RATES------------------------------------------------

            JSONObject ratesJsn = definedRate.getJSONObject("rates");
            JSONArray rateJsnList = ratesJsn.getJSONArray("rateJsonList");


            Element ratesElem = reqDoc.createElement("Rates");

            for (int i = 0; i < rateJsnList.length(); i++) {

                JSONObject rateJsn = rateJsnList.getJSONObject(i);

                Element freeNightsElem = reqDoc.createElement("freenights");

                Element promotionDetailsElem = reqDoc.createElement("PromotionDetails");
                promotionDetailsElem.appendChild(freeNightsElem);

                Element referenceElem = reqDoc.createElement("Reference");
                referenceElem.setAttribute("ID", "NA");
                referenceElem.setAttribute("ID_Context", "NA");
                referenceElem.setAttribute("Instance", "NA");
                referenceElem.setAttribute("Type", "NA");

                Element referencesElem = reqDoc.createElement("References");
                referencesElem.appendChild(referenceElem);

                Element mealsRateElem = reqDoc.createElement("Meals");

                Element supplierReferenceKeyElem = reqDoc.createElement("SupplierReferenceKey");
                supplierReferenceKeyElem.setTextContent(rateJsn.getString("supplierRefKey"));

                Element tpaExtensionsRateElem = reqDoc.createElement("TPA_Extensions");
                tpaExtensionsRateElem.appendChild(supplierReferenceKeyElem);
                tpaExtensionsRateElem.insertBefore(mealsRateElem, supplierReferenceKeyElem);
                tpaExtensionsRateElem.insertBefore(referencesElem, mealsRateElem);
                tpaExtensionsRateElem.insertBefore(promotionDetailsElem, referencesElem);

                Element totalRateElem = reqDoc.createElement("Total");
                totalRateElem.setAttribute("AmountAfterTax", "");
                totalRateElem.setAttribute("CurrencyCode", rateJsn.getString("currCode"));

                Element baseRateElem = reqDoc.createElement("Base");
                baseRateElem.setAttribute("AmountAfterTax", rateJsn.getString("baseAmt"));
                baseRateElem.setAttribute("CurrencyCode", rateJsn.getString("currCode"));

                Element rateElem = reqDoc.createElement("Rate");
                rateElem.setAttribute("EffectiveDate", rateJsn.getString("effectiveDate"));
                rateElem.setAttribute("ExpireDate", "");

                rateElem.appendChild(tpaExtensionsRateElem);
                rateElem.insertBefore(totalRateElem, tpaExtensionsRateElem);
                rateElem.insertBefore(baseRateElem, totalRateElem);

                ratesElem.appendChild(rateElem);
            }

            Element roomRateElem = reqDoc.createElement("RoomRate");
            roomRateElem.setAttribute("AvailabilityStatus", "");
            roomRateElem.setAttribute("BookingCode", "");
            roomRateElem.setAttribute("RatePlanCode", "");
            roomRateElem.setAttribute("RatePlanID", "");
            roomRateElem.setAttribute("RatePlanType", "");

            roomRateElem.appendChild(ratesElem);


            Element changedOccupancyDtlsElem = reqDoc.createElement("ChangedOccupanyDetails");
            changedOccupancyDtlsElem.setAttribute("ExtraBedOccupant", "");
            changedOccupancyDtlsElem.setAttribute("ExtraBedQty", "");
            changedOccupancyDtlsElem.setAttribute("adultsCount", "");
            changedOccupancyDtlsElem.setAttribute("childCount", "");
            changedOccupancyDtlsElem.setAttribute("childrenAges", "");

            Element roomRateAddtnDetail = reqDoc.createElement("RoomRateAdditionalDetail");
            roomRateAddtnDetail.appendChild(changedOccupancyDtlsElem);

            Element roomRateAdditionalDetails = reqDoc.createElement("RoomRateAdditionalDetails");
            roomRateAdditionalDetails.appendChild(roomRateAddtnDetail);

            Element tpaExtionsionsRoomRate = reqDoc.createElement("TPA_Extensions");
            tpaExtionsionsRoomRate.appendChild(roomRateAdditionalDetails);

            Element roomRatesElem = reqDoc.createElement("RoomRates");
            roomRatesElem.appendChild(tpaExtionsionsRoomRate);
            roomRatesElem.insertBefore(roomRateElem, tpaExtionsionsRoomRate);

            //--------------------GUEST COUNTS-----------------------------------------------------------------------

            List<OpsAccommodationPaxInfo> opsAccommInfo = opsRoom.getPaxInfo();
            opsAccommodationPaxInfos.addAll(opsAccommInfo);//to create resguests later for all the passengers

            Iterator<OpsAccommodationPaxInfo> opsAccommInfoItr = opsAccommInfo.iterator();

            Element guestCountsElem = reqDoc.createElement("GuestCounts");
            while (opsAccommInfoItr.hasNext()) {

                Element guestCount = reqDoc.createElement("GuestCount");
                OpsAccommodationPaxInfo paxInfo = opsAccommInfoItr.next();

                if (paxInfo.getPaxType().equalsIgnoreCase("ADT")) {
                    guestCount.setAttribute("Age", "26");
                    guestCount.setAttribute("AgeQualifyingCode", "10");
                } else {
                    guestCount.setAttribute("Age", "4");
                    guestCount.setAttribute("AgeQualifyingCode", "8");
                }
                guestCount.setAttribute("Count", "1");
                guestCount.setAttribute("ResGuestRPH", paxInfo.getPaxID());

                guestCountsElem.appendChild(guestCount);
            }

            //---------------------------------------TIMESPAN------------------------------------------------------------------------------
            Element timeSpan = reqDoc.createElement("TimeSpan");
            timeSpan.setAttribute("Start", opsRoom.getCheckIn());
            timeSpan.setAttribute("End", opsRoom.getCheckOut());

            //--------------------------------------CANCEL PENALTIES------------------------------------------------------------------------

            Element deadLineElem = reqDoc.createElement("Deadline");
            deadLineElem.setAttribute("AbsoluteDeadline", "");

            Element cancelPenalty = reqDoc.createElement("CancelPenalty");
            cancelPenalty.appendChild(deadLineElem);

            Element cancelPenalties = reqDoc.createElement("CancelPenalties");
            cancelPenalties.appendChild(cancelPenalty);

            //-------------------------------------TOTAL------------------------------------------------------------------------------------

            Element totalElem = reqDoc.createElement("Total");
            totalElem.setAttribute("AmountAfterTax", String.valueOf(ratesJsn.getInt("roomRate")));
            totalElem.setAttribute("AmountBeforeTax", "");
            totalElem.setAttribute("CurrencyCode", ratesJsn.getString("currCode"));


            Element taxesElem = reqDoc.createElement("Taxes");
            taxesElem.setAttribute("Amount", "");
            taxesElem.setAttribute("CurrencyCode", "");

            totalElem.appendChild(taxesElem);


            //-------------------------------------BASIC PROPERTY INFO------------------------------------------------------------------------

            Element countryNameElem = reqDoc.createElement("CountryName");
            countryNameElem.setAttribute("Code", "");

            Element addressElem = reqDoc.createElement("Address");
            addressElem.appendChild(countryNameElem);

            Element basicPropertyInfo = reqDoc.createElement("BasicPropertyInfo");
            basicPropertyInfo.setAttribute("HotelCityCode", opsHotelDetails.getCityCode());
            basicPropertyInfo.setAttribute("HotelCode", opsHotelDetails.getHotelCode());
            basicPropertyInfo.setAttribute("HotelCodeContext", "NA");
            basicPropertyInfo.setAttribute("HotelName", opsHotelDetails.getHotelName());

            basicPropertyInfoGlobal = (Element) basicPropertyInfo.cloneNode(true);

            basicPropertyInfo.appendChild(addressElem);



            //-------------------------------------TPA-EXTENSIONS------------------------------------------------------------------------------
//CMS expects these elements in the structure but does not expect any values. Need to create this manually later
            Element tpaExtensionsRoomStay = XMLTransformer.toXMLElement("<TPA_Extensions>\n" +
                    "                                    <Meals />\n" +
                    "                                    <References>\n" +
                    "                                        <Reference ID=\"NA\" ID_Context=\"NA\" Instance=\"NA\" Type=\"NA\" />\n" +
                    "                                    </References>\n" +
                    "                                    <Opt />\n" +
                    "                                    <SupplierDetails>\n" +
                    "                                        <SupplierID />\n" +
                    "                                    </SupplierDetails>\n" +
                    "                                    <PriceInfo AdultPrice=\"\" ChildPrice=\"\" MaxPrice=\"\" MinPrice=\"\" currencyCode=\"INR\" />\n" +
                    "                                    <SelectedSupplements />\n" +
                    "                                    <ExtraBedDetails>\n" +
                    "                                        <ExtraBedDetail ExtraBedIndicator=\"\" />\n" +
                    "                                    </ExtraBedDetails>\n" +
                    "                                </TPA_Extensions>");

            tpaExtensionsRoomStay = (Element) reqDoc.adoptNode(tpaExtensionsRoomStay);

            //------------------------------------COMMENTS----------------------------------------------------------------------------------------
//CMS expects these elements in the structure but does not expect any values. Need to create this manually later
            Element commentsElem = XMLTransformer.toXMLElement("<Comments>\n" +
                    "                                    <Comment>\n" +
                    "                                        <Text />\n" +
                    "                                    </Comment>\n" +
                    "                                </Comments>");

            commentsElem = (Element) reqDoc.adoptNode(commentsElem);

            //-------------------------------------REFERENCE-----------------------------------------------------------------------------------------
//CMS expects these elements in the structure but does not expect any values. Need to create this manually later
            Element referenceElem = XMLTransformer.toXMLElement("<Reference ID=\"NA\" ID_Context=\"NA\" Instance=\"NA\" Type=\"NA\" />");

            referenceElem = (Element) reqDoc.adoptNode(referenceElem);


            roomStay.appendChild(referenceElem);
            roomStay.insertBefore(commentsElem, referenceElem);
            roomStay.insertBefore(tpaExtensionsRoomStay, commentsElem);
            roomStay.insertBefore(basicPropertyInfo, tpaExtensionsRoomStay);
            roomStay.insertBefore(totalElem, basicPropertyInfo);
            roomStay.insertBefore(cancelPenalties, totalElem);
            roomStay.insertBefore(timeSpan, cancelPenalties);
            roomStay.insertBefore(guestCountsElem, timeSpan);
            roomStay.insertBefore(roomRatesElem, guestCountsElem);
            roomStay.insertBefore(ratePlansElem, roomRatesElem);
            roomStay.insertBefore(roomTypesElem, ratePlansElem);

            roomStays.appendChild(roomStay);
        }
        //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX--SERVICES--XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
        Element servicesElem = XMLTransformer.toXMLElement("<Services>\n" +
                "                            <Service ID=\"\" Type=\"\" />\n" +
                "                        </Services>");

        servicesElem = (Element) reqDoc.adoptNode(servicesElem);

        //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX--BILLINGINSTRUCTIONCODE--XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

        Element billingInstructionCodeElem = XMLTransformer.toXMLElement("<BillingInstructionCode BillingCode=\"\" />");
        billingInstructionCodeElem = (Element) reqDoc.adoptNode(billingInstructionCodeElem);


        //XXXXXXXXXXXXXXXXXXXXXXXXXXXXX--RESGUESTS--XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

        Element resGuests = reqDoc.createElement("ResGuests");
        Iterator<OpsAccommodationPaxInfo> infoIterator = opsAccommodationPaxInfos.iterator();
        while (infoIterator.hasNext()) {

            OpsAccommodationPaxInfo opsAccommodationPaxInfo = infoIterator.next();

            //------------------------------------------PERSON NAME-------------------------------------------------------

            Element personName = reqDoc.createElement("PersonName");

            Element namePrefix = reqDoc.createElement("NamePrefix");
            namePrefix.setTextContent(opsAccommodationPaxInfo.getTitle());

            Element givenName = reqDoc.createElement("GivenName");
            givenName.setTextContent(opsAccommodationPaxInfo.getFirstName());

            Element middleName = reqDoc.createElement("MiddleName");
            middleName.setTextContent(opsAccommodationPaxInfo.getMiddleName());

            Element surnamePrefix = reqDoc.createElement("SurnamePrefix");
            surnamePrefix.setTextContent("");

            Element surName = reqDoc.createElement("Surname");
            surName.setTextContent(opsAccommodationPaxInfo.getLastName());

            Element nameTitle = reqDoc.createElement("NameTitle");
            nameTitle.setTextContent(opsAccommodationPaxInfo.getTitle());

            personName.appendChild(nameTitle);
            personName.insertBefore(surName, nameTitle);
            personName.insertBefore(surnamePrefix, surName);
            personName.insertBefore(middleName, surnamePrefix);
            personName.insertBefore(givenName, middleName);
            personName.insertBefore(namePrefix, givenName);

            //----------------------------------------TELEPHONE-------------------------------------------------------------

            Element telephoneElem = reqDoc.createElement("Telephone");
            telephoneElem.setAttribute("AreaCityCode", "");
            telephoneElem.setAttribute("CountryAccessCode", "");
            telephoneElem.setAttribute("Extension", "");
            telephoneElem.setAttribute("PhoneLocationType", "");
            telephoneElem.setAttribute("PhoneNumber", opsAccommodationPaxInfo.getContactDetails().get(0).getContactNumber());
            telephoneElem.setAttribute("PhoneTechType", "");

            //---------------------------------------EMAIL------------------------------------------------------------------

            Element emailElem = reqDoc.createElement("Email");
            emailElem.setTextContent(opsAccommodationPaxInfo.getContactDetails().get(0).getEmail());

            //--------------------------------------ADDRESS-----------------------------------------------------------------

            Element addressLineElem = reqDoc.createElement("AddressLine");
            addressLineElem.setTextContent("");

            Element cityName = reqDoc.createElement("CityName");
            cityName.setTextContent(opsAccommodationPaxInfo.getAddressDetails().getCityName());

            Element postalCode = reqDoc.createElement("PostalCode");
            postalCode.setTextContent(opsAccommodationPaxInfo.getAddressDetails().getZipCode());

            Element stateProv = reqDoc.createElement("StateProv");
            stateProv.setTextContent(opsAccommodationPaxInfo.getAddressDetails().getState());

            Element countryName = reqDoc.createElement("CountryName");
            countryName.setTextContent(opsAccommodationPaxInfo.getAddressDetails().getCountryName());
            countryName.setAttribute("Code", opsAccommodationPaxInfo.getAddressDetails().getCountryName().toLowerCase().contains("in") ? "In" : "");

            Element addressElem = reqDoc.createElement("Address");
            addressElem.appendChild(countryName);
            addressElem.insertBefore(stateProv, countryName);
            addressElem.insertBefore(postalCode, stateProv);
            addressElem.insertBefore(cityName, postalCode);
            addressElem.insertBefore(addressLineElem, cityName);

            //---------------------------------------CITIZENCOUNTRYNAME-----------------------------------------------------

            Element citizenCountryNameElem = reqDoc.createElement("CitizenCountryName");
            citizenCountryNameElem.setAttribute("Code", "");


            //---------------------------------------CUSTOMER---------------------------------------------------------------

            Element customerElem = reqDoc.createElement("Customer");
            customerElem.appendChild(citizenCountryNameElem);
            customerElem.insertBefore(addressElem, citizenCountryNameElem);
            customerElem.insertBefore(emailElem, addressElem);
            customerElem.insertBefore(telephoneElem, emailElem);
            customerElem.insertBefore(personName, telephoneElem);

            //---------------------------------------PROFILE----------------------------------------------------------------

            Element profileElem = reqDoc.createElement("Profile");
            profileElem.appendChild(customerElem);

            //---------------------------------------PROFILEINFO------------------------------------------------------------

            Element profileInfo = reqDoc.createElement("ProfileInfo");
            profileInfo.appendChild(profileElem);

            //--------------------------------------PROFILES----------------------------------------------------------------

            Element profilesElem = reqDoc.createElement("Profiles");
            profilesElem.appendChild(profileInfo);

            //--------------------------------------ARRIVALTRANSPORT--------------------------------------------------------

            Element depTransportInfo = reqDoc.createElement("TransportInfo");
            depTransportInfo.setAttribute("ID", "");
            depTransportInfo.setAttribute("Time", "");

            Element arrivalTransport = reqDoc.createElement("ArrivalTransport");
            arrivalTransport.appendChild(depTransportInfo);
            //-------------------------------------DEPARTURETRANSPORT-------------------------------------------------------

            Element arrTransportInfo = reqDoc.createElement("TransportInfo");
            arrTransportInfo.setAttribute("ID", "");
            arrTransportInfo.setAttribute("Time", "");

            Element departureTransport = reqDoc.createElement("DepartureTransport");
            departureTransport.appendChild(arrTransportInfo);

            //-----------------------------------RESGUESTS--------------------------------------------------------------

            Element resGuestsElem = reqDoc.createElement("ResGuest");
            resGuestsElem.setAttribute("Age", "26");

            if (opsAccommodationPaxInfo.getPaxType().toLowerCase().contains("ad"))
                resGuestsElem.setAttribute("AgeQualifyingCode", "10");
            else
                resGuestsElem.setAttribute("AgeQualifyingCode", "8");

            resGuestsElem.setAttribute("PrimaryIndicator", String.valueOf(opsAccommodationPaxInfo.getLeadPax()));
            resGuestsElem.setAttribute("ResGuestRPH", opsAccommodationPaxInfo.getPaxID());

            resGuestsElem.appendChild(departureTransport);
            resGuestsElem.insertBefore(arrivalTransport, departureTransport);
            resGuestsElem.insertBefore(profilesElem, arrivalTransport);

            resGuests.appendChild(resGuestsElem);
        }

        //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX--RESGLOBALINFO--XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

        Element resGlobalInfo = reqDoc.createElement("ResGlobalInfo");

        //--------------------------------------------COMMENTS----------------------------------------------------------

        Element commentsElem = reqDoc.createElement("Comments");

        //--------------------------------------------GUARANTEE---------------------------------------------------------

        Element guaranteeElem = reqDoc.createElement("Guarantee");
        guaranteeElem.setAttribute("GuaranteeCode", "");
        guaranteeElem.setAttribute("GuaranteeType", "");

        //-------------------------------------------DEPOSITPAYMENTS----------------------------------------------------
        //CMS expects these elements in the structure but does not expect any values. Need to create this manually later
        Element depositPaymentsElem = XMLTransformer.toXMLElement("<DepositPayments>\n" +
                "                                <GuaranteePayment GuaranteeType=\"\">\n" +
                "                                    <AcceptedPayments>\n" +
                "                                        <AcceptedPayment>\n" +
                "                                            <PaymentCard EffectiveDate=\"\" ExpireDate=\"\">\n" +
                "                                                <CardType />\n" +
                "                                                <CardHolderName />\n" +
                "                                                <CardHolderNameDetails>\n" +
                "                                                    <NamePrefix />\n" +
                "                                                    <GivenName />\n" +
                "                                                    <MiddleName />\n" +
                "                                                    <Surname />\n" +
                "                                                </CardHolderNameDetails>\n" +
                "                                                <Address>\n" +
                "                                                    <AddressLine />\n" +
                "                                                    <CityName />\n" +
                "                                                    <PostalCode />\n" +
                "                                                    <CountryName Code=\"\" />\n" +
                "                                                </Address>\n" +
                "                                                <Telephone PhoneNumber=\"\" />\n" +
                "                                                <Email />\n" +
                "                                                <CardNumber>\n" +
                "                                                    <PlainText />\n" +
                "                                                </CardNumber>\n" +
                "                                                <ThreeDomainSecurity>\n" +
                "                                                    <Results CAVV=\"\" />\n" +
                "                                                </ThreeDomainSecurity>\n" +
                "                                            </PaymentCard>\n" +
                "                                            <TPA_Extensions>\n" +
                "                                                <deposit-account-detail>\n" +
                "                                                    <id />\n" +
                "                                                    <password />\n" +
                "                                                </deposit-account-detail>\n" +
                "                                            </TPA_Extensions>\n" +
                "                                        </AcceptedPayment>\n" +
                "                                    </AcceptedPayments>\n" +
                "                                </GuaranteePayment>\n" +
                "                            </DepositPayments>");

        depositPaymentsElem = (Element) reqDoc.adoptNode(depositPaymentsElem);

        //-----------------------------------------TOTAL----------------------------------------------------------------
//CMS expects these elements in the structure but does not expect any values. Need to create this manually later
        Element tpaExtensionsTotal = XMLTransformer.toXMLElement("<TPA_Extensions>\n" +
                "                                    <TaxDetails>\n" +
                "                                        <tax-identifier />\n" +
                "                                        <tax-entity-name />\n" +
                "                                        <tax-holder-address />\n" +
                "                                        <tax-state-code />\n" +
                "                                        <tax-type />\n" +
                "                                        <tax-country-code />\n" +
                "                                        <GSTCompanyEmailId />\n" +
                "                                        <GSTCity />\n" +
                "                                        <GSTPinCode />\n" +
                "                                        <GSTPhoneISD />\n" +
                "                                        <GSTPhoneNumber />\n" +
                "                                        <CustomerName />\n" +
                "                                        <CustomerAddress />\n" +
                "                                        <CustomerState />\n" +
                "                                    </TaxDetails>\n" +
                "                                </TPA_Extensions>");

        tpaExtensionsTotal = (Element) reqDoc.adoptNode(tpaExtensionsTotal);

        //------------------------------------------------------TAXES-----------------------------------------------

        Element taxes = reqDoc.createElement("Taxes");
        taxes.setAttribute("Amount", "0");
        taxes.setAttribute("CurrencyCode", definedRate.getJSONObject("rates").getString("currCode"));

        //-----------------------------------------------------TOTAL----------------------------------------------------

        Element totalElem = reqDoc.createElement("Total");
        totalElem.setAttribute("AmountAfterTax", "");
        totalElem.setAttribute("CurrencyCode", definedRate.getJSONObject("rates").getString("currCode"));

        totalElem.appendChild(tpaExtensionsTotal);
        totalElem.insertBefore(taxes,tpaExtensionsTotal);

        //----------------------------------------------------RESGLOBALINFO-----------------------------------------

        resGlobalInfo.appendChild(basicPropertyInfoGlobal);
        resGlobalInfo.insertBefore(totalElem, basicPropertyInfoGlobal);
        resGlobalInfo.insertBefore(depositPaymentsElem, totalElem);
        resGlobalInfo.insertBefore(guaranteeElem, depositPaymentsElem);
        resGlobalInfo.insertBefore(commentsElem, guaranteeElem);

        //--------------------------------------------------HOTELRESERVATION--------------------------------------------

        Element hotelReservation = reqDoc.createElement("HotelReservation");
        hotelReservation.appendChild(resGlobalInfo);
        hotelReservation.insertBefore(resGuests, resGlobalInfo);
        hotelReservation.insertBefore(billingInstructionCodeElem, resGuests);
        hotelReservation.insertBefore(servicesElem, billingInstructionCodeElem);
        hotelReservation.insertBefore(roomStays, servicesElem);

        hotelReservationsElem.appendChild(hotelReservation);

//        System.out.println(XMLTransformer.toString(reqElem));
        Element resElem=null;
        try {

            URL url = new URL(cmsAccoBookURL);
            Map<String,String> httpHdrs = new HashMap<String,String>();
            httpHdrs.put("Content-Type","application/xml");
            resElem = ChangeSupplierHTTPServiceConsumer.consumeXMLService("CMS CALL",url,httpHdrs,reqElem);

        } catch (MalformedURLException e) {
            throw new OperationException(Constants.OPS_CS_00002);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_CS_00005);
        }

        Element errorElem = XMLUtils.getFirstElementAtXPath(resElem,"./pfx:Body/ns0:AccoInterfaceRS/ns0:ResponseBody/n2:Error");
        if(errorElem!=null)
            throw new OperationException(Constants.OPS_CS_00001);

//        System.out.println(XMLTransformer.toString(resElem));
    }
    catch(OperationException e) {
        logger.error("Exception occured while changing supplier // Class : ChangeSupplierNameServiceImplV2 // Method : processcmsbooking");
        throw e;
    }
    catch(Exception e) {
        logger.error("Exception occured while changing supplier // Class : ChangeSupplierNameServiceImplV2 // Method : processcmsbooking");
        throw e;
    }
        return null;
    }

    private JSONObject getTunedKafkaRQ(JSONObject applyCommercialsRs) throws OperationException{
        try {
            JSONObject tunedCommJson = new JSONObject();

            tunedCommJson.put("requestHeader", applyCommercialsRs.getJSONObject("responseHeader"));

            JSONObject responseBody = applyCommercialsRs.getJSONObject("responseBody");
            JSONArray accommInfoArr = responseBody.getJSONArray("accommodationInfo");

            for (int i = 0; i < accommInfoArr.length(); i++) {
                JSONObject accommInfo = accommInfoArr.getJSONObject(i);

                JSONArray roomStayArr = accommInfo.getJSONArray("roomStay");

                accommInfo.put("roomConfig", roomStayArr);
                accommInfo.remove("roomStay");
            }

            tunedCommJson.put("requestBody", responseBody);
            tunedCommJson.getJSONObject("requestBody").put("bookID", UUID.randomUUID().toString());

            return tunedCommJson;
        }
        catch(Exception e)
        {
            logger.error("Exception occured while changing supplier // Class : ChangeSupplierNameServiceImplV2 // Method : getTunedKafkaRQ");
            throw new OperationException(Constants.OPS_CS_00007);
        }
    }

    private OpsHotelDetails getUpdatedHotelDetails(OpsHotelDetails opsHotelDetails, JSONObject definedRates)
    {
        OpsRoom opsRoom = opsHotelDetails.getRooms().get(0);//Since it will always have 1 room as per the new implementation in BE

        OpsRoomTotalPriceInfo opsRoomTotalPriceInfo = new OpsRoomTotalPriceInfo();
        opsRoomTotalPriceInfo.setOpsTaxes(new OpsTaxes());//CMS doesn't send taxes for now! All the rates are inclusive of tax

        opsRoomTotalPriceInfo.setCurrencyCode(definedRates.getJSONObject("rates").getString("currCode"));
        opsRoomTotalPriceInfo.setRoomTotalPrice(String.valueOf(definedRates.getJSONObject("rates").getInt("roomRate")));

        opsRoom.setRoomTotalPriceInfo(opsRoomTotalPriceInfo);

        OpsRatePlanInfo opsRatePlanInfo = new OpsRatePlanInfo();
        opsRatePlanInfo.setRatePlanCode(definedRates.getString("compRateTypeCode"));
        opsRatePlanInfo.setRatePlanname(opsRoom.getRatePlanInfo().getRatePlanname());//I should get this from CMS
        opsRatePlanInfo.setRatePlanRef(opsRoom.getRatePlanInfo().getRatePlanRef());// I should get this from CMS

        opsRoom.setRatePlanInfo(opsRatePlanInfo);
        opsHotelDetails.setRooms(null);

        List<OpsRoom> opsRoomslist = new ArrayList<>();
        opsRoomslist.add(opsRoom);

        opsHotelDetails.setRooms(opsRoomslist);

        return opsHotelDetails;
    }

    private String getClientGroupProductSuppliers(String clientID, String clientMarket)
    {

        try {
            JSONObject filters = new JSONObject();
            filters.put("deleted",false);
            filters.put("productSupplierAttachedTo.entityId", clientID);
            filters.put("productSupplierAttachedTo.companyMarket", clientMarket);

            String responseDoc = mdmRestUtils.getResponseJSON(String.format("http://10.25.6.103:10051/client-config/v1/enable-disable-product-supplier?filter=%s", filters));

            JSONObject dataJson = new JSONObject(responseDoc);
            JSONArray dataArr = dataJson.getJSONArray("data");

            Iterator<Object> iterator =  dataArr.iterator();

            while(iterator.hasNext())
            {
                JSONObject jsonObject = (JSONObject) iterator.next();
            }


        } catch (OperationException e) {
            e.printStackTrace();
        }

        return null;
    }

    private JSONObject applyCommResBody(OpsHotelDetails hotelDetails, String newSuppID) {
        JSONObject responseBody = new JSONObject();
        JSONArray accommInfoArr = new JSONArray();

        List<OpsRoom> opsRooms = hotelDetails.getRooms();

        for(OpsRoom opsRoom: opsRooms){

            JSONObject roomConfig = new JSONObject();
            roomConfig.put("supplierRef", newSuppID);
            roomConfig.put("payAtHotel",false);
            roomConfig.put("accommodationSubType","Hotel");

            //Total Price Info
            JSONObject totalPriceInfoJson = new JSONObject();
            OpsRoomTotalPriceInfo opsRoomTotalPriceInfo = opsRoom.getRoomTotalPriceInfo();
            OpsTaxes opsTaxes = opsRoomTotalPriceInfo.getOpsTaxes();

            JSONObject taxesJson = new JSONObject();

            List<OpsTax> opsTaxList = opsTaxes.getTax();
            JSONArray taxArr = new JSONArray();
            for(OpsTax opsTax : opsTaxList){

                JSONObject taxJsn = new JSONObject();
                taxJsn.put("amount", opsTax.getAmount());
                taxJsn.put("currencyCode", opsTax.getCurrencyCode());
                taxJsn.put("taxCode", opsTax.getTaxCode());

                taxArr.put(taxJsn);
            }

            taxesJson.put("amount", opsTaxes.getAmount());
            taxesJson.put("taxes", taxArr);
            taxesJson.put("currencyCode", opsTaxes.getCurrencyCode());
            totalPriceInfoJson.put("currencyCode", opsRoomTotalPriceInfo.getCurrencyCode());
            totalPriceInfoJson.put("amount", opsRoomTotalPriceInfo.getRoomTotalPrice());
            totalPriceInfoJson.put("taxes", taxesJson);
            roomConfig.put("totalPriceInfo", totalPriceInfoJson);

            //Occupancy Info
            List<OpsOccupancyInfo> occupancyInfos = opsRoom.getOccupancyInfo();

            JSONArray occupancyArr = new JSONArray();
            for(OpsOccupancyInfo opsOccupancyInfo : occupancyInfos){

                JSONObject occupancyJsn = new JSONObject();

                occupancyJsn.put("minOccupancy",opsOccupancyInfo.getMaxOccupancy());
                occupancyJsn.put("paxType",opsOccupancyInfo.getPaxType());
                occupancyJsn.put("maxAge",opsOccupancyInfo.getMaxAge());
                occupancyJsn.put("minAge",opsOccupancyInfo.getMinAge());
                occupancyJsn.put("maxOccupancy",opsOccupancyInfo.getMinOccupancy());

                occupancyArr.put(occupancyJsn);
            }
            roomConfig.put("occupancyInfo", occupancyArr);

            //nightlyPriceInfo
            List<String> datesList = new ArrayList<String>();
            try{
                datesList = getDateRange(opsRoom.getCheckIn(),opsRoom.getCheckOut(),true,false,false);
            }
            catch(Exception e){
                logger.warn("Error occured while computing dates");
            }

            BigDecimal totalPrice = BigDecimal.valueOf(Double.valueOf(opsRoomTotalPriceInfo.getRoomTotalPrice()));
            String totalPriceCurrencyCode = opsRoomTotalPriceInfo.getCurrencyCode();

            BigDecimal nightlyPrice = totalPrice.divide(BigDecimal.valueOf(Double.valueOf(datesList.size())),2);

            BigDecimal totalTaxAmount = new BigDecimal(0);
            Map<String, JSONObject> taxMap = new HashMap<String, JSONObject>();
            if(opsRoomTotalPriceInfo.getOpsTaxes().getAmount() != null) {
                totalTaxAmount = BigDecimal.valueOf(opsRoomTotalPriceInfo.getOpsTaxes().getAmount());

                String totalTaxCurrencyCode = opsRoomTotalPriceInfo.getOpsTaxes().getCurrencyCode();

                BigDecimal nightlyTotalTax = totalTaxAmount.divide(BigDecimal.valueOf(Double.valueOf(opsRoomTotalPriceInfo.getOpsTaxes().getTax().size())));

                List<OpsTax> taxList = opsRoomTotalPriceInfo.getOpsTaxes().getTax();

                for (OpsTax opsTax : taxList) {
                    JSONObject taxJson = new JSONObject();

                    BigDecimal orderTax = BigDecimal.valueOf(Double.valueOf(opsTax.getAmount()));
                    BigDecimal nightlyTax = orderTax.divide(BigDecimal.valueOf(Double.valueOf(datesList.size())), 2);

                    taxJson.put("taxCode", opsTax.getTaxCode());
                    taxJson.put("currencyCode", opsTax.getCurrencyCode());
                    taxJson.put("amount", nightlyTax);

                    taxMap.put(opsTax.getTaxCode(), taxJson);
                }
            }
            JSONArray nightlyPriceInfoArr = new JSONArray();

            for(String date : datesList) {

                JSONObject nightlyPriceInfo = new JSONObject();

                JSONObject taxObj = new JSONObject();

                JSONArray taxJsnArr = new JSONArray();
                BigDecimal taxesTotAmt = new BigDecimal(0);
                for(Map.Entry<String,JSONObject> entry : taxMap.entrySet())
                {
                    JSONObject taxJson = entry.getValue();
                    taxesTotAmt.add(taxJson.getBigDecimal("amount"));
                    taxJsnArr.put(taxJson);
                }

                taxObj.put("tax",taxJsnArr);
                taxObj.put("amount",taxesTotAmt);
                taxObj.put("currencyCode",totalPriceCurrencyCode);

                nightlyPriceInfo.put("taxes",taxObj);
                nightlyPriceInfo.put("amount",nightlyPrice);
                nightlyPriceInfo.put("currencyCode",totalPriceCurrencyCode);
                nightlyPriceInfo.put("effectiveDate",date);

                nightlyPriceInfoArr.put(nightlyPriceInfo);
            }
            roomConfig.put("nightlyPriceInfo",nightlyPriceInfoArr);

            //roomInfo
            JSONObject roomInfoJsn = new JSONObject();

            OpsRoomTypeInfo opsRoomTypeInfo = opsRoom.getRoomTypeInfo();
            JSONObject roomTypeJson = new JSONObject();
            roomTypeJson.put("roomTypeCode",opsRoomTypeInfo.getRoomTypeCode());
            roomTypeJson.put("roomCategoryCode",opsRoomTypeInfo.getRoomCategoryID());
            roomTypeJson.put("roomRef",opsRoomTypeInfo.getRoomRef());
            roomTypeJson.put("roomTypeName",opsRoomTypeInfo.getRoomTypeName());
            roomTypeJson.put("roomCategoryName",opsRoomTypeInfo.getRoomCategoryName());
            roomInfoJsn.put("roomTypeInfo",roomTypeJson);

            OpsMealInfo opsMealInfo = opsRoom.getMealInfo();
            JSONObject mealInfoJsn = new JSONObject();
            mealInfoJsn.put("mealName",opsMealInfo.getMealName());
            mealInfoJsn.put("mealCode",opsMealInfo.getMealID());
            roomInfoJsn.put("mealInfo",mealInfoJsn);

            OpsRatePlanInfo opsRatePlanInfo = opsRoom.getRatePlanInfo();
            JSONObject ratePlanJsn = new JSONObject();
            ratePlanJsn.put("ratePlanName", opsRatePlanInfo.getRatePlanname());
            ratePlanJsn.put("ratePlanRef", opsRatePlanInfo.getRatePlanRef());
            ratePlanJsn.put("ratePlanCode", opsRatePlanInfo.getRatePlanCode());
            ratePlanJsn.put("bookingRef", opsRatePlanInfo.getBookingRef());
            roomInfoJsn.put("ratePlanInfo",ratePlanJsn);


            JSONObject hotelInfoJsn = new JSONObject();
            hotelInfoJsn.put("hotelCode",hotelDetails.getHotelCode());
            hotelInfoJsn.put("hotelName",hotelDetails.getHotelName());
            roomInfoJsn.put("hotelInfo", hotelInfoJsn);

            roomInfoJsn.put("references",new JSONArray());
            roomInfoJsn.put("availabilityStatus","OnRequest");
            roomInfoJsn.put("requestedRoomIndex",1);

            roomConfig.put("roomInfo",roomInfoJsn);

            JSONArray roomStayArr = new JSONArray();
            roomStayArr.put(roomConfig);

            JSONObject accommJsn = new JSONObject();
            accommJsn.put("accommodationSubType","Hotel");
            accommJsn.put("supplierRef",newSuppID);

            accommJsn.put("roomStay",roomStayArr);

            accommInfoArr.put(accommJsn);
        }

        responseBody.put("accommodationInfo", accommInfoArr);
        return responseBody;
    }

    private JSONObject applyCommReqBody(OpsHotelDetails hotelDetails) {
        JSONObject requestBody = new JSONObject();

        JSONArray accommSubTypesArr = new JSONArray();
        accommSubTypesArr.put("Hotel");//will get it from the ui
        requestBody.put("accommodationSubTypes",accommSubTypesArr);

        requestBody.put("countryCode",hotelDetails.getCountryCode());
        requestBody.put("cityCode",hotelDetails.getCityCode());
        requestBody.put("hotelCode",hotelDetails.getHotelCode());
        requestBody.put("checkIn",hotelDetails.getRooms().get(0).getCheckIn());
        requestBody.put("checkOut",hotelDetails.getRooms().get(0).getCheckOut());
        requestBody.put("paxNationality","IN");

        JSONArray roomConfigArr = new JSONArray();
        /*for(int i=0;i<opsRooms.size();i++) {
            JSONObject roomConfigJson = new JSONObject();
            OpsRoom opsRoom = opsRooms.get(i);

            List<OpsOccupancyInfo> opsOccupancyInfoList = opsRoom.getOccupancyInfo();
            int adtcnt=0; int chdcnt=0;

            for(int j=0;j<opsOccupancyInfoList.size();j++){
                OpsOccupancyInfo opsOccupancyInfo =  opsOccupancyInfoList.get(j);

                if(opsOccupancyInfo.getPaxType().equalsIgnoreCase("ADT"))
                    adtcnt++;
                else
                    chdcnt++;
            }*/
        requestBody.put("roomConfig",roomConfigArr);

        return requestBody;
    }

    private JSONObject applyCommReqHdr(OpsBooking opsBooking) {
        JSONObject requestHeader = new JSONObject();

        //request Header
        JSONObject clientContext = new JSONObject();
        clientContext.put("pointOfSale", opsBooking.getPointOfSale());
        clientContext.put("clientID",  opsBooking.getClientID());
        clientContext.put("clientType", opsBooking.getClientType());
        clientContext.put("clientLanguage", opsBooking.getClientLanguage());
        clientContext.put("clientMarket", opsBooking.getClientMarket());
        clientContext.put("clientCurrency", opsBooking.getClientCurrency());


        requestHeader.put("clientContext", clientContext);
        requestHeader.put("userID", opsBooking.getUserID());
        requestHeader.put("sessionID", UUID.randomUUID().toString());
        requestHeader.put("transactionID", UUID.randomUUID().toString());

        return requestHeader;
    }


    public  Boolean cancelBooking(String bookID,String orderID) throws OperationException {
    try {
            OpsBooking opsBooking = opsBookingService.getBooking(bookID);

            JSONObject payloadJson = new JSONObject();

            JSONObject requestHeader = new JSONObject();

            JSONObject clientContext = new JSONObject();
            clientContext.put("pointOfSale", opsBooking.getPointOfSale());
            clientContext.put("clientID", opsBooking.getClientID());
            clientContext.put("clientType", opsBooking.getClientType());
            clientContext.put("clientLanguage", opsBooking.getClientLanguage());
            clientContext.put("clientMarket", opsBooking.getClientMarket());
            clientContext.put("clientCurrency", opsBooking.getClientCurrency());


            requestHeader.put("clientContext", clientContext);
            requestHeader.put("userID", opsBooking.getUserID());
            requestHeader.put("sessionID", "test11");
            requestHeader.put("transactionID", "12345");


            JSONObject requestBody = new JSONObject();

            JSONArray accommInfoArr = new JSONArray();
            JSONObject accommInfoJson = new JSONObject();
            accommInfoJson.put("modificationType", "FULLCANCELLATION");

            JSONArray orderIDArr = new JSONArray();
            orderIDArr.put(orderID);
            accommInfoJson.put("orderID", orderIDArr);

            accommInfoArr.put(accommInfoJson);

            requestBody.put("bookID", bookID);
            requestBody.put("accommodationInfo", accommInfoArr);

            payloadJson.put("requestHeader", requestHeader);
            payloadJson.put("requestBody", requestBody);

            String cancelRS = "";

            HttpHeaders headers = new HttpHeaders();
            headers.set("content-type", MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<String> httpEntity = new HttpEntity<String>(payloadJson.toString(), headers);
            cancelRS = RestUtils.getTemplate().exchange(cancelURL, HttpMethod.POST, httpEntity, String.class).toString();

            if (cancelRS.length() < 2)
                throw new OperationException(Constants.OPS_CS_00006);

            JSONObject cancelRSJson = new JSONObject(cancelRS);

            JSONArray accomInfoArr = cancelRSJson.getJSONObject("responseBody").getJSONArray("accommodationInfo");

            Boolean isSuccess = false;
            for (int i = 0; i < accomInfoArr.length(); i++) {
                JSONObject accoInfo = accommInfoArr.getJSONObject(i);

                if ("SUCCESS".equalsIgnoreCase(accoInfo.getString("status")))
                    isSuccess = true;
                else {
                    isSuccess = false;
                    break;
                }
            }

            return isSuccess;
        }
        catch(OperationException e){
            throw e;
        }
        catch(Exception e){
            throw new OperationException(Constants.OPS_CS_00007);
        }
    }

    @Override
    public String getCMSDefinedRates(String bookID, String orderID, String suppID) throws OperationException {
        try {
            OpsBooking opsBooking = null;

            opsBooking = opsBookingService.getBooking(bookID);

            List<OpsProduct> opsProducts =  opsBooking.getProducts();

            OpsOrderDetails opsOrderDetails = null;

            for(OpsProduct opsProduct : opsProducts){
                if(opsProduct.getOrderID().equalsIgnoreCase(orderID)) {
                    opsOrderDetails = opsProduct.getOrderDetails();
                    break;
                }
            }

            if(opsOrderDetails==null)
                throw new OperationException(Constants.OPS_CS_00003);

            OpsHotelDetails hotelDetails = opsOrderDetails.getHotelDetails();

            String clientID = opsBooking.getClientID();
            String hotelID = hotelDetails.getHotelCode();

            OpsRoom opsRoom = hotelDetails.getRooms().get(0);

            String checkIn = opsRoom.getCheckIn();
            String checkOut = opsRoom.getCheckOut();
            String currencyCode = opsRoom.getRoomTotalPriceInfo().getCurrencyCode();
            String roomTypeCode = opsRoom.getRoomTypeInfo().getRoomTypeCode();

            int adt=0, chd=0;

            Iterator<OpsAccommodationPaxInfo> opsPaxInfos = opsRoom.getPaxInfo().iterator();
            while(opsPaxInfos.hasNext()) {
                OpsAccommodationPaxInfo opsPaxInfo = opsPaxInfos.next();
                if(opsPaxInfo.getPaxType().equalsIgnoreCase("ADT"))
                    adt++;
                else
                    chd++;
            }


            Element reqElem = XMLTransformer.toXMLElement(definedRatesStruct);
            Document reqDoc = reqElem.getOwnerDocument();

            Element hotelDtlsElm = XMLUtils.getFirstElementAtXPath(reqElem,"/root/Body/HotelDetails");
            hotelDtlsElm.setAttribute("HotelId",hotelID);
            hotelDtlsElm.setAttribute("Start",checkIn);
            hotelDtlsElm.setAttribute("End",checkOut);
            hotelDtlsElm.setAttribute("SupplierId",suppID);
            hotelDtlsElm.setAttribute("ClientId",clientID);
            hotelDtlsElm.setAttribute("Currency",currencyCode);

            //[1]Hardcoding these values because data is there only for these values in cms at the moment
//            hotelDtlsElm.setAttribute("HotelId","TAJ21232434");
//            hotelDtlsElm.setAttribute("Start","2017-01-01");
//            hotelDtlsElm.setAttribute("End","2017-01-03");
//            hotelDtlsElm.setAttribute("SupplierId","Resavenue");
//            hotelDtlsElm.setAttribute("ClientId","clientb2b");
//            hotelDtlsElm.setAttribute("Currency","INR");

            Element roomTypeElm = XMLUtils.getFirstElementAtXPath(reqElem,"/root/Body/HotelDetails/RoomStayCandidates/RoomStayCandidate/RoomType");
            roomTypeElm.setAttribute("RoomTypeCode",roomTypeCode);
//            roomTypeElm.setAttribute("RoomTypeCode","STD01"); // Refer [1]

            Node guestCountsElem = XMLUtils.getFirstElementAtXPath(reqElem,"/root/Body/HotelDetails/RoomStayCandidates/RoomStayCandidate/GuestCounts");

            {
                Element guestCountElem = reqDoc.createElement("GuestCount");
                guestCountElem.setAttribute("AgeQulifyingCode", "10");
                guestCountElem.setAttribute("Count", String.valueOf(adt));
//                guestCountElem.setAttribute("Count", "4"); // Refer [1]

                guestCountsElem.appendChild(guestCountElem);
            }

            {
                Element guestCountElem = reqDoc.createElement("GuestCount");
                guestCountElem.setAttribute("AgeQulifyingCode", "8");
                guestCountElem.setAttribute("Count", String.valueOf(adt));
//                guestCountElem.setAttribute("Count", "2"); // Refer [1]

                guestCountsElem.appendChild(guestCountElem);
            }

            Element resElem = null;
            try {

                URL url = new URL(definedRates);
                Map<String,String> httpHdrs = new HashMap<String,String>();
                httpHdrs.put("Content-Type","application/xml");
                resElem = ChangeSupplierHTTPServiceConsumer.consumeXMLService("CMS CALL",url,httpHdrs,reqElem);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (resElem == null) {
                logger.error("A null response received from CMS");
            }

            try {
                Element[] roomStayElems = XMLUtils.getElementsAtXPath(resElem, "/Response/Sucess/RoomStay");

                Map<String, JSONObject> definedRatesMap = new HashMap<String, JSONObject>();

                for (Element roomStayElem : roomStayElems) {

                    Map<String, List<JSONObject>> ratePlanToRateMap = new HashMap<String, List<JSONObject>>();
                    Element roomRatesElem = XMLUtils.getFirstElementAtXPath(roomStayElem, "./RoomRates");
                    if (roomRatesElem != null) {
                        Element[] roomRateElems = XMLUtils.getElementsAtXPath(roomRatesElem, "./RoomRate");

                        for (Element roomRateElem : roomRateElems) {

                            List<JSONObject> rateJsonList = new ArrayList<JSONObject>();

                            String ratePlanCode = XMLUtils.getValueAtXPath(roomRateElem, "./@RatePlanCode");
                            Element ratesElem = XMLUtils.getFirstElementAtXPath(roomRateElem, "./Rates");
                            if (ratesElem != null) {
                                Element[] rateElems = XMLUtils.getElementsAtXPath(ratesElem, "./Rate");

                                for (Element rateElem : rateElems) {
                                    JSONObject rateJson = new JSONObject();

                                    String effectiveDate = XMLUtils.getValueAtXPath(rateElem, "./@EffectiveDate");
                                    rateJson.put("effectiveDate", effectiveDate);

                                    String baseAmt = XMLUtils.getValueAtXPath(rateElem, "./Base/@AmountAfterTax");
                                    rateJson.put("baseAmt", baseAmt);

                                    String currCode = XMLUtils.getValueAtXPath(rateElem, "./Base/@CurrencyCode");
                                    rateJson.put("currCode", currCode);

                                    String supplierRefKey = XMLUtils.getValueAtXPath(rateElem, "./TPA_Extensions/SupplierReferenceKey");
                                    rateJson.put("supplierRefKey", supplierRefKey);

                                    rateJsonList.add(rateJson);
                                }
                            }
                            ratePlanToRateMap.put(ratePlanCode, rateJsonList);
                        }
                    }

                    Element defineRatesElem = XMLUtils.getFirstElementAtXPath(roomStayElem, "./DefineRates");

                    if (defineRatesElem != null) {

                        Element[] defineRateElems = XMLUtils.getElementsAtXPath(defineRatesElem, "./DefineRate");
                        for (Element defineRateElem : defineRateElems) {

                            JSONObject definedRatesJsn = new JSONObject();

                            String suppRateTypeName = XMLUtils.getValueAtXPath(defineRateElem, "./RateTypes/SupplierRateType/SupplierRateTypeName");
                            definedRatesJsn.put("suppRateTypeName", suppRateTypeName);

                            String suppRateTypeCode = XMLUtils.getValueAtXPath(defineRateElem, "./RateTypes/SupplierRateType/SupplierRateTypeCode");
                            definedRatesJsn.put("suppRateTypeCode", suppRateTypeCode);

                            String compRateTypeName = XMLUtils.getValueAtXPath(defineRateElem, "./RateTypes/CompanyRateType/CompanyRateTypeName");
                            definedRatesJsn.put("compRateTypeName", compRateTypeName);

                            String compRateTypeCode = XMLUtils.getValueAtXPath(defineRateElem, "./RateTypes/CompanyRateType/CompanyRateTypeCode");
                            definedRatesJsn.put("compRateTypeCode", compRateTypeCode);

                            String priceApplicability = XMLUtils.getValueAtXPath(defineRateElem, "./PriceApplicability");
                            definedRatesJsn.put("priceApplicability", priceApplicability);

                            JSONObject clientDetails = new JSONObject();
                            String clientType = XMLUtils.getValueAtXPath(defineRateElem,"./ClientDetails/ClientType");
                            clientDetails.put("clientType",clientType);

                            String clientName = XMLUtils.getValueAtXPath(defineRateElem, "./ClientDetails/ClientName");
                            clientDetails.put("clientName",clientName);

                            String clientGroup = XMLUtils.getValueAtXPath(defineRateElem,"./ClientDetails/ClientType");
                            clientDetails.put("clientGroup",clientGroup);

                            definedRatesJsn.put("clientDtls", clientDetails);

                            JSONObject travelDates = new JSONObject();

                            String trvlDateFrom = XMLUtils.getValueAtXPath(defineRateElem, "./TravelDates/TravelFrom");
                            travelDates.put("trvlDateFrom",trvlDateFrom);

                            String trvlDateTo = XMLUtils.getValueAtXPath(defineRateElem, "./TravelDates/TravelTo");
                            travelDates.put("trvlDateTo",trvlDateTo);

                            definedRatesJsn.put("trvlDtls", travelDates);

                            JSONObject netCommisionObj = new JSONObject();

                            String isCommissionable = XMLUtils.getValueAtXPath(defineRateElem, "./NetCommissionable/@Commisionable");
                            netCommisionObj.put("isCommissionable",isCommissionable);

                            String isNet = XMLUtils.getValueAtXPath(defineRateElem, "./NetCommissionable/@Net");
                            netCommisionObj.put("isNet",isNet);

                            String commissionAmt = XMLUtils.getValueAtXPath(defineRateElem, "./NetCommissionable/Commissionable/@Amount");
                            netCommisionObj.put("commissionAmt",commissionAmt);

                            String percentage = XMLUtils.getValueAtXPath(defineRateElem, "./NetCommissionable/Commissionable/@Percentage");
                            netCommisionObj.put("percentage",percentage);

                            definedRatesJsn.put("netCommissionable",netCommisionObj);
                            definedRatesJsn.put("supplierRef",suppID);

                            if (definedRatesMap.containsKey(compRateTypeCode))
                                definedRatesJsn = definedRatesMap.get(compRateTypeCode);

                            Element ratesElem = XMLUtils.getFirstElementAtXPath(defineRateElem, "./Rates");
                            JSONObject rateJsn = new JSONObject();
                            if (ratesElem != null) {

                                BigDecimal totalAmt = new BigDecimal(0.0);

                                Element[] rateElems = XMLUtils.getElementsAtXPath(ratesElem, "./Rate");
                                for (Element rateElem : rateElems) {
                                    String ratePlanCode = XMLUtils.getValueAtXPath(rateElem, "./RatePlan/@RatePlanCode");
                                    rateJsn.put("ratePlanCode", ratePlanCode);

                                    String rateFor = XMLUtils.getValueAtXPath(rateElem, "./RateFor");
                                    rateJsn.put("rateFor", rateFor);

                                    String amt = XMLUtils.getValueAtXPath(rateElem, "./RoomDetails/RoomRate");
                                    BigDecimal bigDecimal = new BigDecimal(amt);
                                    totalAmt = totalAmt.add(bigDecimal);
                                    rateJsn.put("roomRate", totalAmt);

                                    String checkdIn = XMLUtils.getValueAtXPath(rateElem, "./DefinedFor/@CheckIn");
                                    rateJsn.put("checkIn", checkdIn);

                                    String checkdOut = XMLUtils.getValueAtXPath(rateElem, "./DefinedFor/@CheckOut");
                                    rateJsn.put("checkOut", checkdOut);

                                    String definedFor = XMLUtils.getValueAtXPath(rateElem, "./DefinedFor");
                                    rateJsn.put("definedFor", definedFor);

                                    String roomType = XMLUtils.getValueAtXPath(rateElem, "./RoomDetails/RoomRate");
                                    rateJsn.put("roomType", roomType);

                                    String currCode = XMLUtils.getValueAtXPath(rateElem, "./RoomDetails/RoomRate/@CurrencyCode");
                                    rateJsn.put("currCode", currCode);

                                    String durOfStay = XMLUtils.getValueAtXPath(rateElem, "./DurationOfStay");
                                    rateJsn.put("durationOfStay", durOfStay);

                                    String payUponArrival = XMLUtils.getValueAtXPath(rateElem, "./PayUponArrival");
                                    rateJsn.put("payUponArrival", payUponArrival);

                                    String status = XMLUtils.getValueAtXPath(rateElem, "./Status");
                                    rateJsn.put("status", status);
                                }
                                if (!ratePlanToRateMap.containsKey(compRateTypeCode)) {
                                    logger.trace("No supplierRefObjs from CMS");
                                    throw new OperationException(Constants.OPS_CS_00008);
                                }
                                rateJsn.put("rateJsonList", ratePlanToRateMap.get(compRateTypeCode));
                            }

                            if (definedRatesJsn.has("rates"))
                                definedRatesJsn.getJSONArray("rates").put(rateJsn);
                            else {
                                JSONArray jsonArray = new JSONArray();
                                jsonArray.put(rateJsn);
                                definedRatesJsn.put("rates", jsonArray);
                                definedRatesMap.put(compRateTypeCode, definedRatesJsn);
                            }



                        }
                    }
                }
                JSONArray jsonArray = new JSONArray();
                for (Map.Entry<String, JSONObject> entryMap : definedRatesMap.entrySet()) {
                    jsonArray.put(entryMap.getValue());
                }
                return jsonArray.toString();
            }
            catch(OperationException e)
            {
                logger.error("Exception occured while changing supplier // Class : ChangeSupplierNameServiceImplV2 // Method : getCMSDefinedRates");
                throw e;
            }
            catch(Exception e)
            {
                logger.error("Exception occured while changing supplier // Class : ChangeSupplierNameServiceImplV2 // Method : getCMSDefinedRates");
                throw new OperationException(Constants.OPS_CS_00007);
            }


        } catch (OperationException e) {
            logger.error("Exception occured while changing supplier // Class : ChangeSupplierNameServiceImplV2 // Method : getCMSDefinedRates");
            throw e;
        }catch(Exception e)
        {
            throw new OperationException(Constants.OPS_CS_00007);
        }
    }
    @Override
    public JSONArray getCMS(List<CmsSupplierResource> responseList ,String param){
        try {
            responseList = responseList.parallelStream().filter(x -> x.getName().toLowerCase().contains(param.toLowerCase())).collect(Collectors.toList());
            List<JSONObject> jsonObjects = responseList.parallelStream().map(x -> x.toJsonObject()).collect(Collectors.toList());
            JSONArray jsonArray = new JSONArray();
            for (JSONObject jsonObject : jsonObjects) {
                jsonArray.put(jsonObject);
            }
            return jsonArray;
        }
        catch(Exception e) {
            logger.error("Error Occurred while filtering suppliers");
            return null;
        }


    }

    @Override
    @Cacheable
    public List<CmsSupplierResource> getSupplierElements(String key) throws Exception {
        URL url = new URL(supplierURL);

        Element reqElem = XMLTransformer.toXMLElement(suppAutoSuggReq);
        Element resElem = null;
        try {
            resElem = ChangeSupplierHTTPServiceConsumer.consumeXMLService("Auto Suggest call", url, reqElem);
        }catch(Exception e) {
            logger.error("Error Occurred while Consuming CMS Supplier Dump" + e);
        }

        List<CmsSupplierResource> cmsSupplierResources = new ArrayList<>();
        if(resElem!=null) {
            Element[] contractElems = XMLUtils.getElementsAtXPath(resElem, "./Sucess/CM_HOTEL_CONTRACTS");
            for (Element contractElem : contractElems) {
                String cm_id = XMLUtils.getValueAtXPath(contractElem, "./CM_ID");
                String cm_name = XMLUtils.getValueAtXPath(contractElem, "./CM_NAME");
                String supp_id = XMLUtils.getValueAtXPath(contractElem, "./SUPP_ID");

                cmsSupplierResources.add(new CmsSupplierResource(cm_id, cm_name, supp_id));
            }
        }
        return cmsSupplierResources;
    }
}