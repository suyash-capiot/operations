package com.coxandkings.travel.operations.resource.amendmentandpartialcancellation;

import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

@Component
public class AmendAndpartCancResource implements Serializable {
    private static final long serialVersionUID = 1749428056723461391L;

    @Valid
    private List<AmendmentDetailsResource> amendmentDetails;

    private String approvalStatus;
    private String remark;

    public List<AmendmentDetailsResource> getAmendmentDetails() {
        return amendmentDetails;
    }

    public void setAmendmentDetails(List<AmendmentDetailsResource> amendmentDetails) {
        this.amendmentDetails = amendmentDetails;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
