package com.coxandkings.travel.operations.repository.communication.impl;

import com.coxandkings.travel.operations.criteria.communication.CommunicationTagCriteria;
import com.coxandkings.travel.operations.model.communication.CommunicationTags;
import com.coxandkings.travel.operations.repository.communication.CommunicationTagRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CommunicationTagRepositoryImpl extends SimpleJpaRepository<CommunicationTags, String> implements CommunicationTagRepository {

    private EntityManager entityManager;

    public CommunicationTagRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em)
    {
        super(CommunicationTags.class, em);
        entityManager=em;
    }
    @Override
    public List<CommunicationTags> getCommunicationByTAGCriteria(CommunicationTagCriteria criteria) {
        List<CommunicationTags> communicationTags = new ArrayList<>();
        CriteriaBuilder criteriaBuilder=entityManager.getCriteriaBuilder();
        CriteriaQuery<CommunicationTags> criteriaQuery=criteriaBuilder.createQuery(CommunicationTags.class);
        Root<CommunicationTags> root=criteriaQuery.from(CommunicationTags.class);
        criteriaQuery.select(root);

        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getBookId() != null)
        {
            predicates.add(criteriaBuilder.equal(root.get("bookId"), criteria.getBookId()));
        }
        if(criteria.getOrderID()!=null)
        {
            predicates.add(criteriaBuilder.equal(root.get("orderId"),criteria.getOrderID()));
        }
        if(criteria.getSupplierID()!=null)
        {
            predicates.add(criteriaBuilder.equal(root.get("supplierId"),criteria.getSupplierID()));
        }
        if(criteria.getClientID()!=null)
        {
            predicates.add(criteriaBuilder.equal(root.get("clientId"),criteria.getClientID()));
        }
        if(criteria.getCustomerID()!=null)
        {
            predicates.add(criteriaBuilder.equal(root.get("customerId"),criteria.getCustomerID()));
        }
        if (criteria.getFunction() != null)
        {
            predicates.add(criteriaBuilder.equal(root.get("function"), criteria.getFunction()));
        }
        if(criteria.getProcess()!=null)
        {
            predicates.add(criteriaBuilder.equal(root.get("process"),criteria.getProcess()));
        }
        if(criteria.getScenario()!=null)
        {
            predicates.add(criteriaBuilder.equal(root.get("scenario"),criteria.getScenario()));
        }
        if(criteria.getActionType()!=null)
        {
            predicates.add(criteriaBuilder.equal(root.get("actionType"),criteria.getActionType()));
        }

        if(!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }

        try{
            communicationTags = entityManager.createQuery(criteriaQuery).getResultList();
            return communicationTags;
        }
        catch (NoResultException e){
            return communicationTags;
        } catch (Exception e1) {
            return communicationTags;
        }
    }

}
