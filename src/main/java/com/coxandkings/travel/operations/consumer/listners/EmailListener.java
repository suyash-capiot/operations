package com.coxandkings.travel.operations.consumer.listners;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.email.EmailUsingBodyAndDocumentsResource;
import com.coxandkings.travel.operations.resource.email.EmailUsingTemplateAndDocumentsResource;
import com.coxandkings.travel.operations.resource.email.EmailUsingTemplateResource;
import com.coxandkings.travel.operations.resource.email.EmailWithBodyResource;
import org.json.JSONException;

import javax.jcr.RepositoryException;
import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;

public interface EmailListener {
    void processEmail(EmailWithBodyResource emailWithBodyResource) throws SQLException, OperationException, MessagingException;
    void processEmail(EmailUsingTemplateResource emailUsingTemplateResource) throws SQLException, OperationException, IOException, JSONException, MessagingException;

    void processEmail(EmailUsingTemplateAndDocumentsResource emailUsingTemplateAndDocumentsResource) throws SQLException, OperationException, IOException, JSONException, MessagingException, RepositoryException;

    void processEmail(EmailUsingBodyAndDocumentsResource emailUsingBodyAndDocumentsResource) throws SQLException, OperationException, IOException, MessagingException, RepositoryException;
}
