package com.coxandkings.travel.operations.model.todo;

import com.coxandkings.travel.operations.enums.todo.ToDoTaskGeneratedTypeValues;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "TODO_TASK_GENERATED_TYPE")
public class ToDoTaskGeneratedType implements Serializable {

    @Id
    @Column
    @Enumerated(EnumType.STRING)
    private ToDoTaskGeneratedTypeValues id;

    @Column(name = "generated_type")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ToDoTaskGeneratedTypeValues getId() {
        return id;
    }

    public void setId(ToDoTaskGeneratedTypeValues id) {
        this.id = id;
    }
}
