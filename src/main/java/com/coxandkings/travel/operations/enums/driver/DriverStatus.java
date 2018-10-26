package com.coxandkings.travel.operations.enums.driver;

import org.springframework.util.StringUtils;

public enum DriverStatus {

    ASSIGNED("Assigned"),
    CANCELLED("Cancelled"),
    COMPLETED("Completed"),
    PENDING("Pending");

    private String status;

    DriverStatus(String status) {
        this.status=status;
    }

    public String getStatus() {
        return status;
    }

   public static DriverStatus fromStringToEnum(String status)
   {
       DriverStatus driverStatus = null;

       if(StringUtils.isEmpty(status)) {
           return null;
       }

       for(DriverStatus status1: DriverStatus.values()) {
           if(status1.getStatus().equalsIgnoreCase(status)) {
               driverStatus = status1;
               break;
           }
       }
       return driverStatus;
   }

   public String getvalue()
   {
       return String.format("%s" , status );
   }
}
