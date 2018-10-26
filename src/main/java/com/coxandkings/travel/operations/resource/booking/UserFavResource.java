package com.coxandkings.travel.operations.resource.booking;

import com.coxandkings.travel.operations.resource.BaseResource;

public class UserFavResource extends BaseResource {

    private String favName;
    private String userId;
    private String reqJSON;

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

    public String getReqJSON() {
        return reqJSON;
    }

    public void setReqJSON(String reqJSON) {
        this.reqJSON = reqJSON;
    }
}
