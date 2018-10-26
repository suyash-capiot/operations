package com.coxandkings.travel.operations.service.alert;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.notification.InlineMessageResource;
import com.coxandkings.travel.operations.resource.notification.NotificationResource;
import com.coxandkings.travel.operations.resource.notification.NotificationSearchCriteria;
import com.coxandkings.travel.operations.resource.notification.OpsAlertResponse;
import org.json.JSONException;

import java.util.List;
import java.util.Map;

public interface AlertService {
    NotificationResource sendInlineMessageAlert(InlineMessageResource inlineMessageResource) throws OperationException;

    List<OpsAlertResponse> getByUserId(String userId) throws JSONException, OperationException;

    List<OpsAlertResponse> searchAlertByCriteria(NotificationSearchCriteria notificationSearchCriteria) throws OperationException;

    Map dismissNotification(String alertID, String userID) throws OperationException;

    NotificationResource createAlert(String businessProcess, String function, String companyName, String alertName, String userId, String message) throws OperationException;

    List<OpsAlertResponse> getNotificationsByBusinessProcess(String businessProcess, String userId, String companyName) throws OperationException;
}