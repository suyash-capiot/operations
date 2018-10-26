package com.coxandkings.travel.operations.resource.alternateOption;

import java.time.ZonedDateTime;
import java.util.Set;
import com.coxandkings.travel.operations.enums.alternateOptions.AlternateOptionsStatus;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.coxandkings.travel.operations.model.alternateoptions.RequestLockObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AlternateOptionsResource {


  private String configurationId;
  
  private Set<AlternateOptionsCompanyDetailsResource> companyDetails;
  
  private Set<AlternateOptionsProductDetailsResource> productDetails;
  
  private String alternateOfferProcess; // Auto , manual
  
  private String higherLimitThreshold;
  
  private String lowerLimitThreshold;
  
  private String status;
  
  private String action;
  
  private String lastModifiedByUserId;
  
  private String approverRemark;
  
  private String companyId;
  
  private boolean sameValue;

  private RequestLockObject lock;

  @JsonSerialize(using = LocalDateSerializer.class)
  @JsonDeserialize(using = LocalDateDeserializer.class)
  private ZonedDateTime createdOn;

  @JsonSerialize(using = LocalDateSerializer.class)
  @JsonDeserialize(using = LocalDateDeserializer.class)
  private ZonedDateTime lastModifiedOn;

  public String getConfigurationId() {
    return configurationId;
  }

  public void setConfigurationId(String configurationId) {
    this.configurationId = configurationId;
  }

  public String getAlternateOfferProcess() {
    return alternateOfferProcess;
  }

  public void setAlternateOfferProcess(String alternateOfferProcess) {
    this.alternateOfferProcess = alternateOfferProcess;
  }

  public String getHigherLimitThreshold() {
    return higherLimitThreshold;
  }

  public void setHigherLimitThreshold(String higherLimitThreshold) {
    this.higherLimitThreshold = higherLimitThreshold;
  }

  public String getLowerLimitThreshold() {
    return lowerLimitThreshold;
  }

  public void setLowerLimitThreshold(String lowerLimitThreshold) {
    this.lowerLimitThreshold = lowerLimitThreshold;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public String getApproverRemark() {
    return approverRemark;
  }

  public void setApproverRemark(String approverRemark) {
    this.approverRemark = approverRemark;
  }

  public RequestLockObject getLock() {
    return lock;
  }

  public void setLock(RequestLockObject lock) {
    this.lock = lock;
  }

  public ZonedDateTime getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(ZonedDateTime createdOn) {
    this.createdOn = createdOn;
  }

  public String getLastModifiedByUserId() {
    return lastModifiedByUserId;
  }

  public void setLastModifiedByUserId(String lastModifiedByUserId) {
    this.lastModifiedByUserId = lastModifiedByUserId;
  }

  public Set<AlternateOptionsCompanyDetailsResource> getCompanyDetails() {
    return companyDetails;
  }

  public void setCompanyDetails(Set<AlternateOptionsCompanyDetailsResource> companyDetails) {
    this.companyDetails = companyDetails;
  }

  public Set<AlternateOptionsProductDetailsResource> getProductDetails() {
    return productDetails;
  }

  public void setProductDetails(Set<AlternateOptionsProductDetailsResource> productDetails) {
    this.productDetails = productDetails;
  }

  public ZonedDateTime getLastModifiedOn() {
    return lastModifiedOn;
  }

  public void setLastModifiedOn(ZonedDateTime lastModifiedOn) {
    this.lastModifiedOn = lastModifiedOn;
  }

  public boolean isSameValue() {
    return sameValue;
  }

  public void setSameValue(boolean sameValue) {
    this.sameValue = sameValue;
  }

  public String getCompanyId() {
    return companyId;
  }

  public void setCompanyId(String companyId) {
    this.companyId = companyId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  
  
  
  
}
