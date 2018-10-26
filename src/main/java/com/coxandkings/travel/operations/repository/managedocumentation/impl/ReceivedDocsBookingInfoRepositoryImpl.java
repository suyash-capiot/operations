package com.coxandkings.travel.operations.repository.managedocumentation.impl;

import com.coxandkings.travel.operations.criteria.managedocumentation.ReceivedDocsBookingInfoSearchCriteria;
import com.coxandkings.travel.operations.model.managedocumentation.ReceivedDocsBookingInfo;
import com.coxandkings.travel.operations.repository.managedocumentation.ReceivedDocsBookingInfoRepository;
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
public class ReceivedDocsBookingInfoRepositoryImpl extends SimpleJpaRepository<ReceivedDocsBookingInfo, String> implements ReceivedDocsBookingInfoRepository {

    private EntityManager entityManager;

    ReceivedDocsBookingInfoRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(ReceivedDocsBookingInfo.class, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public ReceivedDocsBookingInfo saveDetails(ReceivedDocsBookingInfo bookingInfo) {
        return this.saveAndFlush(bookingInfo);
    }

    @Override
    public ReceivedDocsBookingInfo updateDetails(ReceivedDocsBookingInfo bookingInfo) {
        return this.saveAndFlush(bookingInfo);
    }

    @Override
    public List<ReceivedDocsBookingInfo> getAllDetails() {
        return this.findAll();
    }

    @Override
    public List<ReceivedDocsBookingInfo> searchDetails(ReceivedDocsBookingInfoSearchCriteria searchCriteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ReceivedDocsBookingInfo> criteriaQuery = criteriaBuilder.createQuery(ReceivedDocsBookingInfo.class);
        Root<ReceivedDocsBookingInfo> root = criteriaQuery.from(ReceivedDocsBookingInfo.class);
        criteriaQuery.select(root);
        List<Predicate> predicateList = new ArrayList<>();
        if (searchCriteria.getId() != null)
            predicateList.add(criteriaBuilder.equal(root.get("id"), searchCriteria.getId()));
        if (searchCriteria.getBookId() != null)
            predicateList.add(criteriaBuilder.equal(root.get("bookId"), searchCriteria.getBookId()));
        if (searchCriteria.getOrderId() != null)
            predicateList.add(criteriaBuilder.equal(root.get("orderId"), searchCriteria.getOrderId()));

        criteriaQuery.where(predicateList.toArray(new Predicate[0]));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public void deleteDetails(String id) {
        this.delete(id);
        this.flush();
    }
}
