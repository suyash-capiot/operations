package com.coxandkings.travel.operations.repository.prodreview.impl;

import com.coxandkings.travel.operations.model.prodreview.mdmtemplate.Template;
import com.coxandkings.travel.operations.repository.prodreview.TemplateRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository("templateRepository")
@Transactional
public class TemplateRepositoryImpl extends SimpleJpaRepository<Template, String> implements TemplateRepository {
    private EntityManager entityManager;

    public TemplateRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(Template.class, entityManager);
        this.entityManager = entityManager;
    }


    public Template getById(String id) {
        return this.findOne(id);
    }

    @Override
    public Boolean getExists(String id) {
        try {
            Template template = getById(id);
            return template.getDone();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Template getByTemplateId(String templateId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Template> criteriaQuery = criteriaBuilder.createQuery(Template.class);
        Root<Template> root = criteriaQuery.from(Template.class);
        criteriaQuery.select(root);
        List<Predicate> predicates = new ArrayList<>();
        Predicate id = criteriaBuilder.equal(root.get("templateId"), templateId);
        predicates.add(id);
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    public Template saveTemplate(Template template) {
        return this.saveAndFlush(template);
    }

    @Override
    public Template update(String id) {
        Template template = getById(id);
        template.setDone(true);
        return this.saveAndFlush(template);
    }

    /*@Override
    public MDMTemplate getMDMTemplateById(String templateId) {
        CriteriaBuilder criteriaBuilder=entityManager.getCriteriaBuilder();
        CriteriaQuery<MDMTemplate> criteriaQuery=criteriaBuilder.createQuery(MDMTemplate.class);
        Root<MDMTemplate> root=criteriaQuery.from(MDMTemplate.class);
        criteriaQuery.select(root);
        List<Predicate> predicates=new ArrayList<>();
        Predicate id = criteriaBuilder.equal(root.get("templateId"),templateId);
        predicates.add(id);
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    public Boolean getExists(String templateId){
        if(getByReference(templateId) != null){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public MDMTemplate saveTemplate(MDMTemplate mdmTemplate) {
        return this.save(mdmTemplate);
    }

    @Override
    public MDMTemplate updateTemplate(MDMTemplate mdmTemplate) {
        return this.saveAndFlush(mdmTemplate);
    }

    @Override
    public List<MDMTemplate> getSearchMDMTemplate(MDMTemplateResource mdmTemplateResource) {
        CriteriaBuilder criteriaBuilder=entityManager.getCriteriaBuilder();
        CriteriaQuery<MDMTemplate> criteriaQuery=criteriaBuilder.createQuery(MDMTemplate.class);
        Root<MDMTemplate> root=criteriaQuery.from(MDMTemplate.class);
        criteriaQuery.select(root);

        List<Predicate> predicates=new ArrayList<>();
        String templateId = mdmTemplateResource.getTemplateId();
        if(templateId != null && !templateId.isEmpty()){
            predicates.add(root.get("templateId").in(templateId));
        }

        String companyID = mdmTemplateResource.getCompanyID();
        if(companyID != null && !companyID.isEmpty()){
            predicates.add(root.get("companyID").in(companyID));
        }
        String templateType = mdmTemplateResource.getTemplateType();
        if(templateId != null && !templateType.isEmpty()){
            predicates.add(root.get("templateType").in(templateType));
        }
        String subTypeForm  = mdmTemplateResource.getSubtypeForm();
        if(subTypeForm!= null && !subTypeForm.isEmpty()){
            predicates.add(root.get("subTypeForm").in(subTypeForm));
        }
        String templateName = mdmTemplateResource.getTemplateName();
        if(templateName != null && !templateName.isEmpty()){
            predicates.add(root.get("templateName").in(templateName));
        }
        String pos = mdmTemplateResource.getPos();
        if(pos != null &&  !pos.isEmpty()){
            predicates.add(root.get("pos").in(pos));
        }
        String createdBy = mdmTemplateResource.getCreatedBy();
        if(createdBy != null && !createdBy.isEmpty()){
            predicates.add(root.get("createdBy").in(createdBy));
        }
        ZonedDateTime createdAt = mdmTemplateResource.getCreatedAt();
        if(createdAt != null){
            predicates.add(root.get("createdAt").in(createdAt));
        }
        String updatedBy = mdmTemplateResource.getUpdatedBy();
        if(updatedBy != null && !updatedBy.isEmpty()){
            predicates.add(root.get("updatedBy").in(updatedBy));
        }
        ZonedDateTime lastUpdated = mdmTemplateResource.getLastUpdated();
        if(lastUpdated != null){
            predicates.add(root.get("lastUpdated").in(lastUpdated));
        }
        Boolean deleted = mdmTemplateResource.getDeleted();
        if(deleted != null){
            predicates.add(root.get("deleted").in(deleted));
        }

        if(!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public MDMTemplate getByReference(String id) {
        CriteriaBuilder criteriaBuilder=entityManager.getCriteriaBuilder();
        CriteriaQuery<MDMTemplate> criteriaQuery=criteriaBuilder.createQuery(MDMTemplate.class);
        Root<MDMTemplate> root=criteriaQuery.from(MDMTemplate.class);
        criteriaQuery.select(root);
        List<Predicate> predicates=new ArrayList<>();
        predicates.add(criteriaBuilder.equal(root.get("uniqueReferenceNumber"),id));
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }*/
}
