package com.coxandkings.travel.operations.criteria.changesuppliername;

public class SupplementOnSupplierPriceCriteria {
    private String id;
    private boolean opsUserApprovalStatus;
    private boolean clientApprovalStatus;
    private String identifier;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isOpsUserApprovalStatus() {
        return opsUserApprovalStatus;
    }

    public void setOpsUserApprovalStatus(boolean opsUserApprovalStatus) {
        this.opsUserApprovalStatus = opsUserApprovalStatus;
    }

    public boolean isClientApprovalStatus() {
        return clientApprovalStatus;
    }

    public void setClientApprovalStatus(boolean clientApprovalStatus) {
        this.clientApprovalStatus = clientApprovalStatus;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
