package com.coxandkings.travel.operations.model.forex;

import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "forex_disbursement_details")
public class DisbursementDetails {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column
    private String id;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime dateOfDisbursement;

    @Column(name = "indent_id")
    private String indent;

    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ZonedDateTime getDateOfDisbursement() {
        return dateOfDisbursement;
    }

    public void setDateOfDisbursement(ZonedDateTime dateOfDisbursement) {
        this.dateOfDisbursement = dateOfDisbursement;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIndent() {
        return indent;
    }

    public void setIndent(String indent) {
        this.indent = indent;
    }
}
