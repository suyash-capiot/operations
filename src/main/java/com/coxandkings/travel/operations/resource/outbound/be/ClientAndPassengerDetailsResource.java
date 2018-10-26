package com.coxandkings.travel.operations.resource.outbound.be;

public class ClientAndPassengerDetailsResource {
//modified
    private String clientType;//done
    private String passengerName;//done
    private String clientCategoryId;
    private String phoneNumber;//done
    private String clientSubCategoryId;
    private String emailId;//done
    private String clientId;//done

    public String getClientType( ) {
        return clientType;
    }

    public void setClientType( String clientType ) {
        this.clientType = clientType;
    }

    public String getPassengerName( ) {
        return passengerName;
    }

    public void setPassengerName( String passengerName ) {
        this.passengerName = passengerName;
    }

    public String getClientCategoryId( ) {
        return clientCategoryId;
    }

    public void setClientCategoryId( String clientCategoryId ) {
        this.clientCategoryId = clientCategoryId;
    }

    public String getPhoneNumber( ) {
        return phoneNumber;
    }

    public void setPhoneNumber( String phoneNumber ) {
        this.phoneNumber = phoneNumber;
    }

    public String getClientSubCategoryId( ) {
        return clientSubCategoryId;
    }

    public void setClientSubCategoryId( String clientSubCategoryId ) {
        this.clientSubCategoryId = clientSubCategoryId;
    }

    public String getEmailId( ) {
        return emailId;
    }

    public void setEmailId( String emailId ) {
        this.emailId = emailId;
    }

    public String getClientId( ) {
        return clientId;
    }

    public void setClientId( String clientId ) {
        this.clientId = clientId;
    }
}
