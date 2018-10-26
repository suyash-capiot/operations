package com.coxandkings.travel.operations.resource.mailroomMaster;

import com.coxandkings.travel.operations.enums.mailroomanddispatch.DispatchStatus;
import com.coxandkings.travel.operations.resource.BaseResource;

public class DicpatchStatusResource extends BaseResource {

    private String name;
    private DispatchStatus code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DispatchStatus getCode() {
        return code;
    }

    public void setCode(DispatchStatus code) {
        this.code = code;
    }
}
