package com.coxandkings.travel.operations.criteria.workflow;

public class GenericCriteria {

    private Integer page;

    private Integer count;

    private String sort;

    private Boolean workflow;

    private WorkFlowFilter workflow_filter;

    public GenericCriteria() {
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Boolean getWorkflow() {
        return workflow;
    }

    public void setWorkflow(Boolean workflow) {
        this.workflow = workflow;
    }

    public WorkFlowFilter getWorkflow_filter() {
        return workflow_filter;
    }

    public void setWorkflow_filter(WorkFlowFilter workflow_filter) {
        this.workflow_filter = workflow_filter;
    }
}
