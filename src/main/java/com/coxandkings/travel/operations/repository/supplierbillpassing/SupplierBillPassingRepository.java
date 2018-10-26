package com.coxandkings.travel.operations.repository.supplierbillpassing;

import com.coxandkings.travel.operations.model.supplierbillpassing.SupplierBillPassing;

import java.util.List;

public interface SupplierBillPassingRepository {
    public SupplierBillPassing add(SupplierBillPassing supplierBillPassing);
    public SupplierBillPassing update(SupplierBillPassing supplierBillPassing);
    public SupplierBillPassing get(String id);
    public void remove(String id);
    public List<SupplierBillPassing> getAll();

}
