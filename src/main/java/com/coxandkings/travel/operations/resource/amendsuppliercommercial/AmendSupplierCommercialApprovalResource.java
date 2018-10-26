package com.coxandkings.travel.operations.resource.amendsuppliercommercial;

import com.coxandkings.travel.operations.enums.amendsuppliercommercial.SupplierCommercialApproval;

public class AmendSupplierCommercialApprovalResource {
    private String amendSupplierCommercialId;
    private SupplierCommercialApproval approvalStatus;
    private String todoTaskId;
    private String remark;
    public String getAmendSupplierCommercialId() {
        return amendSupplierCommercialId;
    }

    public void setAmendSupplierCommercialId(String amendSupplierCommercialId) {
        this.amendSupplierCommercialId = amendSupplierCommercialId;
    }

    public SupplierCommercialApproval getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(SupplierCommercialApproval approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getTodoTaskId() {
        return todoTaskId;
    }

    public void setTodoTaskId(String todoTaskId) {
        this.todoTaskId = todoTaskId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
