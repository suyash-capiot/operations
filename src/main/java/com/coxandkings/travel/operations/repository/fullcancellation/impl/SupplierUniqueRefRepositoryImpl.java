package com.coxandkings.travel.operations.repository.fullcancellation.impl;

import com.coxandkings.travel.operations.model.fullCancellation.SupplierUniqueRef;
import com.coxandkings.travel.operations.repository.fullcancellation.SupplierUniqueRefRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Repository("supplierUnqueRefRepository")
public class SupplierUniqueRefRepositoryImpl extends SimpleJpaRepository<SupplierUniqueRef, String> implements SupplierUniqueRefRepository {
    private EntityManager entityManager;

    public SupplierUniqueRefRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(SupplierUniqueRef.class, entityManager);
        this.entityManager = entityManager;
    }
    @Transactional
    @Override
    public SupplierUniqueRef saveAndUpdate(SupplierUniqueRef supplierUniqueRef) {
        return this.saveAndFlush(supplierUniqueRef);

    }

    @Override
    public SupplierUniqueRef getSupplierUniqueRefById(String id) {
        return this.findOne(id);
    }
}
