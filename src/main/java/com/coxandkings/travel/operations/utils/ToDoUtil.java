package com.coxandkings.travel.operations.utils;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.todo.ToDoTask;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskResource;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

public class ToDoUtil {

    public static ToDoTask create(ToDoTaskResource resource, ToDoTaskService toDoTaskService) throws OperationException, ParseException, InvocationTargetException, IllegalAccessException, IOException, JSONException {
        ToDoTask toDoTask = toDoTaskService.save(resource);
        return toDoTask;
    }
}
