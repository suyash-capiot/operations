package com.coxandkings.travel.operations.repository.activitylog;

import com.coxandkings.travel.operations.criteria.communication.CommunicationCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.activitylog.ActivityLog;
import com.coxandkings.travel.operations.model.communication.BaseCommunication;

import java.util.List;

public interface ActivityLogRepository {
    ActivityLog saveActivityLog(ActivityLog activityLog);
    ActivityLog getActivityLogById(String id);


    List<? extends BaseCommunication> getByCriteria(CommunicationCriteria communicationType) throws OperationException;
}
