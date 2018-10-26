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
@Table(name="plan_delivery_details")
public class PlanDelivery {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="plan_delivery_id")
    private String id;
    @JsonDeserialize(using = MailroomZonedDateTimeDeserializer.class)
    @JsonSerialize(using = MailroomZonedDateSerializer.class)
    private ZonedDateTime planDeliveryDate;
    private String planDeliverySlot;
    private String deliveryEmployee;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public ZonedDateTime getPlanDeliveryDate() {
        return planDeliveryDate;
    }

    public void setPlanDeliveryDate(ZonedDateTime planDeliveryDate) {
        this.planDeliveryDate = planDeliveryDate;
    }

    public String getPlanDeliverySlot() {
        return planDeliverySlot;
    }

    public void setPlanDeliverySlot(String planDeliverySlot) {
        this.planDeliverySlot = planDeliverySlot;
    }

    public String getDeliveryEmployee() {
        return deliveryEmployee;
    }

    public void setDeliveryEmployee(String deliveryEmployee) {
        this.deliveryEmployee = deliveryEmployee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlanDelivery delivery = (PlanDelivery) o;
        return Objects.equals(id, delivery.id) &&
                Objects.equals(planDeliveryDate, delivery.planDeliveryDate) &&
                Objects.equals(planDeliverySlot, delivery.planDeliverySlot) &&
                Objects.equals(deliveryEmployee, delivery.deliveryEmployee);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, planDeliveryDate, planDeliverySlot, deliveryEmployee);
    }

    @Override
    public String toString() {
        return "PlanDelivery{" +
                "id='" + id + '\'' +
                ", planDeliveryDate=" + planDeliveryDate +
                ", planDeliverySlot=" + planDeliverySlot +
                ", deliveryEmployee='" + deliveryEmployee + '\'' +
                '}';
    }
}
