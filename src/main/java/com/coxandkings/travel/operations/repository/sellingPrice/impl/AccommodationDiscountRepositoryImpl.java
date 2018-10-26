package com.coxandkings.travel.operations.repository.sellingPrice.impl;

import com.coxandkings.travel.operations.model.sellingPrice.AccommodationDiscount;
import com.coxandkings.travel.operations.repository.sellingPrice.AccommodationDiscountRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AccommodationDiscountRepositoryImpl extends SimpleJpaRepository<AccommodationDiscount, String> implements AccommodationDiscountRepository {
    EntityManager entityManager;

    public AccommodationDiscountRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(AccommodationDiscount.class, em);
        entityManager = em;
    }

    @Override
    public AccommodationDiscount createDiscount(AccommodationDiscount accommodationDiscount) {
        accommodationDiscount = saveAndFlush(accommodationDiscount);
        return accommodationDiscount;
    }

    @Override
    public List<AccommodationDiscount> getAllDiscounts(String bookingRefId, String orderId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<AccommodationDiscount> criteriaQuery = criteriaBuilder.createQuery(AccommodationDiscount.class);
        Root<AccommodationDiscount> root = criteriaQuery.from(AccommodationDiscount.class);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(criteriaBuilder.equal(root.get("bookingRefId"), bookingRefId));
        predicates.add(criteriaBuilder.equal(root.get("orderId"), orderId));

        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
