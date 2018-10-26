package com.coxandkings.travel.operations.repository.mailroomanddispatch.Impl;

import com.coxandkings.travel.operations.model.mailroomanddispatch.InboundLogEntryStatus;
import com.coxandkings.travel.operations.repository.mailroomanddispatch.InboundEntryStatusRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
@Transactional
public class InboundEntryStatusRepositoryImpl extends SimpleJpaRepository<InboundLogEntryStatus, String> implements InboundEntryStatusRepository {
    private EntityManager entityManager;
    private static final Logger logger = LogManager.getLogger(InboundEntryRepositoryImpl.class);

    public InboundEntryStatusRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(InboundLogEntryStatus.class, em);
        entityManager = em;
    }

    @Override
    public InboundLogEntryStatus saveOrUpdate(InboundLogEntryStatus inboundLogEntryStatus) {
        return this.saveAndFlush(inboundLogEntryStatus);
    }
}
