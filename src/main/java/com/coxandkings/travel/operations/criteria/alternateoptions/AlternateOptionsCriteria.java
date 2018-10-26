package com.coxandkings.travel.operations.criteria.alternateoptions;

import com.coxandkings.travel.operations.criteria.BaseCriteria;

public class AlternateOptionsCriteria extends BaseCriteria {
  
  private String companyId;
  private String companyMarket;
  private String clientType;
  private String productCategory;
  private String productCategorySubType;
  public String getCompanyMarket() {
    return companyMarket;
  }
  public void setCompanyMarket(String companyMarket) {
    this.companyMarket = companyMarket;
  }
  public String getClientType() {
    return clientType;
  }
  public void setClientType(String clientType) {
    this.clientType = clientType;
  }
  public String getProductCategory() {
    return productCategory;
  }
  public void setProductCategory(String productCategory) {
    this.productCategory = productCategory;
  }
  public String getProductCategorySubType() {
    return productCategorySubType;
  }
  public void setProductCategorySubType(String productCategorySubType) {
    this.productCategorySubType = productCategorySubType;
  }
  public String getCompanyId() {
    return companyId;
  }
  public void setCompanyId(String companyId) {
    this.companyId = companyId;
  }
  
  
}
