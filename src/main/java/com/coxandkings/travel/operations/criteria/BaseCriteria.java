package com.coxandkings.travel.operations.criteria;

import com.coxandkings.travel.operations.enums.Status;

import java.io.Serializable;

public class BaseCriteria implements Serializable {
    private String[] ids;
    private String[] excludeIds;
    private String createdByUserId;
    private String lastModifiedByUserId;
    private String sortCriteria;
    private Boolean descending;
    private Status status;

    private Integer pageNumber;
    private Integer pageSize;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    private String[] bookingRefIds;

    public String[] getIds() {
        return ids;
    }

    public void setIds(String... ids) {
        this.ids = ids;
    }

    public String[] getExcludeIds() {
        return excludeIds;
    }

    public void setExcludeIds(String... excludeIds) {
        this.excludeIds = excludeIds;
    }

    public String getSortCriteria() {
        return sortCriteria;
    }

    public void setSortCriteria(String sortCriteria) {
        this.sortCriteria = sortCriteria;
    }

    public Boolean getDescending() {
        return descending;
    }

    public void setDescending(Boolean descending) {
        this.descending = descending;
    }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public String getLastModifiedByUserId() {
        return lastModifiedByUserId;
    }

    public void setLastModifiedByUserId(String lastModifiedByUserId) {
        this.lastModifiedByUserId = lastModifiedByUserId;
    }

    public String[] getBookingRefIds() {
        return bookingRefIds;
    }

    public void setBookingRefIds(String... bookingRefIds) {
        this.bookingRefIds = bookingRefIds;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }
}
