package com.coxandkings.travel.operations.criteria.mailroomanddispatch;

import com.coxandkings.travel.operations.criteria.BaseCriteria;
import com.coxandkings.travel.operations.enums.mailroomanddispatch.MailRoomStatus;

public class RoomStatusCriteria extends BaseCriteria {

    private MailRoomStatus code;

    public MailRoomStatus getCode() {
        return code;
    }

    public void setCode(MailRoomStatus code) {
        this.code = code;
    }
}
