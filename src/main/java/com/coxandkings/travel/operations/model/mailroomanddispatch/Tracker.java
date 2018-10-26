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
@Table(name="dispatch_track")
public class Tracker {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="dispatch_track_id")
    private String id;
    @JsonDeserialize(using = MailroomZonedDateTimeDeserializer.class)
    @JsonSerialize(using = MailroomZonedDateSerializer.class)
    private ZonedDateTime dispatchDate;
    @Enumerated
    private DispatchStatus status;
    /*@ManyToOne
    private OutboundStatus outboundStatus;*/

    @ManyToOne(cascade = CascadeType.ALL)
    private OutboundDispatch dispatch;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ZonedDateTime getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(ZonedDateTime dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    /*public OutboundStatus getOutboundStatus() {
        return outboundStatus;
    }

    public void setOutboundStatus(OutboundStatus outboundStatus) {
        this.outboundStatus = outboundStatus;
    }*/

    public DispatchStatus getStatus() {
        return status;
    }

    public void setStatus(DispatchStatus status) {
        this.status = status;
    }

    public OutboundDispatch getDispatch() {
        return dispatch;
    }

    public void setDispatch(OutboundDispatch dispatch) {
        this.dispatch = dispatch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tracker tracker = (Tracker) o;
        return Objects.equals(id, tracker.id) &&
                Objects.equals(dispatchDate, tracker.dispatchDate) &&
                //Objects.equals(outboundStatus, tracker.outboundStatus) &&
                Objects.equals(dispatch, tracker.dispatch);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, dispatchDate,dispatch);
    }

    @Override
    public String toString() {
        return "Tracker{" +
                "id='" + id + '\'' +
                ", dispatchDate=" + dispatchDate +
                //", outboundStatus=" + outboundStatus +
                ", dispatch=" + dispatch +
                '}';
    }
}
