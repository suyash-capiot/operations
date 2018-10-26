package com.coxandkings.travel.operations.service.amendclientcommercial;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.amendentitycommercial.AmendCommercialMetaData;

public interface AmendClientCommercialsMasterDataLoaderService {

    AmendCommercialMetaData getScreenMetaData(String bookingId, String orderId, String uniqueId) throws OperationException;
     
}
