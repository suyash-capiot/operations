package com.coxandkings.travel.operations.controller.todo;

import com.coxandkings.travel.operations.criteria.todo.ToDoCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.todo.ToDoTask;
import com.coxandkings.travel.operations.resource.MessageResource;
import com.coxandkings.travel.operations.resource.todo.*;
import com.coxandkings.travel.operations.service.todo.ToDoBatchJobService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.utils.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;


@RestController
@RequestMapping("/todo")
@CrossOrigin(value = "*")
public class ToDoController {

    @Autowired
    private ToDoTaskService toDoService;

    @Autowired
    private ToDoBatchJobService toDoBatchJobService;

    private Logger log = LogManager.getLogger(ToDoController.class);

    @GetMapping("/")
    public ResponseEntity<ToDoResponse> getAll() throws OperationException {
        try {
            ToDoResponse tasks = toDoService.getAll();
            toDoService.preProcessBeforeEveryGetList(tasks.getContent());
            List<ToDoTaskResponse> toDoTaskResponses = new LinkedList<>();
            try {
                toDoTaskResponses = toDoService.getToDoResponses(tasks.getContent());
            } catch (JSONException | IOException | OperationException e) {
                e.printStackTrace();
            }
            tasks.setData(toDoTaskResponses);
            tasks.setContent(null);
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_31101);
        }
    }

    @PostMapping("/searchByCriteria")
    public ResponseEntity<ToDoResponse> getToDoElement(@RequestBody ToDoCriteria toDoCriteria) throws OperationException {
        try {
            ToDoResponse toDoTasks = toDoService.getByCriteria(toDoCriteria);
//        toDoService.preProcessBeforeEveryGetList(toDoTasks.getContent());
            return new ResponseEntity<>(toDoTasks, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_31102);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ToDoTask> getToDoElement(@PathVariable(name = "id") String taskId) throws OperationException, JSONException, IOException {
        try {
            ToDoTask toDoTask = toDoService.getById(taskId);
            return new ResponseEntity<>(toDoTask, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_31103);
        }
    }

    @GetMapping("/view")
    public ResponseEntity<ToDoTaskResponse> viewToDo(@RequestParam(name = "id") String id) throws OperationException, IOException, JSONException {
        try {
            ToDoTaskResponse toDoTask = toDoService.view(id);
            return new ResponseEntity<>(toDoTask, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_31104);
        }
    }

    @GetMapping("/userSummary")
    public ResponseEntity<List<UserSummary>> getUserSummary() throws OperationException {
        try {
            List<UserSummary> toDoTaskUserSummary = null;
            try {
                toDoTaskUserSummary = toDoService.getUserSummary();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return new ResponseEntity<>(toDoTaskUserSummary, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_31105);
        }
    }

    @GetMapping("/statusSummary")
    public ResponseEntity<List<StatusSummary>> getStatusSummary() throws OperationException {
        try {
            List<StatusSummary> toDoTaskStatusSummary = null;
            try {
                toDoTaskStatusSummary = toDoService.getStatusSummary();
            } catch (OperationException | IOException | JSONException e) {
                e.printStackTrace();
            }
            return new ResponseEntity<>(toDoTaskStatusSummary, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred in fetching status summery", e);
            throw new OperationException(Constants.OPS_ERR_31106);
        }
    }

    @PostMapping("/")
    public ResponseEntity<ToDoTask> saveToDoElement(@RequestBody ToDoTaskResource toDoRequest) throws OperationException, ParseException, InvocationTargetException, IllegalAccessException, IOException, JSONException {
        try {
            ToDoTask toDoTask = toDoService.save(toDoRequest);
            return new ResponseEntity<>(toDoTask, HttpStatus.CREATED);
        } catch (OperationException oe) {
            throw oe;
        }
    }

    @PutMapping("/")
    public ResponseEntity<ToDoTask> updateToDoElement(@RequestBody ToDoTaskResource toDoRequest) throws OperationException, ParseException, InvocationTargetException, IllegalAccessException, IOException, JSONException {
        try {
            ToDoTask toDoTask = toDoService.save(toDoRequest);
            return new ResponseEntity<>(toDoTask, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_31108);
        }
    }

    @PostMapping("/lock/{id}")
    public ResponseEntity<MessageResource> getLock(@PathVariable(name = "id") String taskId,
                                            @RequestParam(name = "userId") String userId) throws OperationException, IOException, JSONException {

        MessageResource editLock = toDoService.getEditLock(taskId, userId);
        return new ResponseEntity<>(editLock, HttpStatus.OK);

    }

    @PostMapping("/releaseLock/{id}")
    public ResponseEntity<MessageResource> releaseLock(@PathVariable(name = "id") String taskId,
                                                @RequestParam(name = "userId") String userId) throws OperationException, IOException, JSONException {
            MessageResource editLock = toDoService.releaseLock(taskId, userId);
            return new ResponseEntity<MessageResource>(editLock, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable(name = "id") String id) throws OperationException {
        try {
            toDoService.delete(id);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_31111);
        }
    }

    @PostMapping("/assign")
    public ResponseEntity<List<ToDoTaskResponse>> assign(@RequestBody ToDoAssignResource toDoAssignResource) throws OperationException {
        try {
            List<ToDoTaskResponse> toDoTasks = toDoService.assignTasks(toDoAssignResource);
            return new ResponseEntity<>(toDoTasks, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_31112);
        }
    }
}
