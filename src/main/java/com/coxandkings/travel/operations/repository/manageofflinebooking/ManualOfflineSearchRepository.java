package com.coxandkings.travel.operations.repository.manageofflinebooking;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.manageofflinebooking.manualofflinebooking.OfflineSearch;
import org.json.JSONObject;

import java.util.Map;

public interface ManualOfflineSearchRepository {

    void saveCreateRequestDetails(OfflineSearch offlineBooking) throws OperationException;
    public Map<String, Object> searchOfflineBookings(JSONObject criteriaJson) throws OperationException;
    public OfflineSearch getByProductId(String productId) throws OperationException;
}
