package com.coxandkings.travel.operations.repository.serviceOrderAndSupplierLiability.impl;

import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.DuplicateBookings;
import com.coxandkings.travel.operations.repository.serviceOrderAndSupplierLiability.DuplicateBookingsRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
@Transactional
public class DuplicateBookingsRepositoryImpl extends SimpleJpaRepository<DuplicateBookings,String> implements DuplicateBookingsRepository {

    private EntityManager entityManager;

    DuplicateBookingsRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(DuplicateBookings.class, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public DuplicateBookings saveDuplicateBookings(DuplicateBookings duplicateBookings) {
        return this.saveAndFlush(duplicateBookings);
    }

    @Override
    public DuplicateBookings getDuplicateBookingsById(String id) {
        return this.findOne(id);
    }
}
