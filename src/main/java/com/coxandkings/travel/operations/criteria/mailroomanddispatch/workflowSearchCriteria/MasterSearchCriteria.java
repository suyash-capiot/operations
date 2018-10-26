package com.coxandkings.travel.operations.criteria.mailroomanddispatch.workflowSearchCriteria;

import com.coxandkings.travel.operations.criteria.mailroomanddispatch.MailroomSearchCriteriaSorted;
import com.coxandkings.travel.operations.criteria.workflow.GenericCriteria;

public class MasterSearchCriteria extends GenericCriteria {

    private MailroomSearchCriteriaSorted filter;

    public MailroomSearchCriteriaSorted getFilter() {
        return filter;
    }

    public void setFilter(MailroomSearchCriteriaSorted filter) {
        this.filter = filter;
    }
}
