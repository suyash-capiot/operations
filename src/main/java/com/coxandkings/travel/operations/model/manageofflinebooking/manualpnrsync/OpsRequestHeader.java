package com.coxandkings.travel.operations.model.manageofflinebooking.manualpnrsync;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsRequestHeader implements Serializable {
    @JsonProperty("userID")
	String userID;

    @JsonProperty("sessionID")
	String sessionID;

    @JsonProperty("transactionID")
	String transactionID;

	OpsClientContext clientContext;

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public OpsClientContext getClientContext() {
		return clientContext;
	}

	public void setClientContext(OpsClientContext clientContext) {
		this.clientContext = clientContext;
	}
}