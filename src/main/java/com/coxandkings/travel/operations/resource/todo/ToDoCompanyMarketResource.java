package com.coxandkings.travel.operations.resource.todo;

public class ToDoCompanyMarketResource {
    private String companyMarketName;
    private String companyMarketId;

    public ToDoCompanyMarketResource() {
    }

    public ToDoCompanyMarketResource(String companyMarketName, String companyMarketId) {
        this.companyMarketName = companyMarketName;
        this.companyMarketId = companyMarketId;
    }

    public String getCompanyMarketId() {
        return companyMarketId;
    }

    public void setCompanyMarketId(String companyMarketId) {
        this.companyMarketId = companyMarketId;
    }

    public String getCompanyMarketName() {
        return companyMarketName;
    }

    public void setCompanyMarketName(String companyMarketName) {
        this.companyMarketName = companyMarketName;
    }
}
