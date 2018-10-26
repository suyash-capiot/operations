package com.coxandkings.travel.operations.helper.booking.product.comboProducts;

import com.coxandkings.travel.operations.helper.booking.product.Product;

import java.util.Set;

public class ComboProducts extends Product {

    private Set<Product> products;

    private ComboSellingPrice comboSellingPrice;

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public ComboSellingPrice getComboSellingPrice() {
        return comboSellingPrice;
    }

    public void setComboSellingPrice(ComboSellingPrice comboSellingPrice) {
        this.comboSellingPrice = comboSellingPrice;
    }
}
