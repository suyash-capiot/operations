package com.coxandkings.travel.operations.repository.newsupplierfirstbooking;

import com.coxandkings.travel.operations.model.newsupplierfirstbooking.NewSupplierDetails;

public interface NewSupplierDetailsRepository {

    public NewSupplierDetails saveSupplierDetials(NewSupplierDetails newSupplierDetails);

    NewSupplierDetails getByOrderID(String orderID);
}
