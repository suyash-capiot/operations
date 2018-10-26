package com.coxandkings.travel.operations.repository.sms;

import com.coxandkings.travel.operations.model.sms.InboundSMS;

public interface ReceiveMessageRepository {

    InboundSMS saveMessage(InboundSMS inboundSMS);
}
