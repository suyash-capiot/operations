package com.coxandkings.travel.operations.service.newsupplierfirstbooking;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.newsupplierfirstbooking.NewSupplierCommunication;
import com.coxandkings.travel.operations.resource.SupplierEmailResource;
import com.coxandkings.travel.operations.resource.email.EmailResponse;

public interface NewSupplierFirstBookingService {
    public void firstReservationChecks(OpsBooking aBooking);

    public EmailResponse sendEmailToSupplier(SupplierEmailResource supplierEmailResource) throws OperationException;

    public NewSupplierCommunication saveOrUpdateCommunication(NewSupplierCommunication newSupplierCommunication);

    boolean getFlag(String orderId);
}
