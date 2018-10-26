package com.coxandkings.travel.operations.repository.commercialstatements.impl;

import com.coxandkings.travel.operations.criteria.prepaymenttosupplier.PaymentCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.commercialstatements.ClientCommercialStatement;
import com.coxandkings.travel.operations.model.commercialstatements.ClientPaymentAdvice;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice.PaymentAdvice;
import com.coxandkings.travel.operations.model.prepaymenttosupplier.paymentadvice.PaymentAdviceOrderInfo;
import com.coxandkings.travel.operations.repository.commercialstatements.ClientPaymentAdviceRepo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ClientPaymentAdviceRepoImpl extends SimpleJpaRepository<ClientPaymentAdvice,String> implements ClientPaymentAdviceRepo {

    private EntityManager entityManager;

    public ClientPaymentAdviceRepoImpl(@Qualifier("opsEntityManager") EntityManager entityManager) {
        super(ClientPaymentAdvice.class, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public ClientPaymentAdvice add(ClientPaymentAdvice clientPaymentAdvice) {
        return saveAndFlush(clientPaymentAdvice);
    }

    @Override
    @Transactional
    public ClientPaymentAdvice update(ClientPaymentAdvice clientPaymentAdvice) {
        return this.saveAndFlush(clientPaymentAdvice);
    }

    @Override
    public ClientPaymentAdvice getById(String id) {
        return this.findOne(id);
    }

    @Override
    public ClientPaymentAdvice getByPaymentAdviceNumber(String payentAdviceNmber) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(ClientPaymentAdvice.class);
        Root root = criteriaQuery.from(ClientPaymentAdvice.class);
        criteriaQuery.where(criteriaBuilder.equal(root.get("paymentAdviceNumber"), payentAdviceNmber));
        return (ClientPaymentAdvice) entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    @Transactional
    public ClientPaymentAdvice searchPaymentAdvise(PaymentCriteria paymentCriteria) throws OperationException {
        ClientPaymentAdvice clientPaymentAdvice =null;
        CriteriaBuilder criteriaBuilder=entityManager.getCriteriaBuilder();
        CriteriaQuery<ClientPaymentAdvice> criteriaQuery=criteriaBuilder.createQuery(ClientPaymentAdvice.class);

        Root<ClientPaymentAdvice> root=criteriaQuery.from(ClientPaymentAdvice.class);

        SetJoin<ClientPaymentAdvice, ClientCommercialStatement> join = root.joinSet("clientCommercialStatementSet");
        criteriaQuery.select(root);

        List<Predicate> predicates = new ArrayList<>();
        if (paymentCriteria.getPaymentAdviceNumber()!=null)
        {
            predicates.add(criteriaBuilder.equal(root.get("paymentAdviceNumber"), paymentCriteria.getPaymentAdviceNumber()));
        }
        if(!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }
        try{
            clientPaymentAdvice = entityManager.createQuery(criteriaQuery).getSingleResult();

            TypedQuery<ClientPaymentAdvice> query = entityManager.createQuery(criteriaQuery);

            pagination(paymentCriteria.getPageNumber(), paymentCriteria.getPageSize(), query);

            return clientPaymentAdvice;
        }
        catch (NoResultException e){
            return clientPaymentAdvice;
        }
    }

    private void pagination(Integer pageNumber, Integer pageSize, TypedQuery<ClientPaymentAdvice> query) {
        if (pageNumber == null || pageNumber == 0) {
            pageNumber = new Integer(1);
        }
        if (pageSize == null || pageSize == 0) {
            pageSize = new Integer(10);
        }

        Integer firstResult = ((pageNumber - 1) * pageSize) + 1;
        query.setFirstResult(firstResult);
        query.setMaxResults(pageSize);
    }

}
