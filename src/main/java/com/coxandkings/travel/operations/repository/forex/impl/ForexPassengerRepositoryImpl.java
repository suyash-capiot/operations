package com.coxandkings.travel.operations.repository.forex.impl;

import com.coxandkings.travel.operations.model.forex.ForexPassenger;
import com.coxandkings.travel.operations.repository.forex.ForexPassengerRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Transactional
@Repository
public class ForexPassengerRepositoryImpl extends SimpleJpaRepository<ForexPassenger, String> implements ForexPassengerRepository {

    private EntityManager entityManager;

    public ForexPassengerRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(ForexPassenger.class, em);
        entityManager = em;
    }

    @Transactional
    public ForexPassenger saveOrUpdate(ForexPassenger forexPassenger) {
        return saveAndFlush(forexPassenger);
    }

    public ForexPassenger getById(String id) {
        return findOne(id);
    }


}
