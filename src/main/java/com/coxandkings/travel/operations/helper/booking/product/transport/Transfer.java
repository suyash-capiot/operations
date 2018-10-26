package com.coxandkings.travel.operations.helper.booking.product.transport;

import com.coxandkings.travel.operations.helper.booking.Location;
import com.coxandkings.travel.operations.helper.booking.product.Product;

public abstract class Transfer extends Product {
    private Location startLocation;
    private Location endLocation;

    public Transfer() {
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }
}
