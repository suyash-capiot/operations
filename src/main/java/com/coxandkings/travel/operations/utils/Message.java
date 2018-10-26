package com.coxandkings.travel.operations.utils;

import java.util.Arrays;
import java.util.Locale;

public class Message {

    private String errorCode;
    private String[] paramList;
    private Locale locale;

    public Message() {
    }

    public Message(String errorCode, String[] paramList) {
        this.errorCode = errorCode;
        this.paramList = paramList;
    }

    public Message(String errorCode, Locale locale) {
        this.errorCode = errorCode;
        this.locale = locale;
    }

    public Message(String errorCode, String[] paramList, Locale locale) {
        this.errorCode = errorCode;
        this.paramList = paramList;
        this.locale = locale;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String[] getParamList() {
        return paramList;
    }

    public void setParamList(String[] paramList) {
        this.paramList = paramList;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public String toString() {
        return "Message{" +
                "errorCode='" + errorCode + '\'' +
                ", paramList=" + Arrays.toString(paramList) +
                ", locale=" + locale +
                '}';
    }
}
