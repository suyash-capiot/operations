package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OccupancyInfo {

    @JsonProperty("paxType")
    private String paxType;

    @JsonProperty("maxAge")
    private String maxAge;

    @JsonProperty("minOccupancy")
    private String minOccupancy;

    @JsonProperty("minAge")
    private String minAge;

    @JsonProperty("maxOccupancy")
    private String maxOccupancy;

    public String getPaxType ()
    {
        return paxType;
    }

    public void setPaxType (String paxType)
    {
        this.paxType = paxType;
    }

    public String getMaxAge ()
    {
        return maxAge;
    }

    public void setMaxAge (String maxAge)
    {
        this.maxAge = maxAge;
    }

    public String getMinOccupancy ()
    {
        return minOccupancy;
    }

    public void setMinOccupancy (String minOccupancy)
    {
        this.minOccupancy = minOccupancy;
    }

    public String getMinAge ()
    {
        return minAge;
    }

    public void setMinAge (String minAge)
    {
        this.minAge = minAge;
    }

    public String getMaxOccupancy ()
    {
        return maxOccupancy;
    }

    public void setMaxOccupancy (String maxOccupancy)
    {
        this.maxOccupancy = maxOccupancy;
    }

}