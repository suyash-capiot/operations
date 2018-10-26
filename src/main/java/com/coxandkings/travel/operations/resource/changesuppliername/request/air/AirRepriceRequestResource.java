package com.coxandkings.travel.operations.resource.changesuppliername.request.air;

import com.coxandkings.travel.operations.resource.changesuppliername.request.RequestHeader;

public class AirRepriceRequestResource {
    private RequestHeader requestHeader;
    private AirRePriceRequestBody requestBody;

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }

    public AirRePriceRequestBody getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(AirRePriceRequestBody requestBody) {
        this.requestBody = requestBody;
    }
}
