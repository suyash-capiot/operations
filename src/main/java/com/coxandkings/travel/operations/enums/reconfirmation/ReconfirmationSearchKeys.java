package com.coxandkings.travel.operations.enums.reconfirmation;

public enum ReconfirmationSearchKeys {

    configurationFor("productDetails.configurationFor"),
    productCategory("productDetails.productCategory"),
    productCatSubtype("productDetails.productCatSubtype"),
    productId("productDetails.productId"),
    productNameSubType("productDetails.productNameSubType"),
    productFlavor("productDetails.productFlavor");


    private String value;

    ReconfirmationSearchKeys(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
