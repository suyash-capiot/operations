package com.coxandkings.travel.operations.resource.managebookingstatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;



@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "userID",
        "passengerID",
        "status"
})
public class PAXStatusResource {

	    @JsonProperty("userID")
	    private String userID;
	    @JsonProperty("passengerID")
	    private String passengerID;
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

	    @JsonProperty("status")
	    public String getStatus() {
	        return status;
	    }

	    @JsonProperty("status")
	    public void setStatus(String status) {
	        this.status = status;
	    }

	    @JsonProperty("passengerID")
		public String getPassengerID() {
			return passengerID;
		}

	    @JsonProperty("passengerID")
		public void setPassengerID(String passengerID) {
			this.passengerID = passengerID;
		}

	    
}
