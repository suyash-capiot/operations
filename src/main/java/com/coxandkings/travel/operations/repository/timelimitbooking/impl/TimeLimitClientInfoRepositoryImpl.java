package com.coxandkings.travel.operations.repository.timelimitbooking.impl;

import com.coxandkings.travel.operations.model.timelimitbooking.TLBackgroundInfo;
import com.coxandkings.travel.operations.repository.timelimitbooking.TimeLimitClientInfoRepository;
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
public class TimeLimitClientInfoRepositoryImpl extends SimpleJpaRepository<TLBackgroundInfo, String> implements TimeLimitClientInfoRepository {

    private static final Logger logger = LogManager.getLogger(TimeLimitClientInfoRepositoryImpl.class);
    private EntityManager entityManager;

    public TimeLimitClientInfoRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(TLBackgroundInfo.class, entityManager);
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public TLBackgroundInfo saveClientbackgroundInfo(TLBackgroundInfo tlBackgroundInfo) {
        logger.debug("\n saving the backgroundinfo record");
        return this.saveAndFlush(tlBackgroundInfo);
    }


    @Override
    public TLBackgroundInfo getClientBackgroundInfoClientId(String clientId) {
        logger.debug("\n getting ClientBackgroundInfoByClientId record");
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TLBackgroundInfo> criteriaQuery = criteriaBuilder.createQuery(TLBackgroundInfo.class);
        Root<TLBackgroundInfo> root = criteriaQuery.from(TLBackgroundInfo.class);
        criteriaQuery.where(criteriaBuilder.equal(root.get("clientId"), clientId));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    public TLBackgroundInfo getClientBackgroundInfoByBookId(String bookId) {
        logger.debug("\n getting ClientBackgroundInfoByBookId record");
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TLBackgroundInfo> criteriaQuery = criteriaBuilder.createQuery(TLBackgroundInfo.class);
        Root<TLBackgroundInfo> root = criteriaQuery.from(TLBackgroundInfo.class);
        criteriaQuery.where(criteriaBuilder.equal(root.get("bookId"), bookId));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    public boolean isClientBackgroundInfoExists(String bookId)
    {
        return this.exists(bookId);
    }




}
