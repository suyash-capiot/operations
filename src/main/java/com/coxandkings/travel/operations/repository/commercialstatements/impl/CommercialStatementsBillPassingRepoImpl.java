package com.coxandkings.travel.operations.repository.commercialstatements.impl;

import com.coxandkings.travel.operations.model.commercialstatements.CommercialStatementsBillPassing;
import com.coxandkings.travel.operations.repository.commercialstatements.CommercialStatementsBillPassingRepo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
@Transactional
public class CommercialStatementsBillPassingRepoImpl extends SimpleJpaRepository<CommercialStatementsBillPassing,String> implements CommercialStatementsBillPassingRepo {

    public CommercialStatementsBillPassingRepoImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(CommercialStatementsBillPassing.class, entityManager);
    }

    @Override
    public CommercialStatementsBillPassing add(CommercialStatementsBillPassing commercialStatementsBillPassing) {
        return this.save(commercialStatementsBillPassing);
    }

    @Override
    public CommercialStatementsBillPassing update(CommercialStatementsBillPassing commercialStatementsBillPassing) {
        return this.saveAndFlush(commercialStatementsBillPassing);
    }

    @Override
    public CommercialStatementsBillPassing get(String id) {
        return this.findOne(id);
    }
}
