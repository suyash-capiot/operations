package com.coxandkings.travel.operations.model.alert;

import com.coxandkings.travel.operations.model.BaseModel;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table
public class Notification extends BaseModel {

    @NotNull
    private String userId;
    private String message;
    private boolean isRead;
    private String deeplinkurl;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getDeeplinkurl() {
        return deeplinkurl;
    }

    public void setDeeplinkurl(String deeplinkurl) {
        this.deeplinkurl = deeplinkurl;
    }
}
