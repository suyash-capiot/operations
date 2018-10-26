
package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "ancillaryInfo"
})
public class AncillaryServices implements Serializable
{

    @JsonProperty("ancillaryInfo")
    private List<AncillaryInfo> ancillaryInfo = new ArrayList<AncillaryInfo>();
    private final static long serialVersionUID = 5795921425265539055L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public AncillaryServices() {
    }

    /**
     * 
     * @param ancillaryInfo
     */
    public AncillaryServices(List<AncillaryInfo> ancillaryInfo) {
        super();
        this.ancillaryInfo = ancillaryInfo;
    }

    @JsonProperty("ancillaryInfo")
    public List<AncillaryInfo> getAncillaryInfo() {
        return ancillaryInfo;
    }

    @JsonProperty("ancillaryInfo")
    public void setAncillaryInfo(List<AncillaryInfo> ancillaryInfo) {
        this.ancillaryInfo = ancillaryInfo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(AncillaryServices.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("ancillaryInfo");
        sb.append('=');
        sb.append(((this.ancillaryInfo == null)?"<null>":this.ancillaryInfo));
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
        result = ((result* 31)+((this.ancillaryInfo == null)? 0 :this.ancillaryInfo.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AncillaryServices) == false) {
            return false;
        }
        AncillaryServices rhs = ((AncillaryServices) other);
        return ((this.ancillaryInfo == rhs.ancillaryInfo)||((this.ancillaryInfo!= null)&&this.ancillaryInfo.equals(rhs.ancillaryInfo)));
    }

}
