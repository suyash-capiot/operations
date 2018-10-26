package com.coxandkings.travel.operations.service.qcmanagement.impl;

import com.coxandkings.travel.operations.enums.amendclientcommercials.BEInboundOperation;
import com.coxandkings.travel.operations.enums.amendclientcommercials.BEOperationId;
import com.coxandkings.travel.operations.enums.amendclientcommercials.BEServiceUri;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.service.qcmanagement.QcSupplierPriceDetailsService;
import com.coxandkings.travel.operations.utils.BookingEngineElasticData;
import com.coxandkings.travel.operations.utils.TypeConverterUtils;
import com.coxandkings.travel.operations.utils.xml.XMLUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QcSupplierPriceDetailsServiceImpl implements QcSupplierPriceDetailsService {
    private static final Logger logger = LogManager.getLogger(QcSupplierPriceDetailsServiceImpl.class);

    @Autowired
    private BookingEngineElasticData bookingEngineElasticData;

    /**
     * purpose of this method to compare
     * air pricing details with ops pricing details
     *
     * @param opsBooking
     * @param opsProduct
     * @return
     */
    public Boolean qcCheckForAirSupplierPriceDetails(OpsBooking opsBooking, OpsProduct opsProduct) {


        //getting supplier details from SI by calling consumeAirSupplierPriceDetails method
        Element rootResponseBody = consumeAirSupplierPriceDetails(opsBooking);
        Boolean qcStatusValue = false;

        if (rootResponseBody != null) {
            Map<String, String> unMatchedBaseFare = new HashMap<>();
            Map<String, String> unMatchedOpsTaxCode = new HashMap<>();
            Map<String, String> unMatchedOpsFeeCode = new HashMap<>();

            Element[] airPriceWrappers = XMLUtils.getElementsAtXPath(rootResponseBody, "./airi:ResponseBody/air:OTA_AirPriceRSWrapper");
            //checking each wrapper ItinTotalprice
            for (Element aWrapperElement : airPriceWrappers) {
                //get SI supplierID
                String supplierId = XMLUtils.getValueAtXPath(aWrapperElement, "./air:SupplierID");
                //sub element
                Element airPriceResponse = XMLUtils.getFirstElementAtXPath(aWrapperElement, "./ota:OTA_AirPriceRS");

                //get SI supplierTotalPrice
                BigDecimal itinTotalPrice = TypeConverterUtils.convertToBigDecimal(airPriceResponse.getAttribute("ItinTotalPrice"), 0);
                String supplierTotalPrice = itinTotalPrice.toString();

                //get ops supplierPrice
                BigDecimal opsSupplierPriceInDouble = TypeConverterUtils.convertToBigDecimal(opsProduct.getOrderDetails().getFlightDetails().getOpsFlightSupplierPriceInfo().getSupplierPrice(), 0);
                String opsSupplierPrice = opsSupplierPriceInDouble.toString();

                //get SI airlinecode and flight number
                Element codeAndFlightNumberRes = XMLUtils.getFirstElementAtXPath(airPriceResponse, "./ota:PricedItineraries/ota:PricedItinerary/ota:AirItinerary/ota:OriginDestinationOptions/ota:OriginDestinationOption/ota:FlightSegment/ota:OperatingAirline");
                String airlineCode = codeAndFlightNumberRes.getAttribute("Code");
                String flightNumber = codeAndFlightNumberRes.getAttribute("FlightNumber");


                OpsFlightDetails opsFlightDetails = opsProduct.getOrderDetails().getFlightDetails();
                List<OpsFlightSegment> flightSegments = opsProduct.getOrderDetails().getFlightDetails().getOriginDestinationOptions().get(0).getFlightSegment();
                //get ops airline code
                String opsAirlinrCode = flightSegments.get(0).getMarketingAirline().getAirlineCode();
                //get ops flight number
                String opsFlightNumber = flightSegments.get(0).getMarketingAirline().getFlightNumber();

                //checking OpsProduct match with SI product
                if (supplierId.equalsIgnoreCase(opsProduct.getSupplierID())
                        && airlineCode.equalsIgnoreCase(opsAirlinrCode)
                        && flightNumber.equalsIgnoreCase(opsFlightNumber)
                        && supplierTotalPrice.equalsIgnoreCase(opsSupplierPrice)) {
                    //for loop to check each fareBreakDowns
                    Element fareBreakDowns[] = XMLUtils.getElementsAtXPath(airPriceResponse, "./ota:PricedItineraries/ota:PricedItinerary/ota:AirItineraryPricingInfo/ota:PTC_FareBreakdowns/ota:PTC_FareBreakdown");
                    //getting OpsPaxTypeFareFlightSupplier to get ops pax type
                    List<OpsPaxTypeFareFlightSupplier> paxTypeFareFlightSuppliers = opsProduct.getOrderDetails().getFlightDetails().getOpsFlightSupplierPriceInfo().getPaxTypeFares();
                    for (Element fareBreakdown : fareBreakDowns) {
                        //getting SI pax type Adult or Child
                        String paxType = XMLUtils.getFirstElementAtXPath(fareBreakdown, "./ota:PassengerTypeQuantity").getAttribute("Code");

                        for (OpsPaxTypeFareFlightSupplier paxTypeFareFlightSupplier : paxTypeFareFlightSuppliers) {
                            //getting ops pax type and matching with SI pax type
                            String opsPaxType = paxTypeFareFlightSupplier.getPaxType();
                            if (paxType.equalsIgnoreCase(opsPaxType)) {
                                Element passengerFareRes = XMLUtils.getFirstElementAtXPath(fareBreakdown, "./ota:PassengerFare");

                                //get SI base fare amount
                                BigDecimal baseFareAmount = TypeConverterUtils.convertToBigDecimal(XMLUtils.getFirstElementAtXPath(passengerFareRes, "./ota:BaseFare").getAttribute("Amount"), 0);
                                //get ops base fare amount
                                BigDecimal opsBaseFareAmount = TypeConverterUtils.convertToBigDecimal(paxTypeFareFlightSupplier.getBaseFare().getAmount().toString(), 0);

                                if (!baseFareAmount.toString().equalsIgnoreCase(opsBaseFareAmount.toString()))
                                    unMatchedBaseFare.put("opsBaseFare", opsBaseFareAmount.toString());

                                //get SI taxs amounts
                                Element taxesRes[] = XMLUtils.getElementsAtXPath(passengerFareRes, "./ota:Taxes/ota:Tax");
                                List<OpsTax> opsTaxes = paxTypeFareFlightSupplier.getTaxes().getTax();
                                for (Element tax : taxesRes) {

                                    for (OpsTax opsTax : opsTaxes) {
                                        if (tax.getAttribute("TaxCode").equalsIgnoreCase(opsTax.getTaxCode())) {
                                            //get SI Taxs amount
                                            BigDecimal taxAmount = TypeConverterUtils.convertToBigDecimal(tax.getAttribute("Amount"), 0);
                                            //get ops taxes amount
                                            BigDecimal opsTaxAmount = TypeConverterUtils.convertToBigDecimal(opsTax.getAmount().toString(), 0);
                                            if (taxAmount.toString().equalsIgnoreCase(opsTaxAmount.toString())) {
                                                break;
                                            } else {
                                                unMatchedOpsTaxCode.put(opsTax.getTaxCode(), opsTaxAmount.toString());
                                                break;
                                            }
                                        }
                                    }
                                }
                                //get SI fees amounts
                                Element feesRes[] = XMLUtils.getElementsAtXPath(passengerFareRes, "./ota:Fees/ota:Fee");
                                List<OpsFee> opsFees = paxTypeFareFlightSupplier.getFees().getFee();
                                for (Element fee : feesRes) {
                                    //get ops fees and match with SI fee
                                    for (OpsFee opsFee : opsFees) {
                                        if (fee.getAttribute("FeeCode").equalsIgnoreCase(opsFee.getFeeCode())) {
                                            //get SI fee amount
                                            BigDecimal feeAmount = TypeConverterUtils.convertToBigDecimal(fee.getAttribute("Amount"), 0);
                                            //get SI fee amount
                                            BigDecimal opsFeeAmount = TypeConverterUtils.convertToBigDecimal(opsFee.getAmount().toString(), 0);
                                            if (feeAmount.toString().equalsIgnoreCase(opsFeeAmount.toString())) {
                                                break;
                                            } else {
                                                unMatchedOpsFeeCode.put(opsFee.getFeeCode(), opsFeeAmount.toString());
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (unMatchedBaseFare.isEmpty() && unMatchedOpsTaxCode.isEmpty() && unMatchedOpsFeeCode.isEmpty()) {
                        qcStatusValue = true;
                    } else {
                        qcStatusValue = false;
                    }
                }
            }
        }

        return qcStatusValue;
    }
    public Boolean qcCheckForAccoSupplierPriceDetails(OpsBooking opsBooking, OpsProduct opsProduct) {
        Map<String, String> unMatchedOrderSupplierPriceAmount = new HashMap<>();
        Map<String, String> unMatchedOrderTaxesAmount = new HashMap<>();
        Map<String, String> unMatchedOrderSalesTaxAmount = new HashMap<>();
        Map<String, String> unMatchedOrderOtherChargesAmount = new HashMap<>();
        Map<String, String> unMatchedOrderRoomTaxAmount = new HashMap<>();
        Boolean qcStatus = false;
        Element rootElement = consumeAccoSupplierPriceDetails(opsBooking);

        if (rootElement != null) {
            Element[] resWrapperElems = XMLUtils.getElementsAtXPath(rootElement, "./accoi:ResponseBody/acco:OTA_HotelAvailRSWrapper");
            for (Element resWrapperElem : resWrapperElems) {

                String supplierId = XMLUtils.getValueAtXPath(resWrapperElem, "./acco:SupplierID");
                //get SI hotelCityCode and hotelCode
                String hotelCityCode = XMLUtils.getFirstElementAtXPath(resWrapperElem, "./ota:OTA_HotelAvailRS/ota:RoomStays/ota:RoomStay/ota:BasicPropertyInfo").getAttribute("HotelCityCode");
                String hotelCode = XMLUtils.getFirstElementAtXPath(resWrapperElem, "./ota:OTA_HotelAvailRS/ota:RoomStays/ota:RoomStay/ota:BasicPropertyInfo").getAttribute("HotelCode");

                //get ops hotelCode and hotelCityCode
                String opsHotelCityCode = opsProduct.getOrderDetails().getHotelDetails().getCityCode();
                String opsHotelCode = opsProduct.getOrderDetails().getHotelDetails().getHotelCode();

                //checking SI product and opsProduct
                if (opsProduct.getSupplierID().equalsIgnoreCase(supplierId)
                        && opsHotelCityCode.equalsIgnoreCase(hotelCityCode)
                        || opsHotelCode.equalsIgnoreCase(opsHotelCode)) {
                    //if match
                    //SI Calculations
                    //SI Calculated Sum of Supplier Price
                    BigDecimal totalOfAmountAfterTax = new BigDecimal(0);
                    //SI Calculated sum of Taxes Amount
                    BigDecimal totalOfTaxesAmount = new BigDecimal(0);
                    //SI Calculated sum of SalesTaxAmount
                    BigDecimal totalOfSalesTaxAmount = new BigDecimal(0);
                    //SI Calculated sum of OtherChargesAmount
                    BigDecimal totalOfOtherChargesAmount = new BigDecimal(0);
                    //SI Calculated sum of RoomTaxAmount
                    BigDecimal totalOfRoomTaxAmount = new BigDecimal(0);

                    //get SI total sub element
                    Element[] totalResponse = XMLUtils.getElementsAtXPath(resWrapperElem, "./ota:OTA_HotelAvailRS/ota:RoomStays/ota:RoomStay/ota:Total");
                    for (Element aTotal : totalResponse) {
                        BigDecimal amountAfterTax = TypeConverterUtils.convertToBigDecimal(aTotal.getAttribute("AmountAfterTax"), 0);
                        totalOfAmountAfterTax = totalOfAmountAfterTax.add(amountAfterTax);

                        BigDecimal taxesAmount = TypeConverterUtils.convertToBigDecimal(XMLUtils.getFirstElementAtXPath(aTotal, "./ota:Taxes").getAttribute("Amount"), 0);
                        totalOfTaxesAmount = totalOfTaxesAmount.add(taxesAmount);

                        //if check taxType
                        Element[] taxRes = XMLUtils.getElementsAtXPath(aTotal, "./ota:Taxes/ota:Tax");
                        for (Element tax : taxRes) {
                            String taxType = tax.getAttribute("Type");
                            if (taxType.equalsIgnoreCase("salesTax")) {
                                BigDecimal salesTaxAmount = TypeConverterUtils.convertToBigDecimal(XMLUtils.getFirstElementAtXPath(aTotal, "./ota:Taxes/ota:Tax").getAttribute("Amount"), 0);
                                totalOfSalesTaxAmount = totalOfSalesTaxAmount.add(salesTaxAmount);
                            } else if (taxType.equalsIgnoreCase("roomTax")) {
                                BigDecimal roomTaxAmount = TypeConverterUtils.convertToBigDecimal(XMLUtils.getFirstElementAtXPath(aTotal, "./ota:Taxes/ota:Tax").getAttribute("Amount"), 0);
                                totalOfRoomTaxAmount = totalOfRoomTaxAmount.add(roomTaxAmount);
                            } else if (taxType.equalsIgnoreCase("otherCharges")) {
                                BigDecimal otherChargesAmount = TypeConverterUtils.convertToBigDecimal(XMLUtils.getFirstElementAtXPath(aTotal, "./ota:Taxes/ota:Tax").getAttribute("Amount"), 0);
                                totalOfOtherChargesAmount = totalOfOtherChargesAmount.add(otherChargesAmount);
                            }
                        }

                    }

                    //get ops orderSupplierPriceInfo
                    OpsAccoOrderSupplierPriceInfo opsAccoOrderSupplierPriceInfo = opsProduct.getOrderDetails().getHotelDetails().getOpsAccoOrderSupplierPriceInfo();
                    //getting opsSupplierPrice
                    BigDecimal opsSupplierPrice = TypeConverterUtils.convertToBigDecimal(opsAccoOrderSupplierPriceInfo.getSupplierPrice(), 0);

                    //getting opsTaxes amount
                    BigDecimal opsTaxesAmount = TypeConverterUtils.convertToBigDecimal(opsAccoOrderSupplierPriceInfo.getTaxes().getAmount().toString(), 0);

                    if (!opsSupplierPrice.toString().equalsIgnoreCase(totalOfAmountAfterTax.toString())) {
                        unMatchedOrderSupplierPriceAmount.put("orderSupplierPriceAmount", opsSupplierPrice.toString());
                    }
                    if (!opsTaxesAmount.toString().equalsIgnoreCase(totalOfTaxesAmount.toString())) {
                        unMatchedOrderTaxesAmount.put("orderTaxesAmount", opsTaxesAmount.toString());
                    }


                    List<OpsTax> opsTaxList = opsAccoOrderSupplierPriceInfo.getTaxes().getTax();
                    for (OpsTax opsTax : opsTaxList) {
                        if (opsTax.getTaxCode().equalsIgnoreCase("roomTax")) {
                            BigDecimal opsTaxAmount = TypeConverterUtils.convertToBigDecimal(opsTax.getAmount().toString(), 0);
                            if (!opsTaxAmount.toString().equalsIgnoreCase(totalOfRoomTaxAmount.toString())) {
                                //put in array
                                unMatchedOrderRoomTaxAmount.put(opsTax.getTaxCode(), opsTaxAmount.toString());
                            }
                        } else if (opsTax.getTaxCode().equalsIgnoreCase("salesTax")) {
                            BigDecimal opsTaxAmount = TypeConverterUtils.convertToBigDecimal(opsTax.getAmount().toString(), 0);
                            if (!opsTaxAmount.toString().equalsIgnoreCase(totalOfSalesTaxAmount.toString())) {
                                unMatchedOrderSalesTaxAmount.put(opsTax.getTaxCode(), opsTaxAmount.toString());
                            }
                        } else if (opsTax.getTaxCode().equalsIgnoreCase("otherCharges")) {
                            BigDecimal opsTaxAmount = TypeConverterUtils.convertToBigDecimal(opsTax.getAmount().toString(), 0);
                            if (!opsTaxAmount.toString().equalsIgnoreCase(totalOfOtherChargesAmount.toString())) {
                                unMatchedOrderOtherChargesAmount.put(opsTax.getTaxCode(), opsTaxAmount.toString());
                            }
                        }
                    }
                    //checks all and return unmatched data
                    if (unMatchedOrderSupplierPriceAmount.isEmpty()
                            && unMatchedOrderTaxesAmount.isEmpty()
                            && unMatchedOrderSalesTaxAmount.isEmpty()
                            && unMatchedOrderOtherChargesAmount.isEmpty()
                            && unMatchedOrderRoomTaxAmount.isEmpty()) {
                        qcStatus = true;
                    } else
                        qcStatus = false;
                }
            }
        }
        return qcStatus;
    }

    private Element consumeAccoSupplierPriceDetails(OpsBooking opsBooking) {
        Element xmlOfSupplierDetails = null;
        try {
            xmlOfSupplierDetails = bookingEngineElasticData.getXMLData(opsBooking.getUserID(),
                    opsBooking.getTransactionID(),
                    opsBooking.getSessionID(),
                    BEInboundOperation.REPRICE,
                    BEOperationId.SUPPLIER_INTEGRATION_RS,
                    BEServiceUri.HOTEL);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (OperationException e) {
            e.printStackTrace();
        }

        return xmlOfSupplierDetails;
    }

    private Element consumeAirSupplierPriceDetails(OpsBooking opsBooking) {
        Element xmlOfSupplierDetails = null;
        try {
            xmlOfSupplierDetails = bookingEngineElasticData.getXMLData(
                    opsBooking.getUserID(),
                    opsBooking.getTransactionID(),
                    opsBooking.getSessionID(),
                    BEInboundOperation.REPRICE,
                    BEOperationId.SUPPLIER_INTEGRATION_RS,
                    BEServiceUri.FLIGHT);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error while retrieving Supplier Details response from SI" + e);
        }
        return xmlOfSupplierDetails;
    }
}
