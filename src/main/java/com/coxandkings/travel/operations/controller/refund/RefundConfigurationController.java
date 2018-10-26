package com.coxandkings.travel.operations.controller.refund;

import com.coxandkings.travel.operations.enums.refund.RefundTypes;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.refund.RefundConfiguration;
import com.coxandkings.travel.operations.resource.refund.RefundConfigurationResource;
import com.coxandkings.travel.operations.service.refund.RefundConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/refundConfiguration")
public class RefundConfigurationController {
    @Autowired
    private RefundConfigurationService refundConfigurationService;

    @PostMapping("/v1")
    @PutMapping("/v1")
    public RefundConfiguration saveRefundConfiguration(@RequestBody RefundConfiguration refundConfiguration) throws OperationException {
        return refundConfigurationService.saveAndUpdate(refundConfiguration);
    }

    @PostMapping("/v1/getRefundConfiguration")
    public RefundTypes getRefundConfiguration(@RequestBody RefundConfiguration refundConfiguration) {

        return refundConfigurationService.getRefundConfiguration(refundConfiguration);
    }
}
