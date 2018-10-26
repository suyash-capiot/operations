package com.coxandkings.travel.operations.repository.changesuppliername;

import com.coxandkings.travel.operations.model.changesuppliername.DiscountOnSupplierPrice;

public interface DiscountOnSupplierPriceRepository {
    DiscountOnSupplierPrice saveDiscountOnSupplierPrice(DiscountOnSupplierPrice discountOnSupplierPrice);

    DiscountOnSupplierPrice getDiscountOnSupplierPriceById(String referenceId);


}
