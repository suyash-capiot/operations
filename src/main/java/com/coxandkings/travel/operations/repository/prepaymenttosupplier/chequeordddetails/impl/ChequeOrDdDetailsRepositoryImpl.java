package com.coxandkings.travel.operations.repository.prepaymenttosupplier.chequeordddetails.impl;

import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentdetails.ChequeOrDdDetails;
import com.coxandkings.travel.operations.repository.prepaymenttosupplier.chequeordddetails.ChequeOrDdDetailsRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository(value = "ChequeOrDdDetailsRepositoryImpl")
public class ChequeOrDdDetailsRepositoryImpl extends SimpleJpaRepository<ChequeOrDdDetails, String> implements ChequeOrDdDetailsRepository {

    private EntityManager entityManager;

    public ChequeOrDdDetailsRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(ChequeOrDdDetails.class, em);
        entityManager = em;
    }

    @Override
    @Transactional
    public ChequeOrDdDetails saveChequeOrDdDetails(ChequeOrDdDetails chequeOrDdDetails) {
        return this.saveAndFlush(chequeOrDdDetails);
    }
}
