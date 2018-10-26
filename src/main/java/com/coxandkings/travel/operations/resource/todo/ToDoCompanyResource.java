package com.coxandkings.travel.operations.resource.todo;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ToDoCompanyResource {
    private String companyName;
    private String id;

    public ToDoCompanyResource() {
    }

    public ToDoCompanyResource(String companyName, String id) {
        this.companyName = companyName;
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
