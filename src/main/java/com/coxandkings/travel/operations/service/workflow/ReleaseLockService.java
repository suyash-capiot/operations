package com.coxandkings.travel.operations.service.workflow;

import com.coxandkings.travel.operations.exceptions.OperationException;

public interface ReleaseLockService {

    boolean isResponsibleFor(String type)throws OperationException;
}
