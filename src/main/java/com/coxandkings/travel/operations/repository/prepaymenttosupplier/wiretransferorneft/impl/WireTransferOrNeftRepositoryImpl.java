package com.coxandkings.travel.operations.repository.prepaymenttosupplier.wiretransferorneft.impl;

import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentdetails.WireTransferOrNeft;
import com.coxandkings.travel.operations.repository.prepaymenttosupplier.wiretransferorneft.WireTransferOrNeftRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository(value = "WireTransferOrNeftRepositoryImpl")
public class WireTransferOrNeftRepositoryImpl extends SimpleJpaRepository<WireTransferOrNeft, String> implements WireTransferOrNeftRepository {

    private EntityManager entityManager;

    public WireTransferOrNeftRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(WireTransferOrNeft.class, em);
        entityManager = em;
    }


    @Override
    @Transactional
    public WireTransferOrNeft saveWireTransferOrNeft(WireTransferOrNeft wireTransferOrNeft) {
        return this.saveAndFlush(wireTransferOrNeft);
    }
}
