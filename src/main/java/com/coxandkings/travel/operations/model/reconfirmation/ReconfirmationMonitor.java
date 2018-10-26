package com.coxandkings.travel.operations.model.reconfirmation;

import java.time.ZonedDateTime;

public class ReconfirmationMonitor {

    private ZonedDateTime reconfirmationCutOffDate;
    private ZonedDateTime timeLimitExpiryDate;
    private long numberOfAttempts;
    private ZonedDateTime tillDateWhenTheReconfirmationReqCanBeHold;

    public ZonedDateTime getReconfirmationCutOffDate() {
        return reconfirmationCutOffDate;
    }

    public void setReconfirmationCutOffDate(ZonedDateTime reconfirmationCutOffDate) {
        this.reconfirmationCutOffDate = reconfirmationCutOffDate;
    }

    public ZonedDateTime getTimeLimitExpiryDate() {
        return timeLimitExpiryDate;
    }

    public void setTimeLimitExpiryDate(ZonedDateTime timeLimitExpiryDate) {
        this.timeLimitExpiryDate = timeLimitExpiryDate;
    }

    public long getNumberOfAttempts() {
        return numberOfAttempts;
    }

    public void setNumberOfAttempts(long numberOfAttempts) {
        this.numberOfAttempts = numberOfAttempts;
    }

    public ZonedDateTime getTillDateWhenTheReconfirmationReqCanBeHold() {
        return tillDateWhenTheReconfirmationReqCanBeHold;
    }

    public void setTillDateWhenTheReconfirmationReqCanBeHold(ZonedDateTime tillDateWhenTheReconfirmationReqCanBeHold) {
        this.tillDateWhenTheReconfirmationReqCanBeHold = tillDateWhenTheReconfirmationReqCanBeHold;
    }
}
