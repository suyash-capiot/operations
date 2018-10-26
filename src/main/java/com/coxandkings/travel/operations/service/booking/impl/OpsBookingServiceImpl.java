package com.coxandkings.travel.operations.service.booking.impl;

import com.coxandkings.travel.ext.model.be.Booking;
import com.coxandkings.travel.ext.model.be.Product;
import com.coxandkings.travel.operations.beconsumer.BEConstants;
import com.coxandkings.travel.operations.criteria.booking.becriteria.BookingSearchCriteria;
import com.coxandkings.travel.operations.enums.common.MDMClientType;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.enums.user.UserType;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.resource.MessageResource;
import com.coxandkings.travel.operations.resource.amendentitycommercial.MarginDetails;
import com.coxandkings.travel.operations.resource.booking.AssignStaffResource;
import com.coxandkings.travel.operations.resource.outbound.be.*;
import com.coxandkings.travel.operations.resource.searchviewfilter.BookingSearchResponseItem;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.mdmservice.ClientMasterDataService;
import com.coxandkings.travel.operations.service.mdmservice.CompanyMasterDataService;
import com.coxandkings.travel.operations.service.mdmservice.UserMasterDataService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.*;
import com.coxandkings.travel.operations.utils.adapter.OpsBookingAdapter;
import com.coxandkings.travel.operations.utils.adapter.OpsFlightAdapter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OpsBookingServiceImpl implements OpsBookingService, BEConstants {

    private static Logger logger = LogManager.getLogger(OpsBookingServiceImpl.class);

    @Value(value = "${booking_engine.get.booking}")
    private String getBookingUrl;
    @Value(value = "${booking_engine.searchBookings}")
    private String searchBookings;
    @Value(value = "${booking_engine.get.all}")
    private String getAllBookingUrl;
    @Value(value = "${booking_engine.get.product}")
    private String getProductUrl;
    @Value(value = "${booking_engine.base_url}")
    private String beBaseUrl;
    @Value(value = "${booking_engine.lock.acquire_lock}")
    private String acquireLockUrl;
    @Value(value = "${booking_engine.lock.release_lock}")
    private String releaseLockUrl;
    @Value(value = "${ops_booking.finance.get.invoices-for-booking}")
    private String getInvoicesForBookingURL;
    @Value(value = "${account_summary.get_cancellation_charges}")
    private String cancellationChargesURL;
    @Value(value = "${account_summary.get_amendment_charges}")
    private String amendmentChargesURL;
    @Value(value = "${booking_engine.assignStaff}")
    private String assignStaffURL;
    @Value(value = "${booking_engine.update.isReadFlag}")
    private String updateIsReadFlagUrl;

    @Value(value = "${booking_engine.auto_suggestion}")
    private String autoSuggestionBEUrl;

    @Autowired
    private OpsBookingAdapter opsBookingAdapter;

    @Autowired
    private OpsFlightAdapter opsFlightAdapter;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private ClientMasterDataService clientService;

    @Autowired
    private CompanyMasterDataService companyService;

    @Autowired
    private UserMasterDataService userMasterService;

    @Autowired
    private JsonObjectProvider jsonFilter;

    @Autowired
    private MarginCalculatorUtil marginCalculatorUtil;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ActionItemsHelper actionItemsUtil;

    @Autowired
    private RestUtils restUtils;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public OpsBooking getBooking(String bookingRefId) throws OperationException {
        String parameterizedGetBooking = getBookingUrl + bookingRefId;
        OpsBooking opsBooking = null;

        Long startTime = System.currentTimeMillis();
        String bookingDetails = RestUtils.getForObject(parameterizedGetBooking, String.class);
        Long endTime = System.currentTimeMillis();
        logger.info("BE getBooking: " + (endTime - startTime));
        if (bookingDetails.contains("BE_ERR_001")) {
            throw new OperationException("Failed to get booking from BE for id : " + bookingRefId);
        }


        ObjectMapper objMapper = new ObjectMapper();
        objMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Booking aBooking = null;
        try {
            aBooking = objMapper.readValue(bookingDetails, Booking.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new OperationException(Constants.BOOKING_PARSE_ERROR);
        }
        startTime = System.currentTimeMillis();
        opsBooking = opsBookingAdapter.getOpsBooking(aBooking);
        endTime = System.currentTimeMillis();
        logger.info("Adapter time getBooking: " + (endTime - startTime));
        String fileHandlerID = opsBooking.getStaffId();
        ArrayList<String> userIDList = new ArrayList<>(1);
        String fileHandlerName = new String();

        List<OpsProduct> opsProductsList = opsBooking.getProducts();
        if (opsProductsList != null && opsProductsList.size() > 0) {

            updateBookingForMargins(opsBooking);

            if (fileHandlerID != null && fileHandlerID.trim().length() > 0) {
                userIDList.add(fileHandlerID);
                HashMap<String, String> fileHandlerInfoMap = userMasterService.getUserInfo(userIDList);
                fileHandlerName = fileHandlerInfoMap.get(fileHandlerID);
                fileHandlerName = (fileHandlerName != null) ? fileHandlerName : "";
            }

            for (OpsProduct aProduct : opsProductsList) {
                aProduct.setFileHandlerUserID(fileHandlerID);
                aProduct.setFileHandlerUserName(fileHandlerName);
            }
        }
        return opsBooking;
    }

    @Override
    public OpsBooking getBooking(Booking aBooking) throws OperationException {
        OpsBooking opsBooking = null;
        Long startTime,endTime;

        ObjectMapper objMapper = new ObjectMapper();
        startTime = System.currentTimeMillis();
        opsBooking = opsBookingAdapter.getOpsBooking(aBooking);
        endTime = System.currentTimeMillis();
        logger.info("Adapter time getBooking: " + (endTime - startTime));
        String fileHandlerID = opsBooking.getStaffId();
        ArrayList<String> userIDList = new ArrayList<>(1);
        String fileHandlerName = new String();

        List<OpsProduct> opsProductsList = opsBooking.getProducts();
        if (opsProductsList != null && opsProductsList.size() > 0) {

            updateBookingForMargins(opsBooking);

            if (fileHandlerID != null && fileHandlerID.trim().length() > 0) {
                userIDList.add(fileHandlerID);
                HashMap<String, String> fileHandlerInfoMap = userMasterService.getUserInfo(userIDList);
                fileHandlerName = fileHandlerInfoMap.get(fileHandlerID);
                fileHandlerName = (fileHandlerName != null) ? fileHandlerName : "";
            }

            for (OpsProduct aProduct : opsProductsList) {
                aProduct.setFileHandlerUserID(fileHandlerID);
                aProduct.setFileHandlerUserName(fileHandlerName);
            }
        }
        return opsBooking;
    }

    @Override
    public List<OpsUniqueProductSubCategory> getUniqueProductSubCategories(String bookingId) {
        String parameterizedGetBooking = getBookingUrl + bookingId;
//        OpsBooking opsBooking;
        Set<OpsUniqueProductSubCategory> opsUniqueProductSubCategories = new HashSet<>();
        try {
            String bookingDetails = RestUtils.getForObject(parameterizedGetBooking, String.class);

            ObjectMapper objMapper = new ObjectMapper();
            objMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Booking aBooking = objMapper.readValue(bookingDetails, Booking.class);
            List<Product> products = aBooking.getBookingResponseBody().getProducts();
       /*     opsBooking = opsBookingAdapter.getOpsBooking(aBooking);
            List<OpsProduct> opsProducts = opsBooking.getProducts();*/
            for (Product product : products) {
                OpsUniqueProductSubCategory subCategory = new OpsUniqueProductSubCategory(product.getProductCategory(),
                        product.getProductSubCategory());
                opsUniqueProductSubCategories.add(subCategory);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>(opsUniqueProductSubCategories);
    }

    @Override
    public OpsProduct getProduct(String bookingRefId, String orderId) throws OperationException {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(getProductUrl);
        uriComponentsBuilder.queryParam("bookingRefID", bookingRefId);
        uriComponentsBuilder.queryParam("orderId", orderId);
        String parameterizedGetBooking = getBookingUrl + bookingRefId;
        OpsProduct opsProduct = null;
        try {
            String bookingDetails = RestUtils.getForObject(parameterizedGetBooking, String.class);

            ObjectMapper objMapper = new ObjectMapper();
            objMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Booking aBooking = objMapper.readValue(bookingDetails, Booking.class);

            Optional<Product> matchProduct = aBooking.getBookingResponseBody().getProducts().stream().
                    filter(aProduct -> aProduct.getOrderID().equals(orderId)).findFirst();

            if (matchProduct.isPresent()) {
                opsProduct = opsBookingAdapter.getOpsProduct(matchProduct.get());
            } else {
                throw new OperationException(Constants.ER21);
            }

        } catch (IOException e) {
            throw new OperationException(Constants.ER21);
        }
        return opsProduct;
    }


    @Override
    public List<OpsProduct> getProductsBySubCategory(String subCategory, String bookingId) {
        String parameterizedGetBooking = getBookingUrl + bookingId;
        List<OpsProduct> matchProducts = new ArrayList<>();
        try {
            Booking booking = RestUtils.getForObject(parameterizedGetBooking, Booking.class);
            OpsBooking opsBooking = opsBookingAdapter.getOpsBooking(booking);

            setAmendAndCancChargesOfBooking(opsBooking);

            updateBookingForMargins(opsBooking);

            matchProducts = opsBooking.getProducts().stream().filter(aProduct
                    -> aProduct.getProductSubCategory().equalsIgnoreCase(subCategory)).collect(Collectors.toList());

            String fileHandlerID = opsBooking.getStaffId();
            markBookingAsRead(bookingId, fileHandlerID);

            ArrayList<String> userIDList = new ArrayList<>(1);
            if (fileHandlerID != null && fileHandlerID.trim().length() > 0) {
                userIDList.add(fileHandlerID);
                HashMap<String, String> fileHandlerInfoMap = userMasterService.getUserInfo(userIDList);
                String fileHandlerName = fileHandlerInfoMap.get(fileHandlerID);
                fileHandlerName = (fileHandlerName != null) ? fileHandlerName : "";

                if (matchProducts != null && matchProducts.size() > 0) {
                    for (OpsProduct aProduct : matchProducts) {
                        aProduct.setFileHandlerUserID(fileHandlerID);
                        aProduct.setFileHandlerUserName(fileHandlerName);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return matchProducts;
    }

    @Override
    public List<OpsProduct> getProductsBySubCategory(String subCategory, OpsBooking opsBooking) {
        List<OpsProduct> matchProducts = new ArrayList<>();
        try {
            setAmendAndCancChargesOfBooking(opsBooking);

            updateBookingForMargins(opsBooking);

            matchProducts = opsBooking.getProducts().stream().filter(aProduct
                    -> aProduct.getProductSubCategory().equalsIgnoreCase(subCategory)).collect(Collectors.toList());

            String fileHandlerID = opsBooking.getStaffId();
            markBookingAsRead(opsBooking.getBookID(), fileHandlerID);

            ArrayList<String> userIDList = new ArrayList<>(1);
            if (fileHandlerID != null && fileHandlerID.trim().length() > 0) {
                userIDList.add(fileHandlerID);
                HashMap<String, String> fileHandlerInfoMap = userMasterService.getUserInfo(userIDList);
                String fileHandlerName = fileHandlerInfoMap.get(fileHandlerID);
                fileHandlerName = (fileHandlerName != null) ? fileHandlerName : "";

                if (matchProducts != null && matchProducts.size() > 0) {
                    for (OpsProduct aProduct : matchProducts) {
                        aProduct.setFileHandlerUserID(fileHandlerID);
                        aProduct.setFileHandlerUserName(fileHandlerName);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return matchProducts;
    }

    private List<OpsFlightSegment> updateFlightSegments(List<OpsFlightSegment> flightSegment) {
        try {
            for (int i = 0; i < flightSegment.size(); i++) {
                OpsFlightSegment opsFlightSegment = flightSegment.get(i);
                opsFlightSegment.setAmendmentDateAndTime(ZonedDateTime.now().minusDays(5).toOffsetDateTime().toString());
                opsFlightSegment.setCancellationDateAndTime(ZonedDateTime.now().minusDays(2).toOffsetDateTime().toString());
                flightSegment.set(i, opsFlightSegment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flightSegment;
    }


    private List<OpsOriginDestinationOption> updateOpsOriginDestinationOption(List<OpsOriginDestinationOption> originDestinationOptions) {
        try {
            for (int i = 0; i < originDestinationOptions.size(); i++) {
                OpsOriginDestinationOption opsOriginDestinationOption = originDestinationOptions.get(i);
                List<OpsFlightSegment> flightSegment = opsOriginDestinationOption.getFlightSegment();
                flightSegment = updateFlightSegments(flightSegment);

                opsOriginDestinationOption.setFlightSegment(flightSegment);
                originDestinationOptions.set(i, opsOriginDestinationOption);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return originDestinationOptions;
    }


    private void updateBookingForMargins(OpsBooking opsBooking) {
        List<OpsProduct> productsList = opsBooking.getProducts();
        for (OpsProduct aProduct : productsList) {
            setMargin(opsBooking, aProduct);
        }
    }


    @Override
    public OpsProduct getOpsProduct(OpsBooking opsBooking, String orderId) throws OperationException {
        Optional<OpsProduct> optOpsProduct = opsBooking.getProducts().stream()
                .filter(opsProduct -> opsProduct.getOrderID().equalsIgnoreCase(orderId))
                .findAny();
        if (!optOpsProduct.isPresent()) {
            throw new OperationException(Constants.PRODUCT_NOT_FOUND_FOR_ORDER, orderId);
        }
        return optOpsProduct.get();
    }

    @Override
    public List<JSONObject> getAutoSuggestions(InputStream resource, String autoSuggestionType)
    {

        List<JSONObject> autoSuggestedValues = new ArrayList<>();
        JSONObject json = null;
        JSONTokener tokener = new JSONTokener(resource);
        JSONObject jsonObject = new JSONObject(tokener);
        String stringUrl = autoSuggestionBEUrl+autoSuggestionType;
        URI uri = UriComponentsBuilder.fromUriString(stringUrl).build().encode().toUri();
        RestTemplate restTemplate = RestUtils.getTemplate();
        restTemplate.setErrorHandler(new RESTErrorHandler());
        HttpEntity httpEntity = new HttpEntity(jsonObject.toString());

        ResponseEntity<List> exchange = RestUtils.exchange(uri.toString(), HttpMethod.POST, httpEntity, List.class);
        List<String> body = exchange.getBody();
        if ("supplierreferencenumber".equalsIgnoreCase(autoSuggestionType))
        {
            for (String val: body)
            {
                json = new JSONObject();
                json.put("supplierRefNo",val);
                autoSuggestedValues.add(json);
            }
        }

        if ("passangername".equalsIgnoreCase(autoSuggestionType))
        {
            for (String val: body)
            {
                json = new JSONObject();
                json.put("passengerName",val);
                autoSuggestedValues.add(json);
            }
        }

        if ("bookid".equalsIgnoreCase(autoSuggestionType))
        {
            for (String val: body)
            {
                json = new JSONObject();
                json.put("bookId",val);
                autoSuggestedValues.add(json);
            }
        }

        return autoSuggestedValues;
    }

    @Override
    public OpsBooking getBookingByPostCall(String bookId) throws OperationException {

        Booking booking = getRawBooking(bookId);
        //TODO: Check how to create RequestHeader
        String response = null;
        try {
            response = objectMapper.writeValueAsString(booking.getBookingResponseHeader());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        JSONObject requestHeader = new JSONObject(new JSONTokener(response));
        JSONObject requestBody = new JSONObject();
        requestBody.put("bookID", bookId);
        JSONObject request = new JSONObject();
        request.put("requestHeader" , requestHeader);
        request.put("requestBody" , requestBody);
        OpsBooking opsBooking = getBookingByPostCall(bookId, request);
        return opsBooking;
    }

    @Override
    public OpsBooking getBookingByPostCall(String bookId, JSONObject request) throws OperationException{

        OpsBooking opsBooking;
        ResponseEntity<String> response;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity(request.toString(), headers);
        response = RestUtils.exchange( this.getBookingUrl, HttpMethod.POST, entity, String.class);
        String bookingDetails = response.getBody();
        if (bookingDetails.contains("BE_ERR_001")) {
            throw new OperationException("Failed to get booking from BE for id : " + bookId);
        }

        ObjectMapper objMapper = new ObjectMapper();
        objMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Booking aBooking;
        try {
            aBooking = objMapper.readValue(bookingDetails, Booking.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new OperationException(Constants.BOOKING_PARSE_ERROR);
        }
        opsBooking = getBooking(aBooking);
        return opsBooking;
    }

    private OpsProduct setMargin(OpsBooking opsBooking, OpsProduct opsProduct) {
        try {
            MarginDetails marginDetails = marginCalculatorUtil.calculateMargin(opsBooking, opsProduct);
            if (marginDetails != null) {
                BigDecimal netMargin = marginDetails.getNetMargin();
                if (netMargin != null) {
                    opsProduct.setActualMarginAmount(netMargin);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return opsProduct;
    }


    @Override
    public List<OpsBooking> getBookingByCriteria(BookingSearchCriteria bookingSearchCriteria) {

        ResponseEntity<List<Booking>> searchResult = null;
        List<Booking> bookings = null;
        List<OpsBooking> opsBookingList = null;
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(beBaseUrl + "/getBookingsByCriteria");


        RestTemplate restTemplate = RestUtils.getTemplate();
        restTemplate.setErrorHandler(new RESTErrorHandler());
        HttpEntity httpEntity = new HttpEntity(bookingSearchCriteria);

        searchResult = restTemplate.exchange(builder.toUriString(),
                HttpMethod.POST, httpEntity, new ParameterizedTypeReference<List<Booking>>() {
                });


        bookings = searchResult.getBody();
        opsBookingList = bookings.stream().map(booking -> {
            return opsBookingAdapter.getOpsBooking(booking);

        }).collect(Collectors.toList());


        return opsBookingList;
    }

    @Override
    public List<BookingSearchResponseItem> searchBookings(BookingSearchCriteria bookingSearchCriteria) {

        ResponseEntity<List<BookingSearchResponseItem>> searchResult = null;
        List<BookingSearchResponseItem> bookingSearchResponseItemList = null;
        List<OpsBooking> opsBookingList = null;
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(searchBookings);

        if (StringUtils.isEmpty(bookingSearchCriteria.getSortingField())) {
            bookingSearchCriteria.setSortingField("");
        }

        UserType userType = userService.getLoggedInUserType();
        String loggedInUserId = userService.getLoggedInUserId();
        if (userType != null) {
            if (userType.equals(UserType.OPS_USER)) {
                String inputUserId = bookingSearchCriteria.getBookingBasedFilter().getUserId();
                if (inputUserId != null && inputUserId.trim().length() > 0) {
                    if (!(inputUserId.equalsIgnoreCase(loggedInUserId))) {
                        return null;
                    }
                }
                bookingSearchCriteria.getBookingBasedFilter().setUserId(userService.getLoggedInUserId());
            }
        }
        try {
            HttpEntity httpEntity = new HttpEntity(bookingSearchCriteria);
            RestTemplate restTemplate = RestUtils.getTemplate();
            searchResult = restTemplate.exchange(builder.toUriString(),
                    HttpMethod.POST, httpEntity, new ParameterizedTypeReference<List<BookingSearchResponseItem>>() {
                    });

            bookingSearchResponseItemList = searchResult.getBody();

            if (bookingSearchResponseItemList != null && bookingSearchResponseItemList.size() > 0) {
                int clientIDsListSize = 20;
                if (bookingSearchResponseItemList.size() < 20) {
                    clientIDsListSize = bookingSearchResponseItemList.size();
                }

                HashSet<String> b2bClientIDsSet = new HashSet<>();
                HashSet<String> b2cClientIDsSet = new HashSet<>();
                HashSet<String> companiesIDsSet = new HashSet<>();

                for (int index = 0; index < clientIDsListSize; index++) {
                    BookingSearchResponseItem aBookingInfo = bookingSearchResponseItemList.get(index);
                    companiesIDsSet.add(aBookingInfo.getCompany());

                    MDMClientType clientType = MDMClientType.fromString(aBookingInfo.getClientType());
                    switch (clientType) {
                        case B2B: {
                            b2bClientIDsSet.add(aBookingInfo.getClientID());
                        }
                        break;

                        case B2C: {
                            b2cClientIDsSet.add(aBookingInfo.getClientID());
                        }
                        break;
                    }
                }

                // Get client names from MDM
                HashMap<String, String> allClientNamesMap = (HashMap<String, String>) clientService.getB2BClientNames(new ArrayList<String>(b2bClientIDsSet));
                HashMap<String, String> b2CClientNamesMap = (HashMap<String, String>) clientService.getB2CClientNames(new ArrayList<String>(b2cClientIDsSet));
                allClientNamesMap.putAll(b2CClientNamesMap);

                //Get Company names from MDM
                HashMap<String, String> allCompanyNamesMap = (HashMap<String, String>) companyService.getCompanyNames(new ArrayList<String>(companiesIDsSet));

                // Iterate and update BookingSearchResponseItem for Client name & Company Name
                for (BookingSearchResponseItem aSearchResponseElement : bookingSearchResponseItemList) {
                    String clientID = aSearchResponseElement.getClientID();
                    aSearchResponseElement.setClientName(allClientNamesMap.get(clientID));

                    String companyID = aSearchResponseElement.getCompany();
                    aSearchResponseElement.setCompanyName(allCompanyNamesMap.get(companyID));
                }

                // Perform sort on clientDetails, companyDetails
                BookingSearchResponseSorter sorter = null;
                String sortingField = bookingSearchCriteria.getSortingField();
                String sortingOrder = bookingSearchCriteria.getSortingOrder();
                if (sortingField != null && sortingField.trim().length() > 0) {
                    if (sortingField.equalsIgnoreCase("clientDetails")) {
                        if (sortingOrder != null && sortingOrder.equalsIgnoreCase("asc")) {
                            sorter = new BookingSearchResponseSorter("clientDetails", true);
                        } else {
                            sorter = new BookingSearchResponseSorter("clientDetails", false);
                        }
                    } else if (sortingField.equalsIgnoreCase("companyDetails")) {
                        if (sortingOrder != null && sortingOrder.equalsIgnoreCase("asc")) {
                            sorter = new BookingSearchResponseSorter("companyDetails", true);
                        } else {
                            sorter = new BookingSearchResponseSorter("companyDetails", false);
                        }
                    }
                    Collections.sort(bookingSearchResponseItemList, sorter);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookingSearchResponseItemList;
    }

    @Override
    public void getInvoiceDetails(String bookingID) throws OperationException {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(getInvoicesForBookingURL + bookingID);
        String invoiceDetailsResponse = mdmRestUtils.getForObject(builder.toUriString(), String.class);
    }


    @Override
    public OpsActionItemDetails getOrderActionItems(String bookingID, String orderID) throws OperationException {
        OpsActionItemDetails responseMenuItems = null;
        OpsBooking aBooking = getBooking(bookingID);
        List<OpsProduct> productsList = aBooking.getProducts();
        for (OpsProduct aProduct : productsList) {
            String aTmpOrderID = aProduct.getOrderID();
            if (aTmpOrderID.equalsIgnoreCase(orderID)) {
                OpsProductSubCategory productSubCategory = aProduct.getOpsProductSubCategory();
                responseMenuItems = new OpsActionItemDetails(productSubCategory);
                actionItemsUtil.updateActionItemsForTimeLimitBooking(aBooking, aProduct, responseMenuItems, productSubCategory);
                break;
            }
        }
        return responseMenuItems;
    }

    @Override
    public BookingOverview getBookingOverview(OpsBooking aBooking) throws OperationException {

        String clientType = aBooking.getClientType();
        String clientID = aBooking.getClientID();
        String companyID = aBooking.getCompanyId();

        // Need BU, SBU, Sales Office, Sales Office location, distribution channel and division

        BookingOverview aBookingOverview = new BookingOverview(aBooking);


            ArrayList clientIDsList = new ArrayList<>(1);
            StringBuilder clientNamesInfo = new StringBuilder();
            clientIDsList.add(clientID);
            MDMClientType aClientType = MDMClientType.fromString(clientType);
            String clientDetails = null;

            if (aClientType != null) {
                switch (aClientType) {
                    case B2B: {
                        clientDetails = clientService.getB2BClientDetails(clientID);
                        String clientName = jsonFilter.getAttributeValue(clientDetails, "$.clientProfile.clientDetails.clientName", String.class);
                        String email = jsonFilter.getAttributeValue(clientDetails, "$.locationDetails[0].contactDetails[0].officialEmail", String.class);
                        String phone = jsonFilter.getAttributeValue(clientDetails, "$.locationDetails[0].contactDetails[0].phone", String.class);
                        email = (email != null) ? email : "";
                        phone = (phone != null) ? phone : "";
                        aBookingOverview.setClientID(clientID);
                        aBookingOverview.setClientDetails(clientName);
                        aBookingOverview.setClientMobileNumber(phone);
                        aBookingOverview.setClientEmail(email);

                        List<B2BContactDetails> clientDetailList = jsonFilter.getChildrenCollection(clientDetails, "$.locationDetails[:1].contactDetails.*", B2BContactDetails.class);
                        B2BClientDetails b2BClientDetails = new B2BClientDetails();
                        b2BClientDetails.setB2BContactDetails(clientDetailList);

                        aBookingOverview.setClientContactDetails(b2BClientDetails);

                    }
                    break;

                    case B2C: {
                        clientDetails = clientService.getB2CClientDetails(aBooking.getUserID());
                        String firstName = jsonFilter.getAttributeValue(clientDetails, "$.travellerDetails.employee.firstName", String.class);
                        String lastName = jsonFilter.getAttributeValue(clientDetails, "$.travellerDetails.employee.lastName", String.class);
                        String mobileNumber = jsonFilter.getAttributeValue(clientDetails, "$.travellerDetails.employee.mobileNumber", String.class);
                        String personalEmailId = jsonFilter.getAttributeValue(clientDetails, "$.travellerDetails.employee.personalEmailId", String.class);
                        firstName = (firstName != null) ? firstName : "";
                        lastName = (lastName != null) ? lastName : "";
                        clientNamesInfo.append(firstName + " " + lastName + " (" + aClientType.getClientType() + "),");
                        mobileNumber = (mobileNumber != null) ? mobileNumber : "";
                        personalEmailId = (personalEmailId != null) ? personalEmailId : "";
                        aBookingOverview.setClientMobileNumber(mobileNumber);
                        aBookingOverview.setClientEmail(personalEmailId);
                        aBookingOverview.setClientID(clientID);
                        aBookingOverview.setClientDetails(clientNamesInfo.toString());

                        CorporateAddress corporateAddress = (CorporateAddress) jsonFilter.getChildObject(clientDetails, "$.contactDetails.corporateAddress", CorporateAddress.class);
                        B2CContactDetails b2CContactDetails = new B2CContactDetails();
                        b2CContactDetails.setCorporateAddress(corporateAddress);
                        aBookingOverview.setClientContactDetails(b2CContactDetails);
                    }
                    break;
                }
            }

            if (companyID != null) {
                ArrayList<String> companyIDsList = new ArrayList<>(1);
                companyIDsList.add(companyID);
                String companyDetails = companyService.getCompanyDetails(companyID);
                String companyName = jsonFilter.getAttributeValue(companyDetails, "$.name", String.class);
                String countryName = jsonFilter.getAttributeValue(companyDetails, "$.address.country", String.class);
                companyName = (companyName != null) ? companyName : "";
                countryName = (countryName != null) ? countryName : "";
                if (companyName.length() > 0 && countryName.length() > 0) {
                    aBookingOverview.setCompanyDetails(companyName + " (" + countryName + ")");
                }

                //?filter={"companyId":"GC22", "buAvailability.buId":"BU38"}
                String buID = aBooking.getBu();
                if (buID != null && buID.trim().length() > 0) {
                    String divisionData = companyService.getDivisionDetails(companyID, buID);
                    if (divisionData != null) {
                        String division = jsonFilter.getAttributeValue(divisionData, "$.division", String.class);
                        String locationName = jsonFilter.getAttributeValue(divisionData, "$.name", String.class);
                        String locationAddress = jsonFilter.getAttributeValue(divisionData, "$.address.location", String.class);
                        String divisionName = jsonFilter.getAttributeValue(divisionData, "$.divisionName", String.class);
                        String distributionChannel = jsonFilter.getAttributeValue(divisionData, "$.distributionChannel", String.class);
                        String distributionChannelName = jsonFilter.getAttributeValue(divisionData, "$.distributionChannelName", String.class);
                        if (distributionChannel != null && distributionChannel.trim().length() > 0) {
                            if (distributionChannelName != null && distributionChannelName.trim().length() > 0) {
                                distributionChannelName = distributionChannelName + " (" + distributionChannel + ")";
                            }
                        }

                        if ((division != null && division.trim().length() > 0) &&
                                (divisionName != null && divisionName.trim().length() > 0)) {
                            divisionName = divisionName + " (" + division + ")";
                        }
                        aBookingOverview.getCompanyInfo().setDistributionChannel(distributionChannelName);
                        aBookingOverview.getCompanyInfo().setDivision(divisionName);
                        aBookingOverview.getCompanyInfo().setSalesOfficeLocation(locationName);
                        aBookingOverview.getCompanyInfo().setSalesOffice(locationAddress);
                    }
                }
            }

        return aBookingOverview;
    }

    @Override
    public Booking getRawBooking(String bookingRefId) throws OperationException {

        String parameterizedGetBooking = getBookingUrl + bookingRefId;
        Booking aBooking = null;
        try {
            String bookingDetails = RestUtils.getForObject(parameterizedGetBooking, String.class);

            if (bookingDetails.contains("BE_ERR_001")) {
                throw new OperationException("Failed to get booking for booking id : " + bookingRefId);
            }

            ObjectMapper objMapper = new ObjectMapper();
            objMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            aBooking = objMapper.readValue(bookingDetails, Booking.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return aBooking;
    }

    @Override
    public MessageResource assignStaff(AssignStaffResource assignStaffResource) throws OperationException {
        MessageResource messageResource = new MessageResource();
        try {
            RestUtils.put(assignStaffURL, assignStaffResource, String.class);
        } catch (RestClientException e) {
            throw new OperationException(String.format("Unable to assign staff to bookIds"));
        }

        messageResource.setMessage("Update Successful");
        return messageResource;
    }

    private void setAmendAndCancChargesOfBooking(OpsBooking opsBooking) throws OperationException {
        JSONObject amclAmendmentsJSON = null, amclCancellationsJSON = null;

        amclAmendmentsJSON = new JSONObject(RestUtils.getForObject(amendmentChargesURL + opsBooking.getBookID(), String.class));
        amclCancellationsJSON = new JSONObject(RestUtils.getForObject(cancellationChargesURL + opsBooking.getBookID(), String.class));

        for (OpsProduct opsProduct : opsBooking.getProducts()) {
            setAmendCancDetailsList(opsProduct);

            switch (opsProduct.getOpsProductSubCategory()) {
                case PRODUCT_SUB_CATEGORY_FLIGHT:
                    setAmendmentChargesForTransAirOrder(opsProduct, amclAmendmentsJSON);
                    setCancellationChargesForTransAirOrder(opsProduct, amclCancellationsJSON);

                    setAmendmentChargesForAirPax(opsProduct, amclAmendmentsJSON);
                    setCancellationChargesForAirPax(opsProduct, amclCancellationsJSON);
                    break;
                case PRODUCT_SUB_CATEGORY_HOTELS:
                    /**
                     * INFO: As discussed with BE team, there is only order level cancellation for Acco. Order level amendment is not present for Acco.
                     */
                    setCancellationChargesForAccoHotelOrder(opsProduct, amclCancellationsJSON);
                    setAmendmentChargesForAccoHotel(opsProduct, amclAmendmentsJSON);
                    setCancellationChargesForAccoHotel(opsProduct, amclCancellationsJSON);
                    break;
            }
        }
    }

    private void setAmendmentChargesForTransAirOrder(OpsProduct opsProduct, JSONObject amclAmendmentsJSON) throws OperationException {
        JSONArray productsArr = null, orderAmendmentsArr = null;
        JSONObject productEle = null, orderAmendmentsEle = null;
        List<OpsAmendDetails> opsAmendDetailsList = null;
        OpsAmendDetails opsAmendDetails = null;

        try {
            productsArr = amclAmendmentsJSON.has("products") ? amclAmendmentsJSON.getJSONArray("products") : null;

            if (productsArr != null && productsArr.length() != 0) {
                for (int i = 0; i < productsArr.length(); i++) {
                    productEle = productsArr.getJSONObject(i);

                    if (productEle != null && opsProduct != null && opsProduct.getOrderID() != null && productEle.has("orderID") && productEle.getString("orderID") != null
                            && productEle.getString("orderID").equals(opsProduct.getOrderID()) && productEle.has("productSubCategory")
                            && productEle.getString("productSubCategory").equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory())) {
                        orderAmendmentsArr = productEle.has("orderAmendments") ? productEle.getJSONArray("orderAmendments") : null;

                        if (orderAmendmentsArr != null && orderAmendmentsArr.length() != 0) {
                            for (int j = 0; j < orderAmendmentsArr.length(); j++) {
                                orderAmendmentsEle = orderAmendmentsArr.getJSONObject(j);

                                if (orderAmendmentsEle != null) {
                                    opsAmendDetailsList = opsProduct.getAmendmentChargesDetails();

                                    opsAmendDetails = new OpsAmendDetails();
                                    opsAmendDetails.setAmendID(orderAmendmentsEle.has("amendID") ? orderAmendmentsEle.getString("amendID") : "");
                                    opsAmendDetails.setCompanyAmendCharges(orderAmendmentsEle.has("companyAmendCharges") ? orderAmendmentsEle.getString("companyAmendCharges") : "0");
                                    opsAmendDetails.setSupplierAmendCharges(orderAmendmentsEle.has("supplierAmendCharges") ? orderAmendmentsEle.getString("supplierAmendCharges") : "0");
                                    opsAmendDetails.setCompanyAmendChargesCurrencyCode(orderAmendmentsEle.has("companyAmendChargesCurrencyCode") ? orderAmendmentsEle.getString("companyAmendChargesCurrencyCode") : "");
                                    opsAmendDetails.setSupplierAmendChargesCurrencyCode(orderAmendmentsEle.has("supplierAmendChargesCurrencyCode") ? orderAmendmentsEle.getString("supplierAmendChargesCurrencyCode") : "");
                                    opsAmendDetails.setAmendmentDateAndTime(orderAmendmentsEle.has("lastModifiedAt") ? orderAmendmentsEle.getString("lastModifiedAt") : "");
                                    opsAmendDetails.setAmendmentBy(orderAmendmentsEle.has("lastModifiedBy") ? orderAmendmentsEle.getString("lastModifiedBy") : "");
                                    opsAmendDetails.setType(orderAmendmentsEle.has("amendType") ? orderAmendmentsEle.getString("amendType") : "");

                                    opsAmendDetailsList.add(opsAmendDetails);
                                    opsProduct.setAmendmentChargesDetails(opsAmendDetailsList);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception : ", e);
            throw new OperationException(messageSource.getMessage(Constants.OPS_BOOK_SERV_00006, new Object[]{productEle.getString("orderID"), opsProduct.getProductName()}, Locale.US));
        }
    }

    private void setCancellationChargesForTransAirOrder(OpsProduct opsProduct, JSONObject amclCancellationsJSON) throws OperationException {
        JSONArray productsArr = null, orderCancellationsArr = null;
        JSONObject productEle = null, orderCancellationsEle = null;
        List<OpsCancDetails> opsCancDetailsList = null;
        OpsCancDetails opsCancDetails = null;

        try {
            productsArr = amclCancellationsJSON.has("products") ? amclCancellationsJSON.getJSONArray("products") : null;

            if (productsArr != null && productsArr.length() != 0) {
                for (int i = 0; i < productsArr.length(); i++) {
                    productEle = productsArr.getJSONObject(i);

                    if (productEle != null && opsProduct != null && opsProduct.getOrderID() != null && productEle.has("orderID") && productEle.getString("orderID") != null
                            && productEle.getString("orderID").equals(opsProduct.getOrderID()) && productEle.has("productSubCategory")
                            && productEle.getString("productSubCategory").equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory())) {
                        orderCancellationsArr = productEle.has("orderCancellations") ? productEle.getJSONArray("orderCancellations") : null;

                        if (orderCancellationsArr != null && orderCancellationsArr.length() != 0) {
                            for (int j = 0; j < orderCancellationsArr.length(); j++) {
                                orderCancellationsEle = orderCancellationsArr.getJSONObject(j);

                                if (orderCancellationsEle != null) {
                                    opsCancDetailsList = opsProduct.getCancellationChargesDetails();

                                    opsCancDetails = new OpsCancDetails();
                                    opsCancDetails.setCancelID(orderCancellationsEle.has("cancelID") ? orderCancellationsEle.getString("cancelID") : "");
                                    opsCancDetails.setCompanyCancelCharges(orderCancellationsEle.has("companyCancelCharges") ? orderCancellationsEle.getString("companyCancelCharges") : "0");
                                    opsCancDetails.setSupplierCancelCharges(orderCancellationsEle.has("supplierCancelCharges") ? orderCancellationsEle.getString("supplierCancelCharges") : "0");
                                    opsCancDetails.setCompanyCancelChargesCurrencyCode(orderCancellationsEle.has("companyCancelChargesCurrencyCode") ? orderCancellationsEle.getString("companyCancelChargesCurrencyCode") : "");
                                    opsCancDetails.setSupplierCancelChargesCurrencyCode(orderCancellationsEle.has("supplierCancelChargesCurrencyCode") ? orderCancellationsEle.getString("supplierCancelChargesCurrencyCode") : "");
                                    opsCancDetails.setCancellationDateAndTime(orderCancellationsEle.has("lastModifiedAt") ? orderCancellationsEle.getString("lastModifiedAt") : "");
                                    opsCancDetails.setCancellationBy(orderCancellationsEle.has("lastModifiedBy") ? orderCancellationsEle.getString("lastModifiedBy") : "");
                                    opsCancDetails.setType(orderCancellationsEle.has("cancelType") ? orderCancellationsEle.getString("cancelType") : "");

                                    opsCancDetailsList.add(opsCancDetails);
                                    opsProduct.setCancellationChargesDetails(opsCancDetailsList);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception : ", e);
            throw new OperationException(messageSource.getMessage(Constants.OPS_BOOK_SERV_00007, new Object[]{productEle.getString("orderID"), opsProduct.getProductName()}, Locale.US));
        }
    }

    private void setAmendmentChargesForAirPax(OpsProduct opsProduct, JSONObject amclAmendmentsJSON) throws OperationException {
        JSONArray productsArr = null, paxAmendmentsArr = null;
        JSONObject productEle = null, paxAmendmentEle = null;
        List<OpsAmendDetails> opsAmendDetailsList = null;
        OpsAmendDetails opsAmendDetails = null;

        try {
            productsArr = amclAmendmentsJSON.has("products") ? amclAmendmentsJSON.getJSONArray("products") : null;

            if (productsArr != null && productsArr.length() != 0) {
                for (int i = 0; i < productsArr.length(); i++) {
                    productEle = productsArr.getJSONObject(i);

                    if (productEle != null && opsProduct != null && opsProduct.getOrderID() != null && productEle.has("orderID") && productEle.getString("orderID") != null
                            && productEle.getString("orderID").equals(opsProduct.getOrderID()) && productEle.has("productSubCategory")
                            && productEle.getString("productSubCategory").equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory())) {
                        paxAmendmentsArr = productEle.has("paxAmendments") ? productEle.getJSONArray("paxAmendments") : null;

                        if (paxAmendmentsArr != null && paxAmendmentsArr.length() != 0) {
                            for (int j = 0; j < paxAmendmentsArr.length(); j++) {
                                paxAmendmentEle = paxAmendmentsArr.getJSONObject(j);

                                if (paxAmendmentEle != null) {
                                    for (OpsFlightPaxInfo opsFlightPaxInfo : opsProduct.getOrderDetails().getFlightDetails().getPaxInfo()) {
                                        if (opsFlightPaxInfo != null && opsFlightPaxInfo.getPassengerID() != null && paxAmendmentEle.has("paxIDs") && opsFlightPaxInfo.getPassengerID().equals(paxAmendmentEle.getString("paxIDs"))) {
                                            opsAmendDetailsList = opsFlightPaxInfo.getAmendmentChargesDetails();

                                            opsAmendDetails = new OpsAmendDetails();
                                            opsAmendDetails.setAmendID(paxAmendmentEle.has("amendID") ? paxAmendmentEle.getString("amendID") : "");
                                            opsAmendDetails.setCompanyAmendCharges(paxAmendmentEle.has("companyAmendCharges") ? paxAmendmentEle.getString("companyAmendCharges") : "0");
                                            opsAmendDetails.setSupplierAmendCharges(paxAmendmentEle.has("supplierAmendCharges") ? paxAmendmentEle.getString("supplierAmendCharges") : "0");
                                            opsAmendDetails.setCompanyAmendChargesCurrencyCode(paxAmendmentEle.has("companyAmendChargesCurrencyCode") ? paxAmendmentEle.getString("companyAmendChargesCurrencyCode") : "");
                                            opsAmendDetails.setSupplierAmendChargesCurrencyCode(paxAmendmentEle.has("supplierAmendChargesCurrencyCode") ? paxAmendmentEle.getString("supplierAmendChargesCurrencyCode") : "");
                                            opsAmendDetails.setAmendmentDateAndTime(paxAmendmentEle.has("lastModifiedAt") ? paxAmendmentEle.getString("lastModifiedAt") : "");
                                            opsAmendDetails.setAmendmentBy(paxAmendmentEle.has("lastModifiedBy") ? paxAmendmentEle.getString("lastModifiedBy") : "");
                                            opsAmendDetails.setType(paxAmendmentEle.has("amendType") ? paxAmendmentEle.getString("amendType") : "");

                                            opsAmendDetailsList.add(opsAmendDetails);
                                            opsFlightPaxInfo.setAmendmentChargesDetails(opsAmendDetailsList);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception : ", e);
            throw new OperationException(messageSource.getMessage(Constants.OPS_BOOK_SERV_00004, new Object[]{paxAmendmentEle.getString("paxIDs"), opsProduct.getOrderID()}, Locale.US));
        }
    }

    private void setCancellationChargesForAirPax(OpsProduct opsProduct, JSONObject amclCancellationsJSON) throws OperationException {
        JSONArray productsArr = null, paxCancellationsArr = null;
        JSONObject productEle = null, paxCancellationEle = null;
        List<OpsCancDetails> opsCancDetailsList = null;
        OpsCancDetails opsCancDetails = null;

        try {
            productsArr = amclCancellationsJSON.has("products") ? amclCancellationsJSON.getJSONArray("products") : null;

            if (productsArr != null && productsArr.length() != 0) {
                for (int i = 0; i < productsArr.length(); i++) {
                    productEle = productsArr.getJSONObject(i);

                    if (productEle != null && opsProduct != null && opsProduct.getOrderID() != null && productEle.has("orderID") && productEle.getString("orderID") != null
                            && productEle.getString("orderID").equals(opsProduct.getOrderID()) && productEle.has("productSubCategory")
                            && productEle.getString("productSubCategory").equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory())) {
                        paxCancellationsArr = productEle.has("paxCancellations") ? productEle.getJSONArray("paxCancellations") : null;

                        if (paxCancellationsArr != null && paxCancellationsArr.length() != 0) {
                            for (int j = 0; j < paxCancellationsArr.length(); j++) {
                                paxCancellationEle = paxCancellationsArr.getJSONObject(j);

                                if (paxCancellationEle != null) {
                                    for (OpsFlightPaxInfo opsFlightPaxInfo : opsProduct.getOrderDetails().getFlightDetails().getPaxInfo()) {
                                        if (opsFlightPaxInfo != null && opsFlightPaxInfo.getPassengerID() != null && paxCancellationEle.has("paxIDs") && opsFlightPaxInfo.getPassengerID().equals(paxCancellationEle.getString("paxIDs"))) {
                                            opsCancDetailsList = opsFlightPaxInfo.getCancellationChargesDetails();

                                            opsCancDetails = new OpsCancDetails();
                                            opsCancDetails.setCancelID(paxCancellationEle.has("cancelID") ? paxCancellationEle.getString("cancelID") : "");
                                            opsCancDetails.setCompanyCancelCharges(paxCancellationEle.has("companyCancelCharges") ? paxCancellationEle.getString("companyCancelCharges") : "0");
                                            opsCancDetails.setSupplierCancelCharges(paxCancellationEle.has("supplierCancelCharges") ? paxCancellationEle.getString("supplierCancelCharges") : "0");
                                            opsCancDetails.setCompanyCancelChargesCurrencyCode(paxCancellationEle.has("companyCancelChargesCurrencyCode") ? paxCancellationEle.getString("companyCancelChargesCurrencyCode") : "");
                                            opsCancDetails.setSupplierCancelChargesCurrencyCode(paxCancellationEle.has("supplierCancelChargesCurrencyCode") ? paxCancellationEle.getString("supplierCancelChargesCurrencyCode") : "");
                                            opsCancDetails.setCancellationDateAndTime((paxCancellationEle.has("lastModifiedAt") ? paxCancellationEle.getString("lastModifiedAt").toString() : ""));
                                            opsCancDetails.setCancellationBy(paxCancellationEle.has("lastModifiedBy") ? paxCancellationEle.getString("lastModifiedBy") : "");
                                            opsCancDetails.setType(paxCancellationEle.has("cancelType") ? paxCancellationEle.getString("cancelType") : "");

                                            opsCancDetailsList.add(opsCancDetails);
                                            opsFlightPaxInfo.setCancellationChargesDetails(opsCancDetailsList);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception : ", e);
            throw new OperationException(messageSource.getMessage(Constants.OPS_BOOK_SERV_00005, new Object[]{paxCancellationEle.getString("paxIDs"), opsProduct.getOrderID()}, Locale.US));
        }
    }

    private void setCancellationChargesForAccoHotelOrder(OpsProduct opsProduct, JSONObject amclCancellationsJSON) throws OperationException {
        JSONArray productArr = null, orderCancellationsArr = null;
        JSONObject productEle = null, orderCancellationsEle = null;
        List<OpsCancDetails> opsCancDetailsList = null;
        OpsCancDetails opsCancDetails = null;

        try {
            productArr = amclCancellationsJSON.has("products") ? amclCancellationsJSON.getJSONArray("products") : null;

            if (productArr != null && productArr.length() != 0) {
                for (int i = 0; i < productArr.length(); i++) {
                    productEle = productArr.getJSONObject(i);

                    if (productEle != null && productEle.has("orderID") && productEle.getString("orderID") != null && opsProduct != null && opsProduct.getOrderID() != null
                            && productEle.getString("orderID").equals(opsProduct.getOrderID()) && productEle.has("productSubCategory")
                            && productEle.getString("productSubCategory").equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS.getSubCategory())) {

                        orderCancellationsArr = productEle.has("orderCancellations") ? productEle.getJSONArray("orderCancellations") : null;

                        if (orderCancellationsArr != null && orderCancellationsArr.length() != 0) {
                            for (int j = 0; j < orderCancellationsArr.length(); j++) {
                                orderCancellationsEle = orderCancellationsArr.getJSONObject(j);

                                if (orderCancellationsEle != null) {
                                    opsCancDetailsList = opsProduct.getCancellationChargesDetails();
                                    opsCancDetails = new OpsCancDetails();
                                    opsCancDetails.setCancelID(orderCancellationsEle.has("cancelID") ? orderCancellationsEle.getString("cancelID") : "");
                                    opsCancDetails.setCompanyCancelCharges(orderCancellationsEle.has("companyCancelCharges") ? orderCancellationsEle.getString("companyCancelCharges") : "0");
                                    opsCancDetails.setSupplierCancelCharges(orderCancellationsEle.has("supplierCancelCharges") ? orderCancellationsEle.getString("supplierCancelCharges") : "0");
                                    opsCancDetails.setCompanyCancelChargesCurrencyCode(orderCancellationsEle.has("companyCancelChargesCurrencyCode") ? orderCancellationsEle.getString("companyCancelChargesCurrencyCode") : "");
                                    opsCancDetails.setSupplierCancelChargesCurrencyCode(orderCancellationsEle.has("supplierCancelChargesCurrencyCode") ? orderCancellationsEle.getString("supplierCancelChargesCurrencyCode") : "");
                                    opsCancDetails.setCancellationDateAndTime(orderCancellationsEle.has("lastModifiedAt") ? orderCancellationsEle.getString("lastModifiedAt") : "");
                                    opsCancDetails.setCancellationBy(orderCancellationsEle.has("lastModifiedBy") ? orderCancellationsEle.getString("lastModifiedBy") : "");
                                    opsCancDetails.setType(orderCancellationsEle.has("cancelType") ? orderCancellationsEle.getString("cancelType") : "");

                                    opsCancDetailsList.add(opsCancDetails);
                                    opsProduct.setCancellationChargesDetails(opsCancDetailsList);
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.error("Exception : ", e);
            throw new OperationException(messageSource.getMessage(Constants.OPS_BOOK_SERV_00001, new Object[]{opsProduct.getOrderID(), opsProduct.getProductName()}, Locale.US));
        }
    }

    private void setAmendmentChargesForAccoHotel(OpsProduct opsProduct, JSONObject amclAmendmentsJSON) throws OperationException {
        JSONArray productsArr = null, roomAmendmentsArr = null;
        JSONObject productEle = null, roomAmendmentEle = null;
        List<OpsAmendDetails> opsAmendDetailsList = null;
        OpsAmendDetails opsAmendCancDetails = null;

        try {
            productsArr = amclAmendmentsJSON.has("products") ? amclAmendmentsJSON.getJSONArray("products") : null;
            if (null != productsArr && productsArr.length() != 0) {
                for (int i = 0; i < productsArr.length(); i++) {
                    productEle = productsArr.getJSONObject(i);

                    if (productEle != null && productEle.has("orderID") && productEle.getString("orderID") != null && opsProduct != null && opsProduct.getOrderID() != null
                            && opsProduct.getOrderID().equals(productEle.getString("orderID")) && productEle.has("productSubCategory")
                            && productEle.getString("productSubCategory").equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS.getSubCategory())) {
                        roomAmendmentsArr = productEle.has("roomAmendments") ? productEle.getJSONArray("roomAmendments") : null;

                        if (null != roomAmendmentsArr && roomAmendmentsArr.length() != 0) {
                            for (int j = 0; j < roomAmendmentsArr.length(); j++) {
                                roomAmendmentEle = roomAmendmentsArr.getJSONObject(j);

                                for (OpsRoom opsRoom : opsProduct.getOrderDetails().getHotelDetails().getRooms()) {
                                    if (opsRoom.getRoomID() != null && roomAmendmentEle != null && roomAmendmentEle.has("roomID") && opsRoom.getRoomID().equals(roomAmendmentEle.getString("roomID"))) {
                                        opsAmendDetailsList = opsRoom.getAmendmentChargesDetails();

                                        opsAmendCancDetails = new OpsAmendDetails();
                                        opsAmendCancDetails.setAmendID(roomAmendmentEle.has("amendID") ? roomAmendmentEle.getString("amendID") : "");
                                        opsAmendCancDetails.setCompanyAmendCharges(roomAmendmentEle.has("companyAmendCharges") ? roomAmendmentEle.getString("companyAmendCharges") : "0");
                                        opsAmendCancDetails.setSupplierAmendCharges(roomAmendmentEle.has("supplierAmendCharges") ? roomAmendmentEle.getString("supplierAmendCharges") : "0");
                                        opsAmendCancDetails.setCompanyAmendChargesCurrencyCode(roomAmendmentEle.has("companyAmendChargesCurrencyCode") ? roomAmendmentEle.getString("companyAmendChargesCurrencyCode") : "");
                                        opsAmendCancDetails.setSupplierAmendChargesCurrencyCode(roomAmendmentEle.has("supplierAmendChargesCurrencyCode") ? roomAmendmentEle.getString("supplierAmendChargesCurrencyCode") : "");
                                        opsAmendCancDetails.setAmendmentDateAndTime(roomAmendmentEle.has("lastModifiedAt") ? roomAmendmentEle.getString("lastModifiedAt") : "");
                                        opsAmendCancDetails.setAmendmentBy(roomAmendmentEle.has("lastModifiedBy") ? roomAmendmentEle.getString("lastModifiedBy") : "");
                                        opsAmendCancDetails.setType(roomAmendmentEle.has("amendType") ? roomAmendmentEle.getString("amendType") : "");

                                        opsAmendDetailsList.add(opsAmendCancDetails);
                                        opsRoom.setAmendmentChargesDetails(opsAmendDetailsList);
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.error("Exception : ", e);
            throw new OperationException(messageSource.getMessage(Constants.OPS_BOOK_SERV_00002, new Object[]{roomAmendmentEle.getString("roomID"), opsProduct.getOrderID()}, Locale.US));
        }
    }

    private void setCancellationChargesForAccoHotel(OpsProduct opsProduct, JSONObject amclCancellationsJSON) throws OperationException {
        JSONArray productsArr = null, roomCancellationsArr = null;
        JSONObject productEle = null, roomCancellationEle = null;
        List<OpsCancDetails> opsCancDetailsList = null;
        OpsCancDetails opsCancDetails = null;

        try {
            productsArr = amclCancellationsJSON.has("products") ? amclCancellationsJSON.getJSONArray("products") : null;

            if (null != productsArr && productsArr.length() != 0) {
                for (int i = 0; i < productsArr.length(); i++) {
                    productEle = productsArr.getJSONObject(i);

                    if (productEle != null && productEle.has("orderID") && productEle.getString("orderID") != null && opsProduct != null && opsProduct.getOrderID() != null
                            && opsProduct.getOrderID().equals(productEle.getString("orderID")) && productEle.has("productSubCategory")
                            && productEle.getString("productSubCategory").equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS.getSubCategory())) {
                        roomCancellationsArr = productEle.has("roomCancellations") ? productEle.getJSONArray("roomCancellations") : null;

                        if (null != roomCancellationsArr && roomCancellationsArr.length() != 0) {
                            for (int j = 0; j < roomCancellationsArr.length(); j++) {
                                roomCancellationEle = roomCancellationsArr.getJSONObject(j);

                                for (OpsRoom opsRoom : opsProduct.getOrderDetails().getHotelDetails().getRooms()) {

                                    if (opsRoom.getRoomID() != null && roomCancellationEle != null && roomCancellationEle.has("roomID") && opsRoom.getRoomID().equals(roomCancellationEle.getString("roomID"))) {
                                        opsCancDetailsList = opsRoom.getCancellationChargesDetails();

                                        opsCancDetails = new OpsCancDetails();
                                        opsCancDetails.setCancelID(roomCancellationEle.has("cancelID") ? roomCancellationEle.getString("cancelID") : "");
                                        opsCancDetails.setCompanyCancelCharges(roomCancellationEle.has("companyCancelCharges") ? roomCancellationEle.getString("companyCancelCharges") : "0");
                                        opsCancDetails.setSupplierCancelCharges(roomCancellationEle.has("supplierCancelCharges") ? roomCancellationEle.getString("supplierCancelCharges") : "0");
                                        opsCancDetails.setCompanyCancelChargesCurrencyCode(roomCancellationEle.has("companyCancelChargesCurrencyCode") ? roomCancellationEle.getString("companyCancelChargesCurrencyCode") : "");
                                        opsCancDetails.setSupplierCancelChargesCurrencyCode(roomCancellationEle.has("supplierCancelChargesCurrencyCode") ? roomCancellationEle.getString("supplierCancelChargesCurrencyCode") : "");
                                        opsCancDetails.setCancellationDateAndTime(roomCancellationEle.has("lastModifiedAt") ? roomCancellationEle.getString("lastModifiedAt") : "");
                                        opsCancDetails.setCancellationBy(roomCancellationEle.has("lastModifiedBy") ? roomCancellationEle.getString("lastModifiedBy") : "");
                                        opsCancDetails.setType(roomCancellationEle.has("cancelType") ? roomCancellationEle.getString("cancelType") : "");

                                        opsCancDetailsList.add(opsCancDetails);
                                        opsRoom.setCancellationChargesDetails(opsCancDetailsList);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception : ", e);
            throw new OperationException(messageSource.getMessage(Constants.OPS_BOOK_SERV_00003, new Object[]{roomCancellationEle.getString("roomID"), opsProduct.getOrderID()}, Locale.US));
        }
    }

    private void setAmendCancDetailsList(OpsProduct opsProduct) {
        //INFO: Setting order level amendment/cancellation list for UI.
        if (opsProduct.getAmendmentChargesDetails() == null) {
            opsProduct.setAmendmentChargesDetails(new ArrayList<OpsAmendDetails>());
        }

        if (opsProduct.getCancellationChargesDetails() == null) {
            opsProduct.setCancellationChargesDetails(new ArrayList<OpsCancDetails>());
        }

        //INFO: Setting pax level amendment/cancellation list for UI.
        if (opsProduct != null && opsProduct.getProductSubCategory().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory())) {
            for (OpsFlightPaxInfo opsFlightPaxInfo : opsProduct.getOrderDetails().getFlightDetails().getPaxInfo()) {
                if (opsFlightPaxInfo.getAmendmentChargesDetails() == null) {
                    opsFlightPaxInfo.setAmendmentChargesDetails(new ArrayList<OpsAmendDetails>());
                }

                if (opsFlightPaxInfo.getCancellationChargesDetails() == null) {
                    opsFlightPaxInfo.setCancellationChargesDetails(new ArrayList<OpsCancDetails>());
                }
            }
        }

        //INFO: Setting room level amendment/cancellation list for UI.
        if (opsProduct != null && opsProduct.getProductSubCategory().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS.getSubCategory())) {
            for (OpsRoom opsRoom : opsProduct.getOrderDetails().getHotelDetails().getRooms()) {
                if (opsRoom.getAmendmentChargesDetails() == null) {
                    opsRoom.setAmendmentChargesDetails(new ArrayList<OpsAmendDetails>());
                }

                if (opsRoom.getCancellationChargesDetails() == null) {
                    opsRoom.setCancellationChargesDetails(new ArrayList<OpsCancDetails>());
                }
            }
        }
    }

    private void markBookingAsRead(String bookingRefId, String fileHandlerID) {
        JSONObject reqJson = new JSONObject();
        String userID = userService.getLoggedInUserId();
        if (userID != null) {
            UserType userType = userService.getLoggedInUserType();
            if (userType != null) {
                if (userType.equals(UserType.OPS_USER)) {
                    if (userID.equalsIgnoreCase(fileHandlerID)) {
                        reqJson.put(JSON_PROP_BOOKID, bookingRefId);
                        reqJson.put(JSON_PROP_ISRREAD, true);
                        reqJson.put(JSON_PROP_USERID, userID);

                        String beSSRResponse = null;
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        HttpEntity<String> httpEntity = new HttpEntity<>(reqJson.toString(), headers);
                        try {
                            ResponseEntity<String> ratesResponseEntity = restUtils.exchange(this.updateIsReadFlagUrl,
                                    HttpMethod.PUT, httpEntity, String.class);
                            logger.info("after calling update isRead of BE");
                            beSSRResponse = ratesResponseEntity.getBody();
                            logger.info(beSSRResponse);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
