package com.coxandkings.travel.operations.resource.mailroomMaster;

import com.coxandkings.travel.operations.enums.mailroomanddispatch.MailRoomStatus;
import com.coxandkings.travel.operations.resource.BaseResource;

public class RoomStatusResource extends BaseResource {

    private String name;
    private MailRoomStatus code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MailRoomStatus getCode() {
        return code;
    }

    public void setCode(MailRoomStatus code) {
        this.code = code;
    }
}
