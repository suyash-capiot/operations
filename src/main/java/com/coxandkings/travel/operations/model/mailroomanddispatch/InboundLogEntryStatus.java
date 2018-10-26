package com.coxandkings.travel.operations.model.mailroomanddispatch;

import com.coxandkings.travel.operations.enums.mailroomanddispatch.InboundEntryStatus;
import com.coxandkings.travel.operations.enums.mailroomanddispatch.ProofOfDelivery;
import com.coxandkings.travel.operations.utils.mailroomAndDispatch.MailroomZonedDateSerializer;
import com.coxandkings.travel.operations.utils.mailroomAndDispatch.MailroomZonedDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "inbound_status")
public class InboundLogEntryStatus {


    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="inbound_status_id")
    private String id;

    /*@Column
    private String name;//For Multilingual*/

    @Column
    @Enumerated(EnumType.STRING)
    private InboundEntryStatus status;

    @Column
    @JsonSerialize(using = MailroomZonedDateSerializer.class)
    @JsonDeserialize(using = MailroomZonedDateTimeDeserializer.class)
    private ZonedDateTime dateOfDelivery;

    @Column
    private String employeeName;

    @Column
    private String employeeId;

    @Column
    private Integer piecesDelivered;

    @NotNull
    @Column
    @Enumerated(EnumType.STRING)
    private ProofOfDelivery proofOfDelivery;

    @NotNull
    @Column
    private String deliveryEmployee;

    @Column
    private String remarks;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public InboundEntryStatus getStatus() {
        return status;
    }

    public void setStatus(InboundEntryStatus status) {
        this.status = status;
    }

    public ZonedDateTime getDateOfDelivery() {
        return dateOfDelivery;
    }

    public void setDateOfDelivery(ZonedDateTime dateOfDelivery) {
        this.dateOfDelivery = dateOfDelivery;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getPiecesDelivered() {
        return piecesDelivered;
    }

    public void setPiecesDelivered(Integer piecesDelivered) {
        this.piecesDelivered = piecesDelivered;
    }

    public ProofOfDelivery getProofOfDelivery() {
        return proofOfDelivery;
    }

    public void setProofOfDelivery(ProofOfDelivery proofOfDelivery) {
        this.proofOfDelivery = proofOfDelivery;
    }

    public String getDeliveryEmployee() {
        return deliveryEmployee;
    }

    public void setDeliveryEmployee(String deliveryEmployee) {
        this.deliveryEmployee = deliveryEmployee;
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
        InboundLogEntryStatus that = (InboundLogEntryStatus) o;
        return Objects.equals(id, that.id) &&
                status == that.status &&
                Objects.equals(dateOfDelivery, that.dateOfDelivery) &&
                Objects.equals(employeeName, that.employeeName) &&
                Objects.equals(employeeId, that.employeeId) &&
                Objects.equals(piecesDelivered, that.piecesDelivered) &&
                proofOfDelivery == that.proofOfDelivery &&
                Objects.equals(deliveryEmployee, that.deliveryEmployee) &&
                Objects.equals(remarks, that.remarks);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, status, dateOfDelivery, employeeName, employeeId, piecesDelivered, proofOfDelivery, deliveryEmployee, remarks);
    }
}
