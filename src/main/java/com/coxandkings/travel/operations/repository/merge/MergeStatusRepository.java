package com.coxandkings.travel.operations.repository.merge;

import com.coxandkings.travel.operations.enums.merge.MergeStatusValues;
import com.coxandkings.travel.operations.model.merge.MergeStatus;

public interface MergeStatusRepository {
    MergeStatus getStatus(MergeStatusValues code);
}
