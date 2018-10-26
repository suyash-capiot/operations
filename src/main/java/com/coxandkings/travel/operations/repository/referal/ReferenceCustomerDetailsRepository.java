package com.coxandkings.travel.operations.repository.referal;

import com.coxandkings.travel.operations.model.referal.ReferenceCustomerDetails;

public interface ReferenceCustomerDetailsRepository {
    ReferenceCustomerDetails saveOrUpdateReference(ReferenceCustomerDetails referenceCustomerDetails);

    ReferenceCustomerDetails getReferenceById(String bookID);
}
