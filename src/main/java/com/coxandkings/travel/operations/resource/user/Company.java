
package com.coxandkings.travel.operations.resource.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.coxandkings.travel.operations.resource.user.role.Role;
import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class Company implements Serializable {

    @JsonProperty("companyName")
    private String companyName;

    @JsonProperty("companyId")
    private String companyId;

    private List<Role> roles;

    @JsonProperty("defaultCompany")
    private Boolean defaultCompany;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Boolean getDefaultCompany() {
        return defaultCompany;
    }

    public void setDefaultCompany(Boolean defaultCompany) {
        this.defaultCompany = defaultCompany;
    }
}
