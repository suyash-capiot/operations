package com.coxandkings.travel.operations.resource;

import org.springframework.http.HttpStatus;

public class ErrorResponseResource {

    private HttpStatus httpStatus;
    private String message;
    private String code;
    /*private String debugMessage; Not Required for now */

    public ErrorResponseResource() {

    }

    public ErrorResponseResource(HttpStatus httpStatus) {
        this();
        this.httpStatus = httpStatus;
    }

    public ErrorResponseResource(HttpStatus httpStatus, String message, String code) {
        this();
        this.httpStatus = httpStatus;
        this.message = message;
        this.code = code;
    }

    public ErrorResponseResource(HttpStatus httpStatus, Throwable ex) {
        this();
        this.httpStatus = httpStatus;
        this.message = "Unexpected error";
    /*    this.debugMessage = ex.getLocalizedMessage();*/
    }

    public ErrorResponseResource(HttpStatus httpStatus, String message, Throwable ex) {
        this();
        this.httpStatus = httpStatus;
        this.message = message;
       /* this.debugMessage = ex.getLocalizedMessage();*/
    }

    public HttpStatus getStatus() {
        return httpStatus;
    }

    public void setStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
    /*   public String getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }*/

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
