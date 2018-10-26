package com.coxandkings.travel.operations.model.core;

import com.coxandkings.travel.operations.enums.ActionItemStatus;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class OpsHotelOrderLevelActionItems {

    private ArrayList<OpsActionItem> actionItemsList = new ArrayList<>();

    private Map<String,OpsActionItem> hotelOrderActionItemsMap = new TreeMap<>();
    private Map<String,OpsActionItem> sortedHotelOrderActionItemsMap = new TreeMap<>();

    private String[][] hotelOrderActionItemsData = {
            {"View Reference Customer Details ", "viewReferenceCustomerDetails", "false", ""},
            {"View Offer Details", "viewOfferDetails", "false", ""},
            {"Reinstate Booking", "reinstateBooking", "true", "Manage Reinstate a Booking"},
            {"Booking is No Show", "bookingIsNoShow", "true", ""},
            {"Update Payment Schedule", "updatePaymentSchedule", "true", ""},
            {"Update Client Reconfirmation Date", "updateClientReconfirmationDate", "true", ""},
            {"Update Supplier Reconfirmation Date", "updateSupplierReconfirmationDate", "true", ""},
            {"View Client / Customer Reconfirmation Flags", "viewClientCustomerReconfirmationFlags", "false", ""},
            {"View Supplier Reconfirmation Flags", "viewSupplierReconfirmationFlags", "false", ""},
            {"Update Time Limit Expiry Date & Time", "timeLimitExpiry", "true", "Manage Time Limit Bookings"},
            {"Update Booking Status", "bookingStatus", "true", ""},
            {"Confirm Booking using Inventory", "confirmBookingUsingInventory", "false", ""},
            {"Edit Supplier Cancellation Charges", "editSupplierCancellationCharges", "true", ""},
            {"Edit Supplier Amendment Charges", "editSupplierAmendmentCharges", "true", ""},
            {"Edit Company Cancellation Charges", "editCompanyCancellationCharges", "true", ""},
            {"Edit Company Amendment Charges", "editCompanyAmendmentCharges", "true", ""},
            {"Generate Voucher", "generateVoucher", "false", ""},
            {"Update Voucher", "updateVoucher", "false", ""},
            {"Update Ticket", "updateTicket", "false", ""},
//            {"Do Ticketing", "doTicketing", "false", ""},
            {"Discount on Selling Price", "discountOnSellingPrice", "false", ""},
            {"Update Other Special Information", "specialInformation", "true", ""},
            {"Modify Passenger Name", "modifyPassengerName", "true", ""},
            {"Generate Payment Advice", "generatePaymentAdvice", "false", ""},
            {"Login into Supplier System", "loginIntoSupplierSystem", "false", ""},
            {"View Merged booking details", "viewMergedBookingDetails", "false", ""},
            //{"View 3rd Party Voucher Codes", "viewThirdPartyVocherCode", "false", ""},
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
            {"Convert TimeLimit To Paid Booking", "convertTimeLimitToPaid", "true", ""}
    };

    public OpsHotelOrderLevelActionItems()   {
        super();
        init();
    }

    public void init()  {
        hotelOrderActionItemsMap.clear();
        for( String[] aHotelMenuItemInfo : hotelOrderActionItemsData )    {
            OpsActionItem anActionItem = new OpsActionItem( aHotelMenuItemInfo[0], aHotelMenuItemInfo[1],
                    ActionItemStatus.ACTIVE.getActionItemStatus(), new Boolean(aHotelMenuItemInfo[2]), aHotelMenuItemInfo[3]);
            hotelOrderActionItemsMap.put( aHotelMenuItemInfo[1], anActionItem );
            sortedHotelOrderActionItemsMap.put( aHotelMenuItemInfo[0], anActionItem ); //add to map for sorting by Name!!
        }
    }

    public Map<String,OpsActionItem> getHotelOrderActionItemsMap()  {
        return hotelOrderActionItemsMap;
    }

    public ArrayList<OpsActionItem> getHotelOrderActionItemsList()    {
        ArrayList<OpsActionItem> hotelActionItemsList = new ArrayList( sortedHotelOrderActionItemsMap.values() );
        return hotelActionItemsList;
    }
}


