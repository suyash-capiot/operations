package com.coxandkings.travel.operations.repository.managedocumentation.impl;

import com.coxandkings.travel.operations.criteria.managedocumentation.HandoverAndCustomerDocInfoSearchCriteria;
import com.coxandkings.travel.operations.model.managedocumentation.HandoverAndCustomerDocInfo;
import com.coxandkings.travel.operations.repository.managedocumentation.HandoverAndCustomerDocInfoRepository;
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
public class HandoverAndCustomerDocInfoRepositoryImpl extends SimpleJpaRepository<HandoverAndCustomerDocInfo, String> implements HandoverAndCustomerDocInfoRepository {

    private EntityManager entityManager;

    HandoverAndCustomerDocInfoRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(HandoverAndCustomerDocInfo.class, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public HandoverAndCustomerDocInfo saveDetails(HandoverAndCustomerDocInfo handoverAndCustomerDocInfo) {
        return this.saveAndFlush(handoverAndCustomerDocInfo);
    }

    @Override
    public List<HandoverAndCustomerDocInfo> getAllDetails() {
        return this.findAll();
    }

    @Override
    public List<HandoverAndCustomerDocInfo> searchDetails(HandoverAndCustomerDocInfoSearchCriteria searchCriteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<HandoverAndCustomerDocInfo> criteriaQuery = criteriaBuilder.createQuery(HandoverAndCustomerDocInfo.class);
        Root<HandoverAndCustomerDocInfo> root = criteriaQuery.from(HandoverAndCustomerDocInfo.class);
        criteriaQuery.select(root);
        List<Predicate> predicates = new ArrayList<>();
        if (searchCriteria.getId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("id"), searchCriteria.getId()));
        }
        if (searchCriteria.getBookId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("bookId"), searchCriteria.getBookId()));
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
        if (searchCriteria.getDocumentWise() != null) {
            predicates.add(criteriaBuilder.equal(root.get("documentWise"), searchCriteria.getDocumentWise()));
        }
        if (searchCriteria.getDocumentType() != null) {
            predicates.add(criteriaBuilder.equal(root.get("documentType"), searchCriteria.getDocumentType()));
        }

        if (predicates.size() >= 1)
            criteriaQuery.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public void deleteDetails(String id) {
        this.delete(id);
        this.flush();
    }
}
