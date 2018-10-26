package com.coxandkings.travel.operations.model.core;

public enum OpsOrderStatus {
    OK("Confirmed"),
    RQ("On Request"),
    WL("Waitlisted"),
    REJ("Rejected"),
    VCH("Vouchered"),
    TKD("Ticketed"),
    RXL("Request for Cancellation"),
    XL("Cancelled"),
    RAMD("Request for Amendment"),
    FAILED("Failed");

    private String productStatus;

    private OpsOrderStatus(String newStatus )    {
        productStatus = newStatus;
    }

    public String getProductStatus()    {
        return productStatus;
    }

    public static  OpsOrderStatus fromString(String newStatus )  {
        OpsOrderStatus aProductStatus = null;
        if( newStatus == null || newStatus.isEmpty() )  {
            return aProductStatus;
        }

        for( OpsOrderStatus tmpProductStatus : OpsOrderStatus.values() )    {
            if( tmpProductStatus.getProductStatus().equalsIgnoreCase( newStatus ))  {
                aProductStatus = tmpProductStatus;
                break;
            }
        }
        return aProductStatus;
    }
}
