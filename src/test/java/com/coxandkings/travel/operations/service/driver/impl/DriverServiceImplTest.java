///*
//package com.coxandkings.travel.operations.service.driver.impl;
//
//import com.coxandkings.travel.operations.exceptions.OperationException;
//import com.coxandkings.travel.operations.model.driver.Driver;
////import com.coxandkings.travel.operations.model.driver.DriverStatus;
////import com.coxandkings.travel.operations.model.driver.VehicleCategory;
//import com.coxandkings.travel.operations.repository.driver.DriverRepository;
//import com.coxandkings.travel.operations.resource.driver.DriverResource;
//import com.coxandkings.travel.operations.resource.driver.DriverStatusResource;
//import com.coxandkings.travel.operations.resource.driver.VehicleCategoryResource;
//import com.coxandkings.travel.operations.service.driver.DriverService;
////import com.coxandkings.travel.operations.service.driver.DriverStatusService;
//import com.coxandkings.travel.operations.service.driver.VehicleCategoryService;
//import org.junit.*;
//import org.junit.runner.RunWith;
//import org.junit.runners.MethodSorters;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import javax.transaction.Transactional;
//
//import static org.junit.Assert.*;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
//@Transactional
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//public class DriverServiceImplTest {
//
//    @Autowired
//    private DriverRepository driverRepository;
//
//    @Autowired
//    private DriverService driverService;
//
//@Autowired
//    private DriverStatusService driverStatusService;
//
//
//    @Autowired
//    private VehicleCategoryService vehicleCategoryService;
//
//
//    private static DriverResource driverResource;
//
//
//    @Before
//    public void setUp()
//    {
//
//        if(driverResource == null) {
//            driverResource=new DriverResource();
//            driverResource.setBookingRefId("CNK-0001");
//            driverResource.setCountryCode("+91");
//            driverResource.setDriverName("ABC");
//            driverResource.setMobileNumber("9833517588");
//            driverResource.setVehicleName("Honda");
//            driverResource.setVehicleNumber("1234");
//            driverResource.setDriverStatus("1");
//            driverResource.setVehicleCategory("ANCLR3325814");
//            driverResource.setProductId("1");
//        }
//
//    }
//
//
//    @Test
//    public void createDriver() throws OperationException {
//        _create();
//    }
//
//    private void _create() throws OperationException {
//        Driver driver = driverService.saveOrUpdateDriver(driverResource);
//        driverResource.setId(driver.getId());
//        assertDriverBasicDetails(driver, driverResource);
//    }
//
//    @Test
//    public void createDriverFail()
//    {
//        try{
//            _create();
//            Driver driver = driverService.saveOrUpdateDriver(driverResource);
//            assertTrue(false);
//        } catch(Exception e) {
//            assertTrue(true);
//        }
//    }
//
//    private void assertDriverBasicDetails(Driver driver, DriverResource resource) {
//        assertEquals(driver.getBookingRefId(), resource.getBookingRefId());
//        assertEquals(driver.getDriverName(), resource.getDriverName());
//        assertEquals(driver.getMobileNumber(), resource.getMobileNumber());
//    }
//
//    @Test
//    public void updateDriverDetails() throws OperationException {
//        driverResource.setId(null);
//        _create();
//        driverResource.setDriverName("ABCD");
//        Driver driver = driverService.saveOrUpdateDriver(driverResource);
//        assertTrue((driver.getDriverName()).equalsIgnoreCase(driverResource.getDriverName()));
//    }
//
//    @Test
//    public void getDriverDetails() throws OperationException {
//        driverResource.setId(null);
//        _create();
//        Driver driver = driverService.getDriverById(driverResource.getId());
//        assertNotNull(driver);
//    }
//
//}
//*/
