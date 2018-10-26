package com.coxandkings.travel.operations.resource.mailroomMaster;

import com.coxandkings.travel.operations.model.mailroomanddispatch.InboundLogEntryStatus;

import java.util.List;
import java.util.Set;

public class CommonDeliveryResource {
    private List<String> inboundNo;
    private Set<InboundLogEntryStatus> inboundLogEntryStatuses;
    /*@JsonDeserialize(using = MailroomZonedDateTimeDeserializer.class)
    @JsonSerialize(using = MailroomZonedDateSerializer.class)
    private ZonedDateTime deliveryDate;
    private String employyeName;
    private String empiId;
    private String proofOfDelivery;
    private String deliveryEmployee;
    private String signature;
    private String remarks;
    private String piecesDelivered;*/

    public List<String> getInboundNo() {
        return inboundNo;
    }

    public void setInboundNo(List<String> inboundNo) {
        this.inboundNo = inboundNo;
    }

    public Set<InboundLogEntryStatus> getInboundLogEntryStatuses() {
        return inboundLogEntryStatuses;
    }

    public void setInboundLogEntryStatuses(Set<InboundLogEntryStatus> inboundLogEntryStatuses) {
        this.inboundLogEntryStatuses = inboundLogEntryStatuses;
    }
}
