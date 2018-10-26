package com.coxandkings.travel.operations.resource.refund;

import com.coxandkings.travel.operations.enums.refund.RefundStatus;

public class RefundStatusResource {
    private RefundStatus id;
    private String name;

    public RefundStatusResource() {
    }

    public RefundStatusResource(RefundStatus id, String name) {
        this.id = id;
        this.name = name;
    }

    public RefundStatus getId() {
        return id;
    }

    public void setId(RefundStatus id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
