package com.coxandkings.travel.operations.controller.todo;

import com.coxandkings.travel.operations.enums.todo.*;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.todo.ToDoSubType;
import com.coxandkings.travel.operations.resource.todo.*;
import com.coxandkings.travel.operations.service.todo.ToDoMdmService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskSubTypeService;
import com.coxandkings.travel.operations.utils.Constants;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/todo/screen")
@CrossOrigin(value = "*")
public class ToDoScreenController {
    @Autowired
    ToDoMdmService toDoMdmService;

    @Autowired
    ToDoTaskSubTypeService toDoTaskSubTypeService;

    private static Logger logger = Logger.getLogger( ToDoScreenController.class );

    @GetMapping("/assignTo")
    public ResponseEntity<List<ToDoUserResource>> assignTasks() throws JSONException, IOException, OperationException {
        try {
            List<ToDoUserResource> response = toDoMdmService.assign();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error( "Error code OPS_ERR_31113 - Error occurred in /todo/screen/assignTo method" );
            throw new OperationException(Constants.OPS_ERR_31113);
        }
    }

    @GetMapping("/company")
    public ResponseEntity<List<ToDoCompanyResource>> company(@RequestParam(name = "groupCompanyId") String groupCompanyId) throws JSONException, IOException, OperationException {
        try {
            List<ToDoCompanyResource> toDoCompanyResources = toDoMdmService.getCompanies(groupCompanyId);
            return new ResponseEntity<>(toDoCompanyResources, HttpStatus.OK);
        } catch (Exception e) {
            logger.error( "Error code OPS_ERR_31114 - Error occurred in /todo/screen/company method" );
            throw new OperationException(Constants.OPS_ERR_31114);
        }
    }

    @GetMapping("/getCompanyMarkets")
    public ResponseEntity<List<ToDoCompanyMarketResource>> companyMarket(@RequestParam(name = "companyId") String companyid) throws OperationException, IOException, JSONException {
        try {
            List<ToDoCompanyMarketResource> companyMarketResources = toDoMdmService.getCompanyMarkets(companyid);
            return new ResponseEntity<>(companyMarketResources, HttpStatus.OK);
        } catch (Exception e) {
            logger.error( "Error code OPS_ERR_31115 - Error occurred in /todo/screen/getCompanyMarkets method" );
            throw new OperationException(Constants.OPS_ERR_31115);
        }
    }

    @GetMapping("/getCompanies")
    public ResponseEntity<List<ToDoCompanyResource>> getCompanies(@RequestParam(name = "userId") String userId) throws OperationException, IOException, JSONException {
        try {
            List<ToDoCompanyResource> toDoCompanyResources = toDoMdmService.getCompanies(userId);
            return new ResponseEntity<>(toDoCompanyResources, HttpStatus.OK);
        } catch (Exception e) {
            logger.error( "Error code OPS_ERR_31116 - Error occurred in /todo/screen/getCompanies method" );
            throw new OperationException(Constants.OPS_ERR_31116);
        }
    }

    @GetMapping("/getFileHandlers")
    public ResponseEntity<List<ToDoUserResource>> getFileHandlers(@RequestParam(name = "functionalArea") String functionalArea) throws OperationException {
        try {
            List<ToDoUserResource> response = toDoMdmService.assign(functionalArea);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error( "Error code OPS_ERR_31117 - Error occurred in /todo/screen/getFileHandlers method" );
            throw new OperationException(Constants.OPS_ERR_31117);
        }
    }

    @GetMapping("/getFunctionalAreas")
    public ResponseEntity<List<ToDoFunctionalArea>> getAllFunctionalAreas() throws OperationException {
        try {
            List<ToDoFunctionalArea> toDoFunctionalAreas = new ArrayList<>();
            ToDoFunctionalArea toDoFunctionalArea;

            for (ToDoFunctionalAreaValues toDoFunctionalAreaValues : ToDoFunctionalAreaValues.values()) {
                toDoFunctionalArea = new ToDoFunctionalArea();
                toDoFunctionalArea.setId(toDoFunctionalAreaValues.getValue());
                toDoFunctionalArea.setName(toDoFunctionalAreaValues.getValue());
                toDoFunctionalAreas.add(toDoFunctionalArea);
            }

            return new ResponseEntity<>(toDoFunctionalAreas, HttpStatus.OK);
        } catch (Exception e) {
            logger.error( "Error code OPS_ERR_31118 - Error occurred in /todo/screen/getFunctionalAreas method" );
            throw new OperationException(Constants.OPS_ERR_31118);
        }
    }

    @GetMapping("/getTaskStatuses")
    public ResponseEntity<List<ToDoStatus>> getAllStatuses() throws OperationException {
        try {
            List<ToDoStatus> toDoStatuses = new ArrayList<>();
            ToDoStatus toDoStatus;

            for (ToDoTaskStatusValues toDoTaskStatusValues : ToDoTaskStatusValues.values()) {
                toDoStatus = new ToDoStatus();
                toDoStatus.setId(toDoTaskStatusValues.getValue());
                toDoStatus.setName(toDoTaskStatusValues.getValue());
                toDoStatuses.add(toDoStatus);
            }

            return new ResponseEntity<>(toDoStatuses, HttpStatus.OK);
        } catch (Exception e) {
            logger.error( "Error code OPS_ERR_31119 - Error occurred in /todo/screen/getTaskStatuses method" );
            throw new OperationException(Constants.OPS_ERR_31119);
        }
    }

    @GetMapping("/getTaskTypes")
    public ResponseEntity<List<ToDoType>> getAllTaskTypes() throws OperationException {
        try {
            List<ToDoType> toDoTypes = new ArrayList<>();
            ToDoType toDoType;

            for (ToDoTaskTypeValues toDoTaskTypeValues : ToDoTaskTypeValues.values()) {
                toDoType = new ToDoType();
                toDoType.setId(toDoTaskTypeValues.getValue());
                toDoType.setName(toDoTaskTypeValues.getValue());
                toDoTypes.add(toDoType);
            }

            return new ResponseEntity<>(toDoTypes, HttpStatus.OK);
        } catch (Exception e) {
            logger.error( "Error code OPS_ERR_31120 - Error occurred in /todo/screen/getTaskTypes method" );
            throw new OperationException(Constants.OPS_ERR_31120);
        }
    }

    @GetMapping("/getTaskPriorities")
    public ResponseEntity<List<ToDoPriority>> getAllPriorities() throws OperationException {
        try {
            List<ToDoPriority> toDoPriorities = new ArrayList<>();
            ToDoPriority toDoPriority;

            for (ToDoTaskPriorityValues toDoTaskPriorityValues : ToDoTaskPriorityValues.values()) {
                toDoPriority = new ToDoPriority();
                toDoPriority.setId(toDoTaskPriorityValues.getValue());
                toDoPriority.setName(toDoTaskPriorityValues.getValue());
                toDoPriorities.add(toDoPriority);
            }

            return new ResponseEntity<>(toDoPriorities, HttpStatus.OK);
        } catch (Exception e) {
            logger.error( "Error code OPS_ERR_31121 - Error occurred in /todo/screen/getTaskPriorities method" );
            throw new OperationException(Constants.OPS_ERR_31121);
        }
    }

    @GetMapping("/getTaskNames")
    public ResponseEntity<List<ToDoTaskName>> getAlTaskNames() throws OperationException {
        try {
            List<ToDoTaskName> toDoTaskNames = new ArrayList<>();
            ToDoTaskName toDoTaskName;

            for (ToDoTaskNameValues toDoTaskNameValues : ToDoTaskNameValues.values()) {
                toDoTaskName = new ToDoTaskName();
                toDoTaskName.setId(toDoTaskNameValues.getValue());
                toDoTaskName.setName(toDoTaskNameValues.getValue());
                toDoTaskNames.add(toDoTaskName);
            }

            return new ResponseEntity<>(toDoTaskNames, HttpStatus.OK);
        } catch (Exception e) {
            logger.error( "Error code OPS_ERR_31122 - Error occurred in /todo/screen/getTaskNames method" );
            throw new OperationException(Constants.OPS_ERR_31122);
        }
    }

    @GetMapping("/getTaskSubTypes")
    public ResponseEntity<List<ToDoSubType>> getAllSubTypes() throws OperationException {
        try {
            return new ResponseEntity<>(toDoTaskSubTypeService.getAllSubTypes(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error( "Error code OPS_ERR_31123 - Error occurred in /todo/screen/getTaskSubTypes method" );
            throw new OperationException(Constants.OPS_ERR_31123);
        }
    }

    @GetMapping("/search/getFileHandlers")
    public ResponseEntity<List<ToDoUserResource>> getFileHandlersForSearch() throws OperationException {
        try {
            return new ResponseEntity<>(toDoMdmService.getFileHandlersForSearch(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error( "Error code OPS_ERR_31124 - Error occurred in /todo/screen/getFileHandlers method" );
            throw new OperationException(Constants.OPS_ERR_31124);
        }
    }
}
