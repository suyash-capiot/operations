package com.coxandkings.travel.operations.service.amendcompanycommercial;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.amendentitycommercial.AmendCommercialMetaData;

public interface AmendCompanyCommercialMasterDataLoader {

    AmendCommercialMetaData getScreenMetaData(String bookingId, String orderId, String uniqueId) throws OperationException;
}
