package com.coxandkings.travel.operations.resource.user;

public class OpsLoggedInUserDetails {
    private MdmUserInfo userInfo;
    private String userType;

    public MdmUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(MdmUserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
