package com.coxandkings.travel.operations.helper.booking.product.activities;

import com.coxandkings.travel.operations.helper.booking.product.Product;
import com.coxandkings.travel.operations.helper.booking.product.transport.car.Car;

public abstract class Activity extends Product {

    private Car car;

    private float duration;

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }


    public Activity() {
    }
}
