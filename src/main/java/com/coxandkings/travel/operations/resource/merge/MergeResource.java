package com.coxandkings.travel.operations.resource.merge;


import java.util.List;

import com.coxandkings.travel.operations.enums.merge.MergeTypeValues;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "mergeType", visible = true)
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = AccommodationMergeResource.class, name = "ACCOMMODATION")
             
        })
public class MergeResource {
	
	private List<BookIdProductResource> bookingGroup;
    private Boolean withinCancellation;
    private String supplierName;
    private String supplierId;
    int bookingCount;
    private MergeTypeValues mergeType;
	

    public Boolean getWithinCancellation() {
        return withinCancellation;
    }

    public void setWithinCancellation(Boolean withinCancellation) {
        this.withinCancellation = withinCancellation;
    }

	public List<BookIdProductResource> getBookingGroup() {
		return bookingGroup;
	}

	public void setBookingGroup(List<BookIdProductResource> bookingGroup) {
		this.bookingGroup = bookingGroup;
	}

	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	
	public int getBookingCount() {
		return bookingCount;
	}

	public void setBookingCount(int bookingCount) {
		this.bookingCount = bookingCount;
	}

	public MergeTypeValues getMergeType() {
		return mergeType;
	}

	public void setMergeType(MergeTypeValues mergeType) {
		this.mergeType = mergeType;
	}
}
