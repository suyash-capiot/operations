//package com.coxandkings.travel.operations.service.booking;
//
//import com.coxandkings.travel.operations.OperationsApplicationTests;
//import com.coxandkings.travel.operations.exceptions.OperationException;
//import com.coxandkings.travel.operations.resource.booking.BookingStatusResource;
//import com.coxandkings.travel.operations.resource.booking.PotentialListResource;
//import com.coxandkings.travel.operations.resource.booking.TimeLimitExpiryResource;
//import com.coxandkings.travel.operations.utils.Constants;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.client.methods.HttpUriRequest;
//import org.apache.http.batchjob.client.HttpClientBuilder;
//import org.apache.log4j.Logger;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.client.RestClientException;
//
//import java.io.IOException;
//import java.text.ParseException;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.Assert.assertEquals;
//
//public class BookingServiceImplTest extends OperationsApplicationTests {
//
//    @Autowired
//    private BookingService bookingService;
//
//    private BookingStatusResource bookingStatusResource;
//    private TimeLimitExpiryResource timeLimitExpiryResource;
//
//    @Value(value = "${change_passenger_name}")
//    private String changePassengerName;
//
//    @Value(value = "${add_product_info}")
//    private String addProductInfo;
//
//    @Value(value = "${get_other_productDetails}")
//    private String getOtherProductDetails;
//
//    private static final Logger logger=Logger.getLogger(BookingServiceImplTest.class);
//
//    @Before
//    public void setData(){
//        bookingStatusResource=new BookingStatusResource();
//        bookingStatusResource.setBookID("CNK-00001");
//        bookingStatusResource.setOrderID("92d4b700-fc42-4ea1-b16f-0cd119b755d2");
//        bookingStatusResource.setStatus("XL");
//        timeLimitExpiryResource=new TimeLimitExpiryResource();
//        timeLimitExpiryResource.setBookID("TL-CNK-00007");
//        timeLimitExpiryResource.setOrderID("437cb6ad-2fb1-4ffe-b2df-5da58ad1d194");
//        timeLimitExpiryResource.setNewDate("2018-02-19T23:37:36.952");
//    }
//
//   @Test
//    public void testUpdateBookingStatus() throws OperationException {
//        logger.debug("\n entering testUpdateBookingStatus method");
//        Map map=bookingService.updateBookingStatus(bookingStatusResource);
//        assertEquals("successfully updated",map.get("message"));
//        logger.debug("\n exiting testUpdateBookingStatus method");
//   }
//
//   /* @Test
//    public void testTimeLimitDateExtension() throws ParseException, OperationException {
//        logger.debug("\n entering testTimeLimitDateExtension method");
//        Map map=bookingService.timeLimitDateExtension(timeLimitExpiryResource);
//        assertEquals("successfully updated",map.get("message"));
//        logger.debug("\n exiting testTimeLimitDateExtension method");
//    }*/
//
//    @Test
//    public void testFailToUpdateBookingStatusIfBookIDisInCrct() throws OperationException {
//       logger.debug("\n entering testFailToUpdateBookingStatusifBookIDisInCrct method");
//        bookingStatusResource.setBookID("CNK-0000");
//        try {
//            bookingService.updateBookingStatus(bookingStatusResource);
//        }
//        catch (RestClientException e){
//            assertEquals("500 Internal Server Error",e.getMessage());
//        }
//        finally {
//            logger.debug("\n exiting testFailToUpdateBookingStatusIfBookIDisInCrct method");
//        }
//    }
//
//    @Test
//    public void testFailToUpdateBookingStatusIfOrderIdisInCrct() {
//       logger.debug("\n entering testFailToUpdateBookingStatusifOrderIdisInCrct method");
//        bookingStatusResource.setOrderID("CNK-0000");
//        try {
//            bookingService.updateBookingStatus(bookingStatusResource);
//        }
//        catch (OperationException e){
//            assertEquals(Constants.ER32,e.getErrorCode());
//        }
//        finally {
//            logger.debug("\n exiting testFailToUpdateBookingStatusIfOrderIdisInCrct method");
//
//        }
//    }
//
//    @Test
//    public void testFailToUpdateBookingStatusIfStatusisInCrct() {
//        logger.debug("\n entering testFailToUpdateBookingStatusifStatusisInCrct method");
//        bookingStatusResource.setStatus("abc");
//        try {
//            bookingService.updateBookingStatus(bookingStatusResource);
//        }
//        catch (OperationException e){
//            assertEquals(Constants.ER35,e.getErrorCode());
//        }
//        finally {
//            logger.debug("\n entering testFailToUpdateBookingStatusIfStatusisInCrct method");
//
//        }
//    }
//
//    @Test
//    public void testFailToUpdateExpiryIfDateisInCrct() throws  ParseException {
//        logger.debug("\n entering testFailToUpdateExpiryIfDateisInCrct method");
//        timeLimitExpiryResource.setNewDate("abc");
//        try {
//            bookingService.timeLimitDateExtension(timeLimitExpiryResource);
//        }
//        catch (OperationException e){
//            assertEquals(Constants.ER31,e.getErrorCode());
//        }
//        finally {
//            logger.debug("\n entering testFailToUpdateExpiryIfDateisInCrct method");
//        }
//    }
//
//    @Test
//    public void testFailToUpdateExpiryDateIfBehindCurrentDate() throws  ParseException {
//        logger.debug("\n entering testFailToUpdateExpiryDateIfBehindCurrentDate method");
//        timeLimitExpiryResource.setNewDate("2018-02-16T23:37:36.952");
//        try {
//            bookingService.timeLimitDateExtension(timeLimitExpiryResource);
//        }
//        catch (OperationException e){
//            assertEquals(Constants.ER28,e.getErrorCode());
//        }
//    }
//
//    @Test
//    public void testFailToUpdateExpiryDateIfNotTimeLimitBook() throws ParseException {
//        logger.debug("\n entering testFailToUpdateExpiryDateIfNotTimeLimitBook method");
//        timeLimitExpiryResource.setBookID("CNK-00001");
//        try {
//            bookingService.timeLimitDateExtension(timeLimitExpiryResource);
//        }
//        catch (OperationException e){
//            assertEquals(Constants.ER29,e.getErrorCode());
//        }
//    }
//
//    @Test
//    public void addProductInfo() throws IOException {
//        //HttpUriRequest request = new HttpPost(addProductInfo);
//        //HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
//
//        //Assert.assertEquals(400,httpResponse.getStatusLine().getStatusCode());
//    }
//
//    @Test
//    public void getOtherProductInfo() throws IOException, OperationException {
//
//        bookingService.getOtherProductInfo("123", " 123");
//
//        String url = getOtherProductDetails + "?bookingRefID=123&productId=123";
//        System.out.println("url:" + url);
//        HttpUriRequest request = new HttpGet(url);
//        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
//        Assert.assertEquals(200, httpResponse.getStatusLine().getStatusCode());
//    }
//
//    @Test
//    public void getPotentialList() throws OperationException {
//        logger.debug("\n Entering AddProductInfoServiceImplTest :: getPotentialList method");
//
//        List<PotentialListResource> list= bookingService.getPotentialList();
//        Assert.assertEquals(1,list.size());
//
//        logger.debug("\n Exiting AddProductInfoServiceImplTest :: getPotentialList method");
//    }
//
//    @Test
//    public void changePassengerName() throws IOException,OperationException {
//        List<PotentialListResource> list= bookingService.getPotentialList();
//
//        bookingService.changePassengerName(list.get(0));
//        HttpUriRequest request = new HttpPost(changePassengerName);
//        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
//
//        Assert.assertEquals(400, httpResponse.getStatusLine().getStatusCode());
//    }
//
//}