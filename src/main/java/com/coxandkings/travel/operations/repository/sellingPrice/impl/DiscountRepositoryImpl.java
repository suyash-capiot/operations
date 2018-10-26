package com.coxandkings.travel.operations.repository.sellingPrice.impl;

import com.coxandkings.travel.operations.model.sellingPrice.Discount;
import com.coxandkings.travel.operations.repository.sellingPrice.DiscountRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DiscountRepositoryImpl extends SimpleJpaRepository<Discount, String> implements DiscountRepository {
    EntityManager entityManager;

    public DiscountRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(Discount.class, em);
        entityManager = em;
    }

    @Override
    @Transactional
    public Discount createDiscount(Discount discount) {
        return saveAndFlush(discount);
    }

    @Override
    public void deleteDiscount(String id) {
        delete(id);
    }

    @Override
    public Discount getDiscount(String id) {
        return findOne(id);
    }

    @Override
    public List<Discount> getAllDiscounts(String bookingRefId, String productId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Discount> criteriaQuery = criteriaBuilder.createQuery(Discount.class);
        Root<Discount> root = criteriaQuery.from(Discount.class);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(criteriaBuilder.equal(root.get("bookingRefId"), bookingRefId));
        predicates.add(criteriaBuilder.equal(root.get("orderId"), productId));

        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
