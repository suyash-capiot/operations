package com.coxandkings.travel.operations.resource.mailroomMaster;

import com.coxandkings.travel.operations.enums.mailroomanddispatch.DispatchStatus;
import com.coxandkings.travel.operations.resource.BaseResource;
import com.coxandkings.travel.operations.utils.mailroomAndDispatch.MailroomZonedDateSerializer;
import com.coxandkings.travel.operations.utils.mailroomAndDispatch.MailroomZonedDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.ZonedDateTime;

public class TrackerResource extends BaseResource {
    @JsonDeserialize(using = MailroomZonedDateTimeDeserializer.class)
    @JsonSerialize(using = MailroomZonedDateSerializer.class)
    private ZonedDateTime dispatchDate;
    private String statusId;
        private DispatchStatus status;

    public ZonedDateTime getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(ZonedDateTime dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public DispatchStatus getStatus() {
        return status;
    }

    public void setStatus(DispatchStatus status) {
        this.status = status;
    }
}
