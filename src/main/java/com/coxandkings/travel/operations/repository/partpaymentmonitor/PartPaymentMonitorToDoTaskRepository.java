package com.coxandkings.travel.operations.repository.partpaymentmonitor;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.todo.ToDoSubType;
import com.coxandkings.travel.operations.resource.todo.ToDoStatus;

import java.util.List;

public interface PartPaymentMonitorToDoTaskRepository {

    public List<String> getToDoTasksBySubType(ToDoSubType subType, ToDoStatus status ) throws OperationException;

}
