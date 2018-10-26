package com.coxandkings.travel.operations.service.managedocumentation.impl;

import com.coxandkings.travel.operations.enums.email.EmailPriority;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.template.request.TemplateInfo;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.resource.email.EmailUsingBodyAndDocumentsResource;
import com.coxandkings.travel.operations.resource.email.EmailUsingTemplateAndDocumentsResource;
import com.coxandkings.travel.operations.service.managedocumentation.DocumentCommunication;
import com.coxandkings.travel.operations.service.managedocumentation.generatedocument.impl.TemplateBuilder;
import com.coxandkings.travel.operations.utils.EmailUtils;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DocumentCommunicationImpl implements DocumentCommunication {
    @Autowired
    private TemplateBuilder templateBuilder;

    private static final Logger logger = LogManager.getLogger(EmailUtils.class);

    @Value("${communication.email.from_address}")
    private String fromEmailAddress;

    @Value("${communication.email.process}")
    private String process;

    @Value("${manage_document.email.function}")
    private String handoverCustFuncton;

    @Value("${manage_document.email.scenario}")
    private String handoverCustScenario;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Value("${communication.email.sendEmailUsingTemplateAndDocuments}")
    private String emailURL;

    @Override
    public EmailResponse sendEmailToClient(List<String> referenceIDs, String clientEmail, String subject, Map<String, String> dynamicVariables) throws OperationException {
        TemplateInfo templateInfo = templateBuilder.build(handoverCustFuncton, handoverCustScenario);
        EmailUsingTemplateAndDocumentsResource resource = new EmailUsingTemplateAndDocumentsResource();

        resource.setDocumentReferenceIDs(referenceIDs);
        resource.setPriority(EmailPriority.HIGH);
        resource.setSubject(subject);
        resource.setToMail(Arrays.asList(clientEmail));
        resource.setTemplateInfo(templateInfo);
        resource.setFromMail(fromEmailAddress);
        

        resource.setDynamicVariables(dynamicVariables);

        ResponseEntity<EmailResponse> emailResponseResponseEntity = null;

        try {
            emailResponseResponseEntity = mdmRestUtils.postForEntity(emailURL, resource, EmailResponse.class);
        } catch (OperationException e) {
            logger.error("Not able to send mail:" + clientEmail, e);
            throw new OperationException("Unable to send email to client" + clientEmail);
        } catch (Exception e) {
            logger.error("Unable to send email:" + clientEmail, e);

        }

        if (null != emailResponseResponseEntity) {
            return emailResponseResponseEntity.getBody();
        }

        return null;
    }
}
