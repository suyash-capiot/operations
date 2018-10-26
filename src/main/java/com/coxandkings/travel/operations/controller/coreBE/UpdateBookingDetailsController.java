package com.coxandkings.travel.operations.controller.coreBE;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.MessageResource;
import com.coxandkings.travel.operations.resource.booking.UpdateAccommodationDetailResource;
import com.coxandkings.travel.operations.resource.booking.UpdateFlightDetailResource;
import com.coxandkings.travel.operations.resource.booking.UpdatePaxInfoResource;
import com.coxandkings.travel.operations.service.booking.UpdateBookingDetailsService;
import com.coxandkings.travel.operations.utils.Constants;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/updateBookingDetails")
public class UpdateBookingDetailsController {

    private static Logger logger = LogManager.getLogger(RetrieveBookingDetailsController.class);

    @Autowired
    private UpdateBookingDetailsService updateBookingDetailsService;

    @PostMapping("/v1/updatePaxInfo")
    public ResponseEntity<MessageResource> updateFlightPaxInfo(@RequestBody UpdatePaxInfoResource updatePaxInfoResources) throws OperationException {
        try {
            logger.info("-- RetrieveBookingDetailsController : POST : /updatePaxInfo --");
            logger.info("Parameters for above call : " + updatePaxInfoResources);
            MessageResource messageResource = updateBookingDetailsService.updatePaxInfo(updatePaxInfoResources);
            return new ResponseEntity<MessageResource>(messageResource, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_11241);
        }
    }

    @PostMapping("/v1/updateFlightDetails")
    public ResponseEntity<MessageResource> updateOpsOriginDestinationOptions(@RequestBody UpdateFlightDetailResource
                                                                                     updateFlightDetailResource) throws OperationException {
        try {
            logger.info("-- RetrieveBookingDetailsController : POST : /updateFlightDetails --");
            logger.info("Parameters for above call : " + updateFlightDetailResource.toString());
            MessageResource messageResource = updateBookingDetailsService.updateOriginDestinationInformation(updateFlightDetailResource);
            return new ResponseEntity<MessageResource>(messageResource, HttpStatus.OK);
        } catch (OperationException e) {
            throw new OperationException(Constants.OPS_ERR_11242);
        }
    }

    @PostMapping("/v1/updateFlightConfirmationDetails")
    public ResponseEntity<MessageResource> updateFlightConfirmationDetails(@RequestBody UpdateFlightDetailResource
                                                                                   updateFlightDetailResource) throws OperationException {

        logger.info("-- RetrieveBookingDetailsController : POST : /updateFlightConfirmationDetails --");
        logger.info("Parameters for above call : " + updateFlightDetailResource);
        updateBookingDetailsService.updateFlightConfirmationDetails(updateFlightDetailResource);
        MessageResource messageResource=new MessageResource();
        messageResource.setMessage("Confirmation Details Updated Successfully");
        return new ResponseEntity<MessageResource>(messageResource, HttpStatus.OK);

    }
    
    @PostMapping("/v1/updateAccommodationDetails")
    public ResponseEntity<MessageResource> updateAccommodationDetails(@RequestBody UpdateAccommodationDetailResource
                                                                              updateAccommodationDetailResource) throws OperationException {
        try {
            logger.info("-- RetrieveBookingDetailsController : POST : /updateAccommodationDetails --");
            logger.info("Parameters for above call : " + updateAccommodationDetailResource);
            MessageResource messageResource = updateBookingDetailsService.updateAccommodationDetails(updateAccommodationDetailResource);
            return new ResponseEntity<MessageResource>(messageResource, HttpStatus.OK);
        } catch (OperationException e) {
            throw new OperationException(Constants.OPS_ERR_11243);
        }
    }

    @GetMapping("/v1/paxStatus")
    public ResponseEntity<List<String>> getPaxStatus()
    {
        List<String> paxStatus = updateBookingDetailsService.getPaxStatus();
        return new ResponseEntity<List<String>>(paxStatus,HttpStatus.OK);
    }

}
