package com.coxandkings.travel.operations.repository.prodreview;

import com.coxandkings.travel.operations.model.prodreview.mdmtemplate.Template;

public interface TemplateRepository {

    Template saveTemplate(Template template);

    Template getById(String id);

    Boolean getExists(String id);

    Template getByTemplateId(String templateId);

    Template update(String id);

   /* MDMTemplate getMDMTemplateById(String templateId);
    Boolean getExists(String templateId);

    MDMTemplate saveTemplate(MDMTemplate mdmTemplate);

    MDMTemplate updateTemplate(MDMTemplate mdmTemplate);

    List<MDMTemplate> getSearchMDMTemplate(MDMTemplateResource mdmTemplateResource);

    MDMTemplate getByReference(String id);*/
}
