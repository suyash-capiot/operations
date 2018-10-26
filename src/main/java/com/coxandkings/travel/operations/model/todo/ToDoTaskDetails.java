package com.coxandkings.travel.operations.model.todo;

import com.coxandkings.travel.operations.enums.todo.RecordStatusValues;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateDeserializer;
import com.coxandkings.travel.operations.helper.mockBE.LocalDateSerializer;
import com.coxandkings.travel.operations.model.BaseModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.sql.Clob;
import java.time.ZonedDateTime;

@Entity
@Table
public class ToDoTaskDetails extends BaseModel {

    @Enumerated(EnumType.STRING)
    private RecordStatusValues recordStatus;

    @Column
    private String lockedBy;

    @Column
    private boolean read;

    @Column
    private Long readTime;

    @Column
    private Clob updatedRecord;

    @Column
    private String lastModifiedByUserIdUI;

    @Column
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private ZonedDateTime lastModifiedTimeUI;

    public RecordStatusValues getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(RecordStatusValues recordStatus) {
        this.recordStatus = recordStatus;
    }

    public String getLockedBy() {
        return lockedBy;
    }

    public void setLockedBy(String lockedBy) {
        this.lockedBy = lockedBy;
    }

    public boolean getRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public Long getReadTime() {
        return readTime;
    }

    public void setReadTime(Long readTime) {
        this.readTime = readTime;
    }

    public Clob getUpdatedRecord() {
        return updatedRecord;
    }

    public void setUpdatedRecord(Clob updatedRecord) {
        this.updatedRecord = updatedRecord;
    }

    public String getLastModifiedByUserIdUI() {
        return lastModifiedByUserIdUI;
    }

    public void setLastModifiedByUserIdUI(String lastModifiedByUserIdUI) {
        this.lastModifiedByUserIdUI = lastModifiedByUserIdUI;
    }

    public ZonedDateTime getLastModifiedTimeUI() {
        return lastModifiedTimeUI;
    }

    public void setLastModifiedTimeUI(ZonedDateTime lastModifiedTimeUI) {
        this.lastModifiedTimeUI = lastModifiedTimeUI;
    }
}
