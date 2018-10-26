package com.coxandkings.travel.operations.repository.failure.Impl;

import com.coxandkings.travel.operations.model.failure.FailureDetails;
import com.coxandkings.travel.operations.repository.failure.FailureRepository;
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


@Repository(value = "failureDetailsRepository")
@Transactional
public class FailureRepositoryImpl extends SimpleJpaRepository<FailureDetails, String> implements FailureRepository {
    private EntityManager entityManager;

    public FailureRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(FailureDetails.class, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public FailureDetails saveOrUpdate(FailureDetails failureDetails) {
        return this.saveAndFlush(failureDetails);
    }

    @Override
    public FailureDetails getById(String id) {
        return this.findOne(id);
    }

/*    @Override
    public FailureDetails getByBookAndOrderID(String bookID, String orderID){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<FailureDetails> criteriaQuery = criteriaBuilder.createQuery(FailureDetails.class);
        Root<FailureDetails> root = criteriaQuery.from(FailureDetails.class);
        List<Predicate> predicates = new ArrayList<>();

        if(!StringUtils.isEmpty(bookID)){
            predicates.add(criteriaBuilder.equal(root.get("bookID"), bookID));
        } else if(!StringUtils.isEmpty(orderID)){
            predicates.add(criteriaBuilder.equal(root.get("orderID"), orderID));
        }
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }*/

    @Override
    public FailureDetails getByBookID(String bookID) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<FailureDetails> criteriaQuery = criteriaBuilder.createQuery(FailureDetails.class);
        Root<FailureDetails> root = criteriaQuery.from(FailureDetails.class);
        List<Predicate> predicates = new ArrayList<>();

        if (!StringUtils.isEmpty(bookID)) {
            predicates.add(criteriaBuilder.equal(root.get("bookID"), bookID));
        }
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    public List<FailureDetails> getAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<FailureDetails> criteriaQuery = criteriaBuilder.createQuery(FailureDetails.class);
        Root<FailureDetails> root = criteriaQuery.from(FailureDetails.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(root.get("deleted"), false));
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public Boolean getExists(String bookID) {
        try {
            FailureDetails failureDetails = getByBookID(bookID);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
