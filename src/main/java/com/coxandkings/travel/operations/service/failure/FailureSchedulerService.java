package com.coxandkings.travel.operations.service.failure;

import com.coxandkings.travel.operations.exceptions.OperationException;
import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;

public interface FailureSchedulerService {

    void processBooking() throws ParseException, OperationException, IOException, JSONException;

    void closeToDo() throws OperationException;
}
