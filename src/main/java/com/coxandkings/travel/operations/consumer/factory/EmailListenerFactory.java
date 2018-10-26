package com.coxandkings.travel.operations.consumer.factory;

import com.coxandkings.travel.operations.consumer.listners.impl.EmailListenerImpl;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.email.EmailUsingTemplateAndDocumentsResource;
import com.coxandkings.travel.operations.resource.email.EmailUsingTemplateResource;
import com.coxandkings.travel.operations.resource.email.EmailWithBodyResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.jcr.RepositoryException;
import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;

@Component
public class EmailListenerFactory {

        @Autowired
        private ApplicationContext context;

    public void processEmail(String payload) throws IOException, SQLException, OperationException, JSONException, MessagingException, RepositoryException {
            EmailListenerImpl emailListener = getEmailListener(this.context);
            JSONObject jsonObject = new JSONObject(payload);
            String emailResourceType = jsonObject.getString("emailResourceType");
            switch (emailResourceType){
                case "type1":
                    EmailWithBodyResource emailWithBodyResource = new ObjectMapper().readValue(payload, EmailWithBodyResource.class);
                    emailListener.processEmail(emailWithBodyResource);
                    break;
                case "type2":
                    EmailUsingTemplateResource emailUsingTemplateResource = new ObjectMapper().readValue(payload , EmailUsingTemplateResource.class);
                    emailListener.processEmail(emailUsingTemplateResource);
                    break;
                case "type3":
                    EmailUsingTemplateAndDocumentsResource emailUsingTemplateAndDocumentsResource = new ObjectMapper().readValue(payload, EmailUsingTemplateAndDocumentsResource.class);
                    emailListener.processEmail(emailUsingTemplateAndDocumentsResource);
                    break;
            }
            int age = jsonObject.getInt("age");
            EmailWithBodyResource emailWithBodyResource = new ObjectMapper().readValue(payload, EmailWithBodyResource.class);
            emailListener.processEmail(emailWithBodyResource);
        }

        public EmailListenerImpl getEmailListener(ApplicationContext context) {
            return context.getBean(EmailListenerImpl.class);
        }


}
