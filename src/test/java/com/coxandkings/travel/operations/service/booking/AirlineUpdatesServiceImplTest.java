//package com.coxandkings.travel.operations.service.booking;
//
//import com.coxandkings.travel.ext.model.be.*;
//import com.coxandkings.travel.operations.exceptions.OperationException;
//import com.coxandkings.travel.operations.model.booking.AirlineUpdates;
//import com.coxandkings.travel.ext.model.be.*;
//import com.coxandkings.travel.operations.repository.booking.AirlineUpdatesRepository;
//import com.coxandkings.travel.operations.resource.booking.AirlineUpdatesResource;
//import com.coxandkings.travel.operations.utils.Constants;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import org.apache.log4j.Logger;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@SpringBootTest
//@RunWith(SpringJUnit4ClassRunner.class)
//public class AirlineUpdatesServiceImplTest {
//
//    private static final Logger logger = Logger.getLogger(AirlineUpdatesServiceImplTest.class);
//
//    private Booking2 booking;
//    private List<Booking2> bookingEngineList;
//    private AirlineUpdatesResource resource;
//
//    @Autowired
//    private BookingService bookingService;
//
//    @Autowired
//    private AirlineUpdatesRepository airlineUpdatesRepository;
//
//    @Before
//    public void setTestData() {
//        logger.debug("\n Entering AirlineUpdatesServiceImplTest :: setTestData method");
//        logger.debug("\n setting edited flight details to AirlineUpdatesResource");
//        booking = new Booking2();
//        booking.setBookID("CNK-00001");
//        List<ProductResource> products = new ArrayList<>();
//        ProductResource product = new ProductResource();
//        product.setOrderID("1");
//        OrderDetails orderDetails = new OrderDetails();
//        FlightDetails flightDetails = new FlightDetails();
//        List<OriginDestinationOption> destinationOptions = new ArrayList<>();
//        OriginDestinationOption originDestinationOption = new OriginDestinationOption();
//        List<FlightSegment> flightSegments = new ArrayList<>();
//        FlightSegment flightSegment = new FlightSegment();
//        flightSegment.setArrivalDate("2018-03-19T09:35:00");
//        flightSegment.setDepartureDate("2018-03-19T07:20:00");
//        flightSegment.setArrivalTerminal("");
//        flightSegment.setAvailableCount(0);
//        flightSegment.setCabinType("Economy");
//        OperatingAirline operatingAirline = new OperatingAirline();
//        operatingAirline.setFlightNumber("JET-1636");
//        operatingAirline.setAirlineCode("JET-173NH");
//        flightSegment.setOperatingAirline(operatingAirline);
//        flightSegments.add(flightSegment);
//        originDestinationOption.setFlightSegment(flightSegments);
//        destinationOptions.add(originDestinationOption);
//        flightDetails.setOriginDestinationOptions(destinationOptions);
//        orderDetails.setFlightDetails(flightDetails);
//        product.setOrderDetails(orderDetails);
//        products.add(product);
//        booking.setProducts(products);
//        bookingEngineList = new ArrayList<>();
//        bookingEngineList.add(booking);
//        resource = new AirlineUpdatesResource();
//        resource.setUserID("USER11");
//        resource.setOrderID("CNK-02823");
//        resource.setFlightDetails(flightDetails);
//        logger.debug("\n Exiting AirlineUpdatesServiceImplTest :: setTestData method");
//    }
//
//    @Test
//    public void testSaveAirlineUpdates() {
//        logger.debug("\n Entering AirlineUpdatesServiceImplTest :: testSaveAirlineUpdates method");
//        logger.debug("\n testing saveAirlineUpdates method of AirlineUpdatesServiceImpl");
//        Booking2 airlineUpdates = null;
//        try {
//            bookingService.saveAirlineUpdates(bookingEngineList, false);
//            logger.debug("\n saved airline update");
//            airlineUpdates = bookingService.getAirlineUpdates(booking.getBookID(), booking.getProducts().get(0).getOrderID());
//        } catch (JsonProcessingException e) {
//            logger.debug("\n failed to serialize the object");
//        } catch (IOException e) {
//            logger.debug("\n failed to deserialize the object");
//        }
//        Assert.assertEquals(airlineUpdates.getBookId(), booking.getBookId());
//        Assert.assertEquals(airlineUpdates.getProducts().get(0).getOrderID(), booking.getProducts().get(0).getOrderID());
//        Assert.assertEquals(airlineUpdates.getProducts().get(0).getOrderDetails().getFlightDetails(), booking.getProducts().get(0).getOrderDetails().getFlightDetails());
//        logger.debug("\n Exiting AirlineUpdatesServiceImplTest :: testSaveAirlineUpdates method");
//    }
//
//    @Test
//    public void testGetListOfAirlineUpdates() {
//        logger.debug("\n Entering AirlineUpdatesServiceImplTest :: testGetListOfAirlineUpdates method");
//        List<Booking2> airlineUpdates = new ArrayList<>();
//        try {
//            bookingService.saveAirlineUpdates(bookingEngineList, false);
//            airlineUpdates = bookingService.getListOfAirlineUpdates();
//        } catch (JsonProcessingException e) {
//            logger.debug("\n failed to serialize the object");
//        } catch (IOException e) {
//            logger.debug("\n failed to deserialize the object");
//        }
//        logger.debug("\n retrieved the list of airline updates");
//        Assert.assertEquals(airlineUpdates, bookingEngineList);
//        logger.debug("\n Exiting AirlineUpdatesServiceImplTest :: testGetListOfAirlineUpdates method");
//    }
//
//    @Test
//    public void testConfirmAirlineUpdates() {
//        logger.debug("\n Entering AirlineUpdatesServiceImplTest :: testConfirmAirlineUpdates method");
//        try {
//            bookingService.confirmAirlineUpdates(bookingEngineList);
//        } catch (JsonProcessingException e) {
//            logger.debug("\n failed to serialize the object");
//        } catch (IOException e) {
//            logger.debug("\n failed to deserialize the object");
//        }
//        logger.debug("\n updated airline updates to BE");
//        AirlineUpdates airlineUpdates = airlineUpdatesRepository.getAirlineUpdate(booking.getBookID(), booking.getProducts().get(0).getOrderID());
//        Assert.assertEquals(booking.getBookId(), airlineUpdates.getBookId());
//        Assert.assertEquals(booking.getProducts().get(0).getOrderID(), airlineUpdates.getOrderID());
//        Assert.assertEquals(true, airlineUpdates.getBookingUpdated());
//        logger.debug("\n Exiting AirlineUpdatesServiceImplTest :: testConfirmAirlineUpdates method");
//    }
//
//    @Test
//    public void testUpdateFlightDetails() {
//        logger.debug("\n Entering AirlineUpdatesServiceImplTest :: testUpdateFlightDetails method");
//        Map<String, String> response = new HashMap<>();
//        try {
//            logger.debug("\n testing update flight details");
//            response = bookingService.updateFlightDetails(resource);
//        } catch (OperationException e) {
//            logger.debug("\n failed to update flight details as the required parameters are missing");
//        }
//        Assert.assertEquals("Flight details updated successfully!", response.get("message"));
//        logger.debug("\n Exiting AirlineUpdatesServiceImplTest :: testUpdateFlightDetails method");
//    }
//
//    @Test
//    public void testFailToUpdateFlightDetails() {
//        logger.debug("\n Entering AirlineUpdatesServiceImplTest :: testFailToUpdateFlightDetails method");
//        resource.getFlightDetails().getOriginDestinationOptions().get(0).getFlightSegment().get(0).setArrivalDate(null);
//        Map<String, String> response = new HashMap<>();
//        try {
//            logger.debug("\n testing update flight details");
//            response = bookingService.updateFlightDetails(resource);
//        } catch (OperationException e) {
//            logger.debug("\n failed to update flight details as the required parameters are missing");
//            response.put("message",e.getErrorCode());
//        }
//        Assert.assertEquals(Constants.ER17,response.get("message"));
//        logger.debug("\n Exiting AirlineUpdatesServiceImplTest :: testFailToUpdateFlightDetails method");
//    }
//
//    /*@Test
//    public void testGetListOFAirlineUpdatesRest() throws IOException, OperationException {
//        bookingService.saveAirlineUpdates(bookingEngineList,false);
//        HttpUriRequest request = new HttpGet("http://localhost:8067/booking/getAirlineUpdates");
//        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
//
//        Assert.assertThat(
//                httpResponse.getStatusLine().getStatusCode(),
//                equalTo(HttpStatus.SC_OK));
//    }*/
//}
