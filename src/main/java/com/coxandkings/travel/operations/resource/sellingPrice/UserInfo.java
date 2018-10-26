package com.coxandkings.travel.operations.resource.sellingPrice;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserInfo {
    @JsonProperty("username")
    private String userName;

    @JsonProperty("password")
    private String password;

    public UserInfo() {
    }

    public UserInfo(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
