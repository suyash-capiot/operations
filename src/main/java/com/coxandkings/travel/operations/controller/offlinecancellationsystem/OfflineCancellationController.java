package com.coxandkings.travel.operations.controller.offlinecancellationsystem;

import com.coxandkings.travel.operations.criteria.offlinecancellationsystem.OfflineCancellationCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.offlinecancellationsystem.OfflineCancellationRespon;
import com.coxandkings.travel.operations.service.offlinecancellationsystem.OfflineCancellationService;
import com.coxandkings.travel.operations.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/offlinecancellation")
@CrossOrigin(origins = "*")
public class OfflineCancellationController {
    @Autowired
    OfflineCancellationService offlineCancellationService;


    /**
     * get's supplier credentials list w.r.t supplierId and mearket
     *
     * @param offlineCancellationCriteria
     * @return
     * @throws OperationException
     */
    @PostMapping(path = "/v1/getsuppliercredentials")
    public ResponseEntity<OfflineCancellationRespon> sendInlineMessageAlert(@RequestBody OfflineCancellationCriteria offlineCancellationCriteria) throws OperationException {
        OfflineCancellationRespon offlineSupplierCredentials = null;
        try {
            // offlineSupplierCredentials = offlineCancellationService.doCancellation(offlineCancellationCriteria);
            offlineSupplierCredentials = offlineCancellationService.doAmendment(offlineCancellationCriteria);
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_51000);
        }
        return new ResponseEntity<OfflineCancellationRespon>(offlineSupplierCredentials, HttpStatus.OK);
    }

    /**
     * get's supplier market's list w.r.t supplier id
     * @param offlineCancellationCriteria
     * @throws OperationException
     */
   /* @PostMapping(path = "/v1/getsuppliermarkets")
    public ResponseEntity<OfflineCancellationSupplierMarkets> sendSupplierMarkets(@RequestBody OfflineCancellationCriteria offlineCancellationCriteria )throws OperationException {
        OfflineCancellationSupplierMarkets offlineCancellationSupplierMarkets = null;
        try {
            offlineCancellationSupplierMarkets = offlineCancellationService.getSupplierMarketsList(offlineCancellationCriteria);
        }catch(Exception e){
            throw new OperationException(Constants.OPS_ERR_51001);
        }
         return new ResponseEntity<OfflineCancellationSupplierMarkets>(offlineCancellationSupplierMarkets, HttpStatus.OK);
    }*/
}
