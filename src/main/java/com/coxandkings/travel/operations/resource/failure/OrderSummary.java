package com.coxandkings.travel.operations.resource.failure;

public class OrderSummary {

    private String travelDate;

    private String detailsSummary;

    private String productName;

    public String getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(String travelDate) {
        this.travelDate = travelDate;
    }

    public String getDetailsSummary() {
        return detailsSummary;
    }

    public void setDetailsSummary(String detailsSummary) {
        this.detailsSummary = detailsSummary;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
