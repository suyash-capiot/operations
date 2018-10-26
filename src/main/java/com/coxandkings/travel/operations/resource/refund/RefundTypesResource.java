package com.coxandkings.travel.operations.resource.refund;

import com.coxandkings.travel.operations.enums.refund.RefundTypes;

import java.util.Objects;

public class RefundTypesResource {
    private RefundTypes id;
    private String name;

    public RefundTypes getId() {
        return id;
    }

    public void setId(RefundTypes id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RefundTypesResource() {
    }

    public RefundTypesResource(RefundTypes id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefundTypesResource that = (RefundTypesResource) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name);
    }
}
