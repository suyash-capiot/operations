package com.coxandkings.travel.operations.helper.booking.product.holiday;

import com.coxandkings.travel.operations.helper.booking.product.Product;

import java.util.Set;

public class Holiday extends Product {
    private String packageType;//Custom, Set
    private Set<HolidayProduct> holidayProducts;

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public Set<HolidayProduct> getHolidayProducts() {
        return holidayProducts;
    }

    public void setHolidayProducts(Set<HolidayProduct> holidayProducts) {
        this.holidayProducts = holidayProducts;
    }
}
