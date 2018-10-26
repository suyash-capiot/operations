package com.coxandkings.travel.operations.service.reconfirmation.supplier;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.reconfirmation.supplier.SupplierReconfirmationDetails;

import java.time.ZonedDateTime;

public interface SupplierReconfirmationUtilityService {
    /**
     *
     * @param bookingDetails
     * @param hotels
     * @param supplierReconDetails
     * @param supplierReconfirmationDate
     * @return
     * @throws OperationException
     */
    SupplierReconfirmationDetails emailProcessForSupplier( OpsBooking bookingDetails , OpsProduct hotels , SupplierReconfirmationDetails supplierReconDetails , ZonedDateTime supplierReconfirmationDate ) throws OperationException;
}
