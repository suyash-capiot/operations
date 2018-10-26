package com.coxandkings.travel.operations.service.activitylog;


import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.activitylog.ActivityLog;
import com.coxandkings.travel.operations.model.communication.CommunicationTags;
import com.coxandkings.travel.operations.resource.activitylog.ActivityLogResource;
import com.coxandkings.travel.operations.resource.communication.CommunicationTagResource;
import org.springframework.stereotype.Service;

@Service
public interface ActivityLogService {
    ActivityLog getActivityLogById(String id) throws OperationException;

    ActivityLog addActivityLog(ActivityLogResource activityLogResource) throws OperationException;
    ActivityLog updateActivityLog(ActivityLogResource activitiesResouces) throws OperationException;
    ActivityLog markAsRead(String id) throws OperationException;
    CommunicationTags getAssociatedTags(String id) throws OperationException;
    ActivityLog updateCommunicationTags(String id ,CommunicationTagResource communicationTagResource) throws OperationException;
}
