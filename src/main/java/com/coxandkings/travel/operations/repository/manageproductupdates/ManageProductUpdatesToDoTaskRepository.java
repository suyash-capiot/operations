package com.coxandkings.travel.operations.repository.manageproductupdates;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.todo.ToDoSubType;
import com.coxandkings.travel.operations.resource.todo.ToDoStatus;

import java.util.List;

public interface ManageProductUpdatesToDoTaskRepository {

    public List<String> getToDoTasksBySubType(ToDoSubType subType, ToDoStatus status) throws OperationException;

}
