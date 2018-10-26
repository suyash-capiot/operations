package com.coxandkings.travel.operations.repository.todo;

import com.coxandkings.travel.operations.criteria.todo.ToDoCriteria;
import com.coxandkings.travel.operations.model.todo.ToDoTask;
import com.coxandkings.travel.operations.resource.todo.ToDoResponse;

import java.util.HashMap;
import java.util.List;

public interface ToDoTaskRepository {
    ToDoResponse getByCriteria(ToDoCriteria toDoCriteria, Boolean checkEmptyOrNull);
    ToDoTask getById(String id);
    ToDoTask saveOrUpdate(ToDoTask toDoTask);
    void remove(String id);
    Boolean isPresent(String id);
    Long getCountByCriteria(ToDoCriteria toDoCriteria);
    ToDoResponse getAll();
    void deleteById(String id);
    List<String> getAllFileHandlers();
    List<HashMap> reassignUnreadTask();
}
