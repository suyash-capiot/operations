package com.coxandkings.travel.operations.repository.merge.impl;

import com.coxandkings.travel.operations.enums.merge.MergeStatusValues;
import com.coxandkings.travel.operations.model.merge.MergeStatus;
import com.coxandkings.travel.operations.repository.merge.MergeStatusRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class MergeStatusRepositoryImpl extends SimpleJpaRepository<MergeStatus, MergeStatusValues> implements MergeStatusRepository {

    public MergeStatusRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(MergeStatus.class, entityManager);
    }


    @Override
    public MergeStatus getStatus(MergeStatusValues code) {
        return findOne(code);
    }
}
