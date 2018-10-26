package com.coxandkings.travel.operations.repository.prodreview.impl;

import com.coxandkings.travel.operations.model.prodreview.ProductReviewTemplateReference;
import com.coxandkings.travel.operations.repository.prodreview.TemplateReferenceRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
@Transactional
public class TemplateReferenceRepositoryImpl extends SimpleJpaRepository<ProductReviewTemplateReference, String> implements TemplateReferenceRepository {
    private final EntityManager entityManager;

    public TemplateReferenceRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(ProductReviewTemplateReference.class, entityManager);
        this.entityManager = entityManager;

    }

    @Override
    public ProductReviewTemplateReference saveOrUpdate(ProductReviewTemplateReference productReviewTemplateReference) {
        ProductReviewTemplateReference productReviewTemplateReference1 = this.saveAndFlush(productReviewTemplateReference);
        return productReviewTemplateReference1;
    }

    @Override
    public ProductReviewTemplateReference getTemplateReferenceById(String bookingReferenceNo) {
        return this.findOne(bookingReferenceNo);
    }
}
