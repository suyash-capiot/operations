package com.coxandkings.travel.operations.model.failure;

import com.coxandkings.travel.operations.enums.failure.FailureActions;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Map;

@Entity
@Table(name = "failure_details")
public class FailureDetails {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;

    @Column
    private String bookID;

    @ElementCollection(fetch = FetchType.EAGER)
    private Map<String, FailureActions> orderActions;

    @Column
    private Boolean deleted = false;

    @Column
    private Integer communicationCount = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public Integer getCommunicationCount() {
        return communicationCount;
    }

    public void setCommunicationCount(Integer communicationCount) {
        this.communicationCount = communicationCount;
    }

    public Map<String, FailureActions> getOrderActions() {
        return orderActions;
    }

    public void setOrderActions(Map<String, FailureActions> orderActions) {
        this.orderActions = orderActions;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
