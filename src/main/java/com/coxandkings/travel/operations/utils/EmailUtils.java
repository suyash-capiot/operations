package com.coxandkings.travel.operations.utils;

import com.coxandkings.travel.operations.enums.email.EmailPriority;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.template.request.TemplateInfo;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.resource.email.EmailUsingTemplateResource;
import com.coxandkings.travel.operations.resource.email.FileAttachmentResource;
import com.coxandkings.travel.operations.service.managedocumentation.generatedocument.impl.TemplateBuilder;
import com.coxandkings.travel.operations.service.user.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

@Component
public class EmailUtils {
    private static final Logger logger = LogManager.getLogger(EmailUtils.class);

    @Value("${communication.email.from_address}")
    private String fromEmailAddress;
    @Value("${communication.email.process}")
    private String process;
    @Value("${communication.email.api}")
    private String emailUrl;

    @Autowired
    private UserService userService;

    @Autowired
    private TemplateBuilder templateBuilder;

    @Autowired
    private MDMRestUtils mdmRestUtils;


    public EmailResponse buildClientMail(String function, String promoScenario, String emailId, String subject, Map<String, String> dynamicVariables, String fileName, byte[] file) throws OperationException {
        EmailUsingTemplateResource emailUsingTemplateResource = new EmailUsingTemplateResource();
        emailUsingTemplateResource.setFromMail(fromEmailAddress);
        emailUsingTemplateResource.setToMail(Collections.singletonList(emailId));
        emailUsingTemplateResource.setSubject(subject);
        emailUsingTemplateResource.setPriority(EmailPriority.HIGH);
        TemplateInfo templateInfo = new TemplateInfo();
        templateInfo.setIsActive(true);
        templateInfo.setGroupOfCompanies("");
        templateInfo.setGroupCompany("");
        templateInfo.setCompanyName("");
        templateInfo.setSubBusinessUnit("");
        templateInfo.setMarket("");
        templateInfo.setSource("");
        templateInfo.setProductCategory("");
        templateInfo.setProductCategorySubType("");
        templateInfo.setProcess(process);
        templateInfo.setFunction(function);
        templateInfo.setScenario(promoScenario);
        templateInfo.setRule1("");
        templateInfo.setRule2("");
        templateInfo.setRule3("");
        templateInfo.setCommunicationType("");
        templateInfo.setCommunicateTo("");
        templateInfo.setIncomingCommunicationType("");
        templateInfo.setDestination("");
        templateInfo.setBrochure("");
        templateInfo.setTour("");
        emailUsingTemplateResource.setTemplateInfo(templateInfo);

        if (file != null && !StringUtils.isEmpty(fileName)) {
            FileAttachmentResource fileAttachmentResource = new FileAttachmentResource();
            fileAttachmentResource.setFilename(fileName);
            byte[] b = file;
            fileAttachmentResource.setContent(b);
            emailUsingTemplateResource.setFileAttachments(Collections.singletonList(fileAttachmentResource));
        } else {
            emailUsingTemplateResource.setFileAttachments(null);
        }
        emailUsingTemplateResource.setDynamicVariables(dynamicVariables);

        return this.sendMailToClient(emailId, emailUsingTemplateResource);
    }


    public EmailResponse buildClientMail(String bookId, String orderId, String function, String promoScenario, String emailId, String subject, Map<String, String> dynamicVariables, String fileName, byte[] file) throws OperationException {
        EmailUsingTemplateResource emailUsingTemplateResource = new EmailUsingTemplateResource();
        emailUsingTemplateResource.setFromMail(fromEmailAddress);
        emailUsingTemplateResource.setToMail(Collections.singletonList(emailId));
        emailUsingTemplateResource.setSubject(subject);
        emailUsingTemplateResource.setPriority(EmailPriority.HIGH);

        TemplateInfo templateInfo = templateBuilder.build(function, promoScenario, bookId, orderId);

        emailUsingTemplateResource.setTemplateInfo(templateInfo);

        if (file != null && !StringUtils.isEmpty(fileName)) {
            FileAttachmentResource fileAttachmentResource = new FileAttachmentResource();
            fileAttachmentResource.setFilename(fileName);
            byte[] b = file;
            fileAttachmentResource.setContent(b);
            emailUsingTemplateResource.setFileAttachments(Collections.singletonList(fileAttachmentResource));
        } else {
            emailUsingTemplateResource.setFileAttachments(null);
        }
        emailUsingTemplateResource.setDynamicVariables(dynamicVariables);

        return this.sendMailToClient(emailId, emailUsingTemplateResource);
    }

    private EmailResponse sendMailToClient(String clientEmail, EmailUsingTemplateResource emailUsingTemplateResource) throws OperationException {
        ResponseEntity<EmailResponse> emailResponseEntity = null;
        RestTemplate restTemplate = RestUtils.getTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", userService.getLoggedInUserToken());
        HttpEntity<EmailUsingTemplateResource> httpEntity = new HttpEntity<>(emailUsingTemplateResource, headers);
        try {
            URI uri =  UriComponentsBuilder.fromUriString(emailUrl).build().encode().toUri();
//            emailResponseEntity = mdmRestUtils.exchange(uri, HttpMethod.POST, emailUsingTemplateResource, EmailResponse.class, MediaType.APPLICATION_JSON);
            emailResponseEntity = restTemplate.exchange(uri.toString(),HttpMethod.POST,httpEntity,EmailResponse.class);
        } catch (Exception e) {
            logger.error("Unable to send mail:" + clientEmail, e);
            throw new OperationException(Constants.OPS_ERR_20016);
        }
        return emailResponseEntity.getBody();
    }
}
