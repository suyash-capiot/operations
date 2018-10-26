package com.coxandkings.travel.operations.repository.booking;

import com.coxandkings.travel.operations.criteria.booking.UserFavCriteria;
import com.coxandkings.travel.operations.model.booking.UserFavourite;

import java.util.List;

public interface UserFavRepository {
    UserFavourite saveUserFavourite(UserFavourite userFavourite);

    List<UserFavourite> getUserFavouritesByCriteria(UserFavCriteria criteria);

    UserFavourite getUserFavouriteById(String id);

    List<UserFavourite> getUserFavouriteByFavName(UserFavCriteria criteria);

    String deleteById(String id);
}
