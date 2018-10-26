package com.coxandkings.travel.operations.resource.todo;

import com.coxandkings.travel.operations.resource.BaseResource;

public class ToDoTaskSubTypeResource extends BaseResource {
    private String subType;
    private String typeId;
    private String code;

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
