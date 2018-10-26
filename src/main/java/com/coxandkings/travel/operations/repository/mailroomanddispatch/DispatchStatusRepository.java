package com.coxandkings.travel.operations.repository.mailroomanddispatch;

import com.coxandkings.travel.operations.criteria.mailroomanddispatch.DispatchStatusCriteria;
import com.coxandkings.travel.operations.model.mailroomanddispatch.OutboundStatus;

import java.util.List;

public interface DispatchStatusRepository {

    public OutboundStatus saveOrUpdate(OutboundStatus dispatchStatus);
    public OutboundStatus getById(String id);
    public List<OutboundStatus> getByCriteria(DispatchStatusCriteria criteria);

}
