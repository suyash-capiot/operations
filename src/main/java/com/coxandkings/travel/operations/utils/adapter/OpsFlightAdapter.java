package com.coxandkings.travel.operations.utils.adapter;

import com.coxandkings.travel.ext.model.be.*;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.service.newsupplierfirstbooking.NewSupplierFirstBookingService;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.loader.PaymentAdviseMDMLoaderService;
import com.coxandkings.travel.operations.utils.DateTimeUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OpsFlightAdapter {

    private OpsBookingAdapter parentAdapter;

    private static Logger logger = LogManager.getLogger(OpsFlightAdapter.class);

    @Autowired
    private PaymentAdviseMDMLoaderService paymentAdviseMDMLoaderService;

    @Autowired
    private NewSupplierFirstBookingService newSupplierFirstBookingService;

    @Autowired
    private OpsAdapterMdmRequirements opsAdapterMdmRequirements;

    public OpsFlightAdapter(@Qualifier("OpsBookingAdapter") OpsBookingAdapter newAdapter) {
        parentAdapter = newAdapter;
    }


    public OpsOrderDetails getOpsFlightDetails(OrderDetails orderDetails, OpsOrderDetails opsOrderDetails, Product product) {

        boolean prePaymentRequired = false;

        OpsFlightDetails opsFlightDetails = getOpsFlightDetails(orderDetails.getFlightDetails());

        try {
            prePaymentRequired = paymentAdviseMDMLoaderService.getPrePaymentRequiredFlag(product.getSupplierID());
            opsFlightDetails.setPrePaymentRequest(prePaymentRequired);
        } catch (Exception ex) {
            logger.error("Not getting prepayment flag for Supplier " + product.getSupplierID());
        }

        List<FareInfo> fareInfos = orderDetails.getFareInfo();
        if (fareInfos != null && fareInfos.size() > 0) {
            for (FareInfo fareInfo : fareInfos) {
                FareRule fareRule = fareInfo.getFareRule();

                if (fareRule != null) {


                    OpsFareRule opsFareRule = new OpsFareRule();

                    List<SubSection> subSectionList = fareRule.getSubSection();

                    if (subSectionList != null && subSectionList.size() > 0) {
                        List<OpsSubSection> opsSubSectionList = new ArrayList<>();

                        for (SubSection subSection1 : subSectionList) {
                            OpsSubSection opsSubSection = new OpsSubSection();

                            opsSubSection.setParagraph(getOpsParagraph(subSection1.getParagraph()));
                            opsSubSection.setSubTitle(subSection1.getSubTitle());
                            opsSubSectionList.add(opsSubSection);
                        }

                        opsFareRule.setSubSection(opsSubSectionList);

                    }

                    opsOrderDetails.setFareRule(opsFareRule);
                }
            }
        }

        opsFlightDetails.setTicketIssueDate(product.getTicketIssueDate());

        opsFlightDetails.setTicketNumber(product.getTicketNumber());

        opsFlightDetails.setAirlinePNR(orderDetails.getAirlinePNR());

        opsFlightDetails.setTicketPNR(opsFlightDetails.getTicketPNR());

        opsFlightDetails.setTripIndicator(orderDetails.getTripIndicator());

        opsFlightDetails.setTripType(orderDetails.getTripType());

        opsFlightDetails.setCredentialsName(product.getCredentialsName());

        if (product.getExpiryTimeLimit() != null) {
            opsFlightDetails.setTimeLimitExpiryDate(DateTimeUtil.formatBEDateTimeZone(product.getExpiryTimeLimit()));
        }

        OpsFlightSupplierPriceInfo flightSupplierFlightInfo =
                getOpsSupplierPriceInfo(orderDetails.getOrderSupplierPriceInfo());

        opsFlightDetails.setgDSPNR(orderDetails.getGDSPNR());

        opsFlightDetails.setOpsFlightSupplierPriceInfo(flightSupplierFlightInfo);

        OpsFlightTotalPriceInfo opsFlightTotalPriceInfo =
                getOpsTotalPriceInfo(orderDetails.getOrderTotalPriceInfo());

        opsFlightDetails.setTotalPriceInfo(opsFlightTotalPriceInfo);

        opsFlightDetails.setTicketingPCC(orderDetails.getTicketingPCC());

        opsFlightDetails.setBookingPCC(orderDetails.getBookingPCC());

        opsFlightDetails.setPaxInfo(orderDetails.getPaxInfo().stream().map(paxInfo
                -> getOpsFlightPaxInfo(paxInfo)).collect(Collectors.toList()));

        //set
        try {
            opsFlightDetails.setFirstReservationCheck(newSupplierFirstBookingService.getFlag(product.getOrderID()));
        } catch (Exception e) {
            logger.info("Unable to get new supplier flag for order id : " + product.getOrderID());
            e.printStackTrace();
        }
        opsFlightDetails.setSuppReconfirmationStatus(product.getSuppReconfirmationStatus());

        opsFlightDetails.setClientreconfirmationStatus(product.getClientreconfirmationStatus());

        //opsFlightDetails.setClientReconfirmationDateZDT(DateTimeUtil.formatBEDateTimeZone("Current Date"));

        //  opsFlightDetails.setSupplierReconfirmationDateZDT(DateTimeUtil.formatBEDateTimeZone("Current Date"));

        //TODO : Remove this value when BE Provides it
        if (product.getExpiryTimeLimit() != null) {
            opsFlightDetails.setTimeLimitExpiryDate(DateTimeUtil.formatBEDateTimeZone(product.getExpiryTimeLimit()));
        }

        opsOrderDetails.setFlightDetails(opsFlightDetails);

        return opsOrderDetails;
    }

    private OpsParagraph getOpsParagraph(Paragraph paragraph) {
        if (paragraph != null) {
            OpsParagraph opsParagraph = new OpsParagraph();
            opsParagraph.setText(paragraph.getText());

            return opsParagraph;
        }
        return null;
    }


    public OpsFlightPaxInfo getOpsFlightPaxInfo(PaxInfo paxInfo) {

        OpsFlightPaxInfo opsFlightPaxInfo = new OpsFlightPaxInfo();

        opsFlightPaxInfo.setLeadPax(paxInfo.getIsLeadPax());

        opsFlightPaxInfo.setFirstName(paxInfo.getFirstName());

        opsFlightPaxInfo.setLastName(paxInfo.getLastName());

        opsFlightPaxInfo.setPaxType(paxInfo.getPaxType());

        opsFlightPaxInfo.setPassengerID(paxInfo.getPassengerID());

        opsFlightPaxInfo.setMiddleName(paxInfo.getMiddleName());

        opsFlightPaxInfo.setAncillaryServices(parentAdapter.getOpsAncillaryServices(paxInfo.getAncillaryServices()));

        opsFlightPaxInfo.setTitle(paxInfo.getTitle());

        opsFlightPaxInfo.setBirthDate(paxInfo.getBirthDate());

        opsFlightPaxInfo.setContactDetails(paxInfo.getContactDetails().stream().map(contactDetail
                -> parentAdapter.getOpsContactDetail(contactDetail)).collect(Collectors.toList()));

        opsFlightPaxInfo.setStatus(paxInfo.getStatus());

        if (paxInfo.getSpecialRequests() != null) {
            opsFlightPaxInfo.setOpsSpecialRequest(getOpsSpecialRequest(paxInfo.getSpecialRequests()));

        }

        opsFlightPaxInfo.setAddressDetails(parentAdapter.getOpsAddressDetails(paxInfo.getAddressDetails()));

        opsFlightPaxInfo.setTicketNumber(paxInfo.getTicketNumber());

        List<SeatMap> seatMapList = paxInfo.getSeatMap();
        if (seatMapList != null && seatMapList.size() > 0) {
            opsFlightPaxInfo.setSeatMap(getSeatMapList(seatMapList));
        }
        return opsFlightPaxInfo;
    }

    public List<OpsSeatMap> getSeatMapList(List<SeatMap> seatMaps) {
        OpsSeatMap opsSeatMap = null;
        List<OpsSeatMap> opsSeatMapList = new ArrayList<>();
        for (SeatMap seatMap : seatMaps) {
            opsSeatMap = new OpsSeatMap();
            opsSeatMap.setFlightNumber(seatMap.getFlightNumber());
            opsSeatMap.setFlightRefNumberRPHList(seatMap.getFlightRefNumberRPHList());
            List<CabinClass> cabinClasses = seatMap.getCabinClass();
            if (cabinClasses != null && cabinClasses.size() > 0) {
                opsSeatMap.setCabinClass(getOpsCabinClass(cabinClasses));
                opsSeatMapList.add(opsSeatMap);
            }
        }
        return opsSeatMapList;
    }

    public List<OpsCabinClass> getOpsCabinClass(List<CabinClass> cabinClasses) {
        OpsCabinClass opsCabinClass = null;
        List<OpsCabinClass> opsCabinClassArrayList = new ArrayList<>();
        for (CabinClass cabinClass : cabinClasses) {
            opsCabinClass = new OpsCabinClass();
            opsCabinClass.setEndingRow(cabinClass.getEndingRow());
            opsCabinClass.setStartingRow(cabinClass.getStartingRow());
            opsCabinClass.setUpperDeckInd(cabinClass.getUpperDeckInd());
            List<RowInfo> rowInfos = cabinClass.getRowInfo();
            if (rowInfos != null && rowInfos.size() > 0) {
                opsCabinClass.setRowInfo(getOpsRowInfo(rowInfos));
                opsCabinClassArrayList.add(opsCabinClass);
            }

        }
        return opsCabinClassArrayList;
    }

    public List<OpsRowInfo> getOpsRowInfo(List<RowInfo> rowInfos) {
        OpsRowInfo opsRowInfo = null;
        List<OpsRowInfo> opsRowInfoList = new ArrayList<>();
        for (RowInfo rowInfo : rowInfos) {
            opsRowInfo = new OpsRowInfo();
            opsRowInfo.setCabinType(rowInfo.getCabinType());
            opsRowInfo.setRowNumber(rowInfo.getRowNumber());
            opsRowInfo.setSeatInfo(getOpsSeatInfo(rowInfo.getSeatInfo()));
            opsRowInfoList.add(opsRowInfo);
        }
        return opsRowInfoList;
    }

    public List<OpsSeatInfo> getOpsSeatInfo(List<SeatInfo> seatInfos) {
        OpsSeatInfo opsSeatInfo = null;
        List<OpsSeatInfo> opsSeatInfoList = new ArrayList<>();
        for (SeatInfo seatInfo : seatInfos) {
            opsSeatInfo = new OpsSeatInfo();
            opsSeatInfo.setAvailableInd(seatInfo.getAvailableInd());
            opsSeatInfo.setBulkheadInd(seatInfo.getBulkheadInd());
            opsSeatInfo.setExitRowInd(seatInfo.getExitRowInd());
            opsSeatInfo.setFeatures(getOpsFeatures(seatInfo.getFeatures()));
            opsSeatInfo.setGallyInd(seatInfo.getGallyInd());
            opsSeatInfo.setOccupiedInd(seatInfo.getOccupiedInd());
            opsSeatInfo.setSeatNumber(seatInfo.getSeatNumber());
            opsSeatInfo.setSeatPreference(seatInfo.getSeatPreference());
            opsSeatInfo.setSeatSequenceNumber(seatInfo.getSeatSequenceNumber());
            opsSeatInfo.setServiceFees(getOpsServiceFees(seatInfo.getServiceFees()));
            opsSeatInfoList.add(opsSeatInfo);
        }
        return opsSeatInfoList;
    }

    public List<OpsFeatures> getOpsFeatures(List<Features> features) {
        OpsFeatures opsFeatures = null;
        List<OpsFeatures> opsFeaturesList = new ArrayList<>();
        for (Features feature : features) {

        }
        return null;
    }

    public List<OpsServiceFees> getOpsServiceFees(List<ServiceFees> serviceFeesList) {
        OpsServiceFees opsServiceFees = null;
        List<OpsServiceFees> opsServiceFeesList = new ArrayList<>();
        for (ServiceFees serviceFees : serviceFeesList) {
            opsServiceFees = new OpsServiceFees();
            opsServiceFees.setAmount(serviceFees.getAmount());
            opsServiceFees.setCode(serviceFees.getCode());
            opsServiceFees.setCurrencyCode(serviceFees.getCurrencyCode());
            opsServiceFees.setType(serviceFees.getType());
            opsServiceFees.setTaxes(getOpsTaxes(serviceFees.getTaxes()));
            opsServiceFeesList.add(opsServiceFees);
        }
        return opsServiceFeesList;
    }

    public OpsTaxes getOpsTaxes(Taxes taxes) {
        OpsTaxes opsTaxes = new OpsTaxes();
        opsTaxes.setAmount(taxes.getAmount());
        opsTaxes.setCurrencyCode(taxes.getCurrencyCode());
        opsTaxes.setTax(getOpsTaxList(taxes.getTax()));
        return opsTaxes;
    }

    public List<OpsTax> getOpsTaxList(List<Tax> taxList) {
        OpsTax opsTax = null;
        List<OpsTax> opsTaxList = new ArrayList<>();
        for (Tax tax : taxList) {
            opsTax = new OpsTax();
            opsTax.setAmount(tax.getAmount());
            opsTax.setCurrencyCode(tax.getCurrencyCode());
            opsTax.setTaxCode(tax.getTaxCode());
            opsTaxList.add(opsTax);
        }
        return opsTaxList;
    }

    public OpsSpecialRequest getOpsSpecialRequest(SpecialRequest specialRequest) {
        OpsSpecialRequest opsSpecialRequest = new OpsSpecialRequest();
        opsSpecialRequest.setMealRequestInfo(new ArrayList<>());
        opsSpecialRequest.setSpecialRequestInfo(new ArrayList<>());

        specialRequest.getSpecialRequestInfo().stream()
                .forEach(aSpecialRequestInfo -> {
                            if (aSpecialRequestInfo.getSsrCode().endsWith("ML")) {
                                opsSpecialRequest.getMealRequestInfo().add(getOpsSpecialRequestInfo(aSpecialRequestInfo));
                            } else {
                                opsSpecialRequest.getSpecialRequestInfo().add(getOpsSpecialRequestInfo(aSpecialRequestInfo));
                            }
                        }
                );

        return opsSpecialRequest;
    }

    public OpsSpecialRequestInfo getOpsSpecialRequestInfo(SpecialRequestInfo specialRequestInfo) {

        OpsSpecialRequestInfo opsSpecialRequestInfo = new OpsSpecialRequestInfo();

        opsSpecialRequestInfo.setDate(specialRequestInfo.getDate());

        opsSpecialRequestInfo.setAmount(specialRequestInfo.getAmount());

        opsSpecialRequestInfo.setFlightRefNumberRphList(specialRequestInfo.getFlightRefNumberRphList());

        opsSpecialRequestInfo.setServiceQuantity(specialRequestInfo.getServiceQuantity());

        opsSpecialRequestInfo.setDestinationLocation(specialRequestInfo.getDestinationLocation());

        opsSpecialRequestInfo.setType(specialRequestInfo.getType());

        opsSpecialRequestInfo.setOriginLocation(specialRequestInfo.getOriginLocation());

        opsSpecialRequestInfo.setSsrCode(specialRequestInfo.getSsrCode());

        opsSpecialRequestInfo.setFlightNumber(specialRequestInfo.getFlightNumber());

        opsSpecialRequestInfo.setNumber(specialRequestInfo.getNumber());

        opsSpecialRequestInfo.setDescription(specialRequestInfo.getDescription());

        opsSpecialRequestInfo.setAirlineCode(specialRequestInfo.getAirlineCode());

        opsSpecialRequestInfo.setCategoryCode(specialRequestInfo.getCategoryCode());

        if (specialRequestInfo.getServicePrice() != null) {
            opsSpecialRequestInfo.setServicePrice(getOpsServicePrice(specialRequestInfo.getServicePrice()));
        }

        opsSpecialRequestInfo.setCompanyShortName(specialRequestInfo.getCompanyShortName());

        if (specialRequestInfo.getTaxes() != null) {
            opsSpecialRequestInfo.setTaxes(getOpsServiceTaxes(specialRequestInfo.getTaxes()));
        }

        opsSpecialRequestInfo.setCurrencyCode(specialRequestInfo.getCurrencyCode());

        opsSpecialRequestInfo.setStatus(specialRequestInfo.getStatus());

        return opsSpecialRequestInfo;
    }

    private OpsSpecialServiceTaxes getOpsServiceTaxes(SpecialServiceTaxes taxes) {

        OpsSpecialServiceTaxes opsSpecialServiceTaxes = new OpsSpecialServiceTaxes();

        opsSpecialServiceTaxes.setAmount(taxes.getAmount());

        return opsSpecialServiceTaxes;
    }

    private OpsServicePrice getOpsServicePrice(ServicePrice servicePrice) {

        OpsServicePrice opsServicePrice = new OpsServicePrice();

        opsServicePrice.setBasePrice(servicePrice.getBasePrice());

        return opsServicePrice;
    }


    private OpsFlightTotalPriceInfo getOpsTotalPriceInfo(OrderTotalPriceInfo orderTotalPriceInfo) {

        OpsFlightTotalPriceInfo opsFlightTotalPriceInfo = new OpsFlightTotalPriceInfo();

        opsFlightTotalPriceInfo.setBaseFare(getOpsPaxTypeBaseFare(orderTotalPriceInfo.getBaseFare()));

        opsFlightTotalPriceInfo.setFees(getFlightFees(orderTotalPriceInfo.getFees()));

        opsFlightTotalPriceInfo.setTotalPrice(orderTotalPriceInfo.getTotalPrice());

        if (orderTotalPriceInfo.getReceivables() != null) {
            opsFlightTotalPriceInfo.setReceivables(parentAdapter.getOpsReceivables(orderTotalPriceInfo.getReceivables()));
        }

        opsFlightTotalPriceInfo.setTaxes(parentAdapter.getOpsTaxes(orderTotalPriceInfo.getTaxes()));

        opsFlightTotalPriceInfo.setCompanyTaxes(parentAdapter.getCompanyTaxes(orderTotalPriceInfo.getCompanyTaxes()));

        opsFlightTotalPriceInfo.setCurrencyCode(orderTotalPriceInfo.getCurrencyCode());

        opsFlightTotalPriceInfo.setPaxTypeFares(orderTotalPriceInfo.getPaxTypeFares().stream().map(paxTypeFare
                -> getOpsPaxTypeFareClient(paxTypeFare)).collect(Collectors.toList()));

        if (orderTotalPriceInfo.getDiscounts() != null)
            opsFlightTotalPriceInfo.setDiscounts(orderTotalPriceInfo.getDiscounts());
        
        opsFlightTotalPriceInfo.setIncentives(orderTotalPriceInfo.getIncentives());

        if(orderTotalPriceInfo.getSpecialServiceRequests() !=null)
            opsFlightTotalPriceInfo.setSpecialServiceRequests(orderTotalPriceInfo.getSpecialServiceRequests());


        return opsFlightTotalPriceInfo;
    }

   

    private OpsPaxTypeFareFlightClient getOpsPaxTypeFareClient(PaxTypeFare paxTypeFare) {

        OpsPaxTypeFareFlightClient opsPaxTypeFareFlightClient = new OpsPaxTypeFareFlightClient();

        opsPaxTypeFareFlightClient.setPaxType(paxTypeFare.getPaxType());

        opsPaxTypeFareFlightClient.setBaseFare(getOpsPaxTypeBaseFare(paxTypeFare.getBaseFare()));

        opsPaxTypeFareFlightClient.setFees(getFlightFees(paxTypeFare.getFees()));

        opsPaxTypeFareFlightClient.setTotalFare(getFlightTotalFare(paxTypeFare.getTotalFare()));

        opsPaxTypeFareFlightClient.setOpsClientEntityCommercial(paxTypeFare.getClientEntityCommercials().
                stream().map(aClientEntityCommercial -> getOpsClientEntityCommercialPax(aClientEntityCommercial)).
                collect(Collectors.toList()));

        opsPaxTypeFareFlightClient.setTaxes(parentAdapter.getOpsTaxes(paxTypeFare.getTaxes()));

        return opsPaxTypeFareFlightClient;
    }

    private OpsClientEntityCommercial getOpsClientEntityCommercialPax(ClientEntityCommercial aClientEntityCommercial) {

        OpsClientEntityCommercial opsClientEntityCommercial = new OpsClientEntityCommercial();

        opsClientEntityCommercial.setClientID(aClientEntityCommercial.getClientID());

        opsClientEntityCommercial.setOpsPaxRoomClientCommercial(aClientEntityCommercial.getClientCommercials().stream().map(
                aClientCommercial -> getFlightClientCommercials(aClientCommercial)).collect(Collectors.toList()));

        opsClientEntityCommercial.setParentClientID(aClientEntityCommercial.getParentClientID());

        opsClientEntityCommercial.setCommercialEntityType(aClientEntityCommercial.getCommercialEntityType());

        opsClientEntityCommercial.setCommercialEntityID(aClientEntityCommercial.getCommercialEntityID());

        opsClientEntityCommercial.setEntityName(aClientEntityCommercial.getEntityName());

        opsClientEntityCommercial.setClientMarket(aClientEntityCommercial.getClientMarket());

        opsClientEntityCommercial.setCommercialEntityMarket(aClientEntityCommercial.getCommercialEntityMarket());

        return opsClientEntityCommercial;
    }

    private OpsPaxRoomClientCommercial getFlightClientCommercials(ClientCommercial clientCommercial) {

        OpsPaxRoomClientCommercial opsFlightPaxClientCommercial = new OpsPaxRoomClientCommercial();

        opsFlightPaxClientCommercial.setCommercialCurrency(clientCommercial.getCommercialCurrency());

        opsFlightPaxClientCommercial.setCommercialType(clientCommercial.getCommercialType());

        opsFlightPaxClientCommercial.setCommercialAmount(Double.parseDouble(clientCommercial.getCommercialAmount()));

        opsFlightPaxClientCommercial.setCommercialName(clientCommercial.getCommercialName());

        opsFlightPaxClientCommercial.setMdmRuleID(clientCommercial.getMdmRuleID());

        opsFlightPaxClientCommercial.setCommercialCalculationAmount(clientCommercial.getCommercialCalculationAmount());
        opsFlightPaxClientCommercial.setCommercialCalculationPercentage(clientCommercial.getCommercialCalculationPercentage());
        opsFlightPaxClientCommercial.setCommercialFareComponent(clientCommercial.getCommercialFareComponent());

        opsFlightPaxClientCommercial.setRetentionAmountPercentage(clientCommercial.getRetentionAmountPercentage());
        opsFlightPaxClientCommercial.setRetentionPercentage(clientCommercial.getRetentionPercentage());
        opsFlightPaxClientCommercial.setRemainingAmount(clientCommercial.getRemainingAmount());
        opsFlightPaxClientCommercial.setRemainingPercentageAmount(clientCommercial.getRemainingPercentageAmount());

        //TODO: Booing Engine not providing this flag. Added a place holder
        opsFlightPaxClientCommercial.setCompanyFlag(true);

        return opsFlightPaxClientCommercial;
    }

    private OpsFees getFlightFees(Fees fees) {

        OpsFees opsFees = new OpsFees();
        if (fees != null && fees.getFee() != null && fees.getFee().size() > 0) {
        	if(fees.getAmount()!=null) {
        		opsFees.setTotal(fees.getAmount());
        	}
        	else {
        		opsFees.setTotal(fees.getTotal());
        	}

            opsFees.setFee(fees.getFee().stream().map(aFee -> getFlightFee(aFee)).collect(Collectors.toList()));

            opsFees.setCurrencyCode(fees.getCurrencyCode());
        }

        return opsFees;
    }

    private OpsFee getFlightFee(Fee fee) {

        OpsFee opsFee = new OpsFee();

        opsFee.setCurrencyCode(fee.getCurrencyCode());

        opsFee.setAmount(fee.getAmount());

        opsFee.setFeeCode(fee.getFeeCode());

        return opsFee;
    }

    private OpsTotalFare getFlightTotalFare(TotalFare totalFare) {

        OpsTotalFare opsTotalFare = new OpsTotalFare();

        opsTotalFare.setCurrencyCode(totalFare.getCurrencyCode());

        opsTotalFare.setAmount(Double.valueOf(totalFare.getAmount()));

        return opsTotalFare;
    }

    private OpsBaseFare getOpsPaxTypeBaseFare(BaseFare baseFare) {

        OpsBaseFare opsBaseFare = new OpsBaseFare();

        opsBaseFare.setCurrencyCode(baseFare.getCurrencyCode());

        opsBaseFare.setAmount(baseFare.getAmount());

        return opsBaseFare;
    }

    private OpsFlightDetails getOpsFlightDetails(FlightDetails flightDetails) {

        OpsFlightDetails opsFlightDetails = new OpsFlightDetails();

        opsFlightDetails.setOriginDestinationOptions(
                flightDetails.getOriginDestinationOptions().stream().map(originDestinationOption
                        -> getOpsOriginDestinationOption(originDestinationOption)).collect(Collectors.toList()));

        return opsFlightDetails;
    }

    private OpsOriginDestinationOption getOpsOriginDestinationOption(OriginDestinationOption originDestinationOption) {

        OpsOriginDestinationOption opsOriginDestinationOption = new OpsOriginDestinationOption();

        opsOriginDestinationOption.setFlightSegment(originDestinationOption.getFlightSegment().stream().map(
                flightSegment1 -> getOpsFlightSegment(flightSegment1)).collect(Collectors.toList()));

        return opsOriginDestinationOption;
    }

    private OpsFlightSegment getOpsFlightSegment(FlightSegment flightSegment) {

        OpsFlightSegment opsFlightSegment = new OpsFlightSegment();

        opsFlightSegment.setMarketingAirline(getOpsMarketingAirline(flightSegment.getMarketingAirline()));

        opsFlightSegment.setExtendedRPH(flightSegment.getExtendedRPH());

        opsFlightSegment.setAvailableCount(flightSegment.getAvailableCount());

        opsFlightSegment.setStatus(flightSegment.getStatus());

        opsFlightSegment.setDepartureTerminal(flightSegment.getDepartureTerminal());

        opsFlightSegment.setJourneyDuration(flightSegment.getJourneyDuration().doubleValue());

        opsFlightSegment.setDestinationLocation(flightSegment.getDestinationLocation());

        opsFlightSegment.setrPH(flightSegment.getRph());

        opsFlightSegment.setCabinType(flightSegment.getCabinType());

        opsFlightSegment.setOriginLocation(flightSegment.getOriginLocation());

        opsFlightSegment.setQuoteID(flightSegment.getQuoteID());

        opsFlightSegment.setResBookDesigCode(flightSegment.getResBookDesigCode());

        //opsFlightSegment.setDepartureDateZDT(flightSegment.getDepartureDate());

        opsFlightSegment.setRefundableIndicator(flightSegment.getRefundableIndicator());

        opsFlightSegment.setArrivalDateZDT(DateTimeUtil.formatBEDateTimeZone(flightSegment.getArrivalDate()));

        opsFlightSegment.setDepartureDateZDT(DateTimeUtil.formatBEDateTimeZone(flightSegment.getDepartureDate()));

        opsFlightSegment.setConnectionType(flightSegment.getConnectionType());

        if(flightSegment.getFareInfo()!=null) {
            List<OpsFareInfo_> opsFareInfo_list = new ArrayList<>();
            for (FareInfo_ fareInfo : flightSegment.getFareInfo()) {
                OpsFareInfo_ opsFareInfo = new OpsFareInfo_();
                opsFareInfo.setFareBasisCode(fareInfo.getFareBasisCode());
                opsFareInfo.setFareReference(fareInfo.getFareReference());
                opsFareInfo.setPaxType(fareInfo.getPaxType());
                opsFareInfo_list.add(opsFareInfo);
            }
            opsFlightSegment.setFareInfo(opsFareInfo_list);
        }

/* opsFlightSegment.setArrivalDateZDT(flightSegment.getArrivalDate());

        opsFlightSegment.setDepartureDateZDT(flightSegment.getArrivalDate());*/
        opsFlightSegment.setArrivalTerminal(flightSegment.getArrivalTerminal());

        opsFlightSegment.setOperatingAirline(getOpsOperatingAirline(flightSegment.getOperatingAirline()));

        return opsFlightSegment;
    }

    private OpsMarketingAirline getOpsMarketingAirline(MarketingAirline marketingAirline) {

        OpsMarketingAirline opsAirline = new OpsMarketingAirline();

        opsAirline.setAirlineCode(marketingAirline.getAirlineCode());

        opsAirline.setFlightNumber(marketingAirline.getFlightNumber());

        opsAirline.setAirlineName(marketingAirline.getAirlineName());

        /*opsAirline.setAirlineName(opsAdapterMdmRequirements.getAirlineNameFromAirlineCode(
                marketingAirline.getAirlineCode()));*/

        return opsAirline;
    }

    private OpsOperatingAirline getOpsOperatingAirline(OperatingAirline operatingAirline) {

        OpsOperatingAirline opsAirline = new OpsOperatingAirline();

        opsAirline.setAirlineCode(operatingAirline.getAirlineCode());

        opsAirline.setFlightNumber(operatingAirline.getFlightNumber());

        opsAirline.setAirlineName(operatingAirline.getAirlineName());

       /* opsAirline.setAirlineName(opsAdapterMdmRequirements.getAirlineNameFromAirlineCode(
                operatingAirline.getAirlineCode()));*/

        return opsAirline;
    }

    public OpsFlightSupplierPriceInfo getOpsSupplierPriceInfo(OrderSupplierPriceInfo orderSupplierPriceInfo) {
        OpsFlightSupplierPriceInfo opsFlightSupplierPriceInfo = new OpsFlightSupplierPriceInfo();
        //TODO : Inform BE Supplier price should not be String

        if(orderSupplierPriceInfo!=null) {
            opsFlightSupplierPriceInfo.setSupplierPrice(orderSupplierPriceInfo.getSupplierPrice());

            opsFlightSupplierPriceInfo.setCurrencyCode(orderSupplierPriceInfo.getCurrencyCode());

            opsFlightSupplierPriceInfo.setPaxTypeFares(orderSupplierPriceInfo.getPaxTypeFares().stream().map(
                    paxTypeFare -> getOpsPaxTypeFareSupplier(paxTypeFare)).collect(Collectors.toList()));
        }
        return opsFlightSupplierPriceInfo;
    }

    private OpsPaxTypeFareFlightSupplier getOpsPaxTypeFareSupplier(PaxTypeFare paxTypeFare) {
        OpsPaxTypeFareFlightSupplier opsPaxTypeFareFlightSupplier = new OpsPaxTypeFareFlightSupplier();

        opsPaxTypeFareFlightSupplier.setTotalFare(getFlightTotalFare(paxTypeFare.getTotalFare()));

        opsPaxTypeFareFlightSupplier.setPaxType(paxTypeFare.getPaxType());

        opsPaxTypeFareFlightSupplier.setBaseFare(getOpsPaxTypeBaseFare(paxTypeFare.getBaseFare()));

        opsPaxTypeFareFlightSupplier.setFees(getFlightFees(paxTypeFare.getFees()));

        opsPaxTypeFareFlightSupplier.setTotalFare(getFlightTotalFare(paxTypeFare.getTotalFare()));

        opsPaxTypeFareFlightSupplier.setSupplierCommercials(paxTypeFare.getSupplierCommercials().stream().map(
                aSupplierCommercial -> getOpsPaxTypeSupplierCommercials(aSupplierCommercial)).collect(Collectors.toList()));

        opsPaxTypeFareFlightSupplier.setTaxes(parentAdapter.getOpsTaxes(paxTypeFare.getTaxes()));

        return opsPaxTypeFareFlightSupplier;
    }

    private OpsFlightPaxSupplierCommercial getOpsPaxTypeSupplierCommercials(SupplierCommercial aSupplierCommercial) {

        OpsFlightPaxSupplierCommercial opsFlightPaxSupplierCommercial = new OpsFlightPaxSupplierCommercial();
        //TODO: Convert Object to string

        opsFlightPaxSupplierCommercial.setCommercialCurrency((String) aSupplierCommercial.getCommercialCurrency());

        opsFlightPaxSupplierCommercial.setCommercialAmount(aSupplierCommercial.getCommercialAmount());

        opsFlightPaxSupplierCommercial.setCommercialType(aSupplierCommercial.getCommercialType());

        opsFlightPaxSupplierCommercial.setCommercialName(aSupplierCommercial.getCommercialName());
        opsFlightPaxSupplierCommercial.setMdmRuleID(aSupplierCommercial.getMdmRuleID());

        opsFlightPaxSupplierCommercial.setCommercialCalculationAmount(aSupplierCommercial.getCommercialCalculationAmount());
        opsFlightPaxSupplierCommercial.setCommercialCalculationPercentage(aSupplierCommercial.getCommercialCalculationPercentage());
        opsFlightPaxSupplierCommercial.setCommercialFareComponent(aSupplierCommercial.getCommercialFareComponent());
        opsFlightPaxSupplierCommercial.setSupplierID(aSupplierCommercial.getSupplierID());
        return opsFlightPaxSupplierCommercial;
    }
}
