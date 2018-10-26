
package com.coxandkings.travel.operations.resource.managenoshow;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "responseHeader",
        "requestBody",
        "userID"
})
public class UpdateBookingAtrributeResource {

    @JsonProperty("responseHeader")
    private NoShowResponseHeader responseHeader;
    @JsonProperty("requestBody")
    private NoShowRequestBody requestBody;

    @JsonProperty("userID")
    private String userID;

    @JsonProperty("responseHeader")
    public NoShowResponseHeader getResponseHeader() {
        return responseHeader;
    }

    @JsonProperty("responseHeader")
    public void setResponseHeader(NoShowResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    @JsonProperty("requestBody")
    public NoShowRequestBody getRequestBody() {
        return requestBody;
    }

    @JsonProperty("requestBody")
    public void setRequestBody(NoShowRequestBody requestBody) {
        this.requestBody = requestBody;
    }

    @JsonProperty("userID")
    public String getUserID() {
        return userID;
    }

    @JsonProperty("userID")
    public void setUserID(String userID) {
        this.userID = userID;
    }
}
