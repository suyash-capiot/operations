package com.coxandkings.travel.operations.utils.email.impl;

import com.coxandkings.travel.operations.enums.email.EmailPriority;
import com.coxandkings.travel.operations.model.template.request.TemplateInfo;
import com.coxandkings.travel.operations.resource.email.EmailUsingTemplateResource;
import com.coxandkings.travel.operations.resource.email.FileAttachmentResource;
import com.coxandkings.travel.operations.utils.email.EmailResourceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Map;

@Service("EmailResourceServiceImpl")
public class EmailResourceServiceImpl implements EmailResourceService {

    @Value("${communication.email.from_address}")
    private String fromEmailAddress;


    @Override
    public EmailUsingTemplateResource sendEmailUsingTemplate(String process, String function, String scenario,
                                                             Map<String, String> dynamicVariables,
                                                             String fileName, byte[] bytes, String emailId, String subject) {
        EmailUsingTemplateResource emailUsingTemplateResource = new EmailUsingTemplateResource();
        ;
        FileAttachmentResource fileAttachmentResource = null;

        if (!StringUtils.isEmpty(process) && !StringUtils.isEmpty(function) && !StringUtils.isEmpty(scenario) && !StringUtils.isEmpty(emailId)) {

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
            templateInfo.setScenario(scenario);
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
            emailUsingTemplateResource.setDynamicVariables(dynamicVariables);

            if (fileName == null) {
                emailUsingTemplateResource.setFileAttachments(null);
            } else {
                fileAttachmentResource = new FileAttachmentResource();
                fileAttachmentResource.setFilename(fileName);
                fileAttachmentResource.setContent(bytes);
                emailUsingTemplateResource.setFileAttachments(Collections.singletonList(fileAttachmentResource));
            }
        }

        return emailUsingTemplateResource;
    }
}
