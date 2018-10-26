package com.coxandkings.travel.operations.service.commercialstatements.impl;

import com.coxandkings.travel.operations.criteria.booking.becriteria.BookingDetailsFilter;
import com.coxandkings.travel.operations.criteria.booking.becriteria.BookingSearchCriteria;
import com.coxandkings.travel.operations.criteria.booking.becriteria.ClientAndPassengerBasedFilter;
import com.coxandkings.travel.operations.criteria.booking.becriteria.ProductDetailsFilter;
import com.coxandkings.travel.operations.enums.commercialStatements.CommercialStatementFor;
import com.coxandkings.travel.operations.enums.commercialStatements.CommercialType;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.commercialstatements.ClientCommercialStatement;
import com.coxandkings.travel.operations.model.commercialstatements.CommercialStatementDetails;
import com.coxandkings.travel.operations.model.commercialstatements.OrderCommercialDetails;
import com.coxandkings.travel.operations.model.commercialstatements.PassengerDetails;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.repository.commercialstatements.ClientCommercialStatementRepo;
import com.coxandkings.travel.operations.resource.commercialStatement.CommercialSettlemetMdmId;
import com.coxandkings.travel.operations.resource.commercialStatement.CommercialStatementGeneration;
import com.coxandkings.travel.operations.resource.notification.InlineMessageResource;
import com.coxandkings.travel.operations.resource.scheduler.*;
import com.coxandkings.travel.operations.resource.searchviewfilter.BookingSearchResponseItem;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.commercialstatements.ClientCommercialStatementSchedulerService;
import com.coxandkings.travel.operations.systemlogin.MDMToken;
import com.coxandkings.travel.operations.utils.CopyUtils;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.coxandkings.travel.operations.utils.RestUtils;
import com.coxandkings.travel.operations.utils.scheduler.FortnightlySettlementSchedulerUtil;
import com.coxandkings.travel.operations.utils.scheduler.HalfYearlySettlementSchedulerUtil;
import com.coxandkings.travel.operations.utils.scheduler.WeeklySettlementDateswiseSchedulerUtil;
import com.coxandkings.travel.operations.utils.scheduler.WeeklySettlementDaywiseSchedulerUtil;
import com.coxandkings.travel.operations.utils.supplierBillPassing.DateConverter;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ClientCommercialStatementSchedulerServiceImpl implements ClientCommercialStatementSchedulerService {

    @Autowired
    private ClientCommercialStatementRepo clientCommercialStatementRepo;

    @Autowired
    private AlertService alertService;

    @Autowired
    private OpsBookingService opsBookingService;

    @Value(value = "${finance-commercial-statements-settelement.client_step_one}")
    private String financeClientCommercialStatementStepOne;

    @Value(value = "${commercial-statements.client_id}")
    private String getClientSettlement_id;

    @Value(value = "${commercial-statements.getClientSettlementTerm}")
    private String getClientSettlementDetails;

    @Value(value = "${finance-commercial-statements-settelement.get_invoice}")
    private String getInvoiceDetails;

    @Value(value = "${commercial_statements.client_alert}")
    private String clientAlert;

    @Autowired
    private MDMToken mdmToken;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Autowired
    private JsonObjectProvider jsonObjectProvider;

    private Logger logger=Logger.getLogger(ClientCommercialStatementSchedulerServiceImpl.class);


    @Override
    public void scheduler() {
        try {
            URI uri = UriComponentsBuilder.fromUriString(getClientSettlement_id).build().encode().toUri();
            ResponseEntity<String> responseEntity = mdmRestUtils.exchange(uri, HttpMethod.GET, null, String.class);
            List<CommercialSettlemetMdmId> commercialSettlemetMdmIds = jsonObjectProvider.getChildrenCollection(responseEntity.getBody(), "$.data", CommercialSettlemetMdmId.class);

            for (CommercialSettlemetMdmId commercialSettlemetMdmId : commercialSettlemetMdmIds) {
                String url=getClientSettlementDetails + commercialSettlemetMdmId.get_id();
                URI getClientSettlementUrl = UriComponentsBuilder.fromUriString(url).build().encode().toUri();
                ResponseEntity<String> response = mdmRestUtils.exchange(getClientSettlementUrl, HttpMethod.GET, null, String.class);

                JSONObject clientSettlementDetails=new JSONObject(response.getBody());
                String companyMarket=clientSettlementDetails.getJSONObject("settlementAttachedTo").getString("companyMarket");
                String entityName=clientSettlementDetails.getJSONObject("settlementAttachedTo").getString("entityName");
                String clientId = clientSettlementDetails.getString( "entityId");
                JSONArray settlementTerms = clientSettlementDetails.getJSONArray("settlementTerms");

                for (Object settlement:settlementTerms) {
                    JSONObject settlementTerm = (JSONObject) settlement;
                    CommercialStatementGeneration commercialStatementGeneration = new CommercialStatementGeneration();
                    commercialStatementGeneration.setClientId(clientId);
                    commercialStatementGeneration.setCompanyMarket(companyMarket);
                    commercialStatementGeneration.setCommercialHead(settlementTerm.getString("commercialHead"));
                    commercialStatementGeneration.setCommercialtype(settlementTerm.getString("commercialType"));
                    commercialStatementGeneration.setSupplierOrClientName(entityName);

                    JSONArray productInfoList = (JSONArray) settlementTerm.get("productInfo");
                    for (Object product:productInfoList) {
                        JSONObject productInfo = (JSONObject) product;
                        commercialStatementGeneration.setProductCategory(productInfo.getString("productCategory"));
                        commercialStatementGeneration.setProductSubCategory(productInfo.getString("productCategorySubType"));
                        if (productInfo.has("productName"))
                            commercialStatementGeneration.setProductName(productInfo.getString("productName"));
                        //TODO get currency from MDM or based on company market
                        commercialStatementGeneration.setCurrency("IND");
                        if (commercialStatementGeneration.getCommercialtype().equalsIgnoreCase(CommercialType.PAYABLE.getValue())) {
                            JSONObject payableSettlementDetails = settlementTerm.getJSONObject("payable");
                            schedulerStatementCreation(payableSettlementDetails,commercialStatementGeneration);
                        } else {
                            JSONObject receivableSettlementDetails =  settlementTerm.getJSONObject("receivable");
                            schedulerStatementCreation(receivableSettlementDetails,commercialStatementGeneration);
                        }
                    }
                }
            }
        }catch (Exception e) {
            logger.error("Error occured in client commercial statements scheduler");
        }
    }


    private void schedulerStatementCreation(JSONObject settlementTerm, CommercialStatementGeneration commercialStatementGeneration) {
        try {
            JSONObject periodicityDetails = settlementTerm.getJSONObject("periodicity");
            String settlementSchedule = settlementTerm.getString("settlementSchedule");
            commercialStatementGeneration.setSettlementSchedule(settlementSchedule);
            String repeatFreq = periodicityDetails.getString("repeatFreq");

            switch (repeatFreq.toLowerCase()) {
                case "monthly":
                    Boolean shouldRuntoday=false;
                    int settlementDueDay = periodicityDetails.getJSONObject("monthly").getInt("settlementDueDay");
                    ZonedDateTime todayDate = ZonedDateTime.now();
                    ZonedDateTime lastOfMonth = todayDate.with(TemporalAdjusters.lastDayOfMonth());
                    if (todayDate.compareTo(lastOfMonth)==0) shouldRuntoday=true;
                    if (shouldRuntoday) {
                        commercialStatementGeneration.setStatementPeriodFromDate(ZonedDateTime.now().minusMonths(1));
                        commercialStatementGeneration.setStatementPeriodToDate(ZonedDateTime.now());
                        commercialStatementGeneration.setSettlementDueDate(ZonedDateTime.now().plusMonths(1).withDayOfMonth(settlementDueDay));
                        create(commercialStatementGeneration);
                    }
                    break;

                case "weekly":
                    JSONObject weekly = periodicityDetails.getJSONObject("weekly");
                    String weeklySchedule = weekly.getString("weeklySchedule");
                    switch (weeklySchedule.toLowerCase()) {
                        case "day":
                            DayOfWeek fromDay = DayOfWeek.valueOf(weekly.getJSONObject("day").getString("fromDay").toUpperCase());
                            DayOfWeek toDay = DayOfWeek.valueOf(weekly.getJSONObject("day").getString("toDay").toUpperCase());
                            DayOfWeek weeklySettlementDueDay = DayOfWeek.valueOf(weekly.getJSONObject("day").getString("settlementDueDay").toUpperCase());
                            SettlementScheduleResponse settlementScheduleResponse = WeeklySettlementDaywiseSchedulerUtil.computeWeeklyDaywiseSettlementSchedule(fromDay, toDay, weeklySettlementDueDay);
                            if (!settlementScheduleResponse.isShouldRunToday()) break;
                            commercialStatementGeneration.setSettlementDueDate(settlementScheduleResponse.getSettlementDueDay().atStartOfDay(ZoneId.systemDefault()));
                            commercialStatementGeneration.setStatementPeriodFromDate(settlementScheduleResponse.getStartDate().atStartOfDay(ZoneId.systemDefault()));
                            commercialStatementGeneration.setStatementPeriodToDate(settlementScheduleResponse.getEndDate().atStartOfDay(ZoneId.systemDefault()));
                            create(commercialStatementGeneration);
                            break;

                        case "dates":
                            JSONArray dates = weekly.getJSONArray("dates");
                            List<WeeklyDateswisePeriodicityInput> weeklyDateswisePeriodicityInputs = new ArrayList<>();
                            for (Object object : dates) {
                                JSONObject datesObject = (JSONObject) object;
                                LocalDate fromDate = LocalDate.of(datesObject.getInt("fromDate"), Month.valueOf(datesObject.getString("fromMonth").toUpperCase()), datesObject.getInt("toDate"));
                                LocalDate toDate = LocalDate.of(datesObject.getInt("fromDate"), Month.valueOf(datesObject.getString("fromMonth").toUpperCase()), datesObject.getInt("toDate"));
                                LocalDate weeklySettlementDueDate = LocalDate.of(datesObject.getInt("settlementDueDate"), Month.valueOf(datesObject.getString("settlementDueMonth").toUpperCase()), datesObject.getInt("settlementDueYear"));

                                WeeklyDateswisePeriodicityInput weeklyDateswisePeriodicityInput = new WeeklyDateswisePeriodicityInput();
                                weeklyDateswisePeriodicityInput.setFromDate(fromDate);
                                weeklyDateswisePeriodicityInput.setToDate(toDate);
                                weeklyDateswisePeriodicityInput.setSettlementDate(weeklySettlementDueDate);
                                weeklyDateswisePeriodicityInputs.add(weeklyDateswisePeriodicityInput);
                            }
                            SettlementScheduleResponse weeklySettlementSchedulerResponse = WeeklySettlementDateswiseSchedulerUtil.canExecuteDateswiseSettlement(weeklyDateswisePeriodicityInputs);
                            if (!weeklySettlementSchedulerResponse.isShouldRunToday()) break;
                            commercialStatementGeneration.setStatementPeriodToDate(weeklySettlementSchedulerResponse.getEndDate().atStartOfDay(ZoneId.systemDefault()));
                            commercialStatementGeneration.setStatementPeriodFromDate(weeklySettlementSchedulerResponse.getStartDate().atStartOfDay(ZoneId.systemDefault()));
                            commercialStatementGeneration.setSettlementDueDate(weeklySettlementSchedulerResponse.getSettlementDueDay().atStartOfDay(ZoneId.systemDefault()));
                            create(commercialStatementGeneration);
                            break;
                    }
                    break;

                case "end of validity":
                    Integer numberOfDays=null;
                    if (periodicityDetails.has("endOfValidity"))
                        numberOfDays = periodicityDetails.getJSONObject("endOfValidity").getInt("numberOfDays");
                    else numberOfDays=10;
                    //TODO need to know what to do exactly
                    commercialStatementGeneration.setSettlementDueDate(ZonedDateTime.now().plusDays(numberOfDays));
                    commercialStatementGeneration.setStatementPeriodFromDate(ZonedDateTime.now().minusMonths(numberOfDays));
                    commercialStatementGeneration.setStatementPeriodToDate(ZonedDateTime.now());
                    create(commercialStatementGeneration);
                    break;

                case "fortnightly":
                    JSONArray fortNightly = periodicityDetails.getJSONArray("fortnightly");
                    FortnightlyPeriodicityInput fortnightlyPeriodicityInput = new FortnightlyPeriodicityInput();
                    for (Object object : fortNightly) {
                        JSONObject fortNightJson = (JSONObject) object;
                        Integer settlementDueDate = fortNightJson.getInt("settleDueDay");
                        String fortNight = fortNightJson.getString("fortnight");
                        if (fortNight.toLowerCase().contains("first")) {
                            fortnightlyPeriodicityInput.setFirstFortnightSettlementDueDay(settlementDueDate);
                            if (fortNightJson.getString("settlementDueMonth").toLowerCase().equalsIgnoreCase("current"))
                                fortnightlyPeriodicityInput.setSettleFirstFornightByCurrentMonth(true);
                            else fortnightlyPeriodicityInput.setSettleFirstFornightByCurrentMonth(false);
                        } else {
                            fortnightlyPeriodicityInput.setSecondFortnightSettlementDueDay(settlementDueDate);
                            if (fortNightJson.getString("settlementDueMonth").toLowerCase().equalsIgnoreCase("current"))
                                fortnightlyPeriodicityInput.setSettleSecondFornightByCurrentMonth(true);
                            else fortnightlyPeriodicityInput.setSettleSecondFornightByCurrentMonth(false);
                        }
                    }
                    FortnightSettlementScheduleResponse settlementScheduleResponse = FortnightlySettlementSchedulerUtil.computeFortnightSettlementSchedule(fortnightlyPeriodicityInput);
                    if (settlementScheduleResponse.getFirstFortnightScheduleResponse() != null) {
                        if (settlementScheduleResponse.getFirstFortnightScheduleResponse().isShouldRunToday()) {
                            SettlementScheduleResponse firstFortNightSchedulerResponse = settlementScheduleResponse.getFirstFortnightScheduleResponse();
                            CommercialStatementGeneration firstFortnightCommercialStatementGeneration = new CommercialStatementGeneration();
                            CopyUtils.copy(commercialStatementGeneration, firstFortnightCommercialStatementGeneration);
                            firstFortnightCommercialStatementGeneration.setSettlementDueDate(firstFortNightSchedulerResponse.getSettlementDueDay().atStartOfDay(ZoneId.systemDefault()));
                            firstFortnightCommercialStatementGeneration.setStatementPeriodFromDate(firstFortNightSchedulerResponse.getStartDate().atStartOfDay(ZoneId.systemDefault()));
                            firstFortnightCommercialStatementGeneration.setStatementPeriodToDate(firstFortNightSchedulerResponse.getEndDate().atStartOfDay(ZoneId.systemDefault()));
                            create(firstFortnightCommercialStatementGeneration);
                        }
                    }
                    if (settlementScheduleResponse.getSecondFortnightScheduleResponse() != null) {
                        if (settlementScheduleResponse.getSecondFortnightScheduleResponse().isShouldRunToday()) {
                            CommercialStatementGeneration secondFortnightCommercialStatementGeneration = new CommercialStatementGeneration();
                            CopyUtils.copy(commercialStatementGeneration, secondFortnightCommercialStatementGeneration);
                            SettlementScheduleResponse secondFortNightSchedulerResponse = settlementScheduleResponse.getSecondFortnightScheduleResponse();
                            secondFortnightCommercialStatementGeneration.setSettlementDueDate(secondFortNightSchedulerResponse.getSettlementDueDay().atStartOfDay(ZoneId.systemDefault()));
                            secondFortnightCommercialStatementGeneration.setStatementPeriodFromDate(secondFortNightSchedulerResponse.getStartDate().atStartOfDay(ZoneId.systemDefault()));
                            secondFortnightCommercialStatementGeneration.setStatementPeriodToDate(secondFortNightSchedulerResponse.getEndDate().atStartOfDay(ZoneId.systemDefault()));
                            create(secondFortnightCommercialStatementGeneration);
                        }
                    }
                    break;

                case "half yearly":
                    JSONArray halfYearly = periodicityDetails.getJSONArray("halfYearly");
                    HalfYearlyPeriodicityInput halfYearlyPeriodicityInput = new HalfYearlyPeriodicityInput();
                    for (Object object : halfYearly) {
                        JSONObject halfYearlyJson = (JSONObject) object;
                        if (halfYearlyJson.getString("halfYearly").contains("1")) {
                            HalfYearlyPeriodicity firstHalfYearlyPeriodicity = new HalfYearlyPeriodicity();
                            firstHalfYearlyPeriodicity.setStartDate(getStartDate(halfYearlyJson));
                            firstHalfYearlyPeriodicity.setEndDate(getEndDate(halfYearlyJson));
                            firstHalfYearlyPeriodicity.setSettlementDueDate(getSettlementDueDate(halfYearlyJson));
                            halfYearlyPeriodicityInput.setFirstHalfYearlyPeriodicity(firstHalfYearlyPeriodicity);
                            if (HalfYearlySettlementSchedulerUtil.computeForHalfYearlySettelemetSchelule(getEndDate(halfYearlyJson))) {
                                commercialStatementGeneration.setStatementPeriodFromDate(firstHalfYearlyPeriodicity.getStartDate().atStartOfDay(ZoneId.systemDefault()));
                                commercialStatementGeneration.setStatementPeriodToDate(firstHalfYearlyPeriodicity.getEndDate().atStartOfDay(ZoneId.systemDefault()));
                                commercialStatementGeneration.setSettlementDueDate(firstHalfYearlyPeriodicity.getSettlementDueDate().atStartOfDay(ZoneId.systemDefault()));
                                create(commercialStatementGeneration);
                            }

                        } else if (halfYearlyJson.getString("halfYearly").contains("2")) {
                            HalfYearlyPeriodicity secondHalfYearlyPeriodicity = new HalfYearlyPeriodicity();
                            secondHalfYearlyPeriodicity.setStartDate(getStartDate(halfYearlyJson));
                            secondHalfYearlyPeriodicity.setEndDate(getEndDate(halfYearlyJson));
                            secondHalfYearlyPeriodicity.setSettlementDueDate(getSettlementDueDate(halfYearlyJson));
                            halfYearlyPeriodicityInput.setSecondHalfYearlyPeriodicity(secondHalfYearlyPeriodicity);
                            if (HalfYearlySettlementSchedulerUtil.computeForHalfYearlySettelemetSchelule(getEndDate(halfYearlyJson))) {
                                commercialStatementGeneration.setStatementPeriodFromDate(secondHalfYearlyPeriodicity.getStartDate().atStartOfDay(ZoneId.systemDefault()));
                                commercialStatementGeneration.setStatementPeriodToDate(secondHalfYearlyPeriodicity.getEndDate().atStartOfDay(ZoneId.systemDefault()));
                                commercialStatementGeneration.setSettlementDueDate(secondHalfYearlyPeriodicity.getSettlementDueDate().atStartOfDay(ZoneId.systemDefault()));
                                create(commercialStatementGeneration);
                            }
                        }
                    }

                default:
                    break;
            }

        } catch (Exception e) {
            logger.debug("Exception occured in commercial statements scheduler");
        }
    }


    public Map create(CommercialStatementGeneration commercialStatementGeneration) throws OperationException {

        BookingSearchCriteria bookingSearchCriteria = new BookingSearchCriteria();
        bookingSearchCriteria.setSize(1000000);
        bookingSearchCriteria.setPage(1);
        BookingDetailsFilter bookingDetailsFilter = new BookingDetailsFilter();

        ProductDetailsFilter productDetailsFilter = new ProductDetailsFilter();
        if (commercialStatementGeneration.getSettlementSchedule().equalsIgnoreCase("Booking Date")) {
            bookingDetailsFilter.setBookingFromDate(DateConverter.zonedDateTimeToString(commercialStatementGeneration.getStatementPeriodFromDate()));
            bookingDetailsFilter.setBookingToDate(DateConverter.zonedDateTimeToString(commercialStatementGeneration.getStatementPeriodToDate()));
        }else if (commercialStatementGeneration.getSettlementSchedule().equalsIgnoreCase("Travel Date") || commercialStatementGeneration.getSettlementSchedule().equalsIgnoreCase("Before Travel Date") || commercialStatementGeneration.getSettlementSchedule().equalsIgnoreCase("On Travel Date")){
            productDetailsFilter.setTravelFromDate(DateConverter.zonedDateTimeToString(commercialStatementGeneration.getStatementPeriodFromDate()));
            productDetailsFilter.setTravelToDate(DateConverter.zonedDateTimeToString(commercialStatementGeneration.getStatementPeriodToDate()));
        }

        productDetailsFilter.setProductCategoryId(commercialStatementGeneration.getProductCategory());
        productDetailsFilter.setProductCategorySubTypeId(commercialStatementGeneration.getProductSubCategory());

        ClientAndPassengerBasedFilter clientAndPassengerBasedFilter = new ClientAndPassengerBasedFilter();
        clientAndPassengerBasedFilter.setClientId(commercialStatementGeneration.getClientId());

        bookingSearchCriteria.setBookingBasedFilter(bookingDetailsFilter);
        bookingSearchCriteria.setClientPxBasedFilter(clientAndPassengerBasedFilter);
        bookingSearchCriteria.setProductBasedFilter(productDetailsFilter);

        List<BookingSearchResponseItem> bookingSearchResponseItems = opsBookingService.searchBookings(bookingSearchCriteria);

        ClientCommercialStatement clientCommercialStatement = new ClientCommercialStatement();
        CommercialStatementDetails commercialStatementDetails = null;
        Set<CommercialStatementDetails> commercialStatementDetailsSet = new HashSet<>();
        String companyId=null;

        if (bookingSearchResponseItems!=null && bookingSearchResponseItems.size() != 0) {
            for (BookingSearchResponseItem bookingSearchResponseItem : bookingSearchResponseItems) {
                try {
                    if (companyId==null && !StringUtils.isEmpty(bookingSearchResponseItem.getCompany()))
                        companyId=bookingSearchResponseItem.getCompany();

                    OpsBooking opsBooking = opsBookingService.getBooking(bookingSearchResponseItem.getBookID());
                    if (opsBooking == null) continue;
                    if (!opsBooking.getClientID().equalsIgnoreCase(commercialStatementGeneration.getClientId()))
                        continue;

                    commercialStatementDetails = new CommercialStatementDetails();
                    commercialStatementDetails.setBookingRefNum(opsBooking.getBookID());
                    commercialStatementDetails.setBookingDate(opsBooking.getBookingDateZDT());

                    if (StringUtils.isEmpty(clientCommercialStatement.getClientType()) && !StringUtils.isEmpty(opsBooking.getClientType()))
                        clientCommercialStatement.setClientType(opsBooking.getClientType());

                    Set<OrderCommercialDetails> orderCommercialDetailsSet = new HashSet<>();

                    for (OpsProduct opsProduct : opsBooking.getProducts()) {
                        OrderCommercialDetails orderCommercialDetails = null;
                        if (opsProduct.getProductCategory().equalsIgnoreCase(commercialStatementGeneration.getProductCategory()) && opsProduct.getProductSubCategory().equalsIgnoreCase(commercialStatementGeneration.getProductSubCategory())){
                            orderCommercialDetails = generateCommercialStatementDetails(opsProduct, commercialStatementGeneration);
                            if (orderCommercialDetails != null && orderCommercialDetails.getPassengerDetailsSet() != null) {
                                orderCommercialDetails.setNumberOfPassengers(orderCommercialDetails.getPassengerDetailsSet().size());
                                for (PassengerDetails passengerDetails : orderCommercialDetails.getPassengerDetailsSet())
                                    passengerDetails.setOrderCommercialDetails(orderCommercialDetails);
                                orderCommercialDetailsSet.add(orderCommercialDetails);
                            }
                        }
                    }
                    if (orderCommercialDetailsSet.size() != 0) {
                        commercialStatementDetails.setOrderCommercialDetails(orderCommercialDetailsSet);
                        for (OrderCommercialDetails orderCommercialDetails:orderCommercialDetailsSet)
                            orderCommercialDetails.setCommercialStatementDetails(commercialStatementDetails);
                        commercialStatementDetailsSet.add(commercialStatementDetails);
                    }

                } catch (Exception e) {
                    logger.error("error in getting commercial values form BE");
                }
            }

            if (!commercialStatementDetailsSet.isEmpty()) {
                clientCommercialStatement.setCompanyId(companyId);
                clientCommercialStatement=setCommercialStatementDetails(clientCommercialStatement, commercialStatementGeneration, commercialStatementDetailsSet);
                try {
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.set("Authorization", mdmToken.getToken());
                    Set<String> statementIds=new HashSet<>();
                    statementIds.add(clientCommercialStatement.getStatementId());
                    Map<String,Object> entity=new HashMap<>();
                    entity.put("commercialStatementIds",statementIds);
                    HttpEntity httpEntity = new HttpEntity(entity,httpHeaders);
                    RestUtils.getTemplate().exchange(financeClientCommercialStatementStepOne, HttpMethod.POST, httpEntity, String.class);
                } catch (Exception e) {
                    logger.debug("Error in calling finance client commercial statements api after generation");
                }
            }
        }
        Map<String, String> entity = new HashMap<>();
        entity.put("message", "successfully generated");
        return entity;
    }


    private ClientCommercialStatement setCommercialStatementDetails(ClientCommercialStatement commercialStatement, CommercialStatementGeneration commercialStatementGeneration, Set<CommercialStatementDetails> commercialStatementDetailsSet) throws OperationException {
        commercialStatement.setCommercialHead(commercialStatementGeneration.getCommercialHead());
        commercialStatement.setCommercialType(commercialStatementGeneration.getCommercialtype());
        commercialStatement.setCurrency(commercialStatementGeneration.getCurrency());

        if (commercialStatementGeneration.getSettlementSchedule().equalsIgnoreCase("Booking Date")) {
            commercialStatement.setBookingPeriodFrom(commercialStatementGeneration.getStatementPeriodFromDate());
            commercialStatement.setBookingPeriodTo(commercialStatementGeneration.getStatementPeriodToDate());
        }else if (commercialStatementGeneration.getSettlementSchedule().equalsIgnoreCase("Travel Date") || commercialStatementGeneration.getSettlementSchedule().equalsIgnoreCase("Before Travel Date") || commercialStatementGeneration.getSettlementSchedule().equalsIgnoreCase("On Travel Date")) {
            commercialStatement.setTravelDateTo(commercialStatementGeneration.getStatementPeriodToDate());
            commercialStatement.setTravelDateFrom(commercialStatementGeneration.getStatementPeriodFromDate());
        }
        commercialStatement.setProductCategory(commercialStatementGeneration.getProductCategory());
        commercialStatement.setProductCategorySubType(commercialStatementGeneration.getProductSubCategory());
        commercialStatement.setProductName(commercialStatementGeneration.getProductName());

        commercialStatement.setSupplierOrClientId(commercialStatementGeneration.getClientId());
        commercialStatement.setCommercialStatementFor(CommercialStatementFor.CLIENT.getName());

        BigDecimal totalAmount=BigDecimal.ZERO;

        for(CommercialStatementDetails commercialStatementDetails:commercialStatementDetailsSet)
            for (OrderCommercialDetails orderCommercialDetails:commercialStatementDetails.getOrderCommercialDetails())
                totalAmount=totalAmount.add(orderCommercialDetails.getCommercialValue());

        if (commercialStatement.getCommercialType().equalsIgnoreCase(CommercialType.RECEIVABLE.getValue())) {
            commercialStatement.setTotalReceivable(totalAmount);
            commercialStatement.setTotalReceived(BigDecimal.ZERO);
            commercialStatement.setBalanceReceivable(totalAmount.subtract(commercialStatement.getTotalReceived()));
        }
        else{
            commercialStatement.setTotalPayable(totalAmount);
            commercialStatement.setTotalPaid(BigDecimal.ZERO);
            commercialStatement.setBalancePayable(totalAmount.subtract(commercialStatement.getTotalPaid()));
        }

        for (CommercialStatementDetails commercialStatementDetails:commercialStatementDetailsSet)
            commercialStatementDetails.setCommercialStatement(commercialStatement);

        commercialStatement.setStatementName("CNK-"+System.currentTimeMillis());
        commercialStatement.setCommercialStatementDetails(commercialStatementDetailsSet);
        commercialStatement=clientCommercialStatementRepo.add(commercialStatement);

        try {
            InlineMessageResource inlineMessageResource = new InlineMessageResource();
            inlineMessageResource.setNotificationType("System");
            inlineMessageResource.setAlertName(clientAlert);
            ConcurrentHashMap input = new ConcurrentHashMap();
            input.put("statementId", commercialStatement.getStatementId());
            inlineMessageResource.setDynamicVariables(input);
            alertService.sendInlineMessageAlert(inlineMessageResource);
        }catch (Exception e){
            logger.error("Error in sending inline message for clientCommercialStatement");
        }
        return commercialStatement;
    }


    private OrderCommercialDetails generateCommercialStatementDetails(OpsProduct opsProduct, CommercialStatementGeneration commercialStatementGeneration) {
        Set<PassengerDetails> passengerDetailsSet = new HashSet<>();

       /* if (!(opsProduct.getOrderDetails().getClientCommercials().stream().anyMatch(opsOrderClientCommercial -> opsOrderClientCommercial.getCommercialType().equalsIgnoreCase(commercialStatementGeneration.getCommercialtype()) && opsOrderClientCommercial.getCommercialName().equalsIgnoreCase(commercialStatementGeneration.getCommercialHead()) && opsOrderClientCommercial.getCommercialCurrency().equalsIgnoreCase(commercialStatementGeneration.getCurrency()))))
            return null;*/

        OrderCommercialDetails orderCommercialDetails=new OrderCommercialDetails();
        switch (opsProduct.getProductSubCategory()) {
            case "Flight":
                for (OpsFlightPaxInfo opsFlightPaxInfo : opsProduct.getOrderDetails().getFlightDetails().getPaxInfo()) {
                    PassengerDetails passengerDetails = new PassengerDetails();
                    passengerDetails.setPassengerName(opsFlightPaxInfo.getFirstName());
                    passengerDetails.setPassengerType(opsFlightPaxInfo.getPaxType());
                    passengerDetailsSet.add(passengerDetails);
                }
                orderCommercialDetails.setSupplierOrClientCostPrice(new BigDecimal(opsProduct.getOrderDetails().getFlightDetails().getOpsFlightSupplierPriceInfo().getSupplierPrice()));
                break;
            case "Hotel":
               /* if (!opsProduct.getOrderDetails().getHotelDetails().getHotelName().equalsIgnoreCase(commercialStatementGeneration.getProductName()))
                    return null;*/
                for (OpsRoom opsRoom : opsProduct.getOrderDetails().getHotelDetails().getRooms()) {
                    for (OpsAccommodationPaxInfo opsAccommodationPaxInfo : opsRoom.getPaxInfo()) {
                        PassengerDetails passengerDetails = new PassengerDetails();
                        passengerDetails.setPassengerType(opsAccommodationPaxInfo.getPaxType());
                        passengerDetails.setPassengerName(opsAccommodationPaxInfo.getFirstName());
                        passengerDetailsSet.add(passengerDetails);
                    }
                }
                orderCommercialDetails.setSupplierOrClientCostPrice(new BigDecimal(opsProduct.getOrderDetails().getHotelDetails().getOpsAccoOrderSupplierPriceInfo().getSupplierPrice()));
                break;
        }

        orderCommercialDetails.setOrderId(opsProduct.getOrderID());
        for (OpsOrderClientCommercial opsOrderClientCommercial:opsProduct.getOrderDetails().getClientCommercials()){
           // if (opsOrderClientCommercial.getCommercialType().equalsIgnoreCase(commercialStatementGeneration.getCommercialtype()) && opsOrderClientCommercial.getCommercialName().equalsIgnoreCase(commercialStatementGeneration.getCommercialHead())){
                if (StringUtils.isEmpty(orderCommercialDetails.getCommercialValue()))
                    orderCommercialDetails.setCommercialValue(new BigDecimal(opsOrderClientCommercial.getCommercialAmount()));
                else orderCommercialDetails.setCommercialValue(orderCommercialDetails.getCommercialValue().add(new BigDecimal(opsOrderClientCommercial.getCommercialAmount())));
            }
        //}
        orderCommercialDetails.setPassengerDetailsSet(passengerDetailsSet);
        return orderCommercialDetails;
    }


    private LocalDate getStartDate(JSONObject halfYearlyJson) {
        int year = halfYearlyJson.getInt("fromYear");
        String monthStr = halfYearlyJson.getString("fromMonth");
        Month month = Month.valueOf(monthStr.toLowerCase());
        return LocalDate.of(year, month, 1);
    }

    private LocalDate getEndDate(JSONObject halfYearlyJson) {
        int year = halfYearlyJson.getInt("toYear");
        String monthStr = halfYearlyJson.getString("toMonth");
        Month month = Month.valueOf(monthStr.toLowerCase());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month.getValue());
        calendar.set(Calendar.YEAR, year);
        int lastDay = calendar.getActualMaximum(Calendar.DATE);
        return LocalDate.of(year, month, lastDay);
    }

    private LocalDate getSettlementDueDate(JSONObject halfYearlyJson) {
        int year = halfYearlyJson.getInt("settlementDueYear");
        String monthStr = halfYearlyJson.getString("settlementDueMonth");
        int day = halfYearlyJson.getInt("settlementDueDate");
        Month month = Month.valueOf(monthStr.toLowerCase());
        return LocalDate.of(year, month, day);
    }
}
