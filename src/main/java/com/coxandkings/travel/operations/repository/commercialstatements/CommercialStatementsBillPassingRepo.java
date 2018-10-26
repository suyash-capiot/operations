package com.coxandkings.travel.operations.repository.commercialstatements;

import com.coxandkings.travel.operations.model.commercialstatements.CommercialStatementsBillPassing;

public interface CommercialStatementsBillPassingRepo {
    public CommercialStatementsBillPassing add(CommercialStatementsBillPassing commercialStatementsBillPassing);
    public CommercialStatementsBillPassing update(CommercialStatementsBillPassing commercialStatementsBillPassing);
    public CommercialStatementsBillPassing get(String id);
}
