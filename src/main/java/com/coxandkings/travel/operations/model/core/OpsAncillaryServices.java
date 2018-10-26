package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsAncillaryServices implements Serializable {

    @JsonProperty("ancillaryInfo")
    private List<OpsAncillaryInfo> ancillaryInfo = new ArrayList<OpsAncillaryInfo>();

    public OpsAncillaryServices() {
    }

    public List<OpsAncillaryInfo> getAncillaryInfo() {
        return ancillaryInfo;
    }

    public void setAncillaryInfo(List<OpsAncillaryInfo> ancillaryInfo) {
        this.ancillaryInfo = ancillaryInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpsAncillaryServices that = (OpsAncillaryServices) o;
        return Objects.equals(ancillaryInfo, that.ancillaryInfo);
    }

    @Override
    public int hashCode() {

        return Objects.hash(ancillaryInfo);
    }
}
