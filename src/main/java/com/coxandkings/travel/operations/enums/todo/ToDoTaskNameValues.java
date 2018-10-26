package com.coxandkings.travel.operations.enums.todo;

import org.springframework.util.StringUtils;

public enum ToDoTaskNameValues {
    CONFIRM("Confirm"),
    RECONFIRM("Reconfirm"),
    CANCEL("Cancel"),
    BOOK("Book"),
    SEARCH("Search"),
    APPROVE("Approve"),
    UPDATE("Update"),
    VERIFICATION("Verification"),
    SETTLEMENT("Settlement"),
    AMEND("Amend"),
    MANAGE_NO_SHOW("Manage No Show"),
    GENERATE_SERVICE_ORDER("Generate Service Order"),
    ACTION("Action"),
    ADD_REMARK("Add Remark"),
    CANCEL_AND_REFUND("Cancel And Refund"), //for cancellation and refund manage failure
    SEARCH_AND_BOOK("Search And Book"), //for search and book a product manage failure
    UPDATE_PNR("Update PNR"), //for updating pnr for a product manage failure
    REFUND("Refund"), //for refund for a product manage failure
    EMAILTAG("Add Tags to Email Communication"),
    INVESTIGATE_AND_ACTION("Investigate and Action"), //report to technical team in case of payment failure
    CORRECTION("Correction"),
    CONFIGURE("Configure Documents"),
    CUSTOMER_DOCUMENTATION("Customer Documentation"),
    PRODUCT_SHARING("Product Sharing"),
    MANAGE_RECONFIRMATION("Manage Reconfirmation"),
    VERIFY("Verify"),
    INTERNAL_SUPPLIER_CREATOR("Internal Supplier Creator"),// report to internal supplier creator team to create a supplier in case of offline booking
    BOOKING_RECONFIRMATION("Booking Reconfirmation"),
    PAYMENT_RECON_REFUND("PaymentReconRefund"); //added as per Tejas dalal requirement (payment gateway team)

    private String value;

    ToDoTaskNameValues(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ToDoTaskNameValues fromString(String toDoTaskNameValue) {
        ToDoTaskNameValues toDoTaskNameValues = null;

        if (StringUtils.isEmpty(toDoTaskNameValue)) {
            return toDoTaskNameValues;
        }

        for (ToDoTaskNameValues tmpToDoTaskNameValues : ToDoTaskNameValues.values()) {
            if (toDoTaskNameValue.equalsIgnoreCase(tmpToDoTaskNameValues.getValue())) {
                toDoTaskNameValues = tmpToDoTaskNameValues;
            }
        }

        return toDoTaskNameValues;
    }
}
