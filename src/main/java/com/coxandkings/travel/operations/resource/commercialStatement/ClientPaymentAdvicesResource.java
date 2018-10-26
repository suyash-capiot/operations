package com.coxandkings.travel.operations.resource.commercialStatement;

import java.util.List;

public class ClientPaymentAdvicesResource {
    List<String> attachedCommercialStatementIds;

    public List<String> getAttachedCommercialStatementIds() {
        return attachedCommercialStatementIds;
    }

    public void setAttachedCommercialStatementIds(List<String> attachedCommercialStatementIds) {
        this.attachedCommercialStatementIds = attachedCommercialStatementIds;
    }
}
