package com.coxandkings.travel.operations.service.template;


import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.template.request.TemplateInfo;
import com.coxandkings.travel.operations.resource.email.DynamicVariables;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface TemplateLoaderService {
    String getEmailContent(TemplateInfo templateInfo, List<DynamicVariables> dynamicVariables) throws IOException, OperationException, JSONException;
    String getSMSContent(TemplateInfo templateInfo, List<DynamicVariables> dynamicVariables) throws IOException, OperationException, JSONException;
    String getTemplateBodyByID(String templateId) throws OperationException, JSONException;
    String getTemplateDetails(String businessProcess , String function) throws OperationException;
    Map getTemplateInfoById(String id) throws OperationException;
}
