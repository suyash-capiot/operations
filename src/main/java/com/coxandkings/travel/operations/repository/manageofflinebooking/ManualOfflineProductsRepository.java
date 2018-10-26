package com.coxandkings.travel.operations.repository.manageofflinebooking;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.manageofflinebooking.manualofflinebooking.OfflineProducts;
import org.json.JSONObject;

import java.util.List;

public interface ManualOfflineProductsRepository {

    OfflineProducts saveCreateRequestDetails(OfflineProducts offlineBooking) throws OperationException;
    List<OfflineProducts> getBooking(String bookingRefId)throws OperationException;
    OfflineProducts updateBooking(JSONObject updateReq)throws OperationException;
    String deleteBooking(String offlineBookId) throws OperationException;
    OfflineProducts findById(String id)throws OperationException;
}
