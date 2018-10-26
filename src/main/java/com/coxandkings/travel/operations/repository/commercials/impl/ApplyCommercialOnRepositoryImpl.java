package com.coxandkings.travel.operations.repository.commercials.impl;

import com.coxandkings.travel.operations.model.commercials.ApplyCommercialOn;
import com.coxandkings.travel.operations.repository.commercials.ApplyCommercialOnRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository("applyClientCommercialOnRepositoryImpl")
public class ApplyCommercialOnRepositoryImpl extends
        SimpleJpaRepository<ApplyCommercialOn, String> implements ApplyCommercialOnRepository {

    private EntityManager entityManager;

    public ApplyCommercialOnRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em){
        super(ApplyCommercialOn.class,em);
        entityManager = em;
    }


    @Override
    public List<ApplyCommercialOn> getAllStatus() {
        return this.findAll();
    }


}
