package com.coxandkings.travel.operations.model.timelimitbooking;

import com.coxandkings.travel.operations.enums.timelimit.BatchJobStatus;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.ZonedDateTime;


@Entity
@Table(name = "tl_cancellation_batch_job_info")
public class TLCancellationBatchJobInfo {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;

    private String bookId;
    private String orderId;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime calculatedBeforeDueDate;

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

    public ZonedDateTime getCalculatedBeforeDueDate() {
        return calculatedBeforeDueDate;
    }

    public void setCalculatedBeforeDueDate(ZonedDateTime calculatedBeforeDueDate) {
        this.calculatedBeforeDueDate = calculatedBeforeDueDate;
    }

    public BatchJobStatus getBatchJobStatus() {
        return batchJobStatus;
    }

    public void setBatchJobStatus(BatchJobStatus batchJobStatus) {
        this.batchJobStatus = batchJobStatus;
    }
}
