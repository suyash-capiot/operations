package com.coxandkings.travel.operations.utils.adapter;

import com.coxandkings.travel.ext.model.be.*;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.service.newsupplierfirstbooking.NewSupplierFirstBookingService;
import com.coxandkings.travel.operations.service.prepaymenttosupplier.loader.PaymentAdviseMDMLoaderService;
import com.coxandkings.travel.operations.utils.CopyUtils;
import com.coxandkings.travel.operations.utils.DateTimeUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OpsAccommodationAdapter {

    @Autowired
    private OpsBookingAdapter parentAdapter;

    @Autowired
    NewSupplierFirstBookingService newSupplierFirstBookingService;

    private static Logger logger = LogManager.getLogger(OpsAccommodationAdapter.class);
    @Autowired
    private PaymentAdviseMDMLoaderService paymentAdviseMDMLoaderService;

    public OpsAccommodationAdapter(@Qualifier("OpsBookingAdapter") OpsBookingAdapter newAdapter) {
        parentAdapter = newAdapter;
    }

    public OpsOrderDetails getOpsHotelOrderDetails(OrderDetails orderDetails, OpsOrderDetails opsOrderDetails, Product product) {

        OpsHotelDetails opsHotelDetails = getOpsHotelDetails(orderDetails.getHotelDetails(), product);

        opsHotelDetails.setOpsAccoOrderSupplierPriceInfo(getOpsAccoOrderSupplierPriceInfo(
                orderDetails.getOrderSupplierPriceInfo()));

        opsHotelDetails.setOpsAccommodationTotalPriceInfo(getOpsAccommodationTotalPriceInfo(
                orderDetails.getOrderTotalPriceInfo()));


        opsHotelDetails.setCredentialsName(product.getCredentialsName());
        //opsHotelDetails.setTicketingPCC( product.getTicketIssueDate() );

        if (product.getExpiryTimeLimit() != null) {
            opsHotelDetails.setTimeLimitExpiryDate(DateTimeUtil.formatBEDateTimeZone(product.getExpiryTimeLimit()));
        }

        opsOrderDetails.setHotelDetails(opsHotelDetails);

        return opsOrderDetails;
    }


    private OpsHotelDetails getOpsHotelDetails(HotelDetails hotelDetails, Product product) {

        boolean prePaymentRequired = false;

        OpsHotelDetails opsHotelDetails = new OpsHotelDetails();

        opsHotelDetails.setRooms(hotelDetails.getRooms().stream().map(room -> getOpsRoom(room)).collect(Collectors.toList()));

        try {
            prePaymentRequired = paymentAdviseMDMLoaderService.getPrePaymentRequiredFlag(product.getSupplierID());
            opsHotelDetails.setPrePaymentRequest(prePaymentRequired);
        } catch (Exception e) {
            logger.error("Not getting prepayment flag for Supplier " + product.getSupplierID());
        }

        opsHotelDetails.setCountryCode(hotelDetails.getCountryCode());

        opsHotelDetails.setCityCode(hotelDetails.getCityCode());

        opsHotelDetails.setHotelCode(hotelDetails.getHotelCode());

        opsHotelDetails.setHotelName(hotelDetails.getHotelName());

        if (StringUtils.isEmpty(opsHotelDetails.getCityName())) {
            opsHotelDetails.setCityName("");
        }
        if (StringUtils.isEmpty(opsHotelDetails.getCountryName())) {
            opsHotelDetails.setCountryName("");
        }

        opsHotelDetails.setAccommodationReferenceNumber(product.getAccoRefNumber());

        opsHotelDetails.setClientReconfirmationDateZDT(DateTimeUtil.formatBEDateTimeZone(product.getClientReconfirmDate()));

        opsHotelDetails.setSupplierReconfirmationDateZDT(DateTimeUtil.formatBEDateTimeZone(product.getSupplierReconfirmDate()));

        opsHotelDetails.setSuppReconfirmationStatus(product.getSuppReconfirmationStatus());

        opsHotelDetails.setClientreconfirmationStatus(product.getClientreconfirmationStatus());

        if (product.getExpiryTimeLimit() != null) {
            opsHotelDetails.setTimeLimitExpiryDate(DateTimeUtil.formatBEDateTimeZone(product.getExpiryTimeLimit()));
        }

        try {
            opsHotelDetails.setFirstReservationCheck(newSupplierFirstBookingService.getFlag(product.getOrderID()));
        }
        catch(Exception e){
            logger.info("Unable to get new supplier flag for order id : " + product.getOrderID());
            e.printStackTrace();
        }


        return opsHotelDetails;
    }

    //modfiy
    private OpsRoom getOpsRoom(Room room) {

        OpsRoom opsRoom = new OpsRoom();

        opsRoom.setRoomSuppPriceInfo(getOpsAccoRoomSupplierPriceInfo(room.getSupplierPriceInfo()));

        opsRoom.setMealInfo(parentAdapter.getOpsMealInfo(room.getMealInfo()));

        opsRoom.setCheckIn(room.getCheckIn());

        opsRoom.setPolicies(getOpsRoomPolicies(room.getPolicies()));

        opsRoom.setOpsClientEntityCommercial(room.getClientEntityCommercials().stream().map(clientCommercial ->
                getOpsClientEntityCommercialRoom(clientCommercial)).collect(Collectors.toList()));

        opsRoom.setRoomTypeInfo(getOpsRoomTypeInfo(room.getRoomTypeInfo()));

        opsRoom.setRatePlanInfo(getOpsRatePlanInfo(room.getRatePlanInfo()));

        opsRoom.setPaxInfo(room.getPaxInfo().stream().map(paxInfo1 -> getOpsAccommodationPaxInfo(paxInfo1)).collect(Collectors.toList()));

        opsRoom.setRoomTotalPriceInfo(getOpsRoomTotalPriceInfo(room.getTotalPriceInfo()));

        opsRoom.setRoomSuppCommercials(room.getSupplierCommercials().stream().map(supplierCommercial ->
                getOpsRoomSupplierCommercial(supplierCommercial)).collect(Collectors.toList()));

        opsRoom.setCheckOut(room.getCheckOut());

        opsRoom.setRoomID(room.getRoomID());

        opsRoom.setSupplierRoomIndex(room.getSupplierRoomIndex());

        if (room.getOccupancyInfo() != null) {
            opsRoom.setOccupancyInfo(room.getOccupancyInfo().stream().
                    map(aOccupancyInfo -> getOccupancyInfo(aOccupancyInfo)).collect(Collectors.toList()));
        }

        if (room.getSupplierRoomIndex() != null) {
            opsRoom.setSupplierRoomIndex(room.getSupplierRoomIndex());
        }

        opsRoom.setRefundableIndicator(false);
        opsRoom.setStatus(room.getStatus());


        if(room.getOfferCodes()!=null){
            List<OpsOfferCodes> opsOfferCodes = new ArrayList<>();
            List<OfferCodes> offerCodes = room.getOfferCodes();
            OpsOfferCodes opsCode = null;
            for(OfferCodes offerCode:offerCodes){
                opsCode = new OpsOfferCodes();
                if(offerCode.getAmount()!=null){
                    opsCode.setAmount(offerCode.getAmount());
                }

                opsCode.setOfferCodes(offerCode.getOfferCodes());
                opsCode.setOfferId(offerCode.getOfferId());
                opsOfferCodes.add(opsCode);
            }

            opsRoom.setOfferCodes(opsOfferCodes);
        }

        if(room.getOfferDetailsSet()!=null){
            List<OfferDetailsSet> offersSet = room.getOfferDetailsSet();
            List<OpsOfferDetailSet> opsOfferDetailSet = new ArrayList<>();
            OpsOfferDetailSet opsOffer = null;
            for(OfferDetailsSet offer:offersSet){
                opsOffer = new OpsOfferDetailSet();
                CopyUtils.copy(offer,opsOffer);
                OpsRedemptionConstruct opsRedemConstruct = new OpsRedemptionConstruct();
                CopyUtils.copy(offer.getRedemptionConstruct(),opsRedemConstruct);
                opsOffer.setOpsRedemptionConstruct(opsRedemConstruct);
                opsOfferDetailSet.add(opsOffer);
            }
            opsRoom.setOfferDetailSet(opsOfferDetailSet);
        }


        return opsRoom;
    }

    private List<OpsPolicy> getOpsRoomPolicies(List<Policy> policies) {
        List<OpsPolicy> opsPolicies = new ArrayList<>();
        if (policies != null && policies.size() > 0) {
            for (Policy policy : policies) {
                OpsPolicy opsPolicy = new OpsPolicy();
                opsPolicy.setPolicyApplicability(getOpsPolicyApplicability(policy.getPolicyApplicability()));
                opsPolicy.setPolicyCharges(getOpsPolicyCharges(policy.getPolicyCharges()));
                opsPolicy.setPolicyType(policy.getPolicyType());

                opsPolicies.add(opsPolicy);

            }
        }
        return opsPolicies;
    }

    private OpsPolicyCharges getOpsPolicyCharges(PolicyCharges policyCharges) {
        OpsPolicyCharges opsPolicyCharges = new OpsPolicyCharges();
        opsPolicyCharges.setChargeType(policyCharges.getChargeType());
        opsPolicyCharges.setChargeValue(policyCharges.getChargeValue());
        opsPolicyCharges.setCurrencyCode(policyCharges.getCurrencyCode());
        return opsPolicyCharges;
    }


    private OpsPolicyApplicability getOpsPolicyApplicability(PolicyApplicability policyApplicability) {
        OpsPolicyApplicability opsPolicyApplicability = new OpsPolicyApplicability();
        opsPolicyApplicability.setFrom(policyApplicability.getFrom());
        opsPolicyApplicability.setTimeUnit(policyApplicability.getTimeUnit());
        opsPolicyApplicability.setTo(policyApplicability.getTo());
        return opsPolicyApplicability;
    }

    private OpsOccupancyInfo getOccupancyInfo(OccupancyInfo occupancyInfo) {

        OpsOccupancyInfo opsOccupancyInfo = new OpsOccupancyInfo();

        opsOccupancyInfo.setMaxAge(occupancyInfo.getMaxAge());

        opsOccupancyInfo.setMinAge(occupancyInfo.getMinAge());

        opsOccupancyInfo.setMaxOccupancy(occupancyInfo.getMaxOccupancy());

        opsOccupancyInfo.setMinOccupancy(occupancyInfo.getMinOccupancy());

        opsOccupancyInfo.setPaxType(occupancyInfo.getPaxType());

        return opsOccupancyInfo;
    }


    public OpsRoomSuppCommercial getOpsRoomSupplierCommercial(SupplierCommercial supplierCommercial) {

        OpsRoomSuppCommercial opsRoomSuppCommercial = new OpsRoomSuppCommercial();

        opsRoomSuppCommercial.setCommercialCalculationAmount(supplierCommercial.getCommercialCalculationAmount());

        opsRoomSuppCommercial.setCommercialFareComponent(supplierCommercial.getCommercialFareComponent());

        opsRoomSuppCommercial.setCommercialCalculationAmount(supplierCommercial.getCommercialCalculationAmount());

        opsRoomSuppCommercial.setCommercialCalculationPercentage(supplierCommercial.getCommercialCalculationPercentage());

        opsRoomSuppCommercial.setCommercialInitialAmount(supplierCommercial.getCommercialInitialAmount());

        opsRoomSuppCommercial.setCommercialTotalAmount(supplierCommercial.getCommercialTotalAmount());

        opsRoomSuppCommercial.setCommercialCurrency(supplierCommercial.getCommercialCurrency());

        opsRoomSuppCommercial.setCommercialType(supplierCommercial.getCommercialType());

        opsRoomSuppCommercial.setCommercialAmount(supplierCommercial.getCommercialAmount());

        opsRoomSuppCommercial.setCommercialName(supplierCommercial.getCommercialName());

        opsRoomSuppCommercial.setMdmRuleID(supplierCommercial.getMdmRuleID());
        opsRoomSuppCommercial.setSupplierID(supplierCommercial.getSupplierID());

        return opsRoomSuppCommercial;
    }

    public OpsClientEntityCommercial getOpsClientEntityCommercialRoom(ClientEntityCommercial aClientEntityCommercial) {

        OpsClientEntityCommercial opsClientEntityCommercial = new OpsClientEntityCommercial();

        opsClientEntityCommercial.setClientID(aClientEntityCommercial.getClientID());

        opsClientEntityCommercial.setOpsPaxRoomClientCommercial(aClientEntityCommercial.getClientCommercials().stream().map(
                aClientCommercial -> getOpsRoomClientCommercial(aClientCommercial)).collect(Collectors.toList()));

        opsClientEntityCommercial.setParentClientID(aClientEntityCommercial.getParentClientID());

        opsClientEntityCommercial.setCommercialEntityType(aClientEntityCommercial.getCommercialEntityType());

        opsClientEntityCommercial.setCommercialEntityID(aClientEntityCommercial.getCommercialEntityID());
        
        opsClientEntityCommercial.setEntityName(aClientEntityCommercial.getEntityName());
        
        opsClientEntityCommercial.setClientMarket(aClientEntityCommercial.getClientMarket());
        
        opsClientEntityCommercial.setCommercialEntityMarket(aClientEntityCommercial.getCommercialEntityMarket());

        return opsClientEntityCommercial;
    }


    private OpsPaxRoomClientCommercial getOpsRoomClientCommercial(ClientCommercial clientCommercial) {

        OpsPaxRoomClientCommercial opsPaxRoomClientCommercial = new OpsPaxRoomClientCommercial();

        opsPaxRoomClientCommercial.setCommercialAmount(Double.parseDouble(clientCommercial.getCommercialAmount()));

        opsPaxRoomClientCommercial.setCommercialCurrency(clientCommercial.getCommercialCurrency());

        opsPaxRoomClientCommercial.setCompanyFlag(clientCommercial.getCompanyFlag());

        opsPaxRoomClientCommercial.setCommercialType(clientCommercial.getCommercialType());

        opsPaxRoomClientCommercial.setCommercialName(clientCommercial.getCommercialName());

        opsPaxRoomClientCommercial.setMdmRuleID(clientCommercial.getMdmRuleID());
        
        opsPaxRoomClientCommercial.setCommercialCalculationAmount(clientCommercial.getCommercialCalculationAmount());
        opsPaxRoomClientCommercial.setCommercialCalculationPercentage(clientCommercial.getCommercialCalculationPercentage());
        opsPaxRoomClientCommercial.setCommercialFareComponent(clientCommercial.getCommercialFareComponent());
        
        opsPaxRoomClientCommercial.setRetentionAmountPercentage(clientCommercial.getRetentionAmountPercentage());
        opsPaxRoomClientCommercial.setRetentionPercentage(clientCommercial.getRetentionPercentage());
        opsPaxRoomClientCommercial.setRemainingAmount(clientCommercial.getRemainingAmount());
        opsPaxRoomClientCommercial.setRemainingPercentageAmount(clientCommercial.getRemainingPercentageAmount());

        //TODO: Booing Engine not providing this flag. Added a place holder
        opsPaxRoomClientCommercial.setCompanyFlag(true);

        return opsPaxRoomClientCommercial;
    }

    private OpsAccoOrderSupplierPriceInfo getOpsAccoOrderSupplierPriceInfo(OrderSupplierPriceInfo orderSupplierPriceInfo) {

        OpsAccoOrderSupplierPriceInfo opsAccoOrderSupplierPriceInfo = new OpsAccoOrderSupplierPriceInfo();

        opsAccoOrderSupplierPriceInfo.setCurrencyCode(orderSupplierPriceInfo.getCurrencyCode());

        opsAccoOrderSupplierPriceInfo.setSupplierPrice(orderSupplierPriceInfo.getSupplierPrice());

        opsAccoOrderSupplierPriceInfo.setTaxes(parentAdapter.getOpsTaxes(orderSupplierPriceInfo.getTaxes()));

        return opsAccoOrderSupplierPriceInfo;

    }

    private OpsRoomSuppPriceInfo getOpsAccoRoomSupplierPriceInfo(SupplierPriceInfo orderSupplierPriceInfo) {

        OpsRoomSuppPriceInfo opsRoomSuppPriceInfo = new OpsRoomSuppPriceInfo();

        opsRoomSuppPriceInfo.setCurrencyCode(orderSupplierPriceInfo.getCurrencyCode());

        opsRoomSuppPriceInfo.setRoomSupplierPrice(orderSupplierPriceInfo.getSupplierPrice());

        opsRoomSuppPriceInfo.setTaxes(parentAdapter.getOpsTaxes(orderSupplierPriceInfo.getTaxes()));

        return opsRoomSuppPriceInfo;
    }

    private OpsRoomTotalPriceInfo getOpsRoomTotalPriceInfo(TotalPriceInfo totalPriceInfo) {
        OpsRoomTotalPriceInfo opsRoomTotalPriceInfo
                = new OpsRoomTotalPriceInfo();

        opsRoomTotalPriceInfo.setCurrencyCode(totalPriceInfo.getCurrencyCode());

        opsRoomTotalPriceInfo.setRoomTotalPrice(totalPriceInfo.getTotalPrice());

        opsRoomTotalPriceInfo.setOpsTaxes(parentAdapter.getOpsTaxes(totalPriceInfo.getTaxes()));
        
        if (opsRoomTotalPriceInfo.getReceivables() != null) {
        	opsRoomTotalPriceInfo.setReceivables(parentAdapter.getOpsReceivables(totalPriceInfo.getReceivables()));
        }
        
        opsRoomTotalPriceInfo.setCompanyTaxes(parentAdapter.getCompanyTaxes(totalPriceInfo.getCompanyTaxes()));
        
        opsRoomTotalPriceInfo.setDiscounts(totalPriceInfo.getDiscounts());
        
        opsRoomTotalPriceInfo.setIncentives(totalPriceInfo.getIncentives());


        return opsRoomTotalPriceInfo;
    }

    public OpsAccommodationTotalPriceInfo getOpsAccommodationTotalPriceInfo(OrderTotalPriceInfo orderTotalPriceInfo) {
        OpsAccommodationTotalPriceInfo opsAccommodationTotalPriceInfo
                = new OpsAccommodationTotalPriceInfo();

        opsAccommodationTotalPriceInfo.setCurrencyCode(orderTotalPriceInfo.getCurrencyCode());

        opsAccommodationTotalPriceInfo.setTotalPrice(orderTotalPriceInfo.getTotalPrice());

        opsAccommodationTotalPriceInfo.setOpsTaxes(parentAdapter.getOpsTaxes(orderTotalPriceInfo.getTaxes()));

        if(orderTotalPriceInfo.getCompanyTaxes()!=null){
            OpsCompanyTaxes opsCompanyTaxes = new OpsCompanyTaxes();
            CopyUtils.copy(orderTotalPriceInfo.getCompanyTaxes(),opsCompanyTaxes);
            opsAccommodationTotalPriceInfo.setCompanyTaxes(opsCompanyTaxes);
        }

        if(orderTotalPriceInfo.getDiscounts()!=null){
            OpsDiscounts opsDiscounts = new OpsDiscounts();
            CopyUtils.copy(orderTotalPriceInfo.getDiscounts(),opsDiscounts);
            opsAccommodationTotalPriceInfo.setDiscounts(opsDiscounts);
        }

        if(orderTotalPriceInfo.getIncentives()!=null) {
            OpsIncentives opsIncentives = new OpsIncentives();
            CopyUtils.copy(orderTotalPriceInfo.getIncentives(), opsIncentives);
            opsAccommodationTotalPriceInfo.setIncentives(opsIncentives);
        }
        return opsAccommodationTotalPriceInfo;
    }

    public OpsAccommodationPaxInfo getOpsAccommodationPaxInfo(PaxInfo paxInfo) {
        OpsAccommodationPaxInfo opsAccommodationPaxInfo = new OpsAccommodationPaxInfo();

        opsAccommodationPaxInfo.setFirstName(paxInfo.getFirstName());

        opsAccommodationPaxInfo.setLastName(paxInfo.getLastName());

        opsAccommodationPaxInfo.setPaxType(paxInfo.getPaxType());

        opsAccommodationPaxInfo.setLeadPax(paxInfo.getIsLeadPax());

        opsAccommodationPaxInfo.setPaxID(paxInfo.getPaxID());

        opsAccommodationPaxInfo.setMiddleName(paxInfo.getMiddleName());

        opsAccommodationPaxInfo.setAddressDetails(parentAdapter.getOpsAddressDetails(paxInfo.getAddressDetails()));

        opsAccommodationPaxInfo.setTitle(paxInfo.getTitle());

        opsAccommodationPaxInfo.setBirthDate(paxInfo.getBirthDate());
        opsAccommodationPaxInfo.setContactDetails(paxInfo.getContactDetails().stream().map(contactDetail
                -> parentAdapter.getOpsContactDetail(contactDetail)).collect(Collectors.toList()));
        
        opsAccommodationPaxInfo.setStatus(paxInfo.getStatus());

        return opsAccommodationPaxInfo;
    }

    public OpsRatePlanInfo getOpsRatePlanInfo(RatePlanInfo ratePlanInfo) {
        OpsRatePlanInfo opsRatePlanInfo = new OpsRatePlanInfo();
        opsRatePlanInfo.setRatePlanname(ratePlanInfo.getRatePlanname());
        opsRatePlanInfo.setRatePlanRef(ratePlanInfo.getRatePlanRef());
        opsRatePlanInfo.setRatePlanCode(ratePlanInfo.getRatePlanCode());
        opsRatePlanInfo.setBookingRef(ratePlanInfo.getBookingRef());
        return opsRatePlanInfo;
    }

    public OpsRoomTypeInfo getOpsRoomTypeInfo(RoomTypeInfo roomTypeInfo) {
        OpsRoomTypeInfo opsRoomTypeInfo = new OpsRoomTypeInfo();
        opsRoomTypeInfo.setRoomTypeCode(roomTypeInfo.getRoomTypeCode());
        opsRoomTypeInfo.setRoomCategoryID(roomTypeInfo.getRoomCategoryID());
        opsRoomTypeInfo.setRoomRef(roomTypeInfo.getRoomRef());
        opsRoomTypeInfo.setRoomTypeName(roomTypeInfo.getRoomTypeName());
        opsRoomTypeInfo.setRoomCategoryName(roomTypeInfo.getRoomCategoryName());
        return opsRoomTypeInfo;
    }
}
