package com.coxandkings.travel.operations.model.core;

import com.coxandkings.travel.operations.enums.ActionItemStatus;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class OpsHolidaysOrderLevelActionItems {

    private Map<String, OpsActionItem> holidaysOrderActionItemsMap = new TreeMap<>();
    private Map<String, OpsActionItem> sortedHolidaysOrderActionItemsMap = new TreeMap<>();

    private String[][] holidaysOrderActionItemsData = {
            {"View Reference Customer Details", "viewReferenceCustomerDetails", "false", ""},
            {"View Offer Details", "viewOfferDetails", "false", ""},
            {"Reinstate Booking", "reinstateBooking", "false", ""},
            {"Booking is No Show", "bookingIsNoShow", "false", ""},
            {"Update Payment Schedule", "updatePaymentSchedule", "false", ""},
            //{"Update Client Reconfirmation Date", "updateClientReconfirmationDate", "true" ,"" },--at pax level
            //{"Update Supplier Reconfirmation Date", "updateSupplierReconfirmationDate", "true",""  },--at pax level
            //{"View Client / Customer Reconfirmation Flags", "viewClientCustomerReconfirmationFlags", "false" ,"" },--at pax level
            //{"View Supplier Reconfirmation Flags", "viewSupplierReconfirmationFlags", "false",""  },--at pax level
            {"Update time Limit Expiry Date & Time", "timeLimitExpiry", "true", ""},
            {"View Driver Details", "viewDriverDetails", "false", ""}, //enabling this for testing purposes
            {"Update Product Booked Through Other Service Providers", "updateProductBookedThroughOtherServiceProviders", "false", ""},
            {"Update Flight Details", "updateFlightDetails", "false", ""},
            {"Update Pick up & Drop off Details", "updatePickupDropOffDetails", "false", ""},
            {"Update Boarding And Drop Off Time", "updateBoardingAndDropOffTime", "false", ""},
            {"Update Time Of Visit", "updateTimeOfVisit", "false", ""},
            {"View Vehicle Information", "viewVehicleInformation", "false", ""},
            {"Update Booking Status", "bookingStatus", "true", ""},
            {"Update Confirmation Details", "confirmationDetails", "true", ""},
            {"Update Confirmation Details  For holidays", "confirmationDetailsForHolidays", "false", ""},
            {"Confirm booking using Inventory", "confirmBookingUsingInventory", "false", ""},
            {"Edit Supplier Cancellation Charges", "editSupplierCancellationCharges", "true", ""},
            {"Edit Supplier Amendment Charges", "editSupplierAmendmentCharges", "true", ""},
            {"Edit Company Cancellation Charges", "editCompanyCancellationCharges", "true", ""},
            {"Edit Company Amendment Charges", "editCompanyAmendmentCharges", "true", ""},
            {"Generate Voucher", "generateVoucher", "false", ""},
            {"Update Voucher", "updateVoucher", "false", ""},
            {"Update Ticket", "updateTicket", "false", ""},
            {"Do Ticketing", "doTicketing", "false", ""},
            {"Change Supplier / Supplier Price", "changeSupplierPrice", "false", ""},
            //{"Discount on Selling Price", "discountOnSellingPrice", "false" ,"" },--at pax level
            //{"Discount on Supplier Price", "discountOnSupplierPrice", "false" ,"" },--at pax level
            //{"Supplement to Supplier Price", "supplementToSupplierPrice", "false" ,"" },--at pax level
            //{"Supplier Commercials", "supplierCommercials", "false" ,"" },--at pax level
            //{"Client Commercials", "clientCommercials", "false" ,"" },--at pax level
            //{"Company Commercials", "companyCommercials", "false" ,"" },--at pax level
            {"Update Other Special Information", "specialInformation", "true", ""},
            {"Modify Passenger Name", "modifyPassengerName", "true", ""},
            {"Generate Payment Advice", "generatePaymentAdvice", "false", ""},
            {"Login into Supplier System", "loginIntoSupplierSystem", "false", ""},
            {"View Itinerary In Brief", "viewItineraryInBrief", "false", ""},
            {"View Itinerary In Detail", "viewItineraryInDetail", "false", ""},
            {"View Costing Sheet/ SOM", "viewCostingSheetSOM", "false", ""},
            {"View Merged Booking Details", "viewMergedBookingDetails", "false", ""},
            //{"View Shared Booking Details", "viewSharedBookingDetails", "false" ,"" },--at pax level
            {"View Gift Voucher Details", "viewGiftVoucherCodes", "false", ""},
            {"Update Remarks / Notes", "updateRemarkNotes", "false", ""},
            {"View Alternate Options sent", "viewAlternateOptionsSent", "false", ""},
            {"Search Alternate Options", "searchAlternateOptions", "false", ""},
            {"Follow Up for Alternate Options", "followUpForAlternateOptions", "false", ""},
            {"Split Itinerary", "splitItinerary", "false", ""},
            {"Reverse Itinerary", "reverseItinerary", "false", ""},
            {"Edit Services In Package", "editServicesInPackage", "false", ""},
            {"Change Sequence", "changeSequence", "false", ""},
            {"Enter User Remarks / Notes", "enterUserRemarkNotes", "false", ""},
            {"Supplier Rate Definition", "supplierRateDefinition", "false", ""},
            {"Define Supplier Set Up", "defineSupplierSetUp", "false", ""},
            {"Product Set Up", "productSetUp", "false", ""},
            {"Follow Up for Alternate Options", "followUpForAlternateOptions", "false", ""},
            {"Generate Refund Claim", "generateRefundClaim", "false", ""},
            {"Send Review Form To Customer", "sendReviewFormToCustomer", "false", ""},
            {"Send Feedback Form To Customer", "sendFeedbackFormToCustomer", "false", ""},
            {"Add New Service", "addNewService", "false", ""},
            {"Tax Invoice", "taxInvoice", "false", ""}
    };

    public OpsHolidaysOrderLevelActionItems() {
        init();
    }

    public void init() {
        holidaysOrderActionItemsMap.clear();
        for (String[] holidaysMenuItemInfo : holidaysOrderActionItemsData) {
            OpsActionItem anActionItem = new OpsActionItem(holidaysMenuItemInfo[0], holidaysMenuItemInfo[1],
                    ActionItemStatus.ACTIVE.getActionItemStatus(), new Boolean(holidaysMenuItemInfo[2]), holidaysMenuItemInfo[3]);
            holidaysOrderActionItemsMap.put(holidaysMenuItemInfo[1], anActionItem);
            sortedHolidaysOrderActionItemsMap.put(holidaysMenuItemInfo[0], anActionItem); //add to map for sorting by name!
        }
    }

    public ArrayList<OpsActionItem> getHolidaysOrderActionItemsList() {
        ArrayList<OpsActionItem> holidaysActionItemsList = new ArrayList<>(sortedHolidaysOrderActionItemsMap.values());
        return holidaysActionItemsList;
    }

    public Map<String, OpsActionItem> getHolidaysOrderActionItemsMap() {
        return holidaysOrderActionItemsMap;
    }

}
