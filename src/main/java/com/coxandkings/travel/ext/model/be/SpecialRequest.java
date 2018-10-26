package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "contactInfo"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpecialRequest implements Serializable{

    @JsonProperty("specialRequestInfo")
    private List<SpecialRequestInfo> specialRequestInfo;
    private final static long serialVersionUID = 7788335900465460827L;

    public SpecialRequest() {
    }

    public List<SpecialRequestInfo> getSpecialRequestInfo() {
        return specialRequestInfo;
    }

    public void setSpecialRequestInfo(List<SpecialRequestInfo> specialRequestInfo) {
        this.specialRequestInfo = specialRequestInfo;
    }
}