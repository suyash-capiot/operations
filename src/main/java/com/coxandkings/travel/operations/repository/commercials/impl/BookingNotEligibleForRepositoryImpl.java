package com.coxandkings.travel.operations.repository.commercials.impl;

import com.coxandkings.travel.operations.model.commercials.BookingIneligibleFor;
import com.coxandkings.travel.operations.repository.commercials.BookingNotEligibleForRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class BookingNotEligibleForRepositoryImpl extends SimpleJpaRepository<BookingIneligibleFor, String> implements BookingNotEligibleForRepository {

    private EntityManager entityManager;

    public BookingNotEligibleForRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em){
        super(BookingIneligibleFor.class,em);
        entityManager = em;
    }


    @Override
    public List<BookingIneligibleFor> getAllStatus() {
        //return entityManager.createQuery(" FROM com.coxandkings.travel.operations.model.commercials.BookingNotEligibleFor b").getResultList();
    	return this.findAll();
    }

}
