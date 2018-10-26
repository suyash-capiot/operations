package com.coxandkings.travel.operations.repository.todo;

import com.coxandkings.travel.operations.model.todo.ToDoCheckListItem;

public interface CheckListItemRepository {
    public ToDoCheckListItem saveOrUpdate(ToDoCheckListItem checkListItem);
    public ToDoCheckListItem getById(String id);
}
