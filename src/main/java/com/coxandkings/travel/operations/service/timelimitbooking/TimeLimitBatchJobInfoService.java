package com.coxandkings.travel.operations.service.timelimitbooking;

import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.timelimitbooking.TLCancellationBatchJobInfo;
import com.coxandkings.travel.operations.model.timelimitbooking.TimeLimitBatchJobInfo;

import java.util.List;

public interface TimeLimitBatchJobInfoService {

    //public void cancelTLIfNotConverted();
    void tlAboutToExpire(OpsBooking aOpsBooking);

    void cancelTLIfNotConverted(OpsBooking opsBooking);

    List<TimeLimitBatchJobInfo> getTLBatchJobInfoByCriteria();

    List<TLCancellationBatchJobInfo> getTLCancellationBatchInfoByCriteria();

    void sendEmailAndAlertWhenCloseToExpiry(String bookId, String orderId, String dueDate);

    void cancellationRequest(String bookId, String orderId, String calculatedBeforeDate);

}
