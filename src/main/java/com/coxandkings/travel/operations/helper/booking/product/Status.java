package com.coxandkings.travel.operations.helper.booking.product;

import com.coxandkings.travel.operations.model.core.OpsBookingStatus;

public class Status {
    private Integer id;
    private String createdByUserId;
    private Long createdTime;
    private OpsBookingStatus status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public OpsBookingStatus getStatus() {
        return status;
    }

    public void setStatus(OpsBookingStatus status) {
        this.status = status;
    }
}
