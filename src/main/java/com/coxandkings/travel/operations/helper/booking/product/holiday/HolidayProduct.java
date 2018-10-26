package com.coxandkings.travel.operations.helper.booking.product.holiday;

import com.coxandkings.travel.operations.helper.booking.product.Product;

import java.io.Serializable;

public class HolidayProduct implements Serializable {
    private String id;
    private Integer day;
    private Holiday holiday;
    Product product;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Holiday getHoliday() {
        return holiday;
    }

    public void setHoliday(Holiday holiday) {
        this.holiday = holiday;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
