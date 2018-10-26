package com.coxandkings.travel.operations.repository.forex.impl;

import com.coxandkings.travel.operations.model.forex.DisbursementDetails;
import com.coxandkings.travel.operations.repository.forex.ForexDisbursementRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Transactional
@Repository
public class ForexDisbursementRepositoryImpl extends SimpleJpaRepository<DisbursementDetails, String> implements ForexDisbursementRepository {

    private EntityManager entityManager;

    public ForexDisbursementRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(DisbursementDetails.class, em);
        entityManager = em;
    }

    @Override
    public Integer getDisbursementStatusCount(String status) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DisbursementDetails> criteriaQuery = criteriaBuilder.createQuery(DisbursementDetails.class);

        Root<DisbursementDetails> root = criteriaQuery.from(DisbursementDetails.class);
        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.equal(criteriaBuilder.lower(root.get("status")), status.toLowerCase()));
        return entityManager.createQuery(criteriaQuery).getResultList().size();
    }
}
