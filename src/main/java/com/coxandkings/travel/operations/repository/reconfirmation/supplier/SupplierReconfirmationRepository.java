package com.coxandkings.travel.operations.repository.reconfirmation.supplier;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.reconfirmation.supplier.SupplierReconfirmationDetails;

import java.util.List;

public interface SupplierReconfirmationRepository {

    SupplierReconfirmationDetails saveOrUpdateSupplierReconfirmation(SupplierReconfirmationDetails supplierReconfirmation);

    SupplierReconfirmationDetails findByBookRefAndOrderNo(String bookRefNo, String orderNo);

    List<SupplierReconfirmationDetails> getAllSupplierReconfirmation();

    SupplierReconfirmationDetails findBySupplierReconfirmationId(String supplierReconfirmationId) throws OperationException;

    SupplierReconfirmationDetails findByHash(String hash);

    void deleteAllReconfirmation();

    void deleteReconfirmation(String id);
}
