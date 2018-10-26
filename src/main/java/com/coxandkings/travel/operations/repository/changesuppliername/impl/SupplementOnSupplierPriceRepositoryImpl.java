package com.coxandkings.travel.operations.repository.changesuppliername.impl;

import com.coxandkings.travel.operations.criteria.changesuppliername.SupplementOnSupplierPriceCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.changesuppliername.SupplementOnSupplierPrice;
import com.coxandkings.travel.operations.repository.changesuppliername.SupplementOnSupplierPriceRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository("SupplementOnSupplierPriceRepositoryImpl")
public class SupplementOnSupplierPriceRepositoryImpl extends SimpleJpaRepository<SupplementOnSupplierPrice, String> implements SupplementOnSupplierPriceRepository {
    private EntityManager em;

    public SupplementOnSupplierPriceRepositoryImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(SupplementOnSupplierPrice.class, entityManager);
        this.em = entityManager;
    }

    @Override
    @Transactional
    public SupplementOnSupplierPrice saveSupplementOnSupplierPrice(SupplementOnSupplierPrice supplementOnSupplierPrice) {
        return save(supplementOnSupplierPrice);
    }

    @Override
    @Transactional
    public SupplementOnSupplierPrice getSupplementOnSupplierPriceById(String id) {
        return findOne(id);
    }

    @Override
    @Transactional
    public SupplementOnSupplierPrice getByCriteria(SupplementOnSupplierPriceCriteria supplementOnSupplierPriceCriteria) throws OperationException {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<SupplementOnSupplierPrice> criteriaQuery = criteriaBuilder.createQuery(SupplementOnSupplierPrice.class);
        Root<SupplementOnSupplierPrice> root = criteriaQuery.from(SupplementOnSupplierPrice.class);
        criteriaQuery.select(root);
        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.isEmpty(supplementOnSupplierPriceCriteria.getIdentifier())) {
            throw new OperationException("identifier is missing ");
        }
        predicates.add(criteriaBuilder.equal(root.get("identifier"), supplementOnSupplierPriceCriteria.getIdentifier()));
        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
        }
        SupplementOnSupplierPrice singleResult = null;
        try {
            singleResult = em.createQuery(criteriaQuery).getSingleResult();
        } catch (Throwable ex) {
            ex.printStackTrace();
            throw new OperationException("No Record Found");
        }
        return singleResult;
    }
}
