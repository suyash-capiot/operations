package com.coxandkings.travel.operations.enums.alternateOptions;

public enum AlternateOptionFollowUpStatus {

  OPEN("Open"),
  CLOSE("Close");

  private String value;

  AlternateOptionFollowUpStatus(String value) {
      this.value = value;
  }

  public String getValue() {
      return value;
  }
  
}
