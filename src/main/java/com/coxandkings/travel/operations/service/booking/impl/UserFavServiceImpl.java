package com.coxandkings.travel.operations.service.booking.impl;

import com.coxandkings.travel.operations.criteria.booking.UserFavCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.booking.UserFavourite;
import com.coxandkings.travel.operations.repository.booking.UserFavRepository;
import com.coxandkings.travel.operations.resource.MessageResource;
import com.coxandkings.travel.operations.service.booking.UserFavService;
import com.coxandkings.travel.operations.utils.Constants;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service("UserFavService")
@Transactional
public class UserFavServiceImpl implements UserFavService {
    private static Logger log = LogManager.getLogger(UserFavService.class);
    @Autowired
    private UserFavRepository userFavRepository;

    @Override
    public String getUserFavByCrteria(UserFavCriteria criteria) {
        List<UserFavourite> UserFavourites = userFavRepository.getUserFavouritesByCriteria(criteria);
        JSONArray userFavouriteArray = new JSONArray();

        for (UserFavourite userFavourite : UserFavourites) {
            JSONObject userFavouriteJson = new JSONObject();
            userFavouriteJson.put("userFavouriteSearchReq", new JSONObject(userFavourite.getUserFavouriteSearchReq()));
            userFavouriteJson.put("id", userFavourite.getId());
            userFavouriteJson.put("favName", userFavourite.getFavName());
            userFavouriteJson.put("userId", userFavourite.getUserId());

            userFavouriteArray.put(userFavouriteJson);

        }
        return userFavouriteArray.toString();
    }

    @Override
    @Transactional
    public String saveUserFav(JSONObject req) throws OperationException {

        String id = req.getString("id");
        String favName = req.getString("favName");
        String userId = req.getString("userId");

        UserFavCriteria criteria = new UserFavCriteria();

        criteria.setFavName(favName);
        criteria.setUserId(userId);

        //String res = getUserFavByCrteria(criteria);

        if (!(getUserFavByCrteria(criteria).equalsIgnoreCase("[]"))) {
            throw new OperationException("Duplicate Name Found");
        }

        UserFavourite userFav = new UserFavourite();

        JSONObject userFavouriteJson = new JSONObject();

        if (!StringUtils.isEmpty(id)) {
            UserFavourite existingUserFav = userFavRepository.getUserFavouriteById(id);
            if (existingUserFav == null) {
                log.error("User Fav not found id:" + id);
                throw new OperationException(Constants.ER01);
            }
            userFav = userFavRepository.saveUserFavourite(existingUserFav);
        } else {
            userFav.setFavName(favName);
            userFav.setUserId(userId);
            userFav.setUserFavouriteSearchReq(req.getJSONObject("userFavouriteSearchReq").toString());
            userFav.setCreatedTime(System.currentTimeMillis());
            userFav = userFavRepository.saveUserFavourite(userFav);
        }

        userFavouriteJson.put("userFavouriteSearchReq", new JSONObject(userFav.getUserFavouriteSearchReq()));
        userFavouriteJson.put("id", userFav.getId());
        userFavouriteJson.put("favName", userFav.getFavName());
        userFavouriteJson.put("userId", userFav.getUserId());

        return userFavouriteJson.toString();

    }

//    private void updateFavBookgingDetails(UserFavourite userFav, FavBookingDetailsResource resource) {
//        FavBookingDetails favBookingDetails = null;
//        String id = resource.getId();
//        if (!StringUtils.isEmpty(id)) {
//            favBookingDetails = userFav.getFavBookingDetails();
//            CopyUtils.copy(resource, favBookingDetails);
//        } else {
//            favBookingDetails = new FavBookingDetails();
//            CopyUtils.copy(resource, favBookingDetails);
//            favBookingDetails.setFavId(userFav.getId());
//        }
//
//        userFav.setFavBookingDetails(favBookingDetails);
//    }
//
//    private void updateFavProductDetails(UserFavourite userFav, FavProductDetailsResource resource) {
//        FavProductDetails favProductDetails = null;
//        String id = resource.getId();
//        if (!StringUtils.isEmpty(id)) {
//            favProductDetails = userFav.getFavProductDetails();
//            CopyUtils.copy(resource, favProductDetails);
//        } else {
//            favProductDetails = new FavProductDetails();
//            CopyUtils.copy(resource, favProductDetails);
//            favProductDetails.setFavId(userFav.getId());
//        }
//
//        userFav.setFavProductDetails(favProductDetails);
//    }
//
//    private void updateFavCliAndPassengerDetails(UserFavourite userFav, FavCliAndPassengerDetailsResource resource) {
//        FavClientAndPassengerDetails favClientAndPassengerDetails = null;
//        String id = resource.getId();
//        if (!StringUtils.isEmpty(id)) {
//            favClientAndPassengerDetails = userFav.getFavCliAndPassengerDetails();
//            CopyUtils.copy(resource, favClientAndPassengerDetails);
//        } else {
//            favClientAndPassengerDetails = new FavClientAndPassengerDetails();
//            CopyUtils.copy(resource, favClientAndPassengerDetails);
//            favClientAndPassengerDetails.setFavId(userFav.getId());
//        }
//
//        userFav.setFavCliAndPassengerDetails(favClientAndPassengerDetails);
//    }
//
//    private void updateFavCompanyDetails(UserFavourite userFav, FavCompanyDetailsResource resource) {
//        FavCompanyDetails favCompanyDetails = null;
//        String id = resource.getId();
//        if (!StringUtils.isEmpty(id)) {
//            favCompanyDetails = userFav.getFavCompanyDetails();
//            CopyUtils.copy(resource, favCompanyDetails);
//        } else {
//            favCompanyDetails = new FavCompanyDetails();
//            CopyUtils.copy(resource, favCompanyDetails);
//
//            favCompanyDetails.setFavId(userFav.getId());
//        }
//
//        userFav.setFavCompanyDetails(favCompanyDetails);
//    }

    @Override
    public UserFavourite getUserFavById(String id) {
        return userFavRepository.getUserFavouriteById(id);
    }

    @Override
    @Transactional
    public MessageResource deleteUserFav(String id) throws OperationException {

        MessageResource messageResource = new MessageResource();
        messageResource.setMessage(userFavRepository.deleteById(id));

        return messageResource;
    }
}
