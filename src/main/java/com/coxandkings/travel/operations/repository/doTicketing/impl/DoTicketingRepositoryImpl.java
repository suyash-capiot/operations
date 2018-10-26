package com.coxandkings.travel.operations.repository.doTicketing.impl;

import com.coxandkings.travel.operations.model.doTicketing.DoTicketing;
import com.coxandkings.travel.operations.repository.doTicketing.DoTicketingRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Transactional
@Repository
public class DoTicketingRepositoryImpl extends SimpleJpaRepository<DoTicketing, String> implements DoTicketingRepository{

    private EntityManager entityManager;

    public DoTicketingRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(DoTicketing.class, em);
        entityManager = em;
    }

    @Override
    public DoTicketing getById(String id) {
        return this.findOne(id);
    }

    @Override
    @Transactional
    public DoTicketing saveOrUpdate(DoTicketing doTicketing) {
        return this.saveAndFlush(doTicketing);
    }

    @Transactional
    @Override
    public DoTicketing update(DoTicketing doTicketing) {
        return this.saveAndFlush(doTicketing);
    }

    @Override
    public DoTicketing getByBookAndOrderId(String bookID, String orderID) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DoTicketing> criteriaQuery = criteriaBuilder.createQuery(DoTicketing.class);
        Root<DoTicketing> root = criteriaQuery.from(DoTicketing.class);
        criteriaQuery.select(root);

        Predicate p1 = criteriaBuilder.equal(root.get("bookId"), bookID);
        Predicate p2 = criteriaBuilder.equal(root.get("orderId"), orderID);

        criteriaQuery.where(p1, p2);
        TypedQuery<DoTicketing> query = entityManager.createQuery(criteriaQuery);

        DoTicketing doTicketing;
        try {
            doTicketing = query.getSingleResult();
        }catch(NoResultException|IllegalArgumentException e){
            return null;
        }
        return doTicketing;
    }
}
