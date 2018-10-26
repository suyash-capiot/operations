package com.coxandkings.travel.operations.resource.user;

import com.coxandkings.travel.operations.resource.user.role.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class OpsUser implements UserDetails {

    private String userID;
    private String username;
    private String password;
    private String token;
    private List<Role> roles;

    private String companyName;
    private String companyId;
    private String companyGroupName;
    private String companyGroupId;
    private String groupOfCompanyName;
    private String groupOfCompanyId;
    private String branchName;
    private String SBU;
    private String BU;

    private Collection<? extends GrantedAuthority> authorities;

    public OpsUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public OpsUser(String username, String password, Collection<? extends GrantedAuthority> authorities, String token) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.token = token;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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

    public String getCompanyGroupName() {
        return companyGroupName;
    }

    public void setCompanyGroupName(String companyGroupName) {
        this.companyGroupName = companyGroupName;
    }

    public String getCompanyGroupId() {
        return companyGroupId;
    }

    public void setCompanyGroupId(String companyGroupId) {
        this.companyGroupId = companyGroupId;
    }

    public String getGroupOfCompanyName() {
        return groupOfCompanyName;
    }

    public void setGroupOfCompanyName(String groupOfCompanyName) {
        this.groupOfCompanyName = groupOfCompanyName;
    }

    public String getGroupOfCompanyId() {
        return groupOfCompanyId;
    }

    public void setGroupOfCompanyId(String groupOfCompanyId) {
        this.groupOfCompanyId = groupOfCompanyId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getSBU() {
        return SBU;
    }

    public void setSBU(String SBU) {
        this.SBU = SBU;
    }

    public String getBU() {
        return BU;
    }

    public void setBU(String BU) {
        this.BU = BU;
    }
}
