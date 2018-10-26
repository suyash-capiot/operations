package com.coxandkings.travel.operations.repository.fullcancellation;

import com.coxandkings.travel.operations.model.fullCancellation.SupplierUniqueRef;

public interface SupplierUniqueRefRepository {
    SupplierUniqueRef saveAndUpdate(SupplierUniqueRef supplierUniqueRef);

    SupplierUniqueRef getSupplierUniqueRefById(String id);
}
