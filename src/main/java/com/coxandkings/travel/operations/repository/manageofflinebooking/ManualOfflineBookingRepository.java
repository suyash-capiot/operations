package com.coxandkings.travel.operations.repository.manageofflinebooking;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.manageofflinebooking.manualofflinebooking.OfflineBooking;

import java.util.List;

public interface ManualOfflineBookingRepository {
    void saveCreateRequestDetails(OfflineBooking offlineBooking) throws OperationException;
    public List<OfflineBooking> getBooking(String bookingRefId)throws OperationException;
}
