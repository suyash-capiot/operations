package com.coxandkings.travel.operations.resource.todo;

import com.coxandkings.travel.operations.enums.todo.ToDoTaskGeneratedTypeValues;
import com.coxandkings.travel.operations.resource.BaseResource;

public class ToDoTaskGeneratedTypeResource extends BaseResource {
    private String generatedType;
    private ToDoTaskGeneratedTypeValues code;

    public String getGeneratedType() {
        return generatedType;
    }

    public void setGeneratedType(String generatedType) {
        this.generatedType = generatedType;
    }

    public ToDoTaskGeneratedTypeValues getCode() {
        return code;
    }

    public void setCode(ToDoTaskGeneratedTypeValues code) {
        this.code = code;
    }
}
