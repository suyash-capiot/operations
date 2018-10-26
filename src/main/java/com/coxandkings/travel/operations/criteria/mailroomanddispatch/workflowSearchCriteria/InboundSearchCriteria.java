package com.coxandkings.travel.operations.criteria.mailroomanddispatch.workflowSearchCriteria;

import com.coxandkings.travel.operations.criteria.mailroomanddispatch.InboundEntryCriteria;
import com.coxandkings.travel.operations.criteria.workflow.GenericCriteria;

public class InboundSearchCriteria extends GenericCriteria {

    private InboundEntryCriteria filter;

    public InboundEntryCriteria getFilter() {
        return filter;
    }

    public void setFilter(InboundEntryCriteria filter) {
        this.filter = filter;
    }
}
