package com.coxandkings.travel.operations.repository.refund.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.refund.ChangeType;
import com.coxandkings.travel.operations.repository.refund.ChangeTypeRefundRepository;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
public class ChangeTypeRefundRepositoryImpl extends SimpleJpaRepository<ChangeType, String> implements ChangeTypeRefundRepository {

    private static Logger logger = LogManager.getLogger(ChangeTypeRefundRepositoryImpl.class);

    public ChangeTypeRefundRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(ChangeType.class, entityManager);

    }

    @Override
    @Transactional
    public ChangeType saveAndUpdateChangeType(ChangeType changeType) throws OperationException {

        ChangeType changeType1 = null;
        try {
            changeType1 = this.saveAndFlush(changeType);
            logger.info("refund Type change with value " + changeType1);
        } catch (Exception e) {
            logger.error("Unable to save refund mapping", e);
             throw new OperationException("Unable to save refund mapping");

        }
        return changeType1;
    }

    @Override
    public ChangeType getChangeTypeByClaimNo(String claimNo) {
        return this.findOne(claimNo);
    }


}
