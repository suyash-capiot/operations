
package com.coxandkings.travel.operations.resource.changesuppliername.request.air;


import com.coxandkings.travel.operations.resource.changesuppliername.request.RequestHeader;

public class AirRateRequestResource {

    private RequestHeader requestHeader;
    private AirPriceRequestBody requestBody;

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }

    public AirPriceRequestBody getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(AirPriceRequestBody requestBody) {
        this.requestBody = requestBody;
    }

}
