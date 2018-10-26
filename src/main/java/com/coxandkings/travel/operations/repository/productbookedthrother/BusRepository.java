package com.coxandkings.travel.operations.repository.productbookedthrother;

import com.coxandkings.travel.operations.criteria.productbookedthrother.ProductBookedThrOtherCriteria;
import com.coxandkings.travel.operations.model.productbookedthrother.Bus;

public interface BusRepository {

    public Bus saveOrUpdateBus(Bus bus);

    public  Bus getBusById(String id);

    public Boolean isBusExists(String id);

    public Bus getBusByCriteria(ProductBookedThrOtherCriteria productBookedThrOtherCriteria);

}
