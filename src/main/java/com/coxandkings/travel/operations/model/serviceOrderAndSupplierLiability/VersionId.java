package com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability;

import javax.persistence.PrePersist;
import java.io.Serializable;
import java.util.Objects;

public class VersionId implements Serializable{
    private String uniqueId;
    private Float versionNumber;

    public VersionId(){}

    public VersionId(String uniqueId, Float versionNumber) {
        this.uniqueId = uniqueId;
        this.versionNumber = versionNumber;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Float getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Float versionNumber) {
        this.versionNumber = versionNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VersionId versionId = (VersionId) o;
        return Objects.equals(uniqueId, versionId.uniqueId) &&
                Objects.equals(versionNumber, versionId.versionNumber);
    }

    @Override
    public int hashCode() {

        return Objects.hash(uniqueId, versionNumber);
    }

    @PrePersist
    public void generateVersion(){
        VersionId versionId=new VersionId();
        versionId.setUniqueId("1");
        versionId.setVersionNumber(1.1f);
    }
}
