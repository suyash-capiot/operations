package com.coxandkings.travel.operations.service.todo;

import com.coxandkings.travel.operations.exceptions.OperationException;

public interface ToDoBatchJobService  {
    public void reassignUnreadTask() throws OperationException;
}
