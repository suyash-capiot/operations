package com.coxandkings.travel.operations.resource.todo;

import javax.persistence.Column;
import java.io.Serializable;

public class ToDoPriority implements Serializable {
    private String id;
    private Integer value;

    @Column
    private String name;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
