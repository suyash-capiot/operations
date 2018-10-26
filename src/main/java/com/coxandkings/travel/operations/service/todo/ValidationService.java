package com.coxandkings.travel.operations.service.todo;

import com.coxandkings.travel.operations.model.todo.ToDoTask;

import javax.validation.ConstraintViolation;
import java.util.Set;

public interface ValidationService {
    Set<ConstraintViolation<ToDoTask>> validateTask(ToDoTask task);
    Integer getNumberOfDuplicates(ToDoTask task);
    Boolean isValidUpdate(ToDoTask task);
//    Boolean isValidUpdate(FollowingTask followingTask);
//    Boolean isValidUpdate(SubTask subTask);

//    ToDoResponse addTaskValidation(ToDoRequest toDoRequest, ToDoTask task);
//    ToDoResponse addFollowingTaskValidation(ToDoRequest toDoRequest, FollowingTask followingTask);
//    ToDoResponse addSubTaskValidation(ToDoRequest toDoRequest, SubTask subTask);
}
