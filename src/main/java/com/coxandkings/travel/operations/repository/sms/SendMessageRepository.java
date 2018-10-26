package com.coxandkings.travel.operations.repository.sms;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.sms.OutboundSMS;

public interface SendMessageRepository {

     OutboundSMS saveMessage(OutboundSMS message);
     OutboundSMS getSMSById(String id) throws OperationException;
}
