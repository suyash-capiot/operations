package com.coxandkings.travel.operations.resource.remarks;

public class RoleBasicInfo {

    private String id;
    private String company;
    private String roleName;

    public RoleBasicInfo() {
    }

    public RoleBasicInfo(String id, String company, String roleName) {
        this.id = id;
        this.company = company;
        this.roleName = roleName;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
