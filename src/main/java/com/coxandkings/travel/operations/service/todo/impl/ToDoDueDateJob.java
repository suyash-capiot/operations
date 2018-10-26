//package com.coxandkings.travel.operations.service.todo.impl;
//
//import com.coxandkings.travel.operations.exceptions.OperationException;
//import com.coxandkings.travel.operations.service.todo.ToDoTaskService;
//import org.quartz.Job;
//import org.quartz.JobDataMap;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//
//public class ToDoDueDateJob implements Job {
//
//    @Override
//    public void execute(JobExecutionContext context) throws JobExecutionException {
//        System.out.println("ToDo due job is executing...");
//        JobDataMap jobDataMap = context.getMergedJobDataMap();
//        ToDoTaskService toDoTaskService = (ToDoTaskService) jobDataMap.get("toDoService");
//        String id = jobDataMap.getString("id");
//
//        try {
//            toDoTaskService.dueThresholdReached(id);
//        } catch (OperationException e) {
//            e.printStackTrace();
//        }
//    }
//}
