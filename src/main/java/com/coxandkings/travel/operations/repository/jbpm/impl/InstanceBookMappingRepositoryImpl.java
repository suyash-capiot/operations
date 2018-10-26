package com.coxandkings.travel.operations.repository.jbpm.impl;

import com.coxandkings.travel.operations.criteria.jbpm.InstanceBookMappingCriteria;
import com.coxandkings.travel.operations.enums.Status;
import com.coxandkings.travel.operations.enums.WorkFlow;
import com.coxandkings.travel.operations.model.jbpm.InstanceBookMapping;
import com.coxandkings.travel.operations.repository.jbpm.InstanceBookMappingRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class InstanceBookMappingRepositoryImpl extends SimpleJpaRepository<InstanceBookMapping, String> implements InstanceBookMappingRepository {

    private EntityManager em;

    public InstanceBookMappingRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(InstanceBookMapping.class, em);
        this.em = em;
    }

    @Override
    public List<InstanceBookMapping> getByCriteria(InstanceBookMappingCriteria criteria) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<InstanceBookMapping> criteriaQuery=criteriaBuilder.createQuery(InstanceBookMapping.class);
        Root<InstanceBookMapping> root=criteriaQuery.from(InstanceBookMapping.class);
        criteriaQuery.select(root);
        List<Predicate> predicates = new ArrayList<>();
        String[] ids = criteria.getIds();
        if(ids !=null && ids.length > 0) {
            Predicate id = root.get("id").in(ids);
            predicates.add(id);
        }

        String[] excludeIds = criteria.getExcludeIds();
        if(excludeIds !=null && excludeIds.length > 0) {
            Predicate id = criteriaBuilder.not(root.get("id").in(ids));
            predicates.add(id);
        }

        String[] bookingRefIds = criteria.getBookingRefIds();
        if(bookingRefIds != null && bookingRefIds.length > 0 ) {
            predicates.add(root.get("bookingRefId").in(bookingRefIds));
        }

        WorkFlow workFlow = criteria.getWorkFlow();
        if(workFlow != null) {
            predicates.add(criteriaBuilder.equal(root.get("workFlow"), workFlow));
        }

        Status status = criteria.getStatus();
        if(status != null) {
            predicates.add(criteriaBuilder.equal(root.get("status"), status));
        }

        String entityRefId=criteria.getEntityRefId();
        if(!(StringUtils.isEmpty(entityRefId))) {
            predicates.add(criteriaBuilder.equal(root.get("entityRefId"), entityRefId));
        }

        String userTaskName=criteria.getUserTaskName();
        if(!(StringUtils.isEmpty(userTaskName))) {
            predicates.add(criteriaBuilder.equal(root.get("userTaskName"), userTaskName));
        }

        if(!CollectionUtils.isEmpty(predicates)) {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }

        return em.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public InstanceBookMapping getById(String id) {
        return this.findOne(id);
    }

    @Override
    public InstanceBookMapping saveOrUpdate(InstanceBookMapping instanceBookMapping) {
        return this.saveAndFlush(instanceBookMapping);
    }

    @Override
    @Transactional
    public void remove(String id) {
        this.delete(id);
        this.flush();
    }
}
