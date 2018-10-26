package com.coxandkings.travel.operations.resource.productsharing;

public class ProductSharingResponse {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ProductSharingResponse{" +
                "message='" + message + '\'' +
                '}';
    }
}
