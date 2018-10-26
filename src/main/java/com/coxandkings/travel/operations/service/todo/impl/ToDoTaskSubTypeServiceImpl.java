package com.coxandkings.travel.operations.service.todo.impl;

import com.coxandkings.travel.operations.enums.todo.ToDoTaskSubTypeValues;
import com.coxandkings.travel.operations.model.todo.ToDoSubType;
import com.coxandkings.travel.operations.service.todo.ToDoTaskSubTypeService;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class ToDoTaskSubTypeServiceImpl implements ToDoTaskSubTypeService {
    private static Logger log = LogManager.getLogger(ToDoTaskSubTypeService.class);


    @Override
    public ToDoSubType getbyId(String id) {
        ToDoSubType newSubType = new ToDoSubType();
        ToDoTaskSubTypeValues[] allSubTypes = ToDoTaskSubTypeValues.values();
        for (ToDoTaskSubTypeValues aSubTypeValue : allSubTypes) {
            if (id.equalsIgnoreCase(aSubTypeValue.name())) {
                newSubType.setId(aSubTypeValue.name());
                newSubType.setName(aSubTypeValue.getSubTaskType());
                break;
            }
        }
        return newSubType;
    }

    @Override
    public List<ToDoSubType> getAllSubTypes() {
        List<ToDoSubType> toDoSubTypes = new ArrayList<>();

        ToDoTaskSubTypeValues[] allSubTypes = ToDoTaskSubTypeValues.values();
        for (ToDoTaskSubTypeValues aSubTypeValue : allSubTypes) {
            ToDoSubType aSubType = new ToDoSubType();
            aSubType.setId(aSubTypeValue.name());
            aSubType.setName(aSubTypeValue.getSubTaskType());
            toDoSubTypes.add(aSubType);
        }

        return toDoSubTypes;
    }
}
