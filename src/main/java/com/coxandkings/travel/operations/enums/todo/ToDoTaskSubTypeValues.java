package com.coxandkings.travel.operations.enums.todo;

public enum ToDoTaskSubTypeValues {
    ALTERNATE_OPTIONS("Alternate Options"),
    AMEND_CLIENT_COMMERCIAL("Amend Client Commercials"),
    AMEND_COMPANY_COMMERCIAL("Amend Company Commercials"),
    AMEND_SUPPLIER_COMMERCIAL("Amend Supplier Commercials"),
    BANKFUND("Bank Fund"),
    BARTER("Barter"),
    BOOKING("Booking"),
    CHANGE_SUPPLIER_PRICE("Change Supplier Price"),
    CHEAPER_PRICE_UPDATES("Cheaper Price Updates"),
    CLIENT_COMMERCIAL_STATEMENT("Client Commercial Statement"),
    CLIENT_COMMERCIAL_STATEMENT_BILL_PASSING("Client Commercial Statement Bill Passing"),
    CLIENT_COMMERCIAL_STATEMENT_PAYMENT_ADVICE("Client Commercial Statement Payment Advice"),
    CLIENTSETTLEMENT("Client Settlement"),
    COMMERCIAL_STATEMENT("Commercial Statement"),
    COMPANY_CHARGES_AMENDMENT("Company Charges Amendment"),
    COMPANY_CHARGES_CANCELLATION("Company Charges Cancellation"),
    COMPLAINT("Complaint"),
    DISCOUNT_ON_SUPPLIER_PRICE("Discount On Supplier Price"),
    ENQUIRY("Enquiry"),
    EMAILTAG("Email Tag"),
    FEEDBACK("Feedback"),
    FLIGHT_DISCREPANCY("Flight Discrepancy"),
    FULL_CANCELLATION("Full Cancellation"),
    GIFTVOUCHER("Gift Voucher"),
    IMPREST("Imprest"),
    INDENT("Indent"),
    MANAGE_FAILURE("Manage Failure"),
    ORDER("Order"),
    ORDER_DUPLICATE("Duplicate Order"),
    ON_REQUEST("On Request"),
    PART_PAYMENT("Part Payment"),
    PASSENGER("Passenger"),
    PAYMENT_ADVICE("Payment Advice"),
    PAYMENT_ADVICE_SUPPLIER_BILL_PASSING("Payment Advice Supplier Bill Passing"),
    PAYMENT_ADVICE_CLIENT_BILL_PASSING("Payment Advice Client Bill Passing"),
    PRODUCT_REVIEW("Product Review"),
    QUOTATION("Quotation"),
    RECON("Reconciliation"),
    REFUND("Refunds"),
    SELLING_PRICE("Selling Price"),
    SUPPLEMENT_ON_SUPPLIER_PRICE("Supplement On Supplier Price"),
    SUPPLIER_BILL_PASSING("Supplier Bill Passing"),
    SUPPLIER_CHARGES_AMENDMENT("Supplier Charges Amendment"),
    SUPPLIER_CHARGES_CANCELLATION("Supplier Charges Cancellation"),
    SUPPLIER_COMMERCIAL_STATEMENT("Supplier Commercial Statement"),
    SUPPLIER_COMMERCIAL_STATEMENT_BILL_PASSING("Supplier Commercial Statement Bill Passing"),
    SUPPLIER_COMMERCIAL_STATEMENT_PAYMENT_ADVICE("Supplier Commercial Statement Payment Advice"),
    SUPPLIER_SETTLEMENT("Supplier Settlement"),
    TIME_LIMIT_BOOKING("Time Limit Booking"),
    TRANSFER_FUND("Transfer Fund"),
    WAIVE_OFF("Waive Off"),
    RECONFIRMATION_CANCELLATIONS_FOR_CLIENT("Reconfirmation cancellation for client"),
    RECONFIRMATION_CANCELLATIONS_FOR_SUPPLIER("Reconfirmation cancellation for supplier"),
    BOOKING_RECONFIRMATION_FOR_CLIENT("Booking Reconfirmation for client"),
    BOOKING_RECONFIRMATION_FOR_SUPPLIER("Booking Reconfirmation for supplier"),
    RECONFIRMATION_ALTERNATE_OPTIONS_FOR_CLIENT("Reconfirmation alternate options for client"),
    RECONFIRMATION_ALTERNATE_OPTIONS_FOR_SUPPLIER("Reconfirmation alternate options for supplier"),
    RECONFIRMATION_FOLLOW_UP_FOR_SUPPLIER("Reconfirmation follow up for supplier"),
    RECONFIRMATION_FOLLOW_UP_FOR_CLIENT("Reconfirmation follow up for client"),
    PRODUCT_SHARING("Product sharing pax information"),
    CONFIGURE_DOCUMENTS_IN_MDM("Configure Documents in MDM"),
    COLLECT_DOCUMENTS_FOR_BOOKING("Collect Documents for Booking"),
    COLLECT_DOCUMENTS_FOR_ORDER("Collect Documents for Order"),
    COLLECT_DOCUMENTS("Collect Documents from Customer"),
    SEND_DOCUMENTS("Send Documents to Customer"),
    HANDOVER_DOCUMENT_GENERATION("Handover Document Generation"),
    FOREX_INDENT("Forex Indent"),
    FOREX_REQUEST("Forex Request"),
    CONVERT_TIME_LIMIT_BOOKING_TO_PAID("Convert Time Limit Booking To Paid"),
    VERIFY_OR_REJECT_DOCUMENTS("Verify or Reject Documents"),
    ADD_DRIVER_DETAILS("Add Driver Details"),
    OFFLINE_BOOKING("Offline Booking"),
    PAYMENT_RECON_REFUND("PaymentReconRefund"),//added as per Tejas dalal requirement (payment gateway team)
    PAYMENT_PENDING("Payment Pending"),
    PAYMENT_PENDING_AND_BOOKING_NOT_CONFIRMED("Payment Pending and Booking not Confirmed"),
    BOOKING_NOT_CONFIRMED("Booking not Confirmed"),
    REFUND_TO_CLIENT("Refund to Client"),
    ABSORB_BY_COMPANY("Absorb by Company"),
    MAIN_TASK("Main Task");

    private String subTaskType;

    private ToDoTaskSubTypeValues(String newSubTaskType) {
        subTaskType = newSubTaskType;
    }

    public static ToDoTaskSubTypeValues fromString(String aSubTaskType) {
        ToDoTaskSubTypeValues aSubType = null;
        if (aSubTaskType != null && aSubTaskType.trim().length() > 0) {
            for (ToDoTaskSubTypeValues tmpSubTaskType : ToDoTaskSubTypeValues.values()) {
                if (tmpSubTaskType.getSubTaskType().equalsIgnoreCase(aSubTaskType)) {
                    aSubType = tmpSubTaskType;
                    break;
                }
            }
        }

        return aSubType;
    }

    public static ToDoTaskSubTypeValues fromName(String aSubTaskType) {
        ToDoTaskSubTypeValues aSubType = null;
        if (aSubTaskType != null && aSubTaskType.trim().length() > 0) {
            for (ToDoTaskSubTypeValues tmpSubTaskType : ToDoTaskSubTypeValues.values()) {
                if (tmpSubTaskType.name().equalsIgnoreCase(aSubTaskType)) {
                    aSubType = tmpSubTaskType;
                    break;
                }
            }
        }

        return aSubType;
    }

    public String getSubTaskType() {
        return subTaskType;
    }

}
