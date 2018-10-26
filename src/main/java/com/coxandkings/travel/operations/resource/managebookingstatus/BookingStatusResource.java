package com.coxandkings.travel.operations.resource.managebookingstatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	    "userID",
	    "bookID",
	    "status"
	})
	public class BookingStatusResource {

	    @JsonProperty("userID")
	    private String userID;
	    @JsonProperty("bookID")
	    private String bookID;
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

	    
	    @JsonProperty("bookID")
	    public String getBookID() {
			return bookID;
		}

	    @JsonProperty("bookID")
		public void setBookID(String bookID) {
			this.bookID = bookID;
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

