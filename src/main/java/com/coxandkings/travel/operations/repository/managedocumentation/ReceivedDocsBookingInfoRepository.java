package com.coxandkings.travel.operations.repository.managedocumentation;

import com.coxandkings.travel.operations.criteria.managedocumentation.ReceivedDocsBookingInfoSearchCriteria;
import com.coxandkings.travel.operations.model.managedocumentation.ReceivedDocsBookingInfo;

import java.util.List;

public interface ReceivedDocsBookingInfoRepository {

    public ReceivedDocsBookingInfo saveDetails(ReceivedDocsBookingInfo bookingInfo);

    public ReceivedDocsBookingInfo updateDetails(ReceivedDocsBookingInfo bookingInfo);

    public List<ReceivedDocsBookingInfo> getAllDetails();

    public List<ReceivedDocsBookingInfo> searchDetails(ReceivedDocsBookingInfoSearchCriteria searchCriteria);

    public void deleteDetails(String id);

}
