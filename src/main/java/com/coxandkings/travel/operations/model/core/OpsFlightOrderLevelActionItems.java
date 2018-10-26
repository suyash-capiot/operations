package com.coxandkings.travel.operations.model.core;

import com.coxandkings.travel.operations.enums.ActionItemStatus;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class OpsFlightOrderLevelActionItems {

    private Map<String, OpsActionItem> flightOrderActionItemsMap = new TreeMap<>();
    private Map<String, OpsActionItem> sortedFlightOrderActionItemsMap = new TreeMap<>();

    private String[][] flightOrderActionItemsData = {
            {"View Reference Customer Details", "viewReferenceCustomerDetails", "false", ""},
            {"View Offer Details", "viewOfferDetails", "false", ""},
            {"Reinstate Booking", "reinstateBooking", "true", ""},
            {"Booking is No Show", "bookingIsNoShow", "true", ""},
            {"Update Payment Schedule", "updatePaymentSchedule", "true", ""},
            {"Update Client Reconfirmation Date", "updateClientReconfirmationDate", "true", ""},
            {"Update Supplier Reconfirmation Date", "updateSupplierReconfirmationDate", "true", ""},
            {"View Client / Customer Reconfirmation Flags", "viewClientCustomerReconfirmationFlags", "false", ""},
            {"View Supplier Reconfirmation Flags", "viewSupplierReconfirmationFlags", "false", ""},
            {"Update Time Limit Expiry Date & Time", "timeLimitExpiry", "true", ""},
            {"Update Booking Status", "bookingStatus", "true", ""},
            {"Confirm Booking using Inventory", "confirmBookingUsingInventory", "false", ""},
            {"Edit Supplier Cancellation Charges", "editSupplierCancellationCharges", "true", ""},
            {"Edit Supplier Amendment Charges", "editSupplierAmendmentCharges", "true", ""},
            {"Edit Company Cancellation Charges", "editCompanyCancellationCharges", "true", ""},
            {"Edit Company Amendment Charges", "editCompanyAmendmentCharges", "true", ""},
            {"Generate Voucher", "generateVoucher", "false", ""},
            {"Update Voucher", "updateVoucher", "false", ""},
            {"Update Ticket", "updateTicket", "false", ""},
            {"Do Ticketing", "doTicketing", "false", ""},
            {"Change Supplier / Supplier Price", "changeSupplierPrice", "false", ""},
            {"Discount on Selling Price", "discountOnSellingPrice", "false", ""},
            {"Discount on Supplier Price", "discountOnSupplierPrice", "false", ""},
            {"Supplement to Supplier Price", "supplementToSupplierPrice", "false", "Supplement on Supplier Price"},
            {"Supplier Commercials", "supplierCommercials", "false", ""},
            {"Client Commercials", "clientCommercials", "false", ""},
            {"Company Commercials", "companyCommercials", "false", ""},
            {"Update Other Special Information", "specialInformation", "true", ""},
            {"Modify Passenger Name", "modifyPassengerName", "true", ""},
            {"Generate Payment Advice", "generatePaymentAdvice", "false", ""},
            {"Login into Supplier System", "loginIntoSupplierSystem", "false", ""},
            {"View Gift Voucher Details", "viewGiftVoucherCodes", "false", ""},
            {"Enter User Remarks / Notes", "updateRemarkNotes", "false", ""},
            {"Supplier Rate Definition", "supplierRateDefinition", "true", ""},
            {"Define Supplier Set Up", "defineSupplierSetUp", "true", ""},
            {"Product Set Up", "productSetUp", "true", ""},
            {"Generate Refund Claim", "generateRefundClaim", "false", "Refunds"},
            {"Send Review Form To Customer", "sendReviewFormToCustomer", "false", ""},
            {"Send FeedbackForm To Customer", "sendFeedbackFormToCustomer", "false", ""},
            {"View Alternate Options sent", "viewAlternateOptionsSent", "false", ""},
            {"Search Alternate Options", "searchAlternateOptions", "false", ""},
            {"Follow Up for Alternate Options", "followUpForAlternateOptions", "false", ""},
            { "Update Confirmation Details","updateConfirmationDetails","true",""},
            {"Convert TimeLimit To Paid Booking", "convertTimeLimitToPaid", "true", ""},
            {"Update Product Booked Through Other Service Provider", "updateProductBookedThroughOtherServiceProvider", "false", ""},
            {"View Driver Details", "viewDriverDetails", "false", ""}
    };

    public OpsFlightOrderLevelActionItems() {
        init();
    }

    public void init() {
        flightOrderActionItemsMap.clear();
        for (String[] aFlightMenuItemInfo : flightOrderActionItemsData) {
            OpsActionItem anActionItem = new OpsActionItem(aFlightMenuItemInfo[0], aFlightMenuItemInfo[1],
                    ActionItemStatus.ACTIVE.getActionItemStatus(), new Boolean(aFlightMenuItemInfo[2]), aFlightMenuItemInfo[3]);
            flightOrderActionItemsMap.put(aFlightMenuItemInfo[1], anActionItem);
            sortedFlightOrderActionItemsMap.put(aFlightMenuItemInfo[0], anActionItem); //add to map for sorting by name!
        }
    }

    public ArrayList<OpsActionItem> getFlightOrderActionItemsList() {
        ArrayList<OpsActionItem> flightActionItemsList = new ArrayList<>(sortedFlightOrderActionItemsMap.values());
        return flightActionItemsList;
    }

    public Map<String, OpsActionItem> getFlightOrderActionItemsMap() {
        return flightOrderActionItemsMap;
    }
}


