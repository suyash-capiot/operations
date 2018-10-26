package com.coxandkings.travel.operations.controller.failure;

import com.coxandkings.travel.operations.service.failure.FailureThresholdConfigurationService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;

@RestController
@RequestMapping("/failures/thresholdConfiguration")
@CrossOrigin(origins = "*")
public class ManageFailureThresholdController {

    @Autowired
    private FailureThresholdConfigurationService failureThresholdConfigurationService;

    //threshold configuration is assumed to be configured for one pax
    @PostMapping(path = "/v1/addThresholdConfiguration", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addAlternateOptions(@RequestBody JSONObject req) {
        String response = failureThresholdConfigurationService.addThresholdConfiguration(req);
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/v1/searchThresholdConfiguration", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JSONArray> searchAlternateOptions(InputStream req) {
        JSONTokener jsonTok = new JSONTokener(req);
        JSONObject reqJson = new JSONObject(jsonTok);
        JSONArray response = failureThresholdConfigurationService.searchThresholdConfiguration(reqJson);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/v1/fetchThresholdConfiguration/{configurationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> fetchAlternateOptions(@PathVariable("configurationId") String configurationId) {
        String res = failureThresholdConfigurationService.fetchThresholdConfiguration(configurationId);
        return new ResponseEntity<String>(res, HttpStatus.OK);
    }


}
