package com.coxandkings.travel.operations.service.prodreview;

import com.coxandkings.travel.operations.criteria.prodreview.MDMTemplateCriteria;
import com.coxandkings.travel.operations.criteria.prodreview.TemplateCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.prodreview.mdmtemplate.Template;
import com.coxandkings.travel.operations.resource.prodreview.MailTemplateResource;
import com.coxandkings.travel.operations.resource.prodreview.ProductTemplateReferenceResource;
import com.coxandkings.travel.operations.resource.prodreview.mdmtemplateresource.MDMTemplateResource;
import org.json.JSONException;

import java.util.List;
import java.util.Map;

public interface TemplateService {

    MDMTemplateResource getMDMTemplate(String templateId) throws OperationException;

    Template save(Template template);

    String searchTemplate(MDMTemplateCriteria mdmTemplateCriteria) throws OperationException, JSONException;

    List<ProductTemplateReferenceResource> searchTemplateByCriteria(TemplateCriteria templateCriteria) throws OperationException;

    Map sendMailToClient(MailTemplateResource mailTemplateResource) throws OperationException;
}
