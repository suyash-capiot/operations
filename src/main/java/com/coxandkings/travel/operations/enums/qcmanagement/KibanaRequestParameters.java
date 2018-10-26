package com.coxandkings.travel.operations.enums.qcmanagement;

import org.springframework.util.StringUtils;

public enum KibanaRequestParameters {
    MESSAGE("message"),
    INBOUND_OPERATIONS("inbound_operation"),
    SESSIONID("sessionid"),
    USERID("userid"),
    TRANSACTIONID("transactionid"),
    OPERATIONSID("operationid"),
    TIMESTAMP("timestamp"),
    INBOUND_SERVICE_URI("inbound_service_uri"),
    SOURCE("_source"),
    LOGMESSAGE("logmessage");

    private String type;

    KibanaRequestParameters(String newType) {
        type = newType;
    }

    public static KibanaRequestParameters getType(String newType) {
        KibanaRequestParameters types = null;
        if (StringUtils.isEmpty(newType)) {
            return null;
        }
        for (KibanaRequestParameters tempType : KibanaRequestParameters.values()) {
            if (tempType.getStatus().equalsIgnoreCase(newType)) {
                types = tempType;
                break;
            }
        }
        return types;
    }

    public String getStatus() {
        return type;
    }
}