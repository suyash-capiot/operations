package com.coxandkings.travel.operations.resource.managebookingstatus;

import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.coxandkings.travel.operations.model.core.OpsOrderStatus;

public class UpdateOrderStatusResource {
	
    private String orderId;
    private OpsProductSubCategory productSubCategory;
    private OpsOrderStatus orderStatus;
    private String bookingId;

    public OpsProductSubCategory getProductSubCategory() {
		return productSubCategory;
	}

	public void setProductSubCategory(OpsProductSubCategory productSubCategory) {
		this.productSubCategory = productSubCategory;
	}

	public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public OpsOrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OpsOrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }
}
