package com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.impl;

import com.coxandkings.travel.ext.model.be.BookingActionConstants;
import com.coxandkings.travel.ext.model.be.SpecialServiceRequest;
import com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability.ServiceOrderSearchCriteria;
import com.coxandkings.travel.operations.enums.prepaymenttosupplier.ServiceOrderValue;
import com.coxandkings.travel.operations.enums.serviceOrderAndSupplierLiability.ServiceOrderAndSupplierLiabilityType;
import com.coxandkings.travel.operations.enums.todo.*;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice.PaymentAdvice;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice.PaymentAdviceOrderInfo;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentdetails.PaymentDetails;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.*;
import com.coxandkings.travel.operations.repository.serviceOrderAndSupplierLiability.ProvisionalServiceOrderRepository;
import com.coxandkings.travel.operations.repository.serviceOrderAndSupplierLiability.impl.FinalServiceOrderRepositoryImpl;
import com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability.DuplicateBookingsInfoResource;
import com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability.DuplicateOrdersResource;
import com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability.ServiceOrderResource;
import com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability.SupplierPricingResource;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResource;
import com.coxandkings.travel.operations.resource.user.OpsUser;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.*;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.systemlogin.MDMToken;
import com.coxandkings.travel.operations.utils.*;
import com.coxandkings.travel.operations.utils.serviceOrderAndSupplierLiability.ProcessServiceOrder;
import com.coxandkings.travel.operations.utils.serviceOrderAndSupplierLiability.ServiceOrderAndSupplierLiabilityUtils;
import com.coxandkings.travel.operations.utils.serviceOrderAndSupplierLiability.ServiceOrderGenerationDateComparator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.URI;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.coxandkings.travel.operations.enums.product.OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT;
import static com.coxandkings.travel.operations.enums.product.OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS;

@Service
public class ServiceOrderAndSupplierLiabilityServiceImpl implements ServiceOrderAndSupplierLiabilityService {

    @Value(value = "${service_order.get_pax_info.path_expression}")
    private String paxInfoPath;

    @Value(value = "${mdm.get_commercial_info}")
    private String mdmSupplierCommercialsUrl;

    @Value(value = "${service_order.mdm.path_expression}")
    private String supplierCommercialPathExpression;

    @Autowired
    private ProvisionalServiceOrderService provisionalServiceOrderService;

    @Autowired
    private FinalServiceOrderService finalServiceOrderService;

    @Autowired
    private ProvisionalSupplierLiabilityService provisionalSupplierLiabilityService;

    @Autowired
    private FinalSupplierLiabilityService finalSupplierLiabilityService;

    @Autowired
    private ProvisionalServiceOrderRepository provisionalServiceOrderRepository;

    @Autowired
    private FinalServiceOrderRepositoryImpl finalServiceOrderRepository;

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private DuplicateBookingsService duplicateBookingsService;

    @Autowired
    ToDoTaskService toDoTaskService;

    @Value(value = "${ROE.booking-date}")
    private String getROEUrl;

    @Value(value = "${mdm.company}")
    private String company_url;

    @Autowired
    private UserService userService;

    @Autowired
    private MDMToken mdmToken;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static String entityName = "SupplierBillPassing";

    private static Logger logger = LogManager.getLogger(ProcessServiceOrder.class);

    @Override
    public Map<String, Object> getServiceOrdersAndSupplierLiabilities(ServiceOrderSearchCriteria serviceOrderSearchCriteria) throws OperationException, IOException, JSONException {

        return getServiceOrdersAndSupplierLiabilities(serviceOrderSearchCriteria , true);
    }

    @Override
    public Map<String, Object> getServiceOrdersAndSupplierLiabilities(ServiceOrderSearchCriteria serviceOrderSearchCriteria, Boolean checkForUserCompany) throws OperationException, IOException, JSONException {
        Map<String, Object> result = new HashMap<>();
        logger.info("Entered ServiceOrderAndSupplierLiabilityServiceImpl::getServiceOrdersAndSupplierLiabilities() method to search Service Order and Supplier Liabilities");
        Integer pageSize = 10;
        Integer pageNo = 1;
        if (serviceOrderSearchCriteria.getPageSize() != null) {
            pageSize = serviceOrderSearchCriteria.getPageSize();
        }
        if (serviceOrderSearchCriteria.getPageNumber() != null) {
            pageNo = serviceOrderSearchCriteria.getPageNumber();
        }
        if (!StringUtils.isEmpty(serviceOrderSearchCriteria.getServiceOrderType())) {
            if (!StringUtils.isEmpty(serviceOrderSearchCriteria.getApplyPagination()) && !serviceOrderSearchCriteria.getApplyPagination()) {
                serviceOrderSearchCriteria.setPageSize(null);
                serviceOrderSearchCriteria.setPageNumber(null);
            }
            return searchServiceOrderDetails(serviceOrderSearchCriteria, pageNo, pageSize, checkForUserCompany);

        } else if (!StringUtils.isEmpty(serviceOrderSearchCriteria.getServiceOrderTypeValue())) {
            return searchServiceOrderDetailsForSupplierBillPassingEntry(serviceOrderSearchCriteria, pageNo, pageSize, checkForUserCompany);
        } else {
            if (serviceOrderSearchCriteria.getOrderIds() != null && serviceOrderSearchCriteria.getOrderIds().size() == 1)
                serviceOrderSearchCriteria.setOrderId(serviceOrderSearchCriteria.getOrderIds().iterator().next());

            List<ServiceOrderResource> serviceOrderAndSupplierLiabilityList = new ArrayList<>();
            result = finalServiceOrderService.getFinalServiceOrders(serviceOrderSearchCriteria, checkForUserCompany);
            serviceOrderAndSupplierLiabilityList.addAll((Collection<? extends ServiceOrderResource>) result.get("result"));
            if(serviceOrderAndSupplierLiabilityList.size() == 0) {
                result = provisionalServiceOrderService.getProvisionalServiceOrders(serviceOrderSearchCriteria, checkForUserCompany);
                serviceOrderAndSupplierLiabilityList.addAll((Collection<? extends ServiceOrderResource>) result.get("result"));
            }

            if (!StringUtils.isEmpty(serviceOrderSearchCriteria.getApplyPagination()) && !serviceOrderSearchCriteria.getApplyPagination()) {
                result.put("result", serviceOrderAndSupplierLiabilityList);
                result.put("noOfPages", 1);
            } else {
                result.put("result", serviceOrderAndSupplierLiabilityList);
            }
            return result;
        }
    }

    private Map<String, Object> searchServiceOrderDetailsForSupplierBillPassingEntry(ServiceOrderSearchCriteria serviceOrderSearchCriteria, Integer pageNo, Integer pageSize, Boolean checkForUserCompany) throws IOException, OperationException {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> response;
        List<ServiceOrderResource> resourceList;
        if (serviceOrderSearchCriteria.getServiceOrderTypeValue().equals(ServiceOrderAndSupplierLiabilityType.PSO.getValue())) {
            if (serviceOrderSearchCriteria.getOrderIds() != null && serviceOrderSearchCriteria.getOrderIds().size() == 1)
                serviceOrderSearchCriteria.setOrderId(serviceOrderSearchCriteria.getOrderIds().iterator().next());
            response = provisionalServiceOrderService.getProvisionalServiceOrders(serviceOrderSearchCriteria, checkForUserCompany);
        } else if (serviceOrderSearchCriteria.getServiceOrderTypeValue().equals(ServiceOrderAndSupplierLiabilityType.FSO.getValue())) {
            if (serviceOrderSearchCriteria.getOrderIds() != null && serviceOrderSearchCriteria.getOrderIds().size() == 1)
                serviceOrderSearchCriteria.setOrderId(serviceOrderSearchCriteria.getOrderIds().iterator().next());
            response = finalServiceOrderService.getFinalServiceOrders(serviceOrderSearchCriteria, checkForUserCompany);
        } else if (serviceOrderSearchCriteria.getServiceOrderTypeValue().equals(ServiceOrderAndSupplierLiabilityType.PSL.getValue())) {
            if (serviceOrderSearchCriteria.getOrderIds() != null && serviceOrderSearchCriteria.getOrderIds().size() == 1)
                serviceOrderSearchCriteria.setOrderId(serviceOrderSearchCriteria.getOrderIds().iterator().next());
            response = provisionalSupplierLiabilityService.getProvisionalSupplierLiabilities(serviceOrderSearchCriteria);
        } else if (serviceOrderSearchCriteria.getServiceOrderTypeValue().equals(ServiceOrderAndSupplierLiabilityType.FSL.getValue())) {
            if (serviceOrderSearchCriteria.getOrderIds() != null && serviceOrderSearchCriteria.getOrderIds().size() == 1)
                serviceOrderSearchCriteria.setOrderId(serviceOrderSearchCriteria.getOrderIds().iterator().next());
            response = finalSupplierLiabilityService.getFinalSupplierLiabilities(serviceOrderSearchCriteria);
        } else
            throw new OperationException(Constants.ER572);

        resourceList = (List<ServiceOrderResource>) response.get("result");
        if(resourceList.size() == 0){
            response.put("noOfPages", 0);
            return response;
        }
        if (serviceOrderSearchCriteria.getOrderIds() != null && serviceOrderSearchCriteria.getOrderIds().size() >= 2 && resourceList.size() >= 1) {
            result.put("result", resourceList.stream().filter(resource -> serviceOrderSearchCriteria.getOrderIds().contains(resource.getOrderId())).collect(Collectors.toList()));
            return applyPagination((List<ServiceOrderResource>) result.get("result"), pageNo, pageSize);
        } else {
            if(serviceOrderSearchCriteria.getSupplierBillPassingSortingCriteria()==null)
                Collections.sort(resourceList, new ServiceOrderGenerationDateComparator());
            result.put("result", resourceList);
            if (serviceOrderSearchCriteria.getPageSize() != null && serviceOrderSearchCriteria.getPageNumber() != null) {
                result.put("noOfPages", response.get("noOfPages"));
                return result;
            } else {
                return applyPagination((List<ServiceOrderResource>) result.get("result"), pageNo, pageSize);
            }
        }
    }

    private Map<String, Object> searchServiceOrderDetails(ServiceOrderSearchCriteria serviceOrderSearchCriteria, Integer pageNo, Integer pageSize, Boolean checkForUserCompany) throws IOException, OperationException {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> response;
        List<ServiceOrderResource> resourceList;
        if (serviceOrderSearchCriteria.getServiceOrderType().equals(ServiceOrderAndSupplierLiabilityType.PSO)) {
            if (serviceOrderSearchCriteria.getOrderIds() != null && serviceOrderSearchCriteria.getOrderIds().size() == 1)
                serviceOrderSearchCriteria.setOrderId(serviceOrderSearchCriteria.getOrderIds().iterator().next());
            response = provisionalServiceOrderService.getProvisionalServiceOrders(serviceOrderSearchCriteria, checkForUserCompany);
        } else if (serviceOrderSearchCriteria.getServiceOrderType().equals(ServiceOrderAndSupplierLiabilityType.FSO)) {
            if (serviceOrderSearchCriteria.getOrderIds() != null && serviceOrderSearchCriteria.getOrderIds().size() == 1)
                serviceOrderSearchCriteria.setOrderId(serviceOrderSearchCriteria.getOrderIds().iterator().next());
            response = finalServiceOrderService.getFinalServiceOrders(serviceOrderSearchCriteria, checkForUserCompany);
        } else if (serviceOrderSearchCriteria.getServiceOrderType().equals(ServiceOrderAndSupplierLiabilityType.PSL)) {
            if (serviceOrderSearchCriteria.getOrderIds() != null && serviceOrderSearchCriteria.getOrderIds().size() == 1)
                serviceOrderSearchCriteria.setOrderId(serviceOrderSearchCriteria.getOrderIds().iterator().next());
            response = provisionalSupplierLiabilityService.getProvisionalSupplierLiabilities(serviceOrderSearchCriteria);
        } else if (serviceOrderSearchCriteria.getServiceOrderType().equals(ServiceOrderAndSupplierLiabilityType.FSL)) {
            if (serviceOrderSearchCriteria.getOrderIds() != null && serviceOrderSearchCriteria.getOrderIds().size() == 1)
                serviceOrderSearchCriteria.setOrderId(serviceOrderSearchCriteria.getOrderIds().iterator().next());
            response = finalSupplierLiabilityService.getFinalSupplierLiabilities(serviceOrderSearchCriteria);
        } else
            throw new OperationException(Constants.ER572);

        resourceList = (List<ServiceOrderResource>) response.get("result");
        if(resourceList.size() ==0) {
            response.put("noOfPages", 0);
            return response;
        }

        if (serviceOrderSearchCriteria.getOrderIds() != null && serviceOrderSearchCriteria.getOrderIds().size() >= 2 && resourceList.size() >= 1) {
            List<ServiceOrderResource> serviceOrderList = resourceList.stream().filter(resource -> serviceOrderSearchCriteria.getOrderIds().contains(resource.getOrderId())).collect(Collectors.toList());
            if (!StringUtils.isEmpty(serviceOrderSearchCriteria.getApplyPagination()) && !serviceOrderSearchCriteria.getApplyPagination()) {
                if(serviceOrderSearchCriteria.getSupplierBillPassingSortingCriteria()==null)
                Collections.sort(serviceOrderList, new ServiceOrderGenerationDateComparator());
                result.put("result", serviceOrderList);
                result.put("noOfPages", 1);
                return result;
            } else {
                return applyPagination(serviceOrderList, pageNo, pageSize);
            }
        } else {
            Collections.sort(resourceList, new ServiceOrderGenerationDateComparator());
            result.put("result", resourceList);
            if (!StringUtils.isEmpty(serviceOrderSearchCriteria.getApplyPagination()) && !serviceOrderSearchCriteria.getApplyPagination()) {
                result.put("noOfPages", 1);
                return result;
            } else {
                if (serviceOrderSearchCriteria.getPageSize() != null && serviceOrderSearchCriteria.getPageNumber() != null) {
                    result.put("noOfPages", response.get("noOfPages"));
                    return result;
                } else {
                    return applyPagination((List<ServiceOrderResource>) result.get("result"), pageNo, pageSize);
                }
            }
        }
    }

    public static Map<String, Object> applyPagination(List<ServiceOrderResource> serviceOrderAndSupplierLiabilityList, Integer pageNo, Integer pageSize) throws OperationException {
        Map<String, Object> result = new HashMap<>();
        List<ServiceOrderResource> list = new ArrayList<>();
        int value = (pageNo - 1) * pageSize;
        int maxSize = value + pageSize;
        if (serviceOrderAndSupplierLiabilityList != null && serviceOrderAndSupplierLiabilityList.size() <= maxSize) {
            maxSize = serviceOrderAndSupplierLiabilityList.size();
        }
        if (serviceOrderAndSupplierLiabilityList != null && value >= serviceOrderAndSupplierLiabilityList.size()) {
            throw new OperationException(Constants.ER01);
        } else {
            for (int i = value; i < maxSize; i++) {
                list.add(serviceOrderAndSupplierLiabilityList.get(i));
            }
            Collections.sort(list, new ServiceOrderGenerationDateComparator());
            result.put("result", list);
            result.put("noOfPages", ServiceOrderAndSupplierLiabilityUtils.getNoOfPages(pageSize, serviceOrderAndSupplierLiabilityList.size()));
        }
        return result;
    }

    @Override
    public Map<String, String> processSOSLForCancelledProduct(ServiceOrderResource resource) throws OperationException, IOException, JSONException {
        Map<String, String> message = new HashMap<>();
        ServiceOrderSearchCriteria criteria = new ServiceOrderSearchCriteria();
        List<String> orderIds = new ArrayList<>();
        orderIds.add(resource.getOrderId());
        criteria.setBookingRefNo(resource.getBookingRefNo());
        criteria.setOrderIds(orderIds);
        criteria.setSupplierName(resource.getSupplierName());
        criteria.setSupplierId(resource.getSupplierId());
        criteria.setProductCategoryId(resource.getProductCategoryId());
        criteria.setProductCategorySubTypeId(resource.getProductCategorySubTypeId());
        criteria.setProductNameId(resource.getProductNameId());
        criteria.setSupplierCurrency(resource.getSupplierCurrency());
        criteria.setCompanyMarketId(resource.getCompanyMarketId());
        Map<String, Object> result = provisionalServiceOrderService.getProvisionalServiceOrders(criteria);
        List<ProvisionalServiceOrder> pso = (List<ProvisionalServiceOrder>) result.get("result");
        if (pso.size() == 1) {
            ProvisionalServiceOrder serviceOrder = pso.iterator().next();
            if (serviceOrder.getFinalServiceOrderID() == null) {
                serviceOrder.setStatus(Status.PROVISIONAL_SERVICE_ORDER_CANCELLED);
                provisionalServiceOrderRepository.updatePSO(serviceOrder);
                if (resource.getSupplierPricingResource().getCancellationCharges().compareTo(BigDecimal.valueOf(0)) == 1) {
                    resource.setType(ServiceOrderAndSupplierLiabilityType.PSO);
                    provisionalServiceOrderService.generatePSO(resource);
                    message.put("message", "New version of PSO and PSL are generated for supplier cancellation charges");
                } else {
                    message.put("message", "Latest version of PSO and PSL are cancelled");
                }
            } else {
                if (resource.getCreditOrDebitNoteNumber() != null) {
                    finalSupplierLiabilityService.generateFSL(resource);
                    message.put("message", "New version of FSL is generated with credit/debit note number");
                } else
                    throw new OperationException("Credit/Debit note number should not be null");
            }
        } else {
            throw new OperationException("Cannot generate PSO or cannot cancel PSO as PSO does not exist for the product");
        }
        return message;
    }

    @Override
    public void processBooking(OpsBooking aBooking, KafkaBookingMessage message) throws IOException, OperationException, JSONException {
        if (!aBooking.isHolidayBooking()) {
            if (message.getActionType().equalsIgnoreCase(BookingActionConstants.JSON_PROP_NEW_BOOKING)||
                message.getActionType().equalsIgnoreCase(BookingActionConstants.JSON_PROP_ON_REQUEST_BOOKING)) {

                logger.info("Processing Service Orders for Booking " + aBooking.getBookID());
                List<OpsProduct> products = aBooking.getProducts();
                List<OpsProduct> opsProductsList = new ArrayList<>();
                for (OpsProduct product : products) {
                    if (opsProductsList != null && !opsProductsList.contains(product)) {
                        List<OpsProduct> productSet = products.stream().filter(product1 -> (!product.getOrderID().equals(product1.getOrderID())) && product.getProductCategory().equals(product1.getProductCategory()) && product.getProductSubCategory().equals(product1.getProductSubCategory())).collect(Collectors.toList());
                        if (productSet != null && productSet.size() >= 1) {
                            switch (product.getOpsProductSubCategory()) {
                                case PRODUCT_SUB_CATEGORY_FLIGHT:
                                    List<OpsProduct> opsProducts = productSet.stream().filter(product1 -> product.getOrderDetails().getFlightDetails().getPaxInfo().equals(product1.getOrderDetails().getFlightDetails().getPaxInfo()) && product.getOrderDetails().getFlightDetails().getOriginDestinationOptions().iterator().next().getFlightSegment().iterator().next().getDepartureDateZDT().equals(product1.getOrderDetails().getFlightDetails().getOriginDestinationOptions().iterator().next().getFlightSegment().iterator().next().getDepartureDateZDT()) && product.getOrderDetails().getFlightDetails().getOriginDestinationOptions().iterator().next().getFlightSegment().iterator().next().getOriginLocation().equals(product.getOrderDetails().getFlightDetails().getOriginDestinationOptions().iterator().next().getFlightSegment().iterator().next().getOriginLocation())).collect(Collectors.toList());
                                    if (opsProducts.size() >= 1) {
                                        opsProducts.add(product);
                                        try {
                                            createToDoTaskForDuplicateOrderBookings(aBooking.getBookID(), opsProducts);
                                        } catch (Exception ex) {
                                            logger.info("Failed to create todo task");
                                        }
                                        opsProductsList.addAll(opsProducts);
                                    }
                                    break;

                                case PRODUCT_SUB_CATEGORY_HOTELS:
                                    List<OpsProduct> opsProducts1 = productSet.stream().filter(product1 -> product.getOrderDetails().getHotelDetails().getRooms().equals(product1.getOrderDetails().getHotelDetails().getRooms())).collect(Collectors.toList());
                                    if (opsProducts1.size() >= 1) {
                                        opsProducts1.add(product);
                                        try {
                                            createToDoTaskForDuplicateOrderBookings(aBooking.getBookID(), opsProducts1);
                                        } catch (Exception ex) {
                                            logger.info("Failed to create todo task");
                                        }
                                        opsProductsList.addAll(opsProducts1);
                                    }
                                    break;
                            }
                        }
                    }
                }
                if (opsProductsList.size() >= 1)
                    products.removeAll(opsProductsList);
                for (OpsProduct product : products) {
                    try {
                        generateServiceOrder(product, aBooking, false);
                        logger.info("Service Order is generated successfully for Order " + product.getOrderID());
                    } catch (Exception ex) {
                        logger.info("Service Order generation failed for Order " + product.getOrderID());
                    }
                }

            } else {
                if (message.getOrderNo() != null) {
                    OpsProduct opsProduct = aBooking.getProducts().stream().filter(product -> product.getOrderID().equals(message.getOrderNo())).findFirst().get();
                    switch (opsProduct.getOpsProductSubCategory()) {
                        case PRODUCT_SUB_CATEGORY_FLIGHT:
                            if (message.getActionType().equals(BookingActionConstants.JSON_PROP_AIR_CANNCELTYPE_FULLCANCEL)) {
                                generateServiceOrderForCancelledProduct(opsProduct, aBooking.getBookID());
                            } else if (message.getActionType().equals(BookingActionConstants.JSON_PROP_AIR_CANNCELTYPE_CANCELPAX) ||
                                    message.getActionType().equals(BookingActionConstants.JSON_PROP_AIR_CANNCELTYPE_CANCELJOU) ||
                                    message.getActionType().equals(BookingActionConstants.JSON_PROP_AIR_CANNCELTYPE_CANCELSSR) ||
                                    message.getActionType().equals(BookingActionConstants.JSON_PROP_AIR_CANNCELTYPE_CANCELODO) ||
                                    message.getActionType().equals(BookingActionConstants.JSON_PROP_AIR_AMENDTYPE_SSR) ||
                                    message.getActionType().equals(BookingActionConstants.JSON_PROP_AIR_AMENDTYPE_REM) ||
                                    message.getActionType().equals(BookingActionConstants.JSON_PROP_AIR_AMENDTYPE_PIS)) {
                                generateServiceOrderForAmendedProduct(opsProduct, aBooking);
                            } else {
                                //logic for product update
                                OpsProduct product = aBooking.getProducts().stream().filter(order -> order.getOrderID().equals(message.getOrderNo())).findFirst().get();
                                generateServiceOrder(product, aBooking, false);
                            }
                        case PRODUCT_SUB_CATEGORY_HOTELS:
                            if (message.getActionType().equals(BookingActionConstants.JSON_PROP_ACCO_CANNCELTYPE_FULLCANCEL)) {
                                generateServiceOrderForCancelledProduct(opsProduct, aBooking.getBookID());
                            } else if (message.getActionType().equals(BookingActionConstants.JSON_PROP_ACCO_CANNCELTYPE_ADDPAX) ||
                                    message.getActionType().equals(BookingActionConstants.JSON_PROP_ACCO_CANNCELTYPE_CANCELPAX) ||
                                    message.getActionType().equals(BookingActionConstants.JSON_PROP_ACCO_CANNCELTYPE_UPDATEPAX) ||
                                    message.getActionType().equals(BookingActionConstants.JSON_PROP_ACCO_CANNCELTYPE_UPDATEROOM) ||
                                    message.getActionType().equals(BookingActionConstants.JSON_PROP_ACCO_CANNCELTYPE_CANCELROOM) ||
                                    message.getActionType().equals(BookingActionConstants.JSON_PROP_ACCO_CANNCELTYPE_UPDATESTAYDATES)) {
                                generateServiceOrderForAmendedProduct(opsProduct, aBooking);
                            } else {
                                //logic for product update
                                OpsProduct product = aBooking.getProducts().stream().filter(order -> order.getOrderID().equals(message.getOrderNo())).findFirst().get();
                                generateServiceOrder(product, aBooking, false);
                            }
                    }
                }
            }
        } else {
            //logic for holidays

        }
    }

    @Override
    public void generateServiceOrder(OpsProduct product, String bookID, Boolean isAmended) throws IOException, OperationException {
        generateServiceOrder(product, opsBookingService.getBooking(bookID), isAmended);
    }

    @Override
    public void generateServiceOrder(OpsProduct product, OpsBooking opsBooking, Boolean isAmended) throws IOException, OperationException {

        String bookID = opsBooking.getBookID();
        if (!product.getOrderDetails().getOpsBookingAttribute().stream().anyMatch(opsBookingAttribute -> opsBookingAttribute.equals(OpsBookingAttribute.BOOKING_TYPE_TIME_LIMIT))) {
            ServiceOrderResource resource = new ServiceOrderResource();
            String supplierCcy;
            resource.setBookingRefNo(bookID);
            resource.setOrderId(product.getOrderID());
            resource.setProductCategoryId(product.getProductCategory());
            resource.setProductCategorySubTypeId(product.getProductSubCategory());
            resource.setCompanyMarketId(opsBooking.getCompanyMarket());

            ServiceOrderSearchCriteria criteria = new ServiceOrderSearchCriteria();
            CopyUtils.copy(resource, criteria);
            List<ServiceOrderResource> pso = new ArrayList<>();
            List<ServiceOrderResource> psl = new ArrayList<>();
            List<ServiceOrderResource> fso = new ArrayList<>();
            ServiceOrderResource provisionalSupplierLiability = null;
            ServiceOrderResource finalServiceOrder = null;
            try {
                Map<String, Object> result = provisionalServiceOrderService.getProvisionalServiceOrders(criteria);
                pso = (List<ServiceOrderResource>) result.get("result");
                Map<String, Object> pslResult = provisionalSupplierLiabilityService.getProvisionalSupplierLiabilities(criteria);
                psl = (List<ServiceOrderResource>) pslResult.get("result");
                Map<String, Object> fsoResult = finalServiceOrderService.getFinalServiceOrders(criteria);
                fso = (List<ServiceOrderResource>) fsoResult.get("result");
            } catch (Exception ex) {
                logger.info("No records found for Service Order as this booking is a NEW booking");
            }
            if(pso.size()==0 || psl.size()==0 || psl.size()==0)
                logger.info("No records found for Service Order as this booking is a NEW booking");

            if (psl.size() >= 1)
                provisionalSupplierLiability = psl.iterator().next();
            if (fso.size() >= 1)
                finalServiceOrder = fso.iterator().next();

            resource.setSupplierId(product.getSourceSupplierID());
            resource.setSupplierName(product.getSourceSupplierName());
            resource.setSupplierReferenceNumber(product.getSupplierReferenceId());
            resource.setDateOfGeneration(ZonedDateTime.now());
            resource.setCreatedTime(ZonedDateTime.now());

            if (finalServiceOrder == null) {
                if (product.getProductSubCategory().equals(PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory())) {
                    resource.setProductNameId(product.getOrderDetails().getFlightDetails().getOriginDestinationOptions().iterator().next().getFlightSegment().iterator().next().getOperatingAirline().getAirlineCode());
                    supplierCcy = product.getOrderDetails().getFlightDetails().getOpsFlightSupplierPriceInfo().getCurrencyCode();
                    resource.setSupplierCurrency(supplierCcy);
                    resource.setTravelCompletionDate(product.getOrderDetails().getFlightDetails().getOriginDestinationOptions().get(0).getFlightSegment().get(0).getArrivalDateZDT());
                    resource.setSupplierPricingResource(calculateSupplierPricing(opsBooking, product, pso, isAmended, supplierCcy));
                    resource.setNetPayableToSupplier(resource.getSupplierPricingResource().getNetPayableToSupplier());
                    resource.setSupplierCredentialName(product.getOrderDetails().getFlightDetails().getCredentialsName());

                    if (resource.getSupplierPricingResource().getPaymentStatus().equals(Status.PAYMENT_PENDING.getValue()))
                        resource.setSupplierSettlementStatus(Status.UNSETTLED.getValue());
                    else if (resource.getSupplierPricingResource().getPaymentStatus().equals(Status.PAID.getValue()))
                        resource.setSupplierSettlementStatus(Status.SETTLED.getValue());
                    else
                        resource.setSupplierSettlementStatus(Status.PARTIALLY_SETTLED.getValue());

                    if (product.getOrderDetails().getOpsOrderStatus() != null && product.getOrderDetails().getOpsOrderStatus().equals(OpsOrderStatus.TKD)) {
                        if (pso.size() == 0) {
                            resource.setType(ServiceOrderAndSupplierLiabilityType.FSO);
                            finalServiceOrderService.generateFSO(resource);
                        } else {
                            if (isAmended) {
                                resource.setType(ServiceOrderAndSupplierLiabilityType.FSO);
                                finalServiceOrderService.generateFSO(resource);
                                ServiceOrderResource provisionalServiceOrder = pso.iterator().next();
                                provisionalServiceOrder.setStatus(Status.PROVISIONAL_SERVICE_ORDER_CANCELLED);
                                provisionalServiceOrderService.updatePSO(provisionalServiceOrder);
                                if (provisionalSupplierLiability != null) {
                                    provisionalSupplierLiability.setStatus(Status.PROVISIONAL_SUPPLIER_LIABILITY_CANCELLED);
                                    provisionalSupplierLiabilityService.updatePSL(provisionalSupplierLiability);
                                }
                            } else {
                                resource.setType(ServiceOrderAndSupplierLiabilityType.PSO);
                                provisionalServiceOrderService.generatePSO(resource);
                                resource.setType(ServiceOrderAndSupplierLiabilityType.FSO);
                                finalServiceOrderService.generateFSO(resource);
                            }
                        }
                    } else {
                        if (pso.size() >= 1 && isAmended) {
                            resource.setType(ServiceOrderAndSupplierLiabilityType.PSO);
                            provisionalServiceOrderService.generatePSO(resource);
                            ServiceOrderResource provisionalServiceOrder = pso.iterator().next();
                            provisionalServiceOrder.setStatus(Status.PROVISIONAL_SERVICE_ORDER_CANCELLED);
                            provisionalServiceOrderService.updatePSO(provisionalServiceOrder);
                            if (provisionalSupplierLiability != null) {
                                provisionalSupplierLiability.setStatus(Status.PROVISIONAL_SUPPLIER_LIABILITY_CANCELLED);
                                provisionalSupplierLiabilityService.updatePSL(provisionalSupplierLiability);
                            }
                        } else {
                            resource.setType(ServiceOrderAndSupplierLiabilityType.PSO);
                            provisionalServiceOrderService.generatePSO(resource);
                        }
                    }

                } else if (product.getProductSubCategory().equals(PRODUCT_SUB_CATEGORY_HOTELS.getSubCategory())) {
                    resource.setProductNameId(product.getOrderDetails().getHotelDetails().getHotelName());
                    supplierCcy = product.getOrderDetails().getHotelDetails().getOpsAccoOrderSupplierPriceInfo().getCurrencyCode();
                    resource.setSupplierCurrency(supplierCcy);
                    resource.setTravelCompletionDate(DateTimeUtil.formatBEDateTimeZone(product.getOrderDetails().getHotelDetails().getRooms().iterator().next().getCheckOut()));
                    resource.setSupplierPricingResource(calculateSupplierPricing(opsBooking, product, pso, isAmended, supplierCcy));
                    resource.setNetPayableToSupplier(resource.getSupplierPricingResource().getNetPayableToSupplier());
                    resource.setSupplierCredentialName(product.getOrderDetails().getHotelDetails().getCredentialsName());

                    if (resource.getSupplierPricingResource().getPaymentStatus().equals(Status.PAYMENT_PENDING.getValue()))
                        resource.setSupplierSettlementStatus(Status.UNSETTLED.getValue());
                    else if (resource.getSupplierPricingResource().getPaymentStatus().equals(Status.PAID.getValue()))
                        resource.setSupplierSettlementStatus(Status.SETTLED.getValue());
                    else
                        resource.setSupplierSettlementStatus(Status.PARTIALLY_SETTLED.getValue());

                    resource.setType(ServiceOrderAndSupplierLiabilityType.PSO);
                    provisionalServiceOrderService.generatePSO(resource);
                    if (pso.size() >= 1 && isAmended) {
                        ServiceOrderResource provisionalServiceOrder = pso.iterator().next();
                        provisionalServiceOrder.setStatus(Status.PROVISIONAL_SERVICE_ORDER_CANCELLED);
                        provisionalServiceOrderService.updatePSO(provisionalServiceOrder);
                        if (provisionalSupplierLiability != null) {
                            provisionalSupplierLiability.setStatus(Status.PROVISIONAL_SUPPLIER_LIABILITY_CANCELLED);
                            provisionalSupplierLiabilityService.updatePSL(provisionalSupplierLiability);
                        }
                    }
                }
            } else
                throw new OperationException("Failed to generate Service Order as FSO has already been generated for the Order " + product.getOrderID());
        } else
            throw new OperationException("Cannot generate Service Order for Time Limit Booking");
    }

    public SupplierPricingResource calculateSupplierPricing(OpsBooking opsBooking, OpsProduct product, List<ServiceOrderResource> pso, Boolean isAmended,
                                                            String supplierCcy) throws OperationException {
        logger.info("Calculating Supplier Pricing for Service Order for Order " + product.getOrderID());
        SupplierPricingResource supplierPricing = new SupplierPricingResource();
        supplierPricing.setPassengerDetails(getPassengerDetails(product));

        //TODO : To identify whether it is CutAndPay OR  Non-CutAndPay
        if (!isAmended) {
            supplierPricing.setAmendmentCharges(new BigDecimal(0));
            supplierPricing.setCancellationCharges(new BigDecimal(0));
        } else {
            List<OpsProduct> productList = opsBookingService.getProductsBySubCategory(product.getProductSubCategory(), opsBooking);
            if (productList != null && productList.size() >= 1) {
                OpsProduct opsProduct = productList.stream().filter(product1 -> product1.getOrderID().equals(product.getOrderID())).findFirst().get();

                List<OpsAmendDetails> opsAmendDetailsList = opsProduct.getAmendmentChargesDetails();
                OpsAmendDetails opsAmendDetails = (opsAmendDetailsList != null && opsAmendDetailsList.size() != 0) ? opsAmendDetailsList.get(opsAmendDetailsList.size() - 1) : null;

                List<OpsCancDetails> opsCancDetailsList = opsProduct.getCancellationChargesDetails();
                OpsCancDetails opsCancDetails = (opsCancDetailsList != null && opsCancDetailsList.size() != 0) ? opsCancDetailsList.get(opsCancDetailsList.size() - 1) : null;

                if (opsAmendDetails != null && !StringUtils.isEmpty(opsAmendDetails.getSupplierAmendCharges()))
                    supplierPricing.setAmendmentCharges(new BigDecimal(opsAmendDetails.getSupplierAmendCharges()));
                else
                    supplierPricing.setAmendmentCharges(new BigDecimal(0));

                if (opsCancDetails != null && !StringUtils.isEmpty(opsCancDetails.getSupplierCancelCharges()))
                    supplierPricing.setCancellationCharges(new BigDecimal(opsCancDetails.getSupplierCancelCharges()));
                else
                    supplierPricing.setCancellationCharges(new BigDecimal(0));
            } else {
                supplierPricing.setAmendmentCharges(new BigDecimal(0));
                supplierPricing.setCancellationCharges(new BigDecimal(0));
            }
        }

        supplierPricing.setSupplierCommercials(new BigDecimal(0));
        //supplierPricing.setSupplierSettlementDueDate(ZonedDateTime.parse("2018-03-18T12:12:50.252+05:30",dtf));
        supplierPricing.setSurcharges(new BigDecimal(0));

        switch (product.getOpsProductSubCategory()) {

            case PRODUCT_SUB_CATEGORY_FLIGHT:

                BigDecimal supplierCost = BigDecimal.ZERO;
                for(PassengersDetails passengersDetails : supplierPricing.getPassengerDetails()){
                    supplierCost = supplierCost.add(passengersDetails.getSupplierCostPrice());
                }
                supplierPricing.setSupplierCost(supplierCost);
                BigDecimal supplierGst = BigDecimal.ZERO;
                for (OpsPaxTypeFareFlightSupplier paxTypeFare : product.getOrderDetails().getFlightDetails().getOpsFlightSupplierPriceInfo().getPaxTypeFares()) {
                    BigDecimal noOfPax = BigDecimal.ZERO;
                    try {
                        noOfPax = new BigDecimal(supplierPricing.getPassengerDetails().stream().filter(passengersDetails -> passengersDetails.getPassengerType().equals(paxTypeFare.getPaxType())).findFirst().get().getNoOfPassengers());
                    } catch (Exception ex) {
                        logger.info("No proper info regarding paxType is present in paxTypeFare");
                    }
                    supplierGst = supplierGst.add(new BigDecimal(paxTypeFare.getTaxes().getAmount()).multiply(noOfPax));
                }

                supplierPricing.setSupplierGst(supplierGst);
                supplierPricing.setSupplierTotalCost(supplierPricing.getSupplierCost().add(supplierPricing.getSupplierGst()));
                BigDecimal totalAncillaryCost = BigDecimal.ZERO;

                List<SpecialServiceRequest> specialServiceRequests = product.getOrderDetails().getFlightDetails().getTotalPriceInfo().getSpecialServiceRequests();
                if(specialServiceRequests!=null){
                    for(SpecialServiceRequest specialServiceRequest : specialServiceRequests){
                        totalAncillaryCost = totalAncillaryCost.add(new BigDecimal(specialServiceRequest.getAmount()));
                    }
                }

                supplierPricing.setAncillaries(totalAncillaryCost);
                supplierPricing.setSupplierTotalCost(supplierPricing.getSupplierTotalCost().add(supplierPricing.getAncillaries()));
               /* List<OpsAncillaryInfo> ancillaryInfo = jsonObjectProvider.getChildrenCollection(objectMapper.writeValueAsString(product.getOrderDetails().getFlightDetails()), paxInfoPath, OpsAncillaryInfo.class);
                if (ancillaryInfo != null && ancillaryInfo.size() >= 1) {
                    supplierPricing.setAncillaries(new BigDecimal(ancillaryInfo.stream().mapToInt(value -> Integer.parseInt(value.getAmount())).sum()));
                    supplierPricing.setSupplierTotalCost(supplierPricing.getSupplierTotalCost());
                }*/
                    break;

            case PRODUCT_SUB_CATEGORY_HOTELS:

                BigDecimal supplierTax = BigDecimal.ZERO;
                supplierCost = BigDecimal.ZERO;
                Set<PassengersDetails> roomDetails = supplierPricing.getPassengerDetails();
                for(PassengersDetails roomDetail : roomDetails){
                    supplierCost = supplierCost.add(roomDetail.getSupplierCostPrice());
                }
                supplierPricing.setSupplierCost(supplierCost);
//                supplierPricing.setSupplierCurrency(product.getRoe());
                if(product.getOrderDetails().getHotelDetails().getOpsAccoOrderSupplierPriceInfo().getTaxes().getAmount()!=null)
                    supplierTax = new BigDecimal(product.getOrderDetails().getHotelDetails().getOpsAccoOrderSupplierPriceInfo().getTaxes().getAmount());
                supplierPricing.setSupplierGst(supplierTax);
                supplierPricing.setSupplierTotalCost(supplierPricing.getSupplierCost().add(supplierPricing.getSupplierGst()));
                break;
        }
        //Calculate source Suppliers amounts/commercials as service Order will be created for only Source supplier.
        List<OpsOrderSupplierCommercial> opsOrderSupplierCommercials = product.getOrderDetails().getSupplierCommercials();
        if (opsOrderSupplierCommercials != null && opsOrderSupplierCommercials.size() >= 1) {
            BigDecimal commercialValue = calculateCommercials(product, opsBooking, opsOrderSupplierCommercials, supplierCcy);
            supplierPricing.setSupplierCommercials(commercialValue);
            supplierPricing.setNetPayableToSupplier(supplierPricing.getSupplierTotalCost().add(commercialValue));
        } else
            supplierPricing.setNetPayableToSupplier(supplierPricing.getSupplierTotalCost());

        supplierPricing.setNetPayableToSupplier(supplierPricing.getNetPayableToSupplier().add(supplierPricing.getAmendmentCharges()).add(supplierPricing.getCancellationCharges()));
        if (pso != null && pso.size() >= 1) {
            SupplierPricingResource pricing = pso.iterator().next().getSupplierPricingResource();
            supplierPricing.setAmountPaidToSupplier(pricing.getAmountPaidToSupplier());
            if (pricing.getAmountPaidToSupplier().compareTo(supplierPricing.getNetPayableToSupplier()) == 0 || pricing.getAmountPaidToSupplier().compareTo(supplierPricing.getNetPayableToSupplier()) == 1) {
                supplierPricing.setTotalBalanceAmountPayable(new BigDecimal(0));
                supplierPricing.setPaymentStatus(Status.PAID.getValue());
            } else {
                if (supplierPricing.getAmountPaidToSupplier().equals(new BigDecimal("0.00"))) {
                    supplierPricing.setTotalBalanceAmountPayable(supplierPricing.getNetPayableToSupplier().subtract(supplierPricing.getAmountPaidToSupplier()));
                    supplierPricing.setPaymentStatus(Status.PAYMENT_PENDING.getValue());
                } else {
                    supplierPricing.setTotalBalanceAmountPayable(supplierPricing.getNetPayableToSupplier().subtract(supplierPricing.getAmountPaidToSupplier()));
                    supplierPricing.setPaymentStatus(Status.PARTIALLY_PAID.getValue());
                }
            }
        } else {

            supplierPricing.setAmountPaidToSupplier(new BigDecimal(0));
            supplierPricing.setTotalBalanceAmountPayable(supplierPricing.getNetPayableToSupplier().subtract(supplierPricing.getAmountPaidToSupplier()));
            supplierPricing.setPaymentStatus(Status.PAYMENT_PENDING.getValue());
        }
        return supplierPricing;
    }

    public Set<PassengersDetails> getPassengerDetails(OpsProduct product) {

        Set<PassengersDetails> passengersDetailsSet = new HashSet<>();
        PassengersDetails adultPassenger = null;
        PassengersDetails childPassenger = null;
        PassengersDetails infantPassenger = null;

        BigDecimal totalPaxTax = BigDecimal.ZERO;
        BigDecimal totalpaxCost = BigDecimal.ZERO;
        //As discussed by sudhir with dhananjay ratePerPassenger won't include taxes.

        switch (product.getOpsProductSubCategory()) {
            case PRODUCT_SUB_CATEGORY_FLIGHT:
                List<OpsPaxTypeFareFlightSupplier> opsPaxTypeFaresFlightSupplier = product.getOrderDetails().getFlightDetails().getOpsFlightSupplierPriceInfo().getPaxTypeFares();
                for (OpsFlightPaxInfo paxInfo : product.getOrderDetails().getFlightDetails().getPaxInfo()) {
                    if (paxInfo.getPaxType().equals("ADT")) {
                        if (adultPassenger == null) {
                            adultPassenger = getFlightPassengerDetails(paxInfo.getPaxType(), opsPaxTypeFaresFlightSupplier);
                        }
                        adultPassenger.setNoOfPassengers(adultPassenger.getNoOfPassengers() + 1);
                    } else if (paxInfo.getPaxType().equals("CHD")) {
                        if (childPassenger == null) {
                            childPassenger = getFlightPassengerDetails(paxInfo.getPaxType(), opsPaxTypeFaresFlightSupplier);
                        }
                        childPassenger.setNoOfPassengers(childPassenger.getNoOfPassengers() + 1);
                    } else if (paxInfo.getPaxType().equals("INF")) {
                        if (infantPassenger == null) {
                            infantPassenger = getFlightPassengerDetails(paxInfo.getPaxType(), opsPaxTypeFaresFlightSupplier);
                        }
                        infantPassenger.setNoOfPassengers(infantPassenger.getNoOfPassengers() + 1);
                    }
                }
                if (adultPassenger != null) {
                    adultPassenger.setSupplierCostPrice(adultPassenger.getRatePerPassenger().multiply(new BigDecimal(adultPassenger.getNoOfPassengers())));
                    passengersDetailsSet.add(adultPassenger);
                }
                if (childPassenger != null) {
                    childPassenger.setSupplierCostPrice(childPassenger.getRatePerPassenger().multiply(new BigDecimal(childPassenger.getNoOfPassengers())));
                    passengersDetailsSet.add(childPassenger);
                }
                if (infantPassenger != null) {
                    infantPassenger.setSupplierCostPrice(infantPassenger.getRatePerPassenger().multiply(new BigDecimal(infantPassenger.getNoOfPassengers())));
                    passengersDetailsSet.add(infantPassenger);
                }
                break;

            case PRODUCT_SUB_CATEGORY_HOTELS:
                for (OpsRoom room : product.getOrderDetails().getHotelDetails().getRooms()) {

                    PassengersDetails roomDetails = new PassengersDetails();
//                    roomDetails.setPassengerType(paxInfo.getPaxType());
                    roomDetails.setNoOfPassengers(room.getPaxInfo().size());
                    roomDetails.setRatePerPassenger(new BigDecimal(0));
                    roomDetails.setRoomCategoryOrCabinCategory(room.getRoomTypeInfo().getRoomCategoryName());
                    roomDetails.setRoomTypeOrCabinType(room.getRoomTypeInfo().getRoomTypeName());
                    roomDetails.setPassengerType("");
                    BigDecimal roomPrice = new BigDecimal(room.getRoomSuppPriceInfo().getRoomSupplierPrice());
                    BigDecimal tax = BigDecimal.ZERO;
                    if (room.getRoomSuppPriceInfo().getTaxes().getAmount() != null)
                        tax = new BigDecimal(room.getRoomSuppPriceInfo().getTaxes().getAmount());
                    roomDetails.setSupplierCostPrice(roomPrice.subtract(tax));
                    passengersDetailsSet.add(roomDetails);
                }

        }
        return passengersDetailsSet;
    }


    private PassengersDetails getFlightPassengerDetails(String paxType, List<OpsPaxTypeFareFlightSupplier> opsPaxTypeFaresFlightSupplier) {

        BigDecimal totalpaxCost = BigDecimal.ZERO, totalPaxTax = BigDecimal.ZERO;
        PassengersDetails passenger = new PassengersDetails();
        passenger.setPassengerType(paxType);
        passenger.setNoOfPassengers(0);
        for(OpsPaxTypeFareFlightSupplier opsPaxTypeFareFlightSupplier : opsPaxTypeFaresFlightSupplier){
            if(opsPaxTypeFareFlightSupplier.getPaxType().equals(paxType)){
                totalpaxCost = new BigDecimal(opsPaxTypeFareFlightSupplier.getTotalFare().getAmount());
                totalPaxTax = new BigDecimal(opsPaxTypeFareFlightSupplier.getTaxes().getAmount());
            }
        }
        passenger.setRatePerPassenger(totalpaxCost.subtract(totalPaxTax));
        return passenger;
    }

    public BigDecimal calculateCommercials(OpsProduct product, OpsBooking opsBooking, List<OpsOrderSupplierCommercial> supplierCommercials, String supplierCcy) throws OperationException {
        String sourceSupplierId = product.getSourceSupplierID();
        BigDecimal commercialsValue = BigDecimal.ZERO;
        String bookingDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(opsBooking.getBookingDateZDT());
        HashMap<String, BigDecimal> roeMap = new HashMap<>();
        try {
            for (OpsOrderSupplierCommercial supplierCommercial : supplierCommercials) {
               //TODO: As discussed by sudhir with dhananjay, Only source supplier commericals needs to be considered for service order generation.
                if(supplierCommercial.getSupplierID()==null || supplierCommercial.getSupplierID().isEmpty()
                        || sourceSupplierId.equalsIgnoreCase(supplierCommercial.getSupplierID())) {
                    //TODO: Currently the commericalName used by BE and what is stored in MDM does not match(E.g Standard/Standard Commercial) hence commenting it for now
                    //TODO: Any which ways as discussed with Sudhir, Online Booking will always be transactional. So commented the following for now.
                     /* JSONObject jsonObject = new JSONObject();
                    jsonObject.put("commercialType", supplierCommercial.getCommercialName());
                    jsonObject.put("supplierCommercialData.commercialDefinition.supplierId", supplierId);
                    URI uri = UriComponentsBuilder.fromUriString(mdmSupplierCommercialsUrl + jsonObject.toString()).build().encode().toUri();
                    String response = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
                    Boolean isTransactional = (Boolean) jsonObjectProvider.getChildObject(response, supplierCommercialPathExpression, Boolean.class);*/

                    Boolean isTransactional = true;
                    if (isTransactional != null && isTransactional) {
                        BigDecimal commercialAmount = new BigDecimal(supplierCommercial.getCommercialAmount());
                        String commercialCurrency = supplierCommercial.getCommercialCurrency();
                        BigDecimal commercialToSuppROE = BigDecimal.ONE;
                        if(!commercialCurrency.equals(supplierCcy)) {
                            String key = String.format("%s|%s", commercialCurrency, supplierCcy);
                            commercialToSuppROE = roeMap.get(key);
                            if (commercialToSuppROE == null) {
                                try {
                                    UriComponents getROE = UriComponentsBuilder.fromUriString(getROEUrl).pathSegment(commercialCurrency).
                                            pathSegment(supplierCcy).pathSegment(opsBooking.getClientMarket()).pathSegment(bookingDate).build();
                                    commercialToSuppROE = RestUtils.getForObject(getROE.toUriString(), BigDecimal.class);
                                    roeMap.put(key, commercialToSuppROE);
                                } catch (Exception e) {
                                    //TODO: To decide whether to throw an exception.
                                    logger.warn(String.format("Unable to get ROE - %s|%s|%s|%s", commercialCurrency, supplierCcy, opsBooking.getClientMarket(), bookingDate));
                                }
                            }
                        }
                        if (supplierCommercial.getCommercialType().equalsIgnoreCase("Receivable"))
                            commercialsValue = commercialsValue.subtract(commercialAmount).multiply(commercialToSuppROE==null ? BigDecimal.ONE : commercialToSuppROE);
                        else
                            commercialsValue = commercialsValue.add(commercialAmount).multiply(commercialToSuppROE==null? BigDecimal.ONE : commercialToSuppROE);
                    }
                }
            }
        } catch (Exception ex) {
            logger.debug("supplier commercial data doesn't exist");
        }
        return commercialsValue;
    }

    public String saveDuplicateBookings(String bookID, List<OpsProduct> products) {
        DuplicateBookings duplicateBookings = new DuplicateBookings();
        List<String> ordersList = products.stream().map(product -> product.getOrderID()).collect(Collectors.toList());
        duplicateBookings.setBookingRefNo(bookID);
        duplicateBookings.setDuplicateOrderBookingIds(ordersList);
        return duplicateBookingsService.saveDuplicateBookings(duplicateBookings).getId();
    }

    @Override
    public String linkPaymentAdviceWithServiceOrder(PaymentDetails paymentDetails) throws OperationException {
        if (paymentDetails.getPaymentAdviceNumber() != null) {
            try {
                PaymentAdvice paymentAdvice = paymentDetails.getPaymentAdviceNumber();
                if (paymentAdvice.getPaymentAdviceOrderInfoSet() != null && paymentAdvice.getPaymentAdviceOrderInfoSet().size() >= 1) {
                    BigDecimal amountPaidToSupplier = null;
                    for (PaymentAdviceOrderInfo paymentAdviceOrderInfo : paymentAdvice.getPaymentAdviceOrderInfoSet()) {
                        ServiceOrderSearchCriteria serviceOrderSearchCriteria = new ServiceOrderSearchCriteria();
                        serviceOrderSearchCriteria.setBookingRefNo(paymentAdviceOrderInfo.getBookingRefId());
                        serviceOrderSearchCriteria.setOrderId(paymentAdviceOrderInfo.getOrderId());
                        serviceOrderSearchCriteria.setUniqueId(paymentAdviceOrderInfo.getServiceOrderId());
                        if (StringUtils.isEmpty(paymentAdviceOrderInfo.getServiceOrderValue())) {
                            updateProvisionalServiceOrderForPrepayment(serviceOrderSearchCriteria, paymentAdviceOrderInfo, paymentAdvice);
                        } else if (paymentAdviceOrderInfo.getServiceOrderValue().equals(ServiceOrderValue.PSO) && !paymentAdvice.getAmountPayableForSupplier().equals(BigDecimal.ZERO)) {
                            amountPaidToSupplier = updateProvisionalServiceOrder(serviceOrderSearchCriteria, paymentAdvice);
                        } else if (paymentAdviceOrderInfo.getServiceOrderValue().equals(ServiceOrderValue.FSO) && !paymentAdvice.getAmountPayableForSupplier().equals(BigDecimal.ZERO)) {
                            amountPaidToSupplier = updateFinalServiceOrder(serviceOrderSearchCriteria, paymentAdviceOrderInfo, paymentAdvice);
                        }
                        if (amountPaidToSupplier != null)
                            paymentAdvice.setAmountPayableForSupplier(amountPaidToSupplier);

                    }
                    return "Payment Advice is linked With Service Order";
                } else
                    throw new OperationException("Cannot link Payment Advice with Service Order as PaymentAdviceOrderInfoSet has no data");
            } catch (Exception ex) {
                logger.info("Failed to link payment advice with service order");
                throw new OperationException("Failed to link payment advice with service order");
            }

        } else
            throw new OperationException("Cannot link Payment Advice with Service Order as Payment Advice is not linked with Payment Details");
    }

    private void updateProvisionalServiceOrderForPrepayment(ServiceOrderSearchCriteria serviceOrderSearchCriteria, PaymentAdviceOrderInfo paymentAdviceOrderInfo, PaymentAdvice paymentAdvice) throws IOException, OperationException {

        List<ServiceOrderResource> provisionalServiceOrders = null;
        try {
            provisionalServiceOrders = (List<ServiceOrderResource>) provisionalServiceOrderService.getProvisionalServiceOrders(serviceOrderSearchCriteria).get("result");
        } catch (Exception ex) {
            logger.info("No Provisional Service Order details are found for order " + serviceOrderSearchCriteria.getOrderId());
        }

        if (provisionalServiceOrders != null && provisionalServiceOrders.size() >= 1) {
            ServiceOrderResource provisionalServiceOrderResource = new ServiceOrderResource();
            ServiceOrderResource pso = provisionalServiceOrders.iterator().next();
            CopyUtils.copy(pso, provisionalServiceOrderResource);
            SupplierPricingResource supplierPricingResource = new SupplierPricingResource();
            CopyUtils.copy(pso.getSupplierPricingResource(), supplierPricingResource);
            provisionalServiceOrderResource.setUniqueId(null);
            provisionalServiceOrderResource.setVersionNumber(null);
            supplierPricingResource.setAmountPaidToSupplier(supplierPricingResource.getAmountPaidToSupplier().add(paymentAdviceOrderInfo.getOrderLevelAmountPayableForSupplier()));
            supplierPricingResource.setTotalBalanceAmountPayable(supplierPricingResource.getNetPayableToSupplier().subtract(supplierPricingResource.getAmountPaidToSupplier()));
            if (supplierPricingResource.getAmountPaidToSupplier().equals(new BigDecimal("0.00")) || supplierPricingResource.getAmountPaidToSupplier().equals(BigDecimal.ZERO)) {
                supplierPricingResource.setPaymentStatus(Status.PAYMENT_PENDING.getValue());
                provisionalServiceOrderResource.setSupplierSettlementStatus(Status.UNSETTLED.getValue());
            } else if ((!paymentAdviceOrderInfo.getOrderLevelAmountPayableForSupplier().equals(BigDecimal.ZERO) || !paymentAdviceOrderInfo.getOrderLevelAmountPayableForSupplier().equals(new BigDecimal("0.00"))) && supplierPricingResource.getNetPayableToSupplier().compareTo(supplierPricingResource.getAmountPaidToSupplier()) == 1) {
                supplierPricingResource.setPaymentStatus(Status.PARTIALLY_PAID.getValue());
                provisionalServiceOrderResource.setSupplierSettlementStatus(Status.PARTIALLY_SETTLED_AGAINST_PRE_PAYMENT.getValue());
            } else if (supplierPricingResource.getAmountPaidToSupplier().compareTo(supplierPricingResource.getNetPayableToSupplier()) == 0) {
                supplierPricingResource.setPaymentStatus(Status.PAID.getValue());
                provisionalServiceOrderResource.setSupplierSettlementStatus(Status.SETTLED.getValue());
            }
            provisionalServiceOrderResource.setSupplierPricingResource(supplierPricingResource);
            provisionalServiceOrderResource.setNetPayableToSupplier(supplierPricingResource.getNetPayableToSupplier());
            provisionalServiceOrderResource.getPaymentAdviceSet().add(paymentAdvice);
            provisionalServiceOrderService.generatePSO(provisionalServiceOrderResource);
        }
    }

    private BigDecimal updateProvisionalServiceOrder(ServiceOrderSearchCriteria serviceOrderSearchCriteria, PaymentAdvice paymentAdvice) throws IOException, OperationException {

        BigDecimal amountPaid;
        List<ServiceOrderResource> provisionalServiceOrders = null;
        try {
            provisionalServiceOrders = (List<ServiceOrderResource>) provisionalServiceOrderService.getProvisionalServiceOrders(serviceOrderSearchCriteria).get("result");
        } catch (Exception ex) {
            logger.info("No Provisional Service Order details are found for order " + serviceOrderSearchCriteria.getOrderId());
        }
        if (provisionalServiceOrders != null && provisionalServiceOrders.size() >= 1) {
            ServiceOrderResource provisionalServiceOrderResource = new ServiceOrderResource();
            ServiceOrderResource pso = provisionalServiceOrders.iterator().next();
            CopyUtils.copy(pso, provisionalServiceOrderResource);
            SupplierPricingResource supplierPricingResource = new SupplierPricingResource();
            CopyUtils.copy(pso.getSupplierPricingResource(), supplierPricingResource);
            provisionalServiceOrderResource.setUniqueId(null);
            provisionalServiceOrderResource.setVersionNumber(null);

            if (paymentAdvice.getAmountPayableForSupplier().compareTo(supplierPricingResource.getTotalBalanceAmountPayable()) == 1) {
                supplierPricingResource.setAmountPaidToSupplier(supplierPricingResource.getAmountPaidToSupplier().add(supplierPricingResource.getTotalBalanceAmountPayable()));
                amountPaid = paymentAdvice.getAmountPayableForSupplier().subtract(supplierPricingResource.getTotalBalanceAmountPayable());
            } else if (paymentAdvice.getAmountPayableForSupplier().compareTo(supplierPricingResource.getTotalBalanceAmountPayable()) == 0) {
                supplierPricingResource.setAmountPaidToSupplier(supplierPricingResource.getAmountPaidToSupplier().add(supplierPricingResource.getTotalBalanceAmountPayable()));
                amountPaid = BigDecimal.ZERO;
            } else {
                supplierPricingResource.setAmountPaidToSupplier(supplierPricingResource.getAmountPaidToSupplier().add(paymentAdvice.getAmountPayableForSupplier()));
                amountPaid = BigDecimal.ZERO;
            }
            supplierPricingResource.setTotalBalanceAmountPayable(supplierPricingResource.getNetPayableToSupplier().subtract(supplierPricingResource.getAmountPaidToSupplier()));

            if (supplierPricingResource.getAmountPaidToSupplier().equals(new BigDecimal("0.00")) || supplierPricingResource.getAmountPaidToSupplier().equals(BigDecimal.ZERO)) {
                supplierPricingResource.setPaymentStatus(Status.PAYMENT_PENDING.getValue());
                provisionalServiceOrderResource.setSupplierSettlementStatus(Status.UNSETTLED.getValue());
            } else if (supplierPricingResource.getAmountPaidToSupplier().compareTo(supplierPricingResource.getNetPayableToSupplier()) == 0) {
                supplierPricingResource.setPaymentStatus(Status.PAID.getValue());
                provisionalServiceOrderResource.setSupplierSettlementStatus(Status.SETTLED.getValue());
            } else if (supplierPricingResource.getNetPayableToSupplier().compareTo(supplierPricingResource.getAmountPaidToSupplier()) == 1) {
                supplierPricingResource.setPaymentStatus(Status.PARTIALLY_PAID.getValue());
                provisionalServiceOrderResource.setSupplierSettlementStatus(Status.PARTIALLY_SETTLED.getValue());
            }

            provisionalServiceOrderResource.setSupplierPricingResource(supplierPricingResource);
            provisionalServiceOrderResource.setNetPayableToSupplier(supplierPricingResource.getNetPayableToSupplier());
            provisionalServiceOrderResource.getPaymentAdviceSet().add(paymentAdvice);
            provisionalServiceOrderService.generatePSO(provisionalServiceOrderResource);
            return amountPaid;
        } else
            return null;
    }

    private BigDecimal updateFinalServiceOrder(ServiceOrderSearchCriteria serviceOrderSearchCriteria, PaymentAdviceOrderInfo paymentAdviceOrderInfo, PaymentAdvice paymentAdvice) throws OperationException {

        BigDecimal amountPaid;
        List<ServiceOrderResource> finalServiceOrders = null;
        try {
            finalServiceOrders = (List<ServiceOrderResource>) finalServiceOrderService.getFinalServiceOrders(serviceOrderSearchCriteria).get("result");
        } catch (Exception ex) {
            logger.info("No Provisional Service Order details are found for order " + serviceOrderSearchCriteria.getOrderId());
        }
        if (finalServiceOrders != null && finalServiceOrders.size() >= 1) {
            ServiceOrderResource fso = finalServiceOrders.iterator().next();
            SupplierPricingResource supplierPricingResource = new SupplierPricingResource();
            CopyUtils.copy(fso.getSupplierPricingResource(), supplierPricingResource);

            if (paymentAdvice.getAmountPayableForSupplier().compareTo(supplierPricingResource.getTotalBalanceAmountPayable()) == 1) {
                supplierPricingResource.setAmountPaidToSupplier(supplierPricingResource.getAmountPaidToSupplier().add(supplierPricingResource.getTotalBalanceAmountPayable()));
                amountPaid = paymentAdvice.getAmountPayableForSupplier().subtract(supplierPricingResource.getTotalBalanceAmountPayable());
            } else if (paymentAdvice.getAmountPayableForSupplier().compareTo(supplierPricingResource.getTotalBalanceAmountPayable()) == 0) {
                supplierPricingResource.setAmountPaidToSupplier(supplierPricingResource.getAmountPaidToSupplier().add(supplierPricingResource.getTotalBalanceAmountPayable()));
                amountPaid = BigDecimal.ZERO;
            } else {
                supplierPricingResource.setAmountPaidToSupplier(supplierPricingResource.getAmountPaidToSupplier().add(paymentAdvice.getAmountPayableForSupplier()));
                amountPaid = BigDecimal.ZERO;
            }
            supplierPricingResource.setTotalBalanceAmountPayable(supplierPricingResource.getNetPayableToSupplier().subtract(supplierPricingResource.getAmountPaidToSupplier()));

            if (supplierPricingResource.getAmountPaidToSupplier().equals(new BigDecimal("0.00")) || supplierPricingResource.getAmountPaidToSupplier().equals(BigDecimal.ZERO)) {
                supplierPricingResource.setPaymentStatus(Status.PAYMENT_PENDING.getValue());
                fso.setSupplierSettlementStatus(Status.UNSETTLED.getValue());
            } else if (supplierPricingResource.getAmountPaidToSupplier().compareTo(supplierPricingResource.getNetPayableToSupplier()) == 0) {
                supplierPricingResource.setPaymentStatus(Status.PAID.getValue());
                fso.setSupplierSettlementStatus(Status.SETTLED.getValue());
            } else if (supplierPricingResource.getNetPayableToSupplier().compareTo(supplierPricingResource.getAmountPaidToSupplier()) == 1) {
                supplierPricingResource.setPaymentStatus(Status.PARTIALLY_PAID.getValue());
                fso.setSupplierSettlementStatus(Status.PARTIALLY_SETTLED.getValue());
            }

            fso.setSupplierPricingResource(supplierPricingResource);
            fso.setNetPayableToSupplier(supplierPricingResource.getNetPayableToSupplier());
            fso.getPaymentAdviceSet().add(paymentAdvice);
            finalServiceOrderService.updateFSO(fso);
            return amountPaid;
        } else return null;
    }

    @Override
    public List<String> getServiceOrderStatus() {
        List<String> status = new ArrayList<>();
        status.add(Status.PROVISIONAL_SERVICE_ORDER_GENERATED.getValue());
        status.add(Status.PROVISIONAL_SUPPLIER_LIABILITY_GENERATED.getValue());
        status.add(Status.FINAL_SERVICE_ORDER_GENERATED.getValue());
        status.add(Status.FINAL_SUPPLIER_LIABILITY_GENERATED.getValue());
        status.add(Status.PROVISIONAL_SERVICE_ORDER_CANCELLED.getValue());
        status.add(Status.PROVISIONAL_SUPPLIER_LIABILITY_CANCELLED.getValue());
        return status;
    }

    @Override
    public List<String> getSupplierSettlementStatus() {
        List<String> status = new ArrayList<>();
        status.add(Status.UNSETTLED.getValue());
        status.add(Status.PARTIALLY_SETTLED.getValue());
        status.add(Status.PARTIALLY_SETTLED_AGAINST_DEPOSIT.getValue());
        status.add(Status.PARTIALLY_SETTLED_AGAINST_PRE_PAYMENT.getValue());
        status.add(Status.SETTLED.getValue());
        status.add(Status.SETTLED_AGAINST_DEPOSIT.getValue());
        status.add(Status.SETTLED_AGAINST_PRE_PAYMENT.getValue());
        return status;
    }

    @Override
    public void linkCreditOrDebitNoteWithFSL(ServiceOrderResource serviceOrderResource) throws IOException, OperationException {
        ServiceOrderSearchCriteria searchCriteria = new ServiceOrderSearchCriteria();
        searchCriteria.setBookingRefNo(serviceOrderResource.getBookingRefNo());
        searchCriteria.setOrderId(serviceOrderResource.getOrderId());
        List<ServiceOrderResource> finalSupplierLiabilities = (List<ServiceOrderResource>) finalSupplierLiabilityService.getFinalSupplierLiabilities(searchCriteria).get("result");
        if (finalSupplierLiabilities != null && finalSupplierLiabilities.size() >= 1) {
            ServiceOrderResource fsl = finalSupplierLiabilities.iterator().next();
            fsl.setCreditOrDebitNoteNumber(serviceOrderResource.getCreditOrDebitNoteNumber());
            //todo recalculate net payable to supplier
            finalSupplierLiabilityService.generateFSL(fsl);
        } else
            throw new OperationException("No FSL details found for given BookId and OrderId");
    }

    public void generateServiceOrderForCancelledProduct(OpsProduct product, String bookID) throws IOException, OperationException {

        ServiceOrderSearchCriteria searchCriteria = new ServiceOrderSearchCriteria();
        searchCriteria.setBookingRefNo(bookID);
        searchCriteria.setOrderId(product.getOrderID());
        searchCriteria.setProductCategoryId(product.getProductCategory());
        searchCriteria.setProductCategorySubTypeId(product.getProductSubCategory());
        List<ServiceOrderResource> serviceOrderResources = (List<ServiceOrderResource>) provisionalServiceOrderService.getProvisionalServiceOrders(searchCriteria).get("result");
        List<ServiceOrderResource> supplierLiabilityResources = (List<ServiceOrderResource>) provisionalSupplierLiabilityService.getProvisionalSupplierLiabilities(searchCriteria).get("result");
        if (serviceOrderResources != null && serviceOrderResources.size() >= 1) {
            ServiceOrderResource pso = serviceOrderResources.iterator().next();
            if (pso.getFinalServiceOrderID() == null) {
                logger.info("Processing Service Order for Cancelled AbstractProductFactory");
                List<OpsProduct> productList = opsBookingService.getProductsBySubCategory(product.getProductSubCategory(), bookID);
                if (productList != null && productList.size() >= 1) {
                    OpsProduct opsProduct = productList.stream().filter(product1 -> product1.getOrderID().equals(product.getOrderID())).findFirst().get();

                    List<OpsCancDetails> opsCancDetailsList = opsProduct.getCancellationChargesDetails();
                    OpsCancDetails opsCancDetails = (opsCancDetailsList != null && opsCancDetailsList.size() != 0) ? opsCancDetailsList.get(opsCancDetailsList.size() - 1) : null;

                    if (opsCancDetails != null && !StringUtils.isEmpty(opsCancDetails.getSupplierCancelCharges())) {
                        ServiceOrderResource serviceOrderResource = new ServiceOrderResource();
                        CopyUtils.copy(pso, serviceOrderResource);
                        SupplierPricingResource supplierPricingResource = new SupplierPricingResource();
                        CopyUtils.copy(pso.getSupplierPricingResource(), supplierPricingResource);
                        supplierPricingResource.setCancellationCharges(new BigDecimal(opsCancDetails.getSupplierCancelCharges()));
                        supplierPricingResource.setNetPayableToSupplier(supplierPricingResource.getNetPayableToSupplier().add(supplierPricingResource.getCancellationCharges()));
                        supplierPricingResource.setTotalBalanceAmountPayable(supplierPricingResource.getNetPayableToSupplier().subtract(supplierPricingResource.getAmountPaidToSupplier()));
                        if (supplierPricingResource.getAmountPaidToSupplier().equals(new BigDecimal("0.00"))) {
                            supplierPricingResource.setPaymentStatus(Status.PAYMENT_PENDING.getValue());
                            serviceOrderResource.setSupplierSettlementStatus(Status.UNSETTLED.getValue());
                        } else if (supplierPricingResource.getNetPayableToSupplier().compareTo(supplierPricingResource.getAmountPaidToSupplier()) == 1) {
                            supplierPricingResource.setPaymentStatus(Status.PARTIALLY_PAID.getValue());
                            serviceOrderResource.setSupplierSettlementStatus(Status.PARTIALLY_SETTLED.getValue());
                        } else if (supplierPricingResource.getAmountPaidToSupplier().compareTo(supplierPricingResource.getNetPayableToSupplier()) == 0 || supplierPricingResource.getAmountPaidToSupplier().compareTo(supplierPricingResource.getNetPayableToSupplier()) == 1) {
                            supplierPricingResource.setPaymentStatus(Status.PAID.getValue());
                            serviceOrderResource.setSupplierSettlementStatus(Status.SETTLED.getValue());
                        }
                        serviceOrderResource.setSupplierPricingResource(supplierPricingResource);
                        serviceOrderResource.setNetPayableToSupplier(supplierPricingResource.getNetPayableToSupplier());
                        provisionalServiceOrderService.generatePSO(serviceOrderResource);

                    }
                }
                logger.info("Cancelling previous version of Provisional Service Order");
                pso.setStatus(Status.PROVISIONAL_SERVICE_ORDER_CANCELLED);
                provisionalServiceOrderService.updatePSO(pso);
                if (supplierLiabilityResources != null && supplierLiabilityResources.size() >= 1) {
                    ServiceOrderResource psl = supplierLiabilityResources.iterator().next();
                    psl.setStatus(Status.PROVISIONAL_SUPPLIER_LIABILITY_CANCELLED);
                    provisionalSupplierLiabilityService.updatePSL(psl);
                }
            }

        }
    }

    @Override
    public DuplicateBookingsInfoResource getDuplicateBookingsInfo(String id) throws OperationException {
        DuplicateBookings duplicateBookings = duplicateBookingsService.getDuplicateBookingsById(id);
        DuplicateBookingsInfoResource resource = new DuplicateBookingsInfoResource();
        resource.setId(duplicateBookings.getId());
        resource.setBookId(duplicateBookings.getBookingRefNo());
        List<DuplicateOrdersResource> duplicateOrders = new ArrayList<>();
        for (String orderId : duplicateBookings.getDuplicateOrderBookingIds()) {
            OpsProduct product = opsBookingService.getProduct(duplicateBookings.getBookingRefNo(), orderId);
            DuplicateOrdersResource ordersResource = new DuplicateOrdersResource();
            ordersResource.setOrderId(orderId);
            ordersResource.setProductName(getProductName(product));
            ordersResource.setSupplierId(product.getSourceSupplierID());
            ordersResource.setSupplierName(product.getSourceSupplierName());
            ordersResource.setSupplierCurrency(getCurrency(product));
            duplicateOrders.add(ordersResource);
        }
        resource.setDuplicateOrders(duplicateOrders);
        return resource;
    }

    private String getProductName(OpsProduct product) {
        String productName = null;
        switch (product.getOpsProductSubCategory()) {
            case PRODUCT_SUB_CATEGORY_FLIGHT:
                productName = product.getOrderDetails().getFlightDetails().getOriginDestinationOptions().iterator().next().getFlightSegment().iterator().next().getOperatingAirline().getAirlineCode();
                break;
            case PRODUCT_SUB_CATEGORY_HOTELS:
                productName = product.getOrderDetails().getHotelDetails().getHotelName();
                break;
            default:
                break;

        }
        return productName;
    }

    private String getCurrency(OpsProduct product) {
        String currency = null;
        switch (product.getOpsProductSubCategory()) {
            case PRODUCT_SUB_CATEGORY_HOTELS:
                currency = product.getOrderDetails().getHotelDetails().getOpsAccoOrderSupplierPriceInfo().getCurrencyCode();
                break;
            case PRODUCT_SUB_CATEGORY_FLIGHT:
                currency = product.getOrderDetails().getFlightDetails().getOpsFlightSupplierPriceInfo().getCurrencyCode();
                break;
            default:
                break;
        }
        return currency;
    }

    @Override
    public String generatePSOForDuplicateBookings(ServiceOrderResource resource) throws OperationException {
        String message = null;
        OpsProduct product = opsBookingService.getProduct(resource.getBookingRefNo(), resource.getOrderId());
        try {
            generateServiceOrder(product, resource.getBookingRefNo(), false);
            message = "PSO is successfully generated for Order " + resource.getOrderId();
        } catch (Exception ex) {
            message = "Failed to generate Service Order for Order " + resource.getOrderId();
            logger.info(message);
        }
        return message;
    }

    public void generateServiceOrderForAmendedProduct(OpsProduct product, OpsBooking opsBooking) throws IOException, OperationException {

        String bookID = opsBooking.getBookID();
        ServiceOrderSearchCriteria searchCriteria = new ServiceOrderSearchCriteria();
        searchCriteria.setBookingRefNo(bookID);
        searchCriteria.setOrderId(product.getOrderID());
        searchCriteria.setProductCategoryId(product.getProductCategory());
        searchCriteria.setProductCategorySubTypeId(product.getProductSubCategory());
        List<ServiceOrderResource> serviceOrderResources = (List<ServiceOrderResource>) provisionalSupplierLiabilityService.getProvisionalSupplierLiabilities(searchCriteria).get("result");
        if (serviceOrderResources != null && serviceOrderResources.size() >= 1) {
            ServiceOrderResource pso = serviceOrderResources.iterator().next();
            if (pso.getFinalServiceOrderID() == null) {
                generateServiceOrder(product, opsBooking, true);
            }
        } else
            generateServiceOrder(product, opsBooking, true);
    }

    private void createToDoTaskForDuplicateOrderBookings(String bookID, List<OpsProduct> opsProducts) throws InvocationTargetException, IOException, IllegalAccessException, ParseException, OperationException {
        //create to-do task for ops user to generate service order
        ToDoTaskResource todo = new ToDoTaskResource();
        todo.setTaskSubTypeId(ToDoTaskSubTypeValues.ORDER_DUPLICATE.name());
        todo.setTaskPriorityId(ToDoTaskPriorityValues.HIGH.getValue());
        todo.setTaskNameId(ToDoTaskNameValues.GENERATE_SERVICE_ORDER.getValue());
        todo.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
        todo.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());
        todo.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.name());
        todo.setReferenceId(saveDuplicateBookings(bookID, opsProducts));
        todo.setCreatedByUserId(userService.getLoggedInUserId());
        toDoTaskService.save(todo);
    }

    public void setCompanyDetails(BaseServiceOrderDetails serviceOrder) throws OperationException{

            //TODO: Temporary fix if BookId is not provided, Ideally there should be a bookId
            if(serviceOrder.getBookingRefNo()==null || serviceOrder.getBookingRefNo().isEmpty()){
                OpsUser loggedInUser = userService.getLoggedInUser();
                if(loggedInUser!=null)
                    serviceOrder.setCompanyId(loggedInUser.getCompanyId());
                else
                    serviceOrder.setCompanyId(userService.createUserDetailsFromToken(mdmToken.getToken()).getCurrentCompany().getId());
                return;
            }

        OpsBooking opsBooking = opsBookingService.getBooking(serviceOrder.getBookingRefNo());
        String companyId = opsBooking.getCompanyId();

        //TODO: uncomment this later
        serviceOrder.setBU(opsBooking.getBu());
        serviceOrder.setSBU(opsBooking.getSbu());
        serviceOrder.setCompanyId(companyId);

        String url = company_url + "?filter=" + new JSONObject().put("id", companyId).toString();
        String companyResponse;
        URI uri = UriComponentsBuilder.fromUriString(url).build().toUri();

        try {
            companyResponse = mdmRestUtils.exchange(uri, HttpMethod.GET, String.class);
        } catch (Exception e) {
            logger.error("Unable to get company details:" + companyId, e);
            return;
            //TODO: throw an exception
        }

        if (StringUtils.isEmpty(companyResponse)) {
            logger.error("No Company found:" + companyId);
            return;
            //TODO: throw an exception
        }

        JSONObject response = new JSONObject(companyResponse);
        if(response.optJSONArray("data")!=null && response.getJSONArray("data").length()!=0) {
            response = response.getJSONArray("data").getJSONObject(0);
            if(response.has("name"))
                serviceOrder.setCompanyName(response.getString("name"));
            if(response.has("groupCompanyId"))
                serviceOrder.setCompanyGroupId(response.getString("groupCompanyId"));
            if(response.has("groupCompanyName"))
                serviceOrder.setCompanyGroupName(response.getString("groupCompanyName"));
            if(response.has("groupOfCompaniesId"))
                serviceOrder.setGroupOfCompanyId(response.getString("groupOfCompaniesId"));
            if(response.has("groupOfCompaniesName"))
                serviceOrder.setGroupOfCompanyName(response.getString("groupOfCompaniesName"));
        }
    }

    @Override
    public JSONArray getAutoSuggestValues(JSONObject req) {

        String bookId = req.getString("bookingRefNumber");
        JSONArray response = provisionalServiceOrderRepository.getAutoSuggestBookId(bookId);

        return response;
    }

    @Override
    public JSONArray getBookIdAutoSuggest(JSONObject req) {
        Integer page = req.optInt("page",1);
        Integer size = req.optInt("size");
        Integer noOfPages=1, actualSize;
        List<String> l1,l2;
        l1 = provisionalServiceOrderRepository.getAutoSuggestBookId(req);
        l2 = finalServiceOrderRepository.getAutoSuggestBookId(req);
        JSONArray response = new JSONArray();
        for(String bookRef : l1){
            if(!l2.contains(bookRef)) {
                l2.add(bookRef);
            }
        }
        actualSize = l2.size();
        if(size!=0)
            if (actualSize % size == 0)
                noOfPages = actualSize / size;
            else
                noOfPages = actualSize / size + 1;

        int startIdx = ((page-1)<0 ? 0 : page-1)*size;
        startIdx = startIdx>actualSize ? actualSize : startIdx;
        int endIdx = size==0 ? actualSize : (startIdx + size);
        endIdx = (endIdx>actualSize) ? actualSize : endIdx;
        l2 = l2.subList(startIdx, endIdx);
        for(String bookRef : l2){
            JSONObject bookObj = new JSONObject();
            bookObj.put("numberOfPages", noOfPages);
            bookObj.put("bookId", bookRef);
            response.put(bookObj);
        }
        return response;
    }
   /* @Override
    public Map<String, Object> getServiceOrdersAndSupplierLiabilitiesWorkFlow(SupplierBillPassingSearchCriteria searchCriteria) throws OperationException {

        String userId = userService.getLoggedInUserId();
        WorkFlowFilter workflowFilter = searchCriteria.getWorkflow_filter();
        ServiceOrderSearchCriteria filter = searchCriteria.getFilter();

        String prefix = "doc.newDoc";
        Integer pageNo = searchCriteria.getPage();
        Integer size = searchCriteria.getCount();
        String sortCriteria = searchCriteria.getSort();

        JSONObject filterObject = new JSONObject();

//        filterObject.put("", filter.get);

        Map<String, Object> workflowList = workflowService.getWorkFlows(entityName, workflowFilter, filterObject, sortCriteria, pageNo, size);
        return workflowList;

    }*/

}

