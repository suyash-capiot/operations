package com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability;

import com.coxandkings.travel.operations.model.supplierbillpassing.SupplierBillPassing;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.envers.AuditJoinTable;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "provisionalSupplierLiability")
@Audited
public class ProvisionalSupplierLiability extends BaseServiceOrderDetails {

    @ManyToOne
    @JoinColumn(name = "bill_passing_id")
    @JsonIgnore
    @Audited
    @AuditJoinTable
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
        ProvisionalSupplierLiability that = (ProvisionalSupplierLiability) o;
        return Objects.equals(supplierBillPassing, that.supplierBillPassing);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), supplierBillPassing);
    }
}
