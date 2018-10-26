package com.coxandkings.travel.operations.model.amendsuppliercommercial;

import com.coxandkings.travel.operations.model.BaseModel;

import javax.persistence.*;

@Entity
@Table(name = "SUP_BOOKING_NOT_ELIGIBLE_FOR")
public class BookingNotEligibleFor  extends BaseModel{
    @Column(name = "booking_not_eligible_for")
    private String notEligibleFor;
    @ManyToOne
    @JoinColumn(name = "amend_supplier_commercial_id")
    private AmendSupplierCommercial amendSupplierCommercial;

    public BookingNotEligibleFor() {

    }

    public BookingNotEligibleFor(String notEligibleFor) {
        this.notEligibleFor = notEligibleFor;
    }

    public String getNotEligibleFor() {
        return notEligibleFor;
    }

    public void setNotEligibleFor(String notEligibleFor) {
        this.notEligibleFor = notEligibleFor;
    }

    public AmendSupplierCommercial getAmendSupplierCommercial() {
        return amendSupplierCommercial;
    }

    public void setAmendSupplierCommercial(AmendSupplierCommercial amendSupplierCommercial) {
        this.amendSupplierCommercial = amendSupplierCommercial;
    }
}
