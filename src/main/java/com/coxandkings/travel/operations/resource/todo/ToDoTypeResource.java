package com.coxandkings.travel.operations.resource.todo;

import com.coxandkings.travel.operations.enums.todo.ToDoTaskTypeValues;
import com.coxandkings.travel.operations.resource.BaseResource;

public class ToDoTypeResource extends BaseResource {
    String todoType;
    private ToDoTaskTypeValues code;

    public String getTodoType() {
        return todoType;
    }

    public void setTodoType(String todoType) {
        this.todoType = todoType;
    }

    public ToDoTaskTypeValues getCode() {
        return code;
    }

    public void setCode(ToDoTaskTypeValues code) {
        this.code = code;
    }
}
