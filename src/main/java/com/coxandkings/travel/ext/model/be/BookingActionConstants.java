package com.coxandkings.travel.ext.model.be;

public interface BookingActionConstants {

    //Air cancel/amen types
    public static final String JSON_PROP_AIR_CANNCELTYPE_CANCELPAX = "CANCELPASSENGER";
    public static final String JSON_PROP_AIR_CANNCELTYPE_CANCELJOU = "CANCELJOU";
    public static final String JSON_PROP_AIR_CANNCELTYPE_CANCELSSR = "CANCELSSR";
    public static final String JSON_PROP_AIR_CANNCELTYPE_FULLCANCEL = "FULLCANCEL";
    public static final String JSON_PROP_AIR_CANNCELTYPE_CANCELODO = "CANCELODO";
    public static final String JSON_PROP_AIR_AMENDTYPE_SSR = "ADDSSR";
    public static final String JSON_PROP_AIR_AMENDTYPE_REM = "ADDREM";
    public static final String JSON_PROP_AIR_AMENDTYPE_PIS = "UPDATEPIS";

    //Acco cancel/amen types
    public static final String JSON_PROP_ACCO_CANNCELTYPE_ADDPAX = "ADDPASSENGER";
    public static final String JSON_PROP_ACCO_CANNCELTYPE_CANCELPAX = "CANCELPASSENGER";
    public static final String JSON_PROP_ACCO_CANNCELTYPE_UPDATEPAX = "UPDATEPASSENGER";
    public static final String JSON_PROP_ACCO_CANNCELTYPE_UPDATEROOM = "UPDATEROOM";
    public static final String JSON_PROP_ACCO_CANNCELTYPE_CANCELROOM = "CANCELROOM";
    public static final String JSON_PROP_ACCO_CANNCELTYPE_UPDATESTAYDATES = "UPDATESTAYDATES";
    public static final String JSON_PROP_ACCO_CANNCELTYPE_FULLCANCEL = "FULLCANCELLATION";

    public static final String JSON_PROP_NEW_BOOKING = "NEW";
    public static final String JSON_PROP_ERROR_BOOKING = "ERROR";
    public static final String JSON_PROP_ON_REQUEST_BOOKING = "ONREQUEST";
    public static final String JSON_PROP_AMEND = "amend";
    public static final String JSON_PROP_CANCEL = "cancel";
}
