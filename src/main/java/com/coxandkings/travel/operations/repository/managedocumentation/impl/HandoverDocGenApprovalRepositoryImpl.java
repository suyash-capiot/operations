package com.coxandkings.travel.operations.repository.managedocumentation.impl;

import com.coxandkings.travel.operations.criteria.managedocumentation.HandoverDocGenSearchCriteria;
import com.coxandkings.travel.operations.model.managedocumentation.HandoverDocGenApprovalDetails;
import com.coxandkings.travel.operations.repository.managedocumentation.HandoverDocGenApprovalRepository;
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
public class HandoverDocGenApprovalRepositoryImpl extends SimpleJpaRepository<HandoverDocGenApprovalDetails, String> implements HandoverDocGenApprovalRepository {

    private EntityManager entityManager;

    HandoverDocGenApprovalRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(HandoverDocGenApprovalDetails.class, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public HandoverDocGenApprovalDetails saveDetails(HandoverDocGenApprovalDetails handoverDocGenApprovalDetails) {
        return this.saveAndFlush(handoverDocGenApprovalDetails);
    }

    @Override
    public HandoverDocGenApprovalDetails getDetailsById(String id) {
        return this.findOne(id);
    }

    @Override
    public List<HandoverDocGenApprovalDetails> searchDetails(HandoverDocGenSearchCriteria searchCriteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<HandoverDocGenApprovalDetails> criteriaQuery = criteriaBuilder.createQuery(HandoverDocGenApprovalDetails.class);
        Root<HandoverDocGenApprovalDetails> root = criteriaQuery.from(HandoverDocGenApprovalDetails.class);
        criteriaQuery.select(root);
        List<Predicate> predicates = new ArrayList<>();
        if (searchCriteria.getId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("id"), searchCriteria.getId()));
        }
        if (searchCriteria.getBookID() != null) {
            predicates.add(criteriaBuilder.equal(root.get("bookID"), searchCriteria.getBookID()));
        }
        if (searchCriteria.getOrderId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("orderId"), searchCriteria.getOrderId()));
        }
        if (searchCriteria.getPaxId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("paxId"), searchCriteria.getPaxId()));
        }
        if (searchCriteria.getRoomId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("roomId"), searchCriteria.getRoomId()));
        }
        if (searchCriteria.getDocumentSettingId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("documentSettingId"), searchCriteria.getDocumentSettingId()));
        }
        if (searchCriteria.getApprovalRequestJustification() != null) {
            predicates.add(criteriaBuilder.equal(root.get("approvalRequestJustification"), searchCriteria.getApprovalRequestJustification()));
        }

        if (predicates.size() >= 1)
            criteriaQuery.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public void deleteRecordById(String id) {
        this.delete(id);
        this.flush();
    }
}
