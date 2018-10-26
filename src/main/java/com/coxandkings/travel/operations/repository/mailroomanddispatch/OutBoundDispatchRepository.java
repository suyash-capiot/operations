package com.coxandkings.travel.operations.repository.mailroomanddispatch;

import com.coxandkings.travel.operations.criteria.mailroomanddispatch.ExistingOutboundDispatchCriteria;
import com.coxandkings.travel.operations.criteria.mailroomanddispatch.OutBoundDispatchCriteria;
import com.coxandkings.travel.operations.model.mailroomanddispatch.OutboundDispatch;

import java.util.List;
import java.util.Map;

public interface OutBoundDispatchRepository {

    public OutboundDispatch saveOrUpdate(OutboundDispatch outBoundDispatch);
    public Map<String, Object> getByOutBoundCriteria(OutBoundDispatchCriteria outBoundDispatchCriteria);

    public OutboundDispatch getOutBoundId(String dispatchId);
    public void deleteDispatch(String id);

    public List<OutboundDispatch> getAllOutboundDetails();

    public List<OutboundDispatch> getByExistingOutboundCriteria(ExistingOutboundDispatchCriteria existingOutboundDispatchCriteria);
    public List<String> getPassengarNames(String param);
}
