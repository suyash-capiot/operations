
package com.coxandkings.travel.operations.resource.changesuppliername.request.acco;


import com.coxandkings.travel.operations.resource.changesuppliername.request.RequestHeader;

public class AccoRateRequestResource {

    private RequestHeader requestHeader;
    private AccoRequestBodyForPrice requestBody;

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }

    public AccoRequestBodyForPrice getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(AccoRequestBodyForPrice requestBody) {
        this.requestBody = requestBody;
    }

}
