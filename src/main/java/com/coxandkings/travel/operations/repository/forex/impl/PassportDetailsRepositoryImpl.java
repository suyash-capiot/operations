package com.coxandkings.travel.operations.repository.forex.impl;

import com.coxandkings.travel.operations.model.forex.PassportDetails;
import com.coxandkings.travel.operations.repository.forex.PassportDetailsRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Transactional
@Repository
public class PassportDetailsRepositoryImpl extends SimpleJpaRepository<PassportDetails, String> implements PassportDetailsRepository {

    private EntityManager entityManager;

    public PassportDetailsRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(PassportDetails.class, em);
        entityManager = em;
    }

    @Transactional
    public PassportDetails saveOrUpdate(PassportDetails passportDetails) {
        return saveAndFlush(passportDetails);
    }

    public PassportDetails getById(String id) {
        return findOne(id);
    }
}
