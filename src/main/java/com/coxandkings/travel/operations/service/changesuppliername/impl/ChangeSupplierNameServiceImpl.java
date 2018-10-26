package com.coxandkings.travel.operations.service.changesuppliername.impl;

import com.coxandkings.travel.ext.model.be.PaxInfo;
import com.coxandkings.travel.operations.enums.amendclientcommercials.BEInboundOperation;
import com.coxandkings.travel.operations.enums.amendclientcommercials.BEOperationId;
import com.coxandkings.travel.operations.enums.amendclientcommercials.BEServiceUri;
import com.coxandkings.travel.operations.enums.email.EmailPriority;
import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.enums.todo.ToDoFunctionalAreaValues;
import com.coxandkings.travel.operations.enums.todo.ToDoTaskGeneratedTypeValues;
import com.coxandkings.travel.operations.enums.todo.ToDoTaskNameValues;
import com.coxandkings.travel.operations.enums.todo.ToDoTaskOrientedValues;
import com.coxandkings.travel.operations.enums.todo.ToDoTaskPriorityValues;
import com.coxandkings.travel.operations.enums.todo.ToDoTaskStatusValues;
import com.coxandkings.travel.operations.enums.todo.ToDoTaskSubTypeValues;
import com.coxandkings.travel.operations.enums.todo.ToDoTaskTypeValues;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.helper.beconsumption.SupplierDetailAccessor;
import com.coxandkings.travel.operations.model.changesuppliername.DiscountOnSupplierPrice;
import com.coxandkings.travel.operations.model.changesuppliername.SupplementOnSupplierPrice;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.model.productbookedthrother.mdm.B2BClient;
import com.coxandkings.travel.operations.model.productbookedthrother.mdm.B2CClient;
import com.coxandkings.travel.operations.model.refund.finaceRefund.ClientType;
import com.coxandkings.travel.operations.model.todo.ToDoTask;
import com.coxandkings.travel.operations.repository.changesuppliername.DiscountOnSupplierPriceRepository;
import com.coxandkings.travel.operations.repository.changesuppliername.SupplementOnSupplierPriceRepository;
import com.coxandkings.travel.operations.resource.changesuppliername.*;
import com.coxandkings.travel.operations.resource.changesuppliername.request.ClientContext;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.resource.email.EmailWithBodyResource;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResource;
import com.coxandkings.travel.operations.service.amendsuppliercommercial.impl.AmendSupplierCommercialServiceImpl;
import com.coxandkings.travel.operations.service.beconsumer.BeToOpsBookingConverter;
import com.coxandkings.travel.operations.service.beconsumer.acco.AccoBookingEngineConsumptionService;
import com.coxandkings.travel.operations.service.beconsumer.acco.AccoCancellationService;
import com.coxandkings.travel.operations.service.beconsumer.air.AirBookingEngineConsumptionService;
import com.coxandkings.travel.operations.service.bedbservice.acco.AccoBeDBUpdateService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.changesuppliername.ChangeSupplierNameService;
import com.coxandkings.travel.operations.service.email.EmailService;
import com.coxandkings.travel.operations.service.productbookedthrother.mdm.MdmClientService;
import com.coxandkings.travel.operations.service.refund.RefundMDMService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.*;
import com.coxandkings.travel.operations.utils.xml.XMLTransformer;
import com.coxandkings.travel.operations.utils.xml.XMLUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Element;

import javax.transaction.Transactional;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URI;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ChangeSupplierNameServiceImpl implements ChangeSupplierNameService {
    private static Logger logger = LogManager.getLogger(ChangeSupplierNameServiceImpl.class);
   
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
    
    @SuppressWarnings("rawtypes")
	@Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Autowired
    @Qualifier("accoCancellationService")
    private AccoCancellationService accoCancellationService;
    @Autowired
    private BookingEngineElasticData bookingEngineElasticData;

   
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
    
    
    
    @Autowired
    AmendSupplierCommercialServiceImpl amendSupplierCommercialServiceImpl;

    @Value("${booking-engine-core-services.acco.hoteldata}")
    private String redisHotelDataUrl;
    
    @Autowired
    private MdmClientService mdmClientService;
    
    @Autowired
    EmailService emailService;
    
    @Override
    public List<SupplierResource> getSuppliers(String productCategory, String productSubCategory, String clientId, String clientType) throws OperationException {
        return fetchSupplierDetailsFromMdm(productCategory, productSubCategory, clientId, clientType);
    }

    private List<SupplierResource> fetchSupplierDetailsFromMdm(String productCategory, String productSubCategory, String clientId, String clientType) throws OperationException {
        Map<String, String> mdmRequestMap = new HashMap<>();
        mdmRequestMap.put("productCategories.productCategory", productCategory);
        mdmRequestMap.put("productCategories.productCatSubTypes.subType", productSubCategory);
        mdmRequestMap.put("productSupplierAttachedTo.entityId", clientId);
        mdmRequestMap.put("productSupplierAttachedTo.entityType", clientType);
        String request = null;
        try {
            request = objectMapper.writeValueAsString(mdmRequestMap);
            System.out.println(request);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        List<SupplierResource> supplierList = null;
        String fireUrl = this.supplierNamesUrl + request;
        URI uri = UriComponentsBuilder.fromUriString(fireUrl).build().encode().toUri();
        String supplierDetails = null;
        supplierDetails = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);

        supplierList = jsonObjectProvider.getChildrenCollection(supplierDetails, "$.data.*.productCategories.*.productCatSubTypes.*.mappings.*.supplier", SupplierResource.class);

        if (supplierList == null) {
            throw new OperationException(Constants.NO_SUPPLIER_FOUND, productCategory, productSubCategory, clientId, clientType);
        }
        return supplierList;
    }

    @Override
    public Object getRatesForAcco(String bookingRefId, String orderId, String supplierId) throws ParseException, IOException, OperationException, JSONException {

        OpsBooking opsBooking = opsBookingService.getBooking(bookingRefId);
        OpsProduct opsProduct = opsBookingService.getProduct(bookingRefId, orderId);
        SupplierRateDetail supplierRateDetail = new SupplierRateDetail();
        SupplierHeaderRate supplierHeaderRate = new SupplierHeaderRate();
        String rateResult = null;
        JSONObject jsonRateObject = null;

        rateResult = getAccoSupplierDefinedRate(supplierId, opsProduct.getOpsProductSubCategory().getSubCategory());
        jsonRateObject = new JSONObject(rateResult);
        JSONObject defineRate = jsonRateObject.getJSONObject("defineRates");
        if (defineRate != null) {
            supplierHeaderRate.setValidityFrom(defineRate.getString("validityFrom"));
            supplierHeaderRate.setValidityTo(defineRate.getString("validityTo"));
            supplierHeaderRate.setSupplierCurrency(defineRate.getString("currency"));
            supplierHeaderRate.setSupplierMarket(defineRate.getString("supplierMarket"));
            supplierHeaderRate.setTaxesApplicable(defineRate.getBoolean("taxesApplicable"));
        }

        supplierRateDetail.setSupplierHeaderRate(supplierHeaderRate);
        supplierRateDetail.setSupplierBodyRate(jsonRateObject);
        return supplierRateDetail;
    }

    private String getAccoSupplierDefinedRate(String supplierId, String productCategory) throws JsonProcessingException, OperationException {
        String url = this.supplierRateUrl + "accommodation/main?filter=";
        Map<String, String> supplierRateResource = new HashMap<>();
        String completeUrl = null;

        URI uri = null;
        String rateResult = null;

        supplierRateResource.put("supplierId", supplierId);
        supplierRateResource.put("productCategorySubType", productCategory);

        completeUrl = url + objectMapper.writeValueAsString(supplierRateResource);
        uri = UriComponentsBuilder.fromUriString(completeUrl).build().encode().toUri();
        rateResult = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);


        return rateResult;
    }


    private Map<String, String> getSupplierPricingDetailsFromRedisEngine(String key) {
        Map<String, String> supplierDataMap = null;
        supplierDataMap = SupplierDetailAccessor.getSupplierData(key);

        return supplierDataMap;
    }


    private Integer calculateAge(String dob) {

        LocalDate today = LocalDate.now();
        LocalDate birthday = LocalDate.parse(dob);
        Period period = Period.between(birthday, today);
        //Now access the values as below
        System.out.println(period.getDays());
        System.out.println(period.getMonths());
        System.out.println(period.getYears());
        return period.getYears();
    }

    @Override
    public Object getRatesForAir(String bookingRefId, String orderId, String supplierName) throws ParseException, IOException, OperationException, JSONException {

        String priceResultString = null;
        String rePriceResultString = null;
        SupplierRateDetail supplierRateDetail = new SupplierRateDetail();
        SupplierHeaderRate supplierHeaderRate = new SupplierHeaderRate();
        supplierHeaderRate.setSupplierCurrency("INR");
        supplierHeaderRate.setSupplierMarket("India");
        supplierHeaderRate.setTaxesApplicable(true);
        supplierHeaderRate.setValidityTo("04-may-2019");
        supplierHeaderRate.setValidityFrom("02-feb-2018");
        supplierRateDetail.setSupplierHeaderRate(supplierHeaderRate);
        OpsBooking opsBooking = opsBookingService.getBooking(bookingRefId);
        OpsProduct opsProduct = opsBookingService.getProduct(bookingRefId, orderId);
        logger.info("get search method-->");
        //   String bookingDetails = airBookingEngineConsumptionService.search(opsBooking, opsProduct);
        Map<String, String> airMap = null;
        //  if (validateSupplierNameAndGetDetails(bookingDetails, supplierName)) {
        logger.info("get price method-->");
        priceResultString = airBookingEngineConsumptionService.getPriceForAirJson(opsBooking, opsProduct, supplierName);
        logger.info("get price method-->");
        rePriceResultString = airBookingEngineConsumptionService.getRePriceForAirJson(opsBooking, opsProduct, supplierName);
        logger.info("calling Redis engine to get supplier data-->");
        if (airMap != null) {
               /* supplierHeaderRate.setValidityFrom();
                supplierHeaderRate.setValidityTo();
                supplierHeaderRate.setTaxesApplicable();
                supplierHeaderRate.setSupplierMarket();
                supplierHeaderRate.setSupplierCurrency();
                supplierRateDetail.setSupplierHeaderRate();*/
        }
        System.out.println(airMap);

        //  }
        return supplierRateDetail;
    }

    private boolean validateSupplierNameAndGetDetails(String bookingDetails, String supplierName) throws JSONException, IOException {
        boolean result = false;
        JSONObject searchResult = new JSONObject(bookingDetails);
        try {
            JSONArray pricedItinerariesJson = searchResult.getJSONObject("responseBody").getJSONArray("pricedItinerary");
            for (int i = 0; i < pricedItinerariesJson.length(); i++) {
                JSONObject pricedItineraryJson = pricedItinerariesJson.getJSONObject(i);
                if (pricedItineraryJson.getString("supplierRef").equalsIgnoreCase(supplierName)) {
                    //String redisKeyForPricedItinerary = SupplierDetailAccessor.getRedisKeyForPricedItinerary(pricedItineraryJson);

                    return true;
                }
            }


        } catch (Exception e) {
            logger.error("No supplier found in booking search");
            e.printStackTrace();
        }

        return result;
    }


    private List<PaxInfo> getPaxInfoList(OpsProduct product) {
        List<OpsFlightPaxInfo> paxInfos = product.getOrderDetails().getFlightDetails().getPaxInfo();
        List<PaxInfo> paxInfoList = new ArrayList<>();
        Map<String, Integer> paxMap = new HashMap<>();
        for (OpsFlightPaxInfo opsFlightPaxInfo : paxInfos) {
            Integer count = paxMap.get(opsFlightPaxInfo.getPaxType());
            if (count != null) {
                paxMap.put(opsFlightPaxInfo.getPaxType(), ++count);
            } else {
                paxMap.put(opsFlightPaxInfo.getPaxType(), 1);
            }
        }
        for (Map.Entry<String, Integer> entry : paxMap.entrySet()) {
            PaxInfo paxInfo = new PaxInfo();
            paxInfo.setPaxType(entry.getKey());
            paxInfo.setQuantity(entry.getValue().toString());
            paxInfoList.add(paxInfo);
        }
        return paxInfoList;
    }


    @Override
    public Object applyRatesForAir(JSONObject supplierChangeRequest) {

        supplierChangeRequest.getString("bookId");
        supplierChangeRequest.getString("orderId");
        supplierChangeRequest.getString("roomId");

        return null;
    }

    @Override
    public Object applyRatesForAcco(JSONObject supplierChangeRequest) throws ParseException, OperationException {
        String bookId = supplierChangeRequest.getString("bookId");
        String orderId = supplierChangeRequest.getString("orderId");
        String roomId = supplierChangeRequest.getString("roomId");
        String supplierId = supplierChangeRequest.getString("supplierId");
        OpsBooking opsBooking = opsBookingService.getBooking(bookId);
        OpsProduct opsProduct = getOpsProduct(opsBooking, orderId);
        OpsRoom opsRoom = getOpsRoom(opsProduct, roomId);
        opsProduct.getOrderDetails().getHotelDetails().getRooms().clear();
        opsProduct.getOrderDetails().getHotelDetails().getRooms().add(opsRoom);
        String newBookingString = accoBookingEngineConsumptionService.bookFromExistingBooking(opsBooking, opsProduct, supplierId);
        Object canceledRoom = accoBookingEngineConsumptionService.cancelRoom(opsBooking, opsProduct, roomId);

        OpsBooking newOpsBooking = null;
        return "Supplier changed successfully";
    }

    private OpsRoom getOpsRoom(OpsProduct opsProduct, String roomId) throws OperationException {
        Optional<OpsRoom> optOpsRoom = opsProduct.getOrderDetails()
                .getHotelDetails()
                .getRooms()
                .stream()
                .filter(opsRoom -> opsRoom.getRoomID().equalsIgnoreCase(roomId)).findAny();
        if (!optOpsRoom.isPresent()) {
            throw new OperationException("There is no Room Available for roomId=" + roomId);
        }
        return optOpsRoom.get();
    }

    private OpsProduct getOpsProduct(OpsBooking opsBooking, String orderId) throws OperationException {
        Optional<OpsProduct> optionalOpsProduct = opsBooking.getProducts()
                .stream()
                .filter(opsProduct -> opsProduct.getOrderID().equalsIgnoreCase(orderId))
                .findAny();
        if (!optionalOpsProduct.isPresent()) {
            throw new OperationException(Constants.ER32);
        }

        return optionalOpsProduct.get();
    }


    private List<JSONObject> getPaxDetailsJson(OpsProduct opsProduct) throws JSONException {
        List<JSONObject> paxDetails = new ArrayList<>();
        List<OpsFlightPaxInfo> paxInfos = opsProduct.getOrderDetails().getFlightDetails().getPaxInfo();
        if (paxInfos != null) {
            for (OpsFlightPaxInfo opsFlightPaxInfo : paxInfos) {
                JSONObject paxDetail = new JSONObject();
                paxDetail.put("dob", opsFlightPaxInfo.getBirthDate());
                paxDetail.put("firstName", opsFlightPaxInfo.getFirstName());
                paxDetail.put("middleName", opsFlightPaxInfo.getFirstName());
                paxDetail.put("paxType", opsFlightPaxInfo.getPaxType());
                paxDetail.put("title", opsFlightPaxInfo.getTitle());

                // paxDetail.put("gender",opsFlightPaxInfo.get); //todo gender missing
                //  paxDetail.put("surname",opsFlightPaxInfo.getPaxType()); //todo missing surname

                JSONObject addressDetails = getAddressDetailsJson(opsFlightPaxInfo.getAddressDetails());
                JSONObject ancillaryServices = getAncillaryServicesJson(opsFlightPaxInfo.getAncillaryServices());
                JSONObject specialRequests = getSpecialRequestsJson(opsFlightPaxInfo.getOpsSpecialRequest());
                List<JSONObject> contactDetails = getContactDetailsJson(opsFlightPaxInfo.getContactDetails());
                // paxDetail.put("documentDetails",getDocumentDetailsJson()); todo
                paxDetail.put("addressDetails", addressDetails);
                paxDetail.put("specialRequests", specialRequests);
                paxDetail.put("contactDetails", contactDetails);
                paxDetail.put("ancillaryServices", ancillaryServices);
                paxDetails.add(paxDetail);
            }
        }

        return paxDetails;
    }

    private List<JSONObject> getContactDetailsJson(List<OpsContactDetails> contactDetails) throws JSONException {
        List<JSONObject> contactDetailsJson = new ArrayList<>();
        if (contactDetails != null) {
            for (OpsContactDetails opsContactDetails : contactDetails) {
                JSONObject contactDetail = new JSONObject();
                JSONObject contactInfo = new JSONObject(opsContactDetails.getContactInfo());
                contactDetail.put("contactInfo", contactInfo);
                contactDetailsJson.add(contactDetail);
            }
        }
        return contactDetailsJson;
    }

    private JSONObject getSpecialRequestsJson(OpsSpecialRequest opsSpecialRequest) throws JSONException {
        JSONObject specialPriceRequestJson = new JSONObject();
        List<JSONObject> specialRequestInfo = new ArrayList<>();
        List<OpsSpecialRequestInfo> specialRequestInfos = opsSpecialRequest.getSpecialRequestInfo();
        for (OpsSpecialRequestInfo specialRequestInfo1 : specialRequestInfos) {
            JSONObject specialPriceJson = new JSONObject();
            specialPriceJson.put("flightRefNumberRphList", "");
            specialPriceJson.put("amount", "");
            specialPriceJson.put("serviceQuantity", "");
            specialPriceJson.put("destinationLocation", "");
            specialPriceJson.put("originLocation", "");
            specialPriceJson.put("ssrCode", "");
            specialPriceJson.put("flightNumber", "");
            specialPriceJson.put("number", "");
            specialPriceJson.put("type", "");
            JSONObject servicePrice = new JSONObject();
            servicePrice.put("basePrice", "");//todo
            specialPriceJson.put("servicePrice", servicePrice);
            specialPriceJson.put("type", "");
            specialPriceJson.put("currencyCode", "");
            specialPriceJson.put("status", "");
            specialRequestInfo.add(specialPriceJson);
            specialPriceRequestJson.put("specialRequestInfo", specialRequestInfo);
        }
        return specialPriceRequestJson;
    }

    private JSONObject getAncillaryServicesJson(OpsAncillaryServices ancillaryServices) throws JSONException {

        List<JSONObject> ancillaryInfo = new ArrayList<>();
        JSONObject ancillaryJosn = new JSONObject();
        for (OpsAncillaryInfo opsAncillaryInfo : ancillaryServices.getAncillaryInfo()) {
            JSONObject ancillaryJson = new JSONObject();
            ancillaryJson.put("amount", opsAncillaryInfo.getAmount());
            ancillaryJson.put("description", opsAncillaryInfo.getDescription());
            ancillaryJson.put("quantity", opsAncillaryInfo.getQuantity());
            ancillaryJson.put("type", opsAncillaryInfo.getType());
            ancillaryJson.put("unit", opsAncillaryInfo.getUnit());
            ancillaryInfo.add(ancillaryJson);
        }
        ancillaryJosn.put("ancillaryInfo", ancillaryInfo);
        return ancillaryJosn;
    }

    private JSONObject getAddressDetailsJson(OpsAddressDetails addressDetails) throws JSONException {
        JSONObject addressDetail = new JSONObject();
        if (addressDetails != null) {
            addressDetail.put("city", addressDetails.getCityName());
            addressDetail.put("country", addressDetails.getCountryName());
            addressDetail.put("state", addressDetails.getStateName());
            addressDetail.put("zip", addressDetails.getZipCode());
            if (addressDetails.getAddressLines().size() >= 2)
                addressDetail.put("addrLine1", addressDetails.getAddressLines().get(0));
            addressDetail.put("addrLine2", addressDetails.getAddressLines().get(1));

        }
        return addressDetail;
    }

    private List<JSONObject> getPricedItineraryListJson(OpsProduct opsProduct, String supplierName) throws JSONException {
        OpsFlightDetails flightDetails = opsProduct.getOrderDetails().getFlightDetails();
        List<JSONObject> pricedItineraryList = new ArrayList<>();
        JSONObject pricedItinerary = new JSONObject();
        pricedItinerary.put("isReturnJourneyCombined", true);
        JSONObject airItinerary = new JSONObject();
        List<JSONObject> originDestinationOptions = new ArrayList<>();
        List<OpsOriginDestinationOption> opsOriginDestinationOptions = flightDetails.getOriginDestinationOptions();

        if (opsOriginDestinationOptions != null) {
            for (OpsOriginDestinationOption opsOriginDestinationOption : opsOriginDestinationOptions) {
                JSONObject originDestinationOption = new JSONObject();
                List<JSONObject> flightSegmentList = new ArrayList<>();
                List<OpsFlightSegment> opsFlightSegments = opsOriginDestinationOption.getFlightSegment();
                if (opsFlightSegments != null) {
                    for (OpsFlightSegment opsFlightSegment : opsFlightSegments) {
                        JSONObject flightSegment = new JSONObject();
                        flightSegment.put("arrivalTerminal", opsFlightSegment.getArrivalTerminal());
                        flightSegment.put("availableCount", opsFlightSegment.getAvailableCount());
                        flightSegment.put("cabinType", opsFlightSegment.getCabinType());
                        // flightSegment.setConnectionType(opsFlightSegment.get); todo
                        //flightSegment.0(opsFlightSegment.getArrivalDateZDT()); todo
                        flightSegment.put("arrivalDate", opsFlightSegment.getArrivalDateZDT().toOffsetDateTime().toString());
                        flightSegment.put("connectionType", "");
                        flightSegment.put("departureDate", opsFlightSegment.getDepartureDateZDT().toOffsetDateTime().toString());
                        flightSegment.put("cabinType", opsFlightSegment.getCabinType());
                        flightSegment.put("departureTerminal", opsFlightSegment.getDepartureTerminal());
                        flightSegment.put("destinationLocation", opsFlightSegment.getDestinationLocation());
                        flightSegment.put("extendedRPH", opsFlightSegment.getExtendedRPH());
                        flightSegment.put("journeyDuration", opsFlightSegment.getJourneyDuration().intValue());
                        flightSegment.put("refundableIndicator", true); // todo
                        flightSegment.put("resBookDesigCode", "N");
                        flightSegment.put("rph", "");

                        JSONObject marketingAirline = new JSONObject();
                        if (opsFlightSegment.getMarketingAirline() != null) {
                            marketingAirline.put("airLineCode", opsFlightSegment.getMarketingAirline().getAirlineCode());
                            marketingAirline.put("flightNumber", opsFlightSegment.getMarketingAirline().getFlightNumber());
                        }
                        flightSegment.put("marketingAirline", marketingAirline);
                        OpsOperatingAirline opsOperatingAirline = opsFlightSegment.getOperatingAirline();
                        JSONObject operatingAirline = new JSONObject();
                        if (opsOperatingAirline != null) {
                            operatingAirline.put("airlineCode", opsOperatingAirline.getAirlineCode());
                            operatingAirline.put("flightNumber", opsOperatingAirline.getFlightNumber());
                            flightSegment.put("operatingAirline", operatingAirline);
                        }
                        flightSegment.put("originLocation", opsFlightSegment.getOriginLocation());
                        flightSegment.put("quoteID", opsFlightSegment.getQuoteID());
                        flightSegmentList.add(flightSegment);
                    }
                    originDestinationOption.put("flightSegment", flightSegmentList);
                }
                originDestinationOptions.add(originDestinationOption);
            }
        }
        airItinerary.put("originDestinationOptions", originDestinationOptions);
        pricedItinerary.put("airItinerary", airItinerary);
        if (!StringUtils.isEmpty(supplierName)) {
            pricedItinerary.put("supplierRef", supplierName);
        } else {
            pricedItinerary.put("supplierRef", opsProduct.getSupplierID());
        }
        pricedItineraryList.add(pricedItinerary);
        return pricedItineraryList;
    }

    private List<JSONObject> getPaxInfoListJson(OpsProduct opsProduct) throws JSONException {
        List<OpsFlightPaxInfo> paxInfos = opsProduct.getOrderDetails().getFlightDetails().getPaxInfo();
        List<JSONObject> paxInfoList = new ArrayList<>();
        Map<String, Integer> paxMap = new HashMap<>();
        for (OpsFlightPaxInfo opsFlightPaxInfo : paxInfos) {
            Integer count = paxMap.get(opsFlightPaxInfo.getPaxType());
            if (count != null) {
                paxMap.put(opsFlightPaxInfo.getPaxType(), ++count);
            } else {
                paxMap.put(opsFlightPaxInfo.getPaxType(), 1);
            }
        }
        for (Map.Entry<String, Integer> entry : paxMap.entrySet()) {
            JSONObject paxInfo = new JSONObject();
            paxInfo.put("paxType", entry.getKey());
            paxInfo.put("quantity", entry.getValue());
            paxInfoList.add(paxInfo);
        }
        return paxInfoList;
    }

    private JSONObject getRequestHeader(OpsBooking opsBooking) throws JSONException {
        JSONObject requestHeader = new JSONObject();

        requestHeader.put("sessionID", opsBooking.getSessionID());
        requestHeader.put("transactionID", opsBooking.getTransactionID());
        requestHeader.put("userID", opsBooking.getUserID());
        requestHeader.put("clientContext", getClientContextJson(opsBooking));
        return requestHeader;
    }

    private JSONObject getClientContextJson(OpsBooking opsBooking) throws JSONException {
        JSONObject clientContext = new JSONObject();
        clientContext.put("clientID", opsBooking.getClientID());
        clientContext.put("clientMarket", opsBooking.getClientMarket());
        clientContext.put("clientType", opsBooking.getClientType());
        clientContext.put("clientLanguage", opsBooking.getClientLanguage());
        clientContext.put("clientIATANumber", "1234");
        clientContext.put("pointOfSale", opsBooking.getPointOfSale());
        clientContext.put("clientNationality", ""); // todo be not sending currently
        clientContext.put("clientCallbackAddress", "");// todo be not sending currently
        clientContext.put("clientCurrency", opsBooking.getClientCurrency());
        return clientContext;
    }


    @Override
    public Object getRePriceForAcco(String bookId, String orderId) {
        try {
            OpsBooking opsBooking = opsBookingService.getBooking(bookId);
            OpsProduct opsProduct = opsBookingService.getProduct(bookId, orderId);
            accoBookingEngineConsumptionService.getReprice(opsBooking, opsProduct, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public JSONObject getSupplierRates(OpsProductCategory productCategory, OpsProductSubCategory productSubCategory, String supplierId) throws JSONException, JsonProcessingException, OperationException {
        OpsProductSubCategory opsProductSubCategory = OpsProductSubCategory.getProductSubCategory(productCategory, productSubCategory.getSubCategory());
        switch (opsProductSubCategory) {
            case PRODUCT_SUB_CATEGORY_HOTELS:
                return new JSONObject(getAccoSupplierDefinedRate(supplierId, productSubCategory.getSubCategory()));


            case PRODUCT_SUB_CATEGORY_FLIGHT:
                return new JSONObject(getAirSupplierDefinedRate(supplierId, productSubCategory.getSubCategory()));

        }


        return null;
    }


    @Override
    public ChangedSupplierPriceResource getDiscountOrAddSupplementOnSupplierPrice(JSONObject resource) throws OperationException {
    	
        OpsBooking opsOriginalBooking = null;
        OpsProduct opsOriginalProduct = null;
        final String bookId = resource.getString("bookId");
        final String orderId = resource.getString("orderId");
        OpsBooking updatedBooking = null;
        opsOriginalBooking = opsBookingService.getBooking(bookId);
        opsOriginalProduct = opsBookingService.getOpsProduct(opsOriginalBooking,orderId);
        resource.put("clientId", opsOriginalBooking.getClientID());
        resource.put("companyId", opsOriginalBooking.getCompanyId());
        resource.put("clientType", opsOriginalBooking.getClientType());
        
        OpsProductCategory opsProductCategory = OpsProductCategory.getProductCategory(opsOriginalProduct.getProductCategory());
        OpsProductSubCategory opsProductSubCategory = OpsProductSubCategory.getProductSubCategory(opsProductCategory, opsOriginalProduct.getProductSubCategory());
        
        Element xmlData = null;
        JSONObject requestJson = null;
        JSONObject opsAmendement = null;
        JSONObject resJson = null;
      
        Optional<OpsProduct> opsUpdatedProductOpt = null;
        OpsProduct opsUpdatedProduct = null;
        
        switch (opsProductSubCategory) {
        
            case PRODUCT_SUB_CATEGORY_HOTELS:
                String roomId = resource.getString("roomId");
                
                Map<String, BigDecimal> pricingComponentMap = processDiscountOrSupplementAndGetNewSupplierPriceForAcco(resource, opsOriginalProduct);
                xmlData = bookingEngineElasticData.getXMLData(opsOriginalBooking.getUserID(), opsOriginalBooking.getTransactionID(),
                        opsOriginalBooking.getSessionID(),
                        BEInboundOperation.REPRICE,
                        BEOperationId.SUPPLIER_INTEGRATION_RS,
                        BEServiceUri.HOTEL);
                requestJson = bookingEngineElasticData.getJSONData(opsOriginalBooking.getUserID(), opsOriginalBooking.getTransactionID(),
                        opsOriginalBooking.getSessionID(),
                        BEInboundOperation.REPRICE,
                        BEOperationId.BOOKING_ENGINE_RQ,
                        BEServiceUri.HOTEL);

                System.out.println(XMLTransformer.toString(xmlData));
                Optional<OpsRoom> opsRoomOpt = opsOriginalProduct.getOrderDetails()
                        .getHotelDetails()
                        .getRooms()
                        .parallelStream()
                        .filter(opsRoom -> opsRoom.getRoomID().equalsIgnoreCase(resource.getString("roomId")))
                        .findAny();
                if (!opsRoomOpt.isPresent()) {
                    throw new OperationException(String.format("Order <%s> not found", orderId));
                }
                OpsRoom opsRoom = opsRoomOpt.get();
                //  String keyForRoomFromReq = beToOpsBookingConverter.getOpsKeyForReq(opsProduct, opsRoom, opsProduct.getOrderDetails().getHotelDetails());

                /*JSONArray accommodationInfos = requestJson.getJSONObject("requestBody").getJSONArray("accommodationInfo");
                JSONObject accommodationInfo = null;
                for (int i = 0; i < accommodationInfos.length(); i++) {
                    String beKeyForReq = beToOpsBookingConverter.getBEKeyForReq(accommodationInfos.getJSONObject(i));
                    if (keyForRoomFromReq.equalsIgnoreCase(beKeyForReq)) {
                        accommodationInfo = accommodationInfos.getJSONObject(i);
                        requestJson.getJSONObject("requestBody").getJSONArray("accommodationInfo").put(accommodationInfo);
                        break;
                    }
                  //  accommodationInfos.remove(i);
                }*/

             //   XMLTransformer.toString(xmlData);


                String opsRoomKey = beToOpsBookingConverter.getOpsKeyForRoomStay(opsRoom, opsOriginalProduct.getOrderDetails().getHotelDetails());
                JSONObject roomInfoJson = new JSONObject();

                Element[] roomStays = XMLUtils.getElementsAtXPath(xmlData, "./accoi:ResponseBody/acco:OTA_HotelAvailRSWrapper/ota:OTA_HotelAvailRS/ota:RoomStays/ota:RoomStay");
                for (Element roomStay : roomStays) {
                    Element roomTypeElement = XMLUtils.getFirstElementAtXPath(roomStay, "./ota:RoomTypes/ota:RoomType");//roomTypeInfo
                    Element ratePlanInfoElement = XMLUtils.getFirstElementAtXPath(roomStay, "./ota:RatePlans/ota:RatePlan");//ratePlanInfoElement
                    Element hotelInfoElement = XMLUtils.getFirstElementAtXPath(roomStay, "./ota:BasicPropertyInfo");//hotelInfoElement
                    JSONObject roomTypeJson = new JSONObject();
                    JSONObject hotelInfoJson = new JSONObject();
                    JSONObject ratePlanInfoJson = new JSONObject();

                    
                    String roomTypeCode = roomTypeElement.getAttribute("RoomTypeCode");
                    String roomRef = roomTypeElement.getAttribute("RoomID");
                    Element tPA_Extensions = XMLUtils.getFirstElementAtXPath(roomTypeElement, "./ota:TPA_Extensions");
                    String roomCategory = XMLUtils.getValueAtXPath(tPA_Extensions, "./acco:RoomCategoryID");

                    roomTypeJson.put("roomTypeCode", roomTypeCode);
                    roomTypeJson.put("roomRef", roomRef);
                    roomTypeJson.put("roomCategoryCode", roomCategory);
                    String ratePlanCode = ratePlanInfoElement.getAttribute("RatePlanCode");
                    ratePlanInfoJson.put("ratePlanCode", ratePlanCode);
                    String hotelCode = hotelInfoElement.getAttribute("HotelCode");
                    hotelInfoJson.put("hotelCode", hotelCode);

                    roomInfoJson.put("roomTypeInfo", roomTypeJson);
                    roomInfoJson.put("hotelInfo", hotelInfoJson);
                    roomInfoJson.put("ratePlanInfo", ratePlanInfoJson);

                    String beKeyForRoomStay = beToOpsBookingConverter.getBEKeyForRoomStay(roomInfoJson);
                    if (beKeyForRoomStay.equalsIgnoreCase(opsRoomKey)) {


                        Element total = XMLUtils.getFirstElementAtXPath(roomStay, "./ota:Total");
                        if (pricingComponentMap.containsKey("AmountAfterTax"))
                            total.setAttribute("AmountAfterTax", pricingComponentMap.get("AmountAfterTax").toString());
                        Element taxes[] = XMLUtils.getElementsAtXPath(total, "./ota:Taxes/ota:Tax");
                        BigDecimal totalTaxes=new BigDecimal(0);
                        for (Element tax : taxes) {

                            BigDecimal amount = new BigDecimal(tax.getAttribute("Amount"));
                            String TaxType = tax.getAttribute("Type");
                            if (pricingComponentMap.containsKey(TaxType)) {
                                amount= pricingComponentMap.get(TaxType);
                                tax.setAttribute("Amount", amount.toString());
                            }
                            totalTaxes=totalTaxes.add(amount);
                            //TODO how to change total tax amount
                        }
                        break;
                    }

                }


                opsAmendement = new JSONObject();
                opsAmendement.put("siRepriceResponse", XMLTransformer.toString(xmlData));
                opsAmendement.put("actionItem", "discountOrSupplementOnSupplierPrice");
                opsAmendement.put("bookingDate", DateTimeFormatter.ofPattern("yyyy-MM-dd'T00:00:00'").format(opsOriginalBooking.getBookingDateZDT()));
                
                
                String hotelCode=opsOriginalProduct.getOrderDetails().getHotelDetails().getHotelCode();
               
                Map<String, Object> hotelAttrs=null;
                try {
                	
            		hotelAttrs = RestUtils.getForObject(redisHotelDataUrl+hotelCode,Map.class);
                }
                catch(Exception e) {
                	logger.error(e.getMessage(),e);
                	throw new OperationException(Constants.CANNOT_GET_HOTEL_DATA);
                }
                opsAmendement.put("supplierId", opsOriginalProduct.getSupplierID());
                opsAmendement.put("hotelName", hotelAttrs.getOrDefault("name", ""));
                opsAmendement.put("roomTypeName", opsRoom.getRoomTypeInfo().getRoomTypeName());
                opsAmendement.put("roomCategoryName", opsRoom.getRoomTypeInfo().getRoomCategoryName());
                opsAmendement.put("ratePlanName", opsRoom.getRatePlanInfo().getRatePlanname());
                opsAmendement.put("ratePlanCode", opsRoom.getRatePlanInfo().getRatePlanCode());
                opsAmendement.put("bookingId", bookId);
                opsAmendement.put("ineligibleCommercials", new JSONArray());
                
                requestJson.getJSONObject("requestHeader").put("userID", userService.getLoggedInUserId());
                requestJson.getJSONObject("requestBody").put("opsAmendments", opsAmendement);
                resJson = consumeBEOpsReprice(requestJson, opsProductSubCategory);
                if (resJson == null) {
                    throw new OperationException("Failed to get reprice Response from BE");
                }
                updatedBooking = beToOpsBookingConverter.updateAccoOrder(requestJson, resJson, bookId, orderId, resource.getString("roomId"));

                opsUpdatedProductOpt = updatedBooking.getProducts().
                        parallelStream().
                        filter(orders -> orders.getOrderID().equals(orderId)).
                        findAny();
                if (!opsUpdatedProductOpt.isPresent())

                {
                    throw new OperationException(String.format("Order <%s> not found", orderId));
                }
                opsUpdatedProduct = opsUpdatedProductOpt.get();
                Optional<OpsRoom> optRoom = opsUpdatedProduct.getOrderDetails().getHotelDetails().getRooms()
                        .stream().filter(room -> room.getRoomID().equalsIgnoreCase(roomId)).findAny();
                opsUpdatedProduct.getOrderDetails().getHotelDetails().getRooms().clear(); // clear all the rooms

                if (!optRoom.isPresent())

                {
                    throw new OperationException(String.format("Room <%s> not found", roomId));
                }
                optRoom.get().setOpsClientEntityCommercial(opsRoom.getOpsClientEntityCommercial());
                optRoom.get().setRoomTotalPriceInfo(opsRoom.getRoomTotalPriceInfo());
                opsUpdatedProduct.getOrderDetails().getHotelDetails().getRooms().add(optRoom.get());  // update only given roomId
                opsUpdatedProduct.getOrderDetails().setClientCommercials(opsOriginalProduct.getOrderDetails().getClientCommercials());
                opsUpdatedProduct.getOrderDetails().getHotelDetails().setOpsAccommodationTotalPriceInfo(opsOriginalProduct.getOrderDetails().getHotelDetails().getOpsAccommodationTotalPriceInfo());
                break;
                
                
            case PRODUCT_SUB_CATEGORY_FLIGHT:
            	
                Map<String, BigDecimal> pricingComponentMapForFlight = processDiscountOrSupplementAndGetNewSupplierPriceForAir(resource, opsOriginalProduct);
                
                xmlData = bookingEngineElasticData.getXMLData(opsOriginalBooking.getUserID(), opsOriginalBooking.getTransactionID(),
                        opsOriginalBooking.getSessionID(),
                        BEInboundOperation.REPRICE,
                        BEOperationId.SUPPLIER_INTEGRATION_RS,
                        BEServiceUri.FLIGHT);

                requestJson = bookingEngineElasticData.getJSONData(opsOriginalBooking.getUserID(), opsOriginalBooking.getTransactionID(),
                        opsOriginalBooking.getSessionID(),
                        BEInboundOperation.REPRICE,
                        BEOperationId.BOOKING_ENGINE_RQ,
                        BEServiceUri.FLIGHT);  // todo can we take reprice request json from kibana or opsOriginalBooking
                
                JSONObject beRepriceRs = bookingEngineElasticData.getJSONData(opsOriginalBooking.getUserID(), opsOriginalBooking.getTransactionID(),
                		opsOriginalBooking.getSessionID(), BEInboundOperation.REPRICE, BEOperationId.BOOKING_ENGINE_RS, BEServiceUri.FLIGHT);
                
                
               
                String opsKeyForPricedItinerary = beToOpsBookingConverter.getOpsKeyForPricedItinerary(opsOriginalProduct);

                String paxType = resource.getString("paxType");
                
                Element[] airRSWrappers = XMLUtils.getElementsAtXPath(xmlData, "./airi:ResponseBody/air:OTA_AirPriceRSWrapper");
                for (Element airRSWrapper : airRSWrappers) {
                    String supplierId = XMLUtils.getValueAtXPath(airRSWrapper, "./air:SupplierID");
                    Element airPriceResponse = XMLUtils.getFirstElementAtXPath(airRSWrapper, "./ota:OTA_AirPriceRS");
                    String keyFromRepriceResponse = beToOpsBookingConverter.getKeyFormSupplierPricingResponse(airPriceResponse, supplierId);

                   if (opsKeyForPricedItinerary.equalsIgnoreCase(keyFromRepriceResponse)) {

                        changeSupplierPricingForAir(airPriceResponse, opsOriginalProduct, pricingComponentMapForFlight, paxType);
                    }
                }

                //System.out.println(XMLTransformer.toString(xmlData));
                opsAmendement = new JSONObject();
                opsAmendement.put("siRepriceResponse", XMLTransformer.toString(xmlData));
                opsAmendement.put("actionItem", "discountOrSupplementOnSupplierPrice");
                opsAmendement.put("bookingDate", DateTimeFormatter.ofPattern("yyyy-MM-dd'T00:00:00'").format(opsOriginalBooking.getBookingDateZDT()));
                opsAmendement.put("supplierId", opsOriginalProduct.getSupplierID());
                opsAmendement.put("paxType", paxType);
                opsAmendement.put("bookingId", bookId);
                opsAmendement.put("journeyDetailsIdx", getJourneyDetailsIndex(opsOriginalProduct,beRepriceRs));
                opsAmendement.put("ineligibleCommercials", new JSONArray());
                
                
                requestJson.getJSONObject("requestBody").put("opsAmendments", opsAmendement);
                resJson = consumeBEOpsReprice(requestJson, opsProductSubCategory);
                if (resJson == null) {
                    throw new OperationException("Failed to get reprice Response from BE");
                }
                updatedBooking = beToOpsBookingConverter.updateAirOrder(requestJson, resJson, bookId, orderId, paxType);

                opsUpdatedProductOpt = updatedBooking.getProducts().
                        parallelStream().
                        filter(orders -> orders.getOrderID().equals(orderId)).
                        findAny();
                if (!opsUpdatedProductOpt.isPresent())

                {
                    throw new OperationException(String.format("Order <%s> not found", orderId));
                }
                opsUpdatedProduct=opsUpdatedProductOpt.get();
                opsUpdatedProduct.getOrderDetails().setClientCommercials(opsOriginalProduct.getOrderDetails().getClientCommercials());
                opsUpdatedProduct.getOrderDetails().getFlightDetails().setTotalPriceInfo(opsOriginalProduct.getOrderDetails().getFlightDetails().getTotalPriceInfo());
                break;
		default:
			break;
        }


        
        ChangedSupplierPriceResource changedSupplierPriceResource = new ChangedSupplierPriceResource();
        changedSupplierPriceResource.setOpsProduct(opsUpdatedProduct);
        changedSupplierPriceResource.setResource(resource);
        changedSupplierPriceResource.setRevisedPrice(amendSupplierCommercialServiceImpl.calculateMargin(updatedBooking,opsUpdatedProduct));
        changedSupplierPriceResource.setOldPrice(amendSupplierCommercialServiceImpl.calculateMargin(opsOriginalBooking,opsOriginalProduct));
        return changedSupplierPriceResource;
    }

    private int getJourneyDetailsIndex(OpsProduct opsProduct, JSONObject beRepriceRs) {
        JSONArray pricedItinJsonArr = beRepriceRs.getJSONObject("responseBody").getJSONArray("pricedItinerary");
        Map<String, Integer> suppIndexMap = new HashMap<String, Integer>();
        String opsKey = beToOpsBookingConverter.getOpsKeyForPricedItinerary(opsProduct);
        for (int i = 0; i < pricedItinJsonArr.length(); i++) {
            JSONObject pricedItinJson = pricedItinJsonArr.getJSONObject(i);
            String suppID = pricedItinJson.getString("supplierRef");
            int idx = (suppIndexMap.containsKey(suppID)) ? (suppIndexMap.get(suppID) + 1) : 0;
            suppIndexMap.put(suppID, idx);
            String beKey = beToOpsBookingConverter.getBEKeyForPricedItinerary(pricedItinJson);
            if (opsKey.equals(beKey)) {
                return suppIndexMap.get(suppID);
            }
        }
        return -1;
    }

    /*private SupplierPriceResource calculateMargin(OpsProduct opsUpdatedOrder) throws OperationException {
        BigDecimal totalSellingPrice;
        BigDecimal totalSupplierPrice;
        BigDecimal margin = new BigDecimal(0);
        BigDecimal netPayableToSupplier = new BigDecimal(0);
        BigDecimal receivableAmounts = new BigDecimal(0);
        BigDecimal payableAmounts = new BigDecimal(0);
        String totalPrice = null;
        JSONObject pricingDetail = new JSONObject();
        String supplierCurrency = null;

        String clientCurrency=null;
        BigDecimal roe = opsUpdatedOrder.getRoe();
        String supplierPrice = null;
        OpsOrderDetails orderDetails = opsUpdatedOrder.getOrderDetails();
        OpsProductCategory opsProductCategory = OpsProductCategory.getProductCategory(opsUpdatedOrder.getProductCategory());
        OpsProductSubCategory opsProductSubCategory = OpsProductSubCategory.getProductSubCategory(opsProductCategory, opsUpdatedOrder.getProductSubCategory());

        switch (opsProductSubCategory) {
            case PRODUCT_SUB_CATEGORY_HOTELS:
                totalPrice = orderDetails.getHotelDetails().getOpsAccommodationTotalPriceInfo().getTotalPrice();
                supplierPrice = orderDetails.getHotelDetails().getOpsAccoOrderSupplierPriceInfo().getSupplierPrice();
                supplierCurrency = orderDetails.getHotelDetails().getOpsAccoOrderSupplierPriceInfo().getCurrencyCode();
                clientCurrency=orderDetails.getHotelDetails().getOpsAccommodationTotalPriceInfo().getCurrencyCode();
                break;
            case PRODUCT_SUB_CATEGORY_FLIGHT:
                totalPrice = orderDetails.getFlightDetails().getTotalPriceInfo().getTotalPrice();
                supplierPrice = orderDetails.getFlightDetails().getOpsFlightSupplierPriceInfo().getSupplierPrice();
                supplierCurrency=orderDetails.getFlightDetails().getOpsFlightSupplierPriceInfo().getCurrencyCode();
                clientCurrency=orderDetails.getFlightDetails().getTotalPriceInfo().getCurrencyCode();
                break;
        }

        List<OpsOrderSupplierCommercial> supplierCommercials = orderDetails.getSupplierCommercials();
        receivableAmounts = new BigDecimal(0);
        payableAmounts = new BigDecimal(0);

        for (OpsOrderSupplierCommercial opsOrderSupplierCommercial : supplierCommercials) {
            BigDecimal convertedPrice=null;
            if(!supplierCurrency.equalsIgnoreCase(clientCurrency)){
                convertedPrice=  new BigDecimal(opsOrderSupplierCommercial.getCommercialAmount()).multiply(roe);
            }else
            {
                convertedPrice=new BigDecimal(opsOrderSupplierCommercial.getCommercialAmount()) ;
            }
            if (opsOrderSupplierCommercial.getCommercialType().equalsIgnoreCase("Receivable")) {
                String commercialAmount = opsOrderSupplierCommercial.getCommercialAmount();
                receivableAmounts.add(convertedPrice);
            }
            if (opsOrderSupplierCommercial.getCommercialType().equalsIgnoreCase("Payable")) {
                payableAmounts.add(convertedPrice);
            }
        }
        try {
            totalSellingPrice = new BigDecimal(totalPrice);
            totalSupplierPrice = new BigDecimal(supplierPrice);
        } catch (NumberFormatException ex) {
            throw new OperationException("Conversion of string to BigDecimal failed ");
        }
        if(clientCurrency!=supplierCurrency){
            totalSupplierPrice= totalSupplierPrice.multiply(roe);
        }

        margin = new BigDecimal(0);
        netPayableToSupplier = new BigDecimal(0);

        netPayableToSupplier = totalSupplierPrice.subtract(receivableAmounts).add(payableAmounts);
        margin = totalSellingPrice.subtract(netPayableToSupplier);
        SupplierPriceResource supplierPriceResource = new SupplierPriceResource();
        supplierPriceResource.setMargin(margin);
        supplierPriceResource.setNetPayableToSupplier(netPayableToSupplier);
        supplierPriceResource.setTotalSellingPrice(totalSellingPrice);
        logger.info("receivableAmounts {}",receivableAmounts);
        logger.info("payableAmounts {}",payableAmounts);
        logger.info("netPayableToSupplier {}",netPayableToSupplier);
        logger.info("totalSellingPrice {}",totalSellingPrice);
        logger.info("Margin {}",margin);
        return supplierPriceResource;

    }

*/    
    private void changeSupplierPricingForAir(Element airPriceResponse, OpsProduct opsProduct, Map<String, BigDecimal> pricingComponentMap, String paxType) {

        Element fareBreakDowns[] = XMLUtils.getElementsAtXPath(airPriceResponse, "./ota:PricedItineraries/ota:PricedItinerary/ota:AirItineraryPricingInfo/ota:PTC_FareBreakdowns/ota:PTC_FareBreakdown");
        for (Element fareBreakdown : fareBreakDowns) {
            String passengerTypeCode = XMLUtils.getFirstElementAtXPath(fareBreakdown, "./ota:PassengerTypeQuantity").getAttribute("Code");
            if (passengerTypeCode.equalsIgnoreCase(paxType)) {

                Element passengerFareRes = XMLUtils.getFirstElementAtXPath(fareBreakdown, "./ota:PassengerFare");
                Element baseFare = XMLUtils.getFirstElementAtXPath(passengerFareRes, "./ota:BaseFare");

                if (pricingComponentMap.containsKey("BaseFare")) {
                    baseFare.setAttribute("Amount", pricingComponentMap.get("BaseFare").toString());
                }


                Element taxesRes[] = XMLUtils.getElementsAtXPath(passengerFareRes, "./ota:Taxes/ota:Tax");
                if (taxesRes != null) {
                    for (Element tax : taxesRes) {
                        String taxCode = null;
                        try {
                            taxCode = tax.getAttribute("TaxCode");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (taxCode != null) {
                            if(pricingComponentMap.containsKey(taxCode))
                            {
                                BigDecimal taxAmount = pricingComponentMap.get(taxCode);
                                if (taxAmount != null) {
                                    tax.setAttribute("Amount", taxAmount.toString());
                                }
                            }

                        }
                    }
                }
                Element feesRes[] = XMLUtils.getElementsAtXPath(passengerFareRes, "./ota:Fees/ota:Fee");
                if (feesRes != null) {
                    String feeCode = null;

                    for (Element fee : feesRes) {
                        try {
                            feeCode = fee.getAttribute("FeeCode");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (feeCode != null) {
                            if(pricingComponentMap.containsKey(feeCode))
                            {
                                BigDecimal feeCodeValue = pricingComponentMap.get(feeCode);
                                if (feeCodeValue != null) {
                                    fee.setAttribute("Amount", feeCodeValue.toString());
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    /*private JSONObject calculateMarginForAcco(JSONObject roomStayInfo) {
        try {
            JSONObject supplierTotalPriceInfo = roomStayInfo.getJSONObject("supplierTotalPriceInfo");
            Double totalSupplierPrice = supplierTotalPriceInfo.getDouble("amount");
            JSONObject totalPriceInfo = roomStayInfo.getJSONObject("totalPriceInfo");
            Double totalSellingPrice = totalPriceInfo.getDouble("amount");
            JSONArray supplierCommercials = roomStayInfo.getJSONArray("supplierCommercials");
            Double receivableAmount = 0.0;
            Double payableAmount = 0.0;
            Double netPayableToSupplier = 0.0;
            Double margin = 0.0;
            JSONObject supplierPricingDetail = new JSONObject();

            for (int i = 0; i < supplierCommercials.length(); i++) {
                JSONObject supplierCommercial = supplierCommercials.getJSONObject(i);
                if (supplierCommercial.getString("commercialType").equalsIgnoreCase("Receivable")) {
                    Double commercialAmount = supplierCommercial.getDouble("commercialAmount");
                    receivableAmount = receivableAmount + commercialAmount;
                }
                if (supplierCommercial.getString("commercialType").equalsIgnoreCase("Payable")) {
                    payableAmount = payableAmount + supplierCommercial.getDouble("commercialAmount");

                }
            }

            netPayableToSupplier = totalSupplierPrice - receivableAmount + payableAmount;
            margin = totalSellingPrice - netPayableToSupplier;
            supplierPricingDetail.put("margin", margin);
            supplierPricingDetail.put("netPayableToSupplier", netPayableToSupplier);
            supplierPricingDetail.put("totalSellingPrice", totalSellingPrice);
            return supplierPricingDetail;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }*/

    private JSONObject consumeBEOpsReprice(JSONObject siRepriceRQ, OpsProductSubCategory opsProdSubCateogry) throws OperationException {
        JSONObject beRepriceRespJson = null;
        RestTemplate restTemplate = RestUtils.getTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(siRepriceRQ.toString(), headers);
        ResponseEntity<String> searchResponseEntity = null;
        String url = null;
        switch (opsProdSubCateogry) {
            case PRODUCT_SUB_CATEGORY_BUS:
                break;
            case PRODUCT_SUB_CATEGORY_CAR:
                break;
            case PRODUCT_SUB_CATEGORY_FLIGHT:
                url = this.airBookingEngineOpsRepriceUrl;
                break;
            case PRODUCT_SUB_CATEGORY_HOTELS:
                url = this.accoBookingEngineOpsRepriceUrl;
                break;
            case PRODUCT_SUB_CATEGORY_INDIAN_RAIL:
                break;
            case PRODUCT_SUB_CATEGORY_RAIL:
                break;
            default:
                break;

        }
        try {
            searchResponseEntity =
                    restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
            beRepriceRespJson = new JSONObject(searchResponseEntity.getBody());
            logger.info("Booking Engine Reprice Response : " + beRepriceRespJson.toString());
        } catch (RestClientException e) {
            logger.error(e.getMessage(), e);
            throw new OperationException(Constants.BE_OPS_SERVICE_ERROR, url);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
            throw new OperationException(Constants.BE_OPS_SERVICE_ERROR, url);
        }
        return beRepriceRespJson;
    }


    private Map<String, BigDecimal> processDiscountOrSupplementAndGetNewSupplierPriceForAir(JSONObject discountOnSupplierPriceResource, OpsProduct opsProduct) {
        try {
        	BigDecimal amount = null;
        	BigDecimal percentage = null;
        	String paxType = discountOnSupplierPriceResource.getString("paxType");
        	String operation = discountOnSupplierPriceResource.getString("operation");
            Map<String, BigDecimal> pricingComponentMap = new HashMap<>();

            OpsFlightSupplierPriceInfo opsFlightSupplierPriceInfo = opsProduct.getOrderDetails().getFlightDetails().getOpsFlightSupplierPriceInfo();
            List<OpsPaxTypeFareFlightSupplier> paxTypeFares = opsFlightSupplierPriceInfo.getPaxTypeFares();
            Optional<OpsPaxTypeFareFlightSupplier> optOpsPaxTypeFareFlightSupplier=paxTypeFares.parallelStream().filter(paxTypeFare->paxTypeFare.getPaxType().equalsIgnoreCase(paxType)).findAny();
            
            if(!optOpsPaxTypeFareFlightSupplier.isPresent()) {
            	throw new OperationException("PAX Type Not Found");
            }
            
            OpsPaxTypeFareFlightSupplier opsPaxTypeFareFlightSupplier=optOpsPaxTypeFareFlightSupplier.get();
            
            JSONArray supplierPriceComponentsJson = discountOnSupplierPriceResource.getJSONArray("supplierPriceComponents");
            ArrayList<String> supplierPriceComponents = new ArrayList<>();
            if (supplierPriceComponentsJson != null) {
                for (int i = 0; i < supplierPriceComponentsJson.length(); i++) {
                    supplierPriceComponents.add(supplierPriceComponentsJson.getString(i));
                }
            }
            
            boolean isAmount=discountOnSupplierPriceResource.getBoolean("isAmount");
            
            if (isAmount) {
                amount = discountOnSupplierPriceResource.getBigDecimal("amount");
                
            } else {
                percentage = discountOnSupplierPriceResource.getBigDecimal("percentage");
            }

           // BigDecimal totalPrice=new BigDecimal(opsPaxTypeFareFlightSupplier.getBaseFare().getAmount());
            
           // BigDecimal totalTaxes=new BigDecimal(0);
          //  BigDecimal totalFees= new BigDecimal(0);
            
           
            
                   /* Double totalFare = opsPaxTypeFareFlightSupplier.getTotalFare().getAmount();
                    totalFare = totalFare - (totalFare * (percentage / 100));
                    Double totalDiscountAmount = 0.0;
                    opsPaxTypeFareFlightSupplier.getTotalFare().setAmount(totalFare);*/
            
                    for (OpsFee opsFee : opsPaxTypeFareFlightSupplier.getFees().getFee()) {
                    	
                    	
                		
                        if (supplierPriceComponents.contains(opsFee.getFeeCode())) {
                        	BigDecimal feeAmount=new BigDecimal(opsFee.getAmount());
                    		
                    		if(!isAmount) {
                				amount= feeAmount.multiply (percentage.divide(new BigDecimal(100),new MathContext(4,RoundingMode.HALF_UP)));
                			}
                        	if (operation.equalsIgnoreCase(ToDoTaskSubTypeValues.DISCOUNT_ON_SUPPLIER_PRICE.toString())) {
                        		feeAmount = feeAmount .subtract(amount);
                    			if(feeAmount.signum()==-1) {
                    				throw new OperationException("Value for Fee Component "+opsFee.getFeeCode()+" cannot be Negative");
                    			}
                    			
                            } 
                    		
                    		else {
                            	
                    			feeAmount=feeAmount.add(amount);
                            }
                        	
                            pricingComponentMap.put(opsFee.getFeeCode(), feeAmount);

                        }
                    }
                    
                    if (supplierPriceComponents.contains("BaseFare")) {
                       
                    	BigDecimal baseFare=new BigDecimal(opsPaxTypeFareFlightSupplier.getBaseFare().getAmount());
                    	
                    	if(!isAmount) {
            				amount= baseFare.multiply (percentage.divide(new BigDecimal(100),new MathContext(4,RoundingMode.HALF_UP)));
            			}
                    	if (operation.equalsIgnoreCase(ToDoTaskSubTypeValues.DISCOUNT_ON_SUPPLIER_PRICE.toString())) {
                    		baseFare = baseFare .subtract(amount);
                			if(baseFare.signum()==-1) {
                				throw new OperationException("Value for Base Fare cannot be Negative");
                			}
                			
                        } 
                		
                		else {
                        	
                			baseFare=baseFare.add(amount);
                        }
                    	
                    	
                        pricingComponentMap.put("BaseFare", baseFare);

                    }
                    
                    for (OpsTax opsTax : opsPaxTypeFareFlightSupplier.getTaxes().getTax()) {

                        if (supplierPriceComponents.contains(opsTax.getTaxCode())) {
                        	BigDecimal taxAmount= BigDecimal.valueOf(opsTax.getAmount());
                        	
                        	if(!isAmount) {
                				amount= taxAmount.multiply (percentage.divide(new BigDecimal(100),new MathContext(4,RoundingMode.HALF_UP)));
                			}
                        	if (operation.equalsIgnoreCase(ToDoTaskSubTypeValues.DISCOUNT_ON_SUPPLIER_PRICE.toString())) {
                        		taxAmount = taxAmount .subtract(amount);
                    			if(taxAmount.signum()==-1) {
                    				throw new OperationException("Value for Tax Component "+opsTax.getTaxCode()+" cannot be Negative");
                    			}
                    			
                            } 
                    		
                    		else {
                            	
                    			taxAmount=taxAmount.add(amount);
                            }
                            
                            
                            pricingComponentMap.put(opsTax.getTaxCode().toUpperCase(), taxAmount);

                        }
                    }
                    
                    return pricingComponentMap;
            
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }


    private Map<String, BigDecimal> processDiscountOrSupplementAndGetNewSupplierPriceForAcco(JSONObject discountOnSupplierPriceResource, OpsProduct opsProduct) {


        try {
            BigDecimal amount = null;
            BigDecimal percentage = null;
            boolean isAmount=discountOnSupplierPriceResource.getBoolean("isAmount");
            
            List<OpsRoom> opsRooms = opsProduct.getOrderDetails().getHotelDetails().getRooms();
            Map<String, BigDecimal> pricingComponentMap = new HashMap<>();
            String operation = discountOnSupplierPriceResource.getString("operation");
            for (OpsRoom opsRoom : opsRooms) {
                if (opsRoom.getRoomID().equalsIgnoreCase(discountOnSupplierPriceResource.getString("roomId"))) {
                	
                	 JSONArray supplierPriceComponentsJson = discountOnSupplierPriceResource.getJSONArray("supplierPriceComponents");
                     ArrayList<String> supplierPriceComponents = new ArrayList<>();
                     if (supplierPriceComponentsJson != null) {
                         for (int i = 0; i < supplierPriceComponentsJson.length(); i++) {
                             supplierPriceComponents.add(supplierPriceComponentsJson.getString(i));
                         }
                     }
                     
                     
                    
                   
                    if (isAmount) {  
                        amount = discountOnSupplierPriceResource.getBigDecimal("amount");
                        
                    } else {
                        percentage = discountOnSupplierPriceResource.getBigDecimal("percentage");
                    }

                    
                    BigDecimal totalPrice=new BigDecimal(opsRoom.getRoomSuppPriceInfo().getRoomSupplierPrice());
                    BigDecimal totalTaxes=new BigDecimal(0);
                    
                    for(OpsTax tax:opsRoom.getRoomSuppPriceInfo().getTaxes().getTax()) {
                    
                    	if(supplierPriceComponents.contains(tax.getTaxCode())) {
                    		BigDecimal taxAmount=new BigDecimal(tax.getAmount());
                    		
                    		if(!isAmount) {
                				amount= taxAmount.multiply (percentage.divide(new BigDecimal(100),new MathContext(4,RoundingMode.HALF_UP)));
                			}
                    		if (operation.equalsIgnoreCase(ToDoTaskSubTypeValues.DISCOUNT_ON_SUPPLIER_PRICE.toString())) {
                    			taxAmount = taxAmount .subtract(amount);
                    			if(taxAmount.signum()==-1) {
                    				throw new OperationException("Value for Tax Component "+tax.getTaxCode()+" cannot be Negative");
                    			}
                    			
                            } 
                    		
                    		else {
                            	
                    			taxAmount=taxAmount.add(amount);
                            }
                    		
                    		totalTaxes=totalTaxes.add(amount);
                			pricingComponentMap.put(tax.getTaxCode(), taxAmount);
                    	}
                    }

                    if(supplierPriceComponents.contains("Total")) {
                		if(!isAmount) {
                			amount= totalPrice.multiply (percentage.divide(new BigDecimal(100),new MathContext(4,RoundingMode.HALF_UP)));
                		}
                		
                		if (operation.equalsIgnoreCase(ToDoTaskSubTypeValues.DISCOUNT_ON_SUPPLIER_PRICE.toString())) {
                        	
                        	totalPrice=totalPrice.subtract(amount);
                        	if(totalPrice.signum()==-1) {
                        		throw new OperationException("Total Price Cannot be Negative");
                        	}
                        }
                        
                        else {
                        	
                        	totalPrice=totalPrice.add(amount);
                        }
                	}
                    
                    if(totalTaxes.signum()==1) {
                    		if (operation.equalsIgnoreCase(ToDoTaskSubTypeValues.DISCOUNT_ON_SUPPLIER_PRICE.toString())) {
                        	totalPrice=totalPrice.subtract(totalTaxes);
                        	if(totalPrice.signum()==-1) {
                        		throw new OperationException("Total Price Cannot be Negative");
                        	}
                        }
                    		else {
                    			totalPrice=totalPrice.add(totalTaxes);
                    		}
                    	
                    }
                    
                    
                   
                    pricingComponentMap.put("AmountAfterTax", totalPrice);
                    return pricingComponentMap;
              }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    /*@Override
    public JSONObject addSupplementOnSupplierPrice(JSONObject supplementOnSupplierPriceResource) {
        OpsBooking opsBooking = null;
        OpsProduct opsProduct = null;
        String bookId = null;
        String orderId = null;
        try {
            bookId = supplementOnSupplierPriceResource.getString("bookId");
            orderId = supplementOnSupplierPriceResource.getString("orderId");
            opsBooking = opsBookingService.getBooking(bookId);
            opsProduct = opsBookingService.getProduct(bookId, orderId);
            OpsProductCategory opsProductCategory = OpsProductCategory.getProductCategory(opsProduct.getProductCategory());
            OpsProductSubCategory opsProductSubCategory = OpsProductSubCategory.getProductSubCategory(opsProductCategory, opsProduct.getProductSubCategory());
            switch (opsProductSubCategory) {
                case PRODUCT_SUB_CATEGORY_HOTELS:
                    processSupplementAndGetNewSupplierPriceForAcco(supplementOnSupplierPriceResource, opsProduct);


                    break;
                case PRODUCT_SUB_CATEGORY_FLIGHT:
                    processSupplementAndGetNewSupplierPriceForAir(supplementOnSupplierPriceResource, opsProduct);
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    private void processSupplementAndGetNewSupplierPriceForAir(JSONObject supplementOnSupplierPriceResource, OpsProduct opsProduct) {
        try {
            Double amount = null;
            Double percentage = null;

            OpsFlightSupplierPriceInfo opsFlightSupplierPriceInfo = opsProduct.getOrderDetails().getFlightDetails().getOpsFlightSupplierPriceInfo();

            JSONArray supplierPriceComponentsJson = supplementOnSupplierPriceResource.getJSONArray("supplierPriceComponents");
            ArrayList<String> supplierPriceComponents = new ArrayList<>();
            if (supplierPriceComponentsJson != null) {
                for (int i = 0; i < supplierPriceComponentsJson.length(); i++) {
                    supplierPriceComponents.add(supplierPriceComponentsJson.getString(i));
                }
            }
            if (supplementOnSupplierPriceResource.getBoolean("isAmount")) {
                amount = supplementOnSupplierPriceResource.getDouble("amount");
                //  amount = (amount / supplierPriceComponents.length());
            } else {
                percentage = supplementOnSupplierPriceResource.getDouble("percentage");
            }

            List<OpsPaxTypeFareFlightSupplier> paxTypeFares = opsFlightSupplierPriceInfo.getPaxTypeFares();
            for (OpsPaxTypeFareFlightSupplier opsPaxTypeFareFlightSupplier : paxTypeFares) {
                if (opsPaxTypeFareFlightSupplier.getPaxType().equalsIgnoreCase(supplementOnSupplierPriceResource.getString("paxType"))) {
                    Double totalFare = opsPaxTypeFareFlightSupplier.getTotalFare().getAmount();
                    totalFare = totalFare - (totalFare * (percentage / 100));
                    opsPaxTypeFareFlightSupplier.getTotalFare().setAmount(totalFare);
                    for (OpsFee opsFee : opsPaxTypeFareFlightSupplier.getFees().getFee()) {
                        if (supplierPriceComponents.contains(opsFee.getFeeCode())) {
                            Double feeAmount = opsFee.getAmount();
                            feeAmount = feeAmount + (feeAmount * (percentage / 100));
                            opsFee.setAmount(feeAmount);

                        }
                    }
                    if (supplierPriceComponents.contains("BaseFare")) {
                        OpsBaseFare baseFare = opsPaxTypeFareFlightSupplier.getBaseFare();
                        Double baseFareAmount = baseFare.getAmount();
                        baseFareAmount = baseFareAmount + (baseFareAmount * (percentage * 100));
                        baseFare.setAmount(baseFareAmount);

                    }
                    for (OpsTax opsTax : opsPaxTypeFareFlightSupplier.getTaxes().getTax()) {

                        if (supplierPriceComponents.contains(opsTax.getTaxCode())) {
                            Double taxAmount = opsTax.getAmount();
                            taxAmount = taxAmount + (taxAmount * (percentage / 100));
                            opsTax.setAmount(taxAmount);


                        }
                    }


                }
            }


        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void processSupplementAndGetNewSupplierPriceForAcco(JSONObject supplementOnSupplierPriceResource, OpsProduct opsProduct) {
        try {
            Double amount = null;
            Double percentage = null;
            List<OpsRoom> opsRooms = opsProduct.getOrderDetails().getHotelDetails().getRooms();
            for (OpsRoom opsRoom : opsRooms) {
                if (opsRoom.getRoomID().equalsIgnoreCase(supplementOnSupplierPriceResource.getString("roomId"))) {
                    OpsTaxes taxes = opsRoom.getRoomSuppPriceInfo().getTaxes();
                    JSONArray supplierPriceComponents = supplementOnSupplierPriceResource.getJSONArray("supplierPriceComponents");
                    if (supplementOnSupplierPriceResource.getBoolean("isAmount")) {   // currently Amount feature is not enabled due to lack of clarity
                        amount = supplementOnSupplierPriceResource.getDouble("amount");
                        amount = (amount / supplierPriceComponents.length());
                    } else {
                        percentage = supplementOnSupplierPriceResource.getDouble("percentage");
                    }

                    Double supplierPrice = Double.parseDouble(opsRoom.getRoomSuppPriceInfo().getRoomSupplierPrice());
                    supplierPrice = supplierPrice + (supplierPrice * (percentage / 100));
                    opsRoom.getRoomSuppPriceInfo().setRoomSupplierPrice(supplierPrice.toString()); // setting new supplier price at room level
                    for (int i = 0; i < supplierPriceComponents.length(); i++) {
                        String component = supplierPriceComponents.getString(i);

                        opsRoom.getRoomSuppPriceInfo().setRoomSupplierPrice(supplierPrice.toString());
                        for (OpsTax opsTax : taxes.getTax()) {
                            if (opsTax.getTaxCode().equalsIgnoreCase(component)) {
                                double componentAmount = opsTax.getAmount();
                                componentAmount = componentAmount + (componentAmount * (percentage * 100));
                                opsTax.setAmount(componentAmount); // setting new calculated taxes
                            }

                        }
                    }

                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
*/
    
    @Override
    public JSONObject getScreenMetaData(JSONObject jsonResource) throws OperationException {
        OpsBooking opsBooking = null;
        OpsProduct opsProduct = null;
        JSONObject jsonObject = null;
       /* //todo from where to get
        List<String> bookingNotEligibleFor = new ArrayList<>(Arrays.asList(new String[]{"Destination wise Incentive",
                "Sector Wise Incentive",
                "Segment Fee",
                "Passive Segments"
        }));// todo currently hard coded
*/
       
            opsBooking = opsBookingService.getBooking(jsonResource.getString("bookId"));
            opsProduct = opsBookingService.getProduct(jsonResource.getString("bookId"), jsonResource.getString("orderId"));

            OpsOrderStatus orderStatus=opsProduct.getOrderDetails().getOpsOrderStatus();
            if (!orderStatus.getProductStatus().equalsIgnoreCase(OpsOrderStatus.OK.getProductStatus()) && !orderStatus.getProductStatus().equalsIgnoreCase(OpsOrderStatus.VCH.getProductStatus())) {
                throw new OperationException("Change Supplier Price is not allowed for product with status "+ orderStatus.getProductStatus());
            }
            OpsProductCategory opsProductCategory = OpsProductCategory.getProductCategory(opsProduct.getProductCategory());
            OpsProductSubCategory opsProductSubCategory = OpsProductSubCategory.getProductSubCategory(opsProductCategory, opsProduct.getProductSubCategory());
            switch (opsProductSubCategory) {
                case PRODUCT_SUB_CATEGORY_HOTELS:

                    if (jsonResource.optString("roomId") == null) {
                        throw new OperationException(Constants.PARAMETER_MISSING, "RoomId");
                    }
                    jsonObject = fetchMetaDataForAcco(opsBooking, opsProduct, jsonResource.getString("roomId"));


                    break;
                case PRODUCT_SUB_CATEGORY_FLIGHT:

                    String paxType = jsonResource.optString("paxType");
                    if (paxType == null) {
                        throw new OperationException(Constants.PARAMETER_MISSING, "paxType");
                    }
                    jsonObject = fetchMataDataForAir(opsBooking, opsProduct, paxType);
			default:
				break;


            }
            //jsonObject.put("bookingNotEligibleFor", bookingNotEligibleFor);
            return jsonObject;

        } 

    

    private JSONObject fetchMataDataForAir(OpsBooking opsBooking, OpsProduct opsProduct, String paxType) throws OperationException {

        String supplierCurrency = opsProduct.getOrderDetails().getFlightDetails()
                .getOpsFlightSupplierPriceInfo()
                .getCurrencyCode();
        String sourceSupplierName = opsProduct.getSourceSupplierName();
        String supplierId = opsProduct.getSupplierID();
        String supplierRefNo = opsProduct.getSupplierRefNumber();
        String enablerSupplierName = opsProduct.getEnamblerSupplierName();
        List<String> supplierPriceComponents = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        
            jsonObject.put("supplierCurrency", supplierCurrency);
            jsonObject.put("sourceSupplierName", sourceSupplierName);
            jsonObject.put("supplierId", supplierId);
            jsonObject.put("supplierRefNo", supplierRefNo);
            jsonObject.put("enablerSupplierName", enablerSupplierName);

            OpsFlightSupplierPriceInfo opsFlightSupplierPriceInfo = opsProduct.getOrderDetails().getFlightDetails().getOpsFlightSupplierPriceInfo();
            List<OpsPaxTypeFareFlightSupplier> paxTypeFares = opsFlightSupplierPriceInfo.getPaxTypeFares();
            for (OpsPaxTypeFareFlightSupplier opsPaxTypeFareFlightSupplier : paxTypeFares) {
                if (opsPaxTypeFareFlightSupplier.getPaxType().equalsIgnoreCase(paxType)) {
                    jsonObject.put("oldPrice", amendSupplierCommercialServiceImpl.calculateMargin(opsBooking,opsProduct));
                    OpsTaxes opsTaxes = opsPaxTypeFareFlightSupplier.getTaxes();
                    for (OpsTax opsTax : opsTaxes.getTax()) {
                        supplierPriceComponents.add(opsTax.getTaxCode());
                    }
                    //supplierPriceComponents.add("Total");
                    if(opsPaxTypeFareFlightSupplier.getBaseFare()!=null) {
                    	supplierPriceComponents.add("BaseFare");
                    }
                    List<OpsFee> opsFees = opsPaxTypeFareFlightSupplier.getFees().getFee();
                    for (OpsFee opsFee : opsFees) {
                        supplierPriceComponents.add(opsFee.getFeeCode());
                    }

                }
            }
            jsonObject.put("supplierPriceComponents", supplierPriceComponents);
            return jsonObject;
      

    }

    private JSONObject fetchMetaDataForAcco(OpsBooking opsBooking, OpsProduct opsProduct, String roomId) throws OperationException {
        //ToDo Not decided how it will come from booking engine
        String supplierCurrency = null;
        String supplierId = opsProduct.getSupplierID();
        String sourceSupplierName = opsProduct.getSourceSupplierName();
        String enamblerSupplierName = opsProduct.getEnamblerSupplierName();
        String supplierRefNumber = opsProduct.getSupplierRefNumber();
        List<String> supplierPriceComponents = new ArrayList<>();
        List<OpsRoom> opsRooms = opsProduct.getOrderDetails().getHotelDetails().getRooms();
        JSONObject jsonObject = new JSONObject();
        supplierPriceComponents.add(("Total"));
        for (OpsRoom opsRoom : opsRooms) {
            if (opsRoom.getRoomID().equalsIgnoreCase(roomId)) {
                jsonObject.put("oldPrice", amendSupplierCommercialServiceImpl.calculateMargin(opsBooking,opsProduct));
                OpsTaxes opsTaxes = opsRoom.getRoomSuppPriceInfo().getTaxes();
                for (OpsTax opsTax : opsTaxes.getTax()) {
                    supplierPriceComponents.add(opsTax.getTaxCode());
                }
                supplierCurrency = opsRoom.getRoomSuppPriceInfo().getCurrencyCode();
                break;
            }
        }

       
            jsonObject.put("supplierId", supplierId);
            jsonObject.put("supplierCurrency", supplierCurrency);
            jsonObject.put("sourceSupplierName", sourceSupplierName);
            jsonObject.put("enamblerSupplierName", enamblerSupplierName);
            jsonObject.put("supplierRefNumber", supplierRefNumber);
            jsonObject.put("supplierPriceComponents", supplierPriceComponents);
            return jsonObject;
       
    }


    private JSONObject calculateSupplierPricingDetail(OpsRoom opsRoom) throws JSONException {
        OpsRoomSuppPriceInfo roomSuppPriceInfo = opsRoom.getRoomSuppPriceInfo();
        Double totalSupplierPrice = Double.parseDouble(roomSuppPriceInfo.getRoomSupplierPrice());
        List<OpsRoomSuppCommercial> roomSuppCommercials = opsRoom.getRoomSuppCommercials();
        OpsRoomTotalPriceInfo roomTotalPriceInfo = opsRoom.getRoomTotalPriceInfo();
        Double totalSellingPrice = Double.parseDouble(roomTotalPriceInfo.getRoomTotalPrice());
        Double payable = 0.0;
        Double receivable = 0.0;
        Double netPayableToSupplier = 0.0;
        Double margin = 0.0;
        JSONObject supplierPricingDetail = new JSONObject();
        for (OpsRoomSuppCommercial opsRoomSuppCommercial : roomSuppCommercials) {
            if (opsRoomSuppCommercial.getCommercialType().equalsIgnoreCase("Payable")) {
                payable = payable + opsRoomSuppCommercial.getCommercialAmount();
            } else if (opsRoomSuppCommercial.getCommercialType().equalsIgnoreCase("Receivable")) {
                receivable = receivable + opsRoomSuppCommercial.getCommercialAmount();
            }
        }


        netPayableToSupplier = totalSupplierPrice - receivable + payable;
        margin = totalSellingPrice - netPayableToSupplier;
        supplierPricingDetail.put("margin", margin);
        supplierPricingDetail.put("netPayableToSupplier", netPayableToSupplier);
        supplierPricingDetail.put("totalSellingPrice", totalSellingPrice);
        return supplierPricingDetail;
    }

    @Override
    public Object getRePriceForAir(String bookId, String orderId) {
        OpsBooking opsBooking = null;
        OpsProduct opsProduct = null;
        try {
            opsBooking = opsBookingService.getBooking(bookId);
            opsProduct = opsBookingService.getProduct(bookId, orderId);
            String rePriceForAirJson = airBookingEngineConsumptionService.getRePriceForAirJson(opsBooking, opsProduct, null);
            System.out.println(rePriceForAirJson);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    @Transactional
    public void saveDiscountOnSupplierPriceDetail(DiscountOnSupplierPrice discountOnSupplierPrice) throws OperationException {


          
        discountOnSupplierPrice = discountOnSupplierPriceRepository.saveDiscountOnSupplierPrice(discountOnSupplierPrice);
       
        
        
        	
        	String clientType=discountOnSupplierPrice.getClientType();
        	String clientId=discountOnSupplierPrice.getClientId();
        	
        	String clientCategory=null;
        	String clientSubCategory=null;
        	String companyMarket=null;
        	
        	if(clientType.equalsIgnoreCase("B2B")) {
        		B2BClient b2bClient=mdmClientService.getB2bClient(clientId);
        		clientCategory=b2bClient.getClientCategory();
        		clientSubCategory=b2bClient.getClientSubCategory(); 
        		companyMarket= b2bClient.getCompanyId();//TODo: need to add in B2Bclient class
        	}
        	else if(clientType.equalsIgnoreCase("B2C")) {
        		B2CClient b2cClient=mdmClientService.getB2cClient(clientId);
        		clientCategory=b2cClient.getClientCategory();
        		clientSubCategory=b2cClient.getClientSubCategory();
        		companyMarket=b2cClient.getCompanyId();//TODo: need to add in B2Bclient class
        	}
        	
            ToDoTaskResource toDoTaskResource = new ToDoTaskResource();
            toDoTaskResource.setReferenceId(discountOnSupplierPrice.getId()); //OPS DB ClientCommercialAmendmentID
            toDoTaskResource.setBookingRefId(discountOnSupplierPrice.getBookingId());
            toDoTaskResource.setCreatedByUserId(userService.getLoggedInUserId());
            toDoTaskResource.setAssignedBy(userService.getLoggedInUserId());
            
         /*   String productName=null;
            switch(discountOnSupplierPrice.getNewOpsProduct().getOpsProductSubCategory()) {
			
			case PRODUCT_SUB_CATEGORY_FLIGHT:
				
				break;
			case PRODUCT_SUB_CATEGORY_HOLIDAYS:
				break;
			case PRODUCT_SUB_CATEGORY_HOTELS:
				productName=discountOnSupplierPrice.getNewOpsProduct().getOrderDetails().getHotelDetails().getHotelName();
				break;
			
			default:
				break;
            
            }*/
            
            toDoTaskResource.setProductId(discountOnSupplierPrice.getNewOpsProduct().getOpsProductCategory().getCategory());
            
            toDoTaskResource.setClientId(clientId);
            toDoTaskResource.setClientTypeId(clientType);
            toDoTaskResource.setCompanyId(discountOnSupplierPrice.getCompanyId());
            toDoTaskResource.setClientCategoryId(clientCategory);
    		toDoTaskResource.setClientSubCategoryId(clientSubCategory);
            toDoTaskResource.setCompanyMarketId(companyMarket);
            
            toDoTaskResource.setTaskOrientedTypeId(ToDoTaskOrientedValues.APPROVAL_ORIENTED.getValue());
            toDoTaskResource.setTaskNameId(ToDoTaskNameValues.APPROVE.getValue());
            toDoTaskResource.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
            toDoTaskResource.setTaskSubTypeId(ToDoTaskSubTypeValues.DISCOUNT_ON_SUPPLIER_PRICE.toString()); //AMEND_CLIENT_COMMERCIAL
            toDoTaskResource.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue()); // OPERATIONS or FINANCE
            toDoTaskResource.setTaskStatusId(ToDoTaskStatusValues.ASSIGNED.getValue()); //ASSIGN as per my assumption
            toDoTaskResource.setTaskPriorityId(ToDoTaskPriorityValues.HIGH.getValue()); // Dummy value HIGH
            toDoTaskResource.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.toString());
            
            
           
         
            ToDoTask toDoTask = null;

            try {
    			toDoTask = toDoTaskService.save(toDoTaskResource);
            } catch (InvocationTargetException | IllegalAccessException | ParseException | JSONException | IOException e) {
                logger.error(e.getMessage(), e);
                throw new OperationException(Constants.TODO_TASK_CREATION_FAIL);
    		}
    		
            discountOnSupplierPrice.setTodoId(toDoTask.getId());

            
            discountOnSupplierPriceRepository.saveDiscountOnSupplierPrice(discountOnSupplierPrice);
           

          

        } 


    

    @Override
    @Transactional
    public void saveSupplementOnSupplierPriceDetail(SupplementOnSupplierPrice supplementOnSupplierPrice) throws OperationException {

        String clientEmail = null;

        supplementOnSupplierPrice.setIdentifier(generateUniqueString());
        supplementOnSupplierPrice = supplementOnSupplierPriceRepository.saveSupplementOnSupplierPrice(supplementOnSupplierPrice);

        ToDoTask toDoTask = null;//checkForApproval(discountOnSupplierPriceResource); // to do check for approval process


        Double passToClientAmount = supplementOnSupplierPrice.getPassToClientAmount();
        
        String clientType=supplementOnSupplierPrice.getClientType();
    	String clientId=supplementOnSupplierPrice.getClientId();
    	
    	String clientCategory=null;
    	String clientSubCategory=null;
    	String companyMarket=null;
    	String clientName=null;
    	
    	if(clientType.equalsIgnoreCase("B2B")) {
    		B2BClient b2bClient=mdmClientService.getB2bClient(clientId);
    		clientCategory=b2bClient.getClientCategory();
    		clientSubCategory=b2bClient.getClientSubCategory(); 
    		companyMarket= b2bClient.getCompanyId();//TODo: need to add in B2Bclient class
    		clientName=b2bClient.getClientName();
    	}
    	else if(clientType.equalsIgnoreCase("B2C")) {
    		B2CClient b2cClient=mdmClientService.getB2cClient(clientId);
    		clientCategory=b2cClient.getClientCategory();
    		clientSubCategory=b2cClient.getClientSubCategory();
    		companyMarket=b2cClient.getCompanyId();//TODo: need to add in B2Bclient class
    		clientName=b2cClient.getClientName();
    	}
    	
        ToDoTaskResource toDoTaskResource = new ToDoTaskResource();
        toDoTaskResource.setReferenceId(supplementOnSupplierPrice.getId()); //OPS DB ClientCommercialAmendmentID
        toDoTaskResource.setBookingRefId(supplementOnSupplierPrice.getBookingId());
        toDoTaskResource.setCreatedByUserId(userService.getLoggedInUserId());
        toDoTaskResource.setAssignedBy(userService.getLoggedInUserId());
        
        toDoTaskResource.setProductId(supplementOnSupplierPrice.getNewOpsProduct().getOpsProductCategory().getCategory());
        toDoTaskResource.setClientId(clientId);
        toDoTaskResource.setClientTypeId(clientType);
        toDoTaskResource.setCompanyId(supplementOnSupplierPrice.getCompanyId());
        toDoTaskResource.setClientCategoryId(clientCategory);
		toDoTaskResource.setClientSubCategoryId(clientSubCategory);
        toDoTaskResource.setCompanyMarketId(companyMarket);
        
        toDoTaskResource.setTaskOrientedTypeId(ToDoTaskOrientedValues.APPROVAL_ORIENTED.getValue());
        toDoTaskResource.setTaskNameId(ToDoTaskNameValues.APPROVE.getValue());
        toDoTaskResource.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
        toDoTaskResource.setTaskSubTypeId(ToDoTaskSubTypeValues.SUPPLEMENT_ON_SUPPLIER_PRICE.toString()); //AMEND_CLIENT_COMMERCIAL
        toDoTaskResource.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue()); // OPERATIONS or FINANCE
        toDoTaskResource.setTaskStatusId(ToDoTaskStatusValues.ASSIGNED.getValue()); //ASSIGN as per my assumption
        toDoTaskResource.setTaskPriorityId(ToDoTaskPriorityValues.HIGH.getValue()); // Dummy value HIGH
        toDoTaskResource.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.toString());
        
       
        try {
            toDoTask = toDoTaskService.save(toDoTaskResource);
        } catch (ParseException | IOException | IllegalAccessException | InvocationTargetException e) {
            throw new OperationException(Constants.TODO_TASK_CREATION_FAIL);
        }
        
        
        
        
        
        if (passToClientAmount != null && passToClientAmount > 0) {

            if (ClientType.B2B.getClientType().equalsIgnoreCase(supplementOnSupplierPrice.getClientType())) {
                clientEmail = refundMDMService.getB2BClientEmail(supplementOnSupplierPrice.getClientId());
            } else {
                clientEmail = refundMDMService.getB2CClientEmail(supplementOnSupplierPrice.getClientId());
            }


            
            OpsBooking opsBooking = null;

            opsBooking = opsBookingService.getBooking(supplementOnSupplierPrice.getBookingId());

            OpsProduct opsProduct = getOpsProduct(opsBooking, supplementOnSupplierPrice.getOrderId());
            String productCategory = opsProduct.getProductCategory();
            OpsProductCategory opsProductCategory = OpsProductCategory.getProductCategory(opsProduct.getProductCategory());
            OpsProductSubCategory opsProductSubCategory = OpsProductSubCategory.getProductSubCategory(opsProductCategory, opsProduct.getProductSubCategory());
            BigDecimal oldPrice = null;
            BigDecimal newPrice = null;
            OpsProduct newOpsProduct = null;
            switch (opsProductSubCategory) {
                case PRODUCT_SUB_CATEGORY_HOTELS:
                    OpsRoom oldOpsRoom = getOpsRoom(opsProduct, supplementOnSupplierPrice.getRoomId());
                    oldPrice = new BigDecimal(oldOpsRoom.getRoomTotalPriceInfo().getRoomTotalPrice());

                    newOpsProduct = supplementOnSupplierPrice.getNewOpsProduct();
                    OpsRoom opsNewRoom = getOpsRoom(newOpsProduct, supplementOnSupplierPrice.getRoomId());
                    newPrice = new BigDecimal(opsNewRoom.getRoomTotalPriceInfo().getRoomTotalPrice());
                    
                    break;
                case PRODUCT_SUB_CATEGORY_FLIGHT:
                  
                    newOpsProduct = supplementOnSupplierPrice.getNewOpsProduct();
                    opsProduct.getOrderDetails().getFlightDetails().getTotalPriceInfo().getPaxTypeFares();
                    List<OpsPaxTypeFareFlightClient> paxTypeFares = opsProduct.getOrderDetails().getFlightDetails().getTotalPriceInfo().getPaxTypeFares();
                    final String paxType = supplementOnSupplierPrice.getPaxType();
                    for (OpsPaxTypeFareFlightClient opsPaxTypeFareFlightClient : paxTypeFares) {
                        if (opsPaxTypeFareFlightClient.getPaxType().equalsIgnoreCase(paxType)) {
                            oldPrice = new BigDecimal(opsPaxTypeFareFlightClient.getTotalFare().getAmount().toString());
                        }
                    }
                    for (OpsPaxTypeFareFlightClient opsPaxTypeFareFlightClient : newOpsProduct.getOrderDetails().getFlightDetails().getTotalPriceInfo().getPaxTypeFares()) {
                        if (opsPaxTypeFareFlightClient.getPaxType().equalsIgnoreCase(paxType)) {
                        	newPrice = new BigDecimal(opsPaxTypeFareFlightClient.getTotalFare().getAmount().toString());
                        }
                    }


                    break;
                    

            }
            newPrice = newPrice .add(new BigDecimal(passToClientAmount));
            
       /*    // your booking price is increased from 400 to 500 do you want to accept
            Map<String, String> dynamicVariables = new HashMap<>();

            dynamicVariables.put("booking_id", supplementOnSupplierPrice.getBookingId());
 			dynamicVariables.put("original_price", oldPrice.toString());
            dynamicVariables.put("increased_price", newPrice.toString());

            dynamicVariables.put("reject", "<a href='" + clientRejectionUrl + supplementOnSupplierPrice.getIdentifier() + "'>Reject</a>");
            dynamicVariables.put("accept", "<a href='" + clientAcceptanceUrl + supplementOnSupplierPrice.getIdentifier() + "'>Accept</a>");
            EmailResponse emailResponse = null;*/
            
            

          /*  emailResponse = emailUtils.buildClientMail(functionNameForSupplementOnSupplierPrice,
                    scenarioForSupplementOnSupplierPrice,
                    clientEmail,
                    subjectForSupplementOnSupplierPrice,
                    dynamicVariables,
                    null,
                    null);*/
             
           EmailResponse emailResponse = null;     
           EmailWithBodyResource emailBody=new EmailWithBodyResource();
           
           emailBody.setFromMail(fromEmailAddress);
           emailBody.setToMail(Collections.singletonList(clientEmail));
           emailBody.setBookId(opsBooking.getBookID());
           emailBody.setBody(getEmailBody(opsBooking.getBookID(),clientName,oldPrice,newPrice,supplementOnSupplierPrice.getIdentifier()));
           emailBody.setFunction(functionNameForSupplementOnSupplierPrice);
           emailBody.setPriority(EmailPriority.HIGH);
           emailBody.setProductSubCategory(opsProduct.getOpsProductSubCategory().getSubCategory());
           emailBody.setScenario(scenarioForSupplementOnSupplierPrice);
           emailBody.setSubject(subjectForSupplementOnSupplierPrice);
           emailBody.setUserId(userService.getLoggedInUserId());
           
           emailResponse = emailService.sendEmail(emailBody);
                
           
            System.out.println();
        }


        // logic is pending
        if (toDoTask == null) {
            throw new OperationException(Constants.TODO_TASK_CREATION_FAIL);
        }
        
        
        supplementOnSupplierPrice.setTodoId(toDoTask.getId());
        supplementOnSupplierPrice = supplementOnSupplierPriceRepository.saveSupplementOnSupplierPrice(supplementOnSupplierPrice);       
        
    }

    private String getEmailBody(String bookingId, String clientName, BigDecimal oldPrice, BigDecimal newPrice,String identifier) {
    	StringBuilder emailBody = new StringBuilder();
    	emailBody.append("<p>Hi ").append(clientName).append(",</p>\n");
    	emailBody.append("<p>The Price for the Booking "+bookingId+ " is changed from " +oldPrice +" to " +newPrice + " and we require you approval.</p>");
    	emailBody.append("<a href='" + clientAcceptanceUrl + identifier + "'>Accept</a>");
    	emailBody.append("<p><a href='" + clientRejectionUrl + identifier + "'>Reject</a><br />");
    	emailBody.append("</p>\n<p>Thank you,</p>\n<p>Cox and Kings</p>");
		return emailBody.toString();
	}

	private String generateUniqueString() {

        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();

        return generatedString;
    }

    private String getAirSupplierDefinedRate(String supplierId, String subCategory) {
        return null;
    }

    private ClientContext getClientContextResource(OpsBooking opsBooking) {
        ClientContext clientContext = new ClientContext();
        clientContext.setClientID(opsBooking.getClientID());
        clientContext.setClientMarket(opsBooking.getClientMarket());
        clientContext.setClientType(opsBooking.getClientType());
        clientContext.setClientLanguage(opsBooking.getClientLanguage());
        clientContext.setClientIATANumber(opsBooking.getClientIATANumber());
        //clientContextResource.setClientNationality(opsBooking.getC); todo
        clientContext.setPointOfSale(opsBooking.getPointOfSale());
        return clientContext;
    }


}