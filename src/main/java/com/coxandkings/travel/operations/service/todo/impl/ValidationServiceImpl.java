package com.coxandkings.travel.operations.service.todo.impl;

import com.coxandkings.travel.operations.model.todo.ToDoTask;
import com.coxandkings.travel.operations.repository.todo.ToDoTaskRepository;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.service.todo.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.HashSet;
import java.util.Set;

@Service
public class ValidationServiceImpl implements ValidationService{

    private ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    @Autowired
    ToDoTaskService toDoService;

    @Autowired
    ToDoTaskRepository toDoRepository;

//    @Autowired
//    SubToDoRepository subToDoRepository;

//    @Autowired
//    FollowingToDoRepository followingToDoRepository;

    public Boolean isValidUpdate(ToDoTask task) {
        //TODO - delete this when the primary key is not auto generated.
        if(task.getId() != null) {
            if(toDoRepository.getById(task.getId()) == null) {
                //validation failed. User wants to update something which does not exist
                return false;
            }
        }

        return true;
    }

    @Override
//    public ToDoResponse addTaskValidation(ToDoRequest toDoRequest, ToDoTask task) {
//        ToDoResponse toDoResponse = new ToDoResponse();
//        if(toDoRequest.getAcceptDuplicate() == null || !toDoRequest.getAcceptDuplicate()) {
//            Integer integer = getNumberOfDuplicates(task);
//            if(integer > 0) {
//                System.out.println("Duplicate check failed");
//                toDoResponse.setDuplicateFlag(new Boolean(true));
//                toDoResponse.setMessageCode("ER02");
//            }
//            else {
//                toDoResponse.setDuplicateFlag(new Boolean(false));
//            }
//        }
//        else {
//            toDoResponse.setDuplicateFlag(new Boolean(false));
//        }
//        toDoResponse.setConstraintViolationSet(validateTask(task));
//        //toDoResponse.setValidUpdate(isValidUpdate(task));
//
//        return toDoResponse;
//    }

//    @Override //todo - any advanced validations
//    public ToDoResponse addFollowingTaskValidation(ToDoRequest toDoRequest, FollowingTask followingTask) {
//        ToDoResponse toDoResponse = new ToDoResponse();
//        if(toDoRequest.getAcceptDuplicate() == null || !toDoRequest.getAcceptDuplicate()) {
//            Integer integer = getNumberOfDuplicates(followingTask.getTask());
//            if(integer > 0) {
//                System.out.println("Duplicate check failed");
//                toDoResponse.setDuplicateFlag(new Boolean(true));
//            }
//            else {
//                toDoResponse.setDuplicateFlag(new Boolean(false));
//            }
//        }
//        else {
//            toDoResponse.setDuplicateFlag(new Boolean(false));
//        }
//
//        toDoResponse.setConstraintViolationSet(validateTask(followingTask.getTask()));
//        toDoResponse.setValidUpdate(isValidUpdate(followingTask));
//        return toDoResponse;
//    }
//
//    @Override //todo - any advanced validations
//    public ToDoResponse addSubTaskValidation(ToDoRequest toDoRequest, SubTask subTask) {
//        ToDoResponse toDoResponse = new ToDoResponse();
//        if(toDoRequest.getAcceptDuplicate() == null || !toDoRequest.getAcceptDuplicate()) {
//            Integer integer = getNumberOfDuplicates(subTask.getTask());
//            if(integer > 0) {
//                System.out.println("Duplicate check failed");
//                toDoResponse.setDuplicateFlag(new Boolean(true));
//            }
//            else {
//                toDoResponse.setDuplicateFlag(new Boolean(false));
//            }
//        }
//        else {
//            toDoResponse.setDuplicateFlag(new Boolean(false));
//        }
//
//        toDoResponse.setConstraintViolationSet(validateTask(subTask.getTask()));
//        toDoResponse.setValidUpdate(isValidUpdate(subTask));
//        return toDoResponse;
//    }

//    public Boolean isValidUpdate(FollowingTask followingTask) {
////        //TODO - delete this when the primary key is not auto generated.
////        if(followingTask.getId() != null) {
////            if(followingToDoRepository.get(followingTask.getId()) == null) {
////                //TODO - validation failed. User wants to update something which does not exist
////                return false;
////            }
////        }
//
//        return true;
//    }
//
//    public Boolean isValidUpdate(SubTask subTask) {
//        //TODO - delete this when the primary key is not auto generated.
//        if(subTask.getId() != null) {
//            if(subToDoRepository.get(subTask.getId()) == null) {
//                //TODO - validation failed. User wants to update something which does not exist
//                return false;
//            }
//        }
//
//        return true;
//    }

    public Integer getNumberOfDuplicates(ToDoTask task) {
        //Long duplicates = toDoService.detectDuplicate(task);
        //System.out.println("The number of duplicates is " + duplicates);
        //if(duplicates != null && duplicates > 0) return duplicates.intValue();

        return 0;
    }

    public Set<ConstraintViolation<ToDoTask>> validateTask(ToDoTask task) {
        Set<ConstraintViolation<ToDoTask>> constraintViolationSet = new HashSet<>();
        constraintViolationSet = validatorFactory.getValidator().validate(task);
        constraintViolationSet.stream().forEach(element -> System.out.println(element));

        return constraintViolationSet;
    }
}