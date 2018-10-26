
package com.coxandkings.travel.operations.resource.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "salesGroup",
    "salesOffice",
    "SBU",
    "BU",
    "isManager",
    "reportingManager",
    "role",
    "designation",
    "mobile",
    "email",
    "lastName",
    "middleName",
    "firstName",
    "employeeId",
    "lastLoggedOn",
    "companies"
})
public class UserDetails implements Serializable
{

    @JsonProperty("salesGroup")
    private String salesGroup;
    @JsonProperty("salesOffice")
    private String salesOffice;
    @JsonProperty("SBU")
    private String sBU;
    @JsonProperty("BU")
    private String bU;
    @JsonProperty("isManager")
    private Boolean isManager;
    @JsonProperty("reportingManager")
    private String reportingManager;
    @JsonProperty("role")
    private String role;
    @JsonProperty("designation")
    private String designation;
    @JsonProperty("mobile")
    private String mobile;
    @JsonProperty("email")
    private String email;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("middleName")
    private String middleName;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("employeeId")
    private String employeeId;
    @JsonProperty("lastLoggedOn")
    private String lastLoggedOn;
    @JsonProperty("companies")
    private List<Company> companies = new ArrayList<Company>();
    private final static long serialVersionUID = -440881376265210869L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public UserDetails() {
    }

    /**
     * 
     * @param lastName
     * @param reportingManager
     * @param role
     * @param salesOffice
     * @param sBU
     * @param salesGroup
     * @param mobile
     * @param employeeId
     * @param firstName
     * @param companies
     * @param bU
     * @param lastLoggedOn
     * @param isManager
     * @param middleName
     * @param designation
     * @param email
     */
    public UserDetails(String salesGroup, String salesOffice, String sBU, String bU, Boolean isManager, String reportingManager, String role, String designation, String mobile, String email, String lastName, String middleName, String firstName, String employeeId, String lastLoggedOn, List<Company> companies) {
        super();
        this.salesGroup = salesGroup;
        this.salesOffice = salesOffice;
        this.sBU = sBU;
        this.bU = bU;
        this.isManager = isManager;
        this.reportingManager = reportingManager;
        this.role = role;
        this.designation = designation;
        this.mobile = mobile;
        this.email = email;
        this.lastName = lastName;
        this.middleName = middleName;
        this.firstName = firstName;
        this.employeeId = employeeId;
        this.lastLoggedOn = lastLoggedOn;
        this.companies = companies;
    }

    @JsonProperty("salesGroup")
    public String getSalesGroup() {
        return salesGroup;
    }

    @JsonProperty("salesGroup")
    public void setSalesGroup(String salesGroup) {
        this.salesGroup = salesGroup;
    }

    @JsonProperty("salesOffice")
    public String getSalesOffice() {
        return salesOffice;
    }

    @JsonProperty("salesOffice")
    public void setSalesOffice(String salesOffice) {
        this.salesOffice = salesOffice;
    }

    @JsonProperty("SBU")
    public String getSBU() {
        return sBU;
    }

    @JsonProperty("SBU")
    public void setSBU(String sBU) {
        this.sBU = sBU;
    }

    @JsonProperty("BU")
    public String getBU() {
        return bU;
    }

    @JsonProperty("BU")
    public void setBU(String bU) {
        this.bU = bU;
    }

    @JsonProperty("isManager")
    public Boolean getIsManager() {
        return isManager;
    }

    @JsonProperty("isManager")
    public void setIsManager(Boolean isManager) {
        this.isManager = isManager;
    }

    @JsonProperty("reportingManager")
    public String getReportingManager() {
        return reportingManager;
    }

    @JsonProperty("reportingManager")
    public void setReportingManager(String reportingManager) {
        this.reportingManager = reportingManager;
    }

    @JsonProperty("role")
    public String getRole() {
        return role;
    }

    @JsonProperty("role")
    public void setRole(String role) {
        this.role = role;
    }

    @JsonProperty("designation")
    public String getDesignation() {
        return designation;
    }

    @JsonProperty("designation")
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    @JsonProperty("mobile")
    public String getMobile() {
        return mobile;
    }

    @JsonProperty("mobile")
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("lastName")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty("lastName")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty("middleName")
    public String getMiddleName() {
        return middleName;
    }

    @JsonProperty("middleName")
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @JsonProperty("firstName")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("firstName")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty("employeeId")
    public String getEmployeeId() {
        return employeeId;
    }

    @JsonProperty("employeeId")
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    @JsonProperty("lastLoggedOn")
    public String getLastLoggedOn() {
        return lastLoggedOn;
    }

    @JsonProperty("lastLoggedOn")
    public void setLastLoggedOn(String lastLoggedOn) {
        this.lastLoggedOn = lastLoggedOn;
    }

    @JsonProperty("companies")
    public List<Company> getCompanies() {
        return companies;
    }

    @JsonProperty("companies")
    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(UserDetails.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("salesGroup");
        sb.append('=');
        sb.append(((this.salesGroup == null)?"<null>":this.salesGroup));
        sb.append(',');
        sb.append("salesOffice");
        sb.append('=');
        sb.append(((this.salesOffice == null)?"<null>":this.salesOffice));
        sb.append(',');
        sb.append("sBU");
        sb.append('=');
        sb.append(((this.sBU == null)?"<null>":this.sBU));
        sb.append(',');
        sb.append("bU");
        sb.append('=');
        sb.append(((this.bU == null)?"<null>":this.bU));
        sb.append(',');
        sb.append("isManager");
        sb.append('=');
        sb.append(((this.isManager == null)?"<null>":this.isManager));
        sb.append(',');
        sb.append("reportingManager");
        sb.append('=');
        sb.append(((this.reportingManager == null)?"<null>":this.reportingManager));
        sb.append(',');
        sb.append("role");
        sb.append('=');
        sb.append(((this.role == null)?"<null>":this.role));
        sb.append(',');
        sb.append("designation");
        sb.append('=');
        sb.append(((this.designation == null)?"<null>":this.designation));
        sb.append(',');
        sb.append("mobile");
        sb.append('=');
        sb.append(((this.mobile == null)?"<null>":this.mobile));
        sb.append(',');
        sb.append("email");
        sb.append('=');
        sb.append(((this.email == null)?"<null>":this.email));
        sb.append(',');
        sb.append("lastName");
        sb.append('=');
        sb.append(((this.lastName == null)?"<null>":this.lastName));
        sb.append(',');
        sb.append("middleName");
        sb.append('=');
        sb.append(((this.middleName == null)?"<null>":this.middleName));
        sb.append(',');
        sb.append("firstName");
        sb.append('=');
        sb.append(((this.firstName == null)?"<null>":this.firstName));
        sb.append(',');
        sb.append("employeeId");
        sb.append('=');
        sb.append(((this.employeeId == null)?"<null>":this.employeeId));
        sb.append(',');
        sb.append("lastLoggedOn");
        sb.append('=');
        sb.append(((this.lastLoggedOn == null)?"<null>":this.lastLoggedOn));
        sb.append(',');
        sb.append("companies");
        sb.append('=');
        sb.append(((this.companies == null)?"<null>":this.companies));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.lastName == null)? 0 :this.lastName.hashCode()));
        result = ((result* 31)+((this.reportingManager == null)? 0 :this.reportingManager.hashCode()));
        result = ((result* 31)+((this.role == null)? 0 :this.role.hashCode()));
        result = ((result* 31)+((this.salesOffice == null)? 0 :this.salesOffice.hashCode()));
        result = ((result* 31)+((this.sBU == null)? 0 :this.sBU.hashCode()));
        result = ((result* 31)+((this.salesGroup == null)? 0 :this.salesGroup.hashCode()));
        result = ((result* 31)+((this.mobile == null)? 0 :this.mobile.hashCode()));
        result = ((result* 31)+((this.employeeId == null)? 0 :this.employeeId.hashCode()));
        result = ((result* 31)+((this.firstName == null)? 0 :this.firstName.hashCode()));
        result = ((result* 31)+((this.companies == null)? 0 :this.companies.hashCode()));
        result = ((result* 31)+((this.bU == null)? 0 :this.bU.hashCode()));
        result = ((result* 31)+((this.lastLoggedOn == null)? 0 :this.lastLoggedOn.hashCode()));
        result = ((result* 31)+((this.isManager == null)? 0 :this.isManager.hashCode()));
        result = ((result* 31)+((this.middleName == null)? 0 :this.middleName.hashCode()));
        result = ((result* 31)+((this.designation == null)? 0 :this.designation.hashCode()));
        result = ((result* 31)+((this.email == null)? 0 :this.email.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof UserDetails) == false) {
            return false;
        }
        UserDetails rhs = ((UserDetails) other);
        return (((((((((((((((((this.lastName == rhs.lastName)||((this.lastName!= null)&&this.lastName.equals(rhs.lastName)))&&((this.reportingManager == rhs.reportingManager)||((this.reportingManager!= null)&&this.reportingManager.equals(rhs.reportingManager))))&&((this.role == rhs.role)||((this.role!= null)&&this.role.equals(rhs.role))))&&((this.salesOffice == rhs.salesOffice)||((this.salesOffice!= null)&&this.salesOffice.equals(rhs.salesOffice))))&&((this.sBU == rhs.sBU)||((this.sBU!= null)&&this.sBU.equals(rhs.sBU))))&&((this.salesGroup == rhs.salesGroup)||((this.salesGroup!= null)&&this.salesGroup.equals(rhs.salesGroup))))&&((this.mobile == rhs.mobile)||((this.mobile!= null)&&this.mobile.equals(rhs.mobile))))&&((this.employeeId == rhs.employeeId)||((this.employeeId!= null)&&this.employeeId.equals(rhs.employeeId))))&&((this.firstName == rhs.firstName)||((this.firstName!= null)&&this.firstName.equals(rhs.firstName))))&&((this.companies == rhs.companies)||((this.companies!= null)&&this.companies.equals(rhs.companies))))&&((this.bU == rhs.bU)||((this.bU!= null)&&this.bU.equals(rhs.bU))))&&((this.lastLoggedOn == rhs.lastLoggedOn)||((this.lastLoggedOn!= null)&&this.lastLoggedOn.equals(rhs.lastLoggedOn))))&&((this.isManager == rhs.isManager)||((this.isManager!= null)&&this.isManager.equals(rhs.isManager))))&&((this.middleName == rhs.middleName)||((this.middleName!= null)&&this.middleName.equals(rhs.middleName))))&&((this.designation == rhs.designation)||((this.designation!= null)&&this.designation.equals(rhs.designation))))&&((this.email == rhs.email)||((this.email!= null)&&this.email.equals(rhs.email))));
    }

}
