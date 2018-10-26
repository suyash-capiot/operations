package com.coxandkings.travel.operations.service.manageofflinebooking.manualofflinebooking.impl;


import com.coxandkings.travel.operations.enums.todo.ToDoFunctionalAreaValues;
import com.coxandkings.travel.operations.enums.todo.ToDoTaskNameValues;
import com.coxandkings.travel.operations.enums.todo.ToDoTaskSubTypeValues;
import com.coxandkings.travel.operations.enums.todo.ToDoTaskTypeValues;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsBookingStatus;
import com.coxandkings.travel.operations.model.manageofflinebooking.manualofflinebooking.OfflineProducts;
import com.coxandkings.travel.operations.model.manageofflinebooking.manualofflinebooking.OfflineSearch;
import com.coxandkings.travel.operations.model.todo.ToDoTask;
import com.coxandkings.travel.operations.repository.manageofflinebooking.ManualOfflineBookingRepository;
import com.coxandkings.travel.operations.repository.manageofflinebooking.ManualOfflineProductsRepository;
import com.coxandkings.travel.operations.repository.manageofflinebooking.ManualOfflineSearchRepository;
import com.coxandkings.travel.operations.resource.ErrorResponseResource;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResource;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.manageofflinebooking.manualofflinebooking.ManualOfflineBookingService;
import com.coxandkings.travel.operations.service.manageofflinebooking.manualofflinebooking.ProductBookingHandler;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.coxandkings.travel.operations.utils.manageOfflineBooking.MDMDataUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;



import java.net.URI;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.*;

import static com.coxandkings.travel.operations.utils.manageOfflineBooking.MDMDataUtils.isStringNotNullAndNotEmpty;

@Service
public class ManualOfflineBookingServiceImpl implements ManualOfflineBookingService {

    private static Logger logger = LogManager.getLogger(ManualOfflineBookingServiceImpl.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ManualOfflineSearchRepository manualOfflineSearchRepository;

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private List<ProductBookingHandler> services;

    @Autowired
    private ManualOfflineBookingRepository manualOfflineBookingRepository;

    @Autowired
    private ManualOfflineProductsRepository manualOfflineProductsRepository;

    @Autowired
    private ProductBookingHandler productBookingHandler;

    @Autowired
    private UserService userService;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Value("${offline-booking.BE-create-booking}")
    private String createBookingUrl;

    @Value(value= "${offlineBooking.clientType}")
    private String clientTypeURL;

    @Autowired
    private MDMDataUtils dataUtils;

    @Autowired
    private ToDoTaskService toDoTaskService;

    private static String entityName = "offline-booking";

    private ProductBookingHandler serviceForProduct(String product) {
        for (ProductBookingHandler service : services) {
            if (service.isResponsibleFor(product)) {
                return service;
            }
        }
        throw new UnsupportedOperationException("Unsupported ProductType");
    }

    private ProductBookingHandler getProductBookingHandler(JSONObject createBookingJSON) throws OperationException {
        String product;
        try {
            String productCategory = createBookingJSON.getJSONObject("productDetails").getString("productCategory");
            String productSubCategory = createBookingJSON.getJSONObject("productDetails").getString("productSubCategory");
            product = (productCategory.equalsIgnoreCase("Accommodation") || productCategory.equalsIgnoreCase("Activities")) ? productCategory : productSubCategory;
        } catch (Exception e) {
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception in creating product booking handler, %s ", e.getMessage()));
            throw new OperationException(entity);
        }
        return serviceForProduct(product);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public JSONObject createManualOfflineBooking(JSONObject createBookingJSON) throws OperationException {

        JSONObject bookingJSON = new JSONObject();
        JSONObject invoiceJSON = new JSONObject();
        HttpHeaders headers = null;
        HttpEntity<String> httpEntity = null;

        try {
            String product = createBookingJSON.getJSONObject("productDetails").getString("productSubCategory");
            logger.debug(String.format("Offline Booking for %s : Starts", product));
            JSONObject mdmDataJSon = dataUtils.loadMDMData(createBookingJSON);
            productBookingHandler = getProductBookingHandler(createBookingJSON);

            logger.debug("Creating commercials request and getting response : Starts");
            JSONObject strApplyCommResp = productBookingHandler.calculateCommercials(createBookingJSON, mdmDataJSon);
            logger.debug("Creating commercials request and getting response : Ends");

            logger.debug("Create Kafka request : Starts");
            String bookID = generateBookRefrenceNumber(createBookingJSON, mdmDataJSon);
            bookingJSON = productBookingHandler.saveBooking(strApplyCommResp, createBookingJSON, bookID);
            enhanceResponseHeader(strApplyCommResp, mdmDataJSon, createBookingJSON);
            logger.debug("Created Kafka request : Ends");

            logger.debug("Save Booking : Starts");
            headers = new HttpHeaders();
            headers.set("content-type", MediaType.APPLICATION_JSON_VALUE);
            httpEntity = new HttpEntity<String>(bookingJSON.toString(), headers);
            RestUtils.getTemplate().exchange(createBookingUrl, HttpMethod.POST, httpEntity, String.class).toString();
            logger.debug("Save Booking : Ends");

            logger.debug("Generate Booking Invoice: Starts");
            invoiceJSON = productBookingHandler.generateBookingInvoice(strApplyCommResp, createBookingJSON, bookID);
            httpEntity = new HttpEntity<String>(invoiceJSON.toString(), headers);
            RestUtils.getTemplate().exchange(createBookingUrl, HttpMethod.POST, httpEntity, String.class).toString();
            logger.debug("Generate Booking Invoice: End");

            createBookingJSON.put("bookReferenceId", bookID);
            JSONObject res = saveManualOfflineBooking(createBookingJSON);

            return res;
        } catch (OperationException e) {
            logger.error("Exception in createManualOfflineBooking :", e.getMessage());
            logger.debug(String.format("UI JSON Request %s : ", createBookingJSON));
            logger.debug(String.format("KAFKA JSON Request %s : ", bookingJSON.toString()));
            throw e;
        } catch (Exception e) {
            logger.debug(String.format("UI JSON Request %s : ", createBookingJSON));
            logger.debug(String.format("KAFKA JSON Request %s : ", bookingJSON.toString()));
            logger.error("Exception in createManualOfflineBooking :", e.getMessage());
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception in createManualOfflineBooking :" + e.getMessage()));
            throw new OperationException(entity);
        }
    }

    private void enhanceResponseHeader(JSONObject strApplyCommResp, JSONObject mdmDataJSon, JSONObject createBooking) {
        JSONObject resHeader = strApplyCommResp.optJSONObject("responseHeader");
        if (mdmDataJSon.optJSONObject("orgHierarchy") != null) {
            JSONObject orgHierarchy = mdmDataJSon.getJSONObject("orgHierarchy");
            resHeader.put("groupOfCompaniesID", orgHierarchy.optString("groupOfCompaniesId"));
            resHeader.put("groupCompanyID", orgHierarchy.optString("groupCompanyId"));
            resHeader.put("companyID", orgHierarchy.optString("companyId"));
            resHeader.put("companyName", orgHierarchy.optString("name"));
            resHeader.put("sbu", orgHierarchy.optString("SBU"));
            resHeader.put("bu", orgHierarchy.optString("BU"));
        }
        if (createBooking.optJSONObject("clientDetails") != null) {
            resHeader.put("clientCategory", createBooking.getJSONObject("clientDetails").optString("clientCategory"));
            resHeader.put("clientSubCategory", createBooking.getJSONObject("clientDetails").optString("clientSubCategory"));
            resHeader.put("companyMarket", createBooking.getJSONObject("clientDetails").optString("companyMarket"));
        }
    }

    private String generateBookRefrenceNumber(JSONObject createBookingJSON, JSONObject mdmDataJSon) {
        StringBuilder bookId = new StringBuilder();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        bookId.append(cal.get(Calendar.YEAR));
        if (mdmDataJSon.optJSONObject("orgHierarchy") != null) {
            JSONObject orgHierarchy = mdmDataJSon.getJSONObject("orgHierarchy");
            if (isStringNotNullAndNotEmpty(orgHierarchy.optString("name")))
                bookId.append(orgHierarchy.getString("name").substring(0, 3));
            else
                bookId.append("com");

            if (isStringNotNullAndNotEmpty(orgHierarchy.optString("SBU")))
                bookId.append(orgHierarchy.getString("SBU").substring(0, 3));
            else
                bookId.append("SBU").substring(0, 3);

        } else {
            bookId.append("com");
            bookId.append("SBU");
        }

        String randomId = UUID.randomUUID().toString();
        bookId.append(randomId.substring(0, 7));

        return bookId.toString();
    }

    @Override
    public JSONObject saveManualOfflineBooking(JSONObject createBookingJSON) throws OperationException {
        JSONObject response = new JSONObject();
        try {
            logger.debug("Saving data in Operation DB : Starts");
            String product = createBookingJSON.getJSONObject("productDetails").getString("productSubCategory");
            OfflineProducts oldProduct = null;

            if (isStringNotNullAndNotEmpty(createBookingJSON.optString("offlineBookingId")) == false &&
                    isStringNotNullAndNotEmpty(createBookingJSON.optString("bookReferenceId")) == false) {
                oldProduct = populateData(createBookingJSON, productBookingHandler, null);
                if (isStringNotNullAndNotEmpty(createBookingJSON.optString("bookReferenceId")))
                    response.put("bookReferenceId", createBookingJSON.optString("bookReferenceId"));

                response.put("offlineBookingId", oldProduct.getId());
                response.put("message", "Record saved successfully");
                return response;
            } else if (isStringNotNullAndNotEmpty(createBookingJSON.optString("offlineBookingId")) &&
                    isStringNotNullAndNotEmpty(createBookingJSON.optString("bookReferenceId")) == false) {
                oldProduct = manualOfflineProductsRepository.findById(createBookingJSON.optString("offlineBookingId"));
                populateData(createBookingJSON, productBookingHandler, oldProduct);
                if (isStringNotNullAndNotEmpty(createBookingJSON.optString("bookReferenceId")))
                    response.put("bookReferenceId", createBookingJSON.optString("bookReferenceId"));

                response.put("offlineBookingId", oldProduct.getId());
                response.put("message", "Record updated Successfully.");
                return response;
            } else if (isStringNotNullAndNotEmpty(createBookingJSON.optString("offlineBookingId")) &&
                    isStringNotNullAndNotEmpty(createBookingJSON.optString("bookReferenceId"))) {
                oldProduct = manualOfflineProductsRepository.findById(createBookingJSON.optString("offlineBookingId"));
                oldProduct = populateData(createBookingJSON, productBookingHandler, oldProduct);

                response.put("bookReferenceId", createBookingJSON.optString("bookReferenceId"));
                response.put("offlineBookingId", oldProduct.getId());
                response.put("message", "Record Saved Successfully");
            }else {
                oldProduct = populateData(createBookingJSON, productBookingHandler, null);

                if (isStringNotNullAndNotEmpty(createBookingJSON.optString("bookReferenceId")))
                    response.put("bookReferenceId", createBookingJSON.optString("bookReferenceId"));
                response.put("offlineBookingId", oldProduct.getId());
                response.put("message", "Record Saved Successfully");
            }
            logger.debug(String.format("Offline Booking for %s : Ends", product));
        } catch (OperationException e) {
            logger.error("Exception in saveManualOfflineBooking :", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Exception in saveManualOfflineBooking :", e.getMessage());
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception in createManualOfflineBooking :" + e.getMessage()));
            throw new OperationException(entity);
        }
        return response;
    }

    private OfflineProducts populateData(JSONObject createBookingJSON, ProductBookingHandler productBookingHandler, OfflineProducts productExist) throws OperationException {
        OfflineProducts product = null;
        try {
            OfflineSearch search = null;
            if (productExist == null) {
                product = new OfflineProducts();
            } else {
                product = productExist;
            }

            if (createBookingJSON.optJSONObject("clientDetails") != null)
                product.setClientDetails(createBookingJSON.getJSONObject("clientDetails").toString());
            if (createBookingJSON.optJSONObject("productDetails") != null)
                product.setProductDetails(createBookingJSON.optJSONObject("productDetails").toString());
            if (createBookingJSON.optJSONObject("travelAndPassengerDetails") != null)
                product.setTravelAndPassengerDetails(createBookingJSON.optJSONObject("travelAndPassengerDetails").toString());
            if (createBookingJSON.optJSONObject("paymentDetails") != null)
                product.setPaymentDetails(createBookingJSON.optJSONObject("paymentDetails").toString());

            OpsBooking opsBooking = null;
            if (isStringNotNullAndNotEmpty(createBookingJSON.optString("bookReferenceId"))) {
                opsBooking = opsBookingService.getBooking(createBookingJSON.optString("bookReferenceId"));
                if (opsBooking != null) {
                    if (opsBooking.getStatus().equals(OpsBookingStatus.CNF)) {
                        product.setBookingStatus("Booked");
                        product.setBookRefNumber(createBookingJSON.getString("bookReferenceId"));
                    } else {
                        product.setBookingStatus("Pending");
                    }
                } else {
                    product.setBookingStatus("Pending");
                }

            } else {
                product.setBookingStatus("Pending");
            }

            product.setDeleted(false);
            product.setCreatedByUserId(userService.getLoggedInUserId());
            product.setCreatedTime(Instant.now().toEpochMilli());
            product.setLastModifiedByUserId(userService.getLoggedInUserId());
            product.setLastModifiedTime(Instant.now().toEpochMilli());
            OfflineProducts saveProd = manualOfflineProductsRepository.saveCreateRequestDetails(product);

            OfflineSearch oldSearch = manualOfflineSearchRepository.getByProductId(saveProd.getId());
            if (oldSearch == null) {
                search = new OfflineSearch();
            } else {
                search = oldSearch;
            }

            if (isStringNotNullAndNotEmpty(createBookingJSON.optString("bookReferenceId"))) {
                if (opsBooking != null) {
                    if (opsBooking.getStatus().equals(OpsBookingStatus.CNF)) {
                        search.setBookingStatus("Booked");
                        search.setBookRefNumber(createBookingJSON.optString("bookReferenceId"));
                    } else {
                        search.setBookingStatus("Pending");
                    }
                } else {
                    search.setBookingStatus("Pending");
                }

            } else {
                search.setBookingStatus("Pending");
            }

            if (createBookingJSON.optJSONObject("clientDetails") != null) {
                search.setClientName(createBookingJSON.getJSONObject("clientDetails").optString("clientName"));
                search.setClienttype(createBookingJSON.getJSONObject("clientDetails").optString("clientType"));
                search.setCompanyMarket(createBookingJSON.getJSONObject("clientDetails").optString("companyMarket"));
            }

            if (createBookingJSON.optJSONObject("productDetails") != null) {
                JSONObject prodDetails = createBookingJSON.getJSONObject("productDetails");
                search.setProductCategory(prodDetails.optString("productCategory"));
                search.setProductSubCategory(prodDetails.optString("productSubCategory"));
                if (prodDetails.optJSONObject("productName") != null) {
                    search.setProductName(prodDetails.optJSONObject("productName").optString("name"));
                }
            }
            if (createBookingJSON.optJSONObject("travelAndPassengerDetails") != null) {
                search.setSupplierName(productBookingHandler.getSupplierName(createBookingJSON.getJSONObject("travelAndPassengerDetails")));
                search.setLeadPaxName(productBookingHandler.getLeadPaxName(createBookingJSON.getJSONObject("travelAndPassengerDetails")));
            }
            search.setProductId(product.getId());
            search.setCreatedByUserId(userService.getLoggedInUserId());
            search.setCreatedTime(Instant.now().toEpochMilli());
            search.setLastModifiedByUserId(userService.getLoggedInUserId());
            search.setLastModifiedTime(Instant.now().toEpochMilli());
            manualOfflineSearchRepository.saveCreateRequestDetails(search);
        } catch (OperationException e) {
            logger.error("Exception in populateData :", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Exception in populateData :", e.getMessage());
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception in populateData :" + e.getMessage()));
            throw new OperationException(entity);
        }
        return product;
    }

    public ErrorResponseResource getMessageToUser(String errorStr, HttpStatus httpStatus) {
        ErrorResponseResource resource = new ErrorResponseResource();
        resource.setMessage(errorStr);
        resource.setStatus(httpStatus);
        resource.setCode("");
        return resource;
    }

    @Override
    public JSONObject fetchDataFromMDM(JSONObject reqJson) throws OperationException {
        JSONObject jsObjFetchData;
        try {
            logger.debug(String.format("Fetch supplier data from MDM : Starts"));
            productBookingHandler = getProductBookingHandler(reqJson);
            jsObjFetchData = productBookingHandler.fetchDataFromMDM(reqJson);
            logger.debug(String.format("Fetch supplier data from MDM : Ends"));
        } catch (OperationException e) {
            logger.error("Exception in fetchDataFromMDM :", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Exception in fetchDataFromMDM :", e.getMessage());
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception in fetchDataFromMDM :" + e.getMessage()));
            throw new OperationException(entity);
        }
        return jsObjFetchData;
    }

    @Override
    public JSONObject getBooking(String bookingRefId) throws OperationException {
        JSONObject res = new JSONObject();
        try {
            List<OfflineProducts> bookings = manualOfflineProductsRepository.getBooking(bookingRefId);
            if (bookings.size() != 0 || bookings != null) {
                for (OfflineProducts booking : bookings) {
                    res.put("clientDetails", new JSONObject(booking.getClientDetails()));
                    res.put("productDetails", new JSONObject(booking.getProductDetails()));
                    res.put("travelAndPassengerDetails", new JSONObject(booking.getTravelAndPassengerDetails()));
                    res.put("paymentDetails", new JSONObject(booking.getPaymentDetails()));
                }
            }
            if (res.length() == 0) {
                JSONObject msgJson = new JSONObject();
                msgJson.put("message", String.format("No bookings found for bookid %s", bookingRefId));
                return msgJson;
            }
        } catch (OperationException e) {
            logger.error("Exception in getBooking :", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Exception in getBooking :", e.getMessage());
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception in getBooking :" + e.getMessage()));
            throw new OperationException(entity);
        }
        return res;
    }

    @Override
    public Map<String, Object> searchOfflineBookings(JSONObject criteriaJson) throws OperationException {
        return manualOfflineSearchRepository.searchOfflineBookings(criteriaJson);
    }

    @Override
    public ErrorResponseResource updateOfflineBookings(JSONObject updateReq) throws OperationException {
        if (isStringNotNullAndNotEmpty(updateReq.optString("bookId")))
            throw new OperationException("Please provide bookReferenceNumber");

        if (isStringNotNullAndNotEmpty(updateReq.optString("orderId")))
            throw new OperationException("Please provide offline booking Id");


        try {
            OfflineProducts offlineBookings = manualOfflineProductsRepository.updateBooking(updateReq);
            if (offlineBookings != null) {

            }
        } catch (OperationException e) {
            logger.info("Exception in update booking" + e.getErrors());
            throw e;
        } catch (Exception e) {
            logger.info("Exception in update booking");
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception in update booking" + e.getMessage()));
            throw new OperationException(entity);
        }
        return getMessageToUser(messageSource.getMessage(Constants.OPS_MANU_OFF_BOOK_001, new Object[]{"booking updated successfully"}, Locale.US), HttpStatus.OK);
    }

    @Override
    public String deleteBooking(String offlineBookId) throws OperationException {
        if (offlineBookId == null)
            throw new OperationException("please provide offlineBookId");
        try {
            return manualOfflineProductsRepository.deleteBooking(offlineBookId);
        } catch (OperationException e) {
            logger.info("Exception occurred while deleting booking");
            throw e;
        } catch (Exception e) {
            logger.info("Exception occurred while deleting booking");
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred while deleting booking" + e.getMessage()));
            throw new OperationException(entity);
        }
    }

    @Override
    public JSONObject findById(String offlineBookId) throws OperationException {
        JSONObject res = new JSONObject();
        try {
            OfflineProducts product = manualOfflineProductsRepository.findById(offlineBookId);
            res.put("clientDetails", new JSONObject(product.getClientDetails()));
            res.put("productDetails", new JSONObject(product.getProductDetails()));
            res.put("travelAndPassengerDetails", new JSONObject(product.getTravelAndPassengerDetails()));
            res.put("paymentDetails", new JSONObject(product.getPaymentDetails()));
            res.put("bookReferenceId", product.getBookRefNumber());
            res.put("offlineBookingId", product.getId());
        } catch (OperationException e) {
            logger.info("Exception occurred in findById method");
            throw e;
        } catch (Exception e) {
            logger.info("Exception occurred in findById method");
            Map<String, String> entity = new HashMap<>();
            entity.put("message", String.format("Exception occurred in findById method" + e.getMessage()));
            throw new OperationException(entity);
        }
        return res;
    }

    @Override
    @Transactional
    public JSONObject createTODO(JSONObject reqJson) throws OperationException {
        ToDoTaskResource toDoTaskResource = new ToDoTaskResource();
        toDoTaskResource.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());
        toDoTaskResource.setTaskNameId(ToDoTaskNameValues.INTERNAL_SUPPLIER_CREATOR.getValue());
        String uuid = UUID.randomUUID().toString();
        String refId = "SUPP-NOT-FOUND".concat(uuid.substring(0,7));
        toDoTaskResource.setReferenceId(refId);
        toDoTaskResource.setProductId(""); //TODO
        toDoTaskResource.setTaskSubTypeId(ToDoTaskSubTypeValues.OFFLINE_BOOKING.toString());
        toDoTaskResource.setSuggestedActions("create new supplier");
        toDoTaskResource.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
        toDoTaskResource.setDueOnDate(ZonedDateTime.now().plusDays(5));
        toDoTaskResource.setCreatedByUserId(userService.getLoggedInUserId());
        toDoTaskResource.setClientCategoryId(reqJson.getJSONObject("clientDetails").getString("clientCategory"));
        toDoTaskResource.setClientSubCategoryId(reqJson.getJSONObject("clientDetails").getString("clientSubCategory"));
        toDoTaskResource.setClientTypeId(reqJson.getJSONObject("clientDetails").getString("clientType"));
        toDoTaskResource.setCompanyMarketId(reqJson.getJSONObject("clientDetails").getString("companyMarket"));
        JSONObject orgHierarchyJson = MDMDataUtils.getOrgHierarchyJson();
        if(orgHierarchyJson!=null)
            toDoTaskResource.setCompanyId(orgHierarchyJson.optString("companyId"));
        toDoTaskResource.setAssignedBy(userService.getLoggedInUserId());
        toDoTaskResource.setFileHandlerId(""); //TODO: assigned To whom???
        ToDoTask toDoTask = null;
        String message = "";
        try{
            toDoTask = toDoTaskService.save(toDoTaskResource);
            if(toDoTask!=null)
                message = "TODO task created successfully";
            else
                message = "TODO taask is not created successfully";
        }catch (Exception e){
            logger.info("Exception occurred in findById method");
            message = "TODO taask is not created successfully";
        }
        JSONObject res = new JSONObject();
        res.put("message",message);
        return res;
    }

    @Override
    public JSONObject getClientCcy(JSONObject req) throws OperationException {
        String market = req.optString("companyMarket");
        String clientType = req.optString("clientType");
        JSONObject clientTypeJson = getPOSAndClientLang(market,clientType);
        JSONArray transCurrJsonArr = clientTypeJson.getJSONObject(com.coxandkings.travel.operations.utils.manageOfflineBooking.Constants.MDM_PROP_CLIENTSTRUCT).optJSONArray(com.coxandkings.travel.operations.utils.manageOfflineBooking.Constants.MDM_PROP_TRANS_CCY);
        if(transCurrJsonArr!=null){
            for(int i=0;i<transCurrJsonArr.length();i++){
                JSONObject currencyJson = transCurrJsonArr.getJSONObject(i);
                if(market.equalsIgnoreCase(currencyJson.optString(com.coxandkings.travel.operations.utils.manageOfflineBooking.Constants.JSON_PROP_MKT))){
                    if(currencyJson.optString(com.coxandkings.travel.operations.utils.manageOfflineBooking.Constants.JSON_PROP_CCY).isEmpty()){
                        req.put("clientCurrency","");
                    }
                    else {
                        req.put("clientCurrency",currencyJson.optString(com.coxandkings.travel.operations.utils.manageOfflineBooking.Constants.JSON_PROP_CCY));
                    }

                }

            }
        }
        return req;
    }

    public JSONObject getPOSAndClientLang(String market,String clientType)throws OperationException{
        try{
            JSONObject filters = new JSONObject();
            filters.put("deleted", false);
            filters.put(com.coxandkings.travel.operations.utils.manageOfflineBooking.Constants.MDM_PROP_CLIENTSTRUCT.concat(".").concat(com.coxandkings.travel.operations.utils.manageOfflineBooking.Constants.MDM_PROP_CLIENTENTITYTYPE), clientType);
            filters.put(com.coxandkings.travel.operations.utils.manageOfflineBooking.Constants.MDM_PROP_CLIENTSTRUCT.concat(".").concat(com.coxandkings.travel.operations.utils.manageOfflineBooking.Constants.MDM_PROP_CLIENTMARKET), market);
            String URL = clientTypeURL + objectMapper.writeValueAsString(filters);
            URI uri = UriComponentsBuilder.fromUriString(URL).build().encode().toUri();
            String result = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
            JSONObject latestJson = null;
            if(isStringNotNullAndNotEmpty(result)){
                latestJson = MDMDataUtils.getLatestUpdatedJson(new JSONObject(result));
            }
            return latestJson ;
        }catch(Exception e){
            logger.info("Exception occured while fetching POS and client language");
        }
        return null;
    }

}
