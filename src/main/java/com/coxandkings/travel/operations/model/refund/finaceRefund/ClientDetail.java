package com.coxandkings.travel.operations.model.refund.finaceRefund;

import java.util.UUID;


public class ClientDetail {

  private UUID id;
  private ClientType clientType;
  private String clientCategory;
  private String clientGroupId;
  private String clientSubCategory;
  private String clientName;
  private String clientId;
  private String passengerName;
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

  public ClientType getClientType() {
    return clientType;
  }

  public void setClientType(ClientType clientType) {
    this.clientType = clientType;
  }

  public String getClientCategory() {
    return clientCategory;
  }

  public void setClientCategory(String clientCategory) {
    this.clientCategory = clientCategory;
  }

  public String getClientGroupId() {
    return clientGroupId;
  }

  public void setClientGroupId(String clientGroupId) {
    this.clientGroupId = clientGroupId;
  }

  public String getClientSubCategory() {
    return clientSubCategory;
  }

  public void setClientSubCategory(String clientSubCategory) {
    this.clientSubCategory = clientSubCategory;
  }

  public String getClientName() {
    return clientName;
  }

  public void setClientName(String clientName) {
    this.clientName = clientName;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getPassengerName() {
    return passengerName;
  }

  public void setPassengerName(String passengerName) {
    this.passengerName = passengerName;
  }
}
