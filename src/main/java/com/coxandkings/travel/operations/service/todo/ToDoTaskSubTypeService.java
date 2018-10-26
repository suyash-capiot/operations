package com.coxandkings.travel.operations.service.todo;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.todo.ToDoSubType;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskSubTypeResource;

import java.util.List;

public interface ToDoTaskSubTypeService {
    public ToDoSubType getbyId(String id);
    List<ToDoSubType> getAllSubTypes();
}
