package com.coxandkings.travel.operations.model.amendsuppliercommercial;

import com.coxandkings.travel.operations.model.BaseModel;

import javax.persistence.*;

@Table(name = "APPLY_SUPPLIER_COMMERCIAL_ON")
@Entity
public class ApplySupplierCommercialOn  extends BaseModel {
    @Column(name = "apply_on")
    private String applyOn;
    @ManyToOne
    @JoinColumn(name = "amend_supplier_commercial_id")
    private AmendSupplierCommercial amendSupplierCommercial;

    public ApplySupplierCommercialOn() {
    }

    public ApplySupplierCommercialOn(String applyOn) {
        this.applyOn = applyOn;
    }

    public String getApplyOn() {
        return applyOn;
    }

    public void setApplyOn(String applyOn) {
        this.applyOn = applyOn;
    }

    public AmendSupplierCommercial getAmendSupplierCommercial() {
        return amendSupplierCommercial;
    }

    public void setAmendSupplierCommercial(AmendSupplierCommercial amendSupplierCommercial) {
        this.amendSupplierCommercial = amendSupplierCommercial;
    }
}
