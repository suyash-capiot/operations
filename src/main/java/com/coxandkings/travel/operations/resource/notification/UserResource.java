package com.coxandkings.travel.operations.resource.notification;

public class UserResource {

    private String __id;
    private String userType;
    private String userName;
    private String userId;
    private String role;
    private String phone;
    private String email;
    private Boolean actionableUser;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String get__id() {
        return __id;
    }

    public void set__id(String __id) {
        this.__id = __id;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getActionableUser() {
        return actionableUser;
    }

    public void setActionableUser(Boolean actionableUser) {
        this.actionableUser = actionableUser;
    }
}
