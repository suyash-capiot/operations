package com.coxandkings.travel.operations.model.prodreview;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "product_rating")
public class ProductRating {
    @Id
    @NotNull(message = "product id should not null")
    @Column(unique = true)
    private String productId;
    private double rating;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "ProductRating{" +
                "productId='" + productId + '\'' +
                ", rating=" + rating +
                '}';
    }
}
