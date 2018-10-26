package com.coxandkings.travel.operations.exceptions;

import java.util.Map;

public class OperationException extends Exception {
    private String errorCode;
    private String[] params;
    private Map errors;

    public OperationException() {
        super();
    }

    public OperationException(String errorCode) {
        this();
        this.errorCode = errorCode;
    }

    public OperationException(String errorCode, String... params) {
        this();
        this.errorCode = errorCode;
        this.params = params;
    }

    public OperationException(Map errors){
        this.errors=errors;
    }

    public Map getErrors() {
        return errors;
    }

    public void setErrors(Map errors) {
        this.errors = errors;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String... params) {
        this.params = params;
    }
}
