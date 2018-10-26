package com.coxandkings.travel.operations.resource.changesuppliername;

import com.coxandkings.travel.operations.enums.amendsuppliercommercial.SupplierCommercialApproval;

public class ChangeSupplierPriceApprovalResource {
	
    private String todoId;
    private SupplierCommercialApproval approvalStatus;
    private String remark;
    private String operationName;

    public String getTodoId() {
        return todoId;
    }

    public void setTodoId(String todoId) {
        this.todoId = todoId;
    }

    public SupplierCommercialApproval getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(SupplierCommercialApproval approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }
}
