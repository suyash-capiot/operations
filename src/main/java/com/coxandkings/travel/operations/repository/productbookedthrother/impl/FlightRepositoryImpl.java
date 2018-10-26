package com.coxandkings.travel.operations.repository.productbookedthrother.impl;


import com.coxandkings.travel.operations.criteria.productbookedthrother.ProductBookedThrOtherCriteria;
import com.coxandkings.travel.operations.model.productbookedthrother.Attribute;
import com.coxandkings.travel.operations.model.productbookedthrother.Flight;
import com.coxandkings.travel.operations.repository.productbookedthrother.FlightRepository;
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

@Repository(value = "FlightRepositoryImpl")
public class FlightRepositoryImpl extends SimpleJpaRepository<Flight,String> implements FlightRepository
{


    private EntityManager entityManager;

    public FlightRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em)
    {
        super(Flight.class, em);
        entityManager=em;
    }


    @Override
    @Transactional
    public Flight saveOrUpdateFlight(Flight flight) {
        return saveAndFlush(flight);
    }

    @Override
    public Boolean isFlightExists(String id) {
        return exists(id);
    }

    @Override
    public Flight getFlightById(String id) {
        return findOne(id);
    }

    @Override
    public Flight getFlightByCriteria(ProductBookedThrOtherCriteria criteria) {

        Flight flight = null;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Flight> criteriaQuery = criteriaBuilder.createQuery(Flight.class);
        Root<Flight> root = criteriaQuery.from(Flight.class);
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
            flight = (Flight) entityManager.createQuery(criteriaQuery).getSingleResult();
            return flight;
        }
        catch (NoResultException e){
            return flight;
        }
    }
}
