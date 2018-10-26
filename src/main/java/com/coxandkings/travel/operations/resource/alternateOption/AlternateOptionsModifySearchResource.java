package com.coxandkings.travel.operations.resource.alternateOption;

import java.util.Set;
import com.coxandkings.travel.operations.enums.product.OpsProductCategory;
import com.coxandkings.travel.operations.enums.product.OpsProductSubCategory;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlternateOptionsModifySearchResource {

  private String bookId;
  
  private String orderId;
  
  private String supplierName;
  
  private String higherLimitThreshold;
  
  private String lowerLimitThreshold;
  
  private AlternateOptionsModifyAirSearchResource flightSearch;
  
  private AlternateOptionsModifyAccoSearchResource accoSearch;

  public String getSupplierName() {
    return supplierName;
  }

  public void setSupplierName(String supplierName) {
    this.supplierName = supplierName;
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

  public AlternateOptionsModifyAirSearchResource getFlightSearch() {
    return flightSearch;
  }

  public void setFlightSearch(AlternateOptionsModifyAirSearchResource flightSearch) {
    this.flightSearch = flightSearch;
  }

  public String getBookId() {
    return bookId;
  }

  public void setBookId(String bookId) {
    this.bookId = bookId;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public AlternateOptionsModifyAccoSearchResource getAccoSearch() {
    return accoSearch;
  }

  public void setAccoSearch(AlternateOptionsModifyAccoSearchResource accoSearch) {
    this.accoSearch = accoSearch;
  }

  
}
