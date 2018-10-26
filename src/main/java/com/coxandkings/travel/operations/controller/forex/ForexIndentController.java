package com.coxandkings.travel.operations.controller.forex;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.forex.ForexBooking;
import com.coxandkings.travel.operations.model.forex.ForexIndent;
import com.coxandkings.travel.operations.resource.forex.ForexBookingResource;
import com.coxandkings.travel.operations.resource.forex.ForexIndentResource;
import com.coxandkings.travel.operations.service.booking.OpsBookingService;
import com.coxandkings.travel.operations.service.forex.ForexIndentService;
import com.coxandkings.travel.operations.utils.Constants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@CrossOrigin(value = "*")
@RequestMapping("/forexIndent")
public class ForexIndentController {

    private Logger logger = Logger.getLogger(ForexIndentService.class);

    @Autowired
    ForexIndentService forexIndentService;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private OpsBookingService opsBookingService;

    @PostMapping("/saveOrUpdate")
    public ForexIndent saveOrUpdate(@RequestBody ForexIndentResource resource) throws OperationException {
        return forexIndentService.saveOrUpdate(resource);
    }

 /*   @PostMapping("/submit")
    public ForexIndent submitForApproval(@RequestBody ForexIndentResource resource) throws OperationException {
        return forexIndentService.submit(resource);
    }*/

    @RequestMapping(value = "/{id}", method = PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateIndent(@PathVariable("id") String id,
                                               @RequestBody ForexBookingResource resource,
                                               @RequestParam(value = "workflow", required = false) String workflow) throws OperationException{
        if (id == null) {
            throw new OperationException(Constants.ID_NULL_EMPTY);
        }
        try {
            ForexBooking forexBooking = forexIndentService.updateIndent(id, resource, workflow);
            return new ResponseEntity<>(forexBooking, HttpStatus.OK);

        }  catch (Exception e) {
            logger.error("Exception occurred while updating record " +e);
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /*@PostMapping(value = "/{indentId}/approve")
    public ResponseEntity<ForexIndent> approveForexIndent(@PathVariable("indentId") String indentId, @RequestBody String remarks) throws OperationException {
        ForexIndent forexIndent = forexIndentService.approveForexIndent(indentId, remarks);
        return new ResponseEntity<>(forexIndent, HttpStatus.OK);
    }

    @PostMapping(value = "/{indentId}/reject")
    public ResponseEntity<ForexIndent> rejectForexIndent(@PathVariable("indentId") String indentId, @RequestBody String remarks) throws OperationException {
        ForexIndent forexIndent = forexIndentService.rejectForexIndent(indentId, remarks);
        return new ResponseEntity<>(forexIndent, HttpStatus.OK);
    }*/

    @GetMapping(value = "/getById/{indentId}")
    public ResponseEntity<ForexIndent> getIndentById(@PathVariable("indentId") String id) throws OperationException {

        ForexIndent res = forexIndentService.getIndentById(id);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(value = "/sendToSupplier/{indentId}")
    public ResponseEntity<String> sendIndentToSupplier(@PathVariable("indentId") String id) throws OperationException {

        forexIndentService.sendIndentToSupplier(id);
        return new ResponseEntity<>("{\"status\": \"SUCCESS\"}", HttpStatus.OK);
    }

    @GetMapping(value = "/getByRequestId/{requestId}")
    public ResponseEntity<List<ForexIndent>> getIndentsByRequestId(@PathVariable("requestId") String id) throws OperationException {

        List<ForexIndent> res = forexIndentService.getIndentsByRequestId(id);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(value = "/getByForexId/{forexId}")
    public ResponseEntity<List<ForexIndent>> getIndentsByForexId(@PathVariable("forexId") String id) throws OperationException {

        List<ForexIndent> res = forexIndentService.getIndentsByForexId(id);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(value = "/getByRequestId/{requestId}/{type}")
    public ResponseEntity<ForexIndent> getIndentByType(@PathVariable("requestId") String id, @PathVariable("type") String type) throws OperationException {

        ForexIndent res = forexIndentService.getIndentByType(id, type);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
