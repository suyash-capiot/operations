package com.coxandkings.travel.operations.controller.manageofflinebooking;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.ErrorResponseResource;
import com.coxandkings.travel.operations.service.manageofflinebooking.manualofflinebooking.ManualOfflineBookingService;
import com.coxandkings.travel.operations.utils.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("offlineBooking")
@CrossOrigin(origins = "*")
public class ManualOfflineBookingController {
    private static Logger logger = LogManager.getLogger(ManualOfflineBookingController.class);

    @Autowired
    private ManualOfflineBookingService manualOfflineBookingService;

    @PostMapping(value = "/v1/fetchData", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONObject fetchDataFromMDM(@RequestBody JSONObject criteriaJson) throws OperationException {
        logger.info("Calling fetchDataMDM");
        JSONObject jsObjFetchData = new JSONObject();
        try {
            jsObjFetchData = manualOfflineBookingService.fetchDataFromMDM(criteriaJson);
        } catch (OperationException oe) {
            logger.error("An exceptions occurred while fetchDataFromMDM", oe.getErrors());
            throw oe;
        } catch (Exception e) {
            logger.error("An exceptions occurred while fetchDataFromMDM", e);
            throw new OperationException(Constants.OPS_ERR_20800);
        }
        logger.info("Calling fetchDataMDM");
        return jsObjFetchData;
    }

    @PostMapping(value = "/v1/createBooking", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createManualOfflineBooking(@RequestBody JSONObject createBookingJSON) throws OperationException {
        try {
            logger.info("Entered into createManualOfflineBooking");
            JSONObject res = manualOfflineBookingService.createManualOfflineBooking(createBookingJSON);
            return new ResponseEntity<String>(res.toString(), HttpStatus.OK);
        } catch (OperationException oe) {
            logger.error("An exceptions occurred createManualOfflineBooking", oe.getErrors());
            throw oe;
        } catch (Exception e) {
            logger.error("An exceptions occurred while creating manual offline booking", e);
            throw new OperationException(Constants.OPS_ERR_20800);
        }
    }

    @PostMapping(value = "/v1/saveBooking", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> saveManualOfflineBooking(@RequestBody JSONObject reqJson) throws OperationException {
        try {
            logger.info("Entered into saveManualOfflineBooking");
            JSONObject res = manualOfflineBookingService.saveManualOfflineBooking(reqJson);
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (OperationException oe) {
            logger.error("An exceptions occurred while saving offline booking", oe.getErrors());
            throw oe;
        } catch (Exception e) {
            logger.error("An exceptions occurred while saving offline booking", e);
            throw new OperationException(Constants.OPS_ERR_20800);
        }
    }

    @GetMapping("/v1/getBooking/{bookingRefId}")
    public HttpEntity<JSONObject> getBooking(@PathVariable String bookingRefId) throws OperationException {
        try {
            JSONObject toReturn = manualOfflineBookingService.getBooking(bookingRefId);
            return new ResponseEntity<>(toReturn, HttpStatus.OK);
        } catch (OperationException oe) {
            logger.error("An exceptions occurred while calling getBooking method", oe.getErrors());
            throw oe;
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_11200);
        }
    }

    @PostMapping(value = "/v1/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> searchOfflineBooking(@RequestBody JSONObject criteriaJson) throws OperationException {
        logger.info("calling search method");
        Map<String, Object> res = manualOfflineBookingService.searchOfflineBookings(criteriaJson);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping(value = "/v1/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ErrorResponseResource updateOfflineBooking(@RequestBody JSONObject criteriaJson) throws OperationException {
        logger.info("calling save method");
        return manualOfflineBookingService.updateOfflineBookings(criteriaJson);
    }

    @GetMapping("/v1/deleteBooking/{offlineBookId}")
    public HttpEntity<JSONObject> deleteBooking(@PathVariable String offlineBookId) throws OperationException {
        try {
            String toReturn = manualOfflineBookingService.deleteBooking(offlineBookId);
            JSONObject res = new JSONObject();
            res.put("message",toReturn);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }  catch (OperationException oe) {
            logger.error("An exceptions occurred while deleteBooking", oe.getErrors());
            throw oe;
        }
        catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_11200);
        }
    }

    @GetMapping("/v1/find/{offlineBookId}")
    public HttpEntity<JSONObject> findById(@PathVariable String offlineBookId) throws OperationException {
        try {
            JSONObject product = manualOfflineBookingService.findById(offlineBookId);
            return new ResponseEntity<>(product, HttpStatus.OK);
        }  catch (OperationException oe) {
            logger.error("An exceptions occurred while finding booking", oe.getErrors());
            throw oe;
        }catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_11200);
        }
    }

    @PostMapping("/v1/getClientCurrency")
    public HttpEntity<Object> getClientCcy(@RequestBody JSONObject req)throws OperationException{
        try{
            JSONObject product = manualOfflineBookingService.getClientCcy(req);
            return new ResponseEntity<>(product, HttpStatus.OK);
        }catch (OperationException oe){
            logger.info("Exception in retrive client currency ");
            req.put("clientCurrency","");
            return null;
        }
    }
}
