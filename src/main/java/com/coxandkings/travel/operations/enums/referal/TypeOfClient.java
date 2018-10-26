package com.coxandkings.travel.operations.enums.referal;

import org.springframework.util.StringUtils;

public enum TypeOfClient {

    VIP("vip"),
    CIP("cip"),
    REPEAT_CLIENT("repeat_client"),
    CORPORATE_STATUS("corporate_status"),
    EMPTY("");
    private String type;

    TypeOfClient(String newType) {
        type = newType;
    }

    public static TypeOfClient getTypeOfClient(String newType) {
        TypeOfClient typeOfClient = null;
        if (StringUtils.isEmpty(newType)) {
            return null;
        }

        for (TypeOfClient tempTypeOfClient : TypeOfClient.values()) {
            if (tempTypeOfClient.getType().equalsIgnoreCase(newType)) {
                typeOfClient = tempTypeOfClient;
                break;
            }
        }

        return typeOfClient;
    }

    public String getType() {
        return type;
    }

}
