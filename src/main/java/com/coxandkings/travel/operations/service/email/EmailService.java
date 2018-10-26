package com.coxandkings.travel.operations.service.email;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.communication.CommunicationTags;
import com.coxandkings.travel.operations.model.email.Email;
import com.coxandkings.travel.operations.resource.communication.CommunicationTagResource;
import com.coxandkings.travel.operations.resource.email.*;
import org.json.JSONException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface EmailService {

    EmailResponse sendEmail(EmailWithBodyResource resource) throws OperationException;

    EmailResponse sendEmail(EmailUsingTemplateResource resource) throws IOException, OperationException, SQLException, JSONException;

    EmailResponse sendEmail(EmailUsingTemplateAndDocumentsResource resource) throws OperationException, IOException, SQLException, JSONException;

    EmailResponse sendEmail(EmailUsingBodyAndDocumentsResource resource) throws OperationException, IOException, SQLException;
    void receiveCustomerInboundEmails();
    void receiveClientInboundEmails();
    void receiveSupplierInboundEmails();
    void receiveAutoInBoundEmail();
    Email getEmailById(String emailId);
    List<Email> getEmailByMessageId(String messageId) throws OperationException;
    Email markAsRead(String id);
    CommunicationTags getAssociatedTags(String id);
    Email updateCommunicationTags(String id, CommunicationTagResource communicationTagResource);
     Email getEmailByRefId(String messageId) throws OperationException;
}




