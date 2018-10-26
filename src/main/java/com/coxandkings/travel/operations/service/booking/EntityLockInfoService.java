package com.coxandkings.travel.operations.service.booking;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.booking.EntityLockResource;

public interface EntityLockInfoService {

    public String acquireLock(EntityLockResource acquireLock) throws OperationException;

    public String releaseLock(EntityLockResource releaseLock) throws OperationException;
}
