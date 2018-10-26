package com.coxandkings.travel.operations.resource.notification;

import java.util.List;

public class RecipientAlert {
    private List<String> dynamicRecipients;
    private List<String> suppliers;
    private List<Roles> roles;
    private List<UserResource> users;


    public List<String> getDynamicRecipients() {
        return dynamicRecipients;
    }

    public void setDynamicRecipients(List<String> dynamicRecipients) {
        this.dynamicRecipients = dynamicRecipients;
    }

    public List<String> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<String> suppliers) {
        this.suppliers = suppliers;
    }


    public List<UserResource> getUsers() {
        return users;
    }

    public void setUsers(List<UserResource> users) {
        this.users = users;
    }

    public List<Roles> getRoles() {
        return roles;
    }

    public void setRoles(List<Roles> roles) {
        this.roles = roles;
    }
}
