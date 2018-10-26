package com.coxandkings.travel.operations.model.todo;

import com.coxandkings.travel.operations.enums.todo.RecordStatusValues;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "RECORD_STATUS")
public class RecordStatus implements Serializable {
    @Id
    @Column(name = "id")
    @Enumerated(EnumType.STRING)
    private RecordStatusValues id;

    @Column
    private String name;

    @Column(name = "created_by_user_id")
    private String createdByUserId;

    @Column(name = "created_time")
    private Long createdTime;

    @Column(name = "last_modified_by_user_id")
    private String lastModifiedByUserId;

    @Column(name = "last_modified_time")
    private Long lastModifiedTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public String getLastModifiedByUserId() {
        return lastModifiedByUserId;
    }

    public void setLastModifiedByUserId(String lastModifiedByUserId) {
        this.lastModifiedByUserId = lastModifiedByUserId;
    }

    public Long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public RecordStatusValues getId() {
        return id;
    }

    public void setId(RecordStatusValues id) {
        this.id = id;
    }
}
