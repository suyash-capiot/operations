package com.coxandkings.travel.operations.repository.managepickupdropoff.impl;

import com.coxandkings.travel.operations.model.managepickupdropoff.PickUpDropOff;
import com.coxandkings.travel.operations.repository.managepickupdropoff.PickUpDropOffRepository;
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

@Repository(value = "PickUpDropOffRepositoryImpl")
public class PickUpDropOffRepositoryImpl extends SimpleJpaRepository<PickUpDropOff,String> implements PickUpDropOffRepository
{


    private EntityManager entityManager;

    public PickUpDropOffRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em)
    {
        super(PickUpDropOff.class, em);
        entityManager=em;
    }

    @Override
    @Transactional
    public PickUpDropOff savePickUpDropOffDetails(PickUpDropOff pickUpDropOff)
    {
        return saveAndFlush(pickUpDropOff);
    }

    @Override
    public PickUpDropOff searchPickUpDropOff(PickUpDropOff pickUpDropOff)
    {
        PickUpDropOff pickUpDropOff1 = null;
        CriteriaBuilder criteriaBuilder=entityManager.getCriteriaBuilder();
        CriteriaQuery<PickUpDropOff> criteriaQuery=criteriaBuilder.createQuery(PickUpDropOff.class);
        Root<PickUpDropOff> root=criteriaQuery.from(PickUpDropOff.class);
        criteriaQuery.select(root);

        List<Predicate> predicates = new ArrayList<>();

        if (pickUpDropOff.getBookingRefId()!=null)
        {
            predicates.add(criteriaBuilder.equal(root.get("bookingRefId"), pickUpDropOff.getBookingRefId()));
        }

        if (pickUpDropOff.getOrderId()!=null)
        {
            predicates.add(criteriaBuilder.equal(root.get("orderId"), pickUpDropOff.getOrderId()));
        }

        if(!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }

        pickUpDropOff1 =entityManager.createQuery(criteriaQuery).getSingleResult();
        return pickUpDropOff1;
    }


}
