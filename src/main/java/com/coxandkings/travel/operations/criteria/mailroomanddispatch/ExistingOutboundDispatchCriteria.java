package com.coxandkings.travel.operations.criteria.mailroomanddispatch;

import com.coxandkings.travel.operations.resource.BaseResource;

public class ExistingOutboundDispatchCriteria extends BaseResource {

    private String dispatchId;
    private String linkInBoudNumber;
    private String referenceNumber;
    private Integer pageSize;
    private Integer pageNumber;

    public String getDispatchId() {
        return dispatchId;
    }

    public void setDispatchId(String dispatchId) {
        this.dispatchId = dispatchId;
    }

    public String getLinkInBoudNumber() {
        return linkInBoudNumber;
    }

    public void setLinkInBoudNumber(String linkInBoudNumber) {
        this.linkInBoudNumber = linkInBoudNumber;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }
}
