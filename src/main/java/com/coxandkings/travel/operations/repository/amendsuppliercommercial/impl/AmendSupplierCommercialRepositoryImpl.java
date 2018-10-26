package com.coxandkings.travel.operations.repository.amendsuppliercommercial.impl;

import com.coxandkings.travel.operations.model.amendsuppliercommercial.AmendSupplierCommercial;
import com.coxandkings.travel.operations.repository.amendsuppliercommercial.AmendSupplierCommercialRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
@Repository
public class AmendSupplierCommercialRepositoryImpl extends SimpleJpaRepository<AmendSupplierCommercial, String> implements AmendSupplierCommercialRepository {
    private EntityManager entityManager;

    public AmendSupplierCommercialRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(AmendSupplierCommercial.class, entityManager);
        this.entityManager = entityManager;

    }

    @Override
    public AmendSupplierCommercial saveAmendSupplierCommercial(AmendSupplierCommercial amendSupplierCommercial) {
        return saveAndFlush(amendSupplierCommercial);
    }

    @Override
    public AmendSupplierCommercial getAmendSupplierCommercial(String id) {
        return findOne(id);
    }
}
