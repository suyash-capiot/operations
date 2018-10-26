package com.coxandkings.travel.operations.model.mailroomanddispatch;


import com.coxandkings.travel.operations.utils.mailroomAndDispatch.MailroomZonedDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name="common_delivery_details")
public class CommonDelivery{

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="common_delivery_id")
    private String id;
    @JsonDeserialize(using = MailroomZonedDateTimeDeserializer.class)
    private ZonedDateTime deliveryDate;
    private String employyeName;
    private String empiId;
    private String proofOfDelivery;
    private String deliveryEmployee;
    private String signature;
    private String remarks;
    private String piecesDelivered;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ZonedDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(ZonedDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getEmployyeName() {
        return employyeName;
    }

    public void setEmployyeName(String employyeName) {
        this.employyeName = employyeName;
    }

    public String getEmpiId() {
        return empiId;
    }

    public void setEmpiId(String empiId) {
        this.empiId = empiId;
    }

    public String getProofOfDelivery() {
        return proofOfDelivery;
    }

    public void setProofOfDelivery(String proofOfDelivery) {
        this.proofOfDelivery = proofOfDelivery;
    }

    public String getDeliveryEmployee() {
        return deliveryEmployee;
    }

    public void setDeliveryEmployee(String deliveryEmployee) {
        this.deliveryEmployee = deliveryEmployee;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPiecesDelivered() {
        return piecesDelivered;
    }

    public void setPiecesDelivered(String piecesDelivered) {
        this.piecesDelivered = piecesDelivered;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommonDelivery that = (CommonDelivery) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(deliveryDate, that.deliveryDate) &&
                Objects.equals(employyeName, that.employyeName) &&
                Objects.equals(empiId, that.empiId) &&
                Objects.equals(proofOfDelivery, that.proofOfDelivery) &&
                Objects.equals(deliveryEmployee, that.deliveryEmployee) &&
                Objects.equals(signature, that.signature) &&
                Objects.equals(remarks, that.remarks) &&
                Objects.equals(piecesDelivered, that.piecesDelivered);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, deliveryDate, employyeName, empiId, proofOfDelivery, deliveryEmployee, signature, remarks, piecesDelivered);
    }

    @Override
    public String toString() {
        return "CommonDelivery{" +
                "id='" + id + '\'' +
                ", deliveryDate=" + deliveryDate +
                ", employyeName='" + employyeName + '\'' +
                ", empiId='" + empiId + '\'' +
                ", proofOfDelivery='" + proofOfDelivery + '\'' +
                ", deliveryEmployee='" + deliveryEmployee + '\'' +
                ", signature='" + signature + '\'' +
                ", remarks='" + remarks + '\'' +
                ", piecesDelivered='" + piecesDelivered + '\'' +
                '}';
    }
}
