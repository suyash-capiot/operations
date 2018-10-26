package com.coxandkings.travel.operations.criteria.mailroomanddispatch.workflowSearchCriteria;

import com.coxandkings.travel.operations.criteria.mailroomanddispatch.OutBoundDispatchCriteria;
import com.coxandkings.travel.operations.criteria.workflow.GenericCriteria;

public class OutboundSearchCritieria extends GenericCriteria {

    private OutBoundDispatchCriteria filter;

    public OutBoundDispatchCriteria getFilter() {
        return filter;
    }

    public void setFilter(OutBoundDispatchCriteria filter) {
        this.filter = filter;
    }
}
