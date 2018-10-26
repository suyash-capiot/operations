package com.coxandkings.travel.operations.resource.alternateOption;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AlternateOptionsCompanyDetailsResource {

  private String id;
  
  private String companyMarket;
  
  private String clientType;

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

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
  
  
}
