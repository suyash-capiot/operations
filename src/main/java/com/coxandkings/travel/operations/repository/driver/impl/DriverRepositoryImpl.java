package com.coxandkings.travel.operations.repository.driver.impl;


import com.coxandkings.travel.operations.criteria.driver.DriverCriteria;
import com.coxandkings.travel.operations.model.driver.Driver;
import com.coxandkings.travel.operations.repository.driver.DriverRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository("driverRepository")
public class DriverRepositoryImpl extends SimpleJpaRepository<Driver, String> implements DriverRepository {


    private EntityManager entityManager;

    public DriverRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em)
    {
        super(Driver.class, em);
        entityManager=em;
    }

    /*@Override
    @Transactional
    public Driver saveOrUpdateDriver(Driver driver) {
        return this.saveAndFlush(driver);
    }*/

    @Override
    public List<Driver> getDriverByCriteria(DriverCriteria criteria)
    {
        List<Driver> drivers = null;
        CriteriaBuilder criteriaBuilder=entityManager.getCriteriaBuilder();
        CriteriaQuery<Driver> criteriaQuery=criteriaBuilder.createQuery(Driver.class);
        Root<Driver> root=criteriaQuery.from(Driver.class);
        criteriaQuery.select(root);

        List<Predicate> predicates = new ArrayList <>();
        if(criteria.getId()!=null)
        {
            predicates.add(criteriaBuilder.equal(root.get("id"),criteria.getId()));
        }

        if(criteria.getBookingRefId()!=null)
        {
            predicates.add(criteriaBuilder.equal(root.get("bookingRefId"),criteria.getBookingRefId()));
        }

        if (criteria.getDriverName()!=null)
        {
            predicates.add(criteriaBuilder.equal(root.get("driverName"),criteria.getDriverName()));
        }

        if(criteria.getMobileNumber()!=null)
        {
            predicates.add(criteriaBuilder.equal(root.get("mobileNumber"),criteria.getMobileNumber()));
        }

        String productId = criteria.getProductId();
        if(!StringUtils.isEmpty(productId)) {
            predicates.add(criteriaBuilder.equal(root.get("productId"), productId));
        }

        if(!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }

        try{
            drivers = entityManager.createQuery(criteriaQuery).getResultList();
            return drivers;
        }
        catch (NoResultException e){
            return drivers;
        }
    }

    @Override
    public Driver getDriverById(String id) {
        return this.findOne(id);
    }

    @Override
    @Transactional
    public Driver saveDriver(Driver driver) {
        return this.save(driver);
    }

    @Override
    @Transactional
    public Driver updateDriver(Driver driver) {
        return this.saveAndFlush(driver);
    }


}
