package com.coxandkings.travel.operations.model.refund.finaceRefund;

import java.util.UUID;


public class CollectionDetail {

  private UUID id;
  private UUID receiptId;
  private String receiptNo;
  private String transactionCurrency;
  private double transactionAmount;
  private double roe;
  private String functionalCurrency;
  private double functionalAmount;
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

  public UUID getReceiptId() {
    return receiptId;
  }

  public void setReceiptId(UUID receiptId) {
    this.receiptId = receiptId;
  }

  public String getReceiptNo() {
    return receiptNo;
  }

  public void setReceiptNo(String receiptNo) {
    this.receiptNo = receiptNo;
  }

  public String getTransactionCurrency() {
    return transactionCurrency;
  }

  public void setTransactionCurrency(String transactionCurrency) {
    this.transactionCurrency = transactionCurrency;
  }

  public double getTransactionAmount() {
    return transactionAmount;
  }

  public void setTransactionAmount(double transactionAmount) {
    this.transactionAmount = transactionAmount;
  }

  public double getRoe() {
    return roe;
  }

  public void setRoe(double roe) {
    this.roe = roe;
  }

  public String getFunctionalCurrency() {
    return functionalCurrency;
  }

  public void setFunctionalCurrency(String functionalCurrency) {
    this.functionalCurrency = functionalCurrency;
  }

  public double getFunctionalAmount() {
    return functionalAmount;
  }

  public void setFunctionalAmount(double functionalAmount) {
    this.functionalAmount = functionalAmount;
  }
}
