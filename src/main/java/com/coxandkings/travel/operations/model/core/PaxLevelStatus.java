package com.coxandkings.travel.operations.model.core;

public enum PaxLevelStatus {

    REQUEST_FOR_CANCELLATION("Request For Cancellation"),
    CANCELLED("Cancelled"),
    ON_REQUEST("On Request"),
    CONFIRMED("Confirmed");


    private String paxStatus;

    private PaxLevelStatus(String newStatus )    {
        paxStatus = newStatus;
    }

    public String getProductStatus()    {
        return paxStatus;
    }

    public static  PaxLevelStatus fromString(String newStatus )  {
        PaxLevelStatus aPaxStatus = null;
        if( newStatus == null || newStatus.isEmpty() )  {
            return aPaxStatus;
        }

        for( PaxLevelStatus tmpPaxStatus : PaxLevelStatus.values() )    {
            if( tmpPaxStatus.getProductStatus().equalsIgnoreCase( newStatus ))  {
                aPaxStatus = tmpPaxStatus;
                break;
            }
        }
        return aPaxStatus;
    }

}
