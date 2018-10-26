package com.coxandkings.travel.operations.repository.merge.impl;

import com.coxandkings.travel.operations.model.merge.AccommodationBookProduct;
import com.coxandkings.travel.operations.repository.merge.AccommodationBookProductRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;

public class AccommodationBookProductRepositoryImpl extends SimpleJpaRepository<AccommodationBookProduct, String> implements AccommodationBookProductRepository {
    EntityManager entityManager;

    public AccommodationBookProductRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(AccommodationBookProduct.class, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public AccommodationBookProduct saveOrUpdate(AccommodationBookProduct accommodationBookProduct) {
        return saveAndFlush(accommodationBookProduct);
    }

    @Override
    public AccommodationBookProduct getById(String id) {
        return findOne(id);
    }
}
