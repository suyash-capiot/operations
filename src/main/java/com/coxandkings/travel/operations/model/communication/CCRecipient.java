package com.coxandkings.travel.operations.model.communication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "ccrecipient")
public class CCRecipient {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;
    private String recipient;
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

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public BaseCommunication getBaseCommunication() {
        return baseCommunication;
    }

    public void setBaseCommunication(BaseCommunication baseCommunication) {
        this.baseCommunication = baseCommunication;
    }
}
