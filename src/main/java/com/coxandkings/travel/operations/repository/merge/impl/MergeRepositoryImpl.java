package com.coxandkings.travel.operations.repository.merge.impl;

import com.coxandkings.travel.operations.model.merge.AccommodationBookProduct;
import com.coxandkings.travel.operations.model.merge.BookProduct;
import com.coxandkings.travel.operations.model.merge.Merge;
import com.coxandkings.travel.operations.repository.merge.MergeRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MergeRepositoryImpl extends SimpleJpaRepository<Merge, String> implements MergeRepository {
    EntityManager entityManager;

    public MergeRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(Merge.class, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public Merge getById(String id) {
        return findOne(id);
    }

    @Override
    @Transactional
    public Merge saveOrUpdate(Merge merge) {
        return this.saveAndFlush(merge);
    }

    @Override
    public List<Merge> getPotentialAccommodationMerge(Merge currentMerge) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Merge> mergeCriteriaQuery = criteriaBuilder.createQuery(Merge.class);
        Root<Merge> mergeRoot = mergeCriteriaQuery.from(Merge.class);
        Join<Merge, BookProduct> mergeBookProductJoin = mergeRoot.join("products");
        List<Predicate> predicates = new ArrayList<>();
        List<Merge> result = new ArrayList<>();
        for(BookProduct bookProduct: currentMerge.getProducts()) {
            AccommodationBookProduct accommodationBookProduct = (AccommodationBookProduct) bookProduct;
            Predicate roomCategory = (criteriaBuilder.equal(criteriaBuilder.treat(mergeBookProductJoin, AccommodationBookProduct.class).get("roomCategory"), accommodationBookProduct.getRoomCategory()));
            Predicate roomType = (criteriaBuilder.equal(criteriaBuilder.treat(mergeBookProductJoin, AccommodationBookProduct.class).get("roomType"), accommodationBookProduct.getRoomType()));
            Predicate checkInDate = criteriaBuilder.equal(criteriaBuilder.treat(mergeBookProductJoin, AccommodationBookProduct.class).get("checkInDate"), accommodationBookProduct.getCheckInDate());
            Predicate checkOutDate = criteriaBuilder.equal(criteriaBuilder.treat(mergeBookProductJoin, AccommodationBookProduct.class).get("checkOutDate"), accommodationBookProduct.getCheckOutDate());
            Predicate hotelCode = criteriaBuilder.equal(criteriaBuilder.treat(mergeBookProductJoin, AccommodationBookProduct.class).get("hotelName"), accommodationBookProduct.getHotelName());

            mergeCriteriaQuery.where(roomCategory, roomType, checkInDate, checkOutDate, hotelCode);
            mergeCriteriaQuery.distinct(true);
            result.addAll(entityManager.createQuery(mergeCriteriaQuery).getResultList());
            System.out.println(result);
        }

        return result;
    }

    @Override
    public List<Merge> getPotentialMerges() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Merge> criteriaQuery = criteriaBuilder.createQuery(Merge.class);
        Root<Merge> root = criteriaQuery.from(Merge.class);

//        Predicate countPredicate = criteriaBuilder.greaterThan(root.get("count"), 1);
//        criteriaQuery.where(countPredicate);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
