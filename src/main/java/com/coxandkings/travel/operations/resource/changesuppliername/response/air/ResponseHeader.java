
package com.coxandkings.travel.operations.resource.changesuppliername.response.air;


import com.coxandkings.travel.operations.resource.changesuppliername.request.ClientContext;

public class ResponseHeader {

    private ClientContext clientContext;
    private String sessionID;
    private String userID;
    private String transactionID;

    public ClientContext getClientContext() {
        return clientContext;
    }

    public void setClientContext(ClientContext clientContext) {
        this.clientContext = clientContext;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

}
