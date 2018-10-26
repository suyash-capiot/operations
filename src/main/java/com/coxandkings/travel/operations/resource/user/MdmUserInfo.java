
package com.coxandkings.travel.operations.resource.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;


public class MdmUserInfo implements Serializable {

    @JsonProperty("user")
    private User user;
    @JsonProperty("currentCompany")
    private CurrentCompany currentCompany;
    private final static long serialVersionUID = -2575430441244765113L;
    @JsonIgnore
    private Long timeStamp;
    /**
     * No args constructor for use in serialization
     */
    public MdmUserInfo() {
    }

    /**
     * @param currentCompany
     * @param user
     */
    public MdmUserInfo(User user, CurrentCompany currentCompany) {
        super();
        this.user = user;
        this.currentCompany = currentCompany;
    }

    @JsonProperty("user")
    public User getUser() {
        return user;
    }

    @JsonProperty("user")
    public void setUser(User user) {
        this.user = user;
    }

    @JsonProperty("currentCompany")
    public CurrentCompany getCurrentCompany() {
        return currentCompany;
    }

    @JsonProperty("currentCompany")
    public void setCurrentCompany(CurrentCompany currentCompany) {
        this.currentCompany = currentCompany;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(MdmUserInfo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("user");
        sb.append('=');
        sb.append(((this.user == null) ? "<null>" : this.user));
        sb.append(',');
        sb.append("currentCompany");
        sb.append('=');
        sb.append(((this.currentCompany == null) ? "<null>" : this.currentCompany));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result * 31) + ((this.user == null) ? 0 : this.user.hashCode()));
        result = ((result * 31) + ((this.currentCompany == null) ? 0 : this.currentCompany.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof MdmUserInfo) == false) {
            return false;
        }
        MdmUserInfo rhs = ((MdmUserInfo) other);
        return (((this.user == rhs.user) || ((this.user != null) && this.user.equals(rhs.user))) && ((this.currentCompany == rhs.currentCompany) || ((this.currentCompany != null) && this.currentCompany.equals(rhs.currentCompany))));
    }

}
