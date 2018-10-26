package com.coxandkings.travel.operations.repository.callrecord;

import com.coxandkings.travel.operations.criteria.communication.CommunicationCriteria;
import com.coxandkings.travel.operations.model.callrecord.CallRecord;
import com.coxandkings.travel.operations.model.communication.BaseCommunication;

import java.util.List;

public interface CallRecordRepository {
    CallRecord saveOrUpdateCallRecords(CallRecord callRecord);

    CallRecord getCallRecordsById(String id);


    List<? extends BaseCommunication> getByCriteria(CommunicationCriteria criteria);
}
