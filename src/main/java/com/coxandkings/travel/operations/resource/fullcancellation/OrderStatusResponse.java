package com.coxandkings.travel.operations.resource.fullcancellation;

public class OrderStatusResponse {
    private String status ;
    private String messsage;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMesssage() {
        return messsage;
    }

    public void setMesssage(String messsage) {
        this.messsage = messsage;
    }

    @Override
    public String toString() {
        return "OrderStatusResponse{" +
                "status='" + status + '\'' +
                ", messsage='" + messsage + '\'' +
                '}';
    }
}
