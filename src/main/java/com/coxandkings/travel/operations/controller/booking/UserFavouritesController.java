package com.coxandkings.travel.operations.controller.booking;


import com.coxandkings.travel.operations.criteria.booking.UserFavCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.MessageResource;
import com.coxandkings.travel.operations.service.booking.UserFavService;
import com.coxandkings.travel.operations.utils.Constants;
import com.coxandkings.travel.operations.utils.MessageFormatter;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;

@RestController
@RequestMapping("/userfavourites")
@CrossOrigin(origins = "*")
public class UserFavouritesController {

    @Autowired
    private UserFavService userFavouritesService;

    @Autowired
    private MessageFormatter messageFormatter;

    @RequestMapping(value = "/v1/retrieveuserfav", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> retrieveFavourites(@RequestParam("userId") String userId) throws OperationException {
        UserFavCriteria criteria = new UserFavCriteria();
        criteria.setUserId(userId);
        String userFavourites = null;
        try {
            userFavourites = userFavouritesService.getUserFavByCrteria(criteria);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_10600);
        }
        return new ResponseEntity<String>(userFavourites, HttpStatus.OK);
    }

    @PostMapping(value = "/v1/saveuserfav", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveorUpdateUserFavourites(InputStream req) throws OperationException {
        try {
            JSONTokener jsonTok = new JSONTokener(req);
            JSONObject reqJson = new JSONObject(jsonTok);
            String userFavourite = userFavouritesService.saveUserFav(reqJson);
            return new ResponseEntity<String>(userFavourite, HttpStatus.OK);
        } catch (OperationException e) {
            throw new OperationException(Constants.OPS_ERR_10601);
        }
    }

    @RequestMapping(value = "/v1/deleteuserfav/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<MessageResource> deleteFavourites(@PathVariable String id) throws OperationException {
        MessageResource messageResource = new MessageResource();
        try {
            if (id != null) {
                messageResource = userFavouritesService.deleteUserFav(id);
            }
        } catch (OperationException e) {
            throw new OperationException(Constants.OPS_ERR_10602);
        }
        return new ResponseEntity<MessageResource>(messageResource, HttpStatus.OK);
    }
}

