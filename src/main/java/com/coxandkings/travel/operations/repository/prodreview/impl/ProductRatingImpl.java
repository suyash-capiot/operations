package com.coxandkings.travel.operations.repository.prodreview.impl;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.prodreview.ProductRating;
import com.coxandkings.travel.operations.repository.prodreview.ProductRatingRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository("productRatingRepository")
@Transactional
public class ProductRatingImpl extends SimpleJpaRepository<ProductRating, String> implements ProductRatingRepository {

    private final EntityManager entityManager;

    public ProductRatingImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(ProductRating.class, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public ProductRating saveOrUpdate(ProductRating productRating) throws OperationException {
        if (getProductId(productRating.getProductId()) == null) {
            return this.save(productRating);
        } else {
            return this.saveAndFlush(productRating);
        }

    }

    @Override
    public ProductRating getProductId(String productId) throws OperationException {
        return this.findOne(productId);
    }
}
