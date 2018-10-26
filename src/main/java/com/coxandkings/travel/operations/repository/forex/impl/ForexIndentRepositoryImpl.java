package com.coxandkings.travel.operations.repository.forex.impl;

import com.coxandkings.travel.operations.enums.forex.IndentStatus;
import com.coxandkings.travel.operations.enums.forex.IndentType;
import com.coxandkings.travel.operations.model.forex.ForexBooking;
import com.coxandkings.travel.operations.model.forex.ForexIndent;
import com.coxandkings.travel.operations.repository.forex.ForexIndentRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Transactional
@Repository
public class ForexIndentRepositoryImpl extends SimpleJpaRepository<ForexIndent, String> implements ForexIndentRepository {

    private EntityManager entityManager;

    public ForexIndentRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(ForexIndent.class, em);
        entityManager = em;
    }

    @Transactional
    public ForexIndent getById(String id) {
        return this.findOne(id);
    }

    @Override
    public List<ForexIndent> getIndentsByForexId(String id) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ForexIndent> criteriaQuery = criteriaBuilder.createQuery(ForexIndent.class);
        Root<ForexIndent> root = criteriaQuery.from(ForexIndent.class);
        criteriaQuery.select(root);

        Predicate predicate = criteriaBuilder.equal(root.get("forexBooking").get("id"), id);
        criteriaQuery.where(predicate);

        TypedQuery<ForexIndent> query = entityManager.createQuery(criteriaQuery);
        List<ForexIndent> forexIndents = query.getResultList();

        return forexIndents;

    }

    @Override
    public List<String> getSupplierListForGivenName(String name) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);
        Root<ForexIndent> root = criteriaQuery.from(ForexIndent.class);
        criteriaQuery.select(root.get("supplierName"));
        Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("supplierName")), "%" + name.toLowerCase().trim() + "%");
        criteriaQuery.where(predicate);
        criteriaQuery.distinct(true);
        TypedQuery<String> query = entityManager.createQuery(criteriaQuery);

        List<String> supplierList = query.getResultList();
        return supplierList;

    }

    @Override
    public List<String> getSupplierList() {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);
        Root<ForexIndent> root = criteriaQuery.from(ForexIndent.class);
        criteriaQuery.select(root.get("supplierName"));
        criteriaQuery.distinct(true);
        TypedQuery<String> query = entityManager.createQuery(criteriaQuery);

        List<String> supplierList = query.getResultList();
        return supplierList;
    }

    @Override
    public Integer getIndentStatusCount(IndentStatus status) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ForexIndent> criteriaQuery = criteriaBuilder.createQuery(ForexIndent.class);

        Root<ForexIndent> root = criteriaQuery.from(ForexIndent.class);
        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.equal(root.get("indentStatus"), status));
        return entityManager.createQuery(criteriaQuery).getResultList().size();
    }

    @Override
    public ForexBooking getForexByIndentId(String indentId) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ForexBooking> criteriaQuery = criteriaBuilder.createQuery(ForexBooking.class);

        Root<ForexIndent> root = criteriaQuery.from(ForexIndent.class);
        criteriaQuery.select(root.get("forexBooking"));
        criteriaQuery.where(criteriaBuilder.equal(root.get("id"), indentId));

        ForexBooking booking = entityManager.createQuery(criteriaQuery).getSingleResult();
        return booking;
    }

    @Override
    public List<ForexIndent> getIndentsByRequestId(String id) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ForexIndent> criteriaQuery = criteriaBuilder.createQuery(ForexIndent.class);
        Root<ForexIndent> root = criteriaQuery.from(ForexIndent.class);
        criteriaQuery.select(root);

        Predicate predicate = criteriaBuilder.equal(root.get("forexBooking").get("requestId"), id);
        criteriaQuery.where(predicate);

        TypedQuery<ForexIndent> query = entityManager.createQuery(criteriaQuery);
        List<ForexIndent> forexIndents = query.getResultList();

        return forexIndents;
    }

    @Override
    public ForexIndent getIndentsByType(String id, IndentType type) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ForexIndent> criteriaQuery = criteriaBuilder.createQuery(ForexIndent.class);
        Root<ForexIndent> root = criteriaQuery.from(ForexIndent.class);
        criteriaQuery.select(root);

        Predicate p1 = criteriaBuilder.equal(root.get("forexBooking").get("requestId"), id);
        Predicate p2 = criteriaBuilder.equal(root.get("indentFor"), type);
        criteriaQuery.where(p1, p2);

        TypedQuery<ForexIndent> query = entityManager.createQuery(criteriaQuery);
        ForexIndent forexIndent = query.getSingleResult();

        return forexIndent;
    }

    @Transactional
    public ForexIndent saveOrUpdate(ForexIndent forexIndent) {
        return this.saveAndFlush(forexIndent);
    }

}
