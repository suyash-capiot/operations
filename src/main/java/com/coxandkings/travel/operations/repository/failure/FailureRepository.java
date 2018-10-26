package com.coxandkings.travel.operations.repository.failure;

import com.coxandkings.travel.operations.model.failure.FailureDetails;

import java.util.List;

public interface FailureRepository {

    FailureDetails saveOrUpdate(FailureDetails failureDetails);

    public FailureDetails getById(String id);

    public FailureDetails getByBookID(String bookID);

    public List<FailureDetails> getAll();

    Boolean getExists(String bookID);
}
