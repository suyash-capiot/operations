package com.coxandkings.travel.operations.resource;

import javax.persistence.Transient;

public class MessageResource {

    @Transient
    private String message;
    @Transient
    private String code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}