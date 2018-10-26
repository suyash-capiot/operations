package com.coxandkings.travel.operations.criteria.mailroomanddispatch;

import com.coxandkings.travel.operations.criteria.BaseCriteria;
import com.coxandkings.travel.operations.enums.mailroomanddispatch.DispatchStatus;

public class DispatchStatusCriteria extends BaseCriteria {

    private DispatchStatus code;

    public DispatchStatus getCode() {
        return code;
    }

    public void setCode(DispatchStatus code) {
        this.code = code;
    }
}
