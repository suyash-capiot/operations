package com.coxandkings.travel.operations.service.todo;

import com.coxandkings.travel.operations.criteria.todo.ToDoCriteria;
import com.coxandkings.travel.operations.enums.todo.ToDoTaskStatusValues;
import com.coxandkings.travel.operations.enums.todo.ToDoTaskSubTypeValues;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.core.OpsProduct;
import com.coxandkings.travel.operations.model.todo.ToDoTask;
import com.coxandkings.travel.operations.resource.MessageResource;
import com.coxandkings.travel.operations.resource.todo.*;
import org.json.JSONException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;

@Service
public interface ToDoTaskService {

    ToDoResponse getByCriteria(ToDoCriteria criteria) throws OperationException;

    ToDoTask getById(String id) throws OperationException, IOException, JSONException;

    ToDoTaskResponse view(String id) throws OperationException, IOException, JSONException;

    ToDoTask save(ToDoTaskResource resource) throws OperationException, ParseException, InvocationTargetException, IllegalAccessException, IOException, JSONException;

    MessageResource getEditLock(String id, String userId) throws OperationException, IOException, JSONException;

    MessageResource releaseLock(String id, String userId) throws OperationException, IOException, JSONException;

    ToDoTask approve(ToDoTaskResource toDoTaskResource) throws OperationException, ParseException, InvocationTargetException, IllegalAccessException, IOException, JSONException;

    ToDoTask viewThresholdReached(String id) throws OperationException;

    ToDoTask dueThresholdReached(String id) throws OperationException;

    List<UserSummary> getUserSummary() throws OperationException, IOException, JSONException;

    List<StatusSummary> getStatusSummary() throws OperationException, IOException, JSONException;

    void delete(String id);

    ToDoResponse getAll();

    List<ToDoTaskResponse> assignTasks(ToDoAssignResource toDoAssignResource);

    void preProcessBeforeEveryGet(ToDoTask toDoTask) throws OperationException, IOException, JSONException;

    void preProcessBeforeEveryGetList(List<ToDoTask> toDoTasks);

    ToDoTaskResponse getToDoResponse(ToDoTask toDoTask) throws OperationException, JSONException, IOException;

    List<ToDoTaskResponse> getToDoResponses(List<ToDoTask> toDoTasks) throws JSONException, IOException, OperationException;

    void updateToDoTaskStatus(String entityReferenceId, ToDoTaskSubTypeValues subTypeValue, ToDoTaskStatusValues status);

    void createToDoTaskForNewBooking(OpsBooking opsBooking) throws InvocationTargetException, IOException, IllegalAccessException, ParseException, OperationException;

    void createToDoTaskForFailedBooking(OpsBooking opsBooking, OpsProduct opsProduct, KafkaBookingMessage kafkaBookingMessage) throws InvocationTargetException, IOException, IllegalAccessException, OperationException;

    ToDoResponse getDuplicate(ToDoTask toDoTask) throws OperationException;

}