package com.coxandkings.travel.operations.utils.adapter;

import com.coxandkings.travel.ext.model.be.*;
import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.model.core.*;
import com.coxandkings.travel.operations.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class OpsHolidaysAdapter {

    @Autowired
    private OpsBookingAdapter parentAdapter;

    @Autowired
    private OpsAccommodationAdapter accommodationAdapter;

    public OpsHolidaysAdapter(@Qualifier("OpsBookingAdapter") OpsBookingAdapter newAdapter) {
        parentAdapter = newAdapter;
    }

    public OpsOrderDetails getOpsHolidaysDetails(OrderDetails orderDetails, OpsOrderDetails opsOrderDetails) {

        OpsHolidaysDetails opsHolidaysDetails = getOpsHolidaysDetails(orderDetails.getHolidaysDetails());

        opsOrderDetails.setPackageDetails(opsHolidaysDetails);

        System.out.println();
        System.out.println("DONE");

        return opsOrderDetails;
    }

    private OpsHolidaysDetails getOpsHolidaysDetails(HolidaysDetails holidaysDetails) {

        OpsHolidaysDetails opsHolidaysDetails = new OpsHolidaysDetails();

        opsHolidaysDetails.setTourStartDate(holidaysDetails.getTourStart());
        opsHolidaysDetails.setTourEndDate(holidaysDetails.getTourEnd());
        opsHolidaysDetails.setAmendmentDateTime(holidaysDetails.getAmendDate());
        opsHolidaysDetails.setTravelStartDate(holidaysDetails.getTravelStartDate());
        opsHolidaysDetails.setTravelEndDate(holidaysDetails.getTravelEndDate());
        opsHolidaysDetails.setIsMultiCcyBooking(holidaysDetails.getMultiCcyBooking());


        //Setting Tour Details
        OpsHolidaysTourDetails opsHolidaysTourDetails = new OpsHolidaysTourDetails();

        opsHolidaysTourDetails.setTitle(holidaysDetails.getTourName());
        opsHolidaysTourDetails.setNoOfNights(holidaysDetails.getNoOfNights());
        opsHolidaysTourDetails.setDestination(holidaysDetails.getDestination());

        opsHolidaysDetails.setTourDetails(opsHolidaysTourDetails);
        opsHolidaysDetails.setRoe(holidaysDetails.getRoe());
        opsHolidaysDetails.setBookingStatus(holidaysDetails.getStatus());

        //Setting Product Details List
        List<OpsHolidaysProductOrderDetails> opsProductDetailsList = new ArrayList<OpsHolidaysProductOrderDetails>();

        OpsHolidaysProductOrderDetails opsHolidaysProductOrderDetails = new OpsHolidaysProductOrderDetails();
        opsHolidaysProductOrderDetails.setProductName(holidaysDetails.getAccomodationDetails().get(0).getAccomodationType());

        //Setting Holidays Summary
        List<OpsHolidaySummary> opsHolidaySummaryList = new ArrayList<OpsHolidaySummary>();

        //ACCOMMODATION  - Product Details & Holiday Summary
        OpsHolidaySummary opsHolidaySummary = new OpsHolidaySummary();
        opsHolidaySummary.setTravelType(holidaysDetails.getAccomodationDetails().get(0).getAccomodationType());

        List<OpsHolidaysTravelTypeDetails> opsHolidaysTravelTypeDetailsList = new ArrayList<OpsHolidaysTravelTypeDetails>();

        //New Product Details Change -----XX--------
        OpsProduct opsAccoProduct = getOpsHolidayComponentProductInfo(holidaysDetails, holidaysDetails.getComponentPricing().getAccomodationDetails().getOrderID(), "Accommodation", "Hotel");

        OpsOrderDetails opsAccoOrderDetails = getOpsHolidayComponentOrderInfo(holidaysDetails);

        opsAccoOrderDetails = getOpsHolidayAccoOrderInfo(holidaysDetails, opsAccoOrderDetails);

        List<OpsProduct> opsAccoProductList = new ArrayList<OpsProduct>();
        List<OpsOrderDetails> opsAccoOrderList = new ArrayList<OpsOrderDetails>();
        List<OpsRoom> accoRoomList = new ArrayList<OpsRoom>();
        //-------XX-------

        for (int i = 0; i < holidaysDetails.getAccomodationDetails().size(); i++) {
            HolidaysAccommodationDetails holidaysAccommodationDetails = holidaysDetails.getAccomodationDetails().get(i);

            OpsHolidaysTravelTypeDetails opsHolidaysTravelTypeDetails = new OpsHolidaysTravelTypeDetails();

            opsHolidaysTravelTypeDetails.setName(holidaysAccommodationDetails.getRoomInfo().getRoomTypeInfo().getRoomTypeCode());

            String roomDetails = String.format("%s%s%s%s%s%s", "Room Details: ", holidaysAccommodationDetails.getRoomInfo().getRoomTypeInfo().getRoomCategoryID(), " ", holidaysAccommodationDetails.getRoomInfo().getRoomTypeInfo().getRoomTypeCode(), " ", holidaysAccommodationDetails.getRoomInfo().getRoomTypeInfo().getCabinnumber());

            //String HotelDetails =  String.format("%s%s%s%s%s%s%s%s", "Hotel Details: ", holidaysAccommodationDetails.getRoomInfo().getHotelInfo().getHotelCode(), " ", holidaysAccommodationDetails.getRoomInfo().getHotelInfo().getHotelName()," ",holidaysAccommodationDetails.getRoomInfo().getHotelInfo().getHotelSegmentCategoryCode()," ",holidaysAccommodationDetails.getRoomInfo().getHotelInfo().getHotelRef());

            opsHolidaysTravelTypeDetails.setDetails(roomDetails);

            opsHolidaysTravelTypeDetailsList.add(opsHolidaysTravelTypeDetails);

            //New Product Details Change -----XX--------
            //Setting product details
            OpsRoom opsRoom = getOpsHolidayAccoRoomOrderInfo(holidaysAccommodationDetails);

            accoRoomList.add(opsRoom);
            //---------XX-------
        }
        opsHolidaySummary.setTravelTypeDetails(opsHolidaysTravelTypeDetailsList);
        opsHolidaySummaryList.add(opsHolidaySummary);

        //New Product Details Change -----XX--------
        //Setting product Details values
        OpsHotelDetails opsHotelDetails = new OpsHotelDetails();

        opsHotelDetails = opsAccoOrderDetails.getHotelDetails();
        opsHotelDetails.setRooms(accoRoomList);

        //opsAccoOrderDetails.setHotelDetails(opsHotelDetails);
        //opsAccoOrderList.add(opsAccoOrderDetails);
        //opsAccoProduct.setOrderDetails(opsAccoOrderDetails);
        //opsAccoProductList.add(opsAccoProduct);
        opsHolidaysProductOrderDetails.setOpsHotelDetails(opsHotelDetails);
        opsProductDetailsList.add(opsHolidaysProductOrderDetails);
        //---------XX-------

        //TRANSFERS - Product Details & Holiday Summary
        if (holidaysDetails.getTransferDetails() != null && holidaysDetails.getTransferDetails().size() > 0) {
            //Setting Product Details values
            OpsHolidaysProductOrderDetails opsHolidaysPODetails = new OpsHolidaysProductOrderDetails();
            opsHolidaysPODetails.setProductName("Transfer");

            OpsHolidaySummary opsHolidayComponentSummary = new OpsHolidaySummary();
            opsHolidayComponentSummary.setTravelType("Transfer");

            List<OpsHolidaysTravelTypeDetails> opsHolidaysTTDList = new ArrayList<OpsHolidaysTravelTypeDetails>();

            //New Product Details Change -----XX--------
            List<OpsProduct> opsTransferProductList = new ArrayList<OpsProduct>();
            List<OpsOrderDetails> opsTransferOrderList = new ArrayList<OpsOrderDetails>();
            List<OpsTransferDetails> opsTransferDetailsList = new ArrayList<OpsTransferDetails>();
            //-------XX-------

            for (int i = 0; i < holidaysDetails.getTransferDetails().size(); i++) {
                HolidaysTransferDetails holidaysTransferDetails = holidaysDetails.getTransferDetails().get(i);

                OpsHolidaysTravelTypeDetails opsHolidaysTravelTypeDetails = new OpsHolidaysTravelTypeDetails();

                opsHolidaysTravelTypeDetails.setName(holidaysTransferDetails.getTransferInfo().getName());

                String AirportName = String.format("%s%s", " Airport Name: ", holidaysTransferDetails.getTransferInfo().getAirportName());

                String PickUpLocation = String.format("%s%s", " PickUp Location: ", holidaysTransferDetails.getTransferInfo().getPickUpLocation());

                opsHolidaysTravelTypeDetails.setDetails(AirportName.concat(PickUpLocation));

                opsHolidaysTTDList.add(opsHolidaysTravelTypeDetails);

                //New Product Details Change -----XX--------
                OpsProduct opsTransferProduct = getOpsHolidayComponentProductInfo(holidaysDetails, holidaysTransferDetails.getTransferId(), "Transportation", "Transfer");

                OpsOrderDetails opsTransferOrderDetails = getOpsHolidayComponentOrderInfo(holidaysDetails);

                opsTransferOrderDetails = getOpsHolidayTransferOrderInfo(holidaysDetails, opsTransferOrderDetails, holidaysTransferDetails);

          /*opsTransferProduct.setOrderDetails(opsTransferOrderDetails);
          opsTransferProductList.add(opsTransferProduct);
          opsTransferOrderList.add(opsTransferOrderDetails);*/

                opsTransferDetailsList.add(opsTransferOrderDetails.getTransferDetails());
                //-------XX-------
            }

            opsHolidayComponentSummary.setTravelTypeDetails(opsHolidaysTTDList);
            opsHolidaySummaryList.add(opsHolidayComponentSummary);

            //opsHolidaysPODetails.setProductDetails(opsTransferProductList);
            //opsHolidaysPODetails.setProductDetails(opsTransferOrderList);
            opsHolidaysPODetails.setTransferDetails(opsTransferDetailsList);
            opsProductDetailsList.add(opsHolidaysPODetails);
        }

        //INSURANCE - Product Details & Holiday Summary
        if (holidaysDetails.getInsuranceDetails() != null && holidaysDetails.getInsuranceDetails().size() > 0) {
            //Setting Product Details values
            OpsHolidaysProductOrderDetails opsHolidaysPODetails = new OpsHolidaysProductOrderDetails();
            opsHolidaysPODetails.setProductName("Insurance");

            OpsHolidaySummary opsHolidayComponentSummary = new OpsHolidaySummary();
            opsHolidayComponentSummary.setTravelType("Insurance");

            List<OpsHolidaysTravelTypeDetails> opsHolidaysTTDList = new ArrayList<OpsHolidaysTravelTypeDetails>();

            //New Product Details Change -----XX--------
            List<OpsProduct> opsTransferProductList = new ArrayList<OpsProduct>();
            List<OpsOrderDetails> opsInsuranceOrderList = new ArrayList<OpsOrderDetails>();
            List<OpsInsuranceDetails> OpsInsuranceDetailsList = new ArrayList<OpsInsuranceDetails>();
            //-------XX-------

            for (int i = 0; i < holidaysDetails.getInsuranceDetails().size(); i++) {
                HolidaysInsuranceDetails holidaysInsuranceDetails = holidaysDetails.getInsuranceDetails().get(i);

                OpsHolidaysTravelTypeDetails opsHolidaysTravelTypeDetails = new OpsHolidaysTravelTypeDetails();

                opsHolidaysTravelTypeDetails.setName(holidaysInsuranceDetails.getInsCoverageDetail().getName());

                String insuranceDetails = String.format("%s%s%s%s%s%s", " Insurance Details: ", holidaysInsuranceDetails.getInsCoverageDetail().getId(), " ", holidaysInsuranceDetails.getInsCoverageDetail().getName(), " ", holidaysInsuranceDetails.getInsCoverageDetail().getDescription());

                opsHolidaysTravelTypeDetails.setDetails(insuranceDetails);

                opsHolidaysTTDList.add(opsHolidaysTravelTypeDetails);

                //New Product Details Change -----XX--------
                OpsProduct opsInsuranceProduct = getOpsHolidayComponentProductInfo(holidaysDetails, holidaysInsuranceDetails.getInsuranceId(), "OtherProducts", "Insurance");

                OpsOrderDetails opsInsuranceOrderDetails = getOpsHolidayComponentOrderInfo(holidaysDetails);

                opsInsuranceOrderDetails = getOpsHolidayInsuranceOrderInfo(holidaysDetails, opsInsuranceOrderDetails, holidaysInsuranceDetails);

          /*opsInsuranceProduct.setOrderDetails(opsInsuranceOrderDetails);
          opsTransferProductList.add(opsInsuranceProduct);
          opsInsuranceOrderList.add(opsInsuranceOrderDetails);*/

                OpsInsuranceDetailsList.add(opsInsuranceOrderDetails.getInsuranceDetails());
                //-------XX-------
            }

            opsHolidayComponentSummary.setTravelTypeDetails(opsHolidaysTTDList);
            opsHolidaySummaryList.add(opsHolidayComponentSummary);

            //opsHolidaysPODetails.setProductDetails(opsTransferProductList);
            //opsHolidaysPODetails.setProductDetails(opsInsuranceOrderList);
            opsHolidaysPODetails.setInsuranceDetails(OpsInsuranceDetailsList);
            opsProductDetailsList.add(opsHolidaysPODetails);
        }

        //ACTIVITIES - Product Details & Holiday Summary
        if (holidaysDetails.getActivityDetails() != null && holidaysDetails.getActivityDetails().size() > 0) {
            //Setting Product Details values
            OpsHolidaysProductOrderDetails opsHolidaysPODetails = new OpsHolidaysProductOrderDetails();
            opsHolidaysPODetails.setProductName("Activity");

            OpsHolidaySummary opsHolidayComponentSummary = new OpsHolidaySummary();
            opsHolidayComponentSummary.setTravelType("Activity");

            List<OpsHolidaysTravelTypeDetails> opsHolidaysTTDList = new ArrayList<OpsHolidaysTravelTypeDetails>();

            //New Product Details Change -----XX--------
            List<OpsProduct> opsActivityProductList = new ArrayList<OpsProduct>();
            List<OpsOrderDetails> opsActivityOrderList = new ArrayList<OpsOrderDetails>();
            List<OpsActivitiesDetails> opsActivitiesDetailsList = new ArrayList<OpsActivitiesDetails>();
            //-------XX-------

            for (int i = 0; i < holidaysDetails.getActivityDetails().size(); i++) {
                HolidaysActivitiesDetails holidaysActivitiesDetails = holidaysDetails.getActivityDetails().get(i);

                OpsHolidaysTravelTypeDetails opsHolidaysTravelTypeDetails = new OpsHolidaysTravelTypeDetails();

                opsHolidaysTravelTypeDetails.setName(holidaysActivitiesDetails.getActivityInfo().getName());

                String activityDetails = String.format("%s%s%s%s%s%s%s%s", " Activity Details: ", holidaysActivitiesDetails.getActivityInfo().getCode(), " ", holidaysActivitiesDetails.getActivityInfo().getName(), " ", holidaysActivitiesDetails.getActivityInfo().getDescription(), " ", holidaysActivitiesDetails.getActivityInfo().getStart());

                opsHolidaysTravelTypeDetails.setDetails(activityDetails);

                opsHolidaysTTDList.add(opsHolidaysTravelTypeDetails);

                //New Product Details Change -----XX--------
                OpsProduct opsActivityProduct = getOpsHolidayComponentProductInfo(holidaysDetails, holidaysActivitiesDetails.getActivityId(), "Activities", "Events");

                OpsOrderDetails opsActivityOrderDetails = getOpsHolidayComponentOrderInfo(holidaysDetails);

                opsActivityOrderDetails = getOpsHolidayActivityOrderInfo(holidaysDetails, opsActivityOrderDetails, holidaysActivitiesDetails);

          /*opsActivityProduct.setOrderDetails(opsActivityOrderDetails);
          opsActivityProductList.add(opsActivityProduct);
          opsActivityOrderList.add(opsActivityOrderDetails);*/

                opsActivitiesDetailsList.add(opsActivityOrderDetails.getActivityDetails());
                //-------XX-------
            }

            opsHolidayComponentSummary.setTravelTypeDetails(opsHolidaysTTDList);
            opsHolidaySummaryList.add(opsHolidayComponentSummary);

            //opsHolidaysPODetails.setProductDetails(opsActivityProductList);
            //opsHolidaysPODetails.setProductDetails(opsActivityOrderList);
            opsHolidaysPODetails.setActivityDetails(opsActivitiesDetailsList);
            opsProductDetailsList.add(opsHolidaysPODetails);
        }

        //EXTENSION NIGHTS - Product Details & Holiday Summary
        if (holidaysDetails.getExtensionNightsDetails() != null && holidaysDetails.getExtensionNightsDetails().size() > 0) {
            //Setting Product Details values
            OpsHolidaysProductOrderDetails opsHolidaysPODetails = new OpsHolidaysProductOrderDetails();
            opsHolidaysPODetails.setProductName("Extension Nights");

            OpsHolidaySummary opsHolidayComponentSummary = new OpsHolidaySummary();
            opsHolidayComponentSummary.setTravelType("Extension Nights");

            List<OpsHolidaysTravelTypeDetails> opsHolidaysTTDList = new ArrayList<OpsHolidaysTravelTypeDetails>();

            //New Product Details Change -----XX--------
            List<OpsProduct> opsExtensionNightsProductList = new ArrayList<OpsProduct>();
            List<OpsOrderDetails> opsExtensionNightsOrderList = new ArrayList<OpsOrderDetails>();
            List<OpsHolidaysExtensionNightDetails> opsHolidaysExtensionNightDetailsList = new ArrayList<OpsHolidaysExtensionNightDetails>();
            //-------XX-------

            for (int i = 0; i < holidaysDetails.getExtensionNightsDetails().size(); i++) {
                HolidaysExtensionDetails holidaysExtensionDetails = holidaysDetails.getExtensionNightsDetails().get(i);

                OpsHolidaysTravelTypeDetails opsHolidaysTravelTypeDetails = new OpsHolidaysTravelTypeDetails();

                opsHolidaysTravelTypeDetails.setName(holidaysExtensionDetails.getRoomInfo().getRoomTypeInfo().getRoomTypeCode());

                String roomDetails = String.format("%s%s%s%s%s%s", "Room Details: ", holidaysExtensionDetails.getRoomInfo().getRoomTypeInfo().getRoomCategoryID(), " ", holidaysExtensionDetails.getRoomInfo().getRoomTypeInfo().getRoomTypeCode(), " ", holidaysExtensionDetails.getRoomInfo().getRoomTypeInfo().getCabinnumber());

                //String HotelDetails =  String.format("%s%s%s%s%s%s%s%s", "Hotel Details: ", holidaysExtensionDetails.getRoomInfo().getHotelInfo().getHotelCode(), " ", holidaysExtensionDetails.getRoomInfo().getHotelInfo().getHotelName()," ",holidaysExtensionDetails.getRoomInfo().getHotelInfo().getHotelSegmentCategoryCode()," ",holidaysExtensionDetails.getRoomInfo().getHotelInfo().getHotelRef());

                opsHolidaysTravelTypeDetails.setDetails(roomDetails);

                opsHolidaysTTDList.add(opsHolidaysTravelTypeDetails);

                //New Product Details Change -----XX--------
                OpsProduct opsExtensionNightsProduct = getOpsHolidayComponentProductInfo(holidaysDetails, holidaysExtensionDetails.getExtensionId(), "Holidays", null);

                OpsOrderDetails opsExtensionNightOrderDetails = getOpsHolidayComponentOrderInfo(holidaysDetails);

                opsExtensionNightOrderDetails = getOpsHolidayExtensionNightsOrderInfo(holidaysDetails, opsExtensionNightOrderDetails, holidaysExtensionDetails);

          /*opsExtensionNightsProduct.setOrderDetails(opsExtensionNightOrderDetails);
          opsExtensionNightsProductList.add(opsExtensionNightsProduct);
          opsExtensionNightsOrderList.add(opsExtensionNightOrderDetails);*/

                opsHolidaysExtensionNightDetailsList.add(opsExtensionNightOrderDetails.getHolidaysExtensionNightDetails());
                //-------XX-------
            }

            opsHolidayComponentSummary.setTravelTypeDetails(opsHolidaysTTDList);
            opsHolidaySummaryList.add(opsHolidayComponentSummary);

            //opsHolidaysPODetails.setProductDetails(opsExtensionNightsProductList);
            //opsHolidaysPODetails.setProductDetails(opsExtensionNightsOrderList);
            opsHolidaysPODetails.setHolidaysExtensionNightDetails(opsHolidaysExtensionNightDetailsList);
            opsProductDetailsList.add(opsHolidaysPODetails);
        }

        //EXTRAS - Product Details & Holiday Summary
        if (holidaysDetails.getExtrasDetails() != null && holidaysDetails.getExtrasDetails().size() > 0) {
            //Setting Product Details values
            OpsHolidaysProductOrderDetails opsHolidaysPODetails = new OpsHolidaysProductOrderDetails();
            opsHolidaysPODetails.setProductName("Extras");

            OpsHolidaySummary opsHolidayComponentSummary = new OpsHolidaySummary();
            opsHolidayComponentSummary.setTravelType("Extras");

            List<OpsHolidaysTravelTypeDetails> opsHolidaysTTDList = new ArrayList<OpsHolidaysTravelTypeDetails>();

            //New Product Details Change -----XX--------
            List<OpsProduct> opsExtrasProductList = new ArrayList<OpsProduct>();
            List<OpsOrderDetails> opsExtrasOrderList = new ArrayList<OpsOrderDetails>();
            List<OpsHolidaysExtrasDetails> opsHolidaysExtrasDetailsList = new ArrayList<OpsHolidaysExtrasDetails>();
            //-------XX-------

            for (int i = 0; i < holidaysDetails.getExtrasDetails().size(); i++) {
                HolidaysExtrasDetails holidaysExtrasDetails = holidaysDetails.getExtrasDetails().get(i);

                OpsHolidaysTravelTypeDetails opsHolidaysTravelTypeDetails = new OpsHolidaysTravelTypeDetails();

                opsHolidaysTravelTypeDetails.setName(holidaysExtrasDetails.getExtrasInfo().getName());

                String extrasDetails = String.format("%s%s%s%s%s%s%s%s", "Extras Details: ", holidaysExtrasDetails.getExtrasInfo().getCode(), " ", holidaysExtrasDetails.getExtrasInfo().getName(), " ", holidaysExtrasDetails.getExtrasInfo().getDescription(), " ", holidaysExtrasDetails.getExtrasInfo().getQuantity());

                opsHolidaysTravelTypeDetails.setDetails(extrasDetails);

                opsHolidaysTTDList.add(opsHolidaysTravelTypeDetails);

                //New Product Details Change -----XX--------
                OpsProduct opsExtrasProduct = getOpsHolidayComponentProductInfo(holidaysDetails, holidaysExtrasDetails.getExtrasId(), "Holidays", null);

                OpsOrderDetails opsExtrasOrderDetails = getOpsHolidayComponentOrderInfo(holidaysDetails);

                opsExtrasOrderDetails = getOpsHolidayExtrasOrderInfo(holidaysDetails, opsExtrasOrderDetails, holidaysExtrasDetails);

          /*opsExtrasProduct.setOrderDetails(opsExtrasOrderDetails);
          opsExtrasProductList.add(opsExtrasProduct);
          opsExtrasOrderList.add(opsExtrasOrderDetails);*/

                opsHolidaysExtrasDetailsList.add(opsExtrasOrderDetails.getHolidaysExtrasDetails());
                //-------XX-------
            }

            opsHolidayComponentSummary.setTravelTypeDetails(opsHolidaysTTDList);
            opsHolidaySummaryList.add(opsHolidayComponentSummary);

            //opsHolidaysPODetails.setProductDetails(opsExtrasProductList);
            //opsHolidaysPODetails.setProductDetails(opsExtrasOrderList);
            opsHolidaysPODetails.setHolidaysExtrasDetails(opsHolidaysExtrasDetailsList);
            opsProductDetailsList.add(opsHolidaysPODetails);
        }

        opsHolidaysDetails.setHolidaySummary(opsHolidaySummaryList);

        opsHolidaysDetails.setProductDetails(opsProductDetailsList);


        //SETTING PAXINFO OBJECT
        List<OpsHolidaysPaxInfo> opsHolidaysPaxInfoList = getOpsHolidaysPaxInfo(holidaysDetails);
        opsHolidaysDetails.setPaxInfo(opsHolidaysPaxInfoList);

        //Setting Order Supplier Price Info
        OpsHolidaysOrderPriceInfo opsHolidaysOrderSupplierPriceInfo = getOpsHolidaysOrderPriceInfo(holidaysDetails.getOrderSupplierPriceInfo());
        opsHolidaysDetails.setOrderSupplierPriceInfo(opsHolidaysOrderSupplierPriceInfo);

        //Setting Order Total Price Info
        OpsHolidaysOrderPriceInfo opsHolidaysOrderTotalPriceInfo = getOpsHolidaysOrderPriceInfo(holidaysDetails.getOrderTotalPriceInfo());
        opsHolidaysDetails.setOrderClientTotalPriceInfo(opsHolidaysOrderTotalPriceInfo);

        return opsHolidaysDetails;
    }


    private List<OpsHolidaysPaxInfo> getOpsHolidaysPaxInfo(HolidaysDetails holidaysDetails) {

        Map<String, OpsHolidaysPaxInfo> paxIndexMap = new HashMap<String, OpsHolidaysPaxInfo>();

        //Total Price of the package
        String TotalPackagePrice = holidaysDetails.getOrderTotalPriceInfo().getAmountAfterTax();

        //Accommodation in Packages
        for (int i = 0; i < holidaysDetails.getAccomodationDetails().size(); i++) {
            HolidaysAccommodationDetails holidaysAccommodationDetails = holidaysDetails.getAccomodationDetails().get(i);

            for (int j = 0; j < holidaysAccommodationDetails.getPaxInfo().size(); j++) {
                PaxInfo paxInfo = holidaysAccommodationDetails.getPaxInfo().get(j);

                String ProductName = holidaysAccommodationDetails.getAccomodationType();
                String name = holidaysAccommodationDetails.getRoomInfo().getRoomTypeInfo().getRoomTypeCode();

                HolidaysTotalPriceInfo totalPriceInfo = holidaysAccommodationDetails.getTotalPriceInfo();

                setPaxInfo(paxInfo, paxIndexMap, TotalPackagePrice, ProductName, name, totalPriceInfo);

            }
        }

        //Transfers in Packages
        if (holidaysDetails.getTransferDetails() != null && holidaysDetails.getTransferDetails().size() > 0) {
            for (int i = 0; i < holidaysDetails.getTransferDetails().size(); i++) {
                HolidaysTransferDetails holidaysTransferDetails = holidaysDetails.getTransferDetails().get(i);

                for (int j = 0; j < holidaysTransferDetails.getPaxInfo().size(); j++) {
                    PaxInfo paxInfo = holidaysTransferDetails.getPaxInfo().get(j);

                    String ProductName2 = "Transfer";
                    String name2 = holidaysTransferDetails.getTransferInfo().getName();

                    HolidaysTotalPriceInfo totalPriceInfo = holidaysTransferDetails.getTotalPriceInfo();

                    setPaxInfo(paxInfo, paxIndexMap, TotalPackagePrice, ProductName2, name2, totalPriceInfo);

                }
            }
        }

        //Insurance in Packages
        if (holidaysDetails.getInsuranceDetails() != null && holidaysDetails.getInsuranceDetails().size() > 0) {
            for (int i = 0; i < holidaysDetails.getInsuranceDetails().size(); i++) {
                HolidaysInsuranceDetails holidaysInsuranceDetails = holidaysDetails.getInsuranceDetails().get(i);

                for (int j = 0; j < holidaysInsuranceDetails.getPaxInfo().size(); j++) {
                    PaxInfo paxInfo = holidaysInsuranceDetails.getPaxInfo().get(j);

                    String ProductName2 = "Insurance";
                    String name2 = holidaysInsuranceDetails.getInsCoverageDetail().getDescription().toString();

                    HolidaysTotalPriceInfo totalPriceInfo = holidaysInsuranceDetails.getTotalPriceInfo();

                    setPaxInfo(paxInfo, paxIndexMap, TotalPackagePrice, ProductName2, name2, totalPriceInfo);

                }
            }
        }


        //Activities in Packages
        if (holidaysDetails.getActivityDetails() != null && holidaysDetails.getActivityDetails().size() > 0) {
            for (int i = 0; i < holidaysDetails.getActivityDetails().size(); i++) {
                HolidaysActivitiesDetails holidaysActivitiesDetails = holidaysDetails.getActivityDetails().get(i);

                for (int j = 0; j < holidaysActivitiesDetails.getPaxInfo().size(); j++) {
                    PaxInfo paxInfo = holidaysActivitiesDetails.getPaxInfo().get(j);

                    String ProductName2 = "Activity";
                    String name2 = holidaysActivitiesDetails.getActivityInfo().getName();

                    HolidaysTotalPriceInfo totalPriceInfo = holidaysActivitiesDetails.getTotalPriceInfo();

                    setPaxInfo(paxInfo, paxIndexMap, TotalPackagePrice, ProductName2, name2, totalPriceInfo);

                }
            }
        }

        //Extension Nights in Packages
        if (holidaysDetails.getExtensionNightsDetails() != null && holidaysDetails.getExtensionNightsDetails().size() > 0) {
            for (int i = 0; i < holidaysDetails.getExtensionNightsDetails().size(); i++) {
                HolidaysExtensionDetails holidaysExtensionDetails = holidaysDetails.getExtensionNightsDetails().get(i);

                for (int j = 0; j < holidaysExtensionDetails.getPaxInfo().size(); j++) {
                    PaxInfo paxInfo = holidaysExtensionDetails.getPaxInfo().get(j);

                    String ProductName2 = "Extension Night";
                    String name2 = holidaysExtensionDetails.getExtensionType().concat(" - ").concat(holidaysExtensionDetails.getRoomInfo().getRoomTypeInfo().getRoomTypeCode());

                    HolidaysTotalPriceInfo totalPriceInfo = holidaysExtensionDetails.getTotalPriceInfo();

                    setPaxInfo(paxInfo, paxIndexMap, TotalPackagePrice, ProductName2, name2, totalPriceInfo);

                }
            }
        }

        //Extras in Packages
        if (holidaysDetails.getExtrasDetails() != null && holidaysDetails.getExtrasDetails().size() > 0) {
            for (int i = 0; i < holidaysDetails.getExtrasDetails().size(); i++) {
                HolidaysExtrasDetails holidaysExtrasDetails = holidaysDetails.getExtrasDetails().get(i);

                for (int j = 0; j < holidaysExtrasDetails.getPaxInfo().size(); j++) {
                    PaxInfo paxInfo = holidaysExtrasDetails.getPaxInfo().get(j);

                    String ProductName2 = "Extras";
                    String name2 = holidaysExtrasDetails.getExtrasInfo().getName();

                    HolidaysTotalPriceInfo totalPriceInfo = holidaysExtrasDetails.getTotalPriceInfo();

                    setPaxInfo(paxInfo, paxIndexMap, TotalPackagePrice, ProductName2, name2, totalPriceInfo);

                }
            }
        }


        //Setting Holiday PaxInfo List
        List<OpsHolidaysPaxInfo> opsHolidaysPaxInfoList = new ArrayList<OpsHolidaysPaxInfo>();

        for (Map.Entry<String, OpsHolidaysPaxInfo> entry : paxIndexMap.entrySet()) {
            opsHolidaysPaxInfoList.add(entry.getValue());
        }

        return opsHolidaysPaxInfoList;
    }

    private void setPaxInfo(PaxInfo paxInfo, Map<String, OpsHolidaysPaxInfo> paxIndexMap, String TotalPackagePrice, String ProductName, String name, HolidaysTotalPriceInfo totalPriceInfo) {
        if (paxIndexMap.containsKey(paxInfo.getResGuestRPH())) {
            OpsHolidaysPaxInfo opsHolidaysPaxInfo = paxIndexMap.get(paxInfo.getResGuestRPH());

            List<OpsHolidaysProductPrice> opsHolidaysProductPriceList = opsHolidaysPaxInfo.getPriceDetails().getTourPrice().getProductPrice();

            OpsHolidaysProductPrice opsHolidaysProductPrice = setproductPriceObject(paxInfo, opsHolidaysPaxInfo, ProductName, name, totalPriceInfo);

            String check = "0";
            for (int i = 0; i < opsHolidaysProductPriceList.size(); i++) {
                if (opsHolidaysProductPriceList.get(i).getProductName().equals(opsHolidaysProductPrice.getProductName())) {
                    opsHolidaysProductPriceList.get(i).setProductDetails(opsHolidaysProductPrice.getProductDetails());

                    check = "1";
                }
            }

            if (check.equals("0")) {
                opsHolidaysProductPriceList.add(opsHolidaysProductPrice);
            }

            //Setting product Price
            opsHolidaysPaxInfo.getPriceDetails().getTourPrice().setProductPrice(opsHolidaysProductPriceList);

            //Setting tour Net Price
            opsHolidaysPaxInfo.getPriceDetails().getTotalNetPrice().setProductPrice(opsHolidaysProductPriceList);

            paxIndexMap.remove(paxInfo.getResGuestRPH());
            paxIndexMap.put(paxInfo.getResGuestRPH(), opsHolidaysPaxInfo);
        } else {

            OpsHolidaysPaxInfo opsHolidaysPaxInfo = new OpsHolidaysPaxInfo();

            opsHolidaysPaxInfo.setPaxID(paxInfo.getPaxID());
            opsHolidaysPaxInfo.setPaxType(paxInfo.getPaxType());
            opsHolidaysPaxInfo.setResGuestRPH(paxInfo.getResGuestRPH());
            opsHolidaysPaxInfo.setIsLeadPax(paxInfo.getIsLeadPax());
            opsHolidaysPaxInfo.setFirstName(paxInfo.getFirstName());
            opsHolidaysPaxInfo.setLastName(paxInfo.getLastName());
            opsHolidaysPaxInfo.setMiddleName(paxInfo.getMiddleName());
            opsHolidaysPaxInfo.setTitle(paxInfo.getTitle());
            opsHolidaysPaxInfo.setBirthDate(paxInfo.getBirthDate());

            //Setting address Details
            if (paxInfo.getAddressDetails() != null) {
                opsHolidaysPaxInfo.setAddressDetails(parentAdapter.getOpsAddressDetails(paxInfo.getAddressDetails()));
            }
       
       /*
      AddressDetails addressDetails = paxInfo.getAddressDetails();
      
      OpsAddressDetails opsAddressDetails = new OpsAddressDetails();
        
      List<String> addressLine = new ArrayList<String>();
        
      addressLine.add(addressDetails.getAddrLine1());
      addressLine.add(addressDetails.getAddrLine2());
        
      opsAddressDetails.setAddressLines(addressLine);
      opsAddressDetails.setCityName(addressDetails.getCity());
      opsAddressDetails.setState(addressDetails.getState());
      opsAddressDetails.setCountryName(addressDetails.getCountry());
      opsAddressDetails.setZipCode(addressDetails.getZip());
        
      opsHolidaysPaxInfo.setAddressDetails(opsAddressDetails);
      */

            //Setting contact Details
            if (paxInfo.getContactDetails() != null) {
                opsHolidaysPaxInfo.setContactDetails(paxInfo.getContactDetails().stream().map(contactDetail
                        -> parentAdapter.getOpsContactDetail(contactDetail)).collect(Collectors.toList()));
            }
      
      /*List<OpsContactDetails> opsContactDetailsList = new ArrayList<OpsContactDetails>();
      
      for(int z=0; z<paxInfo.getContactDetails().size();z++)
      {
        ContactDetail contactDetail = paxInfo.getContactDetails().get(z);
        
        OpsContactDetails opsContactDetails = new OpsContactDetails();
        
        OpsContactInfo opsContactInfo = new OpsContactInfo();
        
        opsContactInfo.setAreaCityCode(contactDetail.getContactInfo().getAreaCityCode());
        opsContactInfo.setContactType(contactDetail.getContactInfo().getContactType());
        opsContactInfo.setCountryCode(contactDetail.getContactInfo().getCountryCode());
        opsContactInfo.setEmail(contactDetail.getContactInfo().getEmail());
        opsContactInfo.setEmail(contactDetail.getContactInfo().getEmail());
        opsContactInfo.setMobileNo(contactDetail.getContactInfo().getMobileNo());
        
        opsContactDetails.setContactInfo(opsContactInfo);
        opsContactDetailsList.add(opsContactDetails);            
      }
      
      opsHolidaysPaxInfo.setContactDetails(opsContactDetailsList);
      */

            //Setting Ancillary Services
            if (paxInfo.getAncillaryServices() != null) {
                opsHolidaysPaxInfo.setAncillaryServices(parentAdapter.getOpsAncillaryServices(paxInfo.getAncillaryServices()));
            }

            //Setting price Details
            OpsHolidaysPriceDetails opsHolidaysPriceDetails = new OpsHolidaysPriceDetails();

            //Setting tour Price
            OpsHolidaysTourPrice opsHolidaysTourPrice = new OpsHolidaysTourPrice();

            //Setting product Price
            List<OpsHolidaysProductPrice> opsHolidaysProductPriceList = new ArrayList<OpsHolidaysProductPrice>();

            OpsHolidaysProductPrice opsHolidaysProductPrice = setproductPriceObject(paxInfo, opsHolidaysPaxInfo, ProductName, name, totalPriceInfo);
            opsHolidaysProductPriceList.add(opsHolidaysProductPrice);

            opsHolidaysTourPrice.setTotalPackagePrice(TotalPackagePrice);
            opsHolidaysTourPrice.setCurrencyCode(totalPriceInfo.getCurrencyCode());
            opsHolidaysTourPrice.setProductPrice(opsHolidaysProductPriceList);

            opsHolidaysPriceDetails.setTourPrice(opsHolidaysTourPrice);

            //Setting tour Net Price
            opsHolidaysPriceDetails.setTotalNetPrice(opsHolidaysTourPrice);

            opsHolidaysPaxInfo.setPriceDetails(opsHolidaysPriceDetails);


            //Setting the structure in the map
            paxIndexMap.put(paxInfo.getResGuestRPH(), opsHolidaysPaxInfo);

        }

    }

    private OpsHolidaysProductPrice setproductPriceObject(PaxInfo paxInfo, OpsHolidaysPaxInfo opsHolidaysPaxInfo, String ProductName, String name, HolidaysTotalPriceInfo totalPriceInfo) {
        System.out.println(ProductName);

        if (opsHolidaysPaxInfo.getPriceDetails() != null && opsHolidaysPaxInfo.getPriceDetails().getTourPrice().getProductPrice().size() > 0) {
            String found = "0";
            OpsHolidaysProductPrice opsHolidaysProductPrice = null;

            for (int i = 0; i < opsHolidaysPaxInfo.getPriceDetails().getTourPrice().getProductPrice().size(); i++) {
                opsHolidaysProductPrice = opsHolidaysPaxInfo.getPriceDetails().getTourPrice().getProductPrice().get(i);

                System.out.println(opsHolidaysProductPrice.getProductName());

                if (opsHolidaysProductPrice.getProductName().equals(ProductName)) {
                    for (int g = 0; g < totalPriceInfo.getPaxTypeFares().size(); g++) {
                        HolidaysPaxTypeFares paxTypeFare = totalPriceInfo.getPaxTypeFares().get(g);

                        if (paxInfo.getPaxType().equals(paxTypeFare.getType())) {
                            OpsHolidaysProductDetails opsHolidaysProductDetails = new OpsHolidaysProductDetails();
                            opsHolidaysProductDetails.setName(name);
                            opsHolidaysProductDetails.setCurrencyCode(paxTypeFare.getCurrencyCode());
                            opsHolidaysProductDetails.setPrice(paxTypeFare.getAmountAfterTax());

                            opsHolidaysProductPrice.getProductDetails().add(opsHolidaysProductDetails);

                            found = "1";
                        }
                    }

                }
            }

            if (found.equals("0")) {
                opsHolidaysProductPrice = new OpsHolidaysProductPrice();

                List<OpsHolidaysProductDetails> opsHolidaysProductDetailsList = new ArrayList<OpsHolidaysProductDetails>();

                for (int g = 0; g < totalPriceInfo.getPaxTypeFares().size(); g++) {
                    HolidaysPaxTypeFares paxTypeFare = totalPriceInfo.getPaxTypeFares().get(g);

                    if (paxInfo.getPaxType().equals(paxTypeFare.getType())) {
                        OpsHolidaysProductDetails opsHolidaysProductDetails = new OpsHolidaysProductDetails();
                        opsHolidaysProductDetails.setName(name);
                        opsHolidaysProductDetails.setCurrencyCode(paxTypeFare.getCurrencyCode());
                        opsHolidaysProductDetails.setPrice(paxTypeFare.getAmountAfterTax());

                        opsHolidaysProductDetailsList.add(opsHolidaysProductDetails);
                    }
                }
                opsHolidaysProductPrice.setProductName(ProductName);
                opsHolidaysProductPrice.setProductDetails(opsHolidaysProductDetailsList);
            }


            return opsHolidaysProductPrice;
        } else {
            OpsHolidaysProductPrice opsHolidaysProductPrice = new OpsHolidaysProductPrice();

            opsHolidaysProductPrice.setProductName(ProductName);

            List<OpsHolidaysProductDetails> opsHolidaysProductDetailsList = new ArrayList<OpsHolidaysProductDetails>();

            for (int g = 0; g < totalPriceInfo.getPaxTypeFares().size(); g++) {
                HolidaysPaxTypeFares paxTypeFare = totalPriceInfo.getPaxTypeFares().get(g);

                if (paxInfo.getPaxType().equals(paxTypeFare.getType())) {
                    OpsHolidaysProductDetails opsHolidaysProductDetails = new OpsHolidaysProductDetails();
                    opsHolidaysProductDetails.setName(name);
                    opsHolidaysProductDetails.setCurrencyCode(paxTypeFare.getCurrencyCode());

                    opsHolidaysProductDetails.setPrice(paxTypeFare.getAmountAfterTax());

                    opsHolidaysProductDetailsList.add(opsHolidaysProductDetails);
                }
            }

            opsHolidaysProductPrice.setProductDetails(opsHolidaysProductDetailsList);

            return opsHolidaysProductPrice;
        }


    }

    private OpsHolidaysOrderPriceInfo getOpsHolidaysOrderPriceInfo(HolidaysOrderPriceInfo holidaysOrderPriceInfo) {

        OpsHolidaysOrderPriceInfo opsHolidaysOrderPriceInfo = new OpsHolidaysOrderPriceInfo();

        opsHolidaysOrderPriceInfo.setCurrencyCode(holidaysOrderPriceInfo.getCurrencyCode());

        opsHolidaysOrderPriceInfo.setAmountAfterTax(holidaysOrderPriceInfo.getAmountAfterTax());

        opsHolidaysOrderPriceInfo.setAmountBeforeTax(holidaysOrderPriceInfo.getAmountBeforeTax());

        opsHolidaysOrderPriceInfo.setTaxes(parentAdapter.getOpsTaxes(holidaysOrderPriceInfo.getTaxes()));

        return opsHolidaysOrderPriceInfo;

    }

    private OpsProduct getOpsHolidayComponentProductInfo(HolidaysDetails holidaysDetails, String OrderID, String ProductCategory, String ProductSubCategory) {

        OpsProduct opsProduct = new OpsProduct();

        opsProduct.setOrderID(OrderID);

        opsProduct.setAmendDateZDT(DateTimeUtil.formatBEDateTimeZone(holidaysDetails.getAmendDate()));

        opsProduct.setProductCategory(ProductCategory);

        opsProduct.setProductSubCategory(ProductSubCategory);

        OpsProductCategory opsProductCategory = OpsProductCategory.getProductCategory(ProductCategory);
        opsProduct.setOpsProductCategory(OpsProductCategory.getProductCategory(ProductCategory));

        OpsProductSubCategory aProductSubCategory = OpsProductSubCategory.getProductSubCategory(opsProductCategory, ProductSubCategory);

        opsProduct.setOpsProductCategory(opsProductCategory);

        opsProduct.setOpsProductSubCategory(aProductSubCategory);

        return opsProduct;
    }

    private OpsOrderDetails getOpsHolidayComponentOrderInfo(HolidaysDetails holidaysDetails) {

        //Setting Order details
        OpsOrderDetails opsCompnentOrderDetails = new OpsOrderDetails();

        opsCompnentOrderDetails.setSupplierType(OpsSupplierType.SUPPLIER_TYPE_ONLINE.fromString(holidaysDetails.getSupplierType()));

        opsCompnentOrderDetails.setOpsOrderStatus(OpsOrderStatus.fromString(holidaysDetails.getStatus()));

        List<OpsBookingAttribute> opsBookingAttribute = new ArrayList<>();
        opsCompnentOrderDetails.setOpsBookingAttribute(opsBookingAttribute);

        return opsCompnentOrderDetails;

    }

    private OpsOrderDetails getOpsHolidayAccoOrderInfo(HolidaysDetails holidaysDetails, OpsOrderDetails opsCompnentOrderDetails) {

        opsCompnentOrderDetails.setClientCommercials(holidaysDetails.getComponentPricing().getAccomodationDetails().getClientCommercials().stream().map(
                clientCommercial -> parentAdapter.getOpsOrderClientCommercial(clientCommercial)).collect(Collectors.toList()));

        opsCompnentOrderDetails.setSupplierCommercials(holidaysDetails.getComponentPricing().getAccomodationDetails().getSupplierCommercials().stream().
                map(supplierCommercial -> parentAdapter.getOpsOrderSupplierCommercial(supplierCommercial)).
                collect(Collectors.toList()));

        OpsHotelDetails opsHotelDetails = new OpsHotelDetails();


        //Setting supplier Price Info
        OpsAccoOrderSupplierPriceInfo opsAccoOrderSupplierPriceInfo = new OpsAccoOrderSupplierPriceInfo();

        opsAccoOrderSupplierPriceInfo.setCurrencyCode(holidaysDetails.getComponentPricing().getAccomodationDetails().getSupplierPriceBreakup().getCurrencyCode());

        opsAccoOrderSupplierPriceInfo.setSupplierPrice(holidaysDetails.getComponentPricing().getAccomodationDetails().getSupplierPriceBreakup().getAmountAfterTax());

        opsAccoOrderSupplierPriceInfo.setTaxes(parentAdapter.getOpsTaxes(holidaysDetails.getComponentPricing().getAccomodationDetails().getSupplierPriceBreakup().getTaxes()));

        opsHotelDetails.setOpsAccoOrderSupplierPriceInfo(opsAccoOrderSupplierPriceInfo);


        //Setting Total Price Info
        OpsAccommodationTotalPriceInfo opsAccommodationTotalPriceInfo = new OpsAccommodationTotalPriceInfo();

        opsAccommodationTotalPriceInfo.setCurrencyCode(holidaysDetails.getComponentPricing().getAccomodationDetails().getSellingPriceBreakup().getCurrencyCode());

        opsAccommodationTotalPriceInfo.setTotalPrice(holidaysDetails.getComponentPricing().getAccomodationDetails().getSellingPriceBreakup().getAmountAfterTax());

        opsAccommodationTotalPriceInfo.setOpsTaxes(parentAdapter.getOpsTaxes(holidaysDetails.getComponentPricing().getAccomodationDetails().getSellingPriceBreakup().getTaxes()));

        opsHotelDetails.setOpsAccommodationTotalPriceInfo(opsAccommodationTotalPriceInfo);

        opsCompnentOrderDetails.setHotelDetails(opsHotelDetails);

        return opsCompnentOrderDetails;
    }

    private OpsRoom getOpsHolidayAccoRoomOrderInfo(HolidaysAccommodationDetails holidaysAccommodationDetails) {

        //Setting rooms
        OpsRoom opsRoom = new OpsRoom();

        opsRoom.setRoomID(holidaysAccommodationDetails.getRoomID());

        opsRoom.setRefundableIndicator(false);

        //opsRoom.setCheckIn(holidaysAccommodationDetails.getRoomInfo().getTimeSpan().getStart());

        //opsRoom.setCheckOut(holidaysAccommodationDetails.getRoomInfo().getTimeSpan().getEnd());

        //Setting room supplier price info
        OpsRoomSuppPriceInfo opsRoomSuppPriceInfo = new OpsRoomSuppPriceInfo();

        opsRoomSuppPriceInfo.setCurrencyCode(holidaysAccommodationDetails.getSupplierPriceInfo().getCurrencyCode());

        opsRoomSuppPriceInfo.setRoomSupplierPrice(holidaysAccommodationDetails.getSupplierPriceInfo().getSupplierPriceAfterTax());

        opsRoomSuppPriceInfo.setTaxes(parentAdapter.getOpsTaxes(holidaysAccommodationDetails.getSupplierPriceInfo().getTaxes()));

        //Setting room total price info
        OpsRoomTotalPriceInfo opsRoomTotalPriceInfo = new OpsRoomTotalPriceInfo();

        opsRoomTotalPriceInfo.setCurrencyCode(holidaysAccommodationDetails.getTotalPriceInfo().getCurrencyCode());

        opsRoomTotalPriceInfo.setRoomTotalPrice(holidaysAccommodationDetails.getTotalPriceInfo().getTotalPriceAfterTax());

        opsRoomTotalPriceInfo.setOpsTaxes(parentAdapter.getOpsTaxes(holidaysAccommodationDetails.getTotalPriceInfo().getTaxes()));

        //Setting room supplier commercials
        opsRoom.setRoomSuppCommercials(holidaysAccommodationDetails.getSupplierCommercials().stream().map(supplierCommercial ->
                accommodationAdapter.getOpsRoomSupplierCommercial(supplierCommercial)).collect(Collectors.toList()));

        //Setting room client commercials
        opsRoom.setOpsClientEntityCommercial(holidaysAccommodationDetails.getClientCommercials().stream().map(clientCommercial ->
                accommodationAdapter.getOpsClientEntityCommercialRoom(clientCommercial)).collect(Collectors.toList()));

        //Setting Room Info
        opsRoom.setRoomTypeInfo(accommodationAdapter.getOpsRoomTypeInfo(holidaysAccommodationDetails.getRoomInfo().getRoomTypeInfo()));

        opsRoom.setRatePlanInfo(accommodationAdapter.getOpsRatePlanInfo(holidaysAccommodationDetails.getRoomInfo().getRatePlanInfo()));

        //Setting Pax Info
        opsRoom.setPaxInfo(holidaysAccommodationDetails.getPaxInfo().stream().map(paxInfo1 -> accommodationAdapter.getOpsAccommodationPaxInfo(paxInfo1)).collect(Collectors.toList()));

        return opsRoom;
    }

    private OpsOrderDetails getOpsHolidayActivityOrderInfo(HolidaysDetails holidaysDetails, OpsOrderDetails opsCompnentOrderDetails, HolidaysActivitiesDetails holidaysActivitiesDetails) {

        opsCompnentOrderDetails.setClientCommercials(holidaysActivitiesDetails.getOrderClientCommercials().stream().map(
                clientCommercial -> parentAdapter.getOpsOrderClientCommercial(clientCommercial)).collect(Collectors.toList()));

        opsCompnentOrderDetails.setSupplierCommercials(holidaysActivitiesDetails.getOrderSupplierCommercials().stream().
                map(supplierCommercial -> parentAdapter.getOpsOrderSupplierCommercial(supplierCommercial)).
                collect(Collectors.toList()));

        OpsActivitiesDetails opsActivityDetails = new OpsActivitiesDetails();

        //Setting supplier Price Info
        OpsActivitiesSupplierPriceInfo opsActivityOrderSupplierPriceInfo = new OpsActivitiesSupplierPriceInfo();

        opsActivityOrderSupplierPriceInfo.setSupplierPrice(holidaysActivitiesDetails.getSupplierPriceInfo().getSupplierPriceAfterTax());
        opsActivityOrderSupplierPriceInfo.setCurrencyCode(holidaysActivitiesDetails.getSupplierPriceInfo().getCurrencyCode());
        opsActivityOrderSupplierPriceInfo.setOpsPaxTypeFare(getPaxTypeFaresSupplier(holidaysActivitiesDetails.getSupplierPriceInfo().getPaxTypeFares()));

        opsActivityDetails.setOpsActivitiesSupplierPriceInfo(opsActivityOrderSupplierPriceInfo);

        //Setting Total Price Info
        OpsActivitiesTotalPriceInfo opsActivitiesTotalPriceInfo = new OpsActivitiesTotalPriceInfo();

        opsActivitiesTotalPriceInfo.setCurrencyCode(holidaysActivitiesDetails.getTotalPriceInfo().getCurrencyCode());
        opsActivitiesTotalPriceInfo.setTotalPrice(holidaysActivitiesDetails.getTotalPriceInfo().getTotalPriceAfterTax());
        opsActivitiesTotalPriceInfo.setOpsTaxes(parentAdapter.getOpsTaxes(holidaysActivitiesDetails.getTotalPriceInfo().getTaxes()));
        opsActivitiesTotalPriceInfo.setOpsPaxTypeFare(getPaxTypeFaresClient(holidaysActivitiesDetails.getTotalPriceInfo().getPaxTypeFares()));
        //Setting Base Fare
        OpsBaseFare opsBaseFare = new OpsBaseFare();

        opsBaseFare.setAmount(new Double(holidaysActivitiesDetails.getTotalPriceInfo().getTotalPriceBeforeTax()));
        opsBaseFare.setCurrencyCode(holidaysActivitiesDetails.getTotalPriceInfo().getCurrencyCode());

        opsActivitiesTotalPriceInfo.setOpsBaseFare(opsBaseFare);
        //Setting Receivables
        OpsReceivables opsReceivables = new OpsReceivables();

        opsReceivables.setAmount(holidaysActivitiesDetails.getTotalPriceInfo().getReceivables().getAmount());
        opsReceivables.setCurrencyCode(holidaysActivitiesDetails.getTotalPriceInfo().getReceivables().getCurrencyCode());

        List<OpsReceivable> opsReceivableList = new ArrayList<OpsReceivable>();

        for (int k = 0; k < holidaysActivitiesDetails.getTotalPriceInfo().getReceivables().getReceivable().size(); k++) {
            Receivable receivable = holidaysActivitiesDetails.getTotalPriceInfo().getReceivables().getReceivable().get(k);

            OpsReceivable opsReceivable = new OpsReceivable();

            opsReceivable.setAmount(receivable.getAmount());
            opsReceivable.setCode(receivable.getCode());
            opsReceivable.setCurrencyCode(receivable.getCurrencyCode());

            opsReceivableList.add(opsReceivable);
        }
        opsReceivables.setReceivable(opsReceivableList);
        opsActivitiesTotalPriceInfo.setOpsReceivables(opsReceivables);

        opsActivityDetails.setOpsActivitiesTotalPriceInfo(opsActivitiesTotalPriceInfo);

        //Setting Activities Details
        opsActivityDetails.setActivityDetails(holidaysActivitiesDetails.getActivityInfo().getName().concat(holidaysActivitiesDetails.getActivityInfo().getDescription().concat(holidaysActivitiesDetails.getActivityInfo().getCode())));
        opsActivityDetails.setDateTimeOfVist(holidaysActivitiesDetails.getActivityInfo().getStart());
        opsActivityDetails.setBookingStatus(holidaysDetails.getStatus());

        //Setting Pax Details
        List<OpsPaxParticular> opsPaxParticularList = new ArrayList<>();
        for (int i = 0; i < holidaysActivitiesDetails.getPaxInfo().size(); i++) {
            PaxInfo paxInfo = holidaysActivitiesDetails.getPaxInfo().get(i);

            OpsPaxParticular opsPaxParticular = new OpsPaxParticular();

            opsPaxParticular.setPaxKey(paxInfo.getPaxID());
            opsPaxParticular.setPassengerName(paxInfo.getFirstName().concat(paxInfo.getLastName()));
            opsPaxParticular.setDateOfBirth(paxInfo.getBirthDate());

            opsPaxParticularList.add(opsPaxParticular);
        }
        opsActivityDetails.setOpsPaxParticulars(opsPaxParticularList);

        opsCompnentOrderDetails.setActivityDetails(opsActivityDetails);

        return opsCompnentOrderDetails;
    }

    private List<OpsPaxTypeFare> getPaxTypeFaresSupplier(List<HolidaysPaxTypeFares> HolidaysPaxTypeFaresList) {

        List<OpsPaxTypeFare> OpsPaxTypeFareList = new ArrayList<OpsPaxTypeFare>();

        for (int i = 0; i < HolidaysPaxTypeFaresList.size(); i++) {
            HolidaysPaxTypeFares HolidaysPaxTypeFares = HolidaysPaxTypeFaresList.get(i);

            OpsPaxTypeFareHolidaysSupplier opsPaxTypeFareHolidaysSupplier = new OpsPaxTypeFareHolidaysSupplier();

            //Setting Pax Type
            opsPaxTypeFareHolidaysSupplier.setPaxType(HolidaysPaxTypeFares.getType());

            //Setting Base Fare
            OpsBaseFare opsPaxTypeBaseFare = new OpsBaseFare();

            opsPaxTypeBaseFare.setAmount(new Double(HolidaysPaxTypeFares.getAmountBeforeTax()));
            opsPaxTypeBaseFare.setCurrencyCode(HolidaysPaxTypeFares.getCurrencyCode());

            opsPaxTypeFareHolidaysSupplier.setBaseFare(opsPaxTypeBaseFare);

            //Setting Total Fare
            OpsTotalFare opsPaxTypeTotalFare = new OpsTotalFare();

            opsPaxTypeTotalFare.setAmount(new Double(HolidaysPaxTypeFares.getAmountAfterTax()));
            opsPaxTypeTotalFare.setCurrencyCode(HolidaysPaxTypeFares.getCurrencyCode());

            opsPaxTypeFareHolidaysSupplier.setTotalFare(opsPaxTypeTotalFare);

            //Setting Taxes
            opsPaxTypeFareHolidaysSupplier.setTaxes(parentAdapter.getOpsTaxes(HolidaysPaxTypeFares.getTaxes()));

            //Setting Supplier Commercials
            List<OpsHolidaysPaxSupplierCommercial> supplierCommercialsList = new ArrayList<OpsHolidaysPaxSupplierCommercial>();

            if(HolidaysPaxTypeFares.getSupplierCommercials()!= null && HolidaysPaxTypeFares.getSupplierCommercials().size() > 0)
            {
                for(int j=0;j<HolidaysPaxTypeFares.getSupplierCommercials().size();j++)
                {
                    SupplierCommercial supplierCommercial = HolidaysPaxTypeFares.getSupplierCommercials().get(j);

                    OpsHolidaysPaxSupplierCommercial opsHolidaysPaxSupplierCommercial = new OpsHolidaysPaxSupplierCommercial();

                    opsHolidaysPaxSupplierCommercial.setCommercialName(supplierCommercial.getCommercialName());
                    opsHolidaysPaxSupplierCommercial.setCommercialType(supplierCommercial.getCommercialType());
                    opsHolidaysPaxSupplierCommercial.setCommercialAmount(supplierCommercial.getCommercialAmount());
                    opsHolidaysPaxSupplierCommercial.setCommercialType(supplierCommercial.getCommercialCurrency());

                    supplierCommercialsList.add(opsHolidaysPaxSupplierCommercial);
                }
            }

            opsPaxTypeFareHolidaysSupplier.setSupplierCommercials(supplierCommercialsList);

            OpsPaxTypeFareList.add(opsPaxTypeFareHolidaysSupplier);
        }

        return OpsPaxTypeFareList;
    }

    private List<OpsPaxTypeFare> getPaxTypeFaresClient(List<HolidaysPaxTypeFares> HolidaysPaxTypeFaresList) {

        List<OpsPaxTypeFare> OpsPaxTypeFareList = new ArrayList<OpsPaxTypeFare>();

        for (int i = 0; i < HolidaysPaxTypeFaresList.size(); i++) {
            HolidaysPaxTypeFares HolidaysPaxTypeFares = HolidaysPaxTypeFaresList.get(i);

            OpsPaxTypeFareHolidaysClient opsPaxTypeFareHolidaysClient = new OpsPaxTypeFareHolidaysClient();

            //Setting Pax Type
            opsPaxTypeFareHolidaysClient.setPaxType(HolidaysPaxTypeFares.getType());

            //Setting Base Fare
            OpsBaseFare opsPaxTypeBaseFare = new OpsBaseFare();

            opsPaxTypeBaseFare.setAmount(new Double(HolidaysPaxTypeFares.getAmountBeforeTax()));
            opsPaxTypeBaseFare.setCurrencyCode(HolidaysPaxTypeFares.getCurrencyCode());

            opsPaxTypeFareHolidaysClient.setBaseFare(opsPaxTypeBaseFare);

            //Setting Total Fare
            OpsTotalFare opsPaxTypeTotalFare = new OpsTotalFare();

            opsPaxTypeTotalFare.setAmount(new Double(HolidaysPaxTypeFares.getAmountAfterTax()));
            opsPaxTypeTotalFare.setCurrencyCode(HolidaysPaxTypeFares.getCurrencyCode());

            opsPaxTypeFareHolidaysClient.setTotalFare(opsPaxTypeTotalFare);

            //Setting Taxes
            opsPaxTypeFareHolidaysClient.setTaxes(parentAdapter.getOpsTaxes(HolidaysPaxTypeFares.getTaxes()));

            //Setting Client Commercials
            List<ClientEntityCommercial> clientEntityCommercialsList = HolidaysPaxTypeFares.getClientEntityCommercials();
            List<OpsClientEntityCommercial> opsClientEntityCommercialsList = new ArrayList<OpsClientEntityCommercial>();

            if(clientEntityCommercialsList!=null && clientEntityCommercialsList.size() > 0) {
                for(int j=0;j<clientEntityCommercialsList.size();j++)
                {
                    ClientEntityCommercial clientEntityCommercial = clientEntityCommercialsList.get(j);

                    OpsClientEntityCommercial opsClientEntityCommercial = new OpsClientEntityCommercial();

                    opsClientEntityCommercial.setClientID(clientEntityCommercial.getClientID());
                    opsClientEntityCommercial.setCommercialEntityID(clientEntityCommercial.getCommercialEntityID());
                    opsClientEntityCommercial.setCommercialEntityType(clientEntityCommercial.getCommercialEntityType());
                    opsClientEntityCommercial.setParentClientID(clientEntityCommercial.getClientID());

                    List<ClientCommercial> clientCommercials = clientEntityCommercial.getClientCommercials();

                    List<OpsPaxRoomClientCommercial> opsClientCommercials = new ArrayList<OpsPaxRoomClientCommercial>();

                    for (int k = 0; k < clientCommercials.size(); k++) {
                        OpsPaxRoomClientCommercial opsPaxRoomClientCommercial = new OpsPaxRoomClientCommercial();
                        ClientCommercial clientCommercial = clientCommercials.get(k);
                        opsPaxRoomClientCommercial.setCommercialAmount(Double.parseDouble(clientCommercial.getCommercialAmount()));
                        opsPaxRoomClientCommercial.setCommercialCurrency(clientCommercial.getCommercialCurrency());
                        opsPaxRoomClientCommercial.setCommercialName(clientCommercial.getCommercialName());
                        opsPaxRoomClientCommercial.setCommercialType(clientCommercial.getCommercialType());
                        opsPaxRoomClientCommercial.setCompanyFlag(clientCommercial.getCompanyFlag());
                        opsClientCommercials.add(opsPaxRoomClientCommercial);
                    }

                    opsClientEntityCommercial.setOpsPaxRoomClientCommercial(opsClientCommercials);
                    opsClientEntityCommercialsList.add(opsClientEntityCommercial);
                }
            }
            opsPaxTypeFareHolidaysClient.setOpsClientEntityCommercial(opsClientEntityCommercialsList);

            OpsPaxTypeFareList.add(opsPaxTypeFareHolidaysClient);
        }

        return OpsPaxTypeFareList;
    }

    private OpsOrderDetails getOpsHolidayTransferOrderInfo(HolidaysDetails holidaysDetails, OpsOrderDetails opsCompnentOrderDetails, HolidaysTransferDetails holidaysTransferDetails) {

        opsCompnentOrderDetails.setClientCommercials(holidaysTransferDetails.getClientCommercials().stream().map(
                clientCommercial -> parentAdapter.getOpsOrderClientCommercial(clientCommercial)).collect(Collectors.toList()));

        opsCompnentOrderDetails.setSupplierCommercials(holidaysTransferDetails.getSupplierCommercials().stream().
                map(supplierCommercial -> parentAdapter.getOpsOrderSupplierCommercial(supplierCommercial)).
                collect(Collectors.toList()));

        OpsTransferDetails opsTransferDetails = new OpsTransferDetails();

        //Setting supplier Price Info
        OpsTransferSupplierPriceInfo opsTransferSupplierPriceInfo = new OpsTransferSupplierPriceInfo();

        opsTransferSupplierPriceInfo.setSupplierPrice(holidaysTransferDetails.getSupplierPriceInfo().getSupplierPriceAfterTax());
        opsTransferSupplierPriceInfo.setCurrencyCode(holidaysTransferDetails.getSupplierPriceInfo().getCurrencyCode());
        opsTransferSupplierPriceInfo.setOpsPaxTypeFare(getPaxTypeFaresSupplier(holidaysTransferDetails.getSupplierPriceInfo().getPaxTypeFares()));

        opsTransferDetails.setOpsTransferSupplierPriceInfo(opsTransferSupplierPriceInfo);

        //Setting Total Price Info
        OpsTransferTotalPriceInfo opsTransferTotalPriceInfo = new OpsTransferTotalPriceInfo();

        opsTransferTotalPriceInfo.setCurrencyCode(holidaysTransferDetails.getTotalPriceInfo().getCurrencyCode());
        opsTransferTotalPriceInfo.setTotalPrice(holidaysTransferDetails.getTotalPriceInfo().getTotalPriceAfterTax());
        opsTransferTotalPriceInfo.setOpsTaxes(parentAdapter.getOpsTaxes(holidaysTransferDetails.getTotalPriceInfo().getTaxes()));
        opsTransferTotalPriceInfo.setOpsPaxTypeFare(getPaxTypeFaresClient(holidaysTransferDetails.getTotalPriceInfo().getPaxTypeFares()));
        //Setting Base Fare
        OpsBaseFare opsBaseFare = new OpsBaseFare();

        opsBaseFare.setAmount(new Double(holidaysTransferDetails.getTotalPriceInfo().getTotalPriceBeforeTax()));
        opsBaseFare.setCurrencyCode(holidaysTransferDetails.getTotalPriceInfo().getCurrencyCode());

        opsTransferTotalPriceInfo.setOpsBaseFare(opsBaseFare);
        //Setting Receivables
        OpsReceivables opsReceivables = new OpsReceivables();

        opsReceivables.setAmount(holidaysTransferDetails.getTotalPriceInfo().getReceivables().getAmount());
        opsReceivables.setCurrencyCode(holidaysTransferDetails.getTotalPriceInfo().getReceivables().getCurrencyCode());

        List<OpsReceivable> opsReceivableList = new ArrayList<OpsReceivable>();

        for (int k = 0; k < holidaysTransferDetails.getTotalPriceInfo().getReceivables().getReceivable().size(); k++) {
            Receivable receivable = holidaysTransferDetails.getTotalPriceInfo().getReceivables().getReceivable().get(k);

            OpsReceivable opsReceivable = new OpsReceivable();

            opsReceivable.setAmount(receivable.getAmount());
            opsReceivable.setCode(receivable.getCode());
            opsReceivable.setCurrencyCode(receivable.getCurrencyCode());

            opsReceivableList.add(opsReceivable);
        }
        opsReceivables.setReceivable(opsReceivableList);
        opsTransferTotalPriceInfo.setOpsReceivables(opsReceivables);

        opsTransferDetails.setOpsTransferTotalPriceInfo(opsTransferTotalPriceInfo);

        //Setting Transfer Details
        opsTransferDetails.setTransferType(holidaysTransferDetails.getTransferType());
        opsTransferDetails.setName(holidaysTransferDetails.getTransferInfo().getName());
        opsTransferDetails.setDepartureCity(holidaysTransferDetails.getTransferInfo().getDepartureCity());
        opsTransferDetails.setArrivalCity(holidaysTransferDetails.getTransferInfo().getArrivalCity());
        opsTransferDetails.setDepartureDate(holidaysTransferDetails.getTransferInfo().getDepartureDate());
        opsTransferDetails.setArrivalDate(holidaysTransferDetails.getTransferInfo().getArrivalCity());
        opsTransferDetails.setDescription(holidaysTransferDetails.getTransferInfo().getDescription());
        opsTransferDetails.setAirportName(holidaysTransferDetails.getTransferInfo().getAirportName());
        opsTransferDetails.setPickUpLocation(holidaysTransferDetails.getTransferInfo().getPickUpLocation());
        opsTransferDetails.setStart(holidaysTransferDetails.getTransferInfo().getStart());
        opsTransferDetails.setEnd(holidaysTransferDetails.getTransferInfo().getEnd());
        opsTransferDetails.setDuration(holidaysTransferDetails.getTransferInfo().getDuration());

        //Setting Pax Details
        opsTransferDetails.setOpsPaxInfo(holidaysTransferDetails.getPaxInfo().stream().map(paxInfo1 -> accommodationAdapter.getOpsAccommodationPaxInfo(paxInfo1)).collect(Collectors.toList()));


        opsCompnentOrderDetails.setTransferDetails(opsTransferDetails);

        return opsCompnentOrderDetails;
    }

    private OpsOrderDetails getOpsHolidayInsuranceOrderInfo(HolidaysDetails holidaysDetails, OpsOrderDetails opsCompnentOrderDetails, HolidaysInsuranceDetails holidaysInsuranceDetails) {

        opsCompnentOrderDetails.setClientCommercials(holidaysInsuranceDetails.getClientEntityCommercials().stream().map(
                clientCommercial -> parentAdapter.getOpsOrderClientCommercial(clientCommercial)).collect(Collectors.toList()));

        opsCompnentOrderDetails.setSupplierCommercials(holidaysInsuranceDetails.getSupplierCommercials().stream().
                map(supplierCommercial -> parentAdapter.getOpsOrderSupplierCommercial(supplierCommercial)).
                collect(Collectors.toList()));

        OpsInsuranceDetails opsInsuranceDetails = new OpsInsuranceDetails();

        //Setting supplier Price Info
        OpsInsuranceSupplierPriceInfo opsInsuranceSupplierPriceInfo = new OpsInsuranceSupplierPriceInfo();

        opsInsuranceSupplierPriceInfo.setSupplierPrice(holidaysInsuranceDetails.getSupplierPriceInfo().getSupplierPriceAfterTax());
        opsInsuranceSupplierPriceInfo.setCurrencyCode(holidaysInsuranceDetails.getSupplierPriceInfo().getCurrencyCode());
        opsInsuranceSupplierPriceInfo.setOpsPaxTypeFare(getPaxTypeFaresSupplier(holidaysInsuranceDetails.getSupplierPriceInfo().getPaxTypeFares()));

        opsInsuranceDetails.setOpsInsuranceSupplierPriceInfo(opsInsuranceSupplierPriceInfo);

        //Setting Total Price Info
        OpsInsuranceTotalPriceInfo opsInsuranceTotalPriceInfo = new OpsInsuranceTotalPriceInfo();

        opsInsuranceTotalPriceInfo.setCurrencyCode(holidaysInsuranceDetails.getTotalPriceInfo().getCurrencyCode());
        opsInsuranceTotalPriceInfo.setTotalPrice(holidaysInsuranceDetails.getTotalPriceInfo().getTotalPriceAfterTax());
        opsInsuranceTotalPriceInfo.setOpsTaxes(parentAdapter.getOpsTaxes(holidaysInsuranceDetails.getTotalPriceInfo().getTaxes()));
        opsInsuranceTotalPriceInfo.setOpsPaxTypeFare(getPaxTypeFaresClient(holidaysInsuranceDetails.getTotalPriceInfo().getPaxTypeFares()));
        //Setting Base Fare
        OpsBaseFare opsBaseFare = new OpsBaseFare();

        opsBaseFare.setAmount(new Double(holidaysInsuranceDetails.getTotalPriceInfo().getTotalPriceBeforeTax()));
        opsBaseFare.setCurrencyCode(holidaysInsuranceDetails.getTotalPriceInfo().getCurrencyCode());

        opsInsuranceTotalPriceInfo.setOpsBaseFare(opsBaseFare);
        //Setting Receivables
        OpsReceivables opsReceivables = new OpsReceivables();

        opsReceivables.setAmount(holidaysInsuranceDetails.getTotalPriceInfo().getReceivables().getAmount());
        opsReceivables.setCurrencyCode(holidaysInsuranceDetails.getTotalPriceInfo().getReceivables().getCurrencyCode());

        List<OpsReceivable> opsReceivableList = new ArrayList<OpsReceivable>();

        for (int k = 0; k < holidaysInsuranceDetails.getTotalPriceInfo().getReceivables().getReceivable().size(); k++) {
            Receivable receivable = holidaysInsuranceDetails.getTotalPriceInfo().getReceivables().getReceivable().get(k);

            OpsReceivable opsReceivable = new OpsReceivable();

            opsReceivable.setAmount(receivable.getAmount());
            opsReceivable.setCode(receivable.getCode());
            opsReceivable.setCurrencyCode(receivable.getCurrencyCode());

            opsReceivableList.add(opsReceivable);
        }
        opsReceivables.setReceivable(opsReceivableList);
        opsInsuranceTotalPriceInfo.setOpsReceivables(opsReceivables);

        opsInsuranceDetails.setOpsInsuranceTotalPriceInfo(opsInsuranceTotalPriceInfo);

        //Setting Transfer Details
        opsInsuranceDetails.setInsuranceType(holidaysInsuranceDetails.getInsuranceType());
        opsInsuranceDetails.setName(holidaysInsuranceDetails.getInsCoverageDetail().getName());
        opsInsuranceDetails.setDescription(holidaysInsuranceDetails.getInsCoverageDetail().getDescription());
        opsInsuranceDetails.setinsuranceId(holidaysInsuranceDetails.getInsCoverageDetail().getId());

        //Setting Pax Details
        opsInsuranceDetails.setOpsPaxInfo(holidaysInsuranceDetails.getPaxInfo().stream().map(paxInfo1 -> accommodationAdapter.getOpsAccommodationPaxInfo(paxInfo1)).collect(Collectors.toList()));

        opsCompnentOrderDetails.setInsuranceDetails(opsInsuranceDetails);

        return opsCompnentOrderDetails;
    }

    private OpsOrderDetails getOpsHolidayExtrasOrderInfo(HolidaysDetails holidaysDetails, OpsOrderDetails opsCompnentOrderDetails, HolidaysExtrasDetails holidaysExtrasDetails) {

        opsCompnentOrderDetails.setClientCommercials(holidaysExtrasDetails.getClientEntityCommercials().stream().map(
                clientCommercial -> parentAdapter.getOpsOrderClientCommercial(clientCommercial)).collect(Collectors.toList()));

        opsCompnentOrderDetails.setSupplierCommercials(holidaysExtrasDetails.getSupplierCommercials().stream().
                map(supplierCommercial -> parentAdapter.getOpsOrderSupplierCommercial(supplierCommercial)).
                collect(Collectors.toList()));

        OpsHolidaysExtrasDetails opsHolidaysExtrasDetails = new OpsHolidaysExtrasDetails();

        //Setting supplier Price Info
        OpsHolidaysSupplierPriceInfo opsHolidaysSupplierPriceInfo = new OpsHolidaysSupplierPriceInfo();

        opsHolidaysSupplierPriceInfo.setSupplierPrice(holidaysExtrasDetails.getSupplierPriceInfo().getSupplierPriceAfterTax());
        opsHolidaysSupplierPriceInfo.setCurrencyCode(holidaysExtrasDetails.getSupplierPriceInfo().getCurrencyCode());
        opsHolidaysSupplierPriceInfo.setOpsPaxTypeFare(getPaxTypeFaresSupplier(holidaysExtrasDetails.getSupplierPriceInfo().getPaxTypeFares()));

        opsHolidaysExtrasDetails.setOpsHolidaysSupplierPriceInfo(opsHolidaysSupplierPriceInfo);

        //Setting Total Price Info
        OpsHolidaysTotalPriceInfo opsHolidaysTotalPriceInfo = new OpsHolidaysTotalPriceInfo();

        opsHolidaysTotalPriceInfo.setCurrencyCode(holidaysExtrasDetails.getTotalPriceInfo().getCurrencyCode());
        opsHolidaysTotalPriceInfo.setTotalPrice(holidaysExtrasDetails.getTotalPriceInfo().getTotalPriceAfterTax());
        opsHolidaysTotalPriceInfo.setOpsTaxes(parentAdapter.getOpsTaxes(holidaysExtrasDetails.getTotalPriceInfo().getTaxes()));
        opsHolidaysTotalPriceInfo.setOpsPaxTypeFare(getPaxTypeFaresClient(holidaysExtrasDetails.getTotalPriceInfo().getPaxTypeFares()));
        //Setting Base Fare
        OpsBaseFare opsBaseFare = new OpsBaseFare();

        opsBaseFare.setAmount(new Double(holidaysExtrasDetails.getTotalPriceInfo().getTotalPriceBeforeTax()));
        opsBaseFare.setCurrencyCode(holidaysExtrasDetails.getTotalPriceInfo().getCurrencyCode());

        opsHolidaysTotalPriceInfo.setOpsBaseFare(opsBaseFare);
        //Setting Receivables
        OpsReceivables opsReceivables = new OpsReceivables();

        if(holidaysExtrasDetails.getTotalPriceInfo().getReceivables()!=null) {
            opsReceivables.setAmount(holidaysExtrasDetails.getTotalPriceInfo().getReceivables().getAmount());
            opsReceivables.setCurrencyCode(holidaysExtrasDetails.getTotalPriceInfo().getReceivables().getCurrencyCode());
        }

        List<OpsReceivable> opsReceivableList = new ArrayList<OpsReceivable>();

        for(int k=0;holidaysExtrasDetails.getTotalPriceInfo().getReceivables()!=null && k<holidaysExtrasDetails.getTotalPriceInfo().getReceivables().getReceivable().size();k++)
        {
            Receivable receivable = holidaysExtrasDetails.getTotalPriceInfo().getReceivables().getReceivable().get(k);

            OpsReceivable opsReceivable = new OpsReceivable();

            opsReceivable.setAmount(receivable.getAmount());
            opsReceivable.setCode(receivable.getCode());
            opsReceivable.setCurrencyCode(receivable.getCurrencyCode());

            opsReceivableList.add(opsReceivable);
        }
        opsReceivables.setReceivable(opsReceivableList);
        opsHolidaysTotalPriceInfo.setOpsReceivables(opsReceivables);

        opsHolidaysExtrasDetails.setOpsHolidaysTotalPriceInfo(opsHolidaysTotalPriceInfo);

        //Setting Transfer Details
        opsHolidaysExtrasDetails.setExtraType(holidaysExtrasDetails.getExtraType());
        opsHolidaysExtrasDetails.setName(holidaysExtrasDetails.getExtrasInfo().getName());
        opsHolidaysExtrasDetails.setDescription(holidaysExtrasDetails.getExtrasInfo().getDescription());
        opsHolidaysExtrasDetails.setCode(holidaysExtrasDetails.getExtrasInfo().getCode());
        opsHolidaysExtrasDetails.setQuantity(holidaysExtrasDetails.getExtrasInfo().getQuantity());

        //Setting Pax Details
        opsHolidaysExtrasDetails.setOpsPaxInfo(holidaysExtrasDetails.getPaxInfo().stream().map(paxInfo1 -> accommodationAdapter.getOpsAccommodationPaxInfo(paxInfo1)).collect(Collectors.toList()));

        //NEED TO SET EXTRA DETAILS IN OPS ORDER MODEL -  CONFIRM WITH ANANTH
        opsCompnentOrderDetails.setHolidaysExtrasDetails(opsHolidaysExtrasDetails);

        return opsCompnentOrderDetails;
    }

    private OpsOrderDetails getOpsHolidayExtensionNightsOrderInfo(HolidaysDetails holidaysDetails, OpsOrderDetails opsCompnentOrderDetails, HolidaysExtensionDetails holidaysExtensionDetails) {

        opsCompnentOrderDetails.setClientCommercials(holidaysExtensionDetails.getClientEntityCommercials().stream().map(
                clientCommercial -> parentAdapter.getOpsOrderClientCommercial(clientCommercial)).collect(Collectors.toList()));

        opsCompnentOrderDetails.setSupplierCommercials(holidaysExtensionDetails.getSupplierCommercials().stream().
                map(supplierCommercial -> parentAdapter.getOpsOrderSupplierCommercial(supplierCommercial)).
                collect(Collectors.toList()));

        OpsHolidaysExtensionNightDetails opsHolidaysExtensionNightDetails = new OpsHolidaysExtensionNightDetails();

        //Setting supplier Price Info
        OpsHolidaysSupplierPriceInfo opsHolidaysSupplierPriceInfo = new OpsHolidaysSupplierPriceInfo();

        opsHolidaysSupplierPriceInfo.setSupplierPrice(holidaysExtensionDetails.getSupplierPriceInfo().getSupplierPriceAfterTax());
        opsHolidaysSupplierPriceInfo.setCurrencyCode(holidaysExtensionDetails.getSupplierPriceInfo().getCurrencyCode());
        opsHolidaysSupplierPriceInfo.setOpsPaxTypeFare(getPaxTypeFaresSupplier(holidaysExtensionDetails.getSupplierPriceInfo().getPaxTypeFares()));

        opsHolidaysExtensionNightDetails.setOpsHolidaysSupplierPriceInfo(opsHolidaysSupplierPriceInfo);

        //Setting Total Price Info
        OpsHolidaysTotalPriceInfo opsHolidaysTotalPriceInfo = new OpsHolidaysTotalPriceInfo();

        opsHolidaysTotalPriceInfo.setCurrencyCode(holidaysExtensionDetails.getTotalPriceInfo().getCurrencyCode());
        opsHolidaysTotalPriceInfo.setTotalPrice(holidaysExtensionDetails.getTotalPriceInfo().getTotalPriceAfterTax());
        opsHolidaysTotalPriceInfo.setOpsTaxes(parentAdapter.getOpsTaxes(holidaysExtensionDetails.getTotalPriceInfo().getTaxes()));
        opsHolidaysTotalPriceInfo.setOpsPaxTypeFare(getPaxTypeFaresClient(holidaysExtensionDetails.getTotalPriceInfo().getPaxTypeFares()));
        //Setting Base Fare
        OpsBaseFare opsBaseFare = new OpsBaseFare();

        opsBaseFare.setAmount(new Double(holidaysExtensionDetails.getTotalPriceInfo().getTotalPriceBeforeTax()));
        opsBaseFare.setCurrencyCode(holidaysExtensionDetails.getTotalPriceInfo().getCurrencyCode());

        opsHolidaysTotalPriceInfo.setOpsBaseFare(opsBaseFare);
        //Setting Receivables
        OpsReceivables opsReceivables = new OpsReceivables();

        opsReceivables.setAmount(holidaysExtensionDetails.getTotalPriceInfo().getReceivables().getAmount());
        opsReceivables.setCurrencyCode(holidaysExtensionDetails.getTotalPriceInfo().getReceivables().getCurrencyCode());

        List<OpsReceivable> opsReceivableList = new ArrayList<OpsReceivable>();

        for(int k=0;k<holidaysExtensionDetails.getTotalPriceInfo().getReceivables().getReceivable().size();k++)
        {
            Receivable receivable = holidaysExtensionDetails.getTotalPriceInfo().getReceivables().getReceivable().get(k);

            OpsReceivable opsReceivable = new OpsReceivable();

            opsReceivable.setAmount(receivable.getAmount());
            opsReceivable.setCode(receivable.getCode());
            opsReceivable.setCurrencyCode(receivable.getCurrencyCode());

            opsReceivableList.add(opsReceivable);
        }
        opsReceivables.setReceivable(opsReceivableList);
        opsHolidaysTotalPriceInfo.setOpsReceivables(opsReceivables);

        opsHolidaysExtensionNightDetails.setOpsHolidaysTotalPriceInfo(opsHolidaysTotalPriceInfo);

        //Setting Extension Night Details
        //Setting Room Info
        opsHolidaysExtensionNightDetails.setRoomTypeInfo(accommodationAdapter.getOpsRoomTypeInfo(holidaysExtensionDetails.getRoomInfo().getRoomTypeInfo()));

        opsHolidaysExtensionNightDetails.setRatePlanInfo(accommodationAdapter.getOpsRatePlanInfo(holidaysExtensionDetails.getRoomInfo().getRatePlanInfo()));


        //Setting Pax Details
        opsHolidaysExtensionNightDetails.setOpsPaxInfo(holidaysExtensionDetails.getPaxInfo().stream().map(paxInfo1 -> accommodationAdapter.getOpsAccommodationPaxInfo(paxInfo1)).collect(Collectors.toList()));

        //NEED TO SET EXTRA DETAILS IN OPS ORDER MODEL -  CONFIRM WITH ANANTH
        opsCompnentOrderDetails.setHolidaysExtensionNightDetails(opsHolidaysExtensionNightDetails);

        return opsCompnentOrderDetails;
    }


}
