package com.coxandkings.travel.operations.repository.productbookedthrother.impl;

import com.coxandkings.travel.operations.criteria.productbookedthrother.ProductBookedThrOtherCriteria;
import com.coxandkings.travel.operations.model.productbookedthrother.Attribute;
import com.coxandkings.travel.operations.model.productbookedthrother.Bus;
import com.coxandkings.travel.operations.repository.productbookedthrother.BusRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository(value = "BusRepository")
public class BusRepositoryImpl extends SimpleJpaRepository<Bus,String> implements BusRepository {

    private EntityManager entityManager;

    public BusRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em)
    {
        super(Bus.class, em);
        entityManager=em;
    }


    @Override
    @Transactional
    public Bus saveOrUpdateBus(Bus bus) {
        return saveAndFlush(bus);
    }

    @Override
    public Bus getBusById(String id) {
        return findOne(id);
    }

    @Override
    public Boolean isBusExists(String id) {
        return exists(id);
    }

    @Override
    public Bus getBusByCriteria(ProductBookedThrOtherCriteria criteria)
    {
        Bus bus = null;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Bus> criteriaQuery = criteriaBuilder.createQuery(Bus.class);
        Root<Bus> root = criteriaQuery.from(Bus.class);
        criteriaQuery.select(root);

        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getBookingRefId()!=null)
        {
            predicates.add(criteriaBuilder.equal(root.get("bookingRefId"),criteria.getBookingRefId()));
        }

        if (criteria.getOrderId()!=null)
        {
            predicates.add(criteriaBuilder.equal(root.get("orderId"),criteria.getOrderId()));
        }

        if (criteria.getProductCategorySubTypeValue()!=null)
        {
            predicates.add(criteriaBuilder.equal(root.get("productCategorySubType"),criteria.getProductCategorySubTypeValue()));
        }

        if(!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }

        try{
            bus = (Bus) entityManager.createQuery(criteriaQuery).getSingleResult();
            return bus;
        }
        catch (NoResultException e){
            return bus;
        }
    }
}
