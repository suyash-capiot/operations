package com.coxandkings.travel.operations.enums.forex;

import org.springframework.util.StringUtils;

public enum IndentType {

    TOURCOST("Tour Cost"),
    PERSONALEXPENSE("Personal Expense");

    String indentType;

    IndentType(String newStatus) {
        this.indentType = newStatus;
    }

    public static IndentType fromString(String type) {
        IndentType indentType = null;
        if (StringUtils.isEmpty(type)) {
            return null;
        }

        for (IndentType tmpStatus : IndentType.values()) {
            if (tmpStatus.getIndentType().equalsIgnoreCase(type)) {
                indentType = tmpStatus;
                break;
            }
        }
        return indentType;
    }

    public String getIndentType() {
        return indentType;
    }

}
