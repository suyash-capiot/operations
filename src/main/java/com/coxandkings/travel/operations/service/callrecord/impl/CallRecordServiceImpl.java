package com.coxandkings.travel.operations.service.callrecord.impl;

import com.coxandkings.travel.operations.enums.communication.CommunicationType;
import com.coxandkings.travel.operations.enums.communication.Direction;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.callrecord.CallRecord;
import com.coxandkings.travel.operations.model.communication.CommunicationTags;
import com.coxandkings.travel.operations.repository.callrecord.CallRecordRepository;
import com.coxandkings.travel.operations.resource.callrecord.CallRecordResource;
import com.coxandkings.travel.operations.resource.communication.CommunicationTagResource;
import com.coxandkings.travel.operations.service.callrecord.CallRecordService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.CopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;

@Service
public class CallRecordServiceImpl implements CallRecordService {
    @Autowired
    CallRecordRepository callRecordRepository;

    @Override
    public CallRecord getCallRecordById(String id) throws OperationException {
        CallRecord callRecord = callRecordRepository.getCallRecordsById(id);
        if (callRecord == null) {
            throw new OperationException(Constants.ER49);
        }
        return callRecord;
    }

    @Override
    public CallRecord saveOrUpdateCallRecord(CallRecordResource callRecordResource) throws OperationException {
        CallRecord callRecord = null;
        if (callRecordResource != null) {
            callRecord = new CallRecord();

            callRecord.setBookId(callRecordResource.getBookId());
            callRecord.setUserId(callRecordResource.getUserId());
            callRecord.setSender(callRecordResource.getCallFrom());
            callRecord.setRecipientList(Collections.singletonList(callRecordResource.getCallTo()));
            callRecord.setBody(callRecordResource.getDescription());
            callRecord.setCommunicationType(String.valueOf(CommunicationType.CALLRECORD));
            callRecord.setSubject(callRecordResource.getSubject());
            callRecord.setProcess(callRecordResource.getProcess());
            callRecord.setScenario(callRecordResource.getScenario());
            callRecord.setFunction(callRecordResource.getFunction());
            callRecord.setProductSubCategory(callRecordResource.getProductSubCategory());
            callRecord.setSupplier(callRecordResource.getSupplier());
            callRecord.setPhoneNumber(callRecordResource.getPhoneNumber());
            callRecord.setDuration(callRecordResource.getDuration());
            if (callRecordResource.getDirection().equalsIgnoreCase(String.valueOf(Direction.INCOMING))) {
                callRecord.setIs_outbound(false);
            } else if (callRecordResource.getDirection().equalsIgnoreCase(String.valueOf(Direction.OUTGOING))) {
                callRecord.setIs_outbound(true);
            } else {
                throw new OperationException(Constants.ER48);
            }

            if (callRecordResource.getCommunicationTagResource() != null) {
                CommunicationTags communicationTags = new CommunicationTags();
                communicationTags.setBaseCommunication(callRecord);
                CopyUtils.copy(callRecordResource.getCommunicationTagResource(), communicationTags);
                callRecord.setCommunicationTags(communicationTags);
            }

            callRecord.setMediaId(callRecordResource.getMediaId());
            callRecord = callRecordRepository.saveOrUpdateCallRecords(callRecord);
        }

        return callRecord;
    }

    @Override
    public CallRecord updateCallRecord(CallRecordResource callRecordResource) throws OperationException {
        CallRecord callRecord = null;
        if (callRecordResource != null && callRecordResource.getId() != null) {

            callRecord = callRecordRepository.getCallRecordsById(callRecordResource.getId());
            if (callRecord == null) {
                throw new OperationException(Constants.ER49);
            }
            callRecord = new CallRecord();

            callRecord.setBookId(callRecordResource.getBookId());
            callRecord.setUserId(callRecordResource.getUserId());
            callRecord.setSender(callRecordResource.getCallFrom());
            callRecord.setRecipientList(Collections.singletonList(callRecordResource.getCallTo()));
            callRecord.setBody(callRecordResource.getDescription());
            callRecord.setCommunicationType(String.valueOf(CommunicationType.CALLRECORD));
            callRecord.setSubject(callRecordResource.getSubject());
            callRecord.setProcess(callRecordResource.getProcess());
            callRecord.setScenario(callRecordResource.getScenario());
            callRecord.setFunction(callRecordResource.getFunction());
            callRecord.setProductSubCategory(callRecordResource.getProductSubCategory());
            callRecord.setSupplier(callRecordResource.getSupplier());
            callRecord.setPhoneNumber(callRecordResource.getPhoneNumber());

            String[] values = callRecordResource.getDuration().split(":");
            Duration duration = Duration.ofHours(Integer.parseInt(values[0]));
            duration = duration.plusMinutes(Integer.parseInt(values[1]));
            duration = duration.plusSeconds(Integer.parseInt(values[2]));
            callRecord.setDuration(callRecordResource.getDuration());
            if (callRecordResource.getDirection().equalsIgnoreCase(String.valueOf(Direction.INCOMING))) {
                callRecord.setIs_outbound(false);
            }
            if (callRecordResource.getDirection().equalsIgnoreCase(String.valueOf(Direction.OUTGOING))) {
                callRecord.setIs_outbound(true);
            } else {
                throw new OperationException(Constants.ER48);
            }
            callRecord.setMediaId(callRecordResource.getMediaId());
            callRecord = callRecordRepository.saveOrUpdateCallRecords(callRecord);
        } else {
            throw new OperationException("Invalid input");
        }
        return callRecord;
    }

    @Override
    public CallRecord markAsRead(String id) throws OperationException {
        CallRecord callRecord = callRecordRepository.getCallRecordsById(id);
        callRecord.setRead(true);
        return this.callRecordRepository.saveOrUpdateCallRecords(callRecord);
    }

    @Override
    public CommunicationTags getAssociatedTags(String id) {
        CallRecord callRecord = callRecordRepository.getCallRecordsById(id);
        return callRecord.getCommunicationTags();
    }

    @Override
    public CallRecord updateCommunicationTags(String id, CommunicationTagResource communicationTagResource) throws OperationException {
        CallRecord callRecord = getCallRecordById(id);
        CommunicationTags communicationTags = new CommunicationTags();
        CopyUtils.copy(communicationTagResource, communicationTags);
        callRecord.setCommunicationTags(communicationTags);
        return callRecordRepository.saveOrUpdateCallRecords(callRecord);
    }
}
