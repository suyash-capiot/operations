package com.coxandkings.travel.operations.controller.coreBE;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.booking.EntityLockResource;
import com.coxandkings.travel.operations.service.booking.EntityLockInfoService;
import com.coxandkings.travel.operations.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/entityLockService")
public class EntityLockController {

    @Autowired
    private EntityLockInfoService entityLockInfoService;

    @PostMapping(value = "/v1/acquireLock", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> acquireLock(@RequestBody EntityLockResource acquireLock) throws OperationException {
        try {
            String res = entityLockInfoService.acquireLock(acquireLock);
            return new ResponseEntity<String>(res, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_11231);
        }
    }

    @PostMapping(value = "/v1/releaseLock", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> releaseLock(@RequestBody EntityLockResource releaseLock) throws OperationException {
        try {
            String res = entityLockInfoService.releaseLock(releaseLock);
            return new ResponseEntity<String>(res, HttpStatus.OK);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_11232);
        }
    }
}
