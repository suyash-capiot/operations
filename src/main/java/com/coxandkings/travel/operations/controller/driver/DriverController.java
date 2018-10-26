package com.coxandkings.travel.operations.controller.driver;


import com.coxandkings.travel.operations.criteria.driver.DriverCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.driver.Driver;
import com.coxandkings.travel.operations.resource.driver.DriverResource;
import com.coxandkings.travel.operations.resource.email.EmailResponse;
import com.coxandkings.travel.operations.service.driver.DriverService;
import com.coxandkings.travel.operations.service.driver.VehicleCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//import com.coxandkings.travel.operations.model.driver.DriverStatus;

@RestController
@RequestMapping("/drivers")
@CrossOrigin(origins = "*")
public class DriverController {

    @Autowired
    private DriverService driverService;


    @Autowired
    VehicleCategoryService vehicleCategoryService;


    @GetMapping(path = "/v1/bookings/{bookingRefId}/orders/{orderId}")
    public ResponseEntity<DriverResource> loadDriverDetails(@PathVariable("bookingRefId") String bookingRefId, @PathVariable("orderId") String orderId) throws OperationException {
        return new ResponseEntity<DriverResource>(driverService.loadDriverDetails(bookingRefId,orderId),HttpStatus.OK);
    }


    @PostMapping(value="/v1/create")
    public ResponseEntity<Driver> createDriver(@RequestBody DriverResource driverResource) throws OperationException {
        Driver driver = driverService.saveDriver(driverResource);
        return new ResponseEntity<Driver>(driver, HttpStatus.CREATED);
    }

    @PutMapping(value="/v1/update")
    public ResponseEntity<Driver> updateDriver(@RequestBody DriverResource driverResource) throws OperationException {
        Driver driver = driverService.updateDriver(driverResource);
        return new ResponseEntity<Driver>(driver, HttpStatus.CREATED);
    }

    @PostMapping(path="/v1/search")
    public ResponseEntity<List<Driver>> getDriverByCriteria(@RequestBody DriverCriteria criteria) throws OperationException {
        List<Driver> drivers = driverService.getDriverByCriteria(criteria);
        return new ResponseEntity<List<Driver>>(drivers, HttpStatus.OK);
    }

    @GetMapping(value="/v1/{id}")
    public ResponseEntity<Driver> getDriverById(@PathVariable("id") String id)
    {
        Driver driver=driverService.getDriverById(id);
        return new ResponseEntity<Driver>(driver,HttpStatus.OK);
    }

    @PostMapping(value = "/v1/sendDriverDetailsToCustomer")
    public ResponseEntity<EmailResponse> sendDriverDetailsToCustomer(@RequestBody DriverResource driverResource) throws OperationException {
        EmailResponse status = driverService.sendDriverDetailsToCustomer(driverResource);
        return new ResponseEntity<EmailResponse>(status, HttpStatus.OK);
    }

    @GetMapping(value = "/v1/id/{id}")
    public ResponseEntity<Boolean> isDriverDetails(@PathVariable("id") String id) {
        boolean flag = driverService.isDetailsPresent(id);
        return new ResponseEntity<>(flag, HttpStatus.OK);
    }

    @PostMapping(value = "/v1/{bookingRefId}/{orderId}/{automaticTrigger}")
    public ResponseEntity<EmailResponse> sendToSupplier(@PathVariable("bookingRefId") String bookingRef, @PathVariable("orderId") String orderId, @PathVariable("automaticTrigger") boolean automaticTrigger) throws OperationException {
        EmailResponse emailResponse = driverService.sendDriverDetailsToSupplier(bookingRef, orderId, automaticTrigger);
        return new ResponseEntity<>(emailResponse, HttpStatus.OK);
    }

    @PutMapping(value = "/v1/updateBySupplier")
    public ResponseEntity<Driver> updateDriverBySupplier(@RequestBody DriverResource driverResource) throws OperationException {
        Driver driver = driverService.updateDriverBySupplier(driverResource);
        return new ResponseEntity<Driver>(driver, HttpStatus.CREATED);
    }
}