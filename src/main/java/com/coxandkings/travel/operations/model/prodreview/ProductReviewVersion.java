package com.coxandkings.travel.operations.model.prodreview;


import java.io.Serializable;
import java.util.Objects;

public class ProductReviewVersion implements Serializable{

    private String id;

    private Float versionNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Float getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Float versionNumber) {
        this.versionNumber = versionNumber;
    }

    public ProductReviewVersion(String id, Float versionNumber) {
        this.id = id;
        this.versionNumber = versionNumber;
    }

    public ProductReviewVersion() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductReviewVersion that = (ProductReviewVersion) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(versionNumber, that.versionNumber);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, versionNumber);
    }
}
