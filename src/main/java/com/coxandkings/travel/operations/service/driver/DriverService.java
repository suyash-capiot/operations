package com.coxandkings.travel.operations.service.driver;

import com.coxandkings.travel.operations.criteria.driver.DriverCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.core.OpsBooking;
import com.coxandkings.travel.operations.model.driver.Driver;
import com.coxandkings.travel.operations.resource.driver.DriverResource;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DriverService {
    public Driver getDriverById(String id);

    public List<Driver> getDriverByCriteria(DriverCriteria criteria) throws OperationException;

    public List<String> getDriverStatus();

    public void updateProductForAction(OpsBooking opsBooking);

    public DriverResource loadDriverDetails(String bookingRefid, String orderId) throws OperationException;

    public Driver saveDriver(DriverResource driverResource) throws OperationException;

    public Driver updateDriver(DriverResource driverResource) throws OperationException;

    public EmailResponse sendDriverDetailsToCustomer(DriverResource driverResource) throws OperationException;

    public EmailResponse sendDriverDetailsToSupplier(String bookingRefid, String orderId, boolean automaticTrigger) throws OperationException;

    public boolean isDetailsPresent(String id);

    public Driver updateDriverBySupplier(DriverResource driverResource) throws OperationException;


}
