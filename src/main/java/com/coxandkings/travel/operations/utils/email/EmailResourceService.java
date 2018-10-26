package com.coxandkings.travel.operations.utils.email;

import com.coxandkings.travel.operations.resource.email.EmailUsingTemplateResource;

import java.util.Map;

public interface EmailResourceService {

    EmailUsingTemplateResource sendEmailUsingTemplate(String process,
                                                      String function,
                                                      String scenario,
                                                      Map<String, String> dynamicVariables,
                                                      String fileName,
                                                      byte[] bytes,
                                                      String emailId,
                                                      String subject);


}
