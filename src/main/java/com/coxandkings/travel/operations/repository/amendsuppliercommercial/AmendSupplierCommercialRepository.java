package com.coxandkings.travel.operations.repository.amendsuppliercommercial;

import com.coxandkings.travel.operations.model.amendsuppliercommercial.AmendSupplierCommercial;

public interface AmendSupplierCommercialRepository {
    AmendSupplierCommercial saveAmendSupplierCommercial(AmendSupplierCommercial amendSupplierCommercial);

    AmendSupplierCommercial getAmendSupplierCommercial(String id);
}
