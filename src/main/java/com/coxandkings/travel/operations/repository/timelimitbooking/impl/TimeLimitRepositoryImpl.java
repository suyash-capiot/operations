package com.coxandkings.travel.operations.repository.timelimitbooking.impl;

import com.coxandkings.travel.operations.model.timelimitbooking.TimeLimitExpiryInfo;
import com.coxandkings.travel.operations.repository.timelimitbooking.TimeLimitRepository;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

@Repository
public class TimeLimitRepositoryImpl extends SimpleJpaRepository<TimeLimitExpiryInfo,String> implements TimeLimitRepository {

    private static final Logger logger = LogManager.getLogger(TimeLimitClientInfoRepositoryImpl.class);
    private EntityManager entityManager;

    public TimeLimitRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(TimeLimitExpiryInfo.class, entityManager);
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public TimeLimitExpiryInfo saveExpiryInfo(TimeLimitExpiryInfo timeLimitExpiryInfo) {
        logger.debug("\n saving the TimeExpiryInfo record");
        return this.saveAndFlush(timeLimitExpiryInfo);
    }

    @Override
    public TimeLimitExpiryInfo getExpiryInfo(String orderId) {
        logger.debug("\n getting expiryInfo by orderId ");
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TimeLimitExpiryInfo> criteriaQuery = criteriaBuilder.createQuery(TimeLimitExpiryInfo.class);
        Root<TimeLimitExpiryInfo> root = criteriaQuery.from(TimeLimitExpiryInfo.class);
        criteriaQuery.where(criteriaBuilder.equal(root.get("orderId"), orderId));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }
}
