package com.coxandkings.travel.operations.repository.productbookedthrother.impl;

import com.coxandkings.travel.operations.criteria.productbookedthrother.ProductBookedThrOtherCriteria;
import com.coxandkings.travel.operations.model.productbookedthrother.Attribute;
import com.coxandkings.travel.operations.model.productbookedthrother.Train;
import com.coxandkings.travel.operations.repository.productbookedthrother.TrainRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository(value = "TrainRepositoryImpl")
public class TrainRepositoryImpl extends SimpleJpaRepository<Train,String> implements TrainRepository {

    private EntityManager entityManager;

    public TrainRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(Train.class, em);
        entityManager = em;
    }


    @Override
    @Transactional
    public Train saveOrUpdateTrain(Train train) {
        return saveAndFlush(train);
    }

    @Override
    public Boolean isTrainExists(String id) {
        return exists(id);
    }

    @Override
    public Train getTrainById(String id) {
        return findOne(id);
    }

    @Override
    public Train getTrainByCriteria(ProductBookedThrOtherCriteria criteria) {

        Train train = null;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Train> criteriaQuery = criteriaBuilder.createQuery(Train.class);
        Root<Train> root = criteriaQuery.from(Train.class);
        criteriaQuery.select(root);

        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getBookingRefId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("bookingRefId"), criteria.getBookingRefId()));
        }

        if (criteria.getOrderId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("orderId"), criteria.getOrderId()));
        }

        if (criteria.getProductCategorySubTypeValue() != null) {
            predicates.add(criteriaBuilder.equal(root.get("productCategorySubType"), criteria.getProductCategorySubTypeValue()));
        }

        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }

        try {
            train = (Train) entityManager.createQuery(criteriaQuery).getSingleResult();
            return train;
        } catch (NoResultException e) {
            return train;
        }
    }
}
