package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "amount",
        "ssrCode",
        "servicePrice"
})
public class SpecialServiceRequest {

    @JsonProperty("amount")
    private String amount;
    @JsonProperty("ssrCode")
    private String ssrCode;
    @JsonProperty("servicePrice")
    private ServicePrice servicePrice;

    public SpecialServiceRequest(){

    }

    public SpecialServiceRequest(String amount, String ssrCode, ServicePrice servicePrice){
        this.amount = amount;
        this.ssrCode = ssrCode;
        this.servicePrice = servicePrice;

    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSsrCode() {
        return ssrCode;
    }

    public void setSsrCode(String ssrCode) {
        this.ssrCode = ssrCode;
    }

    public ServicePrice getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(ServicePrice servicePrice) {
        this.servicePrice = servicePrice;
    }
}
