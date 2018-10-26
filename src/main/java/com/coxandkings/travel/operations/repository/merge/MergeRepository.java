package com.coxandkings.travel.operations.repository.merge;

import com.coxandkings.travel.operations.model.merge.Merge;

import java.util.List;

public interface MergeRepository {

    Merge getById(String id);

    Merge saveOrUpdate(Merge merge);

    List<Merge> getPotentialAccommodationMerge(Merge currentMerge);

    List<Merge> getPotentialMerges();
}
