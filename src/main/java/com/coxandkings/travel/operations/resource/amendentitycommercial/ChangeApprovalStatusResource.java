package com.coxandkings.travel.operations.resource.amendentitycommercial;

import com.coxandkings.travel.operations.enums.amendclientcommercials.ApprovalStatus;

public class ChangeApprovalStatusResource {

    private String id;
    private ApprovalStatus approvalStatus;
    private String todoTaskId;
    private String approverRemarks;

    public String getTodoTaskId() {
        return todoTaskId;
    }

    public void setTodoTaskId(String todoTaskId) {
        this.todoTaskId = todoTaskId;
    }

    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApproverRemarks() {
        return approverRemarks;
    }

    public void setApproverRemarks(String approverRemarks) {
        this.approverRemarks = approverRemarks;
    }
}
