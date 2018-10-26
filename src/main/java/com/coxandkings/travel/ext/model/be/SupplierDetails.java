package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "reference",
    "rateKey",
    "name",
    "ID"
})
public class SupplierDetails {
	@JsonProperty("reference")
	private String reference;
	@JsonProperty("rateKey")
	private String rateKey;
	@JsonProperty("name")
	private String name;
	@JsonProperty("ID")
	private String id;
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getRateKey() {
		return rateKey;
	}
	public void setRateKey(String rateKey) {
		this.rateKey = rateKey;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
}
