package com.coxandkings.travel.operations.repository.productbookedthrother.impl;

import com.coxandkings.travel.operations.criteria.productbookedthrother.ProductBookedThrOtherCriteria;
import com.coxandkings.travel.operations.model.productbookedthrother.Accomodation;
import com.coxandkings.travel.operations.model.productbookedthrother.Attribute;
import com.coxandkings.travel.operations.repository.productbookedthrother.AccomodationRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service(value = "AccomodationRepositoryImpl")
public class AccomodationRepositoryImpl extends SimpleJpaRepository<Accomodation,String> implements AccomodationRepository
{

    private EntityManager entityManager;

    public AccomodationRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em)
    {
        super(Accomodation.class, em);
        entityManager=em;
    }


    @Override
    @Transactional
    public Accomodation saveOrUpdateAccomodation(Accomodation accomodation) {
        return saveAndFlush(accomodation);
    }

    @Override
    public Boolean isAccomodationExists(String id) {
        return exists(id);
    }

    @Override
    public Accomodation getAccomodationById(String id) {
        return findOne(id);
    }

    @Override
    public Accomodation getAccomodationByCriteria(ProductBookedThrOtherCriteria criteria)
    {
        Accomodation accomodation = null;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Accomodation> criteriaQuery = criteriaBuilder.createQuery(Accomodation.class);
        Root<Accomodation> root = criteriaQuery.from(Accomodation.class);
        criteriaQuery.select(root);

        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getBookingRefId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("bookingRefId"), criteria.getBookingRefId()));
        }

        if (criteria.getOrderId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("orderId"), criteria.getOrderId()));
        }

        if (criteria.getProductCategorySubTypeValue() != null) {
            predicates.add(criteriaBuilder.equal(root.get("productCategorySubType"), criteria.getProductCategorySubTypeValue()));
        }

        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }

        try {
            accomodation = (Accomodation) entityManager.createQuery(criteriaQuery).getSingleResult();
            return accomodation;
        } catch (NoResultException e) {
            return accomodation;
        }
    }
}
