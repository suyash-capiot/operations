//package com.coxandkings.travel.operations.service.booking;
//
//import com.coxandkings.travel.operations.OperationsApplicationTests;
//import com.coxandkings.travel.operations.criteria.booking.UserFavCriteria;
//import com.coxandkings.travel.operations.exceptions.OperationException;
//import com.coxandkings.travel.operations.model.booking.UserFavourite;
//import com.coxandkings.travel.operations.resource.booking.*;
//import org.apache.log4j.Logger;
//import org.junit.Assert;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//
//public class UserFavServiceImplTest extends OperationsApplicationTests {
//
//    private static final Logger logger = Logger.getLogger(UserFavServiceImplTest.class);
//
//    private static UserFavResource resource = null;
//    private static FavProductDetailsResource favProductDetailsResource = null;
//    private static FavCompanyDetailsResource favCompanyDetailsResource = null;
//    private static FavBookingDetailsResource favBookingDetailsResource = null;
//    private static FavCliAndPassengerDetailsResource clientAndPassengerDetailsResource = null;
//
//    @Autowired
//    private UserFavService service;
//
//    static {
//        resource = new UserFavResource();
//        resource.setFavName("TestFav");
//        resource.setDefaultFav(false);
//        resource.setUserId("Operation User");
//        resource.setDelete(false);
//
//        favProductDetailsResource = new FavProductDetailsResource();
//        favProductDetailsResource.setAirlineName("Indigo");
//        favProductDetailsResource.setAirlinePNR("PNR1");
//        favProductDetailsResource.setGsdPnr("Gpnr");
//        resource.setFavProductDetailsResource(favProductDetailsResource);
//
//        favBookingDetailsResource = new FavBookingDetailsResource();
//        favBookingDetailsResource.setBookingRefId("123");
//        favBookingDetailsResource.setBookingFromDate(System.currentTimeMillis());
//        resource.setFavBookingDetailsResource(favBookingDetailsResource);
//
//        favCompanyDetailsResource = new FavCompanyDetailsResource();
//        favCompanyDetailsResource.setBuId("bu1");
//        favCompanyDetailsResource.setCompanyId("21");
//        resource.setFavCompanyDetailsResource(favCompanyDetailsResource);
//
//        clientAndPassengerDetailsResource = new FavCliAndPassengerDetailsResource();
//        clientAndPassengerDetailsResource.setClientCategoryId("1");
//        clientAndPassengerDetailsResource.setClientId("321");
//        resource.setFavCliAndPassengerDetailsResource(clientAndPassengerDetailsResource);
//    }
//
//    @Test
//    public void testCreateUserFav() {
//        logger.info("Entered UserFavServiceImplTest :: createUserFav method ");
//        try {
//            UserFavourite userFavourite = service.saveUserFav(resource);
//            Assert.assertEquals(userFavourite.getFavName(), resource.getFavName());
//            Assert.assertEquals(userFavourite.getUserID(), resource.getUserID());
//            Assert.assertEquals(userFavourite.getDefaultFav(), resource.getDefaultFav());
//            Assert.assertEquals(userFavourite.getFavProductDetails().getFavId(), userFavourite.getId());
//            logger.info("User favourites name " + userFavourite.getFavName());
//        } catch (OperationException e) {
//            Assert.fail(" Failed due to exception....");
//            logger.error(" Failed creation of user favourites due to exception ");
//        } finally {
//            logger.info("Exited UserFavServiceImplTest :: createUserFav method ");
//        }
//    }
//
//    @Test
//    public void testDeleteUserFav() {
//        logger.info("Entered UserFavServiceImplTest :: deleteUserFav method ");
//        try {
//            resource.setDelete(true);
//            UserFavourite userFavourite = service.deleteUserFav(resource);
//            logger.info("Deleted the user favourite");
//            Assert.assertEquals(userFavourite.getDelete(), resource.getDelete());
//            Assert.assertEquals(userFavourite.getFavName(), resource.getFavName());
//            Assert.assertEquals(userFavourite.getUserID(), resource.getUserID());
//            Assert.assertEquals(userFavourite.getDefaultFav(), resource.getDefaultFav());
//            Assert.assertEquals(userFavourite.getFavProductDetails().getFavId(), userFavourite.getId());
//        } catch (OperationException e) {
//            Assert.fail(" Failed due to exception....");
//            logger.error(" Failed deletion of user favourites due to exception ");
//        } finally {
//            logger.info("Exited UserFavServiceImplTest :: deleteUserFav method");
//        }
//    }
//
//    @Test
//    public void testRetrieveUserFav() {
//        logger.info("Entered UserFavServiceImplTest :: retrieveUserFav method");
//        try {
//            resource.setDelete(true);
//            UserFavCriteria criteria = new UserFavCriteria();
//            criteria.setUserId(resource.getUserId());
//            List<UserFavourite> userFavourites = service.getUserFavByCrteria(criteria);
//            logger.info("Retrieved the user favourite");
//            for (UserFavourite userFavourite : userFavourites) {
//                Assert.assertEquals(userFavourite.getUserID(), resource.getUserID());
//            }
//        } catch (Exception e) {
//            Assert.fail(" Failed due to exception....");
//            logger.error(" Failed user favourites retrieval due to exception ");
//        }finally {
//            logger.info("Exited UserFavServiceImplTest :: retrieveUserFav method");
//        }
//    }
//}
