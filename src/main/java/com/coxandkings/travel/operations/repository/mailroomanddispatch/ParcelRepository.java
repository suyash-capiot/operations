package com.coxandkings.travel.operations.repository.mailroomanddispatch;

import com.coxandkings.travel.operations.criteria.mailroomanddispatch.SearchParcelCriteria;
import com.coxandkings.travel.operations.model.mailroomanddispatch.Parcel;

import java.util.List;

public interface ParcelRepository {

    public Parcel saveOrUpdate(Parcel createParcel);
    public Parcel getParcelId(String parcelId);
    public List<Parcel> getParcels();
    public Parcel searchByCriteria(SearchParcelCriteria searchParcel);
}
