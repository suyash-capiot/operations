package com.coxandkings.travel.operations.response;

public class SchedulerResponse {

    private String bookId;
    private String orderId;
    private String productSubCategory;
    private String clientId;
    private String clientGroup;
    private String clientType;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductSubCategory() {
        return productSubCategory;
    }

    public void setProductSubCategory(String productSubCategory) {
        this.productSubCategory = productSubCategory;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientGroup() {
        return clientGroup;
    }

    public void setClientGroup(String clientGroup) {
        this.clientGroup = clientGroup;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    @Override
    public String toString() {
        return "SchedulerResponse{" +
                "bookId='" + bookId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", productSubCategory='" + productSubCategory + '\'' +
                ", clientId='" + clientId + '\'' +
                ", clientGroup='" + clientGroup + '\'' +
                ", clientType='" + clientType + '\'' +
                '}';
    }
}
