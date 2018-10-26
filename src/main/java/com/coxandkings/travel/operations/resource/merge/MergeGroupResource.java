package com.coxandkings.travel.operations.resource.merge;


import com.coxandkings.travel.operations.enums.merge.MergeTypeValues;
import com.coxandkings.travel.operations.resource.amendentitycommercial.MarginDetails;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "mergeType", visible = true)
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = AccomodationMergeGroupResource.class, name = "ACCOMMODATION")
             
        })
public class MergeGroupResource {

	String bookingRefNumber;
	String orderId;
	String supplierName;
	String supplierId;
	MarginDetails margin;
	String leadPassengerName;
	private MergeTypeValues mergeType;
	@JsonProperty("paxCount")
    private int paxCount;
	
	public int getPaxCount() {
		return paxCount;
	}
	public void setPaxCount(int paxCount) {
		this.paxCount = paxCount;
	}
	public String getBookingRefNumber() {
		return bookingRefNumber;
	}
	public void setBookingRefNumber(String bookingRefNumber) {
		this.bookingRefNumber = bookingRefNumber;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}
	public String getLeadPassengerName() {
		return leadPassengerName;
	}
	public void setLeadPassengerName(String leadPassengerName) {
		this.leadPassengerName = leadPassengerName;
	}
	public MergeGroupResource() {
		super();
	}
	public MarginDetails getMargin() {
		return margin;
	}
	public void setMargin(MarginDetails margin) {
		this.margin = margin;
	}
	public MergeTypeValues getMergeType() {
		return mergeType;
	}
	public void setMergeType(MergeTypeValues mergeType) {
		this.mergeType = mergeType;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
}
