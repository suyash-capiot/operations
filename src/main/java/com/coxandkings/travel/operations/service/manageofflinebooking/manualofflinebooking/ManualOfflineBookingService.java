package com.coxandkings.travel.operations.service.manageofflinebooking.manualofflinebooking;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.ErrorResponseResource;
import org.json.JSONObject;

import java.util.Map;

public interface ManualOfflineBookingService {

    JSONObject fetchDataFromMDM(JSONObject criteria) throws OperationException;
    JSONObject createManualOfflineBooking(JSONObject createBookingJSON) throws OperationException;
    JSONObject saveManualOfflineBooking(JSONObject createBookingJSON) throws OperationException;
    JSONObject getBooking(String bookingRefId) throws OperationException;
    Map<String, Object> searchOfflineBookings(JSONObject criteriaJson) throws OperationException;
    ErrorResponseResource updateOfflineBookings(JSONObject updateReq) throws OperationException;
    String deleteBooking(String offlineBookId) throws OperationException;
    JSONObject findById(String offlineBookId)throws OperationException;
    JSONObject createTODO(JSONObject reqJson) throws OperationException;
    JSONObject getClientCcy(JSONObject req) throws OperationException;
}
