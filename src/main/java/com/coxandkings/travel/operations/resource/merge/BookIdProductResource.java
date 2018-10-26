package com.coxandkings.travel.operations.resource.merge;

import com.coxandkings.travel.operations.model.core.OpsProduct;

public class BookIdProductResource {
    private String id;
    private String bookingRefId;
    private OpsProduct product;
    private String roomId;
    private String supplierName;

    public BookIdProductResource() {
    }

    public BookIdProductResource(String id, String bookingRefId, OpsProduct product, String roomId) {
        this.id = id;
        this.bookingRefId = bookingRefId;
        this.product = product;
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookingRefId() {
        return bookingRefId;
    }

    public void setBookingRefId(String bookingRefId) {
        this.bookingRefId = bookingRefId;
    }

    public OpsProduct getProduct() {
        return product;
    }

    public void setProduct(OpsProduct product) {
        this.product = product;
    }

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
}
