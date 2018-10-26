package com.coxandkings.travel.operations.service.activitylog.impl;

import com.coxandkings.travel.operations.enums.communication.CommunicationType;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.activitylog.ActivityLog;
import com.coxandkings.travel.operations.model.communication.CommunicationTags;
import com.coxandkings.travel.operations.repository.activitylog.ActivityLogRepository;
import com.coxandkings.travel.operations.resource.activitylog.ActivityLogResource;
import com.coxandkings.travel.operations.resource.communication.CommunicationTagResource;
import com.coxandkings.travel.operations.service.activitylog.ActivityLogService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.CopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActivityLogServiceImpl implements ActivityLogService {

@Autowired
ActivityLogRepository activityLogRepository;

    @Override
    public ActivityLog getActivityLogById(String id) throws OperationException {
        ActivityLog activityLog = activityLogRepository.getActivityLogById(id);
        if(activityLog == null){
            throw new OperationException(Constants.OPS_ERR_10001);
        }
        return activityLog;
    }

    @Override
    public ActivityLog addActivityLog(ActivityLogResource activityLogResource) throws OperationException {
        ActivityLog activityLog = null;
        try {
            if (activityLogResource != null) {
                activityLog = new ActivityLog();

                activityLog.setUserId(activityLogResource.getUserId());
                activityLog.setSubject(activityLogResource.getTitle());
                activityLog.setBody(activityLogResource.getDescription());
                //  activityLog.setDateTime(ZonedDateTime.parse(activityLogResource.getDate()));
                activityLog.setConcernedPerson(activityLogResource.getConcernedPerson());
                activityLog.setDocumentRefIDs(activityLogResource.getDocumentRefIDs());
                activityLog.setBookId(activityLogResource.getBookId());
                activityLog.setProductSubCategory(activityLogResource.getProductSubCategory());
                activityLog.setSupplier(activityLogResource.getSupplier());
                activityLog.setProcess(activityLogResource.getProcess());
                activityLog.setFunction(activityLogResource.getFunction());
                activityLog.setScenario(activityLogResource.getScenario());
                activityLog.setCommunicationType(String.valueOf(CommunicationType.ACTIVITYLOG));
                activityLog.setDueDate(activityLogResource.getDueDate());
                if (activityLogResource.getCommunicationTagResource() != null) {
                    CommunicationTags communicationTags = new CommunicationTags();
                    communicationTags.setBaseCommunication(activityLog);
                    CopyUtils.copy(activityLogResource.getCommunicationTagResource(), communicationTags);
                    activityLog.setCommunicationTags(communicationTags);
                }
                activityLog = activityLogRepository.saveActivityLog(activityLog);
            }
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_10002);
        }
        return activityLog;

    }

    @Override
    public ActivityLog updateActivityLog(ActivityLogResource activityLogResource) throws OperationException {
        ActivityLog activityLog = null;
        if(activityLogResource != null && activityLogResource.getId() != null){
            try {
                activityLog = activityLogRepository.getActivityLogById(activityLogResource.getId());
                if (activityLog == null) {
                    throw new OperationException("ActivityLog Doesnot exist");
                }
                activityLog = new ActivityLog();

                activityLog.setId(activityLogResource.getId());
                activityLog.setSubject(activityLogResource.getTitle());
                activityLog.setBody(activityLogResource.getDescription());
                activityLog.setConcernedPerson(activityLogResource.getConcernedPerson());
                activityLog.setDocumentRefIDs(activityLogResource.getDocumentRefIDs());
                activityLog.setBookId(activityLogResource.getBookId());
                activityLog.setUserId(activityLogResource.getUserId());
                activityLog.setProductSubCategory(activityLogResource.getProductSubCategory());
                activityLog.setSupplier(activityLogResource.getSupplier());
                activityLog.setProcess(activityLogResource.getProcess());
                activityLog.setFunction(activityLogResource.getFunction());
                activityLog.setScenario(activityLogResource.getScenario());
                activityLog.setCommunicationType(String.valueOf(CommunicationType.ACTIVITYLOG));

                activityLog = activityLogRepository.saveActivityLog(activityLog);
            } catch (Exception e) {
                throw new OperationException(Constants.OPS_ERR_10003);
            }
        }else{
            throw new OperationException(Constants.OPS_ERR_10007);
        }
        return activityLog;
    }

    @Override
    public ActivityLog markAsRead(String id) throws OperationException {
        ActivityLog activityLog = null;
        try {
            activityLog = getActivityLogById(id);
            activityLog.setRead(true);
            activityLog = activityLogRepository.saveActivityLog(activityLog);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_10004);
        }
        return activityLog;
    }

    @Override
    public CommunicationTags getAssociatedTags(String id) throws OperationException {
        try {
            ActivityLog activityLog = getActivityLogById(id);
            return activityLog.getCommunicationTags();
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_10005);
        }
    }

    @Override
    @Transactional
    public ActivityLog updateCommunicationTags(String id , CommunicationTagResource communicationTagResource) throws OperationException {
        try {
            ActivityLog activityLog = getActivityLogById(id);
            CommunicationTags communicationTags = activityLog.getCommunicationTags();
            CopyUtils.copy(communicationTagResource, communicationTags);
            activityLog.setCommunicationTags(communicationTags);
            return activityLogRepository.saveActivityLog(activityLog);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_10006);
        }
    }
}
