package com.coxandkings.travel.operations.repository.alert;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.alert.Notification;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository {
    Notification saveOrUpdate(Notification notification);

    List<Notification> getNotifications(String userId, Integer page, Integer count) throws OperationException;
}
