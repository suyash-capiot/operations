package com.coxandkings.travel.operations.repository.driver;

import com.coxandkings.travel.operations.criteria.driver.DriverCriteria;
import com.coxandkings.travel.operations.model.driver.Driver;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DriverRepository {

//    public Driver saveOrUpdateDriver(Driver driver);
    public List<Driver> getDriverByCriteria(DriverCriteria criteria);

    public Driver getDriverById(String id);

    public Driver saveDriver(Driver driver);

    public Driver updateDriver(Driver driver);
}


