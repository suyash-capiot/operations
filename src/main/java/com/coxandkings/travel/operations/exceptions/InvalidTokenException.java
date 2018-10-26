package com.coxandkings.travel.operations.exceptions;

public class InvalidTokenException extends OperationException {

    public InvalidTokenException(String errorCode) {
        super(errorCode);
    }

}
