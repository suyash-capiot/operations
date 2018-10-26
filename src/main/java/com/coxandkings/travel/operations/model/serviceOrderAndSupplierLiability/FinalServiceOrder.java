package com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability;

import com.coxandkings.travel.operations.model.supplierbillpassing.SupplierBillPassing;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "finalServiceOrder")
@Audited
public class FinalServiceOrder extends BaseServiceOrderDetails {

    @ManyToOne
    @JoinColumn(name = "bill_passing_id")
    @JsonIgnore
    private SupplierBillPassing supplierBillPassing;

    public SupplierBillPassing getSupplierBillPassing() {
        return supplierBillPassing;
    }

    public void setSupplierBillPassing(SupplierBillPassing supplierBillPassing) {
        this.supplierBillPassing = supplierBillPassing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FinalServiceOrder that = (FinalServiceOrder) o;
        return Objects.equals(supplierBillPassing, that.supplierBillPassing);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), supplierBillPassing);
    }
}
