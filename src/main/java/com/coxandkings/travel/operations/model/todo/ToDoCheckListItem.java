package com.coxandkings.travel.operations.model.todo;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
public class ToDoCheckListItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @Column
    private Integer serialNumber;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "todo_task_id")
    private ToDoTask toDoTask;

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ToDoTask getToDoTask() {
        return toDoTask;
    }

    public void setToDoTask(ToDoTask toDoTask) {
        this.toDoTask = toDoTask;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}