package com.coxandkings.travel.operations.service.sms;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.communication.CommunicationTags;
import com.coxandkings.travel.operations.model.sms.InboundSMS;
import com.coxandkings.travel.operations.model.sms.OutboundSMS;
import com.coxandkings.travel.operations.resource.communication.CommunicationTagResource;
import com.coxandkings.travel.operations.resource.sms.OutboundSMSResource;

import javax.servlet.http.HttpServletRequest;

public interface SMSService {

    OutboundSMS sendMsg(OutboundSMSResource outboundSMSResource)throws OperationException;
    OutboundSMS getSMSById(String id) throws OperationException;
    OutboundSMS markAsRead(String id) throws  OperationException;
    InboundSMS receiveMessage(HttpServletRequest request);
    CommunicationTags getAssociatedTags(String id) throws OperationException;
    OutboundSMS updateCommunicationTags(String id, CommunicationTagResource communicationTagResource) throws OperationException;
}
