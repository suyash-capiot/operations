package com.coxandkings.travel.operations.helper.booking.product.forex;

import com.coxandkings.travel.operations.helper.booking.product.Product;

import java.util.Set;

public class Forex extends Product {
    private String currency;
    private Set<ForexPassenger> forexPassengers;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Set<ForexPassenger> getForexPassengers() {
        return forexPassengers;
    }

    public void setForexPassengers(Set<ForexPassenger> forexPassengers) {
        this.forexPassengers = forexPassengers;
    }
}
