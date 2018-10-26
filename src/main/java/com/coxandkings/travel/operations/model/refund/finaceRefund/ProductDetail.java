package com.coxandkings.travel.operations.model.refund.finaceRefund;

import java.util.UUID;


public class ProductDetail {

  private UUID id;
  private String orderId;
  private String productCategory;
  private String productCategorySubType;
  private String productName;
  private String productNameSubType;
  private String productSubName;
  /*private Long createdOn;
  private Long lastUpdatedOn;
  private String createdBy;
  private String lastUpdatedBy;*/

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
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

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getProductNameSubType() {
    return productNameSubType;
  }

  public void setProductNameSubType(String productNameSubType) {
    this.productNameSubType = productNameSubType;
  }

  public String getProductSubName() {
    return productSubName;
  }

  public void setProductSubName(String productSubName) {
    this.productSubName = productSubName;
  }
}
