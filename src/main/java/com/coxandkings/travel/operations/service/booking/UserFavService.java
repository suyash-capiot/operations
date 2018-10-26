package com.coxandkings.travel.operations.service.booking;

import com.coxandkings.travel.operations.criteria.booking.UserFavCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.booking.UserFavourite;
import com.coxandkings.travel.operations.resource.MessageResource;
import org.json.JSONObject;


public interface UserFavService {

    String getUserFavByCrteria(UserFavCriteria criteria);

    String saveUserFav(JSONObject req) throws OperationException;

    UserFavourite getUserFavById(String id);

    MessageResource deleteUserFav(String id) throws OperationException;
}
