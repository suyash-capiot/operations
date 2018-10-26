package com.coxandkings.travel.operations.repository.newsupplierfirstbooking.impl;

import com.coxandkings.travel.operations.model.newsupplierfirstbooking.NewSupplierDetails;
import com.coxandkings.travel.operations.repository.newsupplierfirstbooking.NewSupplierDetailsRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class NewSupplierDetailsRepositoryImpl extends SimpleJpaRepository<NewSupplierDetails, String> implements NewSupplierDetailsRepository {


    private EntityManager entityManager;

    public NewSupplierDetailsRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(NewSupplierDetails.class, entityManager);
        this.entityManager = entityManager;
    }

    @Transactional
    @Override
    public NewSupplierDetails saveSupplierDetials(NewSupplierDetails newSupplierDetails) {
        return this.saveAndFlush(newSupplierDetails);
    }

    @Override
    public NewSupplierDetails getByOrderID(String orderID) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<NewSupplierDetails> criteriaQuery = criteriaBuilder.createQuery(NewSupplierDetails.class);
        Root<NewSupplierDetails> root = criteriaQuery.from(NewSupplierDetails.class);
        criteriaQuery.where(criteriaBuilder.equal(root.get("orderId"), orderID));
        NewSupplierDetails supplierDetails = null;
        try {
            supplierDetails = entityManager.createQuery(criteriaQuery).getSingleResult();
            return supplierDetails;
        } catch (Exception e) {
            return null;
        }
    }
}
