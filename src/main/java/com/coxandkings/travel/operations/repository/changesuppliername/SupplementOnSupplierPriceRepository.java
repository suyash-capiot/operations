package com.coxandkings.travel.operations.repository.changesuppliername;

import com.coxandkings.travel.operations.criteria.changesuppliername.SupplementOnSupplierPriceCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.changesuppliername.SupplementOnSupplierPrice;

public interface SupplementOnSupplierPriceRepository {

    SupplementOnSupplierPrice saveSupplementOnSupplierPrice(SupplementOnSupplierPrice supplementOnSupplierPrice);

    SupplementOnSupplierPrice getSupplementOnSupplierPriceById(String id);

    SupplementOnSupplierPrice getByCriteria(SupplementOnSupplierPriceCriteria supplementOnSupplierPriceCriteria) throws OperationException;
}
