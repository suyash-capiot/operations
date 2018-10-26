package com.coxandkings.travel.operations.enums.alternateOptions;

public enum AlternateOptionsSchedularType {

  ONETIME("OneTime"),
  REOCCURRING("ReOccurring");

  private String value;

  AlternateOptionsSchedularType(String value) {
      this.value = value;
  }

  public String getValue() {
      return value;
  }
  
}
