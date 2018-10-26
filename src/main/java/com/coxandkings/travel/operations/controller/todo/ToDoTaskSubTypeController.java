package com.coxandkings.travel.operations.controller.todo;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.todo.ToDoSubType;
import com.coxandkings.travel.operations.resource.todo.ToDoTaskSubTypeResource;
import com.coxandkings.travel.operations.service.todo.ToDoTaskSubTypeService;
import com.coxandkings.travel.operations.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todo/subtype")
@CrossOrigin(value = "*")
public class ToDoTaskSubTypeController {
    @Autowired
    private ToDoTaskSubTypeService toDoTaskSubTypeService;

    @GetMapping("/{id}")
    public ResponseEntity<ToDoSubType> getById(@PathVariable(name = "id") String id) throws OperationException {
        try {
            return new ResponseEntity<>(toDoTaskSubTypeService.getbyId(id), HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_31125);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<ToDoSubType>> getAll() throws OperationException {
        try {
            return new ResponseEntity<>(toDoTaskSubTypeService.getAllSubTypes(), HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_31126);
        }
    }

//    @PostMapping("/")
//    public ResponseEntity<ToDoSubType> saveSubType(@RequestBody ToDoTaskSubTypeResource resource) throws OperationException {
//        try {
//            return new ResponseEntity<>(toDoTaskSubTypeService.save(resource), HttpStatus.OK);
//        } catch (Exception e) {
//            throw new OperationException(Constants.OPS_ERR_31127);
//        }
//    }

}
