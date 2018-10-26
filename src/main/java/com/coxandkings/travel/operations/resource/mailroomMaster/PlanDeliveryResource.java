package com.coxandkings.travel.operations.resource.mailroomMaster;


import com.coxandkings.travel.operations.utils.mailroomAndDispatch.MailroomZonedDateSerializer;
import com.coxandkings.travel.operations.utils.mailroomAndDispatch.MailroomZonedDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.ZonedDateTime;

public class PlanDeliveryResource {

    @JsonDeserialize(using = MailroomZonedDateTimeDeserializer.class)
    @JsonSerialize(using = MailroomZonedDateSerializer.class)
    private ZonedDateTime planDeliveryDate;
    private String planDeliverySlot;
    private String deliveryEmployee;

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
}
