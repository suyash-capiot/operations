package com.coxandkings.travel.operations.service.mailroomanddispatch;

import com.coxandkings.travel.operations.criteria.mailroomanddispatch.DispatchStatusCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.mailroomanddispatch.OutboundStatus;
import com.coxandkings.travel.operations.resource.mailroomMaster.DicpatchStatusResource;

import java.util.List;

public interface DispatchStatusServie {
    public OutboundStatus save(DicpatchStatusResource resource) throws OperationException;
    public OutboundStatus getById(String typeId);
    public List<OutboundStatus> getByCriteria(DispatchStatusCriteria criteria);
}
