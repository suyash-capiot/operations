package com.coxandkings.travel.operations.helper.booking.product.transport.rail;

import com.coxandkings.travel.operations.helper.booking.product.Product;

import java.util.Set;

public class RailPass extends Product {

    private String passName;
    private Set<String> countriesCoveredIds;
    private Long validTill;

    public String getPassName() {
        return passName;
    }

    public void setPassName(String passName) {
        this.passName = passName;
    }

    public Set<String> getCountriesCoveredIds() {
        return countriesCoveredIds;
    }

    public void setCountriesCoveredIds(Set<String> countriesCoveredIds) {
        this.countriesCoveredIds = countriesCoveredIds;
    }

    public Long getValidTill() {
        return validTill;
    }

    public void setValidTill(Long validTill) {
        this.validTill = validTill;
    }
}
