package com.coxandkings.travel.operations.resource.alternateOption;

import com.coxandkings.travel.operations.model.alternateoptions.AlternateOptions;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AlternateOptionsResponseDetailsResource {

  private String responseId;

  private String leadPaxName;

  private String productCategory;

  private String tourStartDate;

  private String tourEndDate;

  private AlternateOptions alternateOptions;

  private String status;

  public String getResponseId() {
    return responseId;
  }

  public void setResponseId(String responseId) {
    this.responseId = responseId;
  }

  public String getLeadPaxName() {
    return leadPaxName;
  }

  public void setLeadPaxName(String leadPaxName) {
    this.leadPaxName = leadPaxName;
  }

  public String getProductCategory() {
    return productCategory;
  }

  public void setProductCategory(String productCategory) {
    this.productCategory = productCategory;
  }

  public String getTourStartDate() {
    return tourStartDate;
  }

  public void setTourStartDate(String tourStartDate) {
    this.tourStartDate = tourStartDate;
  }

  public String getTourEndDate() {
    return tourEndDate;
  }

  public void setTourEndDate(String tourEndDate) {
    this.tourEndDate = tourEndDate;
  }

  public AlternateOptions getAlternateOptions() {
    return alternateOptions;
  }

  public void setAlternateOptions(AlternateOptions alternateOptions) {
    this.alternateOptions = alternateOptions;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
  
  

}
