package com.coxandkings.travel.operations.model.refund.finaceRefund;

import java.util.UUID;


public class CompanyDetail {

  private UUID id;
  private String companyId;
  private String company;
  private String divisionSBU;
  private String departmentBU;
  private String market;
  private String location;
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

  public String getCompanyId() {
    return companyId;
  }

  public void setCompanyId(String companyId) {
    this.companyId = companyId;
  }

  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public String getDivisionSBU() {
    return divisionSBU;
  }

  public void setDivisionSBU(String divisionSBU) {
    this.divisionSBU = divisionSBU;
  }

  public String getDepartmentBU() {
    return departmentBU;
  }

  public void setDepartmentBU(String departmentBU) {
    this.departmentBU = departmentBU;
  }

  public String getMarket() {
    return market;
  }

  public void setMarket(String market) {
    this.market = market;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }
}
