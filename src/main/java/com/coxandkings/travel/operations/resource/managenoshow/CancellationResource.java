package com.coxandkings.travel.operations.resource.managenoshow;

import org.json.JSONObject;

public class CancellationResource {

    private JSONObject requestHeader;
    private JSONObject requestBody;

    public CancellationResource() {
    }

    public CancellationResource(JSONObject requestHeader, JSONObject requestBody) {
        this.requestHeader = requestHeader;
        this.requestBody = requestBody;
    }

    public JSONObject getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(JSONObject requestHeader) {
        this.requestHeader = requestHeader;
    }

    public JSONObject getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(JSONObject requestBody) {
        this.requestBody = requestBody;
    }
}
