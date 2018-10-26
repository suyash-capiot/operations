package com.coxandkings.travel.operations.model.merge;

import com.coxandkings.travel.operations.enums.merge.MergeStatusValues;

import javax.persistence.*;

@Entity
@Table(name = "merge_status")
public class MergeStatus {
    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "code")
    private MergeStatusValues code;

    @Column(name = "merge_status_name")
    private String name;

    @Column(name = "created_by_user_id")
    private String createdByUserId;

    @Column(name = "created_time")
    private Long createdTime;

    @Column(name = "last_modified_by_user_id")
    private String lastModifiedByUserId;

    @Column(name = "last_modified_time")
    private Long lastModifiedTime;

    public MergeStatus() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MergeStatusValues getCode() {
        return code;
    }

    public void setCode(MergeStatusValues code) {
        this.code = code;
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
}
