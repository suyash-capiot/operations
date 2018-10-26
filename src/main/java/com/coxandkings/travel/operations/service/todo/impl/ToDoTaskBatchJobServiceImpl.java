package com.coxandkings.travel.operations.service.todo.impl;

import com.coxandkings.travel.operations.enums.todo.ToDoTaskStatusValues;
import com.coxandkings.travel.operations.enums.user.UserType;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.todo.ToDoTask;
import com.coxandkings.travel.operations.repository.todo.ToDoTaskRepository;
import com.coxandkings.travel.operations.resource.remarks.UserBasicInfo;
import com.coxandkings.travel.operations.service.alert.AlertService;
import com.coxandkings.travel.operations.service.remarks.MDMUserService;
import com.coxandkings.travel.operations.service.todo.ToDoBatchJobService;
import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
import com.coxandkings.travel.operations.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;

@Transactional
@Service
public class ToDoTaskBatchJobServiceImpl implements ToDoBatchJobService {

    @Autowired
    private ToDoTaskRepository toDoTaskRepository;

    @Autowired
    private ToDoTaskService toDoTaskService;

    @Autowired
    private AlertService alertService;

    @Autowired
    private UserService userService;

    @Autowired
    private MDMUserService mdmUserService;

    @Override
    public void reassignUnreadTask() throws OperationException {

        List<HashMap> updated = toDoTaskRepository.reassignUnreadTask();
        String businessProcess = "Operations";
        String function = "To-Do Task";
        String alertName = "Todo Task has been reassigned to you";
        String message = "ToDo Task is reassigned to user";
        String companyName = "";
        String roleName = UserType.OPS_APPROVER_USER.getUserType();

        List<UserBasicInfo> userBasicInfoList = mdmUserService.getOpsUsersUsingRole(roleName);


        for (HashMap todoTaskId : updated) {
            String userId = null;
            ToDoTask aTask = toDoTaskRepository.getById(todoTaskId.get("id").toString());
            String fileHandlerId = aTask.getFileHandlerId();
            if (StringUtils.isEmpty(fileHandlerId)) {
                message = "";
                for (UserBasicInfo aUserBasicInfo : userBasicInfoList) {
                    userId = aUserBasicInfo.getId(); //User ID of Ops Approver User
                    alertService.createAlert(businessProcess, function, companyName, alertName,
                            userId, message);
                }
            } else {
                alertService.createAlert(businessProcess, function, companyName, alertName,
                        fileHandlerId, message);
            }
            aTask.setAlertNotified(true);
            aTask.setTaskStatus(ToDoTaskStatusValues.OPEN);
            aTask.setLastModifiedTime(System.currentTimeMillis());
            //TODO : modify in case if id for SYSTEM changes
            aTask.setLastModifiedByUserId("SYSTEM");
            toDoTaskRepository.saveOrUpdate(aTask);
        }

    }
}
