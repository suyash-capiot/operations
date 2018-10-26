package com.coxandkings.travel.operations.criteria.booking;

import com.coxandkings.travel.operations.criteria.BaseCriteria;

public class UserFavCriteria extends BaseCriteria {
    private String name;
    private String favName;
    private String userId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFavName() {
        return favName;
    }

    public void setFavName(String favName) {
        this.favName = favName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
