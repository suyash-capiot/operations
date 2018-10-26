package com.coxandkings.travel.operations.controller.supplierbillpassing;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.supplierbillpassing.StopPaymentResource;
import com.coxandkings.travel.operations.service.supplierbillpassing.StopPaymentService;
import com.coxandkings.travel.operations.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/stopPayment")
public class StopPaymentController {

    @Autowired
    private StopPaymentService stopPaymentService;

    @PostMapping("/v1/save")
    public Map save(@RequestBody StopPaymentResource stopPaymentResource) throws OperationException, ParseException {
        try {
            return stopPaymentService.save(stopPaymentResource);
        } catch (OperationException e){
            throw e;
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30802);
        }
    }

    @PostMapping("/v1/release")
    public Map release(@RequestBody StopPaymentResource stopPaymentResource) throws OperationException, ParseException {
        try {
            return stopPaymentService.release(stopPaymentResource);
        } catch (OperationException e){
            throw e;
        } catch (Exception e) {
            throw new OperationException(Constants.OPS_ERR_30803);
        }
    }

}
