package com.coxandkings.travel.operations.repository.productbookedthrother;

import com.coxandkings.travel.operations.criteria.productbookedthrother.ProductBookedThrOtherCriteria;
import com.coxandkings.travel.operations.model.productbookedthrother.Accomodation;

public interface AccomodationRepository
{

    public Accomodation saveOrUpdateAccomodation(Accomodation accomodation);

    public Boolean isAccomodationExists(String id);

    public Accomodation getAccomodationById(String id);

    public Accomodation getAccomodationByCriteria(ProductBookedThrOtherCriteria productBookedThrOtherCriteria);
}
