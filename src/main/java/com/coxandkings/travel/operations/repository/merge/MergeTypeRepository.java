package com.coxandkings.travel.operations.repository.merge;

import com.coxandkings.travel.operations.enums.merge.MergeTypeValues;
import com.coxandkings.travel.operations.model.merge.MergeType;
import org.springframework.stereotype.Repository;

@Repository
public interface MergeTypeRepository {
    MergeType getById(String id);

    MergeType getByCode(MergeTypeValues code);
}
