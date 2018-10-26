package com.coxandkings.travel.operations.criteria.thirdPartyVoucher;

import com.coxandkings.travel.operations.criteria.workflow.GenericCriteria;

public class VoucherCodeSearchCriteria extends GenericCriteria {

    private SupplierConfigSearchCriteria filter;

    public SupplierConfigSearchCriteria getFilter() {
        return filter;
    }

    public void setFilter(SupplierConfigSearchCriteria filter) {
        this.filter = filter;
    }
}
