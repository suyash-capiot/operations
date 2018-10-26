package com.coxandkings.travel.operations.repository.forex;

import com.coxandkings.travel.operations.model.forex.PassportDetails;

public interface PassportDetailsRepository {

    PassportDetails saveOrUpdate(PassportDetails passportDetails);

    PassportDetails getById(String id);
}
