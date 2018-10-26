package com.coxandkings.travel.operations.model.mailroomanddispatch;

import com.coxandkings.travel.operations.utils.mailroomAndDispatch.MailroomZonedDateSerializer;
import com.coxandkings.travel.operations.utils.mailroomAndDispatch.MailroomZonedDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name="plan_collection_details")
public class PlanCollection {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="plan_collection_id")
    private String id;
    @JsonDeserialize(using = MailroomZonedDateTimeDeserializer.class)
    @JsonSerialize(using = MailroomZonedDateSerializer.class)
    private ZonedDateTime planCollectionDate;
    private String planCollectionSlot;
    private String collectionEmployeeName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ZonedDateTime getPlanCollectionDate() {
        return planCollectionDate;
    }

    public void setPlanCollectionDate(ZonedDateTime planCollectionDate) {
        this.planCollectionDate = planCollectionDate;
    }

    public String getPlanCollectionSlot() {
        return planCollectionSlot;
    }

    public void setPlanCollectionSlot(String planCollectionSlot) {
        this.planCollectionSlot = planCollectionSlot;
    }

    public String getCollectionEmployeeName() {
        return collectionEmployeeName;
    }

    public void setCollectionEmployeeName(String collectionEmployeeName) {
        this.collectionEmployeeName = collectionEmployeeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlanCollection that = (PlanCollection) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(planCollectionDate, that.planCollectionDate) &&
                Objects.equals(planCollectionSlot, that.planCollectionSlot) &&
                Objects.equals(collectionEmployeeName, that.collectionEmployeeName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, planCollectionDate, planCollectionSlot, collectionEmployeeName);
    }

    @Override
    public String toString() {
        return "PlanCollection{" +
                "id='" + id + '\'' +
                ", planCollectionDate=" + planCollectionDate +
                ", planCollectionSlot='" + planCollectionSlot + '\'' +
                ", collectionEmployeeName='" + collectionEmployeeName + '\'' +
                '}';
    }
}
