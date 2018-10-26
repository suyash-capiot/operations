package com.coxandkings.travel.operations.service.mailroomanddispatch;

import com.coxandkings.travel.operations.model.mailroomanddispatch.MailRoomMaster;

@FunctionalInterface
public interface MailRoomStatusService {

    public MailRoomMaster mailRoomMasterChangeStatusActive(MailRoomMaster mailRoomMaster);
}
