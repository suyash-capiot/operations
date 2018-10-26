package com.coxandkings.travel.operations.repository.managearrivallist.impl;


import com.coxandkings.travel.operations.model.managearrivallist.ArrivalListInfo;
import com.coxandkings.travel.operations.repository.managearrivallist.ArrivalListRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository(value = "ArrivalListRepositoryImpl")
public class ArrivalListRepositoryImpl extends SimpleJpaRepository<ArrivalListInfo,String> implements ArrivalListRepository
{


    private EntityManager entityManager;

    public ArrivalListRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em)
    {
        super(ArrivalListInfo.class, em);
        entityManager=em;
    }


    @Override
    @Transactional
    public ArrivalListInfo saveArrivalListConfiguration(ArrivalListInfo arrival) {
        return saveAndFlush(arrival);
    }



}
