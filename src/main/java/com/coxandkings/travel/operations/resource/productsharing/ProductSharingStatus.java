package com.coxandkings.travel.operations.resource.productsharing;

public enum ProductSharingStatus {
    PROGRESS("PROGRESS"),
    ACCEPT("ACCEPT"),
    REJECT("REJECT");


    private String productSharingStatus;

    private ProductSharingStatus(String aProductSharingStatus) {
        this.productSharingStatus = aProductSharingStatus;
    }

    public ProductSharingStatus from(String aProductSharingStatus) {
        if (aProductSharingStatus == null || aProductSharingStatus.isEmpty()) {
            return null;
        }

        ProductSharingStatus aStatus = null;
        for (ProductSharingStatus aTmpStatus : ProductSharingStatus.values()) {
            if (aTmpStatus.getProductSharingStatus().equalsIgnoreCase(aProductSharingStatus)) {
                aStatus = aTmpStatus;
                break;
            }
        }

        return aStatus;
    }

    public String getProductSharingStatus () {
        return this.productSharingStatus;
    }

    @Override
    public String toString() {
        return "ProductSharingStatus{" +
                "productSharingStatus='" + productSharingStatus + '\'' +
                '}';
    }
}
