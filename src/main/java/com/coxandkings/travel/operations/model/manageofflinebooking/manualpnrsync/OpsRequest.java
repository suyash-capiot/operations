package com.coxandkings.travel.operations.model.manageofflinebooking.manualpnrsync;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsRequest {

    @JsonProperty("requestBody")
    OpsRequestBody requestBody;

    @JsonProperty("requestHeader")
    OpsRequestHeader requestHeader;

	public OpsRequestBody getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(OpsRequestBody requestBody) {
		this.requestBody = requestBody;
	}

	public OpsRequestHeader getRequestHeader() {
		return requestHeader;
	}

	public void setRequestHeader(OpsRequestHeader requestHeader) {
		this.requestHeader = requestHeader;
	}

}
