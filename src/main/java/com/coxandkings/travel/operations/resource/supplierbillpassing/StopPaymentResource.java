package com.coxandkings.travel.operations.resource.supplierbillpassing;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.utils.supplierBillPassing.DateConverter;
import org.springframework.util.StringUtils;

import java.time.ZonedDateTime;
import java.util.Set;

public class StopPaymentResource {

    private String id;

    private String stopPaymentTill;

    private String dateStr;

    private ZonedDateTime date;

    private String reason;

    private Set<String> provisionalServiceOrderIds;

    private String status;

    public String getStopPaymentTill() { return stopPaymentTill; }

    public void setStopPaymentTill(String stopPaymentTill) { this.stopPaymentTill = stopPaymentTill; }

    public void setDateStr(String dateStr) throws OperationException {
        this.dateStr = dateStr;
        if (!StringUtils.isEmpty(dateStr))this.date= DateConverter.stringToZonedDateTime(dateStr);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateStr() {
        if (date!=null) dateStr= DateConverter.zonedDateTimeToString(date);
        return this.dateStr;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Set<String> getProvisionalServiceOrderIds() {
        return provisionalServiceOrderIds;
    }

    public void setProvisionalServiceOrderIds(Set<String> provisionalServiceOrderIds) {
        this.provisionalServiceOrderIds = provisionalServiceOrderIds;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
