package com.coxandkings.travel.operations.model.mailroomanddispatch;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "MAILROOMMASTERLOCK")
public class RequestLockObject {

    @Id
    @GeneratedValue
    private UUID id;

    @Column
    private String userId;

    @Column
    private boolean enabled;

    @Column
    private String workflowId;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonBackReference
    private MailRoomMaster mailRoomMaster;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonBackReference
    private InboundEntry inboundEntry;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonBackReference
    private OutboundDispatch outboundDispatch;

    @Transient
    private String user;

    public OutboundDispatch getOutboundDispatch() {
        return outboundDispatch;
    }

    public void setOutboundDispatch(OutboundDispatch outboundDispatch) {
        this.outboundDispatch = outboundDispatch;
    }

    public InboundEntry getInboundEntry() {
        return inboundEntry;
    }

    public void setInboundEntry(InboundEntry inboundEntry) {
        this.inboundEntry = inboundEntry;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    public MailRoomMaster getMailRoomMaster() {
        return mailRoomMaster;
    }

    public void setMailRoomMaster(MailRoomMaster mailRoomMaster) {
        this.mailRoomMaster = mailRoomMaster;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}