package com.coxandkings.travel.operations.enums.newsupplierfirstbooking;

import org.springframework.util.StringUtils;

public enum NewSupplierCommunicationStatus {
    ACCEPTED("accepted"),
    REJECTED("rejected");
    String status;
    NewSupplierCommunicationStatus(String status) {
        this.status=status;
    }

    public static NewSupplierCommunicationStatus getStatus(String newStatus)
    {
        NewSupplierCommunicationStatus newSupplierCommunicationStatus=null;
        if(StringUtils.isEmpty(newStatus))
        {
            return null;
        }

        for (NewSupplierCommunicationStatus tempStatus:NewSupplierCommunicationStatus.values()) {
            if(tempStatus.getStatus().equalsIgnoreCase(newStatus)) {
            newSupplierCommunicationStatus=tempStatus;
            }
        }
        return newSupplierCommunicationStatus;
    }

    public String getStatus() {
        return status;
    }
}
