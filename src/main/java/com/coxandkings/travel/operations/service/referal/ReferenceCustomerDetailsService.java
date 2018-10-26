package com.coxandkings.travel.operations.service.referal;

import com.coxandkings.travel.operations.model.referal.ReferenceCustomerDetails;

public interface ReferenceCustomerDetailsService {
    ReferenceCustomerDetails saveOrUpdateReference(ReferenceCustomerDetails referenceCustomerDetails);

    ReferenceCustomerDetails getReferenceById(String bookID);

    ReferenceCustomerDetails updateReferenceDetails(ReferenceCustomerDetails source, ReferenceCustomerDetails dest);
}
