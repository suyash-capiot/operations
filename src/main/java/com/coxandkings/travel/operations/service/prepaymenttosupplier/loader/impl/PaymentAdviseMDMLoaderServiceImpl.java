package com.coxandkings.travel.operations.service.prepaymenttosupplier.loader.impl;

import com.coxandkings.travel.operations.criteria.prepaymenttosupplier.PaymentCriteria;
import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.PaymentAdviceFilter;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.loader.*;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice.PaymentAdvice;
import com.coxandkings.travel.operations.resource.prepaymenttosupplier.paymentadvice.PaymentDueDate;
import com.coxandkings.travel.operations.resource.prepaymenttosupplier.paymentadvice.DateWisePaymentPercentage;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.loader.PaymentAdviseMDMLoaderService;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.paymentadvice.PaymentAdviceService;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import org.apache.commons.codec.EncoderException;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class PaymentAdviseMDMLoaderServiceImpl implements PaymentAdviseMDMLoaderService{


    @Value(value = "${pre-payment-to-supplier.mdm.supplier-settlement}")
    private String supplierSettlementMdmUrl;




    @Autowired
    private MDMRestUtils mdmRestUtil;

    JsonObjectProvider jsonObjectProvider = new JsonObjectProvider();

    @Autowired
    private OpsBookingService opsBookingService;

    @Autowired
    private PaymentAdviceService paymentAdviceService;

    private static Logger logger = LogManager.getLogger(PaymentAdviseMDMLoaderServiceImpl.class);
    URI url;

    @Override
    public List<String> getSupplierCurrenciesList(String supplierID) throws OperationException, EncoderException {
        logger.info("before calling getSupplierCurrenciesList method ");
        List<String> supplierCurrencyList=new ArrayList<>();
        String url = supplierSettlementMdmUrl +makeURLForPaymentAdvice(supplierID);
        URI uri = UriComponentsBuilder.fromUriString(url).build().encode().toUri();
        ResponseEntity<String> responseEntity = mdmRestUtil.exchange(uri,HttpMethod.GET,null,String.class);
        String responseInString=responseEntity.getBody();
        List<String> data = (List<String>) jsonObjectProvider.getChildObject(responseInString,"$.data[0].supplierBankDetails[*].supplierCurrency",List.class);
        logger.info("before calling getSupplierCurrenciesList method ");
        return data;

    }


    @Override
    public Map<String, Object> getSupplierSettlement(String supplierId) throws OperationException
    {
        Map<String,Object> commercials = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("supplierId",supplierId);
        URI uri = UriComponentsBuilder.fromUriString(supplierSettlementMdmUrl+jsonObject.toString()).build().encode().toUri();
        try {
            String supplierSettlement = mdmRestUtil.exchange(uri, HttpMethod.GET, String.class);
            MdmData settlement = (MdmData) jsonObjectProvider.getChildObject(supplierSettlement, "$.data[0]", MdmData.class);
            if (settlement!=null) {
                CommissionableCommercials commissionableCommercials = settlement.getCommissionableCommercials();
                if (commissionableCommercials != null) {
                    List<CommercialHead> commercialHeads = commissionableCommercials.getCommercialHeads();
                    if (commercialHeads != null && commercialHeads.size() > 0) {
                        for (CommercialHead head : commercialHeads) {
                            if (head.getCommercialHead().contains("Receivable") &&
                                    head.getCommercialHead().startsWith("Standard")) {
                                commercials.put("CommissionableCommercials", head);
                                return commercials;
                            }
                        }
                    }
                }

                NonCommissionableCommercials nonCommissionableCommercials = settlement.getNonCommissionableCommercials();
                if (nonCommissionableCommercials != null) {
                    List<CommercialHead> commercialHeads = nonCommissionableCommercials.getCommercialHeads();
                    if (commercialHeads != null && commercialHeads.size() > 0) {
                        for (CommercialHead head : commercialHeads) {
                            if (head.getCommercialHead().contains("Receivable") &&
                                    head.getCommercialHead().startsWith("Standard")) {
                                commercials.put("NonCommissionableCommercials", head);
                                return commercials;
                            }
                        }
                    }
                } else {
                    throw new OperationException("No Commercial defined for Supplier Id: " + supplierId);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("Error While getting supplier Settlements");
        }
        return null;

    }

    @Override
    public String getPaymentAdviceGenerationDueDate(String supplierId) {
        //todo : hit mdm to get payment advice due date
        ZonedDateTime date = ZonedDateTime.now();
//       date.toOffsetDateTime();
        return  date.toOffsetDateTime().toString();
    }

    @Override
    public String getTypeOfSettlement(String supplierId,boolean uiTrigger) throws OperationException {
        String typeOfSettlement = new String();
        logger.info("in getTypeOfSettalment service method method ");
        if (!uiTrigger)
        {
            Map<String, Object> supplierSettlement = getSupplierSettlement(supplierId);
            if (supplierSettlement!=null)
            {
                for (Map.Entry<String,Object> entry : supplierSettlement.entrySet())
                {
                    CommercialHead head = (CommercialHead) entry.getValue();
                    typeOfSettlement = head.getTypeOfSettlement();
                    if ("No-Credit".equalsIgnoreCase(typeOfSettlement))
                    {
                        String creditType = head.getNoCreditSettlement().get(0).getCreditType();
                        return typeOfSettlement.concat(" ").concat(creditType);
                    }
                    else {
                        return typeOfSettlement;
                    }
                }
            }
            else {
                logger.info("Type of Settlement not configured for Supplier Id: "+supplierId);
            }
        }
        else {
            typeOfSettlement = "No credit - pre-payment";
        }

        return typeOfSettlement;
    }


    @Override
    public DateWisePaymentPercentage getPaymentDueDate(String bookingId, String orderId, String supplierId) throws OperationException
    {
        ZonedDateTime paymentDueDate;
        DateWisePaymentPercentage dateWisePaymentPercentage = null;
        if (getPrePaymentRequiredFlag(supplierId))
        {
            PrePaymentSettlement prePaymentSettlement = getPrePaymentSettlement(supplierId);
            PayableAmount payableAmount = prePaymentSettlement.getPayableAmount();
            String prePaymentType = prePaymentSettlement.getPrePaymentType();
            if ("Full".equalsIgnoreCase(prePaymentType)) {
                if (payableAmount != null) {
                    dateWisePaymentPercentage = generateDate(payableAmount.getAdvance(), bookingId, orderId);
                    return dateWisePaymentPercentage;

                }
            }

            if ("Partial".equalsIgnoreCase(prePaymentType))
            {
                if (payableAmount != null) {
                    DateWisePaymentPercentage advance = getAdvance(bookingId, orderId, supplierId);
                    return advance;

                }
            }


        }
        else {
            dateWisePaymentPercentage = new DateWisePaymentPercentage(Date.from(ZonedDateTime.now().toInstant()),BigDecimal.ZERO);
            return dateWisePaymentPercentage;
        }
        return null;
    }

    @Override
    public String getSupplierName(String supplierId) throws JSONException, OperationException {
        JSONObject jsonObject = new JSONObject();
        String supplierName = new String();
        if (!StringUtils.isEmpty(supplierId)) {
            jsonObject.put("supplierId", supplierId);
            String filter = supplierSettlementMdmUrl + jsonObject.toString().concat("&select=supplierName");
            URI uri = UriComponentsBuilder.fromUriString(filter).build().encode().toUri();
            supplierName = mdmRestUtil.exchange(uri, HttpMethod.GET, String.class);
            supplierName = jsonObjectProvider.getAttributeValue(supplierName, "$.data[0].supplierName", String.class);
            return supplierName;
        }
        return null;
    }

    @Override
    public String getModeOfPayment(String supplierId) throws JSONException, OperationException {
        String modeOfPayment = new String();
        if (getPrePaymentRequiredFlag(supplierId))
        {
            PrePaymentSettlement prePaymentSettlement = getPrePaymentSettlement(supplierId);
            modeOfPayment = prePaymentSettlement.getSupplierPreferredPaymentType();

        }
        return modeOfPayment ;
    }


    public boolean getPrePaymentRequiredFlag(String supplierId) throws OperationException
    {
        boolean flag = false;
        Map<String, Object> supplierSettlement = getSupplierSettlement(supplierId);
        if (supplierSettlement!=null)
        {
            for (Map.Entry<String,Object> entry : supplierSettlement.entrySet())
            {
                CommercialHead head = (CommercialHead) entry.getValue();
                return getCreditTypeAsPrePayment(head);
            }
        }

        return flag;
    }

    @Override
    public DateWisePaymentPercentage getAdvance(String bookingId, String orderId, String supplierId) throws OperationException {
        Date date = new Date();
        List<Date> dates = new ArrayList<>();
        List<DateWisePaymentPercentage> dateWisePaymentPercentages = new ArrayList<>();
        DateWisePaymentPercentage dateWisePaymentPercentage = null;
        PaymentCriteria paymentCriteria = new PaymentCriteria();
        paymentCriteria.setBookingRefId(bookingId);
        paymentCriteria.setOrderId(orderId);
        paymentCriteria.setSupplierReferenceId(supplierId);
        List<PaymentAdvice> paymentAdvices = paymentAdviceService.searchSupplierPayment(paymentCriteria);
        if (paymentAdvices != null && paymentAdvices.size() == 0) {
            dateWisePaymentPercentage = generateDate(getPrePaymentSettlement(supplierId).getPayableAmount().getAdvance(), bookingId, orderId);
            return dateWisePaymentPercentage;

        }
        else if (paymentAdvices != null && paymentAdvices.size() > 0)
        {
            PayableAmount payableAmount = getPrePaymentSettlement(supplierId).getPayableAmount();
            List<Balance> balance = payableAmount.getBalance();
            for (Balance balance1 : balance)
            {
                Advance advance = new Advance();
                advance.setAdvance(balance1.getBalance());
                advance.setNumberOfDays(balance1.getNumberOfDays());
                advance.setSettlementSchedule(balance1.getSettlementSchedule());
                dateWisePaymentPercentage = generateDate(advance, bookingId, orderId);
                dateWisePaymentPercentages.add(dateWisePaymentPercentage);
            }

            dateWisePaymentPercentage = getPercentage(dateWisePaymentPercentages);
            return dateWisePaymentPercentage;
        }
        return null;
    }




    private DateWisePaymentPercentage getPercentage(List<DateWisePaymentPercentage> dateWisePaymentPercentages)
    {
        Collections.sort(dateWisePaymentPercentages, new PaymentDueDate());
        return dateWisePaymentPercentages.get(0);
    }



    private ZonedDateTime getBookingDate(String bookingId,String orderId)
    {
        OpsBooking opsBooking = null;
        ZonedDateTime bookingDate = null;
        try {
            opsBooking = opsBookingService.getBooking(bookingId);
            bookingDate = opsBooking.getBookingDateZDT();
        } catch (OperationException e) {

        }
        return bookingDate;
    }

    private ZonedDateTime getTravelDate(String bookingId,String orderId) throws OperationException {
        ZonedDateTime travelDate = null;
        OpsBooking opsBooking = opsBookingService.getBooking(bookingId);
        OpsProduct opsProduct = opsBookingService.getOpsProduct(opsBooking,orderId);

        OpsProductCategory productCategory = OpsProductCategory.getProductCategory(opsProduct.getProductCategory());
        if (!StringUtils.isEmpty(productCategory))
        {
            OpsProductSubCategory opsProductSubCategory = OpsProductSubCategory.getProductSubCategory(productCategory, opsProduct.getProductSubCategory());
            if (!StringUtils.isEmpty(opsProductSubCategory))
            {
                switch (productCategory) {
                    case PRODUCT_CATEGORY_ACCOMMODATION: {
                        switch (opsProductSubCategory) {
                            case PRODUCT_SUB_CATEGORY_FLIGHT:
                                break;
                            case PRODUCT_SUB_CATEGORY_BUS:
                                break;
                            case PRODUCT_SUB_CATEGORY_RAIL:
                                break;
                            case PRODUCT_SUB_CATEGORY_INDIAN_RAIL:
                                break;
                            case PRODUCT_SUB_CATEGORY_CAR:
                                break;
                            case PRODUCT_SUB_CATEGORY_HOTELS: {
                                String checkIn = opsProduct.getOrderDetails().getHotelDetails().getRooms().get(0).getCheckIn();
                                LocalDate checkInLocalDate = LocalDate.parse(checkIn);
                                travelDate = ZonedDateTime.of(checkInLocalDate, LocalTime.of(00, 00, 00, 00), ZoneId.systemDefault());
                                return travelDate;
                            }
                        }
                    }

                    case PRODUCT_CATEGORY_TRANSPORTATION: {
                        switch (opsProductSubCategory) {
                            case PRODUCT_SUB_CATEGORY_FLIGHT: {
                                travelDate = opsProduct.getOrderDetails().getFlightDetails().getOriginDestinationOptions().get(0).getFlightSegment().get(0).getDepartureDateZDT();
                                return travelDate;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }


    private String makeURLForPaymentAdvice(String supplierId ) {

        PaymentAdviceFilter filter = new PaymentAdviceFilter( );
        filter.setSupplierId( supplierId );
        return PaymentAdviceFilter.getUrl( filter );
    }

    private boolean getCreditTypeAsPrePayment(CommercialHead commercialHead)
    {
        boolean flag = false;
        if ("No-Credit".equalsIgnoreCase(commercialHead.getTypeOfSettlement()))
        {
            List<NoCreditSettlement> noCreditSettlements = commercialHead.getNoCreditSettlement();
            for (NoCreditSettlement noCreditSettlement: noCreditSettlements)
            {
                if ("Pre Payment".equalsIgnoreCase(noCreditSettlement.getCreditType()))
                {
                    flag = true;
                }
            }
        }
        return flag;
    }

    private PrePaymentSettlement getPrePaymentSettlement(String supplierId) throws OperationException {
        Map<String, Object> supplierSettlement = getSupplierSettlement(supplierId);
        if (supplierSettlement!=null)
        {
            for (Map.Entry<String,Object> entry: supplierSettlement.entrySet())
            {
                CommercialHead head = (CommercialHead) entry.getValue();
                NoCreditSettlement noCreditSettlement = (NoCreditSettlement) head.getNoCreditSettlement().get(0);
                if (noCreditSettlement!=null)
                {
                    return noCreditSettlement.getPrePaymentSettlement();
                }
            }
        }
        else {
            logger.error("Pre Payment Settlement is not defined for Supplier Id:"+supplierId);
        }
        return null;
    }

    private DateWisePaymentPercentage generateDate(Advance advance, String bookingId, String orderId) throws OperationException {
        ZonedDateTime paymentDueDate = null;
        DateWisePaymentPercentage dateWisePaymentPercentage = null;
        ZonedDateTime bookingDate = getBookingDate(bookingId, orderId);
        ZonedDateTime travelDate = getTravelDate(bookingId, orderId);
        if ("Booking Date".equalsIgnoreCase(advance.getSettlementSchedule()))
        {

            logger.info("### Supplier settlement schedule on "+advance.getSettlementSchedule()+". Number of Days are "+ advance.getNumberOfDays());

            paymentDueDate = bookingDate.plusDays(advance.getNumberOfDays());
            logger.info("Booking date is "+bookingDate);
            logger.info("Payment Due date is "+paymentDueDate);
            dateWisePaymentPercentage = new DateWisePaymentPercentage(Date.from(paymentDueDate.toInstant()),BigDecimal.valueOf(advance.getAdvance()),bookingDate,travelDate);
            return dateWisePaymentPercentage;

        }
        else if ("Travel Date".equalsIgnoreCase(advance.getSettlementSchedule()))
        {
            logger.info("### Supplier settlement schedule on "+advance.getSettlementSchedule()+". Number of Days are "+ advance.getNumberOfDays());

            paymentDueDate = travelDate.plusDays(advance.getNumberOfDays());
            logger.info("Travel date is "+travelDate);
            logger.info("Payment Due date is "+paymentDueDate);
            dateWisePaymentPercentage = new DateWisePaymentPercentage(Date.from(paymentDueDate.toInstant()),BigDecimal.valueOf(advance.getAdvance()),bookingDate,travelDate);
            return dateWisePaymentPercentage;

        }
        else {
            dateWisePaymentPercentage = new DateWisePaymentPercentage(Date.from(ZonedDateTime.now().toInstant()),BigDecimal.valueOf(advance.getAdvance()));
            return dateWisePaymentPercentage;
        }

    }

}
