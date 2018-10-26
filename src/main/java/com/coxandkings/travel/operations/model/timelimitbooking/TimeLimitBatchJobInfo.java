package com.coxandkings.travel.operations.model.timelimitbooking;

import com.coxandkings.travel.operations.enums.timelimit.BatchJobStatus;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Table(name = "batch_job_info_for_timelimit")
public class TimeLimitBatchJobInfo implements Serializable{

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;


    private String bookId;

    private String orderId;

    //ToDo Keep temporary after fixing the DateTimeConverter Remove
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime calculatedExpiryDueDate;

    private BatchJobStatus batchJobStatus;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public ZonedDateTime getCalculatedExpiryDueDate() {
        return calculatedExpiryDueDate;
    }

    public void setCalculatedExpiryDueDate(ZonedDateTime calculatedExpiryDueDate) {
        this.calculatedExpiryDueDate = calculatedExpiryDueDate;
    }

    public BatchJobStatus getBatchJobStatus() {
        return batchJobStatus;
    }

    public void setBatchJobStatus(BatchJobStatus batchJobStatus) {
        this.batchJobStatus = batchJobStatus;
    }
}
