package com.coxandkings.travel.operations.service.todo;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.todo.ToDoCheckListItem;
import com.coxandkings.travel.operations.resource.todo.CheckListItemResource;

public interface CheckListItemService {
    public ToDoCheckListItem save(CheckListItemResource resource) throws OperationException;
}
