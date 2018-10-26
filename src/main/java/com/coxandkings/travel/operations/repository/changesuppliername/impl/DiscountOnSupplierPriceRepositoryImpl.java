package com.coxandkings.travel.operations.repository.changesuppliername.impl;

import com.coxandkings.travel.operations.model.changesuppliername.DiscountOnSupplierPrice;
import com.coxandkings.travel.operations.repository.changesuppliername.DiscountOnSupplierPriceRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Repository("discountOnSupplierPriceRepositoryImpl")
public class DiscountOnSupplierPriceRepositoryImpl extends SimpleJpaRepository<DiscountOnSupplierPrice, String> implements DiscountOnSupplierPriceRepository {
    private EntityManager em;

    public DiscountOnSupplierPriceRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(DiscountOnSupplierPrice.class, entityManager);
        this.em = entityManager;
    }

    @Override
    @Transactional
    public DiscountOnSupplierPrice saveDiscountOnSupplierPrice(DiscountOnSupplierPrice discountOnSupplierPrice) {

        return save(discountOnSupplierPrice);
    }

    @Override
    public DiscountOnSupplierPrice getDiscountOnSupplierPriceById(String referenceId) {
        return findOne(referenceId);
    }


}
