package com.coxandkings.travel.operations.resource.remarks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "userDetails.companies.roleName"
})
public class UserMDMFilter {

    @JsonProperty("userDetails.companies.roleName")
    private String roleName;

    @JsonProperty("userDetails.companies.roleName")
    public String getUserDetailsCompaniesRoleName() {
        return roleName;
    }

    @JsonProperty("userDetails.companies.roleName")
    public void setUserDetailsCompaniesRoleName(String roleName) {
        this.roleName = roleName;
    }
}