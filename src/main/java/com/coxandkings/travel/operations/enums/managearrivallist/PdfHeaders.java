package com.coxandkings.travel.operations.enums.managearrivallist;

public enum PdfHeaders {

    PRODUCT_CATEGORY("Product Category"),
    PRODUCT_CATEGORY_SUB_TYPE("Product Category Sub Type"),
    CLIENT_TYPE("Client Type"),
    CLIENT_GROUP("Client Group"),
    CLIENT_NAME("Client Name"),
    SUPPLIER_NAME("Supplier Name"),
    GENERATED_DATE_AND_TIME("Generated Date & time"),
    AIRLINE_NAME("Airline Name"),
    FROM_SECTOR("From Sector"),
    TO_SECTOR("To Sector"),
    PASSANGER_NAME("Passenger Name"),
    PASSANGER_TYPE("Passenger Type"),
    PCC_HAP_CREDENTIALS("Pcc Hap Credentials"),
    PNR("PNR"),
    CLASS("Class"),
    RDB("RDB"),
    TOTAL_NUMBER_OF_PASSENGER("totalNumberOfPassengers"),
    BOOKING_REFERNCE_NUMBER("Booking Reference Id"),
    CHECK_IN_DATE("Check In Date"),
    CHECK_OUT_Date("Check Out Date"),
    SUPPLIER_REFERNCE_NUMBER("Supplier Reference No"),
    ROOM_CATEGORY("Room Category"),
    ROOM_TYPE("Room Type"),
    TOTAL_NUMBER_OF_ROOMS("Total Number Of Rooms");


    private String header;

    PdfHeaders(String s) {
        header = s;
    }

    public String getvalue() {
        return String.format("%s", header);
    }
}
