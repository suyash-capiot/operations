package com.coxandkings.travel.operations.resource.outbound.be;

public class TicketingPCCResource {
    private String userId;
    private String productId;
    private String ticketingPCC;

    public TicketingPCCResource() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTicketingPCC() {
        return ticketingPCC;
    }

    public void setTicketingPCC(String ticketingPCC) {
        this.ticketingPCC = ticketingPCC;
    }

    @Override
    public String toString() {
        return "TicketingPCCResource{" +
                "userId='" + userId + '\'' +
                ", productId='" + productId + '\'' +
                ", ticketingPCC='" + ticketingPCC + '\'' +
                '}';
    }
}
