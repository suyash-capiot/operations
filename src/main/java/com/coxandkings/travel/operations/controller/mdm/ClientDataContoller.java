package com.coxandkings.travel.operations.controller.mdm;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.service.mdmservice.ClientMasterDataService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

@RestController
@CrossOrigin(value = "*")
@RequestMapping("/clientData")
public class ClientDataContoller {

    @Autowired
    private ClientMasterDataService clientMasterDataService;

    @PostMapping(value = "/autosuggestion/clientName" , produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getClientNames(InputStream stream) throws OperationException {

        JSONObject object = new JSONObject(new JSONTokener(stream));
        ResponseEntity<Object> res = null;
        JSONArray array = clientMasterDataService.getClientNames(object);
        return new ResponseEntity<>(array, HttpStatus.OK);
    }

    //For Corporate Travellers
    @PostMapping(value = "/autosuggestion/clientDetails" , produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getClientDetails(InputStream stream) throws OperationException {

        JSONObject object = new JSONObject(new JSONTokener(stream));
        ResponseEntity<Object> res = null;
        JSONArray array = clientMasterDataService.getCorporateTravellersClient(object);
        return new ResponseEntity<>(array, HttpStatus.OK);
    }

    @PostMapping(value = "/autosuggestion/users" , produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JSONArray> getUsersFromUserManagement(InputStream stream) {

        JSONObject object = new JSONObject(new JSONTokener(stream));
        JSONArray array = clientMasterDataService.getUsersFromUserManagement(object);
        return new ResponseEntity<>(array, HttpStatus.OK);
    }

}
