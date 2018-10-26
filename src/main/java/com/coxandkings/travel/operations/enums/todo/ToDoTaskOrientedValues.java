package com.coxandkings.travel.operations.enums.todo;

import org.springframework.util.StringUtils;

public enum ToDoTaskOrientedValues {

    ACTION_ORIENTED("Action Oriented"),
    APPROVAL_ORIENTED("Approval Oriented");

    private String value;

    ToDoTaskOrientedValues(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ToDoTaskOrientedValues fromString(String toDoTaskTypeValue) {
        ToDoTaskOrientedValues toDoTypeOrientedValues = null;

        if(StringUtils.isEmpty(toDoTaskTypeValue)) {
            return toDoTypeOrientedValues;
        }

        for(ToDoTaskOrientedValues tmpTaskTypeValues: ToDoTaskOrientedValues.values()) {
            if(toDoTaskTypeValue.equalsIgnoreCase(tmpTaskTypeValues.getValue())) {
                toDoTypeOrientedValues = tmpTaskTypeValues;
            }
        }

        return toDoTypeOrientedValues;
    }
}
