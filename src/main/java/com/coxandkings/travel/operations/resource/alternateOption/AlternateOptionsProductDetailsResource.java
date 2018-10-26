package com.coxandkings.travel.operations.resource.alternateOption;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AlternateOptionsProductDetailsResource {
  
  private String id;
  
  private String productCategory;

  private String productCategorySubType;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

  
  
  
}
