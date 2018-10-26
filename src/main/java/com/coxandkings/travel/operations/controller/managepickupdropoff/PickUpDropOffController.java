package com.coxandkings.travel.operations.controller.managepickupdropoff;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.managepickupdropoff.PickUpDropOff;
import com.coxandkings.travel.operations.resource.managepickupdropoff.PickUpDropOffResource;
import com.coxandkings.travel.operations.service.managepickupdropoff.PickUpDropOffService;
import com.coxandkings.travel.operations.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/manage/pickupdropoff")
@CrossOrigin(value = "*")
public class PickUpDropOffController
{

    @Autowired
    private PickUpDropOffService pickUpDropOffService;


    @GetMapping(value = "/v1/getPickUpDropOff/{bookingRefId}/{orderId}")
    public ResponseEntity<PickUpDropOff> getPickUpDropOff(@PathVariable("bookingRefId") String bookingRefId,@PathVariable("orderId") String orderId) throws OperationException {
        try {
            PickUpDropOff pickUpDropOff = pickUpDropOffService.searchPickUpDropOff(bookingRefId, orderId);
            return new ResponseEntity<>(pickUpDropOff, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_20900);
        }
    }

    @PostMapping(value = "/v1/save")
    public ResponseEntity<PickUpDropOff> savePickUpDropOffDetails(@RequestBody PickUpDropOffResource resource) throws Exception {
        try {
            PickUpDropOff pickUpDropOff = pickUpDropOffService.savePickUpDropOff(resource);
            return new ResponseEntity<>(pickUpDropOff, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_20901);
        }
    }

    @PutMapping(value = "/v1/update")
    public ResponseEntity<PickUpDropOff> updatePickUpDropOff(@RequestBody PickUpDropOffResource resource) throws OperationException {
        try {
            PickUpDropOff pickUpDropOff = pickUpDropOffService.updatePickUpDropOff(resource);
            return new ResponseEntity<>(pickUpDropOff, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_20902);
        }
    }


}
