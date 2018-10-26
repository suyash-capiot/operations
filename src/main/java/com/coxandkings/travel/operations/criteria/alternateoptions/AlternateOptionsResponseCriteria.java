package com.coxandkings.travel.operations.criteria.alternateoptions;

import com.coxandkings.travel.operations.criteria.BaseCriteria;

public class AlternateOptionsResponseCriteria{

  private String bookID;
  private String orderID;
  private String configurationId;
  private String leadPaxName;
  private String responseId;
  private Integer pageNumber;
  private Integer pageSize;
  private String sortCriteria;
  private Boolean descending;
  
  public String getBookID() {
    return bookID;
  }
  public void setBookID(String bookID) {
    this.bookID = bookID;
  }
  public String getOrderID() {
    return orderID;
  }
  public void setOrderID(String orderID) {
    this.orderID = orderID;
  }
  public String getConfigurationId() {
    return configurationId;
  }
  public void setConfigurationId(String configurationId) {
    this.configurationId = configurationId;
  }
  public String getLeadPaxName() {
    return leadPaxName;
  }
  public void setLeadPaxName(String leadPaxName) {
    this.leadPaxName = leadPaxName;
  }
  public String getResponseId() {
    return responseId;
  }
  public void setResponseId(String responseId) {
    this.responseId = responseId;
  }
  public String getSortCriteria() {
    return sortCriteria;
  }
  public void setSortCriteria(String sortCriteria) {
    this.sortCriteria = sortCriteria;
  }
  public Integer getPageNumber() {
    return pageNumber;
  }
  public void setPageNumber(Integer pageNumber) {
    this.pageNumber = pageNumber;
  }
  public Integer getPageSize() {
    return pageSize;
  }
  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }
  public Boolean getDescending() {
    return descending;
  }
  public void setDescending(Boolean descending) {
    this.descending = descending;
  }
  
  
}
