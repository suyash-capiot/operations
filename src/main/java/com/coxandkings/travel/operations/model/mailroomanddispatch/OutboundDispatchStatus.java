package com.coxandkings.travel.operations.model.mailroomanddispatch;

import com.coxandkings.travel.operations.enums.mailroomanddispatch.DispatchStatus;
import com.coxandkings.travel.operations.utils.mailroomAndDispatch.MailroomZonedDateSerializer;
import com.coxandkings.travel.operations.utils.mailroomAndDispatch.MailroomZonedDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.ZonedDateTime;
@Entity
@Table(name="outbound_dispatch_status")
public class OutboundDispatchStatus {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="outbound_dispatch_status_id")
    private String id;
    @Enumerated
    private DispatchStatus status;
    @JsonDeserialize(using = MailroomZonedDateTimeDeserializer.class)
    @JsonSerialize(using = MailroomZonedDateSerializer.class)
    private ZonedDateTime dateOfDelivery;
    private String remarks;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DispatchStatus getStatus() {
        return status;
    }

    public void setStatus(DispatchStatus status) {
        this.status = status;
    }

    public ZonedDateTime getDateOfDelivery() {
        return dateOfDelivery;
    }

    public void setDateOfDelivery(ZonedDateTime dateOfDelivery) {
        this.dateOfDelivery = dateOfDelivery;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
