package com.coxandkings.travel.operations.model.mailroomanddispatch;

import com.coxandkings.travel.operations.enums.mailroomanddispatch.DispatchStatus;

import com.coxandkings.travel.operations.utils.mailroomAndDispatch.MailroomZonedDateSerializer;
import com.coxandkings.travel.operations.utils.mailroomAndDispatch.MailroomZonedDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "dispatch_outbound_status")
public class OutboundStatus {


    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="id")
    private String id;

    @Column
    private String name;//For Multilingual

    @Column
    @Enumerated(EnumType.STRING)
    private DispatchStatus code;
    @JsonDeserialize(using = MailroomZonedDateTimeDeserializer.class)
    @JsonSerialize(using = MailroomZonedDateSerializer.class)
    private ZonedDateTime deliveryDate;
    private String remarks;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DispatchStatus getCode() {
        return code;
    }

    public void setCode(DispatchStatus code) {
        this.code = code;
    }

    public ZonedDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(ZonedDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OutboundStatus that = (OutboundStatus) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                code == that.code &&
                Objects.equals(deliveryDate, that.deliveryDate) &&
                Objects.equals(remarks, that.remarks);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, code, deliveryDate, remarks);
    }

    @Override
    public String toString() {
        return "OutboundStatus{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", code=" + code +
                ", deliveryDate=" + deliveryDate +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
