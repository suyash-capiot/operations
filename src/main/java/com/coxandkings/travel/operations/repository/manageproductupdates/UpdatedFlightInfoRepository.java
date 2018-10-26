package com.coxandkings.travel.operations.repository.manageproductupdates;

import com.coxandkings.travel.operations.model.manageproductupdates.UpdatedFlightInfo;

import java.util.Map;

public interface UpdatedFlightInfoRepository {
    UpdatedFlightInfo saveUpdatedFlightInfo(UpdatedFlightInfo updatedFlightInfo);

    public Map<String, Object> getUpdatedFlightInfo(Integer size, Integer page);

    UpdatedFlightInfo getUpdatedFlightInfoByCriteria(String bookId, String orderId);

    UpdatedFlightInfo getById(String id);
}
