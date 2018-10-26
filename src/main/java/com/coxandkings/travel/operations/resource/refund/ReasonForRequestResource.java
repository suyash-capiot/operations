package com.coxandkings.travel.operations.resource.refund;

import com.coxandkings.travel.operations.enums.refund.ReasonForRequest;

import java.util.Objects;

public class ReasonForRequestResource {
    private ReasonForRequest id;
    private String name;

    public ReasonForRequest getId() {
        return id;
    }

    public void setId(ReasonForRequest id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ReasonForRequestResource() {
    }

    public ReasonForRequestResource(ReasonForRequest id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReasonForRequestResource that = (ReasonForRequestResource) o;
        return id == that.id &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name);
    }
}
