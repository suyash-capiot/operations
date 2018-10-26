package com.coxandkings.travel.operations.repository.managedocumentation.impl;

import com.coxandkings.travel.operations.criteria.managedocumentation.MDMUnConfiguredDataSearchCriteria;
import com.coxandkings.travel.operations.model.managedocumentation.MDMUnConfiguredData;
import com.coxandkings.travel.operations.repository.managedocumentation.MDMUnConfiguredDataRepository;
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
public class MDMUnConfiguredDataRepositoryImpl extends SimpleJpaRepository<MDMUnConfiguredData, String> implements MDMUnConfiguredDataRepository {

    private EntityManager entityManager;

    MDMUnConfiguredDataRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(MDMUnConfiguredData.class, em);
        this.entityManager = em;
    }


    @Override
    public MDMUnConfiguredData saveDetails(MDMUnConfiguredData mdmUnConfiguredData) {
        return this.saveAndFlush(mdmUnConfiguredData);
    }

    @Override
    public MDMUnConfiguredData updateDetails(MDMUnConfiguredData mdmUnConfiguredData) {
        return this.saveAndFlush(mdmUnConfiguredData);
    }

    @Override
    public MDMUnConfiguredData getDetails(String id) {
        return this.findOne(id);
    }

    @Override
    public List<MDMUnConfiguredData> searchDetails(MDMUnConfiguredDataSearchCriteria searchCriteria) {
        List<MDMUnConfiguredData> mdmUnConfiguredDataList;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<MDMUnConfiguredData> criteriaQuery = criteriaBuilder.createQuery(MDMUnConfiguredData.class);
        Root<MDMUnConfiguredData> root = criteriaQuery.from(MDMUnConfiguredData.class);
        criteriaQuery.select(root);
        List<Predicate> predicateList = new ArrayList<>();
        if (searchCriteria.getId() != null)
            predicateList.add(criteriaBuilder.equal(root.get("id"), searchCriteria.getId()));
        if (searchCriteria.getProductCategory() != null)
            predicateList.add(criteriaBuilder.equal(root.get("productCategory"), searchCriteria.getProductCategory()));
        if (searchCriteria.getProductCategorySubType() != null)
            predicateList.add(criteriaBuilder.equal(root.get("productCategorySubType"), searchCriteria.getProductCategorySubType()));
        if (searchCriteria.getSupplierId() != null)
            predicateList.add(criteriaBuilder.equal(root.get("supplierId"), searchCriteria.getSupplierId()));
        if (searchCriteria.getOrderId() != null)
            predicateList.add(criteriaBuilder.equal(root.get("orderId"), searchCriteria.getOrderId()));
        if (searchCriteria.getBookId() != null)
            predicateList.add(criteriaBuilder.equal(root.get("bookId"), searchCriteria.getBookId()));

        criteriaQuery.where(predicateList.toArray(new Predicate[0]));

        mdmUnConfiguredDataList = entityManager.createQuery(criteriaQuery).getResultList();
        return mdmUnConfiguredDataList;
    }

    @Override
    public void deleteDetails(String id) {
        this.delete(id);
        this.flush();
    }

    @Override
    public List<MDMUnConfiguredData> findAllDetails() {
        return this.findAll();
    }
}
