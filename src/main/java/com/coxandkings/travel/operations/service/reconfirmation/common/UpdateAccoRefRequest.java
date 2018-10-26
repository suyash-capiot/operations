package com.coxandkings.travel.operations.service.reconfirmation.common;

public class UpdateAccoRefRequest
{
    private String userID;

    private String orderID;

    private String accoRefNumber;

    public String getUserID ()
    {
        return userID;
    }

    public void setUserID (String userID)
    {
        this.userID = userID;
    }

    public String getOrderID ()
    {
        return orderID;
    }

    public void setOrderID (String orderID)
    {
        this.orderID = orderID;
    }

    public String getAccoRefNumber ()
    {
        return accoRefNumber;
    }

    public void setAccoRefNumber (String accoRefNumber)
    {
        this.accoRefNumber = accoRefNumber;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [userID = "+userID+", orderID = "+orderID+", accoRefNumber = "+accoRefNumber+"]";
    }
}
