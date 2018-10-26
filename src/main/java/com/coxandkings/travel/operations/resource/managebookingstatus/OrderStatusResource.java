package com.coxandkings.travel.operations.resource.managebookingstatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	    "userID",
	    "orderID",
	    "status"
	})
	public class OrderStatusResource {

	    @JsonProperty("userID")
	    private String userID;
	    @JsonProperty("orderID")
	    private String orderID;
	    @JsonProperty("status")
	    private String status;
	    
	    @JsonProperty("userID")
	    public String getUserID() {
	        return userID;
	    }

	    @JsonProperty("userID")
	    public void setUserID(String userID) {
	        this.userID = userID;
	    }

	    @JsonProperty("orderID")
	    public String getOrderID() {
	        return orderID;
	    }

	    @JsonProperty("orderID")
	    public void setOrderID(String orderID) {
	        this.orderID = orderID;
	    }

	    @JsonProperty("status")
	    public String getStatus() {
	        return status;
	    }

	    @JsonProperty("status")
	    public void setStatus(String status) {
	        this.status = status;
	    }


	}

