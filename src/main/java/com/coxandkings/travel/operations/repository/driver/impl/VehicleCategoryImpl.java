package com.coxandkings.travel.operations.repository.driver.impl;

import com.coxandkings.travel.operations.model.driver.VehicleCategory;
import com.coxandkings.travel.operations.repository.driver.VehicleCategoryRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class VehicleCategoryImpl  extends SimpleJpaRepository<VehicleCategory,String> implements VehicleCategoryRepository{

    private EntityManager entityManager;

    public VehicleCategoryImpl(@Qualifier("opsEntityManager") EntityManager em) {
        super(VehicleCategory.class, em);
        entityManager=em;
    }

    @Override
    public VehicleCategory getVehicleCategory(String id) {
        return this.findOne(id);
    }

    @Override
    public VehicleCategory saveOrUpdateVehicleCategory(VehicleCategory vehicleCategory) {
        return this.saveAndFlush(vehicleCategory);
    }

    @Override
    public List<VehicleCategory> getVehicleCategory() {
        return this.findAll();
    }
}
