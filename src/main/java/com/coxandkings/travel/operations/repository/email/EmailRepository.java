package com.coxandkings.travel.operations.repository.email;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.email.Email;

import java.util.List;


public interface EmailRepository {

    Email saveOrUpdate(Email email);
    Email getEmailById(String id);
    List<Email> getEmailByMessageId(String messageId) throws OperationException;
    Email markAsRead(String id);


    Email getRefMailId(String refMail);
}
