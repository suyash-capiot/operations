package com.coxandkings.travel.operations.model.communication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "communication_tags")
public class CommunicationTags {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @Column(name="bookId")
    private String bookId ;
    @Column(name="orderId")
    private String orderId ;
    @Column(name="supplierId")
    private String supplierId ;
    @Column(name="clientId")
    private String clientId ;
    @Column(name="customerId")
    private String customerId ;
    @Column(name="function")
    private String function ;
    @Column(name="process")
    private String process ;
    @Column(name="scenario")
    private String scenario ;
    @Column(name="actionType")
    private String actionType ;
    @OneToOne
    @JoinColumn(name = "communication_id")
    @JsonIgnore
    private BaseCommunication baseCommunication;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public BaseCommunication getBaseCommunication() {
        return baseCommunication;
    }

    public void setBaseCommunication(BaseCommunication baseCommunication) {
        this.baseCommunication = baseCommunication;
    }
}
