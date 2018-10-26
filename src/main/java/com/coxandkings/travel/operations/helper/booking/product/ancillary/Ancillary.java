package com.coxandkings.travel.operations.helper.booking.product.ancillary;

import com.coxandkings.travel.operations.helper.booking.product.Product;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "subType", visible = true)
@JsonSubTypes(
    {
        @JsonSubTypes.Type(value = Parking.class, name = "parking"),
        @JsonSubTypes.Type(value = PetCare.class, name = "petCare"),
        @JsonSubTypes.Type(value = Show.class, name = "shows")
    }
)
public abstract class Ancillary extends Product {

    Product mainProduct;
    private String subType;

    public Product getMainProduct() {
        return mainProduct;
    }

    public void setMainProduct(Product mainProduct) {
        this.mainProduct = mainProduct;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }
}
