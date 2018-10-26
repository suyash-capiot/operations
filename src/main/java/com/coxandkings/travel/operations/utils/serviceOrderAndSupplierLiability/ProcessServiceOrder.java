package com.coxandkings.travel.operations.utils.serviceOrderAndSupplierLiability;

import com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability.ServiceOrderSearchCriteria;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.enums.serviceOrderAndSupplierLiability.ServiceOrderAndSupplierLiabilityType;
import com.coxandkings.travel.operations.enums.todo.*;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.DuplicateBookings;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.PassengersDetails;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.ProvisionalServiceOrder;
import com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability.ServiceOrderResource;
import com.coxandkings.travel.operations.resource.serviceOrderAndSupplierLiability.SupplierPricingResource;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResource;
import com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.DuplicateBookingsService;
import com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.FinalServiceOrderService;
import com.coxandkings.travel.operations.service.serviceOrderAndSupplierLiability.ProvisionalServiceOrderService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.service.user.UserService;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ProcessServiceOrder {

    @Autowired
    private ProvisionalServiceOrderService provisionalServiceOrderService;

    @Autowired
    private FinalServiceOrderService finalServiceOrderService;

    @Autowired
    private DuplicateBookingsService duplicateBookingsService;

    @Autowired
    ToDoTaskService toDoTaskService;

    @Autowired
    private UserService userService;

    private static ObjectMapper objectMapper = new ObjectMapper();
    private static Logger logger = LogManager.getLogger(ProcessServiceOrder.class);
    @Value(value = "${service_order.get_pax_info.path_expression}")
    private String paxInfoPath;
    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    public void processBooking(OpsBooking aBooking, KafkaBookingMessage message) throws IllegalAccessException, ParseException, IOException, OperationException, InvocationTargetException, JSONException {

        if (aBooking.isHolidayBooking()) {
            if (message.getOrderNo() == null) {
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

                                        //create to-do task for ops user to generate service order
                                        ToDoTaskResource todo = new ToDoTaskResource();
                                        todo.setTaskSubTypeId(ToDoTaskSubTypeValues.ORDER_DUPLICATE.name());
                                        todo.setTaskPriorityId(ToDoTaskPriorityValues.HIGH.getValue());
                                        todo.setTaskNameId(ToDoTaskNameValues.GENERATE_SERVICE_ORDER.getValue());
                                        todo.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
                                        todo.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());
                                        todo.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.name());
                                        todo.setReferenceId(saveDuplicateBookings(aBooking.getBookID(), opsProducts));
                                        todo.setCreatedByUserId(userService.getLoggedInUserId());
                                        toDoTaskService.save(todo);

                                        opsProductsList.addAll(opsProducts);
                                    }
                                    break;

                                case PRODUCT_SUB_CATEGORY_HOTELS:
                                    List<OpsProduct> opsProducts1 = productSet.stream().filter(product1 -> product.getOrderDetails().getHotelDetails().getRooms().equals(product1.getOrderDetails().getHotelDetails().getRooms())).collect(Collectors.toList());
                                    if (opsProducts1.size() >= 1) {
                                        opsProducts1.add(product);

                                        //create to-do task for ops user to generate service order
                                        ToDoTaskResource todo = new ToDoTaskResource();
                                        todo.setTaskSubTypeId(ToDoTaskSubTypeValues.ORDER_DUPLICATE.name());
                                        todo.setTaskPriorityId(ToDoTaskPriorityValues.HIGH.getValue());
                                        todo.setTaskNameId(ToDoTaskNameValues.GENERATE_SERVICE_ORDER.getValue());
                                        todo.setTaskTypeId(ToDoTaskTypeValues.MAIN.getValue());
                                        todo.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());
                                        todo.setTaskGeneratedTypeId(ToDoTaskGeneratedTypeValues.AUTO.name());
                                        todo.setReferenceId(saveDuplicateBookings(aBooking.getBookID(), opsProducts1));
                                        todo.setCreatedByUserId(userService.getLoggedInUserId());
                                        toDoTaskService.save(todo);

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
                        if (!product.getOrderDetails().getOpsBookingAttribute().stream().anyMatch(opsBookingAttribute -> opsBookingAttribute.equals(OpsBookingAttribute.BOOKING_TYPE_TIME_LIMIT))) {
                            ServiceOrderResource resource = new ServiceOrderResource();
                            resource.setBookingRefNo(aBooking.getBookID());
                            resource.setOrderId(product.getOrderID());
                            resource.setProductCategoryId(product.getProductCategory());
                            resource.setProductCategorySubTypeId(product.getProductSubCategory());
                            resource.setCompanyMarketId("CNK01");
                            resource.setSupplierId(product.getSupplierID());
                            resource.setSupplierName(product.getSourceSupplierName());

                            resource.setDateOfGeneration(ZonedDateTime.now());
                            //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                            SupplierPricingResource supplierPricing = new SupplierPricingResource();
                            supplierPricing.setPassengerDetails(getPassengerDetails(product));
                            supplierPricing.setAmendmentCharges(new BigDecimal(0));
                            supplierPricing.setCancellationCharges(new BigDecimal(0));
                            supplierPricing.setTotalBalanceAmountPayable(new BigDecimal("123"));
                            supplierPricing.setAmountPaidToSupplier(new BigDecimal("123"));
                            supplierPricing.setSupplierCommercials(new BigDecimal("123"));
                            //supplierPricing.setSupplierSettlementDueDate(ZonedDateTime.parse("2018-03-18T12:12:50.252+05:30",dtf));
                            supplierPricing.setSurcharges(new BigDecimal(0));

                            if (product.getProductSubCategory().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_FLIGHT.getSubCategory())) {
                                resource.setProductNameId(product.getOrderDetails().getFlightDetails().getOriginDestinationOptions().iterator().next().getFlightSegment().iterator().next().getOperatingAirline().getAirlineCode());
                                resource.setSupplierCurrency(product.getOrderDetails().getFlightDetails().getOpsFlightSupplierPriceInfo().getCurrencyCode());
                                resource.setTravelCompletionDate(product.getOrderDetails().getFlightDetails().getOriginDestinationOptions().get(0).getFlightSegment().get(0).getArrivalDateZDT());
                                supplierPricing.setSupplierCost(new BigDecimal(product.getOrderDetails().getFlightDetails().getOpsFlightSupplierPriceInfo().getSupplierPrice()));
                                BigDecimal supplierGst = new BigDecimal(0);
                                for (OpsPaxTypeFareFlightSupplier paxTypeFare : product.getOrderDetails().getFlightDetails().getOpsFlightSupplierPriceInfo().getPaxTypeFares()) {
                                    supplierGst.add(new BigDecimal(paxTypeFare.getTaxes().getAmount() * supplierPricing.getPassengerDetails().stream().filter(passengersDetails -> passengersDetails.getPassengerType().equals(paxTypeFare.getPaxType())).findFirst().get().getNoOfPassengers()));
                                }
                                supplierPricing.setSupplierGst(supplierGst);
                                supplierPricing.setSupplierTotalCost(supplierPricing.getSupplierCost().add(supplierGst));
                                supplierPricing.setNetPayableToSupplier(supplierPricing.getSupplierTotalCost());
                                List<OpsAncillaryInfo> ancillaryInfo = jsonObjectProvider.getChildrenCollection(objectMapper.writeValueAsString(product.getOrderDetails().getFlightDetails()), paxInfoPath, OpsAncillaryInfo.class);
                                supplierPricing.setAncillaries(new BigDecimal(ancillaryInfo.stream().mapToInt(value -> Integer.parseInt(value.getAmount())).sum()));
                                //supplierPricing.setAncillaries(new BigDecimal(product.getOrderDetails().getFlightDetails().getPaxInfo().stream().mapToInt(value -> value.getAncillaryServices().getAncillaryInfo().stream().mapToInt(value1 -> Integer.parseInt(value1.getAmount())).sum()).sum()));
                                resource.setSupplierPricingResource(supplierPricing);
                                resource.setNetPayableToSupplier(supplierPricing.getNetPayableToSupplier());
                                if (product.getStatus().equals(OpsOrderStatus.TKD)) {
                                    ServiceOrderSearchCriteria criteria = new ServiceOrderSearchCriteria();
                                    List<String> orderIds = new ArrayList<>();
                                    orderIds.add(resource.getOrderId());
                                    criteria.setBookingRefNo(aBooking.getBookID());
                                    criteria.setOrderIds(orderIds);
                                    criteria.setProductCategoryId(product.getProductCategory());
                                    criteria.setProductCategorySubTypeId(product.getProductSubCategory());
                                    criteria.setProductNameId(product.getProductName());
                                    criteria.setCompanyMarketId("CNK01");
                                    criteria.setSupplierName(resource.getSupplierId());
                                    criteria.setSupplierCurrency(resource.getSupplierCurrency());

                                    Map<String, Object> result = provisionalServiceOrderService.getProvisionalServiceOrders(criteria);
                                    List<ProvisionalServiceOrder> pso = (List<ProvisionalServiceOrder>) result.get("result");
                                    if (pso.size() == 0) {
                                        resource.setType(ServiceOrderAndSupplierLiabilityType.FSO);
                                        finalServiceOrderService.generateFSO(resource);
                                    } else {
                                        resource.setType(ServiceOrderAndSupplierLiabilityType.PSO);
                                        provisionalServiceOrderService.generatePSO(resource);
                                        resource.setType(ServiceOrderAndSupplierLiabilityType.FSO);
                                        finalServiceOrderService.generateFSO(resource);
                                    }
                                } else {
                                    resource.setType(ServiceOrderAndSupplierLiabilityType.PSO);
                                    provisionalServiceOrderService.generatePSO(resource);
                                }
                            } else if (product.getProductSubCategory().equals(OpsProductSubCategory.PRODUCT_SUB_CATEGORY_HOTELS.getSubCategory())) {
                                resource.setProductNameId(product.getOrderDetails().getHotelDetails().getHotelName());
                                resource.setSupplierCurrency(product.getOrderDetails().getHotelDetails().getOpsAccoOrderSupplierPriceInfo().getCurrencyCode());
                                //resource.setTravelCompletionDate(ZonedDateTime.parse(product.getOrderDetails().getHotelDetails().getRooms().iterator().next().getCheckOut(),dtf));
                                supplierPricing.setSupplierCost(new BigDecimal(product.getOrderDetails().getHotelDetails().getOpsAccoOrderSupplierPriceInfo().getSupplierPrice()));
                                supplierPricing.setSupplierGst(new BigDecimal(product.getOrderDetails().getHotelDetails().getOpsAccoOrderSupplierPriceInfo().getTaxes().getAmount()));
                                supplierPricing.setSupplierTotalCost(supplierPricing.getSupplierCost().add(supplierPricing.getSupplierGst()));
                                supplierPricing.setNetPayableToSupplier(supplierPricing.getSupplierTotalCost());
                                resource.setSupplierPricingResource(supplierPricing);
                                resource.setNetPayableToSupplier(supplierPricing.getNetPayableToSupplier());
                                resource.setType(ServiceOrderAndSupplierLiabilityType.PSO);
                                provisionalServiceOrderService.generatePSO(resource);
                            }
                        }
                    } catch (Exception ex) {
                        logger.debug("Service Order update failed");
                    }
                }

            } else {
                //logic for holidays

            }
        }
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
                break;

            case PRODUCT_SUB_CATEGORY_HOTELS:
                for (OpsRoom room : product.getOrderDetails().getHotelDetails().getRooms()) {
                    for (OpsAccommodationPaxInfo paxInfo : room.getPaxInfo()) {
                        if (paxInfo.getPaxType().equals("ADT")) {
                            if (adultPassenger == null) {
                                adultPassenger = new PassengersDetails();
                                adultPassenger.setPassengerType(paxInfo.getPaxType());
                                adultPassenger.setNoOfPassengers(0);
                                adultPassenger.setRatePerPassenger(new BigDecimal(0));
                            }
                            adultPassenger.setNoOfPassengers(adultPassenger.getNoOfPassengers() + 1);
                        } else if (paxInfo.getPaxType().equals("CHD")) {
                            if (childPassenger == null) {
                                childPassenger = new PassengersDetails();
                                childPassenger.setPassengerType(paxInfo.getPaxType());
                                childPassenger.setNoOfPassengers(0);
                                childPassenger.setRatePerPassenger(new BigDecimal(0));
                            }
                            childPassenger.setNoOfPassengers(childPassenger.getNoOfPassengers() + 1);
                        } else if (paxInfo.getPaxType().equals("INF")) {
                            if (infantPassenger == null) {
                                infantPassenger = new PassengersDetails();
                                infantPassenger.setPassengerType(paxInfo.getPaxType());
                                infantPassenger.setNoOfPassengers(0);
                                infantPassenger.setRatePerPassenger(new BigDecimal(0));
                            }
                            infantPassenger.setNoOfPassengers(infantPassenger.getNoOfPassengers() + 1);
                        }
                    }
                }
                break;

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

    public String saveDuplicateBookings(String bookID, List<OpsProduct> products) {
        DuplicateBookings duplicateBookings = new DuplicateBookings();
        List<String> ordersList = products.stream().map(product -> product.getOrderID()).collect(Collectors.toList());
        duplicateBookings.setBookingRefNo(bookID);
        duplicateBookings.setDuplicateOrderBookingIds(ordersList);
        return duplicateBookingsService.saveDuplicateBookings(duplicateBookings).getId();
    }

}
