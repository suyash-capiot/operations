/*
package com.coxandkings.travel.operations.repository.driver.impl;

import com.coxandkings.travel.operations.model.driver.Driver;
import com.coxandkings.travel.operations.model.driver.DriverStatus;
import com.coxandkings.travel.operations.repository.driver.DriverStatusRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class DriverStatusRepositoryImpl extends SimpleJpaRepository<DriverStatus, String > implements DriverStatusRepository
{
    private EntityManager entityManager;

    public DriverStatusRepositoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(DriverStatus.class, em);
        entityManager=em;
    }

    @Override
    public DriverStatus saveOrUpdateDriverStatus(DriverStatus driverStatus) {
        return this.saveAndFlush(driverStatus);
    }

    @Override
    public DriverStatus getDriverStatusById(String id) {
        return this.findOne(id);
    }

    @Override
    public List<DriverStatus> getDriverStatus() {
        return this.findAll();
    }
}
*/
