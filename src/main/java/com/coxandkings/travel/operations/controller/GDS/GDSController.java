package com.coxandkings.travel.operations.controller.GDS;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.service.GDS.GDSService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/GDS")
@CrossOrigin("*")
public class GDSController {

    @Autowired
    private GDSService gDSService;

    @PostMapping(path = "/v1/sendcommunication", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sendCommunication(@RequestBody JSONObject req) throws OperationException {
        String response = gDSService.sendCommunication(req);
        return new ResponseEntity<String>(response, HttpStatus.OK);

    }

}
