package com.coxandkings.travel.operations.repository.newsupplierfirstbooking;

import com.coxandkings.travel.operations.model.newsupplierfirstbooking.NewSupplierCommunication;

public interface NewSupplierCommunicationRepository {
    public NewSupplierCommunication saveOrUpdate(NewSupplierCommunication newSupplierCommunication);
    public boolean isNewSupplierCommunicationExists(String id);
}
