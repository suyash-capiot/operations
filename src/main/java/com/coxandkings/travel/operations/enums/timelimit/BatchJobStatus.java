package com.coxandkings.travel.operations.enums.timelimit;

public enum  BatchJobStatus {

    NOT_PROCESSED("not_processed"),
    COMPLETED("completed"),
    ALERT_SENT_TO_CUSTOMER("alert_send_to_customer"),
    ALERT_SENT_TO_USER("alert_send_to_user");

    private String status;

    BatchJobStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BatchJobStatus fromString(String newAttribute )  {
        BatchJobStatus aJobStatus = null;
        if( newAttribute == null || newAttribute.isEmpty() )  {
            return aJobStatus;
        }

        for( BatchJobStatus tmpBatchJobStatus : BatchJobStatus.values() )    {
            if( tmpBatchJobStatus.getStatus().equalsIgnoreCase( newAttribute ))  {
                aJobStatus = tmpBatchJobStatus;
                break;
            }
        }
        return aJobStatus;
    }
}
