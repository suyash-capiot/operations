package com.coxandkings.travel.operations.repository.productbookedthrother;

import com.coxandkings.travel.operations.criteria.productbookedthrother.ProductBookedThrOtherCriteria;
import com.coxandkings.travel.operations.model.productbookedthrother.Train;

public interface TrainRepository {


    public Train saveOrUpdateTrain(Train train);

    public Boolean isTrainExists(String id);

    public Train getTrainById(String id);

    public Train getTrainByCriteria(ProductBookedThrOtherCriteria productBookedThrOtherCriteria);
}
