package com.coxandkings.travel.operations.repository.commercials.impl;

import com.coxandkings.travel.operations.model.commercials.SellingPriceComponent;
import com.coxandkings.travel.operations.repository.commercials.SellingPriceComponentRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository("sellingPriceComponentRepositoryImpl")
public class SellingPriceComponentRepositoryImpl
    extends SimpleJpaRepository<SellingPriceComponent, String> implements SellingPriceComponentRepository {

    private EntityManager entityManager;

    public SellingPriceComponentRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em){
        super(SellingPriceComponent.class,em);
        entityManager = em;
    }

    public List<SellingPriceComponent> getAllStatus(){
        return this.findAll();
    }

}
