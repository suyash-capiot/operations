package com.coxandkings.travel.operations.config;

import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskSubTypeService;
import org.springframework.beans.factory.annotation.Autowired;

public class ServiceContextBean {
    @Autowired
    private  ToDoTaskService toDoTaskService;
    @Autowired
    private  ToDoTaskSubTypeService toDoTaskSubTypeService;

    public ToDoTaskService getToDoTaskService() {
        return toDoTaskService;
    }

    public ToDoTaskSubTypeService getToDoTaskSubTypeService() {
        return toDoTaskSubTypeService;
    }
}
