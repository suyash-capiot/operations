package com.coxandkings.travel.operations.resource.communication;

import com.coxandkings.travel.operations.model.communication.BaseCommunication;

import java.util.List;

public class CommunicationHistoryResponse {
    private List<BaseCommunication> communications;
    private Long totalUnread;

    public List<BaseCommunication> getCommunications() {
        return communications;
    }

    public void setCommunications(List<BaseCommunication> communications) {
        this.communications = communications;
    }

    public Long getTotalUnread() {
        return totalUnread;
    }

    public void setTotalUnread(Long totalUnread) {
        this.totalUnread = totalUnread;
    }
}
