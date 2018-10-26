//package com.coxandkings.travel.operations.service.todo.impl;
//
//import com.coxandkings.travel.operations.controller.todo.ToDoController;
//import com.coxandkings.travel.operations.enums.todo.*;
//import com.coxandkings.travel.operations.exceptions.OperationException;
//import com.coxandkings.travel.operations.model.todo.RecordStatus;
//import com.coxandkings.travel.operations.model.todo.ToDoTask;
//import com.coxandkings.travel.operations.repository.todo.RecordStatusRepository;
//import com.coxandkings.travel.operations.repository.todo.ToDoTaskRepository;
//import com.coxandkings.travel.operations.resource.todo.ToDoTaskResource;
//import com.coxandkings.travel.operations.service.todo.*;
//import org.apache.log4j.Logger;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.management.OperationsException;
//import java.lang.reflect.InvocationTargetException;
//import java.text.ParseException;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import static org.junit.Assert.*;
//
//@RunWith(value = SpringRunner.class)
//@SpringBootTest
//public class ToDoTaskServiceImplTest {
//
//    private static final Logger logger = Logger.getLogger(ToDoTaskServiceImplTest.class);
//
//    private static ToDoTaskResource toDoTaskResource = null;
//
//    private static ToDoTask toDoTask = null;
//
//    @Autowired
//    private ToDoTaskService toDoService;
//
//    @Autowired
//    private ToDoTaskNameService toDoTaskNameService;
//
//    @Autowired
//    private ToDoTaskTypeService toDoTaskTypeService;
//
//    @Autowired
//    private ToDoTaskSubTypeService toDoTaskSubTypeService;
//
//    @Autowired
//    private ToDoFunctionalAreaService toDoFunctionalAreaService;
//
//    @Autowired
//    private ToDoStatusService toDoStatusService;
//
//    @Autowired
//    private ToDoPriorityService toDoPriorityService;
//
//    @Autowired
//    private ToDoTaskGeneratedTypeService toDoTaskGeneratedTypeService;
//
//    @Autowired
//    private RecordStatusRepository recordStatusRepository;
//
//    @Autowired
//    private ToDoTaskRepository toDoTaskRepository;
//
//    static {
//        toDoTaskResource = new ToDoTaskResource();
//        toDoTaskResource.setCreatedByUserId("Nikhil");
//        toDoTaskResource.setReferenceId("CNK-00002");
//        toDoTaskResource.setClientCategoryId("client-category-id");
//        toDoTaskResource.setClientSubCategoryId("client-sub-category-id");
//        toDoTaskResource.setClientTypeId("ANCLR3049847");
//        toDoTaskResource.setCompanyId("Company-001");
//        toDoTaskResource.setCompanyMarketId("CompanyMarket-001");
//        //toDoTaskResource.setDueOn(System.currentTimeMillis() + 1000000000l);
//        toDoTaskResource.setFileHandlerId("FH1");
//        toDoTaskResource.setSecondaryFileHandlerId("FH2");
//        toDoTaskResource.setNote("Test Note");
//        toDoTaskResource.setProductId("2");
//        toDoTaskResource.setRemark("Test remarks");
//        toDoTaskResource.setSuggestedActions("Test suggestions");
//        toDoTaskResource.setTaskStatusId(ToDoTaskStatusValues.NEW.getValue());
//        toDoTaskResource.setTaskFunctionalAreaId(ToDoFunctionalAreaValues.OPERATIONS.getValue());
//        toDoTaskResource.setTaskNameId("BOOK");
//        toDoTaskResource.setTaskPriorityId(ToDoTaskPriorityValues.HIGH.getValue());;
//        toDoTaskResource.setTaskTypeId("Main task");
//        toDoTaskResource.setTaskSubTypeId("BOOKING");
//    }
//
//    private void populateMasterdata() throws OperationException, ParseException, InvocationTargetException, IllegalAccessException {
//        toDoTask = toDoService.save(toDoTaskResource);
//    }
//
//    private void validateToDoSave(ToDoTask toDoTask) {
//        logger.info("Equating saved task details with to-do resource");
//        assertNotNull(toDoTask.getId());
//        if(toDoTaskResource.getId() != null) {
//            assertEquals(toDoTask.getId(), toDoTaskResource.getId());
//        }
//
//        assertEquals(toDoTask.getTaskName().getId(), toDoTaskResource.getTaskNameId());
//        assertEquals(toDoTask.getTaskFunctionalArea().getId(), toDoTaskResource.getTaskFunctionalAreaId());
//        assertEquals(toDoTask.getTaskType().getId(), toDoTaskResource.getTaskTypeId());
//        assertEquals(toDoTask.getTaskSubType().getId(), toDoTaskResource.getTaskSubTypeId());
//
//        assertEquals(toDoTask.getClientId(), toDoTaskResource.getClientId());
//        assertEquals(toDoTask.getCompanyId(), toDoTaskResource.getCompanyId());
//
//        assertEquals(toDoTask.getFileHandlerId(), toDoTaskResource.getFileHandlerId());
//        assertEquals(toDoTask.getSecondaryFileHandlerId(), toDoTaskResource.getSecondaryFileHandlerId());
//
//        assertEquals(toDoTask.getTaskGeneratedType().getId(), ToDoTaskGeneratedTypeValues.MANUAL);
//        assertEquals(toDoTask.getTaskStatus().getId(), ToDoTaskStatusValues.NEW);
//    }
//
//    private void validateToDoGet(ToDoTask toDoTask, ToDoTask existingTask) {
//        logger.info("Comparing the expected result with response");
//        logger.info("Expected : "+toDoTask);
//        logger.info("Actual : "+existingTask);
//        assertEquals(toDoTask.getId(), existingTask.getId());
//
//        assertEquals(toDoTask.getTaskName().getId(), existingTask.getTaskName().getId());
//        assertEquals(toDoTask.getTaskFunctionalArea().getId(), existingTask.getTaskFunctionalArea().getId());
//        assertEquals(toDoTask.getTaskType().getId(), existingTask.getTaskType().getId());
//        assertEquals(toDoTask.getTaskSubType().getId(), existingTask.getTaskSubType().getId());
//
//        assertEquals(toDoTask.getClientId(), existingTask.getClientId());
//        assertEquals(toDoTask.getCompanyId(), existingTask.getCompanyId());
//
//        assertEquals(toDoTask.getFileHandlerId(), existingTask.getFileHandlerId());
//        assertEquals(toDoTask.getSecondaryFileHandlerId(), existingTask.getSecondaryFileHandlerId());
//
//        assertEquals(toDoTask.getTaskGeneratedType().getId(), existingTask.getTaskGeneratedType().getId());
//        assertEquals(toDoTask.getTaskStatus().getId(), existingTask.getTaskStatus().getId());
//    }
//
//    private void validateToDoView(ToDoTask toDoTask, String loggedInUser) {
//        logger.info("comparing file handler of to-do task with logged in user's id");
//        logger.info("File handler id "+toDoTask.getFileHandlerId());
//        logger.info("Logged in user " + loggedInUser);
//        assertEquals(toDoTask.getFileHandlerId(), "FH1");
//        logger.info("The read flag is expected to be true and it is : "+toDoTask.getToDoTaskDetails().getRead());
//        assertTrue(toDoTask.getToDoTaskDetails().getRead());
//    }
//
//    private void validateToDoEditLock(ToDoTask newTask, String userId) {
//        logger.info("The record status now should change to LOCKED and it is : " + newTask.getToDoTaskDetails().getRecordStatus().getId());
//        assertEquals(newTask.getToDoTaskDetails().getRecordStatus().getId(), RecordStatusValues.LOCKED);
//        logger.info("The lockedBy field should be set \n" + "Expected : " + userId + "\nActual : "+newTask.getToDoTaskDetails().getLockedBy());
//        assertEquals(newTask.getToDoTaskDetails().getLockedBy(), userId);
//    }
//
//    private void validateToDoReleaseLock(ToDoTask toDotask, String fh2) {
//        logger.info("The To-Do task status now should be ACTIVE and now it is : "+toDotask.getToDoTaskDetails().getRecordStatus().getId());
//        assertEquals(toDotask.getToDoTaskDetails().getRecordStatus().getId(), RecordStatusValues.ACTIVE);
//        logger.info("The To-Do task now should have lockedBy field as null and it is : "+ toDotask.getToDoTaskDetails().getLockedBy());
//        assertNull(toDotask.getToDoTaskDetails().getLockedBy());
//    }
////    @Test
////    public void getAll() {
////        List<ToDoTask> toDoTaskList = toDoService.getAll();
////        validateToDoGetAll(tasksInDatabase, toDoTaskList);
////    }
//
//    @Test
//    public void testGetById() throws OperationException, ParseException, InvocationTargetException, IllegalAccessException {
//        logger.info("Entered ToDoTaskServiceImplTest :: testGetById()");
//        if(toDoTask == null) {
//            populateMasterdata();
//        }
//
//        String id = toDoTask.getId();
//        logger.info("Getting ToDo task by id "+ id);
//        ToDoTask existingTask = toDoService.getById(id);
//        validateToDoGet(toDoTask, existingTask);
//        logger.info("Exit ToDoTaskServiceImplTest :: testGetById()");
//    }
//
//    @Test
//    public void testView() throws OperationException, ParseException, InvocationTargetException, IllegalAccessException {
//        logger.info("Entered ToDoTaskServiceImplTest :: testView()");
//        if(toDoTask == null) {
//            populateMasterdata();
//        }
//
//        String id = toDoTask.getId();
//        logger.info("Getting ToDo task by id "+ id);
//        toDoTask = toDoService.view(id, "FH1");
//        validateToDoView(toDoTask, "FH1");
//        logger.info("Exit ToDoTaskServiceImplTest :: testView()");
//    }
//
//    @Test
//    public void testSave() throws OperationException, ParseException, InvocationTargetException, IllegalAccessException {
//        logger.info("Entered ToDoTaskServiceImplTest :: testSave()");
//        if(toDoTask == null) {
//            populateMasterdata();
//        }
//        logger.info("Saving to do task");
//        validateToDoSave(toDoTask);
//        logger.info("Exit ToDoTaskServiceImplTest :: testSave()");
//    }
//
//    @Test
//    public void testUpdate() throws OperationException, ParseException, InvocationTargetException, IllegalAccessException {
//        logger.info("Entered ToDoTaskServiceImplTest :: testUpdate()");
//        if(toDoTask == null) {
//            populateMasterdata();
//        }
//
//        toDoTaskResource.setId(toDoTask.getId());
//        toDoTaskResource.setTaskNameId( ToDoTaskNameValues.APPROVE.getValue() );
//        logger.info("Updating to do task");
//        toDoTask = toDoService.save(toDoTaskResource);
//        validateToDoSave(toDoTask);
//        toDoTask = toDoService.releaseLock(toDoTask.getId(), "Nikhil");
//        logger.info("Exit ToDoTaskServiceImplTest :: testUpdate()");
//    }
//
//    @Test
//    public void testGetEditLock() throws OperationException, ParseException, InvocationTargetException, IllegalAccessException {
//        logger.info("Entered ToDoTaskServiceImplTest :: testGetEditLock()");
//        if(toDoTask == null) {
//            populateMasterdata();
//        }
//
//        if(toDoTask.getToDoTaskDetails().getRecordStatus().getId().equals(RecordStatusValues.LOCKED)) {
//            logger.info("Releasing lock as the task was already locked");
//            toDoTask = toDoService.releaseLock(toDoTask.getId(), "FH2");
//        }
//
//        logger.info("Getting edit lock");
//        toDoTask = toDoService.getEditLock(toDoTask.getId(), "FH2");
//        validateToDoEditLock(toDoTask, "FH2");
//        logger.info("Exit ToDoTaskServiceImplTest :: testGetEditLock()");
//    }
//
//    @Test
//    public void testReleaseLock() throws OperationException, ParseException, InvocationTargetException, IllegalAccessException {
//        logger.info("Entered ToDoTaskServiceImplTest :: testReleaseLock()");
//        if(toDoTask == null) {
//            populateMasterdata();
//        }
//
//        if(!toDoTask.getToDoTaskDetails().getRecordStatus().getId().equals(RecordStatusValues.LOCKED)) {
//            logger.info("Getting lock as the task was not locked earlier");
//            toDoTask = toDoService.getEditLock(toDoTask.getId(), "FH2");
//        }
//        toDoTask = toDoService.releaseLock(toDoTask.getId(), "FH2");
//        validateToDoReleaseLock(toDoTask, "FH2");
//        logger.info("Exit ToDoTaskServiceImplTest :: testReleaseLock()");
//    }
//
//    @Test
//    public void testFailGetEditLockWhenAlreadyLocked() throws OperationException, ParseException, InvocationTargetException, IllegalAccessException {
//        logger.info("Entered ToDoTaskServiceImplTest :: testFailGetEditLockWhenAlreadyLocked()");
//        if(toDoTask == null) {
//            populateMasterdata();
//        }
//
//        if(!toDoTask.getToDoTaskDetails().getRecordStatus().getId().equals(RecordStatusValues.LOCKED)) {
//            toDoTask = toDoService.getEditLock(toDoTask.getId(), "FH2");
//        }
//        try {
//            toDoTask = toDoService.getEditLock(toDoTask.getId(), "Nikhil");
//        } catch (OperationException e) {
//            logger.info("Exception thrown with error : "+ e.getErrorCode() + "Expected : " + "The task has already been locked");
//            assertEquals(e.getErrorCode(), "The task has already been locked");
//        }
//        finally {
//            logger.info("Exit ToDoTaskServiceImplTest :: testFailGetEditLockWhenAlreadyLocked()");
//        }
//    }
//}