package com.coxandkings.travel.operations.service.amendsuppliercommercial;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.amendsuppliercommercial.AmendSupplierCommercialMetaDataResource;

public interface AmendSupplierCommercialMasterDataLoaderService {
    AmendSupplierCommercialMetaDataResource getScreenMetaData(String bookingId, String orderId, String roomId, String paxType) throws OperationException;

}