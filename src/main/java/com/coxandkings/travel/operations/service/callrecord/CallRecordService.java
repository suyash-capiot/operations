package com.coxandkings.travel.operations.service.callrecord;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.callrecord.CallRecord;
import com.coxandkings.travel.operations.model.communication.CommunicationTags;
import com.coxandkings.travel.operations.resource.callrecord.CallRecordResource;
import com.coxandkings.travel.operations.resource.communication.CommunicationTagResource;
import org.springframework.stereotype.Service;

@Service
public interface CallRecordService {
    CallRecord getCallRecordById(String id) throws OperationException;
    CallRecord saveOrUpdateCallRecord(CallRecordResource callRecordsResource) throws OperationException;
    CallRecord updateCallRecord(CallRecordResource callRecordResource) throws OperationException;
    CallRecord markAsRead(String id) throws OperationException;
    CommunicationTags getAssociatedTags(String id);
    CallRecord updateCommunicationTags(String id, CommunicationTagResource communicationTags) throws OperationException;
}
