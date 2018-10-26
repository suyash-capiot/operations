package com.coxandkings.travel.operations.service.referal.impl;

import com.coxandkings.travel.operations.model.referal.ReferenceCustomerDetails;
import com.coxandkings.travel.operations.repository.referal.ReferenceCustomerDetailsRepository;
import com.coxandkings.travel.operations.service.referal.ReferenceCustomerDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReferenceCustomerDetailsServiceImpl implements ReferenceCustomerDetailsService {

    @Autowired
    private ReferenceCustomerDetailsRepository repository;

    @Override
    public ReferenceCustomerDetails saveOrUpdateReference(ReferenceCustomerDetails referenceCustomerDetails) {
        ReferenceCustomerDetails referenceDetails = null;
        ReferenceCustomerDetails detail = null;
        if (referenceCustomerDetails.getBookID() != null)
            detail = repository.getReferenceById(referenceCustomerDetails.getBookID());
        if (detail == null) {
            referenceDetails = repository.saveOrUpdateReference(referenceCustomerDetails);
        } else {
            referenceCustomerDetails = this.updateReferenceDetails(detail, referenceCustomerDetails);
            referenceDetails = repository.saveOrUpdateReference(referenceCustomerDetails);
        }
        return referenceDetails;
    }

    @Override
    public ReferenceCustomerDetails getReferenceById(String bookID) {
        ReferenceCustomerDetails details = repository.getReferenceById(bookID);
        if ("NEW".equalsIgnoreCase(details.getStatus())) {
            repository.saveOrUpdateReference(details);
        }
        return details;
    }

    @Override
    public ReferenceCustomerDetails updateReferenceDetails(ReferenceCustomerDetails source, ReferenceCustomerDetails dest) {
        if (dest.getClientReferalID() != null)
            source.setClientReferalID(dest.getClientReferalID());
        if (dest.getBookID() != null)
            source.setBookID(dest.getBookID());
        if (dest.getPassengerInformation() != null)
            source.setPassengerInformation(dest.getPassengerInformation());
        if (dest.getPassengerName() != null)
            source.setPassengerName(dest.getPassengerName());
        if (dest.getReferedBy() != null)
            source.setReferedBy(dest.getReferedBy());
        if (dest.getRefereeDetails() != null)
            source.setRefereeDetails(dest.getRefereeDetails());
        if (dest.getTypeOfClient() != null)
            source.setTypeOfClient(dest.getTypeOfClient());

        source.setStatus("UPDATED");
        return source;
    }
}
