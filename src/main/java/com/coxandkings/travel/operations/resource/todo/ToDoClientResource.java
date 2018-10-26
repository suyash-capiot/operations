package com.coxandkings.travel.operations.resource.todo;

public class ToDoClientResource {
    private String id;
    private String clientName;
    private String clientType;
    private String clientCategory;
    private String clientSubCategory;

    public ToDoClientResource() {
    }

    public ToDoClientResource(String id, String clientName) {
        this.id = id;
        this.clientName = clientName;
    }

    public ToDoClientResource(String id, String clientName, String toDoid) {
        this.id = id;
        this.clientName = clientName;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getClientCategory() {
        return clientCategory;
    }

    public void setClientCategory(String clientCategory) {
        this.clientCategory = clientCategory;
    }

    public String getClientSubCategory() {
        return clientSubCategory;
    }

    public void setClientSubCategory(String clientSubCategory) {
        this.clientSubCategory = clientSubCategory;
    }
}
