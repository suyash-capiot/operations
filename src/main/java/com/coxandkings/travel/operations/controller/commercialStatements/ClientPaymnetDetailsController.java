package com.coxandkings.travel.operations.controller.commercialStatements;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.commercialstatements.ClientPaymentAdvice;
import com.coxandkings.travel.operations.model.commercialstatements.ClientPaymentDetail;
import com.coxandkings.travel.operations.service.commercialstatements.ClientPaymentDetailsService;
import com.coxandkings.travel.operations.utils.Constants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "/clientPaymentDetails")
@CrossOrigin(value = "*")
public class ClientPaymnetDetailsController {

    private static Logger logger = Logger.getLogger(ClientPaymnetDetailsController.class);

    @Autowired
    private ClientPaymentDetailsService clientPaymentDetailsService;

    @PostMapping(value = "/v1/create")
    public ResponseEntity<ClientPaymentDetail> savePaymentDetails(@RequestBody ClientPaymentDetail resource) throws OperationException, IOException {
        ClientPaymentDetail paymentDetails = clientPaymentDetailsService.createPaymentDetail(resource);
        return new ResponseEntity<>(paymentDetails, HttpStatus.CREATED);

    }

    @PostMapping(value = "/v1/update")
    public ResponseEntity<String> updatePaymentDetails(@RequestBody ClientPaymentDetail resource) throws OperationException, IOException {
//        String message = clientPaymentDetailsService.updatePaymentDetail(resource,id);
        String message = clientPaymentDetailsService.updatePaymentDetail(resource);

        return new ResponseEntity<>(message, HttpStatus.CREATED);

    }

    @GetMapping("/v1/paymentDetail/getById/{id}")
    public ClientPaymentDetail getPaymentDetailById(@PathVariable String id) throws OperationException {
        try {
            return clientPaymentDetailsService.getClientPaymentDetailById(id);
        } catch (OperationException e) {
            throw e;
        } catch (Exception e) {
            throw new OperationException(Constants.ER1030);
        }
    }

    @GetMapping(value = "/v1/getPaymentDetails/{paymentAdviceNumber}")
    public ResponseEntity<ClientPaymentDetail> getPaymentDetails(@PathVariable("paymentAdviceNumber") String paymentAdviceNumber) throws OperationException {
        try {
            ClientPaymentDetail paymentDetailsresource = clientPaymentDetailsService.getPaymentDetails(paymentAdviceNumber);
            return new ResponseEntity<>(paymentDetailsresource, HttpStatus.OK);
        }catch (OperationException e) {
            throw e;
        } catch (Exception e) {
            throw new OperationException(Constants.ER1030);
        }
    }


}
