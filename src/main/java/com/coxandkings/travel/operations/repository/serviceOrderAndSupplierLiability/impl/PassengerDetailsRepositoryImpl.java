package com.coxandkings.travel.operations.repository.serviceOrderAndSupplierLiability.impl;

import com.coxandkings.travel.operations.criteria.serviceOrderAndSupplierLiability.PassengerDetailsCriteria;
import com.coxandkings.travel.operations.model.serviceOrderAndSupplierLiability.PassengersDetails;
import com.coxandkings.travel.operations.repository.serviceOrderAndSupplierLiability.PassengerDetailsRepository;
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
@Transactional
public class PassengerDetailsRepositoryImpl extends SimpleJpaRepository<PassengersDetails, String> implements PassengerDetailsRepository {

    private EntityManager entityManager;

    PassengerDetailsRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(PassengersDetails.class, em);
        this.entityManager = em;
    }

    @Override
    public PassengersDetails getPassengerDetails(PassengerDetailsCriteria criteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PassengersDetails> criteriaQuery = criteriaBuilder.createQuery(PassengersDetails.class);
        Root<PassengersDetails> root = criteriaQuery.from(PassengersDetails.class);
        criteriaQuery.select(root);
        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getId() != null)
            predicates.add(criteriaBuilder.equal(root.get("id"), criteria.getId()));

        if (criteria.getPassengerType() != null)
            predicates.add(criteriaBuilder.equal(root.get("passengerType"), criteria.getPassengerType()));

        if (criteria.getRatePerPassenger() != null)
            predicates.add(criteriaBuilder.equal(root.get("ratePerPassenger"), criteria.getRatePerPassenger()));

        if (criteria.getNoOfPassenger() != null)
            predicates.add(criteriaBuilder.equal(root.get("noOfPassengers"), criteria.getNoOfPassenger()));

        if (criteria.getSupplierCostPrice() != null)
            predicates.add(criteriaBuilder.equal(root.get("supplierCostPrice"), criteria.getSupplierCostPrice()));

        PassengersDetails details = null;
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        try {
            details = entityManager.createQuery(criteriaQuery).getSingleResult();
            return details;
        }
        catch (Exception e){
            return null;
        }
    }
}
