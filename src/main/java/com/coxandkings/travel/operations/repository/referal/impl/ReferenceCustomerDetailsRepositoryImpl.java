package com.coxandkings.travel.operations.repository.referal.impl;

import com.coxandkings.travel.operations.model.referal.ReferenceCustomerDetails;
import com.coxandkings.travel.operations.repository.referal.ReferenceCustomerDetailsRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Repository
public class ReferenceCustomerDetailsRepositoryImpl extends SimpleJpaRepository<ReferenceCustomerDetails, String> implements ReferenceCustomerDetailsRepository {

    private EntityManager entityManager;

    public ReferenceCustomerDetailsRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(ReferenceCustomerDetails.class, entityManager);
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public ReferenceCustomerDetails saveOrUpdateReference(ReferenceCustomerDetails referenceCustomerDetails) {
        return this.saveAndFlush(referenceCustomerDetails);
    }

    @Override
    public ReferenceCustomerDetails getReferenceById(String bookID) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        criteriaBuilder.createQuery(ReferenceCustomerDetails.class);
        CriteriaQuery<ReferenceCustomerDetails> referenceCustomerDetailsCriteriaQuery = null;
        Root<ReferenceCustomerDetails> root = null;
        TypedQuery<ReferenceCustomerDetails> query = null;
        try {
            if (entityManager != null) {
                criteriaBuilder = this.entityManager.getCriteriaBuilder();
                if (criteriaBuilder != null) {
                    referenceCustomerDetailsCriteriaQuery = criteriaBuilder.createQuery(ReferenceCustomerDetails.class);
                    root = referenceCustomerDetailsCriteriaQuery.from(ReferenceCustomerDetails.class);
                    Predicate bookRefNoP = criteriaBuilder.equal(root.get("bookID"), bookID);
                    referenceCustomerDetailsCriteriaQuery.select(root).where(criteriaBuilder.and(bookRefNoP));
                }
            }
            query = this.entityManager.createQuery(referenceCustomerDetailsCriteriaQuery);
            if (query != null) {
                if (query.getSingleResult() != null) {
                    return query.getSingleResult();
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (NonUniqueResultException e) {
            e.printStackTrace();
            return query.getResultList().get(0);
        } catch (Exception e) {
            ReferenceCustomerDetails details = new ReferenceCustomerDetails();
            details.setBookID(bookID);
            details.setStatus("NEW");
            return details;
        }
    }
}