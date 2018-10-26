package com.coxandkings.travel.operations.controller.activityLog;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.activitylog.ActivityLog;
import com.coxandkings.travel.operations.model.communication.CommunicationTags;
import com.coxandkings.travel.operations.resource.activitylog.ActivityLogResource;
import com.coxandkings.travel.operations.resource.communication.UpdateCommunicationTagsResource;
import com.coxandkings.travel.operations.service.activitylog.ActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/activitylogs")
@CrossOrigin(origins = "*")
public class ActivityLogController {
    @Autowired
    ActivityLogService activityLogService;

    @PostMapping(value="/v1/add")
    public ResponseEntity<ActivityLog> createActivityLog(@RequestBody (required = false)ActivityLogResource activityLogResource) throws OperationException {
        ActivityLog activityLog = activityLogService.addActivityLog(activityLogResource);
        return new ResponseEntity<>(activityLog, HttpStatus.CREATED);
    }

    @PostMapping(value="/v1/update")
    public ResponseEntity<ActivityLog> updateActivityLog(@RequestBody (required = false) ActivityLogResource activityLogResource) throws OperationException {
        ActivityLog activityLog = activityLogService.updateActivityLog(activityLogResource);
        return new ResponseEntity<>(activityLog, HttpStatus.CREATED);
    }

    @GetMapping(value="/v1/{id}")
    public ResponseEntity<ActivityLog> getActivityLogById(@PathVariable("id") String id) throws OperationException {
        ActivityLog activityLog = activityLogService.getActivityLogById(id);
        return new ResponseEntity<>(activityLog,HttpStatus.OK);
    }

    @PutMapping(path="/v1/markasread")
    public ResponseEntity<ActivityLog> markAsRead(@RequestParam String id) throws OperationException {
        ActivityLog activityLog = activityLogService.markAsRead(id);
        return new ResponseEntity<>(activityLog, HttpStatus.OK);
    }

    @GetMapping(value="/v1/communicationtags/{id}")
    public ResponseEntity<CommunicationTags> getCommunicationTags(@PathVariable("id") String id) throws OperationException {
        CommunicationTags communicationTags = activityLogService.getAssociatedTags(id);
        return new ResponseEntity<>(communicationTags,HttpStatus.OK);
    }

    @PutMapping(value = "/v1/updatecommunicationtags")
    public ResponseEntity<ActivityLog> getCommunicationTags(@RequestBody UpdateCommunicationTagsResource updateCommunicationTagsResource) throws OperationException {
        ActivityLog activityLog =  activityLogService.updateCommunicationTags(updateCommunicationTagsResource.getId(),updateCommunicationTagsResource.getCommunicationTags());
        return new ResponseEntity<>(activityLog,HttpStatus.OK);
    }

}
