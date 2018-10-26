package com.coxandkings.travel.operations.model.prodreview;

import java.io.Serializable;
import java.util.Objects;

public class Identity implements Serializable {

    private String id;

    private String uniqueReferenceNumber;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identity identity = (Identity) o;
        return Objects.equals(id, identity.id) &&
                Objects.equals(uniqueReferenceNumber, identity.uniqueReferenceNumber);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, uniqueReferenceNumber);
    }
}
