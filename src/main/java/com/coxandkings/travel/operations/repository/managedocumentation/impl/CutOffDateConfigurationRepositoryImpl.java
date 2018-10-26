package com.coxandkings.travel.operations.repository.managedocumentation.impl;

import com.coxandkings.travel.operations.criteria.managedocumentation.CutOffDateConfigurationSearchCriteria;
import com.coxandkings.travel.operations.model.managedocumentation.CutOffDateConfiguration;
import com.coxandkings.travel.operations.repository.managedocumentation.CutOffDateConfigurationRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
public class CutOffDateConfigurationRepositoryImpl extends SimpleJpaRepository<CutOffDateConfiguration, String> implements CutOffDateConfigurationRepository {

    private EntityManager entityManager;

    private static final Logger logger = LogManager.getLogger(CutOffDateConfigurationRepositoryImpl.class);

    CutOffDateConfigurationRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(CutOffDateConfiguration.class, em);
        this.entityManager = em;
    }

    @Override
    public CutOffDateConfiguration saveDetails(CutOffDateConfiguration cutOffDateConfiguration) {
        return this.saveAndFlush(cutOffDateConfiguration);
    }

    @Override
    public CutOffDateConfiguration getDetails(String id) {
        return this.findOne(id);
    }

    @Override
    public List<CutOffDateConfiguration> searchDetails(CutOffDateConfigurationSearchCriteria searchCriteria) {
        List<CutOffDateConfiguration> cutOffDateConfiguration = null;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CutOffDateConfiguration> criteriaQuery = criteriaBuilder.createQuery(CutOffDateConfiguration.class);
        Root<CutOffDateConfiguration> root = criteriaQuery.from(CutOffDateConfiguration.class);
        criteriaQuery.select(root);
        List<Predicate> predicateList = new ArrayList<>();
        if (searchCriteria.getId() != null)
            predicateList.add(criteriaBuilder.equal(root.get("id"), searchCriteria.getId()));
        if (searchCriteria.getBookId() != null)
            predicateList.add(criteriaBuilder.equal(root.get("bookId"), searchCriteria.getBookId()));
        if (searchCriteria.getOrderId() != null)
            predicateList.add(criteriaBuilder.equal(root.get("orderId"), searchCriteria.getOrderId()));
        if (searchCriteria.getPaxId() != null)
            predicateList.add(criteriaBuilder.equal(root.get("paxId"), searchCriteria.getPaxId()));
        if (searchCriteria.getRoomId() != null)
            predicateList.add(criteriaBuilder.equal(root.get("roomId"), searchCriteria.getRoomId()));
        if (searchCriteria.getDocumentSettingId() != null)
            predicateList.add(criteriaBuilder.equal(root.get("documentSettingId"), searchCriteria.getDocumentSettingId()));
        if (searchCriteria.getDocumentWise() != null)
            predicateList.add(criteriaBuilder.equal(root.get("documentWise"), searchCriteria.getDocumentWise()));
        if (searchCriteria.getDocumentBy() != null)
            predicateList.add(criteriaBuilder.equal(root.get("documentBy"), searchCriteria.getDocumentBy()));
        if (searchCriteria.getDocumentName() != null)
            predicateList.add(criteriaBuilder.equal(root.get("documentName"), searchCriteria.getDocumentName()));
        if (searchCriteria.getProductName() != null)
            predicateList.add(criteriaBuilder.equal(root.get("productName"), searchCriteria.getProductName()));
        if (searchCriteria.getCutOffDate() != null)
            predicateList.add(criteriaBuilder.between(root.get("cutOffDate"), searchCriteria.getCutOffDate(), searchCriteria.getCutOffDate().plusDays(5)));

        criteriaQuery.where(predicateList.toArray(new Predicate[0]));
        cutOffDateConfiguration = entityManager.createQuery(criteriaQuery).getResultList();
        return cutOffDateConfiguration;
    }

    @Override
    public void deleteDetails(String id) {
        this.delete(id);
        this.flush();
    }

    @Override
    public List<CutOffDateConfiguration> findAllDetails() {
        return this.findAll();
    }
}
