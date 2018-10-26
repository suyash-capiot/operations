package com.coxandkings.travel.operations.service.template.impl;

import com.coxandkings.travel.operations.enums.template.TemplateTypeValues;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.template.request.TemplateInfo;
import com.coxandkings.travel.operations.model.template.request.TemplateRequestResource;
import com.coxandkings.travel.operations.resource.email.DynamicVariables;
import com.coxandkings.travel.operations.service.template.TemplateLoaderService;
import com.coxandkings.travel.operations.systemlogin.MDMDataSource;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.JsonObjectProvider;
import com.coxandkings.travel.operations.utils.MDMRestUtils;
import com.coxandkings.travel.operations.utils.RestUtils;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TemplateLoaderServiceImpl implements TemplateLoaderService {
    @Value(value = "${dtms.url}")
    private String processDefUrl;

    @Value(value = "${dtms.username}")
    private String dtmsUser;

    @Value(value = "${dtms.password}")
    private String dtmsPass;

    @Value(value = "${dtms.request.lookup}")
    private String lookup;

    @Value(value = "${dtms.request.commands.insert.out-identifier}")
    private String outIdentifier;

    @Value(value = "${dtms.request.commands.insert.return-object}")
    private Boolean returnObject;

    @Value(value = "${dtms.request.commands.insert.entry-point}")
    private String entryPoint;

    @Value(value = "${dtms.request.commands.insert.template-object.document-template-management.headers.user-id:}")
    private String userId;

    @Value(value = "${dtms.request.commands.insert.template-object.document-template-management.headers.transaction-id:}")
    private String transactionId;

    @Value( value = "${dtms.template-content-json-path}")
    private String responseJsonContentPath;

    @Value( value = "${mdm.template}")
    private String mdmTemplateUrl;

    @Value( value = "${mdm.template-body-json-path}")
    private String responseJsonBodyPath;

    @Autowired
    private JsonObjectProvider jsonParser;

    @Autowired
    MDMDataSource mdmDataSource;

    @Autowired
    private MDMRestUtils mdmRestUtils;

    @Override
    public String getEmailContent(TemplateInfo templateInfo, List<DynamicVariables> dynamicVariables) throws IOException, OperationException, JSONException {
        return getTemplateWithData(templateInfo, dynamicVariables, TemplateTypeValues.Email);
    }

    @Override
    public String getSMSContent(com.coxandkings.travel.operations.model.template.request.TemplateInfo templateInfo, List<DynamicVariables> dynamicVariables) throws IOException, OperationException, JSONException {
        return getTemplateWithData(templateInfo, dynamicVariables, TemplateTypeValues.Sms);
    }


    private HttpHeaders createHeaders(String username, String password){
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")) );
            String authHeader = "Basic " + new String( encodedAuth );
            set( "Authorization", authHeader );
            setContentType(MediaType.APPLICATION_JSON);
        }};
    }

    private String getTemplateWithData(com.coxandkings.travel.operations.model.template.request.TemplateInfo templateInfo, List<DynamicVariables> dynamicVariables, TemplateTypeValues templateTypeValues) throws  OperationException, JSONException {
        TemplateRequestResource templateRequestResource = new TemplateRequestResource(templateInfo, dynamicVariables, lookup, outIdentifier, returnObject, entryPoint, userId, transactionId);
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(this.processDefUrl);
        RestTemplate restTemplate = RestUtils.getTemplate();
        HttpEntity<TemplateRequestResource> httpEntity = new HttpEntity<>(templateRequestResource, createHeaders(dtmsUser, dtmsPass));

        // Get response JSON
        ResponseEntity<String> templateResponseAsString = restTemplate.exchange( uriComponentsBuilder.toUriString(), HttpMethod.POST, httpEntity, String.class );
           JSONObject jsonObject = new JSONObject(templateResponseAsString.getBody());
            if(jsonObject.getString("type").toString().toUpperCase().equals("FAILURE"))
            {
                //TODO : Error code  message should not be : "400 null"
                throw new OperationException(Constants.ER47);
            }

        String htmlContent = jsonParser.getAttributeValue( templateResponseAsString.getBody(), responseJsonContentPath, String.class);

        return htmlContent;
    }

    @Override
    public String getTemplateBodyByID(String templateId) throws OperationException, JSONException {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(this.mdmTemplateUrl+"/");
        uriComponentsBuilder.path(templateId);
        String templateResponseAsString =  mdmRestUtils.exchange(uriComponentsBuilder.toUriString(), HttpMethod.GET, String.class);
        String htmlContent = jsonParser.getAttributeValue( templateResponseAsString, responseJsonBodyPath, String.class);

        return htmlContent;
    }

    @Override
    public String getTemplateDetails(String businessProcess, String function) throws OperationException {
        String filter = "{\"applicability.businessProcess\":\"" + businessProcess + "\",\"applicability.function\":\""+function +"\"}";
        String data  = mdmRestUtils.getForObject(( mdmTemplateUrl+ "?filter={filter}&select=templateName,templateID"), String.class, filter);
        return data ;
    }

    @Override
    public Map getTemplateInfoById(String id) throws OperationException,JSONException {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(this.mdmTemplateUrl+"/");
        uriComponentsBuilder.path(id);
        String templateResponseAsString =  mdmRestUtils.exchange(uriComponentsBuilder.toUriString(), HttpMethod.GET, String.class);
        String process=jsonParser.getAttributeValue(templateResponseAsString,"$.applicability.businessProcess[0]",String.class);
        String scenario=jsonParser.getAttributeValue(templateResponseAsString,"$.applicability.scenario[0]",String.class);
        String function=jsonParser.getAttributeValue(templateResponseAsString,"$.applicability.function[0]",String.class);
        String subject=jsonParser.getAttributeValue(templateResponseAsString,"$.templateContent.subject",String.class);
        TemplateInfo templateInfo=new TemplateInfo();
        templateInfo.setFunction(function);
        templateInfo.setProcess(process);
        templateInfo.setScenario(scenario);
        Map map=new HashMap();
        map.put("subject",subject);
        map.put("templateInfo",templateInfo);
        return map;
    }
}
