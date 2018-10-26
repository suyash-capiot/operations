package com.coxandkings.travel.operations.repository.mailroomanddispatch.Impl;

import com.coxandkings.travel.operations.model.mailroomanddispatch.BarCode;
import com.coxandkings.travel.operations.repository.mailroomanddispatch.BarCodeRepository;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Transactional(readOnly=false)
@Repository
public class BarCodeRepositoryImpl extends SimpleJpaRepository<BarCode, String> implements BarCodeRepository {


    private EntityManager entityManager;

    private static final Logger logger = LogManager.getLogger(BarCodeRepositoryImpl.class);

    public BarCodeRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(BarCode.class, em);
        entityManager = em;
    }

    public BarCode saveOrUpdate(BarCode barCode) {
        return saveAndFlush(barCode);
    }

    public BarCode getBarCodeId(String id) {
        return this.findOne(id);
    }
}
