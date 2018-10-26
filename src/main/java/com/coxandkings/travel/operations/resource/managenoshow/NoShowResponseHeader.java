
package com.coxandkings.travel.operations.resource.managenoshow;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "clientContext",
    "sessionID",
    "userID",
    "transactionID"
})
public class NoShowResponseHeader {

    @JsonProperty("clientContext")
    private NoShowClientContext clientContext;
    @JsonProperty("sessionID")
    private String sessionID;
    @JsonProperty("userID")
    private String userID;
    @JsonProperty("transactionID")
    private String transactionID;

    @JsonProperty("clientContext")
    public NoShowClientContext getClientContext() {
        return clientContext;
    }

    @JsonProperty("clientContext")
    public void setClientContext(NoShowClientContext clientContext) {
        this.clientContext = clientContext;
    }

    @JsonProperty("sessionID")
    public String getSessionID() {
        return sessionID;
    }

    @JsonProperty("sessionID")
    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    @JsonProperty("userID")
    public String getUserID() {
        return userID;
    }

    @JsonProperty("userID")
    public void setUserID(String userID) {
        this.userID = userID;
    }

    @JsonProperty("transactionID")
    public String getTransactionID() {
        return transactionID;
    }

    @JsonProperty("transactionID")
    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }
}
