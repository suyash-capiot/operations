package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "paxType",
    "baseFare",
    "totalFare"
    
})
public class orderClientTotalPriceInfo {
    @JsonProperty("paxType")
	private String paxType;
    @JsonProperty("baseFare")
	private BaseFare baseFare;
    @JsonProperty("totalFare")
	private TotalFare totalFare;

	public String getPaxType() {
		return paxType;
	}

	public void setPaxType(String paxType) {
		this.paxType = paxType;
	}

	public BaseFare getBaseFare() {
		return baseFare;
	}

	public void setBaseFare(BaseFare baseFare) {
		this.baseFare = baseFare;
	}

	public TotalFare getTotalFare() {
		return totalFare;
	}

	public void setTotalFare(TotalFare totalFare) {
		this.totalFare = totalFare;
	}
	
	
}
