package com.coxandkings.travel.operations.enums.reconfirmation;

public enum CommonEnums {


    PAID("PAID"),
    CONFIRMED("OK"),
    TICKETED("TICKETED"),
    VOUCHERED("VOUCHERED"),
    ON_REQUEST("ON_REQUEST"),
    client_clientDetail("client.clientDetail"),
    client_hours("client.hours"),
    client_reconfirmationCutOff("client.reconfirmationCutOff"),

    supplier_supplierDetail("supplier.supplierDetail"),
    supplier_hours("supplier.hours"),
    supplier_reconfirmationCutOff("supplier.reconfirmationCutOff"),
    SUPPLIER_COMMUNICATION_SUBJECT("supplier.communication.subject"),
    SUPPLIER_REJECTION_SUBJECT("supplier.rejection.subject"),
    SUPPLIER_COMMUNICATION_TEMPLATE_ID("supplier.communication.template.id"),
    CLIENT_COMMUNICATION_TEMPLATE_ID("client.communication.template.id"),

    TIME_LIMIT_BOOKING("SUPPLIER"),
    CLIENT_REDIRECT_TO_TRAVEL_ERP("ops.client_reconfirmation_hyperlink"),
    SUPPLIER_REDIRECT_TO_TRAVEL_ERP("ops.supplier_reconfirmation_hyperlink"),

    HOURS("24"),
    configurationFor("configurationFor"),
    MINUTES("60"),
    SECONDS("60"),
    MILLI_SECONDS("1000"),
    FLIGHTS("flight"),
    HOTELS("HOTEL"),;


    private String value;

    CommonEnums(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
