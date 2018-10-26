package com.coxandkings.travel.operations.service.mailroomanddispatch;

import com.coxandkings.travel.operations.exceptions.OperationException;
import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

public interface MailRoomBatchService {
    void updateMailroomStatusActive() throws OperationException, JSONException;

    void updateMailroomEmployeeStatusActive() throws OperationException, ParseException, InvocationTargetException, IllegalAccessException, IOException, JSONException;

    void updateMailroomStatusInactive() throws OperationException;

}
