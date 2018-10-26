package com.coxandkings.travel.operations.repository.refund.impl;

import com.coxandkings.travel.operations.model.refund.RefundData;
import com.coxandkings.travel.operations.repository.refund.RefundDataRepository;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
public class RefundDataRepositoryImpl extends SimpleJpaRepository<RefundData, String> implements RefundDataRepository {
    private static Logger logger = LogManager.getLogger(RefundDataRepositoryImpl.class);

    public RefundDataRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(RefundData.class, entityManager);
    }

    @Override
    @Transactional
    public void saveOrUpdate(RefundData refundData) {
        this.saveAndFlush(refundData);
    }

    @Override
    public RefundData getRefund(String claimNo) {
        RefundData refundData = null;
        try {
            refundData = this.findOne(claimNo);
        } catch (Exception e) {
            logger.error("Unable to fetch refund Claim for claim no: " + claimNo, e);


        }
        return refundData;
    }
}
