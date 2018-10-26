package com.coxandkings.travel.operations.service.managedocumentation;

import com.coxandkings.travel.operations.model.managedocumentation.ReceivedDocsBookingInfo;
import com.coxandkings.travel.operations.resource.managedocumentation.DocumentDetailsResponse;

public interface UpdateReceivedDocsBookingInfoDetails {

    public boolean checkReceivedDocsStatus(ReceivedDocsBookingInfo bookingInfo, String documentSettingId, DocumentDetailsResponse documentDetailsResponse);

}
