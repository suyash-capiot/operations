package com.coxandkings.travel.operations.repository.mailroomanddispatch.Impl;

import com.coxandkings.travel.operations.criteria.mailroomanddispatch.DispatchStatusCriteria;
import com.coxandkings.travel.operations.enums.mailroomanddispatch.DispatchStatus;
import com.coxandkings.travel.operations.model.mailroomanddispatch.OutboundStatus;
import com.coxandkings.travel.operations.repository.mailroomanddispatch.DispatchStatusRepository;
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

@Transactional(readOnly=false)
@Repository
public class DispatchStatusRepositoryImpl extends SimpleJpaRepository<OutboundStatus, String> implements DispatchStatusRepository {

    EntityManager entityManager;

    public DispatchStatusRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(OutboundStatus.class, em);
        entityManager = em;
    }

    public OutboundStatus saveOrUpdate(OutboundStatus dispatchStatus) {

        return this.saveAndFlush(dispatchStatus);
    }

    @Override
    public OutboundStatus getById(String id) {

        return findOne(id);
    }

    @Override
    public List<OutboundStatus> getByCriteria(DispatchStatusCriteria criteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OutboundStatus> query = criteriaBuilder.createQuery(OutboundStatus.class);
        Root<OutboundStatus> root = query.from(OutboundStatus.class);

        List<Predicate> predicates = new ArrayList<>();

        String[] ids = criteria.getIds();
        if (ids != null && ids.length > 0) {
            predicates.add(root.get("id").in(ids));
        }

        String[] excludeIds = criteria.getExcludeIds();
        if (excludeIds != null && excludeIds.length > 0) {
            predicates.add(criteriaBuilder.not(root.get("id").in(ids)));
        }

        DispatchStatus code = criteria.getCode();
        if (!StringUtils.isEmpty(code)) {
            predicates.add(criteriaBuilder.equal(root.get("code"), code));
        }
        query.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(query).getResultList();
    }

}
