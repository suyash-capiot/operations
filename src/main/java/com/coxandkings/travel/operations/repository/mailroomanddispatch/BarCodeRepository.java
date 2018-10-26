package com.coxandkings.travel.operations.repository.mailroomanddispatch;

import com.coxandkings.travel.operations.model.mailroomanddispatch.BarCode;

public interface BarCodeRepository {
    public BarCode getBarCodeId(String id);
    public BarCode saveOrUpdate(BarCode barCode);
}
