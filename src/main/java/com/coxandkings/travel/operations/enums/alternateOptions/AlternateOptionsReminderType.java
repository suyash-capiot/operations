package com.coxandkings.travel.operations.enums.alternateOptions;

public enum AlternateOptionsReminderType {

  SMS("SMS"),
  EMAIL("Email"),
  NOTIFICATION("Notification");

  private String value;

  AlternateOptionsReminderType(String value) {
      this.value = value;
  }

  public String getValue() {
      return value;
  }
}
