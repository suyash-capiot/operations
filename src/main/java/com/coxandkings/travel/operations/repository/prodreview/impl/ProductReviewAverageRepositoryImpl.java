package com.coxandkings.travel.operations.repository.prodreview.impl;

import com.coxandkings.travel.operations.criteria.prodreview.ProductAverageCriteria;
import com.coxandkings.travel.operations.model.prodreview.ProductReviewAverage;
import com.coxandkings.travel.operations.repository.prodreview.ProductReviewAverageRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class ProductReviewAverageRepositoryImpl extends SimpleJpaRepository<ProductReviewAverage, String> implements ProductReviewAverageRepository {

    private EntityManager entityManager;

    public ProductReviewAverageRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(ProductReviewAverage.class, entityManager);
        this.entityManager = entityManager;
    }


    public ProductReviewAverage get(String id) {
        return this.findOne(id);
    }

    @Override
    public List<ProductReviewAverage> getByCriteria(ProductAverageCriteria productAverageCriteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductReviewAverage> criteriaQuery = criteriaBuilder.createQuery(ProductReviewAverage.class);
        Root<ProductReviewAverage> root = criteriaQuery.from(ProductReviewAverage.class);
        criteriaQuery.select(root);
        List<Predicate> predicates = new ArrayList<>();
        if (productAverageCriteria.getProductCategory() != null && !productAverageCriteria.getProductCategory().isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("productCategory"), productAverageCriteria.getProductCategory()));
        }
        if (productAverageCriteria.getProductSubCategory() != null && !productAverageCriteria.getProductSubCategory().isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("productCategorySubType"), productAverageCriteria.getProductSubCategory()));
        }
        if (!StringUtils.isEmpty(productAverageCriteria.getProductName())) {
            predicates.add(criteriaBuilder.equal(root.get("productName"), productAverageCriteria.getProductName()));
        }
        if (!StringUtils.isEmpty(productAverageCriteria.getCompanyID())) {
            predicates.add(criteriaBuilder.equal(root.get("companyID"), productAverageCriteria.getCompanyID()));
        }
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }


    @Override
    public ProductReviewAverage saveAndUpdate(ProductReviewAverage productReviewAverage) {

        return this.saveAndFlush(productReviewAverage);
    }

}

