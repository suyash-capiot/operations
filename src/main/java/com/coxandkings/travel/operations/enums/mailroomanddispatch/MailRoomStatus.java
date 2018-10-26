package com.coxandkings.travel.operations.enums.mailroomanddispatch;

import com.coxandkings.travel.operations.enums.refund.ReasonForRequest;

public enum MailRoomStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    EMPTY("");


    private String value;

    MailRoomStatus(String value) {
        this.value = value;
    }

    public static MailRoomStatus getMailRoomStatus(String reason) {
        MailRoomStatus mailRoomStatus = null;
        for (MailRoomStatus mailRoomStatus1 : MailRoomStatus.values()) {
            if (mailRoomStatus1.getValue().equalsIgnoreCase(reason)) {
                mailRoomStatus = mailRoomStatus1;
                break;
            }
        }
        return mailRoomStatus;
    }

    public String getValue() {
        return this.value;
    }


}
