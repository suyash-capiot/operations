package com.coxandkings.travel.operations.service.prepaymenttosupplier.paymentadvice.impl;

import com.coxandkings.travel.operations.criteria.booking.becriteria.BookingSearchCriteria;
import com.coxandkings.travel.operations.criteria.booking.becriteria.ProductDetailsFilter;
import com.coxandkings.travel.operations.criteria.prepaymenttosupplier.PaymentCriteria;
import com.coxandkings.travel.operations.enums.prepaymenttosupplier.PaymentAdviceStatusValues;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice.PaymentAdvice;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice.PaymentAdviceOrderInfo;
import com.coxandkings.travel.operations.model.todo.ToDoTask;
import com.coxandkings.travel.operations.repository.prepaymenttosupplier.paymentadvice.PaymentAdviceRepository;
import com.coxandkings.travel.operations.resource.notification.InlineMessageResource;
import com.coxandkings.travel.operations.resource.notification.NotificationResource;
import com.coxandkings.travel.operations.resource.prepaymenttosupplier.paymentadvice.DateWisePaymentPercentage;
import com.coxandkings.travel.operations.resource.searchviewfilter.BookingSearchResponseItem;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResource;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.loader.PaymentAdviceFinanceLoaderService;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.loader.PaymentAdviseBELoaderService;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.loader.PaymentAdviseMDMLoaderService;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.paymentadvice.AutomaticPaymentAdviceGenerationService;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.paymentadvice.PaymentAdviceLoaderService;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.paymentadvice.PaymentAdviceService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.URI;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service(value = "AutomaticPaymentAdviceGenerationServiceImpl")
public class AutomaticPaymentAdviceGenerationServiceImpl implements AutomaticPaymentAdviceGenerationService {

    private static final Logger logger = LogManager.getLogger(PaymentAdviceServiceImpl.class);
    @Autowired
    ToDoTaskService toDoTaskService;
    ToDoTask toDoTask;
    @Value(value = "${pre-payment-to-supplier.mdm.supplier-id}")
    private String supplierIdUrl;
    @Autowired
    private MDMRestUtils mdmRestUtils;
    @Autowired
    private JsonObjectProvider jsonObjectProvider;
    @Autowired
    private PaymentAdviseMDMLoaderService paymentAdviseMDMLoaderService;
    @Autowired
    private OpsBookingService opsBookingService;
    @Value(value = "${pre-payment-to-supplier.mdm.supplier-settlement}")
    private String settlementUrl;
    @Autowired
    private PaymentAdviseBELoaderService paymentAdviseBELoaderService;
    @Autowired
    private PaymentAdviceFinanceLoaderService paymentAdviceFinanceLoaderService;
    @Autowired
    private PaymentAdviceRepository paymentAdviceRepository;
    @Autowired
    private PaymentAdviceService paymentAdviceService;
    @Autowired
    private AlertService alertService;
    @Autowired
    private PaymentAdviceLoaderService paymentAdviceLoaderService;


    @Override
    public PaymentAdvice generatePaymentAdvice() throws OperationException, IOException, ParseException, InvocationTargetException, IllegalAccessException {
        logger.info( "*** Entering AutomaticPaymentAdviceGenerationServiceImpl.generatePaymentAdvice() method ***" );
        List<String> supplierIds = getAllSupplierId();

        for (String supplierId : supplierIds)
        {
            String ss = supplierId.toString();
            String id = jsonObjectProvider.getAttributeValue(ss, "$.supplierId", String.class);
            if(paymentAdviseMDMLoaderService.getPrePaymentRequiredFlag(id))
            {
                List<BookingSearchResponseItem> bookings = getBookingBasedSupplierId(id);
                if( bookings == null )  {
                    logger.info("*** Number of Bookings fetched from BE for supplier ID: " + id + " is: 0");
                }
                else  {
                    logger.info("*** Number of Bookings fetched from BE for supplier ID: " + id + " is: " + bookings.size() );
                }

                if ( bookings != null && bookings.size() > 0)
                {
                    for (BookingSearchResponseItem bookingSearchResponseItem : bookings)
                    {
                        OpsBooking opsBooking = opsBookingService.getBooking(bookingSearchResponseItem.getBookID());
                        List<OpsProduct> opsProducts = opsBooking.getProducts().stream().filter(opsProduct -> opsProduct.getSupplierID().equals(id)).collect(Collectors.toList());
                        for (OpsProduct opsProduct : opsProducts)
                        {
                            boolean paymentDueDateReached = isPaymentDueDateReached(opsBooking.getBookID(), opsProduct.getOrderID(), id);
                            if (paymentDueDateReached)
                            {
                                String bookID = opsBooking.getBookID();
                                String orderID = opsProduct.getOrderID();
                                PaymentAdvice paymentAdvice = createPaymentAdvice(bookID, orderID, id);
                                if (paymentAdvice!=null)
                                {
                                    paymentAdvice = paymentAdviceRepository.savePaymentAdvice(paymentAdvice);
                                    logger.info("Payment advice has been generated with Paymnet Advice Number: "+paymentAdvice.getPaymentAdviceNumber());
                                    ToDoTaskResource toDoTaskResource = paymentAdviceService.getTodoForFinance(paymentAdvice); //TODO: TODO task for Finance
                                    if (!StringUtils.isEmpty(toDoTaskResource)) {
                                        try {
                                            toDoTask = toDoTaskService.save(toDoTaskResource);
                                            logger.info("Todo created with Todo id " + toDoTask.getId());
                                            paymentAdvice.setFinanceTodoTaskId(toDoTask.getId());
                                            InlineMessageResource inlineMessageResource = new InlineMessageResource();
                                            inlineMessageResource.setAlertName("Alert To Ops User");
                                            inlineMessageResource.setNotificationType("System");
                                            ConcurrentHashMap<String, String> dynamicVariables = new ConcurrentHashMap<>();
                                            dynamicVariables.put("bookingRefNumber", opsBooking.getBookID());
                                            dynamicVariables.put("orderId", opsProduct.getOrderID());
                                            dynamicVariables.put("supplierId", id);
                                            inlineMessageResource.setDynamicVariables(dynamicVariables);
                                            NotificationResource notificationResource = alertService.sendInlineMessageAlert(inlineMessageResource);
                                            if (!StringUtils.isEmpty(notificationResource.get_id())) {
                                                logger.info("Alert has been sent");
                                            } else {
                                                logger.debug("alert not sent", new OperationException("Alert not yet sent"));
                                            }
                                        } catch (Exception e) {
                                            logger.error("Todo is not getting createed");
                                        }
                                    }
                                    return paymentAdvice;
                                }
                            }
                        }
                    }
                }
            }
        }
        logger.info( "*** Exiting AutomaticPaymentAdviceGenerationServiceImpl.generatePaymentAdvice() method ***" );
        return null;
    }

    private List<String> getAllSupplierId() throws OperationException {
        logger.info( "*** Entering AutomaticPaymentAdviceGenerationServiceImpl.getAllSuppliers() method ***" );
        URI uri = UriComponentsBuilder.fromUriString(supplierIdUrl).build().toUri();
        ResponseEntity<String> id = mdmRestUtils.exchange(uri, HttpMethod.GET, null, String.class);
        String body = id.getBody();
        List<String> supplierIds = jsonObjectProvider.getChildrenCollection(body, "$.data", String.class);
        logger.info( "*** Exiting AutomaticPaymentAdviceGenerationServiceImpl.getAllSuppliers() method ***" );
        return supplierIds;
    }

    List<BookingSearchResponseItem> getBookingBasedSupplierId(String supplierId) {
        logger.info( "*** Entering AutomaticPaymentAdviceGenerationServiceImpl.getBookingBasedSupplierId() method ***" );
        BookingSearchCriteria bookingSearchCriteria = new BookingSearchCriteria();
        ProductDetailsFilter filter = new ProductDetailsFilter();
        filter.setSupplierName(supplierId);
        bookingSearchCriteria.setSize( Integer.MAX_VALUE );
        bookingSearchCriteria.setPage(1);
        bookingSearchCriteria.setProductBasedFilter(filter);

        List<BookingSearchResponseItem> bookings = opsBookingService.searchBookings(bookingSearchCriteria);
        if( bookings == null )  {
            logger.info( "*** No Bookings available for Supplier: " + supplierId );
            return null;
        }

        logger.info( "*** Fetched  " +  bookings.size() + "Bookings available for Supplier: " + supplierId );
        logger.info( "*** Exiting AutomaticPaymentAdviceGenerationServiceImpl.getBookingBasedSupplierId() method ***" );
        return bookings;
    }

    private String getSettlement(String supplierId) throws JSONException, OperationException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("supplierId", supplierId);
        String supplierUrl = settlementUrl + jsonObject.toString();
        URI uri = UriComponentsBuilder.fromUriString(supplierUrl).build().toUri();
        ResponseEntity<String> settlement = mdmRestUtils.exchange(uri, HttpMethod.GET, null, String.class);
        return settlement.getBody();
    }

    public BigDecimal getAmountPayableForSupplier(BigDecimal netPayable, BigDecimal balanceAmt, BigDecimal percentage) {
        BigDecimal amtPayableForSupplier = new BigDecimal(0);
        if (!StringUtils.isEmpty(balanceAmt)) {
            amtPayableForSupplier = balanceAmt.multiply((percentage.divide(BigDecimal.valueOf(100))));
            return amtPayableForSupplier;
        }
        return null;
    }

    private Boolean checkDuplicate(String bookingId, String orderId, String supplierId, BigDecimal amtPaidToSupplier, BigDecimal netAmt) {
        logger.info( "*** Entering AutomaticPaymentAdviceGenerationServiceImpl.checkDuplicate() method ***" );
        Boolean duplicateCheckFlag = false;
        PaymentCriteria paymentCriteria = new PaymentCriteria();
        paymentCriteria.setOrderLevelAmountPayableToSupplier(amtPaidToSupplier);
        paymentCriteria.setOrderId(orderId);
        paymentCriteria.setSupplierReferenceId(supplierId);
        paymentCriteria.setBookingRefId(bookingId);
        paymentCriteria.setOrderLevelNetPayableAmt(netAmt);

        List<PaymentAdvice> paymentAdviceList = paymentAdviceRepository.searchPaymentAdvise(paymentCriteria);
        if (paymentAdviceList.size() == 0) {
            duplicateCheckFlag = false;
        } else
            duplicateCheckFlag = true;

        logger.info( "*** Exiting AutomaticPaymentAdviceGenerationServiceImpl.checkDuplicate() method ***" );
        return duplicateCheckFlag;
    }


    private PaymentAdvice createPaymentAdvice(String bookingRefId, String orderId, String supplierId) throws IOException, JSONException, OperationException {
        logger.info( "*** Entering AutomaticPaymentAdviceGenerationServiceImpl.createPaymentAdvice() method ***" );
        PaymentAdvice paymentAdvice = new PaymentAdvice();
        PaymentAdviceOrderInfo paymentAdviceOrderInfo = new PaymentAdviceOrderInfo();
        BigDecimal netPayableValue = paymentAdviceLoaderService.getNetPayableToSupplier(bookingRefId, orderId);
        BigDecimal balancePayableValue = (paymentAdviceFinanceLoaderService.getBalanceAmtPayableToSupplier(bookingRefId, orderId, supplierId, netPayableValue));
        DateWisePaymentPercentage dueDate = paymentAdviseMDMLoaderService.getPaymentDueDate(bookingRefId, orderId, supplierId);
        BigDecimal amtPaidToSupplier = getAmountPayableForSupplier(netPayableValue, balancePayableValue, dueDate.getPercentage());

        if (!checkDuplicate(bookingRefId,orderId,supplierId,amtPaidToSupplier,netPayableValue)) {
            paymentAdviceOrderInfo.setBookingRefId(bookingRefId);
            paymentAdviceOrderInfo.setOrderId(orderId);
            paymentAdviceOrderInfo.setOrderLevelNetPayableToSupplier(netPayableValue);
            paymentAdviceOrderInfo.setOrderLevelAmountPayableForSupplier(amtPaidToSupplier);
            paymentAdvice.getPaymentAdviceOrderInfoSet().add(paymentAdviceOrderInfo);

            paymentAdvice.setSupplierRefId(supplierId);
            paymentAdvice.setSupplierName(paymentAdviseMDMLoaderService.getSupplierName(supplierId));

//        ZonedDateTime zonedDateTime = ZonedDateTime.of(dueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),LocalTime.of(0,0,0),ZoneId.systemDefault());
            ZoneId defaultZoneId = ZoneId.systemDefault();
            paymentAdvice.setPaymentDueDate(dueDate.getPaymentDueDate().toInstant().atZone(defaultZoneId));
            paymentAdvice.setDayOfPayment(String.valueOf(ZonedDateTime.now().getDayOfWeek()));
            paymentAdvice.setSupplierCurrency(paymentAdviseBELoaderService.getSupplierCurrency(bookingRefId, orderId));
            paymentAdvice.setNetPayableToSupplier(netPayableValue);
            paymentAdvice.setPaymentAdviceStatus(PaymentAdviceStatusValues.APPROVED);
            paymentAdvice.setPayToSupplier(true);
            paymentAdvice.setPaymentAdviceGenerationDueDate(ZonedDateTime.now());
            paymentAdvice.setDayOfPaymentAdviceGeneration(ZonedDateTime.now().getDayOfWeek().toString());
            paymentAdvice.setTypeOfSettlement(paymentAdviseMDMLoaderService.getTypeOfSettlement(supplierId, false));
            paymentAdvice.setSelectedSupplierCurrency(paymentAdvice.getSupplierCurrency());
            paymentAdvice.setModeOfPayment(paymentAdviseMDMLoaderService.getModeOfPayment(supplierId));//TODO: doubt
            paymentAdvice.setAdvancedType("On Account");
            paymentAdvice.setAmountPayableForSupplier(amtPaidToSupplier);
            paymentAdvice.setUiTrigger(false);
            paymentAdvice.setBalanceAmtPayableToSupplier(balancePayableValue);
            paymentAdvice.setPaymentAdviceNumber(paymentAdviceService.getNewPaymentAdviceID());
            paymentAdvice.setPayToSupplier(true);
            paymentAdvice.setPrePaymentApplicable(true);
            return paymentAdvice;
        }
        else {
            logger.error("Payment Advice already generated for booking Id"+bookingRefId+" and Order Id "+orderId);
        }

        logger.info( "*** Exiting AutomaticPaymentAdviceGenerationServiceImpl.createPaymentAdvice() method ***" );
        return paymentAdvice;

    }

    //TODO: add hoilday calendar validation
    public boolean isPaymentDueDateReached(String bookingId,String orderId,String supplierId) throws OperationException {
        logger.info( "*** Entering AutomaticPaymentAdviceGenerationServiceImpl.isPaymentDueDateReached() method ***" );
        boolean dateReached = false;
        DateWisePaymentPercentage paymentDueDate = paymentAdviseMDMLoaderService.getPaymentDueDate(bookingId, orderId, supplierId);
        ZonedDateTime todaysDate = ZonedDateTime.now();
        if (!DayOfWeek.SATURDAY.equals(todaysDate.getDayOfWeek()) || !DayOfWeek.SUNDAY.equals(todaysDate.getDayOfWeek()))
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(paymentDueDate.getPaymentDueDate());
            if (!(calendar.get(Calendar.DAY_OF_WEEK)==1 || calendar.get(Calendar.DAY_OF_WEEK) == 7))
            {
                int i = Date.from(todaysDate.toInstant()).compareTo(paymentDueDate.getPaymentDueDate());

                if (i>=0)
                {
                    dateReached = true;
                }
                else
                {
                    dateReached=false;
                }
            }
            else
            {
                if (DayOfWeek.FRIDAY.equals(todaysDate.getDayOfWeek()))
                {
                    dateReached = true;
                }
                else
                {
                    dateReached =  false;
                }

            }

        }
        else {
            dateReached = false;
        }

        logger.info( "Is payment due date reached for " +  bookingId + " orderID:" + orderId + " Supplier ID: " + supplierId  + "::" + dateReached );
        logger.info( "*** Exiting AutomaticPaymentAdviceGenerationServiceImpl.isPaymentDueDateReached() method ***" );
        return dateReached;
    }
//
//    public PaymentAdvice generatePaymentAdviceAutomatically(OpsBooking opsBooking) throws IOException, OperationException, JSONException {
//        JSONObject jsonObject = new JSONObject();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
//        ZonedDateTime todaysDate = ZonedDateTime.now();
//        Instant instant = todaysDate.toInstant();
//        Date date = Date.from(instant);
//
//        System.out.println("Booking2 Id "+opsBooking.getBookID());
//        List<OpsProduct> products = opsBooking.getProducts();
//        for (int i =0;i<=products.size();i++) {
//            for (OpsProduct opsProduct : products) {
//                String supplierID = opsProduct.getSupplierID();
//                System.out.println("supplierReferenceId" + supplierID);
//                String typeOfSettlement = paymentAdviseMDMLoaderService.getTypeOfSettlement(supplierID);
//                if (!StringUtils.isEmpty(typeOfSettlement)) {
//                    if (typeOfSettlement.equalsIgnoreCase("pre-payment")) {
//                        System.out.println("Supplier is applicable for pre-payment");
//                        Date dateTime = paymentAdviseMDMLoaderService.getPaymentDueDate(supplierID);
//                        int j = compareDate(date, dateTime);
//                        if (j==0)
//                        {
//                            System.out.println("Generate Payment Advice");
//                            PaymentAdvice paymentAdvice =new PaymentAdvice();
//                            PaymentAdviceOrderInfo paymentAdviceOrderInfo = new PaymentAdviceOrderInfo();
//                            BigDecimal netPayableValue = paymentAdviseBELoaderService.getNetPayableToSupplier(opsBooking.getBookID(),opsProduct.getOrderID());
//                            BigDecimal balancePayableValue = paymentAdviceFinanceLoaderService.getBalanceAmtPayableToSupplier(opsBooking.getBookID(),opsProduct.getOrderID(),opsProduct.getSupplierID());
//
//                            paymentAdviceOrderInfo.setBookingRefId(opsBooking.getBookID());
//                            paymentAdviceOrderInfo.setOrderId(opsProduct.getOrderID());
//                            paymentAdviceOrderInfo.setOrderLevelNetPayableAmt(netPayableValue);
//                            paymentAdviceOrderInfo.setOrderLevelAmountPayableToSupplier(balancePayableValue);
//                            paymentAdvice.getPaymentAdviceOrderInfoSet().add(paymentAdviceOrderInfo);
//
//                            paymentAdvice.setSupplierRefId(supplierID);
//                            paymentAdvice.setSupplierName(paymentAdviseMDMLoaderService.getSupplierName(supplierID));
//                            paymentAdvice.setPaymentDueDate(ZonedDateTime.parse(simpleDateFormat.format(date)));
//                            paymentAdvice.setDayOfPayment(String.valueOf(ZonedDateTime.parse(simpleDateFormat.format(date)).getDayOfWeek()));
//                            paymentAdvice.setSupplierCurrency(paymentAdviseBELoaderService.getSupplierCurrency(opsBooking.getBookID(),opsProduct.getOrderID()));
//                            paymentAdvice.setNetPayableToSupplier(netPayableValue);
//                            paymentAdvice.setPaymentAdviceStatus(PaymentAdviceStatusValues.APPROVAL_PENDING);
//                            paymentAdvice.setPayToSupplier(true);
//                            paymentAdvice.setPaymentAdviceGenerationDueDate(ZonedDateTime.now());
//                            paymentAdvice.setDayOfPaymentAdviceGeneration(ZonedDateTime.now().getDayOfWeek().toString());
//                            paymentAdvice.setTypeOfSettlement(paymentAdviseMDMLoaderService.getTypeOfSettlement(supplierID));
//                            paymentAdvice.setSelectedSupplierCurrency(paymentAdvice.getSupplierCurrency());
//                            paymentAdvice.setModeOfPayment(paymentAdviseMDMLoaderService.getModeOfPayment(supplierID));//TODO: doubt
//                            paymentAdvice.setAdvancedType("PSO Related");   //TODO: reply from Ashish is pending.
//                            paymentAdvice.setAmountPayableForSupplier(new BigDecimal("123456"));
//                            paymentAdvice.setUiTrigger(false);
//                            paymentAdvice.setBalanceAmtPayableToSupplier(balancePayableValue);
//
//                            paymentAdvice = paymentAdviceRepository.savePaymentAdvice(paymentAdvice);
//                            return paymentAdvice;
//                        }
//
//                    }
//                }
//            }
//        }
//
//
//
//
//
//
//        return null;
//    }
//
//
//
//    private int compareDate(Date todaysDate, Date newDate)
//    {
//        int value = 0;
//        if (!StringUtils.isEmpty(newDate)) {
//            switch (todaysDate.compareTo(newDate)) {
//                case -1:
//                    System.out.println("todaysDate is sooner than newDate");
//                    value = -1;
//                    break;
//                case 0:
//                    System.out.println("todaysDate and newDate are equal");
//                    value = 0;
//                    break;
//                case 1:
//                    System.out.println("todaysDate is later than newDate");
//                    value = 1;
//                    break;
//                default:
//                    System.out.println("Invalid results from date comparison");
//                    value = 2;
//                    break;
//
//            }
//        }
//        return value;
//    }


}
