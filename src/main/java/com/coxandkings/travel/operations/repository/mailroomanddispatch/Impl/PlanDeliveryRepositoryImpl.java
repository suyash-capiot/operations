package com.coxandkings.travel.operations.repository.mailroomanddispatch.Impl;

import com.coxandkings.travel.operations.model.mailroomanddispatch.PlanDelivery;
import com.coxandkings.travel.operations.repository.mailroomanddispatch.PlanDeliveryRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
@Transactional
public class PlanDeliveryRepositoryImpl extends SimpleJpaRepository<PlanDelivery, String> implements PlanDeliveryRepository {

    private EntityManager entityManager;
    private static final Logger logger = LogManager.getLogger(PlanDeliveryRepositoryImpl.class);

    public PlanDeliveryRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(PlanDelivery.class, em);
        entityManager = em;
    }
    @Override
    public PlanDelivery saveOrUpdate(PlanDelivery planDelivery) {
        return saveAndFlush(planDelivery);
    }
}
