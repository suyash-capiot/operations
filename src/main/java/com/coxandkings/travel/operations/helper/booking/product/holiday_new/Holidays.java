package com.coxandkings.travel.operations.helper.booking.product.holiday_new;

import com.coxandkings.travel.operations.helper.booking.product.Product;

import java.util.Set;

public class Holidays extends Product {

    private Integer id;
    private String companyPackageCategory;
    private Integer numberOfNights;
    private Integer numberOfDays;

    //Room Information

    private Set<Product> products;
    private String hub;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyPackageCategory() {
        return companyPackageCategory;
    }

    public void setCompanyPackageCategory(String companyPackageCategory) {
        this.companyPackageCategory = companyPackageCategory;
    }

    public Integer getNumberOfNights() {
        return numberOfNights;
    }

    public void setNumberOfNights(Integer numberOfNights) {
        this.numberOfNights = numberOfNights;
    }

    public Integer getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(Integer numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public String getHub() {
        return hub;
    }

    public void setHub(String hub) {
        this.hub = hub;
    }
}
