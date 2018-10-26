package com.coxandkings.travel.operations.resource.timelimitbooking.convert;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "responseHeader",
        "requestBody",
        "userID"
})
public class UpdateAttributeResource {

    @JsonProperty("responseHeader")
    private TimeLimitResponseHeader responseHeader;
    @JsonProperty("requestBody")
    private TimeLimitRequestBody requestBody;

    @JsonProperty("userID")
    private String userID;

    @JsonProperty("responseHeader")
    public TimeLimitResponseHeader getResponseHeader() {
        return responseHeader;
    }

    @JsonProperty("responseHeader")
    public void setResponseHeader(TimeLimitResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    @JsonProperty("requestBody")
    public TimeLimitRequestBody getRequestBody() {
        return requestBody;
    }

    @JsonProperty("requestBody")
    public void setRequestBody(TimeLimitRequestBody requestBody) {
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
