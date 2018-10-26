package com.coxandkings.travel.operations.repository.mailroomanddispatch;

import com.coxandkings.travel.operations.model.mailroomanddispatch.PlanDelivery;

public interface PlanDeliveryRepository {
    public PlanDelivery saveOrUpdate(PlanDelivery createParcel);
}
